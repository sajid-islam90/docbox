package activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.elune.sajid.myapplication.R;
import com.loopj.android.http.RequestParams;

import org.json.simple.JSONValue;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import objects.media_obj;
import objects.personal_obj;
import utilityClasses.DatabaseHandler;
import utilityClasses.PhotoHelper;
import utilityClasses.uploadfile;
import utilityClasses.utility;

public class PGSubscriptionActivity extends AppCompatActivity {
    EditText idProof;
    ImageView idProofPhoto;
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int PICKFILE_RESULT_CODE = 2;
    String profilePicPath;
    String parent ="";
    RelativeLayout regnRelativeLayout;
    RelativeLayout laterRelativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Student Subscription");
        setContentView(R.layout.activity_pgsubscription);
        regnRelativeLayout = (RelativeLayout)findViewById(R.id.regnRelativeLayout);
        laterRelativeLayout  = (RelativeLayout)findViewById(R.id.laterRelativeLayout);
        idProofPhoto = (ImageView)findViewById(R.id.imageView12) ;
        idProof = (EditText)findViewById(R.id.idProofEditText);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(PGSubscriptionActivity.this);
        String collegeId = prefs.getString("collegeID","");
        if(collegeId.equals(""))
        {
            laterRelativeLayout.setVisibility(View.GONE);
            regnRelativeLayout.setVisibility(View.VISIBLE);
        }
        else
        {
            idProof.setText(collegeId);
            idProofPhoto.setImageBitmap(BitmapFactory.decodeFile(collegeId));
            regnRelativeLayout.setVisibility(View.GONE);
            laterRelativeLayout.setVisibility(View.VISIBLE);
        }
        TextView textView = (TextView)findViewById(R.id.enterAgainTextView);
        if (textView != null) {
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    laterRelativeLayout.setVisibility(View.GONE);
                    regnRelativeLayout.setVisibility(View.VISIBLE);
                }
            });
        }


        if (idProof != null) {
            idProof.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(PGSubscriptionActivity.this);
                    builder.setTitle("Add Photo!");
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            if (items[item].equals("Take Photo")) {

                                dispatchTakePictureIntent();

                            } else if (items[item].equals("Choose from Library")) {
                                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(galleryIntent, PICKFILE_RESULT_CODE);
                            } else if (items[item].equals("Cancel")) {
                                dialog.dismiss();
                            }
                        }
                    });
                    builder.show();
                }
            });
        }
        Button button = (Button)findViewById(R.id.submitButton);
        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(new File(idProof.getText().toString()).exists())
                    {

                        updateCollegeId();

                    }
                    else
                        Toast.makeText(PGSubscriptionActivity.this,"Cannot process request without photo id proof ",Toast.LENGTH_LONG).show();

                }
                }
            );
        }

    }

    private void updateCollegeId() {
        String s1 = "";
        final DatabaseHandler databaseHandler= new DatabaseHandler(PGSubscriptionActivity.this);
        personal_obj personalObj = databaseHandler.getPersonalInfo();
        ArrayList<String> hash = new ArrayList<>();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(PGSubscriptionActivity.this);
        String hashString = prefs.getString(PGSubscriptionActivity.this.getString(R.string.hash_code), "");
        hash.add(hashString);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("collegeID", idProof.getText().toString());
        editor.commit();

        ArrayList<String> data = new ArrayList<>();
        ArrayList<String> data1 = new ArrayList<>();
        ArrayList<ArrayList<String>> personalData = new ArrayList<>();
        data.add(String.valueOf(personalObj.get_customerId()));
        data.add(new File(idProof.getText().toString()).getName());
        data.add("0");// verification status 0 : not verified 1: verified

//        data1.add(String.valueOf(personalObj.get_customerId()));
//        data1.add(idProof.getText().toString());
//        data1.add("0");// verification status 0 : not verified 1: verified

        personalData.add(data);
       // personalData.add(data1);
        personalData.add(hash);
        StringWriter out = new StringWriter();
        try {
            JSONValue.writeJSONString(personalData, out);
            s1 = out.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        final RequestParams requestParams = new RequestParams();
        requestParams.put("verificationJSON", s1);


        final String  address =  getResources().getString(R.string.action_server_ip_address);
        utility.sync("http://" + address + "/savePGStudentSubscription.php", requestParams, PGSubscriptionActivity.this);
        ArrayList<media_obj> paths = new ArrayList<>();
        media_obj mediaObj = new media_obj();
        mediaObj.set_media_path(idProof.getText().toString());
        mediaObj.set_pid(0);
        paths.add(mediaObj);
        uploadfile.uploadImage(PGSubscriptionActivity.this, paths);
        Intent intent = new Intent(PGSubscriptionActivity.this, PGSubscriptionActivity.class);
        finish();
        startActivity(intent);

    }

    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(PGSubscriptionActivity.this,CameraDemoActivity.class);

        //media_obj mediaObj = new media_obj();

        final DatabaseHandler dbHandler = new DatabaseHandler(PGSubscriptionActivity.this);



        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(PGSubscriptionActivity.this.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = PhotoHelper.createImageFile(0);

            } catch (IOException ex) {
                // Error occurred while creating the File

            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra("filePath",photoFile.getPath());
                takePictureIntent.putExtra("parentActivity","pgSubscription");
                profilePicPath = photoFile.getPath();
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);


            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Calendar c = Calendar.getInstance();
        File newFile = null;
        final DatabaseHandler dbHandler = new DatabaseHandler(PGSubscriptionActivity.this);
        Resources resources = getResources();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        if (requestCode == REQUEST_TAKE_PHOTO ) {
            if(new File(profilePicPath).length()>0) {
                idProof.setText(profilePicPath);
                idProofPhoto.setImageBitmap(BitmapFactory.decodeFile(profilePicPath));

            }
        }
        if ( (requestCode == PICKFILE_RESULT_CODE )&&((data !=null)&&(data.getData()!=null))) {
            Uri uri = data.getData();
            File file = null;
            String file_name = "";
            String file_path = "";
            ; // "/mnt/sdcard/FileName.mp3"
            if (uri.getScheme().compareTo("content")==0) {
                Cursor cursor = PGSubscriptionActivity.this.getContentResolver().query(uri, null, null, null, null);
                if (cursor.moveToFirst()) {
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);//Instead of "MediaStore.Images.Media.DATA" can be used "_data"
                    Uri filePathUri = Uri.parse(cursor.getString(column_index));
                    file_name = filePathUri.getLastPathSegment();
                    file_path=filePathUri.getPath();
                }
            }
            try {
                file = new File(file_path);
                String a = file.getAbsolutePath();
                if ((file.getName().contains(".jpeg")) || (file.getName().contains(".jpg"))
                        || (file.getName().contains(".png"))) {
                    if(file.exists()) {
                        idProof.setText(file_path);
                        idProofPhoto.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
                    }

                } else {
                    // Toast.makeText(documents.this,"Please Select from .jpg,.png,.txt,.doc,.pdf files",Toast.LENGTH_SHORT);
                    android.support.v7.app.AlertDialog.Builder builder1 = new android.support.v7.app.AlertDialog.Builder(this);

                    builder1.setMessage("Please Select from .jpg,.png files")
                            .setCancelable(false)
                            .setTitle("ALERT")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    //((Activity) followUp.this).recreate();
                                    return;
                                    //do things
                                }
                            });
                    android.support.v7.app.AlertDialog alert1 = builder1.create();
                    alert1.show();

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_account_verification, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == android.R.id.home) {

            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

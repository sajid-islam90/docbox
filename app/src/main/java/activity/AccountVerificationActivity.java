package activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import objects.Patient;
import objects.media_obj;
import objects.personal_obj;
import utilityClasses.DatabaseHandler;
import utilityClasses.PhotoHelper;
import utilityClasses.uploadfile;
import utilityClasses.utility;

public class AccountVerificationActivity extends AppCompatActivity {
EditText idProof;
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int PICKFILE_RESULT_CODE = 2;
    String profilePicPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_verification);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        final RelativeLayout regnRelativeLayout = (RelativeLayout)findViewById(R.id.regnRelativeLayout);
        final RelativeLayout laterRelativeLayout = (RelativeLayout)findViewById(R.id.laterRelativeLayout);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(AccountVerificationActivity.this);
        idProof = (EditText) findViewById(R.id.idProofEditText);
        final EditText editText = (EditText)findViewById(R.id.graduationRegistrationNumberEditText);
        EditText editText1 = (EditText)findViewById(R.id.graduationRegistrationNumberEditText);
        EditText editText2 = (EditText)findViewById(R.id.grauationRegistrationYearEditText);
        EditText editText3 = (EditText)findViewById(R.id.grauationRegistrationYearEditText);
        idProof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AccountVerificationActivity.this);
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

        String graduationRegistrationNumber = prefs.getString("graduationRegistrationNumber", "");
        String postGraduationRegistrationNumber = prefs.getString("postGraduationRegistrationNumber", "");
        String graduationRegistrationYear = prefs.getString("graduationRegistrationYear", "");
        String postGraduationRegistrationYear = prefs.getString("postGraduationRegistrationYear", "");
        editText.setText(graduationRegistrationNumber);
        editText1.setText(postGraduationRegistrationNumber);
        editText2.setText(graduationRegistrationYear);
        editText3.setText(postGraduationRegistrationYear);
        if((graduationRegistrationNumber.length()>0)||(postGraduationRegistrationNumber.length()>0))
        {
            regnRelativeLayout.setVisibility(View.GONE);
            laterRelativeLayout.setVisibility(View.VISIBLE);
        }
        else {
            regnRelativeLayout.setVisibility(View.VISIBLE);
            laterRelativeLayout.setVisibility(View.GONE);

        }
        TextView textView = (TextView)findViewById(R.id.enterAgainTextView);
        TextView textView1 = (TextView)findViewById(R.id.stillVerifyingTextView);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regnRelativeLayout.setVisibility(View.VISIBLE);
                laterRelativeLayout.setVisibility(View.GONE);

            }
        });
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regnRelativeLayout.setVisibility(View.VISIBLE);
                laterRelativeLayout.setVisibility(View.GONE);

            }
        });

        Button button = (Button)findViewById(R.id.submitButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(new File(idProof.getText().toString()).exists())
                { if((!editText.getText().toString().equals(""))&&(!editText.getText().toString().equals("")))

                updateRegistrationNumber();
                else
                    Toast.makeText(AccountVerificationActivity.this,"Please provide a valid registration id issued by a Govt. Authority ",Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(AccountVerificationActivity.this,"Cannot process request without photo id proof ",Toast.LENGTH_LONG).show();

            }
        });
    }


    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(AccountVerificationActivity.this,CameraDemoActivity.class);

        //media_obj mediaObj = new media_obj();

        final DatabaseHandler dbHandler = new DatabaseHandler(AccountVerificationActivity.this);



        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(AccountVerificationActivity.this.getPackageManager()) != null) {
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
                profilePicPath = photoFile.getPath();
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);


            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Calendar c = Calendar.getInstance();
        File newFile = null;
        final DatabaseHandler dbHandler = new DatabaseHandler(AccountVerificationActivity.this);
        Resources resources = getResources();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        if (requestCode == REQUEST_TAKE_PHOTO ) {
            idProof.setText(profilePicPath);
        }
        if ( (requestCode == PICKFILE_RESULT_CODE )&&((data !=null)&&(data.getData()!=null))) {
            Uri uri = data.getData();
            File file = null;
            String file_name = "";
            String file_path = "";
            ; // "/mnt/sdcard/FileName.mp3"
            if (uri.getScheme().compareTo("content")==0) {
                Cursor cursor = AccountVerificationActivity.this.getContentResolver().query(uri, null, null, null, null);
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
                    idProof.setText(file_path);

                } else {
                    // Toast.makeText(documents.this,"Please Select from .jpg,.png,.txt,.doc,.pdf files",Toast.LENGTH_SHORT);
                    android.support.v7.app.AlertDialog.Builder builder1 = new android.support.v7.app.AlertDialog.Builder(this);

                    builder1.setMessage("Please Select from .jpg,.png,.txt,.doc,.pdf files")
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

    public void updateRegistrationNumber()
    {
        String s1 = "";
        final DatabaseHandler databaseHandler= new DatabaseHandler(AccountVerificationActivity.this);
        EditText editText = (EditText)findViewById(R.id.graduationRegistrationNumberEditText);
        EditText editText1 = (EditText)findViewById(R.id.graduationRegistrationNumberEditText);
        EditText editText2 = (EditText)findViewById(R.id.grauationRegistrationYearEditText);
        EditText editText3 = (EditText)findViewById(R.id.grauationRegistrationYearEditText);
        personal_obj personalObj = databaseHandler.getPersonalInfo();
        ArrayList<String> hash = new ArrayList<>();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(AccountVerificationActivity.this);
        String hashString = prefs.getString(AccountVerificationActivity.this.getString(R.string.hash_code), "");
        hash.add(hashString);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("graduationRegistrationNumber", editText.getText().toString());
        editor.commit();
        editor.putString("graduationRegistrationYear", editText2.getText().toString());
        editor.commit();
        editor.putString("postGraduationRegistrationNumber", editText1.getText().toString());
        editor.commit();
        editor.putString("postGraduationRegistrationYear", editText3.getText().toString());
        editor.commit();



        ArrayList<String> data = new ArrayList<>();
        ArrayList<String> data1 = new ArrayList<>();
        ArrayList<ArrayList<String>> personalData = new ArrayList<>();
        data.add(String.valueOf(personalObj.get_customerId()));
        data.add("1");//verification type 1: graduation 2: post graduation
        data.add(editText.getText().toString());
        data.add(editText2.getText().toString());
        data.add("0");// verification status 0 : not verified 1: verified
        data.add(idProof.getText().toString());

        data1.add(String.valueOf(personalObj.get_customerId()));
        data1.add("2");//verification type 1: graduation 2: post graduation
        data1.add(editText1.getText().toString());
        data1.add(editText3.getText().toString());
        data1.add("0");
        data1.add(idProof.getText().toString());// verification status 0 : not verified 1: verified

        personalData.add(data);
        personalData.add(data1);
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
        utility.sync("http://" + address + "/addVerificationDetails.php", requestParams, AccountVerificationActivity.this);
        ArrayList<media_obj> paths = new ArrayList<>();
        media_obj mediaObj = new media_obj();
        mediaObj.set_media_path(idProof.getText().toString());
        mediaObj.set_pid(0);
        paths.add(mediaObj);
        uploadfile.uploadImage(AccountVerificationActivity.this, paths);
        Intent intent = new Intent(AccountVerificationActivity.this, AccountVerificationActivity.class);
        finish();
        startActivity(intent);




    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_account_verification, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AccountVerificationActivity.this,Activity_main_2.class);
        intent.putExtra("fragmentNumber",2);
        startActivity(intent);
        finish();
        super.onBackPressed();
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
        if (id == R.id.home) {
            Intent intent = new Intent(AccountVerificationActivity.this,Activity_main_2.class);
            intent.putExtra("fragmentNumber",2);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

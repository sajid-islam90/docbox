package activity;
//ACTIVITY TO ENTER OTHER NOTES
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sajid.myapplication.DatabaseHandler;
import adapters.DocumentsAdapter;
import objects.Item;
import com.example.sajid.myapplication.PhotoHelper;
import com.example.sajid.myapplication.R;
import adapters.TwoTextFieldsAdapter;
import objects.media_obj;
import objects.other_obj;
import com.example.sajid.myapplication.utility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class Other_Notes_Activity extends ActionBarActivity {
    int pid ;
    ArrayList<Item> fields = new ArrayList<>();
    final Context context = this;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    private static final int REQUEST_TAKE_PHOTO = 100;
    media_obj mediaObj = new media_obj();

    public static ArrayList<other_obj> otherObj = new ArrayList<>();
    ArrayList<Item> media = new ArrayList<>();
    private Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_notes_);
        Intent intent = getIntent();
        pid = intent.getIntExtra("id",0);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//        otherObj = intent.getExtras().getParcelable("other_obj");
        if(otherObj!=null)
        {
            fields = displayOtherNotes();
            displayAddedField(fields);
        }
        else
        {
            otherObj = new ArrayList<>();
        }
        media = utility.getMediaList(pid, this, 4);

        this.displayAddedMedia(media);


    }

    public void addField(View view)
    {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.other_field_input_prompt, null);


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInputName = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInputFieldName);

        final EditText userInputValue = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInputFieldValue);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                                //searchPatient(userInput.getText().toString());
                                //fields.add(userInputName.getText().toString()) ;
                                String field_Name = userInputName.getText().toString();
                                String field_Value = userInputValue.getText().toString();
                                addOtherNote(field_Name,field_Value);
                                utility.recreateActivityCompat(Other_Notes_Activity.this);


                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();



    }

    public void displayAddedField(ArrayList<Item> fieldList)
    {
        ListView listView = (ListView)findViewById(R.id.listViewExam);

       /* MainActivityList adapter1 = new
                MainActivityList(MainActivity.this, patientList, imageId);*/

        //MyAdapter adapter = new MyAdapter(Exam_Activity.this,patientList);
        TwoTextFieldsAdapter twoTextFieldsAdapter = new TwoTextFieldsAdapter(this,getApplicationContext(),fieldList);
        //ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, fieldList.toArray()) ;
        listView.setAdapter(twoTextFieldsAdapter);
        //listView.setAdapter(twoTextFieldsAdapter);

    }


    public void addVideo(View view) throws IOException {this.dispatchTakeVideoIntent();}
    public void addPhoto(View view) throws IOException {this.dispatchTakePictureIntent();}

    private void dispatchTakeVideoIntent() throws IOException {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        //fileUri = PhotoHelper.createVideoFile(pid);
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File file = null;
            try {
                file = PhotoHelper.createVideoFile(pid,getApplicationContext());
                fileUri= Uri.fromFile(file);

            } catch (IOException ex) {
                // Error occurred while creating the File

            }
        }
        if (fileUri != null) {
            // create a file to save the video
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);  // set the image file name

            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0); // set the video image quality to high

            // start the Video Capture Intent
            startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri videoUri = data.getData();
            String path = videoUri.getPath();
            // media_obj mediaObj = new media_obj();
            DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
            mediaObj.set_media_path(path);
            mediaObj = PhotoHelper.addMissingBmp(mediaObj,CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
           /* Bitmap bmThumbnail;
            bmThumbnail = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.MICRO_KIND);
            bmThumbnail = PhotoHelper.getResizedBitmap(bmThumbnail, 150, 150);
            mediaObj.set_bmp(PhotoHelper.getBitmapAsByteArray(bmThumbnail));*/
            mediaObj.set_pid(pid);
            mediaObj.set_section(4);
            mediaObj.set_version(databaseHandler.getCurrentVersion(pid)+1);
            databaseHandler.addMedia(mediaObj);
            Toast.makeText(this, path, Toast.LENGTH_SHORT)
                    .show();
            // mVideoView.setVideoURI(videoUri);
            utility.recreateActivityCompat(Other_Notes_Activity.this);



        }

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {

            DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
            Bitmap bitmap;
            bitmap = BitmapFactory.decodeFile(mediaObj.get_media_path());
            ByteArrayOutputStream ostream = new ByteArrayOutputStream();

            // save image into gallery
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, ostream);

            try {
                FileOutputStream fout = new FileOutputStream(new File(mediaObj.get_media_path()));
                fout.write(ostream.toByteArray());
                fout.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            mediaObj = PhotoHelper.addMissingBmp(mediaObj,REQUEST_TAKE_PHOTO);
            mediaObj.set_pid(pid);
            mediaObj.set_section(4);
            mediaObj.set_version(databaseHandler.getCurrentVersion(pid)+1);
            databaseHandler.addMedia(mediaObj);

            utility.recreateActivityCompat(Other_Notes_Activity.this);



        }

    }


    public void displayAddedMedia(ArrayList<Item> fieldList)
    {
        GridView listView = (GridView)findViewById(R.id.Other_grid);
        DocumentsAdapter docAdapter = new DocumentsAdapter(this,Other_Notes_Activity.this,fieldList);
        listView.setAdapter(docAdapter);

    }



    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Intent intent = getIntent();
        //media_obj mediaObj = new media_obj();

        final DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());



        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = PhotoHelper.createImageFileForNotes(pid,getApplicationContext());

            } catch (IOException ex) {
                // Error occurred while creating the File

            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra("output",
                        Uri.fromFile(photoFile));
                mediaObj.set_media_name(photoFile.getPath());
                mediaObj.set_media_path(photoFile.getPath());




                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);



            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.history_activity_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.hist_action_add) {
            Toast.makeText(this, "Save selected", Toast.LENGTH_SHORT)
                    .show();
            Intent intent = new Intent();
            intent.putExtra("other_obj",  otherObj);
            intent.putExtra("activity","other");
            setResult(200, intent);
            finish();

            return true;



        }
        else if (id == R.id.hist_action_take_photo)
        {

            this.dispatchTakePictureIntent();

        }

        else if (id== R.id.hist_action_take_video)
        {
            try {
                this.dispatchTakeVideoIntent();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public ArrayList<Item> displayOtherNotes()
    {
        ArrayList<Item> field = new ArrayList<>();


        DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
        Item item;
        //other_obj otherObj[]=dbHandler.getLatestOtherNote(pid,dbHandler.getCurrentVersion(pid));
        if (otherObj != null)
        {
            for (int i = 0; i < otherObj.size(); i++) {
                item = new Item();
                item.setTitle(otherObj.get(i).get_field_name());
                item.setDiagnosis(otherObj.get(i).get_field_value());
                field.add(item);
                //field.add(otherObj[i].get_field_name());
            }
        }
        return  field;
    }


    public void addOtherNote(String fieldName, String fieldValue)
    {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());




        DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
        int version = dbHandler.getCurrentVersion(pid);
        other_obj temp = new other_obj();
        temp.set_version(version + 1);
        temp.set_field_name(fieldName);
        temp.set_field_value(fieldValue);


        temp.set_pid(pid);
        temp.set_date(formattedDate);

        otherObj.add(temp);
        //dbHandler.addOther(otherObj);
       // dbHandler.addTreatment(otherObj);
    }
}

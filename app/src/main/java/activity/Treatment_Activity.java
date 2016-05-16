package activity;
//ACTIVITY TO ENTER TREATMENT NOTES
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import utilityClasses.DatabaseHandler;
import adapters.DocumentsAdapter;
import objects.Item;
import utilityClasses.PhotoHelper;
import com.elune.sajid.myapplication.R;
import objects.media_obj;
import objects.treatment_obj;
import utilityClasses.utility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class Treatment_Activity extends ActionBarActivity {
    int pid ;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    private static final int REQUEST_TAKE_PHOTO = 100;
    private Uri fileUri;
    media_obj mediaObj = new media_obj();
    treatment_obj treatmentObj = new treatment_obj();
    ArrayList<Item> media = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatment_);
        Intent intent = getIntent();
        pid = intent.getIntExtra("id",0);
        treatmentObj = intent.getExtras().getParcelable("treat_obj");
        media = utility.getMediaList(pid, this, 3);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if(treatmentObj!= null)
        {
            displayAddedFields();
        }
        this.displayAddedMedia(media);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.history_activity_actions, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
// do something on back.
        addTreatmentNote();
        Intent intent = new Intent();
        intent.putExtra("treat_obj",  treatmentObj);
        intent.putExtra("activity","treatment");
        setResult(200, intent);
        finish();
        return;
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
            this.addTreatmentNote();
           /* Intent intent = new Intent(this, Other_Notes_Activity.class);
            intent.putExtra("id",pid);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();*/
            Intent intent = new Intent();
            intent.putExtra("treat_obj",  treatmentObj);
            intent.putExtra("activity","treatment");
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
    public void displayAddedFields()
    {
        EditText editText = (EditText)findViewById(R.id.Treat_diagnosis);
        EditText editText1 = (EditText)findViewById(R.id.Treat_treatment);
        EditText editText2 = (EditText)findViewById(R.id.Treat_procedure);
        EditText editText3 = (EditText)findViewById(R.id.Treat_implants);


        editText.setText(treatmentObj.get_diagnosis());
        editText1.setText(treatmentObj.get_treatment());
        editText2.setText(treatmentObj.get_procedure());
        editText3.setText(treatmentObj.get_implants());


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

            DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
            mediaObj.set_media_path(path);
            mediaObj = PhotoHelper.addMissingBmp(mediaObj,CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);

            mediaObj.set_pid(pid);
            mediaObj.set_section(3);
            mediaObj.set_version(databaseHandler.getCurrentVersion(pid)+1);
            databaseHandler.addMedia(mediaObj);
            Toast.makeText(this, path, Toast.LENGTH_SHORT)
                    .show();

            utility.recreateActivityCompat(Treatment_Activity.this);



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
            mediaObj.set_section(3);
            mediaObj.set_version(databaseHandler.getCurrentVersion(pid)+1);
            databaseHandler.addMedia(mediaObj);

            utility.recreateActivityCompat(Treatment_Activity.this);



        }

    }


    public void displayAddedMedia(ArrayList<Item> fieldList)
    {
        GridView listView = (GridView)findViewById(R.id.Treat_grid);
        DocumentsAdapter docAdapter = new DocumentsAdapter(this,Treatment_Activity.this,fieldList);
        listView.setAdapter(docAdapter);

    }



    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Intent intent = getIntent();


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


    public void addTreatmentNote()
    {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());

        EditText diagnosis  = (EditText)findViewById(R.id.Treat_diagnosis);
        EditText treatment = (EditText)findViewById(R.id.Treat_treatment);
        EditText procedure  = (EditText)findViewById(R.id.Treat_procedure);
        EditText implants = (EditText)findViewById(R.id.Treat_implants);


        DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
        int version = dbHandler.getCurrentVersion(pid);

        treatmentObj.set_version(version + 1);
        treatmentObj.set_diagnosis(diagnosis.getText().toString());
        treatmentObj.set_treatment(treatment.getText().toString());
        treatmentObj.set_procedure(procedure.getText().toString());
        treatmentObj.set_implants(implants.getText().toString());

        treatmentObj.set_pid(pid);
        treatmentObj.set_date(formattedDate);

       // dbHandler.addTreatment(treatmentObj);
    }
}

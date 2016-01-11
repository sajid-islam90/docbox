package activity;
//ACTIVITY TO ENTER HIST NOTES
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sajid.myapplication.DatabaseHandler;
import adapters.DocumentsAdapter;
import objects.Item;
import com.example.sajid.myapplication.PhotoHelper;
import com.example.sajid.myapplication.R;

import objects.Patient;
import objects.history_obj;
import objects.media_obj;
import com.example.sajid.myapplication.utility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class History_Activity extends ActionBarActivity {
    int pid ;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    private static final int REQUEST_TAKE_PHOTO = 100;
    private Uri fileUri;
    media_obj mediaObj = new media_obj();
    history_obj historyObj = new history_obj();
    ArrayList<Item> media = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_);
        Intent intent = getIntent();
         pid = intent.getIntExtra("id", 0);
        historyObj = intent.getExtras().getParcelable("history_obj");
        media = utility.getMediaList(pid, this, 1);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if(historyObj!= null)
        {
            this.displayAddedFields();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.hist_action_add) {
            Toast.makeText(this, "Save selected", Toast.LENGTH_SHORT)
                    .show();
            this.addHistoryNote();
          /*  Intent intent = new Intent(this, Exam_Activity.class);

            intent.putExtra("id",pid);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();*/
            Intent intent = new Intent();
            intent.putExtra("history_obj",  historyObj);
            intent.putExtra("activity","history");
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




    public void addHistoryNote()
    {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());

        EditText pastIllness = (EditText)findViewById(R.id.Hist_past);
        EditText presentIllness = (EditText)findViewById(R.id.Hist_presentIll);
        EditText personalHist = (EditText)findViewById(R.id.Hist_personal);
        EditText familyHist = (EditText)findViewById(R.id.Hist_family);

        DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
        int version = dbHandler.getCurrentVersion(pid);

        historyObj.set_version(version + 1);
        historyObj.set_past_illness(pastIllness.getText().toString());
        historyObj.set_present_illness(presentIllness.getText().toString());
        historyObj.set_personal_hist(personalHist.getText().toString());
        historyObj.set_family_hist(familyHist.getText().toString());
        historyObj.set_pid(pid);
        historyObj.set_date(formattedDate);
       // Patient patient = dbHandler.getPatient(pid);
       // patient.set_last_seen_date(formattedDate);
       // dbHandler.updatePatient(patient);







       // dbHandler.addHistory(historyObj);
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
                fileUri=Uri.fromFile(file);

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
            mediaObj.set_section(1);
            mediaObj.set_version(databaseHandler.getCurrentVersion(pid)+1);
            databaseHandler.addMedia(mediaObj);


            utility.recreateActivityCompat(History_Activity.this);
        


    }

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {

            DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());

            mediaObj = PhotoHelper.addMissingBmp(mediaObj,REQUEST_TAKE_PHOTO);
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
            mediaObj.set_pid(pid);
            mediaObj.set_section(1);
            mediaObj.set_version(databaseHandler.getCurrentVersion(pid) + 1);
            databaseHandler.addMedia(mediaObj);

            utility.recreateActivityCompat(History_Activity.this);



        }

    }

    public void displayAddedFields()
    {
        EditText editText = (EditText)findViewById(R.id.Hist_presentIll);
        EditText editText1 = (EditText)findViewById(R.id.Hist_past);
        EditText editText2 = (EditText)findViewById(R.id.Hist_personal);
        EditText editText3 = (EditText)findViewById(R.id.Hist_family);

        editText.setText(historyObj.get_present_illness());
        editText1.setText(historyObj.get_past_illness());
        editText2.setText(historyObj.get_personal_hist());
        editText3.setText(historyObj.get_family_hist());

    }

    @Override
    public void onBackPressed() {
// do something on back.
        addHistoryNote();
        Intent intent = new Intent();
        intent.putExtra("history_obj",  historyObj);
        intent.putExtra("activity","history");
        setResult(200, intent);
        finish();
        return;
    }


    public void displayAddedMedia(ArrayList<Item> fieldList)
    {
        GridView listView = (GridView)findViewById(R.id.Hist_grid);
        DocumentsAdapter docAdapter = new DocumentsAdapter(this,History_Activity.this,fieldList);
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


}

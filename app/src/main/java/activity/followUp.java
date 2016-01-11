package activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.example.sajid.myapplication.DatabaseHandler;
import com.example.sajid.myapplication.PhotoHelper;
import com.example.sajid.myapplication.R;
import com.example.sajid.myapplication.utility;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import adapters.InputAgainstAFieldAdapter;
import adapters.recyclerAdapter;
import objects.Item;
import objects.Patient;
import objects.media_obj;
import objects.other_obj;

public class followUp extends AppCompatActivity {
    static int pid;
    static  int version;
    static  String parent;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int FOLLOW_UP_SECTION_CONST = 10;
    private static final int TWO_TEXT_FIELDS = 1;
    private static final int ONE_PHOTO = 2;
    private static Uri fileUri;
    private static  ArrayList<other_obj> otherObj = new ArrayList<>();
    private static ArrayList<media_obj>mediaObj = new ArrayList<>();
    media_obj mediaObj1 = new media_obj();
    private static ArrayList<Item> media = new ArrayList<>();
    private static ArrayList<Item> FollowUpFields = new ArrayList<Item>();
    private static String formattedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_up);

        Intent intent = getIntent();
        media = new ArrayList<>();
        pid = intent.getIntExtra("id", 0);
        version = intent.getIntExtra("version", 1);
        parent  =intent.getStringExtra("parent");
        setTitle("Follow Up # "+version);
        FloatingActionButton floatingActionButton1 = (FloatingActionButton)findViewById(R.id.view2);
        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    addVideo(v);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        FloatingActionButton floatingActionButton2 = (FloatingActionButton)findViewById(R.id.view3);
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    addPhoto(v);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        FloatingActionButton floatingActionButton3 = (FloatingActionButton)findViewById(R.id.view4);
        floatingActionButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addHistField(v);

            }
        });
        doWork();

    }
    private void doWork() {

        Resources resource = getResources();
        DatabaseHandler databaseHandler = new DatabaseHandler(followUp.this);
        String[] fields = resource.getStringArray(R.array.follow_up);
        Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(c.getTime());
        if(FollowUpFields.size()!=fields.length){
        for(int i = 0;i<fields.length;i++) {
            Item item = new Item();
            item.setTitle(fields[i]);
            item.setDiagnosis("");
            item.setPatient_id(pid);
            item.setSection(version);//0--> pre op section
            item.setDate(formattedDate);
            FollowUpFields.add(item);
        }}
        ListView listView1 = (ListView)findViewById(R.id.filedsList);
        InputAgainstAFieldAdapter inputAgainstAFieldAdapter = new InputAgainstAFieldAdapter(this,FollowUpFields);
        listView1.setAdapter(inputAgainstAFieldAdapter);
        listView1.setItemsCanFocus(true);

        ArrayList<Item> field = getOtherNotesToDisplay();
        displayAddedOtherFields(field);


        getMediaListLocal();
       // media = utility.getMediaList(pid,followUp.this,FOLLOW_UP_SECTION_CONST,version);
        displayAddedMedia(media);




    }


    public void getMediaListLocal()
    {
        if(mediaObj.size()>media.size())
        for(int c = 0;c<mediaObj.size();c++)
        {
            Item item = new Item();
            item.setBmp(BitmapFactory.decodeByteArray(mediaObj.get(c).get_bmp(), 0, mediaObj.get(c).get_bmp().length));
            item.setDiagnosis(mediaObj.get(c).get_media_path());
            item.setPatient_id(mediaObj.get(c).get_pid());
            media.add(item);
        }
    }
    public void addVideo(View view) throws IOException {this.dispatchTakeVideoIntent();}
    public void addPhoto(View view) throws IOException {
        dispatchTakePictureIntent();

        // this.dispatchTakePictureIntent();
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Intent intent = getIntent();


        final DatabaseHandler dbHandler = new DatabaseHandler(followUp.this);



        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(followUp.this.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = PhotoHelper.createImageFileForNotes(pid,followUp.this);

            } catch (IOException ex) {
                // Error occurred while creating the File

            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra("output",
                        Uri.fromFile(photoFile));
                mediaObj1.set_media_name(photoFile.getPath());
                mediaObj1.set_media_path(photoFile.getPath());




                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);



            }
        }
    }
    private void dispatchTakeVideoIntent() throws IOException {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        //fileUri = PhotoHelper.createVideoFile(pid);
        if (intent.resolveActivity(followUp.this.getPackageManager()) != null) {
            // Create the File where the photo should go
            File file = null;
            try {
                file = PhotoHelper.createVideoFile(pid, followUp.this);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

       

        if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri videoUri = data.getData();
            String path = videoUri.getPath();

            DatabaseHandler databaseHandler = new DatabaseHandler(followUp.this);
            mediaObj1.set_media_path(path);
            mediaObj1 = PhotoHelper.addMissingBmp(mediaObj1,CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);

            mediaObj1.set_pid(pid);
            mediaObj1.set_section(FOLLOW_UP_SECTION_CONST);
            mediaObj1.set_version(version);
            mediaObj.add(mediaObj1);
            mediaObj1 = new media_obj();


            Intent intent = getIntent();

            finish();
            startActivity(intent);
        }

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {

            DatabaseHandler databaseHandler = new DatabaseHandler(followUp.this);

            mediaObj1 = PhotoHelper.addMissingBmp(mediaObj1,REQUEST_TAKE_PHOTO);
            Bitmap bitmap;
            bitmap = BitmapFactory.decodeFile(mediaObj1.get_media_path());
            ByteArrayOutputStream ostream = new ByteArrayOutputStream();

            // save image into gallery
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, ostream);

            try {
                FileOutputStream fout = new FileOutputStream(new File(mediaObj1.get_media_path()));
                fout.write(ostream.toByteArray());
                fout.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            mediaObj1.set_pid(pid);
            mediaObj1.set_section(FOLLOW_UP_SECTION_CONST);
            mediaObj1.set_version(version);
           mediaObj.add(mediaObj1);
            mediaObj1 = new media_obj();

            Intent intent = getIntent();
           
            finish();
            startActivity(intent);



        }

    }
    public  void addHistField(View promptsView)
    {
        LayoutInflater li = LayoutInflater.from(followUp.this);

       
        promptsView = li.inflate(R.layout.other_field_input_prompt, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                followUp.this);

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

                                String field_Name = userInputName.getText().toString();
                                String field_Value = userInputValue.getText().toString();
                                addOtherHistNote(field_Name, field_Value);
                                Intent intent = getIntent();
                                
                                finish();
                                startActivity(intent);
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
    public void addOtherHistNote(String fieldName, String fieldValue)
    {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());

       
        DatabaseHandler dbHandler = new DatabaseHandler(followUp.this);

        other_obj temp = new other_obj();
        temp.set_version(version);
        temp.set_field_name(fieldName);
        temp.set_field_value(fieldValue);


        temp.set_pid(pid);
        temp.set_date(formattedDate);

        otherObj.add(temp);

    }
    public ArrayList<Item> getOtherNotesToDisplay()
    {
        ArrayList<Item> field = new ArrayList<>();
        Item item;
        if (otherObj.size()>0)
        {
            for (int i = 0; i < otherObj.size(); i++) {
                item = new Item();
                item.setTitle(otherObj.get(i).get_field_name());
                item.setDiagnosis(otherObj.get(i).get_field_value());
                field.add(item);
            }
        }


        return  field;
    }
    public void displayAddedOtherFields(ArrayList<Item> fieldList)
    {

        RecyclerView listView = (RecyclerView)findViewById(R.id.listViewOtherHist);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerAdapter RecyclerAdapter = new recyclerAdapter(this,this,fieldList,TWO_TEXT_FIELDS,pid,version);
        listView.setLayoutManager(layoutManager);
        listView.setAdapter(RecyclerAdapter);
    }
    public void displayAddedMedia(ArrayList<Item> fieldList)
    {
        RecyclerView listView = (RecyclerView)findViewById(R.id.listViewMedia);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(followUp.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerAdapter RecyclerAdapter = new recyclerAdapter(this,followUp.this,fieldList,ONE_PHOTO,pid,version);
        listView.setLayoutManager(layoutManager);
        listView.setAdapter(RecyclerAdapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_follow_up, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            DatabaseHandler databaseHandler =new DatabaseHandler(followUp.this);
            databaseHandler.saveFollowUp(FollowUpFields);
            for(int i = 0;i<otherObj.size();i++)
            databaseHandler.addOtherFollowUp(otherObj.get(i));
            for(int i = 0;i<mediaObj.size();i++)
            {
                databaseHandler.addMediaFollowUp(mediaObj.get(i));
            }
            Patient patient = databaseHandler.getPatient(pid);
            databaseHandler.updatePatient(patient,0);
            finish();
            otherObj.clear();
            mediaObj.clear();
            FollowUpFields.clear();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

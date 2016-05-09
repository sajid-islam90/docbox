package activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sajid.myapplication.Activity_Video_Capture;
import com.example.sajid.myapplication.DatabaseHandler;
import com.example.sajid.myapplication.FileUtils;
import com.example.sajid.myapplication.PhotoHelper;
import com.example.sajid.myapplication.R;
import com.example.sajid.myapplication.utility;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

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
    static final int PICKFILE_RESULT_CODE = 2;
    private static Uri fileUri;
    private static String nextFollowUpDate ="";
    private static  ArrayList<other_obj> otherObj = new ArrayList<>();
    private static ArrayList<media_obj>mediaObj = new ArrayList<>();
    media_obj mediaObj1 = new media_obj();
    private static ArrayList<Item> media = new ArrayList<>();
    private static ArrayList<Item> FollowUpFields = new ArrayList<Item>();
    private static String formattedDate;
    recyclerAdapter RecyclerAdapter;
    recyclerAdapter RecyclerAdapterOtherNotes;
    ArrayList<Item> field;
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
        TextView textView10 =(TextView)findViewById(R.id.textViewFollowupNotes);
        TextView textView6 =(TextView)findViewById(R.id.textViewFollowupOther);
        TextView textView7 =(TextView)findViewById(R.id.textViewFollowupMedia);
        final ListView listView = (ListView)findViewById(R.id.filedsList);
        final RecyclerView listView2 = (RecyclerView)findViewById(R.id.listViewOtherHistView);
        final RecyclerView listView3 = (RecyclerView)findViewById(R.id.listViewMedia);
        final CardView cardView = (CardView)findViewById(R.id.view13);
        // listView2.setVisibility(View.GONE);
        //  listView3.setVisibility(View.GONE);
        textView10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listView.setVisibility(View.VISIBLE);
                listView2.setVisibility(View.GONE);
                listView3.setVisibility(View.GONE);
                // expandView(cardView);
            }
        });
        textView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listView2.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                listView3.setVisibility(View.GONE);
                //CollapseView(cardView);
            }
        });
        textView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listView3.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                listView2.setVisibility(View.GONE);
                //CollapseView(cardView);
            }
        });
        final FloatingActionsMenu floatingActionsMenu =(FloatingActionsMenu)findViewById(R.id.viewFabButton);
        FloatingActionButton floatingActionButton1 = (FloatingActionButton)findViewById(R.id.view2);
        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    addVideo(v);
                    floatingActionsMenu.toggle();

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
                    floatingActionsMenu.toggle();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        FloatingActionButton floatingActionButton3 = (FloatingActionButton)findViewById(R.id.view4);
        floatingActionButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addHistField(v);floatingActionsMenu.toggle();

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

         getOtherNotesToDisplay();
        displayAddedOtherFields(field);


       // getMediaListLocal();
       // media = utility.getMediaList(pid,followUp.this,FOLLOW_UP_SECTION_CONST,version);
        displayAddedMedia();




    }


    public ArrayList<Item> getMediaListLocal()
    {
        ArrayList<Item> media1 = new ArrayList<>();
        if(mediaObj.size()>media.size())
        for(int c = 0;c<mediaObj.size();c++)
        {
            Item item = new Item();
            item.setBmp(BitmapFactory.decodeByteArray(mediaObj.get(c).get_bmp(), 0, mediaObj.get(c).get_bmp().length));
            item.setDiagnosis(mediaObj.get(c).get_media_path());
            item.setPatient_id(mediaObj.get(c).get_pid());
            media.add(item);
            media1.add(item);
        }
        return media1;
    }
    public void addVideo(View view) throws IOException {  final CharSequence[] items = { "Take Video", "Choose from Library", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(followUp.this);
        builder.setTitle("Add Video!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Video")) {
                    try {
                        dispatchTakeVideoIntent();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (items[item].equals("Choose from Library")) {
                    uploadNotesFromSDCardVideo();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }
    public void addPhoto(View view) throws IOException {


        final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(followUp.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {

                    dispatchTakePictureIntent();

                } else if (items[item].equals("Choose from Library")) {
                    uploadNotesFromSDCard();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();




        // this.dispatchTakePictureIntent();
    }
    private void dispatchTakePictureIntent() {
        //Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Intent takePictureIntent = new Intent(followUp.this,CameraDemoActivity.class);
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
//                takePictureIntent.putExtra("output",
//                        Uri.fromFile(photoFile));
                takePictureIntent.putExtra("pid",pid);
                takePictureIntent.putExtra("filePath",photoFile.getPath());
                mediaObj1.set_media_name(photoFile.getPath());
                mediaObj1.set_media_path(photoFile.getPath());




                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);



            }
        }
    }
    private void dispatchTakeVideoIntent() throws IOException {
        //Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        Intent intent = new Intent(followUp.this,Activity_Video_Capture.class);


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

            intent.putExtra("videoPath", fileUri.getPath()) ;
            mediaObj1.set_media_path(fileUri.getPath());
            //startActivity(intent);
            // create a file to save the video
//             intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
//            intent.putExtra("android.intent.extra.durationLimit", 30);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);  // set the image file name

           // set the video image quality to high
           // if (intent.resolveActivity(getPackageManager()) != null) {
            // start the Video Capture Intent
            startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
        }//}
    }

    public void uploadNotesFromSDCardVideo()
    {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICKFILE_RESULT_CODE);
    }
    public void uploadNotesFromSDCard()
    {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICKFILE_RESULT_CODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        DatabaseHandler databaseHandler = new DatabaseHandler(followUp.this);
        Patient patient = databaseHandler.getPatient(pid);
        Item item = new Item();

        if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE && resultCode == RESULT_CANCELED) {
//            Uri videoUri = data.getData();
//            String path = videoUri.getPath();
            File photoFile = new File(mediaObj1.get_media_path());

            if(photoFile.length()<= 0)
            {
                photoFile.delete();
                return;
                // utility.recreateActivityCompat(followUp.this);
            }


           // mediaObj1.set_media_path(path);
            mediaObj1 = PhotoHelper.addMissingBmp(mediaObj1,CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);

            mediaObj1.set_pid(pid);
            mediaObj1.set_section(FOLLOW_UP_SECTION_CONST);
            mediaObj1.set_version(version);
            mediaObj.add(mediaObj1);
           // mediaObj1 = new media_obj();
             item = new Item();
            item.setBmp(BitmapFactory.decodeByteArray(mediaObj1.get_bmp(), 0,mediaObj1.get_bmp().length));
            item.setDiagnosis(mediaObj1.get_media_path());
            item.setPatient_id(mediaObj1.get_pid());
            media.add(item);
            mediaObj1 = new media_obj();
//            Intent intent = getIntent();
//
//            finish();
//            startActivity(intent);
        }

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_CANCELED) {

            if (new File(mediaObj1.get_media_path()).exists())
            {
                File photoFile = new File(mediaObj1.get_media_path());

                if(photoFile.length()<= 0)
                {
                    photoFile.delete();
                    return;
                    // utility.recreateActivityCompat(followUp.this);
                }
                mediaObj1 = PhotoHelper.addMissingBmp(mediaObj1, REQUEST_TAKE_PHOTO);
            Bitmap bitmap;
            bitmap = BitmapFactory.decodeFile(mediaObj1.get_media_path());
            ByteArrayOutputStream ostream = new ByteArrayOutputStream();

            // save image into gallery
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);

            try {
                FileOutputStream fout = new FileOutputStream(new File(mediaObj1.get_media_path()));
                fout.write(ostream.toByteArray());
                fout.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mediaObj1.set_pid(pid);
            mediaObj1.set_section(FOLLOW_UP_SECTION_CONST);
            mediaObj1.set_version(version);
            mediaObj.add(mediaObj1);
            //
            item = new Item();
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            item.setBmp(BitmapFactory.decodeByteArray(mediaObj1.get_bmp(), 0, mediaObj1.get_bmp().length));
            item.setDiagnosis(mediaObj1.get_media_path());
            item.setPatient_id(mediaObj1.get_pid());

            media.add(item);

            mediaObj1 = new media_obj();
//            Intent intent = getIntent();
//
//            finish();
//            startActivity(intent);

        }

        }
        if ( (requestCode == PICKFILE_RESULT_CODE )&&((data !=null)&&(data.getData()!=null)))
        {Uri uri = data.getData();
            File file = null;
            String file_name = "";
            String file_path = "";


            if (uri.getScheme().compareTo("content")==0) {
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                if (cursor.moveToFirst()) {
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);//Instead of "MediaStore.Images.Media.DATA" can be used "_data"
                    Uri filePathUri = Uri.parse(cursor.getString(column_index));
                    file_name = filePathUri.getLastPathSegment();
                    file_path=filePathUri.getPath();
                }
            }


            try
            {
                File newFile = null;

                File storageDir =
                        new File(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES), "Patient Manager/"+patient.get_id()+"/Notes");
                if(!storageDir.exists())
                    storageDir.mkdir();
                newFile = new File(storageDir.getPath()+"/"+new File(file_path).getName());
                long size = newFile.length();


                if((newFile.getName().contains(".jpeg"))||(newFile.getName().contains(".png"))
                        ||(newFile.getName().contains(".mp4")) ||(newFile.getName().contains(".jpg"))) {

                    FileUtils.copyFile(new File(file_path), newFile);
                    int file_size = Integer.parseInt(String.valueOf(newFile.length()/1024));
                    if(file_size > 20480 )
                    {
                        Toast.makeText(followUp.this, "File Too Large", Toast.LENGTH_SHORT).show();
                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                        final File finalNewFile = newFile;
                        builder.setMessage("File too large \nPlease choose a file less than 20 Mb")
                                .setCancelable(false)
                                .setTitle("ALERT")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        finalNewFile.delete();
                                        //((Activity) followUp.this).recreate();
                                        return;
                                        //do things
                                    }
                                });
                        android.support.v7.app.AlertDialog alert = builder.create();
                        alert.show();
                    return;

                    }
                    mediaObj1.set_pid(pid);
                    mediaObj1.set_section(FOLLOW_UP_SECTION_CONST);
                    mediaObj1.set_version(version);


                    mediaObj1.set_media_path(newFile.getPath());


                    if(newFile.getName().contains(".mp4"))
                    {
                        mediaObj1 = PhotoHelper.addMissingBmp(mediaObj1,CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
                    }
                    else
                    {
                        mediaObj1 = PhotoHelper.addMissingBmp(mediaObj1,REQUEST_TAKE_PHOTO);
                    }
//                        patient.set_last_seen_date(formattedDate);
//                        databaseHandler.updatePatient(patient);

                    mediaObj.add(mediaObj1);
                     item = new Item();
                    item.setBmp(BitmapFactory.decodeByteArray(mediaObj1.get_bmp(), 0, mediaObj1.get_bmp().length));
                    item.setDiagnosis(mediaObj1.get_media_path());
                    item.setPatient_id(mediaObj1.get_pid());
                    media.add(item);
                    mediaObj1 = new media_obj();
                   // mediaObj1 = new media_obj();


                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

      // Item item = new Item();
//        item.setBmp(BitmapFactory.decodeByteArray(mediaObj1.get_bmp(), 0, mediaObj1.get_bmp().length));
//        item.setDiagnosis(mediaObj1.get_media_path());
//        item.setPatient_id(mediaObj1.get_pid());
//        ArrayList<Item> media1 = new ArrayList<>();
//        media1= getMediaListLocal();
        RecyclerAdapter.updateReceiptsList(media);


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
                                 getOtherNotesToDisplay();
                               RecyclerAdapterOtherNotes.updateReceiptsListOtherNotes(field);
//                                Intent intent = getIntent();
//
//                                finish();
//                                startActivity(intent);
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
    public void getOtherNotesToDisplay()
    {
         field = new ArrayList<>();
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



    }
    public void displayAddedOtherFields(ArrayList<Item> fieldList)
    {

        RecyclerView listView = (RecyclerView)findViewById(R.id.listViewOtherHistView);
        TextView textView = (TextView)findViewById(R.id.textViewFollowupNumberOther);
        textView.setText(String.valueOf(fieldList.size()));
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
         RecyclerAdapterOtherNotes = new recyclerAdapter(this,this,fieldList,TWO_TEXT_FIELDS,pid,version);
        listView.setLayoutManager(layoutManager);
        listView.setAdapter(RecyclerAdapterOtherNotes);
    }
    public void displayAddedMedia()
    {
        RecyclerView listView = (RecyclerView)findViewById(R.id.listViewMedia);
        TextView textView = (TextView)findViewById(R.id.textViewFollowupNumberMedia);
        textView.setText(String.valueOf(media.size()));
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(followUp.this, LinearLayoutManager.HORIZONTAL, false);
         RecyclerAdapter = new recyclerAdapter(this,followUp.this,media,ONE_PHOTO,pid,version);
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
            Intent intent = new Intent(followUp.this,PatientProfileActivity.class);
            intent.putExtra("id",pid);
            intent.putExtra("tab",1);
            startActivity(intent);
            otherObj.clear();
            mediaObj.clear();
            FollowUpFields.clear();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void doMoreWork()
    {
        LayoutInflater li = LayoutInflater.from(followUp.this);
        final View promptsView = li.inflate(R.layout.date_picker, null);
        final DatabaseHandler databaseHandler = new DatabaseHandler(followUp.this);
        final Patient patient = databaseHandler.getPatient(pid);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                followUp.this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                                final DatePicker datePicker = (DatePicker) promptsView.findViewById(R.id.datePicker);

                                    int month = datePicker.getMonth()+1;
                                    int year = datePicker.getYear();
                                    int day = datePicker.getDayOfMonth();
                                    nextFollowUpDate = day + "/" + month + "/" + year;
                                    patient.set_next_follow_up_date(nextFollowUpDate);
                                    databaseHandler.updatePatient(patient,0);

                                    utility.recreateActivityCompat(followUp.this);


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
}

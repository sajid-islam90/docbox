package activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.elune.sajid.myapplication.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import adapters.InputAgainstAFieldAdapter;
import adapters.recyclerAdapter;
import objects.Item;
import objects.Patient;
import objects.media_obj;
import objects.other_obj;
import redundant.FileUtils;
import utilityClasses.DatabaseHandler;
import utilityClasses.PhotoHelper;
import utilityClasses.floatingactionbutton.FloatingActionButton;
import utilityClasses.floatingactionbutton.FloatingActionsMenu;
import utilityClasses.utility;

public class edit_followup extends AppCompatActivity {

    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int FOLLOW_UP_SECTION_CONST = 10;
    private static final int TWO_TEXT_FIELDS = 1;
    private static final int ONE_PHOTO = 2;
    static final int PICKFILE_RESULT_CODE = 2;
    int pid;
    int version;
    Menu menu;
    int number;
    ArrayList<Item> FollowUpFields;
    private static Uri fileUri;
    media_obj mediaObj1 = new media_obj();
    ListView listView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_followup);
        Intent intent = getIntent();
        pid = intent.getIntExtra("id",0);
        version = intent.getIntExtra("version",0);
        number = intent.getIntExtra("number",1);
        setTitle("Follow Up #"+number);
        TextView textView10 =(TextView)findViewById(R.id.textViewFollowUpNotesView);
        TextView textView6 =(TextView)findViewById(R.id.textViewFollowupOtherView);
        TextView textView7 =(TextView)findViewById(R.id.textViewFollowupMediaView);
        final ListView listView = (ListView)findViewById(R.id.fieldsListFollowUpView);
        final RecyclerView listView2 = (RecyclerView)findViewById(R.id.listViewOtherHistView);
        final RecyclerView listView3 = (RecyclerView)findViewById(R.id.listViewMediaView);
        //final CardView cardView = (CardView)findViewById(R.id.view13);
        // listView2.setVisibility(View.GONE);
        //  listView3.setVisibility(View.GONE);
        assert textView10 != null;
        textView10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                assert listView != null;
                listView.setVisibility(View.VISIBLE);
                assert listView2 != null;
                listView2.setVisibility(View.GONE);
                assert listView3 != null;
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
        if (floatingActionButton1 != null) {
            floatingActionButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        addVideo();
                        floatingActionsMenu.toggle();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
        FloatingActionButton floatingActionButton2 = (FloatingActionButton)findViewById(R.id.view3);
        if (floatingActionButton2 != null) {
            floatingActionButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        addPhoto();
                        floatingActionsMenu.toggle();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });
        }

        FloatingActionButton floatingActionButton3 = (FloatingActionButton)findViewById(R.id.view4);
        if (floatingActionButton3 != null) {
            floatingActionButton3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   addHistField(v);floatingActionsMenu.toggle();

                }
            });
        }
        doWork();
    }

    public  void addHistField(View promptsView)
    {
        LayoutInflater li = LayoutInflater.from(edit_followup.this);


        promptsView = li.inflate(R.layout.other_field_input_prompt, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                edit_followup.this);
final DatabaseHandler databaseHandler = new DatabaseHandler(edit_followup.this);
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
                                other_obj other_obj = new other_obj();
                                other_obj.set_field_name(field_Name);
                                other_obj.set_field_value(field_Value);
                                other_obj.set_pid(pid);
                                other_obj.set_version(version);
                                databaseHandler.addOtherFollowUp(other_obj);
                                utility.recreateActivityCompat(edit_followup.this);
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

    @Override
    public void onBackPressed() {


        DatabaseHandler databaseHandler = new DatabaseHandler(edit_followup.this);

       databaseHandler.updateFollowUp(FollowUpFields);
        Intent intent = new Intent(edit_followup.this,ViewFollowUp_Activity.class);
        intent.putExtra("version",version);
        intent.putExtra("number",number);
        intent.putExtra("parent",view_all_versions.class.toString());
        intent.putExtra("id", pid);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    public void addVideo() throws IOException {  final CharSequence[] items = { "Take Video", "Choose from Library", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(edit_followup.this);
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
    public void addPhoto() throws IOException {


        final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(edit_followup.this);
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
    private void dispatchTakePictureIntent() {
        //Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Intent takePictureIntent = new Intent(edit_followup.this,CameraDemoActivity.class);
        Intent intent = getIntent();


        final DatabaseHandler dbHandler = new DatabaseHandler(edit_followup.this);

//dbHandler.addMediaFollowUp();

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(edit_followup.this.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = PhotoHelper.createImageFileForNotes(pid,edit_followup.this);

            } catch (IOException ex) {
                // Error occurred while creating the File

            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
//                takePictureIntent.putExtra("output",
//                        Uri.fromFile(photoFile));
                takePictureIntent.putExtra("pid",pid);
                takePictureIntent.putExtra("filePath",photoFile.getPath());
                takePictureIntent.putExtra("parentActivity","followup");
                mediaObj1.set_media_name(photoFile.getName());
                mediaObj1.set_media_path(photoFile.getPath());




                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);



            }
        }
    }
    private void dispatchTakeVideoIntent() throws IOException {
        //Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        Intent intent = new Intent(edit_followup.this,Activity_Video_Capture.class);


        //fileUri = PhotoHelper.createVideoFile(pid);
        if (intent.resolveActivity(edit_followup.this.getPackageManager()) != null) {
            // Create the File where the photo should go
            File file = null;
            try {
                file = PhotoHelper.createVideoFile(pid, edit_followup.this);
                fileUri= Uri.fromFile(file);

            } catch (IOException ex) {
                // Error occurred while creating the File

            }
        }
        if (fileUri != null) {

            intent.putExtra("videoPath", fileUri.getPath()) ;
            mediaObj1.set_media_name(new File(fileUri.getPath()).getName());
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
    public void doWork()
    {
        DatabaseHandler databaseHandler = new DatabaseHandler(edit_followup.this);
        int customerId = databaseHandler.getCustomerId();
         FollowUpFields  = databaseHandler.getFollowUpUnixVersion(pid, version);
        listView1 = (ListView)findViewById(R.id.fieldsListFollowUpView);
        InputAgainstAFieldAdapter inputAgainstAFieldAdapter = new InputAgainstAFieldAdapter(this,FollowUpFields);
        if (listView1 != null) {
            listView1.setAdapter(inputAgainstAFieldAdapter);
        }
       // listView1.setItemsCanFocus(false);
        ArrayList<Item> field = new ArrayList<>();
        ArrayList<Item> media = new ArrayList<>();
        media_obj[] mediaObjs = databaseHandler.getMediaFollowUp(pid,version);
        ArrayList<other_obj> OtherObjs =   databaseHandler.getOtherFollowUp(pid, version);
        if (OtherObjs.size()>0)
        {

            Item item;
            for (int i = 0; i < OtherObjs.size(); i++) {
                item = new Item();
                item.setTitle(OtherObjs.get(i).get_field_name());
                item.setDiagnosis(OtherObjs.get(i).get_field_value());
                field.add(item);

                //field.add(otherObj[i].get_field_name());
            }

        }
        if (mediaObjs != null)
        { Item item;
            for (int i = 0; i < mediaObjs.length; i++) {
                item = new Item();
                // mediaObjs[i]=PhotoHelper.addMissingBmp(mediaObjs[i],CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);

                if(mediaObjs[i].get_bmp()==null)
                    if(mediaObjs[i].get_media_path().contains(".mp4"))
                    {
                        if(new File(mediaObjs[i].get_media_path()).exists())
                            mediaObjs[i] = PhotoHelper.addMissingBmp(mediaObjs[i], 200);
                        else
                        {
                            PhotoHelper.addMissingBmpFromFileName(mediaObjs[i], 200,customerId);
                        }


                    }
                    else
                    {
                        if(new File(mediaObjs[i].get_media_path()).exists())
                            mediaObjs[i] = PhotoHelper.addMissingBmp(mediaObjs[i], 300);
                        else
                        {
                            PhotoHelper.addMissingBmpFromFileName(mediaObjs[i], 300,customerId);
                        }
                    }

                item.setBmp(BitmapFactory.decodeByteArray(mediaObjs[i].get_bmp(), 0, mediaObjs[i].get_bmp().length));

                item.setDiagnosis(mediaObjs[i].get_media_path());
                item.setPatient_id(mediaObjs[i].get_pid());
                //item.setDiagnosis(mediaObjs[i].get_field_value());
                media.add(item);
                //field.add(otherObj[i].get_field_name());
            }
        }
        displayAddedField(field);
        displayAddedMedia(media);

    }


    public void displayAddedField(ArrayList<Item> fieldList)
    {
        TextView textView = (TextView)findViewById(R.id.ViewFollowupNumberOfOtherNotes);
        textView.setText(String.valueOf(fieldList.size()));
        RecyclerView listView = (RecyclerView)findViewById(R.id.listViewOtherHistView);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerAdapter RecyclerAdapter = new recyclerAdapter(this,edit_followup.this,fieldList,TWO_TEXT_FIELDS,pid,version);

        listView.setLayoutManager(layoutManager);
        listView.setAdapter(RecyclerAdapter);
    }
    public void displayAddedMedia(ArrayList<Item> fieldList)
    {
        TextView textView = (TextView)findViewById(R.id.ViewFollowupNumberOfMedia);
        textView.setText(String.valueOf(fieldList.size()));
        RecyclerView listView = (RecyclerView)findViewById(R.id.listViewMediaView);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(edit_followup.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerAdapter RecyclerAdapter = new recyclerAdapter(this,edit_followup.this,fieldList,ONE_PHOTO,pid,version);
        listView.setLayoutManager(layoutManager);
        listView.setAdapter(RecyclerAdapter);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_edit_followup, menu);
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

        return super.onOptionsItemSelected(item);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        DatabaseHandler databaseHandler = new DatabaseHandler(edit_followup.this);
        int customerId = databaseHandler.getCustomerId();
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
           databaseHandler.addMediaFollowUp(mediaObj1);
            android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(edit_followup.this);

            alert.setTitle("Alert!!");
            alert.setMessage("Do you want to add another Video note?");
            alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Toast.makeText(getActivity(), "photo again?", Toast.LENGTH_SHORT).show();
                    //utility .recreateActivityCompat(getActivity());
                    try {
                        addVideo();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();

                }
            });
            alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    utility.recreateActivityCompat(edit_followup.this);
                    dialog.dismiss();
                }
            });

            alert.show();
            mediaObj1 = new media_obj();

        }

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_CANCELED) {
            File photoFile = null;
            if (new File(mediaObj1.get_media_path()).exists())
            {
                photoFile = new File(mediaObj1.get_media_path());
            }
            else
            {

            }
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
            databaseHandler.addMediaFollowUp(mediaObj1);


            android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(edit_followup.this);

            alert.setTitle("Alert!!");
            alert.setMessage("Do you want to add another Photo note?");
            alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    try {
                        addPhoto();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();

                }
            });
            alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                   utility.recreateActivityCompat(edit_followup.this);
                    dialog.dismiss();
                }
            });

            alert.show();

            mediaObj1 = new media_obj();



        }
        if ( (requestCode == PICKFILE_RESULT_CODE )&&((data !=null)&&(data.getData()!=null)))
        {
            Uri uri = data.getData();
            String file_name = "";
            String file_path = "";


            if (uri.getScheme().compareTo("content")==0) {
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);//Instead of "MediaStore.Images.Media.DATA" can be used "_data"
                    Uri filePathUri = Uri.parse(cursor.getString(column_index));
                    file_name = filePathUri.getLastPathSegment();
                    file_path = filePathUri.getPath();
                }
                if (cursor != null) {
                    cursor.close();
                }
            }


            try
            {
                File newFile;

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
                        Toast.makeText(edit_followup.this, "File Too Large", Toast.LENGTH_SHORT).show();
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
                    mediaObj1.set_media_name(newFile.getName());

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

                   databaseHandler.addMediaFollowUp(mediaObj1);
                    databaseHandler.updatePatient(patient);
                    android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(edit_followup.this);

                    alert.setTitle("Alert!!");
                    alert.setMessage("Do you want to add another note?");
                    alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            try {
                                addPhoto();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            dialog.dismiss();

                        }
                    });
                    alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            utility.recreateActivityCompat(edit_followup.this);
                            dialog.dismiss();
                        }
                    });

                    alert.show();
                    mediaObj1 = new media_obj();



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


       // RecyclerAdapter.updateReceiptsList(media);


    }
}

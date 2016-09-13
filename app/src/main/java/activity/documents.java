package activity;
//DISPLAYS ALL THE DOCUMENTS OF A PARTICULAR PATIENT AND HAS BUTTONS FOR ADDING DOCUMENTS AND NOTES.
import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import utilityClasses.floatingactionbutton.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import utilityClasses.DatabaseHandler;
import adapters.DocumentsAdapter;
import objects.Item;
import objects.Patient;

import redundant.FileUtils;
import utilityClasses.PhotoHelper;
import com.elune.sajid.myapplication.R;
import objects.document_obj;
import utilityClasses.utility;
import com.loopj.android.http.RequestParams;


import org.json.simple.JSONValue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


public class documents extends ActionBarActivity {

    static final int REQUEST_TAKE_PHOTO = 1;
    static final int PICKFILE_RESULT_CODE = 2;
    static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    static document_obj doc_obj = new document_obj();
    static NotificationCompat.Builder mBuilder;
    static NotificationManager notifier;
    int pid ;
    String governingActivity;
    File photoFile;
    ArrayList<Item> nameWithImage;
    DocumentsAdapter docAdapter;
    final Context context = this;
    String accountType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents);
        Intent intent = getIntent();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        accountType = prefs.getString(context.getString(R.string.account_type), "");
         pid = intent.getIntExtra("id", 0);
        governingActivity = intent.getStringExtra("governingActivity");
        if(pid>0)
            getDocumentsList(pid);
        displayDocuments(nameWithImage);


        FloatingActionButton floatingActionButtonStartCam = (FloatingActionButton)findViewById(R.id.view2);
        floatingActionButtonStartCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDocCam();
            }
        });

    }

/*
    public void displayDocuments(int id)
    {
        document_obj doc_obj = new document_obj();
        Patient patient = new Patient();

        doc_obj = getDocumentsList(id);
        if(doc_obj !=null) {
            ImageView imageView = (ImageView) findViewById(R.id.docImageView);
            imageView.setImageBitmap(BitmapFactory.decodeFile(doc_obj.get_doc_path()));
            TextView textView = (TextView) findViewById(R.id.textView12);
            textView.setText(doc_obj.get_doc_name());
        }
        patient = getPatientDataEdit(id);
        setTitle(patient.get_name()+"'s documents");
        Toast.makeText(this, patient.get_name(), Toast.LENGTH_SHORT)
                .show();

    }
*/
    public void displayDocuments(ArrayList<Item> documentList)
    {
        ListView listView = (ListView)findViewById(R.id.listViewNews);
         docAdapter = new DocumentsAdapter(this,documents.this,documentList);
        listView.setAdapter(docAdapter);


    }


// gets patient name for title

    public Patient getPatientDataEdit(int id)
    {
        SQLiteDatabase myDataBase= openOrCreateDatabase("patientManager",MODE_PRIVATE,null);
        DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
       // dbHandler.onCreate(myDataBase);

        Patient patient = new Patient();
        patient = dbHandler.getPatientForProfile(id);
        return patient;
    }

    public void getDocumentsList(int id)
    {

        DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
        Resources resources = getResources();
         nameWithImage = new ArrayList<Item>();
        Item displayDocument = new Item();
        Patient patient = dbHandler.getPatient(id);
        setTitle(patient.get_name()+"'s Documents");
        Bitmap bmpImage = null;
        document_obj doc_obj = new document_obj();
        List<document_obj> documentList = dbHandler.getDocuments(id);
        for (int i = 0 ; i < documentList.size();i++)
        {

            if((documentList.get(i).get_doc_name()!=null)|| (documentList.get(i).get_doc_path()!=null))
            {
                displayDocument.setTitle(documentList.get(i).get_doc_name());
                displayDocument.setDiagnosis(documentList.get(i).get_doc_path());
                doc_obj = documentList.get(i);
                if((new File(documentList.get(i).get_doc_path()).exists()))
                {

                    if((documentList.get(i).get_doc_path().contains(".jpeg"))||(documentList.get(i).get_doc_path().contains(".jpg"))||
                        (documentList.get(i).get_doc_path().contains(".png"))||(documentList.get(i).get_doc_path().contains(".PNG")))
                    {   if ((Arrays.equals(documentList.get(i).get_bmp(), new byte[0]))||(documentList.get(i).get_bmp()==null)) {

                                 if(new File(documentList.get(i).get_doc_path()).exists())
                                    {   doc_obj = PhotoHelper.addMissingBmp(doc_obj);
                                       // dbHandler.updateDocument()
                                        Bitmap bmp = null;
                                        bmp = BitmapFactory.decodeFile(doc_obj.get_doc_path());
                                        if(bmp!=null)
                                            bmp = PhotoHelper.getResizedBitmap(bmp,150,150);
                                        documentList.get(i).set_bmp(PhotoHelper.getBitmapAsByteArray(bmp));

                                    }
                             }


                    }
                   else if(documentList.get(i).get_doc_path().contains(".doc"))
                    {
                        documentList.get(i).set_bmp(PhotoHelper.getBitmapAsByteArray(BitmapFactory.decodeResource(resources, R.drawable.ic_doc)));
                    }
                    else if (documentList.get(i).get_doc_path().contains(".pdf"))
                    {
                        documentList.get(i).set_bmp(PhotoHelper.getBitmapAsByteArray(BitmapFactory.decodeResource(resources, R.drawable.ic_pdf)));
                    }
                    else if (documentList.get(i).get_doc_path().contains(".txt"))
                    {
                        documentList.get(i).set_bmp(PhotoHelper.getBitmapAsByteArray(BitmapFactory.decodeResource(resources,R.drawable.ic_txt)));
                    }
                    else{}


                bmpImage = BitmapFactory.decodeByteArray(documentList.get(i).get_bmp(), 0, documentList.get(i).get_bmp().length);

                displayDocument.setBmp(bmpImage);
                displayDocument.setPatient_id(documentList.get(i).get_id());

                nameWithImage.add(displayDocument);
                displayDocument = new Item();
            }
                else
                {
                    File storageDir =
                            new File(Environment.getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_PICTURES), "Patient Manager/" + patient.get_id() + "/Documents");

                     if(documentList.get(i).get_doc_path().contains(".doc"))
                    {
                        documentList.get(i).set_bmp(PhotoHelper.getBitmapAsByteArray(BitmapFactory.decodeResource(resources, R.drawable.ic_doc)));
                        bmpImage = BitmapFactory.decodeResource(resources, R.drawable.ic_doc);
                    }
                    else if (documentList.get(i).get_doc_path().contains(".pdf"))
                    {
                        documentList.get(i).set_bmp(PhotoHelper.getBitmapAsByteArray(BitmapFactory.decodeResource(resources, R.drawable.ic_pdf)));
                        bmpImage = BitmapFactory.decodeResource(resources, R.drawable.ic_pdf);
                    }
                    else if (documentList.get(i).get_doc_path().contains(".txt"))
                    {
                        documentList.get(i).set_bmp(PhotoHelper.getBitmapAsByteArray(BitmapFactory.decodeResource(resources,R.drawable.ic_txt)));
                        bmpImage = BitmapFactory.decodeResource(resources, R.drawable.ic_txt);
                    }
                    else{
                         bmpImage = BitmapFactory.decodeFile(storageDir.getPath()+"/"+doc_obj.get_doc_name());

                         if(bmpImage!=null)
                         bmpImage = PhotoHelper.getResizedBitmap(bmpImage,150,150);

                     }




                    displayDocument.setBmp(bmpImage);
                    displayDocument.setPatient_id(documentList.get(i).get_id());

                    nameWithImage.add(displayDocument);
                    displayDocument = new Item();
                }


            }
        }
        //displayDocuments(nameWithImage);

    }
    public void startAddNotes(View view)
    {
        Intent curIntent = getIntent();
        Intent intent =  new Intent(this, AddClinicalNotesActivity.class);
        int pid = curIntent.getIntExtra("id",0);
        intent.putExtra("parent",PatientProfileActivity.class.toString());
        intent.putExtra("id", pid);
        startActivity(intent);
    }


    public void startDocCam()
    {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {


                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);



                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.

        }
        else
        {
            final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(documents.this);
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


            //dispatchTakePictureIntent();
            //newCam();
        }





    }
    public void uploadNotesFromSDCard()
    {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICKFILE_RESULT_CODE);
    }
    private void newCam()
    {
        Intent intent = new Intent(this,CameraDemoActivity.class);
        intent.putExtra("pid",pid);
        startActivity(intent);
    }



    private void dispatchTakePictureIntent() {
        //Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Intent takePictureIntent = new Intent(documents.this,CameraDemoActivity.class);
        Intent intent = getIntent();

        final DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
        int id = intent.getIntExtra("id", 0);


        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            //File photoFile = null;
            try {
                photoFile = PhotoHelper.createImageFileForDocument(id,getApplicationContext());

            } catch (IOException ex) {
                // Error occurred wh7ile creating the File

            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
//                takePictureIntent.putExtra("output",
//                        Uri.fromFile(photoFile));
                takePictureIntent.putExtra("pid",pid);
                takePictureIntent.putExtra("filePath",photoFile.getPath());
                takePictureIntent.putExtra("parentActivity","documents");
                //final document_obj doc_obj = new document_obj();
                doc_obj.set_doc_name(photoFile.getPath());
                doc_obj.set_doc_path(photoFile.getPath());
               // bitmap =BitmapFactory.decodeFile(photoFile.getPath());
                //doc_obj.set_bmp(PhotoHelper.getBitmapAsByteArray(bitmap));
                doc_obj.set_id(id);

                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);



            }
        }
    }

    @Override
    protected void onResume()
    {
//        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(documents.this);
//
//        alert.setTitle("Alert!!");
//        alert.setMessage("Do you want to add another Report?");
//        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(documents.this, "photo again?", Toast.LENGTH_SHORT).show();
//
//                dialog.dismiss();
//
//            }
//        });
//        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//                dialog.dismiss();
//            }
//        });
//
//        alert.show();

        super.onResume();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Calendar c = Calendar.getInstance();
        File newFile = null;
        final DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
        Resources resources = getResources();
        Patient patient = dbHandler.getPatient(pid);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_CANCELED) {

//            Bitmap bitmap;
//           // File file1 = new File(photoFile)
//            bitmap =BitmapFactory.decodeFile(photoFile.getPath());
//            ByteArrayOutputStream ostream = new ByteArrayOutputStream();
//
//            // save image into gallery
//            if(bitmap!=null)
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, ostream);
//            if(photoFile.length()<= 0)
//            {
//                photoFile.delete();
//                utility.recreateActivityCompat(documents.this);
//                return;
//            }

//            try {
//                FileOutputStream fout = new FileOutputStream(photoFile);
//                fout.write(ostream.toByteArray());
//                fout.close();
//
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//            }


            //doc_obj.set_bmp(PhotoHelper.getBitmapAsByteArray(Bitmap.createScaledBitmap(bitmap,120,120,false)));

            //doc_obj = PhotoHelper.addMissingBmp(doc_obj);
           // if(doc_obj.get_bmp()!=null) {
                //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

//                String imageFileName = "IMG_" + timeStamp  ;
//                File file = new File(doc_obj.get_doc_path());
//                doc_obj.set_doc_name(photoFile.getName());
//                doc_obj.set_date(formattedDate);

                patient.set_last_seen_date(formattedDate);
                dbHandler.updatePatient(patient,0);
               // dbHandler.addDocument(doc_obj);

          //  }
            getDocumentsList(pid);
            docAdapter.updateReceiptsList(nameWithImage);




            //utility.recreateActivityCompat(documents.this);


        //utility.recreateActivityCompat(this);



        }
        else if ( (requestCode == PICKFILE_RESULT_CODE )&&((data !=null)&&(data.getData()!=null))) {
            Uri uri = data.getData();
            File file = null;
            String file_name = "";
            String file_path = "";
            ; // "/mnt/sdcard/FileName.mp3"
            try {
                 file = new File(uri.getPath());
                String a = file.getAbsolutePath();
                if (uri.getScheme().compareTo("content")==0) {
                    Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                    if (cursor.moveToFirst()) {
                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);//Instead of "MediaStore.Images.Media.DATA" can be used "_data"
                        Uri filePathUri = Uri.parse(cursor.getString(column_index));
                        file_name = filePathUri.getLastPathSegment();
                        file_path=filePathUri.getPath();
                    }
                }
                else{
                    Uri filePathUri = Uri.fromFile(file);
                    file_name = filePathUri.getLastPathSegment();
                    file_path=filePathUri.getPath();
                }

                if((file_path.contains(".txt"))||(file_path.contains(".doc"))||
                        (file_path.contains(".pdf"))||(file_path.contains(".jpeg"))||(file_path.contains(".jpg"))
                        ||(file_path.contains(".png"))||(file_path.contains(".PNG")))
                {
                    File storageDir =
                            new File(Environment.getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_PICTURES), "Patient Manager/"+patient.get_id()+"/Documents");
                    if(!storageDir.exists())
                        storageDir.mkdir();
                    newFile = new File(storageDir.getPath()+"/"+new File(file_path).getName());
                    int file_size = Integer.parseInt(String.valueOf(newFile.length()/1024));
                    FileUtils.copyFile(new File(file_path), newFile);
                    if(file_size > 2048 )
                    {
                        Toast.makeText(documents.this, "File Too Large", Toast.LENGTH_SHORT).show();
                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                        final File finalNewFile = newFile;
                        builder.setMessage("File too large \nPlease choose a file less than 2 Mb")
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


                    }
                    if(file_name.contains(".doc"))
                    {
                        doc_obj.set_bmp(PhotoHelper.getBitmapAsByteArray(BitmapFactory.decodeResource(resources,R.drawable.ic_doc)));
                    }
                    else if (file_name.contains(".pdf"))
                    {
                        doc_obj.set_bmp(PhotoHelper.getBitmapAsByteArray(BitmapFactory.decodeResource(resources,R.drawable.ic_pdf)));
                    }
                    else if (file_name.contains(".txt"))
                    {
                        doc_obj.set_bmp(PhotoHelper.getBitmapAsByteArray(BitmapFactory.decodeResource(resources,R.drawable.ic_txt)));
                    }
                    else if ((file_name.contains(".jpg"))||(file_name.contains(".png")))
                    {
                       // doc_obj.set_bmp(PhotoHelper.getBitmapAsByteArray(BitmapFactory.decodeFile(newFile.getPath())));
                        doc_obj.set_bmp(new byte[0]);
                    }


                    if(accountType.equals(context.getString(R.string.account_type_helper))) {
                        int docPid = dbHandler.checkDoctorHelperPatientMapping(patient.get_id());
                        File storageDir1 =
                                new File(Environment.getExternalStoragePublicDirectory(
                                        Environment.DIRECTORY_PICTURES), "Patient Manager/"+docPid+"/Documents");

                        String path = storageDir1.getPath()+"/"+newFile.getName();

                        //  path.replaceAll("/" + String.valueOf(id) + "/", "/" + docPid + "/");
                        dbHandler.mapDoctorHelperDocuments(path,newFile.getPath());
                    }


                    doc_obj.set_doc_name(file_name);
                    doc_obj.set_doc_path(newFile.getPath());
                    doc_obj.set_id(pid);
                    doc_obj.set_date(formattedDate);
                    dbHandler.updatePatient(patient,0);
                    dbHandler.addDocument(doc_obj);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(documents.this);

                            alert.setTitle("Alert!!");
                            alert.setMessage("Do you want to add another Report?");
                            alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(documents.this, "photo again?", Toast.LENGTH_SHORT).show();
                                    //utility .recreateActivityCompat(getActivity());
                                    try {
                                        startDocCam();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    dialog.dismiss();

                                }
                            });
                            alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    utility.recreateActivityCompat(documents.this);
                                    dialog.dismiss();
                                }
                            });

                            alert.show();

                        }
                    });

                    Toast.makeText(this,"File Name & PATH are:"+file_name+"\n"+file_path, Toast.LENGTH_LONG).show();
                }
                else
                {
                   // Toast.makeText(documents.this,"Please Select from .jpg,.png,.txt,.doc,.pdf files",Toast.LENGTH_SHORT);
                    android.support.v7.app.AlertDialog.Builder builder1 = new android.support.v7.app.AlertDialog.Builder(this);

                    builder1.setMessage("Please Select from .jpg,.png files")
                            .setCancelable(false)
                            .setTitle("ALERT")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    utility.recreateActivityCompat(documents.this);
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



           // }
        }


        utility.recreateActivityCompat(documents.this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_documents, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent intent1 = getIntent();
                if(governingActivity!=null)
                {
                    if(governingActivity.equals("patients_today"))
                    {
                        Intent intent = new Intent(documents.this,data_sync_activity.class);
                        startActivity(intent);
                        finish();
                    }
                }
                String parent = intent1.getStringExtra("governingActivity");




                //startActivity(intent);
                finish();
                return true;

            case R.id.action_info:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("This page can be used to store prescriptions,reports etc. These reports will be shared with your patients")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            case R.id.action_ott:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                final DatabaseHandler databaseHandler = new DatabaseHandler(documents.this);
                final String ott = utility.generateOTT();
                if((!databaseHandler.getPatient(pid).get_contact_number().equals(""))&&(databaseHandler.getPatient(pid).get_contact_number()!=null))
                { builder1.setMessage(" Is the One Time Token for sharing documents with "+databaseHandler.getPatient(pid).get_name())
                        .setTitle("'"+ott+"'")
                        .setCancelable(false)
                        .setPositiveButton("Share Token with Patient ", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                String address = getResources().getString(R.string.action_server_ip_address);
                                ArrayList<String> data = new ArrayList<String>();
                                data.add(ott);
                                data.add(databaseHandler.getPersonalInfo().get_name());
                                data.add(databaseHandler.getPatient(pid).get_contact_number());
                                data.add(String.valueOf(databaseHandler.getCustomerId()));
                                data.add(String.valueOf(databaseHandler.getPatient(pid).get_first_aid_id()));
                                String s1 = null;

                                StringWriter out = new StringWriter();
                                try {
                                    JSONValue.writeJSONString(data, out);
                                    s1 = out.toString();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                RequestParams params = new RequestParams();
                                params.put("data", s1);
                                utility.sync("http://" + address + "/OTTFirstAid.php", params, documents.this);
                                Toast.makeText(documents.this, "Passcode Shared", Toast.LENGTH_SHORT);
                                //do things
                            }
                        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alert1 = builder1.create();
                alert1.show();}
                else
                {
                    builder1.setMessage("No phone number found for  "+databaseHandler.getPatient(pid).get_name()+
                    "\n Please add a phone number use this")
                            .setTitle("Missing Phone Number")
                            .setIcon(R.drawable.ic_action_phone1)
                            .setCancelable(false)
                            .setPositiveButton("DISMISS", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {


                                    //do things
                                }
                            });
                    AlertDialog alert1 = builder1.create();
                    alert1.show();
                }
                return true;

//
//            case R.id.action_add_doc:
//                this.dispatchTakePictureIntent();
//
//                return true;
        }
        return super.onOptionsItemSelected(item);

        //noinspection SimplifiableIfStatement



    }

    public void uploadDoc(View view)
    {
        this.uploadDocument();
    }

    public void uploadDocument()
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");
        startActivityForResult(intent,PICKFILE_RESULT_CODE);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    this.dispatchTakePictureIntent();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    public class RestoreWebData extends AsyncTask<Void, Void, Boolean> {




        @Override
        protected void onPreExecute() {
            mBuilder =
                    new NotificationCompat.Builder(documents.this)
                            .setSmallIcon(android.R.drawable.stat_sys_download)
                            .setContentTitle("DocBox")


                            .setContentText("Patient data is being downloaded from cloud");

            notifier.notify(1, mBuilder.build());
            super.onPreExecute();

        }


        @Override
        protected Boolean doInBackground(Void... params) {
            DatabaseHandler databaseHandler = new DatabaseHandler(documents.this);

            List<document_obj> listDocument = databaseHandler.getAllDocumentsForDownload(5);
            List<document_obj> listMediaFollowUp = databaseHandler.getAllMediaFollowUpForSyncStatus(5);
            List<document_obj> listMedia = databaseHandler.getAllMediaForSyncsStatus(5);
            listMedia.addAll(listDocument);
            listMedia.addAll(listMediaFollowUp);
//            for (int i = 0; i < listDocument.size(); i++) {
//                downloadFile(listDocument.get(i).get_id(), listDocument.get(i).get_doc_path());
//            }
            for (int i = 0; i < listMedia.size(); i++) {
                File storageDir =
                        new File(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES), "Patient Manager/"+listMedia.get(i).get_id()+"/Notes");
                if(!storageDir.exists())
                    storageDir.mkdir();
                try {
                    if(i%40 == 0)
                        Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(new File(listMedia.get(i).get_doc_path()).exists())
                    downloadFile(listMedia.get(i).get_id(), listMedia.get(i).get_doc_path());
                else
                    downloadFile(listMedia.get(i).get_id(),storageDir.getPath()+"/"+listMedia.get(i).get_doc_name());
            }

            return null;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
//            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Activity_main_2.this);
//            SharedPreferences.Editor editor = prefs.edit();
            mBuilder =
                    new NotificationCompat.Builder(documents.this)
                            .setSmallIcon(R.drawable.icon_notification)
                            .setContentTitle("DocBox")

                            .setContentText(" Patients' data saved to Phone");

            notifier.notify(1, mBuilder.build());
//            editor.putBoolean("restore", false);
//            editor.commit();
//            pdia.dismiss();

        }

        //i: patient id
        //s: file path
        public void downloadFile(int i, String s) {
            int totalSize = 0;
            int downloadedSize = 0;
            DatabaseHandler databaseHandler = new DatabaseHandler(documents.this);
            int CustomerId = databaseHandler.getPersonalInfo().get_customerId();
            File file1 = new File(s);
            File file2 = file1.getParentFile();
            s= file2.getPath();
            String dwnload_file_path = "http://docbox.co.in/sajid/" + CustomerId + "/" + String.valueOf(i) + "/" + file1.getName();
            try {
                URL url = new URL(dwnload_file_path);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(true);

                //connect
                urlConnection.connect();

                //set the path where we want to save the file
                File SDCardRoot = Environment.getExternalStorageDirectory();
                //create a new file, to save the downloaded file
                File file = new File(file2, file1.getName());

                FileOutputStream fileOutput = new FileOutputStream(file);

                //Stream used for reading the data from the internet
                InputStream inputStream = urlConnection.getInputStream();

                //this is the total size of the file which we are downloading
                totalSize = urlConnection.getContentLength();


                //create a buffer...
                byte[] buffer = new byte[1024];
                int bufferLength = 0;

                while ((bufferLength = inputStream.read(buffer)) > 0) {
                    fileOutput.write(buffer, 0, bufferLength);
                    downloadedSize += bufferLength;
                    // update the progressbar //

                }
                //close the output stream when complete //
                fileOutput.close();


            } catch (final Exception e) {
                e.printStackTrace();
            }
        }


        public void doSomething() {

        }


    }


}

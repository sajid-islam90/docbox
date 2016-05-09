package activity;
//DISPLAYS ALL THE DOCUMENTS OF A PARTICULAR PATIENT AND HAS BUTTONS FOR ADDING DOCUMENTS AND NOTES.
import android.Manifest;
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
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sajid.myapplication.DatabaseHandler;
import adapters.DocumentsAdapter;
import objects.Item;
import objects.Patient;

import com.example.sajid.myapplication.FileUtils;
import com.example.sajid.myapplication.PhotoHelper;
import com.example.sajid.myapplication.R;
import objects.document_obj;
import com.example.sajid.myapplication.utility;
import com.loopj.android.http.RequestParams;


import org.json.simple.JSONValue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class documents extends ActionBarActivity {

    static final int REQUEST_TAKE_PHOTO = 1;
    static final int PICKFILE_RESULT_CODE = 2;
    static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    static document_obj doc_obj = new document_obj();
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
        ListView listView = (ListView)findViewById(R.id.listViewDocuments);
         docAdapter = new DocumentsAdapter(this,documents.this,documentList);
        listView.setAdapter(docAdapter);


    }


// gets patient name for title

    public Patient getPatientDataEdit(int id)
    {
        SQLiteDatabase myDataBase= openOrCreateDatabase("patientManager",MODE_PRIVATE,null);
        DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
        dbHandler.onCreate(myDataBase);

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

            if((documentList.get(i).get_doc_name()!=null)|| (documentList.get(i).get_doc_name()!=null))
            {
                displayDocument.setTitle(documentList.get(i).get_doc_name());
                displayDocument.setDiagnosis(documentList.get(i).get_doc_path());
                if((new File(documentList.get(i).get_doc_path()).exists()))
                {

                    if((documentList.get(i).get_doc_path().contains(".jpeg"))||(documentList.get(i).get_doc_path().contains(".jpg"))||
                        (documentList.get(i).get_doc_path().contains(".png")))
                    {   if (documentList.get(i).get_bmp() == null) {
                                 doc_obj = documentList.get(i);
                                 if(new File(documentList.get(i).get_doc_path()).exists())
                                    {   doc_obj = PhotoHelper.addMissingBmp(doc_obj);
                                        dbHandler.updateDocument(doc_obj,"0");}
                             }}
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
            }}
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


    public void startDocCam(View view)
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
           dispatchTakePictureIntent();
            //newCam();
        }





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

            Bitmap bitmap;
           // File file1 = new File(photoFile)
            bitmap =BitmapFactory.decodeFile(photoFile.getPath());
            ByteArrayOutputStream ostream = new ByteArrayOutputStream();

            // save image into gallery
            if(bitmap!=null)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, ostream);
            if(photoFile.length()<= 0)
            {
                photoFile.delete();
                utility.recreateActivityCompat(documents.this);
                return;
            }

            try {
                FileOutputStream fout = new FileOutputStream(photoFile);
                fout.write(ostream.toByteArray());
                fout.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }


            //doc_obj.set_bmp(PhotoHelper.getBitmapAsByteArray(Bitmap.createScaledBitmap(bitmap,120,120,false)));

            //doc_obj = PhotoHelper.addMissingBmp(doc_obj);
           // if(doc_obj.get_bmp()!=null) {
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

                String imageFileName = "IMG_" + timeStamp  ;
                File file = new File(doc_obj.get_doc_path());
                doc_obj.set_doc_name(photoFile.getName());
                doc_obj.set_date(formattedDate);

                patient.set_last_seen_date(formattedDate);
                dbHandler.updatePatient(patient,0);
                dbHandler.addDocument(doc_obj);

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
                if((file.getName().contains(".txt"))||(file.getName().contains(".doc"))||
                        (file.getName().contains(".pdf"))||(file.getName().contains(".jpeg"))||(file.getName().contains(".jpg"))
                        ||(file.getName().contains(".png")))
                {

                }
                else
                {
                   // Toast.makeText(documents.this,"Please Select from .jpg,.png,.txt,.doc,.pdf files",Toast.LENGTH_SHORT);
                    android.support.v7.app.AlertDialog.Builder builder1 = new android.support.v7.app.AlertDialog.Builder(this);

                    builder1.setMessage("Please Select from .jpg,.png,.txt,.doc,.pdf files")
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
                        doc_obj.set_bmp(PhotoHelper.getBitmapAsByteArray(BitmapFactory.decodeFile(file_path)));
                    }
            try
            {

                File storageDir =
                        new File(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES), "Patient Manager/"+patient.get_id()+"/Documents");
                if(!storageDir.exists())
                    storageDir.mkdir();


                 newFile = new File(storageDir.getPath()+"/"+new File(file_path).getName());
                if(accountType.equals(context.getString(R.string.account_type_helper))) {
                    int docPid = dbHandler.checkDoctorHelperPatientMapping(patient.get_id());
                    File storageDir1 =
                            new File(Environment.getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_PICTURES), "Patient Manager/"+docPid+"/Documents");

                    String path = storageDir1.getPath()+"/"+newFile.getName();

                    //  path.replaceAll("/" + String.valueOf(id) + "/", "/" + docPid + "/");
                    dbHandler.mapDoctorHelperDocuments(path,newFile.getPath());
                }
                int file_size = Integer.parseInt(String.valueOf(newFile.length()/1024));
                if(file_size > 20480 )
                {
                    Toast.makeText(documents.this, "File Too Large", Toast.LENGTH_SHORT).show();
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


                }
                FileUtils.copyFile(new File(file_path), newFile);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

                    doc_obj.set_doc_name(file_name);
                    doc_obj.set_doc_path(newFile.getPath());
                    doc_obj.set_id(pid);
                    doc_obj.set_date(formattedDate);
                    dbHandler.updatePatient(patient,0);
                    dbHandler.addDocument(doc_obj);
                Toast.makeText(this,"File Name & PATH are:"+file_name+"\n"+file_path, Toast.LENGTH_LONG).show();

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


}

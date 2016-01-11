package activity;
//DISPLAYS THE PATIENT PERSONAL DATA

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sajid.myapplication.*;
import com.example.sajid.myapplication.R;

import java.io.File;
import java.util.ArrayList;

import adapters.TwoTextFieldsAdapter;
import objects.Item;
import objects.Patient;


public class PatientProfileActivity extends AppCompatActivity {
    int id;
    RoundImage roundedImage;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile);
        Intent intent = getIntent();
        id = intent.getIntExtra("id",0);
        
        getPatientData(id);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(R.color.status_bar));
    }
   @Override
    protected void onResume()
    {
        super.onResume();
    }

    private void getPatientData(int id) {

        DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());


        Patient patient = dbHandler.getPatient(id);
        displayPatientData(patient);

    }

    public void showDocuments(View view)
    {
        Intent curIntent = getIntent();
        Intent intent =  new Intent(this, documents.class);
        int pid = curIntent.getIntExtra("id",0);
        intent.putExtra("parent",PatientProfileActivity.class.toString());
        intent.putExtra("id",id);
        startActivity(intent);

    }


    public void showNotes(View view)
    {
        Intent curIntent = getIntent();
        DatabaseHandler dbHandler = new DatabaseHandler(this);
        int pid = curIntent.getIntExtra("id",0);
        Intent intent;
       // if(dbHandler.getCurrentVersion(pid) != 0) {
             //intent =  new Intent(this, TabbedActivityCheck.class);
        intent =  new Intent(this, TabbedActivityCheck.class);
        //}
        //else {
          //   intent =  new Intent(this, Add_view_notes.class);
       // }


        intent.putExtra("id", pid);

        //intent.putExtra("version", dbHandler.getCurrentVersion(pid));
        intent.putExtra("parent", PatientProfileActivity.class.toString());
        startActivity(intent);
    }
    public  void deletePatientProfile()
    {

        final Intent intent1 =  new Intent(this, MainActivity.class);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Alert!!");
        alert.setMessage("Are you sure to delete record");
        alert.setPositiveButton("YES",new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = getIntent();
                int id = intent.getIntExtra("id",0);

                DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());

                Patient patient = new Patient();
                patient = dbHandler.getPatientForProfile(id);
                dbHandler.removePatient(patient);
                File storageDir =
                        new File(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES), "Patient Manager/"+patient.get_name());
                utility.deleteDirectory(storageDir);

                startActivity(intent1);
                dialog.dismiss();

            }
        });
        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        alert.show();




    }

    private void displayPatientData(Patient patient) {


        ArrayList<Item> itemsArrayList = new ArrayList<Item>();
        Item Iadd = new Item();
        Iadd.setTitle("Address");
        Iadd.setBmp(BitmapFactory.decodeResource(getResources(),R.drawable.ic_action_address));
        Iadd.setDiagnosis(patient.get_address());


        Item Ioccupation = new Item();
        Ioccupation.setTitle("Occupation");
        Ioccupation.setBmp(BitmapFactory.decodeResource(getResources(),R.drawable.ic_action_occupation));
        Ioccupation.setDiagnosis(patient.get_ocupation());

        Item Idiagnosis = new Item();
        Idiagnosis.setTitle("Diagnosis");
        Idiagnosis.setBmp(BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_diagnosis));
        Idiagnosis.setDiagnosis(patient.get_diagnosis());

        Item Iphone = new Item();
        Iphone.setTitle("Contact");
        Iphone.setBmp(BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_phone));
        Iphone.setDiagnosis(patient.get_contact_number());

        Item Iemail = new Item();
        Iemail.setTitle("Email");
        Iemail.setBmp(BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_email));
        Iemail.setDiagnosis(patient.get_email());

        itemsArrayList.add(Iadd);
        itemsArrayList.add(Ioccupation);
        itemsArrayList.add(Idiagnosis);
        itemsArrayList.add(Iphone);
        itemsArrayList.add(Iemail);
        TwoTextFieldsAdapter twoTextFieldsAdapter = new TwoTextFieldsAdapter(this,PatientProfileActivity.this,itemsArrayList);
        ListView listView = (ListView)findViewById(R.id.patientDatalist);
        listView.setAdapter(twoTextFieldsAdapter);

        TextView name = (TextView)findViewById(R.id.patientProfile_name);



        ImageView bmp = (ImageView)findViewById(R.id.patientPic);



        name.setText(patient.get_name()+"("+patient.get_gender()+"/"+patient.get_age()+")");


        byte[] image = patient.get_bmp();


        Bitmap bmpImage = BitmapFactory.decodeByteArray(image, 0, image.length);
        RoundImage roundedImage = new RoundImage( PhotoHelper.getResizedBitmap(bmpImage, 100, 100));
       // roundedImage = new RoundImage(bmpImage);
        bmp.setImageDrawable(roundedImage);
        setTitle(patient.get_name());
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setSubtitle("last checkup : "+patient.get_last_seen_date());



    }

    public void editpatient(View view)
    {
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);

        SQLiteDatabase myDataBase= openOrCreateDatabase("patientManager",MODE_PRIVATE,null);
        DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
        dbHandler.onCreate(myDataBase);
        Patient patient = new Patient();
        patient = dbHandler.getPatientForProfile(id);

       Intent intent1 = new Intent(this, Edit_patient_data.class);
        intent1.putExtra("id",id);

        startActivity(intent1);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_patient_profile, menu);
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

                finish();
                break;
            case R.id.Delete_Patient:
                deletePatientProfile();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void callPatient(View view)
    {
        DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
        Patient patient = dbHandler.getPatient(id);
        if((patient.get_contact_number()==null)||(patient.get_contact_number().equals("")))
        { Toast.makeText(PatientProfileActivity.this,"Please Add Contact Number To Make A Call",Toast.LENGTH_SHORT).show();}
        else
        {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + patient.get_contact_number()));
            startActivity(intent);

        }

    }
}

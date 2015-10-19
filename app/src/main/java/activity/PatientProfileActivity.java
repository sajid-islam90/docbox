package activity;
//DISPLAYS THE PATIENT PERSONAL DATA
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sajid.myapplication.*;
import com.example.sajid.myapplication.R;

import java.io.File;

import activity.Add_view_notes;
import activity.Edit_patient_data;
import activity.MainActivity;
import activity.TabbedActivityCheck;
import activity.documents;
import objects.Patient;


public class PatientProfileActivity extends ActionBarActivity {
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile);
        Intent intent = getIntent();
        id = intent.getIntExtra("id",0);
        
        getPatientData(id);

    }
   @Override
    protected void onResume()
    {
        super.onResume();
    }

    private void getPatientData(int id) {
        SQLiteDatabase myDataBase= openOrCreateDatabase("patientManager",MODE_PRIVATE,null);
        DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
        dbHandler.onCreate(myDataBase);

        Patient patient = new Patient();
       patient = dbHandler.getPatientForProfile(id);
        displayPatientData(patient);

    }

    public void showDocuments(View view)
    {
        Intent curIntent = getIntent();
        Intent intent =  new Intent(this, documents.class);
        int pid = curIntent.getIntExtra("id",0);
        intent.putExtra("parent",PatientProfileActivity.class.toString());
        intent.putExtra("id",pid);
        startActivity(intent);

    }


    public void showNotes(View view)
    {
        Intent curIntent = getIntent();
        DatabaseHandler dbHandler = new DatabaseHandler(this);
        int pid = curIntent.getIntExtra("id",0);
        Intent intent;
        if(dbHandler.getCurrentVersion(pid) != 0) {
             intent =  new Intent(this, TabbedActivityCheck.class);
        }
        else {
             intent =  new Intent(this, Add_view_notes.class);
        }


        intent.putExtra("id",pid);
        intent.putExtra("version",dbHandler.getCurrentVersion(pid));
        intent.putExtra("parent",PatientProfileActivity.class.toString());
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


        EditText name = (EditText)findViewById(R.id.textView);
        TextView age = (TextView)findViewById(R.id.textView2);
        TextView height = (TextView)findViewById(R.id.textView3);
        TextView gender = (TextView)findViewById(R.id.textView4);
        ImageView bmp = (ImageView)findViewById(R.id.imageView3);
        TextView diagnosis = (TextView) findViewById(R.id.textView10);
        String _name = patient.get_name();

        name.setText(patient.get_name());
        age.setText(patient.get_age());
        height.setText(patient.get_height());
        gender.setText(patient.get_gender());
        byte[] image = patient.get_bmp();
        diagnosis.setText(patient.get_diagnosis());
        Bitmap bmpImage = BitmapFactory.decodeByteArray(image, 0, image.length);
        bmp.setImageBitmap(bmpImage);
        setTitle(patient.get_name()+"'s Profile");

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

    public void removeExtraMedia()
    {
        DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
        int docVersion = dbHandler.getCurrentVersion(id);
        int mediaVersion = dbHandler.getCurrentMediaVersion(id);
    }
}

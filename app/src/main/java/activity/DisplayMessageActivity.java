package activity;
//ADDING A NEW PATIENT
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.sajid.myapplication.DatabaseHandler;
import objects.Patient;
import com.example.sajid.myapplication.PhotoHelper;
import com.example.sajid.myapplication.R;

import java.io.File;


public class DisplayMessageActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);
        Button btn_AddPatient = (Button)findViewById(R.id.save_patient_button);



    }


    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void startCamera(View view)
    {
        dispatchTakePictureIntent();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView mImageView = (ImageView) findViewById(R.id.imageView);
            mImageView.setImageBitmap(imageBitmap);
            Button button = (Button) findViewById(R.id.button);
            button.setVisibility(View.GONE);


        }
    }
    public void saveNewPatient(View view)
    {
        savePatientData();
    }
    public void savePatientData()
    {
        Intent intent =  new Intent(this, documents.class);
        String patientName = new String();
        String patientAge = new String();
        String patientHeight = new String();
        String patientGender = new String();
        String patientDiagnosis = new String();
        int selectedGender = 0;
        byte[] image;
        image = new byte[0];

        EditText edit_name = (EditText) findViewById(R.id.edit_name);
        EditText edit_address = (EditText)findViewById(R.id.edit_add);
        EditText edit_diagnosis = (EditText)findViewById(R.id.edit_diagnosis);

        EditText edit_age = (EditText) findViewById(R.id.edit_age);
        EditText edit_height = (EditText) findViewById(R.id.edit_height);
        ImageView bmp_image = (ImageView) findViewById(R.id.imageView);
        RadioGroup edit_gender = (RadioGroup) findViewById(R.id.edit_gender);
        selectedGender = edit_gender.getCheckedRadioButtonId();
        RadioButton edit_gender_rb = (RadioButton)findViewById(selectedGender);

       // Bundle extras = intent.getExtras();

        if (edit_name.getText().toString().trim().equals(""))
        {
           if(edit_name.getText().toString().trim().equals(""))
            Toast.makeText(this, "Please Add Name ", Toast.LENGTH_SHORT)
                    .show();

            return;

        }
        Bitmap bitmap = null;
        Patient newPatient = new Patient();
        if((bmp_image.getDrawable() != null)) {
             bitmap = ((BitmapDrawable) bmp_image.getDrawable()).getBitmap();
            image = PhotoHelper.getBitmapAsByteArray(bitmap);
            newPatient.set_bmp(image);
        }



        else {

            bmp_image.setImageDrawable(getResources().getDrawable(R.drawable.default_photo));
            bitmap = ((BitmapDrawable) bmp_image.getDrawable()).getBitmap();
            image = PhotoHelper.getBitmapAsByteArray(bitmap);
            newPatient.set_bmp(image);
        }

        patientName = edit_name.getText().toString();
        patientAge =edit_age.getText().toString();
        patientHeight = edit_height.getText().toString();
        patientGender = edit_gender_rb.getText().toString();
        patientDiagnosis = edit_diagnosis.getText().toString();

        newPatient.set_height(patientHeight);
        newPatient.set_diagnosis(patientDiagnosis);
        newPatient.set_gender(patientGender);
        newPatient.set_age(patientAge);
        newPatient.set_name(patientName);


        DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
        SQLiteDatabase myDataBase= openOrCreateDatabase("patientManager",MODE_PRIVATE,null);

        dbHandler.onCreate(myDataBase);

        dbHandler.addPatient(newPatient);

        File storageDir =
                new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES), "Patient Manager/"+patientName);
        if(!storageDir.exists()) {

            storageDir.mkdir();
           /* File storageDir1 =
                    new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES), "Patient Manager/"+patientName+"/Documents");
            storageDir1.mkdir();
            File storageDir2 =
                    new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES), "Patient Manager/"+patientName+"/Notes");
            storageDir2.mkdir();*/



        }

        Toast.makeText(this, "Patient Added", Toast.LENGTH_SHORT)
                .show();
        Patient patient = dbHandler.getLatestPatient();

        intent.putExtra("id",patient.get_id());
        intent.putExtra("parent",DisplayMessageActivity.class.toString());
        startActivity(intent);
        finish();


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_message, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            this.savePatientData();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}

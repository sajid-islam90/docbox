package activity;
//USED FOR EDITING PATIENT PERSONAL DATA
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import utilityClasses.DatabaseHandler;
import utilityClasses.PhotoHelper;
import com.elune.sajid.myapplication.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import objects.Patient;


public class Edit_patient_data extends ActionBarActivity {
    File storageDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_patient_data);
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);



        doWork(id);

    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntentForEdit() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void startCameraForEdit(View view)
    {
        dispatchTakePictureIntentForEdit();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView mImageView = (ImageView) findViewById(R.id.edit_patient_picture_imageView);
            mImageView.setImageBitmap(imageBitmap);
            Button button = (Button) findViewById(R.id.edit_patient_take_photo_button);
            button.setVisibility(View.GONE);


        }
    }


    private void doWork(int id)
    {
        DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
        Patient patient = dbHandler.getPatient(id);
        fillPatientData(patient);
    }

    private void fillPatientData(Patient patient)
    {
        EditText name = (EditText) findViewById(R.id.edit_patient_edit_name);
        TextView id = (TextView)findViewById(R.id.edit_patient_id);

        EditText age = (EditText)findViewById(R.id.edit_patient_edit_age);
        EditText OpdIpd = (EditText)findViewById(R.id.edit_patient_edit_OPD_IPD);
        EditText weight = (EditText)findViewById(R.id.edit_patient_edit_weight);
        EditText address = (EditText)findViewById(R.id.edit_patient_edit_add);
        EditText occupation = (EditText)findViewById(R.id.edit_patient_edit_occupation);
        EditText email = (EditText)findViewById(R.id.edit_patient_edit_email);
        EditText contact = (EditText)findViewById(R.id.edit_patient_edit_phone);
        EditText diagnosis = (EditText)findViewById(R.id.edit_patient_edit_diagnosis);
        RadioGroup gender = (RadioGroup)findViewById(R.id.edit_patient_edit_gender);
        RadioButton male = (RadioButton)findViewById(R.id.edit_patient_male_radio1);
        RadioButton female = (RadioButton)findViewById(R.id.edit_patient_female_radio2);
        ImageView bmp = (ImageView)findViewById(R.id.edit_patient_picture_imageView);
        String _gender = patient.get_gender();
        storageDir =
                new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES), "Patient Manager/"+patient.get_id());
        if(_gender!=null)
        { if(_gender.equals("Male") )
        {
            male.setChecked(true);
        }
        else if(_gender.equals("Female"))
        {
            female.setChecked(true);
        }}
        else
            male.setChecked(true);
        name.setText(patient.get_name());
        address.setText(patient.get_address());
        occupation.setText(patient.get_ocupation());
        OpdIpd.setText(patient.get_opd_ipd());
        email.setText(patient.get_email());
        contact.setText(patient.get_contact_number());
        age.setText(patient.get_age());
        weight.setText(patient.get_weight());
        diagnosis.setText(patient.get_diagnosis());
        id.setText(""+patient.get_id());
        byte[] image = patient.get_bmp();
        Bitmap bmpImage = BitmapFactory.decodeByteArray(image, 0, image.length);

        bmp.setImageBitmap(bmpImage);

        setTitle("Edit "+patient.get_name()+"' Profile");

    }
    public  void  savePatientData2(View view)
    {
        savePatientData1();
    }

    public void savePatientData1()
    {
        Intent intent =  new Intent(this, PatientProfileActivity.class);
        String patientName = new String();
        int patientId = 0;
        String patientAge = new String();
        String patientWeight = new String();
        String patientOpdIpd = new String();
        String patientGender = new String();
        String patientDiagnosis = new String();
        String patientOccupation= new String();
        String patientAddress = new String();
        String patientContact = new String();
        String patientEmail = new String();
        int selectedGender = 0;
        byte[] image;
        image = new byte[0];
DatabaseHandler databaseHandler = new DatabaseHandler(Edit_patient_data.this);
        EditText edit_name = (EditText) findViewById(R.id.edit_patient_edit_name);
        EditText edit_OpdIpd = (EditText)findViewById(R.id.edit_patient_edit_OPD_IPD);
        EditText edit_address = (EditText)findViewById(R.id.edit_patient_edit_add);
        EditText edit_occupation = (EditText)findViewById(R.id.edit_patient_edit_occupation);
        EditText edit_email = (EditText)findViewById(R.id.edit_patient_edit_email);
        EditText edit_contact = (EditText)findViewById(R.id.edit_patient_edit_phone);
        EditText edit_diagnosis = (EditText)findViewById(R.id.edit_patient_edit_diagnosis);
        TextView id = (TextView)findViewById(R.id.edit_patient_id);
        EditText edit_age = (EditText) findViewById(R.id.edit_patient_edit_age);
        EditText edit_weight = (EditText) findViewById(R.id.edit_patient_edit_weight);
        ImageView bmp_image = (ImageView) findViewById(R.id.edit_patient_picture_imageView);
        RadioGroup edit_gender = (RadioGroup) findViewById(R.id.edit_patient_edit_gender);
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
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");Calendar c = Calendar.getInstance();

        String formattedDate = df.format(c.getTime());

        patientName = edit_name.getText().toString();
        patientAge =edit_age.getText().toString();
        patientWeight = edit_weight.getText().toString();
        patientGender = edit_gender_rb.getText().toString();
        patientId =Integer.parseInt(id.getText().toString());
        patientDiagnosis = edit_diagnosis.getText().toString();
        patientAddress = edit_address.getText().toString();
        patientContact = edit_contact.getText().toString();
        patientEmail = edit_email.getText().toString();
        patientOccupation = edit_occupation.getText().toString();
        patientOpdIpd = edit_OpdIpd.getText().toString();

        Patient patient = databaseHandler.getPatient(patientId);
        newPatient.set_weight(patientWeight);
        newPatient.set_diagnosis(patientDiagnosis);
        newPatient.set_gender(patientGender);
        newPatient.set_age(patientAge);
        newPatient.set_name(patientName);
        newPatient.set_id(patientId);
        newPatient.set_next_follow_up_date(patient.get_next_follow_up_date());
        newPatient.set_last_seen_date(formattedDate);
        newPatient.set_address(patientAddress);
        newPatient.set_contact_number(patientContact);
        newPatient.set_email(patientEmail);
        newPatient.set_ocupation(patientOccupation);
        newPatient.set_opd_ipd(patientOpdIpd);


        if(storageDir.exists()) {
            File newDirectory =
                    new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES), "Patient Manager/" + patientId);


           // storageDir.renameTo(newDirectory);
        }
        DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
        dbHandler.updatePatient(newPatient,0);

        Intent curIntent = getIntent();
         int pid = curIntent.getIntExtra("id",0);
        intent.putExtra("id",pid);
        intent.putExtra("parent",Edit_patient_data.class.toString());
        startActivity(intent);
finish();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_patient_data, menu);
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
            case R.id.action_settings:
                return true;
            case R.id.action_add:
                this.savePatientData1();
                return true;
        }
        return super.onOptionsItemSelected(item);





    }
}

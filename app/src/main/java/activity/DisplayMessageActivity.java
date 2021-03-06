package activity;
//ADDING A NEW PATIENT
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
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
import android.widget.Toast;

import com.example.sajid.myapplication.DatabaseHandler;


import objects.Item;
import objects.Patient;
import com.example.sajid.myapplication.PhotoHelper;
import com.example.sajid.myapplication.R;
import com.example.sajid.myapplication.patients_today;
import com.example.sajid.myapplication.utility;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class DisplayMessageActivity extends ActionBarActivity {
    Patient newPatient = new Patient();
    String parentActivity = "main";
    String accountType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);
        Intent intent = getIntent();
        parentActivity = intent.getStringExtra("parent");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(DisplayMessageActivity.this);
        accountType  = prefs.getString(DisplayMessageActivity.this.getString(R.string.account_type), "");
        Button btn_AddPatient = (Button)findViewById(R.id.save_patient_button);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);



    }




    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        File storageDir =
                new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES), "Patient Manager/"+"profile_pictures");
        if(!storageDir.exists())

            storageDir.mkdir();
            try {
                 photoFile = PhotoHelper.createImageFile(0);
            }
            catch (Exception e)
            {

            }
        if (photoFile != null) {
            takePictureIntent.putExtra("output",
                    Uri.fromFile(photoFile));
            newPatient.set_photoPath(photoFile.getPath());
        }
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
           // Bundle extras = data.getExtras();
           // Bitmap imageBitmap = (Bitmap) extras.get("data");
            newPatient= PhotoHelper.addMissingBmp(newPatient);

            ImageView mImageView = (ImageView) findViewById(R.id.imageView);
            mImageView.setImageBitmap(BitmapFactory.decodeByteArray(newPatient.get_bmp(),0,newPatient.get_bmp().length));
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
        Intent intent =  new Intent(this, PatientProfileActivity.class);
        String patientName = new String();
        String patientAge = new String();
        String patientWeight = new String();
        String patientOPDIPD = new String();

        String patientGender = new String();
        String patientDiagnosis = new String();
        String patientOccupation= new String();
        String patientAddress = new String();
        String patientContact = new String();
        String patientEmail = new String();

        int selectedGender = 0;
        byte[] image;
        image = new byte[0];

        EditText edit_name = (EditText) findViewById(R.id.edit_name);
        EditText edit_diagnosis = (EditText)findViewById(R.id.edit_diagnosis);
        EditText edit_address = (EditText)findViewById(R.id.edit_add);
        EditText edit_occupation = (EditText)findViewById(R.id.edit_occupation);
        EditText edit_email = (EditText)findViewById(R.id.edit_email);
        EditText edit_contact = (EditText)findViewById(R.id.edit_phone);
        EditText edit_age = (EditText) findViewById(R.id.edit_age);
        EditText weight = (EditText) findViewById(R.id.edit_weight);
        ImageView bmp_image = (ImageView) findViewById(R.id.imageView);
        RadioGroup edit_gender = (RadioGroup) findViewById(R.id.edit_gender);
        EditText edit_OPDIPD = (EditText)findViewById(R.id.edit_Opd_Ipd);
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




        if((newPatient.get_bmp()==null)||(newPatient.get_bmp().length==0)) {

            bmp_image.setImageDrawable(getResources().getDrawable(R.drawable.default_photo));
            bitmap = ((BitmapDrawable) bmp_image.getDrawable()).getBitmap();
            image = PhotoHelper.getBitmapAsByteArray(bitmap);
            newPatient.set_bmp(image);
        }

        patientName = edit_name.getText().toString();
        patientAge =edit_age.getText().toString();
        patientWeight = weight.getText().toString();
        patientGender = edit_gender_rb.getText().toString();
        patientDiagnosis = edit_diagnosis.getText().toString();
        patientAddress = edit_address.getText().toString();
        patientContact = edit_contact.getText().toString();
        patientEmail = edit_email.getText().toString();
        patientOccupation = edit_occupation.getText().toString();
        patientOPDIPD = edit_OPDIPD.getText().toString();

        newPatient.set_weight(patientWeight);
        newPatient.set_diagnosis(patientDiagnosis);
        newPatient.set_gender(patientGender);
        newPatient.set_age(patientAge);
        newPatient.set_name(patientName);
        newPatient.set_address(patientAddress);
        newPatient.set_contact_number(patientContact);
        newPatient.set_email(patientEmail);
        newPatient.set_ocupation(patientOccupation);
        newPatient.set_next_follow_up_date("");
        newPatient.set_opd_ipd(patientOPDIPD);


        DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
        SQLiteDatabase myDataBase= openOrCreateDatabase("patientManager",MODE_PRIVATE,null);

        dbHandler.onCreate(myDataBase);

       long id = dbHandler.addPatient(newPatient);
        File storageDir =
                new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES), "Patient Manager/"+id);
        if(!storageDir.exists()) {

            storageDir.mkdir(); }

        if(parentActivity.equals("patients_today"))
        {
            if(!accountType.equals(DisplayMessageActivity.this.getString(R.string.account_type_helper)))
            {  Resources res = DisplayMessageActivity.this.getResources();
            final String address = res.getString(R.string.action_server_ip_address);
            final RequestParams params = new RequestParams();
            String patientJson = dbHandler.composeJSONfromSQLitePatient(String.valueOf(id), DisplayMessageActivity.this);
            params.put("usersJSON", patientJson);
            utility.sync("http://" + address + "/insertPatient.php", params, DisplayMessageActivity.this);
            utility.bookAppointmentToday((int) id, DisplayMessageActivity.this);
            intent = new Intent(DisplayMessageActivity.this,patients_today.class);}
            else
            {
                utility.bookAppointmentTodayLocally((int) id, DisplayMessageActivity.this);
                intent = new Intent(DisplayMessageActivity.this,patients_today.class);
            }
        }




        Toast.makeText(this, "Patient Added", Toast.LENGTH_SHORT)
                .show();
        Patient patient = dbHandler.getLatestPatient();

        intent.putExtra("id",patient.get_id());
        intent.putExtra("parent", DisplayMessageActivity.class.toString());
        addNotesFieldsTask task = new addNotesFieldsTask(patient.get_id());
        task.execute();
        if(parentActivity.equals("patients_today")) {
            finish();
        }
        else
        {startActivity(intent);
        finish();}


    }

    public  void addFieldsToPatient(int pid)
    {
        Resources resource = getResources();
        DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());

        String[] tabs = resource.getStringArray(R.array.tabs);
        ArrayList<Item> listOfItems = null;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        for(int j = 0;j<tabs.length;j++){
            int resId = resource.getIdentifier(tabs[j],"array",getPackageName());
            String[] fields = resource.getStringArray(resId);

            listOfItems = new ArrayList<>();
            for(int i = 0;i<fields.length;i++) {
                Item item = new Item();
                item.setTitle(fields[i]);
                item.setDiagnosis("");
                item.setPatient_id(pid);
                item.setSection(j);//0--> pre op section
                item.setDate(formattedDate);
                listOfItems.add(item);
            }
            databaseHandler.saveGenericNote(listOfItems,"0");
        }
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

    private  class addNotesFieldsTask extends AsyncTask{
        int pid;
        public addNotesFieldsTask(int pid) {
            this.pid = pid;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            addFieldsToPatient(pid);
            return null;
        }
    }
}

package activity;
//DISPLAYS THE PATIENT PERSONAL DATA

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.elune.sajid.myapplication.R;

import objects.time;
import utilityClasses.floatingactionbutton.FloatingActionButton;
import com.loopj.android.http.RequestParams;

import org.json.simple.JSONValue;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import adapters.TwoTextFieldsAdapter;
import fragments.Contact_Fragment;
import fragments.Diagnosis;
import objects.Item;
import objects.Patient;
import objects.personal_obj;
import utilityClasses.DatabaseHandler;
import utilityClasses.PhotoHelper;
import utilityClasses.RoundImage;
import utilityClasses.utility;


public class PatientProfileActivity extends AppCompatActivity implements ActionBar.TabListener {
    int id;
    String parent;
    RoundImage roundedImage;
    private int currentPage;
    private static String nextFollowUpDate ="";
    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    boolean firstTime;
    private static int Tabselected = 0;
    RelativeLayout helpLayout;
    FloatingActionButton floatingActionButton;
    FloatingActionButton floatingActionButtonHelp;
    String accountType;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try
        {
        super.onCreate(savedInstanceState);
        DatabaseHandler databaseHandler = new DatabaseHandler(PatientProfileActivity.this);
        setContentView(R.layout.activity_patient_profile);
        Intent intent = getIntent();
        id = intent.getIntExtra("id",0);
        parent = intent.getStringExtra("parent");
        Tabselected = intent.getIntExtra("tab",0);
        sliderFunction(Tabselected);
        final ActionBar actionBar = getSupportActionBar();
        Patient patient = databaseHandler.getPatient(id);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(PatientProfileActivity.this);
        SharedPreferences.Editor editor = prefs.edit();
        firstTime = prefs.getBoolean("firstTimePatientProfile",true);
        accountType  = prefs.getString(PatientProfileActivity.this.getString(R.string.account_type), "");

        LinearLayout linearLayout3 = (LinearLayout)findViewById(R.id.linearLayoutAddNextFollowupDate);
        linearLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doMoreWork();
            }
        });
       // actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        LinearLayout  linearLayout = (LinearLayout)findViewById(R.id.linearLayoutPatientProfileTabsDiagnosis);
        LinearLayout  linearLayout1 = (LinearLayout)findViewById(R.id.linearLayoutPatientProfileTabsFollowUp);
        LinearLayout  linearLayout2 = (LinearLayout)findViewById(R.id.linearLayoutPatientProfileTabsContact);
        LinearLayout linearLayout4 = (LinearLayout)findViewById(R.id.linearLayoutAddNextFollowupDateHelp);
        helpLayout = (RelativeLayout)findViewById(R.id.relativeLayoutHelp);
        if(firstTime) {
            helpLayout.setVisibility(View.VISIBLE);
            editor.putBoolean("firstTimePatientProfile",false);
            editor.commit();
            firstTime = false;
        }
        else {
            helpLayout.setVisibility(View.GONE);
        }
        assert linearLayout4 != null;
        linearLayout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helpLayout.setVisibility(View.GONE);
                doMoreWork();
            }
        });
        helpLayout = (RelativeLayout)findViewById(R.id.relativeLayoutHelp);
helpLayout.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        helpLayout.setVisibility(View.GONE);
    }
});


        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        floatingActionButtonHelp =(FloatingActionButton)findViewById(R.id.fabButtonPatientProfileHelp);
        floatingActionButtonHelp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                helpLayout.setVisibility(View.GONE);
                final CharSequence[] items = { "Clinical Notes", "Reports", "Cancel" };
                AlertDialog.Builder builder = new AlertDialog.Builder(PatientProfileActivity.this);

                builder.setTitle("Access Info!");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("Clinical Notes")) {
                            if (!accountType.equals(PatientProfileActivity.this.getString(R.string.account_type_helper)))
                                showNotes();
                            else
                            Toast.makeText(PatientProfileActivity.this, "You are not authorised to use this feature", Toast.LENGTH_SHORT).show();
                        } else if (items[item].equals("Reports")) {
                            showDocuments();
                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();

            }
        });

        floatingActionButton =(FloatingActionButton)findViewById(R.id.fabButtonPatientProfile);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {"Clinical Notes", "Reports", "Cancel" };
                AlertDialog.Builder builder = new AlertDialog.Builder(PatientProfileActivity.this);

                builder.setTitle("Access Info!");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("Clinical Notes")) {
                            if (!accountType.equals(PatientProfileActivity.this.getString(R.string.account_type_helper)))
                            showNotes();
                            else
                            Toast.makeText(PatientProfileActivity.this, "You are not authorised to use this feature", Toast.LENGTH_SHORT).show();
                        } else if (items[item].equals("Reports")) {
                            showDocuments();
                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();

            }
        });
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pagerPatientProfile);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sliderFunction(0);
                mViewPager.setCurrentItem(0);

            }
        });
        linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sliderFunction(1);
                mViewPager.setCurrentItem(1);

            }
        });
        linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sliderFunction(2);
                mViewPager.setCurrentItem(2);

            }
        });

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
              //  actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));

        }
        mViewPager.setCurrentItem(Tabselected);
        getPatientData(id);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(R.color.status_bar));
        ImageView imageView = (ImageView)findViewById(R.id.deleteProfile);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePatientProfile();
            }
        });
        DetailOnPageChangeListener detailOnPageChangeListener = new DetailOnPageChangeListener();
        mViewPager.setOnPageChangeListener(detailOnPageChangeListener);
    }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
   @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(PatientProfileActivity.this,Activity_main_2.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    private void getPatientData(int id) {

        DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());


        Patient patient = dbHandler.getPatient(id);
        displayPatientData(patient);

    }

    public void showDocuments()
    {
        Intent curIntent = getIntent();
        Intent intent =  new Intent(this, documents.class);
        int pid = curIntent.getIntExtra("id",0);
        intent.putExtra("parent",PatientProfileActivity.class.toString());
        intent.putExtra("governingActivity",parent);
        intent.putExtra("id",id);
        startActivity(intent);

    }


    public void showNotes()
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

        final Intent intent1 =  new Intent(this, Activity_main_2.class);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Alert!!");
        alert.setMessage("Are you sure you want to delete patient");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = getIntent();
                int id = intent.getIntExtra("id", 0);

                DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());

                Patient patient = new Patient();
                patient = dbHandler.getPatientForProfile(id);
                dbHandler.removePatient(patient);
                File storageDir =
                        new File(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES), "Patient Manager/" + patient.get_name());
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
        Iphone.setTitle("Next Follow Up Date");
        Iphone.setBmp(BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_calender_patient_profile));
        Iphone.setDiagnosis(patient.get_next_follow_up_date());

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
         TextView textView = (TextView)findViewById(R.id.textViewNextFollowUpDate);
        TextView name = (TextView)findViewById(R.id.patientProfile_name);
        TextView gender = (TextView)findViewById(R.id.textViewGender);
        TextView age = (TextView)findViewById(R.id.textViewAge);
        TextView OpdIpd = (TextView)findViewById(R.id.textViewOPDIPD);
        TextView weight = (TextView)findViewById(R.id.textViewWeight);
        weight.setText(patient.get_weight()+ " Kgs. ");
        textView.setText(patient.get_next_follow_up_date());

        ImageView bmp = (ImageView)findViewById(R.id.patientPic);

        gender.setText(patient.get_gender()+",");
        if(!patient.get_age().equals(""))
        age.setText(patient.get_age()+" Yrs. old");
        else
            age.setText(" - Yrs. old");
        name.setText(patient.get_name());
        OpdIpd.setText(patient.get_opd_ipd());
DatabaseHandler databaseHandler = new DatabaseHandler(PatientProfileActivity.this);
        byte[] image = patient.get_bmp();
if(image.length==0) {
    //patient.set_bmp(PhotoHelper.getBitmapAsByteArray(BitmapFactory.decodeResource(getResources(), R.drawable.default_photo)));
    //databaseHandler.updatePatient(patient,0);
    image =PhotoHelper.getBitmapAsByteArray(BitmapFactory.decodeResource(getResources(), R.drawable.default_photo));
}
        Bitmap bmpImage = BitmapFactory.decodeByteArray(image, 0, image.length);
        RoundImage roundedImage = new RoundImage( PhotoHelper.getResizedBitmap(bmpImage, 100, 100));
       // roundedImage = new RoundImage(bmpImage);
        bmp.setImageDrawable(roundedImage);
        setTitle(patient.get_name());
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setSubtitle("last edited : "+patient.get_last_seen_date());



    }
    public void doMoreWork() {

        final DatabaseHandler databaseHandler = new DatabaseHandler(PatientProfileActivity.this);
        final Patient patient = databaseHandler.getPatient(id);


        ArrayList<HashMap<String, String>> userList = databaseHandler.getAllSyncUsers();
        if ((userList.size() > 0)&&  (!accountType.equals(PatientProfileActivity.this.getString(R.string.account_type_helper)))) {
            new AlertDialog.Builder(PatientProfileActivity.this)
                    .setTitle("Data Not Synced With Cloud")
                    .setMessage("Please sync data with cloud before setting appointment!!!")
                    .setPositiveButton("Take me to sync data", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(PatientProfileActivity.this, data_sync_activity.class);
                            startActivity(intent);
                            finish();
                            // continue with delete
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                            // do nothingret
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else{
            LayoutInflater li = LayoutInflater.from(PatientProfileActivity.this);
        final View promptsView = li.inflate(R.layout.date_picker, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                PatientProfileActivity.this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                                final Date date = new Date();
                                final String[] appointmentDate = {""};
                                final DatePicker datePicker = (DatePicker) promptsView.findViewById(R.id.datePicker);
                                SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                                final RequestParams paramsDocuments = new RequestParams();
                                final ArrayList<ArrayList<String>> notesList;
                                notesList = new ArrayList<ArrayList<String>>();
                                final ArrayList<String> list = new ArrayList<>();
                                final int month = datePicker.getMonth() + 1;
                                final int year = datePicker.getYear();
                                final int day = datePicker.getDayOfMonth();

                                date.setDate(day);
                                date.setMonth(month - 1);
                                date.setYear(year - 1900);
                                String dayOfTheWeek = sdf.format(date);
                                dayOfTheWeek = dayOfTheWeek.toLowerCase();
                                ArrayList<String> settings = new ArrayList<>();
                                settings = databaseHandler.getAppointmentSettings(dayOfTheWeek);
                                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                                appointmentDate[0] = sdf1.format(date);
                                AlertDialog.Builder builder = new AlertDialog.Builder(PatientProfileActivity.this);
                                final ArrayList<String> finalSettings = settings;
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {


                                        if ((finalSettings.size() > 0)||(accountType.equals(PatientProfileActivity.this.getString(R.string.account_type_helper))))


                                        {
                                            if (accountType.equals(PatientProfileActivity.this.getString(R.string.account_type_helper)))
                                                utility.bookAppointmentTodayLocally(patient.get_id(), PatientProfileActivity.this);
                                            else {

time time;
                                            list.add(String.valueOf(databaseHandler.getCustomerId()));
                                            list.add(String.valueOf(patient.get_first_aid_id()));
                                            list.add(appointmentDate[0]);
                                            list.add(finalSettings.get(1));
                                            list.add((finalSettings.get(2)));
                                            notesList.add(list);

                                                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(PatientProfileActivity.this);


                                                String hash = sharedPref.getString(PatientProfileActivity.this.getString(R.string.hash_code), "");
                                                ArrayList<String> hex = new ArrayList<>();
                                                hex.add(hash);
                                                notesList.add(hex);
                                                String s1 = null;

                                                StringWriter out = new StringWriter();
                                                try {
                                                    JSONValue.writeJSONString(notesList, out);
                                                    s1 = out.toString();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                                paramsDocuments.put("bookAppointment", s1);
                                                String address = "docbox.co.in/sajid/"; //appointment_settings.this.getResources().getString(R.string.action_server_ip_address);
                                                utility.sync("http://" + address + "/bookAppointment.php", paramsDocuments, PatientProfileActivity.this);
                                                // msgBookedPatient(appointmentDate[0],finalSettings.get(1),finalSettings.get(2));
                                                // list.add(settings.get())
                                                nextFollowUpDate = day + "/" + month + "/" + year;
                                                patient.set_next_follow_up_date(nextFollowUpDate);
                                                databaseHandler.updatePatient(patient,1);
                                            }}else{
                                                Toast.makeText(PatientProfileActivity.this, "You are unavailable on this day please choose another date", Toast.LENGTH_SHORT).show();
                                            }


                                            utility.recreateActivityCompat(PatientProfileActivity.this);


                                        // User clicked OK button
                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User cancelled the dialog
                                    }
                                });
                                sdf1 = new SimpleDateFormat("dd-MM-yyyy");
                                String newDate = sdf1.format(date);
                                builder.setMessage("Are you sure you want to book an appointment for " + patient.get_name() + " on " + newDate)
                                        .setTitle("Book Appointment");
                                AlertDialog dialogNew = builder.create();
                                dialogNew.show();


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
    public void doWork(final time time)
    {
        LayoutInflater li = LayoutInflater.from(PatientProfileActivity.this);
        final View promptsView = li.inflate(R.layout.time_picker, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                PatientProfileActivity.this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                                final TimePicker timePicker = (TimePicker) promptsView.findViewById(R.id.timePicker);

                                time.setHour(timePicker.getCurrentHour());
                                time.setMinute(timePicker.getCurrentMinute());

//                                utility.recreateActivityCompat(PatientProfileActivity.this);



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
    public void editpatient(View view)
    {
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);

        SQLiteDatabase myDataBase= openOrCreateDatabase("patientManager",MODE_PRIVATE,null);
        DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
       // dbHandler.onCreate(myDataBase);
        Patient patient = new Patient();
        patient = dbHandler.getPatientForProfile(id);

       Intent intent1 = new Intent(this, Edit_patient_data.class);
        intent1.putExtra("id",id);

        startActivity(intent1);
//finish();


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
       //int id = item.getItemId();

        switch (item.getItemId()) {
//            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent intent1 = new Intent(this,Activity_main_2.class);

                startActivity(intent1);
                finish();
                break;
//
            case R.id.action_help:
                if(helpLayout.getVisibility()==View.VISIBLE)
               helpLayout.setVisibility(View.GONE);
                else
                helpLayout.setVisibility(View.VISIBLE);
                return true;
//            case R.id.add_follow_up:
//                startAddNotes();
//                return true;
//            case R.id.view_follow_ups:
//                Intent intent = new Intent(this,view_all_versions.class);
//                intent.putExtra("id",id);
//                startActivity(intent);
//                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void startAddNotes()
    {
        Intent curIntent = getIntent();
        DatabaseHandler databaseHandler = new DatabaseHandler(PatientProfileActivity.this);
        int version  =  databaseHandler.getMaxFollowupVersion(id);
        Intent intent =  new Intent(this, followUp.class);
        int pid = curIntent.getIntExtra("id",0);
        intent.putExtra("version",version+1);
        intent.putExtra("parent",PatientProfileActivity.class.toString());
        intent.putExtra("id",pid);
        startActivity(intent);
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
    public void msgBookedPatient(String appointmentDate,String bookingStartTime,String bookingEndTime)
    {
        final DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
        final Patient patient = dbHandler.getPatient(id);
        LayoutInflater li = LayoutInflater.from(PatientProfileActivity.this);
        if((patient.get_contact_number()==null)||(patient.get_contact_number().equals("")))
        { Toast.makeText(PatientProfileActivity.this,"Please Add Contact Number To Message",Toast.LENGTH_SHORT).show();}
        else
        {
            final Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
            final View promptsView = li.inflate(R.layout.sms_text, null);
            final TextView textView = (TextView)promptsView.findViewById(R.id.sms_Edit_Text);





                                    String address = getResources().getString(R.string.action_server_ip_address);
                                    ArrayList<String> data = new ArrayList<String>();
                                    data.add(dbHandler.getPersonalInfo().get_name());
                                    data.add(appointmentDate);
                                    // data.add("www.firstaid.com");
                                    data.add(dbHandler.getPersonalInfo().get_password());
                                    data.add(dbHandler.getPersonalInfo().get_address());
                                    data.add("Between "+bookingStartTime+" to "+bookingEndTime);
                                    data.add(patient.get_contact_number());
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
                                    utility.sync("http://" + address + "/appointmentBookedPatientSMS.php", params, PatientProfileActivity.this);
                                    Toast.makeText(PatientProfileActivity.this,"Message Sent",Toast.LENGTH_SHORT);
//                                    smsIntent.setType("vnd.android-dir/mms-sms");
//                                    smsIntent.putExtra("address", patient.get_contact_number());
//                                    smsIntent.putExtra("sms_body", textView.getText() + "\n-Sent From DocBox");
//                                    PatientProfileActivity.this.startActivity(Intent.createChooser(smsIntent, "SMS:"));
                                }




    }

    public void msgPatient(View view)
    {
        final DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
        final Patient patient = dbHandler.getPatient(id);
        LayoutInflater li = LayoutInflater.from(PatientProfileActivity.this);
        if((patient.get_contact_number()==null)||(patient.get_contact_number().equals("")))
        { Toast.makeText(PatientProfileActivity.this,"Please Add Contact Number To Message",Toast.LENGTH_SHORT).show();}
        else
        {
            final Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
            final View promptsView = li.inflate(R.layout.sms_text, null);
            final TextView textView = (TextView)promptsView.findViewById(R.id.sms_Edit_Text);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    PatientProfileActivity.this);

            // set prompts.xml to alertdialog builder
            alertDialogBuilder.setView(promptsView);

            alertDialogBuilder
                    .setCancelable(false)
                    .setTitle("Compose Message")
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    String address = getResources().getString(R.string.action_server_ip_address);
                                    ArrayList<String> data = new ArrayList<String>();
                                    data.add(dbHandler.getPersonalInfo().get_name());
                                    data.add(textView.getText() + "\n-Sent From DocBox");
                                   // data.add("www.firstaid.com");
                                    data.add(patient.get_contact_number());
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
                                    utility.sync("http://" + address + "/patientMessage.php", params, PatientProfileActivity.this);
                                    Toast.makeText(PatientProfileActivity.this,"Message Sent",Toast.LENGTH_SHORT);
//                                    smsIntent.setType("vnd.android-dir/mms-sms");
//                                    smsIntent.putExtra("address", patient.get_contact_number());
//                                    smsIntent.putExtra("sms_body", textView.getText() + "\n-Sent From DocBox");
//                                    PatientProfileActivity.this.startActivity(Intent.createChooser(smsIntent, "SMS:"));
                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    dialog.cancel();
                                }
                            });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();



//            smsIntent.setType("vnd.android-dir/mms-sms");
//            smsIntent.putExtra("address", patient.get_contact_number());
//            smsIntent.putExtra("sms_body", "Sent From DocBox");
//            PatientProfileActivity.this.startActivity(Intent.createChooser(smsIntent, "SMS:"));

        }

    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        mViewPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }


    public class DetailOnPageChangeListener extends ViewPager.SimpleOnPageChangeListener {



        @Override
        public void onPageSelected(int position) {
            currentPage = position;
//            if(position != 0)
//            {
//                sliderSunday.setVisibility(View.GONE);
//            }
//            else
//            {
//                sliderSunday.setVisibility(View.VISIBLE);
//            }
            sliderFunction(position);
        }

        public final int getCurrentPage() {
            return currentPage;
        }
    }

    public void sliderFunction(int position)
    {
        LinearLayout  linearLayout = (LinearLayout)findViewById(R.id.linearLayoutPatientProfileTabsDiagnosis);
        LinearLayout  linearLayout1 = (LinearLayout)findViewById(R.id.linearLayoutPatientProfileTabsFollowUp);
        LinearLayout  linearLayout2 = (LinearLayout)findViewById(R.id.linearLayoutPatientProfileTabsContact);
        if(position == 0)
        {
            linearLayout.setBackgroundResource(R.color.green);
            linearLayout1.setBackgroundResource(android.R.color.transparent);
            linearLayout2.setBackgroundResource(android.R.color.transparent);
        }
        else if(position == 1)
        {
            linearLayout1.setBackgroundResource(R.color.green);
            linearLayout2.setBackgroundResource(android.R.color.transparent);
            linearLayout.setBackgroundResource(android.R.color.transparent);
        }
        else if(position == 2)
        {
            linearLayout2.setBackgroundResource(R.color.green);
            linearLayout.setBackgroundResource(android.R.color.transparent);
            linearLayout1.setBackgroundResource(android.R.color.transparent);
        }
    }
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        Fragment fragment;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            if(position == 0)
            return Diagnosis.newInstance(position,id);
            else if(position == 1)
            {
                return view_all_versions.newInstance(position,id);
            }
else
                return Contact_Fragment.newInstance(position, id);
            //return PlaceholderFragment1.newInstance1(position);

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            DatabaseHandler databaseHandler = new DatabaseHandler(PatientProfileActivity.this);
            personal_obj personalInfo = databaseHandler.getPersonalInfo();
            int specialityId = Integer.parseInt(personalInfo.get_speciality());
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
                case 3:
                    return getString(R.string.title_section4).toUpperCase(l);
            }
            return null;
        }


    }

    public static class PlaceholderFragment1 extends Fragment implements View.OnClickListener    {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static ArrayList<Item> media = new ArrayList<>();

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment1 newInstance1(int sectionNumber) {
            PlaceholderFragment1 fragment1 = new PlaceholderFragment1();
            Bundle args = new Bundle();
            // section = sectionNumber;
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment1.setArguments(args);
            return fragment1;
        }

        public PlaceholderFragment1() {
        }



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_drug_allergy, container, false);


            return rootView;
        }

        @Override
        public void onClick(View v) {

        }
    }
}

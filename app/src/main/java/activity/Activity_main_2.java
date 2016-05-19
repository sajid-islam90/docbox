package activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import utilityClasses.ConnectionDetector;
import utilityClasses.DatabaseHandler;
import utilityClasses.NsdHelper;
import utilityClasses.PhotoHelper;
import com.elune.sajid.myapplication.R;
import utilityClasses.RoundImage;

import fragments.UserProfile;
import utilityClasses.utility;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import objects.Patient;
import objects.document_obj;
import objects.personal_obj;


public class  Activity_main_2 extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private static final int REQUEST_TAKE_PHOTO = 100;
    private PendingIntent pendingIntent;
    private AlarmManager manager;
    boolean doubleBackToExitPressedOnce = false;

    /**
     * Used to store the last screen title. For use in .
     */
    private String profilePicPath;
    private TextView mEmail;
    private TextView mName;
    RoundImage roundedImage;
    private CharSequence mTitle;
    private int fragmentNumber = 1;
    private ProgressDialog pdia;
     ProgressDialog pdia1;
    String accountType;
    static NsdHelper mNsdHelper;
    boolean hasHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_2);
       // startService(new Intent(this, SyncService.class));

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Activity_main_2.this);
        boolean restoreFlag = prefs.getBoolean("restore", false);
       int fragmentNumberToShow =  getIntent().getIntExtra("fragmentNumber",1);
//        Toolbar toolbar = (Toolbar)findViewById(R.id.main_activity_toolbar);
//        setSupportActionBar(toolbar);
        if(restoreFlag){
            pdia = new ProgressDialog(Activity_main_2.this);
            pdia.setMessage("Restoring Data Please Wait");
            pdia.show();
           // while (! LoginActivity.testRestoreData.getStatus().equals(AsyncTask.Status.FINISHED));
            TestRestoreData testRestoreData = new TestRestoreData();
            testRestoreData.execute((Void) null);

        }
//        if(mNsdHelper==null)
//        { mNsdHelper = new NsdHelper(this);
//            mNsdHelper.initializeNsd();}
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        DatabaseHandler dbHandle = new DatabaseHandler(getApplicationContext());
        personal_obj personalObj =  dbHandle.getPersonalInfo();
        ImageView imageView = (ImageView)findViewById(R.id.profilePic);
        Bitmap bmp = null;
        bmp = BitmapFactory.decodeFile(personalObj.get_photoPath());


        if(bmp!=null)
        {bmp = PhotoHelper.getResizedBitmap(bmp, 200, 200);
            roundedImage = new RoundImage(bmp);
            // Bitmap bmpImage = BitmapFactory.decodeByteArray(image, 0, image.length);

        }
        else {

            bmp = BitmapFactory.decodeResource(getResources(),R.drawable.add_new_photo);
            roundedImage = new RoundImage(bmp);
            imageView.setBackgroundResource(R.drawable.add_new_photo);
        }
        imageView.setImageDrawable(roundedImage);
        mName = (TextView)findViewById(R.id.drawer_name);
        if (accountType.equals(Activity_main_2.this.getString(R.string.account_type_doctor)))
        mName.setText("Dr." + personalObj.get_name());
        else
            mName.setText("Mr." + personalObj.get_name());
        mEmail = (TextView)findViewById(R.id.drawer_email);
        mEmail.setText(personalObj.get_email());
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        FragmentManager fragmentManager = getSupportFragmentManager();
        ArrayList<String> latLong = dbHandle.getSavedLatitudeLongitude();
        if((latLong.get(0)==null)&&(latLong.get(1)==null)&&(!accountType.equals(Activity_main_2.this.getString(R.string.account_type_helper))))
        fragmentManager.beginTransaction()
                .replace(R.id.container, UserProfile.newInstance(3))
                .commit();
        else
        {
            if(fragmentNumberToShow == 2)
                fragmentManager.beginTransaction()
                        .replace(R.id.container, activity_view_patient_visits.newInstance(2, true))

                        .commit();
            else
            fragmentManager.beginTransaction()
                    .replace(R.id.container, MainActivity.newInstance(1))
                    .commit();

        }


    }


    public void addHelper()
    {
        LayoutInflater li = LayoutInflater.from(Activity_main_2.this);
        final RequestParams params = new RequestParams();
        Resources res = Activity_main_2.this.getResources();
        final DatabaseHandler databaseHandler = new DatabaseHandler(Activity_main_2.this);
        final String address = res.getString(R.string.action_server_ip_address);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Activity_main_2.this);
        boolean helper  = prefs.getBoolean("helperAdded", false);
        if(!helper)
        {
            final View promptsView = li.inflate(R.layout.sms_text, null);
            final TextView textView = (TextView)promptsView.findViewById(R.id.sms_Edit_Text);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    Activity_main_2.this);

            // set prompts.xml to alertdialog builder
            alertDialogBuilder.setView(promptsView);

            alertDialogBuilder
                    .setCancelable(false)
                    .setTitle("Enter Helper Email")
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    String s1 = null;
                                    ArrayList<String> personalInfo = new ArrayList<>();
                                    personalInfo.add(String.valueOf(databaseHandler.getCustomerId()));
                                    personalInfo.add(textView.getText().toString());

                                    StringWriter out = new StringWriter();
                                    try {
                                        JSONValue.writeJSONString(personalInfo, out);
                                        s1 = out.toString();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    String Json = s1;
                                    params.put("emailPasswordJSON", Json);

                                    // utility.syncData("http://docbox.co.in/sajid/register.php",params,getApplicationContext(),prgDialog,client);

                                    Thread thread = new Thread(){
                                        @Override
                                        public void run() {
                                            try {
                                                AsyncHttpClient client = new SyncHttpClient(true, 80, 443);
                                                client.post("http://" + address + "/registerHelperByDoctor.php", params, new AsyncHttpResponseHandler() {
                                                    // client.post("http://docbox.co.in/sajid/register.php", params, new AsyncHttpResponseHandler() {
                                                    @Override
                                                    public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {

                                                        try {
                                                            String str = new String(bytes, "UTF-8");
                                                            JSONParser parser = new JSONParser();
                                                            JSONObject object = (JSONObject) parser.parse(str);
                                                            String message = (String) object.get("message");
                                                            if(message.equals("registered"))
                                                            {

                                                            }
                                                            else
                                                            {

                                                            }
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }

                                                    }

                                                    @Override
                                                    public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {
                                                        try {


                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }


                                                    }

                                                    @Override
                                                    public void onFinish() {


                                                    }


                                                });

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    };
                                    thread.start();

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


        }
    }




    @Override
    public void onNavigationDrawerItemSelected(int position) {


        // update the main content by replacing fragments

        FragmentManager fragmentManager = getSupportFragmentManager();
        final DatabaseHandler databaseHandler = new DatabaseHandler(Activity_main_2.this);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Activity_main_2.this);
        accountType = prefs.getString(Activity_main_2.this.getString(R.string.account_type), "");
        ArrayList<String> latLong = databaseHandler.getSavedLatitudeLongitude();
       // if (((latLong.get(0) != null) && (latLong.get(1) != null) && (!latLong.get(0).equals("")) && (!latLong.get(1).equals("")))||(accountType.equals(Activity_main_2.this.getString(R.string.account_type_helper))))
        {    String s1 = null;
        ArrayList<String> CstmrId = new ArrayList<>();
        int customerId = databaseHandler.getCustomerId();
        CstmrId.add(String.valueOf(customerId));
        final RequestParams params = new RequestParams();
        StringWriter out = new StringWriter();
        try {
            JSONValue.writeJSONString(CstmrId, out);
            s1 = out.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("DoctorId", s1);
        final AsyncHttpClient client = new SyncHttpClient(true, 80, 443);
        final String address = getResources().getString(R.string.action_server_ip_address);
        pdia1 = new ProgressDialog(Activity_main_2.this);
        pdia1.setMessage("Fetching appointments please wait");
        pdia1.setCanceledOnTouchOutside(false);

        hasHelper = prefs.getBoolean("hasHelper", false);
        if (position == 0) {


            {
                fragmentNumber = 1;
                fragmentManager.beginTransaction()
                        .replace(R.id.container, MainActivity.newInstance(position + 1))

                        .commit();
            }

            {


            }
            //mTitle = "My Calender";
        } else if (position == 1) {

            if (!accountType.equals(Activity_main_2.this.getString(R.string.account_type_helper))) {
                fragmentManager.beginTransaction()
                        .replace(R.id.container, activity_view_patient_visits.newInstance((position + 1), true))

                        .commit();
                fragmentNumber = 2;
            }
            else {
                Toast.makeText(Activity_main_2.this, "You are not authorised to use this feature", Toast.LENGTH_SHORT).show();
//                fragmentManager.beginTransaction()
//                        .replace(R.id.container, MainActivity.newInstance(position + 1))
//
//                        .commit();
            }
            //mTitle = "My Calender";
        } else if (position == 2) {
            if (!accountType.equals(Activity_main_2.this.getString(R.string.account_type_helper)))

            {
                ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
                if (!cd.isNetworkAvailable()) {
                    // Internet Connection is not present
                    new AlertDialog.Builder(Activity_main_2.this)
                            .setTitle("Internet ERROR !!!")
                            .setMessage("No Internet Connection Found Please Connect To Internet To Change Profile Settings")
                            .setPositiveButton("Take me to Mobile settings", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivityForResult(new Intent(Settings.ACTION_SETTINGS), 0);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    // stop executing code by return
                    return;
                }


                LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                if (!statusOfGPS) {
                    new AlertDialog.Builder(Activity_main_2.this)
                            .setTitle("GPS ERROR !!!")
                            .setMessage("Please Enable GPS to Use This Service")
                            .setPositiveButton("Take me to GPS settings", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                fragmentNumber = 3;
                fragmentManager.beginTransaction()
                        .replace(R.id.container, UserProfile.newInstance(position + 1))
                        .commit();
            } else {
                Toast.makeText(Activity_main_2.this, "You are not authorised to use this feature", Toast.LENGTH_SHORT).show();
//                fragmentManager.beginTransaction()
//                        .replace(R.id.container, MainActivity.newInstance(position + 1))
//
//                        .commit();
            }


        } else if (position == 3) {
            boolean wifiEnable = false;
            Object o = new Object();
            String message = "";
            ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            Method[] wmMethods = wifi.getClass().getDeclaredMethods();
            for (Method method : wmMethods) {
                if (method.getName().equals("isWifiApEnabled")) {

                    try {
                        wifiEnable = (boolean) method.invoke(wifi);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
            //wifi = connManager1.
            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (accountType.equals(Activity_main_2.this.getString(R.string.account_type_doctor)))
                message = "Create a WiFi hotspot network";
            if (accountType.equals(Activity_main_2.this.getString(R.string.account_type_helper))) {
                message = "Connect to a WiFi network";
            }

            if ((!mWifi.isConnected()) && (!wifiEnable)) {

                // Internet Connection is not present
                new AlertDialog.Builder(Activity_main_2.this)
                        .setTitle("Not connected to any WiFi network")
                        .setMessage("Enable WiFi ")
                        .setPositiveButton(message, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (accountType.equals(Activity_main_2.this.getString(R.string.account_type_doctor)))
                                    startActivityForResult(new Intent(Settings.ACTION_WIRELESS_SETTINGS), 0);
                                if (accountType.equals(Activity_main_2.this.getString(R.string.account_type_helper))) {
                                    startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), 0);
                                }
                            }
                        })
                        .setNegativeButton("Continue without connecting", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (!accountType.equals(Activity_main_2.this.getString(R.string.account_type_helper)))

                                { // pdia1.show();
                                    Thread thread = new Thread() {
                                        @Override
                                        public void run() {
                                            try {
                                                fragmentNumber = 4;
                                                Intent intent = new Intent(Activity_main_2.this, patients_today.class);
                                                intent.putExtra("nsd", mNsdHelper);
                                                startActivity(intent);
                                                // hitApiForAppointment("http://" + address + "/fetchDoctorAppointments.php",params,client,Activity_main_2.this);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    };
                                    thread.start();
                                } else {


                                    Intent intent = new Intent(Activity_main_2.this, patients_today.class);
                                    startActivity(intent);


//                Intent intent = new Intent(Activity_main_2.this,patients_today.class);
//                startActivity(intent);
                                }
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                // stop executing code by return
                return;
                // Do whatever
            } else {
                if (!accountType.equals(Activity_main_2.this.getString(R.string.account_type_helper)))

                { // pdia1.show();
                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                Intent intent = new Intent(Activity_main_2.this, patients_today.class);
                                startActivity(intent);
                                // hitApiForAppointment("http://" + address + "/fetchDoctorAppointments.php",params,client,Activity_main_2.this);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    thread.start();
                } else {


                    Intent intent = new Intent(Activity_main_2.this, patients_today.class);
                    startActivity(intent);


//                Intent intent = new Intent(Activity_main_2.this,patients_today.class);
//                startActivity(intent);
                }
            }


//            Intent intent =new Intent(Activity_main_2.this, patients_today.class);
//            startActivity(intent);

            //mTitle = "My Calender";
        }

        if (position == 4) {
            if (!accountType.equals(Activity_main_2.this.getString(R.string.account_type_helper)))

            {
                fragmentNumber = 5;
                Intent intent = new Intent(Activity_main_2.this, PaymentActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(Activity_main_2.this, "You are not authorised to use this feature", Toast.LENGTH_SHORT).show();
//                fragmentManager.beginTransaction()
//                        .replace(R.id.container, MainActivity.newInstance(position + 1))
//
//                        .commit();
            }

            //mTitle = "My Calender";
        }
            if(position == 5)
            {
                fragmentNumber = 6;
                addHelper();
            }
    }
//        else
//        {
//            Toast.makeText(Activity_main_2.this,"Please Save your user settings first to continue",Toast.LENGTH_LONG).show();
//        }
       // mTitle = "My Other Thing";
    }
    @Override
    public void onBackPressed() {
//

        if(fragmentNumber!=1)
        {
            fragmentNumber = 1;
            FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.container, MainActivity.newInstance(1))

                .commit();}
        else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                //mTitle = getString(R.string.title_section1);

                break;
            case 2:
               // mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }
    public void hitApiForAppointment(String apiAddress, RequestParams params, AsyncHttpClient client, final Context context) {


        final DatabaseHandler databaseHandler = new DatabaseHandler(context);

        try
        {

            client.post(apiAddress, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i,cz.msebera.android.httpclient. Header[] headers, byte[] bytes) {

                    try {
                        String str = new String(bytes, "UTF-8");
                        JSONArray response;
                        // JSONObject mainObject = new JSONObject(str);
                        ArrayList<String> appointmentPID;

                        response = (JSONArray) JSONValue.parse(str);
                        if(response.size()>0)
                            if(response.get(0).equals("account not verified"))
                            {
                                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Activity_main_2.this);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putBoolean("showOptionMenu", false);
                                editor.commit();

                                Intent intent = new Intent(context, AccountVerificationActivity.class);
                                context.startActivity(intent);
                                return;
                            }
                            else {
                                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Activity_main_2.this);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putBoolean("showOptionMenu", true);
                                editor.commit();

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                String date = sdf.format(new Date());
                                databaseHandler.deleteAppointments(date);
                                //if()
                                utility.saveAppointmentsTable(response, context);

                                appointmentPID  =  databaseHandler.getAppointmentsForDate(date);
                                for(int j = 0;j<appointmentPID.size();j++)
                                {
                                    Patient patient = databaseHandler.getPatient(Integer.parseInt(appointmentPID.get(j)));
                                    if(patient==null)
                                    {
                                        patient = databaseHandler.getPatientFromFirstAidId(Integer.parseInt(appointmentPID.get(j)));
                                        if(patient == null)
                                        {
                                            patient = databaseHandler.getFirstAidPatient(Integer.parseInt(appointmentPID.get(j)));
                                            if(patient.get_bmp() == null)
                                            {
                                                patient.set_bmp(PhotoHelper.getBitmapAsByteArray(BitmapFactory.decodeResource(getResources(), R.drawable.default_photo)));
                                            }

                                            databaseHandler.addPatient(patient);


                                        }
                                    }

                                }




                            }
                        else
                        {
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Activity_main_2.this);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putBoolean("showOptionMenu", true);
                            editor.commit();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            String date = sdf.format(new Date());
                            databaseHandler.deleteAppointments(date);

                        }
                        pdia1.dismiss();
                        Intent intent = new Intent(Activity_main_2.this,patients_today.class);
                        startActivity(intent);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(int i,cz.msebera.android.httpclient. Header[] headers, byte[] bytes, Throwable throwable) {
                    try { pdia1.dismiss();
Toast.makeText(Activity_main_2.this,"Internet Error",Toast.LENGTH_SHORT).show();
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onFinish() {



                    pdia1.dismiss();



                    // Toast.makeText(context,    "END", Toast.LENGTH_LONG).show();
                    //((Activity) context).recreate();

                }


            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        // return customerId[0];
    }
/*
    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }
*/
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main_2, menu);

            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }*/

  /*  @Override
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
    }*/


    //adding profile photo

//    public void addProfilePhoto(View view) throws IOException
//    {
//       // this.dispatchTakePictureIntent();
//    }
//    private void dispatchTakePictureIntent()
//    {
////        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
////        Intent intent = getIntent();
////        //media_obj mediaObj = new media_obj();
////
////        final DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
////
////
////
////        // Ensure that there's a camera activity to handle the intent
////        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
////            // Create the File where the photo should go
////            File photoFile = null;
////            try {
////                photoFile = PhotoHelper.createImageFile(0);
////
////            } catch (IOException ex) {
////                // Error occurred while creating the File
////
////            }
////
////            // Continue only if the File was successfully created
////            if (photoFile != null) {
////                takePictureIntent.putExtra("output",
////                        Uri.fromFile(photoFile));
////                profilePicPath = photoFile.getPath();
////                /*mediaObj.set_media_name(photoFile.getPath());
////                mediaObj.set_media_path(photoFile.getPath());
////*/
////
////
////
////                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
//
//
//
//            }
//        }
//    }


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//
//
//        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
//
//            DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
//            databaseHandler.updatePersonalInfo("documentPath", profilePicPath);
//            try {
//                Bitmap bitmap;
//                bitmap =BitmapFactory.decodeFile(profilePicPath);
//                FileOutputStream out = new FileOutputStream(profilePicPath
//                );
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 10, out);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//
//
//
//
//
//        }
//        utility.recreateActivityCompat(Activity_main_2.this);
//    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((Activity_main_2) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    public class TestRestoreData extends AsyncTask<Void, Void, Boolean> {




        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }


        @Override
        protected Boolean doInBackground(Void... params) {
            DatabaseHandler databaseHandler = new DatabaseHandler(Activity_main_2.this);
            List<document_obj> listDocument = databaseHandler.getAllDocuments();
            List<document_obj> listMediaFollowUp = databaseHandler.getAllMediaFollowUp();
            List<document_obj> listMedia = databaseHandler.getAllMedia();
listMedia.addAll(listDocument);
            listMedia.addAll(listMediaFollowUp);
//            for (int i = 0; i < listDocument.size(); i++) {
//                downloadFile(listDocument.get(i).get_id(), listDocument.get(i).get_doc_path());
//            }
            for (int i = 0; i < listMedia.size(); i++) {
                try {
                    if(i%40 == 0)
                        Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                downloadFile(listMedia.get(i).get_id(), listMedia.get(i).get_doc_path());
            }

            return null;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Activity_main_2.this);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("restore", false);
            editor.commit();
                pdia.dismiss();

        }

        //i: patient id
        //s: file path
        public void downloadFile(int i, String s) {
            int totalSize = 0;
            int downloadedSize = 0;
            DatabaseHandler databaseHandler = new DatabaseHandler(Activity_main_2.this);
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

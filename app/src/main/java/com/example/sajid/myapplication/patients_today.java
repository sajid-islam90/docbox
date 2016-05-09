package com.example.sajid.myapplication;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.nsd.NsdServiceInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import activity.Activity_main_2;
import activity.DisplayMessageActivity;
import activity.PatientProfileActivity;
import adapters.MyAdapter;
import objects.DataBaseEnums;
import objects.Item;
import objects.Patient;
import objects.document_obj;

public class patients_today extends AppCompatActivity {

    ArrayList<String> appointmentPID;
    private ListView listView;
    private Button button;
     ProgressDialog pdia;
    ProgressDialog progressDialogConnection;
    String accountType;
    NsdHelper mNsdHelper;
    public static final String TAG = "docbox";
    private static Handler mUpdateHandler;
    MyAdapter adapter;
    ChatConnection mConnection;
    ArrayList<Item> patientListToDisplay = new ArrayList<>();

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(patients_today.this, Activity_main_2.class);
        startActivity(intent);
       // finish();
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.e(TAG, "on create called");
        int numberAddedPatients = 0;
       // mNsdHelper = (NsdHelper)getIntent().getSerializableExtra("nsd");
        setContentView(R.layout.activity_patients_today);
        final DatabaseHandler databaseHandler = new DatabaseHandler(patients_today.this);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(patients_today.this);

        accountType  = prefs.getString(patients_today.this.getString(R.string.account_type), "");
        progressDialogConnection = new ProgressDialog(patients_today.this);
        mUpdateHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                //  mConnection.sendMessage(String.valueOf(getPatientList()));
               String chatLine = msg.getData().getString("msg");
                StringReader in = null;
                if (chatLine != null) {
                    in = new StringReader(chatLine);
                    ArrayList<ArrayList<String>> data = new ArrayList<>();

                    data= (ArrayList) JSONValue.parse(in);
                      //  patientList = (ArrayList) JSONValue.parse(in);//284


                    if(data!=null)
                    {
                        saveData(  data);

                    }

                }
                else
                    mConnection.sendMessage("thanks");


                Log.d(TAG, chatLine);

            }
        };

        if(mNsdHelper==null)
        { mNsdHelper = new NsdHelper(this);
        mNsdHelper.initializeNsd();}
        mConnection = new ChatConnection(mUpdateHandler,patients_today.this,mNsdHelper);
        final AsyncHttpClient client = new SyncHttpClient(true, 80, 443);
        listView = (ListView)findViewById(R.id.listViewPatientToday);
        button = (Button)findViewById(R.id.button2);


        String s1 = null;
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


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());
        appointmentPID  =  databaseHandler.getAppointmentsForDate(date);


        ConnectionTask connectionTask = new ConnectionTask();
        connectionTask.execute((Void) null);


        numberAddedPatients =  getPatientList();

        displayPatientList(patientListToDisplay);

        final int finalNumberAddedPatients = numberAddedPatients;
        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String i;
                        Log.d(TAG, "Appointments number "+appointmentPID.size());
                        if(appointmentPID.size()>0)
                        i = appointmentPID.get(0);
                        else
                        i="0";
                        for(int k = 0;k<appointmentPID.size();k++)
                        {
                            ArrayList<ArrayList<String>> Data = new ArrayList<ArrayList<String>>();
                            ArrayList<String>patientTable = new ArrayList<String>();
                            patientTable.add(DataBaseEnums.TABLE_PATIENT);
                            ArrayList<String> patient =  databaseHandler.composeJSONfromSQLitePatientHelper(appointmentPID.get(k), patients_today.this, accountType);
                            ArrayList<String>documentsTable = new ArrayList<String>();
                            documentsTable.add(DataBaseEnums.TABLE_DOCUMENTS);
                            ArrayList<ArrayList<String>> documentsList =  databaseHandler.composeJSONfromSQLiteDocumentsHelper(appointmentPID.get(k), patients_today.this, accountType);


                            Data.add(patientTable);
                            Data.add(patient);
                            Data.add(documentsTable);

                            Data.addAll(documentsList);
                            String s1 = null;

                            StringWriter out = new StringWriter();

                            try {
                                JSONValue.writeJSONString(Data, out);
                                s1 = out.toString();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if((patient.size()>0)||(documentsList.size()>0))
                        mConnection.sendMessage(s1);

                            if(accountType.equals(patients_today.this.getString(R.string.account_type_helper)))
                            {
                                sendDocumentFiles(Integer.parseInt(appointmentPID.get(k)));
                            }
//                            if(documentsList.size()>0)
//                            {
//                                for(int j = 0;j<documentsList.size();j++)
//                                {
//                                    ArrayList<ArrayList<String>> temp = new ArrayList<ArrayList<String>>();
//                                    ArrayList<String>document = new ArrayList<String>();
//                                    document.add(documentsList.get(j).get(4));
//                                    temp.add(document);
//                                    mConnection.sendMessage(documentsList.get(j).get(4));
//                                }
//                            }
                           // databaseHandler.updatePatient();
                            s1 = new String();}
                    }
                });
    }
    public void sendDocumentFiles( int pid)
    {
        DatabaseHandler databaseHandler = new DatabaseHandler(patients_today.this);
        ArrayList<ArrayList<String>> documentsList =  databaseHandler.composeJSONfromSQLiteDocumentsHelper(String.valueOf(pid), patients_today.this, accountType);
        if(documentsList.size()>0)
        {
            for(int j = 0;j<documentsList.size();j++)
            {
                ArrayList<ArrayList<String>> temp = new ArrayList<ArrayList<String>>();
                ArrayList<String>document = new ArrayList<String>();
                document.add(documentsList.get(j).get(4));
                temp.add(document);
                mConnection.sendMessage(documentsList.get(j).get(4));
            }
        }
    }

    public void saveData( ArrayList<ArrayList<String>> data)
    {
        String tableName = DataBaseEnums.TABLE_PATIENT;
        DatabaseHandler databaseHandler = new DatabaseHandler(patients_today.this);
        if(data.size() >2) {
            for (int i = 0; i < data.size(); i++) {


                if(data.get(i).size()==1) {
                    tableName = data.get(i).get(0);
                    continue;
                }

                    if (tableName.equals(DataBaseEnums.TABLE_PATIENT)) {
                        if(data.get(i).size()!=0)
                        savePatientTable(data.get(i));

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String date = sdf.format(new Date());
                        // getPatientList();
                        appointmentPID  =  databaseHandler.getAppointmentsForDate(date);
                        Log.e(TAG, "on restart called with patients "+getPatientList()+" "+patientListToDisplay.size());
                        adapter.updateReceiptsList(patientListToDisplay);

                    }




                else if(tableName.equals(DataBaseEnums.TABLE_DOCUMENTS)) {
                        if(data.get(i).size()!=0)
                    saveDocumentsTable(data.get(i));

                }

            }
        }
            else if (data.size() == 2) //sending doc pid and helper pid
        {
                Toast.makeText(patients_today.this, "Doctor " + data.get(0).get(0) + " local pid " + data.get(1).get(0), Toast.LENGTH_SHORT).show();
            if(accountType.equals(patients_today.this.getString(R.string.account_type_doctor)))
            {
                //ArrayList<ArrayList<String>> documentsList =  databaseHandler.composeJSONfromSQLiteDocumentsHelper(data.get(1).get(0), patients_today.this, accountType);

                databaseHandler.updatePatient(DataBaseEnums.KEY_SYNC_STATUS, "5", data.get(0).get(0));
                sendDocumentFiles(Integer.parseInt(data.get(0).get(0)));
                databaseHandler.updateAllDocuments(Integer.parseInt(data.get(0).get(0)), String.valueOf(5));

            }
            else {
                databaseHandler.updatePatient(DataBaseEnums.KEY_SYNC_STATUS, "1", data.get(1).get(0));
                Log.e(TAG, "patient data sent");
                databaseHandler.updateAllDocuments(Integer.parseInt(data.get(1).get(0)), String.valueOf(1));
                Log.e(TAG, "document data sent for pid " + Integer.parseInt(data.get(1).get(0)));
                if(!databaseHandler.checkDoctorHelperPatientMapping(Integer.parseInt(data.get(0).get(0)), Integer.parseInt(data.get(1).get(0))))
                databaseHandler.mapDoctorHelperPatients(Integer.parseInt(data.get(0).get(0)), Integer.parseInt(data.get(1).get(0)));
                mConnection.sendMessage("thanks");
            }
               // mConnection.sendMessage("thanks");
            }
        else if(data.size() ==1)//sending document with path
        {
            Toast.makeText(patients_today.this, "Got the document", Toast.LENGTH_SHORT).show();
            mConnection.sendMessage("thanks");
        }




    }

    public void saveDocumentsTable(ArrayList<String>documentsData)
    {


        DatabaseHandler databaseHandler = new DatabaseHandler(patients_today.this);


        document_obj documentObj = new document_obj();
        documentObj.set_id(Integer.parseInt(String.valueOf(documentsData.get(1))));
        documentObj.set_date(String.valueOf(documentsData.get(2)));
        documentObj.set_doc_name(String.valueOf(documentsData.get(3)));
        documentObj.set_doc_path(String.valueOf(documentsData.get(4)));
         Patient patient = databaseHandler.getPatient(Integer.parseInt(String.valueOf(documentsData.get(1))));

        if(accountType.equals(patients_today.this.getString(R.string.account_type_doctor))) {

            File storageDir =
                    new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES), "Patient Manager/"+Integer.parseInt(String.valueOf(documentsData.get(1)))+"/Documents");
            if(!storageDir.exists()) {

                storageDir.mkdir();
            }


            if (databaseHandler.getSingleDocument(documentObj.get_id(), documentObj.get_doc_path()).get_date() == null) {
                databaseHandler.addDocument(documentObj);

                databaseHandler.updatePatient(DataBaseEnums.KEY_SYNC_STATUS,"5", String.valueOf(documentObj.get_id()));
                databaseHandler.updateDocument(documentObj, "5");
            } else
                databaseHandler.updateDocument(documentObj, "5");
        }
        else
        {
            if (databaseHandler.getMappedHelperDocPath(documentObj.get_doc_path()).compareTo("")==0) {

                int helperPid = databaseHandler.checkDoctorInHelperPatientMapping(documentObj.get_id());

                File storageDir =
                        new File(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES), "Patient Manager/"+helperPid+"/Documents");
                if(!storageDir.exists()) {

                    storageDir.mkdir();
                }
                File newFile = new File(documentObj.get_doc_path());
                String pathHelper = storageDir.getPath()+"/"+newFile.getName();
                databaseHandler.mapDoctorHelperDocuments(documentObj.get_doc_path(), pathHelper);
                documentObj.set_id(helperPid);
                documentObj.set_doc_path(pathHelper);
                databaseHandler.addDocument(documentObj);
                databaseHandler.updatePatient(DataBaseEnums.KEY_SYNC_STATUS, "1", String.valueOf(documentObj.get_id()));
                databaseHandler.updateDocument(documentObj, "1");
            } else {

                int helperPid = databaseHandler.checkDoctorInHelperPatientMapping(documentObj.get_id());
                String helperDocPath = databaseHandler.getMappedHelperDocPath(documentObj.get_doc_path());
                documentObj.set_doc_path(helperDocPath);
                documentObj.set_id(helperPid);
                databaseHandler.updateDocument(documentObj, "1");
            }
        }

            // downloadFile(documentObj.get_id(),documentObj.get_doc_path(), String.valueOf(databaseHandler.getPersonalInfo().get_customerId()),context);
        }







    public void savePatientTable(ArrayList<String>patientData)
    {
             ArrayList<ArrayList<String>> list = new ArrayList<>();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");Calendar c = Calendar.getInstance();

        String formattedDate = df.format(c.getTime());
            DatabaseHandler databaseHandler = new DatabaseHandler(patients_today.this);
            Patient patient = new Patient();
            patient.set_name(patientData.get(2));
            patient.set_opd_ipd(patientData.get(3));
            patient.set_diagnosis(patientData.get(4));
            patient.set_age(patientData.get(5));
            patient.set_gender(patientData.get(6));
            patient.set_contact_number(patientData.get(7));
            patient.set_height(patientData.get(8));
            patient.set_weight(patientData.get(9));
            patient.set_last_seen_date(formattedDate);
            byte[] b = Base64.decode(patientData.get(11), Base64.DEFAULT);
            patient.set_bmp(b);
            long pid = 0;
            if(accountType.equals(patients_today.this.getString(R.string.account_type_doctor)))

            {
                if(Integer.parseInt(patientData.get(0))!=0)
                {

                    patient.set_id(Integer.parseInt(patientData.get(0)));
                    databaseHandler.updatePatient(patient, 5);
                    pid= Integer.parseInt(patientData.get(0));
                }
                else
                {
                    pid =  databaseHandler.addPatient(patient);
                    patient.set_id(Integer.parseInt(String.valueOf(pid)));
                    databaseHandler.updatePatient(patient, 5);
                }

            }
            else
            {
                if(databaseHandler.checkDoctorInHelperPatientMapping(Integer.parseInt(patientData.get(1)))!=0)
                {

                    patient.set_id(databaseHandler.checkDoctorInHelperPatientMapping(Integer.parseInt(patientData.get(1))));
                    databaseHandler.updatePatient(patient,1);
                    pid= patient.get_id();
                }
                else

                {
                    pid =  databaseHandler.addPatient(patient);
                    patient.set_id((int)pid);
                    databaseHandler.updatePatient(patient,1);
                    databaseHandler.mapDoctorHelperPatients((Integer.parseInt(patientData.get(1))), (int) pid);
                }

            }

//                    patient = databaseHandler.getPatient((int)pid);
//patient.set_id();
//                    databaseHandler.updatePatient(patient, 1);
            // temp book appoint ment is commented out
                            if(!accountType.equals(patients_today.this.getString(R.string.account_type_helper)))
                            {  Resources res = patients_today.this.getResources();
                                final String address = res.getString(R.string.action_server_ip_address);
                                final RequestParams params = new RequestParams();
                                String patientJson = databaseHandler.composeJSONfromSQLitePatient(String.valueOf(pid), patients_today.this);
                                params.put("usersJSON", patientJson);
                                utility.sync("http://" + address + "/insertPatient.php", params, patients_today.this);
                                utility.bookAppointmentToday((int)pid,patients_today.this);
                             }
        ArrayList<String> doctorPid = new ArrayList<>();

        ArrayList<String> helperPid = new ArrayList<>();

        if(accountType.equals(patients_today.this.getString(R.string.account_type_helper)))
        {
            utility.bookAppointmentTodayLocally((int) pid, patients_today.this);
            doctorPid.add(patientData.get(1));
            helperPid.add(String.valueOf(pid));
        }
        else
        {
            doctorPid.add(String.valueOf(pid));
            helperPid.add(patientData.get(1));
        }


            list.add(doctorPid);
            list.add(helperPid);
            String s1 = null;

            StringWriter out = new StringWriter();

            try {
                JSONValue.writeJSONString(list, out);
                s1 = out.toString();

            } catch (IOException e) {
                e.printStackTrace();
            }
            mConnection.sendMessage(s1);


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(accountType.equals(patients_today.this.getString(R.string.account_type_doctor)))
        getMenuInflater().inflate(R.menu.menu_patients_today, menu);
        else
            getMenuInflater().inflate(R.menu.menu_patients_today_helper, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        final DatabaseHandler databaseHandler = new DatabaseHandler(patients_today.this);
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            String s1 = null;

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
            final String  address =  getResources().getString(R.string.action_server_ip_address);
            pdia = new ProgressDialog(patients_today.this);
            pdia.setMessage("Fetching appointments please wait");
            pdia.setCanceledOnTouchOutside(false);
            pdia.show();

            Thread thread = new Thread(){
                @Override
                public void run() {
                    try {

                        hitApiForAppointment("http://" + address + "/fetchDoctorAppointments.php",params,client,patients_today.this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            thread.start();


            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date = sdf.format(new Date());
            // getPatientList();
            appointmentPID  =  databaseHandler.getAppointmentsForDate(date);
            Log.e(TAG, "on restart called with patients " + getPatientList() + " " + patientListToDisplay.size());
            adapter.updateReceiptsList(patientListToDisplay);


//            mNsdHelper.tearDown();
//            mConnection.tearDown();
            return true;
        }
        if (id == R.id.action_add) {
            Intent intent = new Intent(patients_today.this, DisplayMessageActivity.class);
            intent.putExtra("parent", "patients_today");
            startActivity(intent);
//finish();

        }

        if (id == R.id.action_add_helper) {
            LayoutInflater li = LayoutInflater.from(patients_today.this);
            final RequestParams params = new RequestParams();
            Resources res = patients_today.this.getResources();
            final String address = res.getString(R.string.action_server_ip_address);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(patients_today.this);
            boolean helper  = prefs.getBoolean("helperAdded", false);
            if(!helper)
            {
                final View promptsView = li.inflate(R.layout.sms_text, null);
                final TextView textView = (TextView)promptsView.findViewById(R.id.sms_Edit_Text);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        patients_today.this);

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
        if (id == R.id.action_connect) {
            String message = "";
            boolean wifiEnable = false;
            ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (accountType.equals(patients_today.this.getString(R.string.account_type_doctor)))
                message = "Create a WiFi hotspot network";
            if (accountType.equals(patients_today.this.getString(R.string.account_type_helper)))
            {
                message = "Connect to a WiFi network";
            }
            WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            Method[] wmMethods = wifi.getClass().getDeclaredMethods();
            for (Method method: wmMethods) {
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


            if ((!mWifi.isConnected())&&(!wifiEnable) ){
                // Internet Connection is not present
                new AlertDialog.Builder(patients_today.this)
                        .setTitle("Not connected to any WiFi network")
                        .setMessage("Enable WiFi ")
                        .setPositiveButton(message, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (accountType.equals(patients_today.this.getString(R.string.account_type_doctor)))
                                startActivityForResult(new Intent(Settings.ACTION_WIRELESS_SETTINGS), 0);
                                if (accountType.equals(patients_today.this.getString(R.string.account_type_helper)))
                                {
                                    startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), 0);
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                // stop executing code by return

                // Do whatever
            }
            else {
                if (accountType.equals(patients_today.this.getString(R.string.account_type_doctor))) {
                    mNsdHelper.tearDown();
                    mConnection.tearDown();
                    Intent intent = getIntent();
                    startActivity(intent);
                    finish();


                } else {
                    Intent intent = getIntent();
                    startActivity(intent);
                    finish();


                }
            }

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {

        Log.e(TAG, "on stop called");
       // mNsdHelper.tearDown();
        //mConnection.tearDown();
        super.onStop();
    }

    @Override
    protected void onPause() {
        Log.e(TAG, "on pause called");
        if (mNsdHelper != null) {
           // mNsdHelper.stopDiscovery();

        }
        super.onPause();
    }

    @Override
    protected void onRestart() {

        DatabaseHandler databaseHandler =new DatabaseHandler(patients_today.this);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());
       // getPatientList();
        appointmentPID  =  databaseHandler.getAppointmentsForDate(date);
        Log.e(TAG, "on restart called with patients "+getPatientList()+" "+patientListToDisplay.size());
        adapter.updateReceiptsList(patientListToDisplay);
        super.onRestart();
    }

    @Override
    protected void onResume() {



        Log.e(TAG, "on resume called");
        getPatientList();
        adapter.updateReceiptsList(patientListToDisplay);
        super.onResume();

//        if(accountType.equals(patients_today.this.getString(R.string.account_type_doctor)))
//        {
//            if(mConnection.getLocalPort() > -1) {
//
//                Log.d(TAG, "Registering service");
//                mNsdHelper.registerService(mConnection.getLocalPort());
//            } else {
//                Log.d(TAG, "ServerSocket isn't bound.");
//            }
//        }
//        else {
//        if(!accountType.equals(patients_today.this.getString(R.string.account_type_doctor))) {
//            NsdServiceInfo service = mNsdHelper.getChosenServiceInfo();
//
//            if (service != null) {
//                Log.d(TAG, "Connecting.");
//                mConnection = new ChatConnection(mUpdateHandler,patients_today.this);
//                mConnection.connectToServer(service.getHost(),
//                        service.getPort());
//            } else {
//                Log.d(TAG, "No service to connect to!");
//            }
//
//
//
//        }
    }


    public int getPatientList()
    {

        DatabaseHandler dbHandler = new DatabaseHandler(patients_today.this);
        patientListToDisplay .clear();
        Bitmap image = null;
        String name = null;
        String diagnosis = null;
        int patient_id = 0;
        byte[] bmpImage ;
        String lastVisit;
        Item nameImage = new Item();
        ArrayList<Item> nameWithImage = new ArrayList<Item>();
        Patient patient ;
        Resources res = patients_today.this.getResources();
        final String address = res.getString(R.string.action_server_ip_address);
        final RequestParams params = new RequestParams();
        List<Patient> patientList = new ArrayList<>();
        for(int i = 0;i <appointmentPID.size();i++)
        {
            patient =null;
           patient =  dbHandler.getPatient(Integer.parseInt(appointmentPID.get(i)));

            if((patient==null))
            {
                patient = dbHandler.getPatientFromFirstAidId(Integer.parseInt(appointmentPID.get(i)));
                if((patient == null))
                {
                    try {
                        patient = dbHandler.getFirstAidPatient(Integer.parseInt(appointmentPID.get(i)));
                        patient.set_bmp(PhotoHelper.getBitmapAsByteArray(BitmapFactory.decodeResource(patients_today.this.getResources(), R.drawable.default_photo)));
                        if(accountType.equals(patients_today.this.getString(R.string.account_type_doctor))) {
                            long patientId = dbHandler.addPatient(patient);
                            utility.addFieldsToPatient((int) patientId, patients_today.this);
                            String patientJson = dbHandler.composeJSONfromSQLitePatient(String.valueOf(patientId), patients_today.this);
                            params.put("usersJSON", patientJson);
                            utility.sync("http://" + address + "/insertPatient.php", params, patients_today.this);
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            patientList.add(patient);
        }

        if(patientList.size() !=0) {
            for (int i = 0; i < patientList.size(); i++) {
                name = patientList.get(i).get_name();
                bmpImage = patientList.get(i).get_bmp();
                diagnosis = patientList.get(i).get_diagnosis();
                patient_id = patientList.get(i).get_id();
                lastVisit = patientList.get(i).get_last_seen_date();


                if(bmpImage != null) {

                    image = BitmapFactory.decodeByteArray(bmpImage, 0, bmpImage.length);
                }

                nameImage.setTitle(name);

                nameImage.setBmp(image);
                nameImage.setDiagnosis(diagnosis);
                nameImage.setDate(lastVisit);
                nameWithImage.add(i, nameImage);
                nameImage.setPatient_id(patient_id);
                nameImage = new Item();

            }
            //pdia.dismiss();
            patientListToDisplay.addAll(nameWithImage);
            //displayPatientList(nameWithImage);


        }
        return nameWithImage.size();
    }
    public void displayPatientList(final ArrayList<Item> patientList)
    {

       /* MainActivityList adapter1 = new
                MainActivityList(MainActivity.this, patientList, imageId);*/
        Collections.reverse(patientList);
    //    MyAdapter adapter = new MyAdapter(patients_today.this,patientList);
        adapter = new MyAdapter(patients_today.this,patientListToDisplay);

        // ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, patientList.toArray()) ;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DatabaseHandler dbHandle = new DatabaseHandler(patients_today.this);
                Patient patient = new Patient();
                patient = dbHandle.getSearchPatient(position, patientList);
                Intent intent = new Intent(patients_today.this, PatientProfileActivity.class);
                intent.putExtra("id", patient.get_id());
                intent.putExtra("parent", "patients_today");
                patients_today.this.startActivity(intent);
            }
        });
        listView.setAdapter(adapter);


    }
//    public Patient getPatientToSendToDoctor(int pid)
//    {
//
//    }



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
                                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(patients_today.this);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putBoolean("showOptionMenu", false);
                                editor.commit();

                                Intent intent = new Intent(context, AccountVerificationActivity.class);
                                context.startActivity(intent);
                            }
                            else {
                                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(patients_today.this);
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
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(patients_today.this);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putBoolean("showOptionMenu", true);
                            editor.commit();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            String date = sdf.format(new Date());
                            databaseHandler.deleteAppointments(date);

                        }
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String date = sdf.format(new Date());
                        appointmentPID  =  databaseHandler.getAppointmentsForDate(date);
                        Log.e(TAG, "on restart called with patients " + getPatientList() + " " + patientListToDisplay.size());
                        adapter.updateReceiptsList(patientListToDisplay);
//                        Intent intent = new Intent(patients_today.this,patients_today.class);
//                        startActivity(intent);
//                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(int i,cz.msebera.android.httpclient. Header[] headers, byte[] bytes, Throwable throwable) {
                    try { pdia.dismiss();
                        Toast.makeText(patients_today.this, "Internet Error", Toast.LENGTH_SHORT).show();
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onFinish() {


                    pdia.dismiss();



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
    public class ConnectionTask extends AsyncTask<Void, Void, Boolean> {


        private  ProgressDialog progressDialog;

        public ConnectionTask() {

            progressDialog = new ProgressDialog(patients_today.this);
        }

        @Override
        protected void onPreExecute() {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {

                        progressDialog.setMessage("Connecting please wait");
                        progressDialog.setCanceledOnTouchOutside(false);
//    progressDialog.show();
}
catch (Exception e)
{
    progressDialog.dismiss();
    e.printStackTrace();
}

                }
            });
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

try {

    if (accountType.equals(patients_today.this.getString(R.string.account_type_helper))) {
        //  progressDialogConnection.setMessage("Connecting to your doctor please wait");
        mNsdHelper.discoverServices();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    } else {
        // mNsdHelper. initializeRegistrationListener();
//        Log.d(TAG, "Registering service"); //28-4
        if (mConnection.getLocalPort() > -1) {
            Log.e(TAG, "Registering service"); //28-4
           if( mNsdHelper.mService==null) {
               Log.e(TAG, "Registering new service");
               mNsdHelper.reRegisterService(mConnection.getLocalPort());

           }
            else
           {
               Log.e(TAG, "Already Registering service");
           }
        } else {
            Log.d(TAG, "ServerSocket isn't bound.");
        }

        // progressDialogConnection.setMessage("Connecting to your helper please wait");
    }
}
catch (Exception e)
{
    progressDialog.hide();
    Log.e(TAG, "error in task "+e.toString());
}

            return null;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if(accountType.equals(patients_today.this.getString(R.string.account_type_helper)))
            {
                NsdServiceInfo service = mNsdHelper.getChosenServiceInfo();

                if (service != null) {
                    Log.d(TAG, "Connecting.");
                    mConnection.connectToServer(service.getHost(),
                            service.getPort());

                } else {
                    Log.d(TAG, "No service to connect to!");
                }
            }
            progressDialog.hide();

        }


        public void doSomething()
        {

        }

    }


}

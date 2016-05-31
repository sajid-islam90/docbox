package utilityClasses;
//UTILITY CLASS
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.widget.Toast;

import com.elune.sajid.myapplication.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import activity.Other_Notes_Activity;
import activity.Treatment_Activity;
import activity.View_Media_notes_grid;


import objects.*;

/**
 * Created by sajid on 3/28/2015.
 */
public class utility {
    /**
     Current Activity instance will go through its lifecycle to onDestroy() and a new instance then created after it.
     */
    @SuppressLint("NewApi")
    public static final void recreateActivityCompat(final Activity a) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            a.recreate();
        } else {
            final Intent intent = a.getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            a.finish();
            a.overridePendingTransition(0, 0);

            a.startActivity(intent);
            a.overridePendingTransition(0, 0);
        }
    }
    public static boolean valuePresentInStringArray(String value,String[] specialities)
    {
        boolean present = false;
        for (String speciality : specialities) {
            if (value.equals(speciality)) {
                present = true;
                break;
            }
        }

        return present;
    }

    public static void bookAppointmentToday(int pid ,Context context)
    {
        final ArrayList<String> list = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        final Date date = new Date();
        final ArrayList<ArrayList<String>> notesList;
        notesList = new ArrayList<ArrayList<String>>();
        String dayOfTheWeek = sdf.format(date);
        final RequestParams paramsDocuments = new RequestParams();
        dayOfTheWeek = dayOfTheWeek.toLowerCase();
        ArrayList<String> settings = new ArrayList<>();
        DatabaseHandler databaseHandler = new DatabaseHandler(context);
        settings = databaseHandler.getAppointmentSettings(dayOfTheWeek);
        if (settings.size() > 0) {
            Patient patient = databaseHandler.getPatient(pid);
            list.add(String.valueOf(databaseHandler.getCustomerId()));
            list.add(String.valueOf(patient.get_first_aid_id()));
            list.add(sdf1.format(date));
            list.add(settings.get(1));
            list.add((settings.get(2)));
            notesList.add(list);
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);


            String hash = sharedPref.getString(context.getString(R.string.hash_code), "");
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
            utility.sync("http://" + address + "/bookAppointment.php", paramsDocuments, context);
            // msgBookedPatient(appointmentDate[0],finalSettings.get(1),finalSettings.get(2));
            // list.add(settings.get())

            databaseHandler.updatePatient(patient,0);
        } else {
            Toast.makeText(context, "You are unavailable on this day please choose another date", Toast.LENGTH_SHORT).show();
        }




    }


    public static final Patient cursorToPatient(Cursor cursor)
    {
        Patient patient = new Patient(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DataBaseEnums.KEY_ID))),
                cursor.getString(cursor.getColumnIndex(DataBaseEnums.KEY_NAME)),
                cursor.getString(cursor.getColumnIndex(DataBaseEnums.KEY_AGE)),cursor.getString(cursor.getColumnIndex(DataBaseEnums.KEY_GENDER)),
                cursor.getString(cursor.getColumnIndex(DataBaseEnums.KEY_HEIGHT)),

                cursor.getBlob(cursor.getColumnIndex(DataBaseEnums.KEY_BMP)),
                cursor.getString(cursor.getColumnIndex(DataBaseEnums.KEY_DIAGNOSIS)));
        patient.set_last_seen_date(cursor.getString(cursor.getColumnIndex(DataBaseEnums.KEY_DATE_LAST_VISIT)));
        patient.set_weight(cursor.getString(cursor.getColumnIndex(DataBaseEnums.KEY_WEIGHT)));
        patient.set_opd_ipd(cursor.getString(cursor.getColumnIndex(DataBaseEnums.KEY_OPD_IPD)));
        patient.set_photoPath(cursor.getString(cursor.getColumnIndex(DataBaseEnums.KEY_PHOTO_PATH)));
        patient.set_address(cursor.getString(cursor.getColumnIndex(DataBaseEnums.KEY_ADDRESS)));
        patient.set_contact_number(cursor.getString(cursor.getColumnIndex(DataBaseEnums.KEY_CONTACT)));
        patient.set_ocupation(cursor.getString(cursor.getColumnIndex(DataBaseEnums.KEY_OCCUPATION)));
        patient.set_email(cursor.getString(cursor.getColumnIndex(DataBaseEnums.KEY_EMAIL)));
        patient.set_next_follow_up_date(cursor.getString(cursor.getColumnIndex(DataBaseEnums.KEY_DATE_NEXT_FOLLOW_UP)));
        if(cursor.getString(cursor.getColumnIndex(DataBaseEnums.KEY_FIRST_AID_ID))!=null)
        patient.set_first_aid_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DataBaseEnums.KEY_FIRST_AID_ID))));
        else
            patient.set_first_aid_id(patient.get_id());

        return patient;
    }

    public static final document_obj cursorToDocument(Cursor cursor)
    {
        document_obj documentObj = new document_obj();
        documentObj.set_date(cursor.getString(cursor.getColumnIndex(DataBaseEnums.KEY_DATE)));
        documentObj.set_doc_name(cursor.getString(cursor.getColumnIndex(DataBaseEnums.KEY_DOC_NAME)));
        documentObj.set_bmp(cursor.getBlob(cursor.getColumnIndex(DataBaseEnums.KEY_BMP)));
        documentObj.set_doc_path(cursor.getString(cursor.getColumnIndex(DataBaseEnums.KEY_DOC_PATH)));
        documentObj.set_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DataBaseEnums.KEY_ID))));





        return documentObj;
    }


    public static  void hitApiForAppointment(String apiAddress, RequestParams params,AsyncHttpClient client, final Context context, final ProgressDialog pdia) {


        final DatabaseHandler databaseHandler = new DatabaseHandler(context);

        try
        {

            client.post(apiAddress, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i,cz.msebera.android.httpclient. Header[] headers, byte[] bytes) {

                    try {
                        String str = new String(bytes, "UTF-8");
                        // JSONObject mainObject = new JSONObject(str);
                        JSONArray response;
                        response = (JSONArray) JSONValue.parse(str);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String date = sdf.format(new Date());
                        databaseHandler.deleteAppointments(date);
                        saveAppointmentsTable(response,context);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(int i,cz.msebera.android.httpclient. Header[] headers, byte[] bytes, Throwable throwable) {
                    try {

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

    public static void syncData(String apiAddress ,  RequestParams params, final Context context, final ProgressDialog progressDialog , AsyncHttpClient client)
    {
        //AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
        client.post(apiAddress, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {

                try {
                    String str = new String(bytes, "UTF-8");
                    if (context == null) {
                        return;
                    }
                    // ((Activity) context).recreate();
                    //progressDialog.hide();
                    Toast.makeText(context, "MySQL DB has been informed about Sync activity", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //controller.updateSyncStatus(obj.get("id").toString(),obj.get("status").toString());

            }

            @Override
            public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {
                try {
                    String str = new String(bytes, "UTF-8");
                    progressDialog.hide();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Toast.makeText(context, "MySQL DB has not been informed about Sync activity", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFinish() {
                Toast.makeText(context, "END", Toast.LENGTH_LONG).show();
            }


        });

    }

    public static void syncData(String apiAddress ,  RequestParams params, final Context context, final ProgressDialog progressDialog,
                                final String pid, final int uploadFilesLength )
    {
        final AsyncHttpClient client = new SyncHttpClient(true, 80, 443);

        final DatabaseHandler databaseHandler = new DatabaseHandler(context);
        client.post(apiAddress, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {

                try {

                    databaseHandler.updatePatient(DataBaseEnums.KEY_SYNC_STATUS, "1", pid);
                    databaseHandler.deletePatient();

                    if (context == null) {
                        return;
                    }
                    if(uploadFilesLength==0){
                        ((Activity) context).recreate();
                    }
//
//
// ((Activity) context).recreate();
//                   // progressDialog.hide();
                  //  Toast.makeText(context, "All Patient Data", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //controller.updateSyncStatus(obj.get("id").toString(),obj.get("status").toString());

            }

            @Override
            public void onFailure(int statusCode,cz.msebera.android.httpclient. Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    //String str = new String(bytes, "UTF-8");
                    progressDialog.hide();
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                Toast.makeText(context, "MySQL DB has not been informed about Sync activity", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFinish() {

                databaseHandler.deletePatient();
                String DocumentsJson = databaseHandler.composeJSONfromSQLiteDocuments(pid,context);
                String notesJson = databaseHandler.composeJSONfromSQLiteNotes(pid, DataBaseEnums.TABLE_NOTES, context);
                String DiagnosisJson = databaseHandler.composeJSONfromSQLiteNotes(pid, DataBaseEnums.TABLE_DIAGNOSIS,context);
                String TreatmentJson = databaseHandler.composeJSONfromSQLiteNotes(pid, DataBaseEnums.TABLE_TREATMENT,context);
                String OtherJson = databaseHandler.composeJSONfromSQLiteNotes(pid, DataBaseEnums.TABLE_OTHER,context);
                String OtherFollowUpJson = databaseHandler.composeJSONfromSQLiteNotes(pid, DataBaseEnums.TABLE_OTHER_FOLLOW_UP,context);
                String FollowUpJson = databaseHandler.composeJSONfromSQLiteNotes(pid, DataBaseEnums.TABLE_FOLLOW_UP,context);
                String MediaFollowUpJson = databaseHandler.composeJSONfromSQLiteNotes(pid, DataBaseEnums.TABLE_MEDIA_FOLLOW_UP,context);
                String MediaJson = databaseHandler.composeJSONfromSQLiteNotes(pid, DataBaseEnums.TABLE_MEDIA,context);
                RequestParams paramsDocuments = new RequestParams();
                RequestParams paramsDiagnosis = new RequestParams();
                RequestParams paramsTreatment = new RequestParams();
                RequestParams paramsNotes = new RequestParams();
                RequestParams paramsFollowUp = new RequestParams();
                RequestParams paramsOther = new RequestParams();
                RequestParams paramsOtherFollowUp = new RequestParams();
                RequestParams paramsMedia = new RequestParams();
                RequestParams paramsMediaFollowUp = new RequestParams();


                paramsDocuments.put("documentsJSON", DocumentsJson);
                paramsDiagnosis.put("diagnosisJSON", DiagnosisJson);
                paramsTreatment.put("treatmentJSON", TreatmentJson);
                paramsNotes.put("NotesJSON", notesJson);
                paramsFollowUp.put("FollowUpJSON", FollowUpJson);
                paramsOther.put("OtherJSON", OtherJson);
                paramsOtherFollowUp.put("OtherFollowUpJSON", OtherFollowUpJson);
                paramsMedia.put("MediaJSON", MediaJson);
                paramsMediaFollowUp.put("MediaFollowUpJSON", MediaFollowUpJson);
                // sync("http://docbox.co.in/sajid/insertDocuments.php", params, context);
                String address = "docbox.co.in/sajid";//context.getResources().getString(R.string.action_server_ip_address);
                syncDataSync("http://" + address + "/insertDocuments.php", paramsDocuments, context);
                syncDataSync("http://" + address + "/insertDiagnosis.php", paramsDiagnosis, context);
                syncDataSync("http://" + address + "/insertTreatment.php", paramsTreatment, context);
                syncDataSync("http://" + address + "/insertNotes.php", paramsNotes, context);
                syncDataSync("http://" + address + "/insertFollowUp.php", paramsFollowUp, context);
                syncDataSync("http://" + address + "/insertOther.php", paramsOther, context);
                syncDataSync("http://" + address + "/insertOtherFollowUp.php", paramsOtherFollowUp, context);
                syncDataSync("http://" + address + "/insertMedia.php", paramsMedia, context);
                syncDataSync("http://" + address + "/insertMediaFollowUp.php", paramsMediaFollowUp, context);
                //((Activity) context).recreate();
                progressDialog.hide();
            }


        });

    }


    public static void sync(String apiAddress, RequestParams params, final Context context) {
        final AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);

        final DatabaseHandler databaseHandler = new DatabaseHandler(context);

        try
        {

        client.post(apiAddress, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {

                try {
                    String str = new String(bytes, "UTF-8");


                    //System.out.print("a");


                    if (context == null) {
                        return;
                    }
                    // ((Activity) context).recreate();

                    //Toast.makeText(context, "MySQL DB has been informed about Sync activity", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //controller.updateSyncStatus(obj.get("id").toString(),obj.get("status").toString());

            }

            @Override
            public void onFailure(int i,cz.msebera.android.httpclient. Header[] headers, byte[] bytes, Throwable throwable) {
                try {
                    String str = new String(bytes, "UTF-8");
                    ((AppCompatActivity)(context)).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context,"Error Please check internet connection and try again",Toast.LENGTH_LONG);
                        }
                    });
                   // Toast.makeText(context, "MySQL DB has not been informed about Sync activity", Toast.LENGTH_LONG).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFinish() {
                // Toast.makeText(context,    "END", Toast.LENGTH_LONG).show();
                //((Activity) context).recreate();

            }


        });
    }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }


    public static void syncDataSync(String apiAddress, RequestParams params, final Context context) {
        final AsyncHttpClient client = new SyncHttpClient(true, 80, 443);

        final DatabaseHandler databaseHandler = new DatabaseHandler(context);

        try
        {

            client.post(apiAddress, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {

                    try {
                        String str = new String(bytes, "UTF-8");


                        //System.out.print("a");


                        if (context == null) {
                            return;
                        }
                        // ((Activity) context).recreate();

                        //Toast.makeText(context, "MySQL DB has been informed about Sync activity", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //controller.updateSyncStatus(obj.get("id").toString(),obj.get("status").toString());

                }

                @Override
                public void onFailure(int i,cz.msebera.android.httpclient. Header[] headers, byte[] bytes, Throwable throwable) {
                    try {
                        String str = new String(bytes, "UTF-8");
                        ((AppCompatActivity)(context)).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context,"Error Please check internet connection and try again",Toast.LENGTH_LONG);
                            }
                        });
                        // Toast.makeText(context, "MySQL DB has not been informed about Sync activity", Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onFinish() {
                    // Toast.makeText(context,    "END", Toast.LENGTH_LONG).show();
                    //((Activity) context).recreate();

                }


            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    public static final ArrayList<String> cursorToPatientArray(Cursor cursor,String customerId)
    {

        ArrayList<String> list = new ArrayList<String>();
        list.add(customerId);
        list.add(cursor.getString(cursor.getColumnIndex("id")));
        list.add(cursor.getString(cursor.getColumnIndex("name")));
        list.add(cursor.getString(cursor.getColumnIndex(DataBaseEnums.KEY_OPD_IPD)));
        list.add(cursor.getString(cursor.getColumnIndex("diagnosis")));
        list.add(cursor.getString(cursor.getColumnIndex("age")));
        list.add(cursor.getString(cursor.getColumnIndex("gender")));
        list.add(cursor.getString(cursor.getColumnIndex("contactNumber")));
        list.add(cursor.getString(cursor.getColumnIndex("height")));
        list.add(cursor.getString(cursor.getColumnIndex("weight")));
        String syncStatus = cursor.getString(cursor.getColumnIndex(DataBaseEnums.KEY_SYNC_STATUS));
        if(syncStatus.equals("0")||syncStatus.equals("5"))
        list.add("1");
        else if(syncStatus.equals("3"))
        {
            list.add("3");
        }

        byte[] a = cursor.getBlob(cursor.getColumnIndex("bitmapBLOB"));

        String ab = Base64.encodeToString(a, Base64.DEFAULT);
        byte[] b = Base64.decode(ab,Base64.DEFAULT);

       list.add(ab);
        if(cursor.getString(cursor.getColumnIndex(DataBaseEnums.KEY_FIRST_AID_ID))==null)
        list.add("0");
        else
            list.add(cursor.getString(cursor.getColumnIndex(DataBaseEnums.KEY_FIRST_AID_ID)));



        return list;
    }

    public static final ArrayList<String>cursorToPersonalInfoArrayList(Cursor cursor)
    {
        ArrayList<String> list = new ArrayList<String>();
        ArrayList<String>photoPath = new ArrayList<>();
        for (int i = 0;i<cursor.getColumnCount();i++) {

            if(cursor.getColumnName(i).equals(DataBaseEnums.KEY_DOC_PATH))
            {
                String path1 = cursor.getString(cursor.getColumnIndex(DataBaseEnums.KEY_DOC_PATH));
                if((path1!=null)){
                    File nFile =new File(path1);

                    list.add(nFile.getName());
                    photoPath.add(path1);
                }
                else
                {
                    list.add("");
                    photoPath.add("");
                }

            }
            else  if(cursor.getColumnName(i).equals(DataBaseEnums.KEY_MAP_SNAPSHOT_PATH)) {
                String path = cursor.getString(cursor.getColumnIndex(DataBaseEnums.KEY_MAP_SNAPSHOT_PATH));
                File nFile = new File(path);

                list.add(nFile.getName());
            }
            else {
                if((cursor.getString(i)==null))
                    list.add("");
                else {
                    if (cursor.getString(i).equals(""))
                    {

                    }
                    list.add(cursor.getString(i));
                }
            }
        }

      //  byte[] a = PhotoHelper.getBitmapAsByteArray(bitmap);

//        String ab = Base64.encodeToString(a, Base64.DEFAULT);
//        ab.replaceAll("\n","\\n");
//        byte[] b = Base64.decode(ab, Base64.DEFAULT);
       // list.add(ab);
      //  photoPath.add(path);
        //list.add(cursor.getString(cursor.getColumnIndex(D)));
        //FTPHelper.Dowork(photoPath,"",cursor.getString(cursor.getColumnIndex(DataBaseEnums.KEY_ID)),c);
        return list;

    }


    public static final ArrayList<String> cursorToSimpleArraylist(Cursor cursor)
    {

        ArrayList<String> list = new ArrayList<String>();

        for (int i = 0;i<cursor.getColumnCount();i++)

            if(cursor.getString(i)!=null)
            list.add(cursor.getString(i));
        else
            list.add("");


        // list.add(cursor.getString(cursor.getColumnIndex("bitmapBLOB")));



        return list;
    }



    public static final ArrayList<String> cursorToArrayList(Cursor cursor,String customerId)
    {

        ArrayList<String> list = new ArrayList<String>();
        list.add(customerId);
        for (int i = 0;i<cursor.getColumnCount();i++)

        {

            if (cursor.getColumnName(i).equals(DataBaseEnums.KEY_SYNC_STATUS))
                list.add("1");
            else
                list.add(cursor.getString(i));
            // list.add(cursor.getString(cursor.getColumnIndex("bitmapBLOB")));


        }
        return list;
    }
    public static final ArrayList<String> cursorToArrayListMedia(Cursor cursor,String customerId)
    {

        ArrayList<String> list = new ArrayList<String>();
        list.add(customerId);
        for (int i = 0;i<cursor.getColumnCount()-1;i++)

            list.add(cursor.getString(i));


        // list.add(cursor.getString(cursor.getColumnIndex("bitmapBLOB")));



        return list;
    }


    public static final ArrayList<String> cursorToDocumentsArray(Cursor cursor,String customerId)
    {

        ArrayList<String> list = new ArrayList<String>();
        list.add(customerId);

        list.add(cursor.getString(cursor.getColumnIndex("id")));
        list.add(cursor.getString(cursor.getColumnIndex("date")));
        list.add(cursor.getString(cursor.getColumnIndex("documentName")));
        list.add(cursor.getString(cursor.getColumnIndex("documentPath")));
        list.add(cursor.getString(cursor.getColumnIndex("syncStatus")));




        return list;
    }

    public void getPatientLastVisit(int pid,Context context)
    {
        DatabaseHandler databaseHandler = new DatabaseHandler(context);


    }

    public static final history_obj cursorToHistory(Cursor cursor)
    {
        history_obj historyObj = new history_obj();
        historyObj.set_past_illness(cursor.getString(cursor.getColumnIndex("histOfIllness")));
        historyObj.set_present_illness(cursor.getString(cursor.getColumnIndex("history")));
        historyObj.set_personal_hist(cursor.getString(cursor.getColumnIndex("personalHist")));
        historyObj.set_family_hist(cursor.getString(cursor.getColumnIndex("familyHist")));
        historyObj.set_date(cursor.getString(cursor.getColumnIndex("date")));
        return historyObj;
    }

    public static final exam_obj cursorToExam(Cursor cursor)
    {
        exam_obj examObj = new exam_obj();
        examObj.set_gen_exam(cursor.getString(cursor.getColumnIndex("genEXAM")));
        examObj.set_local_exam(cursor.getString(cursor.getColumnIndex("locEXAM")));


        examObj.set_date(cursor.getString(cursor.getColumnIndex("date")));
        return examObj;
    }

    public  static final treatment_obj cursorToTreatment(Cursor cursor)
    {
        treatment_obj treatmentObj = new treatment_obj();
        treatmentObj.set_treatment(cursor.getString(cursor.getColumnIndex("treatment")));
        treatmentObj.set_implants(cursor.getString(cursor.getColumnIndex("implant")));
        treatmentObj.set_diagnosis(cursor.getString(cursor.getColumnIndex("diagnosis")));
        treatmentObj.set_procedure(cursor.getString(cursor.getColumnIndex("procedure")));


        return  treatmentObj;

    }



    public  static final personal_obj cursorToPersonal(Cursor cursor)
    {
        personal_obj personalObj = new personal_obj();

        personalObj.set_customerId(cursor.getInt(cursor.getColumnIndex("id")));
        personalObj.set_photoPath(cursor.getString(cursor.getColumnIndex("documentPath")));
        personalObj.set_email(cursor.getString(cursor.getColumnIndex("email")));
        personalObj.set_name(cursor.getString(cursor.getColumnIndex("name")));
        personalObj.set_password(cursor.getString(cursor.getColumnIndex("password")));
        personalObj.set_dob(cursor.getString(cursor.getColumnIndex("dob")));
        personalObj.set_address(cursor.getString(cursor.getColumnIndex(DataBaseEnums.KEY_ADDRESS)));
       // personalObj.set_phone(cursor.getString(cursor.getColumnIndex("contactNumber")));
        personalObj.set_speciality(cursor.getString(cursor.getColumnIndex(DataBaseEnums.KEY_SPECIALITY)));
        personalObj.set_experience(cursor.getString(cursor.getColumnIndex(DataBaseEnums.KEY_EXPERIENCE)));
        personalObj.set_fees(cursor.getString(cursor.getColumnIndex(DataBaseEnums.KEY_CONSULT_FEE)));
        personalObj.set_gender(cursor.getString(cursor.getColumnIndex(DataBaseEnums.KEY_GENDER)));


        return  personalObj;

    }
    public static final ContentValues personalInfoTOValues (personal_obj personalObj , ContentValues values)
    {


        values.put("id", personalObj.get_customerId());
        values.put("email", personalObj.get_email());
        values.put("password", personalObj.get_password());
        values.put("speciality", personalObj.get_speciality());
        values.put("name", personalObj.get_name());
        values.put("designation", "Designation");
        values.put("address", "Address");
        values.put("awards", "Awards");
        values.put("experience", "Experience");
        values.put("consultFee", "ConsultFee");
        values.put("documentPath", personalObj.get_photoPath());
        values.put(DataBaseEnums.KEY_GENDER,personalObj.get_gender());







        return values;
    }


    public  static final other_obj[] cursorToOther(Cursor cursor)
    {
        int length = cursor.getCount();
        other_obj otherObj[] = new other_obj[length];

        for(int i = 0;i<length;i++)
        {
            otherObj[i]= new other_obj();
            otherObj[i].set_field_name(cursor.getString(cursor.getColumnIndex("fieldName")));
            otherObj[i].set_field_value(cursor.getString(cursor.getColumnIndex("fieldValue")));
            cursor.moveToNext();
        }



        return  otherObj;

    }

    public  static final media_obj[] cursorToMedia(Cursor cursor)
    {
        int length = cursor.getCount();
        media_obj mediaObjs[] = new media_obj[length];

        for(int i = 0;i<length;i++)
        {
            mediaObjs[i]= new media_obj();
            mediaObjs[i].set_media_path(cursor.getString(cursor.getColumnIndex("documentPath")));
            mediaObjs[i].set_media_name(cursor.getString(cursor.getColumnIndex("documentName")));
            mediaObjs[i].set_section(cursor.getInt(cursor.getColumnIndex("section")));
            mediaObjs[i].set_version(cursor.getInt(cursor.getColumnIndex("version")));
            mediaObjs[i].set_pid(cursor.getInt(cursor.getColumnIndex("id")));
            mediaObjs[i].set_bmp(cursor.getBlob(cursor.getColumnIndex("bitmapBLOB")));
            cursor.moveToNext();
        }



        return  mediaObjs;

    }
    public  static final  ArrayList<media_obj> cursorToMediaUpload(Cursor cursor)
    {
        int length = cursor.getCount();
        ArrayList<media_obj> mediaObjs = new ArrayList<>();

        for(int i = 0;i<length;i++)
        {
            media_obj mediaObj = new media_obj();
            mediaObj.set_media_path(cursor.getString(cursor.getColumnIndex("documentPath")));
            mediaObj.set_media_name(cursor.getString(cursor.getColumnIndex("documentName")));
            mediaObj.set_section(cursor.getInt(cursor.getColumnIndex("section")));
            mediaObj.set_version(cursor.getInt(cursor.getColumnIndex("version")));
            mediaObj.set_pid(cursor.getInt(cursor.getColumnIndex("id")));
            mediaObj.set_bmp(cursor.getBlob(cursor.getColumnIndex("bitmapBLOB")));
            mediaObjs.add(mediaObj);
            cursor.moveToNext();
        }



        return  mediaObjs;

    }
    public  static final  ArrayList<media_obj> cursorToDocumentsUpload(Cursor cursor)
    {
        int length = cursor.getCount();
        ArrayList<media_obj> mediaObjs = new ArrayList<>();

        for(int i = 0;i<length;i++)
        {
            media_obj mediaObj = new media_obj();
            mediaObj.set_media_path(cursor.getString(cursor.getColumnIndex("documentPath")));
            mediaObj.set_media_name(cursor.getString(cursor.getColumnIndex("documentName")));

            mediaObj.set_pid(cursor.getInt(cursor.getColumnIndex("id")));
            mediaObj.set_bmp(cursor.getBlob(cursor.getColumnIndex("bitmapBLOB")));
            mediaObjs.add(mediaObj);
            cursor.moveToNext();
        }



        return  mediaObjs;

    }


    public static final notes_obj cursorToNotes(Cursor cursor)
    {
        notes_obj notesObj =new notes_obj();
        notesObj.set_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex("id"))));
        notesObj.set_date(cursor.getString(cursor.getColumnIndex("date")));
        notesObj.set_complaint(cursor.getString(cursor.getColumnIndex("chiefComplaint")));
        notesObj.set_hist_present_illness(cursor.getString(cursor.getColumnIndex("histOfIllness")));
        notesObj.set_past_hist(cursor.getString(cursor.getColumnIndex("history")));
        notesObj.set_personal_hist(cursor.getString(cursor.getColumnIndex("personalHist")));
        notesObj.set_family_hist(cursor.getString(cursor.getColumnIndex("familyHist")));
        notesObj.set_gen_exam(cursor.getString(cursor.getColumnIndex("genEXAM")));
        notesObj.set_loc_exam(cursor.getString(cursor.getColumnIndex("locEXAM")));
        notesObj.set_classification(cursor.getString(cursor.getColumnIndex("classification")));
        notesObj.set_treatment(cursor.getString(cursor.getColumnIndex("treatment")));
        notesObj.set_implant(cursor.getString(cursor.getColumnIndex("implant")));
        notesObj.set_score(cursor.getString(cursor.getColumnIndex("score")));
        notesObj.set_remark(cursor.getString(cursor.getColumnIndex("remark")));
        notesObj.set_version(Integer.parseInt(cursor.getString(cursor.getColumnIndex("version"))));



        return notesObj;
    }

    public static final ContentValues notesTOValues (notes_obj notes , ContentValues values)
    {

        values.put("id",notes.get_id());
        values.put("date",notes.get_date());
        values.put("chiefComplaint",notes.get_complaint());
        values.put("histOfIllness", notes.get_hist_present_illness());
        values.put("history", notes.get_past_hist());
        values.put("personalHist", notes.get_personal_hist());
        values.put("familyHist", notes.get_family_hist());
        values.put("genEXAM", notes.get_gen_exam());
        values.put("locEXAM", notes.get_loc_exam());
        values.put("clinicalBLOB", notes.get_clinical());
        values.put("investigationBLOB", notes.get_invest());
        values.put("classification", notes.get_classification());
        values.put("treatment", notes.get_treatment());
        values.put("procedureBLOB", notes.get_procedure());
        values.put("implant", notes.get_implant());
        values.put("score", notes.get_score());
        values.put("remark", notes.get_remark());
        values.put("version", notes.get_version());





        return values;
    }


    public static  ArrayList<Map<String,String>> saveDataTable(JSONArray response,Context context)
    {

        DatabaseHandler databaseHandler = new DatabaseHandler(context);
        ArrayList<Map<String,String>> arrayListIdPhotoPath = new ArrayList<>();

        for (int k = 0;k<response.size();k++) {

            org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) response.get(k);

         if(jsonObject.size()==1) {

             String table = String.valueOf(jsonObject.get("table"));
             if(table.equals("documents")) {
                 saveDocumentsTable(response, context, k + 1);
             }
             else if(table.equals("media")) {
                 saveMediaTable(response, context, k + 1);
             }
             else if(table.equals("mediaFollowUp")) {
                 saveMediaFollowUpTable(response, context, k + 1);
             }
             else if(table.equals("patient")) {
                 savePatientsTable(response, context, k + 1);
             }
             else if(table.equals("notes")) {
                 saveNotesTable(response, context, k + 1);
             }
             else if(table.equals("followUp")) {
                 saveFollowUpTable(response, context, k + 1);
             }
             else if(table.equals("other")) {
                 saveOtherTable(response, context, k + 1);
             }
             else if(table.equals("otherFollowUp")) {
                 saveOtherFollowUpTable(response, context, k + 1);
             }
             else if(table.equals("personalinfo")) {
                 savePersonalInfoTable(response, context, k + 1);
             }
             else if(table.equals("appointmentSettings")) {
                 saveAppointmentSettingsTable(response, context, k + 1);
             }

         }


        }

return  arrayListIdPhotoPath;
    }


    public static void savePatientsTable(JSONArray response,Context context,int index) {

        DatabaseHandler databaseHandler = new DatabaseHandler(context);

        for (int k = index;k<response.size();k++) {


            org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) response.get(k);
            if(jsonObject.size()==1) {

                String table = String.valueOf(jsonObject.get("table"));
                break;
            }


            Patient patient = new Patient();
            BitmapDrawable bitmap = null;
            byte[] image;
            image = new byte[0];
            Bitmap bmp = null;
            bitmap = (BitmapDrawable)context.getResources().getDrawable(R.drawable.default_photo);
            bmp = bitmap.getBitmap();

            image =  Base64.decode(String.valueOf(jsonObject.get("photo")), Base64.DEFAULT);


            patient.set_name(String.valueOf(jsonObject.get("Patient Name")));
            patient.set_diagnosis(String.valueOf(jsonObject.get("Diagnosis")));
            patient.set_id(Integer.parseInt(String.valueOf(jsonObject.get("Patient Id"))));
            patient.set_gender(String.valueOf(jsonObject.get("Gender")));
            patient.set_age(String.valueOf(jsonObject.get("Age")));
            patient.set_bmp(image);
            File storageDir =
                    new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES), "Patient Manager/"+patient.get_id());
            if(!storageDir.exists()) {

                storageDir.mkdir();
            }

            databaseHandler.addPatient(patient);
            utility.addFieldsToPatient((int) patient.get_id(), context);
            databaseHandler.updatePatient(DataBaseEnums.KEY_SYNC_STATUS, "1", String.valueOf(patient.get_id()));
            databaseHandler.updatePatient(DataBaseEnums.KEY_CONTACT, String.valueOf(jsonObject.get("PhoneNumber"))
                    , String.valueOf(patient.get_id()));
            databaseHandler.updatePatient(DataBaseEnums.KEY_FIRST_AID_ID,String.valueOf(jsonObject.get("FirstAidPatientId"))
                    , String.valueOf(patient.get_id()));
        }



    }
    public static void savePersonalInfoTable(JSONArray response,Context context,int index) {

        DatabaseHandler databaseHandler = new DatabaseHandler(context);

        for (int k = index;k<response.size();k++) {


            org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) response.get(k);
            if(jsonObject.size()==1) {

                String table = String.valueOf(jsonObject.get("table"));
                break;
            }
            try {

                databaseHandler.updatePersonalInfo(DataBaseEnums.KEY_NAME, String.valueOf(jsonObject.get("Name")));
                databaseHandler.updatePersonalInfo(DataBaseEnums.KEY_ADDRESS, String.valueOf(jsonObject.get("Address")));
                databaseHandler.updatePersonalInfo(DataBaseEnums.KEY_EXPERIENCE, String.valueOf(jsonObject.get("Experience")));
                databaseHandler.updatePersonalInfo(DataBaseEnums.KEY_CONSULT_FEE, String.valueOf(jsonObject.get("ConsultFee")));
                databaseHandler.updatePersonalInfo(DataBaseEnums.KEY_SPECIALITY, String.valueOf(jsonObject.get("Speciality")));
                databaseHandler.updatePersonalInfo(DataBaseEnums.KEY_PASSWORD, String.valueOf(jsonObject.get("password")));
                databaseHandler.updatePersonalInfo(DataBaseEnums.KEY_MAP_SNAPSHOT_PATH, String.valueOf(jsonObject.get("CoverPicture")));
                databaseHandler.updatePersonalInfo(DataBaseEnums.KEY_DOC_PATH, String.valueOf(jsonObject.get("ProfilePic")));
                String latitude = String.valueOf(jsonObject.get("PositionLat"));
                String longitude = String.valueOf(jsonObject.get("PositionLong"));
                databaseHandler.updatePersonalInfo(DataBaseEnums.KEY_LATITUDE, String.valueOf(jsonObject.get("PositionLat")));
                databaseHandler.updatePersonalInfo(DataBaseEnums.KEY_LONGITUDE, String.valueOf(jsonObject.get("PositionLong")));
                databaseHandler.updatePersonalInfo("dob",String.valueOf(jsonObject.get("Dob")));

            }
catch(Exception e)
{
    e.printStackTrace();
}

        }



    }
    public static void saveAppointmentSettingsTable(JSONArray response,Context context,int index) {

        DatabaseHandler databaseHandler = new DatabaseHandler(context);

        for (int k = index;k<response.size();k++) {


            org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) response.get(k);
            if(jsonObject.size()==1) {

                String table = String.valueOf(jsonObject.get("table"));
                break;
            }
            try {

              databaseHandler.saveAppointmentSettings( String.valueOf(jsonObject.get("onlineDays")),String.valueOf(jsonObject.get("startTime")),
                      String.valueOf(jsonObject.get("endTime")), Integer.parseInt(String.valueOf(jsonObject.get("numberOfPatients"))));

            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

        }



    }

    public static void bookAppointmentTodayLocally(int pid, Context context)
    {
        final ArrayList<String> list = new ArrayList<>();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        final Date date = new Date();
        DatabaseHandler databaseHandler = new DatabaseHandler(context);


        Patient patient = databaseHandler.getPatient(pid);
        list.add(String.valueOf(patient.get_id()));
        list.add(databaseHandler.getCustomerId()+""+String.valueOf(patient.get_id()));
        list.add("");
        list.add("");
        list.add(sdf1.format(date));
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        // notesList.add(list);


        databaseHandler.saveAppointments(list);


    }
    public static void saveAppointmentsTable(JSONArray response,Context context) {
        DatabaseHandler databaseHandler = new DatabaseHandler(context);
        ArrayList<ArrayList<String>> data;
        data = new ArrayList<ArrayList<String>>();
     ArrayList<String> appointment = new ArrayList<>();


        for (int k = 0;k<response.size();k++) {
            org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) response.get(k);
            appointment = new ArrayList<>();
            if(jsonObject.get("Patient Id")==null)
            {
                appointment.add("0");
            }
            else
            appointment.add(String.valueOf(jsonObject.get("Patient Id")));
            appointment.add(String.valueOf(jsonObject.get("FirstAidPatientId")));
            appointment.add(String.valueOf(jsonObject.get("StartTime")));
            appointment.add(String.valueOf(jsonObject.get("EndTime")));
            appointment.add(String.valueOf(jsonObject.get("Date")));
            appointment.add(String.valueOf(jsonObject.get("FirstaidPatientName")));
            appointment.add(String.valueOf(jsonObject.get("FirstaidPatientContact")));
            appointment.add(String.valueOf(jsonObject.get("FirstaidPatientEmail")));
            appointment.add(String.valueOf(jsonObject.get("FirstaidPatientAge")));
            appointment.add(String.valueOf(jsonObject.get("FirstaidPatientGender")));
            appointment.add(String.valueOf(jsonObject.get("SerialNumber")));
            appointment.add(String.valueOf(jsonObject.get("FirstaidPatientHeight")));
            appointment.add(String.valueOf(jsonObject.get("FirstaidPatientWeight")));
            try {
                databaseHandler.saveAppointments(appointment);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

    }



    public static  Map<String,String> saveDocumentsTable(JSONArray response,Context context,int index) {

        DatabaseHandler databaseHandler = new DatabaseHandler(context);
        Map<String,String> map = new HashMap<String,String>() {{

        }};
        ArrayList<String> idPath =new ArrayList<>();
        for (int k = index;k<response.size();k++) {


            org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) response.get(k);
            if(jsonObject.size()==1) {

                String table = String.valueOf(jsonObject.get("table"));
                break;
            }


            document_obj documentObj = new document_obj();

            documentObj.set_doc_path(String.valueOf(jsonObject.get("DocumentPath")));
            documentObj.set_doc_name(String.valueOf(jsonObject.get("DocumentName")));
            documentObj.set_date(String.valueOf(jsonObject.get("Date")));
            documentObj.set_id(Integer.parseInt(String.valueOf(jsonObject.get("PatientId"))));
            map.put(String.valueOf(documentObj.get_id()), documentObj.get_doc_path());
           // documentObj = PhotoHelper.addMissingBmp(documentObj);

            Patient patient = databaseHandler.getPatient(Integer.parseInt(String.valueOf(jsonObject.get("PatientId"))));

            File storageDir =
                    new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES), "Patient Manager/"+patient.get_id()+"/Documents");
            if(!storageDir.exists()) {

                storageDir.mkdir();
            }

            databaseHandler.addDocument(documentObj);
            databaseHandler.updateDocument(documentObj, "1");
           // downloadFile(documentObj.get_id(),documentObj.get_doc_path(), String.valueOf(databaseHandler.getPersonalInfo().get_customerId()),context);
        }

return  map;

    }
    //i: patient id
    //s: file path
    public static void downloadFile(int i, String s,String customerId,Context context){
        int totalSize = 0;
        int downloadedSize = 0;DatabaseHandler databaseHandler = new DatabaseHandler(context);
        int CustomerId = databaseHandler.getPersonalInfo().get_customerId();
        File file1 = new File(s);
        File dirAsFile = file1.getParentFile();

        String dwnload_file_path = "http://docbox.co.in/sajid/"+customerId+"//"+String.valueOf(i)+"//"+file1.getName();
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
            File file = new File(dirAsFile,file1.getName());

            FileOutputStream fileOutput = new FileOutputStream(file);

            //Stream used for reading the data from the internet
            InputStream inputStream = urlConnection.getInputStream();

            //this is the total size of the file which we are downloading
            totalSize = urlConnection.getContentLength();


            //create a buffer...
            byte[] buffer = new byte[1024];
            int bufferLength = 0;

            while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                // update the progressbar //

            }
            //close the output stream when complete //
            fileOutput.close();


        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    public static void saveMediaFollowUpTable(JSONArray response, Context context, int index) {

        DatabaseHandler databaseHandler = new DatabaseHandler(context);

        ArrayList<String> idPath =new ArrayList<>();
        for (int k = index;k<response.size();k++) {


            org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) response.get(k);
            if(jsonObject.size()==1) {

                String table = String.valueOf(jsonObject.get("table"));
                break;
            }

            media_obj mediaObj = new media_obj();
            document_obj documentObj = new document_obj();
            mediaObj.set_media_path(String.valueOf(jsonObject.get("DocumentPath")));
            mediaObj.set_media_name(String.valueOf(jsonObject.get("DocumentName")));
            mediaObj.set_pid(Integer.parseInt(String.valueOf(jsonObject.get("PatientId"))));
            mediaObj.set_section(Integer.parseInt(String.valueOf(jsonObject.get("Section"))));
            mediaObj.set_version(Integer.parseInt(String.valueOf(jsonObject.get("Version"))));


            // documentObj = PhotoHelper.addMissingBmp(documentObj);

            Patient patient = databaseHandler.getPatient(Integer.parseInt(String.valueOf(jsonObject.get("PatientId"))));

            File storageDir =
                    new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES), "Patient Manager/"+patient.get_name()+"/Notes");
            if(!storageDir.exists()) {

                storageDir.mkdir();
            }
            databaseHandler.addMediaFollowUp(mediaObj);

            //databaseHandler.aM(documentObj);
            // downloadFile(documentObj.get_id(),documentObj.get_doc_path(), String.valueOf(databaseHandler.getPersonalInfo().get_customerId()),context);
        }



    }

    public static void saveMediaTable(JSONArray response, Context context, int index) {

        DatabaseHandler databaseHandler = new DatabaseHandler(context);

        ArrayList<String> idPath =new ArrayList<>();
        for (int k = index;k<response.size();k++) {


            org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) response.get(k);
            if(jsonObject.size()==1) {

                String table = String.valueOf(jsonObject.get("table"));
                break;
            }

            media_obj mediaObj = new media_obj();
            document_obj documentObj = new document_obj();
            mediaObj.set_media_path(String.valueOf(jsonObject.get("DocumentPath")));
            mediaObj.set_media_name(String.valueOf(jsonObject.get("DocumentName")));
            mediaObj.set_pid(Integer.parseInt(String.valueOf(jsonObject.get("PatientId"))));
            mediaObj.set_section(0);
            mediaObj.set_version(1);


            // documentObj = PhotoHelper.addMissingBmp(documentObj);

            Patient patient = databaseHandler.getPatient(Integer.parseInt(String.valueOf(jsonObject.get("PatientId"))));

            File storageDir =
                    new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES), "Patient Manager/"+patient.get_name()+"/Notes");
            if(!storageDir.exists()) {

                storageDir.mkdir();
            }
        databaseHandler.addMedia(mediaObj);
            //databaseHandler.aM(documentObj);
            // downloadFile(documentObj.get_id(),documentObj.get_doc_path(), String.valueOf(databaseHandler.getPersonalInfo().get_customerId()),context);
        }



    }

    public static void saveNotesTable(JSONArray response, Context context, int index) {

        DatabaseHandler databaseHandler = new DatabaseHandler(context);
        ArrayList<Item> listOfItems = new ArrayList<>();

        for (int k = index;k<response.size();k++) {

            Item item = new Item();
            org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) response.get(k);
            if (jsonObject.size() == 1) {


                break;
            }

            item.setPatient_id(Integer.parseInt(String.valueOf(jsonObject.get("PatientId"))));
            item.setDate(String.valueOf(jsonObject.get("Date")));
            item.setDiagnosis(String.valueOf(jsonObject.get("FieldValue")));
            item.setTitle(String.valueOf(jsonObject.get("FieldName")));
            item.setSection(Integer.parseInt(String.valueOf(jsonObject.get("Section"))));

            listOfItems.add(item);
//
//            exam_obj examObj = new exam_obj();
//
//            examObj.set_pid(Integer.parseInt(String.valueOf(jsonObject.get("PatientId"))));
//            examObj.set_version(Integer.parseInt(String.valueOf(jsonObject.get("Version"))));
//            examObj.set_date(String.valueOf(jsonObject.get("Date")));
//            examObj.set_gen_exam(String.valueOf(jsonObject.get("GeneralExamination")));
//            examObj.set_local_exam(String.valueOf(jsonObject.get("LocalExamination")));

            Patient patient = databaseHandler.getPatient(Integer.parseInt(String.valueOf(jsonObject.get("PatientId"))));

            File storageDir =
                    new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES), "Patient Manager/"+patient.get_id()+"/Notes");
            if(!storageDir.exists()) {

                storageDir.mkdir();
            }

        }



        databaseHandler.saveGenericNote(listOfItems,"1");

    }




    public static void saveFollowUpTable(JSONArray response, Context context, int index) {

        DatabaseHandler databaseHandler = new DatabaseHandler(context);
        ArrayList<Item> listOfItems = new ArrayList<>();

        for (int k = index;k<response.size();k++) {

            Item item = new Item();
            org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) response.get(k);
            if (jsonObject.size() == 1) {


                break;
            }

            item.setPatient_id(Integer.parseInt(String.valueOf(jsonObject.get("PatientId"))));
            item.setDate(String.valueOf(jsonObject.get("Date")));
            item.setDiagnosis(String.valueOf(jsonObject.get("FieldValue")));
            item.setTitle(String.valueOf(jsonObject.get("FieldName")));
            item.setSection(Integer.parseInt(String.valueOf(jsonObject.get("Version"))));

            listOfItems.add(item);
//
//            exam_obj examObj = new exam_obj();
//
//            examObj.set_pid(Integer.parseInt(String.valueOf(jsonObject.get("PatientId"))));
//            examObj.set_version(Integer.parseInt(String.valueOf(jsonObject.get("Version"))));
//            examObj.set_date(String.valueOf(jsonObject.get("Date")));
//            examObj.set_gen_exam(String.valueOf(jsonObject.get("GeneralExamination")));
//            examObj.set_local_exam(String.valueOf(jsonObject.get("LocalExamination")));

            Patient patient = databaseHandler.getPatient(Integer.parseInt(String.valueOf(jsonObject.get("PatientId"))));

            File storageDir =
                    new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES), "Patient Manager/"+patient.get_id()+"/Notes");
            if(!storageDir.exists()) {

                storageDir.mkdir();
            }

        }
databaseHandler.saveFollowUp(listOfItems);



    }


    public static void saveOtherTable(JSONArray response,Context context,int index) {

        DatabaseHandler databaseHandler = new DatabaseHandler(context);

        for (int k = index;k<response.size();k++) {


            org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) response.get(k);
            if(jsonObject.size()==1) {


                break;
            }


            other_obj otherObj = new other_obj();

            otherObj.set_pid(Integer.parseInt(String.valueOf(jsonObject.get("PatientId"))));
            otherObj.set_version(Integer.parseInt(String.valueOf(jsonObject.get("Version"))));
            otherObj.set_date(String.valueOf(jsonObject.get("Date")));
            otherObj.set_field_name(String.valueOf(jsonObject.get("FieldName")));
            otherObj.set_field_value(String.valueOf(jsonObject.get("FieldValue")));


            Patient patient = databaseHandler.getPatient(Integer.parseInt(String.valueOf(jsonObject.get("PatientId"))));

            File storageDir =
                    new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES), "Patient Manager/"+patient.get_name()+"/Notes");
            if(!storageDir.exists()) {

                storageDir.mkdir();
            }

            databaseHandler.addOther(otherObj);
        }



    }

    public static void saveOtherFollowUpTable(JSONArray response,Context context,int index) {

        DatabaseHandler databaseHandler = new DatabaseHandler(context);

        for (int k = index;k<response.size();k++) {


            org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) response.get(k);
            if(jsonObject.size()==1) {


                break;
            }


            other_obj otherObj = new other_obj();

            otherObj.set_pid(Integer.parseInt(String.valueOf(jsonObject.get("PatientId"))));
            otherObj.set_version(Integer.parseInt(String.valueOf(jsonObject.get("Version"))));
            otherObj.set_date(String.valueOf(jsonObject.get("Date")));
            otherObj.set_field_name(String.valueOf(jsonObject.get("FieldName")));
            otherObj.set_field_value(String.valueOf(jsonObject.get("FieldValue")));


            Patient patient = databaseHandler.getPatient(Integer.parseInt(String.valueOf(jsonObject.get("PatientId"))));

            File storageDir =
                    new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES), "Patient Manager/"+patient.get_name()+"/Notes");
            if(!storageDir.exists()) {

                storageDir.mkdir();
            }

            databaseHandler.addOtherFollowUp(otherObj);
        }



    }

    public static void addFieldsToPatient(int pid,Context context)
    {
        Resources resource = context.getResources();
        DatabaseHandler databaseHandler = new DatabaseHandler(context);

        String[] tabs = resource.getStringArray(R.array.tabs);
        ArrayList<Item> listOfItems = null;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        for(int j = 0;j<tabs.length;j++){
            int resId = resource.getIdentifier(tabs[j],"array",context.getPackageName());
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
            databaseHandler.saveGenericNote(listOfItems, "0");
        }
    }


    public static ArrayList<Item> getMediaList(int pid,Activity activity,int section,int version)
    {
        ArrayList<Item> field = new ArrayList<>();


        DatabaseHandler dbHandler = new DatabaseHandler(activity);
        Item item;
       // int version;
        int mediaVersion = dbHandler.getCurrentMediaVersion(pid);
        int docVersion = dbHandler.getCurrentVersion(pid);


        media_obj mediaObjs[]=dbHandler.getLatestMediaAdd(pid,version,section);
        if (mediaObjs != null)
        {
            for (int i = 0; i < mediaObjs.length; i++) {
                item = new Item();
                // mediaObjs[i]=PhotoHelper.addMissingBmp(mediaObjs[i],CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
                item.setBmp(BitmapFactory.decodeByteArray(mediaObjs[i].get_bmp(), 0, mediaObjs[i].get_bmp().length));
                item.setDiagnosis(mediaObjs[i].get_media_path());
                item.setPatient_id(mediaObjs[i].get_pid());
                //item.setDiagnosis(mediaObjs[i].get_field_value());
                field.add(item);
                //field.add(otherObj[i].get_field_name());
            }
        }
        return  field;
    }


    public static ArrayList<Item> getMediaList(int pid,Activity activity,int section)
    {
        ArrayList<Item> field = new ArrayList<>();


        DatabaseHandler dbHandler = new DatabaseHandler(activity);
        Item item;
        int version;
        int mediaVersion = dbHandler.getCurrentMediaVersion(pid);
        int docVersion = dbHandler.getCurrentVersion(pid);

      /*  if(mediaVersion>docVersion)
        {
            deleteExtraMedia(activity, mediaVersion);
        }*/
        if((activity.getClass() == View_Media_notes_grid.class)||

                (activity.getClass() == Treatment_Activity.class)||
                (activity.getClass()== Other_Notes_Activity.class))
        {
            version = docVersion+1;
        }
        else {
            version = docVersion + 1;
        }

        media_obj mediaObjs[]=dbHandler.getLatestMediaAdd(pid,version,section);
        if (mediaObjs != null)
        {
            for (int i = 0; i < mediaObjs.length; i++) {
                item = new Item();
                // mediaObjs[i]=PhotoHelper.addMissingBmp(mediaObjs[i],CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
                if(mediaObjs[i].get_media_path().contains(".mp4"))
                {
                    mediaObjs[i] = PhotoHelper.addMissingBmp(mediaObjs[i], 200);
                }
                else{
                mediaObjs[i] = PhotoHelper.addMissingBmp(mediaObjs[i],300);}
                item.setBmp(BitmapFactory.decodeByteArray(mediaObjs[i].get_bmp(), 0, mediaObjs[i].get_bmp().length));
                item.setDiagnosis(mediaObjs[i].get_media_path());
                item.setPatient_id(mediaObjs[i].get_pid());
                //item.setDiagnosis(mediaObjs[i].get_field_value());
                field.add(item);
                //field.add(otherObj[i].get_field_name());
            }
        }
        return  field;
    }




    public static void deleteExtraMedia(Activity activity, int version)
    {
        DatabaseHandler dbHandler = new DatabaseHandler(activity);
        dbHandler.deleteMedia(version);
    }

    public static boolean hasActiveInternetConnection(Context context) {
       // if (isNetworkAvailable(context)) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return (urlc.getResponseCode() == 200);
            } catch (IOException e) {
                //Log.e(LOG_TAG, "Error checking internet connection", e);
            }
        //} //else {
            //Log.d(LOG_TAG, "No network available!");
        //}
        return false;
    }
  /*  private static  boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }*/


    public static boolean deleteDirectory(File path) {
        if( path.exists() ) {
            File[] files = path.listFiles();
            if (files == null) {
                return true;
            }
            for(int i=0; i<files.length; i++) {
                if(files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                }
                else {
                    files[i].delete();
                }
            }
        }
        return( path.delete() );
    }
public  static void saveBMPtoFile(Bitmap bmp,String filePath)

{
    FileOutputStream out = null;
    try {
        out = new FileOutputStream(filePath);
        bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
        // PNG is a lossless format, the compression factor (100) is ignored
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        try {
            if (out != null) {
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
public static  int getIdFromSpeciality(String speciality,Context context)
{

    List<String> Lines = Arrays.asList(context.getResources().getStringArray(R.array.specialities));

for(int i = 0;i<Lines.size();i++)
{
    if(Lines.get(i).equals(speciality))
    {
        return i;
    }
}

return 1;
}

    public static  String generateOTT()
    {
        String range = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        String ott = new String();
        for(int i = 0;i<6;i++) {
            Random rand = new Random();
            int n = rand.nextInt(34);
            String a = String.valueOf(range.charAt(n));
            ott= ott+a;
           // ott.concat(a);
        }
        return ott;

    }





}

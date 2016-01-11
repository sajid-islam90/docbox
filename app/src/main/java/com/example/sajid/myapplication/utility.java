package com.example.sajid.myapplication;
//UTILITY CLASS
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;
import org.json.simple.JSONArray;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import activity.Exam_Activity;
import activity.Other_Notes_Activity;
import activity.Treatment_Activity;
import activity.View_Media_notes_grid;

import cz.msebera.android.httpclient.extras.Base64;
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

    public static final Patient cursorToPatient(Cursor cursor)
    {
        Patient patient = new Patient(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DataBaseEnums.KEY_ID))),
                cursor.getString(cursor.getColumnIndex(DataBaseEnums.KEY_NAME)),
                cursor.getString(cursor.getColumnIndex(DataBaseEnums.KEY_AGE)),cursor.getString(cursor.getColumnIndex(DataBaseEnums.KEY_GENDER)),
                cursor.getString(cursor.getColumnIndex(DataBaseEnums.KEY_HEIGHT)),cursor.getBlob(cursor.getColumnIndex(DataBaseEnums.KEY_BMP)),
                cursor.getString(cursor.getColumnIndex(DataBaseEnums.KEY_DIAGNOSIS)));
        patient.set_last_seen_date(cursor.getString(cursor.getColumnIndex(DataBaseEnums.KEY_DATE_LAST_VISIT)));

        patient.set_photoPath(cursor.getString(cursor.getColumnIndex(DataBaseEnums.KEY_PHOTO_PATH)));
        patient.set_address(cursor.getString(cursor.getColumnIndex(DataBaseEnums.KEY_ADDRESS)));
        patient.set_contact_number(cursor.getString(cursor.getColumnIndex(DataBaseEnums.KEY_CONTACT)));
        patient.set_ocupation(cursor.getString(cursor.getColumnIndex(DataBaseEnums.KEY_OCCUPATION)));
        patient.set_email(cursor.getString(cursor.getColumnIndex(DataBaseEnums.KEY_EMAIL)));

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

    public static void syncData(String apiAddress ,  RequestParams params, final Context context, final ProgressDialog progressDialog , AsyncHttpClient client)
    {
        //AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
        client.post(apiAddress, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {

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
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
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

    public static void syncData(String apiAddress ,  RequestParams params, final Context context, final ProgressDialog progressDialog, final String pid )
    {
        final AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);

        final DatabaseHandler databaseHandler = new DatabaseHandler(context);
        client.post(apiAddress, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, JSONObject response) {

                try {


                    databaseHandler.deletePatient();

                    if (context == null) {
                        return;
                    }
                    ((Activity) context).recreate();
                    progressDialog.hide();
                    Toast.makeText(context, "MySQL DB has been informed about Sync activity", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //controller.updateSyncStatus(obj.get("id").toString(),obj.get("status").toString());

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    //String str = new String(bytes, "UTF-8");
                    progressDialog.hide();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(context, "MySQL DB has not been informed about Sync activity", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFinish() {

                databaseHandler.deletePatient();
                String DocumentsJson = databaseHandler.composeJSONfromSQLiteDocuments(pid);
                String notesJson = databaseHandler.composeJSONfromSQLiteNotes(pid, DataBaseEnums.TABLE_NOTES);
                String OtherJson = databaseHandler.composeJSONfromSQLiteNotes(pid, DataBaseEnums.TABLE_OTHER);
                String OtherFollowUpJson = databaseHandler.composeJSONfromSQLiteNotes(pid, DataBaseEnums.TABLE_OTHER_FOLLOW_UP);
                String FollowUpJson = databaseHandler.composeJSONfromSQLiteNotes(pid, DataBaseEnums.TABLE_FOLLOW_UP);
                String MediaFollowUpJson = databaseHandler.composeJSONfromSQLiteNotes(pid, DataBaseEnums.TABLE_MEDIA_FOLLOW_UP);
                String MediaJson = databaseHandler.composeJSONfromSQLiteNotes(pid, DataBaseEnums.TABLE_MEDIA);
                RequestParams paramsDocuments = new RequestParams();
                RequestParams paramsNotes = new RequestParams();
                RequestParams paramsFollowUp = new RequestParams();
                RequestParams paramsOther = new RequestParams();
                RequestParams paramsOtherFollowUp = new RequestParams();
                RequestParams paramsMedia = new RequestParams();
                RequestParams paramsMediaFollowUp = new RequestParams();


                paramsDocuments.put("documentsJSON", DocumentsJson);
                paramsNotes.put("NotesJSON", notesJson);
                paramsFollowUp.put("FollowUpJSON", FollowUpJson);
                paramsOther.put("OtherJSON", OtherJson);
                paramsOtherFollowUp.put("OtherFollowUpJSON", OtherFollowUpJson);
                paramsMedia.put("MediaJSON", MediaJson);
                paramsMediaFollowUp.put("MediaFollowUpJSON", MediaFollowUpJson);
                // sync("http://docbox.co.in/sajid/insertDocuments.php", params, context);
                String address = "docbox.co.in/sajid";//context.getResources().getString(R.string.action_server_ip_address);
                sync("http://" + address + "/insertDocuments.php", paramsDocuments, context);
                sync("http://" + address + "/insertNotes.php", paramsNotes, context);
                sync("http://" + address + "/insertFollowUp.php", paramsFollowUp, context);
                sync("http://" + address + "/insertOther.php", paramsOther, context);
                sync("http://" + address + "/insertOtherFollowUp.php", paramsOtherFollowUp, context);
                sync("http://" + address + "/insertMedia.php", paramsMedia, context);
                sync("http://" + address + "/insertMediaFollowUp.php", paramsMediaFollowUp, context);
                ((Activity) context).recreate();
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
            public void onSuccess(int i, Header[] headers, byte[] bytes) {

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
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                try {
                    String str = new String(bytes, "UTF-8");
                    Toast.makeText(context, "MySQL DB has not been informed about Sync activity", Toast.LENGTH_LONG).show();

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
        list.add(cursor.getString(cursor.getColumnIndex("diagnosis")));
        list.add(cursor.getString(cursor.getColumnIndex("age")));
        list.add(cursor.getString(cursor.getColumnIndex("gender")));
        list.add(cursor.getString(cursor.getColumnIndex("height")));
        list.add("1");

        byte[] a = cursor.getBlob(cursor.getColumnIndex("bitmapBLOB"));

        String ab = Base64.encodeToString(a,Base64.DEFAULT);
        byte[] b = Base64.decode(ab,Base64.DEFAULT);

       list.add(ab);
        list.add(cursor.getString(cursor.getColumnIndex("id")));



        return list;
    }

    public static final ArrayList<String> cursorToSimpleArraylist(Cursor cursor)
    {

        ArrayList<String> list = new ArrayList<String>();

        for (int i = 0;i<cursor.getColumnCount()-1;i++)

            list.add(cursor.getString(i));


        // list.add(cursor.getString(cursor.getColumnIndex("bitmapBLOB")));



        return list;
    }



    public static final ArrayList<String> cursorToArrayList(Cursor cursor,String customerId)
    {

        ArrayList<String> list = new ArrayList<String>();
        list.add(customerId);
        for (int i = 0;i<cursor.getColumnCount();i++)

        list.add(cursor.getString(i));


        // list.add(cursor.getString(cursor.getColumnIndex("bitmapBLOB")));



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
       // personalObj.set_phone(cursor.getString(cursor.getColumnIndex("contactNumber")));
        personalObj.set_speciality(cursor.getString(cursor.getColumnIndex(DataBaseEnums.KEY_SPECIALITY)));


        return  personalObj;

    }
    public static final ContentValues personalInfoTOValues (personal_obj personalObj , ContentValues values)
    {


        values.put("id", personalObj.get_customerId());
        values.put("email", personalObj.get_email());
        values.put("password", personalObj.get_password());
        values.put("speciality",personalObj.get_speciality());
        values.put("name", personalObj.get_name());
        values.put("designation","Designation");
        values.put("address","Address");
        values.put("awards","Awards");
        values.put("experience","Experience");
        values.put("consultFee","ConsultFee");
        values.put("documentPath", personalObj.get_photoPath());







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


    public static void saveDataTable(JSONArray response,Context context)
    {

        DatabaseHandler databaseHandler = new DatabaseHandler(context);

        for (int k = 0;k<response.size();k++) {

            org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) response.get(k);

         if(jsonObject.size()==1) {

             String table = String.valueOf(jsonObject.get("table"));
             if(table.equals("documents")) {
                 saveDocumentsTable(response, context, k + 1);
             }
             else if(table.equals("history")) {
                 saveHistoryTable(response, context, k + 1);
             }
             else if(table.equals("patient")) {
                 savePatientsTable(response, context, k + 1);
             }
             else if(table.equals("exam")) {
                 saveExamTable(response, context, k + 1);
             }
             else if(table.equals("treatment")) {
                 saveTreatmentTable(response, context, k + 1);
             }
             else if(table.equals("other")) {
                 saveOtherTable(response, context, k + 1);
             }

         }


        }


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

            image =  Base64.decode(String.valueOf( jsonObject.get("photo")), Base64.DEFAULT);


            patient.set_name(String.valueOf(jsonObject.get("Patient Name")));
            patient.set_diagnosis(String.valueOf(jsonObject.get("Diagnosis")));
            patient.set_id(Integer.parseInt(String.valueOf(jsonObject.get("Patient Id"))));
            patient.set_gender(String.valueOf(jsonObject.get("Gender")));
            patient.set_bmp(image);
            File storageDir =
                    new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES), "Patient Manager/"+patient.get_name());
            if(!storageDir.exists()) {

                storageDir.mkdir();
            }

            databaseHandler.addPatient(patient);
        }



    }




    public static void saveDocumentsTable(JSONArray response,Context context,int index) {

        DatabaseHandler databaseHandler = new DatabaseHandler(context);

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
            documentObj = PhotoHelper.addMissingBmp(documentObj);

            Patient patient = databaseHandler.getPatient(Integer.parseInt(String.valueOf(jsonObject.get("PatientId"))));

            File storageDir =
                    new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES), "Patient Manager/"+patient.get_name()+"/Documents");
            if(!storageDir.exists()) {

                storageDir.mkdir();
            }

            databaseHandler.addDocument(documentObj);
        }



    }


    public static void saveHistoryTable(JSONArray response,Context context,int index) {

        DatabaseHandler databaseHandler = new DatabaseHandler(context);

        for (int k = index;k<response.size();k++) {


            org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) response.get(k);
            if(jsonObject.size()==1) {

                String table = String.valueOf(jsonObject.get("table"));
                break;
            }


            history_obj historyObj = new history_obj();

            historyObj.set_present_illness(String.valueOf(jsonObject.get("ChiefComplaint")));
            historyObj.set_past_illness(String.valueOf(jsonObject.get("PastHistory")));
            historyObj.set_date(String.valueOf(jsonObject.get("Date")));
            historyObj.set_personal_hist(String.valueOf(jsonObject.get("PersonalHistory")));
            historyObj.set_family_hist(String.valueOf(jsonObject.get("FamilyHistory")));
            historyObj.set_version(Integer.parseInt(String.valueOf(jsonObject.get("Version"))));
            historyObj.set_pid(Integer.parseInt(String.valueOf(jsonObject.get("PatientId"))));


            Patient patient = databaseHandler.getPatient(Integer.parseInt(String.valueOf(jsonObject.get("PatientId"))));

            File storageDir =
                    new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES), "Patient Manager/"+patient.get_name()+"/Notes");
            if(!storageDir.exists()) {

                storageDir.mkdir();
            }

            databaseHandler.addHistory(historyObj);
        }



    }

    public static void saveExamTable(JSONArray response,Context context,int index) {

        DatabaseHandler databaseHandler = new DatabaseHandler(context);

        for (int k = index;k<response.size();k++) {


            org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) response.get(k);
            if(jsonObject.size()==1) {


                break;
            }


            exam_obj examObj = new exam_obj();

            examObj.set_pid(Integer.parseInt(String.valueOf(jsonObject.get("PatientId"))));
            examObj.set_version(Integer.parseInt(String.valueOf(jsonObject.get("Version"))));
            examObj.set_date(String.valueOf(jsonObject.get("Date")));
            examObj.set_gen_exam(String.valueOf(jsonObject.get("GeneralExamination")));
            examObj.set_local_exam(String.valueOf(jsonObject.get("LocalExamination")));


            Patient patient = databaseHandler.getPatient(Integer.parseInt(String.valueOf(jsonObject.get("PatientId"))));

            File storageDir =
                    new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES), "Patient Manager/"+patient.get_name()+"/Notes");
            if(!storageDir.exists()) {

                storageDir.mkdir();
            }

            databaseHandler.addExam(examObj);
        }



    }




    public static void saveTreatmentTable(JSONArray response,Context context,int index) {

        DatabaseHandler databaseHandler = new DatabaseHandler(context);

        for (int k = index;k<response.size();k++) {


            org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) response.get(k);
            if(jsonObject.size()==1) {


                break;
            }


            treatment_obj treatmentObj = new treatment_obj();

            treatmentObj.set_pid(Integer.parseInt(String.valueOf(jsonObject.get("PatientId"))));
            treatmentObj.set_version(Integer.parseInt(String.valueOf(jsonObject.get("Version"))));
            treatmentObj.set_date(String.valueOf(jsonObject.get("Date")));
            treatmentObj.set_diagnosis(String.valueOf(jsonObject.get("Diagnosys")));
            treatmentObj.set_implants(String.valueOf(jsonObject.get("Implants")));
            treatmentObj.set_procedure(String.valueOf(jsonObject.get("Score")));
            treatmentObj.set_treatment(String.valueOf(jsonObject.get("Treatment")));


            Patient patient = databaseHandler.getPatient(Integer.parseInt(String.valueOf(jsonObject.get("PatientId"))));

            File storageDir =
                    new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES), "Patient Manager/"+patient.get_name()+"/Notes");
            if(!storageDir.exists()) {

                storageDir.mkdir();
            }

            databaseHandler.addTreatment(treatmentObj);
        }



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
                (activity.getClass() == Exam_Activity.class)||
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






}

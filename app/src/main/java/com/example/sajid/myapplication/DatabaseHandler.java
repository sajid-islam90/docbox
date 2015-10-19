package com.example.sajid.myapplication;

/**
 * Created by sajid on 2/12/2015.
 */
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.json.simple.JSONValue;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import objects.*;



public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "patientManager";

    //  table names
    private static final String TABLE_PATIENT = "patient";
    private static final String TABLE_DOCUMENTS = "documents";
    private static final String TABLE_NOTES = "notes";
    private static final String TABLE_MEDIA = "media";
    private static final String TABLE_HISTORY = "history";
    private static final String TABLE_HISTORY_HIST = "historyHist";
    private static final String TABLE_EXAM = "exam";
    private static final String TABLE_EXAM_HIST = "examHist";
    private static final String TABLE_TREATMENT = "treatment";
    private static final String TABLE_TREATMENT_HIST = "treatmentHist";
    private static final String TABLE_OTHER = "other";
    private static final String TABLE_OTHER_HIST = "otherHist";
    private static final String TABLE_PERSONAL_INFO = "personalInfo";

    //  Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_AGE = "age";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_CONTACT = "contactNumber";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_HEIGHT = "height";
    private static final String KEY_DIAGNOSIS = "diagnosis";
    private static final String KEY_BMP = "bitmapBLOB";
    private static final String KEY_SYNC_STATUS = "syncStatus";
    private static final String KEY_DOC_NAME = "documentName";
    private static final String KEY_DOC_PATH = "documentPath";

    private static final String KEY_DATE= "date";
    private static final String KEY_DATE_LAST_VISIT= "dateLastVisit";
    private static final String KEY_CHIEF_COMPLAINT = "chiefComplaint";
    private static final String KEY_HIST_OF_ILLNESS = "histOfIllness";
    private static final String KEY_HIST = "history";
    private static final String KEY_PERSONAL_HISTORY = "personalHist";
    private static final String KEY_FAMILY_HISTORY = "familyHist";
    private static final String KEY_GENERAL_EXAM = "genEXAM";
    private static final String KEY_LOCAL_EXAM = "locEXAM";
    private static final String KEY_FIELD_NAME= "fieldName";
    private static final String KEY_FIELD_VALUE = "fieldValue";
    private static final String KEY_SECTION = "section";
    private static final String KEY_TREATMENT = "treatment";
    private static final String KEY_PROCEDURE = "procedure";
    private static final String KEY_IMPLANT = "implant";
    private static final String KEY_SCORE = "score";
    private static final String KEY_REMARK = "remark";
    private static final String KEY_VERSION = "version";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
       String CREATE_PATIENTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_PATIENT + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT," + KEY_DIAGNOSIS + " TEXT,"
                + KEY_AGE + " TEXT,"+KEY_DATE_LAST_VISIT + " TEXT,"+KEY_DATE + " TEXT," + KEY_GENDER + " TEXT," + KEY_HEIGHT + " TEXT, " + KEY_BMP +" BLOB,"+ KEY_SYNC_STATUS + " TEXT " +")";
        db.execSQL(CREATE_PATIENTS_TABLE);

        String CREATE_DOCUMENTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_DOCUMENTS + "("
                + KEY_ID + " INTEGER ," +KEY_DATE + " TEXT,"  +KEY_DOC_NAME + " TEXT," + KEY_DOC_PATH + " TEXT, " + KEY_BMP +" BLOB, "+ KEY_SYNC_STATUS + " TEXT "  +")";
        db.execSQL(CREATE_DOCUMENTS_TABLE);



        String CREATE_HISTORY_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_HISTORY + "("
                + KEY_ID + " INTEGER ,"+KEY_DATE+" TEXT ,"+KEY_VERSION+" INTEGER ," + KEY_HIST+ " TEXT, "+ KEY_HIST_OF_ILLNESS + " TEXT,"
                + KEY_PERSONAL_HISTORY +" TEXT, "+KEY_FAMILY_HISTORY+" TEXT, " + KEY_SYNC_STATUS + " TEXT " +")";
        db.execSQL(CREATE_HISTORY_TABLE);

       String CREATE_HISTORY_HIST_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_HISTORY_HIST + "("
                + KEY_ID + " INTEGER ,"+KEY_DATE+" TEXT ,"+KEY_VERSION+" INTEGER ,"+ KEY_HIST+ " TEXT, "+ KEY_HIST_OF_ILLNESS + " TEXT,"
                + KEY_PERSONAL_HISTORY +" TEXT, "+KEY_FAMILY_HISTORY+" TEXT, " + KEY_SYNC_STATUS + " TEXT " +")";
        db.execSQL(CREATE_HISTORY_HIST_TABLE);


        String CREATE_EXAM_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_EXAM + "("
                + KEY_ID + " INTEGER ,"+KEY_DATE+" TEXT ,"+KEY_VERSION+" INTEGER ," + KEY_GENERAL_EXAM + " TEXT," + KEY_LOCAL_EXAM+ " TEXT, "  + KEY_SYNC_STATUS + " TEXT " +")";
        db.execSQL(CREATE_EXAM_TABLE);

        String CREATE_EXAM_HIST_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_EXAM_HIST + "("
                + KEY_ID + " INTEGER ,"+KEY_DATE+" TEXT ,"+KEY_VERSION+" INTEGER ," + KEY_GENERAL_EXAM + " TEXT," + KEY_LOCAL_EXAM+ " TEXT, "  + KEY_SYNC_STATUS + " TEXT " +")";
        db.execSQL(CREATE_EXAM_HIST_TABLE);



        String CREATE_TREATMENT_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_TREATMENT + "("
                + KEY_ID + " INTEGER ,"+KEY_DATE+" TEXT ,"+KEY_VERSION+" INTEGER ," + KEY_DIAGNOSIS + " TEXT," + KEY_TREATMENT + " TEXT," + KEY_PROCEDURE + " TEXT," + KEY_IMPLANT+ " TEXT, "  + KEY_SYNC_STATUS + " TEXT " +")";
        db.execSQL(CREATE_TREATMENT_TABLE);

        String CREATE_TREATMENT_HIST_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_TREATMENT_HIST + "("
                + KEY_ID + " INTEGER ,"+KEY_DATE+" TEXT ,"+KEY_VERSION+" INTEGER ," + KEY_DIAGNOSIS + " TEXT," + KEY_TREATMENT + " TEXT," + KEY_PROCEDURE + " TEXT," + KEY_IMPLANT+ " TEXT, "  + KEY_SYNC_STATUS + " TEXT " +")";
        db.execSQL(CREATE_TREATMENT_HIST_TABLE);


        String CREATE_OTHER_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_OTHER + "("
                + KEY_ID + " INTEGER ,"+KEY_DATE+" TEXT ,"+KEY_VERSION+" INTEGER ," + KEY_FIELD_NAME + " TEXT," + KEY_FIELD_VALUE+ " TEXT, "  + KEY_SYNC_STATUS + " TEXT " +")";
        db.execSQL(CREATE_OTHER_TABLE);

        String CREATE_OTHER_HIST_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_OTHER_HIST + "("
                + KEY_ID + " INTEGER ,"+KEY_DATE+" TEXT ,"+KEY_VERSION+" INTEGER ," + KEY_FIELD_NAME + " TEXT," + KEY_FIELD_VALUE+ " TEXT, "  + KEY_SYNC_STATUS + " TEXT " +")";
        db.execSQL(CREATE_OTHER_HIST_TABLE);



       String CREATE_DOCUMENTS_MEDIA_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_MEDIA + "("
                + KEY_ID + " INTEGER ," + KEY_DOC_NAME + " TEXT,"+ KEY_VERSION + " TEXT,"+ KEY_SECTION + " TEXT," + KEY_DOC_PATH + " TEXT, " + KEY_BMP +" BLOB, " + KEY_SYNC_STATUS + " TEXT " +")";
        db.execSQL(CREATE_DOCUMENTS_MEDIA_TABLE);


        String CREATE_PERSONAL_INFO_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_PERSONAL_INFO + "("
                + KEY_ID + " INTEGER ," + KEY_NAME + " TEXT,"+ KEY_CONTACT + " TEXT,"+ KEY_EMAIL + " TEXT," + KEY_PASSWORD + " TEXT, " + KEY_DOC_PATH +" TEXT" +")";
        db.execSQL(CREATE_PERSONAL_INFO_TABLE);

        /*String RENAME_TABLE = "DROP TABLE "+TABLE_DOCUMENTS;
        db.execSQL(RENAME_TABLE);
       /* db.execSQL(CREATE_DOCUMENTS_TABLE);
        String RENAME_TABLE_COLUMN = "INSERT INTO documents(id, documentName, documentPath, bitmapBLOB )\n" +
                "SELECT id, documentName,documentPath\n" +
                "FROM documents_temp";*/

        /*db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY_HIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
       // db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDIA);
        /*db.execSQL("DROP TABLE IF EXISTS " + TABLE_TREATMENT_HIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OTHER_HIST);


     /* String UPDATE_PATIENTS_TABLE = "DROP TABLE " + TABLE_PERSONAL_INFO; //+ " ADD " +KEY_VERSION +" TEXT";
        db.execSQL(UPDATE_PATIENTS_TABLE);*/

        File storageDir =
                new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES), "Patient Manager");
        if(!storageDir.exists())
            storageDir.mkdir();



    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PATIENT);

        // Create tables again
        onCreate(db);
    }


    //Personal Deatil Functions

    public personal_obj getPersonalInfo()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        personal_obj personalObj = new personal_obj();
        String strSQL = "SELECT * FROM " + TABLE_PERSONAL_INFO;
        Cursor cursor = db.rawQuery(strSQL,null);
        if(cursor.getCount()!=0) {
            cursor.moveToFirst();
            personalObj = utility.cursorToPersonal(cursor);

        }
        return personalObj;

    }

    public String getPassword(String  email)
    {
        String password = "";
        SQLiteDatabase db = this.getWritableDatabase();
        String strSQL = "SELECT "+KEY_PASSWORD+" FROM " + TABLE_PERSONAL_INFO+" WHERE "+KEY_EMAIL+" = "+email;
        Cursor cursor = db.rawQuery(strSQL, null);
        if(cursor.getCount()!=0) {
            cursor.moveToFirst();
           password = cursor.getString(cursor.getColumnIndex(KEY_PASSWORD));

        }
        return password;


    }
    public int getCustomerId(String  email)
    {
        int CustomerId = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        String strSQL = "SELECT "+KEY_ID+" FROM " + TABLE_PERSONAL_INFO+" WHERE "+KEY_EMAIL+" = "+email;
        Cursor cursor = db.rawQuery(strSQL, null);
        if(cursor.getCount()!=0) {
            cursor.moveToFirst();
            CustomerId = cursor.getInt(cursor.getColumnIndex(KEY_ID));

        }
        return CustomerId;


    }

    public void addPersonalInfo(personal_obj personalObj)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values = utility.personalInfoTOValues(personalObj, values);
        db.insert(TABLE_PERSONAL_INFO,null,values);
        db.close();


    }

    public void updatePersonalInfo(String field,String value)


    {
        String SQL = "UPDATE "+TABLE_PERSONAL_INFO+" SET "+field+" ='"+value;
        personal_obj personalObj = getPersonalInfo();
        String email = personalObj.get_email();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(field, value);

         db.update(TABLE_PERSONAL_INFO, args, KEY_EMAIL + "= '" + email + "'", null) ;

    }








    //CLINICAL NOTES FUNCTIONS
    public void addNotes(notes_obj notes)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values = utility.notesTOValues(notes,values);
        // Inserting Row
        long ret = db.insert(TABLE_NOTES, null, values);
        db.close(); // Closing database connection


    }

    public int updateNote(notes_obj notes) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values = utility.notesTOValues(notes,values);
        String where = KEY_ID +"=? and " + KEY_VERSION + "=?";
        String[] whereArgs = new String[] {String.valueOf(notes.get_id()), String.valueOf(notes.get_version())};

        // updating row
        return  db.update(TABLE_NOTES, values, where,whereArgs);

    }


    public int getCurrentVersion(int pid)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        int version = 0;
        String strSQL = "SELECT "+KEY_VERSION+"   FROM " + TABLE_HISTORY+" WHERE "+KEY_ID+" = "+pid;
        Cursor cursor = db.rawQuery(strSQL,null);

        if(cursor.getCount()!=0) {
            cursor.moveToLast();
            version = cursor.getInt(0);
        }

        return version;
    }


    public ArrayList<String> getAllNotesDates(int pid)
    {
        ArrayList<String> dates = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT "+KEY_DATE+" FROM "+TABLE_HISTORY_HIST+" WHERE "+KEY_ID+" = "+pid;

        Cursor cursor = db.rawQuery(sql,null);
        int c=1;
        if(cursor.getCount()!=0) {

            while (cursor.moveToNext())
            {
                dates.add("Version"+ c++ +" date : "+cursor.getString(0));
            }

        }



        return dates;
    }

    public int getCurrentMediaVersion(int pid)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        int version = 0;
        String strSQL = "SELECT "+KEY_VERSION+"   FROM " + TABLE_MEDIA+" WHERE "+KEY_ID+" = "+pid;
        Cursor cursor = db.rawQuery(strSQL,null);

        if(cursor.getCount()!=0) {
            cursor.moveToLast();
            version = cursor.getInt(0);
        }



        return version;
    }


    public notes_obj getLatestNote(int pid)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        notes_obj notesObj = new notes_obj();
        int version = getCurrentVersion(pid);
        String strSQL = " SELECT * FROM "+ TABLE_NOTES+" WHERE "+KEY_ID+" = "+pid+" AND "+KEY_VERSION+" = "+version;
        Cursor cursor = db.rawQuery(strSQL,null);
        if(cursor.getCount()!=0) {
            cursor.moveToFirst();
           notesObj = utility.cursorToNotes(cursor);

        }
        return notesObj;
    }


    //History notes functions

    public void addHistory(history_obj historyObj)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID,historyObj.get_pid());
        values.put(KEY_DATE,historyObj.get_date());
        values.put(KEY_HIST_OF_ILLNESS,historyObj.get_past_illness());
        values.put(KEY_HIST,historyObj.get_present_illness());
        values.put(KEY_FAMILY_HISTORY,historyObj.get_family_hist());
        values.put(KEY_PERSONAL_HISTORY,historyObj.get_personal_hist());
        values.put(KEY_VERSION,historyObj.get_version());
        values.put(KEY_SYNC_STATUS,"0");
        // Inserting Row
        db.insert(TABLE_HISTORY, null, values);
        db.insert(TABLE_HISTORY_HIST, null, values);
        int previousVersion = historyObj.get_version()-1;
        if(previousVersion!=0) {
            String strSQL = " DELETE FROM " + TABLE_HISTORY + " WHERE " + KEY_ID + " = " + historyObj.get_pid() + " AND " + KEY_VERSION + " <= " + previousVersion;
            db.execSQL(strSQL);
        }
        db.close(); // Closing database connection


    }

    public history_obj getVersionedHistNote(int pid,int version)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        history_obj histObj = new history_obj();

        String strSQL = " SELECT * FROM "+ TABLE_HISTORY_HIST+" WHERE "+KEY_ID+" = "+pid+" AND "+KEY_VERSION+" = "+version;
        Cursor cursor = db.rawQuery(strSQL,null);
        if(cursor.getCount()!=0) {
            cursor.moveToFirst();
            histObj = utility.cursorToHistory(cursor);

        }
        return histObj;
    }




    public history_obj getVersionedHistNote(int pid)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        history_obj histObj = new history_obj();
       int version = getCurrentVersion(pid);
        String strSQL = " SELECT * FROM "+ TABLE_HISTORY+" WHERE "+KEY_ID+" = "+pid+" AND "+KEY_VERSION+" = "+version;
        Cursor cursor = db.rawQuery(strSQL,null);
        if(cursor.getCount()!=0) {
            cursor.moveToFirst();
           histObj = utility.cursorToHistory(cursor);

        }
        return histObj;
    }



    //EXAM notes functions

   public void addExam(exam_obj examObj)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, examObj.get_pid());
        values.put(KEY_DATE, examObj.get_date());
        values.put(KEY_VERSION,examObj.get_version());
        values.put(KEY_GENERAL_EXAM,examObj.get_gen_exam());
        values.put(KEY_LOCAL_EXAM, examObj.get_local_exam());
        values.put(KEY_SYNC_STATUS, 0);

       long i =  db.insert(TABLE_EXAM, null, values);
       long j =  db.insert(TABLE_EXAM_HIST, null, values);
        int previousVersion = examObj.get_version()-1;
        if(previousVersion!=0) {
            String strSQL = " DELETE FROM " + TABLE_EXAM+ " WHERE " + KEY_ID + " = " + examObj.get_pid() + " AND " + KEY_VERSION + " <= " + previousVersion;
            db.execSQL(strSQL);
        }
        db.close(); // Closing database connection


    }

    public exam_obj getLatestExamNote(int pid , int version)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        exam_obj examObj = new exam_obj();
        //int version = getCurrentVersion(pid);
        String strSQL = " SELECT * FROM "+ TABLE_EXAM_HIST+" WHERE "+KEY_ID+" = "+pid+" AND "+KEY_VERSION+" = "+version;
        Cursor cursor = db.rawQuery(strSQL,null);
        if(cursor.getCount()!=0) {
            cursor.moveToFirst();
            examObj = utility.cursorToExam(cursor);

        }
        return examObj;
    }


    //Treatment notes functions

    public void addTreatment(treatment_obj treatmentObj)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, treatmentObj.get_pid());
        values.put(KEY_DATE, treatmentObj.get_date());
        values.put(KEY_VERSION,treatmentObj.get_version());
        values.put(KEY_DIAGNOSIS,treatmentObj.get_diagnosis());
        values.put(KEY_TREATMENT, treatmentObj.get_treatment());
        values.put(KEY_PROCEDURE,treatmentObj.get_procedure());
        values.put(KEY_IMPLANT, treatmentObj.get_implants());
        values.put(KEY_SYNC_STATUS, 0);

        long i =  db.insert(TABLE_TREATMENT, null, values);
        long j =  db.insert(TABLE_TREATMENT_HIST, null, values);
        int previousVersion = treatmentObj.get_version()-1;
        if(previousVersion!=0) {
            String strSQL = " DELETE FROM " + TABLE_TREATMENT+ " WHERE " + KEY_ID + " = " + treatmentObj.get_pid() + " AND " + KEY_VERSION + " <= " + previousVersion;
            db.execSQL(strSQL);
        }
        db.close(); // Closing database connection


    }

    public treatment_obj getLatestTreatmentNote(int pid,int version)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        treatment_obj treatmentObj = new treatment_obj();
       // int version = getCurrentVersion(pid);
        String strSQL = " SELECT * FROM "+ TABLE_TREATMENT_HIST+" WHERE "+KEY_ID+" = "+pid+" AND "+KEY_VERSION+" = "+version;
        Cursor cursor = db.rawQuery(strSQL,null);
        if(cursor.getCount()!=0) {
            cursor.moveToFirst();
            treatmentObj = utility.cursorToTreatment(cursor);

        }
        return treatmentObj;
    }


    //Other Notes Functions

    public void addOther(other_obj otherObj)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, otherObj.get_pid());
        values.put(KEY_DATE, otherObj.get_date());
        values.put(KEY_VERSION,otherObj.get_version());
        values.put(KEY_FIELD_NAME,otherObj.get_field_name());
        values.put(KEY_FIELD_VALUE, otherObj.get_field_value());
        values.put(KEY_SYNC_STATUS,0);


        long i =  db.insert(TABLE_OTHER, null, values);
        long j =  db.insert(TABLE_OTHER_HIST, null, values);
        int previousVersion = otherObj.get_version()-1;
        if(previousVersion!=0) {
            String strSQL = " DELETE FROM " + TABLE_OTHER+ " WHERE " + KEY_ID + " = " + otherObj.get_pid() + " AND " + KEY_VERSION + " <= " + previousVersion;
            db.execSQL(strSQL);
        }
        db.close(); // Closing database connection


    }
    public other_obj[] getLatestOtherNote(int pid,int version)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        other_obj otherObj[] = null;
        //int version = getCurrentVersion(pid);
        String strSQL = " SELECT * FROM "+ TABLE_OTHER_HIST+" WHERE "+KEY_ID+" = "+pid+ " AND " + KEY_VERSION + " = " + version;
        Cursor cursor = db.rawQuery(strSQL, null);

        if(cursor.getCount()!=0) {
            cursor.moveToFirst();
             otherObj = new other_obj[cursor.getCount()];
            otherObj = utility.cursorToOther(cursor);

        }
        return otherObj;
    }



    //Media Functions

    public void addMedia(media_obj mediaObj)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, mediaObj.get_pid());
        values.put(KEY_SECTION, mediaObj.get_section());
        values.put(KEY_VERSION,mediaObj.get_version());
        values.put(KEY_DOC_NAME,mediaObj.get_media_name());
        values.put(KEY_DOC_PATH, mediaObj.get_media_path());
        values.put(KEY_BMP, mediaObj.get_bmp());


        long i = db.insert(TABLE_MEDIA, null, values);
        //long j =  db.insert(TABLE_OTHER_HIST, null, values);


        db.close(); // Closing database connection


    }

    public media_obj[] getLatestMediaAdd(int pid,int version,int section)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        media_obj mediaObjs[] = null;
        //int version = getCurrentVersion(pid)-1;
        String strSQL = " SELECT * FROM "+ TABLE_MEDIA+" WHERE "+KEY_ID+" = "+pid
                        + " AND " + KEY_VERSION + " = " + version
                        + " AND " + KEY_SECTION + " = " + section;
        Cursor cursor = db.rawQuery(strSQL, null);

        if(cursor.getCount()>0) {
            cursor.moveToFirst();
            mediaObjs = new media_obj[cursor.getCount()];
            mediaObjs = utility.cursorToMedia(cursor);

        }
        return mediaObjs;
    }
   public void deleteMedia(String path)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        File file = new File(path);
        boolean a = file.delete();


        db.delete(TABLE_MEDIA, KEY_DOC_PATH+ " = ?",
                new String[] { String.valueOf(path) });
        db.close();


    }
    void deleteMedia(int version)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String path;
        String strSQL = " SELECT * FROM "+ TABLE_MEDIA+" WHERE "+KEY_VERSION+" = "+version;
        Cursor cursor = db.rawQuery(strSQL,null);

        while (cursor.moveToNext()) {

            path = cursor.getString(cursor.getColumnIndex("documentPath"));
            File file = new File(path);
            boolean a = file.delete();


        }
        db.delete(TABLE_MEDIA, KEY_VERSION + " = ?",
                new String[]{String.valueOf(version)});
        db.close();



    }


   /* media_obj getSearchMedia(int id,ArrayList<Item> itemsArrayList)
    {
        media_obj mediaObj = new media_obj();
        int media_id = itemsArrayList.get(id).getPatient_id();
        String media_path = itemsArrayList.get(id).getDiagnosis();
        mediaObj = this.getSingleMedia(media_id,media_path);

        return  mediaObj;
    }



    media_obj getSingleMedia(int id,String pathSearch) {
        SQLiteDatabase db = this.getReadableDatabase();
        String path = new String();

        int pos = 0;
        String strSQL = "SELECT  * FROM " + TABLE_MEDIA+" WHERE "+KEY_ID+" = "+id;
        media_obj mediaObj = new media_obj();
        Cursor cursor = db.rawQuery(strSQL,null);
        int cur = cursor.getCount();
        if (cur != 0) {
            cursor.moveToFirst();

            for(int i = 1;i<=cur;i++) {
                pos = Integer.parseInt(cursor.getString(0));
                path = cursor.getString(2);
                if((pos == id)&&(path.equals(pathSearch))) {
                    String name = cursor.getString(1);

                    int id1 = Integer.parseInt(cursor.getString(0));
                    byte[] bmp = cursor.getBlob(2);
                    mediaObj = new media_obj();
                    mediaObj.set_pid(id1);
                    mediaObj.set_media_name(name);
                    mediaObj.set_media_path(path);
                    mediaObj.set_bmp(bmp);

                    mediaObj.set_bmp(cursor.getBlob(3));

                    break;
                }
                if(i !=cur)
                    cursor.moveToNext();


            }



        }

        db.close();
        return mediaObj;
    }
*/


    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
    //DOCUMENTS FUNCTIONS
    // adding documents to a patient
    public void addDocument(document_obj document)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID,document.get_id());
        values.put(KEY_DOC_NAME,document.get_doc_name());
        values.put(KEY_DOC_PATH,document.get_doc_path());
        values.put(KEY_BMP,document.get_bmp());
        values.put(KEY_DATE,document.get_date());
        values.put(KEY_SYNC_STATUS,"0");

        // Inserting Row
        db.insert(TABLE_DOCUMENTS, null, values);
        db.close(); // Closing database connection


    }




    public document_obj getSearchDocument(int id,ArrayList<Item> itemsArrayList)
    {
        document_obj doc_obj = new document_obj();
        int doc_id = itemsArrayList.get(id).getPatient_id();
        String doc_path = itemsArrayList.get(id).getDiagnosis();
        doc_obj = this.getSingleDocument(doc_id,doc_path);

        return  doc_obj;
    }


    public document_obj getSingleDocument(int id,String pathSearch) {
        SQLiteDatabase db = this.getReadableDatabase();
        String path = new String();

        int pos = 0;
        String strSQL = "SELECT  * FROM " + TABLE_DOCUMENTS+" WHERE "+KEY_ID+" = "+id+" AND "+ KEY_DOC_PATH+" = '"+pathSearch+"'";
        document_obj doc_obj = new document_obj();
        Cursor cursor = null;
        try {
             cursor = db.rawQuery(strSQL, null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        int cur = cursor.getCount();
        if (cur != 0) {
            cursor.moveToFirst();

            for(int i = 1;i<=cur;i++) {
                pos = Integer.parseInt(cursor.getString(0));
                path = cursor.getString(cursor.getColumnIndex("documentPath"));
                if((pos == id)&&(path.equals(pathSearch))) {
                    String name = cursor.getString(1);

                    int id1 = Integer.parseInt(cursor.getString(0));
                    byte[] bmp = cursor.getBlob(2);
                    doc_obj = new document_obj(id1,
                            name, path,bmp);
                    doc_obj.set_bmp(cursor.getBlob(3));

                    break;
                }
                if(i !=cur)
                    cursor.moveToNext();


            }



        }

        db.close();
        return doc_obj;
    }


    public List<document_obj> getDocuments(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<document_obj> docList = new ArrayList<document_obj>();
        int pos = 0;
        String strSQL = "SELECT  * FROM " + TABLE_DOCUMENTS+" WHERE "+KEY_ID+" = "+id+" AND "+ KEY_SYNC_STATUS+" != 3";
        document_obj doc_obj = new document_obj();
        Cursor cursor = db.rawQuery(strSQL,null);
        int cur = cursor.getCount();
        if (cur != 0) {
            cursor.moveToFirst();

            for(int i = 1;i<=cur;i++) {
                pos = Integer.parseInt(cursor.getString(0));
                if(pos == id) {
                    String name = cursor.getString(cursor.getColumnIndex("date"));
                    String path = cursor.getString(cursor.getColumnIndex("documentPath"));
                    byte[] bmp = cursor.getBlob(cursor.getColumnIndex("bitmapBLOB"));
                    int id1 = Integer.parseInt(cursor.getString(0));
                    doc_obj = new document_obj(id1,
                            name, path,bmp);
                    docList.add(doc_obj);
                    //break;
                }
                if(i !=cur)
                    cursor.moveToNext();


            }


        }
        db.close();
        return docList;
    }

    public int updateDocument(document_obj doc_obj,String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        int id = doc_obj.get_id();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, doc_obj.get_id());
        values.put(KEY_DOC_NAME, doc_obj.get_doc_name());
        values.put(KEY_DOC_PATH,doc_obj.get_doc_path());
        values.put(KEY_BMP, doc_obj.get_bmp());
        values.put(KEY_SYNC_STATUS, status);


        // updating row
        return db.update(TABLE_DOCUMENTS, values, KEY_DOC_PATH + " = ?",
                new String[] { doc_obj.get_doc_path() });
    }


    public void deleteDocument(document_obj doc_obj)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        File file = new File(doc_obj.get_doc_path());
        boolean a = file.delete();


        db.delete(TABLE_DOCUMENTS, KEY_DOC_PATH + " = ?",
                new String[]{String.valueOf(doc_obj.get_doc_path())});
        db.close();


    }

    public void removeDocument(document_obj documentObj)
    {
        updateDocument(documentObj, "3");
        updatePatient(getPatient(documentObj.get_id()));

    }

    //patient functions
   public void addPatient(Patient patient) {
        SQLiteDatabase db = this.getWritableDatabase();Calendar c = Calendar.getInstance();
       SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
       String formattedDate = df.format(c.getTime());

        String a = patient.get_diagnosis();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, patient.get_name());
        values.put(KEY_AGE, patient.get_age());
        values.put(KEY_GENDER, patient.get_gender());
        values.put(KEY_HEIGHT, patient.get_height());
        values.put(KEY_BMP,patient.get_bmp());
        values.put(KEY_DIAGNOSIS,patient.get_diagnosis());
       values.put(KEY_DATE_LAST_VISIT,formattedDate );
       values.put(KEY_DATE,formattedDate );
       values.put(KEY_SYNC_STATUS,0);

        // Inserting Row
        db.insert(TABLE_PATIENT, null, values);
        db.close(); // Closing database connection
    }



    // Getting single contact
    public Patient getPatient(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        int pos = 0;
        Patient patient = null;
        String strSQL = "SELECT  * FROM " + TABLE_PATIENT+ " WHERE "+KEY_ID +" = "+id;
        String status;
        Cursor cursor = db.rawQuery(strSQL,null);
                /*db.query(TABLE_PATIENT, new String[] { KEY_ID,
                        KEY_NAME,KEY_AGE,KEY_GENDER,KEY_HEIGHT,KEY_BMP }, KEY_ID + "=?",new String[] { String.valueOf(id) }, null, null, null, null);*/
        if (cursor != null) {
             cursor.moveToFirst();
           // cursor.moveToPosition(id);
            patient = utility.cursorToPatient(cursor);
             status = cursor.getString(cursor.getColumnIndex(KEY_SYNC_STATUS));
            System.out.print(status);


        }
        /*Patient patient = new Patient(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getBlob(5),cursor.getString(6));*/


        // return contact
        db.close();
        return patient;
    }



    public Patient getSearchPatient(int id,ArrayList<Item> itemsArrayList)
    {
        Patient patient = new Patient();
        int patient_id = itemsArrayList.get(id).getPatient_id();
        patient =  this.getPatientForProfile(patient_id);
        return patient;

    }

    public List<String> getAllPatientNames()
    {
       List<Patient> list =  this.getAllPatient();
        List<String> names = new ArrayList<String>();
        for(int i = 1;i<= list.size();i++)
        {
            names.add(i,list.get(i).get_name()) ;
        }

        return names;
    }

    public Patient getLatestPatient()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Patient patient = new Patient();
        String strSQL = "SELECT  * FROM " + TABLE_PATIENT;
        Cursor cursor = db.rawQuery(strSQL, null);
        if(cursor != null)
        {
            cursor.moveToLast();
            patient = utility.cursorToPatient(cursor);

        }

        db.close();
        return patient;
    }









    // Getting All Contacts
    public List<Patient> getAllPatient() {
        List<Patient> patientList = new ArrayList<Patient>();
        // Select All Query
        String selectQuery = "SELECT  * FROM patient WHERE "+KEY_SYNC_STATUS +" != 3" ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int cur = cursor.getCount();
        // looping through all rows and adding to list
        if (cur != 0) {
            cursor.moveToFirst();
            do {

                Patient patient = new Patient();

                patient = utility.cursorToPatient(cursor);
                String s = cursor.getString(cursor.getColumnIndex(KEY_SYNC_STATUS));

                // Adding contact to list
                patientList.add(patient);
            } while (cursor.moveToNext());
        }

        db.close();
        return patientList;
    }




    public Patient getPatientForProfile(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        int pos = 0;
        Patient patient = null;
        String strSQL = "SELECT  * FROM " + TABLE_PATIENT;

        Cursor cursor = db.rawQuery(strSQL,null);
                /*db.query(TABLE_PATIENT, new String[] { KEY_ID,
                        KEY_NAME,KEY_AGE,KEY_GENDER,KEY_HEIGHT,KEY_BMP }, KEY_ID + "=?",new String[] { String.valueOf(id) }, null, null, null, null);*/
        if (cursor != null) {
            cursor.moveToFirst();
           String d = cursor.getString(cursor.getColumnIndex(KEY_SYNC_STATUS));
            pos =  Integer.parseInt(cursor.getString(0));
            while (pos != id) {

                cursor.moveToNext();
                pos =  Integer.parseInt(cursor.getString(0));
            }
            /*patient = new Patient(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1), cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getBlob(5),cursor.getString(6));*/
            patient = utility.cursorToPatient(cursor);
            /*patient = new Patient(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))),
                    cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                    cursor.getString(cursor.getColumnIndex(KEY_AGE)),cursor.getString(cursor.getColumnIndex(KEY_GENDER)),
                    cursor.getString(cursor.getColumnIndex(KEY_HEIGHT)),cursor.getBlob(cursor.getColumnIndex(KEY_BMP)),
                    cursor.getString(cursor.getColumnIndex(KEY_DIAGNOSIS)));*/
        }
        // cursor.moveToFirst();
        //cursor.moveToPosition(id);





        // return contact
        db.close();
        return patient;
    }


    // Updating single contact
    public int updatePatient(Patient patient) {

        SQLiteDatabase db = this.getWritableDatabase();
       int id = patient.get_id();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, patient.get_name());
        values.put(KEY_AGE, patient.get_age());
        values.put(KEY_GENDER, patient.get_gender());
        values.put(KEY_HEIGHT, patient.get_height());
        values.put(KEY_BMP,patient.get_bmp());
        values.put(KEY_DIAGNOSIS, patient.get_diagnosis());
        values.put(KEY_DATE_LAST_VISIT, patient.get_last_seen_date());
        values.put(KEY_SYNC_STATUS, "5");

        // updating row
        return db.update(TABLE_PATIENT, values, KEY_ID + " = ?",
                new String[] { String.valueOf(patient.get_id()) });
    }
    public int updatePatient(Patient patient,int syncStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        int id = patient.get_id();
        ContentValues values = new ContentValues();
        String syncingStatus = Integer.toString(syncStatus);
        values.put(KEY_NAME, patient.get_name());
        values.put(KEY_AGE, patient.get_age());
        values.put(KEY_GENDER, patient.get_gender());
        values.put(KEY_HEIGHT, patient.get_height());
        values.put(KEY_BMP,patient.get_bmp());
        values.put(KEY_DIAGNOSIS, patient.get_diagnosis());
        values.put(KEY_DATE_LAST_VISIT, patient.get_last_seen_date());
        values.put(KEY_SYNC_STATUS,syncingStatus);

        // updating row
        return db.update(TABLE_PATIENT, values, KEY_ID + " = ?",
                new String[] { String.valueOf(patient.get_id()) });
    }

    public void removePatient(Patient patient)
    {
        this.updatePatient(patient, 3);


    }

    // Deleting patient
    public void deletePatient() {


        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM "+TABLE_PATIENT +" WHERE "+KEY_SYNC_STATUS +" = '3'";
        Cursor cursor = db.rawQuery(sql,null);
        if (cursor .getCount()>0) {
            cursor.moveToFirst();
            do {

                Patient patient = new Patient();

                patient = utility.cursorToPatient(cursor);
                db.delete(TABLE_PATIENT, KEY_ID + " = ?",
                        new String[]{String.valueOf(patient.get_id())});
                db.delete(TABLE_DOCUMENTS, KEY_ID + " = ?",
                        new String[]{String.valueOf(patient.get_id())});
                db.delete(TABLE_EXAM, KEY_ID + " = ?",
                        new String[]{String.valueOf(patient.get_id())});
                db.delete(TABLE_EXAM_HIST, KEY_ID + " = ?",
                        new String[]{String.valueOf(patient.get_id())});
                db.delete(TABLE_HISTORY, KEY_ID + " = ?",
                        new String[]{String.valueOf(patient.get_id())});
                db.delete(TABLE_HISTORY_HIST, KEY_ID + " = ?",
                        new String[]{String.valueOf(patient.get_id())});
                db.delete(TABLE_MEDIA, KEY_ID + " = ?",
                        new String[]{String.valueOf(patient.get_id())});
                db.delete(TABLE_OTHER, KEY_ID + " = ?",
                        new String[]{String.valueOf(patient.get_id())});
                db.delete(TABLE_OTHER_HIST, KEY_ID + " = ?",
                        new String[]{String.valueOf(patient.get_id())});
                db.delete(TABLE_TREATMENT, KEY_ID + " = ?",
                        new String[]{String.valueOf(patient.get_id())});
                db.delete(TABLE_TREATMENT_HIST, KEY_ID + " = ?",
                        new String[]{String.valueOf(patient.get_id())});

                // Adding contact to list

            } while (cursor.moveToNext());


            db.close();
        }
    }




    // Getting contacts Count
    public int getPatientsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_PATIENT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        db.close();
        return cursor.getCount();
    }

    public List<Patient> search(String searchString)
    {
        List<Patient> patientList = new ArrayList<Patient>();
        String searchNameQuery = "SELECT * FROM " + TABLE_PATIENT +
                " WHERE " + KEY_NAME + " LIKE '%" + searchString + "%'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(searchNameQuery,null);
        if (cursor.moveToFirst()) {
            do {
                Patient patient = new Patient();
                patient = utility.cursorToPatient(cursor);
               /* patient.set_id(Integer.parseInt(cursor.getString(0)));
                patient.set_name(cursor.getString(1));
                patient.set_age(cursor.getString(2));
                patient.set_gender(cursor.getString(3));
                patient.set_height(cursor.getString(4));
                patient.set_bmp(cursor.getBlob(5));
                patient.set_diagnosis(cursor.getString(6));*/
                // Adding contact to list
                patientList.add(patient);
            } while (cursor.moveToNext());
        }
        db.close();
        return patientList;
    }



    /**
     * Get list of Users from SQLite DB as Array List
     * @return
     */
    public ArrayList<HashMap<String, String>> getAllSyncUsers() {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM patient WHERE "+KEY_SYNC_STATUS +" != 1"  ;
        String customerIdQuery = "SELECT id FROM personalInfo";

        SQLiteDatabase database = this.getWritableDatabase();

        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("userId", cursor.getString(0));
                map.put("userName", cursor.getString(1));
                String status = cursor.getString(cursor.getColumnIndex(KEY_SYNC_STATUS));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        return wordList;
    }

    /**
     * Compose JSON out of SQLite records
     * @return
     */

    public String composeJSONfromEmailPassword()
    {
        SQLiteDatabase database = this.getWritableDatabase();
        String Sql = "Select email,password FROM personalInfo" ;
        Cursor cursor = database.rawQuery(Sql, null);
        HashMap<String, String> map = new HashMap<String, String>();
        if (cursor.moveToFirst())
        {
            map.put( cursor.getString(0), cursor.getString(1));
        }
        database.close();
        String s1 = null;

        StringWriter out = new StringWriter();
        try {
            JSONValue.writeJSONString(map, out);
            s1 = out.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return s1;
    }
    public String composeJSONfromSQLite(){
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM patient ";
        personal_obj personalObj = this.getPersonalInfo();
        String c = String.valueOf(personalObj.get_customerId());
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(cursor.getString(1), c);
                //map.put("customerId", "27");
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();



        String s1 = null;

        StringWriter out = new StringWriter();
        try {
            JSONValue.writeJSONString(wordList, out);
            s1 = out.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Gson gson = new GsonBuilder().create();
        //Use GSON to serialize Array List to JSON
        return s1;
    }
    public String composeJSONforUpdatePatient(){
        ArrayList<ArrayList<String>> patientList;
        patientList = new ArrayList<ArrayList<String>>();
        String selectQuery = "SELECT  * FROM patient WHERE "+KEY_SYNC_STATUS+" = '5'" ;//KEY_SYNC_STATUS+" = '5' for update
        personal_obj personalObj = this.getPersonalInfo();
        String c = String.valueOf(personalObj.get_customerId());
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                ArrayList<String> list = new ArrayList<>();
                Patient patient = new Patient();
                patient = utility.cursorToPatient(cursor);
                this.updatePatient(patient,1);
                list = utility.cursorToPatientArray(cursor,c);
                patientList.add(list);

                //map.put("customerId", "27");

            } while (cursor.moveToNext());
        }
        database.close();



        String s1 = null;

        StringWriter out = new StringWriter();
        try {
            JSONValue.writeJSONString(patientList, out);
            s1 = out.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Gson gson = new GsonBuilder().create();
        //Use GSON to serialize Array List to JSON
        return s1;
    }

    public String composeJSONfromSQLitePatient(){
        ArrayList<ArrayList<String>> patientList;
        patientList = new ArrayList<ArrayList<String>>();
        String selectQuery = "SELECT  * FROM patient WHERE "+KEY_SYNC_STATUS+" != '1'" ;
        personal_obj personalObj = this.getPersonalInfo();
        String c = String.valueOf(personalObj.get_customerId());
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                ArrayList<String> list = new ArrayList<>();
                Patient patient = new Patient();
                patient = utility.cursorToPatient(cursor);
                String syncStatus = cursor.getString(cursor.getColumnIndex(KEY_SYNC_STATUS));
                if(!syncStatus.equals("3"))
                    this.updatePatient(patient,1);
                   list = utility.cursorToPatientArray(cursor,c);
                    patientList.add(list);

                //map.put("customerId", "27");

            } while (cursor.moveToNext());
        }
        database.close();



        String s1 = null;

        StringWriter out = new StringWriter();
        try {
            JSONValue.writeJSONString(patientList, out);
            s1 = out.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Gson gson = new GsonBuilder().create();
        //Use GSON to serialize Array List to JSON
        return s1;
    }
    public String composeJSONfromSQLitePatient(String pid){
        ArrayList<ArrayList<String>> patientList;
        patientList = new ArrayList<ArrayList<String>>();
        String selectQuery = "SELECT  * FROM patient WHERE "+KEY_SYNC_STATUS+" != '1' AND "+KEY_ID+" = "+pid ;
        Patient patient = new Patient();
        patient = this.getPatient(Integer.parseInt(pid));
        personal_obj personalObj = this.getPersonalInfo();
        String c = String.valueOf(personalObj.get_customerId());
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                ArrayList<String> list = new ArrayList<>();

                patient = utility.cursorToPatient(cursor);
                String syncStatus = cursor.getString(cursor.getColumnIndex(KEY_SYNC_STATUS));
                if(!syncStatus.equals("3"))
                    this.updatePatient(patient,1);
                list = utility.cursorToPatientArray(cursor,c);
                patientList.add(list);

                //map.put("customerId", "27");

            } while (cursor.moveToNext());
        }
        database.close();



        String s1 = null;

        StringWriter out = new StringWriter();
        try {
            JSONValue.writeJSONString(patientList, out);
            s1 = out.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Gson gson = new GsonBuilder().create();
        //Use GSON to serialize Array List to JSON
        return s1;
    }



    public String composeJSONfromSQLiteDocuments(String pid){
        ArrayList<ArrayList<String>> documentsList;
        documentsList = new ArrayList<ArrayList<String>>();
        String selectQuery = "SELECT  * FROM documents WHERE "+KEY_SYNC_STATUS+" != '1' AND "+KEY_ID+" = "+pid ;
        document_obj documentObj = new document_obj();
        ArrayList<String> docPaths = new ArrayList<>();
       // patient = this.getPatient(Integer.parseInt(pid));
        personal_obj personalObj = this.getPersonalInfo();
        String c = String.valueOf(personalObj.get_customerId());
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                ArrayList<String> list = new ArrayList<>();
                list = utility.cursorToDocumentsArray(cursor, c);
                documentObj = utility.cursorToDocument(cursor);
                if (docPaths != null) {
                    docPaths.add(documentObj.get_doc_path());
                }


                if (cursor.getString(cursor.getColumnIndex(KEY_SYNC_STATUS)).equals("3"))
                {
                    deleteDocument(documentObj);
                }
                String docPath = list.get(4);
               updateDocument(documentObj,"1");

               // documentObj = utility.cursorToDocument(cursor);
               // String syncStatus = cursor.getString(cursor.getColumnIndex(KEY_SYNC_STATUS));
               /* if(!syncStatus.equals("3"))
                    this.updatePatient(patient,1);*/
              //  list = utility.cursorToPatientArray(cursor,c);
                documentsList.add(list);

                //map.put("customerId", "27");

            } while (cursor.moveToNext());
            FTPHelper.Dowork(docPaths,getPatient(documentObj.get_id()).get_name(),c);


        }
        database.close();



        String s1 = null;

        StringWriter out = new StringWriter();
        try {
            JSONValue.writeJSONString(documentsList, out);
            s1 = out.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Gson gson = new GsonBuilder().create();
        //Use GSON to serialize Array List to JSON
        return s1;
    }




    public String composeJSONfromSQLiteNotes(String pid,String tableName){
        ArrayList<ArrayList<String>> notesList;
        notesList = new ArrayList<ArrayList<String>>();
        String selectQuery = "SELECT  * FROM "+ tableName +" WHERE "+KEY_SYNC_STATUS+" != '1' AND "+KEY_ID+" = "+pid ;

        personal_obj personalObj = this.getPersonalInfo();
        String c = String.valueOf(personalObj.get_customerId());
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                ArrayList<String> list = new ArrayList<>();
                list = utility.cursorToArrayList(cursor, c);

                notesList.add(list);


            } while (cursor.moveToNext());
        }
        database.close();



        String s1 = null;

        StringWriter out = new StringWriter();
        try {
            JSONValue.writeJSONString(notesList, out);
            s1 = out.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return s1;
    }




    /**
     * Get Sync status of SQLite
     * @return
     */
    public String getSyncStatus(){
        String msg = null;
        if(this.dbSyncCount() == 0){
            msg = "SQLite and Remote MySQL DBs are in Sync!";
        }else{
            msg = "DB Sync needed\n";
        }
        return msg;
    }

    /**
     * Get SQLite records that are yet to be Synced
     * @return
     */
    public int dbSyncCount(){
        int count = 0;
        String selectQuery = "SELECT  * FROM patient";// where udpateStatus = '"+"no"+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        count = cursor.getCount();
        database.close();
        return count;
    }

    /**
     * Update Sync status against each User ID
     * @param id
     * @param status
     */
    public void updateSyncStatus(String id, String status){
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "Update patient set udpateStatus = '"+ status +"' where userId="+"'"+ id +"'";
        Log.d("query", updateQuery);
        database.execSQL(updateQuery);
        database.close();
    }




}
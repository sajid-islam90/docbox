package utilityClasses;

/**
 * Created by sajid on 2/12/2015.
 */
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONValue;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;


import com.elune.sajid.myapplication.R;

import objects.*;


public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "patientManager";

    //  table names
    private static final String TABLE_PATIENT = "patient";
    private static final String TABLE_DOCTOR_HELPER_MAPPING= "doctorHelperMapping";

    private static final String TABLE_DOCUMENTS = "documents";
    private static final String TABLE_DOCUMENTS_HELPER_MAPPING = "documentsHelperMapping";
    private static final String TABLE_NOTES = "notes";
    private static final String TABLE_FOLLOW_UP = "followUp";
    private static final String TABLE_MEDIA = "media";
    private static final String TABLE_MEDIA_FOLLOW_UP = "mediaFollowUp";
    private static final String TABLE_HISTORY = "history";
    private static final String TABLE_HISTORY_HIST = "historyHist";
    private static final String TABLE_EXAM = "exam";
    private static final String TABLE_EXAM_HIST = "examHist";
    private static final String TABLE_TREATMENT = "treatment";
    private static final String TABLE_DIAGNOSIS = "diagnosis";
    private static final String TABLE_TREATMENT_HIST = "treatmentHist";
    private static final String TABLE_OTHER = "other";
    private static final String TABLE_OTHER_FOLLOW_UP = "otherFollowUp";
    private static final String TABLE_OTHER_HIST = "otherHist";
    private static final String TABLE_PERSONAL_INFO = "personalInfo";
    private static final String TABLE_APPOINTMENT_SETTINGS = "appointmentSettings";
    private static final String TABLE_APPOINTMENTS = "appointments";

    //  Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_ID_WEB = "idWeb";
    private static final String KEY_ID_PATIENT_DOCTOR = "idDoctor";
    private static final String KEY_ID_Patient_HELPER = "idHelper";
    private static final String KEY_FIRST_AID_ID = "firstAidPID";
    private static final String KEY_NAME = "name";
    private static final String KEY_AGE = "age";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_CONTACT = "contactNumber";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_DESIGNATION = "designation";
    private static final String KEY_AWARDS= "awards";
    private static final String KEY_EXPERIENCE = "experience";
    private static final String KEY_CONSULT_FEE = "consultFee";
    private static final String KEY_OCCUPATION = "occupation";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_HEIGHT = "height";
    private static final String KEY_WEIGHT = "weight";
    private static final String KEY_DIAGNOSIS = "diagnosis";
    private static final String KEY_OPD_IPD = "OPDIPD";
    private static final String KEY_BMP = "bitmapBLOB";
    private static final String KEY_PHOTO_PATH = "photoPath";
    private static final String KEY_SYNC_STATUS = "syncStatus";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_MAP_SNAPSHOT_PATH = "mapSnapShotPath";
    private static final String KEY_DOC_NAME = "documentName";
    private static final String KEY_DOC_PATH = "documentPath";
    private static final String KEY_DOC_PATH_DOCTOR = "documentPathDoctor";
    private static final String KEY_DOC_PATH_HELPER = "documentPathHelper";

    private static final String KEY_DATE = "date";
    private static final String KEY_DOB = "dob";
    private static final String KEY_ONLINE_DAYS = "onlineDays";
    private static final String KEY_START_TIME = "startTime";
    private static final String KEY_END_TIME = "endTime";
    private static final String KEY_SERIAL_NUMBER = "serialNumber";
    private static final String KEY_NUMBER_OF_PATIENTS = "numberOfPatients";
    private static final String KEY_DATE_LAST_VISIT= "dateLastVisit";
    private static final String KEY_DATE_NEXT_FOLLOW_UP= "dateNextFollowUp";
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
    private static final String KEY_SPECIALITY = "speciality";
    private static final String KEY_VERSION = "version";
    private static final String DATABASE_ALTER_APPOINTMENT_TABLE = "ALTER TABLE "
            + TABLE_APPOINTMENTS + " ADD COLUMN " + KEY_WEIGHT + " TEXT";
    private static final String DATABASE_ALTER_APPOINTMENT_TABLE_2 = "ALTER TABLE "
            + TABLE_APPOINTMENTS + " ADD COLUMN " + KEY_HEIGHT + " TEXT";
    private static final String DATABASE_ALTER_PERSONAL_INFO_TABLE = "ALTER TABLE "
            + TABLE_PERSONAL_INFO + " ADD COLUMN " + KEY_GENDER + " TEXT";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
       String CREATE_PATIENTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_PATIENT + "("
                + KEY_ID + " INTEGER PRIMARY KEY," +KEY_NAME + " TEXT," + KEY_DIAGNOSIS + " TEXT,"
                + KEY_AGE + " TEXT,"+KEY_DATE_NEXT_FOLLOW_UP + " TEXT,"+KEY_DATE_LAST_VISIT + " TEXT,"+KEY_DATE + " TEXT," +KEY_CONTACT + " TEXT,"+KEY_EMAIL +
               " TEXT,"+KEY_ADDRESS + " TEXT,"+KEY_OCCUPATION + " TEXT,"  + KEY_GENDER + " TEXT,"+KEY_OPD_IPD + " TEXT," + KEY_WEIGHT + " TEXT, "+KEY_HEIGHT + " TEXT, " +
               KEY_BMP +" BLOB,"+KEY_PHOTO_PATH + " TEXT, "+KEY_FIRST_AID_ID + " TEXT, "  + KEY_SYNC_STATUS + " TEXT " +")";
        db.execSQL(CREATE_PATIENTS_TABLE);


        String CREATE_DOCTOR_HELPER_MAPPING_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_DOCTOR_HELPER_MAPPING + "("
                + KEY_ID_PATIENT_DOCTOR + " INTEGER PRIMARY KEY," + KEY_ID_Patient_HELPER + " INTEGER" +")";
        db.execSQL(CREATE_DOCTOR_HELPER_MAPPING_TABLE);


        String CREATE_DOCUMENTS_HELPER_MAPPING_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_DOCUMENTS_HELPER_MAPPING + "("
                + KEY_DOC_PATH_DOCTOR + " TEXT PRIMARY KEY," + KEY_DOC_PATH_HELPER + " TEXT" +")";
        db.execSQL(CREATE_DOCUMENTS_HELPER_MAPPING_TABLE);

        String CREATE_DOCUMENTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_DOCUMENTS + "("
                + KEY_ID + " INTEGER ," +KEY_DATE + " TEXT,"  +KEY_DOC_NAME + " TEXT," + KEY_DOC_PATH + " TEXT PRIMARY KEY, " + KEY_BMP +" BLOB, "
                + KEY_SYNC_STATUS + " TEXT "  +")";
        db.execSQL(CREATE_DOCUMENTS_TABLE);


        String CREATE_DIAGNOSIS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_DIAGNOSIS + "("
                + KEY_ID + " INTEGER ,"+KEY_VERSION+" INTEGER ,"+KEY_DIAGNOSIS+" TEXT ," + KEY_DATE+ " TEXT, "
                + KEY_SYNC_STATUS + " TEXT " +")";
        db.execSQL(CREATE_DIAGNOSIS_TABLE);
        String CREATE_TREATMENT_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_TREATMENT + "("
                + KEY_ID + " INTEGER ,"+KEY_VERSION+" INTEGER ,"+KEY_TREATMENT+" TEXT ," + KEY_DATE+ " TEXT, "
                + KEY_SYNC_STATUS + " TEXT " +")";
        db.execSQL(CREATE_TREATMENT_TABLE);





        String CREATE_HISTORY_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_HISTORY + "("
                + KEY_ID + " INTEGER ,"+KEY_DATE+" TEXT ,"+KEY_VERSION+" INTEGER ," + KEY_HIST+ " TEXT, "+ KEY_HIST_OF_ILLNESS + " TEXT,"
                + KEY_PERSONAL_HISTORY +" TEXT, "+KEY_FAMILY_HISTORY+" TEXT, " + KEY_SYNC_STATUS + " TEXT " +")";
        db.execSQL(CREATE_HISTORY_TABLE);

       String CREATE_HISTORY_HIST_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_HISTORY_HIST + "("
                + KEY_ID + " INTEGER ,"+KEY_DATE+" TEXT ,"+KEY_VERSION+" INTEGER ,"+ KEY_HIST+ " TEXT, "+ KEY_HIST_OF_ILLNESS + " TEXT,"
                + KEY_PERSONAL_HISTORY +" TEXT, "+KEY_FAMILY_HISTORY+" TEXT, " + KEY_SYNC_STATUS + " TEXT " +")";
        db.execSQL(CREATE_HISTORY_HIST_TABLE);


        String CREATE_EXAM_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_EXAM + "("
                + KEY_ID + " INTEGER ,"+KEY_DATE+" TEXT ,"+KEY_VERSION+" INTEGER ," + KEY_GENERAL_EXAM + " TEXT," +
                KEY_LOCAL_EXAM+ " TEXT, "  + KEY_SYNC_STATUS + " TEXT " +")";
        db.execSQL(CREATE_EXAM_TABLE);

        String CREATE_EXAM_HIST_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_EXAM_HIST + "("
                + KEY_ID + " INTEGER ,"+KEY_DATE+" TEXT ,"+KEY_VERSION+" INTEGER ," + KEY_GENERAL_EXAM + " TEXT," +
                KEY_LOCAL_EXAM+ " TEXT, "  + KEY_SYNC_STATUS + " TEXT " +")";
        db.execSQL(CREATE_EXAM_HIST_TABLE);



        String CREATE_TREATMENT1_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_TREATMENT + "("
                + KEY_ID + " INTEGER ,"+KEY_DATE+" TEXT ,"+KEY_VERSION+" INTEGER ," + KEY_DIAGNOSIS + " TEXT," +
                KEY_TREATMENT + " TEXT," + KEY_PROCEDURE + " TEXT," + KEY_IMPLANT+ " TEXT, "  + KEY_SYNC_STATUS + " TEXT " +")";
        db.execSQL(CREATE_TREATMENT1_TABLE);

        String CREATE_TREATMENT_HIST_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_TREATMENT_HIST + "("
                + KEY_ID + " INTEGER ,"+KEY_DATE+" TEXT ,"+KEY_VERSION+" INTEGER ," + KEY_DIAGNOSIS + " TEXT," +
                KEY_TREATMENT + " TEXT," + KEY_PROCEDURE + " TEXT," + KEY_IMPLANT+ " TEXT, "  + KEY_SYNC_STATUS + " TEXT " +")";
        db.execSQL(CREATE_TREATMENT_HIST_TABLE);


        String CREATE_OTHER_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_OTHER + "("
                + KEY_ID + " INTEGER ,"+KEY_DATE+" TEXT ,"+KEY_VERSION+" INTEGER ," + KEY_FIELD_NAME + " TEXT," +
                KEY_FIELD_VALUE+ " TEXT, "  + KEY_SYNC_STATUS + " TEXT " +")";
        db.execSQL(CREATE_OTHER_TABLE);

        String CREATE_OTHER_FOLLOWUP_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_OTHER_FOLLOW_UP + "("
                + KEY_ID + " INTEGER ,"+KEY_DATE+" TEXT ,"+KEY_VERSION+" INTEGER ," + KEY_FIELD_NAME + " TEXT," +
                KEY_FIELD_VALUE+ " TEXT, "  + KEY_SYNC_STATUS + " TEXT " +")";
        db.execSQL(CREATE_OTHER_FOLLOWUP_TABLE);

        String CREATE_OTHER_HIST_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_OTHER_HIST + "("
                + KEY_ID + " INTEGER ,"+KEY_DATE+" TEXT ,"+KEY_VERSION+" INTEGER ," + KEY_FIELD_NAME + " TEXT," +
                KEY_FIELD_VALUE+ " TEXT, "  + KEY_SYNC_STATUS + " TEXT " +")";
        db.execSQL(CREATE_OTHER_HIST_TABLE);

        String CREATE_NOTES_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NOTES + "("
                + KEY_ID + " INTEGER ,"+KEY_DATE+" TEXT ,"+KEY_SECTION+" INTEGER ," + KEY_FIELD_NAME + " TEXT," +
                KEY_FIELD_VALUE+ " TEXT, "  + KEY_SYNC_STATUS + " TEXT " +")";
        db.execSQL(CREATE_NOTES_TABLE);

        String CREATE_FOLLOW_UP_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_FOLLOW_UP + "("
                + KEY_ID + " INTEGER ,"+KEY_DATE+" TEXT ,"+KEY_VERSION+" INTEGER ," + KEY_FIELD_NAME + " TEXT," +
                KEY_FIELD_VALUE+ " TEXT, "  + KEY_SYNC_STATUS + " TEXT " +")";
        db.execSQL(CREATE_FOLLOW_UP_TABLE);


       String CREATE_DOCUMENTS_MEDIA_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_MEDIA + "("
                + KEY_ID + " INTEGER ," + KEY_DOC_NAME + " TEXT,"+ KEY_VERSION + " TEXT,"+ KEY_SECTION + " TEXT," +
               KEY_DOC_PATH + " TEXT, "  + KEY_SYNC_STATUS + " TEXT, "+ KEY_BMP +" BLOB " +")";
        db.execSQL(CREATE_DOCUMENTS_MEDIA_TABLE);

        String CREATE_FOLLOW_UP_MEDIA_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_MEDIA_FOLLOW_UP + "("
                + KEY_ID + " INTEGER ," + KEY_DOC_NAME + " TEXT,"+ KEY_VERSION + " TEXT,"+ KEY_SECTION + " TEXT," +
                KEY_DOC_PATH + " TEXT, "  + KEY_SYNC_STATUS + " TEXT, "+ KEY_BMP +" BLOB " +")";
        db.execSQL(CREATE_FOLLOW_UP_MEDIA_TABLE);


     String CREATE_PERSONAL_INFO_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_PERSONAL_INFO + "("
                + KEY_ID + " INTEGER ," + KEY_EMAIL + " TEXT,"+ KEY_PASSWORD + " TEXT,"+KEY_SPECIALITY + " TEXT,"+
               KEY_NAME + " TEXT, " +KEY_DOB + " TEXT, " +KEY_DESIGNATION + " TEXT, " + KEY_ADDRESS + " TEXT, " + KEY_AWARDS + " TEXT, "
              + KEY_EXPERIENCE+ " TEXT, " + KEY_CONSULT_FEE + " TEXT, "  + KEY_DOC_PATH +" TEXT, "+ KEY_LATITUDE +" TEXT , "
              +KEY_LONGITUDE +" TEXT , "+ KEY_MAP_SNAPSHOT_PATH +" TEXT, "+ KEY_GENDER + " TEXT " +")";
        db.execSQL(CREATE_PERSONAL_INFO_TABLE);

        String CREATE_APPOINTMENT_SETTINGS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_APPOINTMENT_SETTINGS + "("
               + KEY_ONLINE_DAYS + " TEXT,"+ KEY_START_TIME + " TEXT,"+KEY_END_TIME + " TEXT,"
                + KEY_NUMBER_OF_PATIENTS + " INTEGER " +
                ")";
        db.execSQL(CREATE_APPOINTMENT_SETTINGS_TABLE);

        String CREATE_APPOINTMENT_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_APPOINTMENTS + "("
                + KEY_FIRST_AID_ID + " TEXT,"+KEY_ID + " TEXT,"+KEY_NAME + " TEXT,"+KEY_CONTACT + " TEXT,"+KEY_EMAIL + " TEXT,"
                +KEY_GENDER + " TEXT,"+KEY_AGE + " TEXT,"+ KEY_DATE
                + " DATETIME,"+KEY_START_TIME + " TEXT,"+KEY_END_TIME + " TEXT, "+KEY_SERIAL_NUMBER + " INTEGER, "
                + KEY_WEIGHT +" TEXT, "+KEY_HEIGHT+" TEXT"+
                ", PRIMARY KEY ( "+KEY_FIRST_AID_ID+ " , "+KEY_DATE+" )"+
                ")";
        db.execSQL(CREATE_APPOINTMENT_TABLE);


     /* String RENAME_TABLE = "DROP TABLE "+TABLE_APPOINTMENTS;
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
      if (DATABASE_VERSION ==2)
      {
          db.execSQL(DATABASE_ALTER_APPOINTMENT_TABLE);
          db.execSQL(DATABASE_ALTER_APPOINTMENT_TABLE_2);
          db.execSQL(DATABASE_ALTER_PERSONAL_INFO_TABLE);
      }
        if(DATABASE_VERSION<4)
        {

        }


        // Create tables again

    }


    public  void saveAppointments(ArrayList<String>AppointmentData )
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        String Pid = AppointmentData.get(0),
                firstAidPid = AppointmentData.get(1),
                startTime = AppointmentData.get(2)
                ,endTime = AppointmentData.get(3),
                Date = AppointmentData.get(4),
                firstAidPatientName = AppointmentData.get(5),
                firstAidPatientContact = AppointmentData.get(6),
                firstAidPatientEmail = AppointmentData.get(7),
                firstAidPatientAge = AppointmentData.get(8),firstAidPatientGender = AppointmentData.get(9),
        serialNumber = AppointmentData.get(10),firstAidPatientHeight=AppointmentData.get(11),firstAidPatientWeight = AppointmentData.get(12);
      //  db.execSQL("delete from "+ TABLE_APPOINTMENTS +" WHERE "+KEY_DATE+" = '"+Date+"' ");
        values.put(KEY_ID, Pid);
        values.put(KEY_NAME,firstAidPatientName);
        values.put(KEY_FIRST_AID_ID, firstAidPid);
        values.put(KEY_START_TIME, startTime);
        values.put(KEY_END_TIME, endTime);
        values.put(KEY_DATE, Date);
        values.put(KEY_CONTACT,firstAidPatientContact);
        values.put(KEY_EMAIL,firstAidPatientEmail);
        values.put(KEY_AGE,firstAidPatientAge);
        values.put(KEY_GENDER,firstAidPatientGender);
        values.put(KEY_SERIAL_NUMBER,serialNumber);
        values.put(KEY_WEIGHT,firstAidPatientWeight);
        values.put(KEY_HEIGHT,firstAidPatientHeight);
        try {
            db.insert(TABLE_APPOINTMENTS, null, values);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        db.close();

    }
    public ArrayList<String> getAppointmentsForDate(String date) {
        ArrayList<String> patients = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String Sql = "Select "+KEY_ID+" FROM "+TABLE_APPOINTMENTS+" WHERE "+KEY_DATE+" = '"+date+"'";
        Cursor cursor = db.rawQuery(Sql,null);
        if(cursor.getCount()>0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                patients.add(cursor.getString(cursor.getColumnIndex(KEY_ID)));
                cursor.moveToNext();
            }


        }
        db.close();

        return patients;
    }


    public Patient getFirstAidPatient(int firstAidPid)
    {
        Patient patient = new Patient();
        SQLiteDatabase db = this.getWritableDatabase();
        String Sql = "Select "+KEY_NAME+" , "+KEY_CONTACT+" , "+KEY_EMAIL+" , "
                +KEY_GENDER+" , "+KEY_AGE+" FROM "+TABLE_APPOINTMENTS+" WHERE "+KEY_FIRST_AID_ID+" = '"+firstAidPid+"'";
        Cursor cursor = db.rawQuery(Sql,null);
        if(cursor.getCount()>0) {
            cursor.moveToFirst();
            patient.set_name(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            patient.set_contact_number(cursor.getString(cursor.getColumnIndex(KEY_CONTACT)));
            patient.set_email(cursor.getString(cursor.getColumnIndex(KEY_EMAIL)));
            patient.set_gender(cursor.getString(cursor.getColumnIndex(KEY_GENDER)));
            patient.set_age(cursor.getString(cursor.getColumnIndex(KEY_AGE)));

            patient.set_first_aid_id(firstAidPid);

        }
        db.close();
        return patient;
    }
    public void deleteAppointments(String onlineDay)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_APPOINTMENTS + " WHERE " + KEY_DATE + " >= '" + onlineDay + "'");
        db.close();
    }
public  void saveAppointmentSettings(String onlineDays,String startTime,String endTime,int numberOfPatients )
{
    SQLiteDatabase db = this.getWritableDatabase();
    db.execSQL("delete from "+ TABLE_APPOINTMENT_SETTINGS +" WHERE "+KEY_ONLINE_DAYS+" = '"+onlineDays+"'");
    ContentValues values = new ContentValues();
    values.put(KEY_ONLINE_DAYS, onlineDays);
    values.put(KEY_START_TIME, startTime);
    values.put(KEY_END_TIME, endTime);
    values.put(KEY_NUMBER_OF_PATIENTS, numberOfPatients);
    db.insert(TABLE_APPOINTMENT_SETTINGS, null, values);
    db.close();

}


    public void mapDoctorHelperDocuments(String doctorDocumentPath , String helperDocumentPath)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_DOC_PATH_DOCTOR, doctorDocumentPath);
        values.put(KEY_DOC_PATH_HELPER, helperDocumentPath);
        db.insert(TABLE_DOCUMENTS_HELPER_MAPPING, null, values);
        db.close();


    }
    public String getMappedDoctorDocPath( String helperDocumentPath )
    {
        String mapping = "";

        SQLiteDatabase db = this.getWritableDatabase();
        String Sql = "Select "+KEY_DOC_PATH_DOCTOR+" FROM "+TABLE_DOCUMENTS_HELPER_MAPPING+" WHERE "+KEY_DOC_PATH_HELPER+" = '"+helperDocumentPath+"'";
        Cursor cursor = db.rawQuery(Sql,null);
        if(cursor.getCount()>0) {
            cursor.moveToFirst();

            mapping = cursor.getString(0);
        }
        db.close();

        return  mapping;
    }
    public String getMappedHelperDocPath( String doctorDocumentPath )
    {
        String mapping = "";

        SQLiteDatabase db = this.getWritableDatabase();
        String Sql = "Select "+KEY_DOC_PATH_HELPER+" FROM "+TABLE_DOCUMENTS_HELPER_MAPPING+" WHERE "+KEY_DOC_PATH_DOCTOR+" = '"+doctorDocumentPath+"'";
        Cursor cursor = db.rawQuery(Sql,null);
        if(cursor.getCount()>0) {
            cursor.moveToFirst();

            mapping = cursor.getString(0);
        }
        db.close();

        return  mapping;
    }




    public void mapDoctorHelperPatients(int doctorPid , int helperPid)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_ID_PATIENT_DOCTOR, doctorPid);
        values.put(KEY_ID_Patient_HELPER, helperPid);
        db.insert(TABLE_DOCTOR_HELPER_MAPPING, null, values);
        db.close();


    }
    public boolean checkDoctorHelperPatientMapping(int doctorPid , int helperPid )
    {
        boolean mapping = false;

        SQLiteDatabase db = this.getWritableDatabase();
        String Sql = "Select "+KEY_ID_PATIENT_DOCTOR+" FROM "+TABLE_DOCTOR_HELPER_MAPPING+" WHERE "+KEY_ID_Patient_HELPER+" = '"+helperPid+"'";
        Cursor cursor = db.rawQuery(Sql,null);
        if(cursor.getCount()>0) {
            cursor.moveToFirst();
           int docPid = cursor.getInt(0);
            if(docPid == doctorPid)
                mapping = true;


        }
        db.close();

        return  mapping;
    }

    public int checkDoctorHelperPatientMapping( int helperPid )
    {
        int mapping = 0;

        SQLiteDatabase db = this.getWritableDatabase();
        String Sql = "Select "+KEY_ID_PATIENT_DOCTOR+" FROM "+TABLE_DOCTOR_HELPER_MAPPING+" WHERE "+KEY_ID_Patient_HELPER+" = '"+helperPid+"'";
        Cursor cursor = db.rawQuery(Sql,null);
        if(cursor.getCount()>0) {
            cursor.moveToFirst();

mapping = cursor.getInt(0);
        }
        db.close();

        return  mapping;
    }

    public int checkDoctorInHelperPatientMapping( int docPid )
    {
        int mapping = 0;

        SQLiteDatabase db = this.getWritableDatabase();
        String Sql = "Select "+KEY_ID_Patient_HELPER+" FROM "+TABLE_DOCTOR_HELPER_MAPPING+" WHERE "+KEY_ID_PATIENT_DOCTOR+" = '"+docPid+"'";
        Cursor cursor = db.rawQuery(Sql,null);
        if(cursor.getCount()>0) {
            cursor.moveToFirst();

            mapping = cursor.getInt(0);
        }
        db.close();

        return  mapping;
    }
    public void updatePatient(String columnName,String columnValue,String Pid)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(columnName, columnValue);

        db.update(TABLE_PATIENT, args, KEY_ID + "= '" + Pid + "'", null) ;

    }

    public void updateAppointments(String columnName,String columnValue,String firstAidId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(columnName, columnValue);

        db.update(TABLE_APPOINTMENTS, args, KEY_FIRST_AID_ID + "= '" + firstAidId + "'", null) ;

    }
    public void deleteAppointmentSettings(String onlineDay)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_APPOINTMENT_SETTINGS + " WHERE " + KEY_ONLINE_DAYS + " = '" + onlineDay + "'");
        db.close();
    }
    public void updateAppointmentSettings(String columnName,String columnValue,String day)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(columnName, columnValue);

        db.update(TABLE_APPOINTMENT_SETTINGS, args, KEY_ONLINE_DAYS + "= '" + day + "'", null) ;

    }
    public ArrayList<String> getAppointmentSettings(String day)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String strSql = "SELECT * from "+TABLE_APPOINTMENT_SETTINGS+" WHERE "+KEY_ONLINE_DAYS+" = '"+day+"'";
        Cursor cursor = db.rawQuery(strSql,null);

        ArrayList<String> settings = new ArrayList<>();
        if(cursor.getCount()>0) {
            cursor.moveToFirst();
            for (int i = 0; i < 3; i++)
                settings.add(cursor.getString(i));

            settings.add(String.valueOf(cursor.getInt(3)));
        }
        return settings;

    }
    public ArrayList<String> getAppointmentSettings()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String strSql = "SELECT * from "+TABLE_APPOINTMENT_SETTINGS;
        Cursor cursor = db.rawQuery(strSql,null);

        ArrayList<String> settings = new ArrayList<>();
        if(cursor.getCount()>0) {
            cursor.moveToFirst();
            for (int i = 0; i < 3; i++)
                settings.add(cursor.getString(i));

            settings.add(String.valueOf(cursor.getInt(3)));
        }
return settings;

    }


    //treatment functions
    public  void saveTreatment(String pid,String treatment,String date,int version)
    {//int version = getDiagnosisCurrentVersion(pid);
        SQLiteDatabase db = this.getWritableDatabase();
        //db.execSQL("delete from "+ TABLE_DIAGNOSIS +" WHERE "+KEY_ONLINE_DAYS+" = '"+onlineDays+"'");
        ContentValues values = new ContentValues();
        Long tsLong = System.currentTimeMillis()/1000;
        values.put(KEY_ID, pid);
        values.put(KEY_VERSION, version);
        values.put(KEY_TREATMENT, treatment);
        values.put(KEY_DATE, date);
        values.put(KEY_SYNC_STATUS, 0);
        long i = db.insert(TABLE_TREATMENT, null, values);
        db.close();
        updatePatient(KEY_DATE_LAST_VISIT, date, pid);
        updatePatient(KEY_SYNC_STATUS, "0", pid);
    }


    public  void saveTreatmentWithVersion(String pid,String treatment,String date,String version )
    {


        SQLiteDatabase db = this.getWritableDatabase();
        //db.execSQL("delete from "+ TABLE_DIAGNOSIS +" WHERE "+KEY_ONLINE_DAYS+" = '"+onlineDays+"'");
        ContentValues values = new ContentValues();

        values.put(KEY_ID, pid);
        values.put(KEY_VERSION, version);
        values.put(KEY_TREATMENT, treatment);
        values.put(KEY_DATE, date);
        values.put(KEY_SYNC_STATUS, 0);
        long i = db.insert(TABLE_TREATMENT, null, values);
        db.close();
        updatePatient(KEY_DATE_LAST_VISIT, date, pid);
        updatePatient(KEY_SYNC_STATUS, "1", pid);

    }
    public List getTreatment(String pid,int version)
    {
        List<List<String>> treatmentData =   new ArrayList<List<String>>();
        List<String>treatment = new ArrayList<>();
        List<String>dates = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String strSql = "SELECT * from "+TABLE_TREATMENT+" WHERE "+KEY_ID+" = "+pid+" AND "+KEY_VERSION+" = "+version;
       // String strSql = "SELECT * from "+TABLE_TREATMENT+" WHERE "+KEY_ID+" = "+pid+" ORDER BY "+KEY_VERSION+" ASC";
        Cursor cursor = db.rawQuery(strSql, null);
        if(cursor.getCount()>0)
        {
            cursor.moveToFirst();
            do {
                //treatment.put(cursor.getString(cursor.getColumnIndex(KEY_TREATMENT)), cursor.getString(cursor.getColumnIndex(KEY_DATE)));
                //treatment.put(KEY_DATE, cursor.getString(cursor.getColumnIndex(KEY_DATE)));

                treatment.add(cursor.getString(cursor.getColumnIndex(KEY_TREATMENT)));
                dates.add(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
            }
            while (cursor.moveToNext());



        }
        treatmentData.add(treatment);
        treatmentData.add(dates);
        return treatmentData;
    }


    //diagnosis and treatment functions
    public  void saveDiagnosis(String pid,String diagnosis,String date )
    {int version = getDiagnosisCurrentVersion(pid);

        Long tsLong = System.currentTimeMillis()/1000;
        SQLiteDatabase db = this.getWritableDatabase();
        //db.execSQL("delete from "+ TABLE_DIAGNOSIS +" WHERE "+KEY_ONLINE_DAYS+" = '"+onlineDays+"'");
        ContentValues values = new ContentValues();

        values.put(KEY_ID, pid);
        values.put(KEY_VERSION, tsLong);
        values.put(KEY_DIAGNOSIS, diagnosis);
        values.put(KEY_DATE, date);
        values.put(KEY_SYNC_STATUS, 0);
        long i = db.insert(TABLE_DIAGNOSIS, null, values);
        db.close();
        updatePatient(KEY_DATE_LAST_VISIT, date, pid);
        updatePatient(KEY_SYNC_STATUS, "0", pid);

    }
    public  void saveDiagnosisWithVersion(String pid,String diagnosis,String date,String version )
    {


        SQLiteDatabase db = this.getWritableDatabase();
        //db.execSQL("delete from "+ TABLE_DIAGNOSIS +" WHERE "+KEY_ONLINE_DAYS+" = '"+onlineDays+"'");
        ContentValues values = new ContentValues();

        values.put(KEY_ID, pid);
        values.put(KEY_VERSION, version);
        values.put(KEY_DIAGNOSIS, diagnosis);
        values.put(KEY_DATE, date);
        values.put(KEY_SYNC_STATUS, 0);
        long i = db.insert(TABLE_DIAGNOSIS, null, values);
        db.close();
        updatePatient(KEY_DATE_LAST_VISIT, date, pid);
        updatePatient(KEY_SYNC_STATUS, "1", pid);

    }
    public Map getDiagnosis(String pid,int version)
    {
        Map<String, Object> diagnosis = new HashMap<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String strSql = "SELECT * from "+TABLE_DIAGNOSIS+" WHERE "+KEY_ID+" = "+pid+" ORDER BY "+KEY_VERSION+" ASC";
       // String strSql = "SELECT * from "+TABLE_DIAGNOSIS+" WHERE "+KEY_ID+" = "+pid+" AND "+KEY_VERSION+" = "+version+" ORDER BY "+KEY_VERSION+" ASC";
        Cursor cursor = db.rawQuery(strSql, null);
        if(cursor.getCount()>0)
        {
            cursor.moveToFirst();
cursor.moveToPosition(version-1);
            diagnosis.put(KEY_DIAGNOSIS,cursor.getString(cursor.getColumnIndex(KEY_DIAGNOSIS)));
            diagnosis.put(KEY_DATE,cursor.getString(cursor.getColumnIndex(KEY_DATE)));

        }
        return diagnosis;
    }
    public long getDiagnosisUnixVersion(String pid,int version)
    {
        Map<String, Object> diagnosis = new HashMap<>();
        SQLiteDatabase db = this.getWritableDatabase();
        long unixVersion = 0;
        String strSql = "SELECT * from "+TABLE_DIAGNOSIS+" WHERE "+KEY_ID+" = "+pid+" ORDER BY "+KEY_VERSION+" ASC";
        // String strSql = "SELECT * from "+TABLE_DIAGNOSIS+" WHERE "+KEY_ID+" = "+pid+" AND "+KEY_VERSION+" = "+version+" ORDER BY "+KEY_VERSION+" ASC";
        Cursor cursor = db.rawQuery(strSql, null);
        if(cursor.getCount()>0)
        {
            cursor.moveToFirst();
            cursor.moveToPosition(version-1);
           unixVersion = Long.parseLong(cursor.getString(cursor.getColumnIndex(KEY_VERSION)));

        }
        return unixVersion;
    }
    public void updateTreatment(String field,String value,String version,String pid)


    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(field, value);

        db.update(TABLE_TREATMENT, args, KEY_VERSION + "= '" + version + "' AND " +
                KEY_ID+" = "+pid, null) ;

    }
    public void updateDiagnosis(String field,String value,String version,String pid)


    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(field, value);

        db.update(TABLE_DIAGNOSIS, args, KEY_VERSION + "= '" + version + "' AND " +
                KEY_ID+" = "+pid, null) ;

    }
public int getDiagnosisCurrentVersion(String pid)
{
    SQLiteDatabase db = this.getWritableDatabase();
    int version = 0;
    String strSql = "SELECT COUNT( "+KEY_VERSION +") from "+TABLE_DIAGNOSIS+" WHERE "+KEY_ID+" = "+pid+" ORDER BY "+KEY_VERSION+" ASC";
    Cursor cursor = db.rawQuery(strSql,null);
    if(cursor.getCount()>0)
    {   cursor.moveToLast();
        version = cursor.getInt(cursor.getColumnIndex("COUNT( "+KEY_VERSION +")"));}
db.close();
    return version;
}

    // generic notes functions

    public void saveGenericNote(ArrayList<Item> listOfItems,String syncStatus)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        for(int i = 0;i<listOfItems.size();i++)
        {

            ContentValues values = new ContentValues();
            values.put(KEY_ID, listOfItems.get(i).getPatient_id());
            values.put(KEY_DATE,listOfItems.get(i).getDate());
            values.put(KEY_FIELD_NAME,listOfItems.get(i).getTitle());
            values.put(KEY_FIELD_VALUE,listOfItems.get(i).getDiagnosis());
            values.put(KEY_SECTION,listOfItems.get(i).getSection());
            values.put(KEY_SYNC_STATUS, syncStatus);
         int j =  db.delete(TABLE_NOTES,
                   KEY_ID + "=? AND " + KEY_SECTION + "=? AND " +
                           KEY_FIELD_NAME + "=?  ",
                   new String[]{String.valueOf(listOfItems.get(i).getPatient_id()), String.valueOf(listOfItems.get(i).getSection()), listOfItems.get(i).getTitle()});


            String sql = "DELETE FROM " + TABLE_NOTES + " WHERE " + KEY_SECTION + " = '" + listOfItems.get(i).getSection() + "' AND "
                    + KEY_ID + " = '" + listOfItems.get(i).getPatient_id() + "' AND "
                    + KEY_FIELD_NAME + " = '" + listOfItems.get(i).getTitle() + "' ";
                db.rawQuery(sql, null);

            db.insert(TABLE_NOTES, null, values);
            updatePatient(KEY_DATE_LAST_VISIT, formattedDate, String.valueOf(listOfItems.get(i).getPatient_id()));
        }
        db.close();

    }



    public ArrayList<Item> getGenericNote(int pid,int section) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Item> listOfItems = new ArrayList<>();
        Item item;
        String StrSQL = "SELECT * FROM "+TABLE_NOTES+" WHERE "+
                KEY_SECTION+" = '"+section+"' AND "
                +KEY_ID +" = '"+pid+"'";
        Cursor cursor = db.rawQuery(StrSQL,null);
        if(cursor.getCount()!=0) {
            cursor.moveToFirst();

            do {
                item = new Item();
                item.setTitle(cursor.getString(cursor.getColumnIndex(KEY_FIELD_NAME)));
                item.setDiagnosis(cursor.getString(cursor.getColumnIndex(KEY_FIELD_VALUE)));
                item.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
                item.setPatient_id(pid);
                item.setSection(section);
                listOfItems.add(item);
            }
            while (cursor.moveToNext());

        }
        return  listOfItems;
    }




    //follow up functions
    public void saveFollowUp(ArrayList<Item> listOfItems)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        for(int i = 0;i<listOfItems.size();i++)
        {

            ContentValues values = new ContentValues();
            values.put(KEY_ID, listOfItems.get(i).getPatient_id());
            values.put(KEY_DATE,listOfItems.get(i).getDate());
            values.put(KEY_FIELD_NAME,listOfItems.get(i).getTitle());
            values.put(KEY_FIELD_VALUE,listOfItems.get(i).getDiagnosis());
            values.put(KEY_VERSION,listOfItems.get(i).getSection());
            values.put(KEY_SYNC_STATUS, "0");



            db.insert(TABLE_FOLLOW_UP, null, values);
            updatePatient(KEY_DATE_LAST_VISIT, listOfItems.get(i).getDate(), String.valueOf(listOfItems.get(i).getPatient_id()));
            updatePatient(KEY_SYNC_STATUS, "0", String.valueOf(listOfItems.get(i).getPatient_id()));
        }
        db.close();
    }



    public ArrayList<Item> getFollowUpUnixVersion(int pid,int version) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Item> listOfItems = new ArrayList<>();
        Item item;
        String StrSQL = "SELECT * FROM "+TABLE_FOLLOW_UP+" WHERE "+
                         KEY_VERSION+" = '"+version+"' AND "
                         +KEY_ID +" = '"+pid+"'";
        Cursor cursor = db.rawQuery(StrSQL,null);
        if(cursor.getCount()!=0) {
            cursor.moveToFirst();

            do {
                item = new Item();
                item.setTitle(cursor.getString(cursor.getColumnIndex(KEY_FIELD_NAME)));
                item.setDiagnosis(cursor.getString(cursor.getColumnIndex(KEY_FIELD_VALUE)));
                item.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
                item.setPatient_id(pid);
                item.setSection(version);
                listOfItems.add(item);
            }
            while (cursor.moveToNext());

        }
        return  listOfItems;
    }
    public ArrayList<Item> getFollowUp(int pid,int version) {

        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Item> listOfItems = new ArrayList<>();
        Item item;
        String StrSQL = "SELECT * FROM "+TABLE_FOLLOW_UP+" WHERE "
                +KEY_ID +" = '"+pid+"' AND "+KEY_VERSION+" =  '"+version+"'";
        Cursor cursor = db.rawQuery(StrSQL,null);
        if(cursor.getCount()!=0) {
            cursor.moveToFirst();
           // cursor.moveToPosition(serial-1);

            do {
                item = new Item();
                item.setTitle(cursor.getString(cursor.getColumnIndex(KEY_FIELD_NAME)));
                item.setDiagnosis(cursor.getString(cursor.getColumnIndex(KEY_FIELD_VALUE)));
                item.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
                item.setPatient_id(pid);
                item.setSection(version);
                listOfItems.add(item);
            }
            while (cursor.moveToNext());

        }
        return  listOfItems;
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
        db.close();
        return personalObj;

    }
    public ArrayList<media_obj> getPersonalInfoPhotos()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        personal_obj personalObj = new personal_obj();
        ArrayList<media_obj>  paths =new ArrayList<>();
        media_obj mediaObj = new media_obj();
        String strSQL = "SELECT "+KEY_DOC_PATH+" FROM " + TABLE_PERSONAL_INFO;
        String strSQL2 = "SELECT "+KEY_MAP_SNAPSHOT_PATH+" FROM " + TABLE_PERSONAL_INFO;
        Cursor cursor = db.rawQuery(strSQL,null);
        Cursor cursor2 = db.rawQuery(strSQL2,null);
        if(cursor.getCount()!=0) {
            cursor.moveToFirst();
            if(cursor.getString(0)!=null)
                mediaObj.set_media_path(cursor.getString(0));
            mediaObj.set_pid(0);
            paths.add(mediaObj);


        }
        mediaObj = new media_obj();
        if(cursor2.getCount()!=0) {
            cursor2.moveToFirst();
            if(cursor2.getString(0)!=null)
                mediaObj.set_media_path(cursor2.getString(0));
            mediaObj.set_pid(0);
            paths.add(mediaObj);

        }
        return paths;

    }
    public ArrayList<String> getSavedLatitudeLongitude()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        personal_obj personalObj = new personal_obj();
        String strSQL = "SELECT latitude,longitude FROM " + TABLE_PERSONAL_INFO;
        ArrayList<String> latLong = new ArrayList<>();
        Cursor cursor = db.rawQuery(strSQL,null);
        if(cursor.getCount()!=0) {
            cursor.moveToFirst();
           latLong.add(cursor.getString(cursor.getColumnIndex(KEY_LATITUDE)));
            latLong.add(cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE)));

        }
        return latLong;

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
    public int getCustomerId()
    {
        int CustomerId = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        String strSQL = "SELECT "+KEY_ID+" FROM " + TABLE_PERSONAL_INFO;
        Cursor cursor = db.rawQuery(strSQL, null);
        if(cursor.getCount()!=0) {
            cursor.moveToFirst();
            CustomerId = cursor.getInt(cursor.getColumnIndex(KEY_ID));

        }
        db.close();
        return CustomerId;


    }

    public void addPersonalInfo(personal_obj personalObj)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values = utility.personalInfoTOValues(personalObj, values);
        db.insert(TABLE_PERSONAL_INFO, null, values);
        db.close();


    }
    public void deletePersonalInfo()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String sql = "DELETE FROM "+TABLE_PERSONAL_INFO;
        db.execSQL(sql);
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
    public void updateMedia(String field,String value,String docPath)


    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(field, value);

        db.update(TABLE_MEDIA, args, KEY_DOC_PATH + "= '" + docPath + "'", null) ;

    }
    public void updateTableSyncStatus(String field,String value,String tableName,String pid)


    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(KEY_SYNC_STATUS, "1");
        value =value.replace("'","");
        db.update(tableName, args, KEY_FIELD_NAME + " = '" + field +
                "' AND "+ KEY_FIELD_VALUE+" = '"+value+"' AND "+KEY_ID +" = '"+pid+"'", null) ;

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
        int histVersion = getCurrentVersion(pid,DataBaseEnums.TABLE_HISTORY);
        int examVersion = getCurrentVersion(pid, DataBaseEnums.TABLE_EXAM);
        int treatVersion = getCurrentVersion(pid,DataBaseEnums.TABLE_TREATMENT);
        int otherVersion = getCurrentVersion(pid,DataBaseEnums.TABLE_OTHER);

        int max=((histVersion>examVersion)&&(histVersion>treatVersion)&&(histVersion>otherVersion))?histVersion:(((examVersion>treatVersion)&&(examVersion>otherVersion))?examVersion:(treatVersion>otherVersion)?treatVersion:otherVersion);

return max;






/*
        SQLiteDatabase db = this.getReadableDatabase();
        int version = 0;
        String strSQL = "SELECT "+KEY_VERSION+"   FROM " + TABLE_HISTORY+" WHERE "+KEY_ID+" = "+pid;
        Cursor cursor = db.rawQuery(strSQL,null);

        if(cursor.getCount()!=0) {
            cursor.moveToLast();
            version = cursor.getInt(0);
        }

        return version;*/
    }

    public int getCurrentVersion(int pid,String tableName)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        int version = 0;
        String strSQL = "SELECT "+KEY_VERSION+"   FROM " + tableName+" WHERE "+KEY_ID+" = "+pid;
        Cursor cursor = db.rawQuery(strSQL,null);

        if(cursor.getCount()!=0) {
            cursor.moveToLast();
            version = cursor.getInt(0);
        }

        return version;
    }


    public ArrayList<String> getAllNotesDates(int pid,Context context)
    {
        ArrayList<String> dates = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        int version[] = getMaxFollowupVersion(pid);
        Resources resource = context.getResources();
        String[] fields = resource.getStringArray(R.array.follow_up);
        int c=1;
        String table = null;
for(c=0;dates.size()<version.length;c++)
{
    ArrayList<Item> listOfItems = new ArrayList<>();
    listOfItems = getFollowUp(pid,version[c]);
//    Log.i("debug", String.valueOf(version.length));
//    Log.i("debug", String.valueOf(dates.size()));
    if(listOfItems.size()>0)
        dates.add("# "+ (c+1) +" date : "+getNotesDateFromVersion(version[c],pid)+ "\n  "+ fields[0]+" : "+listOfItems.get(0).getDiagnosis() );
    else
    {
        Log.i("empty followup version", String.valueOf(version[c]+" : "+c));
    }
}
//        while (c<=version.length)
//        {
//            ArrayList<Item> listOfItems = new ArrayList<>();
//            listOfItems = getFollowUp(pid,version[c-1]);
//            Log.i("debug", String.valueOf(version.length));
//            Log.i("debug", String.valueOf(dates.size()));
//            if(listOfItems.size()>0)
//            dates.add("# "+ c +" date : "+getNotesDateFromVersion(version[c-1],pid)+ "\n  "+ fields[0]+" : "+listOfItems.get(0).getDiagnosis() );
//            c++;
//        }

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

    public ArrayList<String> getPatientsForDate(String date)
    {
        ArrayList<String> patients = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
String sql="";


//        sql =  "SELECT DISTINCT "+DataBaseEnums.KEY_ID+
//                " FROM ( "+
//                " select  "+DataBaseEnums.KEY_ID+" from "+
//                DataBaseEnums.TABLE_NOTES+" where "+DataBaseEnums.KEY_DATE+" = '"+date+"' " +
//                " UNION " +
//                "select  "+DataBaseEnums.KEY_ID+" from " +
//                TABLE_FOLLOW_UP+" where "+DataBaseEnums.KEY_DATE+" = '"+date+"' " +
//
//                ")";
        sql = "SELECT DISTINCT "+DataBaseEnums.KEY_ID+" FROM "+DataBaseEnums.TABLE_PATIENT+" WHERE "+KEY_SYNC_STATUS+" != '3' AND "
        +KEY_DATE +" = '"+date+"'";

try {
    Cursor cursor = db.rawQuery(sql, null);
    int c = cursor.getCount();
    if(cursor.getCount()!=0) {
        cursor.moveToFirst();
        do {
            patients.add(cursor.getString(0));

        }
        while (cursor.moveToNext());
    }
}
catch (Exception e)
{
    e.printStackTrace();
}

db.close();
        return patients;
    }




    public String getNotesDateFromVersion(int version,int pid)
    {
        String date = null;




            date = getVersionedNote(pid,version,TABLE_FOLLOW_UP);






        return date;
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
        values.put(KEY_VERSION,1);
        values.put(KEY_SYNC_STATUS, "0");
        // Inserting Row

       // db.insert(TABLE_HISTORY_HIST, null, values);
        int previousVersion = historyObj.get_version();
        if(previousVersion!=0) {
            String strSQL = " DELETE FROM " + TABLE_HISTORY + " WHERE " + KEY_ID + " = " + historyObj.get_pid() + " AND " + KEY_VERSION + " <= " + previousVersion;
            db.execSQL(strSQL);
            db.insert(TABLE_HISTORY, null, values);
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
    //gives date of a version of any notes table
    public String getVersionedNote(int pid,int version,String table)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        history_obj histObj = new history_obj();
        String date = null;

        String strSQL = " SELECT * FROM "+ table+" WHERE "+KEY_ID+" = "+pid+" AND "+KEY_VERSION+" = "+version;
        Cursor cursor = db.rawQuery(strSQL,null);
        if(cursor.getCount()!=0) {
            cursor.moveToFirst();
            date = cursor.getString(cursor.getColumnIndex(DataBaseEnums.KEY_DATE));

        }
        return date;
    }






    public history_obj getVersionedHistNote(int pid)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        history_obj histObj = null;
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
    // version --> segment

    public void addOtherFollowUp(other_obj otherObj)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, otherObj.get_pid());
        values.put(KEY_DATE, otherObj.get_date());
        values.put(KEY_VERSION,otherObj.get_version());
        values.put(KEY_FIELD_NAME,otherObj.get_field_name());
        values.put(KEY_FIELD_VALUE, otherObj.get_field_value());
        values.put(KEY_SYNC_STATUS, 0);


        long i =  db.insert(TABLE_OTHER_FOLLOW_UP, null, values);
        db.close(); // Closing database connection


    }
    public  ArrayList<other_obj> getOtherFollowUp(int pid,int version)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        other_obj otherObj[] = null;
        ArrayList<other_obj>other_objs = new ArrayList<>();

        String strSQL = " SELECT * FROM "+ TABLE_OTHER_FOLLOW_UP+" WHERE "+KEY_ID+" = "+pid+ " AND " + KEY_VERSION + " = " + version;

        Cursor cursor = db.rawQuery(strSQL, null);

        if(cursor.getCount()!=0) {
            cursor.moveToFirst();
            otherObj = new other_obj[cursor.getCount()];
            do{
                otherObj = utility.cursorToOther(cursor);

            }
            while (cursor.moveToNext());
            other_objs = new ArrayList<>(otherObj.length);
            for (int i = 0;i<otherObj.length;i++)
            {
                other_objs.add(otherObj[i]);
            }

        }
        return other_objs;
    }


    public void addOther(other_obj otherObj)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, otherObj.get_pid());
        values.put(KEY_DATE, otherObj.get_date());
        values.put(KEY_VERSION,otherObj.get_version());
        values.put(KEY_FIELD_NAME,otherObj.get_field_name());
        values.put(KEY_FIELD_VALUE, otherObj.get_field_value());
        values.put(KEY_SYNC_STATUS, 0);

        int j =  db.delete(TABLE_OTHER,
                KEY_ID + "=? AND " + KEY_VERSION + "=? AND " +
                        KEY_FIELD_NAME + "=?  ",
                new String[]{String.valueOf(otherObj.get_pid()), String.valueOf(otherObj.get_version()), otherObj.get_field_name()});

        long i =  db.insert(TABLE_OTHER, null, values);
        updatePatient(KEY_SYNC_STATUS,"0", String.valueOf(otherObj.get_pid()));
        updatePatient(KEY_DATE_LAST_VISIT,otherObj.get_date(), String.valueOf(otherObj.get_pid()));
        db.close(); // Closing database connection


    }
    public other_obj[] getLatestOtherNote(int pid,int version)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        other_obj otherObj[] = null;
        //int version = getCurrentVersion(pid);
        String strSQL = " SELECT * FROM "+ TABLE_OTHER+" WHERE "+KEY_ID+" = "+pid+ " AND " + KEY_VERSION + " = " + version;
        System.out.print(strSQL);
        Cursor cursor = db.rawQuery(strSQL, null);

        if(cursor.getCount()!=0) {
            cursor.moveToFirst();
            otherObj = new other_obj[cursor.getCount()];
           do{
                otherObj = utility.cursorToOther(cursor);

            }
           while (cursor.moveToNext());

        }
        return otherObj;
    }
    public ArrayList<other_obj> geOtherNote(int pid,int version)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        other_obj otherObj[] = null;
        ArrayList<other_obj>other_objs = new ArrayList<>();
        //int version = getCurrentVersion(pid);
        String strSQL = " SELECT * FROM "+ TABLE_OTHER+" WHERE "+KEY_ID+" = "+pid+ " AND " + KEY_VERSION + " = " + version;
        System.out.print(strSQL);
        Cursor cursor = db.rawQuery(strSQL, null);

        if(cursor.getCount()!=0) {
            cursor.moveToFirst();
            otherObj = new other_obj[cursor.getCount()];
            do{
                otherObj = utility.cursorToOther(cursor);

            }
            while (cursor.moveToNext());
            other_objs = new ArrayList<>(otherObj.length);
            for (int i = 0;i<otherObj.length;i++)
            {
                other_objs.add(otherObj[i]);
            }

        }
        return other_objs;
    }
    public  void deleteOtherNote(other_obj otherObj,int pid,int version)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String strSQL = " DELETE FROM "+ TABLE_OTHER+" WHERE "+KEY_ID+" = '"+pid+ "' AND " + KEY_VERSION + " = '" + version+ "' AND " + KEY_FIELD_NAME + " = '" + otherObj.get_field_name()+ "' AND " + KEY_FIELD_VALUE + " = '" + otherObj.get_field_value()+"'";
        db.rawQuery(strSQL, null);
        db.close();
    }



    //Media Functions
    public void addMediaFollowUp(media_obj mediaObj)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, mediaObj.get_pid());
        values.put(KEY_SECTION, mediaObj.get_section());
        values.put(KEY_VERSION, mediaObj.get_version());
        values.put(KEY_DOC_NAME,mediaObj.get_media_name());
        values.put(KEY_DOC_PATH, mediaObj.get_media_path());
        values.put(KEY_SYNC_STATUS, 0);
        values.put(KEY_BMP, mediaObj.get_bmp());


        long i = db.insert(TABLE_MEDIA_FOLLOW_UP, null, values);
        //long j =  db.insert(TABLE_OTHER_HIST, null, values);


        db.close(); // Closing database connection


    }
    public void updateMediaFollowUp(String columnName,String columnValue,String docPath)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(columnName, columnValue);

        db.update(TABLE_MEDIA_FOLLOW_UP, args, KEY_DOC_PATH + "= '" + docPath + "'", null) ;

    }
    public media_obj[] getMediaFollowUp(int pid,int version)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        media_obj mediaObjs[] = null;
        //int version = getCurrentVersion(pid)-1;
        String strSQL = " SELECT * FROM "+ TABLE_MEDIA_FOLLOW_UP+" WHERE "+KEY_ID+" = "+pid
                  + " AND " + KEY_VERSION + " = " + version;
                //+ " AND " + KEY_SECTION + " = " + section;
        Cursor cursor = db.rawQuery(strSQL, null);

        if(cursor.getCount()>0) {
            cursor.moveToFirst();
            mediaObjs = new media_obj[cursor.getCount()];
            mediaObjs = utility.cursorToMedia(cursor);

        }
        return mediaObjs;
    }
    public  ArrayList<media_obj> getMediaFollowUpTobeUploaded()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<media_obj> mediaObjs = new ArrayList<>();
        //int version = getCurrentVersion(pid)-1;
        String strSQL = " SELECT * FROM "+ TABLE_MEDIA_FOLLOW_UP+" WHERE "+KEY_SYNC_STATUS+" = "+0 ;
        //+ " AND " + KEY_SECTION + " = " + section;
        Cursor cursor = db.rawQuery(strSQL, null);

        if(cursor.getCount()>0) {
            cursor.moveToFirst();
           // mediaObjs = new media_obj[cursor.getCount()];
            mediaObjs = utility.cursorToMediaUpload(cursor);

        }
        db.close();
        return mediaObjs;
    }
    public ArrayList<media_obj> getMediaTobeUploaded()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<media_obj> mediaObjs = new ArrayList<>();
        //int version = getCurrentVersion(pid)-1;
        String strSQL = " SELECT * FROM "+ TABLE_MEDIA+" WHERE "+KEY_SYNC_STATUS+" = "+0 ;
        //+ " AND " + KEY_SECTION + " = " + section;
        Cursor cursor = db.rawQuery(strSQL, null);

        if(cursor.getCount()>0) {
            cursor.moveToFirst();
            // mediaObjs = new media_obj[cursor.getCount()];
            mediaObjs = utility.cursorToMediaUpload(cursor);

        }
        db.close();
        return mediaObjs;
    }
    public ArrayList<media_obj> getDocumentsTobeUploaded()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<media_obj> mediaObjs = new ArrayList<>();
        //int version = getCurrentVersion(pid)-1;
        String strSQL = " SELECT * FROM "+ TABLE_DOCUMENTS+" WHERE "+KEY_SYNC_STATUS+" = "+0 ;
        //+ " AND " + KEY_SECTION + " = " + section;
        Cursor cursor = db.rawQuery(strSQL, null);

        if(cursor.getCount()>0) {
            cursor.moveToFirst();
            // mediaObjs = new media_obj[cursor.getCount()];
            mediaObjs = utility.cursorToDocumentsUpload(cursor);

        }
        db.close();
        return mediaObjs;
    }

    public void addMedia(media_obj mediaObj)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, mediaObj.get_pid());
        values.put(KEY_SECTION, mediaObj.get_section());
        values.put(KEY_VERSION,mediaObj.get_version());
        values.put(KEY_DOC_NAME,mediaObj.get_media_name());
        values.put(KEY_DOC_PATH, mediaObj.get_media_path());
        values.put(KEY_SYNC_STATUS,0);
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
                      //  + " AND " + KEY_VERSION + " = " + version
                        + " AND " + KEY_SECTION + " = " + section;
        Cursor cursor = db.rawQuery(strSQL, null);

        if(cursor.getCount()>0) {
            cursor.moveToFirst();
            mediaObjs = new media_obj[cursor.getCount()];
            mediaObjs = utility.cursorToMedia(cursor);

        }  db.close();
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
   public void deleteMedia(int version)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String path;
        String strSQL = " SELECT * FROM "+ TABLE_MEDIA+" WHERE "+KEY_VERSION+" = "+version;
        Cursor cursor = db.rawQuery(strSQL, null);

        while (cursor.moveToNext()) {

            path = cursor.getString(cursor.getColumnIndex("documentPath"));
            File file = new File(path);
            boolean a = file.delete();


        }
        db.delete(TABLE_MEDIA, KEY_VERSION + " = ?",
                new String[]{String.valueOf(version)});
        db.close();



    }

    public int[] getMaxFollowupVersion(int pid)

    {
        SQLiteDatabase db = this.getWritableDatabase();
        String strSQL = " SELECT DISTINCT "+KEY_VERSION+" FROM "+ TABLE_FOLLOW_UP+" WHERE "+KEY_ID+" = "+pid;

        int i =0;
        Cursor cursor = db.rawQuery(strSQL, null);
        int versions[] = new int[cursor.getCount()];
        if(cursor.getCount()>0)
        {
            cursor.moveToFirst();
           do {
              versions[i]=  cursor.getInt(cursor.getColumnIndex(KEY_VERSION));
Log.i("versions", String.valueOf(cursor.getInt(cursor.getColumnIndex(KEY_VERSION))));
               i++;


            } while (cursor.moveToNext());
            //cursor.moveToLast();
            db.close();
           // return cursor.getInt(cursor.getColumnIndex("COUNT( DISTINCT "+KEY_VERSION+")"));
        }
        else {
            db.close();
            //return 0;}
        }

        return versions;
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
        updatePatient(KEY_SYNC_STATUS, "0", String.valueOf(document.get_id()));
        updatePatient(KEY_DATE_LAST_VISIT,document.get_date(), String.valueOf(document.get_id()));
        db.close(); // Closing database connection


    }
    public void addDocument(document_obj document,int syncStatus)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID,document.get_id());
        values.put(KEY_DOC_NAME,document.get_doc_name());
        values.put(KEY_DOC_PATH,document.get_doc_path());
        values.put(KEY_BMP,document.get_bmp());
        values.put(KEY_DATE,document.get_date());

        values.put(KEY_SYNC_STATUS,syncStatus);

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
                    String name = cursor.getString(cursor.getColumnIndex("documentName"));
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
    public List<document_obj> getAllDocumentsForDownload(int syncStatus) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<document_obj> docList = new ArrayList<document_obj>();
        int pos = 0;
        String strSQL = "SELECT  * FROM " + TABLE_DOCUMENTS+" WHERE "+ KEY_SYNC_STATUS+" == '"+syncStatus+"'";
        document_obj doc_obj = new document_obj();
        Cursor cursor = db.rawQuery(strSQL,null);
        int cur = cursor.getCount();
        if (cur != 0) {
            cursor.moveToFirst();

            for(int i = 1;i<=cur;i++) {


                String name = cursor.getString(cursor.getColumnIndex("documentName"));
                String path = cursor.getString(cursor.getColumnIndex("documentPath"));
                byte[] bmp = cursor.getBlob(cursor.getColumnIndex("bitmapBLOB"));
                int id1 = Integer.parseInt(cursor.getString(0));
                doc_obj = new document_obj(id1,
                        name, path,bmp);
                docList.add(doc_obj);
                //break;

                if(i !=cur)
                    cursor.moveToNext();


            }


        }
        db.close();
        return docList;
    }
    public List<document_obj> getAllDocuments() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<document_obj> docList = new ArrayList<document_obj>();
        int pos = 0;
        String strSQL = "SELECT  * FROM " + TABLE_DOCUMENTS+" WHERE "+ KEY_SYNC_STATUS+" != 3";
        document_obj doc_obj = new document_obj();
        Cursor cursor = db.rawQuery(strSQL,null);
        int cur = cursor.getCount();
        if (cur != 0) {
            cursor.moveToFirst();

            for(int i = 1;i<=cur;i++) {


                    String name = cursor.getString(cursor.getColumnIndex("documentName"));
                    String path = cursor.getString(cursor.getColumnIndex("documentPath"));
                    byte[] bmp = cursor.getBlob(cursor.getColumnIndex("bitmapBLOB"));
                    int id1 = Integer.parseInt(cursor.getString(0));
                    doc_obj = new document_obj(id1,
                            name, path,bmp);
                    docList.add(doc_obj);
                    //break;

                if(i !=cur)
                    cursor.moveToNext();


            }


        }
        db.close();
        return docList;
    }


    public List<document_obj> getAllMediaForSyncsStatus(int syncStatus) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<document_obj> docList = new ArrayList<document_obj>();
        int pos = 0;
        String strSQL = "SELECT  * FROM " + TABLE_MEDIA+" WHERE "+ KEY_SYNC_STATUS+" == '"+syncStatus+"'";
        document_obj doc_obj = new document_obj();
        Cursor cursor = db.rawQuery(strSQL,null);
        int cur = cursor.getCount();
        if (cur != 0) {
            cursor.moveToFirst();

            for(int i = 1;i<=cur;i++) {


                String name = cursor.getString(cursor.getColumnIndex("documentName"));
                String path = cursor.getString(cursor.getColumnIndex("documentPath"));
                byte[] bmp = cursor.getBlob(cursor.getColumnIndex("bitmapBLOB"));
                int id1 = Integer.parseInt(cursor.getString(0));
                doc_obj = new document_obj(id1,
                        name, path,bmp);
                docList.add(doc_obj);
                //break;

                if(i !=cur)
                    cursor.moveToNext();


            }


        }
        db.close();
        return docList;
    }
    public List<document_obj> getAllMedia() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<document_obj> docList = new ArrayList<document_obj>();
        int pos = 0;
        String strSQL = "SELECT  * FROM " + TABLE_MEDIA+" WHERE "+ KEY_SYNC_STATUS+" != 3";
        document_obj doc_obj = new document_obj();
        Cursor cursor = db.rawQuery(strSQL,null);
        int cur = cursor.getCount();
        if (cur != 0) {
            cursor.moveToFirst();

            for(int i = 1;i<=cur;i++) {


                String name = cursor.getString(cursor.getColumnIndex("documentName"));
                String path = cursor.getString(cursor.getColumnIndex("documentPath"));
                byte[] bmp = cursor.getBlob(cursor.getColumnIndex("bitmapBLOB"));
                int id1 = Integer.parseInt(cursor.getString(0));
                doc_obj = new document_obj(id1,
                        name, path,bmp);
                docList.add(doc_obj);
                //break;

                if(i !=cur)
                    cursor.moveToNext();


            }


        }
        db.close();
        return docList;
    }

    public List<document_obj> getAllMediaFollowUpForSyncStatus(int syncStatus) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<document_obj> docList = new ArrayList<document_obj>();
        int pos = 0;
        String strSQL = "SELECT  * FROM " + TABLE_MEDIA_FOLLOW_UP+" WHERE "+ KEY_SYNC_STATUS+" == '"+syncStatus+"'";
        document_obj doc_obj = new document_obj();
        Cursor cursor = db.rawQuery(strSQL,null);
        int cur = cursor.getCount();
        if (cur != 0) {
            cursor.moveToFirst();

            for(int i = 1;i<=cur;i++) {


                String name = cursor.getString(cursor.getColumnIndex("documentName"));
                String path = cursor.getString(cursor.getColumnIndex("documentPath"));
                byte[] bmp = cursor.getBlob(cursor.getColumnIndex("bitmapBLOB"));
                int id1 = Integer.parseInt(cursor.getString(0));
                doc_obj = new document_obj(id1,
                        name, path,bmp);
                docList.add(doc_obj);
                //break;

                if(i !=cur)
                    cursor.moveToNext();


            }


        }
        db.close();
        return docList;
    }


    public List<document_obj> getAllMediaFollowUp() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<document_obj> docList = new ArrayList<document_obj>();
        int pos = 0;
        String strSQL = "SELECT  * FROM " + TABLE_MEDIA_FOLLOW_UP+" WHERE "+ KEY_SYNC_STATUS+" != 3";
        document_obj doc_obj = new document_obj();
        Cursor cursor = db.rawQuery(strSQL,null);
        int cur = cursor.getCount();
        if (cur != 0) {
            cursor.moveToFirst();

            for(int i = 1;i<=cur;i++) {


                String name = cursor.getString(cursor.getColumnIndex("documentName"));
                String path = cursor.getString(cursor.getColumnIndex("documentPath"));
                byte[] bmp = cursor.getBlob(cursor.getColumnIndex("bitmapBLOB"));
                int id1 = Integer.parseInt(cursor.getString(0));
                doc_obj = new document_obj(id1,
                        name, path,bmp);
                docList.add(doc_obj);
                //break;

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


    public void updateDocument(String field,String value,String docPath) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(field, value);

        db.update(TABLE_DOCUMENTS, args, KEY_DOC_PATH + "= '" + docPath + "'", null) ;
        db.close();

    }

    public int updateAllDocuments(int id ,String status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues args = new ContentValues();
        args.put(KEY_SYNC_STATUS, status);



        // updating row
        return db.update(TABLE_DOCUMENTS, args, KEY_ID + " = ?",
                new String[] {String.valueOf(id)});
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
        updatePatient(getPatient(documentObj.get_id()),0);

    }

    //patient functions
   public long  addPatient(Patient patient,Context context) {
        SQLiteDatabase db = this.getWritableDatabase();
       Calendar c = Calendar.getInstance();
       SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
       String formattedDate = df.format(c.getTime());
       Long tsLong = System.currentTimeMillis()/1000;
        String a = patient.get_diagnosis();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, patient.get_name());
        values.put(KEY_AGE, patient.get_age());
        values.put(KEY_GENDER, patient.get_gender());
        values.put(KEY_HEIGHT, patient.get_height());
        values.put(KEY_WEIGHT, patient.get_weight());
        values.put(KEY_OPD_IPD, patient.get_opd_ipd());
        if(patient.get_id()!=0) {
            values.put(KEY_ID, patient.get_id());
            db.execSQL("delete from " + TABLE_PATIENT + " WHERE " + KEY_ID + " = '" + patient.get_id() + "'");
        }

       if(!patient.get_bmp().equals(PhotoHelper.getBitmapAsByteArray(BitmapFactory.decodeResource(context.getResources(), R.drawable.default_photo))))
           values.put(KEY_BMP,patient.get_bmp());
       else
           values.put(KEY_BMP, new byte[0]);
        values.put(KEY_ADDRESS,patient.get_address());

       values.put(KEY_FIRST_AID_ID,patient.get_first_aid_id());
        values.put(KEY_OCCUPATION,patient.get_ocupation());
        values.put(KEY_CONTACT,patient.get_contact_number());
        values.put(KEY_EMAIL,patient.get_email());
        values.put(KEY_DIAGNOSIS,patient.get_diagnosis());
        values.put(KEY_DATE_LAST_VISIT,formattedDate );
        values.put(KEY_DATE,formattedDate );
        values.put(KEY_SYNC_STATUS,0);

         values.put(KEY_PHOTO_PATH,patient.get_photoPath());
         values.put(KEY_DATE_NEXT_FOLLOW_UP,patient.get_next_follow_up_date());
       if(patient.get_id()!=0) {

           db.execSQL("delete from " + TABLE_PATIENT + " WHERE " + KEY_ID + " = '" + patient.get_id() + "'");
       }

        // Inserting Row
       long id = db.insert(TABLE_PATIENT, null, values);
       if(patient.get_id()==0)
       {updatePatient(KEY_ID, String.valueOf(tsLong),String.valueOf(id));
       id= tsLong;}
       File storageDir =
               new File(Environment.getExternalStoragePublicDirectory(
                       Environment.DIRECTORY_PICTURES), "Patient Manager/"+id);
       if(!storageDir.exists()) {

           storageDir.mkdir();
       }
       File storageDir1 =
               new File(Environment.getExternalStoragePublicDirectory(
                       Environment.DIRECTORY_PICTURES), "Patient Manager/"+id+"/Documents");
       if(!storageDir1.exists())
           storageDir1.mkdir();
        db.close(); // Closing database connection
       if(patient.get_first_aid_id()==0)
       updatePatient(KEY_FIRST_AID_ID, String.valueOf(tsLong),String.valueOf(id));


       return id;
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
        if ((cursor != null)&&(cursor.getCount()>0)) {
             cursor.moveToFirst();
           // cursor.moveToPosition(id);
            patient = utility.cursorToPatient(cursor);
             status = cursor.getString(cursor.getColumnIndex(KEY_SYNC_STATUS));



        }

        db.close();
        return patient;
    }
    public Patient getPatientFromFirstAidId(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        int pos = 0;
        Patient patient = null;
        String strSQL = "SELECT  * FROM " + TABLE_PATIENT+ " WHERE "+KEY_FIRST_AID_ID +" = "+id;
        String status;
        Cursor cursor = db.rawQuery(strSQL,null);
                /*db.query(TABLE_PATIENT, new String[] { KEY_ID,
                        KEY_NAME,KEY_AGE,KEY_GENDER,KEY_HEIGHT,KEY_BMP }, KEY_ID + "=?",new String[] { String.valueOf(id) }, null, null, null, null);*/
        if ((cursor != null)&&(cursor.getCount()>0)) {
            cursor.moveToFirst();
            // cursor.moveToPosition(id);
            patient = utility.cursorToPatient(cursor);
            status = cursor.getString(cursor.getColumnIndex(KEY_SYNC_STATUS));



        }

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
        if(cursor.getCount()>0)
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
                if(cursor.getString(cursor.getColumnIndex(DataBaseEnums.KEY_FIRST_AID_ID))!=null)

                    patient.set_first_aid_id(Long.parseLong(cursor.getString(cursor.getColumnIndex(DataBaseEnums.KEY_FIRST_AID_ID))));
                    else
                    patient.set_first_aid_id(Long.parseLong(cursor.getString(cursor.getColumnIndex(DataBaseEnums.KEY_ID))));
                updatePatient(KEY_FIRST_AID_ID, String.valueOf(patient.get_first_aid_id()),String.valueOf(patient.get_id()));

                String s = cursor.getString(cursor.getColumnIndex(KEY_PHOTO_PATH));

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

        Cursor cursor = db.rawQuery(strSQL, null);
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

            patient = utility.cursorToPatient(cursor);

        }

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
        values.put(KEY_WEIGHT, patient.get_weight());
        values.put(KEY_OPD_IPD, patient.get_opd_ipd());
        values.put(KEY_BMP,patient.get_bmp());
        values.put(KEY_DIAGNOSIS, patient.get_diagnosis());
        values.put(KEY_DATE_LAST_VISIT, patient.get_last_seen_date());
        values.put(KEY_SYNC_STATUS, "5");
        values.put(KEY_ADDRESS,patient.get_address());
        values.put(KEY_OCCUPATION,patient.get_ocupation());
        values.put(KEY_CONTACT,patient.get_contact_number());
        values.put(KEY_EMAIL, patient.get_email());
        values.put(KEY_DATE_NEXT_FOLLOW_UP, patient.get_next_follow_up_date());

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
        values.put(KEY_WEIGHT, patient.get_weight());
        values.put(KEY_OPD_IPD, patient.get_opd_ipd());
        values.put(KEY_BMP,patient.get_bmp());
        values.put(KEY_DIAGNOSIS, patient.get_diagnosis());
        values.put(KEY_DATE_LAST_VISIT, patient.get_last_seen_date());
        values.put(KEY_SYNC_STATUS, syncingStatus);
        values.put(KEY_ADDRESS,patient.get_address());
        values.put(KEY_OCCUPATION,patient.get_ocupation());
        values.put(KEY_CONTACT,patient.get_contact_number());
        values.put(KEY_EMAIL, patient.get_email());
        values.put(KEY_DATE_NEXT_FOLLOW_UP, patient.get_next_follow_up_date());

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

                db.delete(TABLE_MEDIA, KEY_ID + " = ?",
                        new String[]{String.valueOf(patient.get_id())});
                db.delete(TABLE_OTHER, KEY_ID + " = ?",
                        new String[]{String.valueOf(patient.get_id())});
                db.delete(TABLE_FOLLOW_UP, KEY_ID + " = ?",
                        new String[]{String.valueOf(patient.get_id())});
                db.delete(TABLE_MEDIA_FOLLOW_UP, KEY_ID + " = ?",
                        new String[]{String.valueOf(patient.get_id())});
                db.delete(TABLE_OTHER_FOLLOW_UP, KEY_ID + " = ?",
                        new String[]{String.valueOf(patient.get_id())});


                db.delete(TABLE_TREATMENT, KEY_ID + " = ?",
                        new String[]{String.valueOf(patient.get_id())});
                db.delete(TABLE_DIAGNOSIS, KEY_ID + " = ?",
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

    public List<Patient> search(String nameString,String diagnosisString, String locationString)
    {
        List<Patient> patientList = new ArrayList<Patient>();
        String searchNameQuery = "";
        if((!nameString.equals(""))&&(diagnosisString.equals(""))&&(locationString.equals("")))
         searchNameQuery = "SELECT * FROM " + TABLE_PATIENT +
                " WHERE " + KEY_NAME + " LIKE '%" + nameString + "%' AND "+KEY_SYNC_STATUS+" != 3";

        else if(((!nameString.equals(""))&&(!diagnosisString.equals(""))&&(!locationString.equals("")))||
        ((nameString.equals(""))&&(diagnosisString.equals(""))&&(locationString.equals(""))))
            searchNameQuery = "SELECT * FROM " + TABLE_PATIENT +
                    " WHERE " + KEY_NAME + " LIKE '%" + nameString + "%' OR "+
                    KEY_DIAGNOSIS+ " LIKE '%" + diagnosisString + "%' OR "+
                    KEY_ADDRESS+ " LIKE '%" + locationString + "%' AND "+KEY_SYNC_STATUS+" != 3";

        else if((nameString.equals(""))&&(!diagnosisString.equals(""))&&(locationString.equals("")))
            searchNameQuery = "SELECT * FROM " + TABLE_PATIENT +
                    " WHERE " + KEY_DIAGNOSIS+ " LIKE '%" + diagnosisString + "%' AND "+KEY_SYNC_STATUS+" != 3";

        else if((nameString.equals(""))&&(diagnosisString.equals(""))&&(!locationString.equals("")))
            searchNameQuery = "SELECT * FROM " + TABLE_PATIENT +
                    " WHERE " +KEY_ADDRESS+ " LIKE '%" + locationString + "%' AND "+KEY_SYNC_STATUS+" != 3";

        else if((!nameString.equals(""))&&(!diagnosisString.equals(""))&&(locationString.equals("")))
            searchNameQuery = "SELECT * FROM " + TABLE_PATIENT +
                    " WHERE " + KEY_NAME + " LIKE '%" + nameString + "%' OR "+
                    KEY_DIAGNOSIS+ " LIKE '%" + diagnosisString + "%' AND "+KEY_SYNC_STATUS+" != 3";

        else if((nameString.equals(""))&&(!diagnosisString.equals(""))&&(!locationString.equals("")))
            searchNameQuery = "SELECT * FROM " + TABLE_PATIENT +
                    " WHERE " + KEY_DIAGNOSIS+ " LIKE '%" + diagnosisString + "%' OR "+
                    KEY_ADDRESS+ " LIKE '%" + locationString + "%' AND "+KEY_SYNC_STATUS+" != 3";

        else if((!nameString.equals(""))&&(diagnosisString.equals(""))&&(!locationString.equals("")))
            searchNameQuery = "SELECT * FROM " + TABLE_PATIENT +
                    " WHERE " + KEY_NAME + " LIKE '%" + nameString + "%' OR "+
                    KEY_ADDRESS+ " LIKE '%" + locationString + "%' AND "+KEY_SYNC_STATUS+" != 3";

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
        String selectQuery = "SELECT  * FROM patient WHERE "+KEY_SYNC_STATUS +" != 1 AND " +KEY_SYNC_STATUS +" != 5 " ;
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


    public String composeJSONforPersonalInfo(Context context,String city)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        String Sql = "Select * FROM personalInfo" ;
        Cursor cursor = database.rawQuery(Sql, null);
        ArrayList<ArrayList<String>> personalInfo = new ArrayList<ArrayList<String>>();
        ArrayList<String>list = new ArrayList<>();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String accountType = prefs.getString(context.getString(R.string.account_type),"");
        HashMap<String, String> map = new HashMap<String, String>();
        if (cursor.moveToFirst())
        {
            list = utility.cursorToPersonalInfoArrayList(cursor);
            map.put( cursor.getString(0), cursor.getString(1));
        }
        if(accountType.equals(context.getString(R.string.account_type_doctor)))
        {
            list.add("2");
        }
        else
        {
            list.add("1");
        }
        database.close();
        list.add(city);
        personalInfo.add(list);
        String s1 = null;
        ArrayList<String> hex = new ArrayList<>();

        String hash = prefs.getString(context.getString(R.string.hash_code), "");
        hex.add(hash);
        personalInfo.add(hex);
        StringWriter out = new StringWriter();
        try {
            JSONValue.writeJSONString(personalInfo, out);
            s1 = out.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return s1;
    }

    public String composeJSONfromEmailPassword(Context context)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String accountType = prefs.getString(context.getString(R.string.account_type),"");
        String Sql = "Select * FROM personalInfo" ;
        Cursor cursor = database.rawQuery(Sql, null);
        ArrayList<ArrayList<String>> personalInfo = new ArrayList<ArrayList<String>>();
        ArrayList<String>list = new ArrayList<>();

        HashMap<String, String> map = new HashMap<String, String>();
        if (cursor.moveToFirst())
        {
            list = utility.cursorToSimpleArraylist(cursor);
            map.put( cursor.getString(0), cursor.getString(1));
        }
  if(accountType.equals(context.getString(R.string.account_type_doctor)))
  {
      list.add("2");
  }
        else
  {
      list.add("1");
  }
        database.close();
        personalInfo.add(list);
        ArrayList<String>deviceId = new ArrayList<>();

        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
if(telephonyManager.getDeviceId()!=null)
        deviceId.add(telephonyManager.getDeviceId());
        else
    deviceId.add("");


        personalInfo.add(deviceId);
        String s1 = null;

        StringWriter out = new StringWriter();
        try {
            JSONValue.writeJSONString(personalInfo, out);
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



        //Use GSON to serialize Array List to JSON
        return s1;
    }

    public String composeJSONfromSQLitePatient(String pid,Context context){
        ArrayList<ArrayList<String>> patientList;
        patientList = new ArrayList<ArrayList<String>>();
        String selectQuery = "SELECT  * FROM patient WHERE "+KEY_SYNC_STATUS+" != '1' AND "+KEY_SYNC_STATUS+" != '6' AND "+KEY_ID+" = "+pid ;
        Patient patient = new Patient();
        patient = this.getPatient(Integer.parseInt(pid));
        personal_obj personalObj = this.getPersonalInfo();

        String hexString = personalObj.get_customerId()+personalObj.get_email();
        String hexCode = this.convertStringToHex(hexString);
        String c = String.valueOf(personalObj.get_customerId());
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                ArrayList<String> list = new ArrayList<>();

                patient = utility.cursorToPatient(cursor);
                String syncStatus = cursor.getString(cursor.getColumnIndex(KEY_SYNC_STATUS));
                if(Integer.parseInt(syncStatus)==5)
                {
                    if(!syncStatus.equals("3"))
                        this.updatePatient(patient,6);
                }
                else
                {
                    if(!syncStatus.equals("3"))
                        this.updatePatient(patient,1);
                }



                list = utility.cursorToPatientArray(cursor,c);
                patientList.add(list);

                //map.put("customerId", "27");

            } while (cursor.moveToNext());
        }
        database.close();
        ArrayList<String> hex = new ArrayList<>();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        String hash = prefs.getString("Hash", "");
        hex.add(hash);

        patientList.add(hex);
        String s1 = null;

        StringWriter out = new StringWriter();

        try {
            JSONValue.writeJSONString(patientList, out);
            s1 = out.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }



        //Use GSON to serialize Array List to JSON
        return s1;
    }






    public String composeJSONfromSQLiteDocuments(String pid,Context context){
        ArrayList<ArrayList<String>> documentsList;
        documentsList = new ArrayList<ArrayList<String>>();

        String selectQuery = "SELECT  * FROM documents WHERE "+KEY_SYNC_STATUS+" != '1' AND "+KEY_SYNC_STATUS+" != '6' AND "+KEY_ID+" = "+pid ;
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
//                    uploadfile.uploadImage(context, documentObj.get_doc_path(), pid);
                }


                if (cursor.getString(cursor.getColumnIndex(KEY_SYNC_STATUS)).equals("3"))
                {
                    deleteDocument(documentObj);
                }
                String docPath = list.get(4);
                String syncStatus = cursor.getString(cursor.getColumnIndex(KEY_SYNC_STATUS));


//                if(Integer.parseInt(syncStatus)==5)
//                {
//                    if(!syncStatus.equals("3"))
//                        updateDocument(documentObj, "6");
//                } else {
//                    if(!syncStatus.equals("3"))
//                        updateDocument(documentObj,"1");
//                }
              // updateDocument(documentObj,"1");

               // documentObj = utility.cursorToDocument(cursor);
               // String syncStatus = cursor.getString(cursor.getColumnIndex(KEY_SYNC_STATUS));
               /* if(!syncStatus.equals("3"))
                    this.updatePatient(patient,1);*/
              //  list = utility.cursorToPatientArray(cursor,c);
                documentsList.add(list);

                //map.put("customerId", "27");

            } while (cursor.moveToNext());
           // uploadfile.uploadImage(context, docPaths, pid);
           // FTPHelper.Dowork(docPaths,getPatient(documentObj.get_id()).get_name(),c,context);


        }
       // database.close();
        ArrayList<String> hex = new ArrayList<>();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);


        String hash = sharedPref.getString(context.getString(R.string.hash_code), "");
        hex.add(hash);

        documentsList.add(hex);

        String s1 = null;

        StringWriter out = new StringWriter();
        try {
            JSONValue.writeJSONString(documentsList, out);
            s1 = out.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }



        //Use GSON to serialize Array List to JSON
        return s1;
    }
    public String composeJSONfromSQLiteAppointmentSettings(Context context){
        ArrayList<ArrayList<String>> notesList;
        notesList = new ArrayList<ArrayList<String>>();
        String selectQuery = "SELECT  * FROM "+ TABLE_APPOINTMENT_SETTINGS  ;

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
        ArrayList<String> hex = new ArrayList<>();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        String hash = prefs.getString("Hash", "");
        hex.add(hash);
        notesList.add(hex);
        StringWriter out = new StringWriter();
        try {
            JSONValue.writeJSONString(notesList, out);
            s1 = out.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return s1;
    }



    public String composeJSONfromSQLiteNotes(String pid,String tableName,Context context){
        ArrayList<ArrayList<String>> notesList;
        notesList = new ArrayList<ArrayList<String>>();
        String selectQuery = "SELECT  * FROM "+ tableName +" WHERE "+KEY_SYNC_STATUS+" != '1' AND "+KEY_ID+" = "+pid ;
        ArrayList<String> docPaths = new ArrayList<>();
        personal_obj personalObj = this.getPersonalInfo();
        String c = String.valueOf(personalObj.get_customerId());
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                ArrayList<String> list = new ArrayList<>();
                if(tableName.contains("media"))
                {
                    if (docPaths != null) {

                           docPaths.add(cursor.getString(cursor.getColumnIndex(KEY_DOC_PATH)));


                    }

                    list = utility.cursorToArrayListMedia(cursor, c);

                }
                else {
                    if((!tableName.contains("diagnosis"))&&(!tableName.contains("treatment"))&&(!tableName.contains("media"))) {
                        updateTableSyncStatus(cursor.getString(cursor.getColumnIndex(KEY_FIELD_NAME)), cursor.getString(cursor.getColumnIndex(KEY_FIELD_VALUE)), tableName, pid);
                    }
                    else  if(tableName.contains("diagnosis"))
                    {
                        updateDiagnosis(KEY_SYNC_STATUS, "1", cursor.getString(cursor.getColumnIndex(KEY_VERSION)),pid);
                    }
                    else  if(tableName.contains("treatment"))
                    {
                        updateTreatment(KEY_SYNC_STATUS, "1", cursor.getString(cursor.getColumnIndex(KEY_VERSION)),pid);
                    }
                    list = utility.cursorToArrayList(cursor, c);

                }
                notesList.add(list);


            } while (cursor.moveToNext());
        }
       // database.close();
        //uploadfile.uploadImage(context, "");
        if (docPaths.size()>0) {
           // uploadfile.uploadImage(context, docPaths, pid);
       // FTPHelper.Dowork(docPaths, pid, c,context);
        }
        String s1 = null;
        ArrayList<String> hex = new ArrayList<>();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        String hash = prefs.getString("Hash", "");
        hex.add(hash);
        notesList.add(hex);
        StringWriter out = new StringWriter();
        try {
            JSONValue.writeJSONString(notesList, out);
            s1 = out.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return s1;
    }

    public  ArrayList<String>  composeJSONfromSQLitePatientHelper(String pid,Context context,String accountType){

        String selectQuery = "";


        if(accountType.equals(context.getString(R.string.account_type_helper)))

            selectQuery  = "SELECT  * FROM patient WHERE patient."+KEY_SYNC_STATUS+" != '1' AND "+KEY_ID+" = "+pid ;

        else
        {
            selectQuery  = "SELECT  * FROM patient WHERE patient."+KEY_SYNC_STATUS+" in (1,0) AND "+KEY_ID+" = "+pid ;
        }



        Patient patient = new Patient();
        patient = this.getPatient(Integer.parseInt(pid));
        personal_obj personalObj = this.getPersonalInfo();
        ArrayList<String> list = new ArrayList<>();
        String hexString = personalObj.get_customerId()+personalObj.get_email();

        String c = String.valueOf(personalObj.get_customerId());
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {


                patient = utility.cursorToPatient(cursor);
                String syncStatus = cursor.getString(cursor.getColumnIndex(KEY_SYNC_STATUS));
                if(!syncStatus.equals("3"))
                   // this.updatePatient(patient,0);
                list = utility.cursorToPatientArray(cursor,c);
                Log.d("SQL", String.valueOf(patient.get_id()));
                if(accountType.equals(context.getString(R.string.account_type_helper)))
                if(checkDoctorHelperPatientMapping(Integer.parseInt(list.get(1)))==0)
                {
                    list.set(0,"0");
                }
                else
                {
                    list.set(0, String.valueOf(checkDoctorHelperPatientMapping(Integer.parseInt(list.get(1)))));
                }


                //map.put("customerId", "27");

            } while (cursor.moveToNext());
        }
        database.close();

        String s1 = null;

        StringWriter out = new StringWriter();

        try {
            JSONValue.writeJSONString(list, out);
            s1 = out.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }



        //Use GSON to serialize Array List to JSON
        return list;
    }



    public  ArrayList<ArrayList<String>> composeJSONfromSQLiteDocumentsHelper(String pid,Context context,String accountType){
        String selectQuery ="";
        ArrayList<ArrayList<String>> documentsList = new ArrayList<>();
        if(accountType.equals(context.getString(R.string.account_type_helper)))

            selectQuery  = "SELECT  * FROM documents WHERE documents."+KEY_SYNC_STATUS+" != '1' AND "+KEY_ID+" = "+pid ;

        else
        {
            selectQuery  = "SELECT  * FROM documents WHERE documents."+KEY_SYNC_STATUS+" in (1,0) AND "+KEY_ID+" = "+pid ;
        }


        personal_obj personalObj = this.getPersonalInfo();
        String c = String.valueOf(personalObj.get_customerId());
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                ArrayList<String> list = new ArrayList<>();
                list = utility.cursorToDocumentsArray(cursor, c);
                int docPID = checkDoctorHelperPatientMapping(Integer.parseInt(pid));
                if(docPID !=0)
                {
                    list.set(1, String.valueOf(docPID));
                    String newPath =  getMappedDoctorDocPath(list.get(4));
                    list.set(4,newPath);
                }

                documentsList.add(list);
            } while (cursor.moveToNext());

        }
        database.close();



        //Use GSON to serialize Array List to JSON
        return documentsList;
    }








    public String convertStringToHex(String str){

        char[] chars = str.toCharArray();

        StringBuffer hex = new StringBuffer();
        for(int i = 0; i < chars.length; i++){
            hex.append(Integer.toHexString((int)chars[i]));
        }

        return hex.toString();
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
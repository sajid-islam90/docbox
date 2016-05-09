package com.example.sajid.myapplication;
//UTILITY CLASS FOR PHOTO
import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import objects.Patient;
import objects.document_obj;
import objects.media_obj;
import objects.personal_obj;

/**
 * Created by sajid on 2/14/2015.
 */
public class PhotoHelper {

    static String mCurrentPhotoPath;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    private static final int REQUEST_TAKE_PHOTO = 100;


    public static File createImageFile(int id) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_P"+id+"_";
        File storageDir =
                new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES), "Patient Manager/"+"profile_pictures");
        if(!storageDir.exists()) {

            storageDir.mkdir();}
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    public static File createImageFileForNotes(int id,Context context) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        DatabaseHandler databaseHandler = new DatabaseHandler(context);
        Patient patient = databaseHandler.getPatient(id);
       ///String id = patient.get_name();
        String imageFileName = "JPEG_" + timeStamp + "_P"+id+"_";


        File storageDir =
                new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES), "Patient Manager/"+id+"/Notes");
        if(!storageDir.exists())
            storageDir.mkdir();



        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }


    public static File createImageFileForDocument(int id,Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        DatabaseHandler databaseHandler = new DatabaseHandler(context);
       Patient patient = databaseHandler.getPatient(id);
        String name = patient.get_name();
        String imageFileName = "JPEG_" + timeStamp + "_P"+id+"_";
        File storageDir =
                new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES), "Patient Manager/"+id+"/Documents");
        if(!storageDir.exists())
         storageDir.mkdir();




        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        String accountType;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        accountType = prefs.getString(context.getString(R.string.account_type), "");
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        if(accountType.equals(context.getString(R.string.account_type_helper))) {
            int docPid = databaseHandler.checkDoctorHelperPatientMapping(id);
            File storageDir1 =
                    new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES), "Patient Manager/"+docPid+"/Documents");

            String path = storageDir1.getPath()+"/"+image.getName();

           //  path.replaceAll("/" + String.valueOf(id) + "/", "/" + docPid + "/");
            databaseHandler.mapDoctorHelperDocuments(path,image.getPath());
        }
        return image;
    }


    public static File createVideoFile(int id) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "VID_" + timeStamp + "_P"+id+"_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".mp4",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    public static File createVideoFile(int id,Context context) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "VID_" + timeStamp + "_P"+id+"_";




        File storageDir =
                new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES), "Patient Manager/"+id+"/Notes");
        if(!storageDir.exists())
            storageDir.mkdir();



        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".mp4",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }



    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] a = outputStream.toByteArray();
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  a;
    }

    /*public Bitmap getImage(int i){


       // String qu = "select img  from table where feedid=" + i ;
        Cursor cur = db.rawQuery(qu, null);

        if (cur.moveToFirst()){
            byte[] imgByte = cur.getBlob(0);
            cur.close();
            return BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
        }
        if (cur != null && !cur.isClosed()) {
            cur.close();
        }

        return null ;
    }*/

    public static document_obj addMissingBmp(document_obj doc_obj)
    {


       // while (BitmapFactory.decodeFile(doc_obj.get_doc_path())==null);
        Bitmap bmp = BitmapFactory.decodeFile(doc_obj.get_doc_path());
        if(bmp!=null)
        bmp = getResizedBitmap(bmp,150,150);
        doc_obj.set_bmp(PhotoHelper.getBitmapAsByteArray(bmp));
        return doc_obj;

    }
    public static Patient addMissingBmp(Patient patient)
    {
        Bitmap bmp = BitmapFactory.decodeFile(patient.get_photoPath());
        bmp = getResizedBitmap(bmp,150,150);
        patient.set_bmp(PhotoHelper.getBitmapAsByteArray(bmp));
        return patient;

    }
    public static media_obj  addMissingBmp(media_obj mediaObj,int mode)
    {
       // Bitmap bmp = BitmapFactory.decodeFile(mediaObj.get_media_path())
        Bitmap bmp = null;
        if (mode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE ) {
             bmp = ThumbnailUtils.createVideoThumbnail(mediaObj.get_media_path(), MediaStore.Video.Thumbnails.MICRO_KIND);
        }
        else
        {
            bmp = BitmapFactory.decodeFile(mediaObj.get_media_path());

        }
        if(bmp!=null)
        { bmp = getResizedBitmap(bmp,150,150);
        mediaObj.set_bmp(PhotoHelper.getBitmapAsByteArray(bmp));}
        else
        mediaObj.set_bmp(new byte[0]);
        return mediaObj;

    }


    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {






        int width = bm.getWidth();

        int height = bm.getHeight();



        float scaleWidth = ((float) newWidth) / width;

        float scaleHeight = ((float) newHeight) / height;

// CREATE A MATRIX FOR THE MANIPULATION

        Matrix matrix = new Matrix();

// RESIZE THE BIT MAP

        matrix.postScale(scaleWidth, scaleHeight);

// RECREATE THE NEW BITMAP

        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

        return resizedBitmap;

    }



    public static Bitmap changeRes(Bitmap bm)
    {
        int width = bm.getWidth();
        int nHeight,nWidth;

        int height = bm.getHeight();

        if (width>height)
        {
            nHeight = 612;
            nWidth = 816;
        }
        else
        {
            nHeight = 800;
            nWidth = 600;
        }

        float scaleWidth = ((float) nWidth) / width;

        float scaleHeight = ((float) nHeight) / height;

// CREATE A MATRIX FOR THE MANIPULATION

        Matrix matrix = new Matrix();

// RESIZE THE BIT MAP

        matrix.postScale(scaleWidth, scaleHeight);

// RECREATE THE NEW BITMAP

        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

        return resizedBitmap;




    }






}

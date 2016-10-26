package utilityClasses;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.elune.sajid.myapplication.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import activity.Activity_main_2;
import activity.data_sync_activity;
import objects.DataBaseEnums;
import objects.media_obj;


public class uploadfile  {

    static String encodedString;
    static String fileName;
    static  ArrayList<media_obj> path ;//= "/sdcard/Pictures/sample.jpg";
    //ArrayList<media_obj> mediaObjs = new ArrayList<>();
    static String doctorId;
    static String pid;
    static Context context;
    static ProgressBar progressBar ;
    static ProgressDialog progressDialog;
    static long totalSize = 0;
    static TextView textViewCurrentFileNumber;
    static NotificationCompat.Builder mBuilder;
   static NotificationManager notifier;
//    static  Notification n;


    /**
     * API call for upload selected image from gallery to the server
     */
    public static void uploadImage(final Context context1, ArrayList<media_obj>  mediaObjs) {
        context = context1;
        //pid = patientId;

        //progressBar.setMax(100);
//        progressBar.setProgress(100);
//        progressBar.setProgress(200);
//        progressBar.setProgress(300);
        DatabaseHandler databaseHandler = new DatabaseHandler(context);
        doctorId = String.valueOf(databaseHandler.getCustomerId());
        //fileName = new File(filePath.get(0)).getName();
        path = mediaObjs;
        new UploadFileToServer().execute();
    }
    public static void uploadImage(final Context context1,  ArrayList<media_obj> mediaObjs,ProgressBar progressBarActivity,TextView filename) {
        context = context1;
        progressBar = progressBarActivity;
        textViewCurrentFileNumber =filename;
       // progressBar.setProgress(50);
        //pid = patientId;
        DatabaseHandler databaseHandler = new DatabaseHandler(context);
        doctorId = String.valueOf(databaseHandler.getCustomerId());
        //fileName = new File(filePath.get(0)).getName();
        path = mediaObjs;
        new UploadFileToServer().execute();
    }



    public static byte[] readBytes(Uri uri, Context context) throws IOException {
        // this dynamically extends to take the bytes you read
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        // this is storage overwritten on each iteration with bytes
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        // we need to know how may bytes were read to write them to the byteBuffer
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }

        // and then we can return your byte array.
        return byteBuffer.toByteArray();
    }
    public static byte[] convertFileToByteArray(File f)
    {
        byte[] byteArray = null;
        try
        {
            InputStream inputStream = new FileInputStream(f);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024*8];
            int bytesRead =0;

            while ((bytesRead = inputStream.read(b)) != -1)
            {
                bos.write(b, 0, bytesRead);
            }

            byteArray = bos.toByteArray();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return byteArray;
    }

    /**
     * Uploading the file to server
     * */
    private static class UploadFileToServer extends AsyncTask<Void, Integer, Void> {

        int fileNumber =0;
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
           // progressBar.setProgress(0);
            if(!String.valueOf(path.get(fileNumber).get_pid()).equals("0"))

            {
                Intent intent = new Intent(context, data_sync_activity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(
                        context,
                        0,
                        intent,
                        Intent.FLAG_ACTIVITY_NEW_TASK);

//
//                Intent myIntent = new Intent(context, Activity_main_2.class);
//                PendingIntent intent2 = PendingIntent.getBroadcast(context, 1,
//                        myIntent, PendingIntent.FLAG_UPDATE_CURRENT
//                                | PendingIntent.FLAG_ONE_SHOT);
             mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(android.R.drawable.stat_sys_upload)
                            .setContentTitle("DocBox")
                            .setWhen(System.currentTimeMillis())
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true)
                            .setContentText("Uploading " + (fileNumber + 1) + " of total "+ path.size() + " files")
                            .setProgress(100, 0, false);

//             n = new NotificationCompat.Builder(context)
//                    .setContentTitle("Uploading \"+(fileNumber+1)+ \" of total \"+path.size()+\" files" )
//                    .setProgress(0,0,true)
//                    .setContentText("")
//                    .setSmallIcon(android.R.drawable.stat_sys_upload)
//                    .build();

             notifier = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);
                //mBuilder.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;
            notifier.notify(1, mBuilder.build());

            textViewCurrentFileNumber.setText("Uploading "+(fileNumber+1)+ " of total "+path.size()+" files");
        // progressBar.setTitle("Uploading plz wait");
               // progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
               // progressBar.setIndeterminate(false);
               // progressBar.setProgressNumberFormat(null);
                //progressBar.setMessage((fileNumber+1)+" of "+path.length);
               // progressBar.setMax(100);
               // progressBar.setProgress(0);
               // progressBar.show();
        }
            super.onPreExecute();
        }
        public long getTotalSize(int fileNumber)
        {
            long totalSize = 0;

                File file = new File(path.get(fileNumber).get_media_path());

                long fileSizeInBytes = file.length();

            long fileSizeInKB = fileSizeInBytes / 1024;

            long fileSizeInMB = fileSizeInKB / 1024;

            totalSize = totalSize+fileSizeInKB;

            return totalSize;

        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
            // progressBar.setVisibility(View.VISIBLE);

            progressBar.setProgress(progress[0]);

            if(!String.valueOf(path.get(fileNumber).get_pid()).equals("0"))
            // updating progress bar value
            {


               // int progressValue = progressBar.getProgress();

                mBuilder.setProgress(100, progress[0],false);
                notifier.notify(1, mBuilder.build());
                if((progress[0] == 100)&&(fileNumber!=path.size()-1))
                {
                    fileNumber++;
                    textViewCurrentFileNumber.setText("Uploading file "+(fileNumber+1)+ " of total "+path.size()+" files");
                    progressBar.setProgress(0);
                   // progressBar.hide();
                    mBuilder.setContentText("Uploading "+(fileNumber+1)+ " of total "+ path.size() + " files");
                    mBuilder.setProgress(100, 0, false);
                    notifier.notify(1, mBuilder.build());
                    // progressBar = new ProgressBar(context);
                    //progressBar.setTitle("Uploading plz wait");
//                    progressBar.setMessage((fileNumber+1)+" of "+path.length);
//                    progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    //progressBar.setIndeterminate(false);
                   // progressBar.setProgressNumberFormat(null);
                   //progressBar.setMax(100);
                   // progressBar.setProgress(0);
                   // progressBar.show();

                }

            }
            // updating percentage value
            //txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }

        @Override
        protected Void doInBackground(Void... params) {

            for(int i =0;i<path.size();i++) {
               // progressBar.setMessage(path.get(i));
//                Upload u = new Upload();
//                String msg = u.uploadVideo(path.get(i),context);

                uploadFile(i);
                //publishProgress(i+1);
               // progressBar.setProgress(i+1);


            }

            return null;
        }

        @SuppressWarnings("deprecation")
        private String uploadFile(final int i) {
            String responseString = null;
            //if(!pid .equals("profilepictures"))
           // progressBar.setMessage(path.get(i));
            HttpClient httpclient = new DefaultHttpClient();

            HttpPost httppost = new HttpPost(Config.FILE_UPLOAD_URL);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                            int percentageUpload = (int) ((num / (float) totalSize) * 100);

                            if((percentageUpload%20 == 0)&&(percentageUpload!=0))
                            { if (progressBar!=null)
                                if(percentageUpload!=progressBar.getProgress())
                                if(!String.valueOf(path.get(i).get_pid()).equals("0"))
                                publishProgress(percentageUpload);}
                            }
                        });

                File sourceFile = new File(path.get(i).get_media_path());

                // Adding file data to http body
                entity.addPart("image", new FileBody(sourceFile));

                // Extra parameters if you want to pass to server
                entity.addPart("website",
                        new StringBody("www.androidhive.info"));
                entity.addPart("email", new StringBody("abc@gmail.com"));
                entity.addPart("patientId",
                        new StringBody( String.valueOf(path.get(i).get_pid())));
                entity.addPart("DoctorId", new StringBody(doctorId));

                totalSize = entity.getContentLength();
                httppost.setEntity( entity);


                // Making server call
                HttpResponse response = (HttpResponse) httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                    DatabaseHandler databaseHandler = new DatabaseHandler(context);
                  int retVal =  databaseHandler.updateMedia(DataBaseEnums.KEY_SYNC_STATUS, "1", path.get(i).get_media_path());
                   int retValFollowUp = databaseHandler.updateMediaFollowUp(DataBaseEnums.KEY_SYNC_STATUS, "1", path.get(i).get_media_path());
                    Log.e("followup upload",path.get(i).get_media_path()+"is uploaded with ret value"+retValFollowUp);
                    databaseHandler.updateDocument(DataBaseEnums.KEY_SYNC_STATUS, "1", path.get(i).get_media_path());


                    //progressBar.inc(i+1);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (Exception e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(Void result) {
            //Log.e(TAG, "Response from server: " + result);

            // showing the server response in an alert dialog
            //showAlert(result);
            if(!String.valueOf(path.get(fileNumber).get_pid()).equals("0")) {
                progressBar.setProgress(0);

                Intent intent = new Intent(context,Activity_main_2.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(
                        context,
                        0,
                        intent,
                        Intent.FLAG_ACTIVITY_NEW_TASK);
//                Intent newIntent = new Intent(context, data_sync_activity.class);
//
//                Intent myIntent = new Intent(context, Activity_main_2.class);
//                PendingIntent intent2 = PendingIntent.getBroadcast(context, 1,
//                        myIntent, PendingIntent.FLAG_UPDATE_CURRENT
//                                | PendingIntent.FLAG_ONE_SHOT);
                ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(1);
                mBuilder =
                        new NotificationCompat.Builder(context)
                                .setSmallIcon(R.drawable.icon_notification)
                                .setContentTitle("DocBox")
                                .setContentIntent(pendingIntent)
                                .setAutoCancel(true)
                                .setContentText("Files Saved To Cloud");

                notifier.notify(1, mBuilder.build());

                ((Activity) context).recreate();
                //((NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(1);
            }
            super.onPostExecute(result);
        }

    }

}

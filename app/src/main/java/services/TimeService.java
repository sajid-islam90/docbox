
package  services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.elune.sajid.myapplication.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.json.simple.JSONValue;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import activity.Activity_main_2;
import utilityClasses.DatabaseHandler;

public class TimeService extends Service {
    // constant
    public static final long NOTIFY_INTERVAL = 10800 * 1000; // 120 seconds
    static NotificationCompat.Builder mBuilder;
    static NotificationManager notifier;

    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    // timer handling
    private Timer mTimer = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        // cancel if already existed
        if (mTimer != null) {
            mTimer.cancel();
        } else {
            // recreate new
            mTimer = new Timer();
        }
        // schedule task
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);
       // mTimer.
    }

    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            // run on another thread

            CheckAppVersion checkAppVersion = new CheckAppVersion();
            checkAppVersion.execute((Void) null);
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    // display toast
//                    Toast.makeText(getApplicationContext(), getDateTime(),
//                            Toast.LENGTH_SHORT).show();
                }

            });
        }

        private String getDateTime() {
            // get date time in custom format
            SimpleDateFormat sdf = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]");
            return sdf.format(new Date());
        }

    }
    public class CheckAppVersion extends AsyncTask<Void, Void, Boolean> {



boolean update = false;
        @Override
        protected void onPreExecute() {
            mBuilder =
                    new NotificationCompat.Builder(getApplicationContext())
                            .setSmallIcon(android.R.drawable.stat_sys_download)
                            .setContentTitle("DocBox")


                            .setContentText("Patient data is being downloaded from cloud");

            // notifier.notify(1, mBuilder.build());
            super.onPreExecute();

        }


        @Override
        protected Boolean doInBackground(Void... params) {

            String s1 = null;
            DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
            final String  address =  "docbox.co.in/sajid";
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String appVersion = prefs.getString("AppVersion","");
            String lastUpdatedNews = prefs.getString("lastUpdatedNews","2016-10-29 18:50:25");
            String hash = prefs.getString("Hash", "");
            RequestParams param = new RequestParams();

            final AsyncHttpClient client = new SyncHttpClient(true, 80, 443);
            ArrayList<String> CstmrId = new ArrayList<>();
            int customerId = databaseHandler.getCustomerId();
            CstmrId.add(String.valueOf(lastUpdatedNews));
            CstmrId.add(String.valueOf(hash));
            StringWriter out = new StringWriter();

            try {
                JSONValue.writeJSONString(CstmrId, out);
                s1 = out.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            param.put("date", s1);
            hitApiForAppointment("http://" + address + "/checkNewsUpdateRequired.php",param,client,getApplicationContext());

            Log.i("Service","api hit");
            Log.i("Service time",lastUpdatedNews);


            return null;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
//            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Activity_main_2.this);
//            SharedPreferences.Editor editor = prefs.edit();
            if(update){
                Intent myIntent = new Intent(getApplicationContext(), Activity_main_2.class);
                myIntent.putExtra("fragmentNumber",3);
                Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.icon4);
                PendingIntent pendingIntent = PendingIntent.getActivity(
                        getApplicationContext(),
                        0,
                        myIntent,
                        Intent.FLAG_ACTIVITY_NEW_TASK);
            mBuilder =
                    new NotificationCompat.Builder(getApplicationContext())
                            .setSmallIcon(R.drawable.icon_notification)
                            .setLargeIcon(bitmap)
                            .setContentTitle("DocBox")
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true)
                            .setContentText(" News Update");
                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
                notificationManager.notify(1, mBuilder.build());
//            editor.putBoolean("restore", false);
//            editor.commit();
                stopSelf();
                //getApplicationContext().stopService(new Intent(getApplicationContext(), TimeService.class));
            //updateApp();
                 }
            else
            {
//                mBuilder =
//                        new NotificationCompat.Builder(getApplicationContext())
//                                .setSmallIcon(R.drawable.icon_notification)
//                                .setContentTitle("DocBox")
//
//                                .setContentText(" Up to date App version");
//
//                notifier.notify(1, mBuilder.build());
                //Toast.makeText(getApplicationContext(),"up to date",Toast.LENGTH_LONG).show();
            }

            //pdia.dismiss();

        }

        //i: patient id
        //s: file path
        public void checkVersion(int customerId, String appVersion) {


        }
        public void hitApiForAppointment(String apiAddress, RequestParams params, AsyncHttpClient client,
                                         final Context context) {


            final DatabaseHandler databaseHandler = new DatabaseHandler(context);

            try
            {

                client.post(apiAddress, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i,cz.msebera.android.httpclient. Header[] headers, byte[] bytes) {

                        try {
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor editor = prefs.edit();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                           String date =  sdf.format(new Date());
                            String str = new String(bytes, "UTF-8");
                            if(str.equalsIgnoreCase("true"))
                            {
                                update= true;
                                editor.putBoolean("newsNotification",true);

                            }
                            else
                            {
                                update = false;
                                editor.putBoolean("newsNotification",false);
                            }

                            editor.putString("lastUpdatedNews",date);
                            editor.apply();
//                            JSONArray response;
//                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                            SharedPreferences.Editor editor = prefs.edit();
//                            // JSONObject mainObject = new JSONObject(str);
//                            ArrayList<String> appointmentPID;
//
//                            //response = (JSONArray) JSONValue.parse(str);
//                            if(str.contains("invalid version")) {
//                                Log.e("versionCheck", "Please Update App");
//                                editor.putBoolean("appVersionValid",false);
//                               // updateApp();
//                                update = true;
//                            }
//                            else {
//                                Log.e("versionCheck", String.valueOf(str));
//                                editor.putBoolean("appVersionValid",true);
//                            }
//                            editor.apply();

//System.out.print(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int i,cz.msebera.android.httpclient. Header[] headers, byte[] bytes, Throwable throwable) {
                        try {
                            return;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onFinish() {


                        // pdia.dismiss();



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

        public void updateApp() {
            final android.app.AlertDialog.Builder alert1 = new android.app.AlertDialog.Builder(getApplicationContext());
            try {
                alert1.setTitle("App version out of date");
                alert1.setMessage("Please Update the DocBox app to continue usage");
                alert1.setPositiveButton("Take me to PlayStore", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent viewIntent =
                                new Intent("android.intent.action.VIEW",
                                        Uri.parse("https://play.google.com/store/apps/details?id=com.elune.sajid.myapplication&hl=en"));
                        startActivity(viewIntent);
                        dialog.dismiss();
                    }

                });
               // alert1.show();

//                getApplicationContext().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                });

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }

        public void doSomething() {

        }


    }
}
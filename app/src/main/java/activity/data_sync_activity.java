package activity;


import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.elune.sajid.myapplication.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import objects.DataBaseEnums;
import objects.document_obj;
import objects.media_obj;
import objects.personal_obj;
import utilityClasses.DatabaseHandler;
import utilityClasses.uploadfile;
import utilityClasses.utility;

import static com.elune.sajid.myapplication.R.layout.activity_data_sync_activity;

public class data_sync_activity extends AppCompatActivity {
    DatabaseHandler controller = new DatabaseHandler(this);
    //Progress Dialog Object
    ProgressDialog prgDialog;
    static ProgressBar progressBar;
    ArrayList<HashMap<String, String>> userList;
    static NotificationCompat.Builder mBuilder;
    static NotificationManager notifier;
   static TextView textViewFileNumber;
   static ArrayList<media_obj>  mediaObjsFollowUp = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_data_sync_activity);
        userList  =  controller.getAllSyncUsers();
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        notifier = (NotificationManager)
                data_sync_activity.this.getSystemService(Context.NOTIFICATION_SERVICE);
        final RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.progressBarLayout);
        ArrayList<HashMap<String, String>> userList =  controller.getAllSyncUsers();
        Button button = (Button)findViewById(R.id.buttonSaveToCloud);
        button.setVisibility(View.VISIBLE);
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.saveSuccessfullLinearLayout);
        TextView textView = (TextView)findViewById(R.id.saveSuccessfullTextView);
        textView.setText("Press button to sync your data");
        //
        if(userList.size()!=0){
            //Set the User Array list in ListView
            ListAdapter adapter = new SimpleAdapter( data_sync_activity.this,userList, R.layout.view_user_entry, new String[] { "userId","userName"}, new int[] {R.id.userId, R.id.customerId});
            ListView myList=(ListView)findViewById(R.id.synclist);
            textView.setText("Press button to sync your data");
            myList.setAdapter(adapter);
            button.setVisibility(View.VISIBLE);


        }
        else
        {
           // relativeLayout.setVisibility(View.GONE);

           // linearLayout.setVisibility(View.VISIBLE);
           // button.setVisibility(View.GONE);



        }
        //Initialize Progress Dialog properties
        progressBar  =(ProgressBar) findViewById(R.id.progressBar);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Synctask().execute();
                //syncSQLiteMySQLDB();


//                mediaObjsFollowUp = controller.getMediaFollowUpTobeUploaded();
//                ArrayList<media_obj>  mediaObjsMedia = new ArrayList<>();
//                mediaObjsMedia = controller.getMediaTobeUploaded();
//                ArrayList<media_obj>  mediaObjsDocuments = new ArrayList<>();
//                mediaObjsDocuments = controller.getDocumentsTobeUploaded();
//                mediaObjsFollowUp.addAll(mediaObjsMedia);
//                mediaObjsFollowUp.addAll(mediaObjsDocuments);
//                //progressBar.setProgress(50);
//                if(mediaObjsFollowUp.size()>0)
//                {relativeLayout.setVisibility(View.VISIBLE);
//                uploadfile.uploadImage(data_sync_activity.this, mediaObjsFollowUp, progressBar, textViewFileNumber);}

            }
        });

        textViewFileNumber =(TextView)findViewById(R.id.textViewCurrentFileNumber);

        progressBar.setMax(100);
       // progressBar.setProgress(0);

        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Synching SQLite Data with Remote MySQL DB. Please wait...");
        prgDialog.setCancelable(false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_data_sync_activity, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //When Sync action button is clicked
//        if (id == R.id.refresh) {
//            //Sync SQLite DB data to remote MySQL DB
//            syncSQLiteMySQLDB();
//
//            ArrayList<media_obj>  mediaObjsFollowUp = new ArrayList<>();
//            mediaObjsFollowUp = controller.getMediaFollowUpTobeUploaded();
//            ArrayList<media_obj>  mediaObjsMedia = new ArrayList<>();
//            mediaObjsMedia = controller.getMediaTobeUploaded();
//            mediaObjsFollowUp.addAll(mediaObjsMedia);
//            //progressBar.setProgress(50);
//            uploadfile.uploadImage(data_sync_activity.this, mediaObjsFollowUp,progressBar,textViewFileNumber);
//
//            return true;
//        }
        if (id == android.R.id.home) {
            //Sync SQLite DB data to remote MySQL DB
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //Add User method getting called on clicking (+) button
  /*  public void addUser(View view) {
        Intent objIntent = new Intent(getApplicationContext(), NewUser.class);
        startActivity(objIntent);
    }*/

    public void syncSQLiteMySQLDB(){
        //Create AsycHttpClient object
        AsyncHttpClient client = new SyncHttpClient(true, 80, 443);
        RequestParams params = new RequestParams();
        ArrayList<HashMap<String, String>> userList =  controller.getAllSyncUsers();
       // if(userList.size()!=0)

        for (int i = 0;i<userList.size();i++)
        {
            if(controller.dbSyncCount() != 0){
               // prgDialog.show();
               // String Json = controller.composeJSONfromSQLite();
                String jSon = controller.composeJSONfromSQLitePatient(userList.get(i).get("userId"),data_sync_activity.this);
               // String kson = controller.composeJSONfromSQLiteDocuments(userList.get(i).get("userId"));
                //String JsonPatient = controller.composeJSONfromSQLitePatient();
                personal_obj personalObj = controller.getPersonalInfo();
                String hexString = personalObj.get_customerId()+personalObj.get_email();
                String hexCode = this.convertStringToHex(hexString);
               int code =  hexString.hashCode();
              //  Integer integer = Integer.decode(hexString);
               // String a= integer.toString();
                        //"http://"+R.string.action_server_ip_address+"/insertuser.php";
                params.put("usersJSON", jSon);
                String address = getResources().getString(R.string.action_server_ip_address);
                //Toast.makeText(getApplicationContext(),    "MySQL DB has been informed about Sync activity", Toast.LENGTH_LONG).show();
                utility.syncData("http://" + address + "/insertPatient.php", params, data_sync_activity.this, prgDialog, userList.get(i).get("userId"), mediaObjsFollowUp.size());



            }
            else{
               // Toast.makeText(getApplicationContext(), "SQLite and Remote MySQL DBs are in Sync!", Toast.LENGTH_LONG).show();
            }
        }
        if(userList.size()==0)
        {
            String jSon = controller.composeJSONfromSQLiteDummyPatient(data_sync_activity.this);
            params.put("usersJSON", jSon);
            String address = getResources().getString(R.string.action_server_ip_address);
            //Toast.makeText(getApplicationContext(),    "MySQL DB has been informed about Sync activity", Toast.LENGTH_LONG).show();
            utility.syncData("http://" + address + "/insertPatient.php", params, data_sync_activity.this, prgDialog, String.valueOf(0), mediaObjsFollowUp.size());

        }


        //else{
           // Toast.makeText(getApplicationContext(), "No data in SQLite DB, please do enter User name to perform Sync action", Toast.LENGTH_LONG).show();
       // }
    }
    public String convertStringToHex(String str){

        char[] chars = str.toCharArray();

        StringBuffer hex = new StringBuffer();
        for(int i = 0; i < chars.length; i++){
            hex.append(Integer.toHexString((int)chars[i]));
        }

        return hex.toString();
    }

    public class Synctask extends AsyncTask<Void, Void, Boolean> {


        private  ProgressDialog progressDialog;

        public Synctask() {

            progressDialog = new ProgressDialog(data_sync_activity.this);
        }

        @Override
        protected void onPreExecute() {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {

                        progressDialog.setMessage("Connecting please wait");
                        mBuilder =
                                new NotificationCompat.Builder(data_sync_activity.this)
                                        .setSmallIcon(android.R.drawable.stat_sys_upload)
                                        .setContentTitle("DocBox")


                                        .setContentText("Patient Data Being saved to cloud");

                        notifier.notify(1, mBuilder.build());

                        // progressDialog.setCanceledOnTouchOutside(false);
  // progressDialog.show();

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
                syncSQLiteMySQLDB();

            }
            catch (Exception e)
            {
e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
           // progressDialog.dismiss();
            ((NotificationManager) data_sync_activity.this.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(1);
            mBuilder =
                    new NotificationCompat.Builder(data_sync_activity.this)
                            .setSmallIcon(R.drawable.icon_notification)
                            .setContentTitle("DocBox")

                            .setContentText(userList.size()+" Patients' data saved to cloud");

            notifier.notify(1, mBuilder.build());
            TextView textView = (TextView)findViewById(R.id.saveSuccessfullTextView);
            LinearLayout linearLayout = (LinearLayout)findViewById(R.id.saveSuccessfullLinearLayout);
            final RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.progressBarLayout);
            Button button = (Button)findViewById(R.id.buttonSaveToCloud);
            linearLayout.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.GONE);

            // linearLayout.setVisibility(View.VISIBLE);
           // button.setVisibility(View.GONE);

            mediaObjsFollowUp = controller.getMediaFollowUpTobeUploaded();
            ArrayList<media_obj>  mediaObjsMedia = new ArrayList<>();
            mediaObjsMedia = controller.getMediaTobeUploaded();
            ArrayList<media_obj>  mediaObjsDocuments = new ArrayList<>();
            mediaObjsDocuments = controller.getDocumentsTobeUploaded();
            mediaObjsFollowUp.addAll(mediaObjsMedia);
            mediaObjsFollowUp.addAll(mediaObjsDocuments);
            if ((textView != null)&&(mediaObjsFollowUp.size()>0)) {
               // textView.setText("Your Data Is Safe On Our Cloud Please Track The File Upload Progress In The Status Bar Above");
            }
            else
            {
               // textView.setText("Your Data Is Safe On Our Cloud");
            }
            data_sync_activity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new RestoreWebData().execute();
                }
            });

            //progressBar.setProgress(50);
            if(mediaObjsFollowUp.size()>0)
            {relativeLayout.setVisibility(View.VISIBLE);
                uploadfile.uploadImage(data_sync_activity.this, mediaObjsFollowUp, progressBar, textViewFileNumber);}

        }




    }


    public class RestoreWebData extends AsyncTask<Void, Void, Boolean> {




        @Override
        protected void onPreExecute() {
            mBuilder =
                    new NotificationCompat.Builder(data_sync_activity.this)
                            .setSmallIcon(android.R.drawable.stat_sys_download)
                            .setContentTitle("DocBox")


                            .setContentText("Patient data is being downloaded from cloud");

            notifier.notify(1, mBuilder.build());
            super.onPreExecute();

        }


        @Override
        protected Boolean doInBackground(Void... params) {
            DatabaseHandler databaseHandler = new DatabaseHandler(data_sync_activity.this);

            List<document_obj> listDocument = databaseHandler.getAllDocumentsForDownload(5);
            List<document_obj> listMediaFollowUp = databaseHandler.getAllMediaFollowUpForSyncStatus(5);
            List<document_obj> listMedia = databaseHandler.getAllMediaForSyncsStatus(5);
            listDocument.addAll(listMediaFollowUp);
//            listMedia.addAll(listDocument);
            listMedia.addAll(listMediaFollowUp);
//            for (int i = 0; i < listDocument.size(); i++) {
//                downloadFile(listDocument.get(i).get_id(), listDocument.get(i).get_doc_path());
//            }

            for (int i = 0; i < listMedia.size(); i++) {
                File storageDir =
                        new File(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES), "Patient Manager/"+listMedia.get(i).get_id()+"/Notes");
                if(!storageDir.exists())
                    storageDir.mkdir();
                try {
                    if(i%40 == 0)
                        Thread.sleep(30000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.e("download web items", storageDir.getPath()+"/"+listMedia.get(i).get_doc_name());
                databaseHandler.updateAllMediaFollowUp();
                databaseHandler.updateMediaFollowUp(DataBaseEnums.KEY_SYNC_STATUS,"1",listMedia.get(i).get_doc_name());
                if(new File(listMedia.get(i).get_doc_path()).exists())
                { downloadFile(listMedia.get(i).get_id(), listMedia.get(i).get_doc_path());
                    databaseHandler.updateMedia(DataBaseEnums.KEY_SYNC_STATUS,"1",listMedia.get(i).get_doc_path());
                    databaseHandler.updateDocument(DataBaseEnums.KEY_SYNC_STATUS,"1",listMedia.get(i).get_doc_path());
                databaseHandler.updateMediaFollowUp(DataBaseEnums.KEY_SYNC_STATUS,"1",listMedia.get(i).get_doc_path());}
                else
                {

                    downloadFile(listMedia.get(i).get_id(),storageDir.getPath()+"/"+listMedia.get(i).get_doc_name());
                    databaseHandler.updateMedia(DataBaseEnums.KEY_SYNC_STATUS,"1",listMedia.get(i).get_doc_name());
                    databaseHandler.updateDocument(DataBaseEnums.KEY_SYNC_STATUS,"1",listMedia.get(i).get_doc_path());
                    databaseHandler.updateMediaFollowUp(DataBaseEnums.KEY_SYNC_STATUS,"1",listMedia.get(i).get_doc_path());}
            }


            for (int i = 0; i < listDocument.size(); i++) {
                File storageDir =
                        new File(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES), "Patient Manager/"+listDocument.get(i).get_id()+"/Documents");
                if(!storageDir.exists())
                    storageDir.mkdir();
                try {
                    if(i%40 == 0)
                        Thread.sleep(30000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(new File(listDocument.get(i).get_doc_path()).exists())
                { downloadFile(listDocument.get(i).get_id(), listDocument.get(i).get_doc_path());
                    databaseHandler.updateDocument(DataBaseEnums.KEY_SYNC_STATUS,"1",listDocument.get(i).get_doc_path());
                }
                else
                { downloadFile(listDocument.get(i).get_id(),storageDir.getPath()+"/"+listDocument.get(i).get_doc_name());
                    databaseHandler.updateDocument(DataBaseEnums.KEY_SYNC_STATUS,"1",listDocument.get(i).get_doc_name());
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
//            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Activity_main_2.this);
//            SharedPreferences.Editor editor = prefs.edit();
            mBuilder =
                    new NotificationCompat.Builder(data_sync_activity.this)
                            .setSmallIcon(R.drawable.icon_notification)
                            .setContentTitle("DocBox")

                            .setContentText(" Patients' data saved to Phone");

            notifier.notify(1, mBuilder.build());
//            editor.putBoolean("restore", false);
//            editor.commit();
//            pdia.dismiss();

        }

        //i: patient id
        //s: file path
        public void downloadFile(int i, String s) {
            int totalSize = 0;
            int downloadedSize = 0;
            DatabaseHandler databaseHandler = new DatabaseHandler(data_sync_activity.this);
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

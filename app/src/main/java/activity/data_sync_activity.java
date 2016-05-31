package activity;


import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.nsd.NsdServiceInfo;
import android.os.AsyncTask;
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
import android.widget.Toast;

import com.elune.sajid.myapplication.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import java.util.ArrayList;
import java.util.HashMap;

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
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.saveSuccessfullLinearLayout);
        TextView textView = (TextView)findViewById(R.id.saveSuccessfullTextView);
        //
        if(userList.size()!=0){
            //Set the User Array list in ListView
            ListAdapter adapter = new SimpleAdapter( data_sync_activity.this,userList, R.layout.view_user_entry, new String[] { "userId","userName"}, new int[] {R.id.userId, R.id.customerId});
            ListView myList=(ListView)findViewById(R.id.synclist);
            textView.setText("Press button to save your data on our Cloud to keep it safe");
            myList.setAdapter(adapter);
            button.setVisibility(View.VISIBLE);


        }
        else
        {
            relativeLayout.setVisibility(View.GONE);

           // linearLayout.setVisibility(View.VISIBLE);
            button.setVisibility(View.GONE);



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
            textView.setText("Your Data Is Safe On Our Cloud Please Track The File Upload Progress In The Status Bar Above");

            // linearLayout.setVisibility(View.VISIBLE);
            button.setVisibility(View.GONE);

            mediaObjsFollowUp = controller.getMediaFollowUpTobeUploaded();
            ArrayList<media_obj>  mediaObjsMedia = new ArrayList<>();
            mediaObjsMedia = controller.getMediaTobeUploaded();
            ArrayList<media_obj>  mediaObjsDocuments = new ArrayList<>();
            mediaObjsDocuments = controller.getDocumentsTobeUploaded();
            mediaObjsFollowUp.addAll(mediaObjsMedia);
            mediaObjsFollowUp.addAll(mediaObjsDocuments);
            //progressBar.setProgress(50);
            if(mediaObjsFollowUp.size()>0)
            {relativeLayout.setVisibility(View.VISIBLE);
                uploadfile.uploadImage(data_sync_activity.this, mediaObjsFollowUp, progressBar, textViewFileNumber);}

        }



    }

}

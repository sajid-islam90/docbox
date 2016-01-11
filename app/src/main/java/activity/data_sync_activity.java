package activity;


import android.app.ProgressDialog;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.sajid.myapplication.*;
import com.example.sajid.myapplication.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.sajid.myapplication.R.layout.activity_data_sync_activity;

public class data_sync_activity extends AppCompatActivity {
    DatabaseHandler controller = new DatabaseHandler(this);
    //Progress Dialog Object
    ProgressDialog prgDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_data_sync_activity);
        //Get User records from SQLite DB
       /* SQLiteDatabase myDataBase= openOrCreateDatabase("db", MODE_PRIVATE, null);
        DBController dbController = new DBController(getApplicationContext());
        dbController.onCreate(myDataBase);*/
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        ArrayList<HashMap<String, String>> userList =  controller.getAllSyncUsers();

        //
        if(userList.size()!=0){
            //Set the User Array list in ListView
            ListAdapter adapter = new SimpleAdapter( data_sync_activity.this,userList, R.layout.view_user_entry, new String[] { "userId","userName"}, new int[] {R.id.userId, R.id.customerId});
            ListView myList=(ListView)findViewById(R.id.synclist);

            myList.setAdapter(adapter);
            //Display Sync status of SQLite DB
            //Toast.makeText(getApplicationContext(), controller.getSyncStatus(), Toast.LENGTH_LONG).show();
        }
        else
        {
            RelativeLayout myList;
            myList = (RelativeLayout)findViewById(R.id.sync_data_layout);
            myList.setBackgroundResource(R.drawable.check);
        }
        //Initialize Progress Dialog properties
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
        if (id == R.id.refresh) {
            //Sync SQLite DB data to remote MySQL DB
            syncSQLiteMySQLDB();
            return true;
        }
       else if (id == android.R.id.home) {
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
        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
        RequestParams params = new RequestParams();
        ArrayList<HashMap<String, String>> userList =  controller.getAllSyncUsers();
       // if(userList.size()!=0)

        for (int i = 0;i<userList.size();i++)
        {
            if(controller.dbSyncCount() != 0){
               // prgDialog.show();
               // String Json = controller.composeJSONfromSQLite();
                String jSon = controller.composeJSONfromSQLitePatient(userList.get(i).get("userId"));
               // String kson = controller.composeJSONfromSQLiteDocuments(userList.get(i).get("userId"));
                //String JsonPatient = controller.composeJSONfromSQLitePatient();

                        //"http://"+R.string.action_server_ip_address+"/insertuser.php";
                params.put("usersJSON", jSon);
                String address = getResources().getString(R.string.action_server_ip_address);
                //Toast.makeText(getApplicationContext(),    "MySQL DB has been informed about Sync activity", Toast.LENGTH_LONG).show();
                utility.syncData("http://"+ address +"/insertPatient.php", params, data_sync_activity.this, prgDialog, userList.get(i).get("userId"));
              // utility.syncData("http://docbox.co.in/sajid/insertPatient.php", params, data_sync_activity.this,prgDialog,userList.get(i).get("userId"));
               // prgDialog.hide();

               // String JsonUpdatePatient = controller.composeJSONforUpdatePatient();


              //  params.put("usersJSON", JsonUpdatePatient);
               // utility.syncData("http://docbox.co.in/sajid/updatePatient.php",params,getApplicationContext());
               /* client.post("http://docbox.co.in/sajid/updatePatient.php", params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i, Header[] headers, byte[] bytes) {

                        try {
                            String str = new String(bytes, "UTF-8");
                            Toast.makeText(getApplicationContext(), "MySQL DB has been informed about Sync activity", Toast.LENGTH_LONG).show();
                            prgDialog.hide();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        //controller.updateSyncStatus(obj.get("id").toString(),obj.get("status").toString());

                    }

                    @Override
                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                        try {
                            String str = new String(bytes, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(getApplicationContext(), "MySQL DB has not been informed about Sync activity", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onFinish() {
                        Toast.makeText(getApplicationContext(), "END", Toast.LENGTH_LONG).show();
                    }



                });*/




            }
            else{
                Toast.makeText(getApplicationContext(), "SQLite and Remote MySQL DBs are in Sync!", Toast.LENGTH_LONG).show();
            }
        }

        //else{
           // Toast.makeText(getApplicationContext(), "No data in SQLite DB, please do enter User name to perform Sync action", Toast.LENGTH_LONG).show();
       // }
    }
}

package activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.elune.sajid.myapplication.R;

import utilityClasses.DatabaseHandler;

public class Title_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_title_, menu);
        getSupportActionBar().hide();

       /* ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.GET_ACCOUNTS},
                1);*/
        doWork();

        return true;
    }
    @TargetApi(Build.VERSION_CODES.M)
    public void doWork()
    {


//
//        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS)!= PackageManager.PERMISSION_GRANTED)
//         ||(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
//                ||(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED))
//
//
//        {
//           if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                    1);
//           if (ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS)!= PackageManager.PERMISSION_GRANTED)
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.GET_ACCOUNTS},
//                    1);
//
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED)
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.READ_PHONE_STATE},
//                    1);
//           if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.CAMERA},
//                    1);
//        }
//
//        else
//        {
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    final Intent mainIntent = new Intent(getApplicationContext(), LoginActivity.class);
//                   startActivity(mainIntent);
//                    finish();
//                }
//            }, 100);
//
//
//
//        }
        String[] perms = {"android.permission.RECORD_AUDIO", "android.permission.CAMERA","android.permission.GET_ACCOUNTS","android.permission.WRITE_EXTERNAL_STORAGE",
                "android.permission.READ_PHONE_STATE","android.permission.FINE_LOCATION","android.permission.SEND_SMS","android.permission.ACCESS_COARSE_LOCATION"};

        int permsRequestCode = 200;
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS)!= PackageManager.PERMISSION_GRANTED)
         ||(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
               ||(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED))

        requestPermissions(perms, permsRequestCode);
        else
        {
            DatabaseHandler databaseHandler = new DatabaseHandler(Title_Activity.this);

            if(databaseHandler.getPersonalInfo().get_email()==null)
                startActivity(new Intent(this, LoginActivity.class));
            else
                startActivity(new Intent(this, Activity_main_2.class));
            finish();
        }


    }
    @Override

    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults){

        switch(permsRequestCode){

            case 200:

                boolean audioAccepted = grantResults[0]==PackageManager.PERMISSION_GRANTED;

                boolean cameraAccepted = grantResults[1]==PackageManager.PERMISSION_GRANTED;

                boolean accountsAccepted = grantResults[1]==PackageManager.PERMISSION_GRANTED;

                boolean storageAccepted = grantResults[1]==PackageManager.PERMISSION_GRANTED;

                boolean phoneStateAccepted = grantResults[1]==PackageManager.PERMISSION_GRANTED;

                boolean LocationAccepted = grantResults[1]==PackageManager.PERMISSION_GRANTED;

                if(storageAccepted && LocationAccepted)
                {
                    DatabaseHandler databaseHandler = new DatabaseHandler(Title_Activity.this);

                   if(databaseHandler.getPersonalInfo().get_email()==null)
                    startActivity(new Intent(this, LoginActivity.class));
                    else
                       startActivity(new Intent(this, Activity_main_2.class));
                    finish();
                }
                else
                {
                    Toast.makeText(Title_Activity.this,"Please Grant Permissions Before Continuing",Toast.LENGTH_SHORT).show();
                }

                break;

        }


    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case 1: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    if ((ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS)!= PackageManager.PERMISSION_GRANTED)
//                            ||(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
//                            ||(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED))
//                        utility.recreateActivityCompat(Title_Activity.this);
//                    // permission was granted, yay! Do the
//                    // contacts-related task you need to do.
//else
//                    startActivity(new Intent(this, LoginActivity.class));
//
//
//                } else {
//
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//                }
//                return;
//            }
//
//            // other 'case' lines to check for other
//            // permissions this app might request
//        }
//    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

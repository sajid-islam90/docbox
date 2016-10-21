package activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.elune.sajid.myapplication.R;

import me.wangyuwei.particleview.ParticleView;
import utilityClasses.DatabaseHandler;
import utilityClasses.utility;

public class Title_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_);
        final ParticleView  mParticleView = (ParticleView)findViewById(R.id.pv_1) ;
//        mParticleView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mParticleView.startAnim();
//            }
//        },200);
        doWork();
        mParticleView.setOnParticleAnimListener(new ParticleView.ParticleAnimListener() {
            @Override
            public void onAnimationEnd() {
                //mParticleView.clearAnimation();
               //utility.recreateActivityCompat(Title_Activity.this);
                doWork();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_title_, menu);
        getSupportActionBar().hide();

       /* ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.GET_ACCOUNTS},
                1);*/




        return true;
    }
    @TargetApi(Build.VERSION_CODES.M)
    public void permissions()
    {

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.M){
            // Do something for lollipop and above versions
            if ((ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS)!= PackageManager.PERMISSION_GRANTED)
                    ||(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
                    ||(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED)
                    ||(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED)
                    ||(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
                    ||(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED))
            {
                new AlertDialog.Builder(Title_Activity.this)
                    .setTitle("Android permissions required")
                    .setMessage("For android marshmallow and above following permissions are required by DocBox to perform various functions." +
                            "\n1. Account Permission : For securing your credentials." +
                            "\n2. Storage Permission : For storing reports locally." +
                            "\n3. Phone state Permission : For contacting patients." +
                            "\n4. Record audio and camera Permission : For organizing all the reports data." +
                            "\n5.Location Permission : For patients to locate you on the world map.")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            doWork();

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            utility.recreateActivityCompat(Title_Activity.this);
                            return;
                            // do nothingret
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();}
            else
            {
                doWork();
            }
        } else{
            doWork();
            // do something for phones running an SDK before lollipop
        }

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
                "android.permission.READ_PHONE_STATE","android.permission.ACCESS_FINE_LOCATION"};
//        String[] perms = { "android.permission.GET_ACCOUNTS","android.permission.WRITE_EXTERNAL_STORAGE",
//                "android.permission.READ_PHONE_STATE","android.permission.ACCESS_FINE_LOCATION"};

        int permsRequestCode = 200;
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS)!= PackageManager.PERMISSION_GRANTED)
         ||(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
               ||(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED)
                ||(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED)
                ||(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
                ||(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED))

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
    @TargetApi(Build.VERSION_CODES.M)
    @Override

    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults){

        switch(permsRequestCode){

            case 200:

                boolean audioAccepted = grantResults[0]==PackageManager.PERMISSION_GRANTED;

                boolean cameraAccepted = grantResults[1]==PackageManager.PERMISSION_GRANTED;

                boolean accountsAccepted = grantResults[2]==PackageManager.PERMISSION_GRANTED;

                boolean storageAccepted = grantResults[3]==PackageManager.PERMISSION_GRANTED;

                boolean phoneStateAccepted = grantResults[4]==PackageManager.PERMISSION_GRANTED;

                boolean LocationAccepted = grantResults[5]==PackageManager.PERMISSION_GRANTED;


                if(storageAccepted && LocationAccepted&&accountsAccepted&&phoneStateAccepted && cameraAccepted && audioAccepted)
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
                    final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Title_Activity.this);
                    builder.setTitle("Permissions Missing");
                    builder.setMessage("Please grant all the permissions requested\nThese permissions are necessary for your android version to run all the services smoothly");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            utility.recreateActivityCompat(Title_Activity.this);
                        }
                    });
                    builder.show();
                    //Toast.makeText(Title_Activity.this,"Please Grant Permissions Before Continuing",Toast.LENGTH_SHORT).show();

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

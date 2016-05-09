package com.example.sajid.myapplication;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.apache.http.Header;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

import activity.Activity_main_2;
import objects.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity implements LoaderCallbacks<Cursor> , AdapterView.OnItemSelectedListener {

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static String[] CREDENTIALS = new String[]{""};
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;
    android.app.AlertDialog alertDialog;
    android.app.AlertDialog.Builder alertDialogBuilder;
    private personal_obj personalObj;
    private int newUserFlag = 1;
    private ProgressDialog pdia;
    // UI references.
    private CheckedTextView mEmailView;
    private EditText mPasswordView;
    private EditText mName;
    private EditText mAttachedDoctorEmailView;
    private Spinner spinner;
    private View mProgressView;
    private View mLoginFormView;
    String address ;
    LinearLayout linearLayoutAttachedDoctorEmail;
    LinearLayout linearLayoutSpeciality;
    CheckBox checkBox;
    String customerId;
    JSONArray response;
    ProgressDialog prgDialog;
   public static TestRestoreData testRestoreData;
   static ArrayList<Map<String,String>> arrayListIdPhotoPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS)!= PackageManager.PERMISSION_GRANTED))
//        {
//
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.GET_ACCOUNTS},
//                    1);
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.READ_PHONE_STATE},
//                    1);
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.CAMERA},
//                    1);
//
//        }
        SQLiteDatabase myDataBase= openOrCreateDatabase("patientManager",MODE_PRIVATE,null);
        DatabaseHandler dbHandle = new DatabaseHandler(getApplicationContext());
        dbHandle.onCreate(myDataBase);
address =  getResources().getString(R.string.action_server_ip_address);
        setContentView(R.layout.activity_login);
//        new Thread(new Runnable() {
//            public void run() {
//                downloadFile(i, arrayListIdPhotoPath.get(i).get(i));
//            }
//        }).start();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        final DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
        String test = databaseHandler.getPersonalInfo().get_speciality();
        personalObj = databaseHandler.getPersonalInfo();
        mPasswordView = (EditText) findViewById(R.id.password);
        mAttachedDoctorEmailView = (EditText) findViewById(R.id.editTextAttachedDoctorEmail);
        mName = (EditText) findViewById(R.id.name);
        checkBox = (CheckBox)findViewById(R.id.checkBoxHelper);
        linearLayoutAttachedDoctorEmail = (LinearLayout)findViewById(R.id.linearLayoutAttachedDoctorEmail);
        linearLayoutSpeciality = (LinearLayout)findViewById(R.id.linearLayoutSpeciality);
        final TextView textView = (TextView)findViewById((R.id.textView17));

        ImageView imageView =(ImageView)findViewById(R.id.pulse);
        imageView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.pulse));
        String a = mPasswordView.getText().toString();
        final CheckedTextView checkedTextView = (CheckedTextView)findViewById(R.id.checkedTextView);
         spinner = (Spinner) findViewById(R.id.spinner);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                @Override
                                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
if(isChecked)
{
//    spinner.setVisibility(View.GONE);
//    textView.setVisibility(View.GONE);
    linearLayoutSpeciality.setVisibility(View.GONE);
    linearLayoutAttachedDoctorEmail.setVisibility(View.VISIBLE);
}
                                                    else
{
//    spinner.setVisibility(View.VISIBLE);
//    textView.setVisibility(View.VISIBLE);
    linearLayoutSpeciality.setVisibility(View.VISIBLE);
    linearLayoutAttachedDoctorEmail.setVisibility(View.GONE);
}

                                                }
                                            });
                ArrayAdapter < CharSequence > adapter = ArrayAdapter.createFromResource(this,
                        R.array.specialities, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        if(databaseHandler.getPersonalInfo().get_speciality()!=null)
//        if(!personalObj.get_speciality().equals(""))
        spinner.setSelection(Integer.parseInt(databaseHandler.getPersonalInfo().get_speciality())-1);
        else
        spinner.setSelection(1);
        if((mPasswordView.getText().equals(null))||(mPasswordView.getText().equals("")))
            checkedTextView.setVisibility(View.GONE);
        else
            checkedTextView.setVisibility(View.VISIBLE);

        mPasswordView.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if((mPasswordView.getText().toString().equals(null))||(mPasswordView.getText().toString().equals(""))
                ||(mPasswordView.getText().toString().length()<4))
        checkedTextView.setVisibility(View.GONE);
        else

            checkedTextView.setVisibility(View.VISIBLE);

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if((mPasswordView.getText().toString().equals(null))||(mPasswordView.getText().toString().equals(""))
                ||(mPasswordView.getText().toString().length()<4))
            checkedTextView.setVisibility(View.GONE);
        else

            checkedTextView.setVisibility(View.VISIBLE);

    }

    @Override
    public void afterTextChanged(Editable s) {
        if((mPasswordView.getText().toString().equals(null))||(mPasswordView.getText().toString().equals(""))
                ||(mPasswordView.getText().toString().length()<4))
            checkedTextView.setVisibility(View.GONE);
        else
            checkedTextView.setVisibility(View.VISIBLE);

    }
});
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailView = (CheckedTextView) findViewById(R.id.emailSignIn);
        if (personalObj.get_email()!= null)
        {


          //Intent intent = new Intent(getApplicationContext(),MainActivity.class);
           // startActivity(intent);


           CREDENTIALS[0] = personalObj.get_email()+":"+personalObj.get_password();
            String Gmail = UserEmailFetcher.getEmail(this);
            newUserFlag = 0;

            // Set up the login form.
       // ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
         //       android.R.layout.simple_dropdown_item_1line, CREDENTIALS);
       // mEmailView = (AutoCompleteTextView) findViewById(R.id.emailSignIn);
        mEmailView.setText(personalObj.get_email());
mName.setText(personalObj.get_name());

       // mEmailView.setThreshold(2);
      //  mEmailView.setAdapter(adapter);}
        //populateAutoComplete();


        mPasswordView = (EditText) findViewById(R.id.password);

            mPasswordView.setText(personalObj.get_password());


        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {

                    attemptLogin(false);
                    return true;
                }
                return false;
            }
        });

       // Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {


                mAuthTask = new UserLoginTask(mEmailView.getText().toString(), mPasswordView.getText().toString());
                mAuthTask.execute((Void) null);
               /* new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("Restore")
                        .setMessage("Sure want to restore data??")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                // continue with delete
///ArrayList<String> a = databaseHandler.getPatientsForDate("26-Oct-2015");
                                allDataRestore(Integer.toString(personalObj.get_customerId()));
                                DatabaseHandler databaseHandler = new DatabaseHandler(LoginActivity.this);
System.out.print(response);
                               // databaseHandler.addPersonalInfo(personalObj);
                                mAuthTask = new UserLoginTask(mEmailView.getText().toString(), mPasswordView.getText().toString());
                                mAuthTask.execute((Void) null);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing





                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();*/

               // attemptLogin(false);
            }
        });//commented to enable direct login

    }
    else
        {
           // mEmailView = (AutoCompleteTextView) findViewById(R.id.emailSignIn);

//control goes in when user registers for the first time

           String Gmail = UserEmailFetcher.getEmail(this);
            mEmailView.setText(Gmail);
            mPasswordView = (EditText) findViewById(R.id.password);
            mPasswordView.setFocusableInTouchMode(true);
            mPasswordView.requestFocus();
           // Button mEmailSignInButton  = (Button) findViewById(R.id.email_sign_in_button);
            mEmailSignInButton.setText(getString(R.string.action_register));
            mEmailSignInButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    attemptLogin(true);
                }
            });



        }

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position)
        {
            case 0:
                personalObj.set_speciality("Orthopedic Surgery");
                break;
            case 1:
                personalObj.set_speciality("Ophthalmics");
                break;
            case 2:
                personalObj.set_speciality("Dentist");
                break;
            case 3:
                personalObj.set_speciality("Cardiologist");
                break;
            case 4:
                personalObj.set_speciality("General Practice");
                break;
            case 5:
                personalObj.set_speciality("Gynecology");
                break;
            case 6:
                personalObj.set_speciality("Neurology");
                break;
            case 7:
                personalObj.set_speciality("Pediatrics");
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        personalObj.set_speciality("Orthopedic Surgery");

    }



    private void populateAutoComplete() {
        getLoaderManager().initLoader(0, null, this);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    //flag = true : first time

    public void attemptLogin(boolean flag) {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mName.setError(null);

        // Store values at the time of the login attempt.
        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();
        final String name = mName.getText().toString();
        final String attachedDoctorEmail = mAttachedDoctorEmailView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        if (TextUtils.isEmpty(name)) {
            mName.setError(getString(R.string.error_field_required));
            focusView = mName;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }
        if(checkBox.isChecked())
        if (TextUtils.isEmpty(attachedDoctorEmail)) {
            mAttachedDoctorEmailView.setError(getString(R.string.error_field_required));
            focusView = mAttachedDoctorEmailView;
            cancel = true;
        } else if (!isEmailValid(attachedDoctorEmail)) {
            mAttachedDoctorEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mAttachedDoctorEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
if(!flag) { mAuthTask = new UserLoginTask(email, password);
    mAuthTask.execute((Void) null);
return;}
    Thread t = new Thread(new Runnable() {
        @Override
        public void run() {
            if(!checkBox.isChecked())
            checkExistingAccount(email);
          generateOTP(password);

        }
    });


    t.start();
    try {
        t.join();
    } catch (InterruptedException e) {
        e.printStackTrace();
    }



            LayoutInflater li = LayoutInflater.from(LoginActivity.this);
            final View promptsView = li.inflate(R.layout.otp_enter_dialogue_box, null);

             alertDialogBuilder = new android.app.AlertDialog.Builder(
                    LoginActivity.this);

            Button submit = (Button)promptsView.findViewById(R.id.button5);
            Button reEnterOTP = (Button)promptsView.findViewById(R.id.button3);
            submit.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPasswordView.setError(null);
                    // get user input and set it to result
//                                    pdia = new ProgressDialog(LoginActivity.this);
//                                    pdia.setMessage("Restoring Data Please Wait");
//                                    pdia.show();
                    EditText textView = (EditText) promptsView.findViewById(R.id.verifyOTPEditText);
                    final String otpEnter = textView.getText().toString();
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                    final SharedPreferences.Editor editor = prefs.edit();

                    ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
                    ArrayList<String>otp = new ArrayList<String>();
                    otp.add(otpEnter);
                    ArrayList<String>phone = new ArrayList<String>();
                    phone.add(password);
                    data.add(otp);
                    data.add(phone);
                    AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
                    RequestParams params = new RequestParams();
                    String s1 = null;

                    StringWriter out = new StringWriter();
                    try {
                        JSONValue.writeJSONString(data, out);
                        s1 = out.toString();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String Json = s1;
                    params.put("otp", Json);
                    client.post("http://" + address + "/authOTP.php", params, new AsyncHttpResponseHandler() {
                        // client.post("http://docbox.co.in/sajid/register.php", params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {

                            try {
                                String str = new String(bytes, "UTF-8");
                                JSONParser parser = new JSONParser();
                                JSONObject object = (JSONObject) parser.parse(str);
                                final String status = (String) object.get("Message");
                                if (status.equals("success")) {
                               // if (true) {
                                    editor.putBoolean("otpVerified", true);
                                    editor.commit();


                                    int customerid = 0;
                                    if ((customerId != null) && (!customerId.equals("0"))) {
                                        customerid = Integer.parseInt(customerId);
                                        CREDENTIALS[0] = mEmailView.getText().toString() + ":" + mPasswordView.getText().toString();
                                        personalObj.set_email(mEmailView.getText().toString());
                                        personalObj.set_password(mPasswordView.getText().toString());
                                        personalObj.set_speciality(String.valueOf(spinner.getSelectedItemPosition()));
                                        personalObj.set_name(mName.getText().toString());
                                        personalObj.set_customerId(customerid);
                                    }

                                    if (customerid == 0) {
                                        mAuthTask = new UserLoginTask(email, password);
                                        mAuthTask.execute((Void) null);
                                    } else {
                                        new AlertDialog.Builder(LoginActivity.this)
                                                .setTitle(email + "Already Exists")
                                                .setMessage("Sure want to create new account??")
                                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        mAuthTask = new UserLoginTask(email, password);
                                                        CREDENTIALS = new String[]{""};
                                                        mAuthTask.execute((Void) null);
                                                        // continue with delete
                                                    }
                                                })
                                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // do nothing
                                                        allDataRestore(customerId);
                                                        DatabaseHandler databaseHandler = new DatabaseHandler(LoginActivity.this);
                                                        databaseHandler.addPersonalInfo(personalObj);

                                                        // CREDENTIALS[0] = personalObj.get_email()+":"+personalObj.get_password();
                                                        mAuthTask = new UserLoginTask(email, password);
                                                        mAuthTask.execute((Void) null);

                                                    }
                                                })
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .show();
                                    }
                                }
                                else
                                {
                                    mPasswordView.setError("Invalid OTP");
                                    mPasswordView.requestFocus();
                                    editor.putBoolean("otpVerified", false);
                                    editor.commit();
                                }

//
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            //controller.updateSyncStatus(obj.get("id").toString(),obj.get("status").toString());

                        }

                        @Override
                        public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {
                            try {
                                String str = new String(bytes, "UTF-8");

                            } catch (Exception e) {
                                e.printStackTrace();
                                mPasswordView.setError("Something went wrong please check internet connection");
                                mPasswordView.requestFocus();
                            }
                            // Toast.makeText(getApplicationContext(),    "MySQL DB has not been informed about Sync activity", Toast.LENGTH_LONG).show();

                        }

                        @Override
                        public void onFinish() {


                        }


                    });

                }
            });
            reEnterOTP.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                generateOTP(password);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    thread.start();

                }
            });

            alertDialogBuilder.setView(promptsView);
            alertDialogBuilder
                    .setCancelable(false)

//                    .setPositiveButton("Submit",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//
//                                    //registerNewAccount(otpEnter);
//
//                                }
//                            })
//            .setNegativeButton("Re-Send OTP", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//
//
//                            Thread thread = new Thread() {
//                                @Override
//                                public void run() {
//                                    try {
//                                        generateOTP(password);
//
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            };
//
//                            thread.start();
//
//
//
//
//                        }
//                    })
            ;

             alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();



//            int customerid = 0;
//            if((customerId!= null)&&(!customerId.equals("0"))) {
//                customerid = Integer.parseInt(customerId);
//                CREDENTIALS[0] = mEmailView.getText().toString()+":"+mPasswordView.getText().toString();
//                personalObj.set_email(mEmailView.getText().toString());
//                personalObj.set_password(mPasswordView.getText().toString());
//                personalObj.set_speciality(String.valueOf(spinner.getSelectedItemPosition()));
//                personalObj.set_name(" ");
//                personalObj.set_customerId(customerid);
//            }
//
//            if(customerid == 0) {
//                mAuthTask = new UserLoginTask(email, password);
//                mAuthTask.execute((Void) null);
//            }
//            else
//            {
//                new AlertDialog.Builder(LoginActivity.this)
//                        .setTitle(email+"Already Exists")
//                        .setMessage("Sure want to create new account??")
//                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                mAuthTask = new UserLoginTask(email, password);
//                                CREDENTIALS = new String[]{""};
//                                mAuthTask.execute((Void) null);
//                                // continue with delete
//                            }
//                        })
//                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                // do nothing
//                                 allDataRestore(customerId);
//                                DatabaseHandler databaseHandler = new DatabaseHandler(LoginActivity.this);
//                                databaseHandler.addPersonalInfo(personalObj);
//
//                               // CREDENTIALS[0] = personalObj.get_email()+":"+personalObj.get_password();
//                                mAuthTask = new UserLoginTask(email, password);
//                                mAuthTask.execute((Void) null);
//
//                            }
//                        })
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .show();
//            }
        }
    }
    //i: patient id
    //s: file path
   public void downloadFile(int i, String s){
        int totalSize = 0;
        int downloadedSize = 0;DatabaseHandler databaseHandler = new DatabaseHandler(LoginActivity.this);
        int CustomerId = databaseHandler.getPersonalInfo().get_customerId();
        File file1 = new File(s);

        String dwnload_file_path = "http://docbox.co.in/sajid/"+customerId+"//"+String.valueOf(i)+"//"+file1.getName();
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
            File file = new File(file1,file1.getName());

            FileOutputStream fileOutput = new FileOutputStream(file);

            //Stream used for reading the data from the internet
            InputStream inputStream = urlConnection.getInputStream();

            //this is the total size of the file which we are downloading
            totalSize = urlConnection.getContentLength();


            //create a buffer...
            byte[] buffer = new byte[1024];
            int bufferLength = 0;

            while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                // update the progressbar //

            }
            //close the output stream when complete //
            fileOutput.close();


        }
        catch (final Exception e) {
           e.printStackTrace();
        }
    }




    public void allDataRestore(String customerId)
    {
        String s1 = null;
        ArrayList<String>email = new ArrayList<>();
        email.add(customerId);
        RequestParams params = new RequestParams();
        StringWriter out = new StringWriter();
        try {
            JSONValue.writeJSONString(email, out);
            s1 = out.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("customerId", s1);

        final AsyncHttpClient client = new SyncHttpClient(true, 80, 443);

          testRestoreData = new TestRestoreData(params,client,"getAllPatients.php");
        testRestoreData.execute((Void) null);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("restore", true);
        editor.putString("customerId", customerId);
        editor.commit();

//                new Thread(new Runnable() {
//                    public void run() {
//                        while(true)
//                        if (testRestoreData.getStatus() == AsyncTask.Status.FINISHED) {
//                            if(arrayListIdPhotoPath!=null)
//                            for(int i = 0;i<arrayListIdPhotoPath.size();i++)
//                            downloadFile(i,arrayListIdPhotoPath.get(i).get(i));break;
//                        }
//                    }
//                }).start();








    }
    public void generateOTP(String mPassword)
    {
        if (utility.hasActiveInternetConnection(getApplicationContext()))
        {
            AsyncHttpClient client = new SyncHttpClient(true, 80, 443);
            RequestParams params = new RequestParams();
            String s1 = null;

            StringWriter out = new StringWriter();
            try {
                JSONValue.writeJSONString(mPassword, out);
                s1 = out.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String Json = s1;
            params.put("data", Json);
            client.post("http://" + address + "/OTPDocBox.php", params, new AsyncHttpResponseHandler() {
                // client.post("http://docbox.co.in/sajid/register.php", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {

                    try {
                        String str = new String(bytes, "UTF-8");

                        JSONParser parser = new JSONParser();
                        JSONObject object = (JSONObject) parser.parse(str);
                        final Boolean status = (Boolean) object.get("status");
//
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //controller.updateSyncStatus(obj.get("id").toString(),obj.get("status").toString());

                }

                @Override
                public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {
                    try {
                        String str = new String(bytes, "UTF-8");

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    // Toast.makeText(getApplicationContext(),    "MySQL DB has not been informed about Sync activity", Toast.LENGTH_LONG).show();

                }

                @Override
                public void onFinish() {



                }


            });

        }

    }





    public void checkExistingAccount(String mEmail)

    {
        String s1 = null;
        ArrayList<String>email = new ArrayList<>();
        email.add(mEmail);
        RequestParams params = new RequestParams();
        StringWriter out = new StringWriter();
        try {
            JSONValue.writeJSONString(email, out);
            s1 = out.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("email", s1);
        final AsyncHttpClient client = new SyncHttpClient(true, 80, 443);
          this.hitApi("http://" + address + "/checkExistingUser.php", params,client);
       // return customerId;
    }

    public  void hitApi(String apiAddress, RequestParams params,AsyncHttpClient client) {
       // client = new SyncHttpClient(true, 80, 443);

        final DatabaseHandler databaseHandler = new DatabaseHandler(LoginActivity.this);

        try
        {

            client.post(apiAddress, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {

                    try {
                        String str = new String(bytes, "UTF-8");
                        // JSONObject mainObject = new JSONObject(str);
                       // customerId =str;
//                       JSONObject mainObject = new JSONObject();


                        // int k = Integer.parseInt(str);
                        // str = Integer.toString(k);
                        JSONParser parser = new JSONParser();
                        JSONObject object =(JSONObject) parser.parse(str);
                        String hex = (String) object.get("HashCode");
                        customerId =  (String) object.get("CustomerId");

                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString(getString(R.string.hash_code), hex);
                        editor.commit();

                        // ((Activity) context).recreate();

                        //Toast.makeText(context, "MySQL DB has been informed about Sync activity", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //controller.updateSyncStatus(obj.get("id").toString(),obj.get("status").toString());

                }

                @Override
                public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {
                    try {
                       // String str = new String(bytes, "UTF-8");
                        //Toast.makeText(LoginActivity.this, "MySQL DB has not been informed about Sync activity", Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onFinish() {
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


    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        if((newUserFlag==0)&&(email.equals(personalObj.get_email())))
        {
            return true;
        }
        else if ((newUserFlag == 0)&&(!email.equals(personalObj.get_email())))
        {
            return false;
        }

        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<String>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

       // addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }



    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    public class TestRestoreData extends AsyncTask<Void, Void, Boolean> {

        private final AsyncHttpClient client;
        private final RequestParams Tparams;
        private final String ApiName;

        public TestRestoreData(RequestParams Tparams,AsyncHttpClient Mclient,String ApiName) {
            this.client = Mclient;
            this.Tparams = Tparams;
            this.ApiName = ApiName;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            this.hitApi("http://" + address + "/"+ApiName, Tparams,client);



            return null;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), Activity_main_2.class);
            startActivity(intent);
            finish();

        }


        public void doSomething()
        {

        }



        public  void hitApi(String apiAddress, RequestParams params,AsyncHttpClient client) {
            // client = new SyncHttpClient(true, 80, 443);

            final DatabaseHandler databaseHandler = new DatabaseHandler(LoginActivity.this);

            try
            {

                client.post(apiAddress, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i,cz.msebera.android.httpclient. Header[] headers, byte[] bytes) {

                        try {
                            String str = new String(bytes, "UTF-8");
                            // JSONObject mainObject = new JSONObject(str);

                            response = (JSONArray) JSONValue.parse(str);
                            JSONObject tableNameObject = (JSONObject) response.get(0);


                                utility.saveDataTable(response, LoginActivity.this);

                            //System.out.print("a");


                            // ((Activity) context).recreate();

                            //Toast.makeText(context, "MySQL DB has been informed about Sync activity", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        //controller.updateSyncStatus(obj.get("id").toString(),obj.get("status").toString());

                    }

                    @Override
                    public void onFailure(int i,cz.msebera.android.httpclient. Header[] headers, byte[] bytes, Throwable throwable) {
                        try {
                            // String str = new String(bytes, "UTF-8");
                            //Toast.makeText(LoginActivity.this, "MySQL DB has not been informed about Sync activity", Toast.LENGTH_LONG).show();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onFinish() {
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
    }





/*
    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }*/

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;


        UserLoginTask(String email, String password ) {
            this.mEmail = email;
            this.mPassword = password;

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
            // TODO: attempt authentication against a network service.
boolean a = false;
            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    //this.saveToMysql();
                     a = pieces[1].equals(mPassword);
//                    try {
//                        return true;
//                    }
//                    catch (Exception e)
//                    {
//                        e.printStackTrace();
//                    }

                }
            }

            if(CREDENTIALS[0].equals(""))
            {
                //  register the new account here.
                if (utility.hasActiveInternetConnection(getApplicationContext())) {



                    registerNewAccount();

                    personalObj = databaseHandler.getPersonalInfo();
                  return true;
                }

            }


            return a;
        }

public void checkOTP()
{
    if (utility.hasActiveInternetConnection(getApplicationContext()))
    {
        AsyncHttpClient client = new SyncHttpClient(true, 80, 443);
        RequestParams params = new RequestParams();
        String s1 = null;

        StringWriter out = new StringWriter();
        try {
            JSONValue.writeJSONString(mPassword, out);
            s1 = out.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String Json = s1;
        params.put("data", Json);
        client.post("http://" + address + "/OTPDocBox.php", params, new AsyncHttpResponseHandler() {
            // client.post("http://docbox.co.in/sajid/register.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {

                try {
                    String str = new String(bytes, "UTF-8");

                    JSONParser parser = new JSONParser();
                    JSONObject object = (JSONObject) parser.parse(str);
                    final Boolean status = (Boolean) object.get("status");
                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(status) {

                                LayoutInflater li = LayoutInflater.from(LoginActivity.this);
                                final View promptsView = li.inflate(R.layout.otp_enter_dialogue_box, null);

                                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(
                                        LoginActivity.this);

                                alertDialogBuilder.setView(promptsView);
                                alertDialogBuilder
                                        .setCancelable(false)
                                        .setPositiveButton("OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        // get user input and set it to result
                                                        EditText textView = (EditText)promptsView.findViewById(R.id.verifyOTPEditText);
                                                        String otpEnter = textView.getText().toString();


                                                    }
                                                });
                                android.app.AlertDialog alertDialog = alertDialogBuilder.create();

                                // show it
                                alertDialog.show();

                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Please enter a valid phone number", Toast.LENGTH_LONG).show();
                            }

                        }
                    });



                    //Toast.makeText(getApplicationContext(), "MySQL DB has been informed about Sync activity", Toast.LENGTH_LONG).show();
                    //prgDialog.hide();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //controller.updateSyncStatus(obj.get("id").toString(),obj.get("status").toString());

            }

            @Override
            public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {
                try {
                    String str = new String(bytes, "UTF-8");

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                // Toast.makeText(getApplicationContext(),    "MySQL DB has not been informed about Sync activity", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFinish() {



            }


        });

    }

}





        public void registerNewAccount()
        {

            try {
                String otp = "";
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                SharedPreferences.Editor editor = prefs.edit();
                otp = prefs.getString("otp", "0");

                final DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());

                if (ContextCompat.checkSelfPermission(LoginActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(LoginActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            1);
                }


                AsyncHttpClient client = new SyncHttpClient(true, 80, 443);
                personalObj = new personal_obj();
                personalObj.set_email(mEmail);
                personalObj.set_password(mPassword);
                personalObj.set_speciality(String.valueOf(spinner.getSelectedItemPosition() + 1));
                personalObj.set_name(mName.getText().toString());
                RequestParams params = new RequestParams();
                databaseHandler.addPersonalInfo(personalObj);

                if (!checkBox.isChecked())
                {  String Json = databaseHandler.composeJSONfromEmailPassword(LoginActivity.this);
                params.put("emailPasswordJSON", Json);

                // utility.syncData("http://docbox.co.in/sajid/register.php",params,getApplicationContext(),prgDialog,client);

                client.post("http://" + address + "/register.php", params, new AsyncHttpResponseHandler() {
                    // client.post("http://docbox.co.in/sajid/register.php", params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {

                        try {
                            String str = new String(bytes, "UTF-8");
                            // databaseHandler.addPersonalInfo(personalObj);
                            JSONObject mainObject = new JSONObject();


                            // int k = Integer.parseInt(str);
                            // str = Integer.toString(k);
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                            SharedPreferences.Editor editor = prefs.edit();
                            JSONParser parser = new JSONParser();
                            JSONObject object = (JSONObject) parser.parse(str);
                            String hex = (String) object.get("HashCode");
                            String id = (String) object.get("CustomerId");


                            databaseHandler.updatePersonalInfo("id", id);


                            editor.putString(getString(R.string.hash_code), hex);
                            editor.commit();


                            //Toast.makeText(getApplicationContext(), "MySQL DB has been informed about Sync activity", Toast.LENGTH_LONG).show();
                            //prgDialog.hide();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        //controller.updateSyncStatus(obj.get("id").toString(),obj.get("status").toString());

                    }

                    @Override
                    public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {
                        try {
                            String str = new String(bytes, "UTF-8");

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        // Toast.makeText(getApplicationContext(),    "MySQL DB has not been informed about Sync activity", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onFinish() {


                    }


                });
            }
                else
                {
                    String s1 = null;
                    ArrayList<String> personalInfo = new ArrayList<>();
                    personalInfo.add(mAttachedDoctorEmailView.getText().toString());
                    personalInfo.add(mEmail);
                    personalInfo.add(mPassword);
                    personalInfo.add(mName.getText().toString());
                    StringWriter out = new StringWriter();
                    try {
                        JSONValue.writeJSONString(personalInfo, out);
                        s1 = out.toString();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String Json = s1;
                    params.put("emailPasswordJSON", Json);

                    // utility.syncData("http://docbox.co.in/sajid/register.php",params,getApplicationContext(),prgDialog,client);

                    client.post("http://" + address + "/registerHelper.php", params, new AsyncHttpResponseHandler() {
                        // client.post("http://docbox.co.in/sajid/register.php", params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {

                            try {
                                String str = new String(bytes, "UTF-8");
                                JSONParser parser = new JSONParser();
                                JSONObject object = (JSONObject) parser.parse(str);
                                String message = (String) object.get("message");
                                if(message.equals("registered"))
                                {

                                }
                                else
                                {
                                    mAttachedDoctorEmailView.setError("Wrong Email id provided");
                                    mAttachedDoctorEmailView.requestFocus();


                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {
                            try {


                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }

                        @Override
                        public void onFinish() {


                        }


                    });
                }


    }

    catch (Exception e)
    {
        System.out.print(e);
    }

        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
if(mAttachedDoctorEmailView.getError()==null)
            if (success) {
                Intent intent = new Intent();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                boolean restoreFlag = prefs.getBoolean("restore", false);
                SharedPreferences.Editor editor = prefs.edit();

                if(checkBox.isChecked()) {
                    editor.putString(LoginActivity.this.getString(R.string.account_type), LoginActivity.this.getString(R.string.account_type_helper));
                    editor.putString("attachedDoctorEmail", mAttachedDoctorEmailView.getText().toString());

                }
                else

               editor.putString(LoginActivity.this.getString(R.string.account_type), LoginActivity.this.getString(R.string.account_type_doctor));
                editor.commit();
               // boolean otpSuccessAuthFlag = prefs.getBoolean("otpSuccessAuth", true);
                if((!restoreFlag)) {


                    intent.setClass(getApplicationContext(), Activity_main_2.class);
                    startActivity(intent);
                    finish();
                }
//                else
//                {
//                    Toast.makeText(LoginActivity.this,"Wrong OTP",Toast.LENGTH_LONG);
//                    mPasswordView.setError("Wrong OTP entered Try Again");
//                    mPasswordView.requestFocus();
//                }
            } else {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                SharedPreferences.Editor editor = prefs.edit();

                if(checkBox.isChecked())
                    editor.putString(LoginActivity.this.getString(R.string.account_type), LoginActivity.this.getString(R.string.account_type_helper));
                else

                    editor.putString(LoginActivity.this.getString(R.string.account_type), LoginActivity.this.getString(R.string.account_type_doctor));
                editor.commit();
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
            else
{
    DatabaseHandler databaseHandler = new DatabaseHandler(LoginActivity.this);
    databaseHandler.deletePersonalInfo();
    newUserFlag = 1;
utility.recreateActivityCompat(LoginActivity.this);
    alertDialog.dismiss();
}
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}


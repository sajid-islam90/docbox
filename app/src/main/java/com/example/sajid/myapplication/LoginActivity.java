package com.example.sajid.myapplication;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.apache.http.Header;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import objects.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import activity.MainActivity;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity implements LoaderCallbacks<Cursor>  {

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] CREDENTIALS = new String[]{""};
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;
    private personal_obj personalObj;
    private int newUserFlag = 1;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    ProgressDialog prgDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SQLiteDatabase myDataBase= openOrCreateDatabase("patientManager",MODE_PRIVATE,null);
        DatabaseHandler dbHandle = new DatabaseHandler(getApplicationContext());
        dbHandle.onCreate(myDataBase);

        setContentView(R.layout.activity_login);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
        personalObj = databaseHandler.getPersonalInfo();
        mPasswordView = (EditText) findViewById(R.id.password);
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.emailSignIn);
        if (personalObj.get_email()!= null)
        {
            CREDENTIALS[0] = personalObj.get_email()+":"+personalObj.get_password();
            String Gmail = UserEmailFetcher.getEmail(this);
            newUserFlag = 0;


        // Set up the login form.
       // ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
         //       android.R.layout.simple_dropdown_item_1line, CREDENTIALS);
       // mEmailView = (AutoCompleteTextView) findViewById(R.id.emailSignIn);
        mEmailView.setText(personalObj.get_email());


       // mEmailView.setThreshold(2);
      //  mEmailView.setAdapter(adapter);}
        //populateAutoComplete();


        mPasswordView = (EditText) findViewById(R.id.password);
            mPasswordView.setText(personalObj.get_password());
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

       // Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

    }
    else
        {
           // mEmailView = (AutoCompleteTextView) findViewById(R.id.emailSignIn);



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
                    attemptLogin();
                }
            });



        }

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void populateAutoComplete() {
        getLoaderManager().initLoader(0, null, this);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
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

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
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

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
            // TODO: attempt authentication against a network service.

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
                    return pieces[1].equals(mPassword);


                }
            }

            if(CREDENTIALS[0].equals(""))
            {
                //  register the new account here.
                if (utility.hasActiveInternetConnection(getApplicationContext())) {
                    registerNewAccount();
                    personalObj = databaseHandler.getPersonalInfo();
                    if(personalObj.get_customerId()!=0)
                    return true;
                    else
                    {
                        //Toast.makeText(getApplicationContext(), "No Internet Connection Found Please Connect to Register", Toast.LENGTH_LONG).show();
                        return false;
                    }
                }
                else
                {
                    //Toast.makeText(getApplicationContext(), "No Internet Connection Found Please Connect to Register", Toast.LENGTH_LONG).show();
                }
            }


            return false;
        }

        public void registerNewAccount()
        {
            if (utility.hasActiveInternetConnection(getApplicationContext()))
            {
            try {

                final DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());

                if (ContextCompat.checkSelfPermission(LoginActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(LoginActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            1);
                }


           /* prgDialog = new ProgressDialog(getApplicationContext());
            prgDialog.setMessage("Registering Please Wait");
            prgDialog.setCancelable(false);*/
                AsyncHttpClient client = new SyncHttpClient(true, 80, 443);
                personalObj = new personal_obj();
                personalObj.set_email(mEmail);
                personalObj.set_password(mPassword);
                RequestParams params = new RequestParams();
                databaseHandler.addPersonalInfo(personalObj);


                String Json = databaseHandler.composeJSONfromEmailPassword();
                params.put("emailPasswordJSON", Json);

               // utility.syncData("http://docbox.co.in/sajid/register.php",params,getApplicationContext(),prgDialog,client);
                String address = getResources().getString(R.string.action_server_ip_address);
               client.post("http://"+address+"/register.php", params, new AsyncHttpResponseHandler() {
               // client.post("http://docbox.co.in/sajid/register.php", params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i, Header[] headers, byte[] bytes) {

                        try {
                            String str = new String(bytes, "UTF-8");

                            int k = Integer.parseInt(str);
                            str = Integer.toString(k + 1);
                            JSONParser parser = new JSONParser();
                            databaseHandler.updatePersonalInfo("id", str);



                            System.out.print("true");
                            //Toast.makeText(getApplicationContext(), "MySQL DB has been informed about Sync activity", Toast.LENGTH_LONG).show();
                            //prgDialog.hide();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        //controller.updateSyncStatus(obj.get("id").toString(),obj.get("status").toString());

                    }

                    @Override
                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                        try {
                            String str = new String(bytes, "UTF-8");
                            System.out.print("jksdhfsdjkhfdsjk");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        // Toast.makeText(getApplicationContext(),    "MySQL DB has not been informed about Sync activity", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onFinish() {
                        Toast.makeText(getApplicationContext(), "END", Toast.LENGTH_LONG).show();
                    }


                });

            }

            catch (Exception e)
            {
                System.out.print(e);
            }
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}


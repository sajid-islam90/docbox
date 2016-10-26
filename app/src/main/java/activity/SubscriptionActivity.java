package activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.elune.sajid.myapplication.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import utilityClasses.DatabaseHandler;

public class SubscriptionActivity extends AppCompatActivity {
    long days;

    @Override
    public void onBackPressed() {
        if(days<=0) {
            android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(SubscriptionActivity.this);

            alert.setTitle("Subscription End");
            alert.setIcon(android.R.drawable.ic_dialog_alert);
            alert.setMessage("Your DocBox Subscription has ended\nplease renew subscription before continuing");
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.cancel();
                }
            });

            alert.show();
            return;
        }
        else
        {
            Intent intent = new Intent(SubscriptionActivity.this,Activity_main_2.class);

                intent.putExtra("fragmentNumber",1);
            startActivity(intent);
            finish();
        }
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SubscriptionActivity.this);
        String accountType = prefs.getString(SubscriptionActivity.this.getString(R.string.account_type),"");
        TextView subsStartDate = (TextView)findViewById(R.id.textViewSubsStartDate);

        Button button = (Button)findViewById(R.id.buttonRenewPGSubscription);
        TextView textView = (TextView)findViewById(R.id.textView62);
        TextView textView2 = (TextView)findViewById(R.id.textView61);

        Button button1 = (Button)findViewById(R.id.buttonRenewDoctorSubscription);
        TextView textView1 = (TextView)findViewById(R.id.textView63);
        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SubscriptionActivity.this,PGSubscriptionActivity.class);
                    startActivity(intent);

                }
            });
        }
        if (button1 != null) {
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SubscriptionActivity.this,PaymentActivity.class);
                    startActivity(intent);
                }
            });
        }


        TextView subsEndDate = (TextView)findViewById((R.id.textViewsSubsEndDate));
        TextView remainingDays = (TextView)findViewById((R.id.textViewRemainingDays));
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
       String formattedDate = df.format(c.getTime());
String validFrom = prefs.getString(getString(R.string.subscription_valid_from),"");
        if (validFrom.contains(" "))
        {
            validFrom = validFrom.substring(0,validFrom.indexOf(" "));
        }
        String validUpto= prefs.getString(getString(R.string.subscription_valid_upto),"");
        if (validUpto.contains(" "))
        {
            validUpto = validUpto.substring(0,validUpto.indexOf(" "));
        }
        if (subsStartDate != null) {
            subsStartDate.setText(validFrom);
        }
        if (subsEndDate != null) {
            subsEndDate.setText(validUpto);
        }
        try {
            Date date = df.parse(validUpto);
            Date today  = df.parse(formattedDate);
            long diff = date.getTime() - today.getTime();
            long seconds = diff / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            days = hours / 24;
            if(accountType.equals(SubscriptionActivity.this.getString(R.string.account_type_doctor)))
            {

                if(days<=100) {
                    if (textView2 != null) {
                        textView2.setVisibility(View.VISIBLE);
                    }
                    assert button1 != null;
                    button1.setVisibility(View.VISIBLE);
                    assert textView1 != null;
                    textView1.setVisibility(View.VISIBLE);


                }
                else
                {
                    assert textView2 != null;
                    textView2.setVisibility(View.GONE);
                    assert button1 != null;
                    button1.setVisibility(View.GONE);
                    assert textView1 != null;
                    textView1.setVisibility(View.GONE);
                }
                assert button != null;
                button.setVisibility(View.GONE);
                assert textView != null;
                textView.setVisibility(View.GONE);

            }
            if(accountType.equals(SubscriptionActivity.this.getString(R.string.account_type_student)))
            {
                if(days<=100) {
                    assert textView2 != null;
                    textView2.setVisibility(View.VISIBLE);
                    // change for checking payment gateway start
//                    assert button1 != null;
//                    button1.setVisibility(View.VISIBLE);
//                    assert textView1 != null;
//                    textView1.setVisibility(View.VISIBLE);
                    // change for checking payment gateway end

                    assert button != null;
                    button.setVisibility(View.VISIBLE);
                    assert textView != null;
                    textView.setVisibility(View.VISIBLE);
                }
                else
                {
                    assert textView2 != null;
                    textView2.setVisibility(View.GONE);
                    assert button != null;
                    button.setVisibility(View.GONE);
                    assert textView != null;
                    textView.setVisibility(View.GONE);
                }
                // change for checking payment gateway
//                assert button1 != null;
//                button1.setVisibility(View.GONE);
//                assert textView1 != null;
//                textView1.setVisibility(View.GONE);
            }
            if (remainingDays != null) {
                remainingDays.setText(String.valueOf(days));
                if(days>10)
                {
                    remainingDays.setTextColor(Color.GREEN);
                }
                else if(days<0)
                {
                    remainingDays.setText("");
                    TextView textView11 = (TextView)findViewById(R.id.textView66);
                    if (textView11 != null) {
                        textView11.setText("Subscription has ended");
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_subscription, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == android.R.id.home) {
            if(days<=0) {
                android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(SubscriptionActivity.this);

                alert.setTitle("Subscription End");
                alert.setIcon(android.R.drawable.ic_dialog_alert);
                alert.setMessage("Your DocBox Subscription has ended\nplease renew subscription before continuing");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });

                alert.show();

            }
            else
            {
                finish();
            }
            return true;
        }
        if (id == R.id.action_refresh) {
            ProgressDialog progressDialog = new ProgressDialog(SubscriptionActivity.this);
            progressDialog.setTitle("Getting subscription details");
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            DatabaseHandler databaseHandler = new DatabaseHandler(SubscriptionActivity.this);
            int DoctorId = databaseHandler.getCustomerId();
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SubscriptionActivity.this);
            String hashString = prefs.getString(SubscriptionActivity.this.getString(R.string.hash_code), "");


            String s2 ="";
            String s3 ="";
            StringWriter out1 = new StringWriter();
            StringWriter out2 = new StringWriter();
            try {
                JSONValue.writeJSONString(DoctorId, out1);
                JSONValue.writeJSONString(hashString, out2);
                s2 = out1.toString();
                s3 = out2.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            final RequestParams requestParams = new RequestParams();
            requestParams.add("DoctorId", s2);
            requestParams.add("token", s3);
            getSubscription(requestParams,progressDialog);
            finish();

        }

        return super.onOptionsItemSelected(item);
    }
    public void getSubscription(RequestParams params, final ProgressDialog progressDialog) {
        final AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SubscriptionActivity.this);
        final String  address =  getResources().getString(R.string.action_server_ip_address);
        String apiAddress = "http://" + address + "/fetchDoctorSubscriptionDetails.php";
        final DatabaseHandler databaseHandler = new DatabaseHandler(SubscriptionActivity.this);

        try
        {

            client.post(apiAddress, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {

                    try {
                        String str = new String(bytes, "UTF-8");
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SubscriptionActivity.this);
                        SharedPreferences.Editor editor = prefs.edit();
                        JSONParser parser = new JSONParser();
                        JSONObject object = (JSONObject) parser.parse(str);

                        String validUpto = (String) object.get("SubscriptionExpiryDate");
                        String validFrom = (String) object.get("SubscriptionStartDate");
                        if (validFrom.contains(" "))
                        {
                            validFrom = validFrom.substring(0,validFrom.indexOf(" "));
                        }
                        if (validUpto.contains(" "))
                        {
                            validUpto = validUpto.substring(0,validUpto.indexOf(" "));
                        }

                        editor.putString(getString(R.string.subscription_valid_upto), validUpto);
                        editor.putString(getString(R.string.subscription_valid_from), validFrom);
                        editor.commit();
                        progressDialog.dismiss();
                        Intent intent = new Intent(SubscriptionActivity.this, SubscriptionActivity.class);
                        startActivity(intent);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //controller.updateSyncStatus(obj.get("id").toString(),obj.get("status").toString());

                }

                @Override
                public void onFailure(int i,cz.msebera.android.httpclient. Header[] headers, byte[] bytes, Throwable throwable) {
                    try {
//                        String str = new String(bytes, "UTF-8");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SubscriptionActivity.this,"Error Please check internet connection and try again",Toast.LENGTH_LONG).show();
                            }
                        });
                        progressDialog.dismiss();
                        // Toast.makeText(context, "MySQL DB has not been informed about Sync activity", Toast.LENGTH_LONG).show();

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

    }
}

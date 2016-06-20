package activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.elune.sajid.myapplication.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
        TextView subsStartDate = (TextView)findViewById(R.id.textViewSubsStartDate);
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
            if (remainingDays != null) {
                remainingDays.setText(String.valueOf(days));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_account_verification, menu);
        return true;
    }
}

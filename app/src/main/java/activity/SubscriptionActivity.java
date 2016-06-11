package activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.TextView;

import com.elune.sajid.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SubscriptionActivity extends AppCompatActivity {

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
//        Calendar c = Calendar.getInstance();
//        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
//        String formattedDate = df.format(c.getTime());
        subsStartDate.setText(prefs.getString(getString(R.string.subscription_valid_from),""));
        subsEndDate.setText(prefs.getString(getString(R.string.subscription_valid_upto),""));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_account_verification, menu);
        return true;
    }
}

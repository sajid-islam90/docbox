package activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.elune.sajid.myapplication.R;

/**
 * Created by romichandra on 17/11/16.
 */
public class AssociationConferenceScheduleDetailsActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_associations_conference_schedule_details);

        Intent g = this.getIntent();

        String title = g.getStringExtra("ScheduleTitle");
        String time = g.getStringExtra("ScheduleTime");
        String details = g.getStringExtra("ScheduleDetails");

        getSupportActionBar().setTitle(time);
        getSupportActionBar().setSubtitle(title);

        TextView textDetails = (TextView)findViewById(R.id.textScheduleDetails);
        textDetails.setText(details);
    }
}

package activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.elune.sajid.myapplication.R;

/**
 * Created by romichandra on 16/11/16.
 */
public class AssociationConferenceSponsorsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_associations_conference_sponsors);
        getSupportActionBar().setTitle("Sponsors");
    }
}

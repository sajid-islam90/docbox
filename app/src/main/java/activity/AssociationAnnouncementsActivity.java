package activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.elune.sajid.myapplication.R;

import java.util.ArrayList;

import adapters.AnnouncenmentsAdapter;
import objects.modelAnnouncement;

/**
 * Created by romichandra on 13/11/16.
 */
public class AssociationAnnouncementsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_associations_announcements);
        getSupportActionBar().setTitle("Announcements");

        ListView listViewAnnouncements = (ListView)findViewById(R.id.listAssociationAnnouncements);
        ArrayList<modelAnnouncement> listAnnouncement = new ArrayList();

        for (int i = 0; i < 8; i++){
            modelAnnouncement model = new modelAnnouncement();
            model.setDate("22");
            model.setMonth("Dec");
            model.setTitle("Survey of Osteoarticular tuberculosis");
            if (i%2 == 0)
                model.setSubtitle("Please complete the survey on OSTEOARTICULAR TUBERCULOSIS");

            listAnnouncement.add(model);
        }

        listViewAnnouncements.setAdapter(new AnnouncenmentsAdapter(this, listAnnouncement));

    }
}

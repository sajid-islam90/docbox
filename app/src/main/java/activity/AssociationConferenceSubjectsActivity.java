package activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.elune.sajid.myapplication.R;

import java.util.ArrayList;

import adapters.AssociationConferenceSubjectsAdapter;
import objects.modelSubject;

/**
 * Created by romichandra on 15/11/16.
 */
public class AssociationConferenceSubjectsActivity extends AppCompatActivity {

    String[] Titles = new String [] {
        "Cataract",
        "Glaucoma",
        "Neuro ophthal, Oculoplasty",
        "Hospital Management (Very useful)"
    };

    String [] Subtitles = new String [] {
        "SICS with high quality comparable with Phaco &Femto surgery",
        "Diagnostics & treatment basic and advance\n(Lasik (Femto and standard))",
        "aesthetic and common day to day disorders\nCommunity ophthalmology, Strabismus",
        "How to manage our clinics and hospital"
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_associations_conference_subjects);
        getSupportActionBar().setTitle("Subjects");

        ListView listViewSubjects = (ListView)findViewById(R.id.listAssociationSubjects);
        ArrayList<modelSubject> listSubjects = new ArrayList();

        for (int i = 0; i < Titles.length; i++){
            modelSubject model = new modelSubject();
            model.setTitle(Titles[i]);
            model.setSubtitle(Subtitles[i]);


            listSubjects.add(model);
        }

        listViewSubjects.setAdapter(new AssociationConferenceSubjectsAdapter(this, listSubjects));
    }
}

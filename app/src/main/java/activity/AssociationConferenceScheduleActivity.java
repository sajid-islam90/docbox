package activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.elune.sajid.myapplication.R;

import adapters.AssociationConferenceScheduleAdapter;

/**
 * Created by romichandra on 15/11/16.
 */
public class AssociationConferenceScheduleActivity extends AppCompatActivity {

    String [] dates = new String [] {
            "18th Nov\n 2016",
            "18th Nov\n 2016",
            "18th Nov\n 2016",
            "18th Nov\n 2016",
            "18th Nov\n 2016",
            "18th Nov\n 2016",

    };

    String [] times = new String[] {
            "08 : 00 am\n to\n 05 : 00 pm",
            "09 : 00 am\n to\n 06 : 00 pm",
            "09 : 00 am\n to\n 06 : 00 pm",
            "09 : 00 am\n to\n 02 : 00 pm",
            "03 : 00 pm\n to\n 07 : 00 pm",
            "07 : 30 pm",

    };

    String [] titles = new String [] {
            "Registration",
            "Trade",
            "Hands on Training in SSTP (surgical skill training program) and WET lab",
            "Symposium and free papers",
            "Live Surgery (PHACO SICS Femto Cataract, Femto Lasik, Phakic IOL, Squint, Surgery on Lid Glued IOL C3R Pterygium Anterior/Posterioe Vitrectomy etc.)",
            "Dinner"
    };

    String [] locations = new String [] {
            "BRD Medical college, Gorakhpur",
            "BRD Medical college, Gorakhpur",
            "BRD Medical college, Gorakhpur",
            "BRD Medical college, Gorakhpur",
            "BRD Medical college, Gorakhpur",
            "BRD Medical college, Gorakhpur",
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_associations_conference_schedule);
//        getSupportActionBar().setTitle("Schedule");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Schedule");
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("18th Nov"));
        tabLayout.addTab(tabLayout.newTab().setText("19th Nov"));
        tabLayout.addTab(tabLayout.newTab().setText("20th Nov "));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new AssociationConferenceScheduleAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

//        ListView listViewEvents = (ListView)findViewById(R.id.listConferenceSchedule);
//        ArrayList<modelAssociationEvent> listEvents = new ArrayList();
//
//        for (int i = 0; i < titles.length; i++){
//            modelAssociationEvent event = new modelAssociationEvent();
//            event.setDate(dates[i]);
//            event.setTime(times[i]);
//            event.setTitle(titles[i]);
//            event.setLocation(locations[i]);
//            listEvents.add(event);
//        }
//
//        listViewEvents.setAdapter(new AssociationEventsAdapter(this, listEvents));

    }
}

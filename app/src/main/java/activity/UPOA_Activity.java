package activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.elune.sajid.myapplication.R;

import java.util.ArrayList;

import objects.modelAssociationEvent;
import utilityClasses.floatingactionbutton.FloatingActionButton;
import utilityClasses.floatingactionbutton.FloatingActionsMenu;

/**
 * Created by romichandra on 08/11/16.
 */

public class UPOA_Activity extends AppCompatActivity {

    ListView listViewEvents;
    ArrayList<modelAssociationEvent> listEvents = new ArrayList<>();
    FloatingActionsMenu menuFab;
    FloatingActionButton fab1, fab2, fab3, fab4;
    View overlay;

    ImageView img1, img2, img3, img4;

    LinearLayout layout1, layout2, layout3, layout4;

//    String [] dates = new String [] {
//        " 19 - 20\nNov '2016",
//        " 13 - 15\nJan '2017",
//        " 19 - 20\nNov '2016",
//        " 13 - 15\nJan '2017",
//    };
//
//    String [] times = new String[] {
//        "08 : 00 am",
//        "04 : 45 pm",
//        "08 : 00 am",
//        "04 : 45 pm",
//    };
//
//    String [] titles = new String [] {
//        "Basic Concepts in Deformity Correction",
//        "BOS Shoulder Course",
//        "Basic Concepts in Deformity Correction",
//        "BOS Shoulder Course",
//    };
//
//    String [] locations = new String [] {
//        "Dept. of Orthopaedic, Sir J. J. Hospital",
//        "KEM Hospital, Parel, Mumbai",
//        "Dept. of Orthopaedic, Sir J. J. Hospital",
//        "KEM Hospital, Parel, Mumbai"
//    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upoa);
        getSupportActionBar().hide();

        listViewEvents = (ListView)findViewById(R.id.listUpcomingAssociationEvents);
        menuFab = (FloatingActionsMenu)findViewById(R.id.fabMenuAssociations);
        overlay = (View)findViewById(R.id.overlayAssociations);

        img1 = (ImageView)findViewById(R.id.imgFAB1);
        img2 = (ImageView)findViewById(R.id.imgFAB2);
        img3 = (ImageView)findViewById(R.id.imgFAB3);
        img4 = (ImageView)findViewById(R.id.imgFAB4);

        fab1 = (FloatingActionButton)findViewById(R.id.menuMembership);
        fab2 = (FloatingActionButton)findViewById(R.id.menuNotifications);
        fab3 = (FloatingActionButton)findViewById(R.id.menuContactUs);
        fab4 = (FloatingActionButton)findViewById(R.id.menuChangeAssociation);

        layout1 = (LinearLayout) findViewById(R.id.layoutAssociationSubs1);
        layout2 = (LinearLayout)findViewById(R.id.layoutAssociationSubs2);
        layout3 = (LinearLayout)findViewById(R.id.layoutAssociationSubs3);
        layout4 = (LinearLayout)findViewById(R.id.layoutAssociationSubs4);

//        for (int i = 0; i < titles.length; i++){
//            modelAssociationEvent event = new modelAssociationEvent();
//            event.setDate(dates[i]);
//            event.setTime(times[i]);
//            event.setTitle(titles[i]);
//            event.setLocation(locations[i]);
//            listEvents.add(event);
//        }
//
//        Button btnLoadExtra = new Button(this);
//        btnLoadExtra.setText("View All");
//        btnLoadExtra.setBackgroundColor(Color.WHITE);
//        btnLoadExtra.setTextColor(Color.GRAY);
//        listViewEvents.setAdapter(new AssociationEventsAdapter(this, listEvents));
//        listViewEvents.addFooterView(btnLoadExtra);

        menuFab.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                overlay.setVisibility(View.VISIBLE);
            }

            @Override
            public void onMenuCollapsed() {
                overlay.setVisibility(View.GONE);
            }
        });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UPOA_Activity.this, AssociationMembershipActivity.class));
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UPOA_Activity.this, AssociationNotificationsActivity.class));
            }
        });

        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UPOA_Activity.this, AssociationContactUsActivity.class));
            }
        });

        fab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(UPOA_Activity.this, AssociationsActivity.class));
            }
        });

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UPOA_Activity.this, AssociationExecCouncilActivity.class));
            }
        });

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UPOA_Activity.this, AssociationEDirectoryActivity.class));
            }
        });

        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UPOA_Activity.this, AssociationAnnouncementsActivity.class));
            }
        });

        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UPOA_Activity.this, AssociationGalleryActivity.class));
            }
        });

        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UPOA_Activity.this, AssociationConferenceHighlightsActivity.class));
            }
        });

        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UPOA_Activity.this, AssociationConferenceSubjectsActivity.class));
            }
        });

        layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UPOA_Activity.this, AssociationConferenceScheduleActivity.class));
            }
        });

        layout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UPOA_Activity.this, AssociationConferenceSponsorsActivity.class));
            }
        });


    }

    @Override
    public void onBackPressed() {
        if (menuFab.isExpanded()){
            menuFab.collapse();
        }
        else{
            super.onBackPressed();
        }
    }
}

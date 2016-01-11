package activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import objects.DataBaseEnums;
import com.example.sajid.myapplication.DatabaseHandler;
import com.example.sajid.myapplication.R;
import com.example.sajid.myapplication.util.calendar_descriptor;
import com.example.sajid.myapplication.utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import adapters.adapter_on_calendar_date_patients;
import adapters.adapter_view_visit_calendar;

/**
 * Created by nevermore on 10/24/2015.
 */

public class activity_view_patient_visits extends AppCompatActivity {

    GridView gridview_calendar;
    GridView gridview_days;
    TextView current_month;
    //  int totaldays;
    Date startDateCalendar;
    Date endDateCalendar;
    static boolean SwitchState ;
   static adapter_view_visit_calendar adapter;
    ListView listViewPatients;
    RelativeLayout relativeLayout;
    int rightToLeftSwipe = 0;
    boolean isClicked = false;
    int pid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_activity_view_patient_visits);

        gridview_calendar = (GridView) findViewById(R.id.gridview_calendar);
        listViewPatients = (ListView) findViewById(R.id.lvPatientsCalendar);
        relativeLayout = (RelativeLayout) findViewById(R.id.view_inventory_relative_layout);

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setTitle("Patient Name");

        actionBar.setDisplayHomeAsUpEnabled(true);

        int totaldays = calendar_descriptor.getTotalDays();
        final String[] dates = calendar_descriptor.getDates(totaldays);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        if (calendar_descriptor.startDate == null) {
            calendar_descriptor.startDate = cal.getTime();
        }
        Switch aSwitch = (Switch)findViewById(R.id.switch2);
        aSwitch.setChecked(SwitchState);

        calendar_descriptor.startDate = cal.getTime();
        cal.setTime(calendar_descriptor.startDate);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        startDateCalendar = cal.getTime();
        String startDate = sdf.format(startDateCalendar);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        endDateCalendar = cal.getTime();
        String endDate = sdf.format(endDateCalendar);
        String inventory[] = calendar_descriptor.inventory;

        //   String inventory[] = {"1","0","1","0","1","0","1","1","0","1","2","2","3"};
        String days[] = {"Su", "M", "Tu", "W", "Th", "F", "Sa"};
        calendar_descriptor.inventory = new String[dates.length];
        calendar_descriptor.stopsellFlags = new boolean[dates.length];
        try {
            for (int i = 0; i < dates.length; i++) {
                calendar_descriptor.inventory[i] = "N/A";
                calendar_descriptor.stopsellFlags[i] = false;

            }
            adapter = new adapter_view_visit_calendar(this, dates,pid,SwitchState);
            gridview_calendar.setAdapter(adapter);

            aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    SwitchState = isChecked;
                    int totaldays = calendar_descriptor.getTotalDays();
                    String[] datesnew = calendar_descriptor.getDates(totaldays);

                    gridview_calendar.setAdapter(new adapter_view_visit_calendar(activity_view_patient_visits.this, datesnew,pid,SwitchState));                    Intent intent = getIntent();

                   /* finish();
                    startActivity(intent);*/
                }
            });
        }
        catch(Exception e){
            e.printStackTrace();
        }
        calendar_descriptor.inventory = null;
        gridview_days = (GridView) findViewById(R.id.gridview_days);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, days) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.GRAY);
                text.setTypeface(Typeface.DEFAULT_BOLD);
//                text.setWidth(getResources().getDisplayMetrics().widthPixels / 7);
                text.setTextSize(10);
                return view;

            }
        };
        gridview_days.setAdapter(adapter);

        Calendar date = Calendar.getInstance();
        date.setTime(calendar_descriptor.startDate);
        current_month = (TextView) findViewById(R.id.textview_current_month);

        String s = new SimpleDateFormat("MMMM").format(date.getTime());
        String year = new SimpleDateFormat("yyyy").format(date.getTime());
        current_month.setText(s + " " + year);

        Intent i = getIntent();
        pid = i.getIntExtra("id",0);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.view_inventory_relative_layout){
                    hideListView();
                }
            }
        });

        gridview_calendar.setAdapter(new adapter_view_visit_calendar(activity_view_patient_visits.this, dates,pid,SwitchState));

        final GestureDetector gestureDetector = new GestureDetector(new CalendarGestureDetector(activity_view_patient_visits.this));

        gridview_calendar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try{
                    DatabaseHandler db = new DatabaseHandler(activity_view_patient_visits.this);
                   // showListView(db.getPatientsForDate(gridview_calendar.getAdapter().getItem(position).toString()));
                    Toast.makeText(activity_view_patient_visits.this, "done " + position, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        gridview_calendar.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (gestureDetector.onTouchEvent(event)) {

                    return false;
                }
                return false;

            }
        });

    }


    private void hideListView(){
        listViewPatients.setVisibility(View.GONE);
    }

    private void showListView(ArrayList<String> pids){
        listViewPatients.setVisibility(View.VISIBLE);
        adapter_on_calendar_date_patients adapter = new adapter_on_calendar_date_patients(activity_view_patient_visits.this,pids);
        listViewPatients.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_patient_visits, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home : {
                finish();
                break;
            }
            case R.id.action_settings : {
               Intent intent = new Intent(this,appointment_settings.class);
                startActivity(intent);
                break;
            }

        }
        return super.onOptionsItemSelected(item);
    }

    class CalendarGestureDetector extends GestureDetector.SimpleOnGestureListener {
        Context context;

        public CalendarGestureDetector(Context cxt) {
            context = cxt;
        }



        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            isClicked = true;
            gridview_calendar.performClick();
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            isClicked = true;
            return false;
        }



        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                int SWIPE_MAX_OFF_PATH = 700;
                int SWIPE_THRESHOLD_VELOCITY = 100;
                int SWIPE_MIN_DISTANCE = 100;

                Animation animation1 = AnimationUtils.loadAnimation(context, R.anim.appear);
                Animation animation2 = AnimationUtils.loadAnimation(context,R.anim.disappear);

                AnimationSet animationSet1 = new AnimationSet(true);
                animationSet1.addAnimation(animation2);
//                animationSet.addAnimation(animation2);
                LayoutAnimationController controller1 = new LayoutAnimationController(animationSet1,0.1f);
//                gridview_calendar.setLayoutAnimation(controller1);

                Animation animation3 = AnimationUtils.loadAnimation(context, R.anim.disappear);
                Animation animation4 = AnimationUtils.loadAnimation(context,R.anim.appear);

                AnimationSet animationSet2 = new AnimationSet(true);
//                animationSet2.addAnimation(animation3);
                animationSet2.addAnimation(animation4);
                LayoutAnimationController controller2 = new LayoutAnimationController(animationSet2,0.1f);
                relativeLayout.setLayoutAnimation(controller1);
                relativeLayout.setLayoutAnimation(controller2);
                isClicked = true;
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                // right to left swipe
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    MonthChanged(false);
                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    MonthChanged(true);
                }
            } catch (Exception e) {
                // nothing
            }
            return false;
        }

    }

    public void MonthChanged(boolean isLeftSwiped){
        Calendar cal = Calendar.getInstance();
        cal.setTime(calendar_descriptor.startDate);
        if (isLeftSwiped){
            cal.add(Calendar.MONTH,-1);
        }else{
            cal.add(Calendar.MONTH,1);
        }
        calendar_descriptor.startDate = cal.getTime();

        String s = new SimpleDateFormat("MMMM").format(cal.getTime());
        String year = new SimpleDateFormat("yyyy").format(cal.getTime());
        current_month.setText(s + " " + year);

        int totaldays = calendar_descriptor.getTotalDays();
        String[] datesnew = calendar_descriptor.getDates(totaldays);

        gridview_calendar.setAdapter(new adapter_view_visit_calendar(this, datesnew,pid,SwitchState));


    }
}

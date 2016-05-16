package activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import utilityClasses.DatabaseHandler;
import com.elune.sajid.myapplication.R;
import utilityClasses.util.calendar_descriptor;
import utilityClasses.utility;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import adapters.adapter_on_calendar_date_patients;
import adapters.adapter_view_visit_calendar;

/**
 * Created by nevermore on 10/24/2015.
 */

public class activity_view_patient_visits extends Fragment {

    GridView gridview_calendar;
    GridView gridview_days;
    TextView current_month;
    private static final String ARG_SECTION_NUMBER = "section_number";
    //  int totaldays;
    Date startDateCalendar;
    FragmentManager fragManager = null;
    Date endDateCalendar;
    static boolean SwitchState ;
   static adapter_view_visit_calendar adapter;
    ListView listViewPatients;
    RelativeLayout relativeLayout;
    int rightToLeftSwipe = 0;
    boolean isClicked = false;
    boolean showOptionsMenu;
    int pid;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.layout_activity_view_patient_visits, container, false);
        //setContentView(R.layout.layout_activity_view_patient_visits);
final DatabaseHandler databaseHandler = new DatabaseHandler(getActivity());
        gridview_calendar = (GridView)rootView.findViewById(R.id.gridview_calendar);
        listViewPatients = (ListView) rootView.findViewById(R.id.lvPatientsCalendar);
        relativeLayout = (RelativeLayout) rootView.findViewById(R.id.view_inventory_relative_layout);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        showOptionsMenu = prefs.getBoolean("showOptionMenu",false);
        getActivity().setTitle("Calender");
        ActionBar actionBar = ((AppCompatActivity) this.getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Calender");
        }
        fragManager = getActivity().getSupportFragmentManager();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        final TextView textView = (TextView)rootView.findViewById(R.id.patientTextView);
        final TextView textView2 = (TextView)rootView.findViewById(R.id.appointmentTextView);
        int totaldays = calendar_descriptor.getTotalDays();
        final String[] dates = calendar_descriptor.getDates(totaldays);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        if (calendar_descriptor.startDate == null) {
            calendar_descriptor.startDate = cal.getTime();
        }
        final Switch aSwitch = (Switch)rootView.findViewById(R.id.switch2);
        setHasOptionsMenu(showOptionsMenu);
//        SwitchState = aSwitch.isChecked();
        aSwitch.setChecked(SwitchState);
        if(SwitchState)
        {

            textView.setTextColor(Color.BLACK);
            textView2.setTextColor(Color.parseColor("gray"));
        }
        else
        {
            textView.setTextColor(Color.parseColor("gray"));
            textView2.setTextColor(Color.BLACK);
        }
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
            adapter = new adapter_view_visit_calendar(getContext(), dates,pid,SwitchState);
            gridview_calendar.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            final AsyncHttpClient client = new SyncHttpClient(true, 80, 443);
            aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    SwitchState = isChecked;
                    if(SwitchState)
                    {

                        textView.setTextColor(Color.BLACK);
                        textView2.setTextColor(Color.parseColor("gray"));
                    }
                    else
                    {
                        textView.setTextColor(Color.parseColor("gray"));
                        textView2.setTextColor(Color.BLACK);
                        setHasOptionsMenu(showOptionsMenu);
                        String s1 = null;
                        ArrayList<String>CstmrId = new ArrayList<>();
                        int customerId = databaseHandler.getCustomerId();
                        CstmrId.add(String.valueOf(customerId));
                        final RequestParams params = new RequestParams();
                        StringWriter out = new StringWriter();
                        try {
                            JSONValue.writeJSONString(CstmrId, out);
                            s1 = out.toString();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        params.put("DoctorId", s1);
                        final AsyncHttpClient client = new SyncHttpClient(true, 80, 443);
                        final ProgressDialog pdia;
                        pdia = new ProgressDialog(getActivity());
                        pdia.setMessage("Fetching appointments please wait");
                        pdia.show();
                       final String  address =  getResources().getString(R.string.action_server_ip_address);
                        Thread thread = new Thread(){
                            @Override
                            public void run() {
                                try {
                                    hitApiForAppointment("http://" + address + "/fetchDoctorAppointments.php",params,client,getActivity(),fragManager,pdia);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        thread.start();


                    }

                    int totaldays = calendar_descriptor.getTotalDays();
                    String[] datesnew = calendar_descriptor.getDates(totaldays);
                   adapter = new adapter_view_visit_calendar(getContext(), datesnew,pid,SwitchState);
                    gridview_calendar.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    Intent intent = getActivity().getIntent();

                   /* finish();
                    startActivity(intent);*/
                }
            });
        }
        catch(Exception e){
            e.printStackTrace();
        }
        calendar_descriptor.inventory = null;
        gridview_days = (GridView) rootView.findViewById(R.id.gridview_days);
        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, days) {
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
        current_month = (TextView) rootView.findViewById(R.id.textview_current_month);

        String s = new SimpleDateFormat("MMMM").format(date.getTime());
        String year = new SimpleDateFormat("yyyy").format(date.getTime());
        current_month.setText(s + " " + year);

        Intent i = getActivity().getIntent();
        pid = i.getIntExtra("id",0);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.view_inventory_relative_layout) {
                    hideListView();
                }
            }
        });
adapter_view_visit_calendar adapterViewVisitCalendar = new adapter_view_visit_calendar(getActivity(), dates,pid,SwitchState);
        gridview_calendar.setAdapter(adapterViewVisitCalendar);

adapterViewVisitCalendar.notifyDataSetChanged();
        final GestureDetector gestureDetector = new GestureDetector(new CalendarGestureDetector(getActivity()));

        gridview_calendar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try{
                    DatabaseHandler db = new DatabaseHandler(getActivity());
                    if(SwitchState)
                    showListView(db.getPatientsForDate(gridview_calendar.getAdapter().getItem(position).toString()),gridview_calendar.getAdapter().getItem(position).toString());
                    else
                    {
                        showListView(db.getAppointmentsForDate(gridview_calendar.getAdapter().getItem(position).toString()),gridview_calendar.getAdapter().getItem(position).toString());
                        Toast.makeText(getActivity(), "done " + position, Toast.LENGTH_SHORT).show();
                    }

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
        return rootView;

    }


    public void hitApiForAppointment(String apiAddress, RequestParams params, AsyncHttpClient client, final Context context, final FragmentManager fragmentManager, final ProgressDialog pdia) {


        final DatabaseHandler databaseHandler = new DatabaseHandler(context);

        try
        {

            client.post(apiAddress, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i,cz.msebera.android.httpclient. Header[] headers, byte[] bytes) {

                    try {
                        String str = new String(bytes, "UTF-8");
                        JSONArray response;
                        // JSONObject mainObject = new JSONObject(str);


                            response = (JSONArray) JSONValue.parse(str);
                        if(response.size()>0)
                            if(response.get(0).equals("account not verified"))
                            {
                                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putBoolean("showOptionMenu", false);
                                editor.commit();
                                setHasOptionsMenu(false);
                                Intent intent = new Intent(context, AccountVerificationActivity.class);
                                context.startActivity(intent);
                            }
                            else {
                                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putBoolean("showOptionMenu", true);
                                editor.commit();
                                setHasOptionsMenu(true);
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                String date = sdf.format(new Date());
                                databaseHandler.deleteAppointments(date);
                                //if()
                                utility.saveAppointmentsTable(response, context);
                            }
                        else
                        {
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putBoolean("showOptionMenu", true);
                            editor.commit();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            String date = sdf.format(new Date());
                            databaseHandler.deleteAppointments(date);
                            setHasOptionsMenu(true);

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(int i,cz.msebera.android.httpclient. Header[] headers, byte[] bytes, Throwable throwable) {
                    try {

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onFinish() {
                    pdia.dismiss();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, activity_view_patient_visits.newInstance(1,false))
                            .commit();


                    // Toast.makeText(context,    "END", Toast.LENGTH_LONG).show();
                    //((Activity) context).recreate();

                }


            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        // return customerId[0];
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_view_patient_visits, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home : {
                getActivity().finish();
                break;
            }
            case R.id.action_settings : {
                Intent intent = new Intent( getActivity(),AppointmentSettingsCheck.class);
                startActivity(intent);

                return  true;

            }

        }
        return super.onOptionsItemSelected(item);
    }
    public static activity_view_patient_visits newInstance(int sectionNumber,boolean stateOfSwitch) {
        activity_view_patient_visits fragment = new activity_view_patient_visits();

        Bundle args = new Bundle();
        SwitchState = stateOfSwitch;
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    private void hideListView(){
        listViewPatients.setVisibility(View.GONE);
    }

    private void showListView(ArrayList<String> pids,String date){
//        listViewPatients.setVisibility(View.VISIBLE);
//        listViewPatients.requestFocus();
        adapter_on_calendar_date_patients adapter = new adapter_on_calendar_date_patients(getActivity(),pids,date);
        LayoutInflater li = LayoutInflater.from(getActivity());
        final View promptsView = li.inflate(R.layout.calender_patient_list, null);
//        ListView listViewCalenderPatientList = (ListView)
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
        alertDialogBuilder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        adapter.notifyDataSetChanged();
        alertDialogBuilder
                .setCancelable(false)

                .setNegativeButton("Dismiss",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                fragManager.beginTransaction()
                                        .replace(R.id.container, activity_view_patient_visits.newInstance(1,true))
                                        .commit();
                                fragManager.beginTransaction()
                                        .replace(R.id.container, activity_view_patient_visits.newInstance(1,false))
                                        .commit();
                                pid =0;
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();





        //listViewPatients.setAdapter(adapter);
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

        gridview_calendar.setAdapter(new adapter_view_visit_calendar(getActivity(), datesnew,pid,SwitchState));


    }

}

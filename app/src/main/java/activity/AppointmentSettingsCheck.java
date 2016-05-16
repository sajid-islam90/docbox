package activity;

import java.util.ArrayList;
import java.util.Locale;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.elune.sajid.myapplication.R;
import com.loopj.android.http.RequestParams;

import utilityClasses.DatabaseHandler;
import utilityClasses.utility;

public class AppointmentSettingsCheck extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;
    private int currentPage;
    private ImageView sliderSunday;
    private ImageView sliderMonday;
    private ImageView sliderTuesday;
    private ImageView sliderWednesday;
    private ImageView sliderThursday;
    private ImageView sliderFriday;
    private ImageView sliderSaturday;

    private ArrayList<ImageView> week = new ArrayList<>();

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_settings_check);

        final ActionBar actionBar = getSupportActionBar();
       // actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
DatabaseHandler databaseHandler= new DatabaseHandler(AppointmentSettingsCheck.this);
        ArrayList<String>settings = new ArrayList<>();
        // Set up the ViewPager with the sections adapter.
        final CheckBox checkBoxSaturday = (CheckBox)findViewById(R.id.checkBoxSaturday);
        settings= databaseHandler.getAppointmentSettings("saturday");
        if(settings.size()>0)
        {
            checkBoxSaturday.setChecked(true);
        }

        final CheckBox checkBoxSunday = (CheckBox)findViewById(R.id.checkBoxSunday);
        settings= databaseHandler.getAppointmentSettings("sunday");
        if(settings.size()>0)
        {
            checkBoxSunday.setChecked(true);
        }

        final CheckBox checkBoxMonday = (CheckBox)findViewById(R.id.checkBoxMonday);
        settings= databaseHandler.getAppointmentSettings("monday");
        if(settings.size()>0)
        {
            checkBoxMonday.setChecked(true);
        }

        final CheckBox checkBoxTuesday = (CheckBox)findViewById(R.id.checkBoxTuesday);
        settings= databaseHandler.getAppointmentSettings("tuesday");
        if(settings.size()>0)
        {
            checkBoxTuesday.setChecked(true);
        }

        final CheckBox checkBoxWednesday = (CheckBox)findViewById(R.id.checkBoxWednesday);
        settings= databaseHandler.getAppointmentSettings("wednesday");
        if(settings.size()>0)
        {
            checkBoxWednesday.setChecked(true);
        }

        final CheckBox checkBoxThursday = (CheckBox)findViewById(R.id.checkBoxThursday);
        settings= databaseHandler.getAppointmentSettings("thursday");
        if(settings.size()>0)
        {
            checkBoxThursday.setChecked(true);
        }

        final CheckBox checkBoxFriday = (CheckBox)findViewById(R.id.checkBoxFriday);
        settings= databaseHandler.getAppointmentSettings("friday");
        if(settings.size()>0)
        {
            checkBoxFriday.setChecked(true);
        }


        mViewPager = (ViewPager) findViewById(R.id.pager);
        sliderSunday = (ImageView)findViewById(R.id.sliderSunday);
        sliderMonday = (ImageView)findViewById(R.id.sliderMonday);
        sliderTuesday = (ImageView)findViewById(R.id.sliderTuesday);
        sliderWednesday = (ImageView)findViewById(R.id.sliderWednesday);
        sliderThursday = (ImageView)findViewById(R.id.sliderThursday);
        sliderFriday = (ImageView)findViewById(R.id.sliderFriday);
        sliderSaturday = (ImageView)findViewById(R.id.sliderSaturday);
        week.add(sliderSunday);
        week.add(sliderMonday);
        week.add(sliderTuesday);
        week.add(sliderWednesday);
        week.add(sliderThursday);
        week.add(sliderFriday);
        week.add(sliderSaturday);
sliderFunction(0);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        checkBoxSaturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBoxSaturday.isChecked())
                checkBoxSaturday.setChecked(false);
                else
                    checkBoxSaturday.setChecked(true);
                mViewPager.setCurrentItem(6, true);
            }
        });
        checkBoxSunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBoxSunday.isChecked())
                    checkBoxSunday.setChecked(false);
                else
                    checkBoxSunday.setChecked(true);

                mViewPager.setCurrentItem(0,true);
            }
        });
        checkBoxMonday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBoxMonday.isChecked())
                    checkBoxMonday.setChecked(false);
                else
                    checkBoxMonday.setChecked(true);

                mViewPager.setCurrentItem(1,true);
            }
        });
        checkBoxTuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBoxTuesday.isChecked())
                    checkBoxTuesday.setChecked(false);
                else
                    checkBoxTuesday.setChecked(true);

                mViewPager.setCurrentItem(2,true);
            }
        });
        checkBoxWednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBoxWednesday.isChecked())
                    checkBoxWednesday.setChecked(false);
                else
                    checkBoxWednesday.setChecked(true);

                mViewPager.setCurrentItem(3,true);
            }
        });
        checkBoxThursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBoxThursday.isChecked())
                    checkBoxThursday.setChecked(false);
                else
                    checkBoxThursday.setChecked(true);

                mViewPager.setCurrentItem(4,true);
            }
        });
        checkBoxFriday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBoxFriday.isChecked())
                    checkBoxFriday.setChecked(false);
                else
                    checkBoxFriday.setChecked(true);

                mViewPager.setCurrentItem(5,true);
            }
        });
        DetailOnPageChangeListener detailOnPageChangeListener = new DetailOnPageChangeListener();
        mViewPager.setOnPageChangeListener(detailOnPageChangeListener);

    }


    public void sliderFunction(int position)
    {
        for(int i = 0;i<7;i++)
        {
            if(i==position)
            {
                week.get(i).setVisibility(View.VISIBLE);
            }
            else
            {
                week.get(i).setVisibility(View.GONE);
            }
        }
    }
    public class DetailOnPageChangeListener extends ViewPager.SimpleOnPageChangeListener {



        @Override
        public void onPageSelected(int position) {
            currentPage = position;
//            if(position != 0)
//            {
//                sliderSunday.setVisibility(View.GONE);
//            }
//            else
//            {
//                sliderSunday.setVisibility(View.VISIBLE);
//            }
            sliderFunction(position);
        }

        public final int getCurrentPage() {
            return currentPage;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_appointment_settings_check, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            RequestParams paramsDocuments = new RequestParams();
            DatabaseHandler databaseHandler = new DatabaseHandler(AppointmentSettingsCheck.this);

            String s1 = databaseHandler.composeJSONfromSQLiteAppointmentSettings(AppointmentSettingsCheck.this);
            paramsDocuments.put("appointmentPreferenceJSON", s1);
            String address ="docbox.co.in/sajid/"; //appointment_settings.this.getResources().getString(R.string.action_server_ip_address);
            utility. sync("http://" + address + "/saveAppointmentPreference.php", paramsDocuments, AppointmentSettingsCheck.this);
            Toast.makeText(AppointmentSettingsCheck.this, "Appointment Settings Saved Successfully", Toast.LENGTH_SHORT).show();

            // uploadfile.uploadImage(getActivity(), docPaths, pid);

            Thread thread = new Thread(){
                @Override
                public void run() {
                    try {

                        Thread.sleep(2000); // As I am using LENGTH_LONG in Toast
                       finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            thread.start();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return AppointmentDaysFragment.newInstance(true,"18:30","22:30",position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 7;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return "Sunday";
                case 1:
                    return "Monday";
                case 2:
                    return "Tuesday";
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_book_appointment, container, false);
            return rootView;
        }
    }

}

package activity;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sajid.myapplication.DatabaseHandler;
import objects.Item;
import objects.Patient;
import com.example.sajid.myapplication.R;
import adapters.TwoTextFieldsAdapter;
import objects.exam_obj;
import objects.history_obj;
import objects.notes_obj;
import objects.other_obj;
import objects.treatment_obj;


public class TabbedActivityCheck extends ActionBarActivity implements ActionBar.TabListener {
    static int pid;
    static  int version;
    static  String parent;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed_activity_check);
        Intent intent = getIntent();
        pid = intent.getIntExtra("id",0);
        version = intent.getIntExtra("version",1);
        parent  =intent.getStringExtra("parent");
        getLatestVersionTitle();
        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))

                            .setTabListener(this));
        }
    }

    public  void showHistMedia(View view)
    {


    }

    public void getLatestVersionTitle()
    {
        DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
       // notes_obj notesObj  = dbHandler.getLatestNote(pid);
        Patient patient = dbHandler.getPatient(pid);
       history_obj historyObj =   dbHandler.getVersionedHistNote(pid,version);

       // int version = dbHandler.getCurrentVersion(pid);
        if (version >0) {
            setTitle(historyObj.get_date());

            //displayNote(notesObj);
        }
        else
        {
            setTitle(patient.get_name() + "'s Notes ");
            // setContentView(R.layout.activity_clinical_notes_empty);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(parent.equals(PatientProfileActivity.class.toString()) )
        getMenuInflater().inflate(R.menu.menu_tabbed_activity_check, menu);
        else
            getMenuInflater().inflate(R.menu.empty_menu, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_notes) {
            this.startAddNotes();
            finish();

            return true;
        }
        else if(id == R.id.action_view_previous_versions)
        {
            Intent intent = new Intent(this,view_all_versions.class);
            intent.putExtra("id",pid);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
    public void startAddNotes()
    {
        Intent curIntent = getIntent();
        Intent intent =  new Intent(this, History_Activity.class);
        int pid = curIntent.getIntExtra("id",0);
        intent.putExtra("id",pid);
        startActivity(intent);
    }


    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
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
            if(position == 0)
            return PlaceholderFragment.newInstance(position + 1);
            else if (position == 1 )
                return PlaceholderFragment1.newInstance1(position + 1);
            else if (position == 2)
                return   PlaceholderFragment2.newInstance2(position + 1);
            else
                return   PlaceholderFragment3.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
                case 3:
                    return getString(R.string.title_section4).toUpperCase(l);
            }
            return null;
        }

    }



    /**
     * A placeholder fragment containing history view.
     */
    public static class PlaceholderFragment extends Fragment implements View.OnClickListener {
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


            View rootView ;

                 rootView = inflater.inflate(R.layout.activity_history_view, container, false);
                getAndDisplayHistNotes(this, rootView);
            Button button = (Button)rootView.findViewById(R.id.view_media_hist);
            button.setOnClickListener(this);



            return rootView;
        }

        public void displayHistNote(notes_obj notesObj,Fragment f,View rootView)
        {
            TextView presentIll = (TextView)rootView.findViewById(R.id.Hist_presentIll_view);
            TextView pastHist = (TextView)rootView.findViewById(R.id.Hist_past_view);
            TextView personalHist = (TextView)rootView.findViewById(R.id.Hist_personal_view);
            TextView familyHist = (TextView)rootView.findViewById(R.id.Hist_family_view);




            presentIll.setText(notesObj.get_hist_present_illness());
            pastHist.setText(notesObj.get_past_hist());
            personalHist.setText(notesObj.get_personal_hist());
            familyHist.setText(notesObj.get_family_hist());




        }

        public void getAndDisplayHistNotes(Fragment f,View rootView)
        {
            DatabaseHandler dbHandler = new DatabaseHandler(f.getActivity().getApplicationContext());
            history_obj historyObj = new history_obj();
            historyObj = dbHandler.getVersionedHistNote(pid, version);
            //exam_obj examObj = dbHandler.getLatestExamNote(pid);

            notes_obj notesObj  = new notes_obj();

            notesObj.set_date(historyObj.get_date());
            notesObj.set_past_hist(historyObj.get_past_illness());
            notesObj.set_hist_present_illness(historyObj.get_present_illness());
            notesObj.set_family_hist(historyObj.get_family_hist());
            notesObj.set_personal_hist(historyObj.get_personal_hist());

            //getLatestVersionTitle();
            //int version = dbHandler.getCurrentVersion(pid);
            if (version>=1)
                this.displayHistNote(notesObj, this, rootView);







        }


        @Override
        public void onClick(View v) {
            int a = v.getId();
            View b = v.findViewById(R.id.view_media_hist);

            Intent intent = new Intent(v.getContext(),View_Media_notes_grid.class);
            intent.putExtra("id",pid);
            intent.putExtra("section",1);
            intent.putExtra("version",version);
            startActivity(intent);

            int c = b.getId();
            c= R.id.view_media_hist;
            Toast.makeText(this.getActivity().getApplicationContext(),"button pressed",Toast.LENGTH_SHORT).show();
        }
    }


    public static class PlaceholderFragment1 extends Fragment implements View.OnClickListener    {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment1 newInstance1(int sectionNumber) {
            PlaceholderFragment1 fragment1 = new PlaceholderFragment1();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment1.setArguments(args);
            return fragment1;
        }

        public PlaceholderFragment1() {
        }



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_exam_view, container, false);
            Button button = (Button)rootView.findViewById(R.id.view_media_exam);
            button.setOnClickListener(this);
            /* Button button = (Button)rootView.findViewById(R.id.button9);
           final TextView textView = (TextView)rootView.findViewById(R.id.textView13);
            button.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Toast.makeText(getActivity(), textView.getText(), Toast.LENGTH_SHORT)
                            .show();
                }
            });*/
            getAndDisplayExamNotes(this, rootView);
            return rootView;
        }


        public void displayExamNote(notes_obj notesObj,Fragment f,View rootView)
        {


            TextView genExam = (TextView)rootView.findViewById(R.id.Exam_general_view);
            TextView locExam = (TextView)rootView.findViewById(R.id.Exam_local_view);



            genExam.setText(notesObj.get_gen_exam());
            locExam.setText(notesObj.get_loc_exam());



        }

        public void getAndDisplayExamNotes(Fragment f,View rootView)
        {
            DatabaseHandler dbHandler = new DatabaseHandler(f.getActivity().getApplicationContext());


            exam_obj examObj = dbHandler.getLatestExamNote(pid,version);

            notes_obj notesObj  = new notes_obj();


            notesObj.set_gen_exam(examObj.get_gen_exam());
            notesObj.set_loc_exam(examObj.get_local_exam());
            //getLatestVersionTitle();
            int version = dbHandler.getCurrentVersion(pid);
            if (version>=1)
                this.displayExamNote(notesObj, this, rootView);







        }
        @Override
        public void onClick(View v) {
            int a = v.getId();
            View b = v.findViewById(R.id.view_media_exam);

            Intent intent = new Intent(v.getContext(),View_Media_notes_grid.class);
            intent.putExtra("id",pid);
            intent.putExtra("section",2);
            intent.putExtra("version",version);
            startActivity(intent);

            int c = b.getId();
            c= R.id.view_media_hist;
            Toast.makeText(this.getActivity().getApplicationContext(),"button pressed",Toast.LENGTH_SHORT).show();
        }

    }




    public static class PlaceholderFragment2 extends Fragment implements View.OnClickListener  {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment2 newInstance2(int sectionNumber) {
            PlaceholderFragment2 fragment2 = new PlaceholderFragment2();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment2.setArguments(args);
            return fragment2;
        }

        public PlaceholderFragment2() {
        }



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_treatment_view, container, false);
            Button button = (Button)rootView.findViewById(R.id.view_media_treat);
            button.setOnClickListener(this);
            /* Button button = (Button)rootView.findViewById(R.id.button9);
           final TextView textView = (TextView)rootView.findViewById(R.id.textView13);
            button.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Toast.makeText(getActivity(), textView.getText(), Toast.LENGTH_SHORT)
                            .show();
                }
            });*/
            getAndDisplayTreatmentNotes(this, rootView);
            return rootView;
        }

        @Override
        public void onClick(View v) {
            int a = v.getId();
            View b = v.findViewById(R.id.view_media_treat);

            Intent intent = new Intent(v.getContext(),View_Media_notes_grid.class);
            intent.putExtra("id",pid);
            intent.putExtra("section",3);
            intent.putExtra("version",version);
            startActivity(intent);

            int c = b.getId();
            c= R.id.view_media_hist;
            Toast.makeText(this.getActivity().getApplicationContext(),"button pressed",Toast.LENGTH_SHORT).show();
        }


        public void displayTreatmentNote(notes_obj notesObj,Fragment f,View rootView)
        {


            TextView diagnosis = (TextView)rootView.findViewById(R.id.Treatment_diagnosis_view);
            TextView treatment = (TextView)rootView.findViewById(R.id.Treatment_treatment_view);
            TextView procedure = (TextView)rootView.findViewById(R.id.Treatment_procedure_view);
            TextView implants = (TextView)rootView.findViewById(R.id.Treatment_implants_view);


            diagnosis.setText(notesObj.get_diagnosis());
            treatment.setText(notesObj.get_treatment());
            procedure.setText(notesObj.get_procedure());
            implants.setText(notesObj.get_implant());



        }

        public void getAndDisplayTreatmentNotes(Fragment f,View rootView)
        {
            DatabaseHandler dbHandler = new DatabaseHandler(f.getActivity().getApplicationContext());


            treatment_obj treatmentObj = dbHandler.getLatestTreatmentNote(pid,version);

            notes_obj notesObj  = new notes_obj();


            notesObj.set_diagnosis(treatmentObj.get_diagnosis());
            notesObj.set_treatment(treatmentObj.get_treatment());
            notesObj.set_procedure(treatmentObj.get_procedure());
            notesObj.set_implant(treatmentObj.get_implants());
            //getLatestVersionTitle();
            //int version = dbHandler.getCurrentVersion(pid);
            if (version>=1)
                this.displayTreatmentNote(notesObj, this, rootView);







        }
    }




    public static class PlaceholderFragment3 extends Fragment implements View.OnClickListener {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment3 newInstance(int sectionNumber) {
            PlaceholderFragment3 fragment3 = new PlaceholderFragment3();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment3.setArguments(args);
            return fragment3;
        }

        public PlaceholderFragment3() {
        }



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_other_view, container, false);

            ArrayList<Item> fields = new ArrayList<>();
            Button button = (Button)rootView.findViewById(R.id.view_media_other);
            button.setOnClickListener(this);

            fields = displayOtherNotes(this);

            displayAddedField(fields, rootView,this);
           // getAndDisplayOtherNotes(this, rootView);
            return rootView;
        }


        @Override
        public void onClick(View v) {
            int a = v.getId();
            View b = v.findViewById(R.id.view_media_other);

            Intent intent = new Intent(v.getContext(),View_Media_notes_grid.class);
            intent.putExtra("id",pid);
            intent.putExtra("section",4);
            intent.putExtra("version",version);
            startActivity(intent);

            int c = b.getId();
            c= R.id.view_media_hist;

        }

        public ArrayList<Item> displayOtherNotes(Fragment f)
        {
            ArrayList<Item> field = new ArrayList<>();

            Item item;
            DatabaseHandler dbHandler = new DatabaseHandler(f.getActivity().getApplicationContext());
            other_obj otherObj[]=dbHandler.getLatestOtherNote(pid,version);
            if (otherObj != null)
            {
                for (int i = 0; i < otherObj.length; i++) {

                  item = new Item();
                  item.setTitle(otherObj[i].get_field_name());
                  item.setDiagnosis(otherObj[i].get_field_value());
                    field.add(item);
                   // field.add(otherObj[i].get_field_name());
                }
            }
            return  field;
        }

        public void displayAddedField(ArrayList<Item> fieldList,View rootview,Fragment f)
        {
            ListView listView = (ListView)rootview.findViewById(R.id.listViewOther);
       /* MainActivityList adapter1 = new
                MainActivityList(MainActivity.this, patientList, imageId);*/

            //MyAdapter adapter = new MyAdapter(Exam_Activity.this,patientList);
            TwoTextFieldsAdapter twoTextFieldsAdapter = new TwoTextFieldsAdapter(getActivity(),f.getActivity().getApplicationContext(),fieldList);

           // ArrayAdapter adapter = new ArrayAdapter (getActivity(),R.layout.activity_other_view,R.id.textView30,fieldList); ;



            listView.setAdapter(twoTextFieldsAdapter);

        }




    }


}

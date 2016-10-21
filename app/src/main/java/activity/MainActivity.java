package activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.elune.sajid.myapplication.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import adapters.MyAdapter;
import objects.Item;
import objects.Patient;
import pl.bclogic.pulsator4droid.library.PulsatorLayout;
import utilityClasses.ConnectionDetector;
import utilityClasses.DatabaseHandler;
import utilityClasses.RoundImage;
import utilityClasses.floatingactionbutton.FloatingActionButton;


public class MainActivity extends Fragment {
    public final static String EXTRA_MESSAGE = "com.mycompany.myfirstapp.MESSAGE";
    private String profilePicPath;
    private SearchView mSearchView;
    private ActionBarDrawerToggle mDrawerToggle;
    private RelativeLayout mDrawerRelativeLayout;
    private static final int REQUEST_TAKE_PHOTO = 100;
    private CharSequence mDrawerTitle = "Drawer";
    private CharSequence mTitle = "Patient Manager";
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private TextView mEmail;
    private ListView listView;
    private ProgressDialog pdia;
    private int numberOfPatients ;
    private RelativeLayout relativeLayout;
    private  TextView textView;
    private FloatingActionButton floatingActionButton;
    private PulsatorLayout pulsator;
    boolean doubleBackToExitPressedOnce = false;
//    Context context;
    RoundImage roundedImage;
    private static final String ARG_SECTION_NUMBER = "section_number";
    static Context context ;
    private String[] navigationDrawerOptions;
    private TextView mStatusView;
    List<String> patientNames = new ArrayList<String>();
    String accountType;
    MyAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_main, container, false);
        listView = (ListView)rootView.findViewById(R.id.listViewMain);
        context = getActivity();
       // SQLiteDatabase myDataBase= openOrCreateDatabase("patientManager",MODE_PRIVATE,null);
        DatabaseHandler dbHandle = new DatabaseHandler(getActivity());
        setHasOptionsMenu(true);
        getActivity().setTitle("My Patients");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        accountType = prefs.getString(getActivity().getString(R.string.account_type), "");
         pulsator  = (PulsatorLayout) rootView.findViewById(R.id.pulse_button);
       // pulsator.start();
        ActionBar actionBar = ((AppCompatActivity) this.getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("My Patients");
        }
         floatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.addpatientfirst);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),DisplayMessageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("parent", "main");
                startActivity(intent);
                getActivity().finish();
            }
        });
//        fetchPatientsTask fetchPatientsTask = new fetchPatientsTask();
//        fetchPatientsTask.execute((Void)null);
        displayPatientList(new ArrayList<Item>());
       //int numberOfPatients = getPatientList();
        fetchPatientsTask fetchPatientsTask = new fetchPatientsTask();
        fetchPatientsTask.execute((Void) null);
//        if(numberOfPatients == 0)
//        {
//             relativeLayout = (RelativeLayout)rootView.findViewById(R.id.drawer_layout);
//             textView  = (TextView)rootView.findViewById(R.id.textView56);
//            textView.setVisibility(View.VISIBLE);
//            pulsator.start();
//            floatingActionButton.setVisibility(View.VISIBLE);
//           //  relativeLayout.setBackgroundResource(R.drawable.backgroud);
//        }
        return rootView;
    }
    public static MainActivity newInstance(int sectionNumber) {
        MainActivity fragment = new MainActivity();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

 @Override
 public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
     inflater.inflate(R.menu.main_activity_actions, menu);
     super.onCreateOptionsMenu(menu, inflater);
 }


    public void displayPatientList(final ArrayList<Item> patientList)
    {

       /* MainActivityList adapter1 = new
                MainActivityList(MainActivity.this, patientList, imageId);*/
        Collections.reverse(patientList);
         adapter = new MyAdapter(getActivity(),patientList);

       // ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, patientList.toArray()) ;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DatabaseHandler dbHandle = new DatabaseHandler(getActivity());
                Patient patient = new Patient();
                patient = dbHandle.getSearchPatient(position, patientList);
                Intent intent = new Intent(getActivity(), PatientProfileActivity.class);
                intent.putExtra("id", patient.get_id());
                getActivity().startActivity(intent);
            }
        });
        listView.setAdapter(adapter);


    }



    public int getPatientList()
    {

        DatabaseHandler dbHandler = new DatabaseHandler(getActivity());

        Bitmap image = null;
        String name = null;
        String diagnosis = null;
        int patient_id = 0;
        byte[] bmpImage ;
        String lastVisit;
        Item nameImage = new Item();
        ArrayList<Item> nameWithImage = new ArrayList<Item>();
        List<Patient>patientList = dbHandler.getAllPatient();
       // List<String> patientNames = new ArrayList<String>();
        image = BitmapFactory.decodeResource(getResources(),R.drawable.default_photo);
        Collections.sort(patientNames, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });
        if(patientList.size() !=0) {
            for (int i = 0; i < patientList.size(); i++) {
                name = patientList.get(i).get_name();
                bmpImage = patientList.get(i).get_bmp();
                diagnosis = patientList.get(i).get_diagnosis();
                patient_id = patientList.get(i).get_id();
                lastVisit = patientList.get(i).get_last_seen_date();


                if(bmpImage != null) {

                image = BitmapFactory.decodeByteArray(bmpImage, 0, bmpImage.length);
                }

                nameImage.setTitle(name);

                nameImage.setBmp(image);
                nameImage.setDiagnosis(diagnosis);
                nameImage.setDate(lastVisit);
                nameWithImage.add(i, nameImage);
                nameImage.setPatient_id(patient_id);
                nameImage = new Item();
                patientNames.add(i, name);
            }

            displayPatientList(nameWithImage);


        }
        return nameWithImage.size();
    }

    void searchPatient(String searchNameString,String searchDiagnosisString,String searchLocationString)
    {

        DatabaseHandler dbHandler = new DatabaseHandler(getActivity());

        Bitmap image = null;
        String name = null;
        String diagnosis = null;
        int patient_id = 0;
        byte[] bmpImage ;
        Item nameImage = new Item();
        ArrayList<Item> nameWithImage = new ArrayList<Item>();
        List<Patient>patientList = dbHandler.search(searchNameString,searchDiagnosisString,searchLocationString);
        List<String> patientNames = new ArrayList<String>();
        if(patientList.size() !=0) {
            for (int i = 0; i < patientList.size(); i++) {
                name = patientList.get(i).get_name();
                bmpImage = patientList.get(i).get_bmp();
                diagnosis = patientList.get(i).get_diagnosis();
                patient_id = patientList.get(i).get_id();
                if (bmpImage != null)
                    image = BitmapFactory.decodeByteArray(bmpImage, 0, bmpImage.length);

                nameImage.setTitle(name);
                nameImage.setBmp(image);
                nameImage.setDiagnosis(diagnosis);
                nameImage.setPatient_id(patient_id);
                nameWithImage.add(i, nameImage);
                nameImage = new Item();
                patientNames.add(i, name);
            }
            displayPatientList(nameWithImage);
        }

    }


    public boolean onQueryTextChange(String newText) {
        mStatusView.setText("Query = " + newText);
        return false;
    }

    public boolean onQueryTextSubmit(String query) {
        mStatusView.setText("Query = " + query + " : submitted");
        return false;
    }

    public boolean onClose() {
        mStatusView.setText("Closed!");
        return false;
    }

    protected boolean isAlwaysExpanded() {
        return false;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id)
        {
            case R.id.action_add:

                if(!accountType.equals(getActivity().getString(R.string.account_type_helper)))
                {

                Intent intent = new Intent(getActivity(),DisplayMessageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("parent", "main");
                startActivity(intent);
                getActivity().finish();}
                else {
                    Toast.makeText(getActivity(), "You are not authorised to use this feature", Toast.LENGTH_SHORT)
                            .show();
                }

                return true;
            case R.id.action_search:


                LayoutInflater li = LayoutInflater.from(getActivity());
                final View promptsView = li.inflate(R.layout.search_prompt, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        getActivity());

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);
                DatabaseHandler dbHandler = new DatabaseHandler(getActivity());
                List<String> Names = null;
               // Names = dbHandler.getAllPatientNames();
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_list_item_1, patientNames);
                final AutoCompleteTextView userInput = (AutoCompleteTextView) promptsView
                        .findViewById(R.id.editTextDialogUserInput);
                userInput.setThreshold(1);
                userInput.setAdapter(adapter);


                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // get user input and set it to result
                                        // edit text
                                        CheckBox checkBoxName = (CheckBox)promptsView.findViewById(R.id.nameCheckBox);
                                        CheckBox checkBoxDiagnosis = (CheckBox)promptsView.findViewById(R.id.diagnosisCheckBox);
                                        CheckBox checkBoxLocation = (CheckBox)promptsView.findViewById(R.id.locationCheckBox);
                                        String searchName="",searchDiagnosis="",searchLocation = "";
                                        if(checkBoxName.isChecked())
                                            searchName = userInput.getText().toString();
                                        if(checkBoxDiagnosis.isChecked())
                                            searchDiagnosis = userInput.getText().toString();
                                        if(checkBoxLocation.isChecked())
                                            searchLocation = userInput.getText().toString();

                                        searchPatient(searchName,searchDiagnosis,searchLocation);



                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();






                return true;

            case R.id.action_sync:
                if(!accountType.equals(getActivity().getString(R.string.account_type_helper))) {
                    ConnectionDetector cd = new ConnectionDetector(getActivity());
                    if (!cd.isConnectingToInternet()) {
                        // Internet Connection is not present
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Internet ERROR !!!")
                                .setMessage("No Internet Connection Found Please Connect To Internet To Sync Data To Cloud")
                                .setPositiveButton("Take me to Mobile settings", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivityForResult(new Intent(Settings.ACTION_SETTINGS), 0);

                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                        // stop executing code by return

                    } else {
                        Intent intent2 = new Intent(getActivity(), data_sync_activity.class);
                        startActivity(intent2);
                    }
                }
                else {
                    Toast.makeText(getActivity(), "You are not authorised to use this feature", Toast.LENGTH_SHORT)
                            .show();
                }

               /*  Intent i = new Intent(this,NavigationDrawer.class);
                startActivity(i);*/
                return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        getPatientList();
        super.onResume();
    }



    public class fetchPatientsTask extends AsyncTask<Void, Void, Boolean> {



        ArrayList<Item> nameWithImage;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        public int getPatientList()
        {

            DatabaseHandler dbHandler = new DatabaseHandler(context);

            Bitmap image = null;
            String name = null;
            String diagnosis = null;
            int patient_id = 0;
            byte[] bmpImage ;
            String lastVisit;
            Item nameImage = new Item();
             nameWithImage = new ArrayList<Item>();
            List<Patient>patientList = dbHandler.getAllPatient();
            // List<String> patientNames = new ArrayList<String>();
            image = BitmapFactory.decodeResource(context.getResources(),R.drawable.default_photo);
            Collections.sort(patientNames, new Comparator<String>() {
                @Override
                public int compare(String s1, String s2) {
                    return s1.compareToIgnoreCase(s2);
                }
            });
            if(patientList.size() !=0) {
                for (int i = 0; i < patientList.size(); i++) {
                    name = patientList.get(i).get_name();
                    bmpImage = patientList.get(i).get_bmp();
                    diagnosis = patientList.get(i).get_diagnosis();
                    patient_id = patientList.get(i).get_id();
                    lastVisit = patientList.get(i).get_last_seen_date();


                    if(bmpImage != null) {

                        image = BitmapFactory.decodeByteArray(bmpImage, 0, bmpImage.length);
                    }

                    nameImage.setTitle(name);

                    nameImage.setBmp(image);
                    nameImage.setDiagnosis(diagnosis);
                    nameImage.setDate(lastVisit);
                    nameWithImage.add(i, nameImage);
                    nameImage.setPatient_id(patient_id);
                    nameImage = new Item();
                    patientNames.add(i, name);
                }




            }
            return nameWithImage.size();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
           numberOfPatients = getPatientList();
            return null;

        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if(numberOfPatients == 0)
            {

                textView.setVisibility(View.VISIBLE);
                pulsator.start();
                floatingActionButton.setVisibility(View.VISIBLE);
                //  relativeLayout.setBackgroundResource(R.drawable.backgroud);
            }
            else {
                adapter.updateReceiptsList(nameWithImage);
            }
        }




        public void doSomething() {

        }


    }

}

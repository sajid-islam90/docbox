package activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.elune.sajid.myapplication.R;

import java.util.ArrayList;

import adapters.view_all_versions_followup_adapter;
import objects.Item;
import utilityClasses.DatabaseHandler;

public class view_all_versions extends Fragment {
    static int pid;
    static ArrayList<Item> dates = new ArrayList<>();
     int version[];
  //  ArrayList<String> dates = new ArrayList<>();
    private static final String ARG_SECTION_NUMBER = "section_number";
    View rootView;
    String accountType;
    static view_all_versions_followup_adapter view_all_versions_followup_adapter;
    Animation animation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         rootView = inflater.inflate(R.layout.activity_view_all_versions, container, false);
        Button button = (Button)rootView.findViewById(R.id.buttonAddFollowup);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        accountType  = prefs.getString(getActivity().getString(R.string.account_type), "");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAddNotes();
            }
        });
        displayAllVersionDates();
//        animation = AnimationUtils.loadAnimation(getActivity(),
//                R.anim.wobble);
//        button.startAnimation(animation);
        return rootView;

    }
    public void startAddNotes()
    {
        if (!accountType.equals(getActivity().getString(R.string.account_type_helper)))
        {  DatabaseHandler databaseHandler = new DatabaseHandler(getActivity());
//        int version  =  databaseHandler.getMaxFollowupVersion(pid);
        Intent intent =  new Intent(getActivity(), followUp.class);
            final int version[] = databaseHandler.getMaxFollowupVersion(pid);
            Long tsLong = System.currentTimeMillis()/1000;
        intent.putExtra("version",tsLong);
            intent.putExtra("number",version.length+1);
        intent.putExtra("parent",PatientProfileActivity.class.toString());
        intent.putExtra("id", pid);
        startActivity(intent);
       // getActivity().finish();
             }
        else
        {
            Toast.makeText(getActivity(), "You are not authorised to use this feature", Toast.LENGTH_SHORT).show();
        }
    }
    public static view_all_versions newInstance(int sectionNumber,int id) {
        view_all_versions fragment = new view_all_versions();
        pid =id;
        Bundle args = new Bundle();

        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//       // setContentView(R.layout.activity_view_all_versions);
////        Intent intent = getIntent();
////        pid = intent.getIntExtra("id",0);
////       setTitle("All Follow Ups");
//       //getSupportActionBar().setHomeAsUpIndicator();
//        displayAllVersionDates();
//    }
@Override
public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.menu_view_all_versions, menu);
    super.onCreateOptionsMenu(menu, inflater);
}

//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        inflater.inflate(R.menu.menu_user_profile, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//        getMenuInflater().inflate(R.menu.menu_view_all_versions, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void displayAllVersionDates()
    {

        view_all_versions_followup_adapter = new view_all_versions_followup_adapter(getActivity(),getContext(),dates);
        DatabaseHandler databaseHandler = new DatabaseHandler(getActivity());
        // dates = databaseHandler.getAllNotesDates(pid,getActivity());
       fetchFollowups fetchPatientsTask = new fetchFollowups();
        fetchPatientsTask.execute((Void) null);

       // Collections.reverse(dates);
        //Collections.reverse(version);

//        ArrayAdapter<String> itemsAdapter =
//                new ArrayAdapter<String>(getActivity(), R.layout.simple_list_item, dates);
        final ListView listView = (ListView)rootView.findViewById(R.id.allVersionDates);
        listView.setAdapter(view_all_versions_followup_adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapter, View v, final int position,
                                    long arg3) {


                final PopupMenu popup = new PopupMenu(getActivity(),listView.getChildAt(position));
                popup.getMenuInflater().inflate(R.menu.followup_pop_up
                        , popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {



                        if (item.getTitle().equals("Delete")) {

                            final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

                            alert.setTitle("Alert!!");
                            alert.setMessage("Are you sure to delete this follow up dated "+dates.get(position).getDiagnosis()+" ?");

                            alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    DatabaseHandler databaseHandler = new DatabaseHandler(getActivity());
                                    databaseHandler.deleteFollowUp(pid,String.valueOf(version[position]));
//                                    databaseHandler.deleteDiagnosis(_listDataHeader.get(groupPosition));
//                                    _listDataHeader.remove(groupPosition);
//                                    updateReceiptsList();
                                    Toast.makeText(getActivity(),"Follow Up deleted",Toast.LENGTH_LONG).show();
                                    dates.remove(position);
                                    view_all_versions_followup_adapter.updateDataSet(dates);
                                    dialog.dismiss();

                                }
                            });


                            alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();
                                }
                            });
                            alert.show();

                        }
                        if (item.getTitle().equals("Open")) {

                            Intent  intent;

                            intent = new Intent(getActivity(), ViewFollowUp_Activity.class);
                            intent.putExtra("id",pid);
                            intent.putExtra("version",version[position]);
                            intent.putExtra("number",position+1);
                            intent.putExtra("parent",view_all_versions.class.toString());
                            startActivity(intent);
                            getActivity().finish();

                        }


                        return false;


                    }
                });

                /** Showing the popup menu */
                popup.show();







                // String value = (String) adapter.getItemAtPosition(position);
                // assuming string and if you want to get the value on click of list item
                // do what you intend to do on click of listview row
            }
        });
//        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                final PopupMenu popup = new PopupMenu(getActivity(),listView);
//                popup.getMenuInflater().inflate(R.menu.diagnosis_popup
//                        , popup.getMenu());
//
//                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//
//
//
//                        if (item.getTitle().equals("delete")) {
//
//                            final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
//
//                            alert.setTitle("Alert!!");
//                            alert.setMessage("Are you sure to delete this follow up ? ");
//
//                            alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//
////                                    DatabaseHandler databaseHandler = new DatabaseHandler(_context);
////                                    databaseHandler.deleteDiagnosis(_listDataHeader.get(groupPosition));
////                                    _listDataHeader.remove(groupPosition);
////                                    updateReceiptsList();
//                                    dialog.dismiss();
//
//                                }
//                            });
//
//
//                            alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//
//                                    dialog.dismiss();
//                                }
//                            });
//                            alert.show();
//
//                        }
//
//
//                        return false;
//
//
//                    }
//                });
//
//                /** Showing the popup menu */
//                popup.show();
//                return false;
//            }
//        });
    }

    public class fetchFollowups extends AsyncTask<Void, Void, Boolean> {




        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }


        @Override
        protected Boolean doInBackground(Void... params) {
            DatabaseHandler databaseHandler = new DatabaseHandler(getActivity());
             dates = databaseHandler.getAllNotesDates(pid,getActivity());
            version = databaseHandler.getMaxFollowupVersion(pid);
            return null;

        }

        @Override
        protected void onPostExecute(final Boolean success) {

view_all_versions_followup_adapter.updateDataSet(dates);
        }




        public void doSomething() {

        }


    }
}

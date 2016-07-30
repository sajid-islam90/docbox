package activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import utilityClasses.DatabaseHandler;
import com.elune.sajid.myapplication.R;

import java.util.ArrayList;

public class view_all_versions extends Fragment {
    static int pid;
    ArrayList<String> dates = new ArrayList<>();
    private static final String ARG_SECTION_NUMBER = "section_number";
    View rootView;
    String accountType;

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
        DatabaseHandler databaseHandler = new DatabaseHandler(getActivity());
        ArrayList<String> dates = databaseHandler.getAllNotesDates(pid,getActivity());
        final int version[] = databaseHandler.getMaxFollowupVersion(pid);
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, dates);
        ListView listView = (ListView)rootView.findViewById(R.id.allVersionDates);
        listView.setAdapter(itemsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {

                Intent  intent;

                intent = new Intent(getActivity(), ViewFollowUp_Activity.class);
                intent.putExtra("id",pid);
                intent.putExtra("version",version[position]);
                intent.putExtra("number",position+1);
                intent.putExtra("parent",view_all_versions.class.toString());
                startActivity(intent);
                // String value = (String) adapter.getItemAtPosition(position);
                // assuming string and if you want to get the value on click of list item
                // do what you intend to do on click of listview row
            }
        });
    }
}

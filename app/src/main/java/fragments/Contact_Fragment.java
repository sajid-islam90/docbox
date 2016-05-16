package fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import utilityClasses.DatabaseHandler;
import com.elune.sajid.myapplication.R;
import utilityClasses.utility;
import com.loopj.android.http.RequestParams;

import org.json.simple.JSONValue;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

import objects.DataBaseEnums;
import objects.Patient;

public class Contact_Fragment extends Fragment {
    static int pid;
    ArrayList<String> dates = new ArrayList<>();
    private static final String ARG_SECTION_NUMBER = "section_number";
    View rootView;
    String accountType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragmenta_contact, container, false);
        final Button button1 = (Button)rootView.findViewById(R.id.buttonSaveContact);
        final Button button = (Button)rootView.findViewById(R.id.buttonEditContact);
        final Button button2 = (Button)rootView.findViewById(R.id.buttonCallPatient);
        final Button button3 = (Button)rootView.findViewById(R.id.buttonMsgPatient);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        accountType  = prefs.getString(getActivity().getString(R.string.account_type), "");


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeEditable(rootView);
                button1.setVisibility(View.VISIBLE);
                button.setVisibility(View.GONE);
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeUnEditable(rootView);
                button.setVisibility(View.VISIBLE);
                button1.setVisibility(View.GONE);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (accountType.equals(getActivity().getString(R.string.account_type_doctor)))
                callPatient();
                else
                    Toast.makeText(getActivity(), "You are not authorised to use this feature", Toast.LENGTH_SHORT).show();
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (accountType.equals(getActivity().getString(R.string.account_type_doctor)))
                    msgPatient();
                else
                    Toast.makeText(getActivity(), "You are not authorised to use this feature", Toast.LENGTH_SHORT).show();
            }
        });
        displayContactData();
        return rootView;

    }
    public void msgPatient()
    {
        final DatabaseHandler dbHandler = new DatabaseHandler(getActivity());
        final Patient patient = dbHandler.getPatient(pid);
        LayoutInflater li = LayoutInflater.from(getActivity());
        if((patient.get_contact_number()==null)||(patient.get_contact_number().equals("")))
        { Toast.makeText(getActivity(),"Please Add Contact Number To Message",Toast.LENGTH_SHORT).show();}
        else
        {
            final Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
            final View promptsView = li.inflate(R.layout.sms_text, null);
            final TextView textView = (TextView)promptsView.findViewById(R.id.sms_Edit_Text);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    getActivity());

            // set prompts.xml to alertdialog builder
            alertDialogBuilder.setView(promptsView);

            alertDialogBuilder
                    .setCancelable(false)
                    .setTitle("Compose Message")
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    String address = getResources().getString(R.string.action_server_ip_address);
                                    ArrayList<String> data = new ArrayList<String>();
                                    data.add(dbHandler.getPersonalInfo().get_name());
                                    data.add(textView.getText() + "\n-Sent From DocBox");
                                    // data.add("www.firstaid.com");
                                    data.add(patient.get_contact_number());
                                    String s1 = null;

                                    StringWriter out = new StringWriter();
                                    try {
                                        JSONValue.writeJSONString(data, out);
                                        s1 = out.toString();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    RequestParams params = new RequestParams();
                                    params.put("data", s1);
                                    utility.sync("http://" + address + "/patientMessage.php", params, getActivity());
                                    Toast.makeText(getActivity(),"Message Sent",Toast.LENGTH_SHORT).show();
//                                    smsIntent.setType("vnd.android-dir/mms-sms");
//                                    smsIntent.putExtra("address", patient.get_contact_number());
//                                    smsIntent.putExtra("sms_body", textView.getText() + "\n-Sent From DocBox");
//                                    PatientProfileActivity.this.startActivity(Intent.createChooser(smsIntent, "SMS:"));
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



//            smsIntent.setType("vnd.android-dir/mms-sms");
//            smsIntent.putExtra("address", patient.get_contact_number());
//            smsIntent.putExtra("sms_body", "Sent From DocBox");
//            PatientProfileActivity.this.startActivity(Intent.createChooser(smsIntent, "SMS:"));

        }

    }
    public void displayContactData()
    {

        TextView textView =  (TextView)rootView.findViewById(R.id.textViewAddress);
        TextView textView1 = (TextView)rootView.findViewById(R.id.textViewPhoneNumber);
        TextView textView2 = (TextView)rootView.findViewById(R.id.textViewEmail);
        EditText editText = (EditText)rootView.findViewById(R.id.editTextAddress);
        EditText editText2 = (EditText)rootView.findViewById(R.id.editTextPhoneNumber);
        EditText editText3 = (EditText)rootView.findViewById(R.id.editTextEmail);
        DatabaseHandler databaseHandler = new DatabaseHandler(getActivity());
        Patient patient = databaseHandler.getPatient(pid);
        textView.setText(patient.get_address());
        textView1.setText(patient.get_contact_number());
        textView2.setText(patient.get_email());

    }
    public void callPatient()
    {
        DatabaseHandler dbHandler = new DatabaseHandler(getActivity());
        Patient patient = dbHandler.getPatient(pid);
        if((patient.get_contact_number()==null)||(patient.get_contact_number().equals("")))
        { Toast.makeText(getActivity(), "Please Add Contact Number To Make A Call", Toast.LENGTH_SHORT).show();}
        else
        {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + patient.get_contact_number()));
            startActivity(intent);

        }

    }
    public void makeEditable(View rootView)
    {
        LinearLayout linearLayout = (LinearLayout)rootView.findViewById(R.id.linearLayoutContactButtons);
        linearLayout.setVisibility(View.GONE);

        TextView textView =  (TextView)rootView.findViewById(R.id.textViewAddress);
        TextView textView1 = (TextView)rootView.findViewById(R.id.textViewPhoneNumber);
        TextView textView2 = (TextView)rootView.findViewById(R.id.textViewEmail);
        textView.setVisibility(View.GONE);
        textView1.setVisibility(View.GONE);
        textView2.setVisibility(View.GONE);
        DatabaseHandler databaseHandler = new DatabaseHandler(getActivity());
        Patient patient = databaseHandler.getPatient(pid);


        EditText editText = (EditText)rootView.findViewById(R.id.editTextAddress);
        EditText editText2 = (EditText)rootView.findViewById(R.id.editTextPhoneNumber);
        EditText editText3 = (EditText)rootView.findViewById(R.id.editTextEmail);
        editText.setText(patient.get_address());
        editText2.setText(patient.get_contact_number());
        editText3.setText(patient.get_email());
        editText.setVisibility(View.VISIBLE);
        editText2.setVisibility(View.VISIBLE);
        editText3.setVisibility(View.VISIBLE);
    }
    public void makeUnEditable(View rootView)
    {

        DatabaseHandler databaseHandler = new DatabaseHandler(getActivity());

        LinearLayout linearLayout = (LinearLayout)rootView.findViewById(R.id.linearLayoutContactButtons);
        linearLayout.setVisibility(View.VISIBLE);

        EditText editText = (EditText)rootView.findViewById(R.id.editTextAddress);
        EditText editText2 = (EditText)rootView.findViewById(R.id.editTextPhoneNumber);
        EditText editText3 = (EditText)rootView.findViewById(R.id.editTextEmail);
        editText.setVisibility(View.GONE);
        editText2.setVisibility(View.GONE);
        editText3.setVisibility(View.GONE);
        databaseHandler.updatePatient(DataBaseEnums.KEY_ADDRESS, editText.getText().toString(), String.valueOf(pid));
        databaseHandler.updatePatient(DataBaseEnums.KEY_CONTACT,editText2.getText().toString(), String.valueOf(pid));
        databaseHandler.updatePatient(DataBaseEnums.KEY_EMAIL,editText3.getText().toString(), String.valueOf(pid));
        databaseHandler.updatePatient(DataBaseEnums.KEY_SYNC_STATUS,"0", String.valueOf(pid));

        TextView textView =  (TextView)rootView.findViewById(R.id.textViewAddress);
        TextView textView1 = (TextView)rootView.findViewById(R.id.textViewPhoneNumber);
        TextView textView2 = (TextView)rootView.findViewById(R.id.textViewEmail);
        textView.setVisibility(View.VISIBLE);
        textView1.setVisibility(View.VISIBLE);
        textView2.setVisibility(View.VISIBLE);
        textView.setText(editText.getText().toString());
        textView1.setText(editText2.getText().toString());
        textView2.setText(editText3.getText().toString());

    }
    public static Contact_Fragment newInstance(int sectionNumber,int id) {
        Contact_Fragment fragment = new Contact_Fragment();
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


    }
}

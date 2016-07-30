package fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;

import utilityClasses.DatabaseHandler;
import com.elune.sajid.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import activity.PatientProfileActivity;
import adapters.ExpandableListAdapter;
import objects.DataBaseEnums;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Diagnosis.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Diagnosis#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Diagnosis extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayList<String> listDataHeader;
    ArrayList<String> listDataHeaderDates;
    HashMap<String, List<String>> listDataChild;
    HashMap<String, List<String>> listDataChildDates;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View rootView;
    String accountType;
    static int pid;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    private static final String ARG_SECTION_NUMBER = "section_number";
    public static Fragment newInstance(int position,int id) {
        Diagnosis fragment = new Diagnosis();
        Bundle args = new Bundle();
        pid =id;

        args.putInt(ARG_SECTION_NUMBER, position);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Diagnosis() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_drug_allergy, container, false);
        Button button = (Button)rootView.findViewById(R.id.buttonAddDiagnosis);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!accountType.equals(getActivity().getString(R.string.account_type_helper)))
                addDiagnosis();
                else
                    Toast.makeText(getActivity(), "You are not authorised to use this feature", Toast.LENGTH_SHORT).show();
            }
        });
        expListView = (ExpandableListView) rootView.findViewById(R.id.lvExp);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        accountType  = prefs.getString(getActivity().getString(R.string.account_type), "");

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild,listDataHeaderDates,listDataChildDates);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                if (!accountType.equals(getActivity().getString(R.string.account_type_helper)))


                if ((listDataChild.get(
                        listDataHeader.get(groupPosition)).size() != childPosition + 1)) {
                    Toast.makeText(
                            getActivity(),
                            listDataHeader.get(groupPosition)
                                    + " : "
                                    + listDataChild.get(
                                    listDataHeader.get(groupPosition)).get(
                                    childPosition), Toast.LENGTH_SHORT)
                            .show();
                } else {
                    addTreatment(groupPosition);


                }
                else
                {
                    Toast.makeText(getActivity(), "You are not authorised to use this feature", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        return  rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void prepareListData() {

        DatabaseHandler databaseHandler = new DatabaseHandler(getActivity());
        int versions = databaseHandler.getDiagnosisCurrentVersion(String.valueOf(pid));
        Map<String, Object> diagnosis = new HashMap<>();
        listDataHeader =  new ArrayList<>();
        listDataHeaderDates =new ArrayList<>();
        listDataChild = new HashMap<String, List<String>>();
        listDataChildDates = new HashMap<String, List<String>>();


       List<List<String>> childData =   new ArrayList<List<String>>();
        List<List<String>> childDataDates =   new ArrayList<List<String>>();


        for (int i = 1;i<=versions;i++)
        {
            diagnosis = databaseHandler.getDiagnosis(String.valueOf(pid),i);
           long unixVersion =  databaseHandler.getDiagnosisUnixVersion(String.valueOf(pid),i);
            listDataHeader.add((String) diagnosis.get(DataBaseEnums.KEY_DIAGNOSIS));
            listDataHeaderDates.add((String) diagnosis.get(DataBaseEnums.KEY_DATE));
            List<List<String>>  treatmentData = new ArrayList<>();
            treatmentData =databaseHandler.getTreatment(String.valueOf(pid), (int) unixVersion);
            List<String> treatment = treatmentData.get(0);
            List<String>treatmentDates = treatmentData.get(1);
            treatment.add("Click here to Add a Treatment");
            childData.add(treatment);
            childDataDates.add(treatmentDates);

        }
        for(int k = 0;k<childData.size();k++)
        {
            listDataChild.put(listDataHeader.get(k), childData.get(k));
            listDataChildDates.put(listDataHeader.get(k), childDataDates.get(k));
        }

//        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
//        listDataChild.put(listDataHeader.get(1), nowShowing);
//        listDataChild.put(listDataHeader.get(2), comingSoon);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public  void addDiagnosis()
    {
        LayoutInflater li = LayoutInflater.from(getActivity());
        final DatabaseHandler databaseHandler = new DatabaseHandler(getActivity());
        Bundle args = getArguments();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        final int section = args.getInt(ARG_SECTION_NUMBER);
        View promptsView = li.inflate(R.layout.add_diagnosis, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getContext());

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText diagnosis = (EditText) promptsView
                .findViewById(R.id.editTextDiagnosis);

        final EditText date = (EditText) promptsView
                .findViewById(R.id.editTextDate);
        date.setText(formattedDate);
        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Save",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                databaseHandler.saveDiagnosis(String.valueOf(pid),diagnosis.getText().toString(),date.getText().toString());
                                databaseHandler.updatePatient(DataBaseEnums.KEY_DIAGNOSIS,diagnosis.getText().toString(),String.valueOf(pid));
                                Intent intent = new Intent(getActivity(), PatientProfileActivity.class);
                                prepareListData();
                                listAdapter.updateReceiptsList(listDataHeader,listDataChild,listDataHeaderDates, listDataChildDates);

//                                getActivity().finish();
//                                intent.putExtra("id",pid);
//                                intent.putExtra("tab",0);
//                               startActivity(intent);

                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();



    }
    public  void addTreatment(final int groupPosition)
    {
        LayoutInflater li = LayoutInflater.from(getActivity());
        final DatabaseHandler databaseHandler = new DatabaseHandler(getActivity());
        final long unixVersion =  databaseHandler.getDiagnosisUnixVersion(String.valueOf(pid),(groupPosition+1));
        Bundle args = getArguments();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        final int section = args.getInt(ARG_SECTION_NUMBER);
        View promptsView = li.inflate(R.layout.add_treatment, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getContext());

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText treatment = (EditText) promptsView
                .findViewById(R.id.editTextTreatment);

        final EditText date = (EditText) promptsView
                .findViewById(R.id.editTextDateTreatment);
        date.setText(formattedDate);
        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Save",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                               // databaseHandler.saveDiagnosis(String.valueOf(pid),diagnosis.getText().toString(),date.getText().toString());
                                databaseHandler.saveTreatment(String.valueOf(pid), treatment.getText().toString(), date.getText().toString(), (int) unixVersion);
                                prepareListData();
                                listAdapter.updateReceiptsList(listDataHeader,listDataChild,listDataHeaderDates, listDataChildDates);
                                //listAdapter.notifyDataSetChanged();
                               // Toast.makeText(getActivity(), "new treatment is added for diagnosis " + listDataHeader.get((int) unixVersion), Toast.LENGTH_SHORT).show();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();



    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}

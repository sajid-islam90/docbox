package activity;
// Decommissioned
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import utilityClasses.DatabaseHandler;
import objects.Patient;
import com.elune.sajid.myapplication.R;

import objects.history_obj;
import objects.notes_obj;


public class ClinicalNotesActivity extends ActionBarActivity {
    int pid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinical_notes);
        Intent intent = getIntent();
        pid = intent.getIntExtra("id",0);
        //getLatestVersion();
        getAndDisplayNotes();


    }


    public void getAndDisplayNotes()
    {
        DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
        history_obj historyObj = new history_obj();
       // historyObj = dbHandler.getVersionedHistNote(pid);
       // exam_obj examObj = dbHandler.getLatestExamNote(pid);

        notes_obj notesObj  = new notes_obj();

        notesObj.set_date(historyObj.get_date());
        notesObj.set_past_hist(historyObj.get_past_illness());
        notesObj.set_hist_present_illness(historyObj.get_present_illness());
        notesObj.set_family_hist(historyObj.get_family_hist());
        notesObj.set_personal_hist(historyObj.get_personal_hist());
       // notesObj.set_gen_exam(examObj.get_gen_exam());
       // notesObj.set_loc_exam(examObj.get_local_exam());
        getLatestVersionTitle();
        displayNote(notesObj);




    }

    public void getLatestVersionTitle()
    {
        DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
        notes_obj notesObj  = dbHandler.getLatestNote(pid);
        Patient patient = dbHandler.getPatient(pid);
        int version = dbHandler.getCurrentVersion(pid);
        if (version >0) {
            setTitle(patient.get_name() + "'s Notes Version. " + version);

            //displayNote(notesObj);
        }
        else
        {
            setTitle(patient.get_name() + "'s Notes ");
           // setContentView(R.layout.activity_clinical_notes_empty);
        }

    }



    public void displayNote(notes_obj notesObj)
    {
        TextView date = (TextView)findViewById(R.id.Vdate);
        TextView complaints = (TextView)findViewById(R.id.VchiefComplaint);
        TextView presentIll = (TextView)findViewById(R.id.VHistOfPresentIllness);
        TextView pastHist = (TextView)findViewById(R.id.VPastHistory);
        TextView personalHist = (TextView)findViewById(R.id.VPersonalHistory);
        TextView familyHist = (TextView)findViewById(R.id.VFamilyHistory);
        TextView genExam = (TextView)findViewById(R.id.VGeneralExamination);
        TextView locExam = (TextView)findViewById(R.id.VLocalExamination);
        TextView classification = (TextView)findViewById(R.id.VClassification);
        TextView diagnosis = (TextView)findViewById(R.id.VDiagnosis);
        TextView treatment = (TextView)findViewById(R.id.VTreatment);

        TextView implant = (TextView)findViewById(R.id.VImplantUsed);
        TextView score = (TextView)findViewById(R.id.VScore);
        TextView remark = (TextView)findViewById(R.id.VRemark);

        date.setText(notesObj.get_date());
        complaints.setText(notesObj.get_complaint());
        presentIll.setText(notesObj.get_hist_present_illness());
        pastHist.setText(notesObj.get_past_hist());
        personalHist.setText(notesObj.get_personal_hist());
        familyHist.setText(notesObj.get_family_hist());
        genExam.setText(notesObj.get_gen_exam());
        locExam.setText(notesObj.get_loc_exam());
        classification.setText(notesObj.get_classification());
        diagnosis.setText(notesObj.get_diagnosis());
        treatment.setText(notesObj.get_treatment());
        implant.setText(notesObj.get_implant());
        score.setText(notesObj.get_score());
        remark.setText(notesObj.get_remark());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_clinical_notes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
               /* Intent intent1 = getIntent();
                int Activity_id = intent1.getIntExtra("id",0);
                Intent intent = new Intent(this,documents.class);
                intent.putExtra("id",Activity_id);
                startActivity(intent);
                return true;*/
                finish();
            case R.id.ViewHistory:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Created by sajid on 5/3/2015.
     */
    public static class DemoFragment {
    }
}

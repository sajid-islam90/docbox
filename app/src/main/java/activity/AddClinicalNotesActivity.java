package activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sajid.myapplication.DatabaseHandler;
import com.example.sajid.myapplication.R;
import objects.notes_obj;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class AddClinicalNotesActivity extends ActionBarActivity {

    int pid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clinical_notes);
        TextView date = (TextView)findViewById(R.id.AddDate);
        Calendar c = Calendar.getInstance();


        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        date.setText(formattedDate);
        Intent intent = getIntent();
        pid = intent.getIntExtra("id",0);

    }


    public void saveNotes(View view)
    {
        TextView date = (TextView)findViewById(R.id.AddDate);
        EditText complaints = (EditText)findViewById(R.id.AddChiefComplaint);
        EditText presentIll = (EditText)findViewById(R.id.AddHistOfPresentIllness);
        EditText pastHist = (EditText)findViewById(R.id.AddPastHistory);
        EditText personalHist = (EditText)findViewById(R.id.AddPersonalHistory);
        EditText familyHist = (EditText)findViewById(R.id.AddFamilyHistory);
        EditText genExam = (EditText)findViewById(R.id.AddGeneralExamination);
        EditText locExam = (EditText)findViewById(R.id.AddLocalExamination);
        EditText classification = (EditText)findViewById(R.id.AddClassification);
        EditText diagnosis = (EditText)findViewById(R.id.AddDiagnosis);
        EditText treatment = (EditText)findViewById(R.id.AddTreatment);
        EditText procedure = (EditText)findViewById(R.id.AddProcedure);
        EditText implant = (EditText)findViewById(R.id.AddImplantUsed);
        EditText score = (EditText)findViewById(R.id.AddScore);
        EditText remark = (EditText)findViewById(R.id.AddRemark);

        DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());

        notes_obj note = new notes_obj();
        note.set_date(date.getText().toString());
        note.set_complaint(complaints.getText().toString());
        note.set_hist_present_illness(presentIll.getText().toString());
        note.set_past_hist(pastHist.getText().toString());
        note.set_personal_hist(personalHist.getText().toString());
        note.set_family_hist(familyHist.getText().toString());
        note.set_gen_exam(genExam.getText().toString());
        note.set_loc_exam(locExam.getText().toString());
        note.set_classification(classification.getText().toString());
        note.set_diagnosis(diagnosis.getText().toString());
        note.set_treatment(treatment.getText().toString());
        //note.set_procedure(procedure.toString());
        note.set_implant(implant.getText().toString());
        note.set_score(score.getText().toString());
        note.set_remark(remark.getText().toString());
        //note.set_version(dbHandler.getCurrentVersion(pid)+1);
        note.set_id(pid);

        dbHandler.addNotes(note);
        finish();





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
}

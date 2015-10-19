package activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.sajid.myapplication.DatabaseHandler;
import objects.Patient;
import com.example.sajid.myapplication.R;
import objects.exam_obj;
import objects.history_obj;
import objects.notes_obj;


public class history_view extends ActionBarActivity {
    int pid ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_view);
        Intent intent = getIntent();
        pid = intent.getIntExtra("id",0);
        this.getAndDisplayNotes();
    }

    public void getAndDisplayNotes()
    {
        DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
        history_obj historyObj = new history_obj();
        historyObj = dbHandler.getVersionedHistNote(pid);
        //exam_obj examObj = dbHandler.getLatestExamNote(pid);

        notes_obj notesObj  = new notes_obj();

        notesObj.set_date(historyObj.get_date());
        notesObj.set_past_hist(historyObj.get_past_illness());
        notesObj.set_hist_present_illness(historyObj.get_present_illness());
        notesObj.set_family_hist(historyObj.get_family_hist());
        notesObj.set_personal_hist(historyObj.get_personal_hist());
        //notesObj.set_gen_exam(examObj.get_gen_exam());
       // notesObj.set_loc_exam(examObj.get_local_exam());
        getLatestVersionTitle();
        int version = dbHandler.getCurrentVersion(pid);
        if (version>=1)
        this.displayNote(notesObj);
       else
        {
            Intent intent =  new Intent(this, Add_view_notes.class);

            intent.putExtra("id",pid);
            startActivity(intent);
        }






    }


    public void startAddNewNote()
    {
        Intent curIntent = getIntent();
        Intent intent =  new Intent(this, history_view.class);
        int pid = curIntent.getIntExtra("id",0);
        intent.putExtra("id",pid);
        startActivity(intent);
    }
    public void displayNote(notes_obj notesObj)
    {
        TextView presentIll = (TextView)findViewById(R.id.Hist_presentIll_view);
        TextView pastHist = (TextView)findViewById(R.id.Hist_past_view);
        TextView personalHist = (TextView)findViewById(R.id.Hist_personal_view);
        TextView familyHist = (TextView)findViewById(R.id.Hist_family_view);

        TextView genExam = (TextView)findViewById(R.id.Exam_general_view);
        TextView locExam = (TextView)findViewById(R.id.Exam_local_view);


        presentIll.setText(notesObj.get_hist_present_illness());
        pastHist.setText(notesObj.get_past_hist());
        personalHist.setText(notesObj.get_personal_hist());
        familyHist.setText(notesObj.get_family_hist());
        genExam.setText(notesObj.get_gen_exam());
        locExam.setText(notesObj.get_loc_exam());



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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_history_view, menu);
        return true;
    }

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
}

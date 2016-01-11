package activity;
// DISPLAYS ADD BUTTON WHICH REDIRECTS TO HISTORY ACTIVITY
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.sajid.myapplication.DatabaseHandler;
import objects.Patient;
import com.example.sajid.myapplication.R;
import objects.notes_obj;


public class Add_view_notes extends ActionBarActivity {
    int pid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_view_notes);
        Intent intent = getIntent();
        pid = intent.getIntExtra("id",0);
        this.getLatestVersionTitle();

    }
    public void getLatestVersionTitle()
    {
        DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());

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

    public void startAddNotes(View view)
    {

        Intent intent =  new Intent(this, AddClinicalNotesActivity.class);

        intent.putExtra("id",pid);
        startActivity(intent);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_view_notes, menu);
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

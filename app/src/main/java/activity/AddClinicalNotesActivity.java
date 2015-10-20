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

        Intent intent = getIntent();
        pid = intent.getIntExtra("id",0);

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

                finish();
            case R.id.ViewHistory:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

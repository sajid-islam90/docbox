package activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.sajid.myapplication.*;
import com.example.sajid.myapplication.DatabaseHandler;
import com.example.sajid.myapplication.R;
import com.example.sajid.myapplication.UserProfile;

import objects.*;
import objects.notes_obj;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class AddClinicalNotesActivity extends ActionBarActivity {

    private static final int REQUEST_CODE = 200;
    int pid;
    history_obj historyObj = new history_obj();
    exam_obj examObj = new exam_obj();
    treatment_obj treatmentOb= new treatment_obj();
    ArrayList<other_obj> otherObj = new ArrayList<>();
    private String[] navigationDrawerOptions;
    private ListView mDrawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clinical_notes);

        Intent intent = getIntent();
        pid = intent.getIntExtra("id",0);
        navigationDrawerOptions = new String[4];
        navigationDrawerOptions[0] = "History";
        navigationDrawerOptions[1] = "Clinical Examination/Pre-Op";
        navigationDrawerOptions[2] = "Treatment/Post-Op";
        navigationDrawerOptions[3] = "Others";
        mDrawerList = (ListView) findViewById(R.id.clinicalItems);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, navigationDrawerOptions) ;

        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                switch (position)
                {
                    case 0:


                        Intent child = new Intent(AddClinicalNotesActivity.this,History_Activity.class);
                        child.putExtra("id",pid);
                        child.putExtra("history_obj", historyObj);
                        startActivityForResult(child, REQUEST_CODE);
                        break;


                        //startActivity(new Intent(AddClinicalNotesActivity.this, History_Activity.class));

                    case 1:


                        Intent child1 = new Intent(AddClinicalNotesActivity.this,Exam_Activity.class);
                        child1.putExtra("id",pid);
                        child1.putExtra("exam_obj",examObj);
                        startActivityForResult(child1, REQUEST_CODE);break;



                    case 2:


                        Intent child2 = new Intent(AddClinicalNotesActivity.this,Treatment_Activity.class);
                        child2.putExtra("id",pid);
                        child2.putExtra("treat_obj",treatmentOb);
                        startActivityForResult(child2, REQUEST_CODE);
                        break;



                    case 3:


                        Intent child3 = new Intent(AddClinicalNotesActivity.this,Other_Notes_Activity.class);
                        child3.putExtra("id",pid);
                        child3.putExtra("other_obj",otherObj);
                        startActivityForResult(child3, REQUEST_CODE);
                        break;

                }
            }
        });


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_clinical_notes, menu);
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
            case R.id.action_save:

                DatabaseHandler databaseHandler = new DatabaseHandler(AddClinicalNotesActivity.this);
                try {
                    if (historyObj._date!=null)
                        databaseHandler.addHistory(historyObj);
                    if (examObj._date!=null)
                        databaseHandler.addExam(examObj);
                    if (treatmentOb._date!=null)
                        databaseHandler.addTreatment(treatmentOb);
                    if(Other_Notes_Activity.otherObj!=null && Other_Notes_Activity.otherObj.size()>0)
                    {
                        for(int i =0;i<Other_Notes_Activity.otherObj.size();i++)
                        {
                            databaseHandler.addOther(Other_Notes_Activity.otherObj.get(i));
                        }
                        Other_Notes_Activity.otherObj.clear();
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            if ((data.getStringExtra("activity").equals("history")) && (requestCode == REQUEST_CODE)) {

                try {
                    historyObj = data.getExtras().getParcelable("history_obj");
                } catch (Exception e)

                {
                    e.printStackTrace();
                }

            } else if ((data.getStringExtra("activity").equals("exam")) && (requestCode == REQUEST_CODE)) {
                examObj = data.getExtras().getParcelable("exam_obj");
            }
            else if ((data.getStringExtra("activity").equals("treatment")) && (requestCode == REQUEST_CODE)) {
                treatmentOb = data.getExtras().getParcelable("treat_obj");
            }
            else if ((data.getStringExtra("activity").equals("other")) && (requestCode == REQUEST_CODE)) {
                otherObj = data.getExtras().getParcelable("other_obj");
            }
        }
    }

}

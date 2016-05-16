package activity;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import utilityClasses.DatabaseHandler;
import com.elune.sajid.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import adapters.InputAgainstAFieldAdapter;
import objects.Item;

public class genericNotesActivity extends AppCompatActivity {
    private int pid;
    private static ArrayList<Item> listOfItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generic_notes);
        pid = getIntent().getIntExtra("id",0);
        doWork();
    }

    private void doWork() {
        final DatabaseHandler databaseHandler = new DatabaseHandler(genericNotesActivity.this);
        listOfItems = databaseHandler.getGenericNote(pid,1);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        Resources resource = getResources();
        int resId = resource.getIdentifier("post_op", "array", getPackageName());
        String[] fields = resource.getStringArray(resId);
        if(listOfItems.size()<1)
        for(int i = 0;i<fields.length;i++) {
            Item item = new Item();
            item.setTitle(fields[i]);
            item.setDiagnosis("");
            item.setPatient_id(pid);
            item.setSection(1);//1--> pre op section
            item.setDate(formattedDate);
            listOfItems.add(item);
        }

        ListView listView = (ListView)findViewById(R.id.filedsList);
        InputAgainstAFieldAdapter inputAgainstAFieldAdapter = new InputAgainstAFieldAdapter(genericNotesActivity.this,listOfItems);
listView.setItemsCanFocus(true);
        listView.setAdapter(inputAgainstAFieldAdapter);
      /*  Button button = (Button)findViewById(R.id.saveButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseHandler.saveGenericNote(listOfItems);


            }
        });*/



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_generic_notes, menu);
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

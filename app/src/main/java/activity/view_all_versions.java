package activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.sajid.myapplication.DatabaseHandler;
import com.example.sajid.myapplication.R;

import java.util.ArrayList;

import objects.Item;

public class view_all_versions extends AppCompatActivity {
    int pid;
    ArrayList<String> dates = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_versions);
        Intent intent = getIntent();
        pid = intent.getIntExtra("id",0);
       //getSupportActionBar().setHomeAsUpIndicator();
        displayAllVersionDates();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_all_versions, menu);
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


    public void displayAllVersionDates()
    {
        DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
        ArrayList<String> dates = databaseHandler.getAllNotesDates(pid);
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dates);
        ListView listView = (ListView)findViewById(R.id.allVersionDates);
        listView.setAdapter(itemsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {

                Intent  intent;

                intent = new Intent(getApplicationContext(), TabbedActivityCheck.class);
                intent.putExtra("id",pid);
                intent.putExtra("version",position+1);
                intent.putExtra("parent",view_all_versions.class.toString());
                startActivity(intent);
                // String value = (String) adapter.getItemAtPosition(position);
                // assuming string and if you want to get the value on click of list item
                // do what you intend to do on click of listview row
            }
        });
    }
}

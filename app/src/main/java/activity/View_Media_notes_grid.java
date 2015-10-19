package activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Switch;

import adapters.DocumentsAdapter;
import objects.Item;
import com.example.sajid.myapplication.R;
import com.example.sajid.myapplication.utility;

import java.util.ArrayList;


public class View_Media_notes_grid extends ActionBarActivity {
    int pid;
    ArrayList<Item> media = new ArrayList<>();
    int version ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__media_notes_grid);
        Intent intent = getIntent();
        pid = intent.getIntExtra("id",0);
        int section = intent.getIntExtra("section",0);
        String title = null;
        version = intent.getIntExtra("version", 1);
        media = utility.getMediaList(pid, this, section, version);
        switch (section)
        {
            case 1:title = "History Media";break;
            case 2:title = "Pre-Op Media";break;
            case 3:title = "Post-Op Media";break;
            case 4:title = "Other Media";break;
        }
        this.setTitle(title);

        this.displayAddedMedia(media);
    }

    public void displayAddedMedia(ArrayList<Item> fieldList)
    {
        GridView listView = (GridView)findViewById(R.id.gridView2);
        DocumentsAdapter docAdapter = new DocumentsAdapter(this,View_Media_notes_grid.this,fieldList);
        listView.setAdapter(docAdapter);

    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view__media_notes_grid, menu);

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

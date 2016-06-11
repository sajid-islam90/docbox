package activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import utilityClasses.DatabaseHandler;
import utilityClasses.PhotoHelper;
import com.elune.sajid.myapplication.R;

import java.util.ArrayList;

import adapters.InputAgainstAFieldAdapter;
import adapters.recyclerAdapter;
import objects.Item;
import objects.media_obj;
import objects.other_obj;

public class ViewFollowUp_Activity extends AppCompatActivity {
    private static final int TWO_TEXT_FIELDS = 1;
    private static final int ONE_PHOTO = 2;
    int pid;
    int version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_follow_up_);
        Intent intent = getIntent();
        pid = intent.getIntExtra("id",0);
        version = intent.getIntExtra("version",0);
        setTitle("Follow Up #"+version);
        TextView textView10 =(TextView)findViewById(R.id.textViewFollowUpNotesView);
        TextView textView6 =(TextView)findViewById(R.id.textViewFollowupOtherView);
        TextView textView7 =(TextView)findViewById(R.id.textViewFollowupMediaView);
        final ListView listView = (ListView)findViewById(R.id.fieldsListFollowUpView);
        final RecyclerView listView2 = (RecyclerView)findViewById(R.id.listViewOtherHistView);
        final RecyclerView listView3 = (RecyclerView)findViewById(R.id.listViewMediaView);
        //final CardView cardView = (CardView)findViewById(R.id.view13);
        // listView2.setVisibility(View.GONE);
        //  listView3.setVisibility(View.GONE);
        assert textView10 != null;
        textView10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                assert listView != null;
                listView.setVisibility(View.VISIBLE);
                assert listView2 != null;
                listView2.setVisibility(View.GONE);
                assert listView3 != null;
                listView3.setVisibility(View.GONE);
                // expandView(cardView);
            }
        });
        textView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listView2.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                listView3.setVisibility(View.GONE);
                //CollapseView(cardView);
            }
        });
        textView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listView3.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                listView2.setVisibility(View.GONE);
                //CollapseView(cardView);
            }
        });
doWork();
    }


    public void doWork()
    {
        DatabaseHandler databaseHandler = new DatabaseHandler(ViewFollowUp_Activity.this);
        ArrayList<Item> FollowUpFields = databaseHandler.getFollowUp(pid, version);
        ListView listView1 = (ListView)findViewById(R.id.fieldsListFollowUpView);
        InputAgainstAFieldAdapter inputAgainstAFieldAdapter = new InputAgainstAFieldAdapter(this,FollowUpFields);
        if (listView1 != null) {
            listView1.setAdapter(inputAgainstAFieldAdapter);
        }
        listView1.setItemsCanFocus(false);
        ArrayList<Item> field = new ArrayList<>();
        ArrayList<Item> media = new ArrayList<>();
        media_obj[] mediaObjs = databaseHandler.getMediaFollowUp(pid,version);
        ArrayList<other_obj> OtherObjs =   databaseHandler.getOtherFollowUp(pid, version);
        if (OtherObjs.size()>0)
        {

            Item item;
            for (int i = 0; i < OtherObjs.size(); i++) {
                item = new Item();
                item.setTitle(OtherObjs.get(i).get_field_name());
                item.setDiagnosis(OtherObjs.get(i).get_field_value());
                field.add(item);

                //field.add(otherObj[i].get_field_name());
            }

        }
        if (mediaObjs != null)
        { Item item;
            for (int i = 0; i < mediaObjs.length; i++) {
                item = new Item();
                // mediaObjs[i]=PhotoHelper.addMissingBmp(mediaObjs[i],CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);

            if(mediaObjs[i].get_bmp()==null)
                    if(mediaObjs[i].get_media_path().contains(".mp4"))
                    {
                        mediaObjs[i] = PhotoHelper.addMissingBmp(mediaObjs[i], 200);
                    }
                else
                    {
                        mediaObjs[i] = PhotoHelper.addMissingBmp(mediaObjs[i], 300);
                    }

                item.setBmp(BitmapFactory.decodeByteArray(mediaObjs[i].get_bmp(), 0, mediaObjs[i].get_bmp().length));

                item.setDiagnosis(mediaObjs[i].get_media_path());
                item.setPatient_id(mediaObjs[i].get_pid());
                //item.setDiagnosis(mediaObjs[i].get_field_value());
                media.add(item);
                //field.add(otherObj[i].get_field_name());
            }
        }
        displayAddedField(field);
        displayAddedMedia(media);

    }


    public void displayAddedField(ArrayList<Item> fieldList)
    {
        TextView textView = (TextView)findViewById(R.id.ViewFollowupNumberOfOtherNotes);
        textView.setText(String.valueOf(fieldList.size()));
        RecyclerView listView = (RecyclerView)findViewById(R.id.listViewOtherHistView);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerAdapter RecyclerAdapter = new recyclerAdapter(this,ViewFollowUp_Activity.this,fieldList,TWO_TEXT_FIELDS,pid,version);

        listView.setLayoutManager(layoutManager);
        listView.setAdapter(RecyclerAdapter);
    }
    public void displayAddedMedia(ArrayList<Item> fieldList)
    {
        TextView textView = (TextView)findViewById(R.id.ViewFollowupNumberOfMedia);
        textView.setText(String.valueOf(fieldList.size()));
        RecyclerView listView = (RecyclerView)findViewById(R.id.listViewMediaView);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(ViewFollowUp_Activity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerAdapter RecyclerAdapter = new recyclerAdapter(this,ViewFollowUp_Activity.this,fieldList,ONE_PHOTO,pid,version);
        listView.setLayoutManager(layoutManager);
        listView.setAdapter(RecyclerAdapter);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_follow_up_, menu);
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

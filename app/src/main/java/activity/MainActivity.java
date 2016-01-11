package activity;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sajid.myapplication.DatabaseHandler;

import adapters.DrawerListAdapter;
import cz.msebera.android.httpclient.client.cache.Resource;
import objects.*;
import adapters.MyAdapter;
import objects.Patient;

import com.example.sajid.myapplication.PhotoHelper;
import com.example.sajid.myapplication.R;
import com.example.sajid.myapplication.RoundImage;
import com.example.sajid.myapplication.UserProfile;
import com.example.sajid.myapplication.utility;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.example.sajid.myapplication.R.drawable.ic_action_person;


public class MainActivity extends ActionBarActivity {
    public final static String EXTRA_MESSAGE = "com.mycompany.myfirstapp.MESSAGE";
    private String profilePicPath;
    private SearchView mSearchView;
    private ActionBarDrawerToggle mDrawerToggle;
    private RelativeLayout mDrawerRelativeLayout;
    private static final int REQUEST_TAKE_PHOTO = 100;
    private CharSequence mDrawerTitle = "Drawer";
    private CharSequence mTitle = "Patient Manager";
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private TextView mEmail;
    RoundImage roundedImage;
    final Context context = this;
    private String[] navigationDrawerOptions;
    private TextView mStatusView;
    List<String> patientNames = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLiteDatabase myDataBase= openOrCreateDatabase("patientManager",MODE_PRIVATE,null);
        DatabaseHandler dbHandle = new DatabaseHandler(getApplicationContext());
        dbHandle.onCreate(myDataBase);
        Resources res = getResources();
        ArrayList<Item> items = new ArrayList<>();

        Item item =new Item();
        item.setTitle("View Profile");
        item.setBmp(BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_person));
        items.add(item);
        item =new Item();
        item.setTitle("Payment Info");
        item.setBmp(BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_pay));
        items.add(item);
        item =new Item();
        item.setTitle("Exit");
        item.setBmp(BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_exit));
        items.add(item);

        navigationDrawerOptions = new String[3];
        navigationDrawerOptions[0] = "View Profile";
        navigationDrawerOptions[1] = "Payment Info";
        navigationDrawerOptions[2] = "Exit";
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerRelativeLayout = (RelativeLayout) findViewById(R.id.left_drawer_1);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        ArrayAdapter adapter = new ArrayAdapter(this,R.layout.drawer_list_item, navigationDrawerOptions) ;
        DrawerListAdapter drawerListAdapter = new DrawerListAdapter(MainActivity.this,items);
        mDrawerList.setAdapter(drawerListAdapter);
        /*mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                switch (position)
                {
                    case 0:
                        startActivity(new Intent(MainActivity.this, UserProfile.class));
                }
            }
        });*/




        personal_obj personalObj =  dbHandle.getPersonalInfo();
        ImageView imageView = (ImageView)findViewById(R.id.profilePic);
        Bitmap bmp = null;
        bmp = BitmapFactory.decodeFile(personalObj.get_photoPath());


        if(bmp!=null)
        {bmp = PhotoHelper.getResizedBitmap(bmp, 200, 200);
            roundedImage = new RoundImage(bmp);
       // Bitmap bmpImage = BitmapFactory.decodeByteArray(image, 0, image.length);

       }
        else {

           bmp = BitmapFactory.decodeResource(getResources(),R.drawable.add_new_photo);
            roundedImage = new RoundImage(bmp);
            imageView.setBackgroundResource(R.drawable.add_new_photo);
        }
        imageView.setImageDrawable(roundedImage);
        mEmail = (TextView)findViewById(R.id.drawer_email);
        mEmail.setText(personalObj.get_email());




        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);


                getSupportActionBar().setTitle(R.string.app_name);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Drawer");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener

        mDrawerLayout.setDrawerListener(mDrawerToggle);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);

        getPatientList();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    //adding profile photo

    public void addProfilePhoto(View view) throws IOException
    {
        this.dispatchTakePictureIntent();}
    private void dispatchTakePictureIntent()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Intent intent = getIntent();
        //media_obj mediaObj = new media_obj();

        final DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());



        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = PhotoHelper.createImageFile(0);

            } catch (IOException ex) {
                // Error occurred while creating the File

            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra("output",
                        Uri.fromFile(photoFile));
                profilePicPath = photoFile.getPath();
                /*mediaObj.set_media_name(photoFile.getPath());
                mediaObj.set_media_path(photoFile.getPath());
*/



                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);



            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {



        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {

            DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
            databaseHandler.updatePersonalInfo("documentPath",profilePicPath);


            utility.recreateActivityCompat(MainActivity.this);



        }

    }









    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity_actions, menu);


        return super.onCreateOptionsMenu(menu);

    }

    public void displayPatientList(final ArrayList<Item> patientList)
    {
        ListView listView = (ListView)findViewById(R.id.listViewMain);
       /* MainActivityList adapter1 = new
                MainActivityList(MainActivity.this, patientList, imageId);*/

        MyAdapter adapter = new MyAdapter(MainActivity.this,patientList);

       // ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, patientList.toArray()) ;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DatabaseHandler dbHandle = new DatabaseHandler(MainActivity.this);
                Patient patient = new Patient();
                patient = dbHandle.getSearchPatient(position, patientList);
                Intent intent = new Intent(MainActivity.this, PatientProfileActivity.class);
                intent.putExtra("id", patient.get_id());
                context.startActivity(intent);
            }
        });
        listView.setAdapter(adapter);


    }

    public void refresh()

    {
        finish();
        startActivity(getIntent());



    }

    public void getPatientList()
    {
        SQLiteDatabase myDataBase= openOrCreateDatabase("patientManager",MODE_PRIVATE,null);
        DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
        dbHandler.onCreate(myDataBase);
        Bitmap image = null;
        String name = null;
        String diagnosis = null;
        int patient_id = 0;
        byte[] bmpImage ;
        String lastVisit;
        Item nameImage = new Item();
        ArrayList<Item> nameWithImage = new ArrayList<Item>();
        List<Patient>patientList = dbHandler.getAllPatient();
       // List<String> patientNames = new ArrayList<String>();

        Collections.sort(patientNames, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });
        if(patientList.size() !=0) {
            for (int i = 0; i < patientList.size(); i++) {
                name = patientList.get(i).get_name();
                bmpImage = patientList.get(i).get_bmp();
                diagnosis = patientList.get(i).get_diagnosis();
                patient_id = patientList.get(i).get_id();
                lastVisit = patientList.get(i).get_last_seen_date();


                if(bmpImage != null) {
/*if(patientList.get(i).get_photoPath()!=null) {
    File file = new File(patientList.get(i).get_photoPath());
    if (file.exists()) {
        image = BitmapFactory.decodeFile(patientList.get(i).get_photoPath());
       // image = PhotoHelper.getResizedBitmap(image,150,150);
    }
    else
        image = BitmapFactory.decodeByteArray(bmpImage, 0, bmpImage.length);
}
                    else*/
    image = BitmapFactory.decodeByteArray(bmpImage, 0, bmpImage.length);
                }

                nameImage.setTitle(name);

                nameImage.setBmp(image);
                nameImage.setDiagnosis(diagnosis);
                nameImage.setDate(lastVisit);
                nameWithImage.add(i , nameImage);
                nameImage.setPatient_id(patient_id);
                nameImage = new Item();
                patientNames.add(i, name);
            }

            displayPatientList(nameWithImage);
        }
    }

    void searchPatient(String searchString)
    {

        DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());

        Bitmap image = null;
        String name = null;
        String diagnosis = null;
        int patient_id = 0;
        byte[] bmpImage ;
        Item nameImage = new Item();
        ArrayList<Item> nameWithImage = new ArrayList<Item>();
        List<Patient>patientList = dbHandler.search(searchString);
        List<String> patientNames = new ArrayList<String>();
        if(patientList.size() !=0) {
            for (int i = 0; i < patientList.size(); i++) {
                name = patientList.get(i).get_name();
                bmpImage = patientList.get(i).get_bmp();
                diagnosis = patientList.get(i).get_diagnosis();
                patient_id = patientList.get(i).get_id();
                if (bmpImage != null)
                    image = BitmapFactory.decodeByteArray(bmpImage, 0, bmpImage.length);

                nameImage.setTitle(name);
                nameImage.setBmp(image);
                nameImage.setDiagnosis(diagnosis);
                nameImage.setPatient_id(patient_id);
                nameWithImage.add(i, nameImage);
                nameImage = new Item();
                patientNames.add(i, name);
            }
            displayPatientList(nameWithImage);
        }

    }

    private void setupSearchView(MenuItem searchItem) {

        if (isAlwaysExpanded()) {
            mSearchView.setIconifiedByDefault(false);
        } else {
            searchItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM
                    | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        }

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            List<SearchableInfo> searchables = searchManager.getSearchablesInGlobalSearch();

            SearchableInfo info = searchManager.getSearchableInfo(getComponentName());
            for (SearchableInfo inf : searchables) {
                if (inf.getSuggestAuthority() != null
                        && inf.getSuggestAuthority().startsWith("applications")) {
                    info = inf;
                }
            }
            mSearchView.setSearchableInfo(info);
        }

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    public boolean onQueryTextChange(String newText) {
        mStatusView.setText("Query = " + newText);
        return false;
    }

    public boolean onQueryTextSubmit(String query) {
        mStatusView.setText("Query = " + query + " : submitted");
        return false;
    }

    public boolean onClose() {
        mStatusView.setText("Closed!");
        return false;
    }

    protected boolean isAlwaysExpanded() {
        return false;
    }




    /** Called when the user clicks the Send button */
   /* public void sendMessage(View view  ) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String string = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE,string);
        startActivity(intent);
        // Do something in response to button
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id)
        {
            case R.id.action_add:
                Toast.makeText(this, "Add selected", Toast.LENGTH_SHORT)
                        .show();

                Intent intent = new Intent(this,DisplayMessageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

                return true;
            case R.id.action_search:


                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.search_prompt, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);
                DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
                List<String> Names = null;
               // Names = dbHandler.getAllPatientNames();
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1, patientNames);
                final AutoCompleteTextView userInput = (AutoCompleteTextView) promptsView
                        .findViewById(R.id.editTextDialogUserInput);
                userInput.setThreshold(1);
                userInput.setAdapter(adapter);


                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // get user input and set it to result
                                        // edit text
                                        searchPatient(userInput.getText().toString());



                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();






                return true;
            case R.id.action_settings:
                Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT)
                        .show();

            case R.id.action_sync:
               Intent intent2 = new Intent(this, data_sync_activity.class);
                startActivity(intent2);

               /*  Intent i = new Intent(this,NavigationDrawer.class);
                startActivity(i);*/
                return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private static final String[] COUNTRIES = new String[] {
            "foo@example.com", "bar@example.com","sajid.islam90@gmail.com"
    };
}

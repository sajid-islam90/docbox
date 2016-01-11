package activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.example.sajid.myapplication.DatabaseHandler;

import objects.DataBaseEnums;
import objects.Item;
import objects.Patient;

import com.example.sajid.myapplication.PhotoHelper;
import com.example.sajid.myapplication.R;
import com.example.sajid.myapplication.utility;

import adapters.*;
import objects.history_obj;
import objects.media_obj;
import objects.other_obj;


public class TabbedActivityCheck extends ActionBarActivity implements ActionBar.TabListener {
    static int pid;
    static  int version;
    static  String parent;
    private static history_obj historyObj = null;
    private static media_obj mediaObj = new media_obj() ;
    private static Uri fileUri;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int TWO_TEXT_FIELDS = 1;
    private static final int ONE_PHOTO = 2;
    private static int Tabselected = 0;
   private static  ArrayList<other_obj> otherObj = new ArrayList<>();
    private static  ArrayList<ArrayList<other_obj>> OtherObjs = new ArrayList<ArrayList<other_obj>>();
    private static  ArrayList<ArrayList<other_obj>> OtherObjsStatic = new ArrayList<ArrayList<other_obj>>(3);
   public static ArrayList<ArrayList<Item>> NotesFields = new ArrayList<ArrayList<Item>>();
    public static ArrayList<ArrayList<Item>> NotesFieldsStatic = new ArrayList<ArrayList<Item>>(3);


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed_activity_check);
        Intent intent = getIntent();
        pid = intent.getIntExtra("id", 0);
        version = intent.getIntExtra("version", 1);
        parent  =intent.getStringExtra("parent");
DatabaseHandler databaseHandler =new DatabaseHandler(TabbedActivityCheck.this);
if(OtherObjsStatic.size()<3)
{
    for(int c=1;c<=3;c++){
    ArrayList<other_obj> otherObj1 = new ArrayList<>();
    OtherObjsStatic.add(otherObj1);}
}
        if(NotesFieldsStatic.size()<3)
        {
            for(int c=1;c<=3;c++){
                ArrayList<Item> item = new ArrayList<>();
                NotesFieldsStatic.add(item);}
        }
            NotesFields = new ArrayList<ArrayList<Item>>(3);
        OtherObjs = new ArrayList<ArrayList<other_obj>>(3);
        for(int y = 0;y<3;y++) {
            ArrayList<Item> field = databaseHandler.getGenericNote(pid, y);
            ArrayList<other_obj> other = databaseHandler.geOtherNote(pid, y);
            NotesFields.add(field);
            OtherObjs.add(other);
        }
                getLatestVersionTitle();
       // this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))

                            .setTabListener(this));
        }
        mViewPager.setCurrentItem(Tabselected);
    }

    public  void showHistMedia(View view)
    {


    }

    public void getLatestVersionTitle()
    {
        DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
       // notes_obj notesObj  = dbHandler.getLatestNote(pid);
        Patient patient = dbHandler.getPatient(pid);
        String title = null;
       history_obj historyObj =   dbHandler.getVersionedHistNote(pid,version);

        if
                (historyObj._date==null)
        {
           if( dbHandler.getVersionedNote(pid,version, DataBaseEnums.TABLE_EXAM)==null)
           {
               if( dbHandler.getVersionedNote(pid,version, DataBaseEnums.TABLE_TREATMENT)==null)
               {
                  title = dbHandler.getVersionedNote(pid,version, DataBaseEnums.TABLE_OTHER);
               }
               else
               {
                   title =  dbHandler.getVersionedNote(pid,version, DataBaseEnums.TABLE_TREATMENT);
               }
           }
            else
           {
               title =  dbHandler.getVersionedNote(pid,version, DataBaseEnums.TABLE_EXAM);
           }
        }
        else
        {
            title=  dbHandler.getVersionedNote(pid,version, DataBaseEnums.TABLE_HISTORY);
        }

       // int version = dbHandler.getCurrentVersion(pid);
        if (version >0) {
            setTitle(title);

            //displayNote(notesObj);
        }
        else
        {
            setTitle(patient.get_name() + "'s Notes ");
            // setContentView(R.layout.activity_clinical_notes_empty);
        }

    }



    public void displayAddedField(ArrayList<Item> fieldList,View view)
    {

        RecyclerView listView = (RecyclerView)view.findViewById(R.id.listViewOtherHist);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerAdapter RecyclerAdapter = new recyclerAdapter(TabbedActivityCheck.this,TabbedActivityCheck.this,fieldList,TWO_TEXT_FIELDS,pid,version);

        listView.setLayoutManager(layoutManager);
        listView.setAdapter(RecyclerAdapter);
    }

    private void dispatchTakeVideoIntent() throws IOException {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        //fileUri = PhotoHelper.createVideoFile(pid);
        if (intent.resolveActivity(TabbedActivityCheck.this.getPackageManager()) != null) {
            // Create the File where the photo should go
            File file = null;
            try {
                file = PhotoHelper.createVideoFile(pid, TabbedActivityCheck.this);
                fileUri= Uri.fromFile(file);

            } catch (IOException ex) {
                // Error occurred while creating the File

            }
        }
        if (fileUri != null) {
            // create a file to save the video
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);  // set the image file name

            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0); // set the video image quality to high

            // start the Video Capture Intent
            startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
        }
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Intent intent =getIntent();


        final DatabaseHandler dbHandler = new DatabaseHandler(TabbedActivityCheck.this);



        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(TabbedActivityCheck.this.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = PhotoHelper.createImageFileForNotes(pid,TabbedActivityCheck.this);

            } catch (IOException ex) {
                // Error occurred while creating the File

            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra("output",
                        Uri.fromFile(photoFile));
                mediaObj.set_media_name(photoFile.getPath());
                mediaObj.set_media_path(photoFile.getPath());




                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);



            }
        }
    }
   /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri videoUri = data.getData();
            String path = videoUri.getPath();

            DatabaseHandler databaseHandler = new DatabaseHandler(TabbedActivityCheck.this);
            mediaObj.set_media_path(path);
            mediaObj = PhotoHelper.addMissingBmp(mediaObj,CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);

            mediaObj.set_pid(pid);
            mediaObj.set_section((int)CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE/100);
            mediaObj.set_version( 1);
            databaseHandler.addMedia(mediaObj);

            Intent intent = getIntent();

            finish();
            startActivity(intent);
        }

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {

            DatabaseHandler databaseHandler = new DatabaseHandler(TabbedActivityCheck.this);

            mediaObj = PhotoHelper.addMissingBmp(mediaObj,REQUEST_TAKE_PHOTO);
            Bitmap bitmap;
            bitmap = BitmapFactory.decodeFile(mediaObj.get_media_path());
            ByteArrayOutputStream ostream = new ByteArrayOutputStream();

            // save image into gallery
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, ostream);

            try {
                FileOutputStream fout = new FileOutputStream(new File(mediaObj.get_media_path()));
                fout.write(ostream.toByteArray());
                fout.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            mediaObj.set_pid(pid);
            mediaObj.set_section(REQUEST_TAKE_PHOTO);
            mediaObj.set_version(1);
            databaseHandler.addMedia(mediaObj);

            Intent intent = getIntent();

           finish();
            startActivity(intent);



        }

    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(parent.equals(PatientProfileActivity.class.toString()) )
        getMenuInflater().inflate(R.menu.menu_tabbed_activity_check, menu);
        else
            getMenuInflater().inflate(R.menu.empty_menu, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_notes) {
            this.startAddNotes();


            return true;
        }
        else if(id == R.id.action_view_previous_versions)
        {
            Intent intent = new Intent(this,view_all_versions.class);
            intent.putExtra("id",pid);
            startActivity(intent);
        }
        else if(id == R.id.action_save)
        {
           Fragment fragment =  mSectionsPagerAdapter.fragment; //(PlaceholderFragment) getSupportFragmentManager().findFragmentById(R.id.pager);// mSectionsPagerAdapter.fragment;
            //ArrayList<other_obj> otherObj = fragment.
           DatabaseHandler databaseHandler =new DatabaseHandler(getApplicationContext());
            for(int y = 0;y<3;y++){
            for(int i = 1;i<=OtherObjsStatic.get(y).size();i++){
                databaseHandler.addOther(OtherObjsStatic.get(y).get(i-1));
            }}
            for(int i = 0 ;i<NotesFields.size();i++)
            {
                databaseHandler.saveGenericNote(NotesFields.get(i));
            }
            Patient patient = databaseHandler.getPatient(pid);
            databaseHandler.updatePatient(patient,0);
            historyObj = null;
            otherObj = null;
            OtherObjsStatic = null;
            OtherObjsStatic=  new ArrayList<ArrayList<other_obj>>(3);
            otherObj = new ArrayList<other_obj>();
            NotesFields =  null;
            finish();


        }

        return super.onOptionsItemSelected(item);
    }
    public void startAddNotes()
    {
        Intent curIntent = getIntent();
        DatabaseHandler databaseHandler = new DatabaseHandler(TabbedActivityCheck.this);
        int version  =  databaseHandler.getMaxFollowupVersion(pid);
        Intent intent =  new Intent(this, followUp.class);
        int pid = curIntent.getIntExtra("id",0);
        intent.putExtra("version",version+1);
        intent.putExtra("parent",PatientProfileActivity.class.toString());
        intent.putExtra("id",pid);
        startActivity(intent);
    }


    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        Fragment fragment;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {


            return PlaceholderFragment1.newInstance1(position );
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
                case 3:
                    return getString(R.string.title_section4).toUpperCase(l);
            }
            return null;
        }


    }



    /**
     * A placeholder fragment containing history view.
     */

    public static class PlaceholderFragment1 extends Fragment implements View.OnClickListener    {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static ArrayList<Item> media = new ArrayList<>();

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment1 newInstance1(int sectionNumber) {
            PlaceholderFragment1 fragment1 = new PlaceholderFragment1();
            Bundle args = new Bundle();
           // section = sectionNumber;
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment1.setArguments(args);
            return fragment1;
        }

        public PlaceholderFragment1() {
        }



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {



            View rootView = inflater.inflate(R.layout.activity_generic_notes, container, false);
            FloatingActionButton floatingActionButton1 = (FloatingActionButton)rootView.findViewById(R.id.view2);
            floatingActionButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        addVideo(v);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });
            FloatingActionButton floatingActionButton2 = (FloatingActionButton)rootView.findViewById(R.id.view3);
            floatingActionButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        addPhoto(v);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });

            FloatingActionButton floatingActionButton3 = (FloatingActionButton)rootView.findViewById(R.id.view4);
            floatingActionButton3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addHistField(v);

                }
            });

            doWork(rootView);
            return rootView;
        }
        private void doWork(View view) {


            DatabaseHandler databaseHandler = new DatabaseHandler(getContext());
            Bundle args = getArguments();
            int section = args.getInt(ARG_SECTION_NUMBER);
            media = utility.getMediaList(pid, getActivity(), section);
            final ArrayList<Item> listOfItems = NotesFields.get(section );
            ListView listView1 = (ListView)view.findViewById(R.id.filedsList);
            InputAgainstAFieldAdapter inputAgainstAFieldAdapter = new InputAgainstAFieldAdapter(getActivity(),listOfItems);
            ArrayList<Item> field = displayOtherNotes(section);
            TabbedActivityCheck tabbedActivityCheck = (TabbedActivityCheck)getActivity();
            tabbedActivityCheck.displayAddedField(field,view);

            if(databaseHandler.getLatestOtherNote(pid,section)!=null) {
                if ((databaseHandler.getLatestOtherNote(pid, section).length > 0) || (otherObj.size() > 0)) {


                }
            }
            listView1.setAdapter(inputAgainstAFieldAdapter);
            listView1.setItemsCanFocus(true);
           listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
               @Override
               public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                   Toast.makeText(getContext(), position + " " + id, Toast.LENGTH_SHORT);

               }
           });
            displayAddedMedia(media, view);




        }

        public void addOtherHistNote(String fieldName, String fieldValue)
        {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            String formattedDate = df.format(c.getTime());
            Bundle args = getArguments();
            int section = args.getInt(ARG_SECTION_NUMBER);
            DatabaseHandler dbHandler = new DatabaseHandler(getContext());

            other_obj temp = new other_obj();
            temp.set_version(section);
            temp.set_field_name(fieldName);
            temp.set_field_value(fieldValue);


            temp.set_pid(pid);
            temp.set_date(formattedDate);

            OtherObjsStatic.get(section).add(temp);

        }
        public ArrayList<Item> displayOtherNotes(int section)
        {
            ArrayList<Item> field = new ArrayList<>();

            DatabaseHandler databaseHandler = new DatabaseHandler(getContext());

            Item item;


            if (OtherObjs.get(section).size()>0)
            {

                for (int i = 0; i < OtherObjs.get(section).size(); i++) {
                    item = new Item();
                    item.setTitle(OtherObjs.get(section).get(i).get_field_name());
                    item.setDiagnosis(OtherObjs.get(section).get(i).get_field_value());
                    field.add(item);

                    //field.add(otherObj[i].get_field_name());
                }

            }
            if (OtherObjsStatic.size()>0) {
                if (OtherObjsStatic.get(section).size()>0) {
                    for (int i = 0; i < OtherObjsStatic.get(section).size(); i++) {
                        item = new Item();
                        item.setTitle(OtherObjsStatic.get(section ).get(i).get_field_name());
                        item.setDiagnosis(OtherObjsStatic.get(section ).get(i).get_field_value());
                        field.add(item);

                        //field.add(otherObj[i].get_field_name());
                    }
                }
            }

            return  field;
        }
        public  void addHistField(View promptsView)
        {
            LayoutInflater li = LayoutInflater.from(getContext());
            Bundle args = getArguments();
            final int section = args.getInt(ARG_SECTION_NUMBER);
            promptsView = li.inflate(R.layout.other_field_input_prompt, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    getContext());

            // set prompts.xml to alertdialog builder
            alertDialogBuilder.setView(promptsView);

            final EditText userInputName = (EditText) promptsView
                    .findViewById(R.id.editTextDialogUserInputFieldName);

            final EditText userInputValue = (EditText) promptsView
                    .findViewById(R.id.editTextDialogUserInputFieldValue);

            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    String field_Name = userInputName.getText().toString();
                                    String field_Value = userInputValue.getText().toString();
                                    addOtherHistNote(field_Name, field_Value);
                                    Intent intent = getActivity().getIntent();
Tabselected = section;
                                    getActivity().finish();
                                    startActivity(intent);
                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();



        }


        public void addVideo(View view) throws IOException {this.dispatchTakeVideoIntent();}
        public void addPhoto(View view) throws IOException {
           dispatchTakePictureIntent();

            // this.dispatchTakePictureIntent();
        }
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            Bundle args = getArguments();
            int section = args.getInt(ARG_SECTION_NUMBER);

            if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
                Uri videoUri = data.getData();
                String path = videoUri.getPath();

                DatabaseHandler databaseHandler = new DatabaseHandler(getContext());
                mediaObj.set_media_path(path);
                mediaObj = PhotoHelper.addMissingBmp(mediaObj,CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);

                mediaObj.set_pid(pid);
                mediaObj.set_section(section);
                mediaObj.set_version(1);
                databaseHandler.addMedia(mediaObj);

                Intent intent = getActivity().getIntent();
                Tabselected = section;
                getActivity().finish();
                startActivity(intent);
            }

            if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {

                DatabaseHandler databaseHandler = new DatabaseHandler(getContext());

                mediaObj = PhotoHelper.addMissingBmp(mediaObj,REQUEST_TAKE_PHOTO);
                Bitmap bitmap;
                bitmap = BitmapFactory.decodeFile(mediaObj.get_media_path());
                ByteArrayOutputStream ostream = new ByteArrayOutputStream();

                // save image into gallery
                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, ostream);

                try {
                    FileOutputStream fout = new FileOutputStream(new File(mediaObj.get_media_path()));
                    fout.write(ostream.toByteArray());
                    fout.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                mediaObj.set_pid(pid);
                mediaObj.set_section(section);
                mediaObj.set_version(1);
                databaseHandler.addMedia(mediaObj);

                Intent intent = getActivity().getIntent();
                Tabselected = section;
                getActivity().finish();
                startActivity(intent);



            }

        }
        private void dispatchTakePictureIntent() {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Intent intent = getActivity().getIntent();


            final DatabaseHandler dbHandler = new DatabaseHandler(getContext());



            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = PhotoHelper.createImageFileForNotes(pid,getContext());

                } catch (IOException ex) {
                    // Error occurred while creating the File

                }

                // Continue only if the File was successfully created
                if (photoFile != null) {
                    takePictureIntent.putExtra("output",
                            Uri.fromFile(photoFile));
                    mediaObj.set_media_name(photoFile.getPath());
                    mediaObj.set_media_path(photoFile.getPath());




                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);



                }
            }
        }
        private void dispatchTakeVideoIntent() throws IOException {
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

            //fileUri = PhotoHelper.createVideoFile(pid);
            if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                // Create the File where the photo should go
                File file = null;
                try {
                    file = PhotoHelper.createVideoFile(pid, getContext());
                    fileUri= Uri.fromFile(file);

                } catch (IOException ex) {
                    // Error occurred while creating the File

                }
            }
            if (fileUri != null) {
                // create a file to save the video
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);  // set the image file name

                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0); // set the video image quality to high

                // start the Video Capture Intent
                startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
            }
        }
        public void displayAddedMedia(ArrayList<Item> fieldList,View rootView)
        {
            RecyclerView listView = (RecyclerView)rootView.findViewById(R.id.listViewMedia);
            LinearLayoutManager layoutManager
                    = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            recyclerAdapter RecyclerAdapter = new recyclerAdapter(this.getActivity(),getContext(),fieldList,ONE_PHOTO,pid,version);
            listView.setLayoutManager(layoutManager);
            listView.setAdapter(RecyclerAdapter);


        }

        @Override
        public void onClick(View v) {
            int a = v.getId();
            View b = v.findViewById(R.id.view_media_exam);

            Intent intent = new Intent(v.getContext(),View_Media_notes_grid.class);
            intent.putExtra("id",pid);
            intent.putExtra("section",2);
            intent.putExtra("version",version);
            startActivity(intent);

            int c = b.getId();
            c= R.id.view_media_hist;
            Toast.makeText(this.getActivity().getApplicationContext(),"button pressed",Toast.LENGTH_SHORT).show();
        }

    }







}

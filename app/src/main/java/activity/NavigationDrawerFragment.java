package activity;

import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.elune.sajid.myapplication.R;

import java.util.ArrayList;

import adapters.DrawerListAdapter;
import objects.Item;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class NavigationDrawerFragment extends Fragment {

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private View mFragmentContainerView;
    String accountType;
    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;

    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        // Select either the default item (0) or the last selected item.
        selectItem(mCurrentSelectedPosition);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View mView = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
RelativeLayout relativeLayout = (RelativeLayout)mView.findViewById(R.id.profile_pic_layout);

        accountType  = prefs.getString(getActivity().getString(R.string.account_type), "");
        //mDrawerListView = (ListView) inflater.inflate( R.layout.fragment_navigation_drawer, container, false);
        mDrawerListView = (ListView) mView.findViewById(R.id.navDrawerListView);
        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                mDrawerLayout.closeDrawer(mView.findViewById(R.id.navigation_drawer));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        selectItem(position);
                    }
                }, 300);

            }
        });
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(mView.findViewById(R.id.navigation_drawer));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        selectItem(2);
                    }
                }, 300);




            }
        });
        ArrayList<Item> items = new ArrayList<>();

        Item item =new Item();
        item.setTitle(getString(R.string.nav_drawer_item1));
        item.setBmp(BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_home));
        items.add(item);
        item =new Item();
        item.setTitle(getString(R.string.nav_drawer_item2));
        item.setBmp(BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_calender));
        items.add(item);
        item =new Item();
        item.setTitle(getString(R.string.nav_drawer_item3));
        item.setBmp(BitmapFactory.decodeResource(getResources(), R.drawable.ic_user_profile));
        items.add(item);
        item =new Item();
        item.setTitle(getString(R.string.nav_drawer_item4));
        item.setBmp(BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_schedule));
        items.add(item);
        item =new Item();
        item.setTitle(getString(R.string.nav_drawer_item5));
        item.setBmp(BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_pay));
        items.add(item);

        if(accountType.equals(getActivity().getString(R.string.account_type_doctor)))
        {  item =new Item();
            item.setTitle(getString(R.string.nav_drawer_item6));
        item.setBmp(BitmapFactory.decodeResource(getResources(), R.drawable.ic_nav_drawer_add_helper));
        items.add(item);}

        item =new Item();
        item.setTitle(getString(R.string.nav_drawer_item7));
        item.setBmp(BitmapFactory.decodeResource(getResources(), R.drawable.ic_nav_drawer_verify));
        items.add(item);
       DrawerListAdapter drawerListAdapter = new DrawerListAdapter(getActivity(),items);
//        mDrawerListView.setAdapter(drawerListAdapter);
       // mDrawerListView.bringToFront();
//        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(getActionBar().getThemedContext(),
//                R.layout.nav_drawer_list_item,
//                R.id.menuItemTextView,
//                new String[]{
//                        getString(R.string.nav_drawer_item1),
//                        getString(R.string.nav_drawer_item2),
//                        getString(R.string.nav_drawer_item3),
//
//                });


       mDrawerListView.setAdapter(drawerListAdapter);
       // mDrawerListView.setItemsCanFocus(true);
        mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
        return mView;
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.nav_drawer_ic);


        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                R.drawable.nav_drawer_ic,             /* nav drawer image to replace 'Up' caret */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }
                ActionBar actionBar = getActionBar();
                actionBar.setHomeAsUpIndicator(R.drawable.nav_drawer_ic);
                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }
                ActionBar actionBar = getActionBar();
                actionBar.setHomeAsUpIndicator(R.drawable.nav_drawer_close_ic);
                mDrawerLayout.requestLayout();
                mDrawerLayout.bringToFront();
                mDrawerLayout.bringChildToFront(mDrawerListView);
                if (!mUserLearnedDrawer) {
                    // The user manually opened the drawer; store this flag to prevent auto-showing
                    // the navigation drawer automatically in the future.
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void selectItem(int position) {
        mCurrentSelectedPosition = position;
      /*  if (mDrawerLayout != null) {

            while(mDrawerLayout.isDrawerOpen(mFragmentContainerView))
            {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        }*/

        if (mDrawerListView != null) {


            mDrawerListView.setItemChecked(position, true);
        }

        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        if (mDrawerLayout != null && isDrawerOpen()) {
            inflater.inflate(R.menu.global, menu);
            showGlobalContextActionBar();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        if (item.getItemId() == R.id.action_example) {
            Toast.makeText(getActivity(), "Example action.", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Per the navigation drawer design guidelines, updates the action bar to show the global app
     * 'context', rather than just what's in the current screen.
     */
    private void showGlobalContextActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
       // actionBar.setTitle(R.string.app_name);
    }

    private ActionBar getActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position);
    }
}

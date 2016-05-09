package com.example.sajid.myapplication;

import android.animation.ValueAnimator;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sajid.myapplication.util.GeocodingLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import activity.Activity_main_2;
import activity.MainActivity;
import objects.DataBaseEnums;
import objects.media_obj;
import objects.personal_obj;

public class UserProfile extends Fragment implements OnItemSelectedListener {
    RoundImage roundedImage;
    personal_obj personalObj;

    //map
    public GoogleMap mMap;
    private String profilePicPath;
    public LatLng currentLocation, searchedLocation;
    private static Geocoder geocoder;
    private List<Address> addresses;
    private AutoCompleteTextView autoCompView;
   private static EditText Name;
    TextView NameSolid,cityTextView,stateTextView;
    static String dateOfBirth="";
    Bitmap bitmap;
    private FragmentActivity myContext;
    String city;
    private static final String ARG_SECTION_NUMBER = "section_number";
    private final String LOG_TAG = "Romi: Google Places";
    private final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private final String API_KEY = "AIzaSyBhivIm9hCGwEbVgEE95mHT6_aXahGmcPs";
    private final String OUT_JSON = "/json";
    private static final int REQUEST_TAKE_PHOTO = 100;
    static final int PICKFILE_RESULT_CODE = 2;
    AutoCompleteTextView autoCompleteTextView;
    RelativeLayout relativeLayoutMap;
    RelativeLayout relativeLayoutPhotoEmail;
    LinearLayout linearLayoutMap;
    LinearLayout linearLayoutNameAndDetails;
    LinearLayout linearLayoutExperienceFees;
    LinearLayout linearLayoutCollege;
    EditText addressEditText, feeEditText, experienceEditText;

    CardView cardViewMap;
    CardView cardViewCollege;
    CardView cardViewFeeExperience;
    CardView cardViewPersonalInfo;

    ScrollView scrollView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.activity_user_profile, container, false);
        getActivity().setTitle("Profile Settings");
        TextView DOB = (TextView)rootView.findViewById(R.id.ageTextBox);
        autoCompleteTextView = (AutoCompleteTextView)rootView.findViewById(R.id.textSearchedLocationUserProfile);
        relativeLayoutMap = (RelativeLayout)rootView.findViewById(R.id.location_map_relative_layout);
        final TextView personalDetailsTextView = (TextView)rootView.findViewById(R.id.personalDetailsTextView);
        final TextView experienceFeeTextView = (TextView)rootView.findViewById(R.id.experienceFeeTextView);
        final TextView collegeDetailsTextView = (TextView)rootView.findViewById(R.id.collegeDetailsTextView);
        cityTextView = (TextView)rootView.findViewById(R.id.cityTextView);
        stateTextView =(TextView)rootView.findViewById(R.id.stateTextView);
        addressEditText =(EditText)rootView.findViewById(R.id.addressEditText);
        feeEditText = (EditText)rootView.findViewById(R.id.feeEditText);
        experienceEditText = (EditText)rootView.findViewById(R.id.experienceEditText);
        scrollView =(ScrollView)rootView.findViewById(R.id.scrollView6);

        cardViewCollege = (CardView)rootView.findViewById(R.id.card_view_college);
        cardViewFeeExperience = (CardView)rootView.findViewById(R.id.card_view_fees_experience);
         cardViewPersonalInfo =(CardView)rootView.findViewById(R.id.card_view);
        linearLayoutMap = (LinearLayout)rootView.findViewById(R.id.linearLayoutMap);
        linearLayoutNameAndDetails = (LinearLayout)rootView.findViewById(R.id.nameAndDetails);
        linearLayoutExperienceFees = (LinearLayout)rootView.findViewById(R.id.feeExperienceLinearLayout);
        relativeLayoutPhotoEmail = (RelativeLayout)rootView.findViewById(R.id.photoEmailLayout);
        final ImageView imageViewPersonalDetailExpand = (ImageView)rootView.findViewById(R.id.imageViewPersonalDetailExpand);
        final ImageView imageViewLocationExpand = (ImageView)rootView.findViewById(R.id.imageViewLocationExpand);
        final ImageView imageViewMapFullscreen = (ImageView)rootView.findViewById(R.id.imageViewMapFullscreen);
        final ImageView imageViewExperienceFeeExpand = (ImageView)rootView.findViewById(R.id.imageViewExperienceFeeExpand);
        final ImageView imageViewCollegeExpand = (ImageView)rootView.findViewById(R.id.imageViewCollegeExpand);
        android.support.v7.app.ActionBar actionBar = ((AppCompatActivity) this.getActivity()).getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle("Profile Settings");
        }
        linearLayoutCollege =(LinearLayout)rootView.findViewById(R.id.collegeDetailsLinearLayout);
        imageViewPersonalDetailExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linearLayoutNameAndDetails.getVisibility() == View.GONE) {
                    //makeMapAppear();

                   // imageViewPersonalDetailExpand.setImageResource(R.drawable.ic_action_contract);

                    // imageView.setVisibility(View.GONE);
                    //imageView1.setVisibility(View.VISIBLE);
                    expandPersonalDetailSegment();
                    // personalDetailsTextView.setTextSize(12);

                } else {

                    //imageViewPersonalDetailExpand.setImageResource(R.drawable.ic_action_expand);

                    //imageView.setVisibility(View.VISIBLE);
                    // imageView1.setVisibility(View.GONE);
                    collapsePersonalDetailSegment();
                    // personalDetailsTextView.setTextSize(15);

                }

            }
        });
        imageViewCollegeExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(linearLayoutCollege.getVisibility()==View.GONE) {
                    //makeMapAppear();
                   // imageViewCollegeExpand.setImageResource(R.drawable.ic_action_contract);
                    expandCollegeDetailSegment();
                    // personalDetailsTextView.setTextSize(12);

                }
                else
                {
                   // imageViewCollegeExpand.setImageResource(R.drawable.ic_action_expand);
                    collapseCollegeDetailSegment();
                    // personalDetailsTextView.setTextSize(15);

                }

            }
        });

        imageViewExperienceFeeExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(linearLayoutExperienceFees.getVisibility()==View.GONE) {
                    //makeMapAppear();
                   // imageViewExperienceFeeExpand.setImageResource(R.drawable.ic_action_contract);
                    expandExperienceFeeSegment();
                    // personalDetailsTextView.setTextSize(12);

                }
                else
                {
                    //imageViewExperienceFeeExpand.setImageResource(R.drawable.ic_action_expand);
                    collapseExperienceFeeSegment();
                    // personalDetailsTextView.setTextSize(15);

                }

            }
        });
        cardViewMap = (CardView)rootView.findViewById(R.id.card_view_map);
        imageViewMapFullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
if(relativeLayoutPhotoEmail.getVisibility()== View.VISIBLE) {
    //expandView(linearLayoutMap);
    expandView(cardViewMap);//expandView(linearLayoutMap);
}
                else {
   // CollapseView(linearLayoutMap);
    CollapseView(cardViewMap);
}
            }
        });

        final TextView addLocation = (TextView)rootView.findViewById(R.id.enterLocationTextView);
        imageViewLocationExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(linearLayoutMap.getVisibility()==View.GONE) {
                    //makeMapAppear();
                    expandMapSegment();

                    //imageViewLocationExpand.setImageResource(R.drawable.ic_action_contract);

                    addLocation.setText("Clinic/Hospital Location");
                    //addLocation.setTextSize(12);
                   // personalDetailsTextView.setTextSize(15);
                }
                else {
                   // makeMapDisAppear();
                    collapseMapSegment();
                   // imageViewLocationExpand.setImageResource(R.drawable.ic_action_expand);
                    addLocation.setText("Clinic/Hospital Location");
                   // addLocation.setTextSize(15);
                    //personalDetailsTextView.setTextSize(12);
                }

            }
        });
        Name = (EditText)rootView.findViewById(R.id.userProfileName);
        NameSolid = (TextView)rootView.findViewById(R.id.userProfileNameSolid);
        mMap =null;

        setHasOptionsMenu(true);
       // this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        geocoder = new Geocoder(getActivity(), Locale.getDefault());
        ImageView imageView = (ImageView)rootView.findViewById(R.id.userProfileEditNameIcon);
//        if((dateOfBirth!="")||(dateOfBirth!=null))
//        DOB.setText(dateOfBirth);
//        else
//        DOB.setText("Press to enter Date of Birth");
        DOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doWork();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeEditable(v);
            }
        });
        NameSolid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeEditable(v);
            }
        });

        setUpMapIfNeeded();
        setUserProfileData(rootView);
        autoCompView = (AutoCompleteTextView)rootView.findViewById(R.id.textSearchedLocationUserProfile);

        autoCompView.setAdapter(new GooglePlacesAutocompleteAdapter(getActivity(), R.layout.locationautocompletetextitem));
        autoCompView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.requestFocus();
                GeocodingLocation locationAddress = new GeocodingLocation();
                locationAddress.getAddressFromLocation(autoCompView.getAdapter().getItem(position).toString(),
                        getActivity(), new GeocoderHandler());
            }
        });
        return rootView;
    }
    private void expandMapSegment() {
        //set Visible
        linearLayoutMap.setVisibility(View.VISIBLE);
        autoCompleteTextView.setVisibility(View.VISIBLE);

        relativeLayoutMap.setVisibility(View.VISIBLE);
       // linearLayoutNameAndDetails.setVisibility(View.GONE);
        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        linearLayoutMap.measure(widthSpec, heightSpec);

//        ValueAnimator mAnimator = slideAnimator(0, linearLayoutMap.getMeasuredHeight());
//        mAnimator.start();
    }

    private void collapseMapSegment() {
//        int finalHeight = linearLayoutMap.getHeight();
//
//        ValueAnimator mAnimator = slideAnimator(finalHeight, 0);

        linearLayoutMap.setVisibility(View.GONE);
        autoCompleteTextView.setVisibility(View.GONE);

        relativeLayoutMap.setVisibility(View.GONE);
      //  linearLayoutNameAndDetails.setVisibility(View.VISIBLE);
    }
    private void expandPersonalDetailSegment()
    {
        expandView(cardViewPersonalInfo);

        linearLayoutNameAndDetails.setVisibility(View.VISIBLE);
    }
    private void collapsePersonalDetailSegment()
    {
        CollapseView(cardViewPersonalInfo);
        linearLayoutNameAndDetails.setVisibility(View.GONE);
    }
    private void expandCollegeDetailSegment()
    {
       expandView(cardViewCollege);
        linearLayoutCollege.setVisibility(View.VISIBLE);
    }
    private void collapseCollegeDetailSegment()
    {
        CollapseView(cardViewCollege);
        linearLayoutCollege.setVisibility(View.GONE);
    }
    private void expandExperienceFeeSegment()
    {
        expandView(cardViewFeeExperience);
        linearLayoutExperienceFees.setVisibility(View.VISIBLE);
    }
    private void collapseExperienceFeeSegment()
    {
        CollapseView(cardViewFeeExperience);
        linearLayoutExperienceFees.setVisibility(View.GONE);
    }

    private ValueAnimator slideAnimator(int start, int end) {

        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = linearLayoutMap.getLayoutParams();
                layoutParams.height = value;
                linearLayoutMap.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }
    private void makeMapAppear()
    {

//        relativeLayoutMap.animate().translationY(relativeLayoutMap.getHeight());
//        autoCompleteTextView.animate().translationY(autoCompleteTextView.getHeight());
        Animation animation = AnimationUtils.loadAnimation(getActivity(),R.anim.slide_from_bottom);
        cardViewMap.setAnimation(animation);
        autoCompleteTextView.setVisibility(View.VISIBLE);

        relativeLayoutMap.setVisibility(View.VISIBLE);
//        relativeLayoutMap.startAnimation(animation);
//        autoCompleteTextView.setAnimation(animation);


    }
    private void makeMapDisAppear()
    {
//        relativeLayoutMap.animate().translationY(relativeLayoutMap.getHeight());
//        autoCompleteTextView.animate().translationY(autoCompleteTextView.getHeight());
        autoCompleteTextView.setVisibility(View.GONE);

        relativeLayoutMap.setVisibility(View.GONE);
    }
    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }
    public void doWork()
    {
        LayoutInflater li = LayoutInflater.from(getActivity());
        final View promptsView = li.inflate(R.layout.profile_date_picker, null);
        final DatabaseHandler databaseHandler = new DatabaseHandler(getActivity());
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                                final DatePicker datePicker = (DatePicker) promptsView.findViewById(R.id.datePicker);
                                if ((dateOfBirth != "") || (dateOfBirth != null)) {
                                    int month = datePicker.getMonth()+1;
                                    int year = datePicker.getYear();
                                    int day = datePicker.getDayOfMonth();
                                    dateOfBirth = day + "/" + month + "/" + year;

                                    databaseHandler.updatePersonalInfo("dob",dateOfBirth);
                                    databaseHandler.updatePersonalInfo(DataBaseEnums.KEY_NAME,Name.getText().toString());
                                    databaseHandler.updatePersonalInfo(DataBaseEnums.KEY_SPECIALITY,personalObj.get_speciality());
                                    databaseHandler.updatePersonalInfo(DataBaseEnums.KEY_LATITUDE, String.valueOf(searchedLocation.latitude));
                                    databaseHandler.updatePersonalInfo(DataBaseEnums.KEY_LONGITUDE, String.valueOf(searchedLocation.longitude));
                                }


                                final FragmentManager fragManager = myContext.getSupportFragmentManager();
                                fragManager.beginTransaction()
                                        .replace(R.id.container, UserProfile.newInstance(1))
                                        .commit();
                               // utility.recreateActivityCompat(getActivity());


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
    /***** Sets up the map if it is possible to do so *****/
    public  void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment)this.getChildFragmentManager()
                    .findFragmentById(R.id.location_map)).getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
        else
        {
            setUpMap();
        }
    }


    public void expandView(final View v){
        v.measure(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        //cardViewMap.measure(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        DisplayMetrics metrics = new DisplayMetrics();
        if(v!=relativeLayoutPhotoEmail)
        relativeLayoutPhotoEmail.setVisibility(View.GONE);
        if(v!=cardViewCollege)
        cardViewCollege.setVisibility(View.GONE);
        if(v!=cardViewFeeExperience)
        cardViewFeeExperience.setVisibility(View.GONE);
        if(v!=cardViewPersonalInfo)
        cardViewPersonalInfo.setVisibility(View.GONE);
        if(v!=cardViewMap)
        cardViewMap.setVisibility(View.GONE);

       // scrollView.removeView(cardViewMap);
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        final int targetHeight = metrics.heightPixels;
        final int targetWidth = metrics.widthPixels;
//        v.getLayoutParams().height =  metrics.heightPixels;
//        v.getLayoutParams().width = metrics.widthPixels;
       // cardViewMap.getLayoutParams().height =  metrics.heightPixels;
       // cardViewMap.getLayoutParams().width = metrics.widthPixels;
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                super.applyTransformation(interpolatedTime, t);
                v.getLayoutParams().height = (int)(targetHeight);
                v.getLayoutParams().width = targetWidth;
                v.requestLayout();
            }
            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration(((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density)) * 8);
       v.startAnimation(a);
    }
    public void CollapseView(final View v) {
        v.measure(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        int targetHeight;
       // cardViewMap.measure(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        if(v!=relativeLayoutPhotoEmail)
        relativeLayoutPhotoEmail.setVisibility(View.VISIBLE);
        if(v!=cardViewCollege)
        cardViewCollege.setVisibility(View.VISIBLE);
        if(v!=cardViewFeeExperience)
        cardViewFeeExperience.setVisibility(View.VISIBLE);
        if(v!=cardViewPersonalInfo)
            cardViewPersonalInfo.setVisibility(View.VISIBLE);
        if(v!=cardViewMap)
         cardViewMap.setVisibility(View.VISIBLE);


        targetHeight = 200;
        if((v == cardViewMap)||(v == linearLayoutMap))
            targetHeight = 600;


        final int finalTargetHeight = targetHeight;
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                super.applyTransformation(interpolatedTime, t);
                v.getLayoutParams().height = (int)(finalTargetHeight);

                v.requestLayout();
            }
            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        //cardViewMap.getLayoutParams().height =  targetHeight;
        //cardViewMap.getLayoutParams().width = metrics.widthPixels;
        a.setDuration(((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density)) * 8);
        v.startAnimation(a);


    }

    /**
     * This is where we can add markers or lines, add listeners or move the
     * camera.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap}
     * is not null.
     */
    private void setUpMap() {

        // For showing a move to my loction button

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager)getActivity().getSystemService(getActivity().LOCATION_SERVICE);
        boolean gpsOn = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);


DatabaseHandler databaseHandler = new DatabaseHandler(getActivity());
        ArrayList<String> latLong = databaseHandler.getSavedLatitudeLongitude();
        if((latLong.get(0)!=null)&&(latLong.get(1)!=null)&&(!latLong.get(0).equals(""))&&(!latLong.get(1).equals("")))
        currentLocation = new LatLng(Double.parseDouble(latLong.get(0)), Double.parseDouble(latLong.get(1)));

        else
        {
            android.support.v7.app.ActionBar actionBar = ((AppCompatActivity) this.getActivity()).getSupportActionBar();
            assert actionBar != null;
            actionBar.setDisplayHomeAsUpEnabled(false);
            if(!gpsOn)
            {
                Toast.makeText(getActivity(),"Please Move The GPS to High Accuracy To Get Your Current Location",Toast.LENGTH_SHORT).show();
            }
        }
        if (currentLocation != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16.0f));
        }

        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

            @Override
            public void onMyLocationChange(Location arg0) {

                if (currentLocation == null) {
                    currentLocation = new LatLng(arg0.getLatitude(), arg0.getLongitude());
//                    mMap.addMarker(new MarkerOptions().position(new LatLng(arg0.getLatitude(), arg0.getLongitude())).title("It's Me!"));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16.0f));

                }
                currentLocation = new LatLng(arg0.getLatitude(), arg0.getLongitude());
            }
        });

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                try {
                    searchedLocation = mMap.getCameraPosition().target;
                    if (searchedLocation!=null){
                        mMap.clear();
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(searchedLocation);
                        mMap.addMarker(markerOptions);
                    }
                    addresses = geocoder.getFromLocation(searchedLocation.latitude, searchedLocation.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName();
                    String textaddress = address + ", " + city + ", " + state + ", " + country;
                    autoCompView.setText(textaddress);
                    autoCompView.dismissDropDown();
                    cityTextView.setText(city);
                    stateTextView.setText(state);


                } catch (Exception e) {
                    e.printStackTrace();

                } finally {

                }
            }
        });
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    if (bundle.getParcelable("LatLong")!=null)
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(((LatLng) bundle.getParcelable("LatLong")), 16.0f));
                    break;
                default:
                    locationAddress = null;
            }
        }
    }

    class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements Filterable {
        private ArrayList resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index).toString();
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }

    public ArrayList autocomplete(String input) {
        ArrayList resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);

            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return resultList;
        } catch (IOException e) {
            e.printStackTrace();
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    private void CaptureMapScreen()
    {
        GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback() {


            @Override
            public void onSnapshotReady(Bitmap snapshot) {
                bitmap = snapshot;
                File photoFile = null;
                try {
                    photoFile = PhotoHelper.createImageFile(0);

                } catch (IOException ex) {
                    // Error occurred while creating the File

                }
                DatabaseHandler databaseHandler = new DatabaseHandler(getActivity());
                try {
                    FileOutputStream out = new FileOutputStream(photoFile.getAbsolutePath()
                           );

                    databaseHandler.updatePersonalInfo(DataBaseEnums.KEY_MAP_SNAPSHOT_PATH,photoFile.getPath());

                    // above "/mnt ..... png" => is a storage path (where image will be stored) + name of image you can customize as per your Requirement

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 10, out);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String personalInfoJSON =  databaseHandler.composeJSONforPersonalInfo(getActivity(), city);
                RequestParams params = new RequestParams();
                params.put("doctorJSON", personalInfoJSON);
                String address = getResources().getString(R.string.action_server_ip_address);
                utility.sync("http://"+ address +"/updateDoctor.php", params, getActivity());
                Toast.makeText(getActivity(), "Profile Settings Saved Successfully", Toast.LENGTH_SHORT).show();
                final FragmentManager fragManager = myContext.getSupportFragmentManager();
               // uploadfile.uploadImage(getActivity(), docPaths, pid);
                uploadProfilePics();
                Thread thread = new Thread(){
                    @Override
                    public void run() {
                        try {
                            //uploadProfilePics();
                            Thread.sleep(2000); // As I am using LENGTH_LONG in Toast

                            fragManager.beginTransaction()
                                    .replace(R.id.container, MainActivity.newInstance(1))
                                    .commit();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
//                NavigationDrawerFragment mNavigationDrawerFragment;
//                mNavigationDrawerFragment = (NavigationDrawerFragment)
//                        fragManager.findFragmentById(R.id.navigation_drawer);
//mNavigationDrawerFragment.onResume();
//                fragManager.beginTransaction()
//                        .replace(R.id.container, MainActivity.newInstance(1))
//                        .commit();

                Intent intent = new Intent(getActivity(),Activity_main_2.class);
                startActivity(intent);
                getActivity().finish();
               // utility.recreateActivityCompat(getActivity());
            }
        };

       // utility.recreateActivityCompat(getActivity());
        mMap.snapshot(callback);



        // myMap is object of GoogleMap +> GoogleMap myMap;
        // which is initialized in onCreate() =>
        // myMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_pass_home_call)).getMap();
    }

    public void makeEditable(View view)
    {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        Name.setVisibility(View.VISIBLE);
        Name.requestFocus();
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(Name, InputMethodManager.SHOW_IMPLICIT);
        NameSolid.setVisibility(View.GONE);

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_user_profile, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    public void uploadProfilePics()
    {
    DatabaseHandler databaseHandler = new DatabaseHandler(getActivity());
        ArrayList<media_obj> paths = databaseHandler.getPersonalInfoPhotos();
    Context context = getContext();

        uploadfile.uploadImage(context, paths);
    }




    public void addProfilePic() throws IOException
    {
        final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    dispatchTakePictureIntent();
                } else if (items[item].equals("Choose from Library")) {
                    uploadProfilePicFromSDCard();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();


    }

    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Intent intent = getActivity().getIntent();
        //media_obj mediaObj = new media_obj();

        final DatabaseHandler dbHandler = new DatabaseHandler(getActivity());



        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
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
//                DatabaseHandler databaseHandler = new DatabaseHandler(getActivity());
//                databaseHandler.updatePersonalInfo("documentPath", profilePicPath);


                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

                //utility.recreateActivityCompat(getActivity());

            }
        }
    }
    public void uploadProfilePicFromSDCard()
    {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICKFILE_RESULT_CODE);

//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("file/.jpeg");
//        startActivityForResult(intent,PICKFILE_RESULT_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        DatabaseHandler databaseHandler = new DatabaseHandler(getActivity());

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {


            databaseHandler.updatePersonalInfo("documentPath", profilePicPath);
            try {
                Bitmap bitmap;
                bitmap =BitmapFactory.decodeFile(profilePicPath);
                FileOutputStream out = new FileOutputStream(profilePicPath
                );
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }





        }
        if ( (requestCode == PICKFILE_RESULT_CODE )&&((data !=null)&&(data.getData()!=null)))
        {Uri uri = data.getData();
            File file = null;
            String file_name = "";
            String file_path = "";


            if (uri.getScheme().compareTo("content")==0) {
                Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
                if (cursor.moveToFirst()) {
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);//Instead of "MediaStore.Images.Media.DATA" can be used "_data"
                    Uri filePathUri = Uri.parse(cursor.getString(column_index));
                    file_name = filePathUri.getLastPathSegment();
                    file_path=filePathUri.getPath();
                }
            }


            try
            {
                File newFile = null;
                File storageDir =
                        new File(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES), "Patient Manager/profile_pictures");
                if(!storageDir.exists())
                    storageDir.mkdir();
                newFile = new File(storageDir.getPath()+"/"+new File(file_path).getName());
                FileUtils.copyFile(new File(file_path), newFile);
                if((newFile.getName().contains(".jpeg"))||(newFile.getName().contains(".png")))
                databaseHandler.updatePersonalInfo("documentPath", newFile.getPath());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        final FragmentManager fragManager = myContext.getSupportFragmentManager();
        fragManager.beginTransaction()
                .replace(R.id.container, UserProfile.newInstance(1))
                .commit();
       // utility.recreateActivityCompat(Activity_main_2.this);
    }



    public void setUserProfileData(View rootView)
    {
        DatabaseHandler databaseHandler = new DatabaseHandler(getActivity());

         personalObj = databaseHandler.getPersonalInfo();
        ArrayList<String>addressElements = new ArrayList<>();
        if((personalObj.get_address().length()>1)&&(!personalObj.get_address().equals("Address")))
        addressElements = parseAddress(personalObj.get_address());
        if((addressElements!=null)&&(addressElements.size()!=0))
        {addressEditText.setText(addressElements.get(0));
        cityTextView.setText(addressElements.get(1));
        stateTextView.setText(addressElements.get(2));}
        if(personalObj.get_experience()!= null)
        {
            experienceEditText.setText(personalObj.get_experience());
        }
        if(personalObj.get_fees()!= null)
        {
            feeEditText.setText(personalObj.get_fees());
        }
        dateOfBirth = personalObj.get_dob();
        ImageView imageView = (ImageView)rootView.findViewById(R.id.userProfilePicture);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    addProfilePic();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        TextView textView = (TextView)rootView.findViewById(R.id.userProfile_email);

        textView.setText(personalObj.get_email());
        EditText textView1 = (EditText)rootView.findViewById(R.id.userProfileName);
        TextView textView2 = (TextView)rootView.findViewById(R.id.userProfilePhoneNumber);
        TextView textView3 = (TextView)rootView.findViewById(R.id.userProfileNameSolid);
        TextView textView4 = (TextView)rootView.findViewById(R.id.ageTextBox);
        textView4.setText(personalObj.get_dob());
        Spinner spinner = (Spinner)rootView.findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.specialities, R.layout.spinner_item);
        Resources res = getResources();
        String[] Lines = res.getStringArray(R.array.specialities);


        int position = getSpecialityId();
        //personalObj.set_speciality( Lines[position-1]);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setSelection(position - 1);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                personalObj.set_speciality(String.valueOf(position + 1));
//

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }

        });



        textView1.setText(personalObj.get_name());
        textView2.setText(personalObj.get_password());
        textView3.setText(personalObj.get_name());
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


    }
    public ArrayList<String> parseAddress(String address)
    {
        ArrayList<String> addressElements = new ArrayList<>();
        int indexOfLastComma = address.lastIndexOf(",");

        String state = address.substring(indexOfLastComma+1);
        address = address.substring(0,indexOfLastComma);
        indexOfLastComma = address.lastIndexOf(",");
        String city = address.substring(indexOfLastComma+1);
        address = address.substring(0,indexOfLastComma);
        String postalAddress = address;
        addressElements.add(postalAddress);
        addressElements.add(city);
        addressElements.add(state);


        return addressElements;
    }

    public int getSpecialityId()
    {
        String speciality="";
        DatabaseHandler databaseHandler = new DatabaseHandler(getActivity());
        try {
            if (!personalObj.get_speciality().equals(null))
                speciality = personalObj.get_speciality();
        }
        catch (Exception e) {

            return 0;
        }

        Resources res = getResources();
        String[] Lines = res.getStringArray(R.array.specialities);
        int index = 0;
        for (int i=0;i<Lines.length;i++) {
            String name = Lines[i];
            if (name.equals(speciality)) {
                index = i;
                break;

            }
        }
       // if(speciality.equals())
        return Integer.parseInt(databaseHandler.getPersonalInfo().get_speciality());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position)
        {
            case 0:
                personalObj.set_speciality("Orthopedic Surgery");
                break;
            case 1:
                personalObj.set_speciality("Ophthalmics");
                break;
            case 2:
                personalObj.set_speciality("Dentist");
                break;
            case 3:
                personalObj.set_speciality("Cardiologist");
                break;
            case 4:
                personalObj.set_speciality("General Practice");
                break;
            case 5:
                personalObj.set_speciality("Gynecology");
                break;
            case 6:
                personalObj.set_speciality("Neurology");
                break;
            case 7:
                personalObj.set_speciality("Pediatrics");
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {


    }


    public void savePersonalInfo()
    {
        try {
            DatabaseHandler databaseHandler = new DatabaseHandler(getActivity());
            databaseHandler.updatePersonalInfo(DataBaseEnums.KEY_NAME, Name.getText().toString());
            databaseHandler.updatePersonalInfo(DataBaseEnums.KEY_SPECIALITY, personalObj.get_speciality());
            databaseHandler.updatePersonalInfo(DataBaseEnums.KEY_LATITUDE, String.valueOf(searchedLocation.latitude));
            databaseHandler.updatePersonalInfo(DataBaseEnums.KEY_LONGITUDE, String.valueOf(searchedLocation.longitude));
            databaseHandler.updatePersonalInfo("dob", dateOfBirth);
            String address = addressEditText.getText().toString()
                    +","+ cityTextView.getText().toString()
                    +","+stateTextView.getText().toString();
            databaseHandler.updatePersonalInfo(DataBaseEnums.KEY_ADDRESS,addressEditText.getText().toString()
                    +","+ cityTextView.getText().toString()
            +","+stateTextView.getText().toString());
            databaseHandler.updatePersonalInfo(DataBaseEnums.KEY_CONSULT_FEE, feeEditText.getText().toString());
            databaseHandler.updatePersonalInfo(DataBaseEnums.KEY_EXPERIENCE, experienceEditText.getText().toString());
            //databaseHandler.updatePersonalInfo(DataBaseEnums.KEY_MAP_SNAPSHOT_PATH, String.valueOf(searchedLocation.longitude));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        CaptureMapScreen();



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {

            savePersonalInfo();



            return true;
        }
        if(id== android.R.id.home)
        {
            final FragmentManager fragManager = myContext.getSupportFragmentManager();
            fragManager.beginTransaction()
                    .replace(R.id.container, MainActivity.newInstance(1))
                    .commit();
        }

        return super.onOptionsItemSelected(item);
    }
    public static UserProfile newInstance(int sectionNumber) {
        UserProfile fragment = new UserProfile();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

}

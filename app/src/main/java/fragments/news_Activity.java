package fragments;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import com.elune.sajid.myapplication.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import activity.article_view_activity;
import adapters.ExpandableListAdapter;
import adapters.newsAdapter;
import objects.Item;
import utilityClasses.DatabaseHandler;
import utilityClasses.EndlessScrollListener;
import utilityClasses.utility;

import static com.elune.sajid.myapplication.R.id.spinner;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Diagnosis.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Diagnosis#newInstance} factory method to
 * create an instance of this fragment.
 */
public class news_Activity extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayList<String> listDataHeader;
    ArrayList<String> listDataHeaderDates;
    HashMap<String, List<String>> listDataChild;
    HashMap<String, List<String>> listDataChildDates;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ListView  listView;
    View rootView;
    Menu menu;
    ImageView imageView;
    static String specialityToDisplay;
    static newsAdapter news_Adapter;
    String title;
    String accountType;
    static int newsLoadPageCounter = 0;
    static ArrayList<Item> newsArrayList;
    static int postNumberOnTop = 0;
    SwipeRefreshLayout swipeRefreshLayout;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    private static final String ARG_SECTION_NUMBER = "section_number";
    public static Fragment newInstance(int position) {
        news_Activity fragment = new news_Activity();
        Bundle args = new Bundle();


        args.putInt(ARG_SECTION_NUMBER, position);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    public news_Activity() {
        // Required empty public constructor
    }

    @Override
    public void onStop() {
        newsLoadPageCounter = 0;
        super.onStop();
    }

    @Override
    public void onResume() {

        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.data_refresh, menu);
        this.menu=menu;
        MenuItem item = menu.findItem(spinner);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final SharedPreferences.Editor editor = prefs.edit();
        String staticSpecialityNews = prefs.getString("staticSpecialityNews","all");
//        ArrayAdapter<CharSequence> barAdapter = new ArrayAdapter<CharSequence>(this, R.layout.action_sort,
//                android.R.id.text1, getResources().getStringArray(R.array.specialities));
//        barAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//

        Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);
        //MenuItem menuItem = menu.findItem(spinner);
        //Spinner spinner = (Spinner) MenuItemCompat.getActionView(menuItem);
       // if (specialityToDisplay != null)
            if (staticSpecialityNews.equals("all"))

                spinner.setSelection(0);
            else
                spinner.setSelection(Integer.parseInt(staticSpecialityNews));
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
               // R.array.specialities, android.R.layout.simple_spinner_item);
        R.array.specialitiesNews, R.layout.action_sort);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
//        spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                newsLoadPageCounter = 0;
//                String[] myResArray = getResources().getStringArray(R.array.specialitiesNews);
//                if(position == 0 )
//                {
//                    specialityToDisplay = "all";
//                }
//                else
//                {
//                    specialityToDisplay = String.valueOf(position-1);
//                }
//                GetUrlData getUrlData = new GetUrlData();
//                getUrlData.execute((Void) null);
//            }
//        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                newsLoadPageCounter = 0;


                String[] myResArray = getResources().getStringArray(R.array.specialitiesNews);
                if(position == 0 )
                {
                    specialityToDisplay = "all";

                }
                else
                {
                    specialityToDisplay = String.valueOf(position);
                }
                editor.putString("staticSpecialityNews",specialityToDisplay);
               news_Adapter.updateReceiptsList();
                GetUrlData getUrlData = new GetUrlData();
                getUrlData.execute((Void) null);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.refresh1) {
//newsLoadPageCounter = 0;
//            newsArrayList = new ArrayList<Item>();
//            GetUrlData getUrlData = new GetUrlData();
//            getUrlData.execute((Void) null);
//            ListView  listView = (ListView)rootView.findViewById(R.id.listViewNews);
//            listView.setSelection(0);
////            MenuItem menuItemSpinner = menu.findItem(R.id.spinner);
////            menuItemSpinner.setVisible(false);
////            item.collapseActionView();
//            return true;
//        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.activity_news_, container, false);
       // getActivity().setTitle("Latest News");
        setHasOptionsMenu(true);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        specialityToDisplay = prefs.getString("staticSpecialityNews","all");
imageView = (ImageView)rootView.findViewById(R.id.imageViewEmptyNews);
//        Item item = new Item();
//        item.setTitle("");
//        itemsArrayList.add(item);
         swipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Toast.makeText(getActivity(),"hi",Toast.LENGTH_LONG).show();
newsLoadPageCounter = 0;
                newsArrayList = new ArrayList<Item>();
                news_Adapter.updateReceiptsList();
                GetUrlData getUrlData = new GetUrlData();
                getUrlData.execute((Void) null);

            }
        });


        // imageView = (ImageView)rootView.findViewById(R.id.imageView13);
         //textView = (TextView)rootView.findViewById(R.id.textView73);
        if(newsArrayList==null)
            newsArrayList = new ArrayList<>();

        news_Adapter = new newsAdapter(getActivity(),newsArrayList);

          listView        = (ListView)rootView.findViewById(R.id.listViewNews);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), article_view_activity.class);
                intent.putExtra("heading", newsArrayList.get(position).getTitle());
                intent.putExtra("url", newsArrayList.get(position).getDiagnosis());
                String s1 = null;
                DatabaseHandler databaseHandler = new DatabaseHandler(getActivity());
                final String  address =  "docbox.co.in/sajid";
                final RequestParams param = new RequestParams();
                final AsyncHttpClient client = new SyncHttpClient(true, 80, 443);
                ArrayList<String> CstmrId = new ArrayList<>();
                int customerId = databaseHandler.getCustomerId();
                CstmrId.add(String.valueOf(customerId));
                CstmrId.add(String.valueOf(newsArrayList.get(position).getExtra1()));
                StringWriter out = new StringWriter();

                try {
                    JSONValue.writeJSONString(CstmrId, out);
                    s1 = out.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                param.put("DoctorId", s1);
                Thread thread = new Thread() {
                    @Override
                    public void run() {


                            utility.syncDataSync("http://" + address + "/newsViewLog.php",param,getActivity());

                    }
                };

                thread.start();




                getActivity().startActivity(intent);
            }
        });
        listView.setAdapter(news_Adapter);
        listView.setSelection(postNumberOnTop);
listView.setOnScrollListener(new EndlessScrollListener() {


    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        postNumberOnTop = firstVisibleItem;
        this.onScrolled(view, firstVisibleItem, visibleItemCount, totalItemCount);
    }

    @Override
    public boolean onLoadMore(int page, int totalItemsCount) {
        GetUrlData getUrlData = new GetUrlData();
        getUrlData.execute((Void) null);

        // Triggered only when new data needs to be appended to the list
        // Add whatever code is needed to append new items to your AdapterView
//        customLoadMoreDataFromApi(page);
        // or customLoadMoreDataFromApi(totalItemsCount);
        return true; // ONLY if more data is actually being loaded; false otherwise.
    }
});

       // Document doc = null;
        GetUrlData getUrlData = new GetUrlData();
        getUrlData.execute((Void) null);
        //doc = Jsoup.connect("https://ryortho.com/breaking/ankle-hindfoot-study-graftbetter-fusion/").userAgent("android").post();
      //  assert doc != null;
       // String title = doc.title();
        //textView.setText(title);
        return  rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public class GetUrlData extends AsyncTask<Void, Void, Boolean> {

        ArrayList<Item> itemsArrayList;
        InputStream in = null;
        Bitmap mIcon11 = null;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }
        public void parseNewsResponse(JSONArray response)
        {
             itemsArrayList  = new ArrayList<>();

for(int i = 0;i<response.size();i++)
{
    Item item = new Item();
    org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) response.get(i);
    String title = String.valueOf(jsonObject.get("PostHeading"));
    item.setTitle(title);
    String photoLink = String.valueOf(jsonObject.get("MainPicLink"));
    item.setExtra(photoLink);
    String newsLink = String.valueOf(jsonObject.get("PostLink"));
    item.setDiagnosis(newsLink);
    String Date = String.valueOf(jsonObject.get("TimeStamp"));
    item.setDate(Date);
    String postId = String.valueOf(jsonObject.get("PostId"));
    item.setExtra1(postId);

    itemsArrayList.add(item);
}




        }
        public void hitApiForAppointment(String apiAddress, RequestParams params, AsyncHttpClient client,
                                         final Context context) {


            final DatabaseHandler databaseHandler = new DatabaseHandler(context);

            try
            {

                client.post(apiAddress, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i,cz.msebera.android.httpclient. Header[] headers, byte[] bytes) {

                        try {
                            String str = new String(bytes, "UTF-8");
                            JSONArray response;
                            // JSONObject mainObject = new JSONObject(str);
                            ArrayList<String> appointmentPID;
if(!str.equals("0"))
{ response = (JSONArray) JSONValue.parse(str);


parseNewsResponse(response);}


                            newsLoadPageCounter++;
//System.out.print(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int i,cz.msebera.android.httpclient. Header[] headers, byte[] bytes, Throwable throwable) {
                        try {
                            return;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onFinish() {


                       // pdia.dismiss();



                        // Toast.makeText(context,    "END", Toast.LENGTH_LONG).show();
                        //((Activity) context).recreate();

                    }


                });
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            // return customerId[0];
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Document doc = null;
            String s1 = null;
            DatabaseHandler databaseHandler = new DatabaseHandler(getActivity());
            final String  address =  "docbox.co.in/sajid";
             RequestParams param = new RequestParams();
            final AsyncHttpClient client = new SyncHttpClient(true, 80, 443);
            ArrayList<String> CstmrId = new ArrayList<>();
            int customerId = databaseHandler.getCustomerId();
            CstmrId.add(String.valueOf(customerId));
            CstmrId.add(String.valueOf(newsLoadPageCounter));
            CstmrId.add(specialityToDisplay);
            StringWriter out = new StringWriter();

            try {
                JSONValue.writeJSONString(CstmrId, out);
                s1 = out.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            param.put("DoctorId", s1);
            hitApiForAppointment("http://" + address + "/getNews.php",param,client,getActivity());



//            try{
//            doc = Jsoup.connect("https://ryortho.com/breaking/ankle-hindfoot-study-graftbetter-fusion/").userAgent("android").post();
//        } catch (IOException e) {
//            e.printStackTrace();
//
//        }
//
//        if (doc!=null) {
//            title = doc.title();
//
//            Elements elements = doc.getElementsByTag("img");
//            Element element = elements.get(0);
//            int width = Integer.parseInt(element.attr("width"));
//            int height = Integer.parseInt(element.attr("height"));
//
//            for (int i = 0; i <= elements.size(); i++) {
//                Element elementTemp = elements.get(i);
//
//                if (Integer.parseInt(element.attr("width")) * Integer.parseInt(element.attr("height")) > width * height) {
//                    width = Integer.parseInt(element.attr("width"));
//                    height = Integer.parseInt(element.attr("height"));
//                    element = elementTemp;
//                    elementTemp = null;
//
//                }
//
//            }
//
//            String imagelink = element.attr("src");
//            try {
//                in = new java.net.URL(imagelink).openStream();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            mIcon11 = BitmapFactory.decodeStream(in);
//            //elements.
//            //doc.
//            //textView.setText();
//            //imageView.se
//        }
            return null;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            //imageView.setImageBitmap(mIcon11);
           // textView.setText(title);
            news_Adapter.updateReceiptsList(itemsArrayList);
            if(news_Adapter.itemsArrayList.size()==0)
           // if(itemsArrayList==null)
            { imageView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);}
            else
            { imageView.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);}
            //newsArrayList.addAll(itemsArrayList);
            swipeRefreshLayout.setRefreshing(false);

        }

        //i: patient id
        //s: file path



        public void doSomething() {

        }


    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}

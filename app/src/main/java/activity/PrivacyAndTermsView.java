package activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

import com.elune.sajid.myapplication.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import utilityClasses.DatabaseHandler;

public class PrivacyAndTermsView extends Activity {
    TextView textViewContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String address =  getResources().getString(R.string.action_server_ip_address);
        setContentView(R.layout.activity_privacy_and_terms_view);
        textViewContent = (TextView)findViewById(R.id.textView71) ;
        Intent intent = getIntent();
       String content = intent.getStringExtra("content");
        //content = "terms";
        RequestParams params = new RequestParams();
        final AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
        if(content.equals("terms"))
        this.hitApi("http://" + address + "/terms.php", params,client);
        else
            this.hitApi("http://" + address + "/privacy.php", params,client);
    }

    public  void hitApi(String apiAddress, RequestParams params, AsyncHttpClient client) {
        // client = new SyncHttpClient(true, 80, 443);

        final DatabaseHandler databaseHandler = new DatabaseHandler(PrivacyAndTermsView.this);

        try
        {

            client.post(apiAddress, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {

                    try {
                        String str = new String(bytes, "UTF-8");
                        Log.e("terms",str);
                       textViewContent.setText(str);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //controller.updateSyncStatus(obj.get("id").toString(),obj.get("status").toString());

                }

                @Override
                public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {
                    try {
                        // String str = new String(bytes, "UTF-8");
                        //Toast.makeText(LoginActivity.this, "MySQL DB has not been informed about Sync activity", Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onFinish() {
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
}

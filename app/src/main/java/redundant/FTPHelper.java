package redundant;

import android.content.Context;
import android.widget.Toast;

import utilityClasses.DatabaseHandler;
import com.elune.sajid.myapplication.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.simple.JSONValue;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;

/**
 * Created by sajid on 10/10/2015.
 */
public class FTPHelper {
    /*********  work only for Dedicated IP ***********/
    static final String FTP_HOST= "ftp.docbox.co.in";

    /*********  FTP USERNAME ***********/
    static final String FTP_USER = "sajid@docbox.co.in";

    /*********  FTP PASSWORD ***********/
    static final String FTP_PASS  ="aimsaims";
    static String filePath;
    static String patientName;
    static Context context;

    public static void Dowork(ArrayList<String> path,String Name, final String CustomerId,Context context1) {
        final ArrayList<String>filePath = path;
        patientName = Name;
        context = context1;


        new Thread(new Runnable() {
            public void run() {
                for(int i =0;i<filePath.size();i++)

                {
                    String path = filePath.get(i);
                    File f = new File(path);

                // Upload sdcard file
                uploadFile(f,CustomerId);}

            }
        }).start();






        /********** Pick file from sdcard *******/


    }
    public static void uploadUsingPhp(File fileName,String CustomerId,String patientId)
    {
        String destinationPath;
        RequestParams params = new RequestParams();
        ArrayList<String> parameters = new ArrayList<>();
        parameters.add(fileName.getPath());
        parameters.add(CustomerId);
        parameters.add(patientId);
        String s1 = null;

        StringWriter out = new StringWriter();
        try {
            JSONValue.writeJSONString(parameters, out);
            s1 = out.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("fileUpload",s1);
        String address = context.getResources().getString(R.string.action_server_ip_address);
        sync("http://"+ address +"/uploadFile.php",params);

    }
    public static void sync(String apiAddress, RequestParams params) {
        final AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);

        final DatabaseHandler databaseHandler = new DatabaseHandler(context);

        try
        {

            client.post(apiAddress, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {

                    try {
                        String str = new String(bytes, "UTF-8");


                        //System.out.print("a");


                        if (context == null) {
                            return;
                        }
                        // ((Activity) context).recreate();

                        //Toast.makeText(context, "MySQL DB has been informed about Sync activity", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //controller.updateSyncStatus(obj.get("id").toString(),obj.get("status").toString());

                }

                @Override
                public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {
                    try {
                        String str = new String(bytes, "UTF-8");
                        Toast.makeText(context, "MySQL DB has not been informed about Sync activity", Toast.LENGTH_LONG).show();

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

    }

    public static void uploadFile(File fileName,String CustomerId){


        FTPClient client = new FTPClient();
        String directory;

        try {

            client.connect(FTP_HOST, 21);
            client.login(FTP_USER, FTP_PASS);
            client.setType(FTPClient.TYPE_BINARY);

            try {

                client.createDirectory(CustomerId);
            }
            catch (Exception e) {
                e.printStackTrace();

            }

            client.changeDirectory("/" + CustomerId + "/");
            if(!patientName.equals("")) {

                try {

                    client.createDirectory(patientName);


                } catch (Exception e) {
                    e.printStackTrace();
                }

                client.changeDirectory("/" + CustomerId + "/" + patientName + "//");

            }
            client.upload(fileName, new MyTransferListener());


        } catch (Exception e) {

            e.printStackTrace();
            try {
                client.disconnect(true);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

    }

    /*******  Used to file upload and show progress  **********/

    public static class MyTransferListener implements FTPDataTransferListener {

        public void started() {


            // Transfer started
            // Toast.makeText(getBaseContext(), " Upload Started ...", Toast.LENGTH_SHORT).show();
            System.out.println(" Upload Started ...");
        }

        public void transferred(int length) {

            // Yet other length bytes has been transferred since the last time this
            // method was called
            // Toast.makeText(getBaseContext(), " transferred ..." + length, Toast.LENGTH_SHORT).show();
            System.out.println(" transferred ..." + length);
        }

        public void completed() {



            // Toast.makeText(getBaseContext(), " completed ...", Toast.LENGTH_SHORT).show();
            System.out.println(" completed ..." );
        }

        public void aborted() {


            //  Toast.makeText(getBaseContext()," transfer aborted ,
            //please try again...", Toast.LENGTH_SHORT).show();
            System.out.println(" aborted ..." );
        }

        public void failed() {


            // Transfer failed
            System.out.println(" failed ..." );
        }

    }

}

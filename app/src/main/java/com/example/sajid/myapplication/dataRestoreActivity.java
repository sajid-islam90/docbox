package com.example.sajid.myapplication;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import objects.document_obj;

public class dataRestoreActivity extends AppCompatActivity {
    private ProgressDialog pdia;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_restore);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(dataRestoreActivity.this);
        boolean restoreFlag = prefs.getBoolean("restore", false);
        if(restoreFlag){
            pdia = new ProgressDialog(dataRestoreActivity.this);
            pdia.setMessage("Restoring Data Please Wait");
            pdia.show();
            // while (! LoginActivity.testRestoreData.getStatus().equals(AsyncTask.Status.FINISHED));
            TestRestoreData testRestoreData = new TestRestoreData();
            testRestoreData.execute((Void) null);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_data_restore, menu);
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
    public class TestRestoreData extends AsyncTask<Void, Void, Boolean> {




        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }


        @Override
        protected Boolean doInBackground(Void... params) {
            DatabaseHandler databaseHandler = new DatabaseHandler(dataRestoreActivity.this);
            List<document_obj> listDocument = databaseHandler.getAllDocuments();
            List<document_obj> listMedia = databaseHandler.getAllMedia();

            for (int i = 0; i < listDocument.size(); i++) {
                downloadFile(listDocument.get(i).get_id(), listDocument.get(i).get_doc_path());
            }
//            for (int i = 0; i < listMedia.size(); i++) {
//                downloadFile(listMedia.get(i).get_id(), listMedia.get(i).get_doc_path());
//            }

            return null;
        }

        @Override
        protected void onPostExecute(final Boolean success) {


            pdia.dismiss();

        }

        //i: patient id
        //s: file path
        public void downloadFile(int i, String s) {
            int totalSize = 0;
            int downloadedSize = 0;
            DatabaseHandler databaseHandler = new DatabaseHandler(dataRestoreActivity.this);
            int CustomerId = databaseHandler.getPersonalInfo().get_customerId();
            File file1 = new File(s);
            File file2 = file1.getParentFile();
            s= file2.getPath();
            String dwnload_file_path = "http://docbox.co.in/sajid/" + CustomerId + "/" + String.valueOf(i) + "/" + file1.getName();
            try {
                URL url = new URL(dwnload_file_path);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(true);

                //connect
                urlConnection.connect();

                //set the path where we want to save the file
                File SDCardRoot = Environment.getExternalStorageDirectory();
                //create a new file, to save the downloaded file
                File file = new File(file2, file1.getName());

                FileOutputStream fileOutput = new FileOutputStream(file);

                //Stream used for reading the data from the internet
                InputStream inputStream = urlConnection.getInputStream();

                //this is the total size of the file which we are downloading
                totalSize = urlConnection.getContentLength();


                //create a buffer...
                byte[] buffer = new byte[1024];
                int bufferLength = 0;

                while ((bufferLength = inputStream.read(buffer)) > 0) {
                    fileOutput.write(buffer, 0, bufferLength);
                    downloadedSize += bufferLength;
                    // update the progressbar //

                }
                //close the output stream when complete //
                fileOutput.close();


            } catch (final Exception e) {
                e.printStackTrace();
            }
        }


        public void doSomething() {

        }


    }
}

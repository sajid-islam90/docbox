package com.example.sajid.myapplication;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import java.io.File;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.example.sajid.myapplication.R;

public class UploadToServer extends Activity  {


    /*********  work only for Dedicated IP ***********/
    static final String FTP_HOST= "ftp.docbox.co.in";

    /*********  FTP USERNAME ***********/
    static final String FTP_USER = "sajid@docbox.co.in";

    /*********  FTP PASSWORD ***********/
    static final String FTP_PASS  ="aimsaims";
    String filePath;
    String fileName;

    Button btn;
    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);


                setContentView(R.layout.activity_upload_to_server);
                Intent intent =  getIntent();
                filePath = intent.getStringExtra("filePath");
                // fileName = intent.getStringExtra("fileName");


              Dowork();







    }

    public void Dowork() {


        new Thread(new Runnable() {
            public void run() {
                File f = new File(filePath);

                // Upload sdcard file
                uploadFile(f);

            }
        }).start();






        /********** Pick file from sdcard *******/


    }

    public void uploadFile(File fileName){


        FTPClient client = new FTPClient();

        try {

            client.connect(FTP_HOST, 21);
            client.login(FTP_USER, FTP_PASS);
            client.setType(FTPClient.TYPE_BINARY);
            client.changeDirectory("/upload/");

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

    public class MyTransferListener implements FTPDataTransferListener {

        public void started() {


            // Transfer started
           // Toast.makeText(getBaseContext(), " Upload Started ...", Toast.LENGTH_SHORT).show();
            //System.out.println(" Upload Started ...");
        }

        public void transferred(int length) {

            // Yet other length bytes has been transferred since the last time this
            // method was called
           // Toast.makeText(getBaseContext(), " transferred ..." + length, Toast.LENGTH_SHORT).show();
            //System.out.println(" transferred ..." + length);
        }

        public void completed() {



           // Toast.makeText(getBaseContext(), " completed ...", Toast.LENGTH_SHORT).show();
            //System.out.println(" completed ..." );
        }

        public void aborted() {


          //  Toast.makeText(getBaseContext()," transfer aborted ,
                    //please try again...", Toast.LENGTH_SHORT).show();
            //System.out.println(" aborted ..." );
        }

        public void failed() {


            // Transfer failed
            System.out.println(" failed ..." );
        }

    }
}
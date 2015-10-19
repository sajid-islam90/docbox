package com.example.sajid.myapplication;

import java.io.File;
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

    public static void Dowork(ArrayList<String> path,String Name, final String CustomerId) {
        final ArrayList<String>filePath = path;
        patientName = Name;


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

            try {
                client.createDirectory( patientName);

            }
            catch (Exception e) {
                e.printStackTrace();
            }

            client.changeDirectory("/"+CustomerId+"/"+patientName +"//");


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

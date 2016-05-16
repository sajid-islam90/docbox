/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package utilityClasses;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;


import com.elune.sajid.myapplication.R;
import activity.patients_today;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import objects.bundle;

public class ChatConnection {

    private Handler mUpdateHandler;
    private ChatServer mChatServer;
    private ChatClient mChatClient;
    private  Context mContext;
    static NotificationCompat.Builder mBuilder;
    static NotificationManager notifier;

    private static final String TAG = "ChatConnection";
    String accountType;
    private Socket mSocket;
    private int mPort = -1;
    private static int receiverCounter = 0;
    NsdHelper mNsdHelper;

    public ChatConnection(Handler handler,Context context,  NsdHelper NsdHelper) {
        mUpdateHandler = handler;
        mNsdHelper = NsdHelper;
        mContext = context;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        accountType  = prefs.getString(context.getString(R.string.account_type), "");
        //if(!accountType.equals(context.getString(R.string.account_type_helper))) {
        mChatServer = new ChatServer(handler);
    //}
    }

    public void tearDown() {
        if(mChatServer!=null)
        mChatServer.tearDown();
        if(mChatClient!=null)
        mChatClient.tearDown();
    }

    public void connectToServer(InetAddress address, int port) {
        mChatClient = new ChatClient(address, port);
    }

    public void sendMessage(String msg) {
        if (mChatClient != null) {

            mChatClient.sendMessage(msg);
        }
    }

    public int getLocalPort() {
        return mPort;
    }

    public void setLocalPort(int port) {
        mPort = port;
    }



    public synchronized void updateMessages(String msg, boolean local) {
        Log.e(TAG, "Updating message: " + msg);


        Bundle messageBundle = new Bundle();
        messageBundle.putString("msg", msg);
        byte[] bytes = new byte[0];



        Message message = new Message();
        message.setData(messageBundle);
        mUpdateHandler.sendMessage(message);

    }

    private synchronized void setSocket(Socket socket) {
        Log.d(TAG, "setSocket being called.");
        if (socket == null) {
            Log.d(TAG, "Setting a null socket.");
        }
        if (mSocket != null) {
            if (mSocket.isConnected()) {
                try {
                    mSocket.close();
                } catch (IOException e) {
                    // TODO(alexlucas): Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        mSocket = socket;
    }

    private Socket getSocket() {
        return mSocket;
    }

    private class ChatServer {
        ServerSocket mServerSocket = null;
        Thread mThread = null;

        public ChatServer(Handler handler) {
            mThread = new Thread(new ServerThread());
            mThread.start();
        }

        public void tearDown() {
            mThread.interrupt();
//            try {
//               // mServerSocket.close();
//            } catch (IOException ioe) {
//                Log.e(TAG, "Error when closing server socket.");
//            }
        }

        class ServerThread implements Runnable {

            @Override
            public void run() {

                try {
                    // Since discovery will happen via Nsd, we don't need to care which port is
                    // used.  Just grab an available one  and advertise it via Nsd.
                    mServerSocket = new ServerSocket(0);
                    setLocalPort(mServerSocket.getLocalPort());
                   // setLocalPort(6060);


//                    if(mServerSocket!=null)
//                    { if(mServerSocket.getLocalPort()!= 6060)
//                    { mServerSocket = new ServerSocket(6060);
//                    //setLocalPort(mServerSocket.getLocalPort());
//                    setLocalPort(6060);}}
//                    else if(mServerSocket == null)
//                    {
//                        mServerSocket = new ServerSocket(6060);
//                        //setLocalPort(mServerSocket.getLocalPort());
//                        setLocalPort(6060);
//                    }
                    Log.d(TAG, "Server Thread created");
                    while (!Thread.currentThread().isInterrupted()) {
                        Log.d(TAG, "ServerSocket Created, awaiting connection");
                        if(!mServerSocket.isClosed())
                        setSocket(mServerSocket.accept());
                        Log.d(TAG, "Connected." + mServerSocket.getInetAddress() + " : " + mServerSocket.getLocalPort());

                        if (mChatClient == null) {
                            int port = mSocket.getPort();
                            InetAddress address = mSocket.getInetAddress();
                            connectToServer(address, port);
                        }
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Error creating ServerSocket: ", e);
                    e.printStackTrace();
                }
            }
        }
    }

    private class ChatClient {

        private InetAddress mAddress;
        private int PORT;

        private final String CLIENT_TAG = "ChatClient";

        private Thread mSendThread;
        private Thread mRecThread;

        public ChatClient(InetAddress address, int port) {

            Log.d(CLIENT_TAG, "Creating chatClient");
            this.mAddress = address;
            this.PORT = port;

            mSendThread = new Thread(new SendingThread());
            mSendThread.start();
        }

        class SendingThread implements Runnable {

            BlockingQueue<String> mMessageQueue;
            private int QUEUE_CAPACITY = 10;

            public SendingThread() {
                mMessageQueue = new ArrayBlockingQueue<String>(QUEUE_CAPACITY);
            }

            @Override
            public void run() {
                try {
                    if (getSocket() == null) {
                        setSocket(new Socket(mAddress, PORT));
                        Log.d(CLIENT_TAG, "Client-side socket initialized.");
                        ((AppCompatActivity)(mContext)).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "Connected to "+ mAddress +" "+PORT, Toast.LENGTH_SHORT).show();


                            }
                        });

                    } else {
                        Log.d(CLIENT_TAG, "Socket already initialized. skipping!");
                        ((AppCompatActivity)(mContext)).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "Reconnected to "+ mAddress +" "+PORT, Toast.LENGTH_SHORT).show();


                            }
                        });

                    }
                    Bundle messageBundle = new Bundle();
                    messageBundle.putString("msg", "connected");
                    Message message = new Message();
                    message.setData(messageBundle);
                    mUpdateHandler.sendMessage(message);
                    Log.d(CLIENT_TAG, "port "+PORT);
                    Log.d(CLIENT_TAG, "address "+mAddress);
//while(true)
//{
                     mRecThread = new Thread(new ReceivingThread());
                     mRecThread.start();
//}

                } catch (UnknownHostException e) {
                    Log.d(CLIENT_TAG, "Initializing socket failed, UHE", e);
                } catch (IOException e) {
                    Log.d(CLIENT_TAG, "Initializing socket failed, IOE.", e);
                    ((AppCompatActivity)(mContext)).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                    mContext);
                            alertDialogBuilder
                                    .setCancelable(false)
                                    .setTitle("Connection Lost")
                                    .setMessage("Reconnect?")
                                    .setPositiveButton("OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    // get user input and set it to result
                                                    // edit text
                                                    if(accountType.equals(mContext.getString(R.string.account_type_doctor)))
                                                    {
                                                        mNsdHelper.tearDown();
                                                        tearDown();
                                                        mContext.startActivity(new Intent(mContext, patients_today.class));
                                                        ((AppCompatActivity) (mContext)).finish();}
                                                    else
                                                    {
                                                        mContext.startActivity(new Intent(mContext,patients_today.class));
                                                    ((AppCompatActivity) (mContext)).finish();

                                                    }


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
                           // Toast.makeText(mContext, "Connection Lost to doctor\nPlease reconnect", Toast.LENGTH_SHORT).show();


                        }
                    });

                    try {
                        setSocket(new Socket(mAddress, PORT));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                }


//                while (true) {
//                    try {
//                        String msg = mMessageQueue.take();
//                        sendMessage(msg);
//                    } catch (InterruptedException ie) {
//                        Log.d(CLIENT_TAG, "Message sending loop interrupted, exiting");
//                    }
//                }
            }
        }

        class ReceivingThread implements Runnable {


            private Object mPauseLock;
            private boolean mPaused;
            private boolean mFinished;

            public ReceivingThread() {
                mPauseLock = new Object();
                mPaused = false;
                mFinished = false;
            }

            @Override
            public void run() {

                BufferedReader input;

                try {
//                    DataInputStream dis =
//                            new DataInputStream(
//                                    new BufferedInputStream(mSocket.getInputStream()));
                    DataInputStream dis =
                            new DataInputStream(
                                    mSocket.getInputStream());

if(mSocket.isClosed())
{
    if (accountType.equals(mContext.getString(R.string.account_type_doctor))) {
        mNsdHelper.tearDown();
        tearDown();
        mContext.startActivity(new Intent(mContext, patients_today.class));
        ((AppCompatActivity) (mContext)).finish();
    } else {
        mContext.startActivity(new Intent(mContext, patients_today.class));
        ((AppCompatActivity) (mContext)).finish();
    }

}
                    ObjectInputStream ois = new ObjectInputStream(mSocket.getInputStream());

                    DatabaseHandler databaseHandler = new DatabaseHandler(mContext);
                    bundle bundleData =  new bundle() ;
                    try {
                        Object object =  ois.readObject();
                        bundleData = (bundle)object;
                       // bundleData.setFilePath(dis.readUTF()); introduce objectinputStream
                      //  bundleData.setFilePath(ois.readUTF());
                        //objectOutputStream.close();
                        // bundleData.setNumber(dis.readInt());
                        Log.d(CLIENT_TAG, "Read from the stream: "+mSocket.getInetAddress()+" "+mSocket.getPort());
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(CLIENT_TAG, "Data input Stream went kaboom");
                    }
                    if(bundleData.getBytes()!=null)
                    {
                        if(accountType.equals(mContext.getString(R.string.account_type_helper))) {

                            String helperPath = databaseHandler.getMappedHelperDocPath(bundleData.getFilePath());
                            Log.e(CLIENT_TAG, helperPath);
                            bundleData.setFilePath(helperPath);
                        }
                         FileOutputStream fos = new FileOutputStream(bundleData.getFilePath());

                        fos.write(bundleData.getBytes());
                        fos.close();
                        final bundle finalBundleData = bundleData;
                        ((AppCompatActivity) (mContext)).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                Toast.makeText(mContext,"Document Received at "+ finalBundleData.getFilePath(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    if (!bundleData.getFilePath().equals("")) {
                        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        Intent myIntent = new Intent(mContext, patients_today.class);
                        PendingIntent intent2 = PendingIntent.getBroadcast(mContext, 1,
                                myIntent, PendingIntent.FLAG_UPDATE_CURRENT
                                        | PendingIntent.FLAG_ONE_SHOT);
                        mBuilder =
                                new NotificationCompat.Builder(mContext)
                                        .setSmallIcon(R.drawable.icon_notification)
                                        .setContentIntent(intent2)
                                        .setContentTitle("DocBox")
                                        .setContentText("New Patient Added")
                        .setSound(alarmSound);

                        notifier = (NotificationManager)
                                mContext.getSystemService(Context.NOTIFICATION_SERVICE);

                        notifier.notify(1, mBuilder.build());
                        ((AppCompatActivity)(mContext)).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Toast.makeText(mContext, bundleData.getFilePath() + " " + bundleData.getNumber(), Toast.LENGTH_SHORT).show();
                                Toast.makeText(mContext, "received data", Toast.LENGTH_SHORT).show();

                            }
                        });

                        if(!bundleData.getFilePath().equals("thanks")) {

                            updateMessages(bundleData.getFilePath(), false);

                        }
                       // dis.close();
                        receiverCounter = 0;
                       // ois.close();


                        this.run();

                    } else {


                            ((AppCompatActivity) (mContext)).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(mContext,"No Data received\n Receiver Thread going to close "+receiverCounter, Toast.LENGTH_SHORT).show();
                                }
                            });
                        Log.d(CLIENT_TAG, "No Data received\n" +
                                " Receiver Thread going to close");
if(receiverCounter<=1)
{
    receiverCounter++;
    ois.close();
    this.run();
}
                        try {
                            if(getSocket()==null){
                                Log.d(CLIENT_TAG, "New socket being initialised");
                            setSocket(new Socket(mAddress, PORT));}
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                }

                catch (Exception e) {
                    Log.e(CLIENT_TAG, "Server loop error: ", e);
                    ((AppCompatActivity) (mContext)).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Toast.makeText(mContext, "Connection  lost \nPlease Reconnect", Toast.LENGTH_SHORT).show();
                            if (accountType.equals(mContext.getString(R.string.account_type_doctor))) {
                                mNsdHelper.tearDown();
                                tearDown();
                                mContext.startActivity(new Intent(mContext, patients_today.class));
                                ((AppCompatActivity) (mContext)).finish();
                            } else {
                                mContext.startActivity(new Intent(mContext, patients_today.class));
                                ((AppCompatActivity) (mContext)).finish();
                            }
                        }
                    });

                }
            }

        }

        public void tearDown() {
            try {
                getSocket().close();
            } catch (IOException ioe) {
                Log.e(CLIENT_TAG, "Error when closing server socket.");
            }
        }

        public void sendMessage(String msg) {
            try {
                Socket socket = getSocket();
//                File file = new File(
//                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/Patient Manager/",
//                        "video.mp4");
                DatabaseHandler databaseHandler = new DatabaseHandler(mContext);

                BufferedInputStream bis;

                Log.d(CLIENT_TAG, "started sending");
                Log.d(CLIENT_TAG, "receiver socket values");
                Log.d(CLIENT_TAG, String.valueOf(socket.getPort()));
                Log.d(CLIENT_TAG, String.valueOf(socket.getInetAddress()));
                Log.d(CLIENT_TAG, "sender socket values");
                Log.d(CLIENT_TAG, String.valueOf(mSocket.getPort()));
                Log.d(CLIENT_TAG, String.valueOf(mSocket.getInetAddress()));
                // msg = new String(bytes);
                if (socket == null) {
                    Log.d(CLIENT_TAG, "Socket is null, wtf?");
                } else if (socket.getOutputStream() == null) {
                    Log.d(CLIENT_TAG, "Socket output stream is null, wtf?");
                }

//                DataOutputStream dos =
//                        new DataOutputStream( new BufferedOutputStream(socket.getOutputStream()) );
//                DataOutputStream dos =
//                        new DataOutputStream( socket.getOutputStream());

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                File file1 = new File(msg);
                if(accountType.equals(mContext.getString(R.string.account_type_helper))) {
                    String helperDocPath = databaseHandler.getMappedHelperDocPath(msg);
                    file1 = new File(helperDocPath);

                }


                bundle bundleData = new bundle();

                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

              //  bundleData.setNumber(29);
                bundleData.setFilePath(msg);

                if(file1.exists())
                {
//                    if(accountType.equals(mContext.getString(R.string.account_type_helper))) {
//
//                        String docPath = databaseHandler.getMappedDoctorDocPath(bundleData.getFilePath());
//                        Log.e(CLIENT_TAG, docPath);
//                        bundleData.setFilePath(docPath);
//                    }

                    byte[] bytes = new byte[(int) file1.length()];
                    FileInputStream fileInputStream = new FileInputStream(file1);
                    fileInputStream.read(bytes);
                    fileInputStream.close();

                bundleData.setBytes(bytes);}
               // dos.writeUTF(bundleData.getFilePath()); introducing obk=ject output stream
                // dos.writeInt(bundleData.getNumber());//
                oos.writeObject(bundleData);
//                oos.writeUTF(bundleData.getFilePath());
                Log.d(CLIENT_TAG, bundleData.getFilePath());
                Log.d(CLIENT_TAG, String.valueOf(bundleData.getNumber()));
               // dos.flush();  introducing obk=ject output stream
                oos.flush();



            } catch (UnknownHostException e) {
                Log.d(CLIENT_TAG, "Unknown Host", e);
            } catch (IOException e) {
                Log.d(CLIENT_TAG, "I/O Exception", e);
            } catch (Exception e) {
                Log.d(CLIENT_TAG, "Error3", e);
            }
            Log.d(CLIENT_TAG, "Client sent message: " + msg);
        }
    }
}

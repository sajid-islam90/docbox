package utilityClasses;
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

import android.content.Context;
import android.content.SharedPreferences;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.preference.PreferenceManager;
import android.util.Log;


import com.elune.sajid.myapplication.R;

import java.io.Serializable;

import objects.personal_obj;

public class NsdHelper implements Serializable {

    Context mContext;
    String accountType;
    NsdManager mNsdManager;
    NsdManager.ResolveListener mResolveListener;
    NsdManager.DiscoveryListener mDiscoveryListener;
    NsdManager.RegistrationListener mRegistrationListener;

    public static final String SERVICE_TYPE = "_http._tcp.";

    public static final String TAG = "NsdHelper";


    public String mServiceName = "";

   public static NsdServiceInfo mService;

    public NsdHelper(Context context) {
        mContext = context;
        DatabaseHandler databaseHandler = new DatabaseHandler(context);
        mNsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        accountType  = prefs.getString(context.getString(R.string.account_type), "");

        if(accountType.equals(context.getString(R.string.account_type_helper))) {
            String attachedDoctorEmail =  prefs.getString("attachedDoctorEmail", "");
            mServiceName = attachedDoctorEmail;
        }
        else
        {
            personal_obj personalObj = databaseHandler.getPersonalInfo();
            mServiceName = "NsdServer"+personalObj.get_email();
        }
    }

    public void initializeNsd() {
        if(accountType.equals(mContext.getString(R.string.account_type_helper)))
        {initializeResolveListener();
        initializeDiscoveryListener();}
        initializeRegistrationListener();

        //mNsdManager.init(mContext.getMainLooper(), this);

    }

    public void initializeDiscoveryListener() {
        mDiscoveryListener = new NsdManager.DiscoveryListener() {

            @Override
            public void onDiscoveryStarted(String regType) {
                Log.d(TAG, "Service discovery started");
            }

            @Override
            public void onServiceFound(NsdServiceInfo service) {
                Log.d(TAG, "Service discovery success" + service);
                if (!service.getServiceType().equals(SERVICE_TYPE)) {
                    Log.d(TAG, "Unknown Service Type: " + service.getServiceType());
                } else if (service.getServiceName().equals(mServiceName)) {
                    Log.d(TAG, "Same machine: " + mServiceName);
                } else if (service.getServiceName().contains(mServiceName)){
                    try {
                        mNsdManager.resolveService(service, mResolveListener


//                            new NsdManager.ResolveListener() {
//                        @Override
//                        public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
//                            Log.e(TAG, "Resolve failed" + errorCode);
//                        }
//
//                        @Override
//                        public void onServiceResolved(NsdServiceInfo serviceInfo) {
//                            Log.e(TAG, "Resolve Succeeded. " + serviceInfo);
//
//                            if (serviceInfo.getServiceName().equals(mServiceName)) {
//                                Log.d(TAG, "Same IP.");
//                                return;
//                            }
//                            mService = serviceInfo;
//                        }
//                    }
                        );
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onServiceLost(NsdServiceInfo service) {
                Log.e(TAG, "service lost" + service);
                if (mService == service) {
                    mService = null;
                }
            }

            @Override
            public void onDiscoveryStopped(String serviceType) {
                Log.i(TAG, "Discovery stopped: " + serviceType);
            }

            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery failed: Error code:" + errorCode);
                mNsdManager.stopServiceDiscovery(this);
            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery failed: Error code:" + errorCode);
                mNsdManager.stopServiceDiscovery(this);
            }
        };
    }

    public void initializeResolveListener() {
        mResolveListener = new NsdManager.ResolveListener() {

            @Override
            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                Log.e(TAG, "Resolve failed" + errorCode);
            }

            @Override
            public void onServiceResolved(NsdServiceInfo serviceInfo) {
                Log.e(TAG, "Resolve Succeeded. "+mServiceName+" " + serviceInfo);
                stopDiscovery();
                if (serviceInfo.getServiceName().equals(mServiceName)) {
                    Log.d(TAG, "Same IP.");
                    return;
                }
                mService = serviceInfo;
            }
        };
    }

    public void initializeRegistrationListener() {
        mRegistrationListener = new NsdManager.RegistrationListener() {

            @Override
            public void onServiceRegistered(NsdServiceInfo NsdServiceInfo) {
                mServiceName = NsdServiceInfo.getServiceName();
                Log.e(TAG, mServiceName+"Service registered " );
            }

            @Override
            public void onRegistrationFailed(NsdServiceInfo arg0, int arg1) {
            }

            @Override
            public void onServiceUnregistered(NsdServiceInfo arg0) {
                Log.e(TAG, arg0.getServiceName()+"Service unregistered " );
            }

            @Override
            public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
            }

        };
    }

    public void registerService(int port) {
        NsdServiceInfo serviceInfo  = new NsdServiceInfo();
        serviceInfo.setPort(port);
        serviceInfo.setServiceName(mServiceName);
        serviceInfo.setServiceType(SERVICE_TYPE);

        mNsdManager.registerService(
                serviceInfo, NsdManager.PROTOCOL_DNS_SD, mRegistrationListener);

    }
    public void reRegisterService(int port) {
        NsdServiceInfo serviceInfo  = new NsdServiceInfo();
        serviceInfo.setPort(port);
        serviceInfo.setServiceName(mServiceName);
        serviceInfo.setServiceType(SERVICE_TYPE);
        initializeRegistrationListener();
        mNsdManager.registerService(
                serviceInfo, NsdManager.PROTOCOL_DNS_SD, mRegistrationListener);

    }

    public void discoverServices() {

        mNsdManager.discoverServices(
                SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener


//                new NsdManager.DiscoveryListener() {
//
//                    @Override
//                    public void onDiscoveryStarted(String regType) {
//                        Log.d(TAG, "Service discovery started");
//                    }
//
//                    @Override
//                    public void onServiceFound(NsdServiceInfo service) {
//                        Log.d(TAG, "Service discovery success" + service);
//                        if (!service.getServiceType().equals(SERVICE_TYPE)) {
//                            Log.d(TAG, "Unknown Service Type: " + service.getServiceType());
//                        } else if (service.getServiceName().equals(mServiceName)) {
//                            Log.d(TAG, "Same machine: " + mServiceName);
//                        } else if (service.getServiceName().contains(mServiceName)){
//                            mNsdManager.resolveService(service, new NsdManager.ResolveListener() {
//                                @Override
//                                public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
//                                    Log.e(TAG, "Resolve failed" + errorCode);
//                                }
//
//                                @Override
//                                public void onServiceResolved(NsdServiceInfo serviceInfo) {
//                                    Log.e(TAG, "Resolve Succeeded. " + serviceInfo);
//
//                                    if (serviceInfo.getServiceName().equals(mServiceName)) {
//                                        Log.d(TAG, "Same IP.");
//                                        return;
//                                    }
//                                    mService = serviceInfo;
//                                }
//                            });
//                        }
//                    }
//
//                    @Override
//                    public void onServiceLost(NsdServiceInfo service) {
//                        Log.e(TAG, "service lost " + service);
//                        if (mService == service) {
//                            mService = null;
//                        }
//                    }
//
//                    @Override
//                    public void onDiscoveryStopped(String serviceType) {
//                        Log.i(TAG, "Discovery stopped: " + serviceType);
//                    }
//
//                    @Override
//                    public void onStartDiscoveryFailed(String serviceType, int errorCode) {
//                        Log.e(TAG, "Discovery failed: Error code:" + errorCode);
//                        mNsdManager.stopServiceDiscovery(this);
//                    }
//
//                    @Override
//                    public void onStopDiscoveryFailed(String serviceType, int errorCode) {
//                        Log.e(TAG, "Discovery failed: Error code:" + errorCode);
//                        mNsdManager.stopServiceDiscovery(this);
//                    }
//                }
 );
    }

    public void stopDiscovery() {
        mNsdManager.stopServiceDiscovery(mDiscoveryListener);
    }

    public NsdServiceInfo getChosenServiceInfo() {
        return mService;
    }

    public void tearDown() {
        try {
            mNsdManager.unregisterService(mRegistrationListener);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

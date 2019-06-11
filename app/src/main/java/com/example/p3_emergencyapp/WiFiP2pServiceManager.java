package com.example.p3_emergencyapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.ChannelListener;
import android.net.wifi.p2p.WifiP2pManager.DnsSdServiceResponseListener;
import android.net.wifi.p2p.WifiP2pManager.DnsSdTxtRecordListener;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.util.Map;
import java.util.Objects;
import java.util.Random;

import static android.net.wifi.SupplicantState.*;

/**
 * Created by mac on 15/11/2017.
 */

public class WiFiP2pServiceManager {
    private static WiFiP2pServiceManager instance;

    public static String TAG = "SERVICE_MANAGER";

    private WifiManager wifiManager;
    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;

    private Context context;
    public static String deviceId = Integer.toString(new Random().nextInt(10000));

    public String getThisDeviceId() {
        return deviceId;
    }

    private WiFiP2pServiceManager() {
    }

    public static WiFiP2pServiceManager getInstance() {
        if (instance == null) {
            instance = new WiFiP2pServiceManager();
        }
        return instance;
    }

    public void initialize(final Context context) {
        Log.i("ID", "ID:"+deviceId);
        this.context = context;
        // JobManager.getInstance().initialize(context);
        wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        manager = (WifiP2pManager) context.getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(context, context.getMainLooper(), new ChannelListener() {
            @Override
            public void onChannelDisconnected() {
                Log.e(TAG, "Channel LOST ITS GG. Do something about it");
            }
        });
        cleanup();
        prepareServiceDiscovery();
    }

//    private void enableWifi() {
//        while (!wifiManager.isWifiEnabled()) {
//            Log.e(TAG, "WiFi is disabled! Asking to enable WIFI");
//            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    switch (which) {
//                        case DialogInterface.BUTTON_POSITIVE:
//                            wifiManager.setWifiEnabled(true);
//                            break;
//                        case DialogInterface.BUTTON_NEGATIVE:
//                            Toast.makeText(context, "Wrong way lil boy. Not handled at all. Bad programmers. Bad", Toast.LENGTH_SHORT).show();
//                            break;
//                    }
//                }
//            };
//            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            builder.setMessage(R.string.SET_WIFI)
//                    .setPositiveButton(R.string.YES, dialogClickListener)
//                    .setNegativeButton(R.string.NO, dialogClickListener)
//                    .show();
//        }
//        Log.d(TAG, "WiFI is enabled");
//    }

    public void addLocalService(final String instanceName, final String serviceType, final Map serviceFlyer) {
        manager.clearLocalServices(channel, new ActionListener() {
            @Override
            public void onSuccess() {
                WifiP2pDnsSdServiceInfo serviceInfo
                        = WifiP2pDnsSdServiceInfo.newInstance(instanceName, serviceType, serviceFlyer);

                manager.addLocalService(channel, serviceInfo, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "Added local service " + instanceName + " of type " + serviceType);

                    }

                    @Override
                    public void onFailure(int reason) {
                        Log.e(TAG, "Error adding local service");
                    }
                });
            }

            @Override
            public void onFailure(int reason) {
                Log.e(TAG, "Cannot clear local services: " + reason);
            }
        });
    }

    public void addServiceRequest() {
        manager.clearServiceRequests(channel, new ActionListener() {
            @Override
            public void onSuccess() {
                WifiP2pDnsSdServiceRequest serviceRequest = WifiP2pDnsSdServiceRequest.newInstance();
                manager.addServiceRequest(channel, serviceRequest, new ActionListener() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailure(int reason) {
                        Log.e(TAG, "Cannot Request a Service:" + reason);
                    }
                });
            }

            @Override
            public void onFailure(int reason) {
                Log.e(TAG, "Cannot remove service request:" + reason);
            }
        });
    }

    public void broadcastServiceCall(ServiceFlyer serviceFlyer) {
        addLocalService(serviceFlyer.get("INSTANCE_NAME"), "_emgmsg.tcp", serviceFlyer);
        // JobManager.getInstance().startBroadcastingService();
        addServiceRequest();
        startServiceDiscovery();
    }

    public void prepareServiceDiscovery() {
        DnsSdTxtRecordListener txtRecordListener = new DnsSdTxtRecordListener() {
            @Override
            public void onDnsSdTxtRecordAvailable(
                    String fullDomainName, Map<String, String> txtRecordMap, WifiP2pDevice srcDevice) {
                Log.d(TAG, "Service flyer available: " + txtRecordMap.toString());

                if (txtRecordMap.get("INSTANCE_NAME").equals("_g307p3.call")) {
                    if (txtRecordMap.get("SOURCE_DEVICE_ID").equals(deviceId)) {
                        // dont do anything coz its the same relay
                        Log.w(TAG, "This device received its own HELP signal from another device.");
                    } else {

                        Log.d(TAG, "Source device ID: " + txtRecordMap.get("SOURCE_DEVICE_ID") + ", This device ID: " + deviceId);
                        // store all the deviceIDs inside something, if any ofthese is already inside this network, skip it.
                        Log.i(TAG, "Rebroadcasting...");
                        cleanup();
                        ServiceFlyer serviceFlyer = new ServiceFlyer(
                                txtRecordMap.get("SOURCE_DEVICE_ID"),
                                txtRecordMap.get("INSTANCE_NAME"),
                                txtRecordMap.get("LOCATION_X"),
                                txtRecordMap.get("LOCATION_Y"),
                                txtRecordMap.get("PASSING_DEVICES"));
                        serviceFlyer.mark(deviceId);
                        if (wifiFound("pawel")) {
                            String cmd = serviceFlyer.get("SOURCE_DEVICE_ID") + "-"
                                    + serviceFlyer.get("LOCATION_X") + "-"
                                    + serviceFlyer.get("LOCATION_Y") + "-"
                                    + serviceFlyer.get("PASSING_DEVICES");
                            Thread clientThread = new ClientThread(cmd);
                            clientThread.start();
                        } else {
                            broadcastServiceCall(serviceFlyer);
                        }
                    }
                }
//                if (txtRecordMap.get("INSTANCE_NAME").equals("_g307p3.response")) {
//                    // send response service ad with new ServiceFlyer.
//                }
            }
        };

        DnsSdServiceResponseListener serviceRespondListener = new DnsSdServiceResponseListener() {
            @Override
            public void onDnsSdServiceAvailable(String instanceName, String registrationType,
                                                WifiP2pDevice srcDevice) {
            }
        };
        manager.setDnsSdResponseListeners(channel, serviceRespondListener, txtRecordListener);
    }

    public void startServiceDiscovery() {
        manager.clearServiceRequests(channel, new ActionListener() {
            @Override
            public void onSuccess() {
                WifiP2pDnsSdServiceRequest serviceRequest = WifiP2pDnsSdServiceRequest.newInstance();
                manager.addServiceRequest(channel, serviceRequest, new ActionListener() {
                    @Override
                    public void onSuccess() {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    discoverServices();
                                }
                            }, 2000);
//                        discoverServices();
                    }

                    @Override
                    public void onFailure(int i) {

                    }
                });
            }

            @Override
            public void onFailure(int i) {

            }
        });
         // JobManager.getInstance().startServiceDiscovery();
    }

    public boolean wifiFound(String soughtSsid){
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        String ssid;

        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo.getSupplicantState() == COMPLETED) {
            ssid = wifiInfo.getSSID();
            Log.i(TAG, "wifiFound: " + ssid);
        } else {
            Log.e(TAG, "WiFi getSupplicantState not COMPLETED!");
            return false;
        }
        return ssid.equals("\""+soughtSsid+"\"");
    }

    public void miniCleanup() {
        manager.clearLocalServices(channel, new ActionListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int reason) {

            }
        });
    }

    public void cleanup() {
        // JobManager.getInstance().cleanup();
        manager.stopPeerDiscovery(channel, new ActionListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int reason) {

            }
        });
        manager.clearServiceRequests(channel, new ActionListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int reason) {

            }
        });
        manager.clearLocalServices(channel, new ActionListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int reason) {

            }
        });
    }

    public void discoverServices() {
        manager.discoverServices(channel, new ActionListener() {
            @Override
            public void onSuccess() {
                Log.i(TAG, "Discovering...");
            }

            @Override
            public void onFailure(int reason) { Log.i(TAG, "gg..." + reason);

            }
        });
    }

    public void discoverPeers() {
        manager.discoverPeers(channel, new ActionListener() {
            @Override
            public void onSuccess() {}

            @Override
            public void onFailure(int reason) {}
        });
    }
}






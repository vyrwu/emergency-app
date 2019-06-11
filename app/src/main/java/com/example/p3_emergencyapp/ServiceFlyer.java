package com.example.p3_emergencyapp;

import android.util.Log;

import java.util.HashMap;

/**
 * Created by mac on 15/11/2017.
 */

public class ServiceFlyer extends HashMap<String, String> {
    private String sourceDeviceId;
    private String instanceName;
    private String senderLocationX;
    private String senderLocationY;
    private String passingDevices;
//    private String deviceIdRoad;
    // Location, other info, etc.

    public ServiceFlyer(String sourceDeviceId,
                        String instanceName,
                        String senderLocationX,
                        String senderLocationY,
                        String passingDevices) {
        this.sourceDeviceId = sourceDeviceId;
        this.instanceName = instanceName;
        this.senderLocationX = senderLocationX;
        this.senderLocationY = senderLocationY;
        this.passingDevices = passingDevices;
//        this.deviceIdRoad = "";
        super.put("SOURCE_DEVICE_ID", sourceDeviceId);
        super.put("INSTANCE_NAME", instanceName);
        super.put("LOCATION_X", senderLocationX);
        super.put("LOCATION_Y", senderLocationY);
        super.put("PASSING_DEVICES", sourceDeviceId);
    }

    public void mark(String deviceId) {
        String newFootprint = get("PASSING_DEVICES") + " " + deviceId;
        super.put("PASSING_DEVICES", newFootprint);
    }
}

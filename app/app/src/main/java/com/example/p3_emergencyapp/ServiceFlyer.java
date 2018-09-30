package com.example.p3_emergencyapp;

import java.util.HashMap;

/**
 * Created by mac on 15/11/2017.
 */

public class ServiceFlyer extends HashMap {
    private String sourceDeviceId;
    private String instanceName;
    private String senderLocationX;
    private String senderLocationY;
    // Location, other info, etc.

    public ServiceFlyer(String sourceDeviceId, String instanceName, String senderLocationX, String senderLocationY) {
        this.sourceDeviceId = sourceDeviceId;
        this.instanceName = instanceName;
        this.senderLocationX = senderLocationX;
        this.senderLocationY = senderLocationY;
        super.put("SOURCE_DEVICE_ID", sourceDeviceId);
        super.put("INSTANCE_NAME", instanceName);
        super.put("LOCATION_X", senderLocationX);
        super.put("LOCATION_Y", senderLocationY);
    }

}

package com.example.bluetooth_gps_scanner;

public class DeviceData
{
    String deviceName;
    String deviceAddress;
    Integer deviceType;
    String locationKey;

    DeviceData(String deviceAddress, String deviceName, Integer deviceType, String locationKey)
    {
        this.deviceAddress = deviceAddress;
        this.deviceName = deviceName;
        this.deviceType = deviceType;
        this.locationKey = locationKey;
    }

    DeviceData()
    {

    }
}

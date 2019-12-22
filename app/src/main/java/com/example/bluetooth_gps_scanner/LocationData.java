package com.example.bluetooth_gps_scanner;

import android.bluetooth.BluetoothDevice;
import android.content.ServiceConnection;
import android.location.LocationListener;

public class LocationData
{
    double latitude;
    double longitude;
    String deviceName;
    String deviceAddress;
    int deviceType;
    BluetoothDevice device;
    long timeStamp;

    LocationData(double latitude, double longitude)
    {
        this.latitude = latitude;
        this.longitude = longitude;
        timeStamp = System.currentTimeMillis();
    }

    LocationData(double latitude, double longitude, BluetoothDevice device)
    {
        this.latitude = latitude;
        this.longitude = longitude;
        timeStamp = System.currentTimeMillis();
        this.device = device;
        deviceAddress = device.getAddress();
        deviceName = device.getName();
        deviceType = device.getBluetoothClass().getDeviceClass();
    }

    LocationData()
    {

    }

    @Override
    public String toString() {
        return super.toString();
    }
}

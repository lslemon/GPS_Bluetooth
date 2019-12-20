package com.example.bluetooth_gps_scanner;

import android.bluetooth.BluetoothDevice;
import android.content.ServiceConnection;
import android.location.LocationListener;

public class LocationData
{
    public double latitude;
    public double longitude;
    public String deviceName;
    public String deviceAddress;
    public String deviceType;
    public BluetoothDevice device;
    public long timeStamp;

    public LocationData(double latitude, double longitude)
    {
        this.latitude = latitude;
        this.longitude = longitude;
        timeStamp = System.currentTimeMillis();
    }

    public LocationData(double latitude, double longitude, BluetoothDevice device)
    {
        this.latitude = latitude;
        this.longitude = longitude;
        timeStamp = System.currentTimeMillis();
        this.device = device;
    }

    public LocationData()
    {

    }

    @Override
    public String toString() {
        return super.toString();
    }
}

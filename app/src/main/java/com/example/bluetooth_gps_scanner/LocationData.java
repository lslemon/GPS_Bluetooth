package com.example.bluetooth_gps_scanner;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.ServiceConnection;
import android.location.LocationListener;

import java.util.ArrayList;

public class LocationData
{
    double latitude;
    double longitude;
    String deviceName;
    String deviceAddress;
    Integer deviceType  = null;
    ArrayList<Device> devices;
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
        deviceAddress = device.getAddress();
        deviceName = device.getName();
        deviceType = device.getBluetoothClass().getDeviceClass();
    }

    LocationData(double latitude, double longitude, ArrayList<BluetoothDevice> bDevices)
    {
        this.latitude = latitude;
        this.longitude = longitude;
        timeStamp = System.currentTimeMillis();
        devices = createDeviceList(bDevices);
    }

    public ArrayList<Device> createDeviceList(ArrayList<BluetoothDevice> bDevices)
    {
        ArrayList<Device> devices = new ArrayList<>();
        for(BluetoothDevice bDevice: bDevices)
        {
            devices.add(new Device(bDevice.getName(), bDevice.getAddress(), bDevice.getBluetoothClass().getDeviceClass()));
        }
        return devices;
    }

    LocationData()
    {

    }

    @Override
    public String toString() {
        return super.toString();
    }

    class Device
    {
        String deviceName;
        String deviceAddress;
        Integer deviceType  = null;

        Device(String deviceName, String deviceAddress, Integer deviceType)
        {
            this.deviceName = deviceName;
            this.deviceAddress = deviceAddress;
            this.deviceType = deviceType;
        }
    }
}

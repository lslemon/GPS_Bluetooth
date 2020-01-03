package com.example.bluetooth_gps_scanner;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class BluetoothService extends Service
{
    private final String TAG = BluetoothService.class.getSimpleName();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference locationsRef = database.getReference("locations");
    private DatabaseReference deviceRef = database.getReference("devices");

    public static boolean handlerNotify = false;
    private ArrayList<BluetoothDevice> devList= new ArrayList<BluetoothDevice>();
    private BluetoothAdapter bluetoothAdapter;

    private String key;

    final Runnable r = new Runnable() {
        @Override
        public void run() {
            handlerNotify = false;
            Log.i(TAG, "Termiante Scan");
            bluetoothAdapter.cancelDiscovery();

            for(BluetoothDevice device: devList)
            {
                DeviceData deviceData = new DeviceData(device.getAddress(), device.getName(), device.getBluetoothClass().getDeviceClass(), key);
                deviceRef.child(device.getAddress()).setValue(deviceData);
            }
            devList.clear();
        }
    };

    /**
     * FOLLOWING LINES ARE FOR BINDING SERVICE TO
     * AN ACTIVITY
     */
    public class LocalBinder extends Binder {
        BluetoothService getService() {
            return BluetoothService.this;
        }
    }

    private final IBinder mBinder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent)
    {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return mBinder;
    }

    public void scanDevices(Context context)
    {
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        context.registerReceiver(mReceiver, filter);

        if(handlerNotify==true)
        {
            Log.i(TAG, "Initiating Scan");
            bluetoothAdapter.startDiscovery();
        }
    }

    public LocationListener getLocationListener()
    {
        return locationListener;
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location)
        {
            LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
            key =  locationsRef.push().getKey();
            LocationData locationData = new LocationData(latLng.latitude, latLng.longitude);
            locationsRef.child(key).setValue(locationData);
            Log.i(TAG, "Location Updated");
            handlerNotify = true;
            scanDevices(BluetoothService.this);
            new Handler().postDelayed(r, 1000*60);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    private final BroadcastReceiver mReceiver = new BroadcastReceiver()
    {
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action))
            {
            // Discovery has found a device. Get the BluetoothDevice
            // object and its info from the Intent.
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            devList.add(device);
            Log.i("Device Name: " , device.getName());
            Log.i("deviceHardwareAddress " , device.getAddress());
            }
        }
    };

        }

package com.example.bluetooth_gps_scanner;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BluetoothService extends Service
{
    private final String TAG = BluetoothService.class.getSimpleName();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference locationsRef = database.getReference("locations");

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
        return mBinder;
    }

    public LocationListener getLocationListener() {
        return locationListener;
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location)
        {
            LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
            String key =  locationsRef.push().getKey();
            LocationData locationData = new LocationData(latLng.latitude, latLng.longitude);
            locationsRef.child(key).setValue(locationData);
            Log.i("PRP","Location Changed");

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
}

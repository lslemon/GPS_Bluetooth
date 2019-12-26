package com.example.bluetooth_gps_scanner;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class BluetoothService extends Service {
    private final String TAG = BluetoothService.class.getSimpleName();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("locations");
    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");



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

    /*//Permissions are clarified in Opening Activity
    public void startGettingLocations(String provider)
    {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        long MIN_DISTANCE_CHANGE_FOR_UPDATES = 5;// Distance in meters
        long MIN_TIME_BW_UPDATES = 1000 * 10 * 1;// Time in milliseconds

        lm.requestLocationUpdates(
                provider,
                MIN_TIME_BW_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES,
                locationListener);
    }*/

    public LocationListener getLocationListener() {
        return locationListener;
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
            String key =  myRef.push().getKey();
            //LocationData locationData = new LocationData(location.getLatitude(), location.getLongitude());
            LocationData locationData = new LocationData(latLng.latitude, latLng.longitude);
            myRef.child(key).setValue(locationData);
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

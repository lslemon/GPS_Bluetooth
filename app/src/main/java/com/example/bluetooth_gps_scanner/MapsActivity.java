package com.example.bluetooth_gps_scanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.example.bluetooth_gps_scanner.LocationData;
import com.example.bluetooth_gps_scanner.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback
{
    private final String TAG = MapsActivity.class.getSimpleName();
    private GoogleMap mMap;
    private Marker markerLocation;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("locations");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        Log.i(TAG, "CALL ME");
        double co_ords[] = intent.getDoubleArrayExtra("Co-Ords");
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(co_ords[0], co_ords[1]));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(co_ords[0], co_ords[1])));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(co_ords[0], co_ords[1]), 15f));
    }

    private void addMarker(LatLng latLng) {
        if (latLng == null) {
            return;
        }
        if (markerLocation != null) {
            markerLocation.remove();
        }
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("New Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        if (mMap != null)
            markerLocation = mMap.addMarker(markerOptions);


        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latLng.latitude, latLng.longitude))
                .zoom(16)
                .build();

        if (mMap != null)
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        myRef.addChildEventListener(childEventListener);

        /*
        If the activity opened from DeviceListActivty
        zoom to the selected device
         */
        Intent intent = getIntent();
        if(intent.getDoubleArrayExtra("Co-Ords") != null)
        {
            Log.i(TAG, "CALL ME");
            double co_ords[] = intent.getDoubleArrayExtra("Co-Ords");
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(co_ords[0], co_ords[1]));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(co_ords[0], co_ords[1])));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(co_ords[0], co_ords[1]), 15f));
        }
    }



    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
        {
            LocationData location = dataSnapshot.getValue(LocationData.class);
            LatLng loc = new LatLng(location.latitude, location.longitude);
            mMap.addMarker(new MarkerOptions().position(loc).title(location.toString()));
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }

    };
}

package com.example.bluetooth_gps_scanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback
{
    private final String TAG = MapsActivity.class.getSimpleName();
    private GoogleMap mMap;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference locationsRef = database.getReference("locations");
    private DatabaseReference deviceRef = database.getReference("devices");
    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        locationsRef.addChildEventListener(locationsChildEventListener);

        /*
        If the activity opened from DeviceListActivty
        zoom to the selected device
         */
        Intent intent = getIntent();
        /*
        IF the intent is from device list activity then
        when the map is ready it will zoom into the selected
        marker
         */
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

    private ChildEventListener devicesChildEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
        {

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

    private ChildEventListener locationsChildEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
        {
            LocationData location = dataSnapshot.getValue(LocationData.class);
            LatLng loc = new LatLng(location.latitude, location.longitude);
            Calendar cal = GregorianCalendar.getInstance();
            cal.setTimeInMillis(location.timeStamp);
            mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(20)).position(loc).title(dateFormat.format(cal.getTime())));
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
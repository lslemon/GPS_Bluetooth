package com.example.bluetooth_gps_scanner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DeviceListActivity extends AppCompatActivity
{
    private final String TAG = DeviceListActivity.class.getSimpleName();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference locationRef = database.getReference("locations");
    private DatabaseReference deviceRef = database.getReference("devices");
    private ListView listView;
    private DeviceListAdapter deviceListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_list_view);
        listView = findViewById(R.id.deviceListView);
        deviceListAdapter = new DeviceListAdapter(this, 0);
        listView.setAdapter(deviceListAdapter);
        listView.setOnItemClickListener(itemClickListener);
        //locationRef.addChildEventListener(childEventListener);
        deviceRef.addChildEventListener(childEventListener);
    }

    public class DeviceListAdapter extends ArrayAdapter<DeviceData>
    {

        private ArrayList<DeviceData> devices;
        private Context context;

        public DeviceListAdapter(Context context, int resourceInt) {
            super(context, resourceInt);
            this.context = context;
            devices = new ArrayList<>();
        }

        @Override
        public void add(DeviceData device) {
            devices.add(device);
            notifyDataSetChanged();
        }

        @Override
        public DeviceData getItem(int position) {
            return devices.get(position);
        }

        @Override
        public int getCount() {
            return devices.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if(view == null)
            {
                LayoutInflater inflater = ((Activity)context).getLayoutInflater();
                view = inflater.inflate(R.layout.device_list_entry, null);
                ViewHolder viewHolder = new ViewHolder(view);
                view.setTag(viewHolder);
            }

            DeviceData device = devices.get(position);
            ViewHolder holder = (ViewHolder)view.getTag();
            holder.deviceName.setText(device.deviceName);
            holder.addressView.setText(device.deviceAddress);
            holder.deviceType.setText(""+device.deviceType);

            if(device.deviceType == 1)
            {
                holder.imageView.setImageDrawable(getDrawable(R.drawable.ic_heartbeat));
            }
            else
            {
                holder.imageView.setImageDrawable(getDrawable(R.drawable.ic_laptop_windows_black_24dp));
            }
            return view;
        }
    }

    public class ViewHolder
    {
        public TextView deviceName;
        public TextView addressView;
        public TextView deviceType;
        public ImageView imageView;


        public ViewHolder(View view)
        {
            deviceName = view.findViewById(R.id.deviceName);
            addressView = view.findViewById(R.id.macAddress);
            deviceType = view.findViewById(R.id.deviceType);
            imageView = view.findViewById(R.id.imageView);
        }
    }

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
        {
            DeviceData deviceData = (DeviceData)adapterView.getItemAtPosition(position);
            locationRef.child(deviceData.locationKey).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    LocationData locationData = dataSnapshot.getValue(LocationData.class);
                    Intent mapsIntent = new Intent(DeviceListActivity.this, MapsActivity.class);
                    double co_ords[] = new double[2];
                    co_ords[0] = locationData.latitude;
                    co_ords[1] = locationData.longitude;
                    mapsIntent.putExtra("Co-Ords", co_ords);
                    mapsIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(mapsIntent);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    };



    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
        {
            //LocationData locationEntry = dataSnapshot.getValue(LocationData.class);
            DeviceData deviceEntry = dataSnapshot.getValue(DeviceData.class);
            deviceListAdapter.add(deviceEntry);

            /*if (locationEntry.deviceName != null)
            {
                deviceListAdapter.add(locationEntry);
            }*/
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
package com.example.bluetooth_gps_scanner;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bluetooth_gps_scanner.LocationData;
import com.example.bluetooth_gps_scanner.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DeviceListActivity extends AppCompatActivity
{
    private final String TAG = DeviceListActivity.class.getSimpleName();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("locations");
    private ListView listView;
    private DeviceListAdapter deviceListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_list_view);
        listView = findViewById(R.id.deviceListView);
        deviceListAdapter = new DeviceListAdapter(this, 0);
        listView.setAdapter(deviceListAdapter);
//        myRef.addValueEventListener(valueEventListener);
        myRef.addChildEventListener(childEventListener);
    }

    public class DeviceListAdapter extends ArrayAdapter<LocationData>
    {

        private ArrayList<LocationData> locationDevices;
        private Context context;

        public DeviceListAdapter(Context context, int resourceInt) {
            super(context, resourceInt);
            this.context = context;
            locationDevices = new ArrayList<>();
        }

        @Override
        public void add(LocationData location) {
            locationDevices.add(location);
            notifyDataSetChanged();
        }

        @Override
        public LocationData getItem(int position) {
            return locationDevices.get(position);
        }

        @Override
        public int getCount() {
            return locationDevices.size();
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

            LocationData device = locationDevices.get(position);
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

    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
        {
            LocationData locationEntry = dataSnapshot.getValue(LocationData.class);
            if (locationEntry.deviceName != null)
            {
                deviceListAdapter.add(locationEntry);
            }
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
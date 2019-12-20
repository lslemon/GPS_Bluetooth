package com.example.bluetooth_gps_scanner;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.text.Layout;
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

import java.util.ArrayList;

public class DeviceListActivity extends AppCompatActivity
{

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
        myRef.addChildEventListener(childEventListener);
    }

    public class DeviceListAdapter extends ArrayAdapter<BluetoothDevice>
    {

        private ArrayList<BluetoothDevice> devices;
        private Context context;

        public DeviceListAdapter(Context context, int resourceInt) {
            super(context, resourceInt);
            this.context = context;
            ArrayList<BluetoothDevice> devices = new ArrayList<>();
        }

        @Override
        public void add(BluetoothDevice device) {
            devices.add(device);
        }

        @Override
        public BluetoothDevice getItem(int position) {
            return devices.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if(view == null)
            {
                LayoutInflater inflater = ((Activity)context).getLayoutInflater();
                view = inflater.inflate(R.layout.device_list_entry, null);
                ViewHolder viewHolder = new ViewHolder((TextView)findViewById(R.id.deviceNameView),
                        (TextView)findViewById(R.id.addressView), (TextView)findViewById(R.id.deviceTypeView));
                view.setTag(viewHolder);
            }

            BluetoothDevice device = devices.get(position);
            ViewHolder holder = (ViewHolder)view.getTag();
            holder.deviceName.setText(device.getName());
            holder.addressView.setText(device.getAddress());
            holder.deviceType.setText(""+device.getType());
            return view;
        }
    }

    private class ViewHolder
    {
        public TextView deviceName;
        public TextView addressView;
        public TextView deviceType;
        public ImageView imageView;

        public ViewHolder(TextView deviceName, TextView addressView, TextView type)
        {
            this.deviceName = deviceName;
            this.addressView = addressView;
            this.deviceType = type;
        }
    }

    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
        {
            LocationData location = dataSnapshot.getValue(LocationData.class);
            deviceListAdapter.add(location.device);
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

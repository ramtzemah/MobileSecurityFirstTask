package com.example.securitiesles1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;


public class MainActivity extends AppCompatActivity implements ChargeBroadcast.OnChargeReceivedListener, WifiBroadcast.OnWifiReceivedListener{
    private MaterialButton first;
    private MaterialButton contactB;
    private MaterialTextView text;
    private MaterialTextView contact;
    private MaterialTextView wifi;
    private MaterialTextView charge;
    private MaterialTextView stable;
    private EditText batteryLevelText;
    private SensorManager mSensorManager;
    private MovementSensors sensors;
    private SensorEventListener sensorEventListener = initSensorListener();
    private BatteryManager myBatteryManager;
    private boolean isCharge;
    private boolean isStable;
    private boolean isWIFI;
    private boolean isContactPermission;
    private final double deviation = 1.5;
    private ConnectivityManager cm;
    private NetworkInfo activeNetwork;
    private ChargeBroadcast chargeBroadcast;
    private WifiBroadcast wifiBroadcast;
    private int batteryLevel;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();

        isUSBCharging();
        isWIFIConnect();

        chargeBroadcast.setOnChargeReceivedListener(this);
        wifiBroadcast.setOnWifiReceivedListener(this);

        first.setOnClickListener(v -> {
            //
            if(isCharge && isStable && isWIFI && isContactPermission) {
                if(batteryLevelText.getText().toString().isEmpty()){
                    Toast.makeText(this, "Wrong battery percent",Toast.LENGTH_LONG).show();
                }
                else if(batteryLevel == Integer.parseInt(batteryLevelText.getText().toString())){
                    Intent intent = new Intent(MainActivity.this, secondActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(this, "Wrong battery percent",Toast.LENGTH_LONG).show();
                }
            }else {
                Toast.makeText(this, "You didn't do enough things to move on to the next level",Toast.LENGTH_LONG).show();
            }
        });


        contactB.setOnClickListener(v -> {
            checkPermission();
        });

        checkPermission();
    }

    private void checkPermission() {
        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_CONTACTS)!=
        PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_CONTACTS},100);
            isContactPermission = false;
            contact.setText("contact: false");
        }else{
            isContactPermission = true;
            contact.setText("contact: true");
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void isUSBCharging(){
        isCharge = myBatteryManager.isCharging();
        if(isCharge){
            charge.setText("charge: true");
        }else{
            charge.setText("charge: false");
        }
    }
    public void isWIFIConnect(){
        activeNetwork = cm.getActiveNetworkInfo();
        if(activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                isWIFI = true;
                wifi.setText("wifi: true");
            } else {
                isWIFI = false;
                wifi.setText("wifi: false");
            }
        }else {
            isWIFI = false;
            wifi.setText("wifi: false");
        }
    }

    private SensorEventListener initSensorListener() {
        return new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float y = sensorEvent.values[1];
                if(y >= -(deviation) && y <= deviation){
                    isStable = true;
                    stable.setText("stable: true");
                }else{
                    isStable = false;
                    stable.setText("stable: false");
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
    }

    private void findViews() {
        first = findViewById(R.id.first);
        contact = findViewById(R.id.contact);
        text = findViewById(R.id.text);
        wifi = findViewById(R.id.wifi);
        contactB = findViewById(R.id.contactB);
        charge = findViewById(R.id.charge);
        stable = findViewById(R.id.stable);
        batteryLevelText = findViewById(R.id.batteryLevel);
        //// Movement ////
        // Get sensor manager
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        // Get the default sensor of specified type
        sensors = new MovementSensors()
                .setActivity(this)
                .setSensorManager(mSensorManager)
                .setAccSensor(mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));

        //// Charge ////
        myBatteryManager = (BatteryManager) this.getSystemService(Context.BATTERY_SERVICE);

        //// WIFI ////
         cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        chargeBroadcast = new ChargeBroadcast();
        wifiBroadcast = new WifiBroadcast();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();
        sensors.registerListener(sensorEventListener,SensorManager.SENSOR_DELAY_NORMAL);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        registerReceiver(chargeBroadcast,intentFilter);
        IntentFilter intent = new IntentFilter();
        intent.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        registerReceiver(wifiBroadcast,intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensors != null) {
            sensors.unregisterListener();
        }
    }

    @Override
    public void onChargeReceived(boolean isCharging, float batteryPct) {
        isCharge = isCharging;
        if(isCharge){
            charge.setText("charge: true");
        }else{
            charge.setText("charge: false");
        }
        batteryLevel = (int)(batteryPct);
    }


    public void onWifiReceived(boolean isConnectWifi) {
        isWIFI = isConnectWifi;
        if(isWIFI){
            wifi.setText("wifi: true");
        }else{
            wifi.setText("wifi: false");
        }
    }
}

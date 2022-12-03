package com.example.securitiesles1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.widget.Toast;


public class ChargeBroadcast extends BroadcastReceiver {

    public interface OnChargeReceivedListener {
        public void onChargeReceived(boolean isCharging, float batteryPct);
    }

    private OnChargeReceivedListener listener = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        float batteryPct = level * 100 / (float)scale;
        if(intent.getAction().equals((Intent.ACTION_POWER_CONNECTED))){
            if (listener != null) {
                listener.onChargeReceived(true, batteryPct);
            }
        }else if(intent.getAction().equals((Intent.ACTION_POWER_DISCONNECTED))){
            if (listener != null) {
                listener.onChargeReceived(false, batteryPct
                );
            }
        }
    }

    public void setOnChargeReceivedListener(Context context) {
        this.listener = (OnChargeReceivedListener) context;
    }

}

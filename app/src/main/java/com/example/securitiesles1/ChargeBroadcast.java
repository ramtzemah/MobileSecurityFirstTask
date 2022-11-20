package com.example.securitiesles1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


public class ChargeBroadcast extends BroadcastReceiver {

    public interface OnChargeReceivedListener {
        public void onChargeReceived(boolean isCharging);
    }

    private OnChargeReceivedListener listener = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals((Intent.ACTION_POWER_CONNECTED))){
            if (listener != null) {
                listener.onChargeReceived(true);
            }
        }else if(intent.getAction().equals((Intent.ACTION_POWER_DISCONNECTED))){
            if (listener != null) {
                listener.onChargeReceived(false);
            }
        }
    }

    public void setOnChargeReceivedListener(Context context) {
        this.listener = (OnChargeReceivedListener) context;
    }

}

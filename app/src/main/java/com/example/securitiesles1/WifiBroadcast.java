package com.example.securitiesles1;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.widget.Toast;


public class WifiBroadcast extends BroadcastReceiver {

    public interface OnWifiReceivedListener {
        public void onWifiReceived(boolean isConnectWifi);
    }

    private OnWifiReceivedListener listener = null;
    private ConnectivityManager cm;
    private NetworkInfo activeNetwork;

    @Override
    public void onReceive(Context context, Intent intent) {
        findViews(context);
        activeNetwork = cm.getActiveNetworkInfo();
        if(activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                if (listener != null) {
                    listener.onWifiReceived(true);
                }
            } else {
                if (listener != null) {
                    listener.onWifiReceived(false);
                }
            }
        }else {
            if (listener != null) {
                listener.onWifiReceived(false);
            }
        }
    }

    public void setOnWifiReceivedListener(Context context) {
        this.listener = (OnWifiReceivedListener) context;
    }

    private void findViews(Context context) {
        //// WIFI ////
        cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

    }
}

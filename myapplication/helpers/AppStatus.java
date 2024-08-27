package com.example.yuvallehman.myapplication.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class AppStatus {
    private static final String TAG = "AppStatus";
    private boolean connected = false;
    private ConnectivityManager connectivityManager;
    private Context context;

    public AppStatus(Context context2) {
        this.context = context2;
    }

    public boolean isOnline() {
        try {
            this.connectivityManager = (ConnectivityManager) this.context.getSystemService("connectivity");
            NetworkInfo networkInfo = this.connectivityManager.getActiveNetworkInfo();
            this.connected = networkInfo != null && networkInfo.isConnected();
            return this.connected;
        } catch (Exception e) {
            Log.e(TAG, "isOnline: catch " + e.getMessage());
            return this.connected;
        }
    }
}

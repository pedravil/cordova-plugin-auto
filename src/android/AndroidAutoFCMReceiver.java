package com.androidauto.messaging;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;

public class AndroidAutoFCMReceiver extends BroadcastReceiver {

    private final static boolean DEBUG = true;
    private static final String TAG = AndroidAutoFCMReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG, "OnNotificationOpenReceiver onReceive called");
        
        Bundle data = intent.getExtras();
        data.putBoolean("tap", true);

        AndroidAutoPlugin.getFCMNotification(data);
        
    }
}

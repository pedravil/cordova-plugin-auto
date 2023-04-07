package com.androidauto.messaging;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationManagerCompat;

import android.util.Log;

public class AndroidAutoMessagingReceiver extends BroadcastReceiver {
    private static final String TAG = AndroidAutoMessagingReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (AndroidAutoMessagingService.READ_ACTION.equals(intent.getAction())) {
            int conversationId = intent.getIntExtra(AndroidAutoMessagingService.CONVERSATION_ID, -1);
            if (conversationId != -1) {
                Log.d(TAG, "Conversation " + conversationId + " was read");
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                notificationManager.cancel(conversationId);
            }
        }
    }
}

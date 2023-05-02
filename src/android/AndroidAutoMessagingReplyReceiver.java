package com.androidauto.messaging;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.RemoteInput;
import androidx.core.app.NotificationManagerCompat;

import org.apache.cordova.CallbackContext;

import android.util.Log;

/**
 * A receiver that gets called when a reply is sent to a given conversationId
 */
public class AndroidAutoMessagingReplyReceiver extends BroadcastReceiver {

    private CallbackContext callbackContext;

    private final static boolean DEBUG = true;
	private static final String TAG = AndroidAutoMessagingReplyReceiver.class.getSimpleName();

    public AndroidAutoMessagingReplyReceiver(ContextCall callbackContext) {

        this.callbackContext = callbackContext;

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (DEBUG) Log.d(TAG, "Reply Receiver: onReceive");

        if (AndroidAutoMessagingService.REPLY_ACTION.equals(intent.getAction())) {

            int conversationId = intent.getIntExtra(AndroidAutoMessagingService.CONVERSATION_ID, -1);

            CharSequence reply = getMessageText(intent);
            
            if (DEBUG) Log.d(TAG, "Got reply (" + reply + ") for ConversationId " + conversationId);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.cancel(conversationId);

            this.callbackContext.success(reply);
        }
    }

    /**
     * Get the message text from the intent.
     * Note that you should call {@code RemoteInput#getResultsFromIntent(intent)} to process
     * the RemoteInput.
     */
    private CharSequence getMessageText(Intent intent) {

        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            return remoteInput.getCharSequence(AndroidAutoMessagingService.EXTRA_VOICE_REPLY);
        }

        return null;

    }
}

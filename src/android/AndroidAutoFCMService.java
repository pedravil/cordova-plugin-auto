package com.androidauto.messaging;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import android.util.Log;

import android.os.Bundle;

import java.util.List;
import java.util.Map;
import java.util.Random;

import android.text.TextUtils;


public class AndroidAutoFCMService extends FirebaseMessagingService {

    private final static boolean DEBUG = true;
    private static final String TAG = AndroidAutoFCMService.class.getSimpleName();;
    
    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        String title;
        String text;
        String id;
        
        Map<String, String> data = remoteMessage.getData();
        
        if (remoteMessage.getNotification() != null) {

            title = remoteMessage.getNotification().getTitle();
            text = remoteMessage.getNotification().getBody();
            id = remoteMessage.getMessageId();

        } else {

            title = data.get("title");
            text = data.get("text");
            id = data.get("id");
            
            if (TextUtils.isEmpty(text)) {
                text = data.get("body");
            }
            if (TextUtils.isEmpty(text)) {
                text = data.get("message");
            }
        }

        if(DEBUG){
            Log.d(TAG, "From: " + remoteMessage.getFrom());
            Log.d(TAG, "Notification Message id: " + id);
            Log.d(TAG, "Notification Message Title: " + title);
            Log.d(TAG, "Notification Message Body/Text: " + text);
        }


        Bundle bundle = new Bundle();

        for (String key : data.keySet()) {
            bundle.putString(key, data.get(key));
        }
            
        bundle.putString("title", title);
        bundle.putString("body", text);


        AndroidAutoPlugin.getFCMNotification(bundle);
        
    }
    
}
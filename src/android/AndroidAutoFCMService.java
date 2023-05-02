
package com.androidauto.messaging;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import android.util.Log;

import java.util.List;
import java.util.Map;
import java.util.Random;

import android.text.TextUtils;

public class AndroidAutoFCMService extends FirebaseMessagingService {

    private static final String TAG = AndroidAutoFCMService.class.getSimpleName();;
    protected static final String KEY = "badge";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
    */
    @Override
    public void onNewToken(String token) {

      // Log.d(TAG, "Refreshed token: " + token);
      // FirebasePlugin.sendToken(token);

    }

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
      // [START_EXCLUDE]
      // There are two types of messages data messages and notification messages. Data messages are handled
      // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
      // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
      // is in the foreground. When the app is in the background an automatically generated notification is displayed.
      // When the user taps on the notification they are returned to the app. Messages containing both notification
      // and data payloads are treated as notification messages. The Firebase console always sends notification
      // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
      // [END_EXCLUDE]
      
      
      // TODO(developer): Handle FCM messages here.
      // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
      String title;
      String text;
      String id;
      String sound = null;
      String lights = null;
      Map<String, String> data = remoteMessage.getData();
      
      if (remoteMessage.getNotification() != null) {
        title = remoteMessage.getNotification().getTitle();
        text = remoteMessage.getNotification().getBody();
        id = remoteMessage.getMessageId();
      } else {
        title = data.get("title");
        text = data.get("text");
        id = data.get("id");
        sound = data.get("sound");
        lights = data.get("lights"); //String containing hex ARGB color, miliseconds on, miliseconds off, example: '#FFFF00FF,1000,3000'

        if (TextUtils.isEmpty(text)) {
          text = data.get("body");
        }
        if (TextUtils.isEmpty(text)) {
          text = data.get("message");
        }
      }

      if (TextUtils.isEmpty(id)) {
        Random rand = new Random();
        int n = rand.nextInt(50) + 1;
        id = Integer.toString(n);
      }

      String badge = data.get("badge");

      Log.d(TAG, "From: " + remoteMessage.getFrom());
      Log.d(TAG, "Notification Message id: " + id);
      Log.d(TAG, "Notification Message Title: " + title);
      Log.d(TAG, "Notification Message Body/Text: " + text);
      Log.d(TAG, "Notification Message Sound: " + sound);
      Log.d(TAG, "Notification Message Lights: " + lights);
      Log.d(TAG, "Notification Badge: " + badge);

      if (badge != null && !badge.isEmpty()) {
        setBadgeNumber(badge);
      }

      // TODO: Add option to developer to configure if show notification when app on foreground
      if (!TextUtils.isEmpty(text) || !TextUtils.isEmpty(title) || (!data.isEmpty())) {

        boolean showNotification = (FirebasePlugin.inBackground() || !FirebasePlugin.hasNotificationsCallback()) && (!TextUtils.isEmpty(text) || !TextUtils.isEmpty(title));
        Log.d(TAG, "showNotification: " + (showNotification ? "true" : "false"));
        sendNotification(id, title, text, data, showNotification, sound, lights);
        
      }
    }
    
}
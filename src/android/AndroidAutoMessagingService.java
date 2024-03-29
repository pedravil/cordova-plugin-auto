package com.androidauto.messaging;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.Person;

import android.content.Intent;

import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Bundle;

import android.graphics.BitmapFactory;
import android.content.res.Resources;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationCompat.Action;
import androidx.core.app.NotificationCompat.MessagingStyle;
import androidx.core.app.NotificationCompat.CarExtender;
import androidx.core.app.NotificationCompat.CarExtender.UnreadConversation;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.RemoteInput;

import android.R;

import java.util.List;
import android.util.Log;


public class AndroidAutoMessagingService extends Service {

    public static final String READ_ACTION = "com.androidauto.messaging.ACTION_MESSAGE_READ";
    public static final String REPLY_ACTION = "com.androidauto.messaging.ACTION_MESSAGE_REPLY";
    
    public static final String EXTRA_VOICE_REPLY = "extra_voice_reply";

    public static final String CONVERSATION_ID = "conversationId";
    public static final String FROM = "from";
    public static final String BODY = "body";
    public static final String TITLE = "title";
        
    private final static boolean DEBUG = true;
	private static final String TAG = AndroidAutoMessagingService.class.getSimpleName();

    private final Messenger mMessenger = new Messenger(new IncomingHandler());
    private NotificationManagerCompat mNotificationManager;
    
    /**
    * Handler of incoming messages from clients.
    */
    class IncomingHandler extends Handler {
        
        @Override
        public void handleMessage(Message message) {
            
            if (DEBUG) Log.d(TAG, "handleMessage");

            Bundle data = message.getData();

            int conversationId = Integer.parseInt(data.getString(CONVERSATION_ID));
            String messageFrom = data.getString(FROM);
            String messageTitle = data.getString(TITLE);
            String messageBody = data.getString(BODY);
            
            sendNotification(conversationId, messageFrom, messageTitle, messageBody, System.currentTimeMillis());
            
        }
    }    

    @Override
    public void onCreate() {
        mNotificationManager = NotificationManagerCompat.from(getApplicationContext());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    private String getStringResource(String name) {
        return this.getString(this.getResources().getIdentifier(name, "string", this.getPackageName()));
    }

    private Intent createIntent(int conversationId, String action) {
        
        return new Intent()
                .addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
                .setAction(action)
                .putExtra(CONVERSATION_ID, conversationId);
    }

    private RemoteInput createReplyRemoteInput(){

        // Build a RemoteInput for receiving voice input in a Car Notification
        RemoteInput remoteInput = new RemoteInput.Builder(EXTRA_VOICE_REPLY)
                .setLabel("Reply by voice")
                .build();

        return remoteInput;

    }

    private PendingIntent replyPendingIntent(int conversationId, int requestCode){

         // Building a Pending Intent for the reply action to trigger
         return PendingIntent.getBroadcast(getApplicationContext(),
                                            requestCode,
                                            createIntent(conversationId, REPLY_ACTION),
                                            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);

    }

    private PendingIntent markAsReadPendingIntent(int conversationId, int requestCode){

         // A pending Intent for reads
         return PendingIntent.getBroadcast(getApplicationContext(),
                                            requestCode,
                                            createIntent(conversationId, READ_ACTION),
                                            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

    }

    private Action createReplyAction(int conversationId){

        
        Action action = new Action.Builder(R.drawable.ic_menu_add, "Reply", replyPendingIntent(conversationId, 1))
                                            // Provides context to what firing the Action does.
                                            .setSemanticAction(Action.SEMANTIC_ACTION_REPLY)

                                            // The action doesn't show any UI, as required by Android Auto.
                                            .setShowsUserInterface(false)

                                            // Don't forget the reply RemoteInput. Android Auto will use this to
                                            // make a system call that will add the response string into
                                            // the reply intent so it can be extracted by the messaging app.
                                            .addRemoteInput(createReplyRemoteInput())
                                            
                                            .build();
        
        return action;
    }

    private Action createMarkAsReadAction(int conversationId){

        Action action = new Action.Builder(R.drawable.ic_secure, "Mark as Read", markAsReadPendingIntent(conversationId, 1))
                                    .setSemanticAction(Action.SEMANTIC_ACTION_MARK_AS_READ)
                                    .setShowsUserInterface(false)
                                    .build();


        return action;
    }

    private MessagingStyle createMessagingStyle(String messageFrom, String messageTitle, String messageBody, long timestamp){

        MessagingStyle messagingStyle = new NotificationCompat.MessagingStyle("reply name")

                                            // Sets the conversation title. If the app's target version is lower
                                            // than P, this will automatically mark the conversation as a group (to
                                            // maintain backward compatibility). Use `setGroupConversation` after
                                            // setting the conversation title to explicitly override this behavior. See
                                            // the documentation for more information.
                                            .setConversationTitle(messageTitle)
    
                                            // Group conversation means there is more than 1 recipient, so set it as such.
                                            .setGroupConversation(false)

                                            //.bigText(messageBody)

                                            .addMessage(messageBody, timestamp, messageFrom);
    
        return messagingStyle;

    }

    private void sendNotification(int conversationId, String messageFrom, String messageTitle, String messageBody, long timestamp) {
        
        Action replyAction = createReplyAction(conversationId);

        Action markAsReadAction = createMarkAsReadAction(conversationId);

        MessagingStyle messagingStyle = createMessagingStyle(messageFrom, messageTitle, messageBody, timestamp);

        String channelId = this.getStringResource("default_aa_notification_channel_id");
        String channelName = this.getStringResource("default_aa_notification_channel_name");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                // Set the application notification icon:
                .setSmallIcon(getApplicationInfo().icon)

                // Shows in Android Auto as the conversation image.
                //.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_menu_add))

                // Adds MessagingStyle.
                .setStyle(messagingStyle)

                // Adds reply action.
                .addAction(replyAction)

                // Makes the mark-as-read action invisible, so it doesn't appear
                // in the Android UI but the app satisfies Android Auto's
                // mark-as-read Action requirement. Both required actions can be made
                // visible or invisible; it is a stylistic choice.
                .addInvisibleAction(markAsReadAction)
                
                .setAutoCancel(true);
        
        if (DEBUG) Log.d(TAG, "builder");

        // Since android Oreo notification channel is needed.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
              List<NotificationChannel> channels = mNotificationManager.getNotificationChannels();

              boolean channelExists = false;
              for (int i = 0; i < channels.size(); i++) {
                    if (channelId.equals(channels.get(i).getId())) {
                      channelExists = true;
                    }
              }

              if (!channelExists) {
                    NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);

                    mNotificationManager.createNotificationChannel(channel);
              }
        }

        mNotificationManager.notify(1, builder.build());
    }
}

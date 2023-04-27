package com.androidauto.messaging;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationCompat.CarExtender;
import androidx.core.app.NotificationCompat.CarExtender.UnreadConversation;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.RemoteInput;

import java.util.List;

public class AndroidAutoMessagingService extends Service {
    
    public static final String READ_ACTION = "com.androidauto.messaging.ACTION_MESSAGE_READ";
    public static final String REPLY_ACTION = "com.androidauto.messaging.ACTION_MESSAGE_REPLY";
    public static final String CONVERSATION_ID = "conversation_id";
    public static final String EXTRA_VOICE_REPLY = "extra_voice_reply";
    
    private static final String TAG = AndroidAutoMessagingService.class.getSimpleName();
    private final Messenger mMessenger = new Messenger(new IncomingHandler());
    private NotificationManagerCompat mNotificationManager;
    
    /**
    * Handler of incoming messages from clients.
    */
    class IncomingHandler extends Handler {
        
        @Override
        public void handleMessage(Message msg) {
            
            Bundle data = msg.getData();
            String dataString = data.getString("MyString");
            
            sendNotification(1, dataString, "John Doe", System.currentTimeMillis());
            
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
        return RemoteInput.Builder(EXTRA_VOICE_REPLY)
                .setLabel("Reply by voice")
                .build();

    }

    private PendingIntent replyPendingIntent(String conversationId){

         // Building a Pending Intent for the reply action to trigger
         return PendingIntent.getBroadcast(getApplicationContext(),
                                            conversationId,
                                            createIntent(conversationId, REPLY_ACTION),
                                            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

    }

    private PendingIntent markAsReadPendingIntent(String conversationId){

         // A pending Intent for reads
         return PendingIntent.getBroadcast(getApplicationContext(),
                                            conversationId,
                                            createIntent(conversationId, READ_ACTION),
                                            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

    }

    private Action replyAction(){

        return Action.Builder(R.drawable.reply, "Reply", replyPendingIntent())
                                // Provides context to what firing the Action does.
                                .setSemanticAction(Action.SEMANTIC_ACTION_REPLY)

                                // The action doesn't show any UI, as required by Android Auto.
                                .setShowsUserInterface(false)

                                // Don't forget the reply RemoteInput. Android Auto will use this to
                                // make a system call that will add the response string into
                                // the reply intent so it can be extracted by the messaging app.
                                .addRemoteInput(createReplyRemoteInput())
                                .build();
    }

    private Action markAsReadAction(){

        return Action.Builder(R.drawable.mark_as_read, "Mark as Read", markAsReadPendingIntent())
                                .setSemanticAction(Action.SEMANTIC_ACTION_MARK_AS_READ)
                                .setShowsUserInterface(false)
                                .build();
    }

    private MessageSMessagingStyletyle createMessagingStyle(String message){

        Person devicePerson = Person.Builder()
                                // The display name (also the name that's read aloud in Android auto).
                                .setName("Device Person")

                                // The icon to show in the notification shade in the system UI (outside
                                // of Android Auto).
                                //.setIcon(appDeviceUser.icon)

                                // A unique key in case there are multiple people in this conversation with
                                // the same name.
                                .setKey("Device Person Key")
                                .build();
        
        MessagingStyle messagingStyle = MessagingStyle(devicePerson);

        // Sets the conversation title. If the app's target version is lower
        // than P, this will automatically mark the conversation as a group (to
        // maintain backward compatibility). Use `setGroupConversation` after
        // setting the conversation title to explicitly override this behavior. See
        // the documentation for more information.
        messagingStyle.setConversationTitle("Conversation Title");
    
        // Group conversation means there is more than 1 recipient, so set it as such.
        messagingStyle.setGroupConversation(false);


        Person senderPerson = Person.Builder()
                                .setName("Sender Person Name")
                                //.setIcon(appMessage.sender.icon)
                                .setKey("Send Person Key")
                                .build();


        messagingStyle.addMessage("Message Body", System.currentTimeMillis(), senderPerson);
    
        return messagingStyle;

    }

    private void sendNotification(int conversationId, String message, String participant, long timestamp) {
        
        // // Create the UnreadConversation and populate it with the participant name,
        // // read and reply intents.
        // UnreadConversation.Builder unreadConvBuilder =
        //         new UnreadConversation.Builder(participant)
        //                 .setLatestTimestamp(timestamp)
        //                 .setReadPendingIntent(readPendingIntent)
        //                 .setReplyAction(replyIntent, remoteInput);
        
        String channelId = this.getStringResource("default_aa_notification_channel_id");
        String channelName = this.getStringResource("default_aa_notification_channel_name");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                // Set the application notification icon:
                .setSmallIcon(getApplicationInfo().icon)

                // Set the large icon, for example a picture of the other recipient of the message
                //.setLargeIcon(personBitmap)

                .setContentText(message)
                .setWhen(timestamp)
                .setContentTitle(participant)
                .setContentIntent(readPendingIntent)
                .setStyle(new NotificationCompat.InboxStyle())
                        .setTimeoutAfter(10000)
                        .setContentText("Hello")
                        //.addPerson(new Person.Builder().build()).build()
                .extend(new CarExtender()
                        .setUnreadConversation(unreadConvBuilder.build()));
        
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
        
        mNotificationManager.notify(conversationId, builder.build());
    }
}

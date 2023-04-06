package com.AndroidAuto;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationCompat.CarExtender;
import androidx.core.app.NotificationCompat.CarExtender.UnreadConversation;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.RemoteInput;

public class AndroidAutoNotificationSender {
    private Context context;

    public AndroidAutoNotificationSender(Context context) {
        this.context = context;
    }

    public void sendNotification(String title, String message) {
        // Create a notification builder with the appropriate settings
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel_id")
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setAutoCancel(true);

        // Create a bundle to hold the notification options
        Bundle extras = new Bundle();
        extras.putBoolean("android.support.v4.app.EXTRA_AUTO_HEADSUP", true);
        extras.putString("android.support.v4.app.EXTRA_CONVERSATION_TITLE", title);

        // Add the options to the builder
        builder.addExtras(extras);

        // Send the notification using the system's notification manager
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1, builder.build());
    }
}

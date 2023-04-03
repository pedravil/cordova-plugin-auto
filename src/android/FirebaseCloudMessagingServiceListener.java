import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import AndroidAutoNotificationSender;

public class FirebaseCloudMessagingServiceListener extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        
        // Check if message contains data payload.
        if (remoteMessage.getData().size() > 0) {
            // Get the notification data.
            String title = remoteMessage.getData().get("title");
            String body = remoteMessage.getData().get("body");
            String imageUrl = remoteMessage.getData().get("image_url");

            // Do something with the notification data.
            // For example, show a notification to the user.
            showNotification(title, body, imageUrl);
        }
    }

    private void showNotification(String title, String body, String imageUrl) {
        
        AndroidAutoNotificationSender sendNotificationToAndroidAuto = new AndroidAutoNotificationSender ():
        
        sendNotificationToAndroidAuto.sendNotification(title, body);
        
    }
}

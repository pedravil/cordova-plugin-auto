import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

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
        // Implement the code to show a notification to the user.
        // You can use the NotificationCompat.Builder class to create a notification.
        // You can also use an external library like Picasso to load the image from the URL.
    }
}

package p007io.invertase.firebase.messaging;

import android.content.Intent;
import android.support.p000v4.content.LocalBroadcastManager;
import android.util.Log;
import com.facebook.react.HeadlessJsTaskService;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import p007io.invertase.firebase.C1350Utils;

/* renamed from: io.invertase.firebase.messaging.RNFirebaseMessagingService */
public class RNFirebaseMessagingService extends FirebaseMessagingService {
    public static final String MESSAGE_EVENT = "messaging-message";
    public static final String NEW_TOKEN_EVENT = "messaging-token-refresh";
    public static final String REMOTE_NOTIFICATION_EVENT = "notifications-remote-notification";
    private static final String TAG = "RNFMessagingService";

    /* JADX WARNING: type inference failed for: r1v0, types: [android.content.Context, io.invertase.firebase.messaging.RNFirebaseMessagingService] */
    public void onNewToken(String str) {
        Log.d(TAG, "onNewToken event received");
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(NEW_TOKEN_EVENT));
    }

    /* JADX WARNING: type inference failed for: r3v0, types: [android.content.Context, io.invertase.firebase.messaging.RNFirebaseMessagingService] */
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "onMessageReceived event received");
        if (remoteMessage.getNotification() != null) {
            Intent intent = new Intent(REMOTE_NOTIFICATION_EVENT);
            intent.putExtra("notification", remoteMessage);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        } else if (C1350Utils.isAppInForeground(getApplicationContext())) {
            Intent intent2 = new Intent(MESSAGE_EVENT);
            intent2.putExtra("message", remoteMessage);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent2);
        } else {
            try {
                Intent intent3 = new Intent(getApplicationContext(), RNFirebaseBackgroundMessagingService.class);
                intent3.putExtra("message", remoteMessage);
                getApplicationContext().startService(intent3);
                HeadlessJsTaskService.acquireWakeLockNow(getApplicationContext());
            } catch (IllegalStateException e) {
                Log.e(TAG, "Background messages will only work if the message priority is set to 'high'", e);
            }
        }
    }
}

package p007io.invertase.firebase.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/* renamed from: io.invertase.firebase.notifications.RNFirebaseNotificationReceiver */
public class RNFirebaseNotificationReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        new RNFirebaseNotificationManager(context).displayScheduledNotification(intent.getExtras());
    }
}

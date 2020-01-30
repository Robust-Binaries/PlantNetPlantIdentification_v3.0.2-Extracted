package p007io.invertase.firebase.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/* renamed from: io.invertase.firebase.notifications.RNFirebaseNotificationsRebootReceiver */
public class RNFirebaseNotificationsRebootReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Log.i("RNFNotifRebootReceiver", "Received reboot event");
        new RNFirebaseNotificationManager(context).rescheduleNotifications();
    }
}

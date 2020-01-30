package p007io.invertase.firebase.notifications;

import android.content.Intent;
import com.facebook.react.HeadlessJsTaskService;
import com.facebook.react.jstasks.HeadlessJsTaskConfig;
import javax.annotation.Nullable;

/* renamed from: io.invertase.firebase.notifications.RNFirebaseBackgroundNotificationActionsService */
public class RNFirebaseBackgroundNotificationActionsService extends HeadlessJsTaskService {
    /* access modifiers changed from: protected */
    @Nullable
    public HeadlessJsTaskConfig getTaskConfig(Intent intent) {
        if (!RNFirebaseBackgroundNotificationActionReceiver.isBackgroundNotficationIntent(intent)) {
            return null;
        }
        HeadlessJsTaskConfig headlessJsTaskConfig = new HeadlessJsTaskConfig("RNFirebaseBackgroundNotificationAction", RNFirebaseBackgroundNotificationActionReceiver.toNotificationOpenMap(intent), 60000, true);
        return headlessJsTaskConfig;
    }
}

package p007io.invertase.firebase.messaging;

import android.content.Intent;
import com.facebook.react.HeadlessJsTaskService;
import com.facebook.react.jstasks.HeadlessJsTaskConfig;
import javax.annotation.Nullable;

/* renamed from: io.invertase.firebase.messaging.RNFirebaseBackgroundMessagingService */
public class RNFirebaseBackgroundMessagingService extends HeadlessJsTaskService {
    /* access modifiers changed from: protected */
    @Nullable
    public HeadlessJsTaskConfig getTaskConfig(Intent intent) {
        if (intent.getExtras() == null) {
            return null;
        }
        HeadlessJsTaskConfig headlessJsTaskConfig = new HeadlessJsTaskConfig("RNFirebaseBackgroundMessage", MessagingSerializer.parseRemoteMessage(intent.getParcelableExtra("message")), 60000, false);
        return headlessJsTaskConfig;
    }
}

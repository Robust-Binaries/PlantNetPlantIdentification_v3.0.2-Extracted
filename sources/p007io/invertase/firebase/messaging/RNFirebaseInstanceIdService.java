package p007io.invertase.firebase.messaging;

import android.util.Log;
import com.google.firebase.iid.FirebaseInstanceIdService;

/* renamed from: io.invertase.firebase.messaging.RNFirebaseInstanceIdService */
public class RNFirebaseInstanceIdService extends FirebaseInstanceIdService {
    private static final String TAG = "RNFInstanceIdService";

    public void onTokenRefresh() {
        Log.d(TAG, "DEPRECATED onTokenRefresh event received");
    }
}

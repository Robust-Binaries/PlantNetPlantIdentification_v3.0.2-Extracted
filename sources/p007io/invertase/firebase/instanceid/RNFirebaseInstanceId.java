package p007io.invertase.firebase.instanceid;

import android.util.Log;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.google.firebase.iid.FirebaseInstanceId;
import java.io.IOException;

/* renamed from: io.invertase.firebase.instanceid.RNFirebaseInstanceId */
public class RNFirebaseInstanceId extends ReactContextBaseJavaModule {
    private static final String TAG = "RNFirebaseInstanceId";

    public String getName() {
        return TAG;
    }

    public RNFirebaseInstanceId(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
        Log.d(TAG, "New instance");
    }

    @ReactMethod
    public void delete(Promise promise) {
        try {
            Log.d(TAG, "Deleting instance id");
            FirebaseInstanceId.getInstance().deleteInstanceId();
            promise.resolve(null);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            promise.reject("instance_id_error", e.getMessage());
        }
    }

    @ReactMethod
    public void get(Promise promise) {
        promise.resolve(FirebaseInstanceId.getInstance().getId());
    }

    @ReactMethod
    public void getToken(String str, String str2, Promise promise) {
        try {
            String token = FirebaseInstanceId.getInstance().getToken(str, str2);
            String str3 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Firebase token for ");
            sb.append(str);
            sb.append(": ");
            sb.append(token);
            Log.d(str3, sb.toString());
            promise.resolve(token);
        } catch (IOException e) {
            promise.reject("iid/request-failed", "getToken request failed", (Throwable) e);
        }
    }

    @ReactMethod
    public void deleteToken(String str, String str2, Promise promise) {
        try {
            FirebaseInstanceId.getInstance().deleteToken(str, str2);
            String str3 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Firebase token deleted for ");
            sb.append(str);
            Log.d(str3, sb.toString());
            promise.resolve(null);
        } catch (IOException e) {
            promise.reject("iid/request-failed", "deleteToken request failed", (Throwable) e);
        }
    }
}

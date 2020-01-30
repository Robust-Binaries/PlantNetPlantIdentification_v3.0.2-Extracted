package p007io.invertase.firebase.analytics;

import android.app.Activity;
import android.util.Log;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.google.firebase.analytics.FirebaseAnalytics;
import javax.annotation.Nullable;

/* renamed from: io.invertase.firebase.analytics.RNFirebaseAnalytics */
public class RNFirebaseAnalytics extends ReactContextBaseJavaModule {
    private static final String TAG = "RNFirebaseAnalytics";

    public String getName() {
        return TAG;
    }

    RNFirebaseAnalytics(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
        Log.d(TAG, "New instance");
    }

    @ReactMethod
    public void logEvent(String str, @Nullable ReadableMap readableMap) {
        FirebaseAnalytics.getInstance(getReactApplicationContext()).logEvent(str, Arguments.toBundle(readableMap));
    }

    @ReactMethod
    public void setAnalyticsCollectionEnabled(Boolean bool) {
        FirebaseAnalytics.getInstance(getReactApplicationContext()).setAnalyticsCollectionEnabled(bool.booleanValue());
    }

    @ReactMethod
    public void setCurrentScreen(final String str, final String str2) {
        final Activity currentActivity = getCurrentActivity();
        if (currentActivity != null) {
            String str3 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("setCurrentScreen ");
            sb.append(str);
            sb.append(" - ");
            sb.append(str2);
            Log.d(str3, sb.toString());
            currentActivity.runOnUiThread(new Runnable() {
                public void run() {
                    FirebaseAnalytics.getInstance(RNFirebaseAnalytics.this.getReactApplicationContext()).setCurrentScreen(currentActivity, str, str2);
                }
            });
        }
    }

    @ReactMethod
    public void setMinimumSessionDuration(double d) {
        FirebaseAnalytics.getInstance(getReactApplicationContext()).setMinimumSessionDuration((long) d);
    }

    @ReactMethod
    public void setSessionTimeoutDuration(double d) {
        FirebaseAnalytics.getInstance(getReactApplicationContext()).setSessionTimeoutDuration((long) d);
    }

    @ReactMethod
    public void setUserId(String str) {
        FirebaseAnalytics.getInstance(getReactApplicationContext()).setUserId(str);
    }

    @ReactMethod
    public void setUserProperty(String str, String str2) {
        FirebaseAnalytics.getInstance(getReactApplicationContext()).setUserProperty(str, str2);
    }
}

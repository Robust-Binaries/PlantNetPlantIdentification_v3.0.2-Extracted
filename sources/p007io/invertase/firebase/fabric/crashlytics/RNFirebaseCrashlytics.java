package p007io.invertase.firebase.fabric.crashlytics;

import android.util.Log;
import com.crashlytics.android.Crashlytics;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.Kit;

/* renamed from: io.invertase.firebase.fabric.crashlytics.RNFirebaseCrashlytics */
public class RNFirebaseCrashlytics extends ReactContextBaseJavaModule {
    private static final String TAG = "RNFirebaseCrashlytics";

    public String getName() {
        return TAG;
    }

    public RNFirebaseCrashlytics(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
        Log.d(TAG, "New instance");
    }

    @ReactMethod
    public void crash() {
        Crashlytics.getInstance().crash();
    }

    @ReactMethod
    public void log(String str) {
        Crashlytics.log(str);
    }

    @ReactMethod
    public void recordError(int i, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(i);
        sb.append(": ");
        sb.append(str);
        Crashlytics.logException(new Exception(sb.toString()));
    }

    @ReactMethod
    public void setBoolValue(String str, boolean z) {
        Crashlytics.setBool(str, z);
    }

    @ReactMethod
    public void setFloatValue(String str, float f) {
        Crashlytics.setFloat(str, f);
    }

    @ReactMethod
    public void setIntValue(String str, int i) {
        Crashlytics.setInt(str, i);
    }

    @ReactMethod
    public void setStringValue(String str, String str2) {
        Crashlytics.setString(str, str2);
    }

    @ReactMethod
    public void setUserIdentifier(String str) {
        Crashlytics.setUserIdentifier(str);
    }

    @ReactMethod
    public void enableCrashlyticsCollection() {
        Fabric.with(getReactApplicationContext(), new Kit[]{new Crashlytics()});
    }
}

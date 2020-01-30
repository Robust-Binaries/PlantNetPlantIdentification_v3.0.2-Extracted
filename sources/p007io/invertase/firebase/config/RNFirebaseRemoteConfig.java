package p007io.invertase.firebase.config;

import android.util.Log;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigFetchThrottledException;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings.Builder;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigValue;
import java.util.Set;
import javax.annotation.Nonnull;
import p007io.invertase.firebase.C1350Utils;

/* renamed from: io.invertase.firebase.config.RNFirebaseRemoteConfig */
class RNFirebaseRemoteConfig extends ReactContextBaseJavaModule {
    private static final String BOOL_VALUE = "boolValue";
    private static final String DATA_VALUE = "dataValue";
    private static final String NUMBER_VALUE = "numberValue";
    private static final String SOURCE = "source";
    private static final String STRING_VALUE = "stringValue";
    private static final String TAG = "RNFirebaseRemoteConfig";

    public String getName() {
        return TAG;
    }

    RNFirebaseRemoteConfig(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
        Log.d(TAG, "New instance");
    }

    @ReactMethod
    public void enableDeveloperMode() {
        Builder builder = new Builder();
        builder.setDeveloperModeEnabled(true);
        FirebaseRemoteConfig.getInstance().setConfigSettings(builder.build());
    }

    @ReactMethod
    public void fetch(Promise promise) {
        fetchInternal(promise, Boolean.valueOf(false), 0);
    }

    @ReactMethod
    public void fetchWithExpirationDuration(double d, Promise promise) {
        fetchInternal(promise, Boolean.valueOf(true), (long) d);
    }

    @ReactMethod
    public void activateFetched(Promise promise) {
        promise.resolve(Boolean.valueOf(FirebaseRemoteConfig.getInstance().activateFetched()));
    }

    @ReactMethod
    public void getValue(String str, Promise promise) {
        promise.resolve(convertRemoteConfigValue(FirebaseRemoteConfig.getInstance().getValue(str)));
    }

    @ReactMethod
    public void getValues(ReadableArray readableArray, Promise promise) {
        WritableArray createArray = Arguments.createArray();
        for (String value : C1350Utils.recursivelyDeconstructReadableArray(readableArray)) {
            createArray.pushMap(convertRemoteConfigValue(FirebaseRemoteConfig.getInstance().getValue(value)));
        }
        promise.resolve(createArray);
    }

    @ReactMethod
    public void getKeysByPrefix(String str, Promise promise) {
        Set<String> keysByPrefix = FirebaseRemoteConfig.getInstance().getKeysByPrefix(str);
        WritableArray createArray = Arguments.createArray();
        for (String pushString : keysByPrefix) {
            createArray.pushString(pushString);
        }
        promise.resolve(createArray);
    }

    @ReactMethod
    public void setDefaults(ReadableMap readableMap) {
        FirebaseRemoteConfig.getInstance().setDefaults(C1350Utils.recursivelyDeconstructReadableMap(readableMap));
    }

    @ReactMethod
    public void setDefaultsFromResource(int i) {
        FirebaseRemoteConfig.getInstance().setDefaults(i);
    }

    private void fetchInternal(final Promise promise, Boolean bool, long j) {
        FirebaseRemoteConfig instance = FirebaseRemoteConfig.getInstance();
        if (!bool.booleanValue()) {
            j = 43200;
        }
        instance.fetch(j).addOnCompleteListener(new OnCompleteListener<Void>() {
            public void onComplete(@Nonnull Task<Void> task) {
                if (task.isSuccessful()) {
                    promise.resolve(null);
                } else if (task.getException() instanceof FirebaseRemoteConfigFetchThrottledException) {
                    promise.reject("config/throttled", "fetch() operation cannot be completed successfully, due to throttling.", (Throwable) task.getException());
                } else {
                    promise.reject("config/failure", "fetch() operation cannot be completed successfully.", (Throwable) task.getException());
                }
            }
        });
    }

    private WritableMap convertRemoteConfigValue(FirebaseRemoteConfigValue firebaseRemoteConfigValue) {
        WritableMap createMap = Arguments.createMap();
        createMap.putString(STRING_VALUE, firebaseRemoteConfigValue.asString());
        try {
            createMap.putString(DATA_VALUE, new String(firebaseRemoteConfigValue.asByteArray()));
        } catch (Exception unused) {
            createMap.putNull(DATA_VALUE);
        }
        try {
            createMap.putBoolean(BOOL_VALUE, Boolean.valueOf(firebaseRemoteConfigValue.asBoolean()).booleanValue());
        } catch (Exception unused2) {
            createMap.putNull(BOOL_VALUE);
        }
        try {
            createMap.putDouble(NUMBER_VALUE, Double.valueOf(firebaseRemoteConfigValue.asDouble()).doubleValue());
        } catch (Exception unused3) {
            createMap.putNull(NUMBER_VALUE);
        }
        switch (firebaseRemoteConfigValue.getSource()) {
            case 1:
                createMap.putString("source", "default");
                break;
            case 2:
                createMap.putString("source", "remote");
                break;
            default:
                createMap.putString("source", "static");
                break;
        }
        return createMap;
    }
}

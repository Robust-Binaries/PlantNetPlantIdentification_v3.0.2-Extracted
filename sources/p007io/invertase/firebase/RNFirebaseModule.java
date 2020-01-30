package p007io.invertase.firebase;

import android.app.Activity;
import android.content.IntentSender.SendIntentException;
import android.support.p000v4.app.NotificationCompat;
import android.util.Log;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.measurement.api.AppMeasurementSdk.ConditionalUserProperty;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.FirebaseOptions.Builder;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/* renamed from: io.invertase.firebase.RNFirebaseModule */
public class RNFirebaseModule extends ReactContextBaseJavaModule {
    private static final String TAG = "RNFirebase";

    public String getName() {
        return TAG;
    }

    public RNFirebaseModule(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
    }

    @ReactMethod
    public void initializeApp(String str, ReadableMap readableMap, Callback callback) {
        Builder builder = new Builder();
        builder.setApiKey(readableMap.getString("apiKey"));
        builder.setApplicationId(readableMap.getString("appId"));
        builder.setProjectId(readableMap.getString("projectId"));
        builder.setDatabaseUrl(readableMap.getString("databaseURL"));
        builder.setStorageBucket(readableMap.getString("storageBucket"));
        builder.setGcmSenderId(readableMap.getString("messagingSenderId"));
        FirebaseApp.initializeApp(getReactApplicationContext(), builder.build(), str);
        WritableMap createMap = Arguments.createMap();
        createMap.putString("result", Param.SUCCESS);
        callback.invoke(null, createMap);
    }

    @ReactMethod
    public void deleteApp(String str, Promise promise) {
        FirebaseApp instance = FirebaseApp.getInstance(str);
        if (instance != null) {
            instance.delete();
        }
        promise.resolve(null);
    }

    @ReactMethod
    public void getPlayServicesStatus(Promise promise) {
        promise.resolve(getPlayServicesStatusMap());
    }

    private WritableMap getPlayServicesStatusMap() {
        GoogleApiAvailability instance = GoogleApiAvailability.getInstance();
        int isGooglePlayServicesAvailable = instance.isGooglePlayServicesAvailable(getReactApplicationContext());
        WritableMap createMap = Arguments.createMap();
        createMap.putInt(NotificationCompat.CATEGORY_STATUS, isGooglePlayServicesAvailable);
        if (isGooglePlayServicesAvailable == 0) {
            createMap.putBoolean("isAvailable", true);
        } else {
            createMap.putBoolean("isAvailable", false);
            createMap.putString("error", instance.getErrorString(isGooglePlayServicesAvailable));
            createMap.putBoolean("isUserResolvableError", instance.isUserResolvableError(isGooglePlayServicesAvailable));
            createMap.putBoolean("hasResolution", new ConnectionResult(isGooglePlayServicesAvailable).hasResolution());
        }
        return createMap;
    }

    @ReactMethod
    public void promptForPlayServices() {
        GoogleApiAvailability instance = GoogleApiAvailability.getInstance();
        int isGooglePlayServicesAvailable = instance.isGooglePlayServicesAvailable(getReactApplicationContext());
        if (isGooglePlayServicesAvailable != 0 && instance.isUserResolvableError(isGooglePlayServicesAvailable)) {
            Activity currentActivity = getCurrentActivity();
            if (currentActivity != null) {
                instance.getErrorDialog(currentActivity, isGooglePlayServicesAvailable, isGooglePlayServicesAvailable).show();
            }
        }
    }

    @ReactMethod
    public void resolutionForPlayServices() {
        int isGooglePlayServicesAvailable = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getReactApplicationContext());
        ConnectionResult connectionResult = new ConnectionResult(isGooglePlayServicesAvailable);
        if (!connectionResult.isSuccess() && connectionResult.hasResolution()) {
            Activity currentActivity = getCurrentActivity();
            if (currentActivity != null) {
                try {
                    connectionResult.startResolutionForResult(currentActivity, isGooglePlayServicesAvailable);
                } catch (SendIntentException e) {
                    Log.d(TAG, "resolutionForPlayServices", e);
                }
            }
        }
    }

    @ReactMethod
    public void makePlayServicesAvailable() {
        GoogleApiAvailability instance = GoogleApiAvailability.getInstance();
        if (instance.isGooglePlayServicesAvailable(getReactApplicationContext()) != 0) {
            Activity currentActivity = getCurrentActivity();
            if (currentActivity != null) {
                instance.makeGooglePlayServicesAvailable(currentActivity);
            }
        }
    }

    public Map<String, Object> getConstants() {
        HashMap hashMap = new HashMap();
        ArrayList arrayList = new ArrayList();
        for (FirebaseApp firebaseApp : FirebaseApp.getApps(getReactApplicationContext())) {
            String name = firebaseApp.getName();
            FirebaseOptions options = firebaseApp.getOptions();
            HashMap hashMap2 = new HashMap();
            hashMap2.put(ConditionalUserProperty.NAME, name);
            hashMap2.put("apiKey", options.getApiKey());
            hashMap2.put("appId", options.getApplicationId());
            hashMap2.put("projectId", options.getProjectId());
            hashMap2.put("projectId", options.getProjectId());
            hashMap2.put("databaseURL", options.getDatabaseUrl());
            hashMap2.put("messagingSenderId", options.getGcmSenderId());
            hashMap2.put("storageBucket", options.getStorageBucket());
            arrayList.add(hashMap2);
        }
        hashMap.put("apps", arrayList);
        hashMap.put("playServicesAvailability", getPlayServicesStatusMap());
        return hashMap;
    }
}

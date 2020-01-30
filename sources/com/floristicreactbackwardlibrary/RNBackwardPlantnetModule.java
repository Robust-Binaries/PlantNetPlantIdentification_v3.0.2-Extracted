package com.floristicreactbackwardlibrary;

import android.support.annotation.Nullable;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.floristicreactbackwardlibrary.tasks.LoadLocalObservationsTask;
import java.util.HashMap;
import java.util.Map;

public class RNBackwardPlantnetModule extends ReactContextBaseJavaModule {
    public static final String E_BACKWARD_PLANTNET_MODULE = "ERROR_BACKWARD_PLANTNET_MODULE";
    private final ReactApplicationContext reactContext;

    public String getName() {
        return "RNBackwardPlantnet";
    }

    public RNBackwardPlantnetModule(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
        this.reactContext = reactApplicationContext;
    }

    public Map<String, Object> getConstants() {
        return new HashMap();
    }

    @ReactMethod
    public void loadExistingObservationsCallback(Callback callback, Callback callback2) {
        loadExistingObservations(callback, callback2, null);
    }

    @ReactMethod
    public void loadExistingObservationsPromise(Promise promise) {
        loadExistingObservations(null, null, promise);
    }

    private void loadExistingObservations(@Nullable Callback callback, @Nullable Callback callback2, @Nullable Promise promise) {
        new LoadLocalObservationsTask(this.reactContext, callback, callback2, promise).execute(new Integer[0]);
    }
}

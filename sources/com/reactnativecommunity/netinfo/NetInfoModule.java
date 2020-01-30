package com.reactnativecommunity.netinfo;

import android.os.Build.VERSION;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class NetInfoModule extends ReactContextBaseJavaModule {
    public static final String NAME = "RNCNetInfo";
    private final ConnectivityReceiver mConnectivityReceiver;

    public String getName() {
        return NAME;
    }

    public NetInfoModule(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
        if (VERSION.SDK_INT >= 24) {
            this.mConnectivityReceiver = new NetworkCallbackConnectivityReceiver(reactApplicationContext);
        } else {
            this.mConnectivityReceiver = new BroadcastReceiverConnectivityReceiver(reactApplicationContext);
        }
    }

    public void initialize() {
        this.mConnectivityReceiver.register();
    }

    public void onCatalystInstanceDestroy() {
        this.mConnectivityReceiver.unregister();
    }

    @ReactMethod
    public void getCurrentState(Promise promise) {
        this.mConnectivityReceiver.getCurrentState(promise);
    }
}

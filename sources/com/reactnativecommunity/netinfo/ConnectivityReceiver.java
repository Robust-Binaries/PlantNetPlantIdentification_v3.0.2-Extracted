package com.reactnativecommunity.netinfo;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.p000v4.net.ConnectivityManagerCompat;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter;

abstract class ConnectivityReceiver {
    static final String CELLULAR_GENERATION_2G = "2g";
    static final String CELLULAR_GENERATION_3G = "3g";
    static final String CELLULAR_GENERATION_4G = "4g";
    static final String CONNECTION_TYPE_BLUETOOTH = "bluetooth";
    static final String CONNECTION_TYPE_CELLULAR = "cellular";
    static final String CONNECTION_TYPE_ETHERNET = "ethernet";
    static final String CONNECTION_TYPE_NONE = "none";
    static final String CONNECTION_TYPE_OTHER = "other";
    static final String CONNECTION_TYPE_UNKNOWN = "unknown";
    static final String CONNECTION_TYPE_VPN = "vpn";
    static final String CONNECTION_TYPE_WIFI = "wifi";
    static final String CONNECTION_TYPE_WIMAX = "wimax";
    static final String ERROR_MISSING_PERMISSION = "E_MISSING_PERMISSION";
    static final String MISSING_PERMISSION_MESSAGE = "To use NetInfo on Android, add the following to your AndroidManifest.xml:\n<uses-permission android:name=\"android.permission.ACCESS_NETWORK_STATE\" />";
    private String mCellularGeneration = null;
    private String mConnectionType = "unknown";
    private final ConnectivityManager mConnectivityManager;
    private boolean mNoNetworkPermission = false;
    private final ReactApplicationContext mReactContext;

    /* access modifiers changed from: 0000 */
    public abstract void register();

    /* access modifiers changed from: 0000 */
    public abstract void unregister();

    ConnectivityReceiver(ReactApplicationContext reactApplicationContext) {
        this.mReactContext = reactApplicationContext;
        this.mConnectivityManager = (ConnectivityManager) reactApplicationContext.getSystemService("connectivity");
    }

    public void getCurrentState(Promise promise) {
        if (this.mNoNetworkPermission) {
            promise.reject(ERROR_MISSING_PERMISSION, MISSING_PERMISSION_MESSAGE);
        } else {
            promise.resolve(createConnectivityEventMap());
        }
    }

    public ReactApplicationContext getReactContext() {
        return this.mReactContext;
    }

    public ConnectivityManager getConnectivityManager() {
        return this.mConnectivityManager;
    }

    public void setNoNetworkPermission() {
        this.mNoNetworkPermission = true;
    }

    /* access modifiers changed from: 0000 */
    public String getEffectiveConnectionType(NetworkInfo networkInfo) {
        if (networkInfo == null) {
            return null;
        }
        switch (networkInfo.getSubtype()) {
            case 1:
            case 2:
            case 4:
            case 7:
            case 11:
                return CELLULAR_GENERATION_2G;
            case 3:
            case 5:
            case 6:
            case 8:
            case 9:
            case 10:
            case 12:
            case 14:
                return CELLULAR_GENERATION_3G;
            case 13:
            case 15:
                return CELLULAR_GENERATION_4G;
            default:
                return null;
        }
    }

    /* access modifiers changed from: 0000 */
    public void updateConnectivity(String str, String str2) {
        boolean z = true;
        boolean z2 = (str == null && this.mConnectionType != null) || (str != null && !str.equalsIgnoreCase(this.mConnectionType));
        if ((str2 != null || this.mCellularGeneration == null) && (str2 == null || str2.equalsIgnoreCase(this.mCellularGeneration))) {
            z = false;
        }
        if (z2 || z) {
            this.mConnectionType = str;
            this.mCellularGeneration = str2;
            sendConnectivityChangedEvent();
        }
    }

    private void sendConnectivityChangedEvent() {
        ((RCTDeviceEventEmitter) getReactContext().getJSModule(RCTDeviceEventEmitter.class)).emit("netInfo.networkStatusDidChange", createConnectivityEventMap());
    }

    private WritableMap createConnectivityEventMap() {
        WritableNativeMap writableNativeMap = new WritableNativeMap();
        writableNativeMap.putString("type", this.mConnectionType);
        boolean z = !this.mConnectionType.equals("none") && !this.mConnectionType.equals("unknown");
        writableNativeMap.putBoolean("isConnected", z);
        WritableNativeMap writableNativeMap2 = null;
        if (z) {
            writableNativeMap2 = new WritableNativeMap();
            writableNativeMap2.putBoolean("isConnectionExpensive", ConnectivityManagerCompat.isActiveNetworkMetered(getConnectivityManager()));
            if (this.mConnectionType.equals(CONNECTION_TYPE_CELLULAR)) {
                writableNativeMap2.putString("cellularGeneration", this.mCellularGeneration);
            }
        }
        writableNativeMap.putMap("details", writableNativeMap2);
        return writableNativeMap;
    }
}

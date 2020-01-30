package com.reactnativecommunity.netinfo;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.p000v4.p002os.EnvironmentCompat;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewProps;

public class BroadcastReceiverConnectivityReceiver extends ConnectivityReceiver {
    private final ConnectivityBroadcastReceiver mConnectivityBroadcastReceiver = new ConnectivityBroadcastReceiver();

    private class ConnectivityBroadcastReceiver extends BroadcastReceiver {
        private boolean isRegistered;

        private ConnectivityBroadcastReceiver() {
            this.isRegistered = false;
        }

        public void setRegistered(boolean z) {
            this.isRegistered = z;
        }

        public boolean isRegistered() {
            return this.isRegistered;
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null && action.equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                BroadcastReceiverConnectivityReceiver.this.updateAndSendConnectionType();
            }
        }
    }

    public /* bridge */ /* synthetic */ ConnectivityManager getConnectivityManager() {
        return super.getConnectivityManager();
    }

    public /* bridge */ /* synthetic */ void getCurrentState(Promise promise) {
        super.getCurrentState(promise);
    }

    public /* bridge */ /* synthetic */ ReactApplicationContext getReactContext() {
        return super.getReactContext();
    }

    public /* bridge */ /* synthetic */ void setNoNetworkPermission() {
        super.setNoNetworkPermission();
    }

    public BroadcastReceiverConnectivityReceiver(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
    }

    public void register() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        getReactContext().registerReceiver(this.mConnectivityBroadcastReceiver, intentFilter);
        this.mConnectivityBroadcastReceiver.setRegistered(true);
        updateAndSendConnectionType();
    }

    public void unregister() {
        if (this.mConnectivityBroadcastReceiver.isRegistered()) {
            getReactContext().unregisterReceiver(this.mConnectivityBroadcastReceiver);
            this.mConnectivityBroadcastReceiver.setRegistered(false);
        }
    }

    /* access modifiers changed from: private */
    @SuppressLint({"MissingPermission"})
    public void updateAndSendConnectionType() {
        String str = "other";
        String str2 = null;
        try {
            NetworkInfo activeNetworkInfo = getConnectivityManager().getActiveNetworkInfo();
            if (activeNetworkInfo != null) {
                if (activeNetworkInfo.isConnected()) {
                    switch (activeNetworkInfo.getType()) {
                        case 0:
                        case 4:
                            str = "cellular";
                            str2 = getEffectiveConnectionType(activeNetworkInfo);
                            break;
                        case 1:
                            str = "wifi";
                            break;
                        case 6:
                            str = "wimax";
                            break;
                        case 7:
                            str = "bluetooth";
                            break;
                        case 9:
                            str = "ethernet";
                            break;
                        case 17:
                            str = "vpn";
                            break;
                    }
                    updateConnectivity(str, str2);
                }
            }
            str = ViewProps.NONE;
        } catch (SecurityException unused) {
            setNoNetworkPermission();
            str = EnvironmentCompat.MEDIA_UNKNOWN;
        }
        updateConnectivity(str, str2);
    }
}

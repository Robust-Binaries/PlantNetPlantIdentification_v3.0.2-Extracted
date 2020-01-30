package com.reactnativecommunity.netinfo;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.net.ConnectivityManager.NetworkCallback;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewProps;

@TargetApi(24)
class NetworkCallbackConnectivityReceiver extends ConnectivityReceiver {
    /* access modifiers changed from: private */
    public Network mNetwork = null;
    private final ConnectivityNetworkCallback mNetworkCallback = new ConnectivityNetworkCallback();
    /* access modifiers changed from: private */
    public NetworkCapabilities mNetworkCapabilities = null;

    private class ConnectivityNetworkCallback extends NetworkCallback {
        private ConnectivityNetworkCallback() {
        }

        public void onAvailable(Network network) {
            NetworkCallbackConnectivityReceiver.this.mNetwork = network;
            NetworkCallbackConnectivityReceiver networkCallbackConnectivityReceiver = NetworkCallbackConnectivityReceiver.this;
            networkCallbackConnectivityReceiver.mNetworkCapabilities = networkCallbackConnectivityReceiver.getConnectivityManager().getNetworkCapabilities(network);
            NetworkCallbackConnectivityReceiver.this.updateAndSend();
        }

        public void onLosing(Network network, int i) {
            NetworkCallbackConnectivityReceiver.this.mNetwork = network;
            NetworkCallbackConnectivityReceiver networkCallbackConnectivityReceiver = NetworkCallbackConnectivityReceiver.this;
            networkCallbackConnectivityReceiver.mNetworkCapabilities = networkCallbackConnectivityReceiver.getConnectivityManager().getNetworkCapabilities(network);
            NetworkCallbackConnectivityReceiver.this.updateAndSend();
        }

        public void onLost(Network network) {
            NetworkCallbackConnectivityReceiver.this.mNetwork = null;
            NetworkCallbackConnectivityReceiver.this.mNetworkCapabilities = null;
            NetworkCallbackConnectivityReceiver.this.updateAndSend();
        }

        public void onUnavailable() {
            NetworkCallbackConnectivityReceiver.this.mNetwork = null;
            NetworkCallbackConnectivityReceiver.this.mNetworkCapabilities = null;
            NetworkCallbackConnectivityReceiver.this.updateAndSend();
        }

        public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
            NetworkCallbackConnectivityReceiver.this.mNetwork = network;
            NetworkCallbackConnectivityReceiver.this.mNetworkCapabilities = networkCapabilities;
            NetworkCallbackConnectivityReceiver.this.updateAndSend();
        }

        public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
            NetworkCallbackConnectivityReceiver.this.mNetwork = network;
            NetworkCallbackConnectivityReceiver networkCallbackConnectivityReceiver = NetworkCallbackConnectivityReceiver.this;
            networkCallbackConnectivityReceiver.mNetworkCapabilities = networkCallbackConnectivityReceiver.getConnectivityManager().getNetworkCapabilities(network);
            NetworkCallbackConnectivityReceiver.this.updateAndSend();
        }
    }

    public NetworkCallbackConnectivityReceiver(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
    }

    /* access modifiers changed from: 0000 */
    @SuppressLint({"MissingPermission"})
    public void register() {
        try {
            getConnectivityManager().registerDefaultNetworkCallback(this.mNetworkCallback);
            if (getConnectivityManager().getActiveNetwork() == null) {
                updateAndSend();
            }
        } catch (SecurityException unused) {
            setNoNetworkPermission();
        }
    }

    /* access modifiers changed from: 0000 */
    public void unregister() {
        try {
            getConnectivityManager().unregisterNetworkCallback(this.mNetworkCallback);
        } catch (SecurityException unused) {
            setNoNetworkPermission();
        } catch (IllegalArgumentException unused2) {
        }
    }

    /* access modifiers changed from: private */
    @SuppressLint({"MissingPermission"})
    public void updateAndSend() {
        String str = "other";
        NetworkCapabilities networkCapabilities = this.mNetworkCapabilities;
        String str2 = null;
        if (networkCapabilities == null) {
            str = ViewProps.NONE;
        } else if (networkCapabilities.hasTransport(2)) {
            str = "bluetooth";
        } else if (this.mNetworkCapabilities.hasTransport(0)) {
            str = "cellular";
            if (this.mNetwork != null) {
                str2 = getEffectiveConnectionType(getConnectivityManager().getNetworkInfo(this.mNetwork));
            }
        } else if (this.mNetworkCapabilities.hasTransport(3)) {
            str = "ethernet";
        } else if (this.mNetworkCapabilities.hasTransport(1)) {
            str = "wifi";
        } else if (this.mNetworkCapabilities.hasTransport(4)) {
            str = "vpn";
        }
        updateConnectivity(str, str2);
    }
}

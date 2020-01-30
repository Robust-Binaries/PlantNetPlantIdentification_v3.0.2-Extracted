package com.learnium.RNDeviceInfo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;

public class RNDeviceReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if ("com.android.vending.INSTALL_REFERRER".equals(intent.getAction())) {
            Editor edit = context.getSharedPreferences("react-native-device-info", 0).edit();
            edit.putString("installReferrer", intent.getStringExtra("referrer"));
            edit.commit();
        }
    }
}

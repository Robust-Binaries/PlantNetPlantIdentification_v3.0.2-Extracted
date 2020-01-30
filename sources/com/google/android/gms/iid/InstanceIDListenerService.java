package com.google.android.gms.iid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

@Deprecated
public class InstanceIDListenerService extends zze {
    public void onTokenRefresh() {
    }

    public void handleIntent(Intent intent) {
        if ("com.google.android.gms.iid.InstanceID".equals(intent.getAction())) {
            Bundle bundle = null;
            String stringExtra = intent.getStringExtra("subtype");
            if (stringExtra != null) {
                bundle = new Bundle();
                bundle.putString("subtype", stringExtra);
            }
            InstanceID instance = InstanceID.getInstance(this, bundle);
            String stringExtra2 = intent.getStringExtra("CMD");
            if (Log.isLoggable("InstanceID", 3)) {
                StringBuilder sb = new StringBuilder(String.valueOf(stringExtra).length() + 34 + String.valueOf(stringExtra2).length());
                sb.append("Service command. subtype:");
                sb.append(stringExtra);
                sb.append(" command:");
                sb.append(stringExtra2);
                Log.d("InstanceID", sb.toString());
            }
            if ("RST".equals(stringExtra2)) {
                instance.zzo();
                onTokenRefresh();
                return;
            }
            if ("RST_FULL".equals(stringExtra2)) {
                if (!InstanceID.zzp().isEmpty()) {
                    InstanceID.zzp().zzz();
                    onTokenRefresh();
                }
            } else if ("SYNC".equals(stringExtra2)) {
                zzak zzp = InstanceID.zzp();
                String valueOf = String.valueOf(stringExtra);
                String valueOf2 = String.valueOf("|T|");
                zzp.zzi(valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf));
                String valueOf3 = String.valueOf(stringExtra);
                String valueOf4 = String.valueOf("|T-timestamp|");
                zzp.zzi(valueOf4.length() != 0 ? valueOf3.concat(valueOf4) : new String(valueOf3));
                onTokenRefresh();
            }
        }
    }

    static void zzd(Context context, zzak zzak) {
        zzak.zzz();
        Intent intent = new Intent("com.google.android.gms.iid.InstanceID");
        intent.putExtra("CMD", "RST");
        intent.setClassName(context, "com.google.android.gms.gcm.GcmReceiver");
        context.sendBroadcast(intent);
    }
}

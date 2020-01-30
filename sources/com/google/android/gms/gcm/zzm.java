package com.google.android.gms.gcm;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.gms.common.GooglePlayServicesUtilLight;
import com.google.firebase.analytics.FirebaseAnalytics.Param;

final class zzm implements zzn {
    private final PendingIntent zzav;
    private final Context zzl;

    zzm(Context context) {
        this.zzl = context;
        this.zzav = PendingIntent.getBroadcast(context, 0, new Intent().setPackage("com.google.example.invalidpackage"), 0);
    }

    public final boolean zzd(Task task) {
        Intent zzh = zzh("SCHEDULE_TASK");
        Bundle bundle = new Bundle();
        task.toBundle(bundle);
        zzh.putExtras(bundle);
        this.zzl.sendBroadcast(zzh);
        return true;
    }

    public final boolean zzd(ComponentName componentName, String str) {
        Intent zzh = zzh("CANCEL_TASK");
        zzh.putExtra("component", componentName);
        zzh.putExtra("tag", str);
        this.zzl.sendBroadcast(zzh);
        return true;
    }

    public final boolean zzd(ComponentName componentName) {
        Intent zzh = zzh("CANCEL_ALL");
        zzh.putExtra("component", componentName);
        this.zzl.sendBroadcast(zzh);
        return true;
    }

    private final Intent zzh(String str) {
        Intent intent = new Intent("com.google.android.gms.gcm.ACTION_SCHEDULE");
        intent.setPackage("com.google.android.gms");
        intent.putExtra("app", this.zzav);
        intent.putExtra(Param.SOURCE, 4);
        intent.putExtra("source_version", GooglePlayServicesUtilLight.GOOGLE_PLAY_SERVICES_VERSION_CODE);
        intent.putExtra("scheduler_action", str);
        return intent;
    }
}

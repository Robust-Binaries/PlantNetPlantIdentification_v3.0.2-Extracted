package com.google.android.gms.iid;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.util.Log;
import com.google.android.gms.common.util.concurrent.NamedThreadFactory;
import com.google.android.gms.internal.gcm.zzg;
import com.google.android.gms.tasks.Task;
import java.util.concurrent.ScheduledExecutorService;
import javax.annotation.concurrent.GuardedBy;

public final class zzr {
    /* access modifiers changed from: private */
    public final ScheduledExecutorService zzce;
    @GuardedBy("this")
    private zzt zzcf;
    @GuardedBy("this")
    private int zzcg;
    /* access modifiers changed from: private */
    public final Context zzl;

    public zzr(Context context) {
        this(context, zzg.zzaa().zze(1, new NamedThreadFactory("MessengerIpcClient"), 9));
    }

    @VisibleForTesting
    private zzr(Context context, ScheduledExecutorService scheduledExecutorService) {
        this.zzcf = new zzt(this);
        this.zzcg = 1;
        this.zzl = context.getApplicationContext();
        this.zzce = scheduledExecutorService;
    }

    public final Task<Bundle> zzd(int i, Bundle bundle) {
        return zzd((zzz<T>) new zzab<T>(zzs(), 1, bundle));
    }

    private final synchronized <T> Task<T> zzd(zzz<T> zzz) {
        if (Log.isLoggable("MessengerIpcClient", 3)) {
            String valueOf = String.valueOf(zzz);
            StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 9);
            sb.append("Queueing ");
            sb.append(valueOf);
            Log.d("MessengerIpcClient", sb.toString());
        }
        if (!this.zzcf.zze(zzz)) {
            this.zzcf = new zzt(this);
            this.zzcf.zze(zzz);
        }
        return zzz.zzcq.getTask();
    }

    private final synchronized int zzs() {
        int i;
        i = this.zzcg;
        this.zzcg = i + 1;
        return i;
    }
}

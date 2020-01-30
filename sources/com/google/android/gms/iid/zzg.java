package com.google.android.gms.iid;

import android.content.BroadcastReceiver.PendingResult;
import android.content.Intent;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

final class zzg {
    final Intent intent;
    private final PendingResult zzbi;
    private boolean zzbj = false;
    private final ScheduledFuture<?> zzbk;

    zzg(Intent intent2, PendingResult pendingResult, ScheduledExecutorService scheduledExecutorService) {
        this.intent = intent2;
        this.zzbi = pendingResult;
        this.zzbk = scheduledExecutorService.schedule(new zzh(this, intent2), 9500, TimeUnit.MILLISECONDS);
    }

    /* access modifiers changed from: 0000 */
    public final synchronized void finish() {
        if (!this.zzbj) {
            this.zzbi.finish();
            this.zzbk.cancel(false);
            this.zzbj = true;
        }
    }
}

package com.google.android.gms.iid;

import android.os.Looper;
import android.os.Message;
import com.google.android.gms.internal.gcm.zzj;

final class zzag extends zzj {
    private final /* synthetic */ zzaf zzdc;

    zzag(zzaf zzaf, Looper looper) {
        this.zzdc = zzaf;
        super(looper);
    }

    public final void handleMessage(Message message) {
        this.zzdc.zze(message);
    }
}

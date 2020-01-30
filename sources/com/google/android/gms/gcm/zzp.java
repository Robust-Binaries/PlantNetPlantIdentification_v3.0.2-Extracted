package com.google.android.gms.gcm;

import android.annotation.TargetApi;
import android.os.Trace;
import com.google.android.gms.common.util.PlatformVersion;
import com.google.android.gms.iid.zzai;
import com.google.android.gms.iid.zzaj;
import java.io.Closeable;

public final class zzp implements Closeable {
    private static final zzaj<Boolean> zzba = zzai.zzy().zzd("nts.enable_tracing", true);
    private final boolean enabled;

    @TargetApi(18)
    public zzp(String str) {
        this.enabled = PlatformVersion.isAtLeastJellyBeanMR2() && ((Boolean) zzba.get()).booleanValue();
        if (this.enabled) {
            if (str.length() > 127) {
                str = str.substring(0, 127);
            }
            Trace.beginSection(str);
        }
    }

    @TargetApi(18)
    public final void close() {
        if (this.enabled) {
            Trace.endSection();
        }
    }
}

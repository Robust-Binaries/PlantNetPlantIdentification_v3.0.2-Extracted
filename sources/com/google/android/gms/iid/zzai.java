package com.google.android.gms.iid;

import javax.annotation.concurrent.GuardedBy;

public abstract class zzai {
    @GuardedBy("SdkFlagFactory.class")
    private static zzai zzdd;

    public abstract zzaj<Boolean> zzd(String str, boolean z);

    public static synchronized zzai zzy() {
        zzai zzai;
        synchronized (zzai.class) {
            if (zzdd == null) {
                zzdd = new zzac();
            }
            zzai = zzdd;
        }
        return zzai;
    }
}

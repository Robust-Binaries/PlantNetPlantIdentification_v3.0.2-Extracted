package com.google.android.gms.measurement.internal;

import com.google.android.gms.internal.measurement.zzq;

final class zzj implements Runnable {
    private final /* synthetic */ String zzao;
    private final /* synthetic */ String zzav;
    private final /* synthetic */ zzq zzdh;
    private final /* synthetic */ AppMeasurementDynamiteService zzdi;
    private final /* synthetic */ boolean zzdl;

    zzj(AppMeasurementDynamiteService appMeasurementDynamiteService, zzq zzq, String str, String str2, boolean z) {
        this.zzdi = appMeasurementDynamiteService;
        this.zzdh = zzq;
        this.zzao = str;
        this.zzav = str2;
        this.zzdl = z;
    }

    public final void run() {
        this.zzdi.zzl.zzu().zza(this.zzdh, this.zzao, this.zzav, this.zzdl);
    }
}

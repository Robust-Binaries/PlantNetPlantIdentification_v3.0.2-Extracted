package com.google.android.gms.measurement.internal;

import java.util.List;
import java.util.concurrent.Callable;

final class zzch implements Callable<List<zzgc>> {
    private final /* synthetic */ String zzao;
    private final /* synthetic */ String zzav;
    private final /* synthetic */ String zzdk;
    private final /* synthetic */ zzca zzot;

    zzch(zzca zzca, String str, String str2, String str3) {
        this.zzot = zzca;
        this.zzdk = str;
        this.zzao = str2;
        this.zzav = str3;
    }

    public final /* synthetic */ Object call() throws Exception {
        this.zzot.zzkt.zzgf();
        return this.zzot.zzkt.zzdo().zza(this.zzdk, this.zzao, this.zzav);
    }
}

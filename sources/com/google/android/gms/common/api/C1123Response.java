package com.google.android.gms.common.api;

import android.support.annotation.NonNull;
import com.google.android.gms.common.api.Result;

/* renamed from: com.google.android.gms.common.api.Response */
public class C1123Response<T extends Result> {
    private T zzap;

    public C1123Response() {
    }

    protected C1123Response(@NonNull T t) {
        this.zzap = t;
    }

    /* access modifiers changed from: protected */
    @NonNull
    public T getResult() {
        return this.zzap;
    }

    public void setResult(@NonNull T t) {
        this.zzap = t;
    }
}

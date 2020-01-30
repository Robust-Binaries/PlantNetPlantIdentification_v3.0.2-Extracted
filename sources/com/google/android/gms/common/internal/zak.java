package com.google.android.gms.common.internal;

import com.google.android.gms.common.api.C1123Response;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.internal.PendingResultUtil.ResultConverter;

final class zak implements ResultConverter<R, T> {
    private final /* synthetic */ C1123Response zaoz;

    zak(C1123Response response) {
        this.zaoz = response;
    }

    public final /* synthetic */ Object convert(Result result) {
        this.zaoz.setResult(result);
        return this.zaoz;
    }
}

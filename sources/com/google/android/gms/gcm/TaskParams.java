package com.google.android.gms.gcm;

import android.net.Uri;
import android.os.Bundle;
import java.util.List;

public class TaskParams {
    private final Bundle extras;
    private final String tag;
    private final List<Uri> zzaa;
    private final long zzaz;

    public TaskParams(String str) {
        this(str, null);
    }

    public TaskParams(String str, Bundle bundle) {
        this(str, bundle, null);
    }

    public TaskParams(String str, Bundle bundle, List<Uri> list) {
        this(str, bundle, 180, list);
    }

    TaskParams(String str, Bundle bundle, long j, List<Uri> list) {
        this.tag = str;
        this.extras = bundle;
        this.zzaz = j;
        this.zzaa = list;
    }

    public String getTag() {
        return this.tag;
    }

    public Bundle getExtras() {
        return this.extras;
    }
}

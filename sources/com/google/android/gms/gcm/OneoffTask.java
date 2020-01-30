package com.google.android.gms.gcm;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.annotation.RequiresPermission;

public class OneoffTask extends Task {
    public static final Creator<OneoffTask> CREATOR = new zzi();
    private final long zzal;
    private final long zzam;

    public static class Builder extends com.google.android.gms.gcm.Task.Builder {
        /* access modifiers changed from: private */
        public long zzal;
        /* access modifiers changed from: private */
        public long zzam;

        public Builder() {
            this.zzal = -1;
            this.zzam = -1;
            this.isPersisted = false;
        }

        public Builder setExecutionWindow(long j, long j2) {
            this.zzal = j;
            this.zzam = j2;
            return this;
        }

        public Builder setService(Class<? extends GcmTaskService> cls) {
            this.gcmTaskService = cls.getName();
            return this;
        }

        public Builder setRequiredNetwork(int i) {
            this.requiredNetworkState = i;
            return this;
        }

        public Builder setRequiresCharging(boolean z) {
            this.requiresCharging = z;
            return this;
        }

        public Builder setTag(String str) {
            this.tag = str;
            return this;
        }

        @RequiresPermission("android.permission.RECEIVE_BOOT_COMPLETED")
        public Builder setPersisted(boolean z) {
            this.isPersisted = z;
            return this;
        }

        public Builder setUpdateCurrent(boolean z) {
            this.updateCurrent = z;
            return this;
        }

        public Builder setExtras(Bundle bundle) {
            this.extras = bundle;
            return this;
        }

        /* access modifiers changed from: protected */
        public void checkConditions() {
            super.checkConditions();
            long j = this.zzal;
            if (j != -1) {
                long j2 = this.zzam;
                if (j2 != -1) {
                    if (j >= j2) {
                        throw new IllegalArgumentException("Window start must be shorter than window end.");
                    }
                    return;
                }
            }
            throw new IllegalArgumentException("Must specify an execution window using setExecutionWindow.");
        }

        public OneoffTask build() {
            checkConditions();
            return new OneoffTask(this, (zzi) null);
        }
    }

    private OneoffTask(Builder builder) {
        super((com.google.android.gms.gcm.Task.Builder) builder);
        this.zzal = builder.zzal;
        this.zzam = builder.zzam;
    }

    @Deprecated
    private OneoffTask(Parcel parcel) {
        super(parcel);
        this.zzal = parcel.readLong();
        this.zzam = parcel.readLong();
    }

    public void toBundle(Bundle bundle) {
        super.toBundle(bundle);
        bundle.putLong("window_start", this.zzal);
        bundle.putLong("window_end", this.zzam);
    }

    public long getWindowStart() {
        return this.zzal;
    }

    public long getWindowEnd() {
        return this.zzam;
    }

    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeLong(this.zzal);
        parcel.writeLong(this.zzam);
    }

    public String toString() {
        String obj = super.toString();
        long windowStart = getWindowStart();
        long windowEnd = getWindowEnd();
        StringBuilder sb = new StringBuilder(String.valueOf(obj).length() + 64);
        sb.append(obj);
        sb.append(" windowStart=");
        sb.append(windowStart);
        sb.append(" windowEnd=");
        sb.append(windowEnd);
        return sb.toString();
    }

    /* synthetic */ OneoffTask(Parcel parcel, zzi zzi) {
        this(parcel);
    }

    /* synthetic */ OneoffTask(Builder builder, zzi zzi) {
        this(builder);
    }
}

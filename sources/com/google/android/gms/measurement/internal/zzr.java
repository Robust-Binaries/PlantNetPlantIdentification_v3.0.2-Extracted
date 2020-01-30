package com.google.android.gms.measurement.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;

@Class(creator = "ConditionalUserPropertyParcelCreator")
public final class zzr extends AbstractSafeParcelable {
    public static final Creator<zzr> CREATOR = new zzs();
    @Field(mo15077id = 6)
    public boolean active;
    @Field(mo15077id = 5)
    public long creationTimestamp;
    @Field(mo15077id = 3)
    public String origin;
    @Field(mo15077id = 2)
    public String packageName;
    @Field(mo15077id = 11)
    public long timeToLive;
    @Field(mo15077id = 7)
    public String triggerEventName;
    @Field(mo15077id = 9)
    public long triggerTimeout;
    @Field(mo15077id = 4)
    public zzga zzdv;
    @Field(mo15077id = 8)
    public zzaj zzdw;
    @Field(mo15077id = 10)
    public zzaj zzdx;
    @Field(mo15077id = 12)
    public zzaj zzdy;

    zzr(zzr zzr) {
        Preconditions.checkNotNull(zzr);
        this.packageName = zzr.packageName;
        this.origin = zzr.origin;
        this.zzdv = zzr.zzdv;
        this.creationTimestamp = zzr.creationTimestamp;
        this.active = zzr.active;
        this.triggerEventName = zzr.triggerEventName;
        this.zzdw = zzr.zzdw;
        this.triggerTimeout = zzr.triggerTimeout;
        this.zzdx = zzr.zzdx;
        this.timeToLive = zzr.timeToLive;
        this.zzdy = zzr.zzdy;
    }

    @Constructor
    zzr(@Param(mo15080id = 2) String str, @Param(mo15080id = 3) String str2, @Param(mo15080id = 4) zzga zzga, @Param(mo15080id = 5) long j, @Param(mo15080id = 6) boolean z, @Param(mo15080id = 7) String str3, @Param(mo15080id = 8) zzaj zzaj, @Param(mo15080id = 9) long j2, @Param(mo15080id = 10) zzaj zzaj2, @Param(mo15080id = 11) long j3, @Param(mo15080id = 12) zzaj zzaj3) {
        this.packageName = str;
        this.origin = str2;
        this.zzdv = zzga;
        this.creationTimestamp = j;
        this.active = z;
        this.triggerEventName = str3;
        this.zzdw = zzaj;
        this.triggerTimeout = j2;
        this.zzdx = zzaj2;
        this.timeToLive = j3;
        this.zzdy = zzaj3;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, this.packageName, false);
        SafeParcelWriter.writeString(parcel, 3, this.origin, false);
        SafeParcelWriter.writeParcelable(parcel, 4, this.zzdv, i, false);
        SafeParcelWriter.writeLong(parcel, 5, this.creationTimestamp);
        SafeParcelWriter.writeBoolean(parcel, 6, this.active);
        SafeParcelWriter.writeString(parcel, 7, this.triggerEventName, false);
        SafeParcelWriter.writeParcelable(parcel, 8, this.zzdw, i, false);
        SafeParcelWriter.writeLong(parcel, 9, this.triggerTimeout);
        SafeParcelWriter.writeParcelable(parcel, 10, this.zzdx, i, false);
        SafeParcelWriter.writeLong(parcel, 11, this.timeToLive);
        SafeParcelWriter.writeParcelable(parcel, 12, this.zzdy, i, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}

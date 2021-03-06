package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.annotation.Nullable;
import android.support.p000v4.view.ViewCompat;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Reserved;
import java.util.List;

@Class(creator = "CircleOptionsCreator")
@Reserved({1})
public final class CircleOptions extends AbstractSafeParcelable {
    public static final Creator<CircleOptions> CREATOR = new zzc();
    @Field(getter = "getFillColor", mo15077id = 6)
    private int fillColor = 0;
    @Field(getter = "getStrokeColor", mo15077id = 5)
    private int strokeColor = ViewCompat.MEASURED_STATE_MASK;
    @Field(getter = "getCenter", mo15077id = 2)
    private LatLng zzcp = null;
    @Field(getter = "getRadius", mo15077id = 3)
    private double zzcq = 0.0d;
    @Field(getter = "getStrokeWidth", mo15077id = 4)
    private float zzcr = 10.0f;
    @Field(getter = "getZIndex", mo15077id = 7)
    private float zzcs = 0.0f;
    @Field(getter = "isVisible", mo15077id = 8)
    private boolean zzct = true;
    @Field(getter = "isClickable", mo15077id = 9)
    private boolean zzcu = false;
    @Nullable
    @Field(getter = "getStrokePattern", mo15077id = 10)
    private List<PatternItem> zzcv = null;

    public CircleOptions() {
    }

    @Constructor
    CircleOptions(@Param(mo15080id = 2) LatLng latLng, @Param(mo15080id = 3) double d, @Param(mo15080id = 4) float f, @Param(mo15080id = 5) int i, @Param(mo15080id = 6) int i2, @Param(mo15080id = 7) float f2, @Param(mo15080id = 8) boolean z, @Param(mo15080id = 9) boolean z2, @Nullable @Param(mo15080id = 10) List<PatternItem> list) {
        this.zzcp = latLng;
        this.zzcq = d;
        this.zzcr = f;
        this.strokeColor = i;
        this.fillColor = i2;
        this.zzcs = f2;
        this.zzct = z;
        this.zzcu = z2;
        this.zzcv = list;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeParcelable(parcel, 2, getCenter(), i, false);
        SafeParcelWriter.writeDouble(parcel, 3, getRadius());
        SafeParcelWriter.writeFloat(parcel, 4, getStrokeWidth());
        SafeParcelWriter.writeInt(parcel, 5, getStrokeColor());
        SafeParcelWriter.writeInt(parcel, 6, getFillColor());
        SafeParcelWriter.writeFloat(parcel, 7, getZIndex());
        SafeParcelWriter.writeBoolean(parcel, 8, isVisible());
        SafeParcelWriter.writeBoolean(parcel, 9, isClickable());
        SafeParcelWriter.writeTypedList(parcel, 10, getStrokePattern(), false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }

    public final CircleOptions center(LatLng latLng) {
        this.zzcp = latLng;
        return this;
    }

    public final CircleOptions radius(double d) {
        this.zzcq = d;
        return this;
    }

    public final CircleOptions strokeWidth(float f) {
        this.zzcr = f;
        return this;
    }

    public final CircleOptions strokeColor(int i) {
        this.strokeColor = i;
        return this;
    }

    public final CircleOptions strokePattern(@Nullable List<PatternItem> list) {
        this.zzcv = list;
        return this;
    }

    public final CircleOptions fillColor(int i) {
        this.fillColor = i;
        return this;
    }

    public final CircleOptions zIndex(float f) {
        this.zzcs = f;
        return this;
    }

    public final CircleOptions visible(boolean z) {
        this.zzct = z;
        return this;
    }

    public final CircleOptions clickable(boolean z) {
        this.zzcu = z;
        return this;
    }

    public final LatLng getCenter() {
        return this.zzcp;
    }

    public final double getRadius() {
        return this.zzcq;
    }

    public final float getStrokeWidth() {
        return this.zzcr;
    }

    public final int getStrokeColor() {
        return this.strokeColor;
    }

    @Nullable
    public final List<PatternItem> getStrokePattern() {
        return this.zzcv;
    }

    public final int getFillColor() {
        return this.fillColor;
    }

    public final float getZIndex() {
        return this.zzcs;
    }

    public final boolean isVisible() {
        return this.zzct;
    }

    public final boolean isClickable() {
        return this.zzcu;
    }
}

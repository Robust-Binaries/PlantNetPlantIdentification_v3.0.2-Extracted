package com.facebook.yoga;

import com.facebook.proguard.annotations.DoNotStrip;

@DoNotStrip
public class YogaValue {
    static final YogaValue AUTO = new YogaValue(Float.NaN, YogaUnit.AUTO);
    static final YogaValue UNDEFINED = new YogaValue(Float.NaN, YogaUnit.UNDEFINED);
    static final YogaValue ZERO = new YogaValue(0.0f, YogaUnit.POINT);
    public final YogaUnit unit;
    public final float value;

    public YogaValue(float f, YogaUnit yogaUnit) {
        this.value = f;
        this.unit = yogaUnit;
    }

    @DoNotStrip
    YogaValue(float f, int i) {
        this(f, YogaUnit.fromInt(i));
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (obj instanceof YogaValue) {
            YogaValue yogaValue = (YogaValue) obj;
            YogaUnit yogaUnit = this.unit;
            if (yogaUnit == yogaValue.unit) {
                if (yogaUnit == YogaUnit.UNDEFINED || this.unit == YogaUnit.AUTO || Float.compare(this.value, yogaValue.value) == 0) {
                    z = true;
                }
                return z;
            }
        }
        return false;
    }

    public int hashCode() {
        return Float.floatToIntBits(this.value) + this.unit.intValue();
    }

    public String toString() {
        switch (this.unit) {
            case UNDEFINED:
                return "undefined";
            case POINT:
                return Float.toString(this.value);
            case PERCENT:
                StringBuilder sb = new StringBuilder();
                sb.append(this.value);
                sb.append("%");
                return sb.toString();
            case AUTO:
                return "auto";
            default:
                throw new IllegalStateException();
        }
    }

    public static YogaValue parse(String str) {
        if (str == null) {
            return null;
        }
        if ("undefined".equals(str)) {
            return UNDEFINED;
        }
        if ("auto".equals(str)) {
            return AUTO;
        }
        if (str.endsWith("%")) {
            return new YogaValue(Float.parseFloat(str.substring(0, str.length() - 1)), YogaUnit.PERCENT);
        }
        return new YogaValue(Float.parseFloat(str), YogaUnit.POINT);
    }
}

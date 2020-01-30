package com.facebook.react.animated;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import javax.annotation.Nullable;

class InterpolationAnimatedNode extends ValueAnimatedNode {
    public static final String EXTRAPOLATE_TYPE_CLAMP = "clamp";
    public static final String EXTRAPOLATE_TYPE_EXTEND = "extend";
    public static final String EXTRAPOLATE_TYPE_IDENTITY = "identity";
    private final String mExtrapolateLeft;
    private final String mExtrapolateRight;
    private final double[] mInputRange;
    private final double[] mOutputRange;
    @Nullable
    private ValueAnimatedNode mParent;

    private static double[] fromDoubleArray(ReadableArray readableArray) {
        double[] dArr = new double[readableArray.size()];
        for (int i = 0; i < dArr.length; i++) {
            dArr[i] = readableArray.getDouble(i);
        }
        return dArr;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:35:0x008b, code lost:
        if (r1.equals(EXTRAPOLATE_TYPE_EXTEND) != false) goto L_0x008f;
     */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0042  */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x005e  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0060 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0092  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00ae  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00b0 A[RETURN] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static double interpolate(double r11, double r13, double r15, double r17, double r19, java.lang.String r21, java.lang.String r22) {
        /*
            r0 = r21
            r1 = r22
            r2 = 1
            r3 = 0
            r4 = 2
            r5 = 94742715(0x5a5a8bb, float:1.5578507E-35)
            r6 = -135761730(0xfffffffff7e870be, float:-9.428903E33)
            r7 = -1289044198(0xffffffffb32abf1a, float:-3.9755015E-8)
            r8 = -1
            int r9 = (r11 > r13 ? 1 : (r11 == r13 ? 0 : -1))
            if (r9 >= 0) goto L_0x0061
            int r9 = r21.hashCode()
            if (r9 == r7) goto L_0x0034
            if (r9 == r6) goto L_0x002a
            if (r9 == r5) goto L_0x0020
            goto L_0x003e
        L_0x0020:
            java.lang.String r9 = "clamp"
            boolean r9 = r0.equals(r9)
            if (r9 == 0) goto L_0x003e
            r9 = 1
            goto L_0x003f
        L_0x002a:
            java.lang.String r9 = "identity"
            boolean r9 = r0.equals(r9)
            if (r9 == 0) goto L_0x003e
            r9 = 0
            goto L_0x003f
        L_0x0034:
            java.lang.String r9 = "extend"
            boolean r9 = r0.equals(r9)
            if (r9 == 0) goto L_0x003e
            r9 = 2
            goto L_0x003f
        L_0x003e:
            r9 = -1
        L_0x003f:
            switch(r9) {
                case 0: goto L_0x0060;
                case 1: goto L_0x005e;
                case 2: goto L_0x0061;
                default: goto L_0x0042;
            }
        L_0x0042:
            com.facebook.react.bridge.JSApplicationIllegalArgumentException r1 = new com.facebook.react.bridge.JSApplicationIllegalArgumentException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "Invalid extrapolation type "
            r2.append(r3)
            r2.append(r0)
            java.lang.String r0 = "for left extrapolation"
            r2.append(r0)
            java.lang.String r0 = r2.toString()
            r1.<init>(r0)
            throw r1
        L_0x005e:
            r9 = r13
            goto L_0x0062
        L_0x0060:
            return r11
        L_0x0061:
            r9 = r11
        L_0x0062:
            int r0 = (r9 > r15 ? 1 : (r9 == r15 ? 0 : -1))
            if (r0 <= 0) goto L_0x00b1
            int r0 = r22.hashCode()
            if (r0 == r7) goto L_0x0085
            if (r0 == r6) goto L_0x007b
            if (r0 == r5) goto L_0x0071
            goto L_0x008e
        L_0x0071:
            java.lang.String r0 = "clamp"
            boolean r0 = r1.equals(r0)
            if (r0 == 0) goto L_0x008e
            r4 = 1
            goto L_0x008f
        L_0x007b:
            java.lang.String r0 = "identity"
            boolean r0 = r1.equals(r0)
            if (r0 == 0) goto L_0x008e
            r4 = 0
            goto L_0x008f
        L_0x0085:
            java.lang.String r0 = "extend"
            boolean r0 = r1.equals(r0)
            if (r0 == 0) goto L_0x008e
            goto L_0x008f
        L_0x008e:
            r4 = -1
        L_0x008f:
            switch(r4) {
                case 0: goto L_0x00b0;
                case 1: goto L_0x00ae;
                case 2: goto L_0x00b1;
                default: goto L_0x0092;
            }
        L_0x0092:
            com.facebook.react.bridge.JSApplicationIllegalArgumentException r0 = new com.facebook.react.bridge.JSApplicationIllegalArgumentException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "Invalid extrapolation type "
            r2.append(r3)
            r2.append(r1)
            java.lang.String r1 = "for right extrapolation"
            r2.append(r1)
            java.lang.String r1 = r2.toString()
            r0.<init>(r1)
            throw r0
        L_0x00ae:
            r9 = r15
            goto L_0x00b1
        L_0x00b0:
            return r9
        L_0x00b1:
            double r0 = r19 - r17
            double r9 = r9 - r13
            double r0 = r0 * r9
            double r2 = r15 - r13
            double r0 = r0 / r2
            double r0 = r17 + r0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.react.animated.InterpolationAnimatedNode.interpolate(double, double, double, double, double, java.lang.String, java.lang.String):double");
    }

    static double interpolate(double d, double[] dArr, double[] dArr2, String str, String str2) {
        int findRangeIndex = findRangeIndex(d, dArr);
        int i = findRangeIndex + 1;
        return interpolate(d, dArr[findRangeIndex], dArr[i], dArr2[findRangeIndex], dArr2[i], str, str2);
    }

    private static int findRangeIndex(double d, double[] dArr) {
        int i = 1;
        while (i < dArr.length - 1 && dArr[i] < d) {
            i++;
        }
        return i - 1;
    }

    public InterpolationAnimatedNode(ReadableMap readableMap) {
        this.mInputRange = fromDoubleArray(readableMap.getArray("inputRange"));
        this.mOutputRange = fromDoubleArray(readableMap.getArray("outputRange"));
        this.mExtrapolateLeft = readableMap.getString("extrapolateLeft");
        this.mExtrapolateRight = readableMap.getString("extrapolateRight");
    }

    public void onAttachedToNode(AnimatedNode animatedNode) {
        if (this.mParent != null) {
            throw new IllegalStateException("Parent already attached");
        } else if (animatedNode instanceof ValueAnimatedNode) {
            this.mParent = (ValueAnimatedNode) animatedNode;
        } else {
            throw new IllegalArgumentException("Parent is of an invalid type");
        }
    }

    public void onDetachedFromNode(AnimatedNode animatedNode) {
        if (animatedNode == this.mParent) {
            this.mParent = null;
            return;
        }
        throw new IllegalArgumentException("Invalid parent node provided");
    }

    public void update() {
        ValueAnimatedNode valueAnimatedNode = this.mParent;
        if (valueAnimatedNode != null) {
            this.mValue = interpolate(valueAnimatedNode.getValue(), this.mInputRange, this.mOutputRange, this.mExtrapolateLeft, this.mExtrapolateRight);
        }
    }
}

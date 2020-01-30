package org.apache.sanselan.color;

public final class ColorCMYK {

    /* renamed from: C */
    public final double f141C;

    /* renamed from: K */
    public final double f142K;

    /* renamed from: M */
    public final double f143M;

    /* renamed from: Y */
    public final double f144Y;

    public ColorCMYK(double d, double d2, double d3, double d4) {
        this.f141C = d;
        this.f143M = d2;
        this.f144Y = d3;
        this.f142K = d4;
    }

    public final String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("{C: ");
        stringBuffer.append(this.f141C);
        stringBuffer.append(", M: ");
        stringBuffer.append(this.f143M);
        stringBuffer.append(", Y: ");
        stringBuffer.append(this.f144Y);
        stringBuffer.append(", K: ");
        stringBuffer.append(this.f142K);
        stringBuffer.append("}");
        return stringBuffer.toString();
    }
}

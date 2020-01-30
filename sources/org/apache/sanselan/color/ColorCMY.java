package org.apache.sanselan.color;

public final class ColorCMY {

    /* renamed from: C */
    public final double f138C;

    /* renamed from: M */
    public final double f139M;

    /* renamed from: Y */
    public final double f140Y;

    public ColorCMY(double d, double d2, double d3) {
        this.f138C = d;
        this.f139M = d2;
        this.f140Y = d3;
    }

    public final String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("{C: ");
        stringBuffer.append(this.f138C);
        stringBuffer.append(", M: ");
        stringBuffer.append(this.f139M);
        stringBuffer.append(", Y: ");
        stringBuffer.append(this.f140Y);
        stringBuffer.append("}");
        return stringBuffer.toString();
    }
}

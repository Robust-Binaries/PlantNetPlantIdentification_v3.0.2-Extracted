package org.apache.sanselan.color;

public final class ColorXYZ {

    /* renamed from: X */
    public final double f154X;

    /* renamed from: Y */
    public final double f155Y;

    /* renamed from: Z */
    public final double f156Z;

    public ColorXYZ(double d, double d2, double d3) {
        this.f154X = d;
        this.f155Y = d2;
        this.f156Z = d3;
    }

    public final String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("{X: ");
        stringBuffer.append(this.f154X);
        stringBuffer.append(", Y: ");
        stringBuffer.append(this.f155Y);
        stringBuffer.append(", Z: ");
        stringBuffer.append(this.f156Z);
        stringBuffer.append("}");
        return stringBuffer.toString();
    }
}

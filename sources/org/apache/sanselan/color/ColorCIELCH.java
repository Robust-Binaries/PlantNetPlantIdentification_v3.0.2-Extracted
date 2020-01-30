package org.apache.sanselan.color;

public final class ColorCIELCH {

    /* renamed from: C */
    public final double f129C;

    /* renamed from: H */
    public final double f130H;

    /* renamed from: L */
    public final double f131L;

    public ColorCIELCH(double d, double d2, double d3) {
        this.f131L = d;
        this.f129C = d2;
        this.f130H = d3;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("{L: ");
        stringBuffer.append(this.f131L);
        stringBuffer.append(", C: ");
        stringBuffer.append(this.f129C);
        stringBuffer.append(", H: ");
        stringBuffer.append(this.f130H);
        stringBuffer.append("}");
        return stringBuffer.toString();
    }
}

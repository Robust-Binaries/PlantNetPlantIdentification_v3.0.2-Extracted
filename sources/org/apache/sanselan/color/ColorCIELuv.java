package org.apache.sanselan.color;

public final class ColorCIELuv {

    /* renamed from: L */
    public final double f135L;

    /* renamed from: u */
    public final double f136u;

    /* renamed from: v */
    public final double f137v;

    public ColorCIELuv(double d, double d2, double d3) {
        this.f135L = d;
        this.f136u = d2;
        this.f137v = d3;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("{L: ");
        stringBuffer.append(this.f135L);
        stringBuffer.append(", u: ");
        stringBuffer.append(this.f136u);
        stringBuffer.append(", v: ");
        stringBuffer.append(this.f137v);
        stringBuffer.append("}");
        return stringBuffer.toString();
    }
}

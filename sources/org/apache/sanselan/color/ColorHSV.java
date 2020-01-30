package org.apache.sanselan.color;

public final class ColorHSV {

    /* renamed from: H */
    public final double f148H;

    /* renamed from: S */
    public final double f149S;

    /* renamed from: V */
    public final double f150V;

    public ColorHSV(double d, double d2, double d3) {
        this.f148H = d;
        this.f149S = d2;
        this.f150V = d3;
    }

    public final String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("{H: ");
        stringBuffer.append(this.f148H);
        stringBuffer.append(", S: ");
        stringBuffer.append(this.f149S);
        stringBuffer.append(", V: ");
        stringBuffer.append(this.f150V);
        stringBuffer.append("}");
        return stringBuffer.toString();
    }
}

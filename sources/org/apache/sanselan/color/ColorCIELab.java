package org.apache.sanselan.color;

public final class ColorCIELab {

    /* renamed from: L */
    public final double f132L;

    /* renamed from: a */
    public final double f133a;

    /* renamed from: b */
    public final double f134b;

    public ColorCIELab(double d, double d2, double d3) {
        this.f132L = d;
        this.f133a = d2;
        this.f134b = d3;
    }

    public final String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("{L: ");
        stringBuffer.append(this.f132L);
        stringBuffer.append(", a: ");
        stringBuffer.append(this.f133a);
        stringBuffer.append(", b: ");
        stringBuffer.append(this.f134b);
        stringBuffer.append("}");
        return stringBuffer.toString();
    }
}

package org.apache.sanselan.color;

public final class ColorHunterLab {

    /* renamed from: L */
    public final double f151L;

    /* renamed from: a */
    public final double f152a;

    /* renamed from: b */
    public final double f153b;

    public ColorHunterLab(double d, double d2, double d3) {
        this.f151L = d;
        this.f152a = d2;
        this.f153b = d3;
    }

    public final String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("{L: ");
        stringBuffer.append(this.f151L);
        stringBuffer.append(", a: ");
        stringBuffer.append(this.f152a);
        stringBuffer.append(", b: ");
        stringBuffer.append(this.f153b);
        stringBuffer.append("}");
        return stringBuffer.toString();
    }
}

package org.apache.sanselan.color;

public final class ColorHSL {

    /* renamed from: H */
    public final double f145H;

    /* renamed from: L */
    public final double f146L;

    /* renamed from: S */
    public final double f147S;

    public ColorHSL(double d, double d2, double d3) {
        this.f145H = d;
        this.f147S = d2;
        this.f146L = d3;
    }

    public final String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("{H: ");
        stringBuffer.append(this.f145H);
        stringBuffer.append(", S: ");
        stringBuffer.append(this.f147S);
        stringBuffer.append(", L: ");
        stringBuffer.append(this.f146L);
        stringBuffer.append("}");
        return stringBuffer.toString();
    }
}

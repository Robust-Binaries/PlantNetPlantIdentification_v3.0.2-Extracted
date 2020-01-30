package org.apache.sanselan.common;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class RationalNumber extends Number {

    /* renamed from: nf */
    private static final NumberFormat f161nf = DecimalFormat.getInstance();
    private static final long serialVersionUID = -1;
    public final int divisor;
    public final int numerator;

    public RationalNumber(int i, int i2) {
        this.numerator = i;
        this.divisor = i2;
    }

    public static final RationalNumber factoryMethod(long j, long j2) {
        if (j > 2147483647L || j < -2147483648L || j2 > 2147483647L || j2 < -2147483648L) {
            while (true) {
                if ((j > 2147483647L || j < -2147483648L || j2 > 2147483647L || j2 < -2147483648L) && Math.abs(j) > 1 && Math.abs(j2) > 1) {
                    j >>= 1;
                    j2 >>= 1;
                }
            }
            if (j2 == 0) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("Invalid value, numerator: ");
                stringBuffer.append(j);
                stringBuffer.append(", divisor: ");
                stringBuffer.append(j2);
                throw new NumberFormatException(stringBuffer.toString());
            }
        }
        long gcd = gcd(j, j2);
        return new RationalNumber((int) (j / gcd), (int) (j2 / gcd));
    }

    private static long gcd(long j, long j2) {
        return j2 == 0 ? j : gcd(j2, j % j2);
    }

    public RationalNumber negate() {
        return new RationalNumber(-this.numerator, this.divisor);
    }

    public double doubleValue() {
        double d = (double) this.numerator;
        double d2 = (double) this.divisor;
        Double.isNaN(d);
        Double.isNaN(d2);
        return d / d2;
    }

    public float floatValue() {
        return ((float) this.numerator) / ((float) this.divisor);
    }

    public int intValue() {
        return this.numerator / this.divisor;
    }

    public long longValue() {
        return ((long) this.numerator) / ((long) this.divisor);
    }

    public boolean isValid() {
        return this.divisor != 0;
    }

    public String toString() {
        int i = this.divisor;
        if (i == 0) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("Invalid rational (");
            stringBuffer.append(this.numerator);
            stringBuffer.append("/");
            stringBuffer.append(this.divisor);
            stringBuffer.append(")");
            return stringBuffer.toString();
        }
        int i2 = this.numerator;
        if (i2 % i == 0) {
            return f161nf.format((long) (i2 / i));
        }
        StringBuffer stringBuffer2 = new StringBuffer();
        stringBuffer2.append(this.numerator);
        stringBuffer2.append("/");
        stringBuffer2.append(this.divisor);
        stringBuffer2.append(" (");
        NumberFormat numberFormat = f161nf;
        double d = (double) this.numerator;
        double d2 = (double) this.divisor;
        Double.isNaN(d);
        Double.isNaN(d2);
        stringBuffer2.append(numberFormat.format(d / d2));
        stringBuffer2.append(")");
        return stringBuffer2.toString();
    }

    public String toDisplayString() {
        if (this.numerator % this.divisor == 0) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("");
            stringBuffer.append(this.numerator / this.divisor);
            return stringBuffer.toString();
        }
        NumberFormat instance = DecimalFormat.getInstance();
        instance.setMaximumFractionDigits(3);
        double d = (double) this.numerator;
        double d2 = (double) this.divisor;
        Double.isNaN(d);
        Double.isNaN(d2);
        return instance.format(d / d2);
    }
}

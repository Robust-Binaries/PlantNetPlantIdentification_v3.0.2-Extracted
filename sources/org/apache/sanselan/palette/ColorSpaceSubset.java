package org.apache.sanselan.palette;

import java.io.PrintStream;

class ColorSpaceSubset implements Comparable {
    public static long compares;
    public int index;
    public final int[] maxs;
    public final int[] mins;
    public final int precision;
    public final int precision_mask;
    public int rgb;
    public final int total;

    public ColorSpaceSubset(int i, int i2) {
        this.total = i;
        this.precision = i2;
        this.precision_mask = (1 << i2) - 1;
        this.mins = new int[3];
        this.maxs = new int[3];
        for (int i3 = 0; i3 < 3; i3++) {
            this.mins[i3] = 0;
            this.maxs[i3] = this.precision_mask;
        }
        this.rgb = -1;
    }

    public ColorSpaceSubset(int i, int i2, int[] iArr, int[] iArr2, int[] iArr3) {
        this.total = i;
        this.precision = i2;
        this.mins = iArr;
        this.maxs = iArr2;
        this.precision_mask = (1 << i2) - 1;
        this.rgb = -1;
    }

    public final boolean contains(int i, int i2, int i3) {
        compares++;
        int i4 = this.precision;
        int i5 = i >> (8 - i4);
        int[] iArr = this.mins;
        if (iArr[0] > i5) {
            return false;
        }
        int[] iArr2 = this.maxs;
        if (iArr2[0] < i5) {
            return false;
        }
        int i6 = i2 >> (8 - i4);
        if (iArr[1] > i6 || iArr2[1] < i6) {
            return false;
        }
        int i7 = i3 >> (8 - i4);
        if (iArr[2] <= i7 && iArr2[2] >= i7) {
            return true;
        }
        return false;
    }

    public void dump(String str) {
        int[] iArr = this.maxs;
        int i = iArr[0];
        int[] iArr2 = this.mins;
        int i2 = (i - iArr2[0]) + 1;
        int i3 = (iArr[1] - iArr2[1]) + 1;
        int i4 = (iArr[2] - iArr2[2]) + 1;
        int i5 = i2 * i3 * i4;
        PrintStream printStream = System.out;
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(str);
        stringBuffer.append(": [");
        stringBuffer.append(Integer.toHexString(this.rgb));
        stringBuffer.append("] total : ");
        stringBuffer.append(this.total);
        printStream.println(stringBuffer.toString());
        PrintStream printStream2 = System.out;
        StringBuffer stringBuffer2 = new StringBuffer();
        stringBuffer2.append("\trgb: ");
        stringBuffer2.append(Integer.toHexString(this.rgb));
        stringBuffer2.append(", ");
        stringBuffer2.append("red: ");
        stringBuffer2.append(Integer.toHexString(this.mins[0] << (8 - this.precision)));
        stringBuffer2.append(", ");
        stringBuffer2.append(Integer.toHexString(this.maxs[0] << (8 - this.precision)));
        stringBuffer2.append(", ");
        stringBuffer2.append("green: ");
        stringBuffer2.append(Integer.toHexString(this.mins[1] << (8 - this.precision)));
        stringBuffer2.append(", ");
        stringBuffer2.append(Integer.toHexString(this.maxs[1] << (8 - this.precision)));
        stringBuffer2.append(", ");
        stringBuffer2.append("blue: ");
        stringBuffer2.append(Integer.toHexString(this.mins[2] << (8 - this.precision)));
        stringBuffer2.append(", ");
        stringBuffer2.append(Integer.toHexString(this.maxs[2] << (8 - this.precision)));
        printStream2.println(stringBuffer2.toString());
        PrintStream printStream3 = System.out;
        StringBuffer stringBuffer3 = new StringBuffer();
        stringBuffer3.append("\tred: ");
        stringBuffer3.append(this.mins[0]);
        stringBuffer3.append(", ");
        stringBuffer3.append(this.maxs[0]);
        stringBuffer3.append(", ");
        stringBuffer3.append("green: ");
        stringBuffer3.append(this.mins[1]);
        stringBuffer3.append(", ");
        stringBuffer3.append(this.maxs[1]);
        stringBuffer3.append(", ");
        stringBuffer3.append("blue: ");
        stringBuffer3.append(this.mins[2]);
        stringBuffer3.append(", ");
        stringBuffer3.append(this.maxs[2]);
        printStream3.println(stringBuffer3.toString());
        PrintStream printStream4 = System.out;
        StringBuffer stringBuffer4 = new StringBuffer();
        stringBuffer4.append("\trdiff: ");
        stringBuffer4.append(i2);
        stringBuffer4.append(", ");
        stringBuffer4.append("gdiff: ");
        stringBuffer4.append(i3);
        stringBuffer4.append(", ");
        stringBuffer4.append("bdiff: ");
        stringBuffer4.append(i4);
        stringBuffer4.append(", ");
        stringBuffer4.append("color_area: ");
        stringBuffer4.append(i5);
        printStream4.println(stringBuffer4.toString());
    }

    public void dumpJustRGB(String str) {
        PrintStream printStream = System.out;
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("\trgb: ");
        stringBuffer.append(Integer.toHexString(this.rgb));
        stringBuffer.append(", ");
        stringBuffer.append("red: ");
        stringBuffer.append(Integer.toHexString(this.mins[0] << (8 - this.precision)));
        stringBuffer.append(", ");
        stringBuffer.append(Integer.toHexString(this.maxs[0] << (8 - this.precision)));
        stringBuffer.append(", ");
        stringBuffer.append("green: ");
        stringBuffer.append(Integer.toHexString(this.mins[1] << (8 - this.precision)));
        stringBuffer.append(", ");
        stringBuffer.append(Integer.toHexString(this.maxs[1] << (8 - this.precision)));
        stringBuffer.append(", ");
        stringBuffer.append("blue: ");
        stringBuffer.append(Integer.toHexString(this.mins[2] << (8 - this.precision)));
        stringBuffer.append(", ");
        stringBuffer.append(Integer.toHexString(this.maxs[2] << (8 - this.precision)));
        printStream.println(stringBuffer.toString());
    }

    public int getArea() {
        int[] iArr = this.maxs;
        int i = iArr[0];
        int[] iArr2 = this.mins;
        return ((i - iArr2[0]) + 1) * ((iArr[1] - iArr2[1]) + 1) * ((iArr[2] - iArr2[2]) + 1);
    }

    public void setAverageRGB(int[] iArr) {
        ColorSpaceSubset colorSpaceSubset = this;
        int i = colorSpaceSubset.mins[0];
        long j = 0;
        long j2 = 0;
        long j3 = 0;
        while (i <= colorSpaceSubset.maxs[0]) {
            int i2 = colorSpaceSubset.mins[1];
            for (char c = 1; i2 <= colorSpaceSubset.maxs[c]; c = 1) {
                int i3 = colorSpaceSubset.mins[2];
                while (i3 <= colorSpaceSubset.maxs[2]) {
                    int i4 = colorSpaceSubset.precision;
                    int i5 = iArr[(i3 << (i4 * 2)) | (i2 << (i4 * 1)) | (i << (i4 * 0))];
                    j += (long) (i5 * (i << (8 - i4)));
                    j2 += (long) ((i2 << (8 - i4)) * i5);
                    j3 += (long) (i5 * (i3 << (8 - i4)));
                    i3++;
                    i = i;
                    colorSpaceSubset = this;
                }
                int i6 = i;
                i2++;
                colorSpaceSubset = this;
            }
            i++;
            colorSpaceSubset = this;
        }
        int i7 = colorSpaceSubset.total;
        colorSpaceSubset.rgb = (int) ((((j3 / ((long) i7)) & 255) << 0) | (((j / ((long) i7)) & 255) << 16) | (((j2 / ((long) i7)) & 255) << 8));
    }

    public int compareTo(Object obj) {
        return this.rgb - ((ColorSpaceSubset) obj).rgb;
    }

    public final void setIndex(int i) {
        this.index = i;
    }
}

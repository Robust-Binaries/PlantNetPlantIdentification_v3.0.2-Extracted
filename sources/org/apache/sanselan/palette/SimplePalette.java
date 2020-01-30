package org.apache.sanselan.palette;

import org.apache.sanselan.util.Debug;

public class SimplePalette extends Palette {
    private final int[] palette;

    public SimplePalette(int[] iArr) {
        this.palette = iArr;
    }

    public int getPaletteIndex(int i) {
        return getPaletteIndex(this.palette, i);
    }

    public int getEntry(int i) {
        return this.palette[i];
    }

    private int getPaletteIndex(int[] iArr, int i) {
        for (int i2 = 0; i2 < iArr.length; i2++) {
            if (iArr[i2] == i) {
                return i2;
            }
        }
        return -1;
    }

    public void dump() {
        for (int i = 0; i < this.palette.length; i++) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("\tpalette[");
            stringBuffer.append(i);
            stringBuffer.append("]");
            String stringBuffer2 = stringBuffer.toString();
            StringBuffer stringBuffer3 = new StringBuffer();
            stringBuffer3.append(this.palette[i]);
            stringBuffer3.append(" (0x");
            stringBuffer3.append(Integer.toHexString(this.palette[i]));
            stringBuffer3.append(")");
            Debug.debug(stringBuffer2, stringBuffer3.toString());
        }
    }

    public int length() {
        return this.palette.length;
    }
}

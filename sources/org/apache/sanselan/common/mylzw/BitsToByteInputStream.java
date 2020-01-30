package org.apache.sanselan.common.mylzw;

import java.io.IOException;
import java.io.InputStream;

public class BitsToByteInputStream extends InputStream {
    private final int desiredDepth;

    /* renamed from: is */
    private final MyBitInputStream f163is;

    public BitsToByteInputStream(MyBitInputStream myBitInputStream, int i) {
        this.f163is = myBitInputStream;
        this.desiredDepth = i;
    }

    public int read() throws IOException {
        return readBits(8);
    }

    public int readBits(int i) throws IOException {
        int readBits = this.f163is.readBits(i);
        int i2 = this.desiredDepth;
        if (i < i2) {
            return readBits << (i2 - i);
        }
        return i > i2 ? readBits >> (i - i2) : readBits;
    }

    public int[] readBitsArray(int i, int i2) throws IOException {
        int[] iArr = new int[i2];
        for (int i3 = 0; i3 < i2; i3++) {
            iArr[i3] = readBits(i);
        }
        return iArr;
    }
}

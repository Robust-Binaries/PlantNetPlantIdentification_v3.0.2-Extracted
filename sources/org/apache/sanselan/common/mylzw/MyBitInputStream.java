package org.apache.sanselan.common.mylzw;

import android.support.p000v4.view.InputDeviceCompat;
import java.io.IOException;
import java.io.InputStream;
import org.apache.sanselan.common.BinaryConstants;

public class MyBitInputStream extends InputStream implements BinaryConstants {
    private int bitCache = 0;
    private int bitsInCache = 0;
    private final int byteOrder;
    private long bytesRead = 0;

    /* renamed from: is */
    private final InputStream f164is;
    private boolean tiffLZWMode = false;

    public MyBitInputStream(InputStream inputStream, int i) {
        this.byteOrder = i;
        this.f164is = inputStream;
    }

    public int read() throws IOException {
        return readBits(8);
    }

    public void setTiffLZWMode() {
        this.tiffLZWMode = true;
    }

    public int readBits(int i) throws IOException {
        int i2;
        while (true) {
            int i3 = this.bitsInCache;
            if (i3 < i) {
                int read = this.f164is.read();
                if (read >= 0) {
                    int i4 = read & 255;
                    int i5 = this.byteOrder;
                    if (i5 == 77) {
                        this.bitCache = i4 | (this.bitCache << 8);
                    } else if (i5 == 73) {
                        this.bitCache = (i4 << this.bitsInCache) | this.bitCache;
                    } else {
                        StringBuffer stringBuffer = new StringBuffer();
                        stringBuffer.append("Unknown byte order: ");
                        stringBuffer.append(this.byteOrder);
                        throw new IOException(stringBuffer.toString());
                    }
                    this.bytesRead++;
                    this.bitsInCache += 8;
                } else if (this.tiffLZWMode) {
                    return InputDeviceCompat.SOURCE_KEYBOARD;
                } else {
                    return -1;
                }
            } else {
                int i6 = (1 << i) - 1;
                int i7 = this.byteOrder;
                if (i7 == 77) {
                    i2 = (this.bitCache >> (i3 - i)) & i6;
                } else if (i7 == 73) {
                    int i8 = this.bitCache;
                    int i9 = i6 & i8;
                    this.bitCache = i8 >> i;
                    i2 = i9;
                } else {
                    StringBuffer stringBuffer2 = new StringBuffer();
                    stringBuffer2.append("Unknown byte order: ");
                    stringBuffer2.append(this.byteOrder);
                    throw new IOException(stringBuffer2.toString());
                }
                this.bitsInCache -= i;
                this.bitCache = ((1 << this.bitsInCache) - 1) & this.bitCache;
                return i2;
            }
        }
    }

    public void flushCache() {
        this.bitsInCache = 0;
        this.bitCache = 0;
    }

    public long getBytesRead() {
        return this.bytesRead;
    }
}

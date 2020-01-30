package org.apache.sanselan.common.mylzw;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.sanselan.common.BinaryConstants;

public class MyBitOutputStream extends OutputStream implements BinaryConstants {
    private int bitCache = 0;
    private int bitsInCache = 0;
    private final int byteOrder;
    private int bytesWritten = 0;

    /* renamed from: os */
    private final OutputStream f165os;

    public MyBitOutputStream(OutputStream outputStream, int i) {
        this.byteOrder = i;
        this.f165os = outputStream;
    }

    public void write(int i) throws IOException {
        writeBits(i, 8);
    }

    public void writeBits(int i, int i2) throws IOException {
        int i3 = i & ((1 << i2) - 1);
        int i4 = this.byteOrder;
        if (i4 == 77) {
            this.bitCache = i3 | (this.bitCache << i2);
        } else if (i4 == 73) {
            this.bitCache = (i3 << this.bitsInCache) | this.bitCache;
        } else {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("Unknown byte order: ");
            stringBuffer.append(this.byteOrder);
            throw new IOException(stringBuffer.toString());
        }
        this.bitsInCache += i2;
        while (true) {
            int i5 = this.bitsInCache;
            if (i5 >= 8) {
                int i6 = this.byteOrder;
                if (i6 == 77) {
                    actualWrite((this.bitCache >> (i5 - 8)) & 255);
                    this.bitsInCache -= 8;
                } else if (i6 == 73) {
                    actualWrite(this.bitCache & 255);
                    this.bitCache >>= 8;
                    this.bitsInCache -= 8;
                }
                this.bitCache = ((1 << this.bitsInCache) - 1) & this.bitCache;
            } else {
                return;
            }
        }
    }

    private void actualWrite(int i) throws IOException {
        this.f165os.write(i);
        this.bytesWritten++;
    }

    public void flushCache() throws IOException {
        int i = this.bitsInCache;
        if (i > 0) {
            int i2 = this.bitCache & ((1 << i) - 1);
            int i3 = this.byteOrder;
            if (i3 == 77) {
                this.f165os.write(i2 << (8 - i));
            } else if (i3 == 73) {
                this.f165os.write(i2);
            }
        }
        this.bitsInCache = 0;
        this.bitCache = 0;
    }

    public int getBytesWritten() {
        return this.bytesWritten + (this.bitsInCache > 0 ? 1 : 0);
    }
}

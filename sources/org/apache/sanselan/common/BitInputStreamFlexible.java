package org.apache.sanselan.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

public class BitInputStreamFlexible extends InputStream implements BinaryConstants {
    private long bytesRead = 0;
    private int cache;
    private int cacheBitsRemaining = 0;

    /* renamed from: is */
    private final InputStream f160is;

    public BitInputStreamFlexible(InputStream inputStream) {
        this.f160is = inputStream;
    }

    public int read() throws IOException {
        if (this.cacheBitsRemaining <= 0) {
            return this.f160is.read();
        }
        throw new IOException("BitInputStream: incomplete bit read");
    }

    public final int readBits(int i) throws IOException {
        if (i <= 32) {
            int i2 = this.cacheBitsRemaining;
            int i3 = 0;
            if (i2 > 0) {
                if (i >= i2) {
                    int i4 = ((1 << i2) - 1) & this.cache;
                    i -= i2;
                    this.cacheBitsRemaining = 0;
                    i3 = i4;
                } else {
                    this.cacheBitsRemaining = i2 - i;
                    i3 = ((1 << i) - 1) & (this.cache >> this.cacheBitsRemaining);
                    i = 0;
                }
            }
            while (i >= 8) {
                this.cache = this.f160is.read();
                if (this.cache >= 0) {
                    PrintStream printStream = System.out;
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append("cache 1: ");
                    stringBuffer.append(this.cache);
                    stringBuffer.append(" (");
                    stringBuffer.append(Integer.toHexString(this.cache));
                    stringBuffer.append(", ");
                    stringBuffer.append(Integer.toBinaryString(this.cache));
                    stringBuffer.append(")");
                    printStream.println(stringBuffer.toString());
                    this.bytesRead++;
                    i3 = (this.cache & 255) | (i3 << 8);
                    i -= 8;
                } else {
                    throw new IOException("couldn't read bits");
                }
            }
            if (i <= 0) {
                return i3;
            }
            this.cache = this.f160is.read();
            if (this.cache >= 0) {
                PrintStream printStream2 = System.out;
                StringBuffer stringBuffer2 = new StringBuffer();
                stringBuffer2.append("cache 2: ");
                stringBuffer2.append(this.cache);
                stringBuffer2.append(" (");
                stringBuffer2.append(Integer.toHexString(this.cache));
                stringBuffer2.append(", ");
                stringBuffer2.append(Integer.toBinaryString(this.cache));
                stringBuffer2.append(")");
                printStream2.println(stringBuffer2.toString());
                this.bytesRead++;
                this.cacheBitsRemaining = 8 - i;
                return (i3 << i) | (((1 << i) - 1) & (this.cache >> this.cacheBitsRemaining));
            }
            throw new IOException("couldn't read bits");
        }
        throw new IOException("BitInputStream: unknown error");
    }

    public void flushCache() {
        this.cacheBitsRemaining = 0;
    }

    public long getBytesRead() {
        return this.bytesRead;
    }
}

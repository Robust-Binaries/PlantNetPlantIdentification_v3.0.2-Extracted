package org.apache.sanselan.common;

import java.io.IOException;
import java.io.InputStream;

public class BitInputStream extends InputStream implements BinaryConstants {
    private long bytes_read = 0;
    private int cache;
    private int cacheBitsRemaining = 0;

    /* renamed from: is */
    private final InputStream f159is;

    public BitInputStream(InputStream inputStream) {
        this.f159is = inputStream;
    }

    public int read() throws IOException {
        if (this.cacheBitsRemaining <= 0) {
            return this.f159is.read();
        }
        throw new IOException("BitInputStream: incomplete bit read");
    }

    public final int readBits(int i) throws IOException {
        if (i < 8) {
            if (this.cacheBitsRemaining == 0) {
                this.cache = this.f159is.read();
                this.cacheBitsRemaining = 8;
                this.bytes_read++;
            }
            int i2 = this.cacheBitsRemaining;
            if (i <= i2) {
                this.cacheBitsRemaining = i2 - i;
                int i3 = this.cache >> this.cacheBitsRemaining;
                switch (i) {
                    case 1:
                        return i3 & 1;
                    case 2:
                        return i3 & 3;
                    case 3:
                        return i3 & 7;
                    case 4:
                        return i3 & 15;
                    case 5:
                        return i3 & 31;
                    case 6:
                        return i3 & 63;
                    case 7:
                        return i3 & 127;
                }
            } else {
                throw new IOException("BitInputStream: can't read bit fields across bytes");
            }
        }
        if (this.cacheBitsRemaining > 0) {
            throw new IOException("BitInputStream: incomplete bit read");
        } else if (i == 8) {
            this.bytes_read++;
            return this.f159is.read();
        } else if (i == 16) {
            this.bytes_read += 2;
            return (this.f159is.read() << 8) | (this.f159is.read() << 0);
        } else if (i == 24) {
            this.bytes_read += 3;
            return (this.f159is.read() << 16) | (this.f159is.read() << 8) | (this.f159is.read() << 0);
        } else if (i == 32) {
            this.bytes_read += 4;
            return (this.f159is.read() << 24) | (this.f159is.read() << 16) | (this.f159is.read() << 8) | (this.f159is.read() << 0);
        } else {
            throw new IOException("BitInputStream: unknown error");
        }
    }

    public void flushCache() {
        this.cacheBitsRemaining = 0;
    }

    public long getBytesRead() {
        return this.bytes_read;
    }
}

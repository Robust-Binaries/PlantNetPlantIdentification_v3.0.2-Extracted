package org.apache.sanselan.util;

import java.io.IOException;
import java.io.OutputStream;

public class DebugOutputStream extends OutputStream {
    private long count = 0;

    /* renamed from: os */
    private final OutputStream f181os;

    public DebugOutputStream(OutputStream outputStream) {
        this.f181os = outputStream;
    }

    public void write(int i) throws IOException {
        this.f181os.write(i);
        this.count++;
    }

    public void write(byte[] bArr) throws IOException {
        this.f181os.write(bArr);
        this.count += (long) bArr.length;
    }

    public void write(byte[] bArr, int i, int i2) throws IOException {
        this.f181os.write(bArr, i, i2);
        this.count += (long) i2;
    }

    public void flush() throws IOException {
        this.f181os.flush();
    }

    public void close() throws IOException {
        this.f181os.close();
    }

    public long count() {
        return this.count;
    }
}

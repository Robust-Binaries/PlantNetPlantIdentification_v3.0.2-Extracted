package org.apache.sanselan.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CachingOutputStream extends OutputStream {
    private final ByteArrayOutputStream baos = new ByteArrayOutputStream();

    /* renamed from: os */
    private final OutputStream f179os;

    public CachingOutputStream(OutputStream outputStream) {
        this.f179os = outputStream;
    }

    public void write(int i) throws IOException {
        this.f179os.write(i);
        this.baos.write(i);
    }

    public byte[] getCache() {
        return this.baos.toByteArray();
    }

    public void close() throws IOException {
        this.f179os.close();
    }

    public void flush() throws IOException {
        this.f179os.flush();
    }
}

package org.apache.sanselan.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CachingInputStream extends InputStream {
    private final ByteArrayOutputStream baos = new ByteArrayOutputStream();

    /* renamed from: is */
    private final InputStream f178is;

    public CachingInputStream(InputStream inputStream) {
        this.f178is = inputStream;
    }

    public byte[] getCache() {
        return this.baos.toByteArray();
    }

    public int read() throws IOException {
        int read = this.f178is.read();
        this.baos.write(read);
        return read;
    }

    public int available() throws IOException {
        return this.f178is.available();
    }

    public void close() throws IOException {
        this.f178is.close();
    }
}

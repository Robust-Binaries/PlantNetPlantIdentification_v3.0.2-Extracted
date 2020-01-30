package org.apache.sanselan.util;

import java.io.IOException;
import java.io.InputStream;

public class DebugInputStream extends InputStream {
    private long bytes_read = 0;

    /* renamed from: is */
    private final InputStream f180is;

    public DebugInputStream(InputStream inputStream) {
        this.f180is = inputStream;
    }

    public int read() throws IOException {
        int read = this.f180is.read();
        this.bytes_read++;
        return read;
    }

    public long skip(long j) throws IOException {
        long skip = this.f180is.skip(j);
        this.bytes_read += j;
        return skip;
    }

    public int available() throws IOException {
        return this.f180is.available();
    }

    public void close() throws IOException {
        this.f180is.close();
    }

    public long getBytesRead() {
        return this.bytes_read;
    }
}

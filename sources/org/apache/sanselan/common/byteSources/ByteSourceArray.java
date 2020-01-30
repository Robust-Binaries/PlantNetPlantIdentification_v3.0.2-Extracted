package org.apache.sanselan.common.byteSources;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ByteSourceArray extends ByteSource {
    private final byte[] bytes;

    public ByteSourceArray(String str, byte[] bArr) {
        super(str);
        this.bytes = bArr;
    }

    public ByteSourceArray(byte[] bArr) {
        super(null);
        this.bytes = bArr;
    }

    public InputStream getInputStream() {
        return new ByteArrayInputStream(this.bytes);
    }

    public byte[] getBlock(int i, int i2) throws IOException {
        int i3 = i + i2;
        byte[] bArr = this.bytes;
        if (i3 <= bArr.length) {
            byte[] bArr2 = new byte[i2];
            System.arraycopy(bArr, i, bArr2, 0, i2);
            return bArr2;
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Could not read block (block start: ");
        stringBuffer.append(i);
        stringBuffer.append(", block length: ");
        stringBuffer.append(i2);
        stringBuffer.append(", data length: ");
        stringBuffer.append(this.bytes.length);
        stringBuffer.append(").");
        throw new IOException(stringBuffer.toString());
    }

    public long getLength() {
        return (long) this.bytes.length;
    }

    public byte[] getAll() throws IOException {
        return this.bytes;
    }

    public String getDescription() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.bytes.length);
        stringBuffer.append(" byte array");
        return stringBuffer.toString();
    }
}

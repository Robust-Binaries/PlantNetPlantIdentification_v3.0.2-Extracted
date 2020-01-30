package org.apache.sanselan.common;

import java.io.IOException;
import java.io.OutputStream;

public class MyByteArrayOutputStream extends OutputStream {
    private final byte[] bytes;
    private int count = 0;

    public MyByteArrayOutputStream(int i) {
        this.bytes = new byte[i];
    }

    public void write(int i) throws IOException {
        int i2 = this.count;
        byte[] bArr = this.bytes;
        if (i2 < bArr.length) {
            bArr[i2] = (byte) i;
            this.count = i2 + 1;
            return;
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Write exceeded expected length (");
        stringBuffer.append(this.count);
        stringBuffer.append(", ");
        stringBuffer.append(this.bytes.length);
        stringBuffer.append(")");
        throw new IOException(stringBuffer.toString());
    }

    public byte[] toByteArray() {
        int i = this.count;
        byte[] bArr = this.bytes;
        if (i >= bArr.length) {
            return bArr;
        }
        byte[] bArr2 = new byte[i];
        System.arraycopy(bArr, 0, bArr2, 0, i);
        return bArr2;
    }

    public int getBytesWritten() {
        return this.count;
    }
}

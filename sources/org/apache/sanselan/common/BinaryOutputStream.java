package org.apache.sanselan.common;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.sanselan.ImageWriteException;

public class BinaryOutputStream extends OutputStream implements BinaryConstants {
    private int byteOrder = 77;
    private int count = 0;
    protected boolean debug = false;

    /* renamed from: os */
    private final OutputStream f158os;

    public final void setDebug(boolean z) {
        this.debug = z;
    }

    public final boolean getDebug() {
        return this.debug;
    }

    public BinaryOutputStream(OutputStream outputStream, int i) {
        this.byteOrder = i;
        this.f158os = outputStream;
    }

    public BinaryOutputStream(OutputStream outputStream) {
        this.f158os = outputStream;
    }

    /* access modifiers changed from: protected */
    public void setByteOrder(int i, int i2) throws ImageWriteException, IOException {
        if (i != i2) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("Byte Order bytes don't match (");
            stringBuffer.append(i);
            stringBuffer.append(", ");
            stringBuffer.append(i2);
            stringBuffer.append(").");
            throw new ImageWriteException(stringBuffer.toString());
        } else if (i == 77) {
            this.byteOrder = i;
        } else if (i == 73) {
            this.byteOrder = i;
        } else {
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2.append("Unknown Byte Order hint: ");
            stringBuffer2.append(i);
            throw new ImageWriteException(stringBuffer2.toString());
        }
    }

    /* access modifiers changed from: protected */
    public void setByteOrder(int i) {
        this.byteOrder = i;
    }

    public int getByteOrder() {
        return this.byteOrder;
    }

    public void write(int i) throws IOException {
        this.f158os.write(i);
        this.count++;
    }

    public int getByteCount() {
        return this.count;
    }

    public final void write4Bytes(int i) throws ImageWriteException, IOException {
        writeNBytes(i, 4);
    }

    public final void write3Bytes(int i) throws ImageWriteException, IOException {
        writeNBytes(i, 3);
    }

    public final void write2Bytes(int i) throws ImageWriteException, IOException {
        writeNBytes(i, 2);
    }

    public final void write4ByteInteger(int i) throws ImageWriteException, IOException {
        if (this.byteOrder == 77) {
            write((i >> 24) & 255);
            write((i >> 16) & 255);
            write((i >> 8) & 255);
            write(i & 255);
            return;
        }
        write(i & 255);
        write((i >> 8) & 255);
        write((i >> 16) & 255);
        write((i >> 24) & 255);
    }

    public final void write2ByteInteger(int i) throws ImageWriteException, IOException {
        if (this.byteOrder == 77) {
            write((i >> 8) & 255);
            write(i & 255);
            return;
        }
        write(i & 255);
        write((i >> 8) & 255);
    }

    public final void writeByteArray(byte[] bArr) throws IOException {
        this.f158os.write(bArr, 0, bArr.length);
        this.count += bArr.length;
    }

    private byte[] convertValueToByteArray(int i, int i2) {
        byte[] bArr = new byte[i2];
        int i3 = 0;
        if (this.byteOrder == 77) {
            while (i3 < i2) {
                bArr[i3] = (byte) ((i >> (((i2 - i3) - 1) * 8)) & 255);
                i3++;
            }
        } else {
            while (i3 < i2) {
                bArr[i3] = (byte) ((i >> (i3 * 8)) & 255);
                i3++;
            }
        }
        return bArr;
    }

    private final void writeNBytes(int i, int i2) throws ImageWriteException, IOException {
        write(convertValueToByteArray(i, i2));
    }
}

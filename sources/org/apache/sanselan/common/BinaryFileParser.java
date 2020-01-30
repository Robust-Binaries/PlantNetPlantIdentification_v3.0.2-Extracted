package org.apache.sanselan.common;

import java.io.IOException;
import java.io.InputStream;
import org.apache.sanselan.ImageReadException;

public class BinaryFileParser extends BinaryFileFunctions {
    private int byteOrder = 77;

    public BinaryFileParser(int i) {
        this.byteOrder = i;
    }

    public BinaryFileParser() {
    }

    /* access modifiers changed from: protected */
    public void setByteOrder(int i, int i2) throws ImageReadException, IOException {
        if (i != i2) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("Byte Order bytes don't match (");
            stringBuffer.append(i);
            stringBuffer.append(", ");
            stringBuffer.append(i2);
            stringBuffer.append(").");
            throw new ImageReadException(stringBuffer.toString());
        } else if (i == 77) {
            this.byteOrder = i;
        } else if (i == 73) {
            this.byteOrder = i;
        } else {
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2.append("Unknown Byte Order hint: ");
            stringBuffer2.append(i);
            throw new ImageReadException(stringBuffer2.toString());
        }
    }

    /* access modifiers changed from: protected */
    public void setByteOrder(int i) {
        this.byteOrder = i;
    }

    /* access modifiers changed from: protected */
    public int getByteOrder() {
        return this.byteOrder;
    }

    /* access modifiers changed from: protected */
    public final int convertByteArrayToInt(String str, int i, byte[] bArr) {
        return convertByteArrayToInt(str, bArr, i, this.byteOrder);
    }

    /* access modifiers changed from: protected */
    public final int convertByteArrayToInt(String str, byte[] bArr) {
        return convertByteArrayToInt(str, bArr, this.byteOrder);
    }

    public final int convertByteArrayToShort(String str, byte[] bArr) throws ImageReadException {
        return convertByteArrayToShort(str, bArr, this.byteOrder);
    }

    public final int convertByteArrayToShort(String str, int i, byte[] bArr) throws ImageReadException {
        return convertByteArrayToShort(str, i, bArr, this.byteOrder);
    }

    public final int read4Bytes(String str, InputStream inputStream, String str2) throws ImageReadException, IOException {
        return read4Bytes(str, inputStream, str2, this.byteOrder);
    }

    public final int read3Bytes(String str, InputStream inputStream, String str2) throws ImageReadException, IOException {
        return read3Bytes(str, inputStream, str2, this.byteOrder);
    }

    public final int read2Bytes(String str, InputStream inputStream, String str2) throws ImageReadException, IOException {
        return read2Bytes(str, inputStream, str2, this.byteOrder);
    }

    public static boolean byteArrayHasPrefix(byte[] bArr, byte[] bArr2) {
        if (bArr == null || bArr.length < bArr2.length) {
            return false;
        }
        for (int i = 0; i < bArr2.length; i++) {
            if (bArr[i] != bArr2[i]) {
                return false;
            }
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public final byte[] int2ToByteArray(int i) {
        return int2ToByteArray(i, this.byteOrder);
    }
}

package org.apache.sanselan.common;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import kotlin.UByte;
import org.apache.sanselan.ImageReadException;

public class BinaryInputStream extends InputStream implements BinaryConstants {
    private int byteOrder = 77;
    protected boolean debug = false;

    /* renamed from: is */
    private final InputStream f157is;

    protected static final int CharsToQuad(char c, char c2, char c3, char c4) {
        return ((c & 255) << 24) | ((c2 & 255) << 16) | ((c3 & 255) << 8) | ((c4 & 255) << 0);
    }

    public final void setDebug(boolean z) {
        this.debug = z;
    }

    public final boolean getDebug() {
        return this.debug;
    }

    public BinaryInputStream(byte[] bArr, int i) {
        this.byteOrder = i;
        this.f157is = new ByteArrayInputStream(bArr);
    }

    public BinaryInputStream(InputStream inputStream, int i) {
        this.byteOrder = i;
        this.f157is = inputStream;
    }

    public BinaryInputStream(InputStream inputStream) {
        this.f157is = inputStream;
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

    public int read() throws IOException {
        return this.f157is.read();
    }

    /* access modifiers changed from: protected */
    public final int convertByteArrayToInt(String str, byte[] bArr) {
        return convertByteArrayToInt(str, bArr, this.byteOrder);
    }

    public final int convertByteArrayToShort(String str, byte[] bArr) {
        return convertByteArrayToShort(str, bArr, this.byteOrder);
    }

    public final int convertByteArrayToShort(String str, int i, byte[] bArr) {
        return convertByteArrayToShort(str, i, bArr, this.byteOrder);
    }

    public final int read4Bytes(String str, String str2) throws ImageReadException, IOException {
        return read4Bytes(str, str2, this.byteOrder);
    }

    public final int read3Bytes(String str, String str2) throws ImageReadException, IOException {
        return read3Bytes(str, str2, this.byteOrder);
    }

    public final int read2Bytes(String str, String str2) throws ImageReadException, IOException {
        return read2Bytes(str, str2, this.byteOrder);
    }

    /* access modifiers changed from: protected */
    public final void readRandomBytes() throws ImageReadException, IOException {
        for (int i = 0; i < 100; i++) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("");
            stringBuffer.append(i);
            readByte(stringBuffer.toString(), "Random Data");
        }
    }

    public final void debugNumber(String str, int i) {
        debugNumber(str, i, 1);
    }

    public final void debugNumber(String str, int i, int i2) {
        PrintStream printStream = System.out;
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(str);
        stringBuffer.append(": ");
        stringBuffer.append(i);
        stringBuffer.append(" (");
        printStream.print(stringBuffer.toString());
        int i3 = i;
        for (int i4 = 0; i4 < i2; i4++) {
            if (i4 > 0) {
                System.out.print(",");
            }
            int i5 = i3 & 255;
            PrintStream printStream2 = System.out;
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2.append((char) i5);
            stringBuffer2.append(" [");
            stringBuffer2.append(i5);
            stringBuffer2.append("]");
            printStream2.print(stringBuffer2.toString());
            i3 >>= 8;
        }
        PrintStream printStream3 = System.out;
        StringBuffer stringBuffer3 = new StringBuffer();
        stringBuffer3.append(") [0x");
        stringBuffer3.append(Integer.toHexString(i));
        stringBuffer3.append(", ");
        stringBuffer3.append(Integer.toBinaryString(i));
        stringBuffer3.append("]");
        printStream3.println(stringBuffer3.toString());
    }

    public final void readAndVerifyBytes(byte[] bArr, String str) throws ImageReadException, IOException {
        for (int i = 0; i < bArr.length; i++) {
            int read = this.f157is.read();
            byte b = (byte) (read & 255);
            if (read < 0 || b != bArr[i]) {
                PrintStream printStream = System.out;
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("i: ");
                stringBuffer.append(i);
                printStream.println(stringBuffer.toString());
                debugByteArray("expected", bArr);
                StringBuffer stringBuffer2 = new StringBuffer();
                stringBuffer2.append("data[");
                stringBuffer2.append(i);
                stringBuffer2.append("]");
                debugNumber(stringBuffer2.toString(), b);
                throw new ImageReadException(str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public final void readAndVerifyBytes(String str, byte[] bArr, String str2) throws ImageReadException, IOException {
        byte[] readByteArray = readByteArray(str, bArr.length, str2);
        int i = 0;
        while (i < bArr.length) {
            if (readByteArray[i] == bArr[i]) {
                i++;
            } else {
                PrintStream printStream = System.out;
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("i: ");
                stringBuffer.append(i);
                printStream.println(stringBuffer.toString());
                StringBuffer stringBuffer2 = new StringBuffer();
                stringBuffer2.append("bytes[");
                stringBuffer2.append(i);
                stringBuffer2.append("]");
                debugNumber(stringBuffer2.toString(), readByteArray[i]);
                StringBuffer stringBuffer3 = new StringBuffer();
                stringBuffer3.append("expected[");
                stringBuffer3.append(i);
                stringBuffer3.append("]");
                debugNumber(stringBuffer3.toString(), bArr[i]);
                throw new ImageReadException(str2);
            }
        }
    }

    public final void skipBytes(int i, String str) throws IOException {
        long j = 0;
        while (true) {
            long j2 = (long) i;
            if (j2 != j) {
                long skip = this.f157is.skip(j2 - j);
                if (skip >= 1) {
                    j += skip;
                } else {
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append(str);
                    stringBuffer.append(" (");
                    stringBuffer.append(skip);
                    stringBuffer.append(")");
                    throw new IOException(stringBuffer.toString());
                }
            } else {
                return;
            }
        }
    }

    /* access modifiers changed from: protected */
    public final void scanForByte(byte b) throws IOException {
        int i = 0;
        int i2 = 0;
        while (i < 3) {
            int read = this.f157is.read();
            if (read >= 0) {
                if ((read & UByte.MAX_VALUE) == b) {
                    PrintStream printStream = System.out;
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append("\t");
                    stringBuffer.append(i2);
                    stringBuffer.append(": match.");
                    printStream.println(stringBuffer.toString());
                    i++;
                }
                i2++;
            } else {
                return;
            }
        }
    }

    public final byte readByte(String str, String str2) throws IOException {
        int read = this.f157is.read();
        if (read >= 0) {
            if (this.debug) {
                debugNumber(str, read);
            }
            return (byte) (read & 255);
        }
        PrintStream printStream = System.out;
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(str);
        stringBuffer.append(": ");
        stringBuffer.append(read);
        printStream.println(stringBuffer.toString());
        throw new IOException(str2);
    }

    /* access modifiers changed from: protected */
    public final RationalNumber[] convertByteArrayToRationalArray(String str, byte[] bArr, int i, int i2, int i3) {
        int i4 = (i2 * 8) + i;
        if (bArr.length < i4) {
            PrintStream printStream = System.out;
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(str);
            stringBuffer.append(": expected length: ");
            stringBuffer.append(i4);
            stringBuffer.append(", actual length: ");
            stringBuffer.append(bArr.length);
            printStream.println(stringBuffer.toString());
            return null;
        }
        RationalNumber[] rationalNumberArr = new RationalNumber[i2];
        for (int i5 = 0; i5 < i2; i5++) {
            rationalNumberArr[i5] = convertByteArrayToRational(str, bArr, (i5 * 8) + i, i3);
        }
        return rationalNumberArr;
    }

    /* access modifiers changed from: protected */
    public final RationalNumber convertByteArrayToRational(String str, byte[] bArr, int i) {
        return convertByteArrayToRational(str, bArr, 0, i);
    }

    /* access modifiers changed from: protected */
    public final RationalNumber convertByteArrayToRational(String str, byte[] bArr, int i, int i2) {
        return new RationalNumber(convertByteArrayToInt(str, bArr, i + 0, 4, i2), convertByteArrayToInt(str, bArr, i + 4, 4, i2));
    }

    /* access modifiers changed from: protected */
    public final int convertByteArrayToInt(String str, byte[] bArr, int i) {
        return convertByteArrayToInt(str, bArr, 0, 4, i);
    }

    /* access modifiers changed from: protected */
    public final int convertByteArrayToInt(String str, byte[] bArr, int i, int i2, int i3) {
        byte b;
        int i4;
        byte b2 = bArr[i + 0];
        byte b3 = bArr[i + 1];
        byte b4 = bArr[i + 2];
        if (i2 == 4) {
            b = bArr[i + 3];
        } else {
            b = 0;
        }
        if (i3 == 77) {
            i4 = ((b2 & UByte.MAX_VALUE) << 24) + ((b3 & UByte.MAX_VALUE) << 16) + ((b4 & UByte.MAX_VALUE) << 8) + ((b & UByte.MAX_VALUE) << 0);
        } else {
            i4 = ((b2 & UByte.MAX_VALUE) << 0) + ((b & UByte.MAX_VALUE) << 24) + ((b4 & UByte.MAX_VALUE) << 16) + ((b3 & UByte.MAX_VALUE) << 8);
        }
        if (this.debug) {
            debugNumber(str, i4, 4);
        }
        return i4;
    }

    /* access modifiers changed from: protected */
    public final int[] convertByteArrayToIntArray(String str, byte[] bArr, int i, int i2, int i3) {
        int i4 = (i2 * 4) + i;
        if (bArr.length < i4) {
            PrintStream printStream = System.out;
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(str);
            stringBuffer.append(": expected length: ");
            stringBuffer.append(i4);
            stringBuffer.append(", actual length: ");
            stringBuffer.append(bArr.length);
            printStream.println(stringBuffer.toString());
            return null;
        }
        int[] iArr = new int[i2];
        for (int i5 = 0; i5 < i2; i5++) {
            iArr[i5] = convertByteArrayToInt(str, bArr, i + (i5 * 4), 4, i3);
        }
        return iArr;
    }

    /* access modifiers changed from: protected */
    public final int convertByteArrayToShort(String str, byte[] bArr, int i) {
        return convertByteArrayToShort(str, 0, bArr, i);
    }

    /* access modifiers changed from: protected */
    public final int convertByteArrayToShort(String str, int i, byte[] bArr, int i2) {
        int i3;
        byte b = bArr[i + 0];
        byte b2 = bArr[i + 1];
        if (i2 == 77) {
            i3 = ((b & UByte.MAX_VALUE) << 8) + ((b2 & UByte.MAX_VALUE) << 0);
        } else {
            i3 = ((b & UByte.MAX_VALUE) << 0) + ((b2 & UByte.MAX_VALUE) << 8);
        }
        if (this.debug) {
            debugNumber(str, i3, 2);
        }
        return i3;
    }

    /* access modifiers changed from: protected */
    public final int[] convertByteArrayToShortArray(String str, byte[] bArr, int i, int i2, int i3) {
        int i4 = (i2 * 2) + i;
        if (bArr.length < i4) {
            PrintStream printStream = System.out;
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(str);
            stringBuffer.append(": expected length: ");
            stringBuffer.append(i4);
            stringBuffer.append(", actual length: ");
            stringBuffer.append(bArr.length);
            printStream.println(stringBuffer.toString());
            return null;
        }
        int[] iArr = new int[i2];
        for (int i5 = 0; i5 < i2; i5++) {
            iArr[i5] = convertByteArrayToShort(str, (i5 * 2) + i, bArr, i3);
        }
        return iArr;
    }

    public final byte[] readByteArray(String str, int i, String str2) throws ImageReadException, IOException {
        byte[] bArr = new byte[i];
        int i2 = 0;
        int i3 = 0;
        while (i3 < i) {
            int read = this.f157is.read(bArr, i3, i - i3);
            if (read >= 1) {
                i3 += read;
            } else {
                throw new IOException(str2);
            }
        }
        if (this.debug) {
            while (i2 < i && i2 < 150) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(str);
                stringBuffer.append(" (");
                stringBuffer.append(i2);
                stringBuffer.append(")");
                debugNumber(stringBuffer.toString(), bArr[i2] & UByte.MAX_VALUE);
                i2++;
            }
        }
        return bArr;
    }

    /* access modifiers changed from: protected */
    public final void debugByteArray(String str, byte[] bArr) {
        PrintStream printStream = System.out;
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(str);
        stringBuffer.append(": ");
        stringBuffer.append(bArr.length);
        printStream.println(stringBuffer.toString());
        int i = 0;
        while (i < bArr.length && i < 50) {
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2.append(str);
            stringBuffer2.append(" (");
            stringBuffer2.append(i);
            stringBuffer2.append(")");
            debugNumber(stringBuffer2.toString(), bArr[i]);
            i++;
        }
    }

    /* access modifiers changed from: protected */
    public final void debugNumberArray(String str, int[] iArr, int i) {
        PrintStream printStream = System.out;
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(str);
        stringBuffer.append(": ");
        stringBuffer.append(iArr.length);
        printStream.println(stringBuffer.toString());
        int i2 = 0;
        while (i2 < iArr.length && i2 < 50) {
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2.append(str);
            stringBuffer2.append(" (");
            stringBuffer2.append(i2);
            stringBuffer2.append(")");
            debugNumber(stringBuffer2.toString(), iArr[i2], i);
            i2++;
        }
    }

    public final byte[] readBytearray(String str, byte[] bArr, int i, int i2) {
        if (bArr.length < i + i2) {
            return null;
        }
        byte[] bArr2 = new byte[i2];
        System.arraycopy(bArr, i, bArr2, 0, i2);
        if (this.debug) {
            debugByteArray(str, bArr2);
        }
        return bArr2;
    }

    public final byte[] readByteArray(int i, String str) throws ImageReadException, IOException {
        return readByteArray(i, str, false, true);
    }

    public final byte[] readByteArray(int i, String str, boolean z, boolean z2) throws ImageReadException, IOException {
        byte[] bArr = new byte[i];
        int i2 = 0;
        while (true) {
            int read = read(bArr, i2, i - i2);
            if (read <= 0) {
                break;
            }
            i2 += read;
        }
        if (i2 >= i) {
            return bArr;
        }
        if (!z2) {
            if (z) {
                System.out.println(str);
            }
            return null;
        }
        throw new ImageReadException(str);
    }

    /* access modifiers changed from: protected */
    public final byte[] getBytearrayTail(String str, byte[] bArr, int i) {
        return readBytearray(str, bArr, i, bArr.length - i);
    }

    /* access modifiers changed from: protected */
    public final byte[] getBytearrayHead(String str, byte[] bArr, int i) {
        return readBytearray(str, bArr, 0, bArr.length - i);
    }

    public final boolean compareByteArrays(byte[] bArr, int i, byte[] bArr2, int i2, int i3) {
        if (bArr.length < i + i3 || bArr2.length < i2 + i3) {
            return false;
        }
        for (int i4 = 0; i4 < i3; i4++) {
            int i5 = i + i4;
            int i6 = i2 + i4;
            if (bArr[i5] != bArr2[i6]) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("a[");
                stringBuffer.append(i5);
                stringBuffer.append("]");
                debugNumber(stringBuffer.toString(), bArr[i5]);
                StringBuffer stringBuffer2 = new StringBuffer();
                stringBuffer2.append("b[");
                stringBuffer2.append(i6);
                stringBuffer2.append("]");
                debugNumber(stringBuffer2.toString(), bArr2[i6]);
                return false;
            }
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public final int read4Bytes(String str, String str2, int i) throws ImageReadException, IOException {
        byte[] bArr = new byte[4];
        int i2 = 0;
        while (i2 < 4) {
            int read = this.f157is.read(bArr, i2, 4 - i2);
            if (read >= 1) {
                i2 += read;
            } else {
                throw new IOException(str2);
            }
        }
        return convertByteArrayToInt(str, bArr, i);
    }

    /* access modifiers changed from: protected */
    public final int read3Bytes(String str, String str2, int i) throws ImageReadException, IOException {
        byte[] bArr = new byte[3];
        int i2 = 0;
        while (i2 < 3) {
            int read = this.f157is.read(bArr, i2, 3 - i2);
            if (read >= 1) {
                i2 += read;
            } else {
                throw new IOException(str2);
            }
        }
        return convertByteArrayToInt(str, bArr, 0, 3, i);
    }

    /* access modifiers changed from: protected */
    public final int read2Bytes(String str, String str2, int i) throws ImageReadException, IOException {
        byte[] bArr = new byte[2];
        int i2 = 0;
        while (i2 < 2) {
            int read = this.f157is.read(bArr, i2, 2 - i2);
            if (read >= 1) {
                i2 += read;
            } else {
                throw new IOException(str2);
            }
        }
        return convertByteArrayToShort(str, bArr, i);
    }

    public final int read1ByteInteger(String str) throws ImageReadException, IOException {
        int read = this.f157is.read();
        if (read >= 0) {
            return read & 255;
        }
        throw new ImageReadException(str);
    }

    public final int read2ByteInteger(String str) throws ImageReadException, IOException {
        int read = this.f157is.read();
        int read2 = this.f157is.read();
        if (read >= 0 && read2 >= 0) {
            return this.byteOrder == 77 ? ((read & 255) << 8) + ((read2 & 255) << 0) : ((read2 & 255) << 8) + ((read & 255) << 0);
        }
        throw new ImageReadException(str);
    }

    public final int read4ByteInteger(String str) throws ImageReadException, IOException {
        int read = this.f157is.read();
        int read2 = this.f157is.read();
        int read3 = this.f157is.read();
        int read4 = this.f157is.read();
        if (read >= 0 && read2 >= 0 && read3 >= 0 && read4 >= 0) {
            return this.byteOrder == 77 ? ((read & 255) << 24) + ((read2 & 255) << 16) + ((read3 & 255) << 8) + ((read4 & 255) << 0) : ((read4 & 255) << 24) + ((read3 & 255) << 16) + ((read2 & 255) << 8) + ((read & 255) << 0);
        }
        throw new ImageReadException(str);
    }

    /* access modifiers changed from: protected */
    public final void printCharQuad(String str, int i) {
        PrintStream printStream = System.out;
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(str);
        stringBuffer.append(": '");
        stringBuffer.append((char) ((i >> 24) & 255));
        stringBuffer.append((char) ((i >> 16) & 255));
        stringBuffer.append((char) ((i >> 8) & 255));
        stringBuffer.append((char) ((i >> 0) & 255));
        stringBuffer.append("'");
        printStream.println(stringBuffer.toString());
    }

    /* access modifiers changed from: protected */
    public final void printByteBits(String str, byte b) {
        PrintStream printStream = System.out;
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(str);
        stringBuffer.append(": '");
        stringBuffer.append(Integer.toBinaryString(b & UByte.MAX_VALUE));
        printStream.println(stringBuffer.toString());
    }

    public final int findNull(byte[] bArr) {
        return findNull(bArr, 0);
    }

    public final int findNull(byte[] bArr, int i) {
        while (i < bArr.length) {
            if (bArr[i] == 0) {
                return i;
            }
            i++;
        }
        return -1;
    }

    /* access modifiers changed from: protected */
    public final byte[] getRAFBytes(RandomAccessFile randomAccessFile, long j, int i, String str) throws IOException {
        byte[] bArr = new byte[i];
        if (this.debug) {
            PrintStream printStream = System.out;
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("getRAFBytes pos: ");
            stringBuffer.append(j);
            printStream.println(stringBuffer.toString());
            PrintStream printStream2 = System.out;
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2.append("getRAFBytes length: ");
            stringBuffer2.append(i);
            printStream2.println(stringBuffer2.toString());
        }
        randomAccessFile.seek(j);
        int i2 = 0;
        while (i2 < i) {
            int read = randomAccessFile.read(bArr, i2, i - i2);
            if (read >= 1) {
                i2 += read;
            } else {
                throw new IOException(str);
            }
        }
        return bArr;
    }

    /* access modifiers changed from: protected */
    public void skipBytes(int i) throws IOException {
        skipBytes(i, "Couldn't skip bytes");
    }
}

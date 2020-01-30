package org.apache.sanselan.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import kotlin.UByte;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.formats.pnm.PNMConstants;

public class BinaryFileFunctions implements BinaryConstants {
    protected boolean debug = false;

    public static final int CharsToQuad(char c, char c2, char c3, char c4) {
        return ((c & 255) << 24) | ((c2 & 255) << 16) | ((c3 & 255) << 8) | ((c4 & 255) << 0);
    }

    public final void setDebug(boolean z) {
        this.debug = z;
    }

    public final boolean getDebug() {
        return this.debug;
    }

    /* access modifiers changed from: protected */
    public final void readRandomBytes(InputStream inputStream) throws ImageReadException, IOException {
        for (int i = 0; i < 100; i++) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("");
            stringBuffer.append(i);
            readByte(stringBuffer.toString(), inputStream, "Random Data");
        }
    }

    public final void debugNumber(String str, int i) {
        debugNumber(str, i, 1);
    }

    public final void debugNumber(String str, int i, int i2) {
        PrintWriter printWriter = new PrintWriter(System.out);
        debugNumber(printWriter, str, i, i2);
        printWriter.flush();
    }

    public final void debugNumber(PrintWriter printWriter, String str, int i) {
        debugNumber(printWriter, str, i, 1);
    }

    public final void debugNumber(PrintWriter printWriter, String str, int i, int i2) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(str);
        stringBuffer.append(": ");
        stringBuffer.append(i);
        stringBuffer.append(" (");
        printWriter.print(stringBuffer.toString());
        int i3 = i;
        for (int i4 = 0; i4 < i2; i4++) {
            if (i4 > 0) {
                printWriter.print(",");
            }
            int i5 = i3 & 255;
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2.append((char) i5);
            stringBuffer2.append(" [");
            stringBuffer2.append(i5);
            stringBuffer2.append("]");
            printWriter.print(stringBuffer2.toString());
            i3 >>= 8;
        }
        StringBuffer stringBuffer3 = new StringBuffer();
        stringBuffer3.append(") [0x");
        stringBuffer3.append(Integer.toHexString(i));
        stringBuffer3.append(", ");
        stringBuffer3.append(Integer.toBinaryString(i));
        stringBuffer3.append("]");
        printWriter.println(stringBuffer3.toString());
        printWriter.flush();
    }

    public final boolean startsWith(byte[] bArr, byte[] bArr2) {
        if (bArr2 == null || bArr == null || bArr2.length > bArr.length) {
            return false;
        }
        for (int i = 0; i < bArr2.length; i++) {
            if (bArr2[i] != bArr[i]) {
                return false;
            }
        }
        return true;
    }

    public final byte[] readBytes(InputStream inputStream, int i) throws ImageReadException, IOException {
        byte[] bArr = new byte[i];
        for (int i2 = 0; i2 < i; i2++) {
            bArr[i2] = (byte) inputStream.read();
        }
        return bArr;
    }

    public final void readAndVerifyBytes(InputStream inputStream, byte[] bArr, String str) throws ImageReadException, IOException {
        int i = 0;
        while (i < bArr.length) {
            int read = inputStream.read();
            byte b = (byte) (read & 255);
            if (read < 0) {
                throw new ImageReadException("Unexpected EOF.");
            } else if (b == bArr[i]) {
                i++;
            } else {
                throw new ImageReadException(str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public final void readAndVerifyBytes(String str, InputStream inputStream, byte[] bArr, String str2) throws ImageReadException, IOException {
        byte[] readByteArray = readByteArray(str, bArr.length, inputStream, str2);
        int i = 0;
        while (i < bArr.length) {
            if (readByteArray[i] == bArr[i]) {
                i++;
            } else {
                throw new ImageReadException(str2);
            }
        }
    }

    public final void skipBytes(InputStream inputStream, int i, String str) throws IOException {
        long j = 0;
        while (true) {
            long j2 = (long) i;
            if (j2 != j) {
                long skip = inputStream.skip(j2 - j);
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
    public final void scanForByte(InputStream inputStream, byte b) throws IOException {
        int i = 0;
        int i2 = 0;
        while (i < 3) {
            int read = inputStream.read();
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

    public final byte readByte(String str, InputStream inputStream, String str2) throws ImageReadException, IOException {
        int read = inputStream.read();
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
        return new RationalNumber(convertByteArrayToInt(str, bArr, i + 0, i2), convertByteArrayToInt(str, bArr, i + 4, i2));
    }

    /* access modifiers changed from: protected */
    public final int convertByteArrayToInt(String str, byte[] bArr, int i) {
        return convertByteArrayToInt(str, bArr, 0, i);
    }

    /* access modifiers changed from: protected */
    public final int convertByteArrayToInt(String str, byte[] bArr, int i, int i2) {
        int i3;
        byte b = bArr[i + 0];
        byte b2 = bArr[i + 1];
        byte b3 = bArr[i + 2];
        byte b4 = bArr[i + 3];
        if (i2 == 77) {
            i3 = ((b4 & UByte.MAX_VALUE) << 0) | ((b & UByte.MAX_VALUE) << 24) | ((b2 & UByte.MAX_VALUE) << 16) | ((b3 & UByte.MAX_VALUE) << 8);
        } else {
            i3 = ((b4 & UByte.MAX_VALUE) << 24) | ((b3 & UByte.MAX_VALUE) << 16) | ((b2 & UByte.MAX_VALUE) << 8) | ((b & UByte.MAX_VALUE) << 0);
        }
        if (this.debug) {
            debugNumber(str, i3, 4);
        }
        return i3;
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
            iArr[i5] = convertByteArrayToInt(str, bArr, (i5 * 4) + i, i3);
        }
        return iArr;
    }

    /* access modifiers changed from: protected */
    public final void writeIntInToByteArray(int i, byte[] bArr, int i2, int i3) {
        if (i3 == 77) {
            bArr[i2 + 0] = (byte) (i >> 24);
            bArr[i2 + 1] = (byte) (i >> 16);
            bArr[i2 + 2] = (byte) (i >> 8);
            bArr[i2 + 3] = (byte) (i >> 0);
            return;
        }
        bArr[i2 + 3] = (byte) (i >> 24);
        bArr[i2 + 2] = (byte) (i >> 16);
        bArr[i2 + 1] = (byte) (i >> 8);
        bArr[i2 + 0] = (byte) (i >> 0);
    }

    protected static final byte[] int2ToByteArray(int i, int i2) {
        if (i2 == 77) {
            return new byte[]{(byte) (i >> 8), (byte) (i >> 0)};
        }
        return new byte[]{(byte) (i >> 0), (byte) (i >> 8)};
    }

    /* access modifiers changed from: protected */
    public final byte[] convertIntArrayToByteArray(int[] iArr, int i) {
        byte[] bArr = new byte[(iArr.length * 4)];
        for (int i2 = 0; i2 < iArr.length; i2++) {
            writeIntInToByteArray(iArr[i2], bArr, i2 * 4, i);
        }
        return bArr;
    }

    /* access modifiers changed from: protected */
    public final byte[] convertShortArrayToByteArray(int[] iArr, int i) {
        byte[] bArr = new byte[(iArr.length * 2)];
        for (int i2 = 0; i2 < iArr.length; i2++) {
            int i3 = iArr[i2];
            if (i == 77) {
                int i4 = i2 * 2;
                bArr[i4 + 0] = (byte) (i3 >> 8);
                bArr[i4 + 1] = (byte) (i3 >> 0);
            } else {
                int i5 = i2 * 2;
                bArr[i5 + 1] = (byte) (i3 >> 8);
                bArr[i5 + 0] = (byte) (i3 >> 0);
            }
        }
        return bArr;
    }

    /* access modifiers changed from: protected */
    public final byte[] convertShortToByteArray(int i, int i2) {
        byte[] bArr = new byte[2];
        if (i2 == 77) {
            bArr[0] = (byte) (i >> 8);
            bArr[1] = (byte) (i >> 0);
        } else {
            bArr[1] = (byte) (i >> 8);
            bArr[0] = (byte) (i >> 0);
        }
        return bArr;
    }

    /* access modifiers changed from: protected */
    public final byte[] convertIntArrayToRationalArray(int[] iArr, int[] iArr2, int i) throws ImageWriteException {
        if (iArr.length == iArr2.length) {
            byte[] bArr = new byte[(iArr.length * 8)];
            for (int i2 = 0; i2 < iArr.length; i2++) {
                int i3 = i2 * 8;
                writeIntInToByteArray(iArr[i2], bArr, i3, i);
                writeIntInToByteArray(iArr2[i2], bArr, i3 + 4, i);
            }
            return bArr;
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("numerators.length (");
        stringBuffer.append(iArr.length);
        stringBuffer.append(" != denominators.length (");
        stringBuffer.append(iArr2.length);
        stringBuffer.append(")");
        throw new ImageWriteException(stringBuffer.toString());
    }

    /* access modifiers changed from: protected */
    public final byte[] convertRationalArrayToByteArray(RationalNumber[] rationalNumberArr, int i) throws ImageWriteException {
        byte[] bArr = new byte[(rationalNumberArr.length * 8)];
        for (int i2 = 0; i2 < rationalNumberArr.length; i2++) {
            int i3 = i2 * 8;
            writeIntInToByteArray(rationalNumberArr[i2].numerator, bArr, i3, i);
            writeIntInToByteArray(rationalNumberArr[i2].divisor, bArr, i3 + 4, i);
        }
        return bArr;
    }

    /* access modifiers changed from: protected */
    public final byte[] convertRationalToByteArray(RationalNumber rationalNumber, int i) throws ImageWriteException {
        byte[] bArr = new byte[8];
        writeIntInToByteArray(rationalNumber.numerator, bArr, 0, i);
        writeIntInToByteArray(rationalNumber.divisor, bArr, 4, i);
        return bArr;
    }

    /* access modifiers changed from: protected */
    public final int convertByteArrayToShort(String str, byte[] bArr, int i) throws ImageReadException {
        return convertByteArrayToShort(str, 0, bArr, i);
    }

    /* access modifiers changed from: protected */
    public final int convertByteArrayToShort(String str, int i, byte[] bArr, int i2) throws ImageReadException {
        int i3 = i + 1;
        if (i3 < bArr.length) {
            byte b = bArr[i + 0] & UByte.MAX_VALUE;
            byte b2 = bArr[i3] & UByte.MAX_VALUE;
            byte b3 = i2 == 77 ? (b << 8) | b2 : b | (b2 << 8);
            if (this.debug) {
                debugNumber(str, (int) b3, 2);
            }
            return b3;
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Index out of bounds. Array size: ");
        stringBuffer.append(bArr.length);
        stringBuffer.append(", index: ");
        stringBuffer.append(i);
        throw new ImageReadException(stringBuffer.toString());
    }

    /* access modifiers changed from: protected */
    public final int[] convertByteArrayToShortArray(String str, byte[] bArr, int i, int i2, int i3) throws ImageReadException {
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

    public final byte[] readByteArray(String str, int i, InputStream inputStream) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(str);
        stringBuffer.append(" could not be read.");
        return readByteArray(str, i, inputStream, stringBuffer.toString());
    }

    public final byte[] readByteArray(String str, int i, InputStream inputStream, String str2) throws IOException {
        byte[] bArr = new byte[i];
        int i2 = 0;
        int i3 = 0;
        while (i3 < i) {
            int read = inputStream.read(bArr, i3, i - i3);
            if (read >= 1) {
                i3 += read;
            } else {
                throw new IOException(str2);
            }
        }
        if (this.debug) {
            while (i2 < i && i2 < 50) {
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
            stringBuffer2.append("\t (");
            stringBuffer2.append(i);
            stringBuffer2.append(")");
            debugNumber(stringBuffer2.toString(), bArr[i] & UByte.MAX_VALUE);
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

    public final byte[] readBytearray(String str, byte[] bArr, int i, int i2) throws ImageReadException {
        if (bArr.length >= i + i2) {
            byte[] bArr2 = new byte[i2];
            System.arraycopy(bArr, i, bArr2, 0, i2);
            if (this.debug) {
                debugByteArray(str, bArr2);
            }
            return bArr2;
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Invalid read. bytes.length: ");
        stringBuffer.append(bArr.length);
        stringBuffer.append(", start: ");
        stringBuffer.append(i);
        stringBuffer.append(", count: ");
        stringBuffer.append(i2);
        throw new ImageReadException(stringBuffer.toString());
    }

    /* access modifiers changed from: protected */
    public final byte[] getByteArrayTail(String str, byte[] bArr, int i) throws ImageReadException {
        return readBytearray(str, bArr, i, bArr.length - i);
    }

    /* access modifiers changed from: protected */
    public final byte[] getBytearrayHead(String str, byte[] bArr, int i) throws ImageReadException {
        return readBytearray(str, bArr, 0, bArr.length - i);
    }

    public static final byte[] slice(byte[] bArr, int i, int i2) {
        if (bArr.length < i + i2) {
            return null;
        }
        byte[] bArr2 = new byte[i2];
        System.arraycopy(bArr, i, bArr2, 0, i2);
        return bArr2;
    }

    public static final byte[] tail(byte[] bArr, int i) {
        if (i > bArr.length) {
            i = bArr.length;
        }
        return slice(bArr, bArr.length - i, i);
    }

    public static final byte[] head(byte[] bArr, int i) {
        if (i > bArr.length) {
            i = bArr.length;
        }
        return slice(bArr, 0, i);
    }

    public final boolean compareByteArrays(byte[] bArr, byte[] bArr2) {
        if (bArr.length != bArr2.length) {
            return false;
        }
        return compareByteArrays(bArr, 0, bArr2, 0, bArr.length);
    }

    public final boolean compareByteArrays(byte[] bArr, int i, byte[] bArr2, int i2, int i3) {
        if (bArr.length < i + i3 || bArr2.length < i2 + i3) {
            return false;
        }
        for (int i4 = 0; i4 < i3; i4++) {
            if (bArr[i + i4] != bArr2[i2 + i4]) {
                return false;
            }
        }
        return true;
    }

    public static final boolean compareBytes(byte[] bArr, byte[] bArr2) {
        if (bArr.length != bArr2.length) {
            return false;
        }
        return compareBytes(bArr, 0, bArr2, 0, bArr.length);
    }

    public static final boolean compareBytes(byte[] bArr, int i, byte[] bArr2, int i2, int i3) {
        if (bArr.length < i + i3 || bArr2.length < i2 + i3) {
            return false;
        }
        for (int i4 = 0; i4 < i3; i4++) {
            if (bArr[i + i4] != bArr2[i2 + i4]) {
                return false;
            }
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public final int read4Bytes(String str, InputStream inputStream, String str2, int i) throws ImageReadException, IOException {
        byte[] bArr = new byte[4];
        int i2 = 0;
        while (i2 < 4) {
            int read = inputStream.read(bArr, i2, 4 - i2);
            if (read >= 1) {
                i2 += read;
            } else {
                throw new IOException(str2);
            }
        }
        return convertByteArrayToInt(str, bArr, i);
    }

    /* access modifiers changed from: protected */
    public final int read3Bytes(String str, InputStream inputStream, String str2, int i) throws ImageReadException, IOException {
        int i2;
        byte read = (byte) inputStream.read();
        byte read2 = (byte) inputStream.read();
        byte read3 = (byte) inputStream.read();
        if (i == 77) {
            i2 = ((read3 & UByte.MAX_VALUE) << 0) | ((read & UByte.MAX_VALUE) << 16) | ((read2 & UByte.MAX_VALUE) << 8);
        } else {
            i2 = ((read3 & UByte.MAX_VALUE) << 16) | ((read2 & UByte.MAX_VALUE) << 8) | ((read & UByte.MAX_VALUE) << 0);
        }
        if (this.debug) {
            debugNumber(str, i2, 3);
        }
        return i2;
    }

    /* access modifiers changed from: protected */
    public final int read2Bytes(String str, InputStream inputStream, String str2, int i) throws ImageReadException, IOException {
        byte[] bArr = new byte[2];
        int i2 = 0;
        while (i2 < 2) {
            int read = inputStream.read(bArr, i2, 2 - i2);
            if (read >= 1) {
                i2 += read;
            } else {
                throw new IOException(str2);
            }
        }
        return convertByteArrayToShort(str, bArr, i);
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
    public final void printCharQuad(PrintWriter printWriter, String str, int i) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(str);
        stringBuffer.append(": '");
        stringBuffer.append((char) ((i >> 24) & 255));
        stringBuffer.append((char) ((i >> 16) & 255));
        stringBuffer.append((char) ((i >> 8) & 255));
        stringBuffer.append((char) ((i >> 0) & 255));
        stringBuffer.append("'");
        printWriter.println(stringBuffer.toString());
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
        byte[] bArr = new byte[i];
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
    public final float convertByteArrayToFloat(String str, byte[] bArr, int i) {
        return convertByteArrayToFloat(str, bArr, 0, i);
    }

    /* access modifiers changed from: protected */
    public final float convertByteArrayToFloat(String str, byte[] bArr, int i, int i2) {
        int i3;
        byte b = bArr[i + 0];
        byte b2 = bArr[i + 1];
        byte b3 = bArr[i + 2];
        byte b4 = bArr[i + 3];
        if (i2 == 77) {
            i3 = ((b & UByte.MAX_VALUE) << 24) | ((b2 & UByte.MAX_VALUE) << 16) | ((b3 & UByte.MAX_VALUE) << 8) | ((b4 & UByte.MAX_VALUE) << 0);
        } else {
            i3 = ((b & UByte.MAX_VALUE) << 0) | ((b4 & UByte.MAX_VALUE) << 24) | ((b3 & UByte.MAX_VALUE) << 16) | ((b2 & UByte.MAX_VALUE) << 8);
        }
        return Float.intBitsToFloat(i3);
    }

    /* access modifiers changed from: protected */
    public final float[] convertByteArrayToFloatArray(String str, byte[] bArr, int i, int i2, int i3) {
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
        float[] fArr = new float[i2];
        for (int i5 = 0; i5 < i2; i5++) {
            fArr[i5] = convertByteArrayToFloat(str, bArr, (i5 * 4) + i, i3);
        }
        return fArr;
    }

    /* access modifiers changed from: protected */
    public final byte[] convertFloatToByteArray(float f, int i) {
        byte[] bArr = new byte[4];
        int floatToRawIntBits = Float.floatToRawIntBits(f);
        if (i == 77) {
            bArr[0] = (byte) ((floatToRawIntBits >> 0) & 255);
            bArr[1] = (byte) ((floatToRawIntBits >> 8) & 255);
            bArr[2] = (byte) ((floatToRawIntBits >> 16) & 255);
            bArr[3] = (byte) ((floatToRawIntBits >> 24) & 255);
        } else {
            bArr[3] = (byte) ((floatToRawIntBits >> 0) & 255);
            bArr[2] = (byte) ((floatToRawIntBits >> 8) & 255);
            bArr[1] = (byte) ((floatToRawIntBits >> 16) & 255);
            bArr[0] = (byte) ((floatToRawIntBits >> 24) & 255);
        }
        return bArr;
    }

    /* access modifiers changed from: protected */
    public final byte[] convertFloatArrayToByteArray(float[] fArr, int i) {
        byte[] bArr = new byte[(fArr.length * 4)];
        for (int i2 = 0; i2 < fArr.length; i2++) {
            int floatToRawIntBits = Float.floatToRawIntBits(fArr[i2]);
            int i3 = i2 * 4;
            if (i == 77) {
                bArr[i3 + 0] = (byte) ((floatToRawIntBits >> 0) & 255);
                bArr[i3 + 1] = (byte) ((floatToRawIntBits >> 8) & 255);
                bArr[i3 + 2] = (byte) ((floatToRawIntBits >> 16) & 255);
                bArr[i3 + 3] = (byte) ((floatToRawIntBits >> 24) & 255);
            } else {
                bArr[i3 + 3] = (byte) ((floatToRawIntBits >> 0) & 255);
                bArr[i3 + 2] = (byte) ((floatToRawIntBits >> 8) & 255);
                bArr[i3 + 1] = (byte) ((floatToRawIntBits >> 16) & 255);
                bArr[i3 + 0] = (byte) ((floatToRawIntBits >> 24) & 255);
            }
        }
        return bArr;
    }

    /* access modifiers changed from: protected */
    public final byte[] convertDoubleToByteArray(double d, int i) {
        byte[] bArr = new byte[8];
        long doubleToRawLongBits = Double.doubleToRawLongBits(d);
        if (i == 77) {
            bArr[0] = (byte) ((int) ((doubleToRawLongBits >> 0) & 255));
            bArr[1] = (byte) ((int) ((doubleToRawLongBits >> 8) & 255));
            bArr[2] = (byte) ((int) ((doubleToRawLongBits >> 16) & 255));
            bArr[3] = (byte) ((int) ((doubleToRawLongBits >> 24) & 255));
            bArr[4] = (byte) ((int) ((doubleToRawLongBits >> 32) & 255));
            bArr[5] = (byte) ((int) ((doubleToRawLongBits >> 40) & 255));
            bArr[6] = (byte) ((int) ((doubleToRawLongBits >> 48) & 255));
            bArr[7] = (byte) ((int) ((doubleToRawLongBits >> 56) & 255));
        } else {
            bArr[7] = (byte) ((int) ((doubleToRawLongBits >> 0) & 255));
            bArr[6] = (byte) ((int) ((doubleToRawLongBits >> 8) & 255));
            bArr[5] = (byte) ((int) ((doubleToRawLongBits >> 16) & 255));
            bArr[4] = (byte) ((int) ((doubleToRawLongBits >> 24) & 255));
            bArr[3] = (byte) ((int) ((doubleToRawLongBits >> 32) & 255));
            bArr[2] = (byte) ((int) ((doubleToRawLongBits >> 40) & 255));
            bArr[1] = (byte) ((int) ((doubleToRawLongBits >> 48) & 255));
            bArr[0] = (byte) ((int) ((doubleToRawLongBits >> 56) & 255));
        }
        return bArr;
    }

    /* access modifiers changed from: protected */
    public final byte[] convertDoubleArrayToByteArray(double[] dArr, int i) {
        int i2;
        double[] dArr2 = dArr;
        byte[] bArr = new byte[(dArr2.length * 8)];
        boolean z = false;
        int i3 = 0;
        while (i3 < dArr2.length) {
            long doubleToRawLongBits = Double.doubleToRawLongBits(dArr2[i3]);
            int i4 = i3 * 8;
            if (i == 77) {
                i2 = i3;
                bArr[i4 + 0] = (byte) ((int) ((doubleToRawLongBits >> (z ? 1 : 0)) & 255));
                bArr[i4 + 1] = (byte) ((int) ((doubleToRawLongBits >> 8) & 255));
                bArr[i4 + 2] = (byte) ((int) ((doubleToRawLongBits >> 16) & 255));
                bArr[i4 + 3] = (byte) ((int) ((doubleToRawLongBits >> 24) & 255));
                bArr[i4 + 4] = (byte) ((int) ((doubleToRawLongBits >> 32) & 255));
                bArr[i4 + 5] = (byte) ((int) ((doubleToRawLongBits >> 40) & 255));
                bArr[i4 + 6] = (byte) ((int) ((doubleToRawLongBits >> 48) & 255));
                bArr[i4 + 7] = (byte) ((int) ((doubleToRawLongBits >> 56) & 255));
            } else {
                i2 = i3;
                bArr[i4 + 7] = (byte) ((int) ((doubleToRawLongBits >> 0) & 255));
                bArr[i4 + 6] = (byte) ((int) ((doubleToRawLongBits >> 8) & 255));
                bArr[i4 + 5] = (byte) ((int) ((doubleToRawLongBits >> 16) & 255));
                bArr[i4 + 4] = (byte) ((int) ((doubleToRawLongBits >> 24) & 255));
                bArr[i4 + 3] = (byte) ((int) ((doubleToRawLongBits >> 32) & 255));
                bArr[i4 + 2] = (byte) ((int) ((doubleToRawLongBits >> 40) & 255));
                bArr[i4 + 1] = (byte) ((int) ((doubleToRawLongBits >> 48) & 255));
                bArr[i4 + 0] = (byte) ((int) ((doubleToRawLongBits >> 56) & 255));
            }
            i3 = i2 + 1;
            z = false;
        }
        return bArr;
    }

    /* access modifiers changed from: protected */
    public final double convertByteArrayToDouble(String str, byte[] bArr, int i) {
        return convertByteArrayToDouble(str, bArr, 0, i);
    }

    /* access modifiers changed from: protected */
    public final double convertByteArrayToDouble(String str, byte[] bArr, int i, int i2) {
        long j;
        byte b = bArr[i + 0];
        byte b2 = bArr[i + 1];
        byte b3 = bArr[i + 2];
        byte b4 = bArr[i + 3];
        byte b5 = bArr[i + 4];
        byte b6 = bArr[i + 5];
        byte b7 = bArr[i + 6];
        byte b8 = bArr[i + 7];
        if (i2 == 77) {
            j = (long) (((b & UByte.MAX_VALUE) << 56) | ((b2 & UByte.MAX_VALUE) << 48) | ((b3 & UByte.MAX_VALUE) << 40) | ((b4 & UByte.MAX_VALUE) << PNMConstants.PNM_SEPARATOR) | ((b5 & UByte.MAX_VALUE) << 24) | ((b6 & UByte.MAX_VALUE) << 16) | ((b7 & UByte.MAX_VALUE) << 8) | ((b8 & UByte.MAX_VALUE) << 0));
        } else {
            j = (long) (((b & UByte.MAX_VALUE) << 0) | ((b8 & UByte.MAX_VALUE) << 56) | ((b7 & UByte.MAX_VALUE) << 48) | ((b6 & UByte.MAX_VALUE) << 40) | ((b5 & UByte.MAX_VALUE) << PNMConstants.PNM_SEPARATOR) | ((b4 & UByte.MAX_VALUE) << 24) | ((b3 & UByte.MAX_VALUE) << 16) | ((b2 & UByte.MAX_VALUE) << 8));
        }
        return Double.longBitsToDouble(j);
    }

    /* access modifiers changed from: protected */
    public final double[] convertByteArrayToDoubleArray(String str, byte[] bArr, int i, int i2, int i3) {
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
        double[] dArr = new double[i2];
        for (int i5 = 0; i5 < i2; i5++) {
            dArr[i5] = convertByteArrayToDouble(str, bArr, (i5 * 8) + i, i3);
        }
        return dArr;
    }

    /* access modifiers changed from: protected */
    public void skipBytes(InputStream inputStream, int i) throws IOException {
        skipBytes(inputStream, i, "Couldn't skip bytes");
    }

    public final void copyStreamToStream(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] bArr = new byte[1024];
        while (true) {
            int read = inputStream.read(bArr);
            if (read > 0) {
                outputStream.write(bArr, 0, read);
            } else {
                return;
            }
        }
    }

    public final byte[] getStreamBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        copyStreamToStream(inputStream, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}

package org.apache.sanselan.common.mylzw;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class MyLZWDecompressor {
    private static final int MAX_TABLE_SIZE = 4096;
    private final int byteOrder;
    private final int clearCode;
    private int codeSize;
    private int codes;
    private final int eoiCode;
    private final int initialCodeSize;
    private final Listener listener;
    private final byte[][] table;
    private boolean tiffLZWMode;
    private int written;

    public interface Listener {
        void code(int i);

        void init(int i, int i2);
    }

    public MyLZWDecompressor(int i, int i2) {
        this(i, i2, null);
    }

    public MyLZWDecompressor(int i, int i2, Listener listener2) {
        this.codes = -1;
        this.written = 0;
        this.tiffLZWMode = false;
        this.listener = listener2;
        this.byteOrder = i2;
        this.initialCodeSize = i;
        this.table = new byte[4096][];
        this.clearCode = 1 << i;
        int i3 = this.clearCode;
        this.eoiCode = i3 + 1;
        if (listener2 != null) {
            listener2.init(i3, this.eoiCode);
        }
        InitializeTable();
    }

    private final void InitializeTable() {
        this.codeSize = this.initialCodeSize;
        int i = 1 << (this.codeSize + 2);
        for (int i2 = 0; i2 < i; i2++) {
            this.table[i2] = new byte[]{(byte) i2};
        }
    }

    private final void clearTable() {
        int i = this.initialCodeSize;
        this.codes = (1 << i) + 2;
        this.codeSize = i;
        incrementCodeSize();
    }

    private final int getNextCode(MyBitInputStream myBitInputStream) throws IOException {
        int readBits = myBitInputStream.readBits(this.codeSize);
        Listener listener2 = this.listener;
        if (listener2 != null) {
            listener2.code(readBits);
        }
        return readBits;
    }

    private final byte[] stringFromCode(int i) throws IOException {
        if (i < this.codes && i >= 0) {
            return this.table[i];
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Bad Code: ");
        stringBuffer.append(i);
        stringBuffer.append(" codes: ");
        stringBuffer.append(this.codes);
        stringBuffer.append(" code_size: ");
        stringBuffer.append(this.codeSize);
        stringBuffer.append(", table: ");
        stringBuffer.append(this.table.length);
        throw new IOException(stringBuffer.toString());
    }

    private final boolean isInTable(int i) {
        return i < this.codes;
    }

    private final byte firstChar(byte[] bArr) {
        return bArr[0];
    }

    private final void addStringToTable(byte[] bArr) throws IOException {
        int i = this.codes;
        if (i < (1 << this.codeSize)) {
            this.table[i] = bArr;
            this.codes = i + 1;
            checkCodeSize();
            return;
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("AddStringToTable: codes: ");
        stringBuffer.append(this.codes);
        stringBuffer.append(" code_size: ");
        stringBuffer.append(this.codeSize);
        throw new IOException(stringBuffer.toString());
    }

    private final byte[] appendBytes(byte[] bArr, byte b) {
        byte[] bArr2 = new byte[(bArr.length + 1)];
        System.arraycopy(bArr, 0, bArr2, 0, bArr.length);
        bArr2[bArr2.length - 1] = b;
        return bArr2;
    }

    private final void writeToResult(OutputStream outputStream, byte[] bArr) throws IOException {
        outputStream.write(bArr);
        this.written += bArr.length;
    }

    public void setTiffLZWMode() {
        this.tiffLZWMode = true;
    }

    public byte[] decompress(InputStream inputStream, int i) throws IOException {
        MyBitInputStream myBitInputStream = new MyBitInputStream(inputStream, this.byteOrder);
        if (this.tiffLZWMode) {
            myBitInputStream.setTiffLZWMode();
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(i);
        clearTable();
        int i2 = -1;
        do {
            int nextCode = getNextCode(myBitInputStream);
            if (nextCode == this.eoiCode) {
                break;
            } else if (nextCode == this.clearCode) {
                clearTable();
                if (this.written >= i) {
                    break;
                }
                i2 = getNextCode(myBitInputStream);
                if (i2 == this.eoiCode) {
                    break;
                }
                writeToResult(byteArrayOutputStream, stringFromCode(i2));
            } else {
                if (isInTable(nextCode)) {
                    writeToResult(byteArrayOutputStream, stringFromCode(nextCode));
                    addStringToTable(appendBytes(stringFromCode(i2), firstChar(stringFromCode(nextCode))));
                } else {
                    byte[] appendBytes = appendBytes(stringFromCode(i2), firstChar(stringFromCode(i2)));
                    writeToResult(byteArrayOutputStream, appendBytes);
                    addStringToTable(appendBytes);
                }
                i2 = nextCode;
            }
        } while (this.written < i);
        return byteArrayOutputStream.toByteArray();
    }

    private final void checkCodeSize() {
        int i = 1 << this.codeSize;
        if (this.tiffLZWMode) {
            i--;
        }
        if (this.codes == i) {
            incrementCodeSize();
        }
    }

    private final void incrementCodeSize() {
        int i = this.codeSize;
        if (i != 12) {
            this.codeSize = i + 1;
        }
    }
}

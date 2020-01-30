package org.apache.sanselan.common.mylzw;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import kotlin.UByte;

public class MyLZWCompressor {
    private final int byteOrder;
    private final int clearCode;
    private int codeSize;
    private int codes;
    private final boolean earlyLimit;
    private final int eoiCode;
    private final int initialCodeSize;
    private final Listener listener;
    private final Map map;

    private static final class ByteArray {
        private final byte[] bytes;
        private final int hash;
        private final int length;
        private final int start;

        public ByteArray(byte[] bArr) {
            this(bArr, 0, bArr.length);
        }

        public ByteArray(byte[] bArr, int i, int i2) {
            this.bytes = bArr;
            this.start = i;
            this.length = i2;
            byte b = i2;
            for (int i3 = 0; i3 < i2; i3++) {
                b = ((b + (b << 8)) ^ (bArr[i3 + i] & UByte.MAX_VALUE)) ^ i3;
            }
            this.hash = b;
        }

        public final int hashCode() {
            return this.hash;
        }

        public final boolean equals(Object obj) {
            ByteArray byteArray = (ByteArray) obj;
            if (byteArray.hash != this.hash || byteArray.length != this.length) {
                return false;
            }
            for (int i = 0; i < this.length; i++) {
                if (byteArray.bytes[byteArray.start + i] != this.bytes[this.start + i]) {
                    return false;
                }
            }
            return true;
        }
    }

    public interface Listener {
        void clearCode(int i);

        void dataCode(int i);

        void eoiCode(int i);

        void init(int i, int i2);
    }

    public MyLZWCompressor(int i, int i2, boolean z) {
        this(i, i2, z, null);
    }

    public MyLZWCompressor(int i, int i2, boolean z, Listener listener2) {
        this.codes = -1;
        this.map = new HashMap();
        this.listener = listener2;
        this.byteOrder = i2;
        this.earlyLimit = z;
        this.initialCodeSize = i;
        this.clearCode = 1 << i;
        int i3 = this.clearCode;
        this.eoiCode = i3 + 1;
        if (listener2 != null) {
            listener2.init(i3, this.eoiCode);
        }
        InitializeStringTable();
    }

    private final void InitializeStringTable() {
        this.codeSize = this.initialCodeSize;
        int i = (1 << this.codeSize) + 2;
        this.map.clear();
        int i2 = 0;
        while (true) {
            this.codes = i2;
            int i3 = this.codes;
            if (i3 < i) {
                if (!(i3 == this.clearCode || i3 == this.eoiCode)) {
                    this.map.put(arrayToKey((byte) i3), new Integer(this.codes));
                }
                i2 = this.codes + 1;
            } else {
                return;
            }
        }
    }

    private final void clearTable() {
        InitializeStringTable();
        incrementCodeSize();
    }

    private final void incrementCodeSize() {
        int i = this.codeSize;
        if (i != 12) {
            this.codeSize = i + 1;
        }
    }

    private final Object arrayToKey(byte b) {
        return arrayToKey(new byte[]{b}, 0, 1);
    }

    private final Object arrayToKey(byte[] bArr, int i, int i2) {
        return new ByteArray(bArr, i, i2);
    }

    private final void writeDataCode(MyBitOutputStream myBitOutputStream, int i) throws IOException {
        Listener listener2 = this.listener;
        if (listener2 != null) {
            listener2.dataCode(i);
        }
        writeCode(myBitOutputStream, i);
    }

    private final void writeClearCode(MyBitOutputStream myBitOutputStream) throws IOException {
        Listener listener2 = this.listener;
        if (listener2 != null) {
            listener2.dataCode(this.clearCode);
        }
        writeCode(myBitOutputStream, this.clearCode);
    }

    private final void writeEoiCode(MyBitOutputStream myBitOutputStream) throws IOException {
        Listener listener2 = this.listener;
        if (listener2 != null) {
            listener2.eoiCode(this.eoiCode);
        }
        writeCode(myBitOutputStream, this.eoiCode);
    }

    private final void writeCode(MyBitOutputStream myBitOutputStream, int i) throws IOException {
        myBitOutputStream.writeBits(i, this.codeSize);
    }

    private final boolean isInTable(byte[] bArr, int i, int i2) {
        return this.map.containsKey(arrayToKey(bArr, i, i2));
    }

    private final int codeFromString(byte[] bArr, int i, int i2) throws IOException {
        Object obj = this.map.get(arrayToKey(bArr, i, i2));
        if (obj != null) {
            return ((Integer) obj).intValue();
        }
        throw new IOException("CodeFromString");
    }

    private final boolean addTableEntry(MyBitOutputStream myBitOutputStream, byte[] bArr, int i, int i2) throws IOException {
        return addTableEntry(myBitOutputStream, arrayToKey(bArr, i, i2));
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x0024  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final boolean addTableEntry(org.apache.sanselan.common.mylzw.MyBitOutputStream r5, java.lang.Object r6) throws java.io.IOException {
        /*
            r4 = this;
            int r0 = r4.codeSize
            r1 = 1
            int r0 = r1 << r0
            boolean r2 = r4.earlyLimit
            if (r2 == 0) goto L_0x000b
            int r0 = r0 + -1
        L_0x000b:
            int r2 = r4.codes
            if (r2 != r0) goto L_0x0021
            int r0 = r4.codeSize
            r2 = 12
            if (r0 >= r2) goto L_0x0019
            r4.incrementCodeSize()
            goto L_0x0021
        L_0x0019:
            r4.writeClearCode(r5)
            r4.clearTable()
            r5 = 1
            goto L_0x0022
        L_0x0021:
            r5 = 0
        L_0x0022:
            if (r5 != 0) goto L_0x0035
            java.util.Map r0 = r4.map
            java.lang.Integer r2 = new java.lang.Integer
            int r3 = r4.codes
            r2.<init>(r3)
            r0.put(r6, r2)
            int r6 = r4.codes
            int r6 = r6 + r1
            r4.codes = r6
        L_0x0035:
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.sanselan.common.mylzw.MyLZWCompressor.addTableEntry(org.apache.sanselan.common.mylzw.MyBitOutputStream, java.lang.Object):boolean");
    }

    public byte[] compress(byte[] bArr) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(bArr.length);
        MyBitOutputStream myBitOutputStream = new MyBitOutputStream(byteArrayOutputStream, this.byteOrder);
        InitializeStringTable();
        clearTable();
        writeClearCode(myBitOutputStream);
        int i = 0;
        int i2 = 0;
        for (int i3 = 0; i3 < bArr.length; i3++) {
            int i4 = i2 + 1;
            if (isInTable(bArr, i, i4)) {
                i2 = i4;
            } else {
                writeDataCode(myBitOutputStream, codeFromString(bArr, i, i2));
                addTableEntry(myBitOutputStream, bArr, i, i4);
                i = i3;
                i2 = 1;
            }
        }
        writeDataCode(myBitOutputStream, codeFromString(bArr, i, i2));
        writeEoiCode(myBitOutputStream);
        myBitOutputStream.flushCache();
        return byteArrayOutputStream.toByteArray();
    }
}

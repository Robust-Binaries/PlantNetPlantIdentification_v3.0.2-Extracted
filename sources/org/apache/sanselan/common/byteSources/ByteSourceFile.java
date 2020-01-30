package org.apache.sanselan.common.byteSources;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import org.apache.sanselan.util.Debug;

public class ByteSourceFile extends ByteSource {
    private final File file;

    public ByteSourceFile(File file2) {
        super(file2.getName());
        this.file = file2;
    }

    public InputStream getInputStream() throws IOException {
        return new BufferedInputStream(new FileInputStream(this.file));
    }

    public byte[] getBlock(int i, int i2) throws IOException {
        RandomAccessFile randomAccessFile = null;
        try {
            RandomAccessFile randomAccessFile2 = new RandomAccessFile(this.file, "r");
            try {
                byte[] rAFBytes = getRAFBytes(randomAccessFile2, (long) i, i2, "Could not read value from file");
                try {
                    randomAccessFile2.close();
                } catch (Exception e) {
                    Debug.debug((Throwable) e);
                }
                return rAFBytes;
            } catch (Throwable th) {
                th = th;
                randomAccessFile = randomAccessFile2;
                try {
                    randomAccessFile.close();
                } catch (Exception e2) {
                    Debug.debug((Throwable) e2);
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            randomAccessFile.close();
            throw th;
        }
    }

    public long getLength() {
        return this.file.length();
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x002f A[SYNTHETIC, Splitter:B:20:0x002f] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public byte[] getAll() throws java.io.IOException {
        /*
            r5 = this;
            java.io.ByteArrayOutputStream r0 = new java.io.ByteArrayOutputStream
            r0.<init>()
            r1 = 0
            java.io.FileInputStream r2 = new java.io.FileInputStream     // Catch:{ all -> 0x002c }
            java.io.File r3 = r5.file     // Catch:{ all -> 0x002c }
            r2.<init>(r3)     // Catch:{ all -> 0x002c }
            java.io.BufferedInputStream r1 = new java.io.BufferedInputStream     // Catch:{ all -> 0x0029 }
            r1.<init>(r2)     // Catch:{ all -> 0x0029 }
            r2 = 1024(0x400, float:1.435E-42)
            byte[] r2 = new byte[r2]     // Catch:{ all -> 0x002c }
        L_0x0016:
            int r3 = r1.read(r2)     // Catch:{ all -> 0x002c }
            if (r3 <= 0) goto L_0x0021
            r4 = 0
            r0.write(r2, r4, r3)     // Catch:{ all -> 0x002c }
            goto L_0x0016
        L_0x0021:
            byte[] r0 = r0.toByteArray()     // Catch:{ all -> 0x002c }
            r1.close()     // Catch:{ IOException -> 0x0028 }
        L_0x0028:
            return r0
        L_0x0029:
            r0 = move-exception
            r1 = r2
            goto L_0x002d
        L_0x002c:
            r0 = move-exception
        L_0x002d:
            if (r1 == 0) goto L_0x0032
            r1.close()     // Catch:{ IOException -> 0x0032 }
        L_0x0032:
            throw r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.sanselan.common.byteSources.ByteSourceFile.getAll():byte[]");
    }

    public String getDescription() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("File: '");
        stringBuffer.append(this.file.getAbsolutePath());
        stringBuffer.append("'");
        return stringBuffer.toString();
    }
}

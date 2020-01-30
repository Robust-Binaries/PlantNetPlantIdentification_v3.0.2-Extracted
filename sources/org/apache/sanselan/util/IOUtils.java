package org.apache.sanselan.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.sanselan.SanselanConstants;

public class IOUtils implements SanselanConstants {
    private IOUtils() {
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x0030 A[SYNTHETIC, Splitter:B:18:0x0030] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] getInputStreamBytes(java.io.InputStream r5) throws java.io.IOException {
        /*
            r0 = 0
            java.io.ByteArrayOutputStream r1 = new java.io.ByteArrayOutputStream     // Catch:{ all -> 0x002c }
            r2 = 4096(0x1000, float:5.74E-42)
            r1.<init>(r2)     // Catch:{ all -> 0x002c }
            java.io.BufferedInputStream r0 = new java.io.BufferedInputStream     // Catch:{ all -> 0x002a }
            r0.<init>(r5)     // Catch:{ all -> 0x002a }
            byte[] r5 = new byte[r2]     // Catch:{ all -> 0x002a }
        L_0x000f:
            r3 = 0
            int r4 = r0.read(r5, r3, r2)     // Catch:{ all -> 0x002a }
            if (r4 <= 0) goto L_0x001a
            r1.write(r5, r3, r4)     // Catch:{ all -> 0x002a }
            goto L_0x000f
        L_0x001a:
            r1.flush()     // Catch:{ all -> 0x002a }
            byte[] r5 = r1.toByteArray()     // Catch:{ all -> 0x002a }
            r1.close()     // Catch:{ IOException -> 0x0025 }
            goto L_0x0029
        L_0x0025:
            r0 = move-exception
            org.apache.sanselan.util.Debug.debug(r0)
        L_0x0029:
            return r5
        L_0x002a:
            r5 = move-exception
            goto L_0x002e
        L_0x002c:
            r5 = move-exception
            r1 = r0
        L_0x002e:
            if (r1 == 0) goto L_0x0038
            r1.close()     // Catch:{ IOException -> 0x0034 }
            goto L_0x0038
        L_0x0034:
            r0 = move-exception
            org.apache.sanselan.util.Debug.debug(r0)
        L_0x0038:
            throw r5
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.sanselan.util.IOUtils.getInputStreamBytes(java.io.InputStream):byte[]");
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x0019 A[SYNTHETIC, Splitter:B:14:0x0019] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] getFileBytes(java.io.File r2) throws java.io.IOException {
        /*
            r0 = 0
            java.io.FileInputStream r1 = new java.io.FileInputStream     // Catch:{ all -> 0x0016 }
            r1.<init>(r2)     // Catch:{ all -> 0x0016 }
            byte[] r2 = getInputStreamBytes(r1)     // Catch:{ all -> 0x0013 }
            r1.close()     // Catch:{ IOException -> 0x000e }
            goto L_0x0012
        L_0x000e:
            r0 = move-exception
            org.apache.sanselan.util.Debug.debug(r0)
        L_0x0012:
            return r2
        L_0x0013:
            r2 = move-exception
            r0 = r1
            goto L_0x0017
        L_0x0016:
            r2 = move-exception
        L_0x0017:
            if (r0 == 0) goto L_0x0021
            r0.close()     // Catch:{ IOException -> 0x001d }
            goto L_0x0021
        L_0x001d:
            r0 = move-exception
            org.apache.sanselan.util.Debug.debug(r0)
        L_0x0021:
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.sanselan.util.IOUtils.getFileBytes(java.io.File):byte[]");
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x0018 A[SYNTHETIC, Splitter:B:13:0x0018] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void writeToFile(byte[] r2, java.io.File r3) throws java.io.IOException {
        /*
            r0 = 0
            java.io.ByteArrayInputStream r1 = new java.io.ByteArrayInputStream     // Catch:{ all -> 0x0015 }
            r1.<init>(r2)     // Catch:{ all -> 0x0015 }
            putInputStreamToFile(r1, r3)     // Catch:{ all -> 0x0012 }
            r1.close()     // Catch:{ Exception -> 0x000d }
            goto L_0x0011
        L_0x000d:
            r2 = move-exception
            org.apache.sanselan.util.Debug.debug(r2)
        L_0x0011:
            return
        L_0x0012:
            r2 = move-exception
            r0 = r1
            goto L_0x0016
        L_0x0015:
            r2 = move-exception
        L_0x0016:
            if (r0 == 0) goto L_0x0020
            r0.close()     // Catch:{ Exception -> 0x001c }
            goto L_0x0020
        L_0x001c:
            r3 = move-exception
            org.apache.sanselan.util.Debug.debug(r3)
        L_0x0020:
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.sanselan.util.IOUtils.writeToFile(byte[], java.io.File):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x0025 A[SYNTHETIC, Splitter:B:16:0x0025] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void putInputStreamToFile(java.io.InputStream r2, java.io.File r3) throws java.io.IOException {
        /*
            r0 = 0
            java.io.File r1 = r3.getParentFile()     // Catch:{ all -> 0x0022 }
            if (r1 == 0) goto L_0x000e
            java.io.File r1 = r3.getParentFile()     // Catch:{ all -> 0x0022 }
            r1.mkdirs()     // Catch:{ all -> 0x0022 }
        L_0x000e:
            java.io.FileOutputStream r1 = new java.io.FileOutputStream     // Catch:{ all -> 0x0022 }
            r1.<init>(r3)     // Catch:{ all -> 0x0022 }
            copyStreamToStream(r2, r1)     // Catch:{ all -> 0x001f }
            r1.close()     // Catch:{ Exception -> 0x001a }
            goto L_0x001e
        L_0x001a:
            r2 = move-exception
            org.apache.sanselan.util.Debug.debug(r2)
        L_0x001e:
            return
        L_0x001f:
            r2 = move-exception
            r0 = r1
            goto L_0x0023
        L_0x0022:
            r2 = move-exception
        L_0x0023:
            if (r0 == 0) goto L_0x002d
            r0.close()     // Catch:{ Exception -> 0x0029 }
            goto L_0x002d
        L_0x0029:
            r3 = move-exception
            org.apache.sanselan.util.Debug.debug(r3)
        L_0x002d:
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.sanselan.util.IOUtils.putInputStreamToFile(java.io.InputStream, java.io.File):void");
    }

    public static void copyStreamToStream(InputStream inputStream, OutputStream outputStream) throws IOException {
        copyStreamToStream(inputStream, outputStream, true);
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x003b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void copyStreamToStream(java.io.InputStream r4, java.io.OutputStream r5, boolean r6) throws java.io.IOException {
        /*
            r0 = 0
            java.io.BufferedInputStream r1 = new java.io.BufferedInputStream     // Catch:{ all -> 0x0036 }
            r1.<init>(r4)     // Catch:{ all -> 0x0036 }
            java.io.BufferedOutputStream r4 = new java.io.BufferedOutputStream     // Catch:{ all -> 0x0033 }
            r4.<init>(r5)     // Catch:{ all -> 0x0033 }
            r0 = 4096(0x1000, float:5.74E-42)
            byte[] r0 = new byte[r0]     // Catch:{ all -> 0x0031 }
        L_0x000f:
            int r2 = r0.length     // Catch:{ all -> 0x0031 }
            r3 = 0
            int r2 = r1.read(r0, r3, r2)     // Catch:{ all -> 0x0031 }
            if (r2 <= 0) goto L_0x001b
            r5.write(r0, r3, r2)     // Catch:{ all -> 0x0031 }
            goto L_0x000f
        L_0x001b:
            r4.flush()     // Catch:{ all -> 0x0031 }
            if (r6 == 0) goto L_0x0030
            r1.close()     // Catch:{ IOException -> 0x0024 }
            goto L_0x0028
        L_0x0024:
            r5 = move-exception
            org.apache.sanselan.util.Debug.debug(r5)
        L_0x0028:
            r4.close()     // Catch:{ IOException -> 0x002c }
            goto L_0x0030
        L_0x002c:
            r4 = move-exception
            org.apache.sanselan.util.Debug.debug(r4)
        L_0x0030:
            return
        L_0x0031:
            r5 = move-exception
            goto L_0x0039
        L_0x0033:
            r5 = move-exception
            r4 = r0
            goto L_0x0039
        L_0x0036:
            r5 = move-exception
            r4 = r0
            r1 = r4
        L_0x0039:
            if (r6 == 0) goto L_0x004f
            if (r1 == 0) goto L_0x0045
            r1.close()     // Catch:{ IOException -> 0x0041 }
            goto L_0x0045
        L_0x0041:
            r6 = move-exception
            org.apache.sanselan.util.Debug.debug(r6)
        L_0x0045:
            if (r4 == 0) goto L_0x004f
            r4.close()     // Catch:{ IOException -> 0x004b }
            goto L_0x004f
        L_0x004b:
            r4 = move-exception
            org.apache.sanselan.util.Debug.debug(r4)
        L_0x004f:
            throw r5
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.sanselan.util.IOUtils.copyStreamToStream(java.io.InputStream, java.io.OutputStream, boolean):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:24:0x0043 A[SYNTHETIC, Splitter:B:24:0x0043] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x004d A[SYNTHETIC, Splitter:B:29:0x004d] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final boolean copyFileNio(java.io.File r13, java.io.File r14) throws java.io.IOException {
        /*
            r0 = 0
            java.io.FileInputStream r1 = new java.io.FileInputStream     // Catch:{ all -> 0x003f }
            r1.<init>(r13)     // Catch:{ all -> 0x003f }
            java.nio.channels.FileChannel r13 = r1.getChannel()     // Catch:{ all -> 0x003f }
            java.io.FileOutputStream r1 = new java.io.FileOutputStream     // Catch:{ all -> 0x0039 }
            r1.<init>(r14)     // Catch:{ all -> 0x0039 }
            java.nio.channels.FileChannel r14 = r1.getChannel()     // Catch:{ all -> 0x0039 }
            r1 = 16777216(0x1000000, float:2.3509887E-38)
            long r8 = r13.size()     // Catch:{ all -> 0x0034 }
            r2 = 0
            r10 = r2
        L_0x001c:
            int r2 = (r10 > r8 ? 1 : (r10 == r8 ? 0 : -1))
            if (r2 >= 0) goto L_0x002a
            long r5 = (long) r1     // Catch:{ all -> 0x0034 }
            r2 = r13
            r3 = r10
            r7 = r14
            long r2 = r2.transferTo(r3, r5, r7)     // Catch:{ all -> 0x0034 }
            long r10 = r10 + r2
            goto L_0x001c
        L_0x002a:
            r13.close()     // Catch:{ all -> 0x0034 }
            r14.close()     // Catch:{ all -> 0x0032 }
            r13 = 1
            return r13
        L_0x0032:
            r13 = move-exception
            goto L_0x0041
        L_0x0034:
            r0 = move-exception
            r12 = r0
            r0 = r13
            r13 = r12
            goto L_0x0041
        L_0x0039:
            r14 = move-exception
            r12 = r0
            r0 = r13
            r13 = r14
            r14 = r12
            goto L_0x0041
        L_0x003f:
            r13 = move-exception
            r14 = r0
        L_0x0041:
            if (r0 == 0) goto L_0x004b
            r0.close()     // Catch:{ IOException -> 0x0047 }
            goto L_0x004b
        L_0x0047:
            r0 = move-exception
            org.apache.sanselan.util.Debug.debug(r0)
        L_0x004b:
            if (r14 == 0) goto L_0x0055
            r14.close()     // Catch:{ IOException -> 0x0051 }
            goto L_0x0055
        L_0x0051:
            r14 = move-exception
            org.apache.sanselan.util.Debug.debug(r14)
        L_0x0055:
            throw r13
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.sanselan.util.IOUtils.copyFileNio(java.io.File, java.io.File):boolean");
    }
}

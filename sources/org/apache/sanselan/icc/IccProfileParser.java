package org.apache.sanselan.icc;

import java.awt.color.ICC_Profile;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.apache.sanselan.common.BinaryFileParser;
import org.apache.sanselan.common.byteSources.ByteSource;
import org.apache.sanselan.common.byteSources.ByteSourceArray;
import org.apache.sanselan.common.byteSources.ByteSourceFile;
import org.apache.sanselan.util.CachingInputStream;
import org.apache.sanselan.util.Debug;

public class IccProfileParser extends BinaryFileParser implements IccConstants {
    public IccProfileParser() {
        setByteOrder(77);
    }

    public IccProfileInfo getICCProfileInfo(ICC_Profile iCC_Profile) {
        if (iCC_Profile == null) {
            return null;
        }
        return getICCProfileInfo((ByteSource) new ByteSourceArray(iCC_Profile.getData()));
    }

    public IccProfileInfo getICCProfileInfo(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        return getICCProfileInfo((ByteSource) new ByteSourceArray(bArr));
    }

    public IccProfileInfo getICCProfileInfo(File file) {
        if (file == null) {
            return null;
        }
        return getICCProfileInfo((ByteSource) new ByteSourceFile(file));
    }

    /* JADX WARNING: Removed duplicated region for block: B:30:0x0040 A[SYNTHETIC, Splitter:B:30:0x0040] */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x004c  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x0052 A[SYNTHETIC, Splitter:B:39:0x0052] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.apache.sanselan.icc.IccProfileInfo getICCProfileInfo(org.apache.sanselan.common.byteSources.ByteSource r7) {
        /*
            r6 = this;
            r0 = 0
            java.io.InputStream r1 = r7.getInputStream()     // Catch:{ Exception -> 0x0039, all -> 0x0037 }
            org.apache.sanselan.icc.IccProfileInfo r2 = r6.readICCProfileInfo(r1)     // Catch:{ Exception -> 0x0035 }
            if (r2 != 0) goto L_0x0016
            if (r1 == 0) goto L_0x0015
            r1.close()     // Catch:{ Exception -> 0x0011 }
            goto L_0x0015
        L_0x0011:
            r7 = move-exception
            org.apache.sanselan.util.Debug.debug(r7)
        L_0x0015:
            return r0
        L_0x0016:
            r1.close()     // Catch:{ Exception -> 0x0035 }
            r1 = 0
        L_0x001a:
            org.apache.sanselan.icc.IccTag[] r3 = r2.tags     // Catch:{ Exception -> 0x0039, all -> 0x0037 }
            int r3 = r3.length     // Catch:{ Exception -> 0x0039, all -> 0x0037 }
            if (r1 >= r3) goto L_0x0031
            org.apache.sanselan.icc.IccTag[] r3 = r2.tags     // Catch:{ Exception -> 0x0039, all -> 0x0037 }
            r3 = r3[r1]     // Catch:{ Exception -> 0x0039, all -> 0x0037 }
            int r4 = r3.offset     // Catch:{ Exception -> 0x0039, all -> 0x0037 }
            int r5 = r3.length     // Catch:{ Exception -> 0x0039, all -> 0x0037 }
            byte[] r4 = r7.getBlock(r4, r5)     // Catch:{ Exception -> 0x0039, all -> 0x0037 }
            r3.setData(r4)     // Catch:{ Exception -> 0x0039, all -> 0x0037 }
            int r1 = r1 + 1
            goto L_0x001a
        L_0x0031:
            return r2
        L_0x0032:
            r7 = move-exception
            r0 = r1
            goto L_0x0050
        L_0x0035:
            r7 = move-exception
            goto L_0x003b
        L_0x0037:
            r7 = move-exception
            goto L_0x0050
        L_0x0039:
            r7 = move-exception
            r1 = r0
        L_0x003b:
            org.apache.sanselan.util.Debug.debug(r7)     // Catch:{ all -> 0x0032 }
            if (r1 == 0) goto L_0x0048
            r1.close()     // Catch:{ Exception -> 0x0044 }
            goto L_0x0048
        L_0x0044:
            r7 = move-exception
            org.apache.sanselan.util.Debug.debug(r7)
        L_0x0048:
            boolean r7 = r6.debug
            if (r7 == 0) goto L_0x004f
            org.apache.sanselan.util.Debug.debug()
        L_0x004f:
            return r0
        L_0x0050:
            if (r0 == 0) goto L_0x005a
            r0.close()     // Catch:{ Exception -> 0x0056 }
            goto L_0x005a
        L_0x0056:
            r0 = move-exception
            org.apache.sanselan.util.Debug.debug(r0)
        L_0x005a:
            throw r7
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.sanselan.icc.IccProfileParser.getICCProfileInfo(org.apache.sanselan.common.byteSources.ByteSource):org.apache.sanselan.icc.IccProfileInfo");
    }

    private IccProfileInfo readICCProfileInfo(InputStream inputStream) {
        IccProfileParser iccProfileParser = this;
        CachingInputStream cachingInputStream = new CachingInputStream(inputStream);
        if (iccProfileParser.debug) {
            Debug.debug();
        }
        try {
            int read4Bytes = iccProfileParser.read4Bytes("ProfileSize", cachingInputStream, "Not a Valid ICC Profile");
            int read4Bytes2 = iccProfileParser.read4Bytes("Signature", cachingInputStream, "Not a Valid ICC Profile");
            if (iccProfileParser.debug) {
                iccProfileParser.printCharQuad("CMMTypeSignature", read4Bytes2);
            }
            int read4Bytes3 = iccProfileParser.read4Bytes("ProfileVersion", cachingInputStream, "Not a Valid ICC Profile");
            int read4Bytes4 = iccProfileParser.read4Bytes("ProfileDeviceClassSignature", cachingInputStream, "Not a Valid ICC Profile");
            if (iccProfileParser.debug) {
                iccProfileParser.printCharQuad("ProfileDeviceClassSignature", read4Bytes4);
            }
            int read4Bytes5 = iccProfileParser.read4Bytes("ColorSpace", cachingInputStream, "Not a Valid ICC Profile");
            if (iccProfileParser.debug) {
                iccProfileParser.printCharQuad("ColorSpace", read4Bytes5);
            }
            int read4Bytes6 = iccProfileParser.read4Bytes("ProfileConnectionSpace", cachingInputStream, "Not a Valid ICC Profile");
            if (iccProfileParser.debug) {
                iccProfileParser.printCharQuad("ProfileConnectionSpace", read4Bytes6);
            }
            iccProfileParser.skipBytes(cachingInputStream, 12, "Not a Valid ICC Profile");
            int read4Bytes7 = iccProfileParser.read4Bytes("ProfileFileSignature", cachingInputStream, "Not a Valid ICC Profile");
            if (iccProfileParser.debug) {
                iccProfileParser.printCharQuad("ProfileFileSignature", read4Bytes7);
            }
            int read4Bytes8 = iccProfileParser.read4Bytes("PrimaryPlatformSignature", cachingInputStream, "Not a Valid ICC Profile");
            if (iccProfileParser.debug) {
                iccProfileParser.printCharQuad("PrimaryPlatformSignature", read4Bytes8);
            }
            int read4Bytes9 = iccProfileParser.read4Bytes("ProfileFileSignature", cachingInputStream, "Not a Valid ICC Profile");
            if (iccProfileParser.debug) {
                iccProfileParser.printCharQuad("ProfileFileSignature", read4Bytes7);
            }
            int read4Bytes10 = iccProfileParser.read4Bytes("ProfileFileSignature", cachingInputStream, "Not a Valid ICC Profile");
            if (iccProfileParser.debug) {
                iccProfileParser.printCharQuad("DeviceManufacturer", read4Bytes10);
            }
            int read4Bytes11 = iccProfileParser.read4Bytes("DeviceModel", cachingInputStream, "Not a Valid ICC Profile");
            if (iccProfileParser.debug) {
                iccProfileParser.printCharQuad("DeviceModel", read4Bytes11);
            }
            iccProfileParser.skipBytes(cachingInputStream, 8, "Not a Valid ICC Profile");
            int read4Bytes12 = iccProfileParser.read4Bytes("RenderingIntent", cachingInputStream, "Not a Valid ICC Profile");
            if (iccProfileParser.debug) {
                iccProfileParser.printCharQuad("RenderingIntent", read4Bytes12);
            }
            iccProfileParser.skipBytes(cachingInputStream, 12, "Not a Valid ICC Profile");
            int read4Bytes13 = iccProfileParser.read4Bytes("ProfileCreatorSignature", cachingInputStream, "Not a Valid ICC Profile");
            if (iccProfileParser.debug) {
                iccProfileParser.printCharQuad("ProfileCreatorSignature", read4Bytes13);
            }
            int i = read4Bytes12;
            iccProfileParser.skipBytes(cachingInputStream, 16, "Not a Valid ICC Profile");
            iccProfileParser.skipBytes(cachingInputStream, 28, "Not a Valid ICC Profile");
            int read4Bytes14 = iccProfileParser.read4Bytes("TagCount", cachingInputStream, "Not a Valid ICC Profile");
            IccTag[] iccTagArr = new IccTag[read4Bytes14];
            int i2 = read4Bytes13;
            int i3 = 0;
            while (i3 < read4Bytes14) {
                int i4 = read4Bytes14;
                try {
                    StringBuffer stringBuffer = new StringBuffer();
                    int i5 = read4Bytes11;
                    stringBuffer.append("TagSignature[");
                    stringBuffer.append(i3);
                    stringBuffer.append("]");
                    int read4Bytes15 = iccProfileParser.read4Bytes(stringBuffer.toString(), cachingInputStream, "Not a Valid ICC Profile");
                    StringBuffer stringBuffer2 = new StringBuffer();
                    int i6 = read4Bytes10;
                    stringBuffer2.append("OffsetToData[");
                    stringBuffer2.append(i3);
                    stringBuffer2.append("]");
                    int read4Bytes16 = iccProfileParser.read4Bytes(stringBuffer2.toString(), cachingInputStream, "Not a Valid ICC Profile");
                    StringBuffer stringBuffer3 = new StringBuffer();
                    int i7 = read4Bytes9;
                    stringBuffer3.append("ElementSize[");
                    stringBuffer3.append(i3);
                    stringBuffer3.append("]");
                    iccTagArr[i3] = new IccTag(read4Bytes15, read4Bytes16, iccProfileParser.read4Bytes(stringBuffer3.toString(), cachingInputStream, "Not a Valid ICC Profile"), iccProfileParser.getIccTagType(read4Bytes15));
                    i3++;
                    read4Bytes14 = i4;
                    read4Bytes11 = i5;
                    read4Bytes10 = i6;
                    read4Bytes9 = i7;
                    iccProfileParser = this;
                } catch (Exception e) {
                    e = e;
                    Debug.debug((Throwable) e);
                    return null;
                }
            }
            int i8 = read4Bytes11;
            int i9 = read4Bytes9;
            int i10 = read4Bytes10;
            while (cachingInputStream.read() >= 0) {
            }
            byte[] cache = cachingInputStream.getCache();
            if (cache.length >= read4Bytes) {
                IccProfileInfo iccProfileInfo = new IccProfileInfo(cache, read4Bytes, read4Bytes2, read4Bytes3, read4Bytes4, read4Bytes5, read4Bytes6, read4Bytes7, read4Bytes8, i9, i10, i8, i, i2, null, iccTagArr);
                try {
                    if (this.debug) {
                        StringBuffer stringBuffer4 = new StringBuffer();
                        stringBuffer4.append("issRGB: ");
                        stringBuffer4.append(iccProfileInfo.issRGB());
                        Debug.debug(stringBuffer4.toString());
                    }
                    return iccProfileInfo;
                } catch (Exception e2) {
                    e = e2;
                    Debug.debug((Throwable) e);
                    return null;
                }
            } else {
                throw new IOException("Couldn't read ICC Profile.");
            }
        } catch (Exception e3) {
            e = e3;
            IccProfileParser iccProfileParser2 = iccProfileParser;
            Debug.debug((Throwable) e);
            return null;
        }
    }

    private IccTagType getIccTagType(int i) {
        for (int i2 = 0; i2 < TagTypes.length; i2++) {
            if (TagTypes[i2].signature == i) {
                return TagTypes[i2];
            }
        }
        return null;
    }

    public Boolean issRGB(ICC_Profile iCC_Profile) {
        if (iCC_Profile == null) {
            return null;
        }
        return issRGB((ByteSource) new ByteSourceArray(iCC_Profile.getData()));
    }

    public Boolean issRGB(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        return issRGB((ByteSource) new ByteSourceArray(bArr));
    }

    public Boolean issRGB(File file) {
        if (file == null) {
            return null;
        }
        return issRGB((ByteSource) new ByteSourceFile(file));
    }

    public Boolean issRGB(ByteSource byteSource) {
        try {
            if (this.debug) {
                Debug.debug();
            }
            InputStream inputStream = byteSource.getInputStream();
            read4Bytes("ProfileSize", inputStream, "Not a Valid ICC Profile");
            skipBytes(inputStream, 20);
            skipBytes(inputStream, 12, "Not a Valid ICC Profile");
            skipBytes(inputStream, 12);
            int read4Bytes = read4Bytes("ProfileFileSignature", inputStream, "Not a Valid ICC Profile");
            if (this.debug) {
                printCharQuad("DeviceManufacturer", read4Bytes);
            }
            int read4Bytes2 = read4Bytes("DeviceModel", inputStream, "Not a Valid ICC Profile");
            if (this.debug) {
                printCharQuad("DeviceModel", read4Bytes2);
            }
            return new Boolean(read4Bytes == 1229275936 && read4Bytes2 == 1934772034);
        } catch (Exception e) {
            Debug.debug((Throwable) e);
            return null;
        }
    }
}

package org.apache.sanselan.formats.psd;

import java.awt.Dimension;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;
import org.apache.sanselan.ImageFormat;
import org.apache.sanselan.ImageInfo;
import org.apache.sanselan.ImageParser;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.common.byteSources.ByteSource;
import org.apache.sanselan.formats.pnm.PNMConstants;
import org.apache.sanselan.util.Debug;

public class PsdImageParser extends ImageParser {
    private static final String[] ACCEPTED_EXTENSIONS = {DEFAULT_EXTENSION};
    public static final String BLOCK_NAME_XMP = "XMP";
    private static final int COLOR_MODE_INDEXED = 2;
    private static final String DEFAULT_EXTENSION = ".psd";
    public static final int IMAGE_RESOURCE_ID_ICC_PROFILE = 1039;
    public static final int IMAGE_RESOURCE_ID_XMP = 1060;
    private static final int PSD_HEADER_LENGTH = 26;
    private static final int PSD_SECTION_COLOR_MODE = 1;
    private static final int PSD_SECTION_HEADER = 0;
    private static final int PSD_SECTION_IMAGE_DATA = 4;
    private static final int PSD_SECTION_IMAGE_RESOURCES = 2;
    private static final int PSD_SECTION_LAYER_AND_MASK_DATA = 3;

    private int getChannelsPerMode(int i) {
        switch (i) {
            case 0:
                return 1;
            case 1:
                return 1;
            case 2:
                return -1;
            case 3:
                return 3;
            case 4:
                return 4;
            case 7:
                return -1;
            case 8:
                return -1;
            case 9:
                return 4;
            default:
                return -1;
        }
    }

    public boolean embedICCProfile(File file, File file2, byte[] bArr) {
        return false;
    }

    public byte[] embedICCProfile(byte[] bArr, byte[] bArr2) {
        return null;
    }

    public String getDefaultExtension() {
        return DEFAULT_EXTENSION;
    }

    public IImageMetadata getMetadata(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        return null;
    }

    public String getName() {
        return "PSD-Custom";
    }

    public PsdImageParser() {
        super.setByteOrder(77);
    }

    /* access modifiers changed from: protected */
    public String[] getAcceptedExtensions() {
        return ACCEPTED_EXTENSIONS;
    }

    /* access modifiers changed from: protected */
    public ImageFormat[] getAcceptedTypes() {
        return new ImageFormat[]{ImageFormat.IMAGE_FORMAT_PSD};
    }

    private PSDHeaderInfo readHeader(ByteSource byteSource) throws ImageReadException, IOException {
        InputStream inputStream;
        try {
            inputStream = byteSource.getInputStream();
            try {
                PSDHeaderInfo readHeader = readHeader(inputStream);
                try {
                    inputStream.close();
                } catch (Exception e) {
                    Debug.debug((Throwable) e);
                }
                return readHeader;
            } catch (Throwable th) {
                th = th;
                try {
                    inputStream.close();
                } catch (Exception e2) {
                    Debug.debug((Throwable) e2);
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            inputStream = null;
            inputStream.close();
            throw th;
        }
    }

    private PSDHeaderInfo readHeader(InputStream inputStream) throws ImageReadException, IOException {
        readAndVerifyBytes(inputStream, new byte[]{56, 66, PNMConstants.PNM_PREFIX_BYTE, 83}, "Not a Valid PSD File");
        PSDHeaderInfo pSDHeaderInfo = new PSDHeaderInfo(read2Bytes("Version", inputStream, "Not a Valid PSD File"), readByteArray("Reserved", 6, inputStream, "Not a Valid PSD File"), read2Bytes("Channels", inputStream, "Not a Valid PSD File"), read4Bytes("Rows", inputStream, "Not a Valid PSD File"), read4Bytes("Columns", inputStream, "Not a Valid PSD File"), read2Bytes("Depth", inputStream, "Not a Valid PSD File"), read2Bytes("Mode", inputStream, "Not a Valid PSD File"));
        return pSDHeaderInfo;
    }

    private ImageContents readImageContents(InputStream inputStream) throws ImageReadException, IOException {
        PSDHeaderInfo readHeader = readHeader(inputStream);
        int read4Bytes = read4Bytes("ColorModeDataLength", inputStream, "Not a Valid PSD File");
        skipBytes(inputStream, read4Bytes);
        int read4Bytes2 = read4Bytes("ImageResourcesLength", inputStream, "Not a Valid PSD File");
        skipBytes(inputStream, read4Bytes2);
        int read4Bytes3 = read4Bytes("LayerAndMaskDataLength", inputStream, "Not a Valid PSD File");
        skipBytes(inputStream, read4Bytes3);
        ImageContents imageContents = new ImageContents(readHeader, read4Bytes, read4Bytes2, read4Bytes3, read2Bytes("Compression", inputStream, "Not a Valid PSD File"));
        return imageContents;
    }

    private ArrayList readImageResourceBlocks(byte[] bArr, int[] iArr, int i) throws ImageReadException, IOException {
        return readImageResourceBlocks(new ByteArrayInputStream(bArr), iArr, i, bArr.length);
    }

    private boolean keepImageResourceBlock(int i, int[] iArr) {
        if (iArr == null) {
            return true;
        }
        for (int i2 : iArr) {
            if (i == i2) {
                return true;
            }
        }
        return false;
    }

    private ArrayList readImageResourceBlocks(InputStream inputStream, int[] iArr, int i, int i2) throws ImageReadException, IOException {
        ArrayList arrayList = new ArrayList();
        while (i2 > 0) {
            readAndVerifyBytes(inputStream, new byte[]{56, 66, 73, 77}, "Not a Valid PSD File");
            int i3 = i2 - 4;
            int read2Bytes = read2Bytes("ID", inputStream, "Not a Valid PSD File");
            int i4 = i3 - 2;
            byte readByte = readByte("NameLength", inputStream, "Not a Valid PSD File");
            int i5 = i4 - 1;
            byte[] readByteArray = readByteArray("NameData", readByte, inputStream, "Not a Valid PSD File");
            int i6 = i5 - readByte;
            if ((readByte + 1) % 2 != 0) {
                readByte("NameDiscard", inputStream, "Not a Valid PSD File");
                i6--;
            }
            int read4Bytes = read4Bytes("Size", inputStream, "Not a Valid PSD File");
            int i7 = i6 - 4;
            byte[] readByteArray2 = readByteArray("Data", read4Bytes, inputStream, "Not a Valid PSD File");
            i2 = i7 - read4Bytes;
            if (read4Bytes % 2 != 0) {
                readByte("DataDiscard", inputStream, "Not a Valid PSD File");
                i2--;
            }
            if (keepImageResourceBlock(read2Bytes, iArr)) {
                arrayList.add(new ImageResourceBlock(read2Bytes, readByteArray, readByteArray2));
                if (i >= 0 && arrayList.size() >= i) {
                    return arrayList;
                }
            }
        }
        return arrayList;
    }

    private ArrayList readImageResourceBlocks(ByteSource byteSource, int[] iArr, int i) throws ImageReadException, IOException {
        InputStream inputStream;
        try {
            inputStream = byteSource.getInputStream();
            try {
                ImageContents readImageContents = readImageContents(inputStream);
                inputStream.close();
                InputStream inputStream2 = getInputStream(byteSource, 2);
                ArrayList readImageResourceBlocks = readImageResourceBlocks(readByteArray("ImageResources", readImageContents.ImageResourcesLength, inputStream2, "Not a Valid PSD File"), iArr, i);
                try {
                    inputStream2.close();
                } catch (Exception e) {
                    Debug.debug((Throwable) e);
                }
                return readImageResourceBlocks;
            } catch (Throwable th) {
                th = th;
                try {
                    inputStream.close();
                } catch (Exception e2) {
                    Debug.debug((Throwable) e2);
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            inputStream = null;
            inputStream.close();
            throw th;
        }
    }

    private InputStream getInputStream(ByteSource byteSource, int i) throws ImageReadException, IOException {
        InputStream inputStream = byteSource.getInputStream();
        if (i == 0) {
            return inputStream;
        }
        skipBytes(inputStream, 26);
        int read4Bytes = read4Bytes("ColorModeDataLength", inputStream, "Not a Valid PSD File");
        if (i == 1) {
            return inputStream;
        }
        skipBytes(inputStream, read4Bytes);
        int read4Bytes2 = read4Bytes("ImageResourcesLength", inputStream, "Not a Valid PSD File");
        if (i == 2) {
            return inputStream;
        }
        skipBytes(inputStream, read4Bytes2);
        int read4Bytes3 = read4Bytes("LayerAndMaskDataLength", inputStream, "Not a Valid PSD File");
        if (i == 3) {
            return inputStream;
        }
        skipBytes(inputStream, read4Bytes3);
        read2Bytes("Compression", inputStream, "Not a Valid PSD File");
        if (i == 4) {
            return inputStream;
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("getInputStream: Unknown Section: ");
        stringBuffer.append(i);
        throw new ImageReadException(stringBuffer.toString());
    }

    private byte[] getData(ByteSource byteSource, int i) throws ImageReadException, IOException {
        InputStream inputStream;
        try {
            inputStream = byteSource.getInputStream();
            if (i == 0) {
                try {
                    byte[] readByteArray = readByteArray("Header", 26, inputStream, "Not a Valid PSD File");
                    try {
                        inputStream.close();
                    } catch (Exception e) {
                        Debug.debug((Throwable) e);
                    }
                    return readByteArray;
                } catch (Throwable th) {
                    th = th;
                    try {
                        inputStream.close();
                    } catch (Exception e2) {
                        Debug.debug((Throwable) e2);
                    }
                    throw th;
                }
            } else {
                skipBytes(inputStream, 26);
                int read4Bytes = read4Bytes("ColorModeDataLength", inputStream, "Not a Valid PSD File");
                if (i == 1) {
                    byte[] readByteArray2 = readByteArray("ColorModeData", read4Bytes, inputStream, "Not a Valid PSD File");
                    try {
                        inputStream.close();
                    } catch (Exception e3) {
                        Debug.debug((Throwable) e3);
                    }
                    return readByteArray2;
                }
                skipBytes(inputStream, read4Bytes);
                int read4Bytes2 = read4Bytes("ImageResourcesLength", inputStream, "Not a Valid PSD File");
                if (i == 2) {
                    byte[] readByteArray3 = readByteArray("ImageResources", read4Bytes2, inputStream, "Not a Valid PSD File");
                    try {
                        inputStream.close();
                    } catch (Exception e4) {
                        Debug.debug((Throwable) e4);
                    }
                    return readByteArray3;
                }
                skipBytes(inputStream, read4Bytes2);
                int read4Bytes3 = read4Bytes("LayerAndMaskDataLength", inputStream, "Not a Valid PSD File");
                if (i == 3) {
                    byte[] readByteArray4 = readByteArray("LayerAndMaskData", read4Bytes3, inputStream, "Not a Valid PSD File");
                    try {
                        inputStream.close();
                    } catch (Exception e5) {
                        Debug.debug((Throwable) e5);
                    }
                    return readByteArray4;
                }
                skipBytes(inputStream, read4Bytes3);
                read2Bytes("Compression", inputStream, "Not a Valid PSD File");
                try {
                    inputStream.close();
                } catch (Exception e6) {
                    Debug.debug((Throwable) e6);
                }
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("getInputStream: Unknown Section: ");
                stringBuffer.append(i);
                throw new ImageReadException(stringBuffer.toString());
            }
        } catch (Throwable th2) {
            th = th2;
            inputStream = null;
            inputStream.close();
            throw th;
        }
    }

    private ImageContents readImageContents(ByteSource byteSource) throws ImageReadException, IOException {
        InputStream inputStream;
        try {
            inputStream = byteSource.getInputStream();
            try {
                ImageContents readImageContents = readImageContents(inputStream);
                try {
                    inputStream.close();
                } catch (Exception e) {
                    Debug.debug((Throwable) e);
                }
                return readImageContents;
            } catch (Throwable th) {
                th = th;
                try {
                    inputStream.close();
                } catch (Exception e2) {
                    Debug.debug((Throwable) e2);
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            inputStream = null;
            inputStream.close();
            throw th;
        }
    }

    public byte[] getICCProfileBytes(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        ArrayList readImageResourceBlocks = readImageResourceBlocks(byteSource, new int[]{1039}, 1);
        if (readImageResourceBlocks == null || readImageResourceBlocks.size() < 1) {
            return null;
        }
        byte[] bArr = ((ImageResourceBlock) readImageResourceBlocks.get(0)).data;
        if (bArr == null || bArr.length < 1) {
            return null;
        }
        return bArr;
    }

    public Dimension getImageSize(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        PSDHeaderInfo readHeader = readHeader(byteSource);
        if (readHeader != null) {
            return new Dimension(readHeader.Columns, readHeader.Rows);
        }
        throw new ImageReadException("PSD: couldn't read header");
    }

    public ImageInfo getImageInfo(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        String str;
        ImageContents readImageContents = readImageContents(byteSource);
        if (readImageContents != null) {
            PSDHeaderInfo pSDHeaderInfo = readImageContents.header;
            if (pSDHeaderInfo != null) {
                int i = pSDHeaderInfo.Columns;
                int i2 = pSDHeaderInfo.Rows;
                ArrayList arrayList = new ArrayList();
                int channelsPerMode = pSDHeaderInfo.Depth * getChannelsPerMode(pSDHeaderInfo.Mode);
                int i3 = channelsPerMode < 0 ? 0 : channelsPerMode;
                ImageFormat imageFormat = ImageFormat.IMAGE_FORMAT_PSD;
                String str2 = ImageInfo.COMPRESSION_ALGORITHM_PSD;
                String str3 = "image/x-photoshop";
                double d = (double) i;
                int i4 = i3;
                double d2 = (double) 72;
                Double.isNaN(d);
                Double.isNaN(d2);
                float f = (float) (d / d2);
                double d3 = (double) i2;
                Double.isNaN(d3);
                Double.isNaN(d2);
                float f2 = (float) (d3 / d2);
                String str4 = "Psd";
                boolean z = pSDHeaderInfo.Mode == 2;
                switch (readImageContents.Compression) {
                    case 0:
                        str = ImageInfo.COMPRESSION_ALGORITHM_NONE;
                        break;
                    case 1:
                        str = ImageInfo.COMPRESSION_ALGORITHM_PSD;
                        break;
                    default:
                        str = ImageInfo.COMPRESSION_ALGORITHM_UNKNOWN;
                        break;
                }
                ImageInfo imageInfo = new ImageInfo(str4, i4, arrayList, imageFormat, str2, i2, str3, -1, 72, f2, 72, f, i, false, false, z, -2, str);
                return imageInfo;
            }
            throw new ImageReadException("PSD: Couldn't read Header");
        }
        throw new ImageReadException("PSD: Couldn't read blocks");
    }

    private ImageResourceBlock findImageResourceBlock(ArrayList arrayList, int i) throws ImageReadException, IOException {
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            ImageResourceBlock imageResourceBlock = (ImageResourceBlock) arrayList.get(i2);
            if (imageResourceBlock.f170id == i) {
                return imageResourceBlock;
            }
        }
        return null;
    }

    public boolean dumpImageFile(PrintWriter printWriter, ByteSource byteSource) throws ImageReadException, IOException {
        printWriter.println("gif.dumpImageFile");
        ImageInfo imageInfo = getImageInfo(byteSource);
        if (imageInfo == null) {
            return false;
        }
        imageInfo.toString(printWriter, "");
        ImageContents readImageContents = readImageContents(byteSource);
        readImageContents.dump(printWriter);
        readImageContents.header.dump(printWriter);
        ArrayList readImageResourceBlocks = readImageResourceBlocks(byteSource, (int[]) null, -1);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("blocks.size(): ");
        stringBuffer.append(readImageResourceBlocks.size());
        printWriter.println(stringBuffer.toString());
        for (int i = 0; i < readImageResourceBlocks.size(); i++) {
            ImageResourceBlock imageResourceBlock = (ImageResourceBlock) readImageResourceBlocks.get(i);
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2.append("\t");
            stringBuffer2.append(i);
            stringBuffer2.append(" (");
            stringBuffer2.append(Integer.toHexString(imageResourceBlock.f170id));
            stringBuffer2.append(", ");
            stringBuffer2.append("'");
            stringBuffer2.append(new String(imageResourceBlock.nameData));
            stringBuffer2.append("' (");
            stringBuffer2.append(imageResourceBlock.nameData.length);
            stringBuffer2.append("), ");
            stringBuffer2.append(" data: ");
            stringBuffer2.append(imageResourceBlock.data.length);
            stringBuffer2.append(" type: '");
            stringBuffer2.append(new PSDConstants().getDescription(imageResourceBlock.f170id));
            stringBuffer2.append("' ");
            stringBuffer2.append(")");
            printWriter.println(stringBuffer2.toString());
        }
        printWriter.println("");
        return true;
    }

    /* JADX WARNING: type inference failed for: r3v1, types: [java.io.InputStream] */
    /* JADX WARNING: type inference failed for: r3v3 */
    /* JADX WARNING: type inference failed for: r3v4 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.awt.image.BufferedImage getBufferedImage(org.apache.sanselan.common.byteSources.ByteSource r6, java.util.Map r7) throws org.apache.sanselan.ImageReadException, java.io.IOException {
        /*
            r5 = this;
            org.apache.sanselan.formats.psd.ImageContents r0 = r5.readImageContents(r6)
            if (r0 == 0) goto L_0x00ba
            org.apache.sanselan.formats.psd.PSDHeaderInfo r1 = r0.header
            if (r1 == 0) goto L_0x00b2
            r2 = -1
            r3 = 0
            r5.readImageResourceBlocks(r6, r3, r2)
            int r2 = r1.Columns
            int r1 = r1.Rows
            r4 = 0
            org.apache.sanselan.common.IBufferedImageFactory r7 = r5.getBufferedImageFactory(r7)
            java.awt.image.BufferedImage r7 = r7.getColorBufferedImage(r2, r1, r4)
            org.apache.sanselan.formats.psd.PSDHeaderInfo r1 = r0.header
            int r1 = r1.Mode
            switch(r1) {
                case 0: goto L_0x0062;
                case 1: goto L_0x005c;
                case 2: goto L_0x0050;
                case 3: goto L_0x004a;
                case 4: goto L_0x0044;
                case 5: goto L_0x0023;
                case 6: goto L_0x0023;
                case 7: goto L_0x0023;
                case 8: goto L_0x005c;
                case 9: goto L_0x003e;
                default: goto L_0x0023;
            }
        L_0x0023:
            org.apache.sanselan.ImageReadException r6 = new org.apache.sanselan.ImageReadException
            java.lang.StringBuffer r7 = new java.lang.StringBuffer
            r7.<init>()
            java.lang.String r1 = "Unknown Mode: "
            r7.append(r1)
            org.apache.sanselan.formats.psd.PSDHeaderInfo r0 = r0.header
            int r0 = r0.Mode
            r7.append(r0)
            java.lang.String r7 = r7.toString()
            r6.<init>(r7)
            throw r6
        L_0x003e:
            org.apache.sanselan.formats.psd.dataparsers.DataParserLab r1 = new org.apache.sanselan.formats.psd.dataparsers.DataParserLab
            r1.<init>()
            goto L_0x0067
        L_0x0044:
            org.apache.sanselan.formats.psd.dataparsers.DataParserCMYK r1 = new org.apache.sanselan.formats.psd.dataparsers.DataParserCMYK
            r1.<init>()
            goto L_0x0067
        L_0x004a:
            org.apache.sanselan.formats.psd.dataparsers.DataParserRGB r1 = new org.apache.sanselan.formats.psd.dataparsers.DataParserRGB
            r1.<init>()
            goto L_0x0067
        L_0x0050:
            r1 = 1
            byte[] r1 = r5.getData(r6, r1)
            org.apache.sanselan.formats.psd.dataparsers.DataParserIndexed r2 = new org.apache.sanselan.formats.psd.dataparsers.DataParserIndexed
            r2.<init>(r1)
            r1 = r2
            goto L_0x0067
        L_0x005c:
            org.apache.sanselan.formats.psd.dataparsers.DataParserGrayscale r1 = new org.apache.sanselan.formats.psd.dataparsers.DataParserGrayscale
            r1.<init>()
            goto L_0x0067
        L_0x0062:
            org.apache.sanselan.formats.psd.dataparsers.DataParserBitmap r1 = new org.apache.sanselan.formats.psd.dataparsers.DataParserBitmap
            r1.<init>()
        L_0x0067:
            int r2 = r0.Compression
            switch(r2) {
                case 0: goto L_0x008b;
                case 1: goto L_0x0085;
                default: goto L_0x006c;
            }
        L_0x006c:
            org.apache.sanselan.ImageReadException r6 = new org.apache.sanselan.ImageReadException
            java.lang.StringBuffer r7 = new java.lang.StringBuffer
            r7.<init>()
            java.lang.String r1 = "Unknown Compression: "
            r7.append(r1)
            int r0 = r0.Compression
            r7.append(r0)
            java.lang.String r7 = r7.toString()
            r6.<init>(r7)
            throw r6
        L_0x0085:
            org.apache.sanselan.formats.psd.datareaders.CompressedDataReader r2 = new org.apache.sanselan.formats.psd.datareaders.CompressedDataReader
            r2.<init>(r1)
            goto L_0x0090
        L_0x008b:
            org.apache.sanselan.formats.psd.datareaders.UncompressedDataReader r2 = new org.apache.sanselan.formats.psd.datareaders.UncompressedDataReader
            r2.<init>(r1)
        L_0x0090:
            r1 = 4
            java.io.InputStream r3 = r5.getInputStream(r6, r1)     // Catch:{ all -> 0x00a6 }
            r2.readData(r3, r7, r0, r5)     // Catch:{ all -> 0x00a6 }
            r2.dump()     // Catch:{ all -> 0x00a6 }
            if (r3 == 0) goto L_0x00a5
            r3.close()     // Catch:{ Exception -> 0x00a1 }
            goto L_0x00a5
        L_0x00a1:
            r6 = move-exception
            org.apache.sanselan.util.Debug.debug(r6)
        L_0x00a5:
            return r7
        L_0x00a6:
            r6 = move-exception
            if (r3 == 0) goto L_0x00b1
            r3.close()     // Catch:{ Exception -> 0x00ad }
            goto L_0x00b1
        L_0x00ad:
            r7 = move-exception
            org.apache.sanselan.util.Debug.debug(r7)
        L_0x00b1:
            throw r6
        L_0x00b2:
            org.apache.sanselan.ImageReadException r6 = new org.apache.sanselan.ImageReadException
            java.lang.String r7 = "PSD: Couldn't read Header"
            r6.<init>(r7)
            throw r6
        L_0x00ba:
            org.apache.sanselan.ImageReadException r6 = new org.apache.sanselan.ImageReadException
            java.lang.String r7 = "PSD: Couldn't read blocks"
            r6.<init>(r7)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.sanselan.formats.psd.PsdImageParser.getBufferedImage(org.apache.sanselan.common.byteSources.ByteSource, java.util.Map):java.awt.image.BufferedImage");
    }

    public String getXmpXml(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        ImageContents readImageContents = readImageContents(byteSource);
        if (readImageContents == null) {
            throw new ImageReadException("PSD: Couldn't read blocks");
        } else if (readImageContents.header != null) {
            ArrayList readImageResourceBlocks = readImageResourceBlocks(byteSource, new int[]{1060}, -1);
            if (readImageResourceBlocks == null || readImageResourceBlocks.size() < 1) {
                return null;
            }
            ArrayList arrayList = new ArrayList();
            arrayList.addAll(readImageResourceBlocks);
            if (arrayList.size() < 1) {
                return null;
            }
            if (arrayList.size() <= 1) {
                ImageResourceBlock imageResourceBlock = (ImageResourceBlock) arrayList.get(0);
                try {
                    return new String(imageResourceBlock.data, 0, imageResourceBlock.data.length, "utf-8");
                } catch (UnsupportedEncodingException unused) {
                    throw new ImageReadException("Invalid JPEG XMP Segment.");
                }
            } else {
                throw new ImageReadException("PSD contains more than one XMP block.");
            }
        } else {
            throw new ImageReadException("PSD: Couldn't read Header");
        }
    }
}

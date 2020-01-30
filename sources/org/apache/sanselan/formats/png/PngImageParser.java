package org.apache.sanselan.formats.png;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;
import org.apache.sanselan.ImageFormat;
import org.apache.sanselan.ImageInfo;
import org.apache.sanselan.ImageParser;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.common.ImageMetadata;
import org.apache.sanselan.common.byteSources.ByteSource;
import org.apache.sanselan.formats.png.chunks.PNGChunk;
import org.apache.sanselan.formats.png.chunks.PNGChunkIDAT;
import org.apache.sanselan.formats.png.chunks.PNGChunkIHDR;
import org.apache.sanselan.formats.png.chunks.PNGChunkPLTE;
import org.apache.sanselan.formats.png.chunks.PNGChunkgAMA;
import org.apache.sanselan.formats.png.chunks.PNGChunkiCCP;
import org.apache.sanselan.formats.png.chunks.PNGChunkiTXt;
import org.apache.sanselan.formats.png.chunks.PNGChunkpHYs;
import org.apache.sanselan.formats.png.chunks.PNGChunktEXt;
import org.apache.sanselan.formats.png.chunks.PNGChunkzTXt;
import org.apache.sanselan.formats.png.chunks.PNGTextChunk;
import org.apache.sanselan.formats.transparencyfilters.TransparencyFilter;
import org.apache.sanselan.formats.transparencyfilters.TransparencyFilterGrayscale;
import org.apache.sanselan.formats.transparencyfilters.TransparencyFilterIndexedColor;
import org.apache.sanselan.formats.transparencyfilters.TransparencyFilterTrueColor;
import org.apache.sanselan.util.Debug;

public class PngImageParser extends ImageParser implements PngConstants {
    private static final String[] ACCEPTED_EXTENSIONS = {DEFAULT_EXTENSION};
    private static final String DEFAULT_EXTENSION = ".png";

    private String getColorTypeDescription(int i) {
        if (i == 0) {
            return "grayscale";
        }
        if (i == 6) {
            return "RGB w/ alpha";
        }
        switch (i) {
            case 2:
                return "rgb";
            case 3:
                return "indexed rgb";
            case 4:
                return "grayscale w/ alpha";
            default:
                return "Unknown Color Type";
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

    public String getName() {
        return "Png-Custom";
    }

    /* access modifiers changed from: protected */
    public String[] getAcceptedExtensions() {
        return ACCEPTED_EXTENSIONS;
    }

    /* access modifiers changed from: protected */
    public ImageFormat[] getAcceptedTypes() {
        return new ImageFormat[]{ImageFormat.IMAGE_FORMAT_PNG};
    }

    private boolean keepChunk(int i, int[] iArr) {
        if (iArr == null) {
            return true;
        }
        for (int i2 : iArr) {
            if (i2 == i) {
                return true;
            }
        }
        return false;
    }

    private ArrayList readChunks(InputStream inputStream, int[] iArr, boolean z) throws ImageReadException, IOException {
        int read4Bytes;
        ArrayList arrayList = new ArrayList();
        do {
            if (this.debug) {
                System.out.println("");
            }
            int read4Bytes2 = read4Bytes("Length", inputStream, "Not a Valid PNG File");
            read4Bytes = read4Bytes("ChunkType", inputStream, "Not a Valid PNG File");
            if (this.debug) {
                printCharQuad("ChunkType", read4Bytes);
                debugNumber("Length", read4Bytes2, 4);
            }
            boolean keepChunk = keepChunk(read4Bytes, iArr);
            byte[] bArr = null;
            if (keepChunk) {
                bArr = readByteArray("Chunk Data", read4Bytes2, inputStream, "Not a Valid PNG File: Couldn't read Chunk Data.");
            } else {
                skipBytes(inputStream, read4Bytes2, "Not a Valid PNG File");
            }
            if (this.debug && bArr != null) {
                debugNumber("bytes", bArr.length, 4);
            }
            int read4Bytes3 = read4Bytes("CRC", inputStream, "Not a Valid PNG File");
            if (keepChunk) {
                if (read4Bytes == iCCP) {
                    arrayList.add(new PNGChunkiCCP(read4Bytes2, read4Bytes, read4Bytes3, bArr));
                } else if (read4Bytes == tEXt) {
                    arrayList.add(new PNGChunktEXt(read4Bytes2, read4Bytes, read4Bytes3, bArr));
                } else if (read4Bytes == zTXt) {
                    arrayList.add(new PNGChunkzTXt(read4Bytes2, read4Bytes, read4Bytes3, bArr));
                } else if (read4Bytes == IHDR) {
                    arrayList.add(new PNGChunkIHDR(read4Bytes2, read4Bytes, read4Bytes3, bArr));
                } else if (read4Bytes == PLTE) {
                    arrayList.add(new PNGChunkPLTE(read4Bytes2, read4Bytes, read4Bytes3, bArr));
                } else if (read4Bytes == pHYs) {
                    arrayList.add(new PNGChunkpHYs(read4Bytes2, read4Bytes, read4Bytes3, bArr));
                } else if (read4Bytes == IDAT) {
                    arrayList.add(new PNGChunkIDAT(read4Bytes2, read4Bytes, read4Bytes3, bArr));
                } else if (read4Bytes == gAMA) {
                    arrayList.add(new PNGChunkgAMA(read4Bytes2, read4Bytes, read4Bytes3, bArr));
                } else if (read4Bytes == iTXt) {
                    arrayList.add(new PNGChunkiTXt(read4Bytes2, read4Bytes, read4Bytes3, bArr));
                } else {
                    arrayList.add(new PNGChunk(read4Bytes2, read4Bytes, read4Bytes3, bArr));
                }
                if (z) {
                    return arrayList;
                }
            }
        } while (read4Bytes != IEND);
        return arrayList;
    }

    private void readSignature(InputStream inputStream) throws ImageReadException, IOException {
        readAndVerifyBytes(inputStream, PNG_Signature, "Not a Valid PNG Segment: Incorrect Signature");
    }

    private ArrayList readChunks(ByteSource byteSource, int[] iArr, boolean z) throws ImageReadException, IOException {
        InputStream inputStream;
        try {
            inputStream = byteSource.getInputStream();
            try {
                readSignature(inputStream);
                ArrayList readChunks = readChunks(inputStream, iArr, z);
                try {
                    inputStream.close();
                } catch (Exception e) {
                    Debug.debug((Throwable) e);
                }
                return readChunks;
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
        ArrayList readChunks = readChunks(byteSource, new int[]{iCCP}, true);
        if (readChunks == null || readChunks.size() < 1) {
            return null;
        }
        if (readChunks.size() <= 1) {
            return ((PNGChunkiCCP) readChunks.get(0)).UncompressedProfile;
        }
        throw new ImageReadException("PNG contains more than one ICC Profile ");
    }

    public Dimension getImageSize(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        ArrayList readChunks = readChunks(byteSource, new int[]{IHDR}, true);
        if (readChunks == null || readChunks.size() < 1) {
            throw new ImageReadException("Png: No chunks");
        } else if (readChunks.size() <= 1) {
            PNGChunkIHDR pNGChunkIHDR = (PNGChunkIHDR) readChunks.get(0);
            return new Dimension(pNGChunkIHDR.width, pNGChunkIHDR.height);
        } else {
            throw new ImageReadException("PNG contains more than one Header");
        }
    }

    public IImageMetadata getMetadata(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        ArrayList readChunks = readChunks(byteSource, new int[]{tEXt, zTXt}, true);
        if (readChunks == null || readChunks.size() < 1) {
            return null;
        }
        ImageMetadata imageMetadata = new ImageMetadata();
        for (int i = 0; i < readChunks.size(); i++) {
            PNGTextChunk pNGTextChunk = (PNGTextChunk) readChunks.get(i);
            imageMetadata.add(pNGTextChunk.getKeyword(), pNGTextChunk.getText());
        }
        return imageMetadata;
    }

    private boolean isGrayscale(int i) throws ImageReadException {
        if (i == 0) {
            return true;
        }
        if (i == 6) {
            return false;
        }
        switch (i) {
            case 2:
                return false;
            case 3:
                return false;
            case 4:
                return true;
            default:
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("PNG: unknown color type: ");
                stringBuffer.append(i);
                throw new ImageReadException(stringBuffer.toString());
        }
    }

    private int samplesPerPixel(int i) throws ImageReadException {
        if (i == 0) {
            return 1;
        }
        if (i == 6) {
            return 4;
        }
        switch (i) {
            case 2:
                return 3;
            case 3:
                return 1;
            case 4:
                return 2;
            default:
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("PNG: unknown color type: ");
                stringBuffer.append(i);
                throw new ImageReadException(stringBuffer.toString());
        }
    }

    private ArrayList filterChunks(ArrayList arrayList, int i) {
        ArrayList arrayList2 = new ArrayList();
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            PNGChunk pNGChunk = (PNGChunk) arrayList.get(i2);
            if (pNGChunk.chunkType == i) {
                arrayList2.add(pNGChunk);
            }
        }
        return arrayList2;
    }

    private boolean hasAlphaChannel(int i) throws ImageReadException, IOException {
        if (i != 0) {
            if (i != 6) {
                switch (i) {
                    case 2:
                    case 3:
                        break;
                    case 4:
                        break;
                    default:
                        StringBuffer stringBuffer = new StringBuffer();
                        stringBuffer.append("PNG: unknown color type: ");
                        stringBuffer.append(i);
                        throw new ImageReadException(stringBuffer.toString());
                }
            }
            return true;
        }
        return false;
    }

    private TransparencyFilter getTransparencyFilter(int i, PNGChunk pNGChunk) throws ImageReadException, IOException {
        if (i == 0) {
            return new TransparencyFilterGrayscale(pNGChunk.bytes);
        }
        switch (i) {
            case 2:
                return new TransparencyFilterTrueColor(pNGChunk.bytes);
            case 3:
                return new TransparencyFilterIndexedColor(pNGChunk.bytes);
            default:
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("Simple Transparency not compatible with ColorType: ");
                stringBuffer.append(i);
                throw new ImageReadException(stringBuffer.toString());
        }
    }

    public ImageInfo getImageInfo(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        boolean z;
        PNGChunkIHDR pNGChunkIHDR;
        float f;
        int i;
        float f2;
        boolean z2;
        PNGChunkIHDR pNGChunkIHDR2;
        int i2;
        ArrayList readChunks = readChunks(byteSource, new int[]{IHDR, pHYs, tEXt, zTXt, tRNS, PLTE, iTXt}, false);
        if (readChunks == null || readChunks.size() < 1) {
            throw new ImageReadException("PNG: no chunks");
        }
        ArrayList filterChunks = filterChunks(readChunks, IHDR);
        if (filterChunks.size() == 1) {
            PNGChunkIHDR pNGChunkIHDR3 = (PNGChunkIHDR) filterChunks.get(0);
            if (filterChunks(readChunks, tRNS).size() > 0) {
                PNGChunk pNGChunk = (PNGChunk) filterChunks.get(0);
                z = true;
            } else {
                hasAlphaChannel(pNGChunkIHDR3.colorType);
                z = false;
            }
            PNGChunkpHYs pNGChunkpHYs = null;
            ArrayList filterChunks2 = filterChunks(readChunks, pHYs);
            if (filterChunks2.size() <= 1) {
                if (filterChunks2.size() == 1) {
                    pNGChunkpHYs = (PNGChunkpHYs) filterChunks2.get(0);
                }
                ArrayList filterChunks3 = filterChunks(readChunks, tEXt);
                ArrayList filterChunks4 = filterChunks(readChunks, zTXt);
                ArrayList filterChunks5 = filterChunks(readChunks, iTXt);
                ArrayList arrayList = new ArrayList();
                ArrayList arrayList2 = new ArrayList();
                for (int i3 = 0; i3 < filterChunks3.size(); i3++) {
                    PNGChunktEXt pNGChunktEXt = (PNGChunktEXt) filterChunks3.get(i3);
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append(pNGChunktEXt.keyword);
                    stringBuffer.append(": ");
                    stringBuffer.append(pNGChunktEXt.text);
                    arrayList.add(stringBuffer.toString());
                    arrayList2.add(pNGChunktEXt.getContents());
                }
                for (int i4 = 0; i4 < filterChunks4.size(); i4++) {
                    PNGChunkzTXt pNGChunkzTXt = (PNGChunkzTXt) filterChunks4.get(i4);
                    StringBuffer stringBuffer2 = new StringBuffer();
                    stringBuffer2.append(pNGChunkzTXt.keyword);
                    stringBuffer2.append(": ");
                    stringBuffer2.append(pNGChunkzTXt.text);
                    arrayList.add(stringBuffer2.toString());
                    arrayList2.add(pNGChunkzTXt.getContents());
                }
                for (int i5 = 0; i5 < filterChunks5.size(); i5++) {
                    PNGChunkiTXt pNGChunkiTXt = (PNGChunkiTXt) filterChunks5.get(i5);
                    StringBuffer stringBuffer3 = new StringBuffer();
                    stringBuffer3.append(pNGChunkiTXt.keyword);
                    stringBuffer3.append(": ");
                    stringBuffer3.append(pNGChunkiTXt.text);
                    arrayList.add(stringBuffer3.toString());
                    arrayList2.add(pNGChunkiTXt.getContents());
                }
                int samplesPerPixel = pNGChunkIHDR3.bitDepth * samplesPerPixel(pNGChunkIHDR3.colorType);
                ImageFormat imageFormat = ImageFormat.IMAGE_FORMAT_PNG;
                String str = "PNG Portable Network Graphics";
                int i6 = pNGChunkIHDR3.height;
                String str2 = "image/png";
                int i7 = pNGChunkIHDR3.width;
                boolean z3 = pNGChunkIHDR3.interlaceMethod != 0;
                int i8 = -1;
                if (pNGChunkpHYs == null || pNGChunkpHYs.UnitSpecifier != 1) {
                    pNGChunkIHDR = pNGChunkIHDR3;
                    f2 = -1.0f;
                    i = -1;
                    f = -1.0f;
                } else {
                    pNGChunkIHDR = pNGChunkIHDR3;
                    double d = (double) pNGChunkpHYs.PixelsPerUnitXAxis;
                    Double.isNaN(d);
                    int round = (int) Math.round(d * 0.0254d);
                    double d2 = (double) i7;
                    int i9 = round;
                    double d3 = (double) pNGChunkpHYs.PixelsPerUnitXAxis;
                    Double.isNaN(d2);
                    Double.isNaN(d3);
                    float f3 = (float) (d2 * d3 * 0.0254d);
                    double d4 = (double) pNGChunkpHYs.PixelsPerUnitYAxis;
                    Double.isNaN(d4);
                    double d5 = (double) i6;
                    float f4 = f3;
                    int round2 = (int) Math.round(d4 * 0.0254d);
                    double d6 = (double) pNGChunkpHYs.PixelsPerUnitYAxis;
                    Double.isNaN(d5);
                    Double.isNaN(d6);
                    f2 = (float) (d5 * d6 * 0.0254d);
                    f = f4;
                    i = i9;
                    i8 = round2;
                }
                String str3 = "Png";
                if (filterChunks(readChunks, PLTE).size() > 1) {
                    pNGChunkIHDR2 = pNGChunkIHDR;
                    z2 = true;
                } else {
                    pNGChunkIHDR2 = pNGChunkIHDR;
                    z2 = false;
                }
                int i10 = pNGChunkIHDR2.colorType;
                if (i10 != 0) {
                    if (i10 != 6) {
                        switch (i10) {
                            case 2:
                            case 3:
                                break;
                            case 4:
                                break;
                            default:
                                StringBuffer stringBuffer4 = new StringBuffer();
                                stringBuffer4.append("Png: Unknown ColorType: ");
                                stringBuffer4.append(pNGChunkIHDR2.colorType);
                                throw new ImageReadException(stringBuffer4.toString());
                        }
                    }
                    i2 = 2;
                    int i11 = i7;
                    PngImageInfo pngImageInfo = new PngImageInfo(str3, samplesPerPixel, arrayList, imageFormat, str, i6, str2, 1, i8, f2, i, f, i11, z3, z, z2, i2, ImageInfo.COMPRESSION_ALGORITHM_PNG_FILTER, arrayList2);
                    return pngImageInfo;
                }
                i2 = 1;
                int i112 = i7;
                PngImageInfo pngImageInfo2 = new PngImageInfo(str3, samplesPerPixel, arrayList, imageFormat, str, i6, str2, 1, i8, f2, i, f, i112, z3, z, z2, i2, ImageInfo.COMPRESSION_ALGORITHM_PNG_FILTER, arrayList2);
                return pngImageInfo2;
            }
            StringBuffer stringBuffer5 = new StringBuffer();
            stringBuffer5.append("PNG contains more than one pHYs: ");
            stringBuffer5.append(filterChunks2.size());
            throw new ImageReadException(stringBuffer5.toString());
        }
        throw new ImageReadException("PNG contains more than one Header");
    }

    /* JADX WARNING: type inference failed for: r3v6, types: [org.apache.sanselan.formats.png.ScanExpediter] */
    /* JADX WARNING: type inference failed for: r11v10, types: [org.apache.sanselan.formats.png.ScanExpediterInterlaced] */
    /* JADX WARNING: type inference failed for: r11v11, types: [org.apache.sanselan.formats.png.ScanExpediterSimple] */
    /* JADX WARNING: type inference failed for: r11v20, types: [org.apache.sanselan.formats.png.ScanExpediterInterlaced] */
    /* JADX WARNING: type inference failed for: r11v21, types: [org.apache.sanselan.formats.png.ScanExpediterSimple] */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r11v20, types: [org.apache.sanselan.formats.png.ScanExpediterInterlaced]
      assigns: [org.apache.sanselan.formats.png.ScanExpediterInterlaced, org.apache.sanselan.formats.png.ScanExpediterSimple]
      uses: [org.apache.sanselan.formats.png.ScanExpediterInterlaced, org.apache.sanselan.formats.png.ScanExpediter, org.apache.sanselan.formats.png.ScanExpediterSimple]
      mth insns count: 209
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x0152  */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x01ed  */
    /* JADX WARNING: Unknown variable types count: 3 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.awt.image.BufferedImage getBufferedImage(org.apache.sanselan.common.byteSources.ByteSource r23, java.util.Map r24) throws org.apache.sanselan.ImageReadException, java.io.IOException {
        /*
            r22 = this;
            r0 = r22
            r1 = r24
            java.lang.String r2 = "VERBOSE"
            r3 = 0
            org.apache.sanselan.util.ParamMap.getParamBoolean(r1, r2, r3)
            java.lang.String r2 = "VERBOSE"
            boolean r2 = r1.containsKey(r2)
            if (r2 == 0) goto L_0x0017
            java.lang.String r2 = "VERBOSE"
            r1.remove(r2)
        L_0x0017:
            r2 = 7
            int[] r2 = new int[r2]
            int r4 = IHDR
            r2[r3] = r4
            int r4 = PLTE
            r5 = 1
            r2[r5] = r4
            r4 = 2
            int r6 = IDAT
            r2[r4] = r6
            r4 = 3
            int r6 = tRNS
            r2[r4] = r6
            int r4 = iCCP
            r6 = 4
            r2[r6] = r4
            r4 = 5
            int r7 = gAMA
            r2[r4] = r7
            int r4 = sRGB
            r7 = 6
            r2[r7] = r4
            r4 = r23
            java.util.ArrayList r2 = r0.readChunks(r4, r2, r3)
            if (r2 == 0) goto L_0x0236
            int r4 = r2.size()
            if (r4 < r5) goto L_0x0236
            int r4 = IHDR
            java.util.ArrayList r4 = r0.filterChunks(r2, r4)
            int r8 = r4.size()
            if (r8 != r5) goto L_0x022e
            java.lang.Object r4 = r4.get(r3)
            org.apache.sanselan.formats.png.chunks.PNGChunkIHDR r4 = (org.apache.sanselan.formats.png.chunks.PNGChunkIHDR) r4
            int r8 = PLTE
            java.util.ArrayList r8 = r0.filterChunks(r2, r8)
            int r9 = r8.size()
            if (r9 > r5) goto L_0x0226
            int r9 = r8.size()
            r10 = 0
            if (r9 != r5) goto L_0x0076
            java.lang.Object r8 = r8.get(r3)
            org.apache.sanselan.formats.png.chunks.PNGChunkPLTE r8 = (org.apache.sanselan.formats.png.chunks.PNGChunkPLTE) r8
            goto L_0x0077
        L_0x0076:
            r8 = r10
        L_0x0077:
            int r9 = IDAT
            java.util.ArrayList r9 = r0.filterChunks(r2, r9)
            int r11 = r9.size()
            if (r11 < r5) goto L_0x021e
            java.io.ByteArrayOutputStream r11 = new java.io.ByteArrayOutputStream
            r11.<init>()
            r12 = 0
        L_0x0089:
            int r13 = r9.size()
            if (r12 >= r13) goto L_0x009d
            java.lang.Object r13 = r9.get(r12)
            org.apache.sanselan.formats.png.chunks.PNGChunkIDAT r13 = (org.apache.sanselan.formats.png.chunks.PNGChunkIDAT) r13
            byte[] r13 = r13.bytes
            r11.write(r13)
            int r12 = r12 + 1
            goto L_0x0089
        L_0x009d:
            byte[] r9 = r11.toByteArray()
            int r11 = tRNS
            java.util.ArrayList r11 = r0.filterChunks(r2, r11)
            int r12 = r11.size()
            if (r12 <= 0) goto L_0x00bc
            java.lang.Object r11 = r11.get(r3)
            org.apache.sanselan.formats.png.chunks.PNGChunk r11 = (org.apache.sanselan.formats.png.chunks.PNGChunk) r11
            int r12 = r4.colorType
            org.apache.sanselan.formats.transparencyfilters.TransparencyFilter r11 = r0.getTransparencyFilter(r12, r11)
            r21 = r11
            goto L_0x00be
        L_0x00bc:
            r21 = r10
        L_0x00be:
            int r11 = sRGB
            java.util.ArrayList r11 = r0.filterChunks(r2, r11)
            int r12 = gAMA
            java.util.ArrayList r12 = r0.filterChunks(r2, r12)
            int r13 = iCCP
            java.util.ArrayList r2 = r0.filterChunks(r2, r13)
            int r13 = r11.size()
            if (r13 > r5) goto L_0x0216
            int r13 = r12.size()
            if (r13 > r5) goto L_0x020e
            int r13 = r2.size()
            if (r13 > r5) goto L_0x0206
            int r11 = r11.size()
            if (r11 != r5) goto L_0x00f4
            boolean r2 = r0.debug
            if (r2 == 0) goto L_0x0144
            java.io.PrintStream r2 = java.lang.System.out
            java.lang.String r11 = "sRGB, no color management neccesary."
            r2.println(r11)
            goto L_0x0144
        L_0x00f4:
            int r11 = r2.size()
            if (r11 != r5) goto L_0x0115
            boolean r11 = r0.debug
            if (r11 == 0) goto L_0x0105
            java.io.PrintStream r11 = java.lang.System.out
            java.lang.String r12 = "iCCP."
            r11.println(r12)
        L_0x0105:
            java.lang.Object r2 = r2.get(r3)
            org.apache.sanselan.formats.png.chunks.PNGChunkiCCP r2 = (org.apache.sanselan.formats.png.chunks.PNGChunkiCCP) r2
            byte[] r2 = r2.UncompressedProfile
            java.awt.color.ICC_Profile r2 = java.awt.color.ICC_Profile.getInstance(r2)
            r20 = r10
            r10 = r2
            goto L_0x0146
        L_0x0115:
            int r2 = r12.size()
            if (r2 != r5) goto L_0x0144
            java.lang.Object r2 = r12.get(r3)
            org.apache.sanselan.formats.png.chunks.PNGChunkgAMA r2 = (org.apache.sanselan.formats.png.chunks.PNGChunkgAMA) r2
            double r11 = r2.getGamma()
            r13 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            double r15 = r13 - r11
            double r15 = java.lang.Math.abs(r15)
            r17 = 4602678819172646912(0x3fe0000000000000, double:0.5)
            int r2 = (r15 > r17 ? 1 : (r15 == r17 ? 0 : -1))
            if (r2 < 0) goto L_0x0139
            org.apache.sanselan.formats.png.GammaCorrection r2 = new org.apache.sanselan.formats.png.GammaCorrection
            r2.<init>(r11, r13)
            goto L_0x013a
        L_0x0139:
            r2 = r10
        L_0x013a:
            if (r2 == 0) goto L_0x0141
            if (r8 == 0) goto L_0x0141
            r8.correct(r2)
        L_0x0141:
            r20 = r2
            goto L_0x0146
        L_0x0144:
            r20 = r10
        L_0x0146:
            int r12 = r4.width
            int r13 = r4.height
            int r2 = r4.colorType
            int r15 = r4.bitDepth
            int r11 = r4.filterMethod
            if (r11 != 0) goto L_0x01ed
            int r11 = r4.colorType
            int r11 = r0.samplesPerPixel(r11)
            int r14 = r4.colorType
            boolean r14 = r0.isGrayscale(r14)
            int r18 = r15 * r11
            if (r2 == r6) goto L_0x0164
            if (r2 != r7) goto L_0x0165
        L_0x0164:
            r3 = 1
        L_0x0165:
            if (r14 == 0) goto L_0x0170
            org.apache.sanselan.common.IBufferedImageFactory r1 = r0.getBufferedImageFactory(r1)
            java.awt.image.BufferedImage r1 = r1.getGrayscaleBufferedImage(r12, r13, r3)
            goto L_0x0178
        L_0x0170:
            org.apache.sanselan.common.IBufferedImageFactory r1 = r0.getBufferedImageFactory(r1)
            java.awt.image.BufferedImage r1 = r1.getColorBufferedImage(r12, r13, r3)
        L_0x0178:
            java.io.ByteArrayInputStream r3 = new java.io.ByteArrayInputStream
            r3.<init>(r9)
            java.util.zip.InflaterInputStream r14 = new java.util.zip.InflaterInputStream
            r14.<init>(r3)
            int r3 = r4.interlaceMethod
            if (r3 != 0) goto L_0x0195
            org.apache.sanselan.formats.png.ScanExpediterSimple r3 = new org.apache.sanselan.formats.png.ScanExpediterSimple
            r11 = r3
            r6 = r15
            r15 = r1
            r16 = r2
            r17 = r6
            r19 = r8
            r11.<init>(r12, r13, r14, r15, r16, r17, r18, r19, r20, r21)
            goto L_0x01a7
        L_0x0195:
            r6 = r15
            int r3 = r4.interlaceMethod
            if (r3 != r5) goto L_0x01d4
            org.apache.sanselan.formats.png.ScanExpediterInterlaced r3 = new org.apache.sanselan.formats.png.ScanExpediterInterlaced
            r11 = r3
            r15 = r1
            r16 = r2
            r17 = r6
            r19 = r8
            r11.<init>(r12, r13, r14, r15, r16, r17, r18, r19, r20, r21)
        L_0x01a7:
            r3.drive()
            if (r10 == 0) goto L_0x01d3
            org.apache.sanselan.icc.IccProfileParser r2 = new org.apache.sanselan.icc.IccProfileParser
            r2.<init>()
            java.lang.Boolean r2 = r2.issRGB(r10)
            if (r2 == 0) goto L_0x01bd
            boolean r2 = r2.booleanValue()
            if (r2 != 0) goto L_0x01d3
        L_0x01bd:
            java.awt.color.ICC_ColorSpace r2 = new java.awt.color.ICC_ColorSpace
            r2.<init>(r10)
            java.awt.image.ColorModel r3 = java.awt.image.ColorModel.getRGBdefault()
            java.awt.color.ColorSpace r3 = r3.getColorSpace()
            org.apache.sanselan.ColorTools r4 = new org.apache.sanselan.ColorTools
            r4.<init>()
            java.awt.image.BufferedImage r1 = r4.convertBetweenColorSpaces(r1, r2, r3)
        L_0x01d3:
            return r1
        L_0x01d4:
            org.apache.sanselan.ImageReadException r1 = new org.apache.sanselan.ImageReadException
            java.lang.StringBuffer r2 = new java.lang.StringBuffer
            r2.<init>()
            java.lang.String r3 = "Unknown InterlaceMethod: "
            r2.append(r3)
            int r3 = r4.interlaceMethod
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            r1.<init>(r2)
            throw r1
        L_0x01ed:
            org.apache.sanselan.ImageReadException r1 = new org.apache.sanselan.ImageReadException
            java.lang.StringBuffer r2 = new java.lang.StringBuffer
            r2.<init>()
            java.lang.String r3 = "PNG: unknown FilterMethod: "
            r2.append(r3)
            int r3 = r4.filterMethod
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            r1.<init>(r2)
            throw r1
        L_0x0206:
            org.apache.sanselan.ImageReadException r1 = new org.apache.sanselan.ImageReadException
            java.lang.String r2 = "PNG: unexpected iCCP chunk"
            r1.<init>(r2)
            throw r1
        L_0x020e:
            org.apache.sanselan.ImageReadException r1 = new org.apache.sanselan.ImageReadException
            java.lang.String r2 = "PNG: unexpected gAMA chunk"
            r1.<init>(r2)
            throw r1
        L_0x0216:
            org.apache.sanselan.ImageReadException r1 = new org.apache.sanselan.ImageReadException
            java.lang.String r2 = "PNG: unexpected sRGB chunk"
            r1.<init>(r2)
            throw r1
        L_0x021e:
            org.apache.sanselan.ImageReadException r1 = new org.apache.sanselan.ImageReadException
            java.lang.String r2 = "PNG missing image data"
            r1.<init>(r2)
            throw r1
        L_0x0226:
            org.apache.sanselan.ImageReadException r1 = new org.apache.sanselan.ImageReadException
            java.lang.String r2 = "PNG contains more than one Palette"
            r1.<init>(r2)
            throw r1
        L_0x022e:
            org.apache.sanselan.ImageReadException r1 = new org.apache.sanselan.ImageReadException
            java.lang.String r2 = "PNG contains more than one Header"
            r1.<init>(r2)
            throw r1
        L_0x0236:
            org.apache.sanselan.ImageReadException r1 = new org.apache.sanselan.ImageReadException
            java.lang.String r2 = "PNG: no chunks"
            r1.<init>(r2)
            throw r1
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.sanselan.formats.png.PngImageParser.getBufferedImage(org.apache.sanselan.common.byteSources.ByteSource, java.util.Map):java.awt.image.BufferedImage");
    }

    public boolean dumpImageFile(PrintWriter printWriter, ByteSource byteSource) throws ImageReadException, IOException {
        ImageInfo imageInfo = getImageInfo(byteSource);
        if (imageInfo == null) {
            return false;
        }
        imageInfo.toString(printWriter, "");
        ArrayList readChunks = readChunks(byteSource, (int[]) null, false);
        ArrayList filterChunks = filterChunks(readChunks, IHDR);
        if (filterChunks.size() != 1) {
            if (this.debug) {
                System.out.println("PNG contains more than one Header");
            }
            return false;
        }
        PNGChunkIHDR pNGChunkIHDR = (PNGChunkIHDR) filterChunks.get(0);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Color: ");
        stringBuffer.append(getColorTypeDescription(pNGChunkIHDR.colorType));
        printWriter.println(stringBuffer.toString());
        StringBuffer stringBuffer2 = new StringBuffer();
        stringBuffer2.append("chunks: ");
        stringBuffer2.append(readChunks.size());
        printWriter.println(stringBuffer2.toString());
        if (readChunks.size() < 1) {
            return false;
        }
        for (int i = 0; i < readChunks.size(); i++) {
            PNGChunk pNGChunk = (PNGChunk) readChunks.get(i);
            StringBuffer stringBuffer3 = new StringBuffer();
            stringBuffer3.append("\t");
            stringBuffer3.append(i);
            stringBuffer3.append(": ");
            printCharQuad(printWriter, stringBuffer3.toString(), pNGChunk.chunkType);
        }
        printWriter.println("");
        printWriter.flush();
        return true;
    }

    public void writeImage(BufferedImage bufferedImage, OutputStream outputStream, Map map) throws ImageWriteException, IOException {
        new PngWriter(map).writeImage(bufferedImage, outputStream, map);
    }

    public String getXmpXml(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        ArrayList readChunks = readChunks(byteSource, new int[]{iTXt}, false);
        if (readChunks == null || readChunks.size() < 1) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < readChunks.size(); i++) {
            PNGChunkiTXt pNGChunkiTXt = (PNGChunkiTXt) readChunks.get(i);
            if (pNGChunkiTXt.getKeyword().equals(PngConstants.XMP_KEYWORD)) {
                arrayList.add(pNGChunkiTXt);
            }
        }
        if (arrayList.size() < 1) {
            return null;
        }
        if (arrayList.size() <= 1) {
            return ((PNGChunkiTXt) arrayList.get(0)).getText();
        }
        throw new ImageReadException("PNG contains more than one XMP chunk.");
    }
}

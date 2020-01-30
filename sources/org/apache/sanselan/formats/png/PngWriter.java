package org.apache.sanselan.formats.png;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.DeflaterOutputStream;
import kotlin.UByte;
import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.SanselanConstants;
import org.apache.sanselan.common.ZLibUtils;
import org.apache.sanselan.formats.png.PngText.iTXt;
import org.apache.sanselan.formats.png.PngText.tEXt;
import org.apache.sanselan.formats.png.PngText.zTXt;
import org.apache.sanselan.palette.MedianCutQuantizer;
import org.apache.sanselan.palette.Palette;
import org.apache.sanselan.palette.PaletteFactory;
import org.apache.sanselan.util.Debug;
import org.apache.sanselan.util.ParamMap;
import org.apache.sanselan.util.UnicodeUtils;

public class PngWriter implements PngConstants {
    private final boolean verbose;

    private static class ImageHeader {
        public final byte bit_depth;
        public final byte colorType;
        public final byte compressionMethod;
        public final byte filterMethod;
        public final int height;
        public final byte interlaceMethod;
        public final int width;

        public ImageHeader(int i, int i2, byte b, byte b2, byte b3, byte b4, byte b5) {
            this.width = i;
            this.height = i2;
            this.bit_depth = b;
            this.colorType = b2;
            this.compressionMethod = b3;
            this.filterMethod = b4;
            this.interlaceMethod = b5;
        }
    }

    private byte getColourType(boolean z, boolean z2) {
        return z2 ? z ? (byte) 4 : 0 : z ? (byte) 6 : 2;
    }

    public PngWriter(boolean z) {
        this.verbose = z;
    }

    public PngWriter(Map map) {
        this.verbose = ParamMap.getParamBoolean(map, SanselanConstants.PARAM_KEY_VERBOSE, false);
    }

    private final void writeInt(OutputStream outputStream, int i) throws IOException {
        outputStream.write((i >> 24) & 255);
        outputStream.write((i >> 16) & 255);
        outputStream.write((i >> 8) & 255);
        outputStream.write((i >> 0) & 255);
    }

    private final void writeChunk(OutputStream outputStream, byte[] bArr, byte[] bArr2) throws IOException {
        writeInt(outputStream, bArr2 == null ? 0 : bArr2.length);
        outputStream.write(bArr);
        if (bArr2 != null) {
            outputStream.write(bArr2);
        }
        PngCrc pngCrc = new PngCrc();
        long start_partial_crc = pngCrc.start_partial_crc(bArr, bArr.length);
        if (bArr2 != null) {
            start_partial_crc = pngCrc.continue_partial_crc(start_partial_crc, bArr2, bArr2.length);
        }
        writeInt(outputStream, (int) pngCrc.finish_partial_crc(start_partial_crc));
    }

    private void writeChunkIHDR(OutputStream outputStream, ImageHeader imageHeader) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        writeInt(byteArrayOutputStream, imageHeader.width);
        writeInt(byteArrayOutputStream, imageHeader.height);
        byteArrayOutputStream.write(imageHeader.bit_depth & UByte.MAX_VALUE);
        byteArrayOutputStream.write(imageHeader.colorType & UByte.MAX_VALUE);
        byteArrayOutputStream.write(imageHeader.compressionMethod & UByte.MAX_VALUE);
        byteArrayOutputStream.write(imageHeader.filterMethod & UByte.MAX_VALUE);
        byteArrayOutputStream.write(imageHeader.interlaceMethod & UByte.MAX_VALUE);
        writeChunk(outputStream, IHDR_CHUNK_TYPE, byteArrayOutputStream.toByteArray());
    }

    private void writeChunkiTXt(OutputStream outputStream, iTXt itxt) throws IOException, ImageWriteException {
        if (!UnicodeUtils.isValidISO_8859_1(itxt.keyword)) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("Png tEXt chunk keyword is not ISO-8859-1: ");
            stringBuffer.append(itxt.keyword);
            throw new ImageWriteException(stringBuffer.toString());
        } else if (UnicodeUtils.isValidISO_8859_1(itxt.languageTag)) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byteArrayOutputStream.write(itxt.keyword.getBytes("ISO-8859-1"));
            byteArrayOutputStream.write(0);
            byteArrayOutputStream.write(1);
            byteArrayOutputStream.write(0);
            byteArrayOutputStream.write(itxt.languageTag.getBytes("ISO-8859-1"));
            byteArrayOutputStream.write(0);
            byteArrayOutputStream.write(itxt.translatedKeyword.getBytes("utf-8"));
            byteArrayOutputStream.write(0);
            byteArrayOutputStream.write(new ZLibUtils().deflate(itxt.text.getBytes("utf-8")));
            writeChunk(outputStream, iTXt_CHUNK_TYPE, byteArrayOutputStream.toByteArray());
        } else {
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2.append("Png tEXt chunk language tag is not ISO-8859-1: ");
            stringBuffer2.append(itxt.languageTag);
            throw new ImageWriteException(stringBuffer2.toString());
        }
    }

    private void writeChunkzTXt(OutputStream outputStream, zTXt ztxt) throws IOException, ImageWriteException {
        if (!UnicodeUtils.isValidISO_8859_1(ztxt.keyword)) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("Png zTXt chunk keyword is not ISO-8859-1: ");
            stringBuffer.append(ztxt.keyword);
            throw new ImageWriteException(stringBuffer.toString());
        } else if (UnicodeUtils.isValidISO_8859_1(ztxt.text)) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byteArrayOutputStream.write(ztxt.keyword.getBytes("ISO-8859-1"));
            byteArrayOutputStream.write(0);
            byteArrayOutputStream.write(0);
            byteArrayOutputStream.write(new ZLibUtils().deflate(ztxt.text.getBytes("ISO-8859-1")));
            writeChunk(outputStream, zTXt_CHUNK_TYPE, byteArrayOutputStream.toByteArray());
        } else {
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2.append("Png zTXt chunk text is not ISO-8859-1: ");
            stringBuffer2.append(ztxt.text);
            throw new ImageWriteException(stringBuffer2.toString());
        }
    }

    private void writeChunktEXt(OutputStream outputStream, tEXt text) throws IOException, ImageWriteException {
        if (!UnicodeUtils.isValidISO_8859_1(text.keyword)) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("Png tEXt chunk keyword is not ISO-8859-1: ");
            stringBuffer.append(text.keyword);
            throw new ImageWriteException(stringBuffer.toString());
        } else if (UnicodeUtils.isValidISO_8859_1(text.text)) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byteArrayOutputStream.write(text.keyword.getBytes("ISO-8859-1"));
            byteArrayOutputStream.write(0);
            byteArrayOutputStream.write(text.text.getBytes("ISO-8859-1"));
            writeChunk(outputStream, tEXt_CHUNK_TYPE, byteArrayOutputStream.toByteArray());
        } else {
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2.append("Png tEXt chunk text is not ISO-8859-1: ");
            stringBuffer2.append(text.text);
            throw new ImageWriteException(stringBuffer2.toString());
        }
    }

    private void writeChunkXmpiTXt(OutputStream outputStream, String str) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(PngConstants.XMP_KEYWORD.getBytes("ISO-8859-1"));
        byteArrayOutputStream.write(0);
        byteArrayOutputStream.write(1);
        byteArrayOutputStream.write(0);
        byteArrayOutputStream.write(0);
        byteArrayOutputStream.write(PngConstants.XMP_KEYWORD.getBytes("utf-8"));
        byteArrayOutputStream.write(0);
        byteArrayOutputStream.write(new ZLibUtils().deflate(str.getBytes("utf-8")));
        writeChunk(outputStream, iTXt_CHUNK_TYPE, byteArrayOutputStream.toByteArray());
    }

    private void writeChunkPLTE(OutputStream outputStream, Palette palette) throws IOException {
        int length = palette.length();
        byte[] bArr = new byte[(length * 3)];
        for (int i = 0; i < length; i++) {
            int entry = palette.getEntry(i);
            int i2 = i * 3;
            bArr[i2 + 0] = (byte) ((entry >> 16) & 255);
            bArr[i2 + 1] = (byte) ((entry >> 8) & 255);
            bArr[i2 + 2] = (byte) ((entry >> 0) & 255);
        }
        writeChunk(outputStream, PLTE_CHUNK_TYPE, bArr);
    }

    private void writeChunkIEND(OutputStream outputStream) throws IOException {
        writeChunk(outputStream, IEND_CHUNK_TYPE, null);
    }

    private void writeChunkIDAT(OutputStream outputStream, byte[] bArr) throws IOException {
        writeChunk(outputStream, IDAT_CHUNK_TYPE, bArr);
    }

    private byte getBitDepth(byte b, Map map) {
        byte b2;
        Object obj = map.get(PngConstants.PARAM_KEY_PNG_BIT_DEPTH);
        if (obj == null || !(obj instanceof Number)) {
            return 8;
        }
        int intValue = ((Number) obj).intValue();
        if (!(intValue == 4 || intValue == 8 || intValue == 16)) {
            switch (intValue) {
                case 1:
                case 2:
                    break;
                default:
                    b2 = 8;
                    break;
            }
        }
        b2 = (byte) intValue;
        if (b != 0) {
            if (b != 6) {
                switch (b) {
                    case 2:
                    case 4:
                        break;
                    case 3:
                        return (byte) Math.min(8, b2);
                }
            }
            return (byte) Math.max(8, b2);
        }
        return b2;
    }

    public void writeImage(BufferedImage bufferedImage, OutputStream outputStream, Map map) throws ImageWriteException, IOException {
        byte b;
        Palette palette;
        BufferedImage bufferedImage2 = bufferedImage;
        OutputStream outputStream2 = outputStream;
        HashMap hashMap = new HashMap(map);
        if (hashMap.containsKey(SanselanConstants.PARAM_KEY_FORMAT)) {
            hashMap.remove(SanselanConstants.PARAM_KEY_FORMAT);
        }
        if (hashMap.containsKey(SanselanConstants.PARAM_KEY_VERBOSE)) {
            hashMap.remove(SanselanConstants.PARAM_KEY_VERBOSE);
        }
        HashMap hashMap2 = new HashMap(hashMap);
        if (hashMap.containsKey(PngConstants.PARAM_KEY_PNG_FORCE_TRUE_COLOR)) {
            hashMap.remove(PngConstants.PARAM_KEY_PNG_FORCE_TRUE_COLOR);
        }
        if (hashMap.containsKey(PngConstants.PARAM_KEY_PNG_FORCE_INDEXED_COLOR)) {
            hashMap.remove(PngConstants.PARAM_KEY_PNG_FORCE_INDEXED_COLOR);
        }
        if (hashMap.containsKey(PngConstants.PARAM_KEY_PNG_BIT_DEPTH)) {
            hashMap.remove(PngConstants.PARAM_KEY_PNG_BIT_DEPTH);
        }
        if (hashMap.containsKey(SanselanConstants.PARAM_KEY_XMP_XML)) {
            hashMap.remove(SanselanConstants.PARAM_KEY_XMP_XML);
        }
        if (hashMap.containsKey(PngConstants.PARAM_KEY_PNG_TEXT_CHUNKS)) {
            hashMap.remove(PngConstants.PARAM_KEY_PNG_TEXT_CHUNKS);
        }
        if (hashMap.size() <= 0) {
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();
            boolean hasTransparency = new PaletteFactory().hasTransparency(bufferedImage2);
            if (this.verbose) {
                Debug.debug("hasAlpha", hasTransparency);
            }
            boolean isGrayscale = new PaletteFactory().isGrayscale(bufferedImage2);
            if (this.verbose) {
                Debug.debug("isGrayscale", isGrayscale);
            }
            boolean paramBoolean = ParamMap.getParamBoolean(hashMap2, PngConstants.PARAM_KEY_PNG_FORCE_INDEXED_COLOR, false);
            boolean paramBoolean2 = ParamMap.getParamBoolean(hashMap2, PngConstants.PARAM_KEY_PNG_FORCE_TRUE_COLOR, false);
            if (!paramBoolean || !paramBoolean2) {
                if (paramBoolean) {
                    b = 3;
                } else if (paramBoolean2) {
                    b = (byte) (hasTransparency ? 6 : 2);
                } else {
                    b = getColourType(hasTransparency, isGrayscale);
                }
                if (this.verbose) {
                    Debug.debug("colorType", (int) b);
                }
                byte bitDepth = getBitDepth(b, hashMap2);
                if (this.verbose) {
                    Debug.debug("bit_depth", (int) bitDepth);
                }
                byte b2 = b == 3 ? 8 : bitDepth;
                if (this.verbose) {
                    Debug.debug("sample_depth", (int) b2);
                }
                outputStream2.write(PNG_Signature);
                ImageHeader imageHeader = r11;
                boolean z = isGrayscale;
                ImageHeader imageHeader2 = new ImageHeader(width, height, bitDepth, b, 0, 0, 0);
                writeChunkIHDR(outputStream2, imageHeader);
                boolean z2 = true;
                if (b == 3) {
                    Palette process = new MedianCutQuantizer(true).process(bufferedImage2, hasTransparency ? 255 : 256, this.verbose);
                    writeChunkPLTE(outputStream2, process);
                    palette = process;
                } else {
                    palette = null;
                }
                if (hashMap2.containsKey(SanselanConstants.PARAM_KEY_XMP_XML)) {
                    writeChunkXmpiTXt(outputStream2, (String) hashMap2.get(SanselanConstants.PARAM_KEY_XMP_XML));
                }
                if (hashMap2.containsKey(PngConstants.PARAM_KEY_PNG_TEXT_CHUNKS)) {
                    List list = (List) hashMap2.get(PngConstants.PARAM_KEY_PNG_TEXT_CHUNKS);
                    for (int i = 0; i < list.size(); i++) {
                        PngText pngText = (PngText) list.get(i);
                        if (pngText instanceof tEXt) {
                            writeChunktEXt(outputStream2, (tEXt) pngText);
                        } else if (pngText instanceof zTXt) {
                            writeChunkzTXt(outputStream2, (zTXt) pngText);
                        } else if (pngText instanceof iTXt) {
                            writeChunkiTXt(outputStream2, (iTXt) pngText);
                        } else {
                            StringBuffer stringBuffer = new StringBuffer();
                            stringBuffer.append("Unknown text to embed in PNG: ");
                            stringBuffer.append(pngText);
                            throw new ImageWriteException(stringBuffer.toString());
                        }
                    }
                }
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                if (!(b == 4 || b == 6)) {
                    z2 = false;
                }
                int[] iArr = new int[width];
                int i2 = 0;
                while (i2 < height) {
                    int i3 = i2;
                    boolean z3 = z;
                    int i4 = height;
                    int i5 = width;
                    bufferedImage.getRGB(0, i2, width, 1, iArr, 0, width);
                    byteArrayOutputStream.write(0);
                    int i6 = i5;
                    for (int i7 = 0; i7 < i6; i7++) {
                        int i8 = iArr[i7];
                        if (palette != null) {
                            byteArrayOutputStream.write(palette.getPaletteIndex(i8) & 255);
                        } else {
                            int i9 = (i8 >> 24) & 255;
                            int i10 = (i8 >> 16) & 255;
                            int i11 = (i8 >> 8) & 255;
                            int i12 = (i8 >> 0) & 255;
                            if (z3) {
                                byteArrayOutputStream.write(((i10 + i11) + i12) / 3);
                            } else {
                                byteArrayOutputStream.write(i10);
                                byteArrayOutputStream.write(i11);
                                byteArrayOutputStream.write(i12);
                            }
                            if (z2) {
                                byteArrayOutputStream.write(i9);
                            }
                        }
                    }
                    i2 = i3 + 1;
                    width = i6;
                    z = z3;
                    height = i4;
                }
                int i13 = 0;
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
                DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(byteArrayOutputStream2);
                while (i13 < byteArray.length) {
                    int i14 = i13 + 262144;
                    deflaterOutputStream.write(byteArray, i13, Math.min(byteArray.length, i14) - i13);
                    deflaterOutputStream.flush();
                    byteArrayOutputStream2.flush();
                    byte[] byteArray2 = byteArrayOutputStream2.toByteArray();
                    byteArrayOutputStream2.reset();
                    if (byteArray2.length > 0) {
                        writeChunkIDAT(outputStream2, byteArray2);
                    }
                    i13 = i14;
                }
                deflaterOutputStream.finish();
                byte[] byteArray3 = byteArrayOutputStream2.toByteArray();
                if (byteArray3.length > 0) {
                    writeChunkIDAT(outputStream2, byteArray3);
                }
                writeChunkIEND(outputStream2);
                outputStream.close();
                return;
            }
            throw new ImageWriteException("Params: Cannot force both indexed and true color modes");
        }
        Object next = hashMap.keySet().iterator().next();
        StringBuffer stringBuffer2 = new StringBuffer();
        stringBuffer2.append("Unknown parameter: ");
        stringBuffer2.append(next);
        throw new ImageWriteException(stringBuffer2.toString());
    }
}

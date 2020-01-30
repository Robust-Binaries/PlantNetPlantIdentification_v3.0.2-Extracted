package org.apache.sanselan.formats.bmp;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import kotlin.UByte;
import org.apache.sanselan.FormatCompliance;
import org.apache.sanselan.ImageFormat;
import org.apache.sanselan.ImageInfo;
import org.apache.sanselan.ImageParser;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.SanselanConstants;
import org.apache.sanselan.common.BinaryOutputStream;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.common.byteSources.ByteSource;
import org.apache.sanselan.formats.bmp.pixelparsers.PixelParser;
import org.apache.sanselan.formats.bmp.pixelparsers.PixelParserBitFields;
import org.apache.sanselan.formats.bmp.pixelparsers.PixelParserRgb;
import org.apache.sanselan.formats.bmp.pixelparsers.PixelParserRle;
import org.apache.sanselan.formats.bmp.writers.BMPWriter;
import org.apache.sanselan.formats.bmp.writers.BMPWriterPalette;
import org.apache.sanselan.formats.bmp.writers.BMPWriterRGB;
import org.apache.sanselan.palette.PaletteFactory;
import org.apache.sanselan.palette.SimplePalette;
import org.apache.sanselan.util.Debug;
import org.apache.sanselan.util.ParamMap;

public class BmpImageParser extends ImageParser {
    private static final String[] ACCEPTED_EXTENSIONS = {DEFAULT_EXTENSION};
    private static final int BITMAP_FILE_HEADER_SIZE = 14;
    private static final int BITMAP_INFO_HEADER_SIZE = 40;
    private static final int BI_BITFIELDS = 3;
    private static final int BI_RGB = 0;
    private static final int BI_RLE4 = 2;
    private static final int BI_RLE8 = 1;
    private static final byte[] BMP_HEADER_SIGNATURE = {66, 77};
    private static final String DEFAULT_EXTENSION = ".bmp";

    private String getBmpTypeDescription(int i, int i2) {
        return (i == 66 && i2 == 77) ? "Windows 3.1x, 95, NT," : (i == 66 && i2 == 65) ? "OS/2 Bitmap Array" : (i == 67 && i2 == 73) ? "OS/2 Color Icon" : (i == 67 && i2 == 80) ? "OS/2 Color Pointer" : (i == 73 && i2 == 67) ? "OS/2 Icon" : (i == 80 && i2 == 84) ? "OS/2 Pointer" : ImageInfo.COMPRESSION_ALGORITHM_UNKNOWN;
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

    public byte[] getICCProfileBytes(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        return null;
    }

    public IImageMetadata getMetadata(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        return null;
    }

    public String getName() {
        return "Bmp-Custom";
    }

    public String getXmpXml(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        return null;
    }

    public BmpImageParser() {
        super.setByteOrder(73);
    }

    /* access modifiers changed from: protected */
    public String[] getAcceptedExtensions() {
        return ACCEPTED_EXTENSIONS;
    }

    /* access modifiers changed from: protected */
    public ImageFormat[] getAcceptedTypes() {
        return new ImageFormat[]{ImageFormat.IMAGE_FORMAT_BMP};
    }

    private BmpHeaderInfo readBmpHeaderInfo(InputStream inputStream, FormatCompliance formatCompliance, boolean z) throws ImageReadException, IOException {
        int i;
        int i2;
        InputStream inputStream2 = inputStream;
        FormatCompliance formatCompliance2 = formatCompliance;
        byte readByte = readByte("Identifier1", inputStream2, "Not a Valid BMP File");
        byte readByte2 = readByte("Identifier2", inputStream2, "Not a Valid BMP File");
        if (formatCompliance2 != null) {
            formatCompliance2.compare_bytes("Signature", BMP_HEADER_SIGNATURE, new byte[]{readByte, readByte2});
        }
        int read4Bytes = read4Bytes("File Size", inputStream2, "Not a Valid BMP File");
        int read4Bytes2 = read4Bytes("Reserved", inputStream2, "Not a Valid BMP File");
        int read4Bytes3 = read4Bytes("Bitmap Data Offset", inputStream2, "Not a Valid BMP File");
        int read4Bytes4 = read4Bytes("Bitmap Header Size", inputStream2, "Not a Valid BMP File");
        int read4Bytes5 = read4Bytes("Width", inputStream2, "Not a Valid BMP File");
        int read4Bytes6 = read4Bytes("Height", inputStream2, "Not a Valid BMP File");
        int read2Bytes = read2Bytes("Planes", inputStream2, "Not a Valid BMP File");
        int read2Bytes2 = read2Bytes("Bits Per Pixel", inputStream2, "Not a Valid BMP File");
        int read4Bytes7 = read4Bytes("Compression", inputStream2, "Not a Valid BMP File");
        int read4Bytes8 = read4Bytes("Bitmap Data Size", inputStream2, "Not a Valid BMP File");
        int read4Bytes9 = read4Bytes("HResolution", inputStream2, "Not a Valid BMP File");
        int read4Bytes10 = read4Bytes("VResolution", inputStream2, "Not a Valid BMP File");
        int read4Bytes11 = read4Bytes("ColorsUsed", inputStream2, "Not a Valid BMP File");
        int read4Bytes12 = read4Bytes("ColorsImportant", inputStream2, "Not a Valid BMP File");
        if (z) {
            debugNumber("identifier1", (int) readByte, 1);
            debugNumber("identifier2", (int) readByte2, 1);
            debugNumber("fileSize", read4Bytes, 4);
            debugNumber("reserved", read4Bytes2, 4);
            debugNumber("bitmapDataOffset", read4Bytes3, 4);
            debugNumber("bitmapHeaderSize", read4Bytes4, 4);
            debugNumber("width", read4Bytes5, 4);
            debugNumber("height", read4Bytes6, 4);
            debugNumber("planes", read2Bytes, 2);
            debugNumber("bitsPerPixel", read2Bytes2, 2);
            debugNumber("compression", read4Bytes7, 4);
            debugNumber("bitmapDataSize", read4Bytes8, 4);
            i = read4Bytes8;
            debugNumber("hResolution", read4Bytes9, 4);
            debugNumber("vResolution", read4Bytes10, 4);
            i2 = read4Bytes11;
            debugNumber("colorsUsed", i2, 4);
            debugNumber("colorsImportant", read4Bytes12, 4);
        } else {
            i = read4Bytes8;
            i2 = read4Bytes11;
        }
        BmpHeaderInfo bmpHeaderInfo = new BmpHeaderInfo(readByte, readByte2, read4Bytes, read4Bytes2, read4Bytes3, read4Bytes4, read4Bytes5, read4Bytes6, read2Bytes, read2Bytes2, read4Bytes7, i, read4Bytes9, read4Bytes10, i2, read4Bytes12);
        return bmpHeaderInfo;
    }

    private byte[] getRLEBytes(InputStream inputStream, int i) throws ImageReadException, IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        boolean z = false;
        while (!z) {
            byte readByte = readByte("RLE a", inputStream, "BMP: Bad RLE") & UByte.MAX_VALUE;
            byteArrayOutputStream.write(readByte);
            byte readByte2 = readByte("RLE b", inputStream, "BMP: Bad RLE") & UByte.MAX_VALUE;
            byteArrayOutputStream.write(readByte2);
            if (readByte == 0) {
                switch (readByte2) {
                    case 0:
                        break;
                    case 1:
                        z = true;
                        break;
                    case 2:
                        byteArrayOutputStream.write(readByte("RLE c", inputStream, "BMP: Bad RLE") & UByte.MAX_VALUE);
                        byteArrayOutputStream.write(readByte("RLE d", inputStream, "BMP: Bad RLE") & UByte.MAX_VALUE);
                        break;
                    default:
                        int i2 = readByte2 / i;
                        if (readByte2 % i > 0) {
                            i2++;
                        }
                        if (i2 % 2 != 0) {
                            i2++;
                        }
                        byteArrayOutputStream.write(readByteArray("bytes", i2, inputStream, "RLE: Absolute Mode"));
                        break;
                }
            }
        }
        return byteArrayOutputStream.toByteArray();
    }

    private ImageContents readImageContents(InputStream inputStream, FormatCompliance formatCompliance, boolean z) throws ImageReadException, IOException {
        int i;
        byte[] bArr;
        PixelParser pixelParser;
        String str;
        BmpHeaderInfo readBmpHeaderInfo = readBmpHeaderInfo(inputStream, formatCompliance, z);
        int i2 = readBmpHeaderInfo.colorsUsed;
        boolean z2 = true;
        if (i2 == 0) {
            i2 = 1 << readBmpHeaderInfo.bitsPerPixel;
        }
        if (z) {
            debugNumber("ColorsUsed", readBmpHeaderInfo.colorsUsed, 4);
            debugNumber("BitsPerPixel", readBmpHeaderInfo.bitsPerPixel, 4);
            debugNumber("ColorTableSize", i2, 4);
            debugNumber("bhi.colorsUsed", readBmpHeaderInfo.colorsUsed, 4);
            debugNumber("Compression", readBmpHeaderInfo.compression, 4);
        }
        int i3 = 0;
        switch (readBmpHeaderInfo.compression) {
            case 0:
                if (z) {
                    System.out.println("Compression: BI_RGB");
                }
                if (readBmpHeaderInfo.bitsPerPixel > 8) {
                    i = 0;
                    z2 = false;
                    break;
                } else {
                    i3 = i2 * 4;
                    i = 0;
                    z2 = false;
                    break;
                }
            case 1:
                if (z) {
                    System.out.println("Compression: BI_RLE8");
                }
                i3 = i2 * 4;
                i = 1;
                break;
            case 2:
                if (z) {
                    System.out.println("Compression: BI_RLE4");
                }
                i3 = i2 * 4;
                i = 2;
                break;
            case 3:
                if (z) {
                    System.out.println("Compression: BI_BITFIELDS");
                }
                i = 0;
                z2 = false;
                i3 = 12;
                break;
            default:
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("BMP: Unknown Compression: ");
                stringBuffer.append(readBmpHeaderInfo.compression);
                throw new ImageReadException(stringBuffer.toString());
        }
        byte[] bArr2 = null;
        if (i3 > 0) {
            bArr2 = readByteArray("ColorTable", i3, inputStream, "Not a Valid BMP File");
        }
        if (z) {
            debugNumber("paletteLength", i3, 4);
            PrintStream printStream = System.out;
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2.append("ColorTable: ");
            if (bArr2 == null) {
                str = "null";
            } else {
                StringBuffer stringBuffer3 = new StringBuffer();
                stringBuffer3.append("");
                stringBuffer3.append(bArr2.length);
                str = stringBuffer3.toString();
            }
            stringBuffer2.append(str);
            printStream.println(stringBuffer2.toString());
        }
        int i4 = readBmpHeaderInfo.width * readBmpHeaderInfo.height;
        int i5 = ((readBmpHeaderInfo.bitsPerPixel * readBmpHeaderInfo.width) + 7) / 8;
        if (z) {
            debugNumber("bhi.Width", readBmpHeaderInfo.width, 4);
            debugNumber("bhi.Height", readBmpHeaderInfo.height, 4);
            debugNumber("ImageLineLength", i5, 4);
            debugNumber("PixelCount", i4, 4);
        }
        while (i5 % 4 != 0) {
            i5++;
        }
        int i6 = i3 + 54;
        if (z) {
            debugNumber("bhi.BitmapDataOffset", readBmpHeaderInfo.bitmapDataOffset, 4);
            debugNumber("expectedDataOffset", i6, 4);
        }
        int i7 = readBmpHeaderInfo.bitmapDataOffset - i6;
        if (i7 >= 0) {
            if (i7 > 0) {
                readByteArray("BitmapDataOffset", i7, inputStream, "Not a Valid BMP File");
            }
            int i8 = readBmpHeaderInfo.height * i5;
            if (z) {
                debugNumber("imageDataSize", i8, 4);
            }
            if (z2) {
                bArr = getRLEBytes(inputStream, i);
            } else {
                bArr = readByteArray("ImageData", i8, inputStream, "Not a Valid BMP File");
            }
            if (z) {
                debugNumber("ImageData.length", bArr.length, 4);
            }
            switch (readBmpHeaderInfo.compression) {
                case 0:
                    pixelParser = new PixelParserRgb(readBmpHeaderInfo, bArr2, bArr);
                    break;
                case 1:
                case 2:
                    pixelParser = new PixelParserRle(readBmpHeaderInfo, bArr2, bArr);
                    break;
                case 3:
                    pixelParser = new PixelParserBitFields(readBmpHeaderInfo, bArr2, bArr);
                    break;
                default:
                    StringBuffer stringBuffer4 = new StringBuffer();
                    stringBuffer4.append("BMP: Unknown Compression: ");
                    stringBuffer4.append(readBmpHeaderInfo.compression);
                    throw new ImageReadException(stringBuffer4.toString());
            }
            return new ImageContents(readBmpHeaderInfo, bArr2, bArr, pixelParser);
        }
        StringBuffer stringBuffer5 = new StringBuffer();
        stringBuffer5.append("BMP has invalid image data offset: ");
        stringBuffer5.append(readBmpHeaderInfo.bitmapDataOffset);
        stringBuffer5.append(" (expected: ");
        stringBuffer5.append(i6);
        stringBuffer5.append(", paletteLength: ");
        stringBuffer5.append(i3);
        stringBuffer5.append(", headerSize: ");
        stringBuffer5.append(54);
        stringBuffer5.append(")");
        throw new ImageReadException(stringBuffer5.toString());
    }

    private BmpHeaderInfo readBmpHeaderInfo(ByteSource byteSource, boolean z) throws ImageReadException, IOException {
        InputStream inputStream;
        try {
            inputStream = byteSource.getInputStream();
            try {
                BmpHeaderInfo readBmpHeaderInfo = readBmpHeaderInfo(inputStream, null, z);
                try {
                    inputStream.close();
                } catch (Exception e) {
                    Debug.debug((Throwable) e);
                }
                return readBmpHeaderInfo;
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

    public Dimension getImageSize(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        HashMap hashMap = map == null ? new HashMap() : new HashMap(map);
        boolean paramBoolean = ParamMap.getParamBoolean(hashMap, SanselanConstants.PARAM_KEY_VERBOSE, false);
        if (hashMap.containsKey(SanselanConstants.PARAM_KEY_VERBOSE)) {
            hashMap.remove(SanselanConstants.PARAM_KEY_VERBOSE);
        }
        if (hashMap.size() <= 0) {
            BmpHeaderInfo readBmpHeaderInfo = readBmpHeaderInfo(byteSource, paramBoolean);
            if (readBmpHeaderInfo != null) {
                return new Dimension(readBmpHeaderInfo.width, readBmpHeaderInfo.height);
            }
            throw new ImageReadException("BMP: couldn't read header");
        }
        Object next = hashMap.keySet().iterator().next();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Unknown parameter: ");
        stringBuffer.append(next);
        throw new ImageReadException(stringBuffer.toString());
    }

    public ImageInfo getImageInfo(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        Map map2 = map;
        HashMap hashMap = map2 == null ? new HashMap() : new HashMap(map2);
        boolean paramBoolean = ParamMap.getParamBoolean(hashMap, SanselanConstants.PARAM_KEY_VERBOSE, false);
        if (hashMap.containsKey(SanselanConstants.PARAM_KEY_VERBOSE)) {
            hashMap.remove(SanselanConstants.PARAM_KEY_VERBOSE);
        }
        if (hashMap.size() <= 0) {
            ImageContents readImageContents = readImageContents(byteSource.getInputStream(), FormatCompliance.getDefault(), paramBoolean);
            if (readImageContents != null) {
                BmpHeaderInfo bmpHeaderInfo = readImageContents.bhi;
                byte[] bArr = readImageContents.colorTable;
                if (bmpHeaderInfo != null) {
                    int i = bmpHeaderInfo.height;
                    int i2 = bmpHeaderInfo.width;
                    ArrayList arrayList = new ArrayList();
                    int i3 = bmpHeaderInfo.bitsPerPixel;
                    ImageFormat imageFormat = ImageFormat.IMAGE_FORMAT_BMP;
                    String str = "BMP Windows Bitmap";
                    String str2 = "image/x-ms-bmp";
                    double d = (double) bmpHeaderInfo.hResolution;
                    Double.isNaN(d);
                    int i4 = (int) ((d * 1000.0d) / 2.54d);
                    double d2 = (double) i2;
                    double d3 = (double) i4;
                    Double.isNaN(d2);
                    Double.isNaN(d3);
                    float f = (float) (d2 / d3);
                    double d4 = (double) bmpHeaderInfo.vResolution;
                    Double.isNaN(d4);
                    int i5 = (int) ((d4 * 1000.0d) / 2.54d);
                    int i6 = i4;
                    double d5 = (double) i;
                    int i7 = i2;
                    double d6 = (double) i5;
                    Double.isNaN(d5);
                    Double.isNaN(d6);
                    float f2 = (float) (d5 / d6);
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append("Bmp (");
                    stringBuffer.append((char) bmpHeaderInfo.identifier1);
                    stringBuffer.append((char) bmpHeaderInfo.identifier2);
                    stringBuffer.append(": ");
                    stringBuffer.append(getBmpTypeDescription(bmpHeaderInfo.identifier1, bmpHeaderInfo.identifier2));
                    stringBuffer.append(")");
                    ImageInfo imageInfo = new ImageInfo(stringBuffer.toString(), i3, arrayList, imageFormat, str, i, str2, -1, i5, f2, i6, f, i7, false, false, bArr != null, 2, ImageInfo.COMPRESSION_ALGORITHM_RLE);
                    return imageInfo;
                }
                throw new ImageReadException("BMP: couldn't read header");
            }
            throw new ImageReadException("Couldn't read BMP Data");
        }
        Object next = hashMap.keySet().iterator().next();
        StringBuffer stringBuffer2 = new StringBuffer();
        stringBuffer2.append("Unknown parameter: ");
        stringBuffer2.append(next);
        throw new ImageReadException(stringBuffer2.toString());
    }

    public boolean dumpImageFile(PrintWriter printWriter, ByteSource byteSource) throws ImageReadException, IOException {
        printWriter.println("bmp.dumpImageFile");
        ImageInfo imageInfo = getImageInfo(byteSource, null);
        if (imageInfo == null) {
            return false;
        }
        imageInfo.toString(printWriter, "");
        printWriter.println("");
        return true;
    }

    public FormatCompliance getFormatCompliance(ByteSource byteSource) throws ImageReadException, IOException {
        FormatCompliance formatCompliance = new FormatCompliance(byteSource.getDescription());
        readImageContents(byteSource.getInputStream(), formatCompliance, false);
        return formatCompliance;
    }

    public BufferedImage getBufferedImage(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        HashMap hashMap = map == null ? new HashMap() : new HashMap(map);
        boolean paramBoolean = ParamMap.getParamBoolean(hashMap, SanselanConstants.PARAM_KEY_VERBOSE, false);
        if (hashMap.containsKey(SanselanConstants.PARAM_KEY_VERBOSE)) {
            hashMap.remove(SanselanConstants.PARAM_KEY_VERBOSE);
        }
        if (hashMap.containsKey(SanselanConstants.BUFFERED_IMAGE_FACTORY)) {
            hashMap.remove(SanselanConstants.BUFFERED_IMAGE_FACTORY);
        }
        if (hashMap.size() <= 0) {
            ImageContents readImageContents = readImageContents(byteSource.getInputStream(), FormatCompliance.getDefault(), paramBoolean);
            if (readImageContents != null) {
                BmpHeaderInfo bmpHeaderInfo = readImageContents.bhi;
                int i = bmpHeaderInfo.width;
                int i2 = bmpHeaderInfo.height;
                BufferedImage colorBufferedImage = getBufferedImageFactory(hashMap).getColorBufferedImage(i, i2, false);
                if (paramBoolean) {
                    PrintStream printStream = System.out;
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append("width: ");
                    stringBuffer.append(i);
                    printStream.println(stringBuffer.toString());
                    PrintStream printStream2 = System.out;
                    StringBuffer stringBuffer2 = new StringBuffer();
                    stringBuffer2.append("height: ");
                    stringBuffer2.append(i2);
                    printStream2.println(stringBuffer2.toString());
                    PrintStream printStream3 = System.out;
                    StringBuffer stringBuffer3 = new StringBuffer();
                    stringBuffer3.append("width*height: ");
                    int i3 = i * i2;
                    stringBuffer3.append(i3);
                    printStream3.println(stringBuffer3.toString());
                    PrintStream printStream4 = System.out;
                    StringBuffer stringBuffer4 = new StringBuffer();
                    stringBuffer4.append("width*height*4: ");
                    stringBuffer4.append(i3 * 4);
                    printStream4.println(stringBuffer4.toString());
                }
                readImageContents.pixelParser.processImage(colorBufferedImage);
                return colorBufferedImage;
            }
            throw new ImageReadException("Couldn't read BMP Data");
        }
        Object next = hashMap.keySet().iterator().next();
        StringBuffer stringBuffer5 = new StringBuffer();
        stringBuffer5.append("Unknown parameter: ");
        stringBuffer5.append(next);
        throw new ImageReadException(stringBuffer5.toString());
    }

    public void writeImage(BufferedImage bufferedImage, OutputStream outputStream, Map map) throws ImageWriteException, IOException {
        BMPWriter bMPWriter;
        HashMap hashMap = map == null ? new HashMap() : new HashMap(map);
        if (hashMap.containsKey(SanselanConstants.PARAM_KEY_FORMAT)) {
            hashMap.remove(SanselanConstants.PARAM_KEY_FORMAT);
        }
        if (hashMap.size() <= 0) {
            SimplePalette makePaletteSimple = new PaletteFactory().makePaletteSimple(bufferedImage, 256);
            if (makePaletteSimple == null) {
                bMPWriter = new BMPWriterRGB();
            } else {
                bMPWriter = new BMPWriterPalette(makePaletteSimple);
            }
            byte[] imageData = bMPWriter.getImageData(bufferedImage);
            BinaryOutputStream binaryOutputStream = new BinaryOutputStream(outputStream, 73);
            outputStream.write(66);
            outputStream.write(77);
            binaryOutputStream.write4Bytes((bMPWriter.getPaletteSize() * 4) + 54 + imageData.length);
            binaryOutputStream.write4Bytes(0);
            binaryOutputStream.write4Bytes((bMPWriter.getPaletteSize() * 4) + 54);
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();
            binaryOutputStream.write4Bytes(40);
            binaryOutputStream.write4Bytes(width);
            binaryOutputStream.write4Bytes(height);
            binaryOutputStream.write2Bytes(1);
            binaryOutputStream.write2Bytes(bMPWriter.getBitsPerPixel());
            binaryOutputStream.write4Bytes(0);
            binaryOutputStream.write4Bytes(imageData.length);
            binaryOutputStream.write4Bytes(0);
            binaryOutputStream.write4Bytes(0);
            if (makePaletteSimple == null) {
                binaryOutputStream.write4Bytes(0);
            } else {
                binaryOutputStream.write4Bytes(makePaletteSimple.length());
            }
            binaryOutputStream.write4Bytes(0);
            bMPWriter.writePalette(binaryOutputStream);
            binaryOutputStream.writeByteArray(imageData);
            return;
        }
        Object next = hashMap.keySet().iterator().next();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Unknown parameter: ");
        stringBuffer.append(next);
        throw new ImageWriteException(stringBuffer.toString());
    }
}

package org.apache.sanselan.formats.ico;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Map;
import kotlin.UByte;
import org.apache.sanselan.ImageFormat;
import org.apache.sanselan.ImageInfo;
import org.apache.sanselan.ImageParser;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.common.byteSources.ByteSource;
import org.apache.sanselan.util.Debug;

public class IcoImageParser extends ImageParser {
    private static final String[] ACCEPTED_EXTENSIONS = {DEFAULT_EXTENSION, ".cur"};
    private static final String DEFAULT_EXTENSION = ".ico";

    private static class BitmapHeader {
        public final int BitCount;
        public final int ColorsImportant;
        public final int ColorsUsed;
        public final int Compression;
        public final int Height;
        public final int Planes;
        public final int Size;
        public final int SizeImage;
        public final int Width;
        public final int XPelsPerMeter;
        public final int YPelsPerMeter;

        public BitmapHeader(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11) {
            this.Size = i;
            this.Width = i2;
            this.Height = i3;
            this.Planes = i4;
            this.BitCount = i5;
            this.Compression = i6;
            this.SizeImage = i7;
            this.XPelsPerMeter = i8;
            this.YPelsPerMeter = i9;
            this.ColorsUsed = i10;
            this.ColorsImportant = i11;
        }

        public void dump() {
            System.out.println("BitmapHeader");
            PrintStream printStream = System.out;
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("Size: ");
            stringBuffer.append(this.Size);
            printStream.println(stringBuffer.toString());
            PrintStream printStream2 = System.out;
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2.append("Width: ");
            stringBuffer2.append(this.Width);
            printStream2.println(stringBuffer2.toString());
            PrintStream printStream3 = System.out;
            StringBuffer stringBuffer3 = new StringBuffer();
            stringBuffer3.append("Height: ");
            stringBuffer3.append(this.Height);
            printStream3.println(stringBuffer3.toString());
            PrintStream printStream4 = System.out;
            StringBuffer stringBuffer4 = new StringBuffer();
            stringBuffer4.append("Planes: ");
            stringBuffer4.append(this.Planes);
            printStream4.println(stringBuffer4.toString());
            PrintStream printStream5 = System.out;
            StringBuffer stringBuffer5 = new StringBuffer();
            stringBuffer5.append("BitCount: ");
            stringBuffer5.append(this.BitCount);
            printStream5.println(stringBuffer5.toString());
            PrintStream printStream6 = System.out;
            StringBuffer stringBuffer6 = new StringBuffer();
            stringBuffer6.append("Compression: ");
            stringBuffer6.append(this.Compression);
            printStream6.println(stringBuffer6.toString());
            PrintStream printStream7 = System.out;
            StringBuffer stringBuffer7 = new StringBuffer();
            stringBuffer7.append("SizeImage: ");
            stringBuffer7.append(this.SizeImage);
            printStream7.println(stringBuffer7.toString());
            PrintStream printStream8 = System.out;
            StringBuffer stringBuffer8 = new StringBuffer();
            stringBuffer8.append("XPelsPerMeter: ");
            stringBuffer8.append(this.XPelsPerMeter);
            printStream8.println(stringBuffer8.toString());
            PrintStream printStream9 = System.out;
            StringBuffer stringBuffer9 = new StringBuffer();
            stringBuffer9.append("YPelsPerMeter: ");
            stringBuffer9.append(this.YPelsPerMeter);
            printStream9.println(stringBuffer9.toString());
            PrintStream printStream10 = System.out;
            StringBuffer stringBuffer10 = new StringBuffer();
            stringBuffer10.append("ColorsUsed: ");
            stringBuffer10.append(this.ColorsUsed);
            printStream10.println(stringBuffer10.toString());
            PrintStream printStream11 = System.out;
            StringBuffer stringBuffer11 = new StringBuffer();
            stringBuffer11.append("ColorsImportant: ");
            stringBuffer11.append(this.ColorsImportant);
            printStream11.println(stringBuffer11.toString());
            System.out.println("");
        }
    }

    private static class FileHeader {
        public final int iconCount;
        public final int iconType;
        public final int reserved;

        public FileHeader(int i, int i2, int i3) {
            this.reserved = i;
            this.iconType = i2;
            this.iconCount = i3;
        }

        public void dump() {
            System.out.println("FileHeader");
            PrintStream printStream = System.out;
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("Reserved: ");
            stringBuffer.append(this.reserved);
            printStream.println(stringBuffer.toString());
            PrintStream printStream2 = System.out;
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2.append("IconType: ");
            stringBuffer2.append(this.iconType);
            printStream2.println(stringBuffer2.toString());
            PrintStream printStream3 = System.out;
            StringBuffer stringBuffer3 = new StringBuffer();
            stringBuffer3.append("IconCount: ");
            stringBuffer3.append(this.iconCount);
            printStream3.println(stringBuffer3.toString());
            System.out.println("");
        }
    }

    private static class IconData {
        public final byte[] color_map;
        public final BitmapHeader header;
        public final IconInfo iconInfo;
        public final byte[] palette;
        public final int scanline_size;
        public final int t_scanline_size;
        public final byte[] transparency_map;

        public IconData(IconInfo iconInfo2, BitmapHeader bitmapHeader, byte[] bArr, byte[] bArr2, int i, byte[] bArr3, int i2) {
            this.iconInfo = iconInfo2;
            this.header = bitmapHeader;
            this.palette = bArr;
            this.color_map = bArr2;
            this.scanline_size = i;
            this.transparency_map = bArr3;
            this.t_scanline_size = i2;
        }

        public void dump() {
            String str;
            String str2;
            String str3;
            System.out.println("IconData");
            this.iconInfo.dump();
            this.header.dump();
            PrintStream printStream = System.out;
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("scanline_size: ");
            stringBuffer.append(this.scanline_size);
            printStream.println(stringBuffer.toString());
            PrintStream printStream2 = System.out;
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2.append("t_scanline_size: ");
            stringBuffer2.append(this.t_scanline_size);
            printStream2.println(stringBuffer2.toString());
            PrintStream printStream3 = System.out;
            StringBuffer stringBuffer3 = new StringBuffer();
            stringBuffer3.append("palette: ");
            if (this.palette == null) {
                str = "null";
            } else {
                StringBuffer stringBuffer4 = new StringBuffer();
                stringBuffer4.append("");
                stringBuffer4.append(this.palette.length);
                str = stringBuffer4.toString();
            }
            stringBuffer3.append(str);
            printStream3.println(stringBuffer3.toString());
            PrintStream printStream4 = System.out;
            StringBuffer stringBuffer5 = new StringBuffer();
            stringBuffer5.append("color_map: ");
            if (this.color_map == null) {
                str2 = "null";
            } else {
                StringBuffer stringBuffer6 = new StringBuffer();
                stringBuffer6.append("");
                stringBuffer6.append(this.color_map.length);
                str2 = stringBuffer6.toString();
            }
            stringBuffer5.append(str2);
            printStream4.println(stringBuffer5.toString());
            PrintStream printStream5 = System.out;
            StringBuffer stringBuffer7 = new StringBuffer();
            stringBuffer7.append("transparency_map: ");
            if (this.transparency_map == null) {
                str3 = "null";
            } else {
                StringBuffer stringBuffer8 = new StringBuffer();
                stringBuffer8.append("");
                stringBuffer8.append(this.transparency_map.length);
                str3 = stringBuffer8.toString();
            }
            stringBuffer7.append(str3);
            printStream5.println(stringBuffer7.toString());
            System.out.println("");
        }
    }

    private static class IconInfo {
        public final int BitCount;
        public final byte ColorCount;
        public final byte Height;
        public final int ImageOffset;
        public final int ImageSize;
        public final int Planes;
        public final byte Reserved;
        public final byte Width;

        public IconInfo(byte b, byte b2, byte b3, byte b4, int i, int i2, int i3, int i4) {
            this.Width = b;
            this.Height = b2;
            this.ColorCount = b3;
            this.Reserved = b4;
            this.Planes = i;
            this.BitCount = i2;
            this.ImageSize = i3;
            this.ImageOffset = i4;
        }

        public void dump() {
            System.out.println("IconInfo");
            PrintStream printStream = System.out;
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("Width: ");
            stringBuffer.append(this.Width);
            printStream.println(stringBuffer.toString());
            PrintStream printStream2 = System.out;
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2.append("Height: ");
            stringBuffer2.append(this.Height);
            printStream2.println(stringBuffer2.toString());
            PrintStream printStream3 = System.out;
            StringBuffer stringBuffer3 = new StringBuffer();
            stringBuffer3.append("ColorCount: ");
            stringBuffer3.append(this.ColorCount);
            printStream3.println(stringBuffer3.toString());
            PrintStream printStream4 = System.out;
            StringBuffer stringBuffer4 = new StringBuffer();
            stringBuffer4.append("Reserved: ");
            stringBuffer4.append(this.Reserved);
            printStream4.println(stringBuffer4.toString());
            PrintStream printStream5 = System.out;
            StringBuffer stringBuffer5 = new StringBuffer();
            stringBuffer5.append("Planes: ");
            stringBuffer5.append(this.Planes);
            printStream5.println(stringBuffer5.toString());
            PrintStream printStream6 = System.out;
            StringBuffer stringBuffer6 = new StringBuffer();
            stringBuffer6.append("BitCount: ");
            stringBuffer6.append(this.BitCount);
            printStream6.println(stringBuffer6.toString());
            PrintStream printStream7 = System.out;
            StringBuffer stringBuffer7 = new StringBuffer();
            stringBuffer7.append("ImageSize: ");
            stringBuffer7.append(this.ImageSize);
            printStream7.println(stringBuffer7.toString());
            PrintStream printStream8 = System.out;
            StringBuffer stringBuffer8 = new StringBuffer();
            stringBuffer8.append("ImageOffset: ");
            stringBuffer8.append(this.ImageOffset);
            printStream8.println(stringBuffer8.toString());
            System.out.println("");
        }
    }

    private static class ImageContents {
        public final FileHeader fileHeader;
        public final IconData[] iconDatas;

        public ImageContents(FileHeader fileHeader2, IconData[] iconDataArr) {
            this.fileHeader = fileHeader2;
            this.iconDatas = iconDataArr;
        }
    }

    public boolean embedICCProfile(File file, File file2, byte[] bArr) {
        return false;
    }

    public String getDefaultExtension() {
        return DEFAULT_EXTENSION;
    }

    public byte[] getICCProfileBytes(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        return null;
    }

    public ImageInfo getImageInfo(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        return null;
    }

    public Dimension getImageSize(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        return null;
    }

    public IImageMetadata getMetadata(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        return null;
    }

    public String getName() {
        return "ico-Custom";
    }

    public String getXmpXml(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        return null;
    }

    public IcoImageParser() {
        super.setByteOrder(73);
    }

    /* access modifiers changed from: protected */
    public String[] getAcceptedExtensions() {
        return ACCEPTED_EXTENSIONS;
    }

    /* access modifiers changed from: protected */
    public ImageFormat[] getAcceptedTypes() {
        return new ImageFormat[]{ImageFormat.IMAGE_FORMAT_ICO};
    }

    private FileHeader readFileHeader(InputStream inputStream) throws ImageReadException, IOException {
        return new FileHeader(read2Bytes("Reserved", inputStream, "Not a Valid ICO File"), read2Bytes("IconType", inputStream, "Not a Valid ICO File"), read2Bytes("IconCount", inputStream, "Not a Valid ICO File"));
    }

    private IconInfo readIconInfo(InputStream inputStream) throws ImageReadException, IOException {
        IconInfo iconInfo = new IconInfo(readByte("Width", inputStream, "Not a Valid ICO File"), readByte("Height", inputStream, "Not a Valid ICO File"), readByte("ColorCount", inputStream, "Not a Valid ICO File"), readByte("Reserved", inputStream, "Not a Valid ICO File"), read2Bytes("Planes", inputStream, "Not a Valid ICO File"), read2Bytes("BitCount", inputStream, "Not a Valid ICO File"), read4Bytes("ImageSize", inputStream, "Not a Valid ICO File"), read4Bytes("ImageOffset", inputStream, "Not a Valid ICO File"));
        return iconInfo;
    }

    private IconData readIconData(InputStream inputStream, IconInfo iconInfo) throws ImageReadException, IOException {
        byte[] bArr;
        InputStream inputStream2 = inputStream;
        IconInfo iconInfo2 = iconInfo;
        BitmapHeader bitmapHeader = new BitmapHeader(read4Bytes("Size", inputStream2, "Not a Valid ICO File"), read4Bytes("Width", inputStream2, "Not a Valid ICO File"), read4Bytes("Height", inputStream2, "Not a Valid ICO File"), read2Bytes("Planes", inputStream2, "Not a Valid ICO File"), read2Bytes("BitCount", inputStream2, "Not a Valid ICO File"), read4Bytes("Compression", inputStream2, "Not a Valid ICO File"), read4Bytes("SizeImage", inputStream2, "Not a Valid ICO File"), read4Bytes("XPelsPerMeter", inputStream2, "Not a Valid ICO File"), read4Bytes("YPelsPerMeter", inputStream2, "Not a Valid ICO File"), read4Bytes("ColorsUsed", inputStream2, "Not a Valid ICO File"), read4Bytes("ColorsImportant", inputStream2, "Not a Valid ICO File"));
        if (iconInfo2.BitCount == 1 || iconInfo2.BitCount == 4 || iconInfo2.BitCount == 8) {
            bArr = readByteArray("palette", (1 << iconInfo2.BitCount) * 4, inputStream2, "Not a Valid ICO File");
        } else {
            bArr = null;
        }
        int i = ((iconInfo2.BitCount * iconInfo2.Width) + 7) / 8;
        int i2 = i % 4;
        int i3 = i2 != 0 ? i + (4 - i2) : i;
        byte[] readByteArray = readByteArray("color_map", iconInfo2.Height * i3, inputStream2, "Not a Valid ICO File");
        int i4 = (iconInfo2.Width + 7) / 8;
        int i5 = i4 % 4;
        if (i5 != 0) {
            i4 += 4 - i5;
        }
        IconData iconData = new IconData(iconInfo, bitmapHeader, bArr, readByteArray, i3, readByteArray("transparency_map", iconInfo2.Height * i4, inputStream2, "Not a Valid ICO File"), i4);
        return iconData;
    }

    private ImageContents readImage(ByteSource byteSource) throws ImageReadException, IOException {
        InputStream inputStream;
        try {
            inputStream = byteSource.getInputStream();
            try {
                FileHeader readFileHeader = readFileHeader(inputStream);
                IconInfo[] iconInfoArr = new IconInfo[readFileHeader.iconCount];
                for (int i = 0; i < readFileHeader.iconCount; i++) {
                    iconInfoArr[i] = readIconInfo(inputStream);
                }
                IconData[] iconDataArr = new IconData[readFileHeader.iconCount];
                for (int i2 = 0; i2 < readFileHeader.iconCount; i2++) {
                    iconDataArr[i2] = readIconData(inputStream, iconInfoArr[i2]);
                }
                ImageContents imageContents = new ImageContents(readFileHeader, iconDataArr);
                try {
                    inputStream.close();
                } catch (Exception e) {
                    Debug.debug((Throwable) e);
                }
                return imageContents;
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

    public final BufferedImage getBufferedImage(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        throw new ImageReadException("Use getAllBufferedImages() instead for .ico images.");
    }

    private BufferedImage readBufferedImage(IconData iconData) throws ImageReadException {
        int i;
        int i2;
        byte b;
        IconData iconData2 = iconData;
        IconInfo iconInfo = iconData2.iconInfo;
        byte b2 = iconInfo.Width;
        byte b3 = iconInfo.Height;
        int i3 = b2 * b3;
        int[] iArr = new int[i3];
        for (int i4 = 0; i4 < b3; i4++) {
            for (int i5 = 0; i5 < b2; i5++) {
                int i6 = (((iconData2.transparency_map[(iconData2.t_scanline_size * i4) + (i5 / 8)] & UByte.MAX_VALUE) >> (7 - (i5 % 8))) & 1) == 0 ? 255 : 0;
                if (iconInfo.BitCount == 1 || iconInfo.BitCount == 4 || iconInfo.BitCount == 8) {
                    int i7 = (iconData2.scanline_size * i4 * 8) + (iconInfo.BitCount * i5);
                    int i8 = (((iconData2.color_map[i7 >> 3] & UByte.MAX_VALUE) >> ((8 - (i7 % 8)) - iconInfo.BitCount)) & ((1 << iconInfo.BitCount) - 1)) * 4;
                    byte b4 = iconData2.palette[i8 + 2] & UByte.MAX_VALUE;
                    byte b5 = iconData2.palette[i8 + 1] & UByte.MAX_VALUE;
                    b = iconData2.palette[i8 + 0] & UByte.MAX_VALUE;
                    i2 = (b4 & UByte.MAX_VALUE) << 16;
                    i = (b5 & UByte.MAX_VALUE) << 8;
                } else if (iconInfo.BitCount == 24 || iconInfo.BitCount == 32) {
                    int i9 = (iconData2.scanline_size * i4) + ((iconInfo.BitCount >> 3) * i5);
                    b = iconData2.color_map[i9 + 0] & UByte.MAX_VALUE;
                    i2 = ((iconData2.color_map[i9 + 2] & UByte.MAX_VALUE) & UByte.MAX_VALUE) << 16;
                    i = ((iconData2.color_map[i9 + 1] & UByte.MAX_VALUE) & UByte.MAX_VALUE) << 8;
                } else {
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append("Unknown BitCount: ");
                    stringBuffer.append(iconInfo.BitCount);
                    throw new ImageReadException(stringBuffer.toString());
                }
                iArr[(((b3 - i4) - 1) * b2) + i5] = ((i6 & 255) << 24) | (16777215 & (((b & UByte.MAX_VALUE) << 0) | i2 | i));
            }
        }
        ColorModel rGBdefault = ColorModel.getRGBdefault();
        return new BufferedImage(rGBdefault, Raster.createWritableRaster(rGBdefault.createCompatibleSampleModel(b2, b3), new DataBufferInt(iArr, i3), null), false, null);
    }

    public ArrayList getAllBufferedImages(ByteSource byteSource) throws ImageReadException, IOException {
        ArrayList arrayList = new ArrayList();
        ImageContents readImage = readImage(byteSource);
        FileHeader fileHeader = readImage.fileHeader;
        for (int i = 0; i < fileHeader.iconCount; i++) {
            arrayList.add(readBufferedImage(readImage.iconDatas[i]));
        }
        return arrayList;
    }
}

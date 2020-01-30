package org.apache.sanselan.formats.bmp;

public class BmpHeaderInfo {
    public final int bitmapDataOffset;
    public final int bitmapDataSize;
    public final int bitmapHeaderSize;
    public final int bitsPerPixel;
    public final int colorsImportant;
    public final int colorsUsed;
    public final int compression;
    public final int fileSize;
    public final int hResolution;
    public final int height;
    public final byte identifier1;
    public final byte identifier2;
    public final int planes;
    public final int reserved;
    public final int vResolution;
    public final int width;

    public BmpHeaderInfo(byte b, byte b2, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11, int i12, int i13, int i14) {
        this.identifier1 = b;
        this.identifier2 = b2;
        this.fileSize = i;
        this.reserved = i2;
        this.bitmapDataOffset = i3;
        this.bitmapHeaderSize = i4;
        this.width = i5;
        this.height = i6;
        this.planes = i7;
        this.bitsPerPixel = i8;
        this.compression = i9;
        this.bitmapDataSize = i10;
        this.hResolution = i11;
        this.vResolution = i12;
        this.colorsUsed = i13;
        this.colorsImportant = i14;
    }
}

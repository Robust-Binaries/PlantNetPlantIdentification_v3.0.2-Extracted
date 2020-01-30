package org.apache.sanselan.formats.bmp;

import org.apache.sanselan.formats.bmp.pixelparsers.PixelParser;

class ImageContents {
    public final BmpHeaderInfo bhi;
    public final byte[] colorTable;
    public final byte[] imageData;
    public final PixelParser pixelParser;

    public ImageContents(BmpHeaderInfo bmpHeaderInfo, byte[] bArr, byte[] bArr2, PixelParser pixelParser2) {
        this.bhi = bmpHeaderInfo;
        this.colorTable = bArr;
        this.imageData = bArr2;
        this.pixelParser = pixelParser2;
    }
}

package org.apache.sanselan.formats.bmp.pixelparsers;

import android.support.p000v4.view.ViewCompat;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import kotlin.UByte;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.formats.bmp.BmpHeaderInfo;

public class PixelParserBitFields extends PixelParserSimple {
    private final int blueMask;
    private final int blueShift;
    private int bytecount = 0;
    private final int greenMask;
    private final int greenShift;
    private final int redMask;
    private final int redShift;

    private int getMaskShift(int i) {
        int i2 = 0;
        int i3 = 0;
        while ((i & 1) == 0) {
            i = (i >> 1) & Integer.MAX_VALUE;
            i3++;
        }
        while ((i & 1) == 1) {
            i = (i >> 1) & Integer.MAX_VALUE;
            i2++;
        }
        return i3 - (8 - i2);
    }

    public PixelParserBitFields(BmpHeaderInfo bmpHeaderInfo, byte[] bArr, byte[] bArr2) throws ImageReadException, IOException {
        super(bmpHeaderInfo, bArr, bArr2);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        this.redMask = this.bfp.read4Bytes("redMask", byteArrayInputStream, "BMP BI_BITFIELDS Bad Color Table");
        this.greenMask = this.bfp.read4Bytes("greenMask", byteArrayInputStream, "BMP BI_BITFIELDS Bad Color Table");
        this.blueMask = this.bfp.read4Bytes("blueMask", byteArrayInputStream, "BMP BI_BITFIELDS Bad Color Table");
        this.redShift = getMaskShift(this.redMask);
        this.greenShift = getMaskShift(this.greenMask);
        this.blueShift = getMaskShift(this.blueMask);
    }

    public int getNextRGB() throws ImageReadException, IOException {
        int i;
        if (this.bhi.bitsPerPixel == 8) {
            byte[] bArr = this.imageData;
            int i2 = this.bytecount;
            i = bArr[i2 + 0] & UByte.MAX_VALUE;
            this.bytecount = i2 + 1;
        } else if (this.bhi.bitsPerPixel == 24) {
            i = this.bfp.read3Bytes("Pixel", this.f166is, "BMP Image Data");
            this.bytecount += 3;
        } else if (this.bhi.bitsPerPixel == 32) {
            i = this.bfp.read4Bytes("Pixel", this.f166is, "BMP Image Data");
            this.bytecount += 4;
        } else if (this.bhi.bitsPerPixel == 16) {
            i = this.bfp.read2Bytes("Pixel", this.f166is, "BMP Image Data");
            this.bytecount += 2;
        } else {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("Unknown BitsPerPixel: ");
            stringBuffer.append(this.bhi.bitsPerPixel);
            throw new ImageReadException(stringBuffer.toString());
        }
        int i3 = this.redMask & i;
        int i4 = this.greenMask & i;
        int i5 = i & this.blueMask;
        int i6 = this.redShift;
        int i7 = i6 >= 0 ? i3 >> i6 : i3 << (-i6);
        int i8 = this.greenShift;
        int i9 = i8 >= 0 ? i4 >> i8 : i4 << (-i8);
        int i10 = this.blueShift;
        return ((i10 >= 0 ? i5 >> i10 : i5 << (-i10)) << 0) | (i7 << 16) | ViewCompat.MEASURED_STATE_MASK | (i9 << 8);
    }

    public void newline() throws ImageReadException, IOException {
        while (this.bytecount % 4 != 0) {
            this.bfp.readByte("Pixel", this.f166is, "BMP Image Data");
            this.bytecount++;
        }
    }
}

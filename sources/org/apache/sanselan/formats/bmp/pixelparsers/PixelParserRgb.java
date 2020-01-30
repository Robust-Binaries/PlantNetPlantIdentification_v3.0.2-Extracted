package org.apache.sanselan.formats.bmp.pixelparsers;

import android.support.p000v4.view.ViewCompat;
import java.io.IOException;
import kotlin.UByte;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.formats.bmp.BmpHeaderInfo;

public class PixelParserRgb extends PixelParserSimple {
    private int bytecount = 0;
    private int cached_bit_count = 0;
    private int cached_byte = 0;
    int pixelCount = 0;

    public PixelParserRgb(BmpHeaderInfo bmpHeaderInfo, byte[] bArr, byte[] bArr2) {
        super(bmpHeaderInfo, bArr, bArr2);
    }

    public int getNextRGB() throws ImageReadException, IOException {
        this.pixelCount++;
        if (this.bhi.bitsPerPixel == 1 || this.bhi.bitsPerPixel == 4) {
            if (this.cached_bit_count < this.bhi.bitsPerPixel) {
                int i = this.cached_bit_count;
                if (i == 0) {
                    this.cached_bit_count = i + 8;
                    byte[] bArr = this.imageData;
                    int i2 = this.bytecount;
                    this.cached_byte = bArr[i2] & UByte.MAX_VALUE;
                    this.bytecount = i2 + 1;
                } else {
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append("Unexpected leftover bits: ");
                    stringBuffer.append(this.cached_bit_count);
                    stringBuffer.append("/");
                    stringBuffer.append(this.bhi.bitsPerPixel);
                    throw new ImageReadException(stringBuffer.toString());
                }
            }
            int i3 = ((1 << this.bhi.bitsPerPixel) - 1) & (this.cached_byte >> (8 - this.bhi.bitsPerPixel));
            this.cached_byte = (this.cached_byte << this.bhi.bitsPerPixel) & 255;
            this.cached_bit_count -= this.bhi.bitsPerPixel;
            return getColorTableRGB(i3);
        } else if (this.bhi.bitsPerPixel == 8) {
            int colorTableRGB = getColorTableRGB(this.imageData[this.bytecount + 0] & UByte.MAX_VALUE);
            this.bytecount++;
            return colorTableRGB;
        } else if (this.bhi.bitsPerPixel == 16) {
            int read2Bytes = this.bfp.read2Bytes("Pixel", this.f166is, "BMP Image Data");
            int i4 = ((((read2Bytes >> 10) & 31) << 3) << 16) | ViewCompat.MEASURED_STATE_MASK | ((((read2Bytes >> 5) & 31) << 3) << 8) | ((((read2Bytes >> 0) & 31) << 3) << 0);
            this.bytecount += 2;
            return i4;
        } else if (this.bhi.bitsPerPixel == 24) {
            byte b = this.imageData[this.bytecount + 0] & UByte.MAX_VALUE;
            byte b2 = this.imageData[this.bytecount + 1] & UByte.MAX_VALUE;
            byte[] bArr2 = this.imageData;
            int i5 = this.bytecount;
            int i6 = b << 0;
            int i7 = i6 | (b2 << 8) | ((bArr2[i5 + 2] & UByte.MAX_VALUE) << 16) | ViewCompat.MEASURED_STATE_MASK;
            this.bytecount = i5 + 3;
            return i7;
        } else if (this.bhi.bitsPerPixel == 32) {
            byte b3 = this.imageData[this.bytecount + 0] & UByte.MAX_VALUE;
            byte b4 = this.imageData[this.bytecount + 1] & UByte.MAX_VALUE;
            byte[] bArr3 = this.imageData;
            int i8 = this.bytecount;
            int i9 = b3 << 0;
            int i10 = i9 | (b4 << 8) | -16777216 | ((bArr3[i8 + 2] & UByte.MAX_VALUE) << 16);
            this.bytecount = i8 + 4;
            return i10;
        } else {
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2.append("Unknown BitsPerPixel: ");
            stringBuffer2.append(this.bhi.bitsPerPixel);
            throw new ImageReadException(stringBuffer2.toString());
        }
    }

    public void newline() throws ImageReadException, IOException {
        this.cached_bit_count = 0;
        while (this.bytecount % 4 != 0) {
            this.bfp.readByte("Pixel", this.f166is, "BMP Image Data");
            this.bytecount++;
        }
    }
}

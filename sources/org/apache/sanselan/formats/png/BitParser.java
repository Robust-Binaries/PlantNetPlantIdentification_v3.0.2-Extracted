package org.apache.sanselan.formats.png;

import java.io.IOException;
import kotlin.UByte;
import org.apache.sanselan.ImageReadException;

public class BitParser {
    private final int bitDepth;
    private final int bitsPerPixel;
    private final byte[] bytes;

    public BitParser(byte[] bArr, int i, int i2) {
        this.bytes = bArr;
        this.bitsPerPixel = i;
        this.bitDepth = i2;
    }

    public int getSample(int i, int i2) throws ImageReadException, IOException {
        int i3 = this.bitsPerPixel * i;
        int i4 = this.bitDepth;
        int i5 = ((i2 * i4) + i3) >> 3;
        if (i4 == 8) {
            return this.bytes[i5] & UByte.MAX_VALUE;
        }
        if (i4 < 8) {
            return ((1 << i4) - 1) & ((this.bytes[i5] & UByte.MAX_VALUE) >> (8 - ((i3 & 7) + i4)));
        } else if (i4 == 16) {
            byte[] bArr = this.bytes;
            return (bArr[i5 + 1] & UByte.MAX_VALUE) | ((bArr[i5] & UByte.MAX_VALUE) << 8);
        } else {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("PNG: bad BitDepth: ");
            stringBuffer.append(this.bitDepth);
            throw new ImageReadException(stringBuffer.toString());
        }
    }

    public int getSampleAsByte(int i, int i2) throws ImageReadException, IOException {
        int sample = getSample(i, i2);
        int i3 = 8 - this.bitDepth;
        if (i3 > 0) {
            sample <<= i3;
        } else if (i3 < 0) {
            sample >>= -i3;
        }
        return sample & 255;
    }
}

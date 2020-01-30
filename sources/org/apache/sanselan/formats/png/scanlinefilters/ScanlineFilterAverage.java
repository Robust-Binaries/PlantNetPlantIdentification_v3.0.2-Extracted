package org.apache.sanselan.formats.png.scanlinefilters;

import java.io.IOException;
import kotlin.UByte;
import org.apache.sanselan.ImageReadException;

public class ScanlineFilterAverage extends ScanlineFilter {
    private final int BytesPerPixel;

    public ScanlineFilterAverage(int i) {
        this.BytesPerPixel = i;
    }

    public void unfilter(byte[] bArr, byte[] bArr2, byte[] bArr3) throws ImageReadException, IOException {
        int i = 0;
        while (i < bArr.length) {
            int i2 = i - this.BytesPerPixel;
            bArr2[i] = (byte) ((bArr[i] + ((((i2 >= 0 ? bArr2[i2] : 0) & UByte.MAX_VALUE) + ((bArr3 != null ? bArr3[i] : 0) & UByte.MAX_VALUE)) / 2)) % 256);
            i++;
        }
    }
}

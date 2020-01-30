package org.apache.sanselan.formats.png.scanlinefilters;

import java.io.IOException;
import kotlin.UByte;
import org.apache.sanselan.ImageReadException;

public class ScanlineFilterPaeth extends ScanlineFilter {
    private final int BytesPerPixel;

    public ScanlineFilterPaeth(int i) {
        this.BytesPerPixel = i;
    }

    private int PaethPredictor(int i, int i2, int i3) {
        int i4 = (i + i2) - i3;
        int abs = Math.abs(i4 - i);
        int abs2 = Math.abs(i4 - i2);
        int abs3 = Math.abs(i4 - i3);
        if (abs > abs2 || abs > abs3) {
            return abs2 <= abs3 ? i2 : i3;
        }
        return i;
    }

    public void unfilter(byte[] bArr, byte[] bArr2, byte[] bArr3) throws ImageReadException, IOException {
        int i = 0;
        while (i < bArr.length) {
            int i2 = i - this.BytesPerPixel;
            bArr2[i] = (byte) ((bArr[i] + PaethPredictor((i2 >= 0 ? bArr2[i2] : 0) & UByte.MAX_VALUE, (bArr3 != null ? bArr3[i] : 0) & UByte.MAX_VALUE, ((i2 < 0 || bArr3 == null) ? 0 : bArr3[i2]) & UByte.MAX_VALUE)) % 256);
            i++;
        }
    }
}

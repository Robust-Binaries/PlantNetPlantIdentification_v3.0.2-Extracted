package org.apache.sanselan.formats.png.scanlinefilters;

import java.io.IOException;
import org.apache.sanselan.ImageReadException;

public class ScanlineFilterSub extends ScanlineFilter {
    private final int BytesPerPixel;

    public ScanlineFilterSub(int i) {
        this.BytesPerPixel = i;
    }

    public void unfilter(byte[] bArr, byte[] bArr2, byte[] bArr3) throws ImageReadException, IOException {
        for (int i = 0; i < bArr.length; i++) {
            int i2 = i - this.BytesPerPixel;
            if (i2 >= 0) {
                bArr2[i] = (byte) ((bArr[i] + bArr2[i2]) % 256);
            } else {
                bArr2[i] = bArr[i];
            }
        }
    }
}

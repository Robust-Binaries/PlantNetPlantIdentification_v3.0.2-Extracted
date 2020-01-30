package org.apache.sanselan.formats.png.scanlinefilters;

import java.io.IOException;
import org.apache.sanselan.ImageReadException;

public class ScanlineFilterUp extends ScanlineFilter {
    private final int BytesPerPixel;

    public ScanlineFilterUp(int i) {
        this.BytesPerPixel = i;
    }

    public void unfilter(byte[] bArr, byte[] bArr2, byte[] bArr3) throws ImageReadException, IOException {
        for (int i = 0; i < bArr.length; i++) {
            if (bArr3 != null) {
                bArr2[i] = (byte) ((bArr[i] + bArr3[i]) % 256);
            } else {
                bArr2[i] = bArr[i];
            }
        }
    }
}
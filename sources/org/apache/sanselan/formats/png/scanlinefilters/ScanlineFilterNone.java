package org.apache.sanselan.formats.png.scanlinefilters;

import java.io.IOException;
import org.apache.sanselan.ImageReadException;

public class ScanlineFilterNone extends ScanlineFilter {
    public void unfilter(byte[] bArr, byte[] bArr2, byte[] bArr3) throws ImageReadException, IOException {
        for (int i = 0; i < bArr.length; i++) {
            bArr2[i] = bArr[i];
        }
    }
}

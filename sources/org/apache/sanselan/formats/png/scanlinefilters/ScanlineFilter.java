package org.apache.sanselan.formats.png.scanlinefilters;

import java.io.IOException;
import org.apache.sanselan.ImageReadException;

public abstract class ScanlineFilter {
    public abstract void unfilter(byte[] bArr, byte[] bArr2, byte[] bArr3) throws ImageReadException, IOException;
}

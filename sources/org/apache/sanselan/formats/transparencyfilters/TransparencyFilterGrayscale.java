package org.apache.sanselan.formats.transparencyfilters;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.sanselan.ImageReadException;

public class TransparencyFilterGrayscale extends TransparencyFilter {
    private final int transparent_color;

    public TransparencyFilterGrayscale(byte[] bArr) throws ImageReadException, IOException {
        super(bArr);
        this.transparent_color = read2Bytes("transparent_color", new ByteArrayInputStream(bArr), "tRNS: Missing transparent_color");
    }

    public int filter(int i, int i2) throws ImageReadException, IOException {
        if (i2 != this.transparent_color) {
            return i;
        }
        return 0;
    }
}

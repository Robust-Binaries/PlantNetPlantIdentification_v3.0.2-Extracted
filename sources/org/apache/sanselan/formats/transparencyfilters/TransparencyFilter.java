package org.apache.sanselan.formats.transparencyfilters;

import java.io.IOException;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.common.BinaryFileParser;

public abstract class TransparencyFilter extends BinaryFileParser {
    protected final byte[] bytes;

    public abstract int filter(int i, int i2) throws ImageReadException, IOException;

    public TransparencyFilter(byte[] bArr) {
        this.bytes = bArr;
    }
}

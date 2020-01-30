package org.apache.sanselan.icc;

import java.io.IOException;
import org.apache.sanselan.ImageReadException;

public abstract class IccTagDataType {
    public final String name;
    public final int signature;

    public abstract void dump(String str, byte[] bArr) throws ImageReadException, IOException;

    public IccTagDataType(String str, int i) {
        this.name = str;
        this.signature = i;
    }
}

package org.apache.sanselan.formats.psd;

import java.io.IOException;
import org.apache.sanselan.ImageReadException;

public class ImageResourceType {
    public final String Description;

    /* renamed from: ID */
    public final int f171ID;

    public ImageResourceType(int i, String str) {
        this.f171ID = i;
        this.Description = str;
    }

    public ImageResourceType(int i, int i2, String str) throws ImageReadException, IOException {
        this(i, str);
        if (i != i2) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("Mismatch ID: ");
            stringBuffer.append(i);
            stringBuffer.append(" ID2: ");
            stringBuffer.append(i2);
            throw new ImageReadException(stringBuffer.toString());
        }
    }
}

package org.apache.sanselan.formats.transparencyfilters;

import android.support.p000v4.view.ViewCompat;
import java.io.IOException;
import kotlin.UByte;
import org.apache.sanselan.ImageReadException;

public class TransparencyFilterIndexedColor extends TransparencyFilter {
    int count = 0;

    public TransparencyFilterIndexedColor(byte[] bArr) {
        super(bArr);
    }

    public int filter(int i, int i2) throws ImageReadException, IOException {
        if (i2 >= this.bytes.length) {
            return i;
        }
        if (i2 < 0 || i2 > this.bytes.length) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("TransparencyFilterIndexedColor index: ");
            stringBuffer.append(i2);
            stringBuffer.append(", bytes.length: ");
            stringBuffer.append(this.bytes.length);
            throw new ImageReadException(stringBuffer.toString());
        }
        int i3 = (i & ViewCompat.MEASURED_SIZE_MASK) | ((this.bytes[i2] & UByte.MAX_VALUE) << 24);
        int i4 = this.count;
        if (i4 < 100 && i2 > 0) {
            this.count = i4 + 1;
        }
        return i3;
    }
}

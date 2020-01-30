package org.apache.sanselan.formats.psd.dataparsers;

import android.support.p000v4.view.ViewCompat;
import org.apache.sanselan.formats.psd.ImageContents;

public class DataParserRGB extends DataParser {
    public int getBasicChannelsCount() {
        return 3;
    }

    /* access modifiers changed from: protected */
    public int getRGB(int[][][] iArr, int i, int i2, ImageContents imageContents) {
        int i3 = iArr[0][i2][i] & 255;
        int i4 = iArr[1][i2][i] & 255;
        return (((iArr[2][i2][i] & 255) & 255) << 0) | ((i3 & 255) << 16) | ViewCompat.MEASURED_STATE_MASK | ((i4 & 255) << 8);
    }
}

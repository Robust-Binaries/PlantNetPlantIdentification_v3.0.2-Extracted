package org.apache.sanselan.formats.psd.dataparsers;

import android.support.p000v4.view.ViewCompat;
import org.apache.sanselan.formats.psd.ImageContents;

public class DataParserGrayscale extends DataParser {
    public int getBasicChannelsCount() {
        return 1;
    }

    /* access modifiers changed from: protected */
    public int getRGB(int[][][] iArr, int i, int i2, ImageContents imageContents) {
        int i3 = iArr[0][i2][i] & 255 & 255;
        return (i3 << 0) | (i3 << 16) | ViewCompat.MEASURED_STATE_MASK | (i3 << 8);
    }
}

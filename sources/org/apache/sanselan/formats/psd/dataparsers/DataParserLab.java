package org.apache.sanselan.formats.psd.dataparsers;

import org.apache.sanselan.color.ColorConversions;
import org.apache.sanselan.formats.psd.ImageContents;

public class DataParserLab extends DataParser {
    public void dump() {
    }

    public int getBasicChannelsCount() {
        return 3;
    }

    /* access modifiers changed from: protected */
    public int getRGB(int[][][] iArr, int i, int i2, ImageContents imageContents) {
        return ColorConversions.convertCIELabtoARGBTest(iArr[0][i2][i] & 255, (iArr[1][i2][i] & 255) - 128, (iArr[2][i2][i] & 255) - 128);
    }
}

package org.apache.sanselan.formats.psd.dataparsers;

import org.apache.sanselan.color.ColorConversions;
import org.apache.sanselan.formats.psd.ImageContents;

public class DataParserCMYK extends DataParser {
    public int getBasicChannelsCount() {
        return 4;
    }

    /* access modifiers changed from: protected */
    public int getRGB(int[][][] iArr, int i, int i2, ImageContents imageContents) {
        return ColorConversions.convertCMYKtoRGB(255 - (iArr[0][i2][i] & 255), 255 - (iArr[1][i2][i] & 255), 255 - (iArr[2][i2][i] & 255), 255 - (iArr[3][i2][i] & 255));
    }
}

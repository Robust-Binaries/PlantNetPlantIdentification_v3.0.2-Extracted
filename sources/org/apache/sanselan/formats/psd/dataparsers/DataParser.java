package org.apache.sanselan.formats.psd.dataparsers;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import org.apache.sanselan.formats.psd.ImageContents;
import org.apache.sanselan.formats.psd.PSDHeaderInfo;

public abstract class DataParser {
    public void dump() {
    }

    public abstract int getBasicChannelsCount();

    /* access modifiers changed from: protected */
    public abstract int getRGB(int[][][] iArr, int i, int i2, ImageContents imageContents);

    public final void parseData(int[][][] iArr, BufferedImage bufferedImage, ImageContents imageContents) {
        DataBuffer dataBuffer = bufferedImage.getRaster().getDataBuffer();
        PSDHeaderInfo pSDHeaderInfo = imageContents.header;
        int i = pSDHeaderInfo.Columns;
        int i2 = pSDHeaderInfo.Rows;
        for (int i3 = 0; i3 < i2; i3++) {
            for (int i4 = 0; i4 < i; i4++) {
                dataBuffer.setElem((i3 * i) + i4, getRGB(iArr, i4, i3, imageContents));
            }
        }
    }
}

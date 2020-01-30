package org.apache.sanselan.formats.psd.dataparsers;

import android.support.p000v4.view.ViewCompat;
import kotlin.UByte;
import org.apache.sanselan.formats.psd.ImageContents;

public class DataParserIndexed extends DataParser {
    private final int[] ColorTable = new int[256];

    public int getBasicChannelsCount() {
        return 1;
    }

    public DataParserIndexed(byte[] bArr) {
        for (int i = 0; i < 256; i++) {
            this.ColorTable[i] = (((bArr[i + 0] & UByte.MAX_VALUE) & UByte.MAX_VALUE) << 16) | ViewCompat.MEASURED_STATE_MASK | (((bArr[i + 256] & UByte.MAX_VALUE) & UByte.MAX_VALUE) << 8) | (((bArr[i + 512] & UByte.MAX_VALUE) & UByte.MAX_VALUE) << 0);
        }
    }

    /* access modifiers changed from: protected */
    public int getRGB(int[][][] iArr, int i, int i2, ImageContents imageContents) {
        return this.ColorTable[iArr[0][i2][i] & 255];
    }
}

package org.apache.sanselan.formats.png.chunks;

import java.io.ByteArrayInputStream;
import org.apache.sanselan.common.BinaryFileParser;

public class PNGChunk extends BinaryFileParser {
    public final boolean ancillary;
    public final byte[] bytes;
    public final int chunkType;
    public final int crc;
    public final boolean isPrivate;
    public final int length;
    public final boolean[] propertyBits = new boolean[4];
    public final boolean reserved;
    public final boolean safeToCopy;

    public PNGChunk(int i, int i2, int i3, byte[] bArr) {
        this.length = i;
        this.chunkType = i2;
        this.crc = i3;
        this.bytes = bArr;
        int i4 = 0;
        int i5 = 24;
        while (true) {
            boolean z = true;
            if (i4 < 4) {
                int i6 = (i2 >> i5) & 255;
                i5 -= 8;
                boolean[] zArr = this.propertyBits;
                if ((i6 & 32) <= 0) {
                    z = false;
                }
                zArr[i4] = z;
                i4++;
            } else {
                boolean[] zArr2 = this.propertyBits;
                this.ancillary = zArr2[0];
                this.isPrivate = zArr2[1];
                this.reserved = zArr2[2];
                this.safeToCopy = zArr2[3];
                return;
            }
        }
    }

    /* access modifiers changed from: protected */
    public ByteArrayInputStream getDataStream() {
        return new ByteArrayInputStream(this.bytes);
    }
}

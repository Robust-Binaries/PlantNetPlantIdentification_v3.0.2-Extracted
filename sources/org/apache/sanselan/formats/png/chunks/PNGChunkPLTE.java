package org.apache.sanselan.formats.png.chunks;

import android.support.p000v4.view.ViewCompat;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import kotlin.UByte;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.formats.png.GammaCorrection;

public class PNGChunkPLTE extends PNGChunk {
    public final int[] rgb;

    public PNGChunkPLTE(int i, int i2, int i3, byte[] bArr) throws ImageReadException, IOException {
        super(i, i2, i3, bArr);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        if (i % 3 == 0) {
            int i4 = i / 3;
            this.rgb = new int[i4];
            for (int i5 = 0; i5 < i4; i5++) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("red[");
                stringBuffer.append(i5);
                stringBuffer.append("]");
                byte readByte = readByte(stringBuffer.toString(), byteArrayInputStream, "Not a Valid Png File: PLTE Corrupt");
                StringBuffer stringBuffer2 = new StringBuffer();
                stringBuffer2.append("green[");
                stringBuffer2.append(i5);
                stringBuffer2.append("]");
                byte readByte2 = readByte(stringBuffer2.toString(), byteArrayInputStream, "Not a Valid Png File: PLTE Corrupt");
                StringBuffer stringBuffer3 = new StringBuffer();
                stringBuffer3.append("blue[");
                stringBuffer3.append(i5);
                stringBuffer3.append("]");
                this.rgb[i5] = ((readByte & UByte.MAX_VALUE) << 16) | ViewCompat.MEASURED_STATE_MASK | ((readByte2 & UByte.MAX_VALUE) << 8) | ((readByte(stringBuffer3.toString(), byteArrayInputStream, "Not a Valid Png File: PLTE Corrupt") & UByte.MAX_VALUE) << 0);
            }
            return;
        }
        StringBuffer stringBuffer4 = new StringBuffer();
        stringBuffer4.append("PLTE: wrong length: ");
        stringBuffer4.append(i);
        throw new ImageReadException(stringBuffer4.toString());
    }

    public int getRGB(int i) throws ImageReadException, IOException {
        if (i >= 0) {
            int[] iArr = this.rgb;
            if (i < iArr.length) {
                return iArr[i];
            }
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("PNG: unknown Palette reference: ");
        stringBuffer.append(i);
        throw new ImageReadException(stringBuffer.toString());
    }

    public void correct(GammaCorrection gammaCorrection) {
        int i = 0;
        while (true) {
            int[] iArr = this.rgb;
            if (i < iArr.length) {
                iArr[i] = gammaCorrection.correctARGB(iArr[i]);
                i++;
            } else {
                return;
            }
        }
    }
}

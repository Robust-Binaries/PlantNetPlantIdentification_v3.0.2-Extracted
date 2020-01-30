package org.apache.sanselan.formats.tiff.photometricinterpreters;

import android.support.p000v4.view.ViewCompat;
import java.awt.image.BufferedImage;
import java.io.IOException;
import org.apache.sanselan.ImageReadException;

public class PhotometricInterpreterPalette extends PhotometricInterpreter {
    private final int[] fColorMap;

    public PhotometricInterpreterPalette(int i, int[] iArr, int i2, int i3, int i4, int[] iArr2) {
        super(i, iArr, i2, i3, i4);
        this.fColorMap = iArr2;
    }

    public void interpretPixel(BufferedImage bufferedImage, int[] iArr, int i, int i2) throws ImageReadException, IOException {
        int i3 = 1 << this.bitsPerSample[0];
        int i4 = iArr[0];
        int[] iArr2 = this.fColorMap;
        int i5 = iArr2[i4] >> 8;
        int i6 = iArr2[i4 + i3] >> 8;
        bufferedImage.setRGB(i, i2, ((iArr2[i4 + (i3 * 2)] >> 8) << 0) | (i5 << 16) | ViewCompat.MEASURED_STATE_MASK | (i6 << 8));
    }
}

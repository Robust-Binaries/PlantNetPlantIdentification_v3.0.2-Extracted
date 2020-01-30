package org.apache.sanselan.formats.tiff.photometricinterpreters;

import android.support.p000v4.view.ViewCompat;
import java.awt.image.BufferedImage;
import java.io.IOException;
import org.apache.sanselan.ImageReadException;

public class PhotometricInterpreterRGB extends PhotometricInterpreter {
    public PhotometricInterpreterRGB(int i, int[] iArr, int i2, int i3, int i4) {
        super(i, iArr, i2, i3, i4);
    }

    public void interpretPixel(BufferedImage bufferedImage, int[] iArr, int i, int i2) throws ImageReadException, IOException {
        int i3 = iArr[0];
        int i4 = iArr[1];
        bufferedImage.setRGB(i, i2, (iArr[2] << 0) | (i3 << 16) | ViewCompat.MEASURED_STATE_MASK | (i4 << 8));
    }
}

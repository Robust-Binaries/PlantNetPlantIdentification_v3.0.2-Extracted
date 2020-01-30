package org.apache.sanselan.formats.tiff.photometricinterpreters;

import java.awt.image.BufferedImage;
import java.io.IOException;
import org.apache.sanselan.ImageReadException;

public class PhotometricInterpreterBiLevel extends PhotometricInterpreter {
    private final boolean invert;

    public PhotometricInterpreterBiLevel(int i, int i2, int[] iArr, int i3, int i4, int i5, boolean z) {
        super(i2, iArr, i3, i4, i5);
        this.invert = z;
    }

    public void interpretPixel(BufferedImage bufferedImage, int[] iArr, int i, int i2) throws ImageReadException, IOException {
        int i3 = iArr[0];
        if (this.invert) {
            i3 = 255 - i3;
        }
        bufferedImage.setRGB(i, i2, (i3 << 0) | -16777216 | (i3 << 16) | (i3 << 8));
    }
}

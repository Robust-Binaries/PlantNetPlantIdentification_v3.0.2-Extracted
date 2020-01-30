package org.apache.sanselan.formats.tiff.photometricinterpreters;

import java.awt.image.BufferedImage;
import java.io.IOException;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.color.ColorConversions;

public class PhotometricInterpreterCMYK extends PhotometricInterpreter {
    public PhotometricInterpreterCMYK(int i, int[] iArr, int i2, int i3, int i4) {
        super(i, iArr, i2, i3, i4);
    }

    public void interpretPixel(BufferedImage bufferedImage, int[] iArr, int i, int i2) throws ImageReadException, IOException {
        bufferedImage.setRGB(i, i2, ColorConversions.convertCMYKtoRGB(iArr[0], iArr[1], iArr[2], iArr[3]));
    }
}

package org.apache.sanselan.formats.tiff.photometricinterpreters;

import android.support.p000v4.view.ViewCompat;
import java.awt.image.BufferedImage;
import java.io.IOException;
import org.apache.sanselan.ImageReadException;

public class PhotometricInterpreterLogLUV extends PhotometricInterpreter {
    private float cube(float f) {
        return f * f * f;
    }

    public void dumpstats() throws ImageReadException, IOException {
    }

    public PhotometricInterpreterLogLUV(int i, int[] iArr, int i2, int i3, int i4, boolean z) {
        super(i, iArr, i2, i3, i4);
    }

    public void interpretPixel(BufferedImage bufferedImage, int[] iArr, int i, int i2) throws ImageReadException, IOException {
        float f = (((((float) iArr[0]) * 100.0f) / 255.0f) + 16.0f) / 116.0f;
        float f2 = (((float) ((byte) iArr[1])) / 500.0f) + f;
        float f3 = f - (((float) ((byte) iArr[2])) / 200.0f);
        float cube = cube(f2);
        float cube2 = cube(f);
        float cube3 = cube(f3);
        if (cube2 <= 0.008856f) {
            cube2 = (f - 0.13793103f) / 7.787f;
        }
        if (cube <= 0.008856f) {
            cube = (f2 - 0.13793103f) / 7.787f;
        }
        if (cube3 <= 0.008856f) {
            cube3 = (f3 - 0.13793103f) / 7.787f;
        }
        float f4 = (cube * 95.047f) / 100.0f;
        float f5 = (cube2 * 100.0f) / 100.0f;
        float f6 = (cube3 * 108.883f) / 100.0f;
        float f7 = (3.2406f * f4) + (-1.5372f * f5) + (-0.4986f * f6);
        float f8 = (-0.9689f * f4) + (1.8758f * f5) + (0.0415f * f6);
        float f9 = (f4 * 0.0557f) + (f5 * -0.204f) + (f6 * 1.057f);
        double d = (double) f7;
        float pow = d > 0.0031308d ? (((float) Math.pow(d, 0.4166666666666667d)) * 1.055f) - 0.055f : f7 * 12.92f;
        double d2 = (double) f8;
        float pow2 = d2 > 0.0031308d ? (((float) Math.pow(d2, 0.4166666666666667d)) * 1.055f) - 0.055f : f8 * 12.92f;
        double d3 = (double) f9;
        bufferedImage.setRGB(i, i2, (Math.min(255, Math.max(0, (int) ((d3 > 0.0031308d ? (((float) Math.pow(d3, 0.4166666666666667d)) * 1.055f) - 0.055f : f9 * 12.92f) * 255.0f))) << 0) | (Math.min(255, Math.max(0, (int) (pow * 255.0f))) << 16) | ViewCompat.MEASURED_STATE_MASK | (Math.min(255, Math.max(0, (int) (pow2 * 255.0f))) << 8));
    }
}

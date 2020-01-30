package org.apache.sanselan.formats.tiff.photometricinterpreters;

import android.support.p000v4.view.ViewCompat;
import java.awt.image.BufferedImage;
import java.io.IOException;
import org.apache.sanselan.ImageReadException;

public class PhotometricInterpreterYCbCr extends PhotometricInterpreter {
    public PhotometricInterpreterYCbCr(double[] dArr, int[] iArr, int[] iArr2, double[] dArr2, int i, int[] iArr3, int i2, int i3, int i4) {
        super(i, iArr3, i2, i3, i4);
    }

    public int limit(int i, int i2, int i3) {
        return Math.min(i3, Math.max(i2, i));
    }

    public int convertYCbCrtoRGB(int i, int i2, int i3) {
        double d = (double) i;
        Double.isNaN(d);
        double d2 = (d - 16.0d) * 1.164d;
        double d3 = (double) i3;
        Double.isNaN(d3);
        double d4 = d3 - 128.0d;
        double d5 = (1.596d * d4) + d2;
        double d6 = d2 - (d4 * 0.813d);
        double d7 = (double) i2;
        Double.isNaN(d7);
        double d8 = d7 - 128.0d;
        double d9 = d6 - (0.392d * d8);
        double d10 = d2 + (d8 * 2.017d);
        return (limit((int) d5, 0, 255) << 16) | ViewCompat.MEASURED_STATE_MASK | (limit((int) d9, 0, 255) << 8) | (limit((int) d10, 0, 255) << 0);
    }

    public void interpretPixel(BufferedImage bufferedImage, int[] iArr, int i, int i2) throws ImageReadException, IOException {
        int i3 = iArr[0];
        int i4 = iArr[1];
        double d = (double) i3;
        double d2 = (double) iArr[2];
        Double.isNaN(d2);
        double d3 = d2 - 128.0d;
        double d4 = 1.402d * d3;
        Double.isNaN(d);
        double d5 = d4 + d;
        double d6 = (double) i4;
        Double.isNaN(d6);
        double d7 = d6 - 128.0d;
        double d8 = 0.34414d * d7;
        Double.isNaN(d);
        double d9 = (d - d8) - (d3 * 0.71414d);
        double d10 = d7 * 1.772d;
        Double.isNaN(d);
        double d11 = d + d10;
        int limit = limit((int) d11, 0, 255) << 0;
        bufferedImage.setRGB(i, i2, limit | (limit((int) d5, 0, 255) << 16) | ViewCompat.MEASURED_STATE_MASK | (limit((int) d9, 0, 255) << 8));
    }
}

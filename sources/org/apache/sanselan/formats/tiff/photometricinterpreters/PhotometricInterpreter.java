package org.apache.sanselan.formats.tiff.photometricinterpreters;

import java.awt.image.BufferedImage;
import java.io.IOException;
import org.apache.sanselan.ImageReadException;

public abstract class PhotometricInterpreter {
    protected final int[] bitsPerSample;
    protected final int height;
    protected final int predictor;
    protected final int samplesPerPixel;
    protected final int width;

    public void dumpstats() throws ImageReadException, IOException {
    }

    public abstract void interpretPixel(BufferedImage bufferedImage, int[] iArr, int i, int i2) throws ImageReadException, IOException;

    public PhotometricInterpreter(int i, int[] iArr, int i2, int i3, int i4) {
        this.samplesPerPixel = i;
        this.bitsPerSample = iArr;
        this.predictor = i2;
        this.width = i3;
        this.height = i4;
    }
}

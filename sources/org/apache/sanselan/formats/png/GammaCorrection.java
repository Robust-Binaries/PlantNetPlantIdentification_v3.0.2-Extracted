package org.apache.sanselan.formats.png;

public class GammaCorrection {
    private static final boolean DEBUG = false;
    private final int[] lookupTable = new int[256];

    public GammaCorrection(double d, double d2) {
        for (int i = 0; i < 256; i++) {
            this.lookupTable[i] = correctSample(i, d, d2);
        }
    }

    public int correctSample(int i) {
        return this.lookupTable[i];
    }

    public int correctARGB(int i) {
        int i2 = -16777216 & i;
        int i3 = (i >> 8) & 255;
        int i4 = (i >> 0) & 255;
        int correctSample = correctSample((i >> 16) & 255);
        return ((correctSample(i4) & 255) << 0) | i2 | ((correctSample & 255) << 16) | ((correctSample(i3) & 255) << 8);
    }

    private int correctSample(int i, double d, double d2) {
        double d3 = (double) i;
        Double.isNaN(d3);
        return (int) Math.round(Math.pow(d3 / 255.0d, d / d2) * 255.0d);
    }
}

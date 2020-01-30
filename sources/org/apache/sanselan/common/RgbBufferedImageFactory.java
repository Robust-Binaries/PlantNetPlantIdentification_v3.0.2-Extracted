package org.apache.sanselan.common;

import java.awt.image.BufferedImage;

public class RgbBufferedImageFactory implements IBufferedImageFactory {
    public BufferedImage getColorBufferedImage(int i, int i2, boolean z) {
        if (z) {
            return new BufferedImage(i, i2, 2);
        }
        return new BufferedImage(i, i2, 1);
    }

    public BufferedImage getGrayscaleBufferedImage(int i, int i2, boolean z) {
        return getColorBufferedImage(i, i2, z);
    }
}

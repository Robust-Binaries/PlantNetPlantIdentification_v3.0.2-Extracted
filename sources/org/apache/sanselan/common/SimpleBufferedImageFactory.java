package org.apache.sanselan.common;

import java.awt.image.BufferedImage;

public class SimpleBufferedImageFactory implements IBufferedImageFactory {
    public BufferedImage getColorBufferedImage(int i, int i2, boolean z) {
        if (z) {
            return new BufferedImage(i, i2, 2);
        }
        return new BufferedImage(i, i2, 1);
    }

    public BufferedImage getGrayscaleBufferedImage(int i, int i2, boolean z) {
        if (z) {
            return new BufferedImage(i, i2, 2);
        }
        return new BufferedImage(i, i2, 10);
    }
}

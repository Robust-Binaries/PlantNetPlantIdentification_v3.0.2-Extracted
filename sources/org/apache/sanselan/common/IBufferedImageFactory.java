package org.apache.sanselan.common;

import java.awt.image.BufferedImage;

public interface IBufferedImageFactory {
    BufferedImage getColorBufferedImage(int i, int i2, boolean z);

    BufferedImage getGrayscaleBufferedImage(int i, int i2, boolean z);
}

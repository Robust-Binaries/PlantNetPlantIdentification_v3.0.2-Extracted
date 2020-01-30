package org.apache.sanselan.formats.bmp.pixelparsers;

import java.awt.image.BufferedImage;
import java.io.IOException;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.formats.bmp.BmpHeaderInfo;

public abstract class PixelParserSimple extends PixelParser {
    public abstract int getNextRGB() throws ImageReadException, IOException;

    public abstract void newline() throws ImageReadException, IOException;

    public PixelParserSimple(BmpHeaderInfo bmpHeaderInfo, byte[] bArr, byte[] bArr2) {
        super(bmpHeaderInfo, bArr, bArr2);
    }

    public void processImage(BufferedImage bufferedImage) throws ImageReadException, IOException {
        for (int i = this.bhi.height - 1; i >= 0; i--) {
            for (int i2 = 0; i2 < this.bhi.width; i2++) {
                bufferedImage.setRGB(i2, i, getNextRGB());
            }
            newline();
        }
    }
}

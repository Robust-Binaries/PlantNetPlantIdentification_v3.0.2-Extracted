package org.apache.sanselan.formats.png;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.formats.png.chunks.PNGChunkPLTE;
import org.apache.sanselan.formats.transparencyfilters.TransparencyFilter;

public class ScanExpediterSimple extends ScanExpediter {
    public ScanExpediterSimple(int i, int i2, InputStream inputStream, BufferedImage bufferedImage, int i3, int i4, int i5, PNGChunkPLTE pNGChunkPLTE, GammaCorrection gammaCorrection, TransparencyFilter transparencyFilter) {
        super(i, i2, inputStream, bufferedImage, i3, i4, i5, pNGChunkPLTE, gammaCorrection, transparencyFilter);
    }

    public void drive() throws ImageReadException, IOException {
        int bitsToBytesRoundingUp = getBitsToBytesRoundingUp(this.bitsPerPixel * this.width);
        byte[] bArr = null;
        for (int i = 0; i < this.height; i++) {
            bArr = getNextScanline(this.f168is, bitsToBytesRoundingUp, bArr, this.bytesPerPixel);
            BitParser bitParser = new BitParser(bArr, this.bitsPerPixel, this.bitDepth);
            for (int i2 = 0; i2 < this.width; i2++) {
                this.f167bi.setRGB(i2, i, getRGB(bitParser, i2));
            }
        }
    }
}

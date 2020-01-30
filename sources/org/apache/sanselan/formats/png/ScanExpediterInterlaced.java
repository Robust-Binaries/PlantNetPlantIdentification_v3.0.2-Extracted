package org.apache.sanselan.formats.png;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.formats.png.chunks.PNGChunkPLTE;
import org.apache.sanselan.formats.transparencyfilters.TransparencyFilter;

public class ScanExpediterInterlaced extends ScanExpediter {
    public static final int[] Block_Height = {8, 8, 4, 4, 2, 2, 1};
    public static final int[] Block_Width = {8, 4, 4, 2, 2, 1, 1};
    public static final int[] Col_Increment = {8, 8, 4, 4, 2, 2, 1};
    public static final int[] Row_Increment = {8, 8, 8, 4, 4, 2, 2};
    public static final int[] Starting_Col = {0, 4, 0, 2, 0, 1, 0};
    public static final int[] Starting_Row = {0, 0, 4, 0, 2, 0, 1};

    public ScanExpediterInterlaced(int i, int i2, InputStream inputStream, BufferedImage bufferedImage, int i3, int i4, int i5, PNGChunkPLTE pNGChunkPLTE, GammaCorrection gammaCorrection, TransparencyFilter transparencyFilter) {
        super(i, i2, inputStream, bufferedImage, i3, i4, i5, pNGChunkPLTE, gammaCorrection, transparencyFilter);
    }

    private void visit(int i, int i2, BufferedImage bufferedImage, BitParser bitParser, int i3, int i4, PNGChunkPLTE pNGChunkPLTE, GammaCorrection gammaCorrection) throws ImageReadException, IOException {
        bufferedImage.setRGB(i, i2, getRGB(bitParser, i4));
    }

    public void drive() throws ImageReadException, IOException {
        for (int i = 1; i <= 7; i++) {
            byte[] bArr = null;
            int i2 = i - 1;
            int i3 = Starting_Row[i2];
            int i4 = this.height;
            for (int i5 = i3; i5 < this.height; i5 += Row_Increment[i2]) {
                int i6 = Starting_Col[i2];
                if (i6 < this.width) {
                    byte[] nextScanline = getNextScanline(this.f168is, getBitsToBytesRoundingUp(this.bitsPerPixel * ((((this.width - Starting_Col[i2]) - 1) / Col_Increment[i2]) + 1)), bArr, this.bytesPerPixel);
                    BitParser bitParser = new BitParser(nextScanline, this.bitsPerPixel, this.bitDepth);
                    int i7 = i6;
                    int i8 = 0;
                    while (i7 < this.width) {
                        int i9 = i7;
                        visit(i7, i5, this.f167bi, bitParser, this.colorType, i8, this.pngChunkPLTE, this.gammaCorrection);
                        i7 = i9 + Col_Increment[i2];
                        i8++;
                    }
                    bArr = nextScanline;
                }
            }
        }
    }
}

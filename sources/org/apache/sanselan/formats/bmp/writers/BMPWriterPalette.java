package org.apache.sanselan.formats.bmp.writers;

import android.support.p000v4.view.ViewCompat;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.sanselan.common.BinaryOutputStream;
import org.apache.sanselan.palette.SimplePalette;

public class BMPWriterPalette extends BMPWriter {
    private final int bitsPerSample;
    private final SimplePalette palette;

    public BMPWriterPalette(SimplePalette simplePalette) {
        this.palette = simplePalette;
        if (simplePalette.length() <= 2) {
            this.bitsPerSample = 1;
        } else if (simplePalette.length() <= 16) {
            this.bitsPerSample = 4;
        } else {
            this.bitsPerSample = 8;
        }
    }

    public int getPaletteSize() {
        return this.palette.length();
    }

    public int getBitsPerPixel() {
        return this.bitsPerSample;
    }

    public void writePalette(BinaryOutputStream binaryOutputStream) throws IOException {
        for (int i = 0; i < this.palette.length(); i++) {
            int entry = this.palette.getEntry(i);
            int i2 = (entry >> 16) & 255;
            int i3 = (entry >> 8) & 255;
            binaryOutputStream.write((entry >> 0) & 255);
            binaryOutputStream.write(i3);
            binaryOutputStream.write(i2);
            binaryOutputStream.write(0);
        }
    }

    public byte[] getImageData(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        for (int i4 = height - 1; i4 >= 0; i4--) {
            int i5 = i3;
            int i6 = i;
            for (int i7 = 0; i7 < width; i7++) {
                int paletteIndex = this.palette.getPaletteIndex(bufferedImage.getRGB(i7, i4) & ViewCompat.MEASURED_SIZE_MASK);
                int i8 = this.bitsPerSample;
                if (i8 == 8) {
                    byteArrayOutputStream.write(paletteIndex & 255);
                    i5++;
                } else {
                    i6 = (i6 << i8) | paletteIndex;
                    i2 += i8;
                    if (i2 >= 8) {
                        byteArrayOutputStream.write(i6 & 255);
                        i5++;
                        i2 = 0;
                        i6 = 0;
                    }
                }
            }
            if (i2 > 0) {
                byteArrayOutputStream.write((i6 << (8 - i2)) & 255);
                i3 = i5 + 1;
                i = 0;
                i2 = 0;
            } else {
                i = i6;
                i3 = i5;
            }
            while (i3 % 4 != 0) {
                byteArrayOutputStream.write(0);
                i3++;
            }
        }
        return byteArrayOutputStream.toByteArray();
    }
}

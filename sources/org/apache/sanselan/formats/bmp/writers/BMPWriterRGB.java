package org.apache.sanselan.formats.bmp.writers;

import android.support.p000v4.view.ViewCompat;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.sanselan.common.BinaryOutputStream;

public class BMPWriterRGB extends BMPWriter {
    public int getBitsPerPixel() {
        return 24;
    }

    public int getPaletteSize() {
        return 0;
    }

    public void writePalette(BinaryOutputStream binaryOutputStream) throws IOException {
    }

    public byte[] getImageData(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i = 0;
        for (int i2 = height - 1; i2 >= 0; i2--) {
            int i3 = i;
            for (int i4 = 0; i4 < width; i4++) {
                int rgb = bufferedImage.getRGB(i4, i2) & ViewCompat.MEASURED_SIZE_MASK;
                int i5 = (rgb >> 16) & 255;
                int i6 = (rgb >> 8) & 255;
                byteArrayOutputStream.write((rgb >> 0) & 255);
                byteArrayOutputStream.write(i6);
                byteArrayOutputStream.write(i5);
                i3 += 3;
            }
            i = i3;
            while (i % 4 != 0) {
                byteArrayOutputStream.write(0);
                i++;
            }
        }
        return byteArrayOutputStream.toByteArray();
    }
}

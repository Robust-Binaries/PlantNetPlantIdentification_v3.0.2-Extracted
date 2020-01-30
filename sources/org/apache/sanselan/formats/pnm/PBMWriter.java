package org.apache.sanselan.formats.pnm;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import org.apache.sanselan.ImageWriteException;

public class PBMWriter extends PNMWriter implements PNMConstants {
    public PBMWriter(boolean z) {
        super(z);
    }

    public void writeImage(BufferedImage bufferedImage, OutputStream outputStream, Map map) throws ImageWriteException, IOException {
        outputStream.write(80);
        outputStream.write(this.RAWBITS ? 52 : 49);
        outputStream.write(32);
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("");
        stringBuffer.append(width);
        outputStream.write(stringBuffer.toString().getBytes());
        outputStream.write(32);
        StringBuffer stringBuffer2 = new StringBuffer();
        stringBuffer2.append("");
        stringBuffer2.append(height);
        outputStream.write(stringBuffer2.toString().getBytes());
        outputStream.write(32);
        int i = 0;
        int i2 = 0;
        for (int i3 = 0; i3 < height; i3++) {
            int i4 = i;
            for (int i5 = 0; i5 < width; i5++) {
                int rgb = bufferedImage.getRGB(i5, i3);
                int i6 = ((((rgb >> 16) & 255) + ((rgb >> 8) & 255)) + ((rgb >> 0) & 255)) / 3 > 127 ? 0 : 1;
                if (this.RAWBITS) {
                    i4 = (i4 << 1) | (i6 & 1);
                    i2++;
                    if (i2 >= 8) {
                        outputStream.write((byte) i4);
                        i2 = 0;
                        i4 = 0;
                    }
                } else {
                    StringBuffer stringBuffer3 = new StringBuffer();
                    stringBuffer3.append("");
                    stringBuffer3.append(i6);
                    outputStream.write(stringBuffer3.toString().getBytes());
                    outputStream.write(32);
                }
            }
            if (!this.RAWBITS || i2 <= 0) {
                i = i4;
            } else {
                outputStream.write((byte) (i4 << (8 - i2)));
                i = 0;
                i2 = 0;
            }
        }
    }
}

package org.apache.sanselan.formats.pnm;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import org.apache.sanselan.ImageWriteException;

public class PGMWriter extends PNMWriter {
    public PGMWriter(boolean z) {
        super(z);
    }

    public void writeImage(BufferedImage bufferedImage, OutputStream outputStream, Map map) throws ImageWriteException, IOException {
        outputStream.write(80);
        outputStream.write(this.RAWBITS ? 53 : 50);
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
        outputStream.write("255".getBytes());
        outputStream.write(10);
        for (int i = 0; i < height; i++) {
            for (int i2 = 0; i2 < width; i2++) {
                int rgb = bufferedImage.getRGB(i2, i);
                int i3 = ((((rgb >> 16) & 255) + ((rgb >> 8) & 255)) + ((rgb >> 0) & 255)) / 3;
                if (this.RAWBITS) {
                    outputStream.write((byte) i3);
                } else {
                    StringBuffer stringBuffer3 = new StringBuffer();
                    stringBuffer3.append("");
                    stringBuffer3.append(i3);
                    outputStream.write(stringBuffer3.toString().getBytes());
                    outputStream.write(32);
                }
            }
        }
    }
}

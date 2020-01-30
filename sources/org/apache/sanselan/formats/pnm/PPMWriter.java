package org.apache.sanselan.formats.pnm;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import org.apache.sanselan.ImageWriteException;

public class PPMWriter extends PNMWriter {
    public PPMWriter(boolean z) {
        super(z);
    }

    public void writeImage(BufferedImage bufferedImage, OutputStream outputStream, Map map) throws ImageWriteException, IOException {
        outputStream.write(80);
        outputStream.write(this.RAWBITS ? 54 : 51);
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
                int i3 = (rgb >> 16) & 255;
                int i4 = (rgb >> 8) & 255;
                int i5 = (rgb >> 0) & 255;
                if (this.RAWBITS) {
                    outputStream.write((byte) i3);
                    outputStream.write((byte) i4);
                    outputStream.write((byte) i5);
                } else {
                    StringBuffer stringBuffer3 = new StringBuffer();
                    stringBuffer3.append("");
                    stringBuffer3.append(i3);
                    outputStream.write(stringBuffer3.toString().getBytes());
                    outputStream.write(32);
                    StringBuffer stringBuffer4 = new StringBuffer();
                    stringBuffer4.append("");
                    stringBuffer4.append(i4);
                    outputStream.write(stringBuffer4.toString().getBytes());
                    outputStream.write(32);
                    StringBuffer stringBuffer5 = new StringBuffer();
                    stringBuffer5.append("");
                    stringBuffer5.append(i5);
                    outputStream.write(stringBuffer5.toString().getBytes());
                    outputStream.write(32);
                }
            }
        }
    }
}

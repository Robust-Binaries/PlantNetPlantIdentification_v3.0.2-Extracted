package org.apache.sanselan.formats.pnm;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.IOException;
import java.io.InputStream;
import org.apache.sanselan.ImageFormat;

public abstract class FileInfo {
    protected final boolean RAWBITS;
    protected final int height;
    protected final int width;

    public void dump() {
    }

    public abstract int getBitDepth();

    public abstract int getColorType();

    public abstract ImageFormat getImageType();

    public abstract String getImageTypeDescription();

    public abstract String getMIMEType();

    public abstract int getNumComponents();

    public abstract int getRGB(InputStream inputStream) throws IOException;

    public abstract int getRGB(WhiteSpaceReader whiteSpaceReader) throws IOException;

    /* access modifiers changed from: protected */
    public void newline() {
    }

    public FileInfo(int i, int i2, boolean z) {
        this.width = i;
        this.height = i2;
        this.RAWBITS = z;
    }

    public void readImage(BufferedImage bufferedImage, InputStream inputStream) throws IOException {
        DataBuffer dataBuffer = bufferedImage.getRaster().getDataBuffer();
        if (!this.RAWBITS) {
            WhiteSpaceReader whiteSpaceReader = new WhiteSpaceReader(inputStream);
            for (int i = 0; i < this.height; i++) {
                for (int i2 = 0; i2 < this.width; i2++) {
                    dataBuffer.setElem((this.width * i) + i2, getRGB(whiteSpaceReader));
                }
                newline();
            }
            return;
        }
        for (int i3 = 0; i3 < this.height; i3++) {
            for (int i4 = 0; i4 < this.width; i4++) {
                dataBuffer.setElem((this.width * i3) + i4, getRGB(inputStream));
            }
            newline();
        }
    }
}

package org.apache.sanselan.formats.pnm;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import org.apache.sanselan.ImageWriteException;

public abstract class PNMWriter {
    protected final boolean RAWBITS;

    public abstract void writeImage(BufferedImage bufferedImage, OutputStream outputStream, Map map) throws ImageWriteException, IOException;

    public PNMWriter(boolean z) {
        this.RAWBITS = z;
    }
}

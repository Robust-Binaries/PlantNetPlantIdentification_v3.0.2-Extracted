package org.apache.sanselan.formats.bmp.writers;

import java.awt.image.BufferedImage;
import java.io.IOException;
import org.apache.sanselan.common.BinaryOutputStream;

public abstract class BMPWriter {
    public abstract int getBitsPerPixel();

    public abstract byte[] getImageData(BufferedImage bufferedImage);

    public abstract int getPaletteSize();

    public abstract void writePalette(BinaryOutputStream binaryOutputStream) throws IOException;
}

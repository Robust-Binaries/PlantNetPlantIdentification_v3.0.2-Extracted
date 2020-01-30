package org.apache.sanselan.formats.png.chunks;

import java.io.IOException;
import org.apache.sanselan.formats.png.PngText;

public abstract class PNGTextChunk extends PNGChunk {
    public abstract PngText getContents();

    public abstract String getKeyword();

    public abstract String getText();

    public PNGTextChunk(int i, int i2, int i3, byte[] bArr) throws IOException {
        super(i, i2, i3, bArr);
    }
}

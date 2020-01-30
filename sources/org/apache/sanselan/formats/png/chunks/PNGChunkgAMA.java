package org.apache.sanselan.formats.png.chunks;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.sanselan.ImageReadException;

public class PNGChunkgAMA extends PNGChunk {
    public final int Gamma;

    public PNGChunkgAMA(int i, int i2, int i3, byte[] bArr) throws ImageReadException, IOException {
        super(i, i2, i3, bArr);
        this.Gamma = read4Bytes("Gamma", new ByteArrayInputStream(bArr), "Not a Valid Png File: gAMA Corrupt");
    }

    public double getGamma() {
        double d = (double) this.Gamma;
        Double.isNaN(d);
        return 1.0d / (d / 100000.0d);
    }
}

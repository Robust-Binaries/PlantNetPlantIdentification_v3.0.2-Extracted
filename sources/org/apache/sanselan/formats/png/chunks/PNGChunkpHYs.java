package org.apache.sanselan.formats.png.chunks;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.sanselan.ImageReadException;

public class PNGChunkpHYs extends PNGChunk {
    public final int PixelsPerUnitXAxis;
    public final int PixelsPerUnitYAxis;
    public final int UnitSpecifier;

    public PNGChunkpHYs(int i, int i2, int i3, byte[] bArr) throws ImageReadException, IOException {
        super(i, i2, i3, bArr);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        this.PixelsPerUnitXAxis = read4Bytes("PixelsPerUnitXAxis", byteArrayInputStream, "Not a Valid Png File: pHYs Corrupt");
        this.PixelsPerUnitYAxis = read4Bytes("PixelsPerUnitYAxis", byteArrayInputStream, "Not a Valid Png File: pHYs Corrupt");
        this.UnitSpecifier = readByte("Unit specifier", byteArrayInputStream, "Not a Valid Png File: pHYs Corrupt");
    }
}

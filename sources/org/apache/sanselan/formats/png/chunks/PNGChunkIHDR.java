package org.apache.sanselan.formats.png.chunks;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.sanselan.ImageReadException;

public class PNGChunkIHDR extends PNGChunk {
    public final int bitDepth;
    public final int colorType;
    public final int compressionMethod;
    public final int filterMethod;
    public final int height;
    public final int interlaceMethod;
    public final int width;

    public PNGChunkIHDR(int i, int i2, int i3, byte[] bArr) throws ImageReadException, IOException {
        super(i, i2, i3, bArr);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        this.width = read4Bytes("Width", byteArrayInputStream, "Not a Valid Png File: IHDR Corrupt");
        this.height = read4Bytes("Height", byteArrayInputStream, "Not a Valid Png File: IHDR Corrupt");
        this.bitDepth = readByte("BitDepth", byteArrayInputStream, "Not a Valid Png File: IHDR Corrupt");
        this.colorType = readByte("ColorType", byteArrayInputStream, "Not a Valid Png File: IHDR Corrupt");
        this.compressionMethod = readByte("CompressionMethod", byteArrayInputStream, "Not a Valid Png File: IHDR Corrupt");
        this.filterMethod = readByte("FilterMethod", byteArrayInputStream, "Not a Valid Png File: IHDR Corrupt");
        this.interlaceMethod = readByte("InterlaceMethod", byteArrayInputStream, "Not a Valid Png File: IHDR Corrupt");
    }
}

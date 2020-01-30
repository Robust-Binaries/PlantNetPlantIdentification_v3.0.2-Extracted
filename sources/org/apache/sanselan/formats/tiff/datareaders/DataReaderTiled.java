package org.apache.sanselan.formats.tiff.datareaders;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.common.BitInputStream;
import org.apache.sanselan.formats.tiff.TiffElement.DataElement;
import org.apache.sanselan.formats.tiff.TiffImageData.Tiles;
import org.apache.sanselan.formats.tiff.photometricinterpreters.PhotometricInterpreter;

public final class DataReaderTiled extends DataReader {
    private final int bitsPerPixel;
    private final int compression;
    private final int height;
    private final Tiles imageData;
    private final int tileLength;
    private final int tileWidth;
    private final int width;

    public DataReaderTiled(PhotometricInterpreter photometricInterpreter, int i, int i2, int i3, int[] iArr, int i4, int i5, int i6, int i7, int i8, Tiles tiles) {
        super(photometricInterpreter, iArr, i4, i5);
        this.tileWidth = i;
        this.tileLength = i2;
        this.bitsPerPixel = i3;
        this.width = i6;
        this.height = i7;
        this.compression = i8;
        this.imageData = tiles;
    }

    private void interpretTile(BufferedImage bufferedImage, byte[] bArr, int i, int i2) throws ImageReadException, IOException {
        BitInputStream bitInputStream = new BitInputStream(new ByteArrayInputStream(bArr));
        int i3 = this.tileWidth * this.tileLength;
        int i4 = 0;
        int i5 = 0;
        for (int i6 = 0; i6 < i3; i6++) {
            int i7 = i4 + i;
            int i8 = i5 + i2;
            int[] samplesAsBytes = getSamplesAsBytes(bitInputStream);
            if (i7 < this.width && i8 < this.height) {
                this.photometricInterpreter.interpretPixel(bufferedImage, applyPredictor(samplesAsBytes, i7), i7, i8);
            }
            i4++;
            if (i4 >= this.tileWidth) {
                i5++;
                bitInputStream.flushCache();
                if (i5 < this.tileLength) {
                    i4 = 0;
                } else {
                    return;
                }
            }
        }
    }

    public void readImageData(BufferedImage bufferedImage) throws ImageReadException, IOException {
        int i = (((this.tileWidth * this.bitsPerPixel) + 7) / 8) * this.tileLength;
        int i2 = 0;
        int i3 = 0;
        for (DataElement dataElement : this.imageData.tiles) {
            interpretTile(bufferedImage, decompress(dataElement.data, this.compression, i), i2, i3);
            i2 += this.tileWidth;
            if (i2 >= this.width) {
                i3 += this.tileLength;
                if (i3 < this.height) {
                    i2 = 0;
                } else {
                    return;
                }
            }
        }
    }
}

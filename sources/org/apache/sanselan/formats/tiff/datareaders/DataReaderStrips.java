package org.apache.sanselan.formats.tiff.datareaders;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.common.BitInputStream;
import org.apache.sanselan.formats.tiff.TiffImageData.Strips;
import org.apache.sanselan.formats.tiff.photometricinterpreters.PhotometricInterpreter;

public final class DataReaderStrips extends DataReader {
    private final int bitsPerPixel;
    private final int compression;
    private final int height;
    private final Strips imageData;
    private final int rowsPerStrip;
    private final int width;

    /* renamed from: x */
    private int f176x = 0;

    /* renamed from: y */
    private int f177y = 0;

    public DataReaderStrips(PhotometricInterpreter photometricInterpreter, int i, int[] iArr, int i2, int i3, int i4, int i5, int i6, int i7, Strips strips) {
        super(photometricInterpreter, iArr, i2, i3);
        this.bitsPerPixel = i;
        this.width = i4;
        this.height = i5;
        this.compression = i6;
        this.rowsPerStrip = i7;
        this.imageData = strips;
    }

    private void interpretStrip(BufferedImage bufferedImage, byte[] bArr, int i) throws ImageReadException, IOException {
        BitInputStream bitInputStream = new BitInputStream(new ByteArrayInputStream(bArr));
        for (int i2 = 0; i2 < i; i2++) {
            int[] samplesAsBytes = getSamplesAsBytes(bitInputStream);
            int i3 = this.f176x;
            if (i3 < this.width && this.f177y < this.height) {
                this.photometricInterpreter.interpretPixel(bufferedImage, applyPredictor(samplesAsBytes, i3), this.f176x, this.f177y);
            }
            this.f176x++;
            if (this.f176x >= this.width) {
                this.f176x = 0;
                this.f177y++;
                bitInputStream.flushCache();
                if (this.f177y >= this.height) {
                    return;
                }
            }
        }
    }

    public void readImageData(BufferedImage bufferedImage) throws ImageReadException, IOException {
        for (int i = 0; i < this.imageData.strips.length; i++) {
            int i2 = this.height;
            int i3 = this.rowsPerStrip;
            int min = Math.min(i2 - (i * i3), i3) * this.width;
            interpretStrip(bufferedImage, decompress(this.imageData.strips[i].data, this.compression, ((this.bitsPerPixel * min) + 7) / 8), min);
        }
    }
}

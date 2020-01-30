package org.apache.sanselan.formats.tiff.datareaders;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.common.BinaryConstants;
import org.apache.sanselan.common.BitInputStream;
import org.apache.sanselan.common.PackBits;
import org.apache.sanselan.common.mylzw.MyLZWDecompressor;
import org.apache.sanselan.formats.tiff.constants.TiffConstants;
import org.apache.sanselan.formats.tiff.photometricinterpreters.PhotometricInterpreter;

public abstract class DataReader implements TiffConstants, BinaryConstants {
    protected final int[] bitsPerSample;
    private int count = 0;
    protected final int[] last;
    protected final PhotometricInterpreter photometricInterpreter;
    protected final int predictor;
    protected final int samplesPerPixel;

    public abstract void readImageData(BufferedImage bufferedImage) throws ImageReadException, IOException;

    public DataReader(PhotometricInterpreter photometricInterpreter2, int[] iArr, int i, int i2) {
        this.photometricInterpreter = photometricInterpreter2;
        this.bitsPerSample = iArr;
        this.samplesPerPixel = i2;
        this.predictor = i;
        this.last = new int[i2];
    }

    /* access modifiers changed from: protected */
    public int[] getSamplesAsBytes(BitInputStream bitInputStream) throws ImageReadException, IOException {
        int[] iArr = new int[this.bitsPerSample.length];
        int i = 0;
        while (true) {
            int[] iArr2 = this.bitsPerSample;
            if (i >= iArr2.length) {
                return iArr;
            }
            int i2 = iArr2[i];
            int readBits = bitInputStream.readBits(i2);
            if (i2 < 8) {
                int i3 = 8 - i2;
                int i4 = readBits << i3;
                readBits = (readBits & 1) > 0 ? i4 | ((1 << i3) - 1) : i4;
            } else if (i2 > 8) {
                readBits >>= i2 - 8;
            }
            iArr[i] = readBits;
            i++;
        }
    }

    /* access modifiers changed from: protected */
    public int[] applyPredictor(int[] iArr, int i) {
        if (this.predictor == 2) {
            for (int i2 = 0; i2 < iArr.length; i2++) {
                if (i > 0) {
                    iArr[i2] = (iArr[i2] + this.last[i2]) & 255;
                }
                this.last[i2] = iArr[i2];
            }
        }
        return iArr;
    }

    /* access modifiers changed from: protected */
    public byte[] decompress(byte[] bArr, int i, int i2) throws ImageReadException, IOException {
        if (i == 5) {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
            MyLZWDecompressor myLZWDecompressor = new MyLZWDecompressor(8, 77);
            myLZWDecompressor.setTiffLZWMode();
            return myLZWDecompressor.decompress(byteArrayInputStream, i2);
        } else if (i != 32773) {
            switch (i) {
                case 1:
                    return bArr;
                case 2:
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append("Tiff: unknown compression: ");
                    stringBuffer.append(i);
                    throw new ImageReadException(stringBuffer.toString());
                default:
                    StringBuffer stringBuffer2 = new StringBuffer();
                    stringBuffer2.append("Tiff: unknown compression: ");
                    stringBuffer2.append(i);
                    throw new ImageReadException(stringBuffer2.toString());
            }
        } else {
            byte[] decompress = new PackBits().decompress(bArr, i2);
            this.count++;
            return decompress;
        }
    }
}

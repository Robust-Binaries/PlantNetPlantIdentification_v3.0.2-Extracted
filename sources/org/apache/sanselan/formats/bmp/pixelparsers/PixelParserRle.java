package org.apache.sanselan.formats.bmp.pixelparsers;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.IOException;
import java.io.PrintStream;
import kotlin.UByte;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.common.BinaryFileParser;
import org.apache.sanselan.formats.bmp.BmpHeaderInfo;

public class PixelParserRle extends PixelParser {
    public PixelParserRle(BmpHeaderInfo bmpHeaderInfo, byte[] bArr, byte[] bArr2) {
        super(bmpHeaderInfo, bArr, bArr2);
    }

    private int getSamplesPerByte() throws ImageReadException, IOException {
        if (this.bhi.bitsPerPixel == 8) {
            return 1;
        }
        if (this.bhi.bitsPerPixel == 4) {
            return 2;
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("BMP RLE: bad BitsPerPixel: ");
        stringBuffer.append(this.bhi.bitsPerPixel);
        throw new ImageReadException(stringBuffer.toString());
    }

    private int[] convertDataToSamples(int i) throws ImageReadException, IOException {
        if (this.bhi.bitsPerPixel == 8) {
            return new int[]{getColorTableRGB(i)};
        } else if (this.bhi.bitsPerPixel == 4) {
            return new int[]{getColorTableRGB(i >> 4), getColorTableRGB(i & 15)};
        } else {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("BMP RLE: bad BitsPerPixel: ");
            stringBuffer.append(this.bhi.bitsPerPixel);
            throw new ImageReadException(stringBuffer.toString());
        }
    }

    private int processByteOfData(int[] iArr, int i, int i2, int i3, int i4, int i5, DataBuffer dataBuffer, BufferedImage bufferedImage) throws ImageReadException {
        int i6 = i2;
        int i7 = 0;
        for (int i8 = 0; i8 < i; i8++) {
            if (i6 < 0 || i6 >= i4 || i3 < 0 || i3 >= i5) {
                PrintStream printStream = System.out;
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("skipping bad pixel (");
                stringBuffer.append(i6);
                stringBuffer.append(",");
                stringBuffer.append(i3);
                stringBuffer.append(")");
                printStream.println(stringBuffer.toString());
            } else {
                dataBuffer.setElem((this.bhi.width * i3) + i6, iArr[i8 % iArr.length]);
            }
            i6++;
            i7++;
        }
        return i7;
    }

    public void processImage(BufferedImage bufferedImage) throws ImageReadException, IOException {
        DataBuffer dataBuffer = bufferedImage.getRaster().getDataBuffer();
        int i = this.bhi.width;
        int i2 = this.bhi.height;
        int i3 = i2 - 1;
        boolean z = false;
        int i4 = 0;
        while (!z) {
            BinaryFileParser binaryFileParser = this.bfp;
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("RLE (");
            stringBuffer.append(i4);
            stringBuffer.append(",");
            stringBuffer.append(i3);
            stringBuffer.append(") a");
            byte readByte = binaryFileParser.readByte(stringBuffer.toString(), this.f166is, "BMP: Bad RLE") & UByte.MAX_VALUE;
            BinaryFileParser binaryFileParser2 = this.bfp;
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2.append("RLE (");
            stringBuffer2.append(i4);
            stringBuffer2.append(",");
            stringBuffer2.append(i3);
            stringBuffer2.append(")  b");
            byte readByte2 = binaryFileParser2.readByte(stringBuffer2.toString(), this.f166is, "BMP: Bad RLE") & UByte.MAX_VALUE;
            if (readByte == 0) {
                switch (readByte2) {
                    case 0:
                        i3--;
                        i4 = 0;
                        break;
                    case 1:
                        z = true;
                        break;
                    case 2:
                        this.bfp.readByte("RLE c", this.f166is, "BMP: Bad RLE");
                        this.bfp.readByte("RLE d", this.f166is, "BMP: Bad RLE");
                        break;
                    default:
                        int samplesPerByte = getSamplesPerByte();
                        int i5 = readByte2 / samplesPerByte;
                        if (readByte2 % samplesPerByte > 0) {
                            i5++;
                        }
                        if (i5 % 2 != 0) {
                            i5++;
                        }
                        byte[] readByteArray = this.bfp.readByteArray("bytes", i5, this.f166is, "RLE: Absolute Mode");
                        int i6 = i4;
                        int i7 = 0;
                        int i8 = readByte2;
                        while (i8 > 0) {
                            int i9 = samplesPerByte;
                            int i10 = i3;
                            int processByteOfData = processByteOfData(convertDataToSamples(readByteArray[i7] & UByte.MAX_VALUE), Math.min(i8, samplesPerByte), i6, i3, i, i2, dataBuffer, bufferedImage);
                            i6 += processByteOfData;
                            i8 -= processByteOfData;
                            i7++;
                            samplesPerByte = i9;
                            i3 = i10;
                        }
                        int i11 = i3;
                        i4 = i6;
                        break;
                }
            } else {
                int i12 = i3;
                i4 = processByteOfData(convertDataToSamples(readByte2), readByte, i4, i12, i, i2, dataBuffer, bufferedImage) + i4;
                i3 = i12;
            }
        }
    }
}

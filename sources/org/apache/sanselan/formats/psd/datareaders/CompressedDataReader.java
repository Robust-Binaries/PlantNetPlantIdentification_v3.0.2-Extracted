package org.apache.sanselan.formats.psd.datareaders;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.common.BinaryFileParser;
import org.apache.sanselan.common.PackBits;
import org.apache.sanselan.common.mylzw.BitsToByteInputStream;
import org.apache.sanselan.common.mylzw.MyBitInputStream;
import org.apache.sanselan.formats.psd.ImageContents;
import org.apache.sanselan.formats.psd.PSDHeaderInfo;
import org.apache.sanselan.formats.psd.dataparsers.DataParser;

public class CompressedDataReader extends DataReader {
    public CompressedDataReader(DataParser dataParser) {
        super(dataParser);
    }

    public void readData(InputStream inputStream, BufferedImage bufferedImage, ImageContents imageContents, BinaryFileParser binaryFileParser) throws ImageReadException, IOException {
        InputStream inputStream2 = inputStream;
        ImageContents imageContents2 = imageContents;
        BinaryFileParser binaryFileParser2 = binaryFileParser;
        PSDHeaderInfo pSDHeaderInfo = imageContents2.header;
        int i = pSDHeaderInfo.Columns;
        int i2 = pSDHeaderInfo.Rows;
        int i3 = pSDHeaderInfo.Channels * i2;
        int[] iArr = new int[i3];
        for (int i4 = 0; i4 < i3; i4++) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("scanline_bytecount[");
            stringBuffer.append(i4);
            stringBuffer.append("]");
            iArr[i4] = binaryFileParser2.read2Bytes(stringBuffer.toString(), inputStream2, "PSD: bad Image Data");
        }
        binaryFileParser2.setDebug(false);
        int i5 = pSDHeaderInfo.Depth;
        int basicChannelsCount = this.dataParser.getBasicChannelsCount();
        int[][][] iArr2 = (int[][][]) Array.newInstance(int[].class, new int[]{basicChannelsCount, i2});
        for (int i6 = 0; i6 < basicChannelsCount; i6++) {
            for (int i7 = 0; i7 < i2; i7++) {
                iArr2[i6][i7] = new BitsToByteInputStream(new MyBitInputStream(new ByteArrayInputStream(new PackBits().decompress(binaryFileParser2.readByteArray("scanline", iArr[(i6 * i2) + i7], inputStream2, "PSD: Missing Image Data"), i)), 77), 8).readBitsArray(i5, i);
            }
        }
        this.dataParser.parseData(iArr2, bufferedImage, imageContents2);
    }
}

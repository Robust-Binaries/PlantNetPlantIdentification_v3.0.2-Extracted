package org.apache.sanselan.formats.psd.datareaders;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.common.BinaryFileParser;
import org.apache.sanselan.common.mylzw.BitsToByteInputStream;
import org.apache.sanselan.common.mylzw.MyBitInputStream;
import org.apache.sanselan.formats.psd.ImageContents;
import org.apache.sanselan.formats.psd.PSDHeaderInfo;
import org.apache.sanselan.formats.psd.dataparsers.DataParser;

public class UncompressedDataReader extends DataReader {
    public UncompressedDataReader(DataParser dataParser) {
        super(dataParser);
    }

    public void readData(InputStream inputStream, BufferedImage bufferedImage, ImageContents imageContents, BinaryFileParser binaryFileParser) throws ImageReadException, IOException {
        PSDHeaderInfo pSDHeaderInfo = imageContents.header;
        int i = pSDHeaderInfo.Columns;
        int i2 = pSDHeaderInfo.Rows;
        binaryFileParser.setDebug(false);
        int basicChannelsCount = this.dataParser.getBasicChannelsCount();
        int i3 = pSDHeaderInfo.Depth;
        BitsToByteInputStream bitsToByteInputStream = new BitsToByteInputStream(new MyBitInputStream(inputStream, 77), 8);
        int[][][] iArr = (int[][][]) Array.newInstance(int.class, new int[]{basicChannelsCount, i2, i});
        for (int i4 = 0; i4 < basicChannelsCount; i4++) {
            for (int i5 = 0; i5 < i2; i5++) {
                for (int i6 = 0; i6 < i; i6++) {
                    iArr[i4][i5][i6] = (byte) bitsToByteInputStream.readBits(i3);
                }
            }
        }
        this.dataParser.parseData(iArr, bufferedImage, imageContents);
    }
}

package org.apache.sanselan.formats.psd.datareaders;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.common.BinaryConstants;
import org.apache.sanselan.common.BinaryFileParser;
import org.apache.sanselan.formats.psd.ImageContents;
import org.apache.sanselan.formats.psd.dataparsers.DataParser;

public abstract class DataReader implements BinaryConstants {
    protected final DataParser dataParser;

    public abstract void readData(InputStream inputStream, BufferedImage bufferedImage, ImageContents imageContents, BinaryFileParser binaryFileParser) throws ImageReadException, IOException;

    public DataReader(DataParser dataParser2) {
        this.dataParser = dataParser2;
    }

    public void dump() {
        this.dataParser.dump();
    }
}

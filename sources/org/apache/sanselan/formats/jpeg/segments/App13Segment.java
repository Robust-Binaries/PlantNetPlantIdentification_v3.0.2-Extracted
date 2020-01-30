package org.apache.sanselan.formats.jpeg.segments;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.formats.jpeg.JpegImageParser;
import org.apache.sanselan.formats.jpeg.iptc.IPTCParser;
import org.apache.sanselan.formats.jpeg.iptc.PhotoshopApp13Data;

public class App13Segment extends APPNSegment {
    protected final JpegImageParser parser;

    public App13Segment(JpegImageParser jpegImageParser, int i, byte[] bArr) throws ImageReadException, IOException {
        this(jpegImageParser, i, bArr.length, new ByteArrayInputStream(bArr));
    }

    public App13Segment(JpegImageParser jpegImageParser, int i, int i2, InputStream inputStream) throws ImageReadException, IOException {
        super(i, i2, inputStream);
        this.parser = jpegImageParser;
    }

    public boolean isPhotoshopJpegSegment() throws ImageReadException, IOException {
        return new IPTCParser().isPhotoshopJpegSegment(this.bytes);
    }

    public PhotoshopApp13Data parsePhotoshopSegment(Map map) throws ImageReadException, IOException {
        if (!new IPTCParser().isPhotoshopJpegSegment(this.bytes)) {
            return null;
        }
        return new IPTCParser().parsePhotoshopSegment(this.bytes, map);
    }
}

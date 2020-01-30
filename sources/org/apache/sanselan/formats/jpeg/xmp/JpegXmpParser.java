package org.apache.sanselan.formats.jpeg.xmp;

import java.io.UnsupportedEncodingException;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.common.BinaryFileParser;
import org.apache.sanselan.formats.jpeg.JpegConstants;

public class JpegXmpParser extends BinaryFileParser implements JpegConstants {
    public JpegXmpParser() {
        setByteOrder(77);
    }

    public boolean isXmpJpegSegment(byte[] bArr) {
        if (bArr.length < XMP_IDENTIFIER.length) {
            return false;
        }
        for (int i = 0; i < XMP_IDENTIFIER.length; i++) {
            if (bArr[i] < XMP_IDENTIFIER[i]) {
                return false;
            }
        }
        return true;
    }

    public String parseXmpJpegSegment(byte[] bArr) throws ImageReadException {
        if (bArr.length >= XMP_IDENTIFIER.length) {
            int i = 0;
            while (i < XMP_IDENTIFIER.length) {
                if (bArr[i] >= XMP_IDENTIFIER[i]) {
                    i++;
                } else {
                    throw new ImageReadException("Invalid JPEG XMP Segment.");
                }
            }
            try {
                return new String(bArr, i, bArr.length - i, "utf-8");
            } catch (UnsupportedEncodingException unused) {
                throw new ImageReadException("Invalid JPEG XMP Segment.");
            }
        } else {
            throw new ImageReadException("Invalid JPEG XMP Segment.");
        }
    }
}

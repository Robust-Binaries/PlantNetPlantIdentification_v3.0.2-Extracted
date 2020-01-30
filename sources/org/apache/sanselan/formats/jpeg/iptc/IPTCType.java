package org.apache.sanselan.formats.jpeg.iptc;

import org.apache.sanselan.ImageInfo;
import org.apache.sanselan.formats.jpeg.JpegConstants;

public class IPTCType implements JpegConstants, IPTCConstants {
    public final String name;
    public final int type;

    public IPTCType(int i, String str) {
        this.type = i;
        this.name = str;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.name);
        stringBuffer.append(" (");
        stringBuffer.append(this.type);
        stringBuffer.append(")");
        return stringBuffer.toString();
    }

    public static IPTCType getUnknown(int i) {
        return new IPTCType(i, ImageInfo.COMPRESSION_ALGORITHM_UNKNOWN);
    }
}

package org.apache.sanselan.formats.jpeg.iptc;

import java.util.HashMap;
import java.util.Map;

public abstract class IPTCTypeLookup implements IPTCConstants {
    private static final Map IPTC_TYPE_MAP = new HashMap();

    static {
        for (IPTCType iPTCType : IPTC_TYPES) {
            IPTC_TYPE_MAP.put(new Integer(iPTCType.type), iPTCType);
        }
    }

    public static final IPTCType getIptcType(int i) {
        Integer num = new Integer(i);
        if (!IPTC_TYPE_MAP.containsKey(num)) {
            return IPTCType.getUnknown(i);
        }
        return (IPTCType) IPTC_TYPE_MAP.get(num);
    }
}

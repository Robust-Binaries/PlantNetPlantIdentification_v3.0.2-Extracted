package org.apache.sanselan.formats.jpeg.iptc;

import java.util.Comparator;

public class IPTCRecord {
    public static final Comparator COMPARATOR = new Comparator() {
        public int compare(Object obj, Object obj2) {
            return ((IPTCRecord) obj).iptcType.type - ((IPTCRecord) obj2).iptcType.type;
        }
    };
    public final IPTCType iptcType;
    public final String value;

    public IPTCRecord(IPTCType iPTCType, String str) {
        this.iptcType = iPTCType;
        this.value = str;
    }

    public String getValue() {
        return this.value;
    }

    public String getIptcTypeName() {
        return this.iptcType.name;
    }
}

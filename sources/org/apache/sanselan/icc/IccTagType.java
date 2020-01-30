package org.apache.sanselan.icc;

public class IccTagType {
    public final String name;
    public final int signature;
    public final String type_description;

    public IccTagType(String str, String str2, int i) {
        this.name = str;
        this.type_description = str2;
        this.signature = i;
    }
}

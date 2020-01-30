package com.learnium.RNDeviceInfo;

import org.apache.sanselan.ImageInfo;

public enum DeviceType {
    HANDSET("Handset"),
    TABLET("Tablet"),
    TV("Tv"),
    UNKNOWN(ImageInfo.COMPRESSION_ALGORITHM_UNKNOWN);
    
    private final String value;

    private DeviceType(String str) {
        this.value = str;
    }

    public String getValue() {
        return this.value;
    }
}

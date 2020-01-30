package org.apache.sanselan.formats.png;

import java.util.ArrayList;
import java.util.List;
import org.apache.sanselan.ImageFormat;
import org.apache.sanselan.ImageInfo;

public class PngImageInfo extends ImageInfo {
    private final List textChunks;

    public PngImageInfo(String str, int i, ArrayList arrayList, ImageFormat imageFormat, String str2, int i2, String str3, int i3, int i4, float f, int i5, float f2, int i6, boolean z, boolean z2, boolean z3, int i7, String str4, List list) {
        super(str, i, arrayList, imageFormat, str2, i2, str3, i3, i4, f, i5, f2, i6, z, z2, z3, i7, str4);
        this.textChunks = list;
    }

    public List getTextChunks() {
        return new ArrayList(this.textChunks);
    }
}

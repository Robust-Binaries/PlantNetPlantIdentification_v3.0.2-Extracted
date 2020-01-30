package org.apache.sanselan.formats.gif;

import java.util.ArrayList;

class ImageContents {
    public final ArrayList blocks;
    public final GIFHeaderInfo gifHeaderInfo;
    public final byte[] globalColorTable;

    public ImageContents(GIFHeaderInfo gIFHeaderInfo, byte[] bArr, ArrayList arrayList) {
        this.gifHeaderInfo = gIFHeaderInfo;
        this.globalColorTable = bArr;
        this.blocks = arrayList;
    }
}

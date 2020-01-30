package org.apache.sanselan.formats.psd;

import java.io.UnsupportedEncodingException;
import org.apache.sanselan.util.Debug;

class ImageResourceBlock {
    protected final byte[] data;

    /* renamed from: id */
    protected final int f170id;
    protected final byte[] nameData;

    public ImageResourceBlock(int i, byte[] bArr, byte[] bArr2) {
        this.f170id = i;
        this.nameData = bArr;
        this.data = bArr2;
    }

    public String getName() throws UnsupportedEncodingException {
        Debug.debug("getName", this.nameData.length);
        return new String(this.nameData, "ISO-8859-1");
    }
}

package org.apache.sanselan.formats.gif;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

class GenericGIFBlock extends GIFBlock {
    public final ArrayList subblocks;

    public GenericGIFBlock(int i, ArrayList arrayList) {
        super(i);
        this.subblocks = arrayList;
    }

    public byte[] appendSubBlocks() throws IOException {
        return appendSubBlocks(false);
    }

    public byte[] appendSubBlocks(boolean z) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for (int i = 0; i < this.subblocks.size(); i++) {
            byte[] bArr = (byte[]) this.subblocks.get(i);
            if (z && i > 0) {
                byteArrayOutputStream.write(bArr.length);
            }
            byteArrayOutputStream.write(bArr);
        }
        return byteArrayOutputStream.toByteArray();
    }
}

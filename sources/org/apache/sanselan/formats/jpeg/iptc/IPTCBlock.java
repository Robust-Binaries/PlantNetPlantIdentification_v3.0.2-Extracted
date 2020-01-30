package org.apache.sanselan.formats.jpeg.iptc;

public class IPTCBlock {
    public final byte[] blockData;
    public final byte[] blockNameBytes;
    public final int blockType;

    public IPTCBlock(int i, byte[] bArr, byte[] bArr2) {
        this.blockData = bArr2;
        this.blockNameBytes = bArr;
        this.blockType = i;
    }

    public boolean isIPTCBlock() {
        return this.blockType == 1028;
    }
}

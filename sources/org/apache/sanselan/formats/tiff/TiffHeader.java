package org.apache.sanselan.formats.tiff;

public class TiffHeader extends TiffElement {
    public final int byteOrder;
    public final int offsetToFirstIFD;
    public final int tiffVersion;

    public String getElementDescription(boolean z) {
        if (z) {
            return null;
        }
        return "TIFF Header";
    }

    public TiffHeader(int i, int i2, int i3) {
        super(0, 8);
        this.byteOrder = i;
        this.tiffVersion = i2;
        this.offsetToFirstIFD = i3;
    }
}

package org.apache.sanselan.formats.gif;

public class ImageDescriptor extends GIFBlock {
    public final byte[] imageData;
    public final int imageHeight;
    public final int imageLeftPosition;
    public final int imageTopPosition;
    public final int imageWidth;
    public final boolean interlaceFlag;
    public final byte[] localColorTable;
    public final boolean localColorTableFlag;
    public final byte packedFields;
    public final byte sizeOfLocalColorTable;
    public final boolean sortFlag;

    public ImageDescriptor(int i, int i2, int i3, int i4, int i5, byte b, boolean z, boolean z2, boolean z3, byte b2, byte[] bArr, byte[] bArr2) {
        super(i);
        this.imageLeftPosition = i2;
        this.imageTopPosition = i3;
        this.imageWidth = i4;
        this.imageHeight = i5;
        this.packedFields = b;
        this.localColorTableFlag = z;
        this.interlaceFlag = z2;
        this.sortFlag = z3;
        this.sizeOfLocalColorTable = b2;
        this.localColorTable = bArr;
        this.imageData = bArr2;
    }
}

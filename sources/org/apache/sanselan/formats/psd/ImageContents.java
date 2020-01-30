package org.apache.sanselan.formats.psd;

import java.io.PrintWriter;

public class ImageContents {
    public final int ColorModeDataLength;
    public final int Compression;
    public final int ImageResourcesLength;
    public final int LayerAndMaskDataLength;
    public final PSDHeaderInfo header;

    public ImageContents(PSDHeaderInfo pSDHeaderInfo, int i, int i2, int i3, int i4) {
        this.header = pSDHeaderInfo;
        this.ColorModeDataLength = i;
        this.ImageResourcesLength = i2;
        this.LayerAndMaskDataLength = i3;
        this.Compression = i4;
    }

    public void dump() {
        PrintWriter printWriter = new PrintWriter(System.out);
        dump(printWriter);
        printWriter.flush();
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println("");
        printWriter.println("ImageContents");
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Compression: ");
        stringBuffer.append(this.Compression);
        stringBuffer.append(" (");
        stringBuffer.append(Integer.toHexString(this.Compression));
        stringBuffer.append(")");
        printWriter.println(stringBuffer.toString());
        StringBuffer stringBuffer2 = new StringBuffer();
        stringBuffer2.append("ColorModeDataLength: ");
        stringBuffer2.append(this.ColorModeDataLength);
        stringBuffer2.append(" (");
        stringBuffer2.append(Integer.toHexString(this.ColorModeDataLength));
        stringBuffer2.append(")");
        printWriter.println(stringBuffer2.toString());
        StringBuffer stringBuffer3 = new StringBuffer();
        stringBuffer3.append("ImageResourcesLength: ");
        stringBuffer3.append(this.ImageResourcesLength);
        stringBuffer3.append(" (");
        stringBuffer3.append(Integer.toHexString(this.ImageResourcesLength));
        stringBuffer3.append(")");
        printWriter.println(stringBuffer3.toString());
        StringBuffer stringBuffer4 = new StringBuffer();
        stringBuffer4.append("LayerAndMaskDataLength: ");
        stringBuffer4.append(this.LayerAndMaskDataLength);
        stringBuffer4.append(" (");
        stringBuffer4.append(Integer.toHexString(this.LayerAndMaskDataLength));
        stringBuffer4.append(")");
        printWriter.println(stringBuffer4.toString());
        printWriter.println("");
        printWriter.flush();
    }
}

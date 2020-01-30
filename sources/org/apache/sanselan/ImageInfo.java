package org.apache.sanselan;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

public class ImageInfo {
    public static final int COLOR_TYPE_BW = 0;
    public static final int COLOR_TYPE_CMYK = 3;
    public static final int COLOR_TYPE_GRAYSCALE = 1;
    public static final int COLOR_TYPE_OTHER = -1;
    public static final int COLOR_TYPE_RGB = 2;
    public static final int COLOR_TYPE_UNKNOWN = -2;
    public static final String COMPRESSION_ALGORITHM_CCITT_1D = "CCITT 1D";
    public static final String COMPRESSION_ALGORITHM_CCITT_GROUP_3 = "CCITT Group 3 1-Dimensional Modified Huffman run-length encoding.";
    public static final String COMPRESSION_ALGORITHM_CCITT_GROUP_4 = "CCITT Group 4";
    public static final String COMPRESSION_ALGORITHM_JPEG = "JPEG";
    public static final String COMPRESSION_ALGORITHM_LZW = "LZW";
    public static final String COMPRESSION_ALGORITHM_NONE = "None";
    public static final String COMPRESSION_ALGORITHM_PACKBITS = "PackBits";
    public static final String COMPRESSION_ALGORITHM_PNG_FILTER = "PNG Filter";
    public static final String COMPRESSION_ALGORITHM_PSD = "Photoshop";
    public static final String COMPRESSION_ALGORITHM_RLE = "RLE: Run-Length Encoding";
    public static final String COMPRESSION_ALGORITHM_UNKNOWN = "Unknown";
    private final int bitsPerPixel;
    private final int colorType;
    private final ArrayList comments;
    private final String compressionAlgorithm;
    private final ImageFormat format;
    private final String formatDetails;
    private final String formatName;
    private final int height;
    private final boolean isProgressive;
    private final boolean isTransparent;
    private final String mimeType;
    private final int numberOfImages;
    private final int physicalHeightDpi;
    private final float physicalHeightInch;
    private final int physicalWidthDpi;
    private final float physicalWidthInch;
    private final boolean usesPalette;
    private final int width;

    public ImageInfo(String str, int i, ArrayList arrayList, ImageFormat imageFormat, String str2, int i2, String str3, int i3, int i4, float f, int i5, float f2, int i6, boolean z, boolean z2, boolean z3, int i7, String str4) {
        this.formatDetails = str;
        this.bitsPerPixel = i;
        this.comments = arrayList;
        this.format = imageFormat;
        this.formatName = str2;
        this.height = i2;
        this.mimeType = str3;
        this.numberOfImages = i3;
        this.physicalHeightDpi = i4;
        this.physicalHeightInch = f;
        this.physicalWidthDpi = i5;
        this.physicalWidthInch = f2;
        this.width = i6;
        this.isProgressive = z;
        this.isTransparent = z2;
        this.usesPalette = z3;
        this.colorType = i7;
        this.compressionAlgorithm = str4;
    }

    public int getBitsPerPixel() {
        return this.bitsPerPixel;
    }

    public ArrayList getComments() {
        return new ArrayList(this.comments);
    }

    public ImageFormat getFormat() {
        return this.format;
    }

    public String getFormatName() {
        return this.formatName;
    }

    public int getHeight() {
        return this.height;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public int getNumberOfImages() {
        return this.numberOfImages;
    }

    public int getPhysicalHeightDpi() {
        return this.physicalHeightDpi;
    }

    public float getPhysicalHeightInch() {
        return this.physicalHeightInch;
    }

    public int getPhysicalWidthDpi() {
        return this.physicalWidthDpi;
    }

    public float getPhysicalWidthInch() {
        return this.physicalWidthInch;
    }

    public int getWidth() {
        return this.width;
    }

    public boolean getIsProgressive() {
        return this.isProgressive;
    }

    public int getColorType() {
        return this.colorType;
    }

    public String getColorTypeDescription() {
        switch (this.colorType) {
            case -2:
                return COMPRESSION_ALGORITHM_UNKNOWN;
            case -1:
                return "Other";
            case 0:
                return "Black and White";
            case 1:
                return "Grayscale";
            case 2:
                return "RGB";
            case 3:
                return "CMYK";
            default:
                return COMPRESSION_ALGORITHM_UNKNOWN;
        }
    }

    public void dump() {
        System.out.print(toString());
    }

    public String toString() {
        try {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            toString(printWriter, "");
            printWriter.flush();
            return stringWriter.toString();
        } catch (Exception unused) {
            return "Image Data: Error";
        }
    }

    public void toString(PrintWriter printWriter, String str) throws ImageReadException, IOException {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Format Details: ");
        stringBuffer.append(this.formatDetails);
        printWriter.println(stringBuffer.toString());
        StringBuffer stringBuffer2 = new StringBuffer();
        stringBuffer2.append("Bits Per Pixel: ");
        stringBuffer2.append(this.bitsPerPixel);
        printWriter.println(stringBuffer2.toString());
        StringBuffer stringBuffer3 = new StringBuffer();
        stringBuffer3.append("Comments: ");
        stringBuffer3.append(this.comments.size());
        printWriter.println(stringBuffer3.toString());
        for (int i = 0; i < this.comments.size(); i++) {
            String str2 = (String) this.comments.get(i);
            StringBuffer stringBuffer4 = new StringBuffer();
            stringBuffer4.append("\t");
            stringBuffer4.append(i);
            stringBuffer4.append(": '");
            stringBuffer4.append(str2);
            stringBuffer4.append("'");
            printWriter.println(stringBuffer4.toString());
        }
        StringBuffer stringBuffer5 = new StringBuffer();
        stringBuffer5.append("Format: ");
        stringBuffer5.append(this.format.name);
        printWriter.println(stringBuffer5.toString());
        StringBuffer stringBuffer6 = new StringBuffer();
        stringBuffer6.append("Format Name: ");
        stringBuffer6.append(this.formatName);
        printWriter.println(stringBuffer6.toString());
        StringBuffer stringBuffer7 = new StringBuffer();
        stringBuffer7.append("Compression Algorithm: ");
        stringBuffer7.append(this.compressionAlgorithm);
        printWriter.println(stringBuffer7.toString());
        StringBuffer stringBuffer8 = new StringBuffer();
        stringBuffer8.append("Height: ");
        stringBuffer8.append(this.height);
        printWriter.println(stringBuffer8.toString());
        StringBuffer stringBuffer9 = new StringBuffer();
        stringBuffer9.append("MimeType: ");
        stringBuffer9.append(this.mimeType);
        printWriter.println(stringBuffer9.toString());
        StringBuffer stringBuffer10 = new StringBuffer();
        stringBuffer10.append("Number Of Images: ");
        stringBuffer10.append(this.numberOfImages);
        printWriter.println(stringBuffer10.toString());
        StringBuffer stringBuffer11 = new StringBuffer();
        stringBuffer11.append("Physical Height Dpi: ");
        stringBuffer11.append(this.physicalHeightDpi);
        printWriter.println(stringBuffer11.toString());
        StringBuffer stringBuffer12 = new StringBuffer();
        stringBuffer12.append("Physical Height Inch: ");
        stringBuffer12.append(this.physicalHeightInch);
        printWriter.println(stringBuffer12.toString());
        StringBuffer stringBuffer13 = new StringBuffer();
        stringBuffer13.append("Physical Width Dpi: ");
        stringBuffer13.append(this.physicalWidthDpi);
        printWriter.println(stringBuffer13.toString());
        StringBuffer stringBuffer14 = new StringBuffer();
        stringBuffer14.append("Physical Width Inch: ");
        stringBuffer14.append(this.physicalWidthInch);
        printWriter.println(stringBuffer14.toString());
        StringBuffer stringBuffer15 = new StringBuffer();
        stringBuffer15.append("Width: ");
        stringBuffer15.append(this.width);
        printWriter.println(stringBuffer15.toString());
        StringBuffer stringBuffer16 = new StringBuffer();
        stringBuffer16.append("Is Progressive: ");
        stringBuffer16.append(this.isProgressive);
        printWriter.println(stringBuffer16.toString());
        StringBuffer stringBuffer17 = new StringBuffer();
        stringBuffer17.append("Is Transparent: ");
        stringBuffer17.append(this.isTransparent);
        printWriter.println(stringBuffer17.toString());
        StringBuffer stringBuffer18 = new StringBuffer();
        stringBuffer18.append("Color Type: ");
        stringBuffer18.append(getColorTypeDescription());
        printWriter.println(stringBuffer18.toString());
        StringBuffer stringBuffer19 = new StringBuffer();
        stringBuffer19.append("Uses Palette: ");
        stringBuffer19.append(this.usesPalette);
        printWriter.println(stringBuffer19.toString());
        printWriter.flush();
    }
}

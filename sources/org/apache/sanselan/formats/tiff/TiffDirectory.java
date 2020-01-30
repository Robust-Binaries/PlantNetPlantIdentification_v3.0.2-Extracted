package org.apache.sanselan.formats.tiff;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import org.apache.sanselan.ImageInfo;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.common.byteSources.ByteSource;
import org.apache.sanselan.formats.tiff.constants.TagInfo;
import org.apache.sanselan.formats.tiff.constants.TiffConstants;

public class TiffDirectory extends TiffElement implements TiffConstants {
    public final ArrayList entries;
    private JpegImageData jpegImageData = null;
    public final int nextDirectoryOffset;
    private TiffImageData tiffImageData = null;
    public final int type;

    public final class ImageDataElement extends TiffElement {
        public String getElementDescription(boolean z) {
            if (z) {
                return null;
            }
            return "ImageDataElement";
        }

        public ImageDataElement(int i, int i2) {
            super(i, i2);
        }
    }

    public static final String description(int i) {
        switch (i) {
            case -4:
                return "Interoperability";
            case -3:
                return "Gps";
            case -2:
                return "Exif";
            case -1:
                return ImageInfo.COMPRESSION_ALGORITHM_UNKNOWN;
            case 0:
                return "Root";
            case 1:
                return "Sub";
            case 2:
                return "Thumbnail";
            default:
                return "Bad Type";
        }
    }

    public String description() {
        return description(this.type);
    }

    public String getElementDescription(boolean z) {
        if (!z) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("TIFF Directory (");
            stringBuffer.append(description());
            stringBuffer.append(")");
            return stringBuffer.toString();
        }
        int i = this.offset + 2;
        StringBuffer stringBuffer2 = new StringBuffer();
        for (int i2 = 0; i2 < this.entries.size(); i2++) {
            TiffField tiffField = (TiffField) this.entries.get(i2);
            stringBuffer2.append("\t");
            StringBuffer stringBuffer3 = new StringBuffer();
            stringBuffer3.append("[");
            stringBuffer3.append(i);
            stringBuffer3.append("]: ");
            stringBuffer2.append(stringBuffer3.toString());
            stringBuffer2.append(tiffField.tagInfo.name);
            StringBuffer stringBuffer4 = new StringBuffer();
            stringBuffer4.append(" (");
            stringBuffer4.append(tiffField.tag);
            stringBuffer4.append(", 0x");
            stringBuffer4.append(Integer.toHexString(tiffField.tag));
            stringBuffer4.append(")");
            stringBuffer2.append(stringBuffer4.toString());
            StringBuffer stringBuffer5 = new StringBuffer();
            stringBuffer5.append(", ");
            stringBuffer5.append(tiffField.fieldType.name);
            stringBuffer2.append(stringBuffer5.toString());
            StringBuffer stringBuffer6 = new StringBuffer();
            stringBuffer6.append(", ");
            stringBuffer6.append(tiffField.fieldType.getRawBytes(tiffField).length);
            stringBuffer2.append(stringBuffer6.toString());
            StringBuffer stringBuffer7 = new StringBuffer();
            stringBuffer7.append(": ");
            stringBuffer7.append(tiffField.getValueDescription());
            stringBuffer2.append(stringBuffer7.toString());
            stringBuffer2.append(ReactEditTextInputConnectionWrapper.NEWLINE_RAW_VALUE);
            i += 12;
        }
        return stringBuffer2.toString();
    }

    public TiffDirectory(int i, ArrayList arrayList, int i2, int i3) {
        super(i2, (arrayList.size() * 12) + 2 + 4);
        this.type = i;
        this.entries = arrayList;
        this.nextDirectoryOffset = i3;
    }

    public ArrayList getDirectoryEntrys() {
        return new ArrayList(this.entries);
    }

    /* access modifiers changed from: protected */
    public void fillInValues(ByteSource byteSource) throws ImageReadException, IOException {
        for (int i = 0; i < this.entries.size(); i++) {
            ((TiffField) this.entries.get(i)).fillInValue(byteSource);
        }
    }

    public void dump() {
        for (int i = 0; i < this.entries.size(); i++) {
            ((TiffField) this.entries.get(i)).dump();
        }
    }

    public boolean hasJpegImageData() throws ImageReadException {
        return findField(TIFF_TAG_JPEG_INTERCHANGE_FORMAT) != null;
    }

    public boolean hasTiffImageData() throws ImageReadException {
        if (findField(TIFF_TAG_TILE_OFFSETS) == null && findField(TIFF_TAG_STRIP_OFFSETS) == null) {
            return false;
        }
        return true;
    }

    public BufferedImage getTiffImage() throws ImageReadException, IOException {
        return getTiffImage(null);
    }

    public BufferedImage getTiffImage(Map map) throws ImageReadException, IOException {
        if (this.tiffImageData == null) {
            return null;
        }
        return new TiffImageParser().getBufferedImage(this, map);
    }

    public TiffField findField(TagInfo tagInfo) throws ImageReadException {
        return findField(tagInfo, false);
    }

    public TiffField findField(TagInfo tagInfo, boolean z) throws ImageReadException {
        if (this.entries == null) {
            return null;
        }
        for (int i = 0; i < this.entries.size(); i++) {
            TiffField tiffField = (TiffField) this.entries.get(i);
            if (tiffField.tag == tagInfo.tag) {
                return tiffField;
            }
        }
        if (!z) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Missing expected field: ");
        stringBuffer.append(tagInfo.getDescription());
        throw new ImageReadException(stringBuffer.toString());
    }

    private ArrayList getRawImageDataElements(TiffField tiffField, TiffField tiffField2) throws ImageReadException {
        int[] intArrayValue = tiffField.getIntArrayValue();
        int[] intArrayValue2 = tiffField2.getIntArrayValue();
        if (intArrayValue.length == intArrayValue2.length) {
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < intArrayValue.length; i++) {
                arrayList.add(new ImageDataElement(intArrayValue[i], intArrayValue2[i]));
            }
            return arrayList;
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("offsets.length(");
        stringBuffer.append(intArrayValue.length);
        stringBuffer.append(") != byteCounts.length(");
        stringBuffer.append(intArrayValue2.length);
        stringBuffer.append(")");
        throw new ImageReadException(stringBuffer.toString());
    }

    public ArrayList getTiffRawImageDataElements() throws ImageReadException {
        TiffField findField = findField(TIFF_TAG_TILE_OFFSETS);
        TiffField findField2 = findField(TIFF_TAG_TILE_BYTE_COUNTS);
        TiffField findField3 = findField(TIFF_TAG_STRIP_OFFSETS);
        TiffField findField4 = findField(TIFF_TAG_STRIP_BYTE_COUNTS);
        if (findField != null && findField2 != null) {
            return getRawImageDataElements(findField, findField2);
        }
        if (findField3 != null && findField4 != null) {
            return getRawImageDataElements(findField3, findField4);
        }
        throw new ImageReadException("Couldn't find image data.");
    }

    public boolean imageDataInStrips() throws ImageReadException {
        TiffField findField = findField(TIFF_TAG_TILE_OFFSETS);
        TiffField findField2 = findField(TIFF_TAG_TILE_BYTE_COUNTS);
        TiffField findField3 = findField(TIFF_TAG_STRIP_OFFSETS);
        TiffField findField4 = findField(TIFF_TAG_STRIP_BYTE_COUNTS);
        if (findField != null && findField2 != null) {
            return false;
        }
        if (findField3 != null && findField4 != null) {
            return true;
        }
        if (findField3 != null && findField4 != null) {
            return true;
        }
        throw new ImageReadException("Couldn't find image data.");
    }

    public ImageDataElement getJpegRawImageDataElement() throws ImageReadException {
        TiffField findField = findField(TIFF_TAG_JPEG_INTERCHANGE_FORMAT);
        TiffField findField2 = findField(TIFF_TAG_JPEG_INTERCHANGE_FORMAT_LENGTH);
        if (findField != null && findField2 != null) {
            return new ImageDataElement(findField.getIntArrayValue()[0], findField2.getIntArrayValue()[0]);
        }
        throw new ImageReadException("Couldn't find image data.");
    }

    public void setTiffImageData(TiffImageData tiffImageData2) {
        this.tiffImageData = tiffImageData2;
    }

    public TiffImageData getTiffImageData() {
        return this.tiffImageData;
    }

    public void setJpegImageData(JpegImageData jpegImageData2) {
        this.jpegImageData = jpegImageData2;
    }

    public JpegImageData getJpegImageData() {
        return this.jpegImageData;
    }
}

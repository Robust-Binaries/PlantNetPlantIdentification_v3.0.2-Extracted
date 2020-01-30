package org.apache.sanselan.formats.tiff.write;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.common.BinaryOutputStream;
import org.apache.sanselan.formats.tiff.JpegImageData;
import org.apache.sanselan.formats.tiff.TiffDirectory;
import org.apache.sanselan.formats.tiff.TiffElement.DataElement;
import org.apache.sanselan.formats.tiff.TiffImageData;
import org.apache.sanselan.formats.tiff.constants.TagConstantsUtils;
import org.apache.sanselan.formats.tiff.constants.TagInfo;
import org.apache.sanselan.formats.tiff.constants.TiffConstants;
import org.apache.sanselan.formats.tiff.constants.TiffDirectoryConstants.ExifDirectoryType;
import org.apache.sanselan.formats.tiff.fieldtypes.FieldType;
import org.apache.sanselan.formats.tiff.write.TiffOutputItem.Value;

public final class TiffOutputDirectory extends TiffOutputItem implements TiffConstants {
    private final ArrayList fields = new ArrayList();
    private JpegImageData jpegImageData = null;
    private TiffOutputDirectory nextDirectory = null;
    private TiffImageData tiffImageData = null;
    public final int type;

    public void setNextDirectory(TiffOutputDirectory tiffOutputDirectory) {
        this.nextDirectory = tiffOutputDirectory;
    }

    public TiffOutputDirectory(int i) {
        this.type = i;
    }

    public void add(TiffOutputField tiffOutputField) {
        this.fields.add(tiffOutputField);
    }

    public ArrayList getFields() {
        return new ArrayList(this.fields);
    }

    public void removeField(TagInfo tagInfo) {
        removeField(tagInfo.tag);
    }

    public void removeField(int i) {
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < this.fields.size(); i2++) {
            TiffOutputField tiffOutputField = (TiffOutputField) this.fields.get(i2);
            if (tiffOutputField.tag == i) {
                arrayList.add(tiffOutputField);
            }
        }
        this.fields.removeAll(arrayList);
    }

    public TiffOutputField findField(TagInfo tagInfo) {
        return findField(tagInfo.tag);
    }

    public TiffOutputField findField(int i) {
        for (int i2 = 0; i2 < this.fields.size(); i2++) {
            TiffOutputField tiffOutputField = (TiffOutputField) this.fields.get(i2);
            if (tiffOutputField.tag == i) {
                return tiffOutputField;
            }
        }
        return null;
    }

    public void sortFields() {
        Collections.sort(this.fields, new Comparator() {
            public int compare(Object obj, Object obj2) {
                TiffOutputField tiffOutputField = (TiffOutputField) obj;
                TiffOutputField tiffOutputField2 = (TiffOutputField) obj2;
                if (tiffOutputField.tag != tiffOutputField2.tag) {
                    return tiffOutputField.tag - tiffOutputField2.tag;
                }
                return tiffOutputField.getSortHint() - tiffOutputField2.getSortHint();
            }
        });
    }

    public String description() {
        return TiffDirectory.description(this.type);
    }

    public void writeItem(BinaryOutputStream binaryOutputStream) throws IOException, ImageWriteException {
        binaryOutputStream.write2Bytes(this.fields.size());
        for (int i = 0; i < this.fields.size(); i++) {
            ((TiffOutputField) this.fields.get(i)).writeField(binaryOutputStream);
        }
        TiffOutputDirectory tiffOutputDirectory = this.nextDirectory;
        int offset = tiffOutputDirectory != null ? tiffOutputDirectory.getOffset() : 0;
        if (offset == -1) {
            binaryOutputStream.write4Bytes(0);
        } else {
            binaryOutputStream.write4Bytes(offset);
        }
    }

    public void setJpegImageData(JpegImageData jpegImageData2) {
        this.jpegImageData = jpegImageData2;
    }

    public JpegImageData getRawJpegImageData() {
        return this.jpegImageData;
    }

    public void setTiffImageData(TiffImageData tiffImageData2) {
        this.tiffImageData = tiffImageData2;
    }

    public TiffImageData getRawTiffImageData() {
        return this.tiffImageData;
    }

    public int getItemLength() {
        return (this.fields.size() * 12) + 2 + 4;
    }

    public String getItemDescription() {
        ExifDirectoryType exifDirectoryType = TagConstantsUtils.getExifDirectoryType(this.type);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Directory: ");
        stringBuffer.append(exifDirectoryType.name);
        stringBuffer.append(" (");
        stringBuffer.append(this.type);
        stringBuffer.append(")");
        return stringBuffer.toString();
    }

    private void removeFieldIfPresent(TagInfo tagInfo) {
        TiffOutputField findField = findField(tagInfo);
        if (findField != null) {
            this.fields.remove(findField);
        }
    }

    /* access modifiers changed from: protected */
    public List getOutputItems(TiffOutputSummary tiffOutputSummary) throws ImageWriteException {
        TiffOutputField tiffOutputField;
        TagInfo tagInfo;
        TagInfo tagInfo2;
        removeFieldIfPresent(TIFF_TAG_JPEG_INTERCHANGE_FORMAT);
        removeFieldIfPresent(TIFF_TAG_JPEG_INTERCHANGE_FORMAT_LENGTH);
        ImageDataOffsets imageDataOffsets = null;
        if (this.jpegImageData != null) {
            tiffOutputField = new TiffOutputField(TIFF_TAG_JPEG_INTERCHANGE_FORMAT, FIELD_TYPE_LONG, 1, FieldType.getStubLocalValue());
            add(tiffOutputField);
            add(new TiffOutputField(TIFF_TAG_JPEG_INTERCHANGE_FORMAT_LENGTH, FIELD_TYPE_LONG, 1, FIELD_TYPE_LONG.writeData(new int[]{this.jpegImageData.length}, tiffOutputSummary.byteOrder)));
        } else {
            tiffOutputField = null;
        }
        removeFieldIfPresent(TIFF_TAG_STRIP_OFFSETS);
        removeFieldIfPresent(TIFF_TAG_STRIP_BYTE_COUNTS);
        removeFieldIfPresent(TIFF_TAG_TILE_OFFSETS);
        removeFieldIfPresent(TIFF_TAG_TILE_BYTE_COUNTS);
        TiffImageData tiffImageData2 = this.tiffImageData;
        if (tiffImageData2 != null) {
            if (tiffImageData2.stripsNotTiles()) {
                tagInfo2 = TIFF_TAG_STRIP_OFFSETS;
                tagInfo = TIFF_TAG_STRIP_BYTE_COUNTS;
            } else {
                tagInfo2 = TIFF_TAG_TILE_OFFSETS;
                tagInfo = TIFF_TAG_TILE_BYTE_COUNTS;
            }
            DataElement[] imageData = this.tiffImageData.getImageData();
            int[] iArr = new int[imageData.length];
            int[] iArr2 = new int[imageData.length];
            for (int i = 0; i < imageData.length; i++) {
                iArr2[i] = imageData[i].length;
            }
            TiffOutputField tiffOutputField2 = new TiffOutputField(tagInfo2, FIELD_TYPE_LONG, iArr.length, FIELD_TYPE_LONG.writeData(iArr, tiffOutputSummary.byteOrder));
            add(tiffOutputField2);
            add(new TiffOutputField(tagInfo, FIELD_TYPE_LONG, iArr2.length, FIELD_TYPE_LONG.writeData(iArr2, tiffOutputSummary.byteOrder)));
            imageDataOffsets = new ImageDataOffsets(imageData, iArr, tiffOutputField2);
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(this);
        sortFields();
        for (int i2 = 0; i2 < this.fields.size(); i2++) {
            TiffOutputField tiffOutputField3 = (TiffOutputField) this.fields.get(i2);
            if (!tiffOutputField3.isLocalValue()) {
                arrayList.add(tiffOutputField3.getSeperateValue());
            }
        }
        if (imageDataOffsets != null) {
            for (TiffOutputItem add : imageDataOffsets.outputItems) {
                arrayList.add(add);
            }
            tiffOutputSummary.addTiffImageData(imageDataOffsets);
        }
        JpegImageData jpegImageData2 = this.jpegImageData;
        if (jpegImageData2 != null) {
            Value value = new Value("JPEG image data", jpegImageData2.data);
            arrayList.add(value);
            tiffOutputSummary.add(value, tiffOutputField);
        }
        return arrayList;
    }
}

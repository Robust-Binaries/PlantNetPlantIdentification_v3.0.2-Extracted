package org.apache.sanselan.formats.tiff;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.common.byteSources.ByteSource;
import org.apache.sanselan.formats.tiff.constants.TagInfo;
import org.apache.sanselan.formats.tiff.constants.TiffConstants;
import org.apache.sanselan.formats.tiff.fieldtypes.FieldType;

public class TiffField implements TiffConstants {
    private static final Map ALL_TAG_MAP = makeTagMap(ALL_TAGS, true, "All");
    public static final String Attribute_Tag = "Tag";
    private static final Map EXIF_TAG_MAP = makeTagMap(ALL_EXIF_TAGS, true, "EXIF");
    private static final Map GPS_TAG_MAP = makeTagMap(ALL_GPS_TAGS, false, "GPS");
    private static final Map TIFF_TAG_MAP = makeTagMap(ALL_TIFF_TAGS, false, "TIFF");
    public final int byteOrder;
    public final int directoryType;
    public final FieldType fieldType;
    public final int length;
    public byte[] oversizeValue = null;
    private int sortHint = -1;
    public final int tag;
    public final TagInfo tagInfo;
    public final int type;
    public final int valueOffset;
    public final byte[] valueOffsetBytes;

    public final class OversizeValueElement extends TiffElement {
        public OversizeValueElement(int i, int i2) {
            super(i, i2);
        }

        public String getElementDescription(boolean z) {
            if (z) {
                return null;
            }
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("OversizeValueElement, tag: ");
            stringBuffer.append(TiffField.this.tagInfo.name);
            stringBuffer.append(", fieldType: ");
            stringBuffer.append(TiffField.this.fieldType.name);
            return stringBuffer.toString();
        }
    }

    public TiffField(int i, int i2, int i3, int i4, int i5, byte[] bArr, int i6) {
        this.tag = i;
        this.directoryType = i2;
        this.type = i3;
        this.length = i4;
        this.valueOffset = i5;
        this.valueOffsetBytes = bArr;
        this.byteOrder = i6;
        this.fieldType = getFieldType(i3);
        this.tagInfo = getTag(i2, i);
    }

    public boolean isLocalValue() {
        return this.fieldType.isLocalValue(this);
    }

    public int getBytesLength() throws ImageReadException {
        return this.fieldType.getBytesLength(this);
    }

    public TiffElement getOversizeValueElement() {
        if (this.fieldType.isLocalValue(this)) {
            return null;
        }
        return new OversizeValueElement(this.valueOffset, this.oversizeValue.length);
    }

    public void setOversizeValue(byte[] bArr) {
        this.oversizeValue = bArr;
    }

    private static FieldType getFieldType(int i) {
        for (FieldType fieldType2 : FIELD_TYPES) {
            if (fieldType2.type == i) {
                return fieldType2;
            }
        }
        return FIELD_TYPE_UNKNOWN;
    }

    private static TagInfo getTag(int i, int i2, List list) {
        if (list.size() < 1) {
            return null;
        }
        for (int i3 = 0; i3 < list.size(); i3++) {
            TagInfo tagInfo2 = (TagInfo) list.get(i3);
            if (tagInfo2.directoryType != EXIF_DIRECTORY_UNKNOWN) {
                if (i == -2 && tagInfo2.directoryType == EXIF_DIRECTORY_EXIF_IFD) {
                    return tagInfo2;
                }
                if (i == -4 && tagInfo2.directoryType == EXIF_DIRECTORY_INTEROP_IFD) {
                    return tagInfo2;
                }
                if (i == -3 && tagInfo2.directoryType == EXIF_DIRECTORY_GPS) {
                    return tagInfo2;
                }
                if (i == -5 && tagInfo2.directoryType == EXIF_DIRECTORY_MAKER_NOTES) {
                    return tagInfo2;
                }
                if (i == 0 && tagInfo2.directoryType == TIFF_DIRECTORY_IFD0) {
                    return tagInfo2;
                }
                if (i == 1 && tagInfo2.directoryType == TIFF_DIRECTORY_IFD1) {
                    return tagInfo2;
                }
                if (i == 2 && tagInfo2.directoryType == TIFF_DIRECTORY_IFD2) {
                    return tagInfo2;
                }
                if (i == 3 && tagInfo2.directoryType == TIFF_DIRECTORY_IFD3) {
                    return tagInfo2;
                }
            }
        }
        for (int i4 = 0; i4 < list.size(); i4++) {
            TagInfo tagInfo3 = (TagInfo) list.get(i4);
            if (tagInfo3.directoryType != EXIF_DIRECTORY_UNKNOWN) {
                if (i >= 0 && tagInfo3.directoryType.isImageDirectory()) {
                    return tagInfo3;
                }
                if (i < 0 && !tagInfo3.directoryType.isImageDirectory()) {
                    return tagInfo3;
                }
            }
        }
        for (int i5 = 0; i5 < list.size(); i5++) {
            TagInfo tagInfo4 = (TagInfo) list.get(i5);
            if (tagInfo4.directoryType == EXIF_DIRECTORY_UNKNOWN) {
                return tagInfo4;
            }
        }
        return TIFF_TAG_UNKNOWN;
    }

    private static TagInfo getTag(int i, int i2) {
        List list = (List) EXIF_TAG_MAP.get(new Integer(i2));
        if (list == null) {
            return TIFF_TAG_UNKNOWN;
        }
        return getTag(i, i2, list);
    }

    private int getValueLengthInBytes() {
        return this.fieldType.length * this.length;
    }

    public void fillInValue(ByteSource byteSource) throws ImageReadException, IOException {
        if (!this.fieldType.isLocalValue(this)) {
            setOversizeValue(byteSource.getBlock(this.valueOffset, getValueLengthInBytes()));
        }
    }

    public String getValueDescription() {
        try {
            return getValueDescription(getValue());
        } catch (ImageReadException e) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("Invalid value: ");
            stringBuffer.append(e.getMessage());
            return stringBuffer.toString();
        }
    }

    private String getValueDescription(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Number) {
            return obj.toString();
        }
        if (obj instanceof String) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("'");
            stringBuffer.append(obj.toString().trim());
            stringBuffer.append("'");
            return stringBuffer.toString();
        } else if (obj instanceof Date) {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format((Date) obj);
        } else {
            int i = 0;
            if (obj instanceof Object[]) {
                Object[] objArr = (Object[]) obj;
                StringBuffer stringBuffer2 = new StringBuffer();
                while (true) {
                    if (i >= objArr.length) {
                        break;
                    }
                    Object obj2 = objArr[i];
                    if (i > 50) {
                        StringBuffer stringBuffer3 = new StringBuffer();
                        stringBuffer3.append("... (");
                        stringBuffer3.append(objArr.length);
                        stringBuffer3.append(")");
                        stringBuffer2.append(stringBuffer3.toString());
                        break;
                    }
                    if (i > 0) {
                        stringBuffer2.append(", ");
                    }
                    StringBuffer stringBuffer4 = new StringBuffer();
                    stringBuffer4.append("");
                    stringBuffer4.append(obj2);
                    stringBuffer2.append(stringBuffer4.toString());
                    i++;
                }
                return stringBuffer2.toString();
            } else if (obj instanceof int[]) {
                int[] iArr = (int[]) obj;
                StringBuffer stringBuffer5 = new StringBuffer();
                while (true) {
                    if (i >= iArr.length) {
                        break;
                    }
                    int i2 = iArr[i];
                    if (i > 50) {
                        StringBuffer stringBuffer6 = new StringBuffer();
                        stringBuffer6.append("... (");
                        stringBuffer6.append(iArr.length);
                        stringBuffer6.append(")");
                        stringBuffer5.append(stringBuffer6.toString());
                        break;
                    }
                    if (i > 0) {
                        stringBuffer5.append(", ");
                    }
                    StringBuffer stringBuffer7 = new StringBuffer();
                    stringBuffer7.append("");
                    stringBuffer7.append(i2);
                    stringBuffer5.append(stringBuffer7.toString());
                    i++;
                }
                return stringBuffer5.toString();
            } else if (obj instanceof long[]) {
                long[] jArr = (long[]) obj;
                StringBuffer stringBuffer8 = new StringBuffer();
                while (true) {
                    if (i >= jArr.length) {
                        break;
                    }
                    long j = jArr[i];
                    if (i > 50) {
                        StringBuffer stringBuffer9 = new StringBuffer();
                        stringBuffer9.append("... (");
                        stringBuffer9.append(jArr.length);
                        stringBuffer9.append(")");
                        stringBuffer8.append(stringBuffer9.toString());
                        break;
                    }
                    if (i > 0) {
                        stringBuffer8.append(", ");
                    }
                    StringBuffer stringBuffer10 = new StringBuffer();
                    stringBuffer10.append("");
                    stringBuffer10.append(j);
                    stringBuffer8.append(stringBuffer10.toString());
                    i++;
                }
                return stringBuffer8.toString();
            } else if (obj instanceof double[]) {
                double[] dArr = (double[]) obj;
                StringBuffer stringBuffer11 = new StringBuffer();
                while (true) {
                    if (i >= dArr.length) {
                        break;
                    }
                    double d = dArr[i];
                    if (i > 50) {
                        StringBuffer stringBuffer12 = new StringBuffer();
                        stringBuffer12.append("... (");
                        stringBuffer12.append(dArr.length);
                        stringBuffer12.append(")");
                        stringBuffer11.append(stringBuffer12.toString());
                        break;
                    }
                    if (i > 0) {
                        stringBuffer11.append(", ");
                    }
                    StringBuffer stringBuffer13 = new StringBuffer();
                    stringBuffer13.append("");
                    stringBuffer13.append(d);
                    stringBuffer11.append(stringBuffer13.toString());
                    i++;
                }
                return stringBuffer11.toString();
            } else if (obj instanceof byte[]) {
                byte[] bArr = (byte[]) obj;
                StringBuffer stringBuffer14 = new StringBuffer();
                while (true) {
                    if (i >= bArr.length) {
                        break;
                    }
                    byte b = bArr[i];
                    if (i > 50) {
                        StringBuffer stringBuffer15 = new StringBuffer();
                        stringBuffer15.append("... (");
                        stringBuffer15.append(bArr.length);
                        stringBuffer15.append(")");
                        stringBuffer14.append(stringBuffer15.toString());
                        break;
                    }
                    if (i > 0) {
                        stringBuffer14.append(", ");
                    }
                    StringBuffer stringBuffer16 = new StringBuffer();
                    stringBuffer16.append("");
                    stringBuffer16.append(b);
                    stringBuffer14.append(stringBuffer16.toString());
                    i++;
                }
                return stringBuffer14.toString();
            } else if (obj instanceof char[]) {
                char[] cArr = (char[]) obj;
                StringBuffer stringBuffer17 = new StringBuffer();
                while (true) {
                    if (i >= cArr.length) {
                        break;
                    }
                    char c = cArr[i];
                    if (i > 50) {
                        StringBuffer stringBuffer18 = new StringBuffer();
                        stringBuffer18.append("... (");
                        stringBuffer18.append(cArr.length);
                        stringBuffer18.append(")");
                        stringBuffer17.append(stringBuffer18.toString());
                        break;
                    }
                    if (i > 0) {
                        stringBuffer17.append(", ");
                    }
                    StringBuffer stringBuffer19 = new StringBuffer();
                    stringBuffer19.append("");
                    stringBuffer19.append(c);
                    stringBuffer17.append(stringBuffer19.toString());
                    i++;
                }
                return stringBuffer17.toString();
            } else if (obj instanceof float[]) {
                float[] fArr = (float[]) obj;
                StringBuffer stringBuffer20 = new StringBuffer();
                while (true) {
                    if (i >= fArr.length) {
                        break;
                    }
                    float f = fArr[i];
                    if (i > 50) {
                        StringBuffer stringBuffer21 = new StringBuffer();
                        stringBuffer21.append("... (");
                        stringBuffer21.append(fArr.length);
                        stringBuffer21.append(")");
                        stringBuffer20.append(stringBuffer21.toString());
                        break;
                    }
                    if (i > 0) {
                        stringBuffer20.append(", ");
                    }
                    StringBuffer stringBuffer22 = new StringBuffer();
                    stringBuffer22.append("");
                    stringBuffer22.append(f);
                    stringBuffer20.append(stringBuffer22.toString());
                    i++;
                }
                return stringBuffer20.toString();
            } else {
                StringBuffer stringBuffer23 = new StringBuffer();
                stringBuffer23.append("Unknown: ");
                stringBuffer23.append(obj.getClass().getName());
                return stringBuffer23.toString();
            }
        }
    }

    public void dump() {
        PrintWriter printWriter = new PrintWriter(System.out);
        dump(printWriter);
        printWriter.flush();
    }

    public void dump(PrintWriter printWriter) {
        dump(printWriter, null);
    }

    public void dump(PrintWriter printWriter, String str) {
        if (str != null) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(str);
            stringBuffer.append(": ");
            printWriter.print(stringBuffer.toString());
        }
        printWriter.println(toString());
        printWriter.flush();
    }

    public String getDescriptionWithoutValue() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.tag);
        stringBuffer.append(" (0x");
        stringBuffer.append(Integer.toHexString(this.tag));
        stringBuffer.append(": ");
        stringBuffer.append(this.tagInfo.name);
        stringBuffer.append("): ");
        return stringBuffer.toString();
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer stringBuffer2 = new StringBuffer();
        stringBuffer2.append(this.tag);
        stringBuffer2.append(" (0x");
        stringBuffer2.append(Integer.toHexString(this.tag));
        stringBuffer2.append(": ");
        stringBuffer2.append(this.tagInfo.name);
        stringBuffer2.append("): ");
        stringBuffer.append(stringBuffer2.toString());
        StringBuffer stringBuffer3 = new StringBuffer();
        stringBuffer3.append(getValueDescription());
        stringBuffer3.append(" (");
        stringBuffer3.append(this.length);
        stringBuffer3.append(" ");
        stringBuffer3.append(this.fieldType.name);
        stringBuffer3.append(")");
        stringBuffer.append(stringBuffer3.toString());
        return stringBuffer.toString();
    }

    public String getTagName() {
        if (this.tagInfo != TIFF_TAG_UNKNOWN) {
            return this.tagInfo.name;
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.tagInfo.name);
        stringBuffer.append(" (0x");
        stringBuffer.append(Integer.toHexString(this.tag));
        stringBuffer.append(")");
        return stringBuffer.toString();
    }

    public String getFieldTypeName() {
        return this.fieldType.name;
    }

    public Object getValue() throws ImageReadException {
        return this.tagInfo.getValue(this);
    }

    public String getStringValue() throws ImageReadException {
        Object value = getValue();
        if (value == null) {
            return null;
        }
        if (value instanceof String) {
            return (String) value;
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Expected String value(");
        stringBuffer.append(this.tagInfo.getDescription());
        stringBuffer.append("): ");
        stringBuffer.append(value);
        throw new ImageReadException(stringBuffer.toString());
    }

    private static final Map makeTagMap(TagInfo[] tagInfoArr, boolean z, String str) {
        Hashtable hashtable = new Hashtable();
        for (TagInfo tagInfo2 : tagInfoArr) {
            Integer num = new Integer(tagInfo2.tag);
            List list = (List) hashtable.get(num);
            if (list == null) {
                list = new ArrayList();
                hashtable.put(num, list);
            }
            list.add(tagInfo2);
        }
        return hashtable;
    }

    public int[] getIntArrayValue() throws ImageReadException {
        Object value = getValue();
        int i = 0;
        if (value instanceof Number) {
            return new int[]{((Number) value).intValue()};
        } else if (value instanceof Number[]) {
            Number[] numberArr = (Number[]) value;
            int[] iArr = new int[numberArr.length];
            while (i < numberArr.length) {
                iArr[i] = numberArr[i].intValue();
                i++;
            }
            return iArr;
        } else if (value instanceof int[]) {
            int[] iArr2 = (int[]) value;
            int[] iArr3 = new int[iArr2.length];
            while (i < iArr2.length) {
                iArr3[i] = iArr2[i];
                i++;
            }
            return iArr3;
        } else {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("Unknown value: ");
            stringBuffer.append(value);
            stringBuffer.append(" for: ");
            stringBuffer.append(this.tagInfo.getDescription());
            throw new ImageReadException(stringBuffer.toString());
        }
    }

    public double[] getDoubleArrayValue() throws ImageReadException {
        Object value = getValue();
        int i = 0;
        if (value instanceof Number) {
            return new double[]{((Number) value).doubleValue()};
        } else if (value instanceof Number[]) {
            Number[] numberArr = (Number[]) value;
            double[] dArr = new double[numberArr.length];
            while (i < numberArr.length) {
                dArr[i] = numberArr[i].doubleValue();
                i++;
            }
            return dArr;
        } else if (value instanceof int[]) {
            int[] iArr = (int[]) value;
            double[] dArr2 = new double[iArr.length];
            while (i < iArr.length) {
                dArr2[i] = (double) iArr[i];
                i++;
            }
            return dArr2;
        } else if (value instanceof float[]) {
            float[] fArr = (float[]) value;
            double[] dArr3 = new double[fArr.length];
            while (i < fArr.length) {
                dArr3[i] = (double) fArr[i];
                i++;
            }
            return dArr3;
        } else if (value instanceof double[]) {
            double[] dArr4 = (double[]) value;
            double[] dArr5 = new double[dArr4.length];
            while (i < dArr4.length) {
                dArr5[i] = dArr4[i];
                i++;
            }
            return dArr5;
        } else {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("Unknown value: ");
            stringBuffer.append(value);
            stringBuffer.append(" for: ");
            stringBuffer.append(this.tagInfo.getDescription());
            throw new ImageReadException(stringBuffer.toString());
        }
    }

    public int getIntValueOrArraySum() throws ImageReadException {
        Object value = getValue();
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        int i = 0;
        if (value instanceof Number[]) {
            Number[] numberArr = (Number[]) value;
            int i2 = 0;
            while (i < numberArr.length) {
                i2 += numberArr[i].intValue();
                i++;
            }
            return i2;
        } else if (value instanceof int[]) {
            int[] iArr = (int[]) value;
            int i3 = 0;
            while (i < iArr.length) {
                i3 += iArr[i];
                i++;
            }
            return i3;
        } else {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("Unknown value: ");
            stringBuffer.append(value);
            stringBuffer.append(" for: ");
            stringBuffer.append(this.tagInfo.getDescription());
            throw new ImageReadException(stringBuffer.toString());
        }
    }

    public int getIntValue() throws ImageReadException {
        Object value = getValue();
        if (value != null) {
            return ((Number) value).intValue();
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Missing value: ");
        stringBuffer.append(this.tagInfo.getDescription());
        throw new ImageReadException(stringBuffer.toString());
    }

    public double getDoubleValue() throws ImageReadException {
        Object value = getValue();
        if (value != null) {
            return ((Number) value).doubleValue();
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Missing value: ");
        stringBuffer.append(this.tagInfo.getDescription());
        throw new ImageReadException(stringBuffer.toString());
    }

    public byte[] getByteArrayValue() throws ImageReadException {
        return this.fieldType.getRawBytes(this);
    }

    public int getSortHint() {
        return this.sortHint;
    }

    public void setSortHint(int i) {
        this.sortHint = i;
    }
}

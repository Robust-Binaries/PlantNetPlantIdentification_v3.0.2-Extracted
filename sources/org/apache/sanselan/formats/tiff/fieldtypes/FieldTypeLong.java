package org.apache.sanselan.formats.tiff.fieldtypes;

import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.formats.tiff.TiffField;
import org.apache.sanselan.util.Debug;

public class FieldTypeLong extends FieldType {
    public FieldTypeLong(int i, String str) {
        super(i, 4, str);
    }

    public Object getSimpleValue(TiffField tiffField) {
        if (tiffField.length == 1) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(this.name);
            stringBuffer.append(" (");
            stringBuffer.append(tiffField.tagInfo.name);
            stringBuffer.append(")");
            return new Integer(convertByteArrayToInt(stringBuffer.toString(), tiffField.valueOffsetBytes, tiffField.byteOrder));
        }
        StringBuffer stringBuffer2 = new StringBuffer();
        stringBuffer2.append(this.name);
        stringBuffer2.append(" (");
        stringBuffer2.append(tiffField.tagInfo.name);
        stringBuffer2.append(")");
        return convertByteArrayToIntArray(stringBuffer2.toString(), getRawBytes(tiffField), 0, tiffField.length, tiffField.byteOrder);
    }

    public byte[] writeData(Object obj, int i) throws ImageWriteException {
        if (obj instanceof Integer) {
            return convertIntArrayToByteArray(new int[]{((Integer) obj).intValue()}, i);
        } else if (obj instanceof int[]) {
            return convertIntArrayToByteArray((int[]) obj, i);
        } else {
            if (obj instanceof Integer[]) {
                Integer[] numArr = (Integer[]) obj;
                int[] iArr = new int[numArr.length];
                for (int i2 = 0; i2 < iArr.length; i2++) {
                    iArr[i2] = numArr[i2].intValue();
                }
                return convertIntArrayToByteArray(iArr, i);
            }
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("Invalid data: ");
            stringBuffer.append(obj);
            stringBuffer.append(" (");
            stringBuffer.append(Debug.getType(obj));
            stringBuffer.append(")");
            throw new ImageWriteException(stringBuffer.toString());
        }
    }
}

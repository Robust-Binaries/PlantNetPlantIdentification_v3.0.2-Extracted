package org.apache.sanselan.formats.tiff.fieldtypes;

import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.formats.tiff.TiffField;
import org.apache.sanselan.util.Debug;

public class FieldTypeShort extends FieldType {
    public FieldTypeShort(int i, String str) {
        super(i, 2, str);
    }

    public Object getSimpleValue(TiffField tiffField) throws ImageReadException {
        if (tiffField.length == 1) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(this.name);
            stringBuffer.append(" (");
            stringBuffer.append(tiffField.tagInfo.name);
            stringBuffer.append(")");
            return new Integer(convertByteArrayToShort(stringBuffer.toString(), tiffField.valueOffsetBytes, tiffField.byteOrder));
        }
        StringBuffer stringBuffer2 = new StringBuffer();
        stringBuffer2.append(this.name);
        stringBuffer2.append(" (");
        stringBuffer2.append(tiffField.tagInfo.name);
        stringBuffer2.append(")");
        return convertByteArrayToShortArray(stringBuffer2.toString(), getRawBytes(tiffField), 0, tiffField.length, tiffField.byteOrder);
    }

    public byte[] writeData(Object obj, int i) throws ImageWriteException {
        if (obj instanceof Integer) {
            return convertShortArrayToByteArray(new int[]{((Integer) obj).intValue()}, i);
        } else if (obj instanceof int[]) {
            return convertShortArrayToByteArray((int[]) obj, i);
        } else {
            if (obj instanceof Integer[]) {
                Integer[] numArr = (Integer[]) obj;
                int[] iArr = new int[numArr.length];
                for (int i2 = 0; i2 < iArr.length; i2++) {
                    iArr[i2] = numArr[i2].intValue();
                }
                return convertShortArrayToByteArray(iArr, i);
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

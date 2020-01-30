package org.apache.sanselan.formats.tiff.fieldtypes;

import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.formats.tiff.TiffField;
import org.apache.sanselan.util.Debug;

public class FieldTypeFloat extends FieldType {
    public FieldTypeFloat() {
        super(11, 4, "Float");
    }

    public Object getSimpleValue(TiffField tiffField) {
        if (tiffField.length == 1) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(this.name);
            stringBuffer.append(" (");
            stringBuffer.append(tiffField.tagInfo.name);
            stringBuffer.append(")");
            return new Float(convertByteArrayToFloat(stringBuffer.toString(), tiffField.valueOffsetBytes, tiffField.byteOrder));
        }
        StringBuffer stringBuffer2 = new StringBuffer();
        stringBuffer2.append(this.name);
        stringBuffer2.append(" (");
        stringBuffer2.append(tiffField.tagInfo.name);
        stringBuffer2.append(")");
        return convertByteArrayToFloatArray(stringBuffer2.toString(), getRawBytes(tiffField), 0, tiffField.length, tiffField.byteOrder);
    }

    public byte[] writeData(Object obj, int i) throws ImageWriteException {
        if (obj instanceof Float) {
            return convertFloatToByteArray(((Float) obj).floatValue(), i);
        }
        if (obj instanceof float[]) {
            return convertFloatArrayToByteArray((float[]) obj, i);
        }
        if (obj instanceof Float[]) {
            Float[] fArr = (Float[]) obj;
            float[] fArr2 = new float[fArr.length];
            for (int i2 = 0; i2 < fArr2.length; i2++) {
                fArr2[i2] = fArr[i2].floatValue();
            }
            return convertFloatArrayToByteArray(fArr2, i);
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

package org.apache.sanselan.formats.tiff.fieldtypes;

import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.formats.tiff.TiffField;
import org.apache.sanselan.util.Debug;

public class FieldTypeByte extends FieldType {
    public FieldTypeByte(int i, String str) {
        super(i, 1, str);
    }

    public Object getSimpleValue(TiffField tiffField) {
        if (tiffField.length == 1) {
            return new Byte(tiffField.valueOffsetBytes[0]);
        }
        return getRawBytes(tiffField);
    }

    public byte[] writeData(Object obj, int i) throws ImageWriteException {
        if (obj instanceof Byte) {
            return new byte[]{((Byte) obj).byteValue()};
        } else if (obj instanceof byte[]) {
            return (byte[]) obj;
        } else {
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

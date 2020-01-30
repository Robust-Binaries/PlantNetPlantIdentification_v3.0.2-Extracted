package org.apache.sanselan.formats.tiff.fieldtypes;

import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.formats.tiff.TiffField;

public class FieldTypeASCII extends FieldType {
    public FieldTypeASCII(int i, String str) {
        super(i, 1, str);
    }

    public Object getSimpleValue(TiffField tiffField) {
        return new String(getRawBytes(tiffField));
    }

    public byte[] writeData(Object obj, int i) throws ImageWriteException {
        if (obj instanceof byte[]) {
            return (byte[]) obj;
        }
        if (obj instanceof String) {
            return ((String) obj).getBytes();
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Unknown data type: ");
        stringBuffer.append(obj);
        throw new ImageWriteException(stringBuffer.toString());
    }
}

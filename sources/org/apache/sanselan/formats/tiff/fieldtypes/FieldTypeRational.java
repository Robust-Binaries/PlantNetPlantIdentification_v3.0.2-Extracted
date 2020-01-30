package org.apache.sanselan.formats.tiff.fieldtypes;

import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.common.RationalNumber;
import org.apache.sanselan.common.RationalNumberUtilities;
import org.apache.sanselan.formats.tiff.TiffField;
import org.apache.sanselan.util.Debug;

public class FieldTypeRational extends FieldType {
    public FieldTypeRational(int i, String str) {
        super(i, 8, str);
    }

    public Object getSimpleValue(TiffField tiffField) {
        if (tiffField.length == 1) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(this.name);
            stringBuffer.append(" (");
            stringBuffer.append(tiffField.tagInfo.name);
            stringBuffer.append(")");
            return convertByteArrayToRational(stringBuffer.toString(), tiffField.oversizeValue, tiffField.byteOrder);
        }
        StringBuffer stringBuffer2 = new StringBuffer();
        stringBuffer2.append(this.name);
        stringBuffer2.append(" (");
        stringBuffer2.append(tiffField.tagInfo.name);
        stringBuffer2.append(")");
        return convertByteArrayToRationalArray(stringBuffer2.toString(), getRawBytes(tiffField), 0, tiffField.length, tiffField.byteOrder);
    }

    public byte[] writeData(Object obj, int i) throws ImageWriteException {
        if (obj instanceof RationalNumber) {
            return convertRationalToByteArray((RationalNumber) obj, i);
        }
        if (obj instanceof RationalNumber[]) {
            return convertRationalArrayToByteArray((RationalNumber[]) obj, i);
        }
        if (obj instanceof Number) {
            return convertRationalToByteArray(RationalNumberUtilities.getRationalNumber(((Number) obj).doubleValue()), i);
        }
        int i2 = 0;
        if (obj instanceof Number[]) {
            Number[] numberArr = (Number[]) obj;
            RationalNumber[] rationalNumberArr = new RationalNumber[numberArr.length];
            while (i2 < numberArr.length) {
                rationalNumberArr[i2] = RationalNumberUtilities.getRationalNumber(numberArr[i2].doubleValue());
                i2++;
            }
            return convertRationalArrayToByteArray(rationalNumberArr, i);
        } else if (obj instanceof double[]) {
            double[] dArr = (double[]) obj;
            RationalNumber[] rationalNumberArr2 = new RationalNumber[dArr.length];
            while (i2 < dArr.length) {
                rationalNumberArr2[i2] = RationalNumberUtilities.getRationalNumber(dArr[i2]);
                i2++;
            }
            return convertRationalArrayToByteArray(rationalNumberArr2, i);
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

    public byte[] writeData(int i, int i2, int i3) throws ImageWriteException {
        return writeData(new int[]{i}, new int[]{i2}, i3);
    }

    public byte[] writeData(int[] iArr, int[] iArr2, int i) throws ImageWriteException {
        return convertIntArrayToRationalArray(iArr, iArr2, i);
    }
}

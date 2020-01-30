package org.apache.sanselan.formats.tiff;

import java.util.Comparator;

public abstract class TiffElement {
    public static final Comparator COMPARATOR = new Comparator() {
        public int compare(Object obj, Object obj2) {
            return ((TiffElement) obj).offset - ((TiffElement) obj2).offset;
        }
    };
    public final int length;
    public final int offset;

    public static abstract class DataElement extends TiffElement {
        public final byte[] data;

        public DataElement(int i, int i2, byte[] bArr) {
            super(i, i2);
            this.data = bArr;
        }
    }

    public static final class Stub extends TiffElement {
        public Stub(int i, int i2) {
            super(i, i2);
        }

        public String getElementDescription(boolean z) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("Element, offset: ");
            stringBuffer.append(this.offset);
            stringBuffer.append(", length: ");
            stringBuffer.append(this.length);
            stringBuffer.append(", last: ");
            stringBuffer.append(this.offset + this.length);
            stringBuffer.append("");
            return stringBuffer.toString();
        }
    }

    public abstract String getElementDescription(boolean z);

    public TiffElement(int i, int i2) {
        this.offset = i;
        this.length = i2;
    }

    public String getElementDescription() {
        return getElementDescription(false);
    }
}

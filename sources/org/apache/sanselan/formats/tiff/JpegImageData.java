package org.apache.sanselan.formats.tiff;

import org.apache.sanselan.formats.tiff.TiffElement.DataElement;

public class JpegImageData extends DataElement {
    public JpegImageData(int i, int i2, byte[] bArr) {
        super(i, i2, bArr);
    }

    public String getElementDescription(boolean z) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Jpeg image data: ");
        stringBuffer.append(this.data.length);
        stringBuffer.append(" bytes");
        return stringBuffer.toString();
    }
}

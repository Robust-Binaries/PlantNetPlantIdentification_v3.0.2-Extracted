package org.apache.sanselan.formats.transparencyfilters;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.sanselan.ImageReadException;

public class TransparencyFilterTrueColor extends TransparencyFilter {
    private final int transparent_blue;
    private final int transparent_color = ((((this.transparent_red & 255) << 16) | ((this.transparent_green & 255) << 8)) | ((this.transparent_blue & 255) << 0));
    private final int transparent_green;
    private final int transparent_red;

    public TransparencyFilterTrueColor(byte[] bArr) throws ImageReadException, IOException {
        super(bArr);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        this.transparent_red = read2Bytes("transparent_red", byteArrayInputStream, "tRNS: Missing transparent_color");
        this.transparent_green = read2Bytes("transparent_green", byteArrayInputStream, "tRNS: Missing transparent_color");
        this.transparent_blue = read2Bytes("transparent_blue", byteArrayInputStream, "tRNS: Missing transparent_color");
    }

    public int filter(int i, int i2) throws ImageReadException, IOException {
        if ((16777215 & i) == this.transparent_color) {
            return 0;
        }
        return i;
    }
}

package org.apache.sanselan.formats.png.chunks;

import java.io.IOException;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.common.ZLibUtils;
import org.apache.sanselan.formats.png.PngText;
import org.apache.sanselan.formats.png.PngText.iTXt;

public class PNGChunkiTXt extends PNGTextChunk {
    public final String keyword;
    public final String languageTag;
    public final String text;
    public final String translatedKeyword;

    public PNGChunkiTXt(int i, int i2, int i3, byte[] bArr) throws ImageReadException, IOException {
        super(i, i2, i3, bArr);
        int findNull = findNull(bArr);
        if (findNull >= 0) {
            this.keyword = new String(bArr, 0, findNull, "ISO-8859-1");
            int i4 = findNull + 1;
            int i5 = i4 + 1;
            byte b = bArr[i4];
            if (b == 0 || b == 1) {
                boolean z = b == 1;
                int i6 = i5 + 1;
                byte b2 = bArr[i5];
                if (z) {
                    if (b2 != 0) {
                        StringBuffer stringBuffer = new StringBuffer();
                        stringBuffer.append("PNG iTXt chunk has unexpected compression method: ");
                        stringBuffer.append(b2);
                        throw new ImageReadException(stringBuffer.toString());
                    } else if (b2 != 0) {
                        StringBuffer stringBuffer2 = new StringBuffer();
                        stringBuffer2.append("PNG iTXt chunk has unexpected compression method: ");
                        stringBuffer2.append(b2);
                        throw new ImageReadException(stringBuffer2.toString());
                    }
                }
                int findNull2 = findNull(bArr, i6);
                if (findNull2 >= 0) {
                    this.languageTag = new String(bArr, i6, findNull2 - i6, "ISO-8859-1");
                    int i7 = findNull2 + 1;
                    int findNull3 = findNull(bArr, i7);
                    if (findNull3 >= 0) {
                        this.translatedKeyword = new String(bArr, i7, findNull3 - i7, "utf-8");
                        int i8 = findNull3 + 1;
                        if (z) {
                            int length = bArr.length - i8;
                            byte[] bArr2 = new byte[length];
                            System.arraycopy(bArr, i8, bArr2, 0, length);
                            this.text = new String(new ZLibUtils().inflate(bArr2), "utf-8");
                            return;
                        }
                        this.text = new String(bArr, i8, bArr.length - i8, "utf-8");
                        return;
                    }
                    throw new ImageReadException("PNG iTXt chunk translated keyword is not terminated.");
                }
                throw new ImageReadException("PNG iTXt chunk language tag is not terminated.");
            }
            StringBuffer stringBuffer3 = new StringBuffer();
            stringBuffer3.append("PNG iTXt chunk has invalid compression flag: ");
            stringBuffer3.append(b);
            throw new ImageReadException(stringBuffer3.toString());
        }
        throw new ImageReadException("PNG iTXt chunk keyword is not terminated.");
    }

    public String getKeyword() {
        return this.keyword;
    }

    public String getText() {
        return this.text;
    }

    public PngText getContents() {
        return new iTXt(this.keyword, this.text, this.languageTag, this.translatedKeyword);
    }
}

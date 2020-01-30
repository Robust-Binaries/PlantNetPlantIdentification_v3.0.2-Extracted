package org.apache.sanselan.formats.png.chunks;

import java.io.IOException;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.common.ZLibUtils;
import org.apache.sanselan.formats.png.PngText;
import org.apache.sanselan.formats.png.PngText.zTXt;

public class PNGChunkzTXt extends PNGTextChunk {
    public final String keyword;
    public final String text;

    public PNGChunkzTXt(int i, int i2, int i3, byte[] bArr) throws ImageReadException, IOException {
        super(i, i2, i3, bArr);
        int findNull = findNull(bArr);
        if (findNull >= 0) {
            this.keyword = new String(bArr, 0, findNull, "ISO-8859-1");
            int i4 = findNull + 1;
            int i5 = i4 + 1;
            byte b = bArr[i4];
            if (b == 0) {
                int length = bArr.length - i5;
                byte[] bArr2 = new byte[length];
                System.arraycopy(bArr, i5, bArr2, 0, length);
                this.text = new String(new ZLibUtils().inflate(bArr2), "ISO-8859-1");
                return;
            }
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("PNG zTXt chunk has unexpected compression method: ");
            stringBuffer.append(b);
            throw new ImageReadException(stringBuffer.toString());
        }
        throw new ImageReadException("PNG zTXt chunk keyword is unterminated.");
    }

    public String getKeyword() {
        return this.keyword;
    }

    public String getText() {
        return this.text;
    }

    public PngText getContents() {
        return new zTXt(this.keyword, this.text);
    }
}

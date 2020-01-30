package org.apache.sanselan.formats.png.chunks;

import java.io.IOException;
import java.io.PrintStream;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.formats.png.PngText;
import org.apache.sanselan.formats.png.PngText.tEXt;

public class PNGChunktEXt extends PNGTextChunk {
    public final String keyword;
    public final String text;

    public PNGChunktEXt(int i, int i2, int i3, byte[] bArr) throws ImageReadException, IOException {
        super(i, i2, i3, bArr);
        int findNull = findNull(bArr);
        if (findNull >= 0) {
            this.keyword = new String(bArr, 0, findNull, "ISO-8859-1");
            int i4 = findNull + 1;
            this.text = new String(bArr, i4, bArr.length - i4, "ISO-8859-1");
            if (getDebug()) {
                PrintStream printStream = System.out;
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("Keyword: ");
                stringBuffer.append(this.keyword);
                printStream.println(stringBuffer.toString());
                PrintStream printStream2 = System.out;
                StringBuffer stringBuffer2 = new StringBuffer();
                stringBuffer2.append("Text: ");
                stringBuffer2.append(this.text);
                printStream2.println(stringBuffer2.toString());
                return;
            }
            return;
        }
        throw new ImageReadException("PNG tEXt chunk keyword is not terminated.");
    }

    public String getKeyword() {
        return this.keyword;
    }

    public String getText() {
        return this.text;
    }

    public PngText getContents() {
        return new tEXt(this.keyword, this.text);
    }
}

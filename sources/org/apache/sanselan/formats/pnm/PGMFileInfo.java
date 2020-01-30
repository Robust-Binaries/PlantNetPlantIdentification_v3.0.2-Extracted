package org.apache.sanselan.formats.pnm;

import android.support.p000v4.view.ViewCompat;
import java.io.IOException;
import java.io.InputStream;
import org.apache.sanselan.ImageFormat;

public class PGMFileInfo extends FileInfo {
    private final int max;

    public int getBitDepth() {
        return 8;
    }

    public int getColorType() {
        return 2;
    }

    public String getImageTypeDescription() {
        return "PGM: portable pixmap file\tformat";
    }

    public String getMIMEType() {
        return "image/x-portable-pixmap";
    }

    public int getNumComponents() {
        return 1;
    }

    public PGMFileInfo(int i, int i2, boolean z, int i3) {
        super(i, i2, z);
        this.max = i3;
    }

    public ImageFormat getImageType() {
        return ImageFormat.IMAGE_FORMAT_PPM;
    }

    public int getRGB(InputStream inputStream) throws IOException {
        int read = inputStream.read();
        if (read >= 0) {
            int i = read & 255;
            return (i << 0) | -16777216 | (i << 16) | (i << 8);
        }
        throw new IOException("PGM: Unexpected EOF");
    }

    public int getRGB(WhiteSpaceReader whiteSpaceReader) throws IOException {
        int parseInt = Integer.parseInt(whiteSpaceReader.readtoWhiteSpace()) & 255;
        return (parseInt << 0) | (parseInt << 16) | ViewCompat.MEASURED_STATE_MASK | (parseInt << 8);
    }
}

package org.apache.sanselan.formats.pnm;

import android.support.p000v4.view.ViewCompat;
import java.io.IOException;
import java.io.InputStream;
import org.apache.sanselan.ImageFormat;

public class PPMFileInfo extends FileInfo {
    private final int max;

    public void dump() {
    }

    public int getBitDepth() {
        return 8;
    }

    public int getColorType() {
        return 1;
    }

    public String getImageTypeDescription() {
        return "PGM: portable graymap file\tformat";
    }

    public String getMIMEType() {
        return "image/x-portable-graymap";
    }

    public int getNumComponents() {
        return 3;
    }

    public PPMFileInfo(int i, int i2, boolean z, int i3) {
        super(i, i2, z);
        this.max = i3;
    }

    public ImageFormat getImageType() {
        return ImageFormat.IMAGE_FORMAT_PGM;
    }

    public int getRGB(InputStream inputStream) throws IOException {
        int read = inputStream.read();
        int read2 = inputStream.read();
        int read3 = inputStream.read();
        if (read < 0 || read2 < 0 || read3 < 0) {
            throw new IOException("PPM: Unexpected EOF");
        }
        return ((read3 & 255) << 0) | ((read & 255) << 16) | ViewCompat.MEASURED_STATE_MASK | ((read2 & 255) << 8);
    }

    public int getRGB(WhiteSpaceReader whiteSpaceReader) throws IOException {
        int parseInt = Integer.parseInt(whiteSpaceReader.readtoWhiteSpace());
        int parseInt2 = Integer.parseInt(whiteSpaceReader.readtoWhiteSpace());
        return ((Integer.parseInt(whiteSpaceReader.readtoWhiteSpace()) & 255) << 0) | ((parseInt & 255) << 16) | ViewCompat.MEASURED_STATE_MASK | ((parseInt2 & 255) << 8);
    }
}

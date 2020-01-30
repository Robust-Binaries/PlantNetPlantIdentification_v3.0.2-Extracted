package org.apache.sanselan.formats.pnm;

import android.support.p000v4.view.ViewCompat;
import java.io.IOException;
import java.io.InputStream;
import org.apache.sanselan.ImageFormat;

public class PBMFileInfo extends FileInfo {
    private int bitcache = 0;
    private int bits_in_cache = 0;

    public int getBitDepth() {
        return 1;
    }

    public int getColorType() {
        return 0;
    }

    public String getImageTypeDescription() {
        return "PBM: portable bitmap fileformat";
    }

    public String getMIMEType() {
        return "image/x-portable-bitmap";
    }

    public int getNumComponents() {
        return 1;
    }

    public PBMFileInfo(int i, int i2, boolean z) {
        super(i, i2, z);
    }

    public ImageFormat getImageType() {
        return ImageFormat.IMAGE_FORMAT_PBM;
    }

    /* access modifiers changed from: protected */
    public void newline() {
        this.bitcache = 0;
        this.bits_in_cache = 0;
    }

    public int getRGB(InputStream inputStream) throws IOException {
        if (this.bits_in_cache < 1) {
            int read = inputStream.read();
            if (read >= 0) {
                this.bitcache = read & 255;
                this.bits_in_cache += 8;
            } else {
                throw new IOException("PBM: Unexpected EOF");
            }
        }
        int i = this.bitcache;
        int i2 = (i >> 7) & 1;
        this.bitcache = i << 1;
        this.bits_in_cache--;
        if (i2 == 0) {
            return -1;
        }
        if (i2 == 1) {
            return ViewCompat.MEASURED_STATE_MASK;
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("PBM: bad bit: ");
        stringBuffer.append(i2);
        throw new IOException(stringBuffer.toString());
    }

    public int getRGB(WhiteSpaceReader whiteSpaceReader) throws IOException {
        int parseInt = Integer.parseInt(whiteSpaceReader.readtoWhiteSpace());
        if (parseInt == 0) {
            return ViewCompat.MEASURED_STATE_MASK;
        }
        if (parseInt == 1) {
            return -1;
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("PBM: bad bit: ");
        stringBuffer.append(parseInt);
        throw new IOException(stringBuffer.toString());
    }
}

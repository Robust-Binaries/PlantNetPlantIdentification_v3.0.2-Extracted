package org.apache.sanselan.formats.bmp.pixelparsers;

import android.support.p000v4.view.ViewCompat;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import kotlin.UByte;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.common.BinaryFileParser;
import org.apache.sanselan.formats.bmp.BmpHeaderInfo;

public abstract class PixelParser {
    protected final BinaryFileParser bfp = new BinaryFileParser(73);
    public final BmpHeaderInfo bhi;
    public final byte[] colorTable;
    public final byte[] imageData;

    /* renamed from: is */
    protected final ByteArrayInputStream f166is;

    public abstract void processImage(BufferedImage bufferedImage) throws ImageReadException, IOException;

    public PixelParser(BmpHeaderInfo bmpHeaderInfo, byte[] bArr, byte[] bArr2) {
        this.bhi = bmpHeaderInfo;
        this.colorTable = bArr;
        this.imageData = bArr2;
        this.f166is = new ByteArrayInputStream(bArr2);
    }

    /* access modifiers changed from: protected */
    public int getColorTableRGB(int i) {
        int i2 = i * 4;
        byte[] bArr = this.colorTable;
        return ((bArr[i2 + 2] & UByte.MAX_VALUE) << 16) | ViewCompat.MEASURED_STATE_MASK | ((bArr[i2 + 1] & UByte.MAX_VALUE) << 8) | ((bArr[i2 + 0] & UByte.MAX_VALUE) << 0);
    }
}

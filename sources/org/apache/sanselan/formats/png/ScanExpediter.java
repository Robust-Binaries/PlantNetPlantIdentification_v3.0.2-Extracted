package org.apache.sanselan.formats.png;

import android.support.p000v4.view.ViewCompat;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.common.BinaryFileParser;
import org.apache.sanselan.formats.png.chunks.PNGChunkPLTE;
import org.apache.sanselan.formats.png.scanlinefilters.ScanlineFilter;
import org.apache.sanselan.formats.png.scanlinefilters.ScanlineFilterAverage;
import org.apache.sanselan.formats.png.scanlinefilters.ScanlineFilterNone;
import org.apache.sanselan.formats.png.scanlinefilters.ScanlineFilterPaeth;
import org.apache.sanselan.formats.png.scanlinefilters.ScanlineFilterSub;
import org.apache.sanselan.formats.png.scanlinefilters.ScanlineFilterUp;
import org.apache.sanselan.formats.transparencyfilters.TransparencyFilter;

public abstract class ScanExpediter extends BinaryFileParser {

    /* renamed from: bi */
    protected final BufferedImage f167bi;
    protected final int bitDepth;
    protected final int bitsPerPixel;
    protected final int bytesPerPixel;
    protected final int colorType;
    protected final GammaCorrection gammaCorrection;
    protected final int height;

    /* renamed from: is */
    protected final InputStream f168is;
    protected final PNGChunkPLTE pngChunkPLTE;
    protected final TransparencyFilter transparencyFilter;
    protected final int width;

    public abstract void drive() throws ImageReadException, IOException;

    /* access modifiers changed from: protected */
    public final int getPixelARGB(int i, int i2, int i3, int i4) {
        return ((i & 255) << 24) | ((i2 & 255) << 16) | ((i3 & 255) << 8) | ((i4 & 255) << 0);
    }

    public ScanExpediter(int i, int i2, InputStream inputStream, BufferedImage bufferedImage, int i3, int i4, int i5, PNGChunkPLTE pNGChunkPLTE, GammaCorrection gammaCorrection2, TransparencyFilter transparencyFilter2) {
        this.width = i;
        this.height = i2;
        this.f168is = inputStream;
        this.f167bi = bufferedImage;
        this.colorType = i3;
        this.bitDepth = i4;
        this.bytesPerPixel = getBitsToBytesRoundingUp(i5);
        this.bitsPerPixel = i5;
        this.pngChunkPLTE = pNGChunkPLTE;
        this.gammaCorrection = gammaCorrection2;
        this.transparencyFilter = transparencyFilter2;
    }

    /* access modifiers changed from: protected */
    public int getBitsToBytesRoundingUp(int i) {
        int i2 = i / 8;
        return i % 8 > 0 ? i2 + 1 : i2;
    }

    /* access modifiers changed from: protected */
    public final int getPixelRGB(int i, int i2, int i3) {
        return getPixelARGB(255, i, i2, i3);
    }

    /* access modifiers changed from: protected */
    public int getRGB(BitParser bitParser, int i) throws ImageReadException, IOException {
        int i2 = this.colorType;
        if (i2 == 0) {
            int sampleAsByte = bitParser.getSampleAsByte(i, 0);
            GammaCorrection gammaCorrection2 = this.gammaCorrection;
            if (gammaCorrection2 != null) {
                sampleAsByte = gammaCorrection2.correctSample(sampleAsByte);
            }
            int pixelRGB = getPixelRGB(sampleAsByte, sampleAsByte, sampleAsByte);
            TransparencyFilter transparencyFilter2 = this.transparencyFilter;
            if (transparencyFilter2 != null) {
                pixelRGB = transparencyFilter2.filter(pixelRGB, sampleAsByte);
            }
            return pixelRGB;
        } else if (i2 != 6) {
            switch (i2) {
                case 2:
                    int sampleAsByte2 = bitParser.getSampleAsByte(i, 0);
                    int sampleAsByte3 = bitParser.getSampleAsByte(i, 1);
                    int sampleAsByte4 = bitParser.getSampleAsByte(i, 2);
                    int pixelRGB2 = getPixelRGB(sampleAsByte2, sampleAsByte3, sampleAsByte4);
                    TransparencyFilter transparencyFilter3 = this.transparencyFilter;
                    if (transparencyFilter3 != null) {
                        pixelRGB2 = transparencyFilter3.filter(pixelRGB2, -1);
                    }
                    GammaCorrection gammaCorrection3 = this.gammaCorrection;
                    if (gammaCorrection3 != null) {
                        pixelRGB2 = getPixelARGB((pixelRGB2 & ViewCompat.MEASURED_STATE_MASK) >> 24, gammaCorrection3.correctSample(sampleAsByte2), this.gammaCorrection.correctSample(sampleAsByte3), this.gammaCorrection.correctSample(sampleAsByte4));
                    }
                    return pixelRGB2;
                case 3:
                    int sample = bitParser.getSample(i, 0);
                    int rgb = this.pngChunkPLTE.getRGB(sample);
                    TransparencyFilter transparencyFilter4 = this.transparencyFilter;
                    if (transparencyFilter4 != null) {
                        rgb = transparencyFilter4.filter(rgb, sample);
                    }
                    return rgb;
                case 4:
                    int sampleAsByte5 = bitParser.getSampleAsByte(i, 0);
                    int sampleAsByte6 = bitParser.getSampleAsByte(i, 1);
                    GammaCorrection gammaCorrection4 = this.gammaCorrection;
                    if (gammaCorrection4 != null) {
                        sampleAsByte5 = gammaCorrection4.correctSample(sampleAsByte5);
                    }
                    return getPixelARGB(sampleAsByte6, sampleAsByte5, sampleAsByte5, sampleAsByte5);
                default:
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append("PNG: unknown color type: ");
                    stringBuffer.append(this.colorType);
                    throw new ImageReadException(stringBuffer.toString());
            }
        } else {
            int sampleAsByte7 = bitParser.getSampleAsByte(i, 0);
            int sampleAsByte8 = bitParser.getSampleAsByte(i, 1);
            int sampleAsByte9 = bitParser.getSampleAsByte(i, 2);
            int sampleAsByte10 = bitParser.getSampleAsByte(i, 3);
            GammaCorrection gammaCorrection5 = this.gammaCorrection;
            if (gammaCorrection5 != null) {
                sampleAsByte7 = gammaCorrection5.correctSample(sampleAsByte7);
                sampleAsByte8 = this.gammaCorrection.correctSample(sampleAsByte8);
                sampleAsByte9 = this.gammaCorrection.correctSample(sampleAsByte9);
            }
            return getPixelARGB(sampleAsByte10, sampleAsByte7, sampleAsByte8, sampleAsByte9);
        }
    }

    /* access modifiers changed from: protected */
    public ScanlineFilter getScanlineFilter(int i, int i2) throws ImageReadException, IOException {
        switch (i) {
            case 0:
                return new ScanlineFilterNone();
            case 1:
                return new ScanlineFilterSub(i2);
            case 2:
                return new ScanlineFilterUp(i2);
            case 3:
                return new ScanlineFilterAverage(i2);
            case 4:
                return new ScanlineFilterPaeth(i2);
            default:
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("PNG: unknown filter_type: ");
                stringBuffer.append(i);
                throw new ImageReadException(stringBuffer.toString());
        }
    }

    /* access modifiers changed from: protected */
    public byte[] unfilterScanline(int i, byte[] bArr, byte[] bArr2, int i2) throws ImageReadException, IOException {
        ScanlineFilter scanlineFilter = getScanlineFilter(i, i2);
        byte[] bArr3 = new byte[bArr.length];
        scanlineFilter.unfilter(bArr, bArr3, bArr2);
        return bArr3;
    }

    /* access modifiers changed from: protected */
    public byte[] getNextScanline(InputStream inputStream, int i, byte[] bArr, int i2) throws ImageReadException, IOException {
        int read = inputStream.read();
        if (read >= 0) {
            return unfilterScanline(read, readByteArray("scanline", i, inputStream, "PNG: missing image data"), bArr, i2);
        }
        throw new ImageReadException("PNG: missing filter type");
    }
}

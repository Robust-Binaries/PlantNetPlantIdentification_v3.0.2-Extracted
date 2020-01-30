package org.apache.sanselan.formats.pnm;

import android.support.p000v4.view.ViewCompat;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import kotlin.UByte;
import org.apache.sanselan.ImageFormat;
import org.apache.sanselan.ImageInfo;
import org.apache.sanselan.ImageParser;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.SanselanConstants;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.common.byteSources.ByteSource;
import org.apache.sanselan.util.Debug;

public class PNMImageParser extends ImageParser implements PNMConstants {
    private static final String[] ACCEPTED_EXTENSIONS = {".pbm", ".pgm", ".ppm", DEFAULT_EXTENSION};
    private static final String DEFAULT_EXTENSION = ".pnm";
    public static final String PARAM_KEY_PNM_RAWBITS = "PNM_RAWBITS";
    public static final String PARAM_VALUE_PNM_RAWBITS_NO = "NO";
    public static final String PARAM_VALUE_PNM_RAWBITS_YES = "YES";

    public boolean embedICCProfile(File file, File file2, byte[] bArr) {
        return false;
    }

    public byte[] embedICCProfile(byte[] bArr, byte[] bArr2) {
        return null;
    }

    public String getDefaultExtension() {
        return DEFAULT_EXTENSION;
    }

    public byte[] getICCProfileBytes(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        return null;
    }

    public IImageMetadata getMetadata(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        return null;
    }

    public String getName() {
        return "Pbm-Custom";
    }

    public String getXmpXml(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        return null;
    }

    public PNMImageParser() {
        super.setByteOrder(73);
    }

    /* access modifiers changed from: protected */
    public String[] getAcceptedExtensions() {
        return ACCEPTED_EXTENSIONS;
    }

    /* access modifiers changed from: protected */
    public ImageFormat[] getAcceptedTypes() {
        return new ImageFormat[]{ImageFormat.IMAGE_FORMAT_PBM, ImageFormat.IMAGE_FORMAT_PGM, ImageFormat.IMAGE_FORMAT_PPM, ImageFormat.IMAGE_FORMAT_PNM};
    }

    private FileInfo readHeader(InputStream inputStream) throws ImageReadException, IOException {
        byte readByte = readByte("Identifier1", inputStream, "Not a Valid PNM File");
        byte readByte2 = readByte("Identifier2", inputStream, "Not a Valid PNM File");
        WhiteSpaceReader whiteSpaceReader = new WhiteSpaceReader(inputStream);
        int parseInt = Integer.parseInt(whiteSpaceReader.readtoWhiteSpace());
        int parseInt2 = Integer.parseInt(whiteSpaceReader.readtoWhiteSpace());
        if (readByte != 80) {
            throw new ImageReadException("PNM file has invalid header.");
        } else if (readByte2 == 49) {
            return new PBMFileInfo(parseInt, parseInt2, false);
        } else {
            if (readByte2 == 52) {
                return new PBMFileInfo(parseInt, parseInt2, true);
            }
            if (readByte2 == 50) {
                return new PGMFileInfo(parseInt, parseInt2, false, Integer.parseInt(whiteSpaceReader.readtoWhiteSpace()));
            }
            if (readByte2 == 53) {
                return new PGMFileInfo(parseInt, parseInt2, true, Integer.parseInt(whiteSpaceReader.readtoWhiteSpace()));
            }
            if (readByte2 == 51) {
                return new PPMFileInfo(parseInt, parseInt2, false, Integer.parseInt(whiteSpaceReader.readtoWhiteSpace()));
            }
            if (readByte2 == 54) {
                return new PPMFileInfo(parseInt, parseInt2, true, Integer.parseInt(whiteSpaceReader.readtoWhiteSpace()));
            }
            throw new ImageReadException("PNM file has invalid header.");
        }
    }

    private FileInfo readHeader(ByteSource byteSource) throws ImageReadException, IOException {
        InputStream inputStream;
        try {
            inputStream = byteSource.getInputStream();
            try {
                FileInfo readHeader = readHeader(inputStream);
                try {
                    inputStream.close();
                } catch (Exception e) {
                    Debug.debug((Throwable) e);
                }
                return readHeader;
            } catch (Throwable th) {
                th = th;
                try {
                    inputStream.close();
                } catch (Exception e2) {
                    Debug.debug((Throwable) e2);
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            inputStream = null;
            inputStream.close();
            throw th;
        }
    }

    public Dimension getImageSize(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        FileInfo readHeader = readHeader(byteSource);
        if (readHeader != null) {
            return new Dimension(readHeader.width, readHeader.height);
        }
        throw new ImageReadException("PNM: Couldn't read Header");
    }

    public ImageInfo getImageInfo(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        FileInfo readHeader = readHeader(byteSource);
        if (readHeader != null) {
            ArrayList arrayList = r1;
            ArrayList arrayList2 = new ArrayList();
            int bitDepth = readHeader.getBitDepth() * readHeader.getNumComponents();
            ImageFormat imageType = readHeader.getImageType();
            String imageTypeDescription = readHeader.getImageTypeDescription();
            String mIMEType = readHeader.getMIMEType();
            double d = (double) readHeader.width;
            double d2 = (double) 72;
            Double.isNaN(d);
            Double.isNaN(d2);
            float f = (float) (d / d2);
            double d3 = (double) readHeader.height;
            Double.isNaN(d3);
            Double.isNaN(d2);
            float f2 = (float) (d3 / d2);
            ImageInfo imageInfo = new ImageInfo(readHeader.getImageTypeDescription(), bitDepth, arrayList, imageType, imageTypeDescription, readHeader.height, mIMEType, 1, 72, f2, 72, f, readHeader.width, false, false, false, readHeader.getColorType(), ImageInfo.COMPRESSION_ALGORITHM_NONE);
            return imageInfo;
        }
        throw new ImageReadException("PNM: Couldn't read Header");
    }

    public boolean dumpImageFile(PrintWriter printWriter, ByteSource byteSource) throws ImageReadException, IOException {
        printWriter.println("pnm.dumpImageFile");
        ImageInfo imageInfo = getImageInfo(byteSource);
        if (imageInfo == null) {
            return false;
        }
        imageInfo.toString(printWriter, "");
        printWriter.println("");
        return true;
    }

    private int[] getColorTable(byte[] bArr) throws ImageReadException, IOException {
        if (bArr.length % 3 == 0) {
            int length = bArr.length / 3;
            int[] iArr = new int[length];
            for (int i = 0; i < length; i++) {
                int i2 = i * 3;
                byte b = bArr[i2 + 0] & UByte.MAX_VALUE;
                byte b2 = bArr[i2 + 1] & UByte.MAX_VALUE;
                iArr[i] = ((bArr[i2 + 2] & UByte.MAX_VALUE) << 0) | (b << 16) | ViewCompat.MEASURED_STATE_MASK | (b2 << 8);
            }
            return iArr;
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Bad Color Table Length: ");
        stringBuffer.append(bArr.length);
        throw new ImageReadException(stringBuffer.toString());
    }

    public BufferedImage getBufferedImage(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        InputStream inputStream;
        try {
            inputStream = byteSource.getInputStream();
            try {
                FileInfo readHeader = readHeader(inputStream);
                BufferedImage colorBufferedImage = getBufferedImageFactory(map).getColorBufferedImage(readHeader.width, readHeader.height, false);
                readHeader.readImage(colorBufferedImage, inputStream);
                try {
                    inputStream.close();
                } catch (Exception e) {
                    Debug.debug((Throwable) e);
                }
                return colorBufferedImage;
            } catch (Throwable th) {
                th = th;
                try {
                    inputStream.close();
                } catch (Exception e2) {
                    Debug.debug((Throwable) e2);
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            inputStream = null;
            inputStream.close();
            throw th;
        }
    }

    public void writeImage(BufferedImage bufferedImage, OutputStream outputStream, Map map) throws ImageWriteException, IOException {
        PNMWriter pNMWriter = null;
        boolean z = true;
        if (map != null) {
            Object obj = map.get(PARAM_KEY_PNM_RAWBITS);
            if (obj != null && obj.equals(PARAM_VALUE_PNM_RAWBITS_NO)) {
                z = false;
            }
            Object obj2 = map.get(SanselanConstants.PARAM_KEY_FORMAT);
            if (obj2 != null) {
                if (obj2.equals(ImageFormat.IMAGE_FORMAT_PBM)) {
                    pNMWriter = new PBMWriter(z);
                } else if (obj2.equals(ImageFormat.IMAGE_FORMAT_PGM)) {
                    pNMWriter = new PGMWriter(z);
                } else if (obj2.equals(ImageFormat.IMAGE_FORMAT_PPM)) {
                    pNMWriter = new PPMWriter(z);
                }
            }
        }
        if (pNMWriter == null) {
            pNMWriter = new PPMWriter(z);
        }
        HashMap hashMap = new HashMap(map);
        if (hashMap.containsKey(SanselanConstants.PARAM_KEY_FORMAT)) {
            hashMap.remove(SanselanConstants.PARAM_KEY_FORMAT);
        }
        if (hashMap.size() <= 0) {
            pNMWriter.writeImage(bufferedImage, outputStream, hashMap);
            return;
        }
        Object next = hashMap.keySet().iterator().next();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Unknown parameter: ");
        stringBuffer.append(next);
        throw new ImageWriteException(stringBuffer.toString());
    }
}

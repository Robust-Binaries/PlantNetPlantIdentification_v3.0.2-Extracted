package org.apache.sanselan;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Map;
import org.apache.sanselan.common.BinaryFileParser;
import org.apache.sanselan.common.IBufferedImageFactory;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.common.SimpleBufferedImageFactory;
import org.apache.sanselan.common.byteSources.ByteSource;
import org.apache.sanselan.common.byteSources.ByteSourceArray;
import org.apache.sanselan.common.byteSources.ByteSourceFile;
import org.apache.sanselan.formats.bmp.BmpImageParser;
import org.apache.sanselan.formats.gif.GifImageParser;
import org.apache.sanselan.formats.ico.IcoImageParser;
import org.apache.sanselan.formats.jpeg.JpegImageParser;
import org.apache.sanselan.formats.png.PngImageParser;
import org.apache.sanselan.formats.pnm.PNMImageParser;
import org.apache.sanselan.formats.psd.PsdImageParser;
import org.apache.sanselan.formats.tiff.TiffImageParser;
import org.apache.sanselan.util.Debug;

public abstract class ImageParser extends BinaryFileParser implements SanselanConstants {
    public boolean dumpImageFile(PrintWriter printWriter, ByteSource byteSource) throws ImageReadException, IOException {
        return false;
    }

    public abstract boolean embedICCProfile(File file, File file2, byte[] bArr);

    /* access modifiers changed from: protected */
    public abstract String[] getAcceptedExtensions();

    /* access modifiers changed from: protected */
    public abstract ImageFormat[] getAcceptedTypes();

    public abstract BufferedImage getBufferedImage(ByteSource byteSource, Map map) throws ImageReadException, IOException;

    public abstract String getDefaultExtension();

    public FormatCompliance getFormatCompliance(ByteSource byteSource) throws ImageReadException, IOException {
        return null;
    }

    public abstract byte[] getICCProfileBytes(ByteSource byteSource, Map map) throws ImageReadException, IOException;

    public abstract ImageInfo getImageInfo(ByteSource byteSource, Map map) throws ImageReadException, IOException;

    public abstract Dimension getImageSize(ByteSource byteSource, Map map) throws ImageReadException, IOException;

    public abstract IImageMetadata getMetadata(ByteSource byteSource, Map map) throws ImageReadException, IOException;

    public abstract String getName();

    public abstract String getXmpXml(ByteSource byteSource, Map map) throws ImageReadException, IOException;

    public static final ImageParser[] getAllImageParsers() {
        return new ImageParser[]{new JpegImageParser(), new TiffImageParser(), new PngImageParser(), new BmpImageParser(), new GifImageParser(), new PsdImageParser(), new PNMImageParser(), new IcoImageParser()};
    }

    public final IImageMetadata getMetadata(ByteSource byteSource) throws ImageReadException, IOException {
        return getMetadata(byteSource, (Map) null);
    }

    public final IImageMetadata getMetadata(byte[] bArr) throws ImageReadException, IOException {
        return getMetadata(bArr);
    }

    public final IImageMetadata getMetadata(byte[] bArr, Map map) throws ImageReadException, IOException {
        return getMetadata((ByteSource) new ByteSourceArray(bArr), map);
    }

    public final IImageMetadata getMetadata(File file) throws ImageReadException, IOException {
        return getMetadata(file, (Map) null);
    }

    public final IImageMetadata getMetadata(File file, Map map) throws ImageReadException, IOException {
        if (this.debug) {
            PrintStream printStream = System.out;
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(getName());
            stringBuffer.append(".getMetadata");
            stringBuffer.append(": ");
            stringBuffer.append(file.getName());
            printStream.println(stringBuffer.toString());
        }
        if (!canAcceptExtension(file)) {
            return null;
        }
        return getMetadata((ByteSource) new ByteSourceFile(file), map);
    }

    public final ImageInfo getImageInfo(ByteSource byteSource) throws ImageReadException, IOException {
        return getImageInfo(byteSource, (Map) null);
    }

    public final ImageInfo getImageInfo(byte[] bArr, Map map) throws ImageReadException, IOException {
        return getImageInfo((ByteSource) new ByteSourceArray(bArr), map);
    }

    public final ImageInfo getImageInfo(File file, Map map) throws ImageReadException, IOException {
        if (!canAcceptExtension(file)) {
            return null;
        }
        return getImageInfo((ByteSource) new ByteSourceFile(file), map);
    }

    public final FormatCompliance getFormatCompliance(byte[] bArr) throws ImageReadException, IOException {
        return getFormatCompliance((ByteSource) new ByteSourceArray(bArr));
    }

    public final FormatCompliance getFormatCompliance(File file) throws ImageReadException, IOException {
        if (!canAcceptExtension(file)) {
            return null;
        }
        return getFormatCompliance((ByteSource) new ByteSourceFile(file));
    }

    public ArrayList getAllBufferedImages(ByteSource byteSource) throws ImageReadException, IOException {
        BufferedImage bufferedImage = getBufferedImage(byteSource, (Map) null);
        ArrayList arrayList = new ArrayList();
        arrayList.add(bufferedImage);
        return arrayList;
    }

    public final ArrayList getAllBufferedImages(byte[] bArr) throws ImageReadException, IOException {
        return getAllBufferedImages((ByteSource) new ByteSourceArray(bArr));
    }

    public final ArrayList getAllBufferedImages(File file) throws ImageReadException, IOException {
        if (!canAcceptExtension(file)) {
            return null;
        }
        return getAllBufferedImages((ByteSource) new ByteSourceFile(file));
    }

    public final BufferedImage getBufferedImage(byte[] bArr, Map map) throws ImageReadException, IOException {
        return getBufferedImage((ByteSource) new ByteSourceArray(bArr), map);
    }

    public final BufferedImage getBufferedImage(File file, Map map) throws ImageReadException, IOException {
        if (!canAcceptExtension(file)) {
            return null;
        }
        return getBufferedImage((ByteSource) new ByteSourceFile(file), map);
    }

    public void writeImage(BufferedImage bufferedImage, OutputStream outputStream, Map map) throws ImageWriteException, IOException {
        try {
            outputStream.close();
        } catch (Exception e) {
            Debug.debug((Throwable) e);
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("This image format (");
        stringBuffer.append(getName());
        stringBuffer.append(") cannot be written.");
        throw new ImageWriteException(stringBuffer.toString());
    }

    public final Dimension getImageSize(byte[] bArr) throws ImageReadException, IOException {
        return getImageSize(bArr, (Map) null);
    }

    public final Dimension getImageSize(byte[] bArr, Map map) throws ImageReadException, IOException {
        return getImageSize((ByteSource) new ByteSourceArray(bArr), map);
    }

    public final Dimension getImageSize(File file) throws ImageReadException, IOException {
        return getImageSize(file, (Map) null);
    }

    public final Dimension getImageSize(File file, Map map) throws ImageReadException, IOException {
        if (!canAcceptExtension(file)) {
            return null;
        }
        return getImageSize((ByteSource) new ByteSourceFile(file), map);
    }

    public final byte[] getICCProfileBytes(byte[] bArr) throws ImageReadException, IOException {
        return getICCProfileBytes(bArr, (Map) null);
    }

    public final byte[] getICCProfileBytes(byte[] bArr, Map map) throws ImageReadException, IOException {
        return getICCProfileBytes((ByteSource) new ByteSourceArray(bArr), map);
    }

    public final byte[] getICCProfileBytes(File file) throws ImageReadException, IOException {
        return getICCProfileBytes(file, (Map) null);
    }

    public final byte[] getICCProfileBytes(File file, Map map) throws ImageReadException, IOException {
        if (!canAcceptExtension(file)) {
            return null;
        }
        if (this.debug) {
            PrintStream printStream = System.out;
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(getName());
            stringBuffer.append(": ");
            stringBuffer.append(file.getName());
            printStream.println(stringBuffer.toString());
        }
        return getICCProfileBytes((ByteSource) new ByteSourceFile(file), map);
    }

    public final String dumpImageFile(byte[] bArr) throws ImageReadException, IOException {
        return dumpImageFile((ByteSource) new ByteSourceArray(bArr));
    }

    public final String dumpImageFile(File file) throws ImageReadException, IOException {
        if (!canAcceptExtension(file)) {
            return null;
        }
        if (this.debug) {
            PrintStream printStream = System.out;
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(getName());
            stringBuffer.append(": ");
            stringBuffer.append(file.getName());
            printStream.println(stringBuffer.toString());
        }
        return dumpImageFile((ByteSource) new ByteSourceFile(file));
    }

    public final String dumpImageFile(ByteSource byteSource) throws ImageReadException, IOException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        dumpImageFile(printWriter, byteSource);
        printWriter.flush();
        return stringWriter.toString();
    }

    public boolean canAcceptType(ImageFormat imageFormat) {
        ImageFormat[] acceptedTypes = getAcceptedTypes();
        for (ImageFormat equals : acceptedTypes) {
            if (equals.equals(imageFormat)) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public final boolean canAcceptExtension(File file) {
        return canAcceptExtension(file.getName());
    }

    /* access modifiers changed from: protected */
    public final boolean canAcceptExtension(String str) {
        String[] acceptedExtensions = getAcceptedExtensions();
        if (acceptedExtensions == null) {
            return true;
        }
        int lastIndexOf = str.lastIndexOf(46);
        if (lastIndexOf >= 0) {
            String lowerCase = str.substring(lastIndexOf).toLowerCase();
            for (String lowerCase2 : acceptedExtensions) {
                if (lowerCase2.toLowerCase().equals(lowerCase)) {
                    return true;
                }
            }
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public IBufferedImageFactory getBufferedImageFactory(Map map) {
        if (map == null) {
            return new SimpleBufferedImageFactory();
        }
        IBufferedImageFactory iBufferedImageFactory = (IBufferedImageFactory) map.get(SanselanConstants.BUFFERED_IMAGE_FACTORY);
        if (iBufferedImageFactory != null) {
            return iBufferedImageFactory;
        }
        return new SimpleBufferedImageFactory();
    }

    public static final boolean isStrict(Map map) {
        if (map == null || !map.containsKey(SanselanConstants.PARAM_KEY_STRICT)) {
            return false;
        }
        return ((Boolean) map.get(SanselanConstants.PARAM_KEY_STRICT)).booleanValue();
    }
}

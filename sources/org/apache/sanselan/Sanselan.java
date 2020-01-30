package org.apache.sanselan;

import java.awt.Dimension;
import java.awt.color.ICC_Profile;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.common.byteSources.ByteSource;
import org.apache.sanselan.common.byteSources.ByteSourceArray;
import org.apache.sanselan.common.byteSources.ByteSourceFile;
import org.apache.sanselan.common.byteSources.ByteSourceInputStream;
import org.apache.sanselan.icc.IccProfileParser;

public abstract class Sanselan implements SanselanConstants {
    public static boolean hasImageFileExtension(File file) {
        if (!file.isFile()) {
            return false;
        }
        return hasImageFileExtension(file.getName());
    }

    public static boolean hasImageFileExtension(String str) {
        String lowerCase = str.toLowerCase();
        ImageParser[] allImageParsers = ImageParser.getAllImageParsers();
        for (ImageParser acceptedExtensions : allImageParsers) {
            String[] acceptedExtensions2 = acceptedExtensions.getAcceptedExtensions();
            for (String lowerCase2 : acceptedExtensions2) {
                if (lowerCase.endsWith(lowerCase2.toLowerCase())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static ImageFormat guessFormat(byte[] bArr) throws ImageReadException, IOException {
        return guessFormat((ByteSource) new ByteSourceArray(bArr));
    }

    public static ImageFormat guessFormat(File file) throws ImageReadException, IOException {
        return guessFormat((ByteSource) new ByteSourceFile(file));
    }

    /* JADX WARNING: Removed duplicated region for block: B:181:0x0157 A[SYNTHETIC, Splitter:B:181:0x0157] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:164:0x0134=Splitter:B:164:0x0134, B:174:0x0149=Splitter:B:174:0x0149} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static org.apache.sanselan.ImageFormat guessFormat(org.apache.sanselan.common.byteSources.ByteSource r7) throws org.apache.sanselan.ImageReadException, java.io.IOException {
        /*
            java.io.InputStream r7 = r7.getInputStream()     // Catch:{ all -> 0x0153 }
            int r0 = r7.read()     // Catch:{ all -> 0x0151 }
            int r1 = r7.read()     // Catch:{ all -> 0x0151 }
            if (r0 < 0) goto L_0x0149
            if (r1 < 0) goto L_0x0149
            r2 = 255(0xff, float:3.57E-43)
            r0 = r0 & r2
            r1 = r1 & r2
            r3 = 71
            r4 = 73
            if (r0 != r3) goto L_0x0029
            if (r1 != r4) goto L_0x0029
            org.apache.sanselan.ImageFormat r0 = org.apache.sanselan.ImageFormat.IMAGE_FORMAT_GIF     // Catch:{ all -> 0x0151 }
            if (r7 == 0) goto L_0x0028
            r7.close()     // Catch:{ IOException -> 0x0024 }
            goto L_0x0028
        L_0x0024:
            r7 = move-exception
            org.apache.sanselan.util.Debug.debug(r7)
        L_0x0028:
            return r0
        L_0x0029:
            r3 = 137(0x89, float:1.92E-43)
            r5 = 80
            if (r0 != r3) goto L_0x003e
            if (r1 != r5) goto L_0x003e
            org.apache.sanselan.ImageFormat r0 = org.apache.sanselan.ImageFormat.IMAGE_FORMAT_PNG     // Catch:{ all -> 0x0151 }
            if (r7 == 0) goto L_0x003d
            r7.close()     // Catch:{ IOException -> 0x0039 }
            goto L_0x003d
        L_0x0039:
            r7 = move-exception
            org.apache.sanselan.util.Debug.debug(r7)
        L_0x003d:
            return r0
        L_0x003e:
            if (r0 != r2) goto L_0x0051
            r3 = 216(0xd8, float:3.03E-43)
            if (r1 != r3) goto L_0x0051
            org.apache.sanselan.ImageFormat r0 = org.apache.sanselan.ImageFormat.IMAGE_FORMAT_JPEG     // Catch:{ all -> 0x0151 }
            if (r7 == 0) goto L_0x0050
            r7.close()     // Catch:{ IOException -> 0x004c }
            goto L_0x0050
        L_0x004c:
            r7 = move-exception
            org.apache.sanselan.util.Debug.debug(r7)
        L_0x0050:
            return r0
        L_0x0051:
            r3 = 77
            r6 = 66
            if (r0 != r6) goto L_0x0066
            if (r1 != r3) goto L_0x0066
            org.apache.sanselan.ImageFormat r0 = org.apache.sanselan.ImageFormat.IMAGE_FORMAT_BMP     // Catch:{ all -> 0x0151 }
            if (r7 == 0) goto L_0x0065
            r7.close()     // Catch:{ IOException -> 0x0061 }
            goto L_0x0065
        L_0x0061:
            r7 = move-exception
            org.apache.sanselan.util.Debug.debug(r7)
        L_0x0065:
            return r0
        L_0x0066:
            if (r0 != r3) goto L_0x0077
            if (r1 != r3) goto L_0x0077
            org.apache.sanselan.ImageFormat r0 = org.apache.sanselan.ImageFormat.IMAGE_FORMAT_TIFF     // Catch:{ all -> 0x0151 }
            if (r7 == 0) goto L_0x0076
            r7.close()     // Catch:{ IOException -> 0x0072 }
            goto L_0x0076
        L_0x0072:
            r7 = move-exception
            org.apache.sanselan.util.Debug.debug(r7)
        L_0x0076:
            return r0
        L_0x0077:
            if (r0 != r4) goto L_0x0088
            if (r1 != r4) goto L_0x0088
            org.apache.sanselan.ImageFormat r0 = org.apache.sanselan.ImageFormat.IMAGE_FORMAT_TIFF     // Catch:{ all -> 0x0151 }
            if (r7 == 0) goto L_0x0087
            r7.close()     // Catch:{ IOException -> 0x0083 }
            goto L_0x0087
        L_0x0083:
            r7 = move-exception
            org.apache.sanselan.util.Debug.debug(r7)
        L_0x0087:
            return r0
        L_0x0088:
            r3 = 56
            if (r0 != r3) goto L_0x009b
            if (r1 != r6) goto L_0x009b
            org.apache.sanselan.ImageFormat r0 = org.apache.sanselan.ImageFormat.IMAGE_FORMAT_PSD     // Catch:{ all -> 0x0151 }
            if (r7 == 0) goto L_0x009a
            r7.close()     // Catch:{ IOException -> 0x0096 }
            goto L_0x009a
        L_0x0096:
            r7 = move-exception
            org.apache.sanselan.util.Debug.debug(r7)
        L_0x009a:
            return r0
        L_0x009b:
            if (r0 != r5) goto L_0x00ae
            r3 = 49
            if (r1 != r3) goto L_0x00ae
            org.apache.sanselan.ImageFormat r0 = org.apache.sanselan.ImageFormat.IMAGE_FORMAT_PBM     // Catch:{ all -> 0x0151 }
            if (r7 == 0) goto L_0x00ad
            r7.close()     // Catch:{ IOException -> 0x00a9 }
            goto L_0x00ad
        L_0x00a9:
            r7 = move-exception
            org.apache.sanselan.util.Debug.debug(r7)
        L_0x00ad:
            return r0
        L_0x00ae:
            if (r0 != r5) goto L_0x00c1
            r3 = 52
            if (r1 != r3) goto L_0x00c1
            org.apache.sanselan.ImageFormat r0 = org.apache.sanselan.ImageFormat.IMAGE_FORMAT_PBM     // Catch:{ all -> 0x0151 }
            if (r7 == 0) goto L_0x00c0
            r7.close()     // Catch:{ IOException -> 0x00bc }
            goto L_0x00c0
        L_0x00bc:
            r7 = move-exception
            org.apache.sanselan.util.Debug.debug(r7)
        L_0x00c0:
            return r0
        L_0x00c1:
            r3 = 50
            if (r0 != r5) goto L_0x00d4
            if (r1 != r3) goto L_0x00d4
            org.apache.sanselan.ImageFormat r0 = org.apache.sanselan.ImageFormat.IMAGE_FORMAT_PGM     // Catch:{ all -> 0x0151 }
            if (r7 == 0) goto L_0x00d3
            r7.close()     // Catch:{ IOException -> 0x00cf }
            goto L_0x00d3
        L_0x00cf:
            r7 = move-exception
            org.apache.sanselan.util.Debug.debug(r7)
        L_0x00d3:
            return r0
        L_0x00d4:
            if (r0 != r5) goto L_0x00e7
            r4 = 53
            if (r1 != r4) goto L_0x00e7
            org.apache.sanselan.ImageFormat r0 = org.apache.sanselan.ImageFormat.IMAGE_FORMAT_PGM     // Catch:{ all -> 0x0151 }
            if (r7 == 0) goto L_0x00e6
            r7.close()     // Catch:{ IOException -> 0x00e2 }
            goto L_0x00e6
        L_0x00e2:
            r7 = move-exception
            org.apache.sanselan.util.Debug.debug(r7)
        L_0x00e6:
            return r0
        L_0x00e7:
            if (r0 != r5) goto L_0x00fa
            r4 = 51
            if (r1 != r4) goto L_0x00fa
            org.apache.sanselan.ImageFormat r0 = org.apache.sanselan.ImageFormat.IMAGE_FORMAT_PPM     // Catch:{ all -> 0x0151 }
            if (r7 == 0) goto L_0x00f9
            r7.close()     // Catch:{ IOException -> 0x00f5 }
            goto L_0x00f9
        L_0x00f5:
            r7 = move-exception
            org.apache.sanselan.util.Debug.debug(r7)
        L_0x00f9:
            return r0
        L_0x00fa:
            if (r0 != r5) goto L_0x010d
            r4 = 54
            if (r1 != r4) goto L_0x010d
            org.apache.sanselan.ImageFormat r0 = org.apache.sanselan.ImageFormat.IMAGE_FORMAT_PPM     // Catch:{ all -> 0x0151 }
            if (r7 == 0) goto L_0x010c
            r7.close()     // Catch:{ IOException -> 0x0108 }
            goto L_0x010c
        L_0x0108:
            r7 = move-exception
            org.apache.sanselan.util.Debug.debug(r7)
        L_0x010c:
            return r0
        L_0x010d:
            r4 = 151(0x97, float:2.12E-43)
            if (r0 != r4) goto L_0x013c
            r0 = 74
            if (r1 != r0) goto L_0x013c
            int r0 = r7.read()     // Catch:{ all -> 0x0151 }
            int r1 = r7.read()     // Catch:{ all -> 0x0151 }
            if (r0 < 0) goto L_0x0134
            if (r1 < 0) goto L_0x0134
            r0 = r0 & r2
            r1 = r1 & r2
            if (r0 != r6) goto L_0x013c
            if (r1 != r3) goto L_0x013c
            org.apache.sanselan.ImageFormat r0 = org.apache.sanselan.ImageFormat.IMAGE_FORMAT_JBIG2     // Catch:{ all -> 0x0151 }
            if (r7 == 0) goto L_0x0133
            r7.close()     // Catch:{ IOException -> 0x012f }
            goto L_0x0133
        L_0x012f:
            r7 = move-exception
            org.apache.sanselan.util.Debug.debug(r7)
        L_0x0133:
            return r0
        L_0x0134:
            org.apache.sanselan.ImageReadException r0 = new org.apache.sanselan.ImageReadException     // Catch:{ all -> 0x0151 }
            java.lang.String r1 = "Couldn't read magic numbers to guess format."
            r0.<init>(r1)     // Catch:{ all -> 0x0151 }
            throw r0     // Catch:{ all -> 0x0151 }
        L_0x013c:
            org.apache.sanselan.ImageFormat r0 = org.apache.sanselan.ImageFormat.IMAGE_FORMAT_UNKNOWN     // Catch:{ all -> 0x0151 }
            if (r7 == 0) goto L_0x0148
            r7.close()     // Catch:{ IOException -> 0x0144 }
            goto L_0x0148
        L_0x0144:
            r7 = move-exception
            org.apache.sanselan.util.Debug.debug(r7)
        L_0x0148:
            return r0
        L_0x0149:
            org.apache.sanselan.ImageReadException r0 = new org.apache.sanselan.ImageReadException     // Catch:{ all -> 0x0151 }
            java.lang.String r1 = "Couldn't read magic numbers to guess format."
            r0.<init>(r1)     // Catch:{ all -> 0x0151 }
            throw r0     // Catch:{ all -> 0x0151 }
        L_0x0151:
            r0 = move-exception
            goto L_0x0155
        L_0x0153:
            r0 = move-exception
            r7 = 0
        L_0x0155:
            if (r7 == 0) goto L_0x015f
            r7.close()     // Catch:{ IOException -> 0x015b }
            goto L_0x015f
        L_0x015b:
            r7 = move-exception
            org.apache.sanselan.util.Debug.debug(r7)
        L_0x015f:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.sanselan.Sanselan.guessFormat(org.apache.sanselan.common.byteSources.ByteSource):org.apache.sanselan.ImageFormat");
    }

    public static ICC_Profile getICCProfile(byte[] bArr) throws ImageReadException, IOException {
        return getICCProfile(bArr, (Map) null);
    }

    public static ICC_Profile getICCProfile(byte[] bArr, Map map) throws ImageReadException, IOException {
        return getICCProfile((ByteSource) new ByteSourceArray(bArr), map);
    }

    public static ICC_Profile getICCProfile(InputStream inputStream, String str) throws ImageReadException, IOException {
        return getICCProfile(inputStream, str, null);
    }

    public static ICC_Profile getICCProfile(InputStream inputStream, String str, Map map) throws ImageReadException, IOException {
        return getICCProfile((ByteSource) new ByteSourceInputStream(inputStream, str), map);
    }

    public static ICC_Profile getICCProfile(File file) throws ImageReadException, IOException {
        return getICCProfile(file, (Map) null);
    }

    public static ICC_Profile getICCProfile(File file, Map map) throws ImageReadException, IOException {
        return getICCProfile((ByteSource) new ByteSourceFile(file), map);
    }

    protected static ICC_Profile getICCProfile(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        byte[] iCCProfileBytes = getICCProfileBytes(byteSource, map);
        if (iCCProfileBytes != null && !new IccProfileParser().getICCProfileInfo(iCCProfileBytes).issRGB()) {
            return ICC_Profile.getInstance(iCCProfileBytes);
        }
        return null;
    }

    public static byte[] getICCProfileBytes(byte[] bArr) throws ImageReadException, IOException {
        return getICCProfileBytes(bArr, (Map) null);
    }

    public static byte[] getICCProfileBytes(byte[] bArr, Map map) throws ImageReadException, IOException {
        return getICCProfileBytes((ByteSource) new ByteSourceArray(bArr), map);
    }

    public static byte[] getICCProfileBytes(File file) throws ImageReadException, IOException {
        return getICCProfileBytes(file, (Map) null);
    }

    public static byte[] getICCProfileBytes(File file, Map map) throws ImageReadException, IOException {
        return getICCProfileBytes((ByteSource) new ByteSourceFile(file), map);
    }

    private static byte[] getICCProfileBytes(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        return getImageParser(byteSource).getICCProfileBytes(byteSource, map);
    }

    public static ImageInfo getImageInfo(String str, byte[] bArr, Map map) throws ImageReadException, IOException {
        return getImageInfo((ByteSource) new ByteSourceArray(str, bArr), map);
    }

    public static ImageInfo getImageInfo(String str, byte[] bArr) throws ImageReadException, IOException {
        return getImageInfo((ByteSource) new ByteSourceArray(str, bArr), (Map) null);
    }

    public static ImageInfo getImageInfo(InputStream inputStream, String str) throws ImageReadException, IOException {
        return getImageInfo((ByteSource) new ByteSourceInputStream(inputStream, str), (Map) null);
    }

    public static ImageInfo getImageInfo(InputStream inputStream, String str, Map map) throws ImageReadException, IOException {
        return getImageInfo((ByteSource) new ByteSourceInputStream(inputStream, str), map);
    }

    public static ImageInfo getImageInfo(byte[] bArr) throws ImageReadException, IOException {
        return getImageInfo((ByteSource) new ByteSourceArray(bArr), (Map) null);
    }

    public static ImageInfo getImageInfo(byte[] bArr, Map map) throws ImageReadException, IOException {
        return getImageInfo((ByteSource) new ByteSourceArray(bArr), map);
    }

    public static ImageInfo getImageInfo(File file, Map map) throws ImageReadException, IOException {
        return getImageInfo((ByteSource) new ByteSourceFile(file), map);
    }

    public static ImageInfo getImageInfo(File file) throws ImageReadException, IOException {
        return getImageInfo(file, (Map) null);
    }

    private static ImageInfo getImageInfo(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        return getImageParser(byteSource).getImageInfo(byteSource, map);
    }

    private static final ImageParser getImageParser(ByteSource byteSource) throws ImageReadException, IOException {
        ImageFormat guessFormat = guessFormat(byteSource);
        if (!guessFormat.equals(ImageFormat.IMAGE_FORMAT_UNKNOWN)) {
            ImageParser[] allImageParsers = ImageParser.getAllImageParsers();
            for (ImageParser imageParser : allImageParsers) {
                if (imageParser.canAcceptType(guessFormat)) {
                    return imageParser;
                }
            }
        }
        String filename = byteSource.getFilename();
        if (filename != null) {
            ImageParser[] allImageParsers2 = ImageParser.getAllImageParsers();
            for (ImageParser imageParser2 : allImageParsers2) {
                if (imageParser2.canAcceptExtension(filename)) {
                    return imageParser2;
                }
            }
        }
        throw new ImageReadException("Can't parse this format.");
    }

    public static Dimension getImageSize(InputStream inputStream, String str) throws ImageReadException, IOException {
        return getImageSize(inputStream, str, null);
    }

    public static Dimension getImageSize(InputStream inputStream, String str, Map map) throws ImageReadException, IOException {
        return getImageSize((ByteSource) new ByteSourceInputStream(inputStream, str), map);
    }

    public static Dimension getImageSize(byte[] bArr) throws ImageReadException, IOException {
        return getImageSize(bArr, (Map) null);
    }

    public static Dimension getImageSize(byte[] bArr, Map map) throws ImageReadException, IOException {
        return getImageSize((ByteSource) new ByteSourceArray(bArr), map);
    }

    public static Dimension getImageSize(File file) throws ImageReadException, IOException {
        return getImageSize(file, (Map) null);
    }

    public static Dimension getImageSize(File file, Map map) throws ImageReadException, IOException {
        return getImageSize((ByteSource) new ByteSourceFile(file), map);
    }

    public static Dimension getImageSize(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        return getImageParser(byteSource).getImageSize(byteSource, map);
    }

    public static String getXmpXml(InputStream inputStream, String str) throws ImageReadException, IOException {
        return getXmpXml(inputStream, str, null);
    }

    public static String getXmpXml(InputStream inputStream, String str, Map map) throws ImageReadException, IOException {
        return getXmpXml((ByteSource) new ByteSourceInputStream(inputStream, str), map);
    }

    public static String getXmpXml(byte[] bArr) throws ImageReadException, IOException {
        return getXmpXml(bArr, (Map) null);
    }

    public static String getXmpXml(byte[] bArr, Map map) throws ImageReadException, IOException {
        return getXmpXml((ByteSource) new ByteSourceArray(bArr), map);
    }

    public static String getXmpXml(File file) throws ImageReadException, IOException {
        return getXmpXml(file, (Map) null);
    }

    public static String getXmpXml(File file, Map map) throws ImageReadException, IOException {
        return getXmpXml((ByteSource) new ByteSourceFile(file), map);
    }

    public static String getXmpXml(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        return getImageParser(byteSource).getXmpXml(byteSource, map);
    }

    public static IImageMetadata getMetadata(byte[] bArr) throws ImageReadException, IOException {
        return getMetadata(bArr, (Map) null);
    }

    public static IImageMetadata getMetadata(byte[] bArr, Map map) throws ImageReadException, IOException {
        return getMetadata((ByteSource) new ByteSourceArray(bArr), map);
    }

    public static IImageMetadata getMetadata(InputStream inputStream, String str) throws ImageReadException, IOException {
        return getMetadata(inputStream, str, null);
    }

    public static IImageMetadata getMetadata(InputStream inputStream, String str, Map map) throws ImageReadException, IOException {
        return getMetadata((ByteSource) new ByteSourceInputStream(inputStream, str), map);
    }

    public static IImageMetadata getMetadata(File file) throws ImageReadException, IOException {
        return getMetadata(file, (Map) null);
    }

    public static IImageMetadata getMetadata(File file, Map map) throws ImageReadException, IOException {
        return getMetadata((ByteSource) new ByteSourceFile(file), map);
    }

    private static IImageMetadata getMetadata(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        return getImageParser(byteSource).getMetadata(byteSource, map);
    }

    public static String dumpImageFile(byte[] bArr) throws ImageReadException, IOException {
        return dumpImageFile((ByteSource) new ByteSourceArray(bArr));
    }

    public static String dumpImageFile(File file) throws ImageReadException, IOException {
        return dumpImageFile((ByteSource) new ByteSourceFile(file));
    }

    private static String dumpImageFile(ByteSource byteSource) throws ImageReadException, IOException {
        return getImageParser(byteSource).dumpImageFile(byteSource);
    }

    public static FormatCompliance getFormatCompliance(byte[] bArr) throws ImageReadException, IOException {
        return getFormatCompliance((ByteSource) new ByteSourceArray(bArr));
    }

    public static FormatCompliance getFormatCompliance(File file) throws ImageReadException, IOException {
        return getFormatCompliance((ByteSource) new ByteSourceFile(file));
    }

    private static FormatCompliance getFormatCompliance(ByteSource byteSource) throws ImageReadException, IOException {
        return getImageParser(byteSource).getFormatCompliance(byteSource);
    }

    public static ArrayList getAllBufferedImages(InputStream inputStream, String str) throws ImageReadException, IOException {
        return getAllBufferedImages((ByteSource) new ByteSourceInputStream(inputStream, str));
    }

    public static ArrayList getAllBufferedImages(byte[] bArr) throws ImageReadException, IOException {
        return getAllBufferedImages((ByteSource) new ByteSourceArray(bArr));
    }

    public static ArrayList getAllBufferedImages(File file) throws ImageReadException, IOException {
        return getAllBufferedImages((ByteSource) new ByteSourceFile(file));
    }

    private static ArrayList getAllBufferedImages(ByteSource byteSource) throws ImageReadException, IOException {
        return getImageParser(byteSource).getAllBufferedImages(byteSource);
    }

    public static BufferedImage getBufferedImage(InputStream inputStream) throws ImageReadException, IOException {
        return getBufferedImage(inputStream, (Map) null);
    }

    public static BufferedImage getBufferedImage(InputStream inputStream, Map map) throws ImageReadException, IOException {
        return getBufferedImage((ByteSource) new ByteSourceInputStream(inputStream, (map == null || !map.containsKey(SanselanConstants.PARAM_KEY_FILENAME)) ? null : (String) map.get(SanselanConstants.PARAM_KEY_FILENAME)), map);
    }

    public static BufferedImage getBufferedImage(byte[] bArr) throws ImageReadException, IOException {
        return getBufferedImage((ByteSource) new ByteSourceArray(bArr), (Map) null);
    }

    public static BufferedImage getBufferedImage(byte[] bArr, Map map) throws ImageReadException, IOException {
        return getBufferedImage((ByteSource) new ByteSourceArray(bArr), map);
    }

    public static BufferedImage getBufferedImage(File file) throws ImageReadException, IOException {
        return getBufferedImage((ByteSource) new ByteSourceFile(file), (Map) null);
    }

    public static BufferedImage getBufferedImage(File file, Map map) throws ImageReadException, IOException {
        return getBufferedImage((ByteSource) new ByteSourceFile(file), map);
    }

    private static BufferedImage getBufferedImage(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        ImageParser imageParser = getImageParser(byteSource);
        if (map == null) {
            map = new HashMap();
        }
        return imageParser.getBufferedImage(byteSource, map);
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x001d A[SYNTHETIC, Splitter:B:15:0x001d] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void writeImage(java.awt.image.BufferedImage r2, java.io.File r3, org.apache.sanselan.ImageFormat r4, java.util.Map r5) throws org.apache.sanselan.ImageWriteException, java.io.IOException {
        /*
            r0 = 0
            java.io.FileOutputStream r1 = new java.io.FileOutputStream     // Catch:{ all -> 0x001a }
            r1.<init>(r3)     // Catch:{ all -> 0x001a }
            java.io.BufferedOutputStream r0 = new java.io.BufferedOutputStream     // Catch:{ all -> 0x0017 }
            r0.<init>(r1)     // Catch:{ all -> 0x0017 }
            writeImage(r2, r0, r4, r5)     // Catch:{ all -> 0x001a }
            r0.close()     // Catch:{ Exception -> 0x0012 }
            goto L_0x0016
        L_0x0012:
            r2 = move-exception
            org.apache.sanselan.util.Debug.debug(r2)
        L_0x0016:
            return
        L_0x0017:
            r2 = move-exception
            r0 = r1
            goto L_0x001b
        L_0x001a:
            r2 = move-exception
        L_0x001b:
            if (r0 == 0) goto L_0x0025
            r0.close()     // Catch:{ Exception -> 0x0021 }
            goto L_0x0025
        L_0x0021:
            r3 = move-exception
            org.apache.sanselan.util.Debug.debug(r3)
        L_0x0025:
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.sanselan.Sanselan.writeImage(java.awt.image.BufferedImage, java.io.File, org.apache.sanselan.ImageFormat, java.util.Map):void");
    }

    public static byte[] writeImageToBytes(BufferedImage bufferedImage, ImageFormat imageFormat, Map map) throws ImageWriteException, IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        writeImage(bufferedImage, (OutputStream) byteArrayOutputStream, imageFormat, map);
        return byteArrayOutputStream.toByteArray();
    }

    public static void writeImage(BufferedImage bufferedImage, OutputStream outputStream, ImageFormat imageFormat, Map map) throws ImageWriteException, IOException {
        ImageParser[] allImageParsers = ImageParser.getAllImageParsers();
        if (map == null) {
            map = new HashMap();
        }
        map.put(SanselanConstants.PARAM_KEY_FORMAT, imageFormat);
        int i = 0;
        while (i < allImageParsers.length) {
            ImageParser imageParser = allImageParsers[i];
            if (!imageParser.canAcceptType(imageFormat)) {
                i++;
            } else {
                imageParser.writeImage(bufferedImage, outputStream, map);
                return;
            }
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Unknown Format: ");
        stringBuffer.append(imageFormat);
        throw new ImageWriteException(stringBuffer.toString());
    }
}

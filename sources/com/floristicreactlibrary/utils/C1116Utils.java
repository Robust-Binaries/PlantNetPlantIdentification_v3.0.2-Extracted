package com.floristicreactlibrary.utils;

import java.io.File;
import java.io.IOException;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.Sanselan;
import org.apache.sanselan.formats.jpeg.JpegImageMetadata;
import org.apache.sanselan.formats.tiff.TiffImageMetadata;
import org.apache.sanselan.formats.tiff.write.TiffOutputDirectory;
import org.apache.sanselan.formats.tiff.write.TiffOutputSet;

/* renamed from: com.floristicreactlibrary.utils.Utils */
public class C1116Utils {
    /* JADX WARNING: Code restructure failed: missing block: B:101:0x0160, code lost:
        if (r2.exists() != false) goto L_0x0162;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:102:0x0162, code lost:
        r2.delete();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:111:0x0176, code lost:
        if (r2.exists() != false) goto L_0x0162;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:120:0x0188, code lost:
        if (r2.exists() != false) goto L_0x0162;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:129:0x019a, code lost:
        if (r2.exists() != false) goto L_0x0162;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:138:0x01ac, code lost:
        if (r2.exists() != false) goto L_0x0162;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:147:0x01be, code lost:
        if (r2.exists() != false) goto L_0x0162;
     */
    /* JADX WARNING: Removed duplicated region for block: B:100:0x015c  */
    /* JADX WARNING: Removed duplicated region for block: B:106:0x016b A[SYNTHETIC, Splitter:B:106:0x016b] */
    /* JADX WARNING: Removed duplicated region for block: B:110:0x0172  */
    /* JADX WARNING: Removed duplicated region for block: B:115:0x017d A[SYNTHETIC, Splitter:B:115:0x017d] */
    /* JADX WARNING: Removed duplicated region for block: B:119:0x0184  */
    /* JADX WARNING: Removed duplicated region for block: B:124:0x018f A[SYNTHETIC, Splitter:B:124:0x018f] */
    /* JADX WARNING: Removed duplicated region for block: B:128:0x0196  */
    /* JADX WARNING: Removed duplicated region for block: B:133:0x01a1 A[SYNTHETIC, Splitter:B:133:0x01a1] */
    /* JADX WARNING: Removed duplicated region for block: B:137:0x01a8  */
    /* JADX WARNING: Removed duplicated region for block: B:142:0x01b3 A[SYNTHETIC, Splitter:B:142:0x01b3] */
    /* JADX WARNING: Removed duplicated region for block: B:146:0x01ba  */
    /* JADX WARNING: Removed duplicated region for block: B:85:0x0140 A[SYNTHETIC, Splitter:B:85:0x0140] */
    /* JADX WARNING: Removed duplicated region for block: B:96:0x0155 A[SYNTHETIC, Splitter:B:96:0x0155] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.Boolean copyExifData(java.io.File r11, java.io.File r12, java.util.List<org.apache.sanselan.formats.tiff.constants.TagInfo> r13, java.lang.Boolean r14) throws java.lang.Throwable {
        /*
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = r12.getAbsolutePath()
            r0.append(r1)
            java.lang.String r1 = ".tmp"
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            r1 = 0
            java.io.File r2 = new java.io.File     // Catch:{ ImageReadException -> 0x01af, ImageWriteException -> 0x019d, IOException -> 0x018b, Exception -> 0x0179, NoClassDefFoundError -> 0x0167, Error -> 0x0151, all -> 0x013c }
            r2.<init>(r0)     // Catch:{ ImageReadException -> 0x01af, ImageWriteException -> 0x019d, IOException -> 0x018b, Exception -> 0x0179, NoClassDefFoundError -> 0x0167, Error -> 0x0151, all -> 0x013c }
            r0 = 73
            org.apache.sanselan.formats.tiff.write.TiffOutputSet r11 = getSanselanOutputSet(r11, r0)     // Catch:{ ImageReadException -> 0x0139, ImageWriteException -> 0x0136, IOException -> 0x0134, Exception -> 0x0132, NoClassDefFoundError -> 0x0130, Error -> 0x012e, all -> 0x012c }
            int r0 = r11.byteOrder     // Catch:{ ImageReadException -> 0x0139, ImageWriteException -> 0x0136, IOException -> 0x0134, Exception -> 0x0132, NoClassDefFoundError -> 0x0130, Error -> 0x012e, all -> 0x012c }
            org.apache.sanselan.formats.tiff.write.TiffOutputSet r0 = getSanselanOutputSet(r12, r0)     // Catch:{ ImageReadException -> 0x0139, ImageWriteException -> 0x0136, IOException -> 0x0134, Exception -> 0x0132, NoClassDefFoundError -> 0x0130, Error -> 0x012e, all -> 0x012c }
            int r3 = r11.byteOrder     // Catch:{ ImageReadException -> 0x0139, ImageWriteException -> 0x0136, IOException -> 0x0134, Exception -> 0x0132, NoClassDefFoundError -> 0x0130, Error -> 0x012e, all -> 0x012c }
            int r4 = r0.byteOrder     // Catch:{ ImageReadException -> 0x0139, ImageWriteException -> 0x0136, IOException -> 0x0134, Exception -> 0x0132, NoClassDefFoundError -> 0x0130, Error -> 0x012e, all -> 0x012c }
            r5 = 0
            if (r3 == r4) goto L_0x003c
            java.lang.Boolean r11 = java.lang.Boolean.valueOf(r5)     // Catch:{ ImageReadException -> 0x0139, ImageWriteException -> 0x0136, IOException -> 0x0134, Exception -> 0x0132, NoClassDefFoundError -> 0x0130, Error -> 0x012e, all -> 0x012c }
            boolean r12 = r2.exists()
            if (r12 == 0) goto L_0x003b
            r2.delete()
        L_0x003b:
            return r11
        L_0x003c:
            r0.getOrCreateExifDirectory()     // Catch:{ ImageReadException -> 0x0139, ImageWriteException -> 0x0136, IOException -> 0x0134, Exception -> 0x0132, NoClassDefFoundError -> 0x0130, Error -> 0x012e, all -> 0x012c }
            java.util.List r11 = r11.getDirectories()     // Catch:{ ImageReadException -> 0x0139, ImageWriteException -> 0x0136, IOException -> 0x0134, Exception -> 0x0132, NoClassDefFoundError -> 0x0130, Error -> 0x012e, all -> 0x012c }
            r3 = 0
        L_0x0044:
            int r4 = r11.size()     // Catch:{ ImageReadException -> 0x0139, ImageWriteException -> 0x0136, IOException -> 0x0134, Exception -> 0x0132, NoClassDefFoundError -> 0x0130, Error -> 0x012e, all -> 0x012c }
            if (r3 >= r4) goto L_0x00db
            java.lang.Object r4 = r11.get(r3)     // Catch:{ ImageReadException -> 0x0139, ImageWriteException -> 0x0136, IOException -> 0x0134, Exception -> 0x0132, NoClassDefFoundError -> 0x0130, Error -> 0x012e, all -> 0x012c }
            org.apache.sanselan.formats.tiff.write.TiffOutputDirectory r4 = (org.apache.sanselan.formats.tiff.write.TiffOutputDirectory) r4     // Catch:{ ImageReadException -> 0x0139, ImageWriteException -> 0x0136, IOException -> 0x0134, Exception -> 0x0132, NoClassDefFoundError -> 0x0130, Error -> 0x012e, all -> 0x012c }
            org.apache.sanselan.formats.tiff.write.TiffOutputDirectory r6 = getOrCreateExifDirectory(r0, r4)     // Catch:{ ImageReadException -> 0x0139, ImageWriteException -> 0x0136, IOException -> 0x0134, Exception -> 0x0132, NoClassDefFoundError -> 0x0130, Error -> 0x012e, all -> 0x012c }
            if (r6 != 0) goto L_0x0058
            goto L_0x00d7
        L_0x0058:
            if (r14 == 0) goto L_0x0089
            boolean r7 = r14.booleanValue()     // Catch:{ ImageReadException -> 0x0139, ImageWriteException -> 0x0136, IOException -> 0x0134, Exception -> 0x0132, NoClassDefFoundError -> 0x0130, Error -> 0x012e, all -> 0x012c }
            if (r7 == 0) goto L_0x0089
            java.util.ArrayList r7 = r6.getFields()     // Catch:{ ImageReadException -> 0x0139, ImageWriteException -> 0x0136, IOException -> 0x0134, Exception -> 0x0132, NoClassDefFoundError -> 0x0130, Error -> 0x012e, all -> 0x012c }
            r8 = 0
        L_0x0065:
            int r9 = r7.size()     // Catch:{ ImageReadException -> 0x0139, ImageWriteException -> 0x0136, IOException -> 0x0134, Exception -> 0x0132, NoClassDefFoundError -> 0x0130, Error -> 0x012e, all -> 0x012c }
            if (r8 >= r9) goto L_0x0089
            java.lang.Object r9 = r7.get(r8)     // Catch:{ ImageReadException -> 0x0139, ImageWriteException -> 0x0136, IOException -> 0x0134, Exception -> 0x0132, NoClassDefFoundError -> 0x0130, Error -> 0x012e, all -> 0x012c }
            org.apache.sanselan.formats.tiff.write.TiffOutputField r9 = (org.apache.sanselan.formats.tiff.write.TiffOutputField) r9     // Catch:{ ImageReadException -> 0x0139, ImageWriteException -> 0x0136, IOException -> 0x0134, Exception -> 0x0132, NoClassDefFoundError -> 0x0130, Error -> 0x012e, all -> 0x012c }
            if (r13 == 0) goto L_0x0081
            org.apache.sanselan.formats.tiff.constants.TagInfo r10 = r9.tagInfo     // Catch:{ ImageReadException -> 0x0139, ImageWriteException -> 0x0136, IOException -> 0x0134, Exception -> 0x0132, NoClassDefFoundError -> 0x0130, Error -> 0x012e, all -> 0x012c }
            boolean r10 = r13.contains(r10)     // Catch:{ ImageReadException -> 0x0139, ImageWriteException -> 0x0136, IOException -> 0x0134, Exception -> 0x0132, NoClassDefFoundError -> 0x0130, Error -> 0x012e, all -> 0x012c }
            if (r10 == 0) goto L_0x0081
            org.apache.sanselan.formats.tiff.constants.TagInfo r9 = r9.tagInfo     // Catch:{ ImageReadException -> 0x0139, ImageWriteException -> 0x0136, IOException -> 0x0134, Exception -> 0x0132, NoClassDefFoundError -> 0x0130, Error -> 0x012e, all -> 0x012c }
            r6.removeField(r9)     // Catch:{ ImageReadException -> 0x0139, ImageWriteException -> 0x0136, IOException -> 0x0134, Exception -> 0x0132, NoClassDefFoundError -> 0x0130, Error -> 0x012e, all -> 0x012c }
            goto L_0x0086
        L_0x0081:
            org.apache.sanselan.formats.tiff.constants.TagInfo r9 = r9.tagInfo     // Catch:{ ImageReadException -> 0x0139, ImageWriteException -> 0x0136, IOException -> 0x0134, Exception -> 0x0132, NoClassDefFoundError -> 0x0130, Error -> 0x012e, all -> 0x012c }
            r6.removeField(r9)     // Catch:{ ImageReadException -> 0x0139, ImageWriteException -> 0x0136, IOException -> 0x0134, Exception -> 0x0132, NoClassDefFoundError -> 0x0130, Error -> 0x012e, all -> 0x012c }
        L_0x0086:
            int r8 = r8 + 1
            goto L_0x0065
        L_0x0089:
            java.util.ArrayList r4 = r4.getFields()     // Catch:{ ImageReadException -> 0x0139, ImageWriteException -> 0x0136, IOException -> 0x0134, Exception -> 0x0132, NoClassDefFoundError -> 0x0130, Error -> 0x012e, all -> 0x012c }
            r7 = 0
        L_0x008e:
            int r8 = r4.size()     // Catch:{ ImageReadException -> 0x0139, ImageWriteException -> 0x0136, IOException -> 0x0134, Exception -> 0x0132, NoClassDefFoundError -> 0x0130, Error -> 0x012e, all -> 0x012c }
            if (r7 >= r8) goto L_0x00d7
            java.lang.Object r8 = r4.get(r7)     // Catch:{ ImageReadException -> 0x0139, ImageWriteException -> 0x0136, IOException -> 0x0134, Exception -> 0x0132, NoClassDefFoundError -> 0x0130, Error -> 0x012e, all -> 0x012c }
            org.apache.sanselan.formats.tiff.write.TiffOutputField r8 = (org.apache.sanselan.formats.tiff.write.TiffOutputField) r8     // Catch:{ ImageReadException -> 0x0139, ImageWriteException -> 0x0136, IOException -> 0x0134, Exception -> 0x0132, NoClassDefFoundError -> 0x0130, Error -> 0x012e, all -> 0x012c }
            if (r13 == 0) goto L_0x00aa
            org.apache.sanselan.formats.tiff.constants.TagInfo r9 = r8.tagInfo     // Catch:{ ImageReadException -> 0x0139, ImageWriteException -> 0x0136, IOException -> 0x0134, Exception -> 0x0132, NoClassDefFoundError -> 0x0130, Error -> 0x012e, all -> 0x012c }
            boolean r9 = r13.contains(r9)     // Catch:{ ImageReadException -> 0x0139, ImageWriteException -> 0x0136, IOException -> 0x0134, Exception -> 0x0132, NoClassDefFoundError -> 0x0130, Error -> 0x012e, all -> 0x012c }
            if (r9 == 0) goto L_0x00aa
            org.apache.sanselan.formats.tiff.constants.TagInfo r8 = r8.tagInfo     // Catch:{ ImageReadException -> 0x0139, ImageWriteException -> 0x0136, IOException -> 0x0134, Exception -> 0x0132, NoClassDefFoundError -> 0x0130, Error -> 0x012e, all -> 0x012c }
            r6.removeField(r8)     // Catch:{ ImageReadException -> 0x0139, ImageWriteException -> 0x0136, IOException -> 0x0134, Exception -> 0x0132, NoClassDefFoundError -> 0x0130, Error -> 0x012e, all -> 0x012c }
            goto L_0x00d4
        L_0x00aa:
            org.apache.sanselan.formats.tiff.constants.TagInfo r9 = r8.tagInfo     // Catch:{ ImageReadException -> 0x0139, ImageWriteException -> 0x0136, IOException -> 0x0134, Exception -> 0x0132, NoClassDefFoundError -> 0x0130, Error -> 0x012e, all -> 0x012c }
            r6.removeField(r9)     // Catch:{ ImageReadException -> 0x0139, ImageWriteException -> 0x0136, IOException -> 0x0134, Exception -> 0x0132, NoClassDefFoundError -> 0x0130, Error -> 0x012e, all -> 0x012c }
            org.apache.sanselan.formats.tiff.constants.TagInfo r9 = r8.tagInfo     // Catch:{ ImageReadException -> 0x0139, ImageWriteException -> 0x0136, IOException -> 0x0134, Exception -> 0x0132, NoClassDefFoundError -> 0x0130, Error -> 0x012e, all -> 0x012c }
            java.lang.String r9 = r9.name     // Catch:{ ImageReadException -> 0x0139, ImageWriteException -> 0x0136, IOException -> 0x0134, Exception -> 0x0132, NoClassDefFoundError -> 0x0130, Error -> 0x012e, all -> 0x012c }
            if (r9 == 0) goto L_0x00c5
            org.apache.sanselan.formats.tiff.constants.TagInfo r9 = r8.tagInfo     // Catch:{ ImageReadException -> 0x0139, ImageWriteException -> 0x0136, IOException -> 0x0134, Exception -> 0x0132, NoClassDefFoundError -> 0x0130, Error -> 0x012e, all -> 0x012c }
            java.lang.String r9 = r9.name     // Catch:{ ImageReadException -> 0x0139, ImageWriteException -> 0x0136, IOException -> 0x0134, Exception -> 0x0132, NoClassDefFoundError -> 0x0130, Error -> 0x012e, all -> 0x012c }
            java.lang.String r10 = "Orientation"
            boolean r9 = r9.equals(r10)     // Catch:{ ImageReadException -> 0x0139, ImageWriteException -> 0x0136, IOException -> 0x0134, Exception -> 0x0132, NoClassDefFoundError -> 0x0130, Error -> 0x012e, all -> 0x012c }
            if (r9 != 0) goto L_0x00c5
            r6.add(r8)     // Catch:{ ImageReadException -> 0x0139, ImageWriteException -> 0x0136, IOException -> 0x0134, Exception -> 0x0132, NoClassDefFoundError -> 0x0130, Error -> 0x012e, all -> 0x012c }
            goto L_0x00d4
        L_0x00c5:
            org.apache.sanselan.formats.tiff.constants.TagInfo r8 = org.apache.sanselan.formats.tiff.constants.TiffConstants.EXIF_TAG_ORIENTATION     // Catch:{ ImageReadException -> 0x0139, ImageWriteException -> 0x0136, IOException -> 0x0134, Exception -> 0x0132, NoClassDefFoundError -> 0x0130, Error -> 0x012e, all -> 0x012c }
            int r9 = r0.byteOrder     // Catch:{ ImageReadException -> 0x0139, ImageWriteException -> 0x0136, IOException -> 0x0134, Exception -> 0x0132, NoClassDefFoundError -> 0x0130, Error -> 0x012e, all -> 0x012c }
            java.lang.Integer r10 = java.lang.Integer.valueOf(r5)     // Catch:{ ImageReadException -> 0x0139, ImageWriteException -> 0x0136, IOException -> 0x0134, Exception -> 0x0132, NoClassDefFoundError -> 0x0130, Error -> 0x012e, all -> 0x012c }
            org.apache.sanselan.formats.tiff.write.TiffOutputField r8 = org.apache.sanselan.formats.tiff.write.TiffOutputField.create(r8, r9, r10)     // Catch:{ ImageReadException -> 0x0139, ImageWriteException -> 0x0136, IOException -> 0x0134, Exception -> 0x0132, NoClassDefFoundError -> 0x0130, Error -> 0x012e, all -> 0x012c }
            r6.add(r8)     // Catch:{ ImageReadException -> 0x0139, ImageWriteException -> 0x0136, IOException -> 0x0134, Exception -> 0x0132, NoClassDefFoundError -> 0x0130, Error -> 0x012e, all -> 0x012c }
        L_0x00d4:
            int r7 = r7 + 1
            goto L_0x008e
        L_0x00d7:
            int r3 = r3 + 1
            goto L_0x0044
        L_0x00db:
            java.io.BufferedOutputStream r11 = new java.io.BufferedOutputStream     // Catch:{ ImageReadException -> 0x0139, ImageWriteException -> 0x0136, IOException -> 0x0134, Exception -> 0x0132, NoClassDefFoundError -> 0x0130, Error -> 0x012e, all -> 0x012c }
            java.io.FileOutputStream r13 = new java.io.FileOutputStream     // Catch:{ ImageReadException -> 0x0139, ImageWriteException -> 0x0136, IOException -> 0x0134, Exception -> 0x0132, NoClassDefFoundError -> 0x0130, Error -> 0x012e, all -> 0x012c }
            r13.<init>(r2)     // Catch:{ ImageReadException -> 0x0139, ImageWriteException -> 0x0136, IOException -> 0x0134, Exception -> 0x0132, NoClassDefFoundError -> 0x0130, Error -> 0x012e, all -> 0x012c }
            r11.<init>(r13)     // Catch:{ ImageReadException -> 0x0139, ImageWriteException -> 0x0136, IOException -> 0x0134, Exception -> 0x0132, NoClassDefFoundError -> 0x0130, Error -> 0x012e, all -> 0x012c }
            org.apache.sanselan.formats.jpeg.exifRewrite.ExifRewriter r13 = new org.apache.sanselan.formats.jpeg.exifRewrite.ExifRewriter     // Catch:{ ImageReadException -> 0x0127, ImageWriteException -> 0x0122, IOException -> 0x011d, Exception -> 0x0118, NoClassDefFoundError -> 0x0113, Error -> 0x010f, all -> 0x010b }
            r13.<init>()     // Catch:{ ImageReadException -> 0x0127, ImageWriteException -> 0x0122, IOException -> 0x011d, Exception -> 0x0118, NoClassDefFoundError -> 0x0113, Error -> 0x010f, all -> 0x010b }
            r13.updateExifMetadataLossless(r12, r11, r0)     // Catch:{ ImageReadException -> 0x0127, ImageWriteException -> 0x0122, IOException -> 0x011d, Exception -> 0x0118, NoClassDefFoundError -> 0x0113, Error -> 0x010f, all -> 0x010b }
            r11.close()     // Catch:{ ImageReadException -> 0x0127, ImageWriteException -> 0x0122, IOException -> 0x011d, Exception -> 0x0118, NoClassDefFoundError -> 0x0113, Error -> 0x010f, all -> 0x010b }
            boolean r13 = r12.delete()     // Catch:{ ImageReadException -> 0x0127, ImageWriteException -> 0x0122, IOException -> 0x011d, Exception -> 0x0118, NoClassDefFoundError -> 0x0113, Error -> 0x010f, all -> 0x010b }
            if (r13 == 0) goto L_0x00f9
            r2.renameTo(r12)     // Catch:{ ImageReadException -> 0x0127, ImageWriteException -> 0x0122, IOException -> 0x011d, Exception -> 0x0118, NoClassDefFoundError -> 0x0113, Error -> 0x010f, all -> 0x010b }
        L_0x00f9:
            r12 = 1
            java.lang.Boolean r12 = java.lang.Boolean.valueOf(r12)     // Catch:{ ImageReadException -> 0x0127, ImageWriteException -> 0x0122, IOException -> 0x011d, Exception -> 0x0118, NoClassDefFoundError -> 0x0113, Error -> 0x010f, all -> 0x010b }
            r11.close()     // Catch:{ IOException -> 0x0101 }
        L_0x0101:
            boolean r11 = r2.exists()
            if (r11 == 0) goto L_0x010a
            r2.delete()
        L_0x010a:
            return r12
        L_0x010b:
            r12 = move-exception
            r1 = r11
            r11 = r12
            goto L_0x013e
        L_0x010f:
            r12 = move-exception
            r1 = r11
            r11 = r12
            goto L_0x0153
        L_0x0113:
            r12 = move-exception
            r1 = r11
            r11 = r12
            goto L_0x0169
        L_0x0118:
            r12 = move-exception
            r1 = r11
            r11 = r12
            goto L_0x017b
        L_0x011d:
            r12 = move-exception
            r1 = r11
            r11 = r12
            goto L_0x018d
        L_0x0122:
            r12 = move-exception
            r1 = r11
            r11 = r12
            goto L_0x019f
        L_0x0127:
            r12 = move-exception
            r1 = r11
            r11 = r12
            goto L_0x01b1
        L_0x012c:
            r11 = move-exception
            goto L_0x013e
        L_0x012e:
            r11 = move-exception
            goto L_0x0153
        L_0x0130:
            r11 = move-exception
            goto L_0x0169
        L_0x0132:
            r11 = move-exception
            goto L_0x017b
        L_0x0134:
            r11 = move-exception
            goto L_0x018d
        L_0x0136:
            r11 = move-exception
            goto L_0x019f
        L_0x0139:
            r11 = move-exception
            goto L_0x01b1
        L_0x013c:
            r11 = move-exception
            r2 = r1
        L_0x013e:
            if (r1 == 0) goto L_0x0145
            r1.close()     // Catch:{ IOException -> 0x0144 }
            goto L_0x0145
        L_0x0144:
        L_0x0145:
            if (r2 == 0) goto L_0x0150
            boolean r12 = r2.exists()
            if (r12 == 0) goto L_0x0150
            r2.delete()
        L_0x0150:
            throw r11
        L_0x0151:
            r11 = move-exception
            r2 = r1
        L_0x0153:
            if (r1 == 0) goto L_0x015a
            r1.close()     // Catch:{ IOException -> 0x0159 }
            goto L_0x015a
        L_0x0159:
        L_0x015a:
            if (r2 == 0) goto L_0x01c1
            boolean r12 = r2.exists()
            if (r12 == 0) goto L_0x01c1
        L_0x0162:
            r2.delete()
            goto L_0x01c1
        L_0x0167:
            r11 = move-exception
            r2 = r1
        L_0x0169:
            if (r1 == 0) goto L_0x0170
            r1.close()     // Catch:{ IOException -> 0x016f }
            goto L_0x0170
        L_0x016f:
        L_0x0170:
            if (r2 == 0) goto L_0x01c1
            boolean r12 = r2.exists()
            if (r12 == 0) goto L_0x01c1
            goto L_0x0162
        L_0x0179:
            r11 = move-exception
            r2 = r1
        L_0x017b:
            if (r1 == 0) goto L_0x0182
            r1.close()     // Catch:{ IOException -> 0x0181 }
            goto L_0x0182
        L_0x0181:
        L_0x0182:
            if (r2 == 0) goto L_0x01c1
            boolean r12 = r2.exists()
            if (r12 == 0) goto L_0x01c1
            goto L_0x0162
        L_0x018b:
            r11 = move-exception
            r2 = r1
        L_0x018d:
            if (r1 == 0) goto L_0x0194
            r1.close()     // Catch:{ IOException -> 0x0193 }
            goto L_0x0194
        L_0x0193:
        L_0x0194:
            if (r2 == 0) goto L_0x01c1
            boolean r12 = r2.exists()
            if (r12 == 0) goto L_0x01c1
            goto L_0x0162
        L_0x019d:
            r11 = move-exception
            r2 = r1
        L_0x019f:
            if (r1 == 0) goto L_0x01a6
            r1.close()     // Catch:{ IOException -> 0x01a5 }
            goto L_0x01a6
        L_0x01a5:
        L_0x01a6:
            if (r2 == 0) goto L_0x01c1
            boolean r12 = r2.exists()
            if (r12 == 0) goto L_0x01c1
            goto L_0x0162
        L_0x01af:
            r11 = move-exception
            r2 = r1
        L_0x01b1:
            if (r1 == 0) goto L_0x01b8
            r1.close()     // Catch:{ IOException -> 0x01b7 }
            goto L_0x01b8
        L_0x01b7:
        L_0x01b8:
            if (r2 == 0) goto L_0x01c1
            boolean r12 = r2.exists()
            if (r12 == 0) goto L_0x01c1
            goto L_0x0162
        L_0x01c1:
            r11.printStackTrace()
            throw r11
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.floristicreactlibrary.utils.C1116Utils.copyExifData(java.io.File, java.io.File, java.util.List, java.lang.Boolean):java.lang.Boolean");
    }

    private static TiffOutputSet getSanselanOutputSet(File file, int i) throws IOException, ImageReadException, ImageWriteException {
        TiffImageMetadata tiffImageMetadata;
        JpegImageMetadata jpegImageMetadata = (JpegImageMetadata) Sanselan.getMetadata(file);
        TiffOutputSet tiffOutputSet = null;
        if (jpegImageMetadata != null) {
            tiffImageMetadata = jpegImageMetadata.getExif();
            if (tiffImageMetadata != null) {
                tiffOutputSet = tiffImageMetadata.getOutputSet();
            }
        } else {
            tiffImageMetadata = null;
        }
        if (tiffOutputSet == null) {
            if (tiffImageMetadata != null) {
                i = tiffImageMetadata.contents.header.byteOrder;
            }
            tiffOutputSet = new TiffOutputSet(i);
        }
        return tiffOutputSet;
    }

    private static TiffOutputDirectory getOrCreateExifDirectory(TiffOutputSet tiffOutputSet, TiffOutputDirectory tiffOutputDirectory) {
        TiffOutputDirectory findDirectory = tiffOutputSet.findDirectory(tiffOutputDirectory.type);
        if (findDirectory != null) {
            return findDirectory;
        }
        TiffOutputDirectory tiffOutputDirectory2 = new TiffOutputDirectory(tiffOutputDirectory.type);
        try {
            tiffOutputSet.addDirectory(tiffOutputDirectory2);
            return tiffOutputDirectory2;
        } catch (ImageWriteException unused) {
            return null;
        }
    }
}

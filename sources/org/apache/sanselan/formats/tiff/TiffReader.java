package org.apache.sanselan.formats.tiff;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.sanselan.FormatCompliance;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.SanselanConstants;
import org.apache.sanselan.common.BinaryFileParser;
import org.apache.sanselan.common.byteSources.ByteSource;
import org.apache.sanselan.formats.tiff.TiffDirectory.ImageDataElement;
import org.apache.sanselan.formats.tiff.TiffImageData.Data;
import org.apache.sanselan.formats.tiff.TiffImageData.Strips;
import org.apache.sanselan.formats.tiff.TiffImageData.Tiles;
import org.apache.sanselan.formats.tiff.constants.TiffConstants;

public class TiffReader extends BinaryFileParser implements TiffConstants {
    private final boolean strict;

    private static class Collector implements Listener {
        private ArrayList directories;
        private ArrayList fields;
        private final boolean readThumbnails;
        private TiffHeader tiffHeader;

        public boolean readOffsetDirectories() {
            return true;
        }

        public Collector() {
            this(null);
        }

        public Collector(Map map) {
            this.tiffHeader = null;
            this.directories = new ArrayList();
            this.fields = new ArrayList();
            this.readThumbnails = (map == null || !map.containsKey(SanselanConstants.PARAM_KEY_READ_THUMBNAILS)) ? true : Boolean.TRUE.equals(map.get(SanselanConstants.PARAM_KEY_READ_THUMBNAILS));
        }

        public boolean setTiffHeader(TiffHeader tiffHeader2) {
            this.tiffHeader = tiffHeader2;
            return true;
        }

        public boolean addDirectory(TiffDirectory tiffDirectory) {
            this.directories.add(tiffDirectory);
            return true;
        }

        public boolean addField(TiffField tiffField) {
            this.fields.add(tiffField);
            return true;
        }

        public boolean readImageData() {
            return this.readThumbnails;
        }

        public TiffContents getContents() {
            return new TiffContents(this.tiffHeader, this.directories);
        }
    }

    private static class DirectoryCollector extends Collector {
        private final boolean readImageData;

        public DirectoryCollector(boolean z) {
            this.readImageData = z;
        }

        public boolean addDirectory(TiffDirectory tiffDirectory) {
            super.addDirectory(tiffDirectory);
            return false;
        }

        public boolean readImageData() {
            return this.readImageData;
        }
    }

    private static class FirstDirectoryCollector extends Collector {
        private final boolean readImageData;

        public FirstDirectoryCollector(boolean z) {
            this.readImageData = z;
        }

        public boolean addDirectory(TiffDirectory tiffDirectory) {
            super.addDirectory(tiffDirectory);
            return false;
        }

        public boolean readImageData() {
            return this.readImageData;
        }
    }

    public interface Listener {
        boolean addDirectory(TiffDirectory tiffDirectory);

        boolean addField(TiffField tiffField);

        boolean readImageData();

        boolean readOffsetDirectories();

        boolean setTiffHeader(TiffHeader tiffHeader);
    }

    public TiffReader(boolean z) {
        this.strict = z;
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x0019 A[SYNTHETIC, Splitter:B:14:0x0019] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private org.apache.sanselan.formats.tiff.TiffHeader readTiffHeader(org.apache.sanselan.common.byteSources.ByteSource r1, org.apache.sanselan.FormatCompliance r2) throws org.apache.sanselan.ImageReadException, java.io.IOException {
        /*
            r0 = this;
            java.io.InputStream r1 = r1.getInputStream()     // Catch:{ all -> 0x0015 }
            org.apache.sanselan.formats.tiff.TiffHeader r2 = r0.readTiffHeader(r1, r2)     // Catch:{ all -> 0x0013 }
            if (r1 == 0) goto L_0x0012
            r1.close()     // Catch:{ Exception -> 0x000e }
            goto L_0x0012
        L_0x000e:
            r1 = move-exception
            org.apache.sanselan.util.Debug.debug(r1)
        L_0x0012:
            return r2
        L_0x0013:
            r2 = move-exception
            goto L_0x0017
        L_0x0015:
            r2 = move-exception
            r1 = 0
        L_0x0017:
            if (r1 == 0) goto L_0x0021
            r1.close()     // Catch:{ Exception -> 0x001d }
            goto L_0x0021
        L_0x001d:
            r1 = move-exception
            org.apache.sanselan.util.Debug.debug(r1)
        L_0x0021:
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.sanselan.formats.tiff.TiffReader.readTiffHeader(org.apache.sanselan.common.byteSources.ByteSource, org.apache.sanselan.FormatCompliance):org.apache.sanselan.formats.tiff.TiffHeader");
    }

    private TiffHeader readTiffHeader(InputStream inputStream, FormatCompliance formatCompliance) throws ImageReadException, IOException {
        byte readByte = readByte("BYTE_ORDER_1", inputStream, "Not a Valid TIFF File");
        setByteOrder(readByte, readByte("BYTE_ORDER_2", inputStream, "Not a Valid TIFF File"));
        int read2Bytes = read2Bytes("tiffVersion", inputStream, "Not a Valid TIFF File");
        if (read2Bytes == 42) {
            int read4Bytes = read4Bytes("offsetToFirstIFD", inputStream, "Not a Valid TIFF File");
            skipBytes(inputStream, read4Bytes - 8, "Not a Valid TIFF File: couldn't find IFDs");
            if (this.debug) {
                System.out.println("");
            }
            return new TiffHeader(readByte, read2Bytes, read4Bytes);
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Unknown Tiff Version: ");
        stringBuffer.append(read2Bytes);
        throw new ImageReadException(stringBuffer.toString());
    }

    private void readDirectories(ByteSource byteSource, FormatCompliance formatCompliance, Listener listener) throws ImageReadException, IOException {
        TiffHeader readTiffHeader = readTiffHeader(byteSource, formatCompliance);
        if (listener.setTiffHeader(readTiffHeader)) {
            readDirectory(byteSource, readTiffHeader.offsetToFirstIFD, 0, formatCompliance, listener, new ArrayList());
        }
    }

    private boolean readDirectory(ByteSource byteSource, int i, int i2, FormatCompliance formatCompliance, Listener listener, List list) throws ImageReadException, IOException {
        return readDirectory(byteSource, i, i2, formatCompliance, listener, false, list);
    }

    /* JADX WARNING: Removed duplicated region for block: B:125:0x01d3 A[SYNTHETIC, Splitter:B:125:0x01d3] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean readDirectory(org.apache.sanselan.common.byteSources.ByteSource r21, int r22, int r23, org.apache.sanselan.FormatCompliance r24, org.apache.sanselan.formats.tiff.TiffReader.Listener r25, boolean r26, java.util.List r27) throws org.apache.sanselan.ImageReadException, java.io.IOException {
        /*
            r20 = this;
            r9 = r20
            r0 = r21
            r1 = r22
            r8 = r23
            r7 = r25
            r6 = r27
            java.lang.Integer r2 = new java.lang.Integer
            r2.<init>(r1)
            boolean r3 = r6.contains(r2)
            r4 = 0
            if (r3 == 0) goto L_0x0019
            return r4
        L_0x0019:
            r6.add(r2)
            r2 = 0
            java.io.InputStream r5 = r21.getInputStream()     // Catch:{ all -> 0x01cd }
            if (r1 <= 0) goto L_0x002e
            long r2 = (long) r1
            r5.skip(r2)     // Catch:{ all -> 0x0028 }
            goto L_0x002e
        L_0x0028:
            r0 = move-exception
            r1 = r0
            r16 = r5
            goto L_0x01d1
        L_0x002e:
            java.util.ArrayList r3 = new java.util.ArrayList     // Catch:{ all -> 0x01c8 }
            r3.<init>()     // Catch:{ all -> 0x01c8 }
            long r10 = (long) r1     // Catch:{ all -> 0x01c8 }
            long r12 = r21.getLength()     // Catch:{ all -> 0x01c8 }
            r18 = 1
            int r2 = (r10 > r12 ? 1 : (r10 == r12 ? 0 : -1))
            if (r2 < 0) goto L_0x004a
            if (r5 == 0) goto L_0x0049
            r5.close()     // Catch:{ Exception -> 0x0044 }
            goto L_0x0049
        L_0x0044:
            r0 = move-exception
            r1 = r0
            org.apache.sanselan.util.Debug.debug(r1)
        L_0x0049:
            return r18
        L_0x004a:
            java.lang.String r2 = "DirectoryEntryCount"
            java.lang.String r10 = "Not a Valid TIFF File"
            int r2 = r9.read2Bytes(r2, r5, r10)     // Catch:{ IOException -> 0x01b2 }
            r15 = 0
        L_0x0053:
            if (r15 >= r2) goto L_0x00b9
            java.lang.String r10 = "Tag"
            java.lang.String r11 = "Not a Valid TIFF File"
            int r11 = r9.read2Bytes(r10, r5, r11)     // Catch:{ all -> 0x0028 }
            java.lang.String r10 = "Type"
            java.lang.String r12 = "Not a Valid TIFF File"
            int r13 = r9.read2Bytes(r10, r5, r12)     // Catch:{ all -> 0x0028 }
            java.lang.String r10 = "Length"
            java.lang.String r12 = "Not a Valid TIFF File"
            int r14 = r9.read4Bytes(r10, r5, r12)     // Catch:{ all -> 0x0028 }
            java.lang.String r10 = "ValueOffset"
            r12 = 4
            java.lang.String r4 = "Not a Valid TIFF File"
            byte[] r4 = r9.readByteArray(r10, r12, r5, r4)     // Catch:{ all -> 0x0028 }
            java.lang.String r10 = "ValueOffset"
            int r16 = r9.convertByteArrayToInt(r10, r4)     // Catch:{ all -> 0x0028 }
            if (r11 != 0) goto L_0x0082
            r19 = r2
            r6 = r15
            goto L_0x00b1
        L_0x0082:
            org.apache.sanselan.formats.tiff.TiffField r12 = new org.apache.sanselan.formats.tiff.TiffField     // Catch:{ all -> 0x0028 }
            int r17 = r20.getByteOrder()     // Catch:{ all -> 0x0028 }
            r10 = r12
            r19 = r2
            r2 = r12
            r12 = r23
            r6 = r15
            r15 = r16
            r16 = r4
            r10.<init>(r11, r12, r13, r14, r15, r16, r17)     // Catch:{ all -> 0x0028 }
            r2.setSortHint(r6)     // Catch:{ all -> 0x0028 }
            r2.fillInValue(r0)     // Catch:{ all -> 0x0028 }
            r3.add(r2)     // Catch:{ all -> 0x0028 }
            boolean r2 = r7.addField(r2)     // Catch:{ all -> 0x0028 }
            if (r2 != 0) goto L_0x00b1
            if (r5 == 0) goto L_0x00b0
            r5.close()     // Catch:{ Exception -> 0x00ab }
            goto L_0x00b0
        L_0x00ab:
            r0 = move-exception
            r1 = r0
            org.apache.sanselan.util.Debug.debug(r1)
        L_0x00b0:
            return r18
        L_0x00b1:
            int r15 = r6 + 1
            r2 = r19
            r4 = 0
            r6 = r27
            goto L_0x0053
        L_0x00b9:
            java.lang.String r2 = "nextDirectoryOffset"
            java.lang.String r4 = "Not a Valid TIFF File"
            int r2 = r9.read4Bytes(r2, r5, r4)     // Catch:{ all -> 0x01c8 }
            org.apache.sanselan.formats.tiff.TiffDirectory r10 = new org.apache.sanselan.formats.tiff.TiffDirectory     // Catch:{ all -> 0x01c8 }
            r10.<init>(r8, r3, r1, r2)     // Catch:{ all -> 0x01c8 }
            boolean r1 = r25.readImageData()     // Catch:{ all -> 0x01c8 }
            if (r1 == 0) goto L_0x00e6
            boolean r1 = r10.hasTiffImageData()     // Catch:{ all -> 0x0028 }
            if (r1 == 0) goto L_0x00d9
            org.apache.sanselan.formats.tiff.TiffImageData r1 = r9.getTiffRawImageData(r0, r10)     // Catch:{ all -> 0x0028 }
            r10.setTiffImageData(r1)     // Catch:{ all -> 0x0028 }
        L_0x00d9:
            boolean r1 = r10.hasJpegImageData()     // Catch:{ all -> 0x0028 }
            if (r1 == 0) goto L_0x00e6
            org.apache.sanselan.formats.tiff.JpegImageData r1 = r9.getJpegRawImageData(r0, r10)     // Catch:{ all -> 0x0028 }
            r10.setJpegImageData(r1)     // Catch:{ all -> 0x0028 }
        L_0x00e6:
            boolean r1 = r7.addDirectory(r10)     // Catch:{ all -> 0x01c8 }
            if (r1 != 0) goto L_0x00f8
            if (r5 == 0) goto L_0x00f7
            r5.close()     // Catch:{ Exception -> 0x00f2 }
            goto L_0x00f7
        L_0x00f2:
            r0 = move-exception
            r1 = r0
            org.apache.sanselan.util.Debug.debug(r1)
        L_0x00f7:
            return r18
        L_0x00f8:
            boolean r1 = r25.readOffsetDirectories()     // Catch:{ all -> 0x01c8 }
            if (r1 == 0) goto L_0x018d
            java.util.ArrayList r11 = new java.util.ArrayList     // Catch:{ all -> 0x01c8 }
            r11.<init>()     // Catch:{ all -> 0x01c8 }
            r12 = 0
        L_0x0104:
            int r1 = r3.size()     // Catch:{ all -> 0x01c8 }
            if (r12 >= r1) goto L_0x0186
            java.lang.Object r1 = r3.get(r12)     // Catch:{ all -> 0x01c8 }
            r13 = r1
            org.apache.sanselan.formats.tiff.TiffField r13 = (org.apache.sanselan.formats.tiff.TiffField) r13     // Catch:{ all -> 0x01c8 }
            int r1 = r13.tag     // Catch:{ all -> 0x01c8 }
            org.apache.sanselan.formats.tiff.constants.TagInfo r2 = org.apache.sanselan.formats.tiff.constants.TiffConstants.EXIF_TAG_EXIF_OFFSET     // Catch:{ all -> 0x01c8 }
            int r2 = r2.tag     // Catch:{ all -> 0x01c8 }
            if (r1 == r2) goto L_0x012e
            int r1 = r13.tag     // Catch:{ all -> 0x0028 }
            org.apache.sanselan.formats.tiff.constants.TagInfo r2 = org.apache.sanselan.formats.tiff.constants.TiffConstants.EXIF_TAG_GPSINFO     // Catch:{ all -> 0x0028 }
            int r2 = r2.tag     // Catch:{ all -> 0x0028 }
            if (r1 == r2) goto L_0x012e
            int r1 = r13.tag     // Catch:{ all -> 0x0028 }
            org.apache.sanselan.formats.tiff.constants.TagInfo r2 = org.apache.sanselan.formats.tiff.constants.TiffConstants.EXIF_TAG_INTEROP_OFFSET     // Catch:{ all -> 0x0028 }
            int r2 = r2.tag     // Catch:{ all -> 0x0028 }
            if (r1 != r2) goto L_0x012a
            goto L_0x012e
        L_0x012a:
            r15 = r3
            r16 = r5
            goto L_0x0172
        L_0x012e:
            java.lang.Object r1 = r13.getValue()     // Catch:{ all -> 0x01c8 }
            java.lang.Number r1 = (java.lang.Number) r1     // Catch:{ all -> 0x01c8 }
            int r4 = r1.intValue()     // Catch:{ all -> 0x01c8 }
            int r1 = r13.tag     // Catch:{ all -> 0x01c8 }
            org.apache.sanselan.formats.tiff.constants.TagInfo r2 = org.apache.sanselan.formats.tiff.constants.TiffConstants.EXIF_TAG_EXIF_OFFSET     // Catch:{ all -> 0x01c8 }
            int r2 = r2.tag     // Catch:{ all -> 0x01c8 }
            if (r1 != r2) goto L_0x0143
            r1 = -2
            r6 = -2
            goto L_0x0158
        L_0x0143:
            int r1 = r13.tag     // Catch:{ all -> 0x01c8 }
            org.apache.sanselan.formats.tiff.constants.TagInfo r2 = org.apache.sanselan.formats.tiff.constants.TiffConstants.EXIF_TAG_GPSINFO     // Catch:{ all -> 0x01c8 }
            int r2 = r2.tag     // Catch:{ all -> 0x01c8 }
            if (r1 != r2) goto L_0x014e
            r1 = -3
            r6 = -3
            goto L_0x0158
        L_0x014e:
            int r1 = r13.tag     // Catch:{ all -> 0x01c8 }
            org.apache.sanselan.formats.tiff.constants.TagInfo r2 = org.apache.sanselan.formats.tiff.constants.TiffConstants.EXIF_TAG_INTEROP_OFFSET     // Catch:{ all -> 0x01c8 }
            int r2 = r2.tag     // Catch:{ all -> 0x01c8 }
            if (r1 != r2) goto L_0x017c
            r1 = -4
            r6 = -4
        L_0x0158:
            r14 = 1
            r1 = r20
            r2 = r21
            r15 = r3
            r3 = r4
            r4 = r6
            r16 = r5
            r5 = r24
            r6 = r25
            r7 = r14
            r8 = r27
            boolean r1 = r1.readDirectory(r2, r3, r4, r5, r6, r7, r8)     // Catch:{ all -> 0x01c6 }
            if (r1 != 0) goto L_0x0172
            r11.add(r13)     // Catch:{ all -> 0x01c6 }
        L_0x0172:
            int r12 = r12 + 1
            r8 = r23
            r7 = r25
            r3 = r15
            r5 = r16
            goto L_0x0104
        L_0x017c:
            r16 = r5
            org.apache.sanselan.ImageReadException r0 = new org.apache.sanselan.ImageReadException     // Catch:{ all -> 0x01c6 }
            java.lang.String r1 = "Unknown subdirectory type."
            r0.<init>(r1)     // Catch:{ all -> 0x01c6 }
            throw r0     // Catch:{ all -> 0x01c6 }
        L_0x0186:
            r15 = r3
            r16 = r5
            r15.removeAll(r11)     // Catch:{ all -> 0x01c6 }
            goto L_0x018f
        L_0x018d:
            r16 = r5
        L_0x018f:
            if (r26 != 0) goto L_0x01a6
            int r1 = r10.nextDirectoryOffset     // Catch:{ all -> 0x01c6 }
            if (r1 <= 0) goto L_0x01a6
            int r3 = r10.nextDirectoryOffset     // Catch:{ all -> 0x01c6 }
            int r4 = r23 + 1
            r1 = r20
            r2 = r21
            r5 = r24
            r6 = r25
            r7 = r27
            r1.readDirectory(r2, r3, r4, r5, r6, r7)     // Catch:{ all -> 0x01c6 }
        L_0x01a6:
            if (r16 == 0) goto L_0x01b1
            r16.close()     // Catch:{ Exception -> 0x01ac }
            goto L_0x01b1
        L_0x01ac:
            r0 = move-exception
            r1 = r0
            org.apache.sanselan.util.Debug.debug(r1)
        L_0x01b1:
            return r18
        L_0x01b2:
            r0 = move-exception
            r16 = r5
            boolean r1 = r9.strict     // Catch:{ all -> 0x01c6 }
            if (r1 != 0) goto L_0x01c5
            if (r16 == 0) goto L_0x01c4
            r16.close()     // Catch:{ Exception -> 0x01bf }
            goto L_0x01c4
        L_0x01bf:
            r0 = move-exception
            r1 = r0
            org.apache.sanselan.util.Debug.debug(r1)
        L_0x01c4:
            return r18
        L_0x01c5:
            throw r0     // Catch:{ all -> 0x01c6 }
        L_0x01c6:
            r0 = move-exception
            goto L_0x01cb
        L_0x01c8:
            r0 = move-exception
            r16 = r5
        L_0x01cb:
            r1 = r0
            goto L_0x01d1
        L_0x01cd:
            r0 = move-exception
            r1 = r0
            r16 = r2
        L_0x01d1:
            if (r16 == 0) goto L_0x01dc
            r16.close()     // Catch:{ Exception -> 0x01d7 }
            goto L_0x01dc
        L_0x01d7:
            r0 = move-exception
            r2 = r0
            org.apache.sanselan.util.Debug.debug(r2)
        L_0x01dc:
            throw r1
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.sanselan.formats.tiff.TiffReader.readDirectory(org.apache.sanselan.common.byteSources.ByteSource, int, int, org.apache.sanselan.FormatCompliance, org.apache.sanselan.formats.tiff.TiffReader$Listener, boolean, java.util.List):boolean");
    }

    public TiffContents readFirstDirectory(ByteSource byteSource, Map map, boolean z, FormatCompliance formatCompliance) throws ImageReadException, IOException {
        FirstDirectoryCollector firstDirectoryCollector = new FirstDirectoryCollector(z);
        read(byteSource, map, formatCompliance, firstDirectoryCollector);
        TiffContents contents = firstDirectoryCollector.getContents();
        if (contents.directories.size() >= 1) {
            return contents;
        }
        throw new ImageReadException("Image did not contain any directories.");
    }

    public TiffContents readDirectories(ByteSource byteSource, boolean z, FormatCompliance formatCompliance) throws ImageReadException, IOException {
        FirstDirectoryCollector firstDirectoryCollector = new FirstDirectoryCollector(z);
        readDirectories(byteSource, formatCompliance, (Listener) firstDirectoryCollector);
        TiffContents contents = firstDirectoryCollector.getContents();
        if (contents.directories.size() >= 1) {
            return contents;
        }
        throw new ImageReadException("Image did not contain any directories.");
    }

    public TiffContents readContents(ByteSource byteSource, Map map, FormatCompliance formatCompliance) throws ImageReadException, IOException {
        Collector collector = new Collector(map);
        read(byteSource, map, formatCompliance, collector);
        return collector.getContents();
    }

    public void read(ByteSource byteSource, Map map, FormatCompliance formatCompliance, Listener listener) throws ImageReadException, IOException {
        readDirectories(byteSource, formatCompliance, listener);
    }

    private TiffImageData getTiffRawImageData(ByteSource byteSource, TiffDirectory tiffDirectory) throws ImageReadException, IOException {
        ArrayList tiffRawImageDataElements = tiffDirectory.getTiffRawImageDataElements();
        Data[] dataArr = new Data[tiffRawImageDataElements.size()];
        for (int i = 0; i < tiffRawImageDataElements.size(); i++) {
            ImageDataElement imageDataElement = (ImageDataElement) tiffRawImageDataElements.get(i);
            dataArr[i] = new Data(imageDataElement.offset, imageDataElement.length, byteSource.getBlock(imageDataElement.offset, imageDataElement.length));
        }
        if (tiffDirectory.imageDataInStrips()) {
            TiffField findField = tiffDirectory.findField(TIFF_TAG_ROWS_PER_STRIP);
            if (findField != null) {
                return new Strips(dataArr, findField.getIntValue());
            }
            throw new ImageReadException("Can't find rows per strip field.");
        }
        TiffField findField2 = tiffDirectory.findField(TIFF_TAG_TILE_WIDTH);
        if (findField2 != null) {
            int intValue = findField2.getIntValue();
            TiffField findField3 = tiffDirectory.findField(TIFF_TAG_TILE_LENGTH);
            if (findField3 != null) {
                return new Tiles(dataArr, intValue, findField3.getIntValue());
            }
            throw new ImageReadException("Can't find tile length field.");
        }
        throw new ImageReadException("Can't find tile width field.");
    }

    private JpegImageData getJpegRawImageData(ByteSource byteSource, TiffDirectory tiffDirectory) throws ImageReadException, IOException {
        ImageDataElement jpegRawImageDataElement = tiffDirectory.getJpegRawImageDataElement();
        int i = jpegRawImageDataElement.offset;
        int i2 = jpegRawImageDataElement.length;
        if (((long) (i + i2)) == byteSource.getLength() + 1) {
            i2--;
        }
        return new JpegImageData(i, i2, byteSource.getBlock(i, i2));
    }
}

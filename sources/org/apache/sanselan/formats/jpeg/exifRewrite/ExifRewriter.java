package org.apache.sanselan.formats.jpeg.exifRewrite;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.common.BinaryFileParser;
import org.apache.sanselan.common.byteSources.ByteSource;
import org.apache.sanselan.common.byteSources.ByteSourceArray;
import org.apache.sanselan.common.byteSources.ByteSourceFile;
import org.apache.sanselan.common.byteSources.ByteSourceInputStream;
import org.apache.sanselan.formats.jpeg.JpegConstants;
import org.apache.sanselan.formats.jpeg.JpegUtils;
import org.apache.sanselan.formats.jpeg.JpegUtils.Visitor;
import org.apache.sanselan.formats.tiff.write.TiffImageWriterBase;
import org.apache.sanselan.formats.tiff.write.TiffImageWriterLossless;
import org.apache.sanselan.formats.tiff.write.TiffImageWriterLossy;
import org.apache.sanselan.formats.tiff.write.TiffOutputSet;
import org.apache.sanselan.util.Debug;

public class ExifRewriter extends BinaryFileParser implements JpegConstants {

    public static class ExifOverflowException extends ImageWriteException {
        public ExifOverflowException(String str) {
            super(str);
        }
    }

    private static abstract class JFIFPiece {
        /* access modifiers changed from: protected */
        public abstract void write(OutputStream outputStream) throws IOException;

        private JFIFPiece() {
        }
    }

    private static class JFIFPieceImageData extends JFIFPiece {
        public final byte[] imageData;
        public final byte[] markerBytes;

        public JFIFPieceImageData(byte[] bArr, byte[] bArr2) {
            super();
            this.markerBytes = bArr;
            this.imageData = bArr2;
        }

        /* access modifiers changed from: protected */
        public void write(OutputStream outputStream) throws IOException {
            outputStream.write(this.markerBytes);
            outputStream.write(this.imageData);
        }
    }

    private static class JFIFPieceSegment extends JFIFPiece {
        public final int marker;
        public final byte[] markerBytes;
        public final byte[] markerLengthBytes;
        public final byte[] segmentData;

        public JFIFPieceSegment(int i, byte[] bArr, byte[] bArr2, byte[] bArr3) {
            super();
            this.marker = i;
            this.markerBytes = bArr;
            this.markerLengthBytes = bArr2;
            this.segmentData = bArr3;
        }

        /* access modifiers changed from: protected */
        public void write(OutputStream outputStream) throws IOException {
            outputStream.write(this.markerBytes);
            outputStream.write(this.markerLengthBytes);
            outputStream.write(this.segmentData);
        }
    }

    private static class JFIFPieceSegmentExif extends JFIFPieceSegment {
        public JFIFPieceSegmentExif(int i, byte[] bArr, byte[] bArr2, byte[] bArr3) {
            super(i, bArr, bArr2, bArr3);
        }
    }

    private static class JFIFPieces {
        public final List exifPieces;
        public final List pieces;

        public JFIFPieces(List list, List list2) {
            this.pieces = list;
            this.exifPieces = list2;
        }
    }

    public ExifRewriter() {
        setByteOrder(77);
    }

    public ExifRewriter(int i) {
        setByteOrder(i);
    }

    private JFIFPieces analyzeJFIF(ByteSource byteSource) throws ImageReadException, IOException {
        final ArrayList arrayList = new ArrayList();
        final ArrayList arrayList2 = new ArrayList();
        new JpegUtils().traverseJFIF(byteSource, new Visitor() {
            public boolean beginSOS() {
                return true;
            }

            public void visitSOS(int i, byte[] bArr, byte[] bArr2) {
                arrayList.add(new JFIFPieceImageData(bArr, bArr2));
            }

            public boolean visitSegment(int i, byte[] bArr, int i2, byte[] bArr2, byte[] bArr3) throws ImageReadException, IOException {
                if (i != 65505) {
                    arrayList.add(new JFIFPieceSegment(i, bArr, bArr2, bArr3));
                } else if (!BinaryFileParser.byteArrayHasPrefix(bArr3, JpegConstants.EXIF_IDENTIFIER_CODE)) {
                    arrayList.add(new JFIFPieceSegment(i, bArr, bArr2, bArr3));
                } else {
                    JFIFPieceSegmentExif jFIFPieceSegmentExif = new JFIFPieceSegmentExif(i, bArr, bArr2, bArr3);
                    arrayList.add(jFIFPieceSegmentExif);
                    arrayList2.add(jFIFPieceSegmentExif);
                }
                return true;
            }
        });
        return new JFIFPieces(arrayList, arrayList2);
    }

    public void removeExifMetadata(File file, OutputStream outputStream) throws ImageReadException, IOException, ImageWriteException {
        removeExifMetadata((ByteSource) new ByteSourceFile(file), outputStream);
    }

    public void removeExifMetadata(byte[] bArr, OutputStream outputStream) throws ImageReadException, IOException, ImageWriteException {
        removeExifMetadata((ByteSource) new ByteSourceArray(bArr), outputStream);
    }

    public void removeExifMetadata(InputStream inputStream, OutputStream outputStream) throws ImageReadException, IOException, ImageWriteException {
        removeExifMetadata((ByteSource) new ByteSourceInputStream(inputStream, null), outputStream);
    }

    public void removeExifMetadata(ByteSource byteSource, OutputStream outputStream) throws ImageReadException, IOException, ImageWriteException {
        writeSegmentsReplacingExif(outputStream, analyzeJFIF(byteSource).pieces, null);
    }

    public void updateExifMetadataLossless(File file, OutputStream outputStream, TiffOutputSet tiffOutputSet) throws ImageReadException, IOException, ImageWriteException {
        updateExifMetadataLossless((ByteSource) new ByteSourceFile(file), outputStream, tiffOutputSet);
    }

    public void updateExifMetadataLossless(byte[] bArr, OutputStream outputStream, TiffOutputSet tiffOutputSet) throws ImageReadException, IOException, ImageWriteException {
        updateExifMetadataLossless((ByteSource) new ByteSourceArray(bArr), outputStream, tiffOutputSet);
    }

    public void updateExifMetadataLossless(InputStream inputStream, OutputStream outputStream, TiffOutputSet tiffOutputSet) throws ImageReadException, IOException, ImageWriteException {
        updateExifMetadataLossless((ByteSource) new ByteSourceInputStream(inputStream, null), outputStream, tiffOutputSet);
    }

    public void updateExifMetadataLossless(ByteSource byteSource, OutputStream outputStream, TiffOutputSet tiffOutputSet) throws ImageReadException, IOException, ImageWriteException {
        TiffImageWriterBase tiffImageWriterBase;
        JFIFPieces analyzeJFIF = analyzeJFIF(byteSource);
        List list = analyzeJFIF.pieces;
        if (analyzeJFIF.exifPieces.size() > 0) {
            tiffImageWriterBase = new TiffImageWriterLossless(tiffOutputSet.byteOrder, getByteArrayTail("trimmed exif bytes", ((JFIFPieceSegment) analyzeJFIF.exifPieces.get(0)).segmentData, 6));
        } else {
            tiffImageWriterBase = new TiffImageWriterLossy(tiffOutputSet.byteOrder);
        }
        writeSegmentsReplacingExif(outputStream, list, writeExifSegment(tiffImageWriterBase, tiffOutputSet, true));
    }

    public void updateExifMetadataLossy(byte[] bArr, OutputStream outputStream, TiffOutputSet tiffOutputSet) throws ImageReadException, IOException, ImageWriteException {
        updateExifMetadataLossy((ByteSource) new ByteSourceArray(bArr), outputStream, tiffOutputSet);
    }

    public void updateExifMetadataLossy(InputStream inputStream, OutputStream outputStream, TiffOutputSet tiffOutputSet) throws ImageReadException, IOException, ImageWriteException {
        updateExifMetadataLossy((ByteSource) new ByteSourceInputStream(inputStream, null), outputStream, tiffOutputSet);
    }

    public void updateExifMetadataLossy(File file, OutputStream outputStream, TiffOutputSet tiffOutputSet) throws ImageReadException, IOException, ImageWriteException {
        updateExifMetadataLossy((ByteSource) new ByteSourceFile(file), outputStream, tiffOutputSet);
    }

    public void updateExifMetadataLossy(ByteSource byteSource, OutputStream outputStream, TiffOutputSet tiffOutputSet) throws ImageReadException, IOException, ImageWriteException {
        writeSegmentsReplacingExif(outputStream, analyzeJFIF(byteSource).pieces, writeExifSegment(new TiffImageWriterLossy(tiffOutputSet.byteOrder), tiffOutputSet, true));
    }

    private void writeSegmentsReplacingExif(OutputStream outputStream, List list, byte[] bArr) throws ImageWriteException, IOException {
        int byteOrder = getByteOrder();
        try {
            outputStream.write(SOI);
            boolean z = false;
            for (int i = 0; i < list.size(); i++) {
                if (((JFIFPiece) list.get(i)) instanceof JFIFPieceSegmentExif) {
                    z = true;
                }
            }
            if (!z && bArr != null) {
                byte[] convertShortToByteArray = convertShortToByteArray(JpegConstants.JPEG_APP1_Marker, byteOrder);
                if (bArr.length <= 65535) {
                    byte[] convertShortToByteArray2 = convertShortToByteArray(bArr.length + 2, byteOrder);
                    int i2 = ((JFIFPieceSegment) list.get(0)).marker;
                    list.add(0, new JFIFPieceSegmentExif(JpegConstants.JPEG_APP1_Marker, convertShortToByteArray, convertShortToByteArray2, bArr));
                } else {
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append("APP1 Segment is too long: ");
                    stringBuffer.append(bArr.length);
                    throw new ExifOverflowException(stringBuffer.toString());
                }
            }
            boolean z2 = false;
            for (int i3 = 0; i3 < list.size(); i3++) {
                JFIFPiece jFIFPiece = (JFIFPiece) list.get(i3);
                if (!(jFIFPiece instanceof JFIFPieceSegmentExif)) {
                    jFIFPiece.write(outputStream);
                } else if (!z2) {
                    if (bArr != null) {
                        byte[] convertShortToByteArray3 = convertShortToByteArray(JpegConstants.JPEG_APP1_Marker, byteOrder);
                        if (bArr.length <= 65535) {
                            byte[] convertShortToByteArray4 = convertShortToByteArray(bArr.length + 2, byteOrder);
                            outputStream.write(convertShortToByteArray3);
                            outputStream.write(convertShortToByteArray4);
                            outputStream.write(bArr);
                        } else {
                            StringBuffer stringBuffer2 = new StringBuffer();
                            stringBuffer2.append("APP1 Segment is too long: ");
                            stringBuffer2.append(bArr.length);
                            throw new ExifOverflowException(stringBuffer2.toString());
                        }
                    }
                    z2 = true;
                }
            }
            try {
            } catch (Exception e) {
                Debug.debug((Throwable) e);
            }
        } finally {
            try {
                outputStream.close();
            } catch (Exception e2) {
                Debug.debug((Throwable) e2);
            }
        }
    }

    private byte[] writeExifSegment(TiffImageWriterBase tiffImageWriterBase, TiffOutputSet tiffOutputSet, boolean z) throws IOException, ImageWriteException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (z) {
            byteArrayOutputStream.write(EXIF_IDENTIFIER_CODE);
            byteArrayOutputStream.write(0);
            byteArrayOutputStream.write(0);
        }
        tiffImageWriterBase.write(byteArrayOutputStream, tiffOutputSet);
        return byteArrayOutputStream.toByteArray();
    }
}

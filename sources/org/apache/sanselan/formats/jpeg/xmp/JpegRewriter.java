package org.apache.sanselan.formats.jpeg.xmp;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.common.BinaryFileParser;
import org.apache.sanselan.common.byteSources.ByteSource;
import org.apache.sanselan.formats.jpeg.JpegConstants;
import org.apache.sanselan.formats.jpeg.JpegUtils;
import org.apache.sanselan.formats.jpeg.JpegUtils.Visitor;
import org.apache.sanselan.formats.jpeg.iptc.IPTCParser;

public class JpegRewriter extends BinaryFileParser implements JpegConstants {
    private static final SegmentFilter EXIF_SEGMENT_FILTER = new SegmentFilter() {
        public boolean filter(JFIFPieceSegment jFIFPieceSegment) {
            return jFIFPieceSegment.isExifSegment();
        }
    };
    private static final int JPEG_BYTE_ORDER = 77;
    private static final SegmentFilter PHOTOSHOP_APP13_SEGMENT_FILTER = new SegmentFilter() {
        public boolean filter(JFIFPieceSegment jFIFPieceSegment) {
            return jFIFPieceSegment.isPhotoshopApp13Segment();
        }
    };
    private static final SegmentFilter XMP_SEGMENT_FILTER = new SegmentFilter() {
        public boolean filter(JFIFPieceSegment jFIFPieceSegment) {
            return jFIFPieceSegment.isXmpSegment();
        }
    };

    protected static abstract class JFIFPiece {
        /* access modifiers changed from: protected */
        public abstract void write(OutputStream outputStream) throws IOException;

        protected JFIFPiece() {
        }

        public String toString() {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("[");
            stringBuffer.append(getClass().getName());
            stringBuffer.append("]");
            return stringBuffer.toString();
        }
    }

    protected static class JFIFPieceImageData extends JFIFPiece {
        public final byte[] imageData;
        public final byte[] markerBytes;

        public JFIFPieceImageData(byte[] bArr, byte[] bArr2) {
            this.markerBytes = bArr;
            this.imageData = bArr2;
        }

        /* access modifiers changed from: protected */
        public void write(OutputStream outputStream) throws IOException {
            outputStream.write(this.markerBytes);
            outputStream.write(this.imageData);
        }
    }

    protected static class JFIFPieceSegment extends JFIFPiece {
        public final int marker;
        public final byte[] markerBytes;
        public final byte[] segmentData;
        public final byte[] segmentLengthBytes;

        public JFIFPieceSegment(int i, byte[] bArr) {
            this(i, JpegRewriter.int2ToByteArray(i, 77), JpegRewriter.int2ToByteArray(bArr.length + 2, 77), bArr);
        }

        public JFIFPieceSegment(int i, byte[] bArr, byte[] bArr2, byte[] bArr3) {
            this.marker = i;
            this.markerBytes = bArr;
            this.segmentLengthBytes = bArr2;
            this.segmentData = bArr3;
        }

        public String toString() {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("[");
            stringBuffer.append(getClass().getName());
            stringBuffer.append(" (0x");
            stringBuffer.append(Integer.toHexString(this.marker));
            stringBuffer.append(")]");
            return stringBuffer.toString();
        }

        /* access modifiers changed from: protected */
        public void write(OutputStream outputStream) throws IOException {
            outputStream.write(this.markerBytes);
            outputStream.write(this.segmentLengthBytes);
            outputStream.write(this.segmentData);
        }

        public boolean isApp1Segment() {
            return this.marker == 65505;
        }

        public boolean isAppSegment() {
            int i = this.marker;
            return i >= 65504 && i <= 65519;
        }

        public boolean isExifSegment() {
            if (this.marker == 65505 && BinaryFileParser.byteArrayHasPrefix(this.segmentData, JpegConstants.EXIF_IDENTIFIER_CODE)) {
                return true;
            }
            return false;
        }

        public boolean isPhotoshopApp13Segment() {
            if (this.marker == 65517 && new IPTCParser().isPhotoshopJpegSegment(this.segmentData)) {
                return true;
            }
            return false;
        }

        public boolean isXmpSegment() {
            if (this.marker == 65505 && BinaryFileParser.byteArrayHasPrefix(this.segmentData, JpegConstants.XMP_IDENTIFIER)) {
                return true;
            }
            return false;
        }
    }

    protected static class JFIFPieces {
        public final List pieces;
        public final List segmentPieces;

        public JFIFPieces(List list, List list2) {
            this.pieces = list;
            this.segmentPieces = list2;
        }
    }

    public static class JpegSegmentOverflowException extends ImageWriteException {
        public JpegSegmentOverflowException(String str) {
            super(str);
        }
    }

    private interface SegmentFilter {
        boolean filter(JFIFPieceSegment jFIFPieceSegment);
    }

    public JpegRewriter() {
        setByteOrder(77);
    }

    /* access modifiers changed from: protected */
    public JFIFPieces analyzeJFIF(ByteSource byteSource) throws ImageReadException, IOException {
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
                JFIFPieceSegment jFIFPieceSegment = new JFIFPieceSegment(i, bArr, bArr2, bArr3);
                arrayList.add(jFIFPieceSegment);
                arrayList2.add(jFIFPieceSegment);
                return true;
            }
        });
        return new JFIFPieces(arrayList, arrayList2);
    }

    /* access modifiers changed from: protected */
    public List removeXmpSegments(List list) {
        return filterSegments(list, XMP_SEGMENT_FILTER);
    }

    /* access modifiers changed from: protected */
    public List removePhotoshopApp13Segments(List list) {
        return filterSegments(list, PHOTOSHOP_APP13_SEGMENT_FILTER);
    }

    /* access modifiers changed from: protected */
    public List findPhotoshopApp13Segments(List list) {
        return filterSegments(list, PHOTOSHOP_APP13_SEGMENT_FILTER, true);
    }

    /* access modifiers changed from: protected */
    public List removeExifSegments(List list) {
        return filterSegments(list, EXIF_SEGMENT_FILTER);
    }

    /* access modifiers changed from: protected */
    public List filterSegments(List list, SegmentFilter segmentFilter) {
        return filterSegments(list, segmentFilter, false);
    }

    /* access modifiers changed from: protected */
    public List filterSegments(List list, SegmentFilter segmentFilter, boolean z) {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            JFIFPiece jFIFPiece = (JFIFPiece) list.get(i);
            if (jFIFPiece instanceof JFIFPieceSegment) {
                if (segmentFilter.filter((JFIFPieceSegment) jFIFPiece) ^ (!z)) {
                    arrayList.add(jFIFPiece);
                }
            } else if (!z) {
                arrayList.add(jFIFPiece);
            }
        }
        return arrayList;
    }

    /* access modifiers changed from: protected */
    public List insertBeforeFirstAppSegments(List list, List list2) throws ImageWriteException {
        int i = -1;
        for (int i2 = 0; i2 < list.size(); i2++) {
            JFIFPiece jFIFPiece = (JFIFPiece) list.get(i2);
            if ((jFIFPiece instanceof JFIFPieceSegment) && ((JFIFPieceSegment) jFIFPiece).isAppSegment() && i == -1) {
                i = i2;
            }
        }
        ArrayList arrayList = new ArrayList(list);
        if (i != -1) {
            arrayList.addAll(i, list2);
            return arrayList;
        }
        throw new ImageWriteException("JPEG file has no APP segments.");
    }

    /* access modifiers changed from: protected */
    public List insertAfterLastAppSegments(List list, List list2) throws ImageWriteException {
        int i = -1;
        for (int i2 = 0; i2 < list.size(); i2++) {
            JFIFPiece jFIFPiece = (JFIFPiece) list.get(i2);
            if ((jFIFPiece instanceof JFIFPieceSegment) && ((JFIFPieceSegment) jFIFPiece).isAppSegment()) {
                i = i2;
            }
        }
        ArrayList arrayList = new ArrayList(list);
        if (i != -1) {
            arrayList.addAll(i + 1, list2);
        } else if (list.size() >= 1) {
            arrayList.addAll(1, list2);
        } else {
            throw new ImageWriteException("JPEG file has no APP segments.");
        }
        return arrayList;
    }

    /* access modifiers changed from: protected */
    public void writeSegments(OutputStream outputStream, List list) throws ImageWriteException, IOException {
        try {
            outputStream.write(SOI);
            for (int i = 0; i < list.size(); i++) {
                ((JFIFPiece) list.get(i)).write(outputStream);
            }
            outputStream.close();
        } catch (Throwable th) {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (Exception unused) {
                }
            }
            throw th;
        }
    }
}

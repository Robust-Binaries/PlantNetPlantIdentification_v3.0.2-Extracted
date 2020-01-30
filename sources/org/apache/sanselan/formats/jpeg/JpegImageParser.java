package org.apache.sanselan.formats.jpeg;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.sanselan.ImageFormat;
import org.apache.sanselan.ImageInfo;
import org.apache.sanselan.ImageParser;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.SanselanConstants;
import org.apache.sanselan.common.BinaryFileParser;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.common.byteSources.ByteSource;
import org.apache.sanselan.formats.jpeg.JpegUtils.Visitor;
import org.apache.sanselan.formats.jpeg.iptc.IPTCParser;
import org.apache.sanselan.formats.jpeg.iptc.PhotoshopApp13Data;
import org.apache.sanselan.formats.jpeg.segments.App13Segment;
import org.apache.sanselan.formats.jpeg.segments.App2Segment;
import org.apache.sanselan.formats.jpeg.segments.GenericSegment;
import org.apache.sanselan.formats.jpeg.segments.JFIFSegment;
import org.apache.sanselan.formats.jpeg.segments.SOFNSegment;
import org.apache.sanselan.formats.jpeg.segments.Segment;
import org.apache.sanselan.formats.jpeg.segments.UnknownSegment;
import org.apache.sanselan.formats.jpeg.xmp.JpegXmpParser;
import org.apache.sanselan.formats.tiff.TiffField;
import org.apache.sanselan.formats.tiff.TiffImageMetadata;
import org.apache.sanselan.formats.tiff.TiffImageParser;
import org.apache.sanselan.formats.tiff.constants.TiffTagConstants;
import org.apache.sanselan.util.Debug;

public class JpegImageParser extends ImageParser implements JpegConstants, TiffTagConstants {
    public static final String[] AcceptedExtensions = {DEFAULT_EXTENSION, ".jpeg"};
    private static final String DEFAULT_EXTENSION = ".jpg";
    public static final boolean permissive = true;

    public boolean embedICCProfile(File file, File file2, byte[] bArr) {
        return false;
    }

    public byte[] embedICCProfile(byte[] bArr, byte[] bArr2) {
        return null;
    }

    public String getDefaultExtension() {
        return DEFAULT_EXTENSION;
    }

    public String getName() {
        return "Jpeg-Custom";
    }

    public JpegImageParser() {
        setByteOrder(77);
    }

    /* access modifiers changed from: protected */
    public ImageFormat[] getAcceptedTypes() {
        return new ImageFormat[]{ImageFormat.IMAGE_FORMAT_JPEG};
    }

    /* access modifiers changed from: protected */
    public String[] getAcceptedExtensions() {
        return AcceptedExtensions;
    }

    public final BufferedImage getBufferedImage(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        throw new ImageReadException("Sanselan cannot read or write JPEG images.");
    }

    /* access modifiers changed from: private */
    public boolean keepMarker(int i, int[] iArr) {
        if (iArr == null) {
            return true;
        }
        for (int i2 : iArr) {
            if (i2 == i) {
                return true;
            }
        }
        return false;
    }

    public ArrayList readSegments(ByteSource byteSource, int[] iArr, boolean z, boolean z2) throws ImageReadException, IOException {
        ArrayList arrayList = new ArrayList();
        final int[] iArr2 = iArr;
        final ArrayList arrayList2 = arrayList;
        final boolean z3 = z;
        C15531 r0 = new Visitor() {
            public boolean beginSOS() {
                return false;
            }

            public void visitSOS(int i, byte[] bArr, byte[] bArr2) {
            }

            public boolean visitSegment(int i, byte[] bArr, int i2, byte[] bArr2, byte[] bArr3) throws ImageReadException, IOException {
                if (i == 65497) {
                    return false;
                }
                if (!JpegImageParser.this.keepMarker(i, iArr2)) {
                    return true;
                }
                if (i == 65517) {
                    arrayList2.add(new App13Segment(this, i, bArr3));
                } else if (i == 65506) {
                    arrayList2.add(new App2Segment(i, bArr3));
                } else if (i == 65504) {
                    arrayList2.add(new JFIFSegment(i, bArr3));
                } else if (i >= 65472 && i <= 65487) {
                    arrayList2.add(new SOFNSegment(i, bArr3));
                } else if (i >= 65505 && i <= 65519) {
                    arrayList2.add(new UnknownSegment(i, bArr3));
                }
                return !z3;
            }
        };
        new JpegUtils().traverseJFIF(byteSource, r0);
        return arrayList;
    }

    private byte[] assembleSegments(ArrayList arrayList) throws ImageReadException, IOException {
        try {
            return assembleSegments(arrayList, false);
        } catch (ImageReadException unused) {
            return assembleSegments(arrayList, true);
        }
    }

    private byte[] assembleSegments(ArrayList arrayList, boolean z) throws ImageReadException, IOException {
        if (arrayList.size() >= 1) {
            int i = ((App2Segment) arrayList.get(0)).num_markers;
            if (arrayList.size() == i) {
                Collections.sort(arrayList);
                boolean z2 = !z;
                int i2 = 0;
                int i3 = 0;
                while (i2 < arrayList.size()) {
                    App2Segment app2Segment = (App2Segment) arrayList.get(i2);
                    if (i2 + (z2 ? 1 : 0) != app2Segment.cur_marker) {
                        dumpSegments(arrayList);
                        StringBuffer stringBuffer = new StringBuffer();
                        stringBuffer.append("Incoherent App2 Segment Ordering.  i: ");
                        stringBuffer.append(i2);
                        stringBuffer.append(", segment[");
                        stringBuffer.append(i2);
                        stringBuffer.append("].cur_marker: ");
                        stringBuffer.append(app2Segment.cur_marker);
                        stringBuffer.append(".");
                        throw new ImageReadException(stringBuffer.toString());
                    } else if (i == app2Segment.num_markers) {
                        i3 += app2Segment.icc_bytes.length;
                        i2++;
                    } else {
                        dumpSegments(arrayList);
                        StringBuffer stringBuffer2 = new StringBuffer();
                        stringBuffer2.append("Inconsistent App2 Segment Count info.  markerCount: ");
                        stringBuffer2.append(i);
                        stringBuffer2.append(", segment[");
                        stringBuffer2.append(i2);
                        stringBuffer2.append("].num_markers: ");
                        stringBuffer2.append(app2Segment.num_markers);
                        stringBuffer2.append(".");
                        throw new ImageReadException(stringBuffer2.toString());
                    }
                }
                byte[] bArr = new byte[i3];
                int i4 = 0;
                for (int i5 = 0; i5 < arrayList.size(); i5++) {
                    App2Segment app2Segment2 = (App2Segment) arrayList.get(i5);
                    System.arraycopy(app2Segment2.icc_bytes, 0, bArr, i4, app2Segment2.icc_bytes.length);
                    i4 += app2Segment2.icc_bytes.length;
                }
                return bArr;
            }
            StringBuffer stringBuffer3 = new StringBuffer();
            stringBuffer3.append("App2 Segments Missing.  Found: ");
            stringBuffer3.append(arrayList.size());
            stringBuffer3.append(", Expected: ");
            stringBuffer3.append(i);
            stringBuffer3.append(".");
            throw new ImageReadException(stringBuffer3.toString());
        }
        throw new ImageReadException("No App2 Segments Found.");
    }

    private void dumpSegments(ArrayList arrayList) {
        Debug.debug();
        Debug.debug("dumpSegments", arrayList.size());
        for (int i = 0; i < arrayList.size(); i++) {
            App2Segment app2Segment = (App2Segment) arrayList.get(i);
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(i);
            stringBuffer.append(": ");
            stringBuffer.append(app2Segment.cur_marker);
            stringBuffer.append(" / ");
            stringBuffer.append(app2Segment.num_markers);
            Debug.debug(stringBuffer.toString());
        }
        Debug.debug();
    }

    public ArrayList readSegments(ByteSource byteSource, int[] iArr, boolean z) throws ImageReadException, IOException {
        return readSegments(byteSource, iArr, z, false);
    }

    public byte[] getICCProfileBytes(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        ArrayList readSegments = readSegments(byteSource, new int[]{65506}, false);
        if (readSegments != null) {
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < readSegments.size(); i++) {
                App2Segment app2Segment = (App2Segment) readSegments.get(i);
                if (app2Segment.icc_bytes != null) {
                    arrayList.add(app2Segment);
                }
            }
            readSegments = arrayList;
        }
        String str = null;
        if (readSegments == null || readSegments.size() < 1) {
            return null;
        }
        byte[] assembleSegments = assembleSegments(readSegments);
        if (this.debug) {
            PrintStream printStream = System.out;
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("bytes: ");
            if (assembleSegments != null) {
                StringBuffer stringBuffer2 = new StringBuffer();
                stringBuffer2.append("");
                stringBuffer2.append(assembleSegments.length);
                str = stringBuffer2.toString();
            }
            stringBuffer.append(str);
            printStream.println(stringBuffer.toString());
        }
        if (this.debug) {
            System.out.println("");
        }
        return assembleSegments;
    }

    public IImageMetadata getMetadata(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        TiffImageMetadata exifMetadata = getExifMetadata(byteSource, map);
        JpegPhotoshopMetadata photoshopMetadata = getPhotoshopMetadata(byteSource, map);
        if (exifMetadata == null && photoshopMetadata == null) {
            return null;
        }
        return new JpegImageMetadata(photoshopMetadata, exifMetadata);
    }

    public static boolean isExifAPP1Segment(GenericSegment genericSegment) {
        return byteArrayHasPrefix(genericSegment.bytes, EXIF_IDENTIFIER_CODE);
    }

    private ArrayList filterAPP1Segments(ArrayList arrayList) {
        ArrayList arrayList2 = new ArrayList();
        for (int i = 0; i < arrayList.size(); i++) {
            GenericSegment genericSegment = (GenericSegment) arrayList.get(i);
            if (isExifAPP1Segment(genericSegment)) {
                arrayList2.add(genericSegment);
            }
        }
        return arrayList2;
    }

    private ArrayList filterSegments(ArrayList arrayList, List list) {
        ArrayList arrayList2 = new ArrayList();
        for (int i = 0; i < arrayList.size(); i++) {
            Segment segment = (Segment) arrayList.get(i);
            if (list.contains(new Integer(segment.marker))) {
                arrayList2.add(segment);
            }
        }
        return arrayList2;
    }

    public TiffImageMetadata getExifMetadata(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        byte[] exifRawData = getExifRawData(byteSource);
        if (exifRawData == null) {
            return null;
        }
        if (map == null) {
            map = new HashMap();
        }
        if (!map.containsKey(SanselanConstants.PARAM_KEY_READ_THUMBNAILS)) {
            map.put(SanselanConstants.PARAM_KEY_READ_THUMBNAILS, Boolean.TRUE);
        }
        return (TiffImageMetadata) new TiffImageParser().getMetadata(exifRawData, map);
    }

    public byte[] getExifRawData(ByteSource byteSource) throws ImageReadException, IOException {
        ArrayList readSegments = readSegments(byteSource, new int[]{65505}, false);
        if (readSegments == null || readSegments.size() < 1) {
            return null;
        }
        ArrayList filterAPP1Segments = filterAPP1Segments(readSegments);
        if (this.debug) {
            PrintStream printStream = System.out;
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("exif_segments.size: ");
            stringBuffer.append(filterAPP1Segments.size());
            printStream.println(stringBuffer.toString());
        }
        if (filterAPP1Segments.size() < 1) {
            return null;
        }
        if (filterAPP1Segments.size() <= 1) {
            return getByteArrayTail("trimmed exif bytes", ((GenericSegment) filterAPP1Segments.get(0)).bytes, 6);
        }
        throw new ImageReadException("Sanselan currently can't parse EXIF metadata split across multiple APP1 segments.  Please send this image to the Sanselan project.");
    }

    public boolean hasExifSegment(ByteSource byteSource) throws ImageReadException, IOException {
        final boolean[] zArr = {false};
        new JpegUtils().traverseJFIF(byteSource, new Visitor() {
            public boolean beginSOS() {
                return false;
            }

            public void visitSOS(int i, byte[] bArr, byte[] bArr2) {
            }

            public boolean visitSegment(int i, byte[] bArr, int i2, byte[] bArr2, byte[] bArr3) throws ImageReadException, IOException {
                if (i == 65497) {
                    return false;
                }
                if (i != 65505 || !BinaryFileParser.byteArrayHasPrefix(bArr3, JpegConstants.EXIF_IDENTIFIER_CODE)) {
                    return true;
                }
                zArr[0] = true;
                return false;
            }
        });
        return zArr[0];
    }

    public boolean hasIptcSegment(ByteSource byteSource) throws ImageReadException, IOException {
        final boolean[] zArr = {false};
        new JpegUtils().traverseJFIF(byteSource, new Visitor() {
            public boolean beginSOS() {
                return false;
            }

            public void visitSOS(int i, byte[] bArr, byte[] bArr2) {
            }

            public boolean visitSegment(int i, byte[] bArr, int i2, byte[] bArr2, byte[] bArr3) throws ImageReadException, IOException {
                if (i == 65497) {
                    return false;
                }
                if (i != 65517 || !new IPTCParser().isPhotoshopJpegSegment(bArr3)) {
                    return true;
                }
                zArr[0] = true;
                return false;
            }
        });
        return zArr[0];
    }

    public boolean hasXmpSegment(ByteSource byteSource) throws ImageReadException, IOException {
        final boolean[] zArr = {false};
        new JpegUtils().traverseJFIF(byteSource, new Visitor() {
            public boolean beginSOS() {
                return false;
            }

            public void visitSOS(int i, byte[] bArr, byte[] bArr2) {
            }

            public boolean visitSegment(int i, byte[] bArr, int i2, byte[] bArr2, byte[] bArr3) throws ImageReadException, IOException {
                if (i == 65497) {
                    return false;
                }
                if (i != 65505 || !new JpegXmpParser().isXmpJpegSegment(bArr3)) {
                    return true;
                }
                zArr[0] = true;
                return false;
            }
        });
        return zArr[0];
    }

    public String getXmpXml(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        final ArrayList arrayList = new ArrayList();
        new JpegUtils().traverseJFIF(byteSource, new Visitor() {
            public boolean beginSOS() {
                return false;
            }

            public void visitSOS(int i, byte[] bArr, byte[] bArr2) {
            }

            public boolean visitSegment(int i, byte[] bArr, int i2, byte[] bArr2, byte[] bArr3) throws ImageReadException, IOException {
                if (i == 65497) {
                    return false;
                }
                if (i != 65505 || !new JpegXmpParser().isXmpJpegSegment(bArr3)) {
                    return true;
                }
                arrayList.add(new JpegXmpParser().parseXmpJpegSegment(bArr3));
                return false;
            }
        });
        if (arrayList.size() < 1) {
            return null;
        }
        if (arrayList.size() <= 1) {
            return (String) arrayList.get(0);
        }
        throw new ImageReadException("Jpeg file contains more than one XMP segment.");
    }

    public JpegPhotoshopMetadata getPhotoshopMetadata(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        int i = 0;
        ArrayList readSegments = readSegments(byteSource, new int[]{65517}, false);
        if (readSegments == null || readSegments.size() < 1) {
            return null;
        }
        PhotoshopApp13Data photoshopApp13Data = null;
        while (i < readSegments.size()) {
            PhotoshopApp13Data parsePhotoshopSegment = ((App13Segment) readSegments.get(i)).parsePhotoshopSegment(map);
            if (parsePhotoshopSegment == null || photoshopApp13Data == null) {
                i++;
                photoshopApp13Data = parsePhotoshopSegment;
            } else {
                throw new ImageReadException("Jpeg contains more than one Photoshop App13 segment.");
            }
        }
        if (photoshopApp13Data == null) {
            return null;
        }
        return new JpegPhotoshopMetadata(photoshopApp13Data);
    }

    public Dimension getImageSize(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        ArrayList readSegments = readSegments(byteSource, new int[]{JpegConstants.SOF0Marker, JpegConstants.SOF1Marker, JpegConstants.SOF2Marker, JpegConstants.SOF3Marker, JpegConstants.SOF5Marker, JpegConstants.SOF6Marker, JpegConstants.SOF7Marker, JpegConstants.SOF9Marker, JpegConstants.SOF10Marker, JpegConstants.SOF11Marker, JpegConstants.SOF13Marker, JpegConstants.SOF14Marker, JpegConstants.SOF15Marker}, true);
        if (readSegments == null || readSegments.size() < 1) {
            throw new ImageReadException("No JFIF Data Found.");
        } else if (readSegments.size() <= 1) {
            SOFNSegment sOFNSegment = (SOFNSegment) readSegments.get(0);
            return new Dimension(sOFNSegment.width, sOFNSegment.height);
        } else {
            throw new ImageReadException("Redundant JFIF Data Found.");
        }
    }

    public ImageInfo getImageInfo(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        double d;
        double d2;
        String str;
        float f;
        int i;
        float f2;
        ByteSource byteSource2 = byteSource;
        ArrayList readSegments = readSegments(byteSource2, new int[]{JpegConstants.SOF0Marker, JpegConstants.SOF1Marker, JpegConstants.SOF2Marker, JpegConstants.SOF3Marker, JpegConstants.SOF5Marker, JpegConstants.SOF6Marker, JpegConstants.SOF7Marker, JpegConstants.SOF9Marker, JpegConstants.SOF10Marker, JpegConstants.SOF11Marker, JpegConstants.SOF13Marker, JpegConstants.SOF14Marker, JpegConstants.SOF15Marker}, false);
        if (readSegments != null) {
            ArrayList readSegments2 = readSegments(byteSource2, new int[]{65504}, true);
            SOFNSegment sOFNSegment = (SOFNSegment) readSegments.get(0);
            if (sOFNSegment != null) {
                int i2 = sOFNSegment.width;
                int i3 = sOFNSegment.height;
                JFIFSegment jFIFSegment = null;
                if (readSegments2 != null && readSegments2.size() > 0) {
                    jFIFSegment = (JFIFSegment) readSegments2.get(0);
                }
                double d3 = -1.0d;
                if (jFIFSegment != null) {
                    d2 = (double) jFIFSegment.xDensity;
                    d = (double) jFIFSegment.yDensity;
                    int i4 = jFIFSegment.densityUnits;
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append("Jpeg/JFIF v.");
                    stringBuffer.append(jFIFSegment.jfifMajorVersion);
                    stringBuffer.append(".");
                    stringBuffer.append(jFIFSegment.jfifMinorVersion);
                    String stringBuffer2 = stringBuffer.toString();
                    switch (i4) {
                        case 1:
                            d3 = 1.0d;
                            break;
                        case 2:
                            d3 = 2.54d;
                            break;
                    }
                    str = stringBuffer2;
                } else {
                    JpegImageMetadata jpegImageMetadata = (JpegImageMetadata) getMetadata(byteSource, map);
                    if (jpegImageMetadata != null) {
                        TiffField findEXIFValue = jpegImageMetadata.findEXIFValue(TIFF_TAG_XRESOLUTION);
                        double doubleValue = findEXIFValue != null ? ((Number) findEXIFValue.getValue()).doubleValue() : -1.0d;
                        TiffField findEXIFValue2 = jpegImageMetadata.findEXIFValue(TIFF_TAG_YRESOLUTION);
                        double doubleValue2 = findEXIFValue2 != null ? ((Number) findEXIFValue2.getValue()).doubleValue() : -1.0d;
                        TiffField findEXIFValue3 = jpegImageMetadata.findEXIFValue(TIFF_TAG_RESOLUTION_UNIT);
                        if (findEXIFValue3 != null) {
                            switch (((Number) findEXIFValue3.getValue()).intValue()) {
                                case 2:
                                    d = doubleValue2;
                                    d3 = 1.0d;
                                    d2 = doubleValue;
                                    break;
                                case 3:
                                    d = doubleValue2;
                                    d3 = 2.54d;
                                    d2 = doubleValue;
                                    break;
                            }
                        }
                        d = doubleValue2;
                        d2 = doubleValue;
                    } else {
                        d2 = -1.0d;
                        d = -1.0d;
                    }
                    str = "Jpeg/DCM";
                }
                int i5 = -1;
                if (d3 > 0.0d) {
                    i = (int) Math.round(d2 / d3);
                    double d4 = (double) i2;
                    double d5 = d2 * d3;
                    Double.isNaN(d4);
                    float f3 = (float) (d4 / d5);
                    double d6 = d * d3;
                    int round = (int) Math.round(d6);
                    double d7 = (double) i3;
                    Double.isNaN(d7);
                    f = f3;
                    i5 = round;
                    f2 = (float) (d7 / d6);
                } else {
                    f2 = -1.0f;
                    i = -1;
                    f = -1.0f;
                }
                ArrayList arrayList = new ArrayList();
                int i6 = sOFNSegment.numberOfComponents;
                int i7 = i6 * sOFNSegment.precision;
                ImageFormat imageFormat = ImageFormat.IMAGE_FORMAT_JPEG;
                String str2 = "JPEG (Joint Photographic Experts Group) Format";
                String str3 = "image/jpeg";
                boolean z = sOFNSegment.marker == 65474;
                int i8 = i6 == 1 ? 0 : i6 == 3 ? 2 : i6 == 4 ? 3 : -2;
                ImageInfo imageInfo = new ImageInfo(str, i7, arrayList, imageFormat, str2, i3, str3, 1, i5, f2, i, f, i2, z, false, false, i8, ImageInfo.COMPRESSION_ALGORITHM_JPEG);
                return imageInfo;
            }
            throw new ImageReadException("No SOFN Data Found.");
        }
        throw new ImageReadException("No SOFN Data Found.");
    }

    public boolean dumpImageFile(PrintWriter printWriter, ByteSource byteSource) throws ImageReadException, IOException {
        printWriter.println("tiff.dumpImageFile");
        ImageInfo imageInfo = getImageInfo(byteSource);
        if (imageInfo == null) {
            return false;
        }
        imageInfo.toString(printWriter, "");
        printWriter.println("");
        ArrayList readSegments = readSegments(byteSource, null, false);
        if (readSegments != null) {
            for (int i = 0; i < readSegments.size(); i++) {
                Segment segment = (Segment) readSegments.get(i);
                NumberFormat integerInstance = NumberFormat.getIntegerInstance();
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(i);
                stringBuffer.append(": marker: ");
                stringBuffer.append(Integer.toHexString(segment.marker));
                stringBuffer.append(", ");
                stringBuffer.append(segment.getDescription());
                stringBuffer.append(" (length: ");
                stringBuffer.append(integerInstance.format((long) segment.length));
                stringBuffer.append(")");
                printWriter.println(stringBuffer.toString());
                segment.dump(printWriter);
            }
            printWriter.println("");
            return true;
        }
        throw new ImageReadException("No Segments Found.");
    }
}

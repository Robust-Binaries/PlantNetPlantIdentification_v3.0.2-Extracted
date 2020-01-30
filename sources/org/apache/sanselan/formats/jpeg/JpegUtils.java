package org.apache.sanselan.formats.jpeg;

import java.io.IOException;
import java.io.InputStream;
import org.apache.sanselan.ImageInfo;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.common.BinaryFileParser;
import org.apache.sanselan.common.byteSources.ByteSource;
import org.apache.sanselan.util.Debug;

public class JpegUtils extends BinaryFileParser implements JpegConstants {

    public interface Visitor {
        boolean beginSOS();

        void visitSOS(int i, byte[] bArr, byte[] bArr2);

        boolean visitSegment(int i, byte[] bArr, int i2, byte[] bArr2, byte[] bArr3) throws ImageReadException, IOException;
    }

    public static String getMarkerName(int i) {
        if (i == 65498) {
            return "SOS_Marker";
        }
        switch (i) {
            case JpegConstants.SOF0Marker /*65472*/:
                return "SOF0Marker";
            case JpegConstants.SOF1Marker /*65473*/:
                return "SOF1Marker";
            case JpegConstants.SOF2Marker /*65474*/:
                return "SOF2Marker";
            case JpegConstants.SOF3Marker /*65475*/:
                return "SOF3Marker";
            case JpegConstants.SOF4Marker /*65476*/:
                return "SOF4Marker";
            case JpegConstants.SOF5Marker /*65477*/:
                return "SOF5Marker";
            case JpegConstants.SOF6Marker /*65478*/:
                return "SOF6Marker";
            case JpegConstants.SOF7Marker /*65479*/:
                return "SOF7Marker";
            case JpegConstants.SOF8Marker /*65480*/:
                return "SOF8Marker";
            case JpegConstants.SOF9Marker /*65481*/:
                return "SOF9Marker";
            case JpegConstants.SOF10Marker /*65482*/:
                return "SOF10Marker";
            case JpegConstants.SOF11Marker /*65483*/:
                return "SOF11Marker";
            case JpegConstants.SOF12Marker /*65484*/:
                return "SOF12Marker";
            case JpegConstants.SOF13Marker /*65485*/:
                return "SOF13Marker";
            case JpegConstants.SOF14Marker /*65486*/:
                return "SOF14Marker";
            case JpegConstants.SOF15Marker /*65487*/:
                return "SOF15Marker";
            default:
                switch (i) {
                    case 65504:
                        return "JFIFMarker";
                    case JpegConstants.JPEG_APP1_Marker /*65505*/:
                        return "JPEG_APP1_Marker";
                    case JpegConstants.JPEG_APP2_Marker /*65506*/:
                        return "JPEG_APP2_Marker";
                    default:
                        switch (i) {
                            case JpegConstants.JPEG_APP13_Marker /*65517*/:
                                return "JPEG_APP13_Marker";
                            case JpegConstants.JPEG_APP14_Marker /*65518*/:
                                return "JPEG_APP14_Marker";
                            case JpegConstants.JPEG_APP15_Marker /*65519*/:
                                return "JPEG_APP15_Marker";
                            default:
                                return ImageInfo.COMPRESSION_ALGORITHM_UNKNOWN;
                        }
                }
        }
    }

    public JpegUtils() {
        setByteOrder(77);
    }

    public void traverseJFIF(ByteSource byteSource, Visitor visitor) throws ImageReadException, IOException {
        InputStream inputStream;
        byte[] readByteArray;
        int convertByteArrayToShort;
        byte[] readByteArray2;
        int convertByteArrayToShort2;
        try {
            inputStream = byteSource.getInputStream();
            try {
                readAndVerifyBytes(inputStream, SOI, "Not a Valid JPEG File: doesn't begin with 0xffd8");
                int byteOrder = getByteOrder();
                do {
                    readByteArray = readByteArray("markerBytes", 2, inputStream, "markerBytes");
                    convertByteArrayToShort = convertByteArrayToShort("marker", readByteArray, byteOrder);
                    if (convertByteArrayToShort != 65497) {
                        if (convertByteArrayToShort != 65498) {
                            readByteArray2 = readByteArray("segmentLengthBytes", 2, inputStream, "segmentLengthBytes");
                            convertByteArrayToShort2 = convertByteArrayToShort("segmentLength", readByteArray2, byteOrder);
                        }
                    }
                    if (!visitor.beginSOS()) {
                        try {
                            inputStream.close();
                        } catch (Exception e) {
                            Debug.debug((Throwable) e);
                        }
                        return;
                    }
                    visitor.visitSOS(convertByteArrayToShort, readByteArray, getStreamBytes(inputStream));
                    try {
                        inputStream.close();
                    } catch (Exception e2) {
                        Debug.debug((Throwable) e2);
                    }
                    return;
                } while (visitor.visitSegment(convertByteArrayToShort, readByteArray, convertByteArrayToShort2, readByteArray2, readByteArray("Segment Data", convertByteArrayToShort2 - 2, inputStream, "Invalid Segment: insufficient data")));
                try {
                    inputStream.close();
                } catch (Exception e3) {
                    Debug.debug((Throwable) e3);
                }
            } catch (Throwable th) {
                th = th;
                try {
                    inputStream.close();
                } catch (Exception e4) {
                    Debug.debug((Throwable) e4);
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

    public void dumpJFIF(ByteSource byteSource) throws ImageReadException, IOException, ImageWriteException {
        traverseJFIF(byteSource, new Visitor() {
            public boolean beginSOS() {
                return true;
            }

            public void visitSOS(int i, byte[] bArr, byte[] bArr2) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("SOS marker.  ");
                stringBuffer.append(bArr2.length);
                stringBuffer.append(" bytes of image data.");
                Debug.debug(stringBuffer.toString());
                Debug.debug("");
            }

            public boolean visitSegment(int i, byte[] bArr, int i2, byte[] bArr2, byte[] bArr3) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("Segment marker: ");
                stringBuffer.append(Integer.toHexString(i));
                stringBuffer.append(" (");
                stringBuffer.append(JpegUtils.getMarkerName(i));
                stringBuffer.append("), ");
                stringBuffer.append(bArr3.length);
                stringBuffer.append(" bytes of segment data.");
                Debug.debug(stringBuffer.toString());
                return true;
            }
        });
    }
}

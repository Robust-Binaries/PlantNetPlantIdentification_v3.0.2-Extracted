package org.apache.sanselan.formats.jpeg.iptc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import kotlin.UByte;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.SanselanConstants;
import org.apache.sanselan.common.BinaryFileParser;
import org.apache.sanselan.common.BinaryInputStream;
import org.apache.sanselan.common.BinaryOutputStream;
import org.apache.sanselan.util.Debug;
import org.apache.sanselan.util.ParamMap;

public class IPTCParser extends BinaryFileParser implements IPTCConstants {
    private static final int APP13_BYTE_ORDER = 77;

    public IPTCParser() {
        setByteOrder(77);
    }

    public boolean isPhotoshopJpegSegment(byte[] bArr) {
        if (!compareByteArrays(bArr, 0, PHOTOSHOP_IDENTIFICATION_STRING, 0, PHOTOSHOP_IDENTIFICATION_STRING.length)) {
            return false;
        }
        int length = PHOTOSHOP_IDENTIFICATION_STRING.length;
        if (CONST_8BIM.length + length > bArr.length) {
            return false;
        }
        if (!compareByteArrays(bArr, length, CONST_8BIM, 0, CONST_8BIM.length)) {
            return false;
        }
        return true;
    }

    public PhotoshopApp13Data parsePhotoshopSegment(byte[] bArr, Map map) throws ImageReadException, IOException {
        return parsePhotoshopSegment(bArr, ParamMap.getParamBoolean(map, SanselanConstants.PARAM_KEY_VERBOSE, false), ParamMap.getParamBoolean(map, SanselanConstants.PARAM_KEY_STRICT, false));
    }

    public PhotoshopApp13Data parsePhotoshopSegment(byte[] bArr, boolean z, boolean z2) throws ImageReadException, IOException {
        ArrayList arrayList = new ArrayList();
        List parseAllBlocks = parseAllBlocks(bArr, z, z2);
        for (int i = 0; i < parseAllBlocks.size(); i++) {
            IPTCBlock iPTCBlock = (IPTCBlock) parseAllBlocks.get(i);
            if (iPTCBlock.isIPTCBlock()) {
                arrayList.addAll(parseIPTCBlock(iPTCBlock.blockData, z));
            }
        }
        return new PhotoshopApp13Data(arrayList, parseAllBlocks);
    }

    /* access modifiers changed from: protected */
    public List parseIPTCBlock(byte[] bArr, boolean z) throws ImageReadException, IOException {
        ArrayList arrayList = new ArrayList();
        int i = 0;
        while (true) {
            int i2 = i + 1;
            if (i2 >= bArr.length) {
                return arrayList;
            }
            byte b = bArr[i] & UByte.MAX_VALUE;
            if (z) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(b);
                stringBuffer.append(" (0x");
                stringBuffer.append(Integer.toHexString(b));
                stringBuffer.append(")");
                Debug.debug("tagMarker", stringBuffer.toString());
            }
            if (b != 28) {
                if (z) {
                    System.out.println("Unexpected record tag marker in IPTC data.");
                }
                return arrayList;
            }
            i = i2 + 1;
            byte b2 = bArr[i2] & UByte.MAX_VALUE;
            if (z) {
                StringBuffer stringBuffer2 = new StringBuffer();
                stringBuffer2.append(b2);
                stringBuffer2.append(" (0x");
                stringBuffer2.append(Integer.toHexString(b2));
                stringBuffer2.append(")");
                Debug.debug("recordNumber", stringBuffer2.toString());
            }
            if (b2 == 2) {
                byte b3 = bArr[i] & UByte.MAX_VALUE;
                if (z) {
                    StringBuffer stringBuffer3 = new StringBuffer();
                    stringBuffer3.append(b3);
                    stringBuffer3.append(" (0x");
                    stringBuffer3.append(Integer.toHexString(b3));
                    stringBuffer3.append(")");
                    Debug.debug("recordType", stringBuffer3.toString());
                }
                int i3 = i + 1;
                int convertByteArrayToShort = convertByteArrayToShort("recordSize", i3, bArr);
                int i4 = i3 + 2;
                boolean z2 = convertByteArrayToShort > 32767;
                int i5 = convertByteArrayToShort & IPTCConstants.IPTC_NON_EXTENDED_RECORD_MAXIMUM_SIZE;
                if (z2 && z) {
                    StringBuffer stringBuffer4 = new StringBuffer();
                    stringBuffer4.append("extendedDataset. dataFieldCountLength: ");
                    stringBuffer4.append(i5);
                    Debug.debug(stringBuffer4.toString());
                }
                if (z2) {
                    return arrayList;
                }
                byte[] readBytearray = readBytearray("recordData", bArr, i4, convertByteArrayToShort);
                i = i4 + convertByteArrayToShort;
                if (b3 != 0) {
                    arrayList.add(new IPTCRecord(IPTCTypeLookup.getIptcType(b3), new String(readBytearray, "ISO-8859-1")));
                } else if (z) {
                    PrintStream printStream = System.out;
                    StringBuffer stringBuffer5 = new StringBuffer();
                    stringBuffer5.append("ignore record version record! ");
                    stringBuffer5.append(arrayList.size());
                    printStream.println(stringBuffer5.toString());
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public List parseAllBlocks(byte[] bArr, boolean z, boolean z2) throws ImageReadException, IOException {
        byte[] bArr2;
        ArrayList arrayList = new ArrayList();
        BinaryInputStream binaryInputStream = new BinaryInputStream(bArr, 77);
        if (compareByteArrays(binaryInputStream.readByteArray(PHOTOSHOP_IDENTIFICATION_STRING.length, "App13 Segment missing identification string"), PHOTOSHOP_IDENTIFICATION_STRING)) {
            while (true) {
                byte[] readByteArray = binaryInputStream.readByteArray(CONST_8BIM.length, "App13 Segment missing identification string", false, false);
                if (readByteArray == null) {
                    break;
                } else if (compareByteArrays(readByteArray, CONST_8BIM)) {
                    int read2ByteInteger = binaryInputStream.read2ByteInteger("Image Resource Block missing type");
                    if (z) {
                        StringBuffer stringBuffer = new StringBuffer();
                        stringBuffer.append(read2ByteInteger);
                        stringBuffer.append(" (0x");
                        stringBuffer.append(Integer.toHexString(read2ByteInteger));
                        stringBuffer.append(")");
                        Debug.debug("blockType", stringBuffer.toString());
                    }
                    int read1ByteInteger = binaryInputStream.read1ByteInteger("Image Resource Block missing name length");
                    if (z && read1ByteInteger > 0) {
                        StringBuffer stringBuffer2 = new StringBuffer();
                        stringBuffer2.append(read1ByteInteger);
                        stringBuffer2.append(" (0x");
                        stringBuffer2.append(Integer.toHexString(read1ByteInteger));
                        stringBuffer2.append(")");
                        Debug.debug("blockNameLength", stringBuffer2.toString());
                    }
                    if (read1ByteInteger != 0) {
                        byte[] readByteArray2 = binaryInputStream.readByteArray(read1ByteInteger, "Invalid Image Resource Block name", z, z2);
                        if (readByteArray2 == null) {
                            break;
                        }
                        if (read1ByteInteger % 2 == 0) {
                            binaryInputStream.read1ByteInteger("Image Resource Block missing padding byte");
                        }
                        bArr2 = readByteArray2;
                    } else {
                        binaryInputStream.read1ByteInteger("Image Resource Block has invalid name");
                        bArr2 = new byte[0];
                    }
                    int read4ByteInteger = binaryInputStream.read4ByteInteger("Image Resource Block missing size");
                    if (z) {
                        StringBuffer stringBuffer3 = new StringBuffer();
                        stringBuffer3.append(read4ByteInteger);
                        stringBuffer3.append(" (0x");
                        stringBuffer3.append(Integer.toHexString(read4ByteInteger));
                        stringBuffer3.append(")");
                        Debug.debug("blockSize", stringBuffer3.toString());
                    }
                    byte[] readByteArray3 = binaryInputStream.readByteArray(read4ByteInteger, "Invalid Image Resource Block data", z, z2);
                    if (readByteArray3 == null) {
                        break;
                    }
                    arrayList.add(new IPTCBlock(read2ByteInteger, bArr2, readByteArray3));
                    if (read4ByteInteger % 2 != 0) {
                        binaryInputStream.read1ByteInteger("Image Resource Block missing padding byte");
                    }
                } else {
                    throw new ImageReadException("Invalid Image Resource Block Signature");
                }
            }
            return arrayList;
        }
        throw new ImageReadException("Not a Photoshop App13 Segment");
    }

    public byte[] writePhotoshopApp13Segment(PhotoshopApp13Data photoshopApp13Data) throws IOException, ImageWriteException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BinaryOutputStream binaryOutputStream = new BinaryOutputStream(byteArrayOutputStream);
        binaryOutputStream.write(PHOTOSHOP_IDENTIFICATION_STRING);
        List rawBlocks = photoshopApp13Data.getRawBlocks();
        int i = 0;
        while (i < rawBlocks.size()) {
            IPTCBlock iPTCBlock = (IPTCBlock) rawBlocks.get(i);
            binaryOutputStream.write(CONST_8BIM);
            if (iPTCBlock.blockType < 0 || iPTCBlock.blockType > 65535) {
                throw new ImageWriteException("Invalid IPTC block type.");
            }
            binaryOutputStream.write2ByteInteger(iPTCBlock.blockType);
            if (iPTCBlock.blockNameBytes.length <= 255) {
                binaryOutputStream.write(iPTCBlock.blockNameBytes.length);
                binaryOutputStream.write(iPTCBlock.blockNameBytes);
                if (iPTCBlock.blockNameBytes.length % 2 == 0) {
                    binaryOutputStream.write(0);
                }
                if (iPTCBlock.blockData.length <= 32767) {
                    binaryOutputStream.write4ByteInteger(iPTCBlock.blockData.length);
                    binaryOutputStream.write(iPTCBlock.blockData);
                    if (iPTCBlock.blockData.length % 2 == 1) {
                        binaryOutputStream.write(0);
                    }
                    i++;
                } else {
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append("IPTC block data is too long: ");
                    stringBuffer.append(iPTCBlock.blockData.length);
                    throw new ImageWriteException(stringBuffer.toString());
                }
            } else {
                StringBuffer stringBuffer2 = new StringBuffer();
                stringBuffer2.append("IPTC block name is too long: ");
                stringBuffer2.append(iPTCBlock.blockNameBytes.length);
                throw new ImageWriteException(stringBuffer2.toString());
            }
        }
        binaryOutputStream.flush();
        return byteArrayOutputStream.toByteArray();
    }

    public byte[] writeIPTCBlock(List list) throws ImageWriteException, IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BinaryOutputStream binaryOutputStream = new BinaryOutputStream(byteArrayOutputStream, getByteOrder());
        binaryOutputStream.write(28);
        binaryOutputStream.write(2);
        binaryOutputStream.write(IPTC_TYPE_RECORD_VERSION.type);
        binaryOutputStream.write2Bytes(2);
        binaryOutputStream.write2Bytes(2);
        ArrayList arrayList = new ArrayList(list);
        Collections.sort(arrayList, new Comparator() {
            public int compare(Object obj, Object obj2) {
                return ((IPTCRecord) obj2).iptcType.type - ((IPTCRecord) obj).iptcType.type;
            }
        });
        for (int i = 0; i < arrayList.size(); i++) {
            IPTCRecord iPTCRecord = (IPTCRecord) arrayList.get(i);
            if (iPTCRecord.iptcType.type != IPTC_TYPE_RECORD_VERSION.type) {
                binaryOutputStream.write(28);
                binaryOutputStream.write(2);
                if (iPTCRecord.iptcType.type < 0 || iPTCRecord.iptcType.type > 255) {
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append("Invalid record type: ");
                    stringBuffer.append(iPTCRecord.iptcType.type);
                    throw new ImageWriteException(stringBuffer.toString());
                }
                binaryOutputStream.write(iPTCRecord.iptcType.type);
                byte[] bytes = iPTCRecord.value.getBytes("ISO-8859-1");
                if (new String(bytes, "ISO-8859-1").equals(iPTCRecord.value)) {
                    binaryOutputStream.write2Bytes(bytes.length);
                    binaryOutputStream.write(bytes);
                } else {
                    throw new ImageWriteException("Invalid record value, not ISO-8859-1");
                }
            }
        }
        return byteArrayOutputStream.toByteArray();
    }
}

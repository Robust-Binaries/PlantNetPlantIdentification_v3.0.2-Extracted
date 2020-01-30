package org.apache.sanselan.icc;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.common.BinaryInputStream;

public interface IccConstants {
    public static final IccTagType AToB0Tag = new IccTagType("AToB0Tag", "lut8Type or lut16Type or lutAtoBType", 1093812784);
    public static final IccTagType AToB1Tag = new IccTagType("AToB1Tag", "lut8Type or lut16Type or lutAtoBType", 1093812785);
    public static final IccTagType AToB2Tag = new IccTagType("AToB2Tag", "lut8Type or lut16Type or lutAtoBType", 1093812786);
    public static final IccTagType BToA0Tag = new IccTagType("BToA0Tag", "lut8Type or lut16Type or lutBtoAType", 1110589744);
    public static final IccTagType BToA1Tag = new IccTagType("BToA1Tag", "lut8Type or lut16Type or lutBtoAType", 1110589745);
    public static final IccTagType BToA2Tag = new IccTagType("BToA2Tag", "lut8Type or lut16Type or lutBtoAType", 1110589746);
    public static final int IEC = 1229275936;
    public static final IccTagDataType[] IccTagDataTypes = {descType, dataType, multiLocalizedUnicodeType, signatureType, textType};
    public static final IccTagType[] TagTypes = {AToB0Tag, AToB1Tag, AToB2Tag, blueMatrixColumnTag, blueTRCTag, BToA0Tag, BToA1Tag, BToA2Tag, calibrationDateTimeTag, charTargetTag, chromaticAdaptationTag, chromaticityTag, colorantOrderTag, colorantTableTag, copyrightTag, deviceMfgDescTag, deviceModelDescTag, gamutTag, grayTRCTag, greenMatrixColumnTag, greenTRCTag, luminanceTag, measurementTag, mediaBlackPointTag, mediaWhitePointTag, namedColor2Tag, outputResponseTag, preview0Tag, preview1Tag, preview2Tag, profileDescriptionTag, profileSequenceDescTag, redMatrixColumnTag, redTRCTag, technologyTag, viewingCondDescTag, viewingConditionsTag};
    public static final IccTagType blueMatrixColumnTag = new IccTagType("blueMatrixColumnTag", "XYZType", 1649957210);
    public static final IccTagType blueTRCTag = new IccTagType("blueTRCTag", "curveType or parametricCurveType", 1649693251);
    public static final IccTagType calibrationDateTimeTag = new IccTagType("calibrationDateTimeTag", "dateTimeType", 1667329140);
    public static final IccTagType charTargetTag = new IccTagType("charTargetTag", "textType", 1952543335);
    public static final IccTagType chromaticAdaptationTag = new IccTagType("chromaticAdaptationTag", "s15Fixed16ArrayType", 1667785060);
    public static final IccTagType chromaticityTag = new IccTagType("chromaticityTag", "chromaticityType", 1667789421);
    public static final IccTagType colorantOrderTag = new IccTagType("colorantOrderTag", "colorantOrderType", 1668051567);
    public static final IccTagType colorantTableTag = new IccTagType("colorantTableTag", "colorantTableType", 1668051572);
    public static final IccTagType copyrightTag = new IccTagType("copyrightTag", "multiLocalizedUnicodeType", 1668313716);
    public static final IccTagDataType dataType = new IccTagDataType("dataType", 1684108385) {
        public void dump(String str, byte[] bArr) throws ImageReadException, IOException {
            new BinaryInputStream((InputStream) new ByteArrayInputStream(bArr), 77).read4Bytes("type_signature", "ICC: corrupt tag data");
        }
    };
    public static final IccTagDataType descType = new IccTagDataType("descType", 1684370275) {
        public void dump(String str, byte[] bArr) throws ImageReadException, IOException {
            BinaryInputStream binaryInputStream = new BinaryInputStream((InputStream) new ByteArrayInputStream(bArr), 77);
            binaryInputStream.read4Bytes("type_signature", "ICC: corrupt tag data");
            binaryInputStream.read4Bytes("ignore", "ICC: corrupt tag data");
            String str2 = new String(bArr, 12, binaryInputStream.read4Bytes("string_length", "ICC: corrupt tag data") - 1);
            PrintStream printStream = System.out;
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(str);
            stringBuffer.append("s: '");
            stringBuffer.append(str2);
            stringBuffer.append("'");
            printStream.println(stringBuffer.toString());
        }
    };
    public static final IccTagType deviceMfgDescTag = new IccTagType("deviceMfgDescTag", "multiLocalizedUnicodeType", 1684893284);
    public static final IccTagType deviceModelDescTag = new IccTagType("deviceModelDescTag", "multiLocalizedUnicodeType", 1684890724);
    public static final IccTagType gamutTag = new IccTagType("gamutTag", "lut8Type or lut16Type or lutBtoAType", 1734438260);
    public static final IccTagType grayTRCTag = new IccTagType("grayTRCTag", "curveType or parametricCurveType", 1800688195);
    public static final IccTagType greenMatrixColumnTag = new IccTagType("greenMatrixColumnTag", "XYZType", 1733843290);
    public static final IccTagType greenTRCTag = new IccTagType("greenTRCTag", "curveType or parametricCurveType", 1733579331);
    public static final IccTagType luminanceTag = new IccTagType("luminanceTag", "XYZType", 1819635049);
    public static final IccTagType measurementTag = new IccTagType("measurementTag", "measurementType", 1835360627);
    public static final IccTagType mediaBlackPointTag = new IccTagType("mediaBlackPointTag", "XYZType", 1651208308);
    public static final IccTagType mediaWhitePointTag = new IccTagType("mediaWhitePointTag", "XYZType", 2004119668);
    public static final IccTagDataType multiLocalizedUnicodeType = new IccTagDataType("multiLocalizedUnicodeType", 1835824483) {
        public void dump(String str, byte[] bArr) throws ImageReadException, IOException {
            new BinaryInputStream((InputStream) new ByteArrayInputStream(bArr), 77).read4Bytes("type_signature", "ICC: corrupt tag data");
        }
    };
    public static final IccTagType namedColor2Tag = new IccTagType("namedColor2Tag", "namedColor2Type", 1852009522);
    public static final IccTagType outputResponseTag = new IccTagType("outputResponseTag", "responseCurveSet16Type", 1919251312);
    public static final IccTagType preview0Tag = new IccTagType("preview0Tag", "lut8Type or lut16Type or lutBtoAType", 1886545200);
    public static final IccTagType preview1Tag = new IccTagType("preview1Tag", "lut8Type or lut16Type or lutBtoAType", 1886545201);
    public static final IccTagType preview2Tag = new IccTagType("preview2Tag", "lut8Type or lut16Type or lutBtoAType", 1886545202);
    public static final IccTagType profileDescriptionTag = new IccTagType("profileDescriptionTag", "multiLocalizedUnicodeType", 1684370275);
    public static final IccTagType profileSequenceDescTag = new IccTagType("profileSequenceDescTag", "profileSequenceDescType", 1886610801);
    public static final IccTagType redMatrixColumnTag = new IccTagType("redMatrixColumnTag", "XYZType", 1918392666);
    public static final IccTagType redTRCTag = new IccTagType("redTRCTag", "curveType or parametricCurveType", 1918128707);
    public static final int sRGB = 1934772034;
    public static final IccTagDataType signatureType = new IccTagDataType("signatureType", 1936287520) {
        public void dump(String str, byte[] bArr) throws ImageReadException, IOException {
            BinaryInputStream binaryInputStream = new BinaryInputStream((InputStream) new ByteArrayInputStream(bArr), 77);
            binaryInputStream.read4Bytes("type_signature", "ICC: corrupt tag data");
            binaryInputStream.read4Bytes("ignore", "ICC: corrupt tag data");
            int read4Bytes = binaryInputStream.read4Bytes("thesignature ", "ICC: corrupt tag data");
            PrintStream printStream = System.out;
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(str);
            stringBuffer.append("thesignature: ");
            stringBuffer.append(Integer.toHexString(read4Bytes));
            stringBuffer.append(" (");
            stringBuffer.append(new String(new byte[]{(byte) ((read4Bytes >> 24) & 255), (byte) ((read4Bytes >> 16) & 255), (byte) ((read4Bytes >> 8) & 255), (byte) ((read4Bytes >> 0) & 255)}));
            stringBuffer.append(")");
            printStream.println(stringBuffer.toString());
        }
    };
    public static final IccTagType technologyTag = new IccTagType("technologyTag", "signatureType", 1952801640);
    public static final IccTagDataType textType = new IccTagDataType("textType", 1952807028) {
        public void dump(String str, byte[] bArr) throws ImageReadException, IOException {
            BinaryInputStream binaryInputStream = new BinaryInputStream((InputStream) new ByteArrayInputStream(bArr), 77);
            binaryInputStream.read4Bytes("type_signature", "ICC: corrupt tag data");
            binaryInputStream.read4Bytes("ignore", "ICC: corrupt tag data");
            String str2 = new String(bArr, 8, bArr.length - 8);
            PrintStream printStream = System.out;
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(str);
            stringBuffer.append("s: '");
            stringBuffer.append(str2);
            stringBuffer.append("'");
            printStream.println(stringBuffer.toString());
        }
    };
    public static final IccTagType viewingCondDescTag = new IccTagType("viewingCondDescTag", "multiLocalizedUnicodeType", 1987405156);
    public static final IccTagType viewingConditionsTag = new IccTagType("viewingConditionsTag", "viewingConditionsType", 1986618743);
}

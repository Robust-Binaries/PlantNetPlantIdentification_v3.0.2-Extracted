package org.apache.sanselan.formats.tiff;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.sanselan.FormatCompliance;
import org.apache.sanselan.ImageFormat;
import org.apache.sanselan.ImageInfo;
import org.apache.sanselan.ImageParser;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.common.byteSources.ByteSource;
import org.apache.sanselan.formats.tiff.TiffDirectory.ImageDataElement;
import org.apache.sanselan.formats.tiff.TiffImageMetadata.Directory;
import org.apache.sanselan.formats.tiff.constants.ExifTagConstants;
import org.apache.sanselan.formats.tiff.constants.TiffConstants;
import org.apache.sanselan.formats.tiff.photometricinterpreters.PhotometricInterpreter;
import org.apache.sanselan.formats.tiff.photometricinterpreters.PhotometricInterpreterBiLevel;
import org.apache.sanselan.formats.tiff.photometricinterpreters.PhotometricInterpreterCIELAB;
import org.apache.sanselan.formats.tiff.photometricinterpreters.PhotometricInterpreterCMYK;
import org.apache.sanselan.formats.tiff.photometricinterpreters.PhotometricInterpreterLogLUV;
import org.apache.sanselan.formats.tiff.photometricinterpreters.PhotometricInterpreterPalette;
import org.apache.sanselan.formats.tiff.photometricinterpreters.PhotometricInterpreterRGB;
import org.apache.sanselan.formats.tiff.photometricinterpreters.PhotometricInterpreterYCbCr;
import org.apache.sanselan.formats.tiff.write.TiffImageWriterLossy;

public class TiffImageParser extends ImageParser implements TiffConstants {
    private static final String[] ACCEPTED_EXTENSIONS = {DEFAULT_EXTENSION, ".tiff"};
    private static final String DEFAULT_EXTENSION = ".tif";

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
        return "Tiff-Custom";
    }

    /* access modifiers changed from: protected */
    public String[] getAcceptedExtensions() {
        return ACCEPTED_EXTENSIONS;
    }

    /* access modifiers changed from: protected */
    public ImageFormat[] getAcceptedTypes() {
        return new ImageFormat[]{ImageFormat.IMAGE_FORMAT_TIFF};
    }

    public byte[] getICCProfileBytes(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        TiffField findField = ((TiffDirectory) new TiffReader(isStrict(map)).readFirstDirectory(byteSource, map, false, FormatCompliance.getDefault()).directories.get(0)).findField(EXIF_TAG_ICC_PROFILE);
        if (findField == null) {
            return null;
        }
        return findField.oversizeValue;
    }

    public Dimension getImageSize(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        TiffDirectory tiffDirectory = (TiffDirectory) new TiffReader(isStrict(map)).readFirstDirectory(byteSource, map, false, FormatCompliance.getDefault()).directories.get(0);
        return new Dimension(tiffDirectory.findField(TIFF_TAG_IMAGE_WIDTH).getIntValue(), tiffDirectory.findField(TIFF_TAG_IMAGE_LENGTH).getIntValue());
    }

    public IImageMetadata getMetadata(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        TiffContents readContents = new TiffReader(isStrict(map)).readContents(byteSource, map, FormatCompliance.getDefault());
        ArrayList arrayList = readContents.directories;
        TiffImageMetadata tiffImageMetadata = new TiffImageMetadata(readContents);
        for (int i = 0; i < arrayList.size(); i++) {
            TiffDirectory tiffDirectory = (TiffDirectory) arrayList.get(i);
            Directory directory = new Directory(tiffDirectory);
            ArrayList directoryEntrys = tiffDirectory.getDirectoryEntrys();
            for (int i2 = 0; i2 < directoryEntrys.size(); i2++) {
                directory.add((TiffField) directoryEntrys.get(i2));
            }
            tiffImageMetadata.add(directory);
        }
        return tiffImageMetadata;
    }

    public ImageInfo getImageInfo(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        float f;
        int i;
        float f2;
        int i2;
        String str;
        float f3;
        int i3;
        TiffContents readDirectories = new TiffReader(isStrict(map)).readDirectories(byteSource, false, FormatCompliance.getDefault());
        TiffDirectory tiffDirectory = (TiffDirectory) readDirectories.directories.get(0);
        TiffField findField = tiffDirectory.findField(TIFF_TAG_IMAGE_WIDTH, true);
        TiffField findField2 = tiffDirectory.findField(TIFF_TAG_IMAGE_LENGTH, true);
        if (findField == null || findField2 == null) {
            throw new ImageReadException("TIFF image missing size info.");
        }
        int intValue = findField2.getIntValue();
        int intValue2 = findField.getIntValue();
        TiffField findField3 = tiffDirectory.findField(TIFF_TAG_RESOLUTION_UNIT);
        int i4 = 2;
        if (!(findField3 == null || findField3.getValue() == null)) {
            i4 = findField3.getIntValue();
        }
        double d = -1.0d;
        switch (i4) {
            case 2:
                d = 1.0d;
                break;
            case 3:
                d = 0.0254d;
                break;
        }
        TiffField findField4 = tiffDirectory.findField(TIFF_TAG_XRESOLUTION);
        TiffField findField5 = tiffDirectory.findField(TIFF_TAG_YRESOLUTION);
        if (d > 0.0d) {
            if (findField4 == null || findField4.getValue() == null) {
                i3 = -1;
                f3 = -1.0f;
            } else {
                double doubleValue = findField4.getDoubleValue();
                i3 = (int) (doubleValue / d);
                double d2 = (double) intValue2;
                double d3 = doubleValue * d;
                Double.isNaN(d2);
                f3 = (float) (d2 / d3);
            }
            if (findField5 == null || findField5.getValue() == null) {
                i = i3;
                f = f3;
                i2 = -1;
                f2 = -1.0f;
            } else {
                double doubleValue2 = findField5.getDoubleValue();
                int i5 = (int) (doubleValue2 / d);
                double d4 = (double) intValue;
                double d5 = doubleValue2 * d;
                Double.isNaN(d4);
                i = i3;
                i2 = i5;
                f2 = (float) (d4 / d5);
                f = f3;
            }
        } else {
            i2 = -1;
            f2 = -1.0f;
            i = -1;
            f = -1.0f;
        }
        TiffField findField6 = tiffDirectory.findField(TIFF_TAG_BITS_PER_SAMPLE);
        int intValueOrArraySum = (findField6 == null || findField6.getValue() == null) ? -1 : findField6.getIntValueOrArraySum();
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = tiffDirectory.entries;
        for (int i6 = 0; i6 < arrayList2.size(); i6++) {
            arrayList.add(((TiffField) arrayList2.get(i6)).toString());
        }
        ImageFormat imageFormat = ImageFormat.IMAGE_FORMAT_TIFF;
        String str2 = "TIFF Tag-based Image File Format";
        String str3 = "image/tiff";
        int size = readDirectories.directories.size();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Tiff v.");
        stringBuffer.append(readDirectories.header.tiffVersion);
        String stringBuffer2 = stringBuffer.toString();
        boolean z = tiffDirectory.findField(TIFF_TAG_COLOR_MAP) != null;
        int intValue3 = tiffDirectory.findField(TIFF_TAG_COMPRESSION).getIntValue();
        if (intValue3 == 32771) {
            str = ImageInfo.COMPRESSION_ALGORITHM_NONE;
        } else if (intValue3 != 32773) {
            switch (intValue3) {
                case 1:
                    str = ImageInfo.COMPRESSION_ALGORITHM_NONE;
                    break;
                case 2:
                    str = ImageInfo.COMPRESSION_ALGORITHM_CCITT_1D;
                    break;
                case 3:
                    str = ImageInfo.COMPRESSION_ALGORITHM_CCITT_GROUP_3;
                    break;
                case 4:
                    str = ImageInfo.COMPRESSION_ALGORITHM_CCITT_GROUP_4;
                    break;
                case 5:
                    str = ImageInfo.COMPRESSION_ALGORITHM_LZW;
                    break;
                case 6:
                    str = ImageInfo.COMPRESSION_ALGORITHM_JPEG;
                    break;
                default:
                    str = ImageInfo.COMPRESSION_ALGORITHM_UNKNOWN;
                    break;
            }
        } else {
            str = ImageInfo.COMPRESSION_ALGORITHM_PACKBITS;
        }
        ImageInfo imageInfo = new ImageInfo(stringBuffer2, intValueOrArraySum, arrayList, imageFormat, str2, intValue, str3, size, i2, f2, i, f, intValue2, false, false, z, 2, str);
        return imageInfo;
    }

    public String getXmpXml(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        TiffField findField = ((TiffDirectory) new TiffReader(isStrict(map)).readDirectories(byteSource, false, FormatCompliance.getDefault()).directories.get(0)).findField(TIFF_TAG_XMP, false);
        if (findField == null) {
            return null;
        }
        try {
            return new String(findField.getByteArrayValue(), "utf-8");
        } catch (UnsupportedEncodingException unused) {
            throw new ImageReadException("Invalid JPEG XMP Segment.");
        }
    }

    public boolean dumpImageFile(PrintWriter printWriter, ByteSource byteSource) throws ImageReadException, IOException {
        try {
            printWriter.println("tiff.dumpImageFile");
            ImageInfo imageInfo = getImageInfo(byteSource);
            if (imageInfo != null) {
                imageInfo.toString(printWriter, "");
                printWriter.println("");
                ArrayList arrayList = new TiffReader(true).readContents(byteSource, null, FormatCompliance.getDefault()).directories;
                if (arrayList != null) {
                    int i = 0;
                    while (i < arrayList.size()) {
                        ArrayList arrayList2 = ((TiffDirectory) arrayList.get(i)).entries;
                        if (arrayList2 != null) {
                            for (int i2 = 0; i2 < arrayList2.size(); i2++) {
                                TiffField tiffField = (TiffField) arrayList2.get(i2);
                                StringBuffer stringBuffer = new StringBuffer();
                                stringBuffer.append(i);
                                stringBuffer.append("");
                                tiffField.dump(printWriter, stringBuffer.toString());
                            }
                            i++;
                        }
                    }
                    printWriter.println("");
                    printWriter.println("");
                    return true;
                }
            }
            return false;
        } finally {
            printWriter.println("");
        }
    }

    public FormatCompliance getFormatCompliance(ByteSource byteSource) throws ImageReadException, IOException {
        FormatCompliance formatCompliance = FormatCompliance.getDefault();
        new TiffReader(isStrict(null)).readContents(byteSource, null, formatCompliance);
        return formatCompliance;
    }

    public List collectRawImageData(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        TiffContents readDirectories = new TiffReader(isStrict(map)).readDirectories(byteSource, true, FormatCompliance.getDefault());
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < readDirectories.directories.size(); i++) {
            ArrayList tiffRawImageDataElements = ((TiffDirectory) readDirectories.directories.get(i)).getTiffRawImageDataElements();
            for (int i2 = 0; i2 < tiffRawImageDataElements.size(); i2++) {
                ImageDataElement imageDataElement = (ImageDataElement) tiffRawImageDataElements.get(i2);
                arrayList.add(byteSource.getBlock(imageDataElement.offset, imageDataElement.length));
            }
        }
        return arrayList;
    }

    public BufferedImage getBufferedImage(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        BufferedImage tiffImage = ((TiffDirectory) new TiffReader(isStrict(map)).readFirstDirectory(byteSource, map, true, FormatCompliance.getDefault()).directories.get(0)).getTiffImage(map);
        if (tiffImage != null) {
            return tiffImage;
        }
        throw new ImageReadException("TIFF does not contain an image.");
    }

    /* access modifiers changed from: protected */
    public BufferedImage getBufferedImage(TiffDirectory tiffDirectory, Map map) throws ImageReadException, IOException {
        TiffDirectory tiffDirectory2 = tiffDirectory;
        ArrayList arrayList = tiffDirectory2.entries;
        if (arrayList != null) {
            int intValue = tiffDirectory2.findField(TIFF_TAG_PHOTOMETRIC_INTERPRETATION, true).getIntValue();
            int intValue2 = tiffDirectory2.findField(TIFF_TAG_COMPRESSION, true).getIntValue();
            int intValue3 = tiffDirectory2.findField(TIFF_TAG_IMAGE_WIDTH, true).getIntValue();
            int intValue4 = tiffDirectory2.findField(TIFF_TAG_IMAGE_LENGTH, true).getIntValue();
            int intValue5 = tiffDirectory2.findField(TIFF_TAG_SAMPLES_PER_PIXEL, true).getIntValue();
            int[] intArrayValue = tiffDirectory2.findField(TIFF_TAG_BITS_PER_SAMPLE, true).getIntArrayValue();
            int intValueOrArraySum = tiffDirectory2.findField(TIFF_TAG_BITS_PER_SAMPLE, true).getIntValueOrArraySum();
            TiffField findField = tiffDirectory2.findField(TIFF_TAG_PREDICTOR);
            int intValueOrArraySum2 = findField != null ? findField.getIntValueOrArraySum() : -1;
            if (intValue5 == intArrayValue.length) {
                int i = intValueOrArraySum;
                int[] iArr = intArrayValue;
                int i2 = intValueOrArraySum2;
                int i3 = intValue5;
                BufferedImage colorBufferedImage = getBufferedImageFactory(map).getColorBufferedImage(intValue3, intValue4, false);
                int i4 = intValue3;
                int i5 = intValue4;
                PhotometricInterpreter photometricInterpreter = getPhotometricInterpreter(tiffDirectory, intValue, i, iArr, i2, i3, i4, i5);
                BufferedImage bufferedImage = colorBufferedImage;
                tiffDirectory.getTiffImageData().getDataReader(arrayList, photometricInterpreter, i, iArr, i2, i3, i4, i5, intValue2).readImageData(bufferedImage);
                photometricInterpreter.dumpstats();
                return bufferedImage;
            }
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("Tiff: samplesPerPixel (");
            stringBuffer.append(intValue5);
            stringBuffer.append(")!=fBitsPerSample.length (");
            stringBuffer.append(intArrayValue.length);
            stringBuffer.append(")");
            throw new ImageReadException(stringBuffer.toString());
        }
        throw new ImageReadException("TIFF missing entries");
    }

    private PhotometricInterpreter getPhotometricInterpreter(TiffDirectory tiffDirectory, int i, int i2, int[] iArr, int i3, int i4, int i5, int i6) throws IOException, ImageReadException {
        TiffDirectory tiffDirectory2 = tiffDirectory;
        int i7 = i;
        switch (i7) {
            case 0:
            case 1:
                PhotometricInterpreterBiLevel photometricInterpreterBiLevel = new PhotometricInterpreterBiLevel(i2, i4, iArr, i3, i5, i6, i7 == 0);
                return photometricInterpreterBiLevel;
            case 2:
                PhotometricInterpreterRGB photometricInterpreterRGB = new PhotometricInterpreterRGB(i4, iArr, i3, i5, i6);
                return photometricInterpreterRGB;
            case 3:
                int[] intArrayValue = tiffDirectory2.findField(TIFF_TAG_COLOR_MAP, true).getIntArrayValue();
                int i8 = (1 << i2) * 3;
                if (intArrayValue.length == i8) {
                    PhotometricInterpreterPalette photometricInterpreterPalette = new PhotometricInterpreterPalette(i4, iArr, i3, i5, i6, intArrayValue);
                    return photometricInterpreterPalette;
                }
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("Tiff: fColorMap.length (");
                stringBuffer.append(intArrayValue.length);
                stringBuffer.append(")!=expected_colormap_size (");
                stringBuffer.append(i8);
                stringBuffer.append(")");
                throw new ImageReadException(stringBuffer.toString());
            case 5:
                PhotometricInterpreterCMYK photometricInterpreterCMYK = new PhotometricInterpreterCMYK(i4, iArr, i3, i5, i6);
                return photometricInterpreterCMYK;
            case 6:
                PhotometricInterpreterYCbCr photometricInterpreterYCbCr = new PhotometricInterpreterYCbCr(tiffDirectory2.findField(TIFF_TAG_YCBCR_COEFFICIENTS, true).getDoubleArrayValue(), tiffDirectory2.findField(TIFF_TAG_YCBCR_POSITIONING, true).getIntArrayValue(), tiffDirectory2.findField(TIFF_TAG_YCBCR_SUB_SAMPLING, true).getIntArrayValue(), tiffDirectory2.findField(TIFF_TAG_REFERENCE_BLACK_WHITE, true).getDoubleArrayValue(), i4, iArr, i3, i5, i6);
                return photometricInterpreterYCbCr;
            case 8:
                PhotometricInterpreterCIELAB photometricInterpreterCIELAB = new PhotometricInterpreterCIELAB(i4, iArr, i3, i5, i6);
                return photometricInterpreterCIELAB;
            case ExifTagConstants.PHOTOMETRIC_INTERPRETATION_VALUE_PIXAR_LOG_L /*32844*/:
            case ExifTagConstants.PHOTOMETRIC_INTERPRETATION_VALUE_PIXAR_LOG_LUV /*32845*/:
                PhotometricInterpreterLogLUV photometricInterpreterLogLUV = new PhotometricInterpreterLogLUV(i4, iArr, i3, i5, i6, i7 == 32844);
                return photometricInterpreterLogLUV;
            default:
                StringBuffer stringBuffer2 = new StringBuffer();
                stringBuffer2.append("TIFF: Unknown fPhotometricInterpretation: ");
                stringBuffer2.append(i7);
                throw new ImageReadException(stringBuffer2.toString());
        }
    }

    public void writeImage(BufferedImage bufferedImage, OutputStream outputStream, Map map) throws ImageWriteException, IOException {
        new TiffImageWriterLossy().writeImage(bufferedImage, outputStream, map);
    }
}

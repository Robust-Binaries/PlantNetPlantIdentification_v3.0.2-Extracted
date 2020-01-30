package org.apache.sanselan.formats.tiff.write;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.SanselanConstants;
import org.apache.sanselan.common.BinaryConstants;
import org.apache.sanselan.common.BinaryOutputStream;
import org.apache.sanselan.common.PackBits;
import org.apache.sanselan.common.mylzw.MyLZWCompressor;
import org.apache.sanselan.formats.tiff.TiffElement.DataElement;
import org.apache.sanselan.formats.tiff.TiffImageData.Data;
import org.apache.sanselan.formats.tiff.TiffImageData.Strips;
import org.apache.sanselan.formats.tiff.constants.TiffConstants;

public abstract class TiffImageWriterBase implements TiffConstants, BinaryConstants {
    protected final int byteOrder;

    public abstract void write(OutputStream outputStream, TiffOutputSet tiffOutputSet) throws IOException, ImageWriteException;

    public TiffImageWriterBase() {
        this.byteOrder = 73;
    }

    public TiffImageWriterBase(int i) {
        this.byteOrder = i;
    }

    protected static final int imageDataPaddingLength(int i) {
        return (4 - (i % 4)) % 4;
    }

    /* access modifiers changed from: protected */
    public TiffOutputSummary validateDirectories(TiffOutputSet tiffOutputSet) throws ImageWriteException {
        List directories = tiffOutputSet.getDirectories();
        if (1 <= directories.size()) {
            ArrayList arrayList = new ArrayList();
            HashMap hashMap = new HashMap();
            int i = 0;
            TiffOutputDirectory tiffOutputDirectory = null;
            TiffOutputDirectory tiffOutputDirectory2 = null;
            TiffOutputDirectory tiffOutputDirectory3 = null;
            TiffOutputField tiffOutputField = null;
            TiffOutputField tiffOutputField2 = null;
            TiffOutputField tiffOutputField3 = null;
            while (i < directories.size()) {
                TiffOutputDirectory tiffOutputDirectory4 = (TiffOutputDirectory) directories.get(i);
                int i2 = tiffOutputDirectory4.type;
                Integer num = new Integer(i2);
                hashMap.put(num, tiffOutputDirectory4);
                if (i2 < 0) {
                    switch (i2) {
                        case -4:
                            if (tiffOutputDirectory == null) {
                                tiffOutputDirectory = tiffOutputDirectory4;
                                break;
                            } else {
                                throw new ImageWriteException("More than one Interoperability directory.");
                            }
                        case -3:
                            if (tiffOutputDirectory2 == null) {
                                tiffOutputDirectory2 = tiffOutputDirectory4;
                                break;
                            } else {
                                throw new ImageWriteException("More than one GPS directory.");
                            }
                        case -2:
                            if (tiffOutputDirectory3 == null) {
                                tiffOutputDirectory3 = tiffOutputDirectory4;
                                break;
                            } else {
                                throw new ImageWriteException("More than one EXIF directory.");
                            }
                        default:
                            StringBuffer stringBuffer = new StringBuffer();
                            stringBuffer.append("Unknown directory: ");
                            stringBuffer.append(i2);
                            throw new ImageWriteException(stringBuffer.toString());
                    }
                } else if (!arrayList.contains(num)) {
                    arrayList.add(new Integer(i2));
                } else {
                    StringBuffer stringBuffer2 = new StringBuffer();
                    stringBuffer2.append("More than one directory with index: ");
                    stringBuffer2.append(i2);
                    stringBuffer2.append(".");
                    throw new ImageWriteException(stringBuffer2.toString());
                }
                HashSet hashSet = new HashSet();
                ArrayList fields = tiffOutputDirectory4.getFields();
                TiffOutputField tiffOutputField4 = tiffOutputField3;
                TiffOutputField tiffOutputField5 = tiffOutputField2;
                TiffOutputField tiffOutputField6 = tiffOutputField;
                int i3 = 0;
                while (i3 < fields.size()) {
                    TiffOutputField tiffOutputField7 = (TiffOutputField) fields.get(i3);
                    List list = directories;
                    Integer num2 = new Integer(tiffOutputField7.tag);
                    if (!hashSet.contains(num2)) {
                        hashSet.add(num2);
                        if (tiffOutputField7.tag == EXIF_TAG_EXIF_OFFSET.tag) {
                            if (tiffOutputField5 == null) {
                                tiffOutputField5 = tiffOutputField7;
                            } else {
                                throw new ImageWriteException("More than one Exif directory offset field.");
                            }
                        } else if (tiffOutputField7.tag == EXIF_TAG_INTEROP_OFFSET.tag) {
                            if (tiffOutputField6 == null) {
                                tiffOutputField6 = tiffOutputField7;
                            } else {
                                throw new ImageWriteException("More than one Interoperability directory offset field.");
                            }
                        } else if (tiffOutputField7.tag != EXIF_TAG_GPSINFO.tag) {
                            continue;
                        } else if (tiffOutputField4 == null) {
                            tiffOutputField4 = tiffOutputField7;
                        } else {
                            throw new ImageWriteException("More than one GPS directory offset field.");
                        }
                        i3++;
                        directories = list;
                    } else {
                        StringBuffer stringBuffer3 = new StringBuffer();
                        stringBuffer3.append("Tag (");
                        stringBuffer3.append(tiffOutputField7.tagInfo.getDescription());
                        stringBuffer3.append(") appears twice in directory.");
                        throw new ImageWriteException(stringBuffer3.toString());
                    }
                }
                List list2 = directories;
                i++;
                tiffOutputField = tiffOutputField6;
                tiffOutputField2 = tiffOutputField5;
                tiffOutputField3 = tiffOutputField4;
            }
            if (arrayList.size() >= 1) {
                Collections.sort(arrayList);
                int i4 = 0;
                TiffOutputDirectory tiffOutputDirectory5 = null;
                while (i4 < arrayList.size()) {
                    Integer num3 = (Integer) arrayList.get(i4);
                    if (num3.intValue() == i4) {
                        TiffOutputDirectory tiffOutputDirectory6 = (TiffOutputDirectory) hashMap.get(num3);
                        if (tiffOutputDirectory5 != null) {
                            tiffOutputDirectory5.setNextDirectory(tiffOutputDirectory6);
                        }
                        i4++;
                        tiffOutputDirectory5 = tiffOutputDirectory6;
                    } else {
                        StringBuffer stringBuffer4 = new StringBuffer();
                        stringBuffer4.append("Missing directory: ");
                        stringBuffer4.append(i4);
                        stringBuffer4.append(".");
                        throw new ImageWriteException(stringBuffer4.toString());
                    }
                }
                TiffOutputDirectory tiffOutputDirectory7 = (TiffOutputDirectory) hashMap.get(new Integer(0));
                TiffOutputSummary tiffOutputSummary = new TiffOutputSummary(this.byteOrder, tiffOutputDirectory7, hashMap);
                if (tiffOutputDirectory != null || tiffOutputField == null) {
                    if (tiffOutputDirectory != null) {
                        if (tiffOutputDirectory3 == null) {
                            tiffOutputDirectory3 = tiffOutputSet.addExifDirectory();
                        }
                        if (tiffOutputField == null) {
                            tiffOutputField = TiffOutputField.createOffsetField(EXIF_TAG_INTEROP_OFFSET, this.byteOrder);
                            tiffOutputDirectory3.add(tiffOutputField);
                        }
                        tiffOutputSummary.add(tiffOutputDirectory, tiffOutputField);
                    }
                    if (tiffOutputDirectory3 != null || tiffOutputField2 == null) {
                        if (tiffOutputDirectory3 != null) {
                            if (tiffOutputField2 == null) {
                                tiffOutputField2 = TiffOutputField.createOffsetField(EXIF_TAG_EXIF_OFFSET, this.byteOrder);
                                tiffOutputDirectory7.add(tiffOutputField2);
                            }
                            tiffOutputSummary.add(tiffOutputDirectory3, tiffOutputField2);
                        }
                        if (tiffOutputDirectory2 != null || tiffOutputField3 == null) {
                            if (tiffOutputDirectory2 != null) {
                                if (tiffOutputField3 == null) {
                                    tiffOutputField3 = TiffOutputField.createOffsetField(EXIF_TAG_GPSINFO, this.byteOrder);
                                    tiffOutputDirectory7.add(tiffOutputField3);
                                }
                                tiffOutputSummary.add(tiffOutputDirectory2, tiffOutputField3);
                            }
                            return tiffOutputSummary;
                        }
                        throw new ImageWriteException("Output set has GPS Directory Offset field, but no GPS Directory");
                    }
                    throw new ImageWriteException("Output set has Exif Directory Offset field, but no Exif Directory");
                }
                throw new ImageWriteException("Output set has Interoperability Directory Offset field, but no Interoperability Directory");
            }
            throw new ImageWriteException("Missing root directory.");
        }
        throw new ImageWriteException("No directories.");
    }

    public void writeImage(BufferedImage bufferedImage, OutputStream outputStream, Map map) throws ImageWriteException, IOException {
        int i;
        HashMap hashMap = new HashMap(map);
        if (hashMap.containsKey(SanselanConstants.PARAM_KEY_FORMAT)) {
            hashMap.remove(SanselanConstants.PARAM_KEY_FORMAT);
        }
        String str = null;
        if (hashMap.containsKey(SanselanConstants.PARAM_KEY_XMP_XML)) {
            str = (String) hashMap.get(SanselanConstants.PARAM_KEY_XMP_XML);
            hashMap.remove(SanselanConstants.PARAM_KEY_XMP_XML);
        }
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        if (hashMap.containsKey(SanselanConstants.PARAM_KEY_COMPRESSION)) {
            Object obj = hashMap.get(SanselanConstants.PARAM_KEY_COMPRESSION);
            if (obj == null) {
                i = 5;
            } else if (obj instanceof Number) {
                i = ((Number) obj).intValue();
            } else {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("Invalid compression parameter: ");
                stringBuffer.append(obj);
                throw new ImageWriteException(stringBuffer.toString());
            }
            hashMap.remove(SanselanConstants.PARAM_KEY_COMPRESSION);
        } else {
            i = 5;
        }
        int max = Math.max(1, 8000 / (width * 3));
        byte[][] strips = getStrips(bufferedImage, 3, 8, max);
        if (hashMap.size() <= 0) {
            if (i == 32773) {
                for (int i2 = 0; i2 < strips.length; i2++) {
                    strips[i2] = new PackBits().compress(strips[i2]);
                }
            } else if (i == 5) {
                for (int i3 = 0; i3 < strips.length; i3++) {
                    strips[i3] = new MyLZWCompressor(8, 77, true).compress(strips[i3]);
                }
            } else if (i != 1) {
                throw new ImageWriteException("Invalid compression parameter (Only LZW, Packbits and uncompressed supported).");
            }
            DataElement[] dataElementArr = new DataElement[strips.length];
            for (int i4 = 0; i4 < strips.length; i4++) {
                dataElementArr[i4] = new Data(0, strips[i4].length, strips[i4]);
            }
            TiffOutputSet tiffOutputSet = new TiffOutputSet(this.byteOrder);
            TiffOutputDirectory addRootDirectory = tiffOutputSet.addRootDirectory();
            addRootDirectory.add(new TiffOutputField(TIFF_TAG_IMAGE_WIDTH, FIELD_TYPE_LONG, 1, FIELD_TYPE_LONG.writeData(new int[]{width}, this.byteOrder)));
            addRootDirectory.add(new TiffOutputField(TIFF_TAG_IMAGE_LENGTH, FIELD_TYPE_LONG, 1, FIELD_TYPE_LONG.writeData(new int[]{height}, this.byteOrder)));
            addRootDirectory.add(new TiffOutputField(TIFF_TAG_PHOTOMETRIC_INTERPRETATION, FIELD_TYPE_SHORT, 1, FIELD_TYPE_SHORT.writeData(new int[]{2}, this.byteOrder)));
            addRootDirectory.add(new TiffOutputField(TIFF_TAG_COMPRESSION, FIELD_TYPE_SHORT, 1, FIELD_TYPE_SHORT.writeData(new int[]{i}, this.byteOrder)));
            addRootDirectory.add(new TiffOutputField(TIFF_TAG_SAMPLES_PER_PIXEL, FIELD_TYPE_SHORT, 1, FIELD_TYPE_SHORT.writeData(new int[]{3}, this.byteOrder)));
            addRootDirectory.add(new TiffOutputField(TIFF_TAG_BITS_PER_SAMPLE, FIELD_TYPE_SHORT, 3, FIELD_TYPE_SHORT.writeData(new int[]{8, 8, 8}, this.byteOrder)));
            addRootDirectory.add(new TiffOutputField(TIFF_TAG_ROWS_PER_STRIP, FIELD_TYPE_LONG, 1, FIELD_TYPE_LONG.writeData(new int[]{max}, this.byteOrder)));
            addRootDirectory.add(new TiffOutputField(TIFF_TAG_RESOLUTION_UNIT, FIELD_TYPE_SHORT, 1, FIELD_TYPE_SHORT.writeData(new int[]{2}, this.byteOrder)));
            addRootDirectory.add(new TiffOutputField(TIFF_TAG_XRESOLUTION, FIELD_TYPE_RATIONAL, 1, FIELD_TYPE_RATIONAL.writeData(72, 1, this.byteOrder)));
            addRootDirectory.add(new TiffOutputField(TIFF_TAG_YRESOLUTION, FIELD_TYPE_RATIONAL, 1, FIELD_TYPE_RATIONAL.writeData(72, 1, this.byteOrder)));
            if (str != null) {
                byte[] bytes = str.getBytes("utf-8");
                addRootDirectory.add(new TiffOutputField(TIFF_TAG_XMP, FIELD_TYPE_BYTE, bytes.length, bytes));
            }
            addRootDirectory.setTiffImageData(new Strips(dataElementArr, max));
            write(outputStream, tiffOutputSet);
            return;
        }
        Object next = hashMap.keySet().iterator().next();
        StringBuffer stringBuffer2 = new StringBuffer();
        stringBuffer2.append("Unknown parameter: ");
        stringBuffer2.append(next);
        throw new ImageWriteException(stringBuffer2.toString());
    }

    private byte[][] getStrips(BufferedImage bufferedImage, int i, int i2, int i3) {
        int i4 = i3;
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        int i5 = ((height + i4) - 1) / i4;
        byte[][] bArr = null;
        byte[][] bArr2 = new byte[i5][];
        int i6 = height;
        int i7 = 0;
        while (i7 < i5) {
            int min = Math.min(i4, i6);
            i6 -= min;
            byte[] bArr3 = new byte[(((((min * i2) * width) * i) + 7) / 8)];
            int i8 = i7 * i4;
            int i9 = i8 + i4;
            int i10 = 0;
            while (i8 < height && i8 < i9) {
                int i11 = i10;
                int i12 = 0;
                while (i12 < width) {
                    int rgb = bufferedImage.getRGB(i12, i8);
                    int i13 = (rgb >> 8) & 255;
                    int i14 = (rgb >> 0) & 255;
                    int i15 = i11 + 1;
                    bArr3[i11] = (byte) ((rgb >> 16) & 255);
                    int i16 = i15 + 1;
                    bArr3[i15] = (byte) i13;
                    int i17 = i16 + 1;
                    bArr3[i16] = (byte) i14;
                    i12++;
                    i11 = i17;
                    int i18 = i3;
                }
                BufferedImage bufferedImage2 = bufferedImage;
                i8++;
                i10 = i11;
                int i19 = i3;
            }
            BufferedImage bufferedImage3 = bufferedImage;
            bArr2[i7] = bArr3;
            i7++;
            i4 = i3;
        }
        return bArr2;
    }

    /* access modifiers changed from: protected */
    public void writeImageFileHeader(BinaryOutputStream binaryOutputStream) throws IOException, ImageWriteException {
        writeImageFileHeader(binaryOutputStream, 8);
    }

    /* access modifiers changed from: protected */
    public void writeImageFileHeader(BinaryOutputStream binaryOutputStream, int i) throws IOException, ImageWriteException {
        binaryOutputStream.write(this.byteOrder);
        binaryOutputStream.write(this.byteOrder);
        binaryOutputStream.write2Bytes(42);
        binaryOutputStream.write4Bytes(i);
    }
}

package org.apache.sanselan.formats.tiff;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.common.IImageMetadata.IImageMetadataItem;
import org.apache.sanselan.common.ImageMetadata;
import org.apache.sanselan.common.RationalNumber;
import org.apache.sanselan.formats.tiff.constants.TagInfo;
import org.apache.sanselan.formats.tiff.constants.TagInfo.Offset;
import org.apache.sanselan.formats.tiff.constants.TiffConstants;
import org.apache.sanselan.formats.tiff.constants.TiffDirectoryConstants;
import org.apache.sanselan.formats.tiff.fieldtypes.FieldType;
import org.apache.sanselan.formats.tiff.write.TiffOutputDirectory;
import org.apache.sanselan.formats.tiff.write.TiffOutputField;
import org.apache.sanselan.formats.tiff.write.TiffOutputSet;

public class TiffImageMetadata extends ImageMetadata implements TiffDirectoryConstants {
    public final TiffContents contents;

    public static class Directory extends ImageMetadata implements IImageMetadataItem {
        /* access modifiers changed from: private */
        public final TiffDirectory directory;
        public final int type;

        public Directory(TiffDirectory tiffDirectory) {
            this.type = tiffDirectory.type;
            this.directory = tiffDirectory;
        }

        public void add(TiffField tiffField) {
            add(new Item(tiffField));
        }

        public BufferedImage getThumbnail() throws ImageReadException, IOException {
            return this.directory.getTiffImage();
        }

        public TiffImageData getTiffImageData() {
            return this.directory.getTiffImageData();
        }

        public TiffField findField(TagInfo tagInfo) throws ImageReadException {
            return this.directory.findField(tagInfo);
        }

        public List getAllFields() throws ImageReadException {
            return this.directory.getDirectoryEntrys();
        }

        public JpegImageData getJpegImageData() {
            return this.directory.getJpegImageData();
        }

        public String toString(String str) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(str != null ? str : "");
            stringBuffer.append(this.directory.description());
            stringBuffer.append(": ");
            stringBuffer.append(getTiffImageData() != null ? " (tiffImageData)" : "");
            stringBuffer.append(getJpegImageData() != null ? " (jpegImageData)" : "");
            stringBuffer.append(ReactEditTextInputConnectionWrapper.NEWLINE_RAW_VALUE);
            stringBuffer.append(super.toString(str));
            stringBuffer.append(ReactEditTextInputConnectionWrapper.NEWLINE_RAW_VALUE);
            return stringBuffer.toString();
        }

        public TiffOutputDirectory getOutputDirectory(int i) throws ImageWriteException {
            try {
                TiffOutputDirectory tiffOutputDirectory = new TiffOutputDirectory(this.type);
                ArrayList items = getItems();
                for (int i2 = 0; i2 < items.size(); i2++) {
                    TiffField tiffField = ((Item) items.get(i2)).getTiffField();
                    if (tiffOutputDirectory.findField(tiffField.tag) == null) {
                        if (!(tiffField.tagInfo instanceof Offset)) {
                            TagInfo tagInfo = tiffField.tagInfo;
                            FieldType fieldType = tiffField.fieldType;
                            TiffOutputField tiffOutputField = new TiffOutputField(tiffField.tag, tagInfo, fieldType, tiffField.length, tagInfo.encodeValue(fieldType, tiffField.getValue(), i));
                            tiffOutputField.setSortHint(tiffField.getSortHint());
                            tiffOutputDirectory.add(tiffOutputField);
                        }
                    }
                }
                tiffOutputDirectory.setTiffImageData(getTiffImageData());
                tiffOutputDirectory.setJpegImageData(getJpegImageData());
                return tiffOutputDirectory;
            } catch (ImageReadException e) {
                throw new ImageWriteException(e.getMessage(), e);
            }
        }
    }

    public static class GPSInfo {
        public final RationalNumber latitudeDegrees;
        public final RationalNumber latitudeMinutes;
        public final String latitudeRef;
        public final RationalNumber latitudeSeconds;
        public final RationalNumber longitudeDegrees;
        public final RationalNumber longitudeMinutes;
        public final String longitudeRef;
        public final RationalNumber longitudeSeconds;

        public GPSInfo(String str, String str2, RationalNumber rationalNumber, RationalNumber rationalNumber2, RationalNumber rationalNumber3, RationalNumber rationalNumber4, RationalNumber rationalNumber5, RationalNumber rationalNumber6) {
            this.latitudeRef = str;
            this.longitudeRef = str2;
            this.latitudeDegrees = rationalNumber;
            this.latitudeMinutes = rationalNumber2;
            this.latitudeSeconds = rationalNumber3;
            this.longitudeDegrees = rationalNumber4;
            this.longitudeMinutes = rationalNumber5;
            this.longitudeSeconds = rationalNumber6;
        }

        public String toString() {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("[GPS. ");
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2.append("Latitude: ");
            stringBuffer2.append(this.latitudeDegrees.toDisplayString());
            stringBuffer2.append(" degrees, ");
            stringBuffer2.append(this.latitudeMinutes.toDisplayString());
            stringBuffer2.append(" minutes, ");
            stringBuffer2.append(this.latitudeSeconds.toDisplayString());
            stringBuffer2.append(" seconds ");
            stringBuffer2.append(this.latitudeRef);
            stringBuffer.append(stringBuffer2.toString());
            StringBuffer stringBuffer3 = new StringBuffer();
            stringBuffer3.append(", Longitude: ");
            stringBuffer3.append(this.longitudeDegrees.toDisplayString());
            stringBuffer3.append(" degrees, ");
            stringBuffer3.append(this.longitudeMinutes.toDisplayString());
            stringBuffer3.append(" minutes, ");
            stringBuffer3.append(this.longitudeSeconds.toDisplayString());
            stringBuffer3.append(" seconds ");
            stringBuffer3.append(this.longitudeRef);
            stringBuffer.append(stringBuffer3.toString());
            stringBuffer.append("]");
            return stringBuffer.toString();
        }

        public double getLongitudeAsDegreesEast() throws ImageReadException {
            double doubleValue = this.longitudeDegrees.doubleValue() + (this.longitudeMinutes.doubleValue() / 60.0d) + (this.longitudeSeconds.doubleValue() / 3600.0d);
            if (this.longitudeRef.trim().equalsIgnoreCase("e")) {
                return doubleValue;
            }
            if (this.longitudeRef.trim().equalsIgnoreCase("w")) {
                return -doubleValue;
            }
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("Unknown longitude ref: \"");
            stringBuffer.append(this.longitudeRef);
            stringBuffer.append("\"");
            throw new ImageReadException(stringBuffer.toString());
        }

        public double getLatitudeAsDegreesNorth() throws ImageReadException {
            double doubleValue = this.latitudeDegrees.doubleValue() + (this.latitudeMinutes.doubleValue() / 60.0d) + (this.latitudeSeconds.doubleValue() / 3600.0d);
            if (this.latitudeRef.trim().equalsIgnoreCase("n")) {
                return doubleValue;
            }
            if (this.latitudeRef.trim().equalsIgnoreCase("s")) {
                return -doubleValue;
            }
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("Unknown latitude ref: \"");
            stringBuffer.append(this.latitudeRef);
            stringBuffer.append("\"");
            throw new ImageReadException(stringBuffer.toString());
        }
    }

    public static class Item extends org.apache.sanselan.common.ImageMetadata.Item {
        private final TiffField entry;

        public Item(TiffField tiffField) {
            super(tiffField.getTagName(), tiffField.getValueDescription());
            this.entry = tiffField;
        }

        public TiffField getTiffField() {
            return this.entry;
        }
    }

    public TiffImageMetadata(TiffContents tiffContents) {
        this.contents = tiffContents;
    }

    public ArrayList getDirectories() {
        return super.getItems();
    }

    public ArrayList getItems() {
        ArrayList arrayList = new ArrayList();
        ArrayList items = super.getItems();
        for (int i = 0; i < items.size(); i++) {
            arrayList.addAll(((Directory) items.get(i)).getItems());
        }
        return arrayList;
    }

    public TiffOutputSet getOutputSet() throws ImageWriteException {
        int i = this.contents.header.byteOrder;
        TiffOutputSet tiffOutputSet = new TiffOutputSet(i);
        ArrayList directories = getDirectories();
        for (int i2 = 0; i2 < directories.size(); i2++) {
            Directory directory = (Directory) directories.get(i2);
            if (tiffOutputSet.findDirectory(directory.type) == null) {
                tiffOutputSet.addDirectory(directory.getOutputDirectory(i));
            }
        }
        return tiffOutputSet;
    }

    public TiffField findField(TagInfo tagInfo) throws ImageReadException {
        ArrayList directories = getDirectories();
        for (int i = 0; i < directories.size(); i++) {
            TiffField findField = ((Directory) directories.get(i)).findField(tagInfo);
            if (findField != null) {
                return findField;
            }
        }
        return null;
    }

    public TiffDirectory findDirectory(int i) {
        ArrayList directories = getDirectories();
        for (int i2 = 0; i2 < directories.size(); i2++) {
            Directory directory = (Directory) directories.get(i2);
            if (directory.type == i) {
                return directory.directory;
            }
        }
        return null;
    }

    public List getAllFields() throws ImageReadException {
        ArrayList arrayList = new ArrayList();
        ArrayList directories = getDirectories();
        for (int i = 0; i < directories.size(); i++) {
            arrayList.addAll(((Directory) directories.get(i)).getAllFields());
        }
        return arrayList;
    }

    public GPSInfo getGPS() throws ImageReadException {
        TiffDirectory findDirectory = findDirectory(-3);
        if (findDirectory == null) {
            return null;
        }
        TiffField findField = findDirectory.findField(TiffConstants.GPS_TAG_GPS_LATITUDE_REF);
        TiffField findField2 = findDirectory.findField(TiffConstants.GPS_TAG_GPS_LATITUDE);
        TiffField findField3 = findDirectory.findField(TiffConstants.GPS_TAG_GPS_LONGITUDE_REF);
        TiffField findField4 = findDirectory.findField(TiffConstants.GPS_TAG_GPS_LONGITUDE);
        if (findField == null || findField2 == null || findField3 == null || findField4 == null) {
            return null;
        }
        String stringValue = findField.getStringValue();
        RationalNumber[] rationalNumberArr = (RationalNumber[]) findField2.getValue();
        String stringValue2 = findField3.getStringValue();
        RationalNumber[] rationalNumberArr2 = (RationalNumber[]) findField4.getValue();
        if (rationalNumberArr.length == 3 && rationalNumberArr2.length == 3) {
            GPSInfo gPSInfo = new GPSInfo(stringValue, stringValue2, rationalNumberArr[0], rationalNumberArr[1], rationalNumberArr[2], rationalNumberArr2[0], rationalNumberArr2[1], rationalNumberArr2[2]);
            return gPSInfo;
        }
        throw new ImageReadException("Expected three values for latitude and longitude.");
    }
}

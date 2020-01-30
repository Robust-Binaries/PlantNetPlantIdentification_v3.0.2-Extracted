package org.apache.sanselan.formats.tiff.write;

import java.util.ArrayList;
import java.util.List;
import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.formats.tiff.constants.TagInfo;
import org.apache.sanselan.formats.tiff.constants.TiffConstants;
import org.apache.sanselan.util.Debug;

public final class TiffOutputSet implements TiffConstants {
    private static final String newline = System.getProperty("line.separator");
    public final int byteOrder;
    private final ArrayList directories;

    public TiffOutputSet() {
        this(73);
    }

    public TiffOutputSet(int i) {
        this.directories = new ArrayList();
        this.byteOrder = i;
    }

    /* access modifiers changed from: protected */
    public List getOutputItems(TiffOutputSummary tiffOutputSummary) throws ImageWriteException {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < this.directories.size(); i++) {
            arrayList.addAll(((TiffOutputDirectory) this.directories.get(i)).getOutputItems(tiffOutputSummary));
        }
        return arrayList;
    }

    public void addDirectory(TiffOutputDirectory tiffOutputDirectory) throws ImageWriteException {
        if (findDirectory(tiffOutputDirectory.type) == null) {
            this.directories.add(tiffOutputDirectory);
            return;
        }
        throw new ImageWriteException("Output set already contains a directory of that type.");
    }

    public List getDirectories() {
        return new ArrayList(this.directories);
    }

    public TiffOutputDirectory getRootDirectory() {
        return findDirectory(0);
    }

    public TiffOutputDirectory getExifDirectory() {
        return findDirectory(-2);
    }

    public TiffOutputDirectory getOrCreateRootDirectory() throws ImageWriteException {
        TiffOutputDirectory findDirectory = findDirectory(0);
        if (findDirectory != null) {
            return findDirectory;
        }
        return addRootDirectory();
    }

    public TiffOutputDirectory getOrCreateExifDirectory() throws ImageWriteException {
        getOrCreateRootDirectory();
        TiffOutputDirectory findDirectory = findDirectory(-2);
        if (findDirectory != null) {
            return findDirectory;
        }
        return addExifDirectory();
    }

    public TiffOutputDirectory getOrCreateGPSDirectory() throws ImageWriteException {
        getOrCreateExifDirectory();
        TiffOutputDirectory findDirectory = findDirectory(-3);
        if (findDirectory != null) {
            return findDirectory;
        }
        return addGPSDirectory();
    }

    public TiffOutputDirectory getGPSDirectory() {
        return findDirectory(-3);
    }

    public TiffOutputDirectory getInteroperabilityDirectory() {
        return findDirectory(-4);
    }

    public TiffOutputDirectory findDirectory(int i) {
        for (int i2 = 0; i2 < this.directories.size(); i2++) {
            TiffOutputDirectory tiffOutputDirectory = (TiffOutputDirectory) this.directories.get(i2);
            if (tiffOutputDirectory.type == i) {
                return tiffOutputDirectory;
            }
        }
        return null;
    }

    public void setGPSInDegrees(double d, double d2) throws ImageWriteException {
        TiffOutputDirectory orCreateGPSDirectory = getOrCreateGPSDirectory();
        String str = d < 0.0d ? "W" : "E";
        double abs = Math.abs(d);
        String str2 = d2 < 0.0d ? "S" : "N";
        double abs2 = Math.abs(d2);
        TiffOutputField create = TiffOutputField.create(TiffConstants.GPS_TAG_GPS_LONGITUDE_REF, this.byteOrder, str);
        orCreateGPSDirectory.removeField(TiffConstants.GPS_TAG_GPS_LONGITUDE_REF);
        orCreateGPSDirectory.add(create);
        TiffOutputField create2 = TiffOutputField.create(TiffConstants.GPS_TAG_GPS_LATITUDE_REF, this.byteOrder, str2);
        orCreateGPSDirectory.removeField(TiffConstants.GPS_TAG_GPS_LATITUDE_REF);
        orCreateGPSDirectory.add(create2);
        double d3 = (abs % 1.0d) * 60.0d;
        TiffOutputField create3 = TiffOutputField.create(TiffConstants.GPS_TAG_GPS_LONGITUDE, this.byteOrder, (Number[]) new Double[]{new Double((double) ((long) abs)), new Double((double) ((long) d3)), new Double((d3 % 1.0d) * 60.0d)});
        orCreateGPSDirectory.removeField(TiffConstants.GPS_TAG_GPS_LONGITUDE);
        orCreateGPSDirectory.add(create3);
        double d4 = (abs2 % 1.0d) * 60.0d;
        TiffOutputField create4 = TiffOutputField.create(TiffConstants.GPS_TAG_GPS_LATITUDE, this.byteOrder, (Number[]) new Double[]{new Double((double) ((long) abs2)), new Double((double) ((long) d4)), new Double((d4 % 1.0d) * 60.0d)});
        orCreateGPSDirectory.removeField(TiffConstants.GPS_TAG_GPS_LATITUDE);
        orCreateGPSDirectory.add(create4);
    }

    public void removeField(TagInfo tagInfo) {
        removeField(tagInfo.tag);
    }

    public void removeField(int i) {
        for (int i2 = 0; i2 < this.directories.size(); i2++) {
            ((TiffOutputDirectory) this.directories.get(i2)).removeField(i);
        }
    }

    public TiffOutputField findField(TagInfo tagInfo) {
        return findField(tagInfo.tag);
    }

    public TiffOutputField findField(int i) {
        for (int i2 = 0; i2 < this.directories.size(); i2++) {
            TiffOutputField findField = ((TiffOutputDirectory) this.directories.get(i2)).findField(i);
            if (findField != null) {
                return findField;
            }
        }
        return null;
    }

    public TiffOutputDirectory addRootDirectory() throws ImageWriteException {
        TiffOutputDirectory tiffOutputDirectory = new TiffOutputDirectory(0);
        addDirectory(tiffOutputDirectory);
        return tiffOutputDirectory;
    }

    public TiffOutputDirectory addExifDirectory() throws ImageWriteException {
        TiffOutputDirectory tiffOutputDirectory = new TiffOutputDirectory(-2);
        addDirectory(tiffOutputDirectory);
        return tiffOutputDirectory;
    }

    public TiffOutputDirectory addGPSDirectory() throws ImageWriteException {
        TiffOutputDirectory tiffOutputDirectory = new TiffOutputDirectory(-3);
        addDirectory(tiffOutputDirectory);
        return tiffOutputDirectory;
    }

    public TiffOutputDirectory addInteroperabilityDirectory() throws ImageWriteException {
        getOrCreateExifDirectory();
        TiffOutputDirectory tiffOutputDirectory = new TiffOutputDirectory(-4);
        addDirectory(tiffOutputDirectory);
        return tiffOutputDirectory;
    }

    public String toString() {
        return toString(null);
    }

    public String toString(String str) {
        if (str == null) {
            str = "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(str);
        stringBuffer.append("TiffOutputSet {");
        stringBuffer.append(newline);
        stringBuffer.append(str);
        StringBuffer stringBuffer2 = new StringBuffer();
        stringBuffer2.append("byteOrder: ");
        stringBuffer2.append(this.byteOrder);
        stringBuffer.append(stringBuffer2.toString());
        stringBuffer.append(newline);
        for (int i = 0; i < this.directories.size(); i++) {
            TiffOutputDirectory tiffOutputDirectory = (TiffOutputDirectory) this.directories.get(i);
            stringBuffer.append(str);
            StringBuffer stringBuffer3 = new StringBuffer();
            stringBuffer3.append("\tdirectory ");
            stringBuffer3.append(i);
            stringBuffer3.append(": ");
            stringBuffer3.append(tiffOutputDirectory.description());
            stringBuffer3.append(" (");
            stringBuffer3.append(tiffOutputDirectory.type);
            stringBuffer3.append(")");
            stringBuffer.append(stringBuffer3.toString());
            stringBuffer.append(newline);
            ArrayList fields = tiffOutputDirectory.getFields();
            for (int i2 = 0; i2 < fields.size(); i2++) {
                TiffOutputField tiffOutputField = (TiffOutputField) fields.get(i2);
                stringBuffer.append(str);
                StringBuffer stringBuffer4 = new StringBuffer();
                stringBuffer4.append("\t\tfield ");
                stringBuffer4.append(i);
                stringBuffer4.append(": ");
                stringBuffer4.append(tiffOutputField.tagInfo);
                stringBuffer.append(stringBuffer4.toString());
                stringBuffer.append(newline);
            }
        }
        stringBuffer.append(str);
        stringBuffer.append("}");
        stringBuffer.append(newline);
        return stringBuffer.toString();
    }

    public void dump() {
        Debug.debug(toString());
    }
}

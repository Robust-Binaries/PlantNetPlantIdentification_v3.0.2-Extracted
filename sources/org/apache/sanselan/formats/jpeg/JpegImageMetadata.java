package org.apache.sanselan.formats.jpeg;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.formats.tiff.TiffField;
import org.apache.sanselan.formats.tiff.TiffImageData;
import org.apache.sanselan.formats.tiff.TiffImageMetadata;
import org.apache.sanselan.formats.tiff.TiffImageMetadata.Directory;
import org.apache.sanselan.formats.tiff.TiffImageMetadata.Item;
import org.apache.sanselan.formats.tiff.constants.TagInfo;
import org.apache.sanselan.util.Debug;

public class JpegImageMetadata implements IImageMetadata {
    private static final String newline = System.getProperty("line.separator");
    private final TiffImageMetadata exif;
    private final JpegPhotoshopMetadata photoshop;

    public JpegImageMetadata(JpegPhotoshopMetadata jpegPhotoshopMetadata, TiffImageMetadata tiffImageMetadata) {
        this.photoshop = jpegPhotoshopMetadata;
        this.exif = tiffImageMetadata;
    }

    public TiffImageMetadata getExif() {
        return this.exif;
    }

    public JpegPhotoshopMetadata getPhotoshop() {
        return this.photoshop;
    }

    public TiffField findEXIFValue(TagInfo tagInfo) {
        ArrayList items = getItems();
        for (int i = 0; i < items.size(); i++) {
            Object obj = items.get(i);
            if (obj instanceof Item) {
                TiffField tiffField = ((Item) obj).getTiffField();
                if (tiffField.tag == tagInfo.tag) {
                    return tiffField;
                }
            }
        }
        return null;
    }

    public BufferedImage getEXIFThumbnail() throws ImageReadException, IOException {
        ArrayList directories = this.exif.getDirectories();
        for (int i = 0; i < directories.size(); i++) {
            BufferedImage thumbnail = ((Directory) directories.get(i)).getThumbnail();
            if (thumbnail != null) {
                return thumbnail;
            }
        }
        return null;
    }

    public TiffImageData getRawImageData() {
        ArrayList directories = this.exif.getDirectories();
        for (int i = 0; i < directories.size(); i++) {
            TiffImageData tiffImageData = ((Directory) directories.get(i)).getTiffImageData();
            if (tiffImageData != null) {
                return tiffImageData;
            }
        }
        return null;
    }

    public ArrayList getItems() {
        ArrayList arrayList = new ArrayList();
        TiffImageMetadata tiffImageMetadata = this.exif;
        if (tiffImageMetadata != null) {
            arrayList.addAll(tiffImageMetadata.getItems());
        }
        JpegPhotoshopMetadata jpegPhotoshopMetadata = this.photoshop;
        if (jpegPhotoshopMetadata != null) {
            arrayList.addAll(jpegPhotoshopMetadata.getItems());
        }
        return arrayList;
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
        if (this.exif == null) {
            stringBuffer.append("No Exif metadata.");
        } else {
            stringBuffer.append("Exif metadata:");
            stringBuffer.append(newline);
            stringBuffer.append(this.exif.toString("\t"));
        }
        stringBuffer.append(newline);
        stringBuffer.append(str);
        if (this.photoshop == null) {
            stringBuffer.append("No Photoshop (IPTC) metadata.");
        } else {
            stringBuffer.append("Photoshop (IPTC) metadata:");
            stringBuffer.append(newline);
            stringBuffer.append(this.photoshop.toString("\t"));
        }
        return stringBuffer.toString();
    }

    public void dump() {
        Debug.debug(toString());
    }
}

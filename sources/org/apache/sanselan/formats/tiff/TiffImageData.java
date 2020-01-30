package org.apache.sanselan.formats.tiff;

import java.io.IOException;
import java.util.ArrayList;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.formats.tiff.TiffElement.DataElement;
import org.apache.sanselan.formats.tiff.datareaders.DataReader;
import org.apache.sanselan.formats.tiff.datareaders.DataReaderStrips;
import org.apache.sanselan.formats.tiff.datareaders.DataReaderTiled;
import org.apache.sanselan.formats.tiff.photometricinterpreters.PhotometricInterpreter;

public abstract class TiffImageData {

    public static class Data extends DataElement {
        public Data(int i, int i2, byte[] bArr) {
            super(i, i2, bArr);
        }

        public String getElementDescription(boolean z) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("Tiff image data: ");
            stringBuffer.append(this.data.length);
            stringBuffer.append(" bytes");
            return stringBuffer.toString();
        }
    }

    public static class Strips extends TiffImageData {
        public final int rowsPerStrip;
        public final DataElement[] strips;

        public boolean stripsNotTiles() {
            return true;
        }

        public Strips(DataElement[] dataElementArr, int i) {
            this.strips = dataElementArr;
            this.rowsPerStrip = i;
        }

        public DataElement[] getImageData() {
            return this.strips;
        }

        public DataReader getDataReader(ArrayList arrayList, PhotometricInterpreter photometricInterpreter, int i, int[] iArr, int i2, int i3, int i4, int i5, int i6) throws IOException, ImageReadException {
            DataReaderStrips dataReaderStrips = new DataReaderStrips(photometricInterpreter, i, iArr, i2, i3, i4, i5, i6, this.rowsPerStrip, this);
            return dataReaderStrips;
        }
    }

    public static class Tiles extends TiffImageData {
        private final int tileLength;
        private final int tileWidth;
        public final DataElement[] tiles;

        public boolean stripsNotTiles() {
            return false;
        }

        public Tiles(DataElement[] dataElementArr, int i, int i2) {
            this.tiles = dataElementArr;
            this.tileWidth = i;
            this.tileLength = i2;
        }

        public DataElement[] getImageData() {
            return this.tiles;
        }

        public DataReader getDataReader(ArrayList arrayList, PhotometricInterpreter photometricInterpreter, int i, int[] iArr, int i2, int i3, int i4, int i5, int i6) throws IOException, ImageReadException {
            DataReaderTiled dataReaderTiled = new DataReaderTiled(photometricInterpreter, this.tileWidth, this.tileLength, i, iArr, i2, i3, i4, i5, i6, this);
            return dataReaderTiled;
        }
    }

    public abstract DataReader getDataReader(ArrayList arrayList, PhotometricInterpreter photometricInterpreter, int i, int[] iArr, int i2, int i3, int i4, int i5, int i6) throws IOException, ImageReadException;

    public abstract DataElement[] getImageData();

    public abstract boolean stripsNotTiles();
}

package org.apache.sanselan.palette;

import java.util.ArrayList;
import org.apache.sanselan.ImageWriteException;

public class QuantizedPalette extends Palette {
    private final int precision;
    private final ColorSpaceSubset[] straight;
    private final ArrayList subsets;

    public void dump() {
    }

    public QuantizedPalette(ArrayList arrayList, int i) {
        this.subsets = arrayList;
        this.precision = i;
        this.straight = new ColorSpaceSubset[(1 << (i * 3))];
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            ColorSpaceSubset colorSpaceSubset = (ColorSpaceSubset) arrayList.get(i2);
            colorSpaceSubset.setIndex(i2);
            for (int i3 = colorSpaceSubset.mins[0]; i3 <= colorSpaceSubset.maxs[0]; i3++) {
                for (int i4 = colorSpaceSubset.mins[1]; i4 <= colorSpaceSubset.maxs[1]; i4++) {
                    for (int i5 = colorSpaceSubset.mins[2]; i5 <= colorSpaceSubset.maxs[2]; i5++) {
                        this.straight[(i3 << (i * 2)) | (i4 << (i * 1)) | (i5 << (i * 0))] = colorSpaceSubset;
                    }
                }
            }
        }
    }

    public int getPaletteIndex(int i) throws ImageWriteException {
        int i2 = this.precision;
        int i3 = (1 << i2) - 1;
        return this.straight[((i >> (8 - i2)) & i3) | ((i >> (24 - (i2 * 3))) & (i3 << (i2 << 1))) | ((i >> (16 - (i2 * 2))) & (i3 << i2))].index;
    }

    public int getEntry(int i) {
        return ((ColorSpaceSubset) this.subsets.get(i)).rgb;
    }

    public int length() {
        return this.subsets.size();
    }
}

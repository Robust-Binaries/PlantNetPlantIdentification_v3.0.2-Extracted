package org.apache.sanselan.palette;

import org.apache.sanselan.ImageWriteException;

public abstract class Palette {
    public void dump() {
    }

    public abstract int getEntry(int i);

    public abstract int getPaletteIndex(int i) throws ImageWriteException;

    public abstract int length();
}

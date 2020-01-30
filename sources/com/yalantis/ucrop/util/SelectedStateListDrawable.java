package com.yalantis.ucrop.util;

import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

public class SelectedStateListDrawable extends StateListDrawable {
    private int mSelectionColor;

    public boolean isStateful() {
        return true;
    }

    public SelectedStateListDrawable(Drawable drawable, int i) {
        this.mSelectionColor = i;
        addState(new int[]{16842913}, drawable);
        addState(new int[0], drawable);
    }

    /* access modifiers changed from: protected */
    public boolean onStateChange(int[] iArr) {
        boolean z = false;
        for (int i : iArr) {
            if (i == 16842913) {
                z = true;
            }
        }
        if (z) {
            super.setColorFilter(this.mSelectionColor, Mode.SRC_ATOP);
        } else {
            super.clearColorFilter();
        }
        return super.onStateChange(iArr);
    }
}

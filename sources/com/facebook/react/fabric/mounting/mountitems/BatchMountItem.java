package com.facebook.react.fabric.mounting.mountitems;

import com.facebook.proguard.annotations.DoNotStrip;
import com.facebook.react.fabric.mounting.MountingManager;
import com.facebook.systrace.Systrace;

@DoNotStrip
public class BatchMountItem implements MountItem {
    private final MountItem[] mMountItems;
    private final int mSize;

    public BatchMountItem(MountItem[] mountItemArr, int i) {
        if (mountItemArr == null) {
            throw new NullPointerException();
        } else if (i < 0 || i > mountItemArr.length) {
            StringBuilder sb = new StringBuilder();
            sb.append("Invalid size received by parameter size: ");
            sb.append(i);
            sb.append(" items.size = ");
            sb.append(mountItemArr.length);
            throw new IllegalArgumentException(sb.toString());
        } else {
            this.mMountItems = mountItemArr;
            this.mSize = i;
        }
    }

    public void execute(MountingManager mountingManager) {
        StringBuilder sb = new StringBuilder();
        sb.append("FabricUIManager::mountViews (");
        sb.append(this.mSize);
        sb.append(" items)");
        Systrace.beginSection(0, sb.toString());
        for (int i = 0; i < this.mSize; i++) {
            this.mMountItems[i].execute(mountingManager);
        }
        Systrace.endSection(0);
    }
}

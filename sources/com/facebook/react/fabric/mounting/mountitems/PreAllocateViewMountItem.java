package com.facebook.react.fabric.mounting.mountitems;

import com.facebook.react.fabric.mounting.MountingManager;
import com.facebook.react.uimanager.ThemedReactContext;

public class PreAllocateViewMountItem implements MountItem {
    private final String mComponent;
    private final ThemedReactContext mContext;
    private final int mRootTag;

    public PreAllocateViewMountItem(ThemedReactContext themedReactContext, int i, String str) {
        this.mContext = themedReactContext;
        this.mComponent = str;
        this.mRootTag = i;
    }

    public void execute(MountingManager mountingManager) {
        mountingManager.preallocateView(this.mContext, this.mComponent);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(this.mRootTag);
        sb.append("] - Preallocate ");
        sb.append(this.mComponent);
        return sb.toString();
    }
}

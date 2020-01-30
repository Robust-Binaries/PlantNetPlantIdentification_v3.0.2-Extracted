package com.facebook.react.fabric.mounting.mountitems;

import com.facebook.react.fabric.mounting.MountingManager;
import com.facebook.react.uimanager.ThemedReactContext;

public class CreateMountItem implements MountItem {
    private final String mComponentName;
    private final boolean mIsVirtual;
    private final int mReactTag;
    private final ThemedReactContext mThemedReactContext;

    public CreateMountItem(ThemedReactContext themedReactContext, String str, int i, boolean z) {
        this.mReactTag = i;
        this.mThemedReactContext = themedReactContext;
        this.mComponentName = str;
        this.mIsVirtual = z;
    }

    public void execute(MountingManager mountingManager) {
        mountingManager.createView(this.mThemedReactContext, this.mComponentName, this.mReactTag, this.mIsVirtual);
    }

    public String getComponentName() {
        return this.mComponentName;
    }

    public ThemedReactContext getThemedReactContext() {
        return this.mThemedReactContext;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CreateMountItem [");
        sb.append(this.mReactTag);
        sb.append("] ");
        sb.append(this.mComponentName);
        return sb.toString();
    }
}

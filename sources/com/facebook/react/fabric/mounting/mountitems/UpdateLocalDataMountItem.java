package com.facebook.react.fabric.mounting.mountitems;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableNativeMap;
import com.facebook.react.fabric.mounting.MountingManager;

public class UpdateLocalDataMountItem implements MountItem {
    private final ReadableMap mNewLocalData;
    private final int mReactTag;

    public UpdateLocalDataMountItem(int i, ReadableNativeMap readableNativeMap) {
        this.mReactTag = i;
        this.mNewLocalData = readableNativeMap;
    }

    public void execute(MountingManager mountingManager) {
        mountingManager.updateLocalData(this.mReactTag, this.mNewLocalData);
    }

    public ReadableMap getNewLocalData() {
        return this.mNewLocalData;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UpdateLocalDataMountItem [");
        sb.append(this.mReactTag);
        sb.append("] - localData: ");
        sb.append(this.mNewLocalData);
        return sb.toString();
    }
}

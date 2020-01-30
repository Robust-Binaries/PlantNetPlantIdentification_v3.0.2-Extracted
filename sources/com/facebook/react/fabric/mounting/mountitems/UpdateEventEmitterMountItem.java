package com.facebook.react.fabric.mounting.mountitems;

import com.facebook.react.fabric.jsi.EventEmitterWrapper;
import com.facebook.react.fabric.mounting.MountingManager;

public class UpdateEventEmitterMountItem implements MountItem {
    private final EventEmitterWrapper mEventHandler;
    private final int mReactTag;

    public UpdateEventEmitterMountItem(int i, EventEmitterWrapper eventEmitterWrapper) {
        this.mReactTag = i;
        this.mEventHandler = eventEmitterWrapper;
    }

    public void execute(MountingManager mountingManager) {
        mountingManager.updateEventEmitter(this.mReactTag, this.mEventHandler);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UpdateEventEmitterMountItem [");
        sb.append(this.mReactTag);
        sb.append("]");
        return sb.toString();
    }
}
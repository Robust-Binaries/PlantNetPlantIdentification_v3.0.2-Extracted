package com.facebook.react.fabric.mounting.mountitems;

import com.facebook.react.fabric.mounting.MountingManager;

public class UpdateLayoutMountItem implements MountItem {
    private final int mHeight;
    private final int mReactTag;
    private final int mWidth;

    /* renamed from: mX */
    private final int f64mX;

    /* renamed from: mY */
    private final int f65mY;

    public UpdateLayoutMountItem(int i, int i2, int i3, int i4, int i5) {
        this.mReactTag = i;
        this.f64mX = i2;
        this.f65mY = i3;
        this.mWidth = i4;
        this.mHeight = i5;
    }

    public void execute(MountingManager mountingManager) {
        mountingManager.updateLayout(this.mReactTag, this.f64mX, this.f65mY, this.mWidth, this.mHeight);
    }

    public int getX() {
        return this.f64mX;
    }

    public int getY() {
        return this.f65mY;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public int getWidth() {
        return this.mWidth;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UpdateLayoutMountItem [");
        sb.append(this.mReactTag);
        sb.append("] - x: ");
        sb.append(this.f64mX);
        sb.append(" - y: ");
        sb.append(this.f65mY);
        sb.append(" - height: ");
        sb.append(this.mHeight);
        sb.append(" - width: ");
        sb.append(this.mWidth);
        return sb.toString();
    }
}

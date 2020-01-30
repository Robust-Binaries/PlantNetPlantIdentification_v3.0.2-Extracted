package com.facebook.react.animated;

import com.facebook.react.bridge.ReadableMap;
import javax.annotation.Nullable;

class ValueAnimatedNode extends AnimatedNode {
    double mOffset = 0.0d;
    double mValue = Double.NaN;
    @Nullable
    private AnimatedNodeValueListener mValueListener;

    public ValueAnimatedNode() {
    }

    public ValueAnimatedNode(ReadableMap readableMap) {
        this.mValue = readableMap.getDouble("value");
        this.mOffset = readableMap.getDouble("offset");
    }

    public double getValue() {
        return this.mOffset + this.mValue;
    }

    public void flattenOffset() {
        this.mValue += this.mOffset;
        this.mOffset = 0.0d;
    }

    public void extractOffset() {
        this.mOffset += this.mValue;
        this.mValue = 0.0d;
    }

    public void onValueUpdate() {
        AnimatedNodeValueListener animatedNodeValueListener = this.mValueListener;
        if (animatedNodeValueListener != null) {
            animatedNodeValueListener.onValueUpdate(getValue());
        }
    }

    public void setValueListener(@Nullable AnimatedNodeValueListener animatedNodeValueListener) {
        this.mValueListener = animatedNodeValueListener;
    }
}

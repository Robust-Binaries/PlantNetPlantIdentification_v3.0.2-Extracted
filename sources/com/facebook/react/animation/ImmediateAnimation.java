package com.facebook.react.animation;

public class ImmediateAnimation extends Animation {
    public ImmediateAnimation(int i, AnimationPropertyUpdater animationPropertyUpdater) {
        super(i, animationPropertyUpdater);
    }

    public void run() {
        onUpdate(1.0f);
        finish();
    }
}

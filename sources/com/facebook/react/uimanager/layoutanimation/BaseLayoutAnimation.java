package com.facebook.react.uimanager.layoutanimation;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import com.facebook.react.uimanager.IllegalViewOperationException;

abstract class BaseLayoutAnimation extends AbstractLayoutAnimation {
    /* access modifiers changed from: 0000 */
    public abstract boolean isReverse();

    BaseLayoutAnimation() {
    }

    /* access modifiers changed from: 0000 */
    public boolean isValid() {
        return this.mDurationMs > 0 && this.mAnimatedProperty != null;
    }

    /* access modifiers changed from: 0000 */
    public Animation createAnimationImpl(View view, int i, int i2, int i3, int i4) {
        if (this.mAnimatedProperty != null) {
            float f = 0.0f;
            switch (this.mAnimatedProperty) {
                case OPACITY:
                    float alpha = isReverse() ? view.getAlpha() : 0.0f;
                    if (!isReverse()) {
                        f = view.getAlpha();
                    }
                    return new OpacityAnimation(view, alpha, f);
                case SCALE_XY:
                    float f2 = isReverse() ? 1.0f : 0.0f;
                    float f3 = isReverse() ? 0.0f : 1.0f;
                    ScaleAnimation scaleAnimation = new ScaleAnimation(f2, f3, f2, f3, 1, 0.5f, 1, 0.5f);
                    return scaleAnimation;
                case SCALE_X:
                    ScaleAnimation scaleAnimation2 = new ScaleAnimation(isReverse() ? 1.0f : 0.0f, isReverse() ? 0.0f : 1.0f, 1.0f, 1.0f, 1, 0.5f, 1, 0.0f);
                    return scaleAnimation2;
                case SCALE_Y:
                    ScaleAnimation scaleAnimation3 = new ScaleAnimation(1.0f, 1.0f, isReverse() ? 1.0f : 0.0f, isReverse() ? 0.0f : 1.0f, 1, 0.0f, 1, 0.5f);
                    return scaleAnimation3;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Missing animation for property : ");
                    sb.append(this.mAnimatedProperty);
                    throw new IllegalViewOperationException(sb.toString());
            }
        } else {
            throw new IllegalViewOperationException("Missing animated property from animation config");
        }
    }
}

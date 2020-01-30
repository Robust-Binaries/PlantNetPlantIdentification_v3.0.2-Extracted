package com.facebook.react.animation;

import android.view.View;
import com.facebook.infer.annotation.Assertions;
import javax.annotation.Nullable;

public abstract class Animation {
    @Nullable
    private View mAnimatedView;
    private final int mAnimationID;
    @Nullable
    private AnimationListener mAnimationListener;
    private volatile boolean mCancelled = false;
    private volatile boolean mIsFinished = false;
    private final AnimationPropertyUpdater mPropertyUpdater;

    public abstract void run();

    public Animation(int i, AnimationPropertyUpdater animationPropertyUpdater) {
        this.mAnimationID = i;
        this.mPropertyUpdater = animationPropertyUpdater;
    }

    public void setAnimationListener(AnimationListener animationListener) {
        this.mAnimationListener = animationListener;
    }

    public final void start(View view) {
        this.mAnimatedView = view;
        this.mPropertyUpdater.prepare(view);
        run();
    }

    /* access modifiers changed from: protected */
    public final boolean onUpdate(float f) {
        Assertions.assertCondition(!this.mIsFinished, "Animation must not already be finished!");
        if (!this.mCancelled) {
            this.mPropertyUpdater.onUpdate((View) Assertions.assertNotNull(this.mAnimatedView), f);
        }
        return !this.mCancelled;
    }

    /* access modifiers changed from: protected */
    public final void finish() {
        Assertions.assertCondition(!this.mIsFinished, "Animation must not already be finished!");
        this.mIsFinished = true;
        if (!this.mCancelled) {
            View view = this.mAnimatedView;
            if (view != null) {
                this.mPropertyUpdater.onFinish(view);
            }
            AnimationListener animationListener = this.mAnimationListener;
            if (animationListener != null) {
                animationListener.onFinished();
            }
        }
    }

    public final void cancel() {
        if (!this.mIsFinished && !this.mCancelled) {
            this.mCancelled = true;
            AnimationListener animationListener = this.mAnimationListener;
            if (animationListener != null) {
                animationListener.onCancel();
            }
        }
    }

    public int getAnimationID() {
        return this.mAnimationID;
    }
}

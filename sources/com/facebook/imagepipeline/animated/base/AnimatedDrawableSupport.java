package com.facebook.imagepipeline.animated.base;

import android.view.animation.LinearInterpolator;
import com.facebook.common.time.MonotonicClock;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;
import java.util.concurrent.ScheduledExecutorService;

public class AnimatedDrawableSupport extends AbstractAnimatedDrawable implements AnimatableDrawableSupport {
    public AnimatedDrawableSupport(ScheduledExecutorService scheduledExecutorService, AnimatedDrawableCachingBackend animatedDrawableCachingBackend, AnimatedDrawableDiagnostics animatedDrawableDiagnostics, MonotonicClock monotonicClock) {
        super(scheduledExecutorService, animatedDrawableCachingBackend, animatedDrawableDiagnostics, monotonicClock);
    }

    public ValueAnimator createValueAnimator(int i) {
        ValueAnimator createValueAnimator = createValueAnimator();
        createValueAnimator.setRepeatCount(Math.max(i / getAnimatedDrawableBackend().getDurationMs(), 1));
        return createValueAnimator;
    }

    public ValueAnimator createValueAnimator() {
        int loopCount = getAnimatedDrawableBackend().getLoopCount();
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setIntValues(0, getDuration());
        valueAnimator.setDuration((long) getDuration());
        if (loopCount == 0) {
            loopCount = -1;
        }
        valueAnimator.setRepeatCount(loopCount);
        valueAnimator.setRepeatMode(1);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(createAnimatorUpdateListener());
        return valueAnimator;
    }

    public AnimatorUpdateListener createAnimatorUpdateListener() {
        return new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                AnimatedDrawableSupport.this.setLevel(((Integer) valueAnimator.getAnimatedValue()).intValue());
            }
        };
    }
}

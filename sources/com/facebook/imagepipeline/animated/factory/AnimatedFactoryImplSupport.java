package com.facebook.imagepipeline.animated.factory;

import android.content.res.Resources;
import com.facebook.common.internal.DoNotStrip;
import com.facebook.imagepipeline.animated.impl.AnimatedDrawableBackendProvider;
import com.facebook.imagepipeline.animated.impl.AnimatedDrawableCachingBackendImplProvider;
import com.facebook.imagepipeline.animated.util.AnimatedDrawableUtil;
import com.facebook.imagepipeline.bitmaps.PlatformBitmapFactory;
import com.facebook.imagepipeline.core.ExecutorSupplier;
import java.util.concurrent.ScheduledExecutorService;
import javax.annotation.concurrent.NotThreadSafe;

@DoNotStrip
@NotThreadSafe
public class AnimatedFactoryImplSupport extends AnimatedFactoryImpl {
    @DoNotStrip
    public AnimatedFactoryImplSupport(PlatformBitmapFactory platformBitmapFactory, ExecutorSupplier executorSupplier) {
        super(platformBitmapFactory, executorSupplier);
    }

    /* access modifiers changed from: protected */
    public AnimatedDrawableFactory createAnimatedDrawableFactory(AnimatedDrawableBackendProvider animatedDrawableBackendProvider, AnimatedDrawableCachingBackendImplProvider animatedDrawableCachingBackendImplProvider, AnimatedDrawableUtil animatedDrawableUtil, ScheduledExecutorService scheduledExecutorService, Resources resources) {
        AnimatedDrawableFactoryImplSupport animatedDrawableFactoryImplSupport = new AnimatedDrawableFactoryImplSupport(animatedDrawableBackendProvider, animatedDrawableCachingBackendImplProvider, animatedDrawableUtil, scheduledExecutorService, resources);
        return animatedDrawableFactoryImplSupport;
    }
}

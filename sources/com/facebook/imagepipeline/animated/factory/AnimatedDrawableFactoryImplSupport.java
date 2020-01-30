package com.facebook.imagepipeline.animated.factory;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import com.facebook.common.time.MonotonicClock;
import com.facebook.imagepipeline.animated.base.AnimatedDrawableBackend;
import com.facebook.imagepipeline.animated.base.AnimatedDrawableOptions;
import com.facebook.imagepipeline.animated.base.AnimatedDrawableSupport;
import com.facebook.imagepipeline.animated.base.AnimatedImage;
import com.facebook.imagepipeline.animated.base.AnimatedImageResult;
import com.facebook.imagepipeline.animated.impl.AnimatedDrawableBackendProvider;
import com.facebook.imagepipeline.animated.impl.AnimatedDrawableCachingBackendImpl;
import com.facebook.imagepipeline.animated.impl.AnimatedDrawableCachingBackendImplProvider;
import com.facebook.imagepipeline.animated.impl.AnimatedDrawableDiagnosticsImpl;
import com.facebook.imagepipeline.animated.impl.AnimatedDrawableDiagnosticsNoop;
import com.facebook.imagepipeline.animated.util.AnimatedDrawableUtil;
import com.facebook.imagepipeline.image.CloseableAnimatedImage;
import com.facebook.imagepipeline.image.CloseableImage;
import java.util.concurrent.ScheduledExecutorService;

public class AnimatedDrawableFactoryImplSupport implements AnimatedDrawableFactory {
    private final AnimatedDrawableBackendProvider mAnimatedDrawableBackendProvider;
    private final AnimatedDrawableCachingBackendImplProvider mAnimatedDrawableCachingBackendProvider;
    private final AnimatedDrawableUtil mAnimatedDrawableUtil;
    private final MonotonicClock mMonotonicClock = new MonotonicClock() {
        public long now() {
            return SystemClock.uptimeMillis();
        }
    };
    private final Resources mResources;
    private final ScheduledExecutorService mScheduledExecutorServiceForUiThread;

    public AnimatedDrawableFactoryImplSupport(AnimatedDrawableBackendProvider animatedDrawableBackendProvider, AnimatedDrawableCachingBackendImplProvider animatedDrawableCachingBackendImplProvider, AnimatedDrawableUtil animatedDrawableUtil, ScheduledExecutorService scheduledExecutorService, Resources resources) {
        this.mAnimatedDrawableBackendProvider = animatedDrawableBackendProvider;
        this.mAnimatedDrawableCachingBackendProvider = animatedDrawableCachingBackendImplProvider;
        this.mAnimatedDrawableUtil = animatedDrawableUtil;
        this.mScheduledExecutorServiceForUiThread = scheduledExecutorService;
        this.mResources = resources;
    }

    /* JADX WARNING: type inference failed for: r4v4, types: [com.facebook.imagepipeline.animated.base.AnimatedDrawableSupport, android.graphics.drawable.Drawable] */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r4v4, types: [com.facebook.imagepipeline.animated.base.AnimatedDrawableSupport, android.graphics.drawable.Drawable]
      assigns: [com.facebook.imagepipeline.animated.base.AnimatedDrawableSupport]
      uses: [android.graphics.drawable.Drawable]
      mth insns count: 14
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.graphics.drawable.Drawable create(com.facebook.imagepipeline.image.CloseableImage r4) {
        /*
            r3 = this;
            boolean r0 = r4 instanceof com.facebook.imagepipeline.image.CloseableAnimatedImage
            if (r0 == 0) goto L_0x0011
            com.facebook.imagepipeline.image.CloseableAnimatedImage r4 = (com.facebook.imagepipeline.image.CloseableAnimatedImage) r4
            com.facebook.imagepipeline.animated.base.AnimatedImageResult r4 = r4.getImageResult()
            com.facebook.imagepipeline.animated.base.AnimatedDrawableOptions r0 = com.facebook.imagepipeline.animated.base.AnimatedDrawableOptions.DEFAULTS
            com.facebook.imagepipeline.animated.base.AnimatedDrawableSupport r4 = r3.create(r4, r0)
            return r4
        L_0x0011:
            java.lang.UnsupportedOperationException r0 = new java.lang.UnsupportedOperationException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Unrecognized image class: "
            r1.append(r2)
            r1.append(r4)
            java.lang.String r4 = r1.toString()
            r0.<init>(r4)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.imagepipeline.animated.factory.AnimatedDrawableFactoryImplSupport.create(com.facebook.imagepipeline.image.CloseableImage):android.graphics.drawable.Drawable");
    }

    private AnimatedDrawableSupport create(AnimatedImageResult animatedImageResult, AnimatedDrawableOptions animatedDrawableOptions) {
        AnimatedImage image = animatedImageResult.getImage();
        return createAnimatedDrawable(animatedDrawableOptions, this.mAnimatedDrawableBackendProvider.get(animatedImageResult, new Rect(0, 0, image.getWidth(), image.getHeight())));
    }

    private AnimatedImageResult getImageIfCloseableAnimatedImage(CloseableImage closeableImage) {
        if (closeableImage instanceof CloseableAnimatedImage) {
            return ((CloseableAnimatedImage) closeableImage).getImageResult();
        }
        return null;
    }

    private AnimatedDrawableSupport createAnimatedDrawable(AnimatedDrawableOptions animatedDrawableOptions, AnimatedDrawableBackend animatedDrawableBackend) {
        AnimatedDrawableDiagnosticsImpl animatedDrawableDiagnosticsImpl;
        DisplayMetrics displayMetrics = this.mResources.getDisplayMetrics();
        AnimatedDrawableCachingBackendImpl animatedDrawableCachingBackendImpl = this.mAnimatedDrawableCachingBackendProvider.get(animatedDrawableBackend, animatedDrawableOptions);
        if (animatedDrawableOptions.enableDebugging) {
            animatedDrawableDiagnosticsImpl = new AnimatedDrawableDiagnosticsImpl(this.mAnimatedDrawableUtil, displayMetrics);
        } else {
            animatedDrawableDiagnosticsImpl = AnimatedDrawableDiagnosticsNoop.getInstance();
        }
        return new AnimatedDrawableSupport(this.mScheduledExecutorServiceForUiThread, animatedDrawableCachingBackendImpl, animatedDrawableDiagnosticsImpl, this.mMonotonicClock);
    }
}

package com.facebook.fresco.animation.bitmap;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import com.facebook.common.references.CloseableReference;
import com.facebook.fresco.animation.backend.AnimationBackend;
import com.facebook.fresco.animation.backend.AnimationBackendDelegateWithInactivityCheck.InactivityListener;
import com.facebook.fresco.animation.backend.AnimationInformation;
import com.facebook.fresco.animation.bitmap.preparation.BitmapFramePreparationStrategy;
import com.facebook.fresco.animation.bitmap.preparation.BitmapFramePreparer;
import com.facebook.imagepipeline.bitmaps.PlatformBitmapFactory;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.annotation.Nullable;

public class BitmapAnimationBackend implements AnimationBackend, InactivityListener {
    public static final int FRAME_TYPE_CACHED = 0;
    public static final int FRAME_TYPE_CREATED = 2;
    public static final int FRAME_TYPE_FALLBACK = 3;
    public static final int FRAME_TYPE_REUSED = 1;
    public static final int FRAME_TYPE_UNKNOWN = -1;
    private static final Class<?> TAG = BitmapAnimationBackend.class;
    private final AnimationInformation mAnimationInformation;
    private Config mBitmapConfig = Config.ARGB_8888;
    private final BitmapFrameCache mBitmapFrameCache;
    @Nullable
    private final BitmapFramePreparationStrategy mBitmapFramePreparationStrategy;
    @Nullable
    private final BitmapFramePreparer mBitmapFramePreparer;
    private final BitmapFrameRenderer mBitmapFrameRenderer;
    private int mBitmapHeight;
    private int mBitmapWidth;
    @Nullable
    private Rect mBounds;
    @Nullable
    private FrameListener mFrameListener;
    private final Paint mPaint;
    private final PlatformBitmapFactory mPlatformBitmapFactory;

    public interface FrameListener {
        void onDrawFrameStart(BitmapAnimationBackend bitmapAnimationBackend, int i);

        void onFrameDrawn(BitmapAnimationBackend bitmapAnimationBackend, int i, int i2);

        void onFrameDropped(BitmapAnimationBackend bitmapAnimationBackend, int i);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface FrameType {
    }

    public BitmapAnimationBackend(PlatformBitmapFactory platformBitmapFactory, BitmapFrameCache bitmapFrameCache, AnimationInformation animationInformation, BitmapFrameRenderer bitmapFrameRenderer, @Nullable BitmapFramePreparationStrategy bitmapFramePreparationStrategy, @Nullable BitmapFramePreparer bitmapFramePreparer) {
        this.mPlatformBitmapFactory = platformBitmapFactory;
        this.mBitmapFrameCache = bitmapFrameCache;
        this.mAnimationInformation = animationInformation;
        this.mBitmapFrameRenderer = bitmapFrameRenderer;
        this.mBitmapFramePreparationStrategy = bitmapFramePreparationStrategy;
        this.mBitmapFramePreparer = bitmapFramePreparer;
        this.mPaint = new Paint(6);
        updateBitmapDimensions();
    }

    public void setBitmapConfig(Config config) {
        this.mBitmapConfig = config;
    }

    public void setFrameListener(@Nullable FrameListener frameListener) {
        this.mFrameListener = frameListener;
    }

    public int getFrameCount() {
        return this.mAnimationInformation.getFrameCount();
    }

    public int getFrameDurationMs(int i) {
        return this.mAnimationInformation.getFrameDurationMs(i);
    }

    public int getLoopCount() {
        return this.mAnimationInformation.getLoopCount();
    }

    public boolean drawFrame(Drawable drawable, Canvas canvas, int i) {
        FrameListener frameListener = this.mFrameListener;
        if (frameListener != null) {
            frameListener.onDrawFrameStart(this, i);
        }
        boolean drawFrameOrFallback = drawFrameOrFallback(canvas, i, 0);
        if (!drawFrameOrFallback) {
            FrameListener frameListener2 = this.mFrameListener;
            if (frameListener2 != null) {
                frameListener2.onFrameDropped(this, i);
            }
        }
        BitmapFramePreparationStrategy bitmapFramePreparationStrategy = this.mBitmapFramePreparationStrategy;
        if (bitmapFramePreparationStrategy != null) {
            BitmapFramePreparer bitmapFramePreparer = this.mBitmapFramePreparer;
            if (bitmapFramePreparer != null) {
                bitmapFramePreparationStrategy.prepareFrames(bitmapFramePreparer, this.mBitmapFrameCache, this, i);
            }
        }
        return drawFrameOrFallback;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0036, code lost:
        r10 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0037, code lost:
        com.facebook.common.logging.FLog.m85w(TAG, "Failed to create frame bitmap", (java.lang.Throwable) r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x003e, code lost:
        com.facebook.common.references.CloseableReference.closeSafely(null);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0041, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x0075, code lost:
        r10 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x0076, code lost:
        com.facebook.common.references.CloseableReference.closeSafely(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x0079, code lost:
        throw r10;
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [B:4:0x000d, B:7:0x0019] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean drawFrameOrFallback(android.graphics.Canvas r10, int r11, int r12) {
        /*
            r9 = this;
            r0 = 2
            r1 = 3
            r2 = -1
            r3 = 1
            r4 = 0
            r5 = 0
            switch(r12) {
                case 0: goto L_0x005c;
                case 1: goto L_0x0042;
                case 2: goto L_0x0019;
                case 3: goto L_0x000d;
                default: goto L_0x0009;
            }
        L_0x0009:
            com.facebook.common.references.CloseableReference.closeSafely(r5)
            return r4
        L_0x000d:
            com.facebook.fresco.animation.bitmap.BitmapFrameCache r12 = r9.mBitmapFrameCache     // Catch:{ all -> 0x0075 }
            com.facebook.common.references.CloseableReference r5 = r12.getFallbackFrame(r11)     // Catch:{ all -> 0x0075 }
            boolean r12 = r9.drawBitmapAndCache(r11, r5, r10, r1)     // Catch:{ all -> 0x0075 }
            r0 = -1
            goto L_0x0067
        L_0x0019:
            com.facebook.imagepipeline.bitmaps.PlatformBitmapFactory r12 = r9.mPlatformBitmapFactory     // Catch:{ RuntimeException -> 0x0036 }
            int r6 = r9.mBitmapWidth     // Catch:{ RuntimeException -> 0x0036 }
            int r7 = r9.mBitmapHeight     // Catch:{ RuntimeException -> 0x0036 }
            android.graphics.Bitmap$Config r8 = r9.mBitmapConfig     // Catch:{ RuntimeException -> 0x0036 }
            com.facebook.common.references.CloseableReference r5 = r12.createBitmap(r6, r7, r8)     // Catch:{ RuntimeException -> 0x0036 }
            boolean r12 = r9.renderFrameInBitmap(r11, r5)     // Catch:{ all -> 0x0075 }
            if (r12 == 0) goto L_0x0033
            boolean r12 = r9.drawBitmapAndCache(r11, r5, r10, r0)     // Catch:{ all -> 0x0075 }
            if (r12 == 0) goto L_0x0033
            r12 = 1
            goto L_0x0034
        L_0x0033:
            r12 = 0
        L_0x0034:
            r0 = 3
            goto L_0x0067
        L_0x0036:
            r10 = move-exception
            java.lang.Class<?> r11 = TAG     // Catch:{ all -> 0x0075 }
            java.lang.String r12 = "Failed to create frame bitmap"
            com.facebook.common.logging.FLog.m85w(r11, r12, r10)     // Catch:{ all -> 0x0075 }
            com.facebook.common.references.CloseableReference.closeSafely(r5)
            return r4
        L_0x0042:
            com.facebook.fresco.animation.bitmap.BitmapFrameCache r12 = r9.mBitmapFrameCache     // Catch:{ all -> 0x0075 }
            int r1 = r9.mBitmapWidth     // Catch:{ all -> 0x0075 }
            int r6 = r9.mBitmapHeight     // Catch:{ all -> 0x0075 }
            com.facebook.common.references.CloseableReference r5 = r12.getBitmapToReuseForFrame(r11, r1, r6)     // Catch:{ all -> 0x0075 }
            boolean r12 = r9.renderFrameInBitmap(r11, r5)     // Catch:{ all -> 0x0075 }
            if (r12 == 0) goto L_0x005a
            boolean r12 = r9.drawBitmapAndCache(r11, r5, r10, r3)     // Catch:{ all -> 0x0075 }
            if (r12 == 0) goto L_0x005a
            r12 = 1
            goto L_0x0067
        L_0x005a:
            r12 = 0
            goto L_0x0067
        L_0x005c:
            com.facebook.fresco.animation.bitmap.BitmapFrameCache r12 = r9.mBitmapFrameCache     // Catch:{ all -> 0x0075 }
            com.facebook.common.references.CloseableReference r5 = r12.getCachedFrame(r11)     // Catch:{ all -> 0x0075 }
            boolean r12 = r9.drawBitmapAndCache(r11, r5, r10, r4)     // Catch:{ all -> 0x0075 }
            r0 = 1
        L_0x0067:
            com.facebook.common.references.CloseableReference.closeSafely(r5)
            if (r12 != 0) goto L_0x0074
            if (r0 != r2) goto L_0x006f
            goto L_0x0074
        L_0x006f:
            boolean r10 = r9.drawFrameOrFallback(r10, r11, r0)
            return r10
        L_0x0074:
            return r12
        L_0x0075:
            r10 = move-exception
            com.facebook.common.references.CloseableReference.closeSafely(r5)
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.fresco.animation.bitmap.BitmapAnimationBackend.drawFrameOrFallback(android.graphics.Canvas, int, int):boolean");
    }

    public void setAlpha(@IntRange(from = 0, mo116to = 255) int i) {
        this.mPaint.setAlpha(i);
    }

    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        this.mPaint.setColorFilter(colorFilter);
    }

    public void setBounds(@Nullable Rect rect) {
        this.mBounds = rect;
        this.mBitmapFrameRenderer.setBounds(rect);
        updateBitmapDimensions();
    }

    public int getIntrinsicWidth() {
        return this.mBitmapWidth;
    }

    public int getIntrinsicHeight() {
        return this.mBitmapHeight;
    }

    public int getSizeInBytes() {
        return this.mBitmapFrameCache.getSizeInBytes();
    }

    public void clear() {
        this.mBitmapFrameCache.clear();
    }

    public void onInactive() {
        clear();
    }

    private void updateBitmapDimensions() {
        this.mBitmapWidth = this.mBitmapFrameRenderer.getIntrinsicWidth();
        int i = -1;
        if (this.mBitmapWidth == -1) {
            Rect rect = this.mBounds;
            this.mBitmapWidth = rect == null ? -1 : rect.width();
        }
        this.mBitmapHeight = this.mBitmapFrameRenderer.getIntrinsicHeight();
        if (this.mBitmapHeight == -1) {
            Rect rect2 = this.mBounds;
            if (rect2 != null) {
                i = rect2.height();
            }
            this.mBitmapHeight = i;
        }
    }

    private boolean renderFrameInBitmap(int i, @Nullable CloseableReference<Bitmap> closeableReference) {
        if (!CloseableReference.isValid(closeableReference)) {
            return false;
        }
        boolean renderFrame = this.mBitmapFrameRenderer.renderFrame(i, (Bitmap) closeableReference.get());
        if (!renderFrame) {
            CloseableReference.closeSafely(closeableReference);
        }
        return renderFrame;
    }

    private boolean drawBitmapAndCache(int i, @Nullable CloseableReference<Bitmap> closeableReference, Canvas canvas, int i2) {
        if (!CloseableReference.isValid(closeableReference)) {
            return false;
        }
        if (this.mBounds == null) {
            canvas.drawBitmap((Bitmap) closeableReference.get(), 0.0f, 0.0f, this.mPaint);
        } else {
            canvas.drawBitmap((Bitmap) closeableReference.get(), null, this.mBounds, this.mPaint);
        }
        if (i2 != 3) {
            this.mBitmapFrameCache.onFrameRendered(i, closeableReference, i2);
        }
        FrameListener frameListener = this.mFrameListener;
        if (frameListener != null) {
            frameListener.onFrameDrawn(this, i, i2);
        }
        return true;
    }
}

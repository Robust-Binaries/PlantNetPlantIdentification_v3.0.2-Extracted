package com.facebook.fresco.animation.bitmap.preparation;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.util.SparseArray;
import com.facebook.common.logging.FLog;
import com.facebook.common.references.CloseableReference;
import com.facebook.fresco.animation.backend.AnimationBackend;
import com.facebook.fresco.animation.bitmap.BitmapFrameCache;
import com.facebook.fresco.animation.bitmap.BitmapFrameRenderer;
import com.facebook.imagepipeline.bitmaps.PlatformBitmapFactory;
import java.util.concurrent.ExecutorService;

public class DefaultBitmapFramePreparer implements BitmapFramePreparer {
    /* access modifiers changed from: private */
    public static final Class<?> TAG = DefaultBitmapFramePreparer.class;
    /* access modifiers changed from: private */
    public final Config mBitmapConfig;
    /* access modifiers changed from: private */
    public final BitmapFrameRenderer mBitmapFrameRenderer;
    private final ExecutorService mExecutorService;
    /* access modifiers changed from: private */
    public final SparseArray<Runnable> mPendingFrameDecodeJobs = new SparseArray<>();
    /* access modifiers changed from: private */
    public final PlatformBitmapFactory mPlatformBitmapFactory;

    private class FrameDecodeRunnable implements Runnable {
        private final AnimationBackend mAnimationBackend;
        private final BitmapFrameCache mBitmapFrameCache;
        private final int mFrameNumber;
        private final int mHashCode;

        public FrameDecodeRunnable(AnimationBackend animationBackend, BitmapFrameCache bitmapFrameCache, int i, int i2) {
            this.mAnimationBackend = animationBackend;
            this.mBitmapFrameCache = bitmapFrameCache;
            this.mFrameNumber = i;
            this.mHashCode = i2;
        }

        public void run() {
            try {
                if (this.mBitmapFrameCache.contains(this.mFrameNumber)) {
                    FLog.m69v(DefaultBitmapFramePreparer.TAG, "Frame %d is cached already.", (Object) Integer.valueOf(this.mFrameNumber));
                    synchronized (DefaultBitmapFramePreparer.this.mPendingFrameDecodeJobs) {
                        DefaultBitmapFramePreparer.this.mPendingFrameDecodeJobs.remove(this.mHashCode);
                    }
                    return;
                }
                if (prepareFrameAndCache(this.mFrameNumber, 1)) {
                    FLog.m69v(DefaultBitmapFramePreparer.TAG, "Prepared frame frame %d.", (Object) Integer.valueOf(this.mFrameNumber));
                } else {
                    FLog.m46e(DefaultBitmapFramePreparer.TAG, "Could not prepare frame %d.", Integer.valueOf(this.mFrameNumber));
                }
                synchronized (DefaultBitmapFramePreparer.this.mPendingFrameDecodeJobs) {
                    DefaultBitmapFramePreparer.this.mPendingFrameDecodeJobs.remove(this.mHashCode);
                }
            } catch (Throwable th) {
                synchronized (DefaultBitmapFramePreparer.this.mPendingFrameDecodeJobs) {
                    DefaultBitmapFramePreparer.this.mPendingFrameDecodeJobs.remove(this.mHashCode);
                    throw th;
                }
            }
        }

        private boolean prepareFrameAndCache(int i, int i2) {
            CloseableReference closeableReference;
            int i3;
            switch (i2) {
                case 1:
                    closeableReference = this.mBitmapFrameCache.getBitmapToReuseForFrame(i, this.mAnimationBackend.getIntrinsicWidth(), this.mAnimationBackend.getIntrinsicHeight());
                    i3 = 2;
                    break;
                case 2:
                    try {
                        closeableReference = DefaultBitmapFramePreparer.this.mPlatformBitmapFactory.createBitmap(this.mAnimationBackend.getIntrinsicWidth(), this.mAnimationBackend.getIntrinsicHeight(), DefaultBitmapFramePreparer.this.mBitmapConfig);
                        i3 = -1;
                        break;
                    } catch (RuntimeException e) {
                        FLog.m85w(DefaultBitmapFramePreparer.TAG, "Failed to create frame bitmap", (Throwable) e);
                        CloseableReference.closeSafely(null);
                        return false;
                    } catch (Throwable th) {
                        CloseableReference.closeSafely(null);
                        throw th;
                    }
                default:
                    CloseableReference.closeSafely(null);
                    return false;
            }
            boolean renderFrameAndCache = renderFrameAndCache(i, closeableReference, i2);
            CloseableReference.closeSafely(closeableReference);
            if (renderFrameAndCache || i3 == -1) {
                return renderFrameAndCache;
            }
            return prepareFrameAndCache(i, i3);
        }

        private boolean renderFrameAndCache(int i, CloseableReference<Bitmap> closeableReference, int i2) {
            if (!CloseableReference.isValid(closeableReference) || !DefaultBitmapFramePreparer.this.mBitmapFrameRenderer.renderFrame(i, (Bitmap) closeableReference.get())) {
                return false;
            }
            FLog.m69v(DefaultBitmapFramePreparer.TAG, "Frame %d ready.", (Object) Integer.valueOf(this.mFrameNumber));
            synchronized (DefaultBitmapFramePreparer.this.mPendingFrameDecodeJobs) {
                this.mBitmapFrameCache.onFramePrepared(this.mFrameNumber, closeableReference, i2);
            }
            return true;
        }
    }

    public DefaultBitmapFramePreparer(PlatformBitmapFactory platformBitmapFactory, BitmapFrameRenderer bitmapFrameRenderer, Config config, ExecutorService executorService) {
        this.mPlatformBitmapFactory = platformBitmapFactory;
        this.mBitmapFrameRenderer = bitmapFrameRenderer;
        this.mBitmapConfig = config;
        this.mExecutorService = executorService;
    }

    public boolean prepareFrame(BitmapFrameCache bitmapFrameCache, AnimationBackend animationBackend, int i) {
        int uniqueId = getUniqueId(animationBackend, i);
        synchronized (this.mPendingFrameDecodeJobs) {
            if (this.mPendingFrameDecodeJobs.get(uniqueId) != null) {
                FLog.m69v(TAG, "Already scheduled decode job for frame %d", (Object) Integer.valueOf(i));
                return true;
            } else if (bitmapFrameCache.contains(i)) {
                FLog.m69v(TAG, "Frame %d is cached already.", (Object) Integer.valueOf(i));
                return true;
            } else {
                FrameDecodeRunnable frameDecodeRunnable = new FrameDecodeRunnable(animationBackend, bitmapFrameCache, i, uniqueId);
                this.mPendingFrameDecodeJobs.put(uniqueId, frameDecodeRunnable);
                this.mExecutorService.execute(frameDecodeRunnable);
                return true;
            }
        }
    }

    private static int getUniqueId(AnimationBackend animationBackend, int i) {
        return (animationBackend.hashCode() * 31) + i;
    }
}

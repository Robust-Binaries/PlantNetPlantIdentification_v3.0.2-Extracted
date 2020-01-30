package com.facebook.imagepipeline.platform;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Rect;
import android.support.p000v4.util.Pools.SynchronizedPool;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.internal.VisibleForTesting;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.streams.LimitedInputStream;
import com.facebook.common.streams.TailAppendingInputStream;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.memory.BitmapPool;
import java.io.InputStream;
import java.nio.ByteBuffer;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

@TargetApi(21)
@ThreadSafe
public class ArtDecoder implements PlatformDecoder {
    private static final int DECODE_BUFFER_SIZE = 16384;
    private static final byte[] EOI_TAIL = {-1, -39};
    private static final Class<?> TAG = ArtDecoder.class;
    private final BitmapPool mBitmapPool;
    @VisibleForTesting
    final SynchronizedPool<ByteBuffer> mDecodeBuffers;

    public ArtDecoder(BitmapPool bitmapPool, int i, SynchronizedPool synchronizedPool) {
        this.mBitmapPool = bitmapPool;
        this.mDecodeBuffers = synchronizedPool;
        for (int i2 = 0; i2 < i; i2++) {
            this.mDecodeBuffers.release(ByteBuffer.allocate(16384));
        }
    }

    public CloseableReference<Bitmap> decodeFromEncodedImage(EncodedImage encodedImage, Config config, @Nullable Rect rect) {
        Options decodeOptionsForStream = getDecodeOptionsForStream(encodedImage, config);
        boolean z = decodeOptionsForStream.inPreferredConfig != Config.ARGB_8888;
        try {
            return decodeStaticImageFromStream(encodedImage.getInputStream(), decodeOptionsForStream, rect);
        } catch (RuntimeException e) {
            if (z) {
                return decodeFromEncodedImage(encodedImage, Config.ARGB_8888, rect);
            }
            throw e;
        }
    }

    public CloseableReference<Bitmap> decodeJPEGFromEncodedImage(EncodedImage encodedImage, Config config, @Nullable Rect rect, int i) {
        boolean isCompleteAt = encodedImage.isCompleteAt(i);
        Options decodeOptionsForStream = getDecodeOptionsForStream(encodedImage, config);
        InputStream inputStream = encodedImage.getInputStream();
        Preconditions.checkNotNull(inputStream);
        if (encodedImage.getSize() > i) {
            inputStream = new LimitedInputStream(inputStream, i);
        }
        InputStream tailAppendingInputStream = !isCompleteAt ? new TailAppendingInputStream(inputStream, EOI_TAIL) : inputStream;
        boolean z = decodeOptionsForStream.inPreferredConfig != Config.ARGB_8888;
        try {
            return decodeStaticImageFromStream(tailAppendingInputStream, decodeOptionsForStream, rect);
        } catch (RuntimeException e) {
            if (z) {
                return decodeFromEncodedImage(encodedImage, Config.ARGB_8888, rect);
            }
            throw e;
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        com.facebook.common.logging.FLog.m46e(TAG, "Could not decode region %s, decoding full bitmap instead.", r11);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0060, code lost:
        if (r0 != null) goto L_0x0062;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:?, code lost:
        r0.recycle();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0066, code lost:
        r10 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0069, code lost:
        r0.recycle();
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:24:0x0054 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:57:0x00b7 */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x0069 A[Catch:{ IllegalArgumentException -> 0x0099, RuntimeException -> 0x0092, all -> 0x0090, IOException -> 0x00b7 }] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0070 A[Catch:{ IllegalArgumentException -> 0x0099, RuntimeException -> 0x0092, all -> 0x0090, IOException -> 0x00b7 }] */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x007b  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x0082  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.facebook.common.references.CloseableReference<android.graphics.Bitmap> decodeStaticImageFromStream(java.io.InputStream r9, android.graphics.BitmapFactory.Options r10, @javax.annotation.Nullable android.graphics.Rect r11) {
        /*
            r8 = this;
            com.facebook.common.internal.Preconditions.checkNotNull(r9)
            int r0 = r10.outWidth
            int r1 = r10.outHeight
            if (r11 == 0) goto L_0x0011
            int r0 = r11.width()
            int r1 = r11.height()
        L_0x0011:
            android.graphics.Bitmap$Config r2 = r10.inPreferredConfig
            int r2 = com.facebook.imageutils.BitmapUtil.getSizeInByteForBitmap(r0, r1, r2)
            com.facebook.imagepipeline.memory.BitmapPool r3 = r8.mBitmapPool
            java.lang.Object r2 = r3.get(r2)
            android.graphics.Bitmap r2 = (android.graphics.Bitmap) r2
            if (r2 == 0) goto L_0x00be
            r10.inBitmap = r2
            android.support.v4.util.Pools$SynchronizedPool<java.nio.ByteBuffer> r3 = r8.mDecodeBuffers
            java.lang.Object r3 = r3.acquire()
            java.nio.ByteBuffer r3 = (java.nio.ByteBuffer) r3
            if (r3 != 0) goto L_0x0033
            r3 = 16384(0x4000, float:2.2959E-41)
            java.nio.ByteBuffer r3 = java.nio.ByteBuffer.allocate(r3)
        L_0x0033:
            byte[] r4 = r3.array()     // Catch:{ IllegalArgumentException -> 0x0099, RuntimeException -> 0x0092 }
            r10.inTempStorage = r4     // Catch:{ IllegalArgumentException -> 0x0099, RuntimeException -> 0x0092 }
            r4 = 0
            if (r11 == 0) goto L_0x006d
            r5 = 1
            android.graphics.Bitmap$Config r6 = r10.inPreferredConfig     // Catch:{ IOException -> 0x0053, all -> 0x0050 }
            r2.reconfigure(r0, r1, r6)     // Catch:{ IOException -> 0x0053, all -> 0x0050 }
            android.graphics.BitmapRegionDecoder r0 = android.graphics.BitmapRegionDecoder.newInstance(r9, r5)     // Catch:{ IOException -> 0x0053, all -> 0x0050 }
            android.graphics.Bitmap r11 = r0.decodeRegion(r11, r10)     // Catch:{ IOException -> 0x0054 }
            if (r0 == 0) goto L_0x006e
            r0.recycle()     // Catch:{ IllegalArgumentException -> 0x0099, RuntimeException -> 0x0092 }
            goto L_0x006e
        L_0x0050:
            r10 = move-exception
            r0 = r4
            goto L_0x0067
        L_0x0053:
            r0 = r4
        L_0x0054:
            java.lang.Class<?> r1 = TAG     // Catch:{ all -> 0x0066 }
            java.lang.String r6 = "Could not decode region %s, decoding full bitmap instead."
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch:{ all -> 0x0066 }
            r7 = 0
            r5[r7] = r11     // Catch:{ all -> 0x0066 }
            com.facebook.common.logging.FLog.m46e(r1, r6, r5)     // Catch:{ all -> 0x0066 }
            if (r0 == 0) goto L_0x006d
            r0.recycle()     // Catch:{ IllegalArgumentException -> 0x0099, RuntimeException -> 0x0092 }
            goto L_0x006d
        L_0x0066:
            r10 = move-exception
        L_0x0067:
            if (r0 == 0) goto L_0x006c
            r0.recycle()     // Catch:{ IllegalArgumentException -> 0x0099, RuntimeException -> 0x0092 }
        L_0x006c:
            throw r10     // Catch:{ IllegalArgumentException -> 0x0099, RuntimeException -> 0x0092 }
        L_0x006d:
            r11 = r4
        L_0x006e:
            if (r11 != 0) goto L_0x0074
            android.graphics.Bitmap r11 = android.graphics.BitmapFactory.decodeStream(r9, r4, r10)     // Catch:{ IllegalArgumentException -> 0x0099, RuntimeException -> 0x0092 }
        L_0x0074:
            android.support.v4.util.Pools$SynchronizedPool<java.nio.ByteBuffer> r9 = r8.mDecodeBuffers
            r9.release(r3)
            if (r2 != r11) goto L_0x0082
            com.facebook.imagepipeline.memory.BitmapPool r9 = r8.mBitmapPool
            com.facebook.common.references.CloseableReference r9 = com.facebook.common.references.CloseableReference.m113of(r11, r9)
            return r9
        L_0x0082:
            com.facebook.imagepipeline.memory.BitmapPool r9 = r8.mBitmapPool
            r9.release(r2)
            r11.recycle()
            java.lang.IllegalStateException r9 = new java.lang.IllegalStateException
            r9.<init>()
            throw r9
        L_0x0090:
            r9 = move-exception
            goto L_0x00b8
        L_0x0092:
            r9 = move-exception
            com.facebook.imagepipeline.memory.BitmapPool r10 = r8.mBitmapPool     // Catch:{ all -> 0x0090 }
            r10.release(r2)     // Catch:{ all -> 0x0090 }
            throw r9     // Catch:{ all -> 0x0090 }
        L_0x0099:
            r10 = move-exception
            com.facebook.imagepipeline.memory.BitmapPool r11 = r8.mBitmapPool     // Catch:{ all -> 0x0090 }
            r11.release(r2)     // Catch:{ all -> 0x0090 }
            r9.reset()     // Catch:{ IOException -> 0x00b7 }
            android.graphics.Bitmap r9 = android.graphics.BitmapFactory.decodeStream(r9)     // Catch:{ IOException -> 0x00b7 }
            if (r9 == 0) goto L_0x00b6
            com.facebook.imagepipeline.bitmaps.SimpleBitmapReleaser r11 = com.facebook.imagepipeline.bitmaps.SimpleBitmapReleaser.getInstance()     // Catch:{ IOException -> 0x00b7 }
            com.facebook.common.references.CloseableReference r9 = com.facebook.common.references.CloseableReference.m113of(r9, r11)     // Catch:{ IOException -> 0x00b7 }
            android.support.v4.util.Pools$SynchronizedPool<java.nio.ByteBuffer> r10 = r8.mDecodeBuffers
            r10.release(r3)
            return r9
        L_0x00b6:
            throw r10     // Catch:{ IOException -> 0x00b7 }
        L_0x00b7:
            throw r10     // Catch:{ all -> 0x0090 }
        L_0x00b8:
            android.support.v4.util.Pools$SynchronizedPool<java.nio.ByteBuffer> r10 = r8.mDecodeBuffers
            r10.release(r3)
            throw r9
        L_0x00be:
            java.lang.NullPointerException r9 = new java.lang.NullPointerException
            java.lang.String r10 = "BitmapPool.get returned null"
            r9.<init>(r10)
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.imagepipeline.platform.ArtDecoder.decodeStaticImageFromStream(java.io.InputStream, android.graphics.BitmapFactory$Options, android.graphics.Rect):com.facebook.common.references.CloseableReference");
    }

    private static Options getDecodeOptionsForStream(EncodedImage encodedImage, Config config) {
        Options options = new Options();
        options.inSampleSize = encodedImage.getSampleSize();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(encodedImage.getInputStream(), null, options);
        if (options.outWidth == -1 || options.outHeight == -1) {
            throw new IllegalArgumentException();
        }
        options.inJustDecodeBounds = false;
        options.inDither = true;
        options.inPreferredConfig = config;
        options.inMutable = true;
        return options;
    }
}

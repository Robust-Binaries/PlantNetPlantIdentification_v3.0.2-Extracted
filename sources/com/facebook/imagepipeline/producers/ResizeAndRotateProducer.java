package com.facebook.imagepipeline.producers;

import com.facebook.common.internal.ImmutableList;
import com.facebook.common.internal.ImmutableMap;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.internal.VisibleForTesting;
import com.facebook.common.memory.PooledByteBufferFactory;
import com.facebook.common.util.TriState;
import com.facebook.imageformat.DefaultImageFormats;
import com.facebook.imageformat.ImageFormat;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.common.RotationOptions;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.producers.JobScheduler.JobRunnable;
import com.facebook.imagepipeline.request.ImageRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;

public class ResizeAndRotateProducer implements Producer<EncodedImage> {
    @VisibleForTesting
    static final int DEFAULT_JPEG_QUALITY = 85;
    private static final String DOWNSAMPLE_ENUMERATOR_KEY = "downsampleEnumerator";
    private static final String FRACTION_KEY = "Fraction";
    private static final int FULL_ROUND = 360;
    /* access modifiers changed from: private */
    public static final ImmutableList<Integer> INVERTED_EXIF_ORIENTATIONS = ImmutableList.m18of(Integer.valueOf(2), Integer.valueOf(7), Integer.valueOf(4), Integer.valueOf(5));
    @VisibleForTesting
    static final int MAX_JPEG_SCALE_NUMERATOR = 8;
    @VisibleForTesting
    static final int MIN_TRANSFORM_INTERVAL_MS = 100;
    private static final String ORIGINAL_SIZE_KEY = "Original size";
    public static final String PRODUCER_NAME = "ResizeAndRotateProducer";
    private static final String REQUESTED_SIZE_KEY = "Requested size";
    private static final String ROTATION_ANGLE_KEY = "rotationAngle";
    private static final String SOFTWARE_ENUMERATOR_KEY = "softwareEnumerator";
    /* access modifiers changed from: private */
    public final Executor mExecutor;
    private final Producer<EncodedImage> mInputProducer;
    /* access modifiers changed from: private */
    public final PooledByteBufferFactory mPooledByteBufferFactory;
    /* access modifiers changed from: private */
    public final boolean mResizingEnabled;
    /* access modifiers changed from: private */
    public final boolean mUseDownsamplingRatio;

    private class TransformingConsumer extends DelegatingConsumer<EncodedImage, EncodedImage> {
        /* access modifiers changed from: private */
        public boolean mIsCancelled = false;
        /* access modifiers changed from: private */
        public final JobScheduler mJobScheduler;
        /* access modifiers changed from: private */
        public final ProducerContext mProducerContext;

        public TransformingConsumer(final Consumer<EncodedImage> consumer, ProducerContext producerContext) {
            super(consumer);
            this.mProducerContext = producerContext;
            this.mJobScheduler = new JobScheduler(ResizeAndRotateProducer.this.mExecutor, new JobRunnable(ResizeAndRotateProducer.this) {
                public void run(EncodedImage encodedImage, int i) {
                    TransformingConsumer.this.doTransform(encodedImage, i);
                }
            }, 100);
            this.mProducerContext.addCallbacks(new BaseProducerContextCallbacks(ResizeAndRotateProducer.this) {
                public void onIsIntermediateResultExpectedChanged() {
                    if (TransformingConsumer.this.mProducerContext.isIntermediateResultExpected()) {
                        TransformingConsumer.this.mJobScheduler.scheduleJob();
                    }
                }

                public void onCancellationRequested() {
                    TransformingConsumer.this.mJobScheduler.clearJob();
                    TransformingConsumer.this.mIsCancelled = true;
                    consumer.onCancellation();
                }
            });
        }

        /* access modifiers changed from: protected */
        public void onNewResultImpl(@Nullable EncodedImage encodedImage, int i) {
            if (!this.mIsCancelled) {
                boolean isLast = isLast(i);
                if (encodedImage == null) {
                    if (isLast) {
                        getConsumer().onNewResult(null, 1);
                    }
                    return;
                }
                TriState access$600 = ResizeAndRotateProducer.shouldTransform(this.mProducerContext.getImageRequest(), encodedImage, ResizeAndRotateProducer.this.mResizingEnabled);
                if (!isLast && access$600 == TriState.UNSET) {
                    return;
                }
                if (access$600 != TriState.YES) {
                    if (!(this.mProducerContext.getImageRequest().getRotationOptions().canDeferUntilRendered() || encodedImage.getRotationAngle() == 0 || encodedImage.getRotationAngle() == -1)) {
                        encodedImage = moveImage(encodedImage);
                        encodedImage.setRotationAngle(0);
                    }
                    getConsumer().onNewResult(encodedImage, i);
                } else if (this.mJobScheduler.updateJob(encodedImage, i)) {
                    if (isLast || this.mProducerContext.isIntermediateResultExpected()) {
                        this.mJobScheduler.scheduleJob();
                    }
                }
            }
        }

        private EncodedImage moveImage(EncodedImage encodedImage) {
            EncodedImage cloneOrNull = EncodedImage.cloneOrNull(encodedImage);
            encodedImage.close();
            return cloneOrNull;
        }

        /* access modifiers changed from: private */
        /* JADX WARNING: Removed duplicated region for block: B:51:0x00ff A[Catch:{ all -> 0x010d }] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void doTransform(com.facebook.imagepipeline.image.EncodedImage r17, int r18) {
            /*
                r16 = this;
                r8 = r16
                r0 = r17
                com.facebook.imagepipeline.producers.ProducerContext r1 = r8.mProducerContext
                com.facebook.imagepipeline.producers.ProducerListener r1 = r1.getListener()
                com.facebook.imagepipeline.producers.ProducerContext r2 = r8.mProducerContext
                java.lang.String r2 = r2.getId()
                java.lang.String r3 = "ResizeAndRotateProducer"
                r1.onProducerStart(r2, r3)
                com.facebook.imagepipeline.producers.ProducerContext r1 = r8.mProducerContext
                com.facebook.imagepipeline.request.ImageRequest r3 = r1.getImageRequest()
                com.facebook.imagepipeline.producers.ResizeAndRotateProducer r1 = com.facebook.imagepipeline.producers.ResizeAndRotateProducer.this
                com.facebook.common.memory.PooledByteBufferFactory r1 = r1.mPooledByteBufferFactory
                com.facebook.common.memory.PooledByteBufferOutputStream r9 = r1.newOutputStream()
                r10 = 0
                com.facebook.imagepipeline.producers.ResizeAndRotateProducer r1 = com.facebook.imagepipeline.producers.ResizeAndRotateProducer.this     // Catch:{ Exception -> 0x00e4, all -> 0x00e1 }
                boolean r1 = r1.mResizingEnabled     // Catch:{ Exception -> 0x00e4, all -> 0x00e1 }
                int r6 = com.facebook.imagepipeline.producers.ResizeAndRotateProducer.getSoftwareNumerator(r3, r0, r1)     // Catch:{ Exception -> 0x00e4, all -> 0x00e1 }
                int r11 = com.facebook.imagepipeline.producers.DownsampleUtil.determineSampleSize(r3, r0)     // Catch:{ Exception -> 0x00e4, all -> 0x00e1 }
                int r5 = com.facebook.imagepipeline.producers.ResizeAndRotateProducer.calculateDownsampleNumerator(r11)     // Catch:{ Exception -> 0x00e4, all -> 0x00e1 }
                com.facebook.imagepipeline.producers.ResizeAndRotateProducer r1 = com.facebook.imagepipeline.producers.ResizeAndRotateProducer.this     // Catch:{ Exception -> 0x00e4, all -> 0x00e1 }
                boolean r1 = r1.mUseDownsamplingRatio     // Catch:{ Exception -> 0x00e4, all -> 0x00e1 }
                if (r1 == 0) goto L_0x0042
                r12 = r5
                goto L_0x0043
            L_0x0042:
                r12 = r6
            L_0x0043:
                java.io.InputStream r13 = r17.getInputStream()     // Catch:{ Exception -> 0x00e4, all -> 0x00e1 }
                com.facebook.common.internal.ImmutableList r1 = com.facebook.imagepipeline.producers.ResizeAndRotateProducer.INVERTED_EXIF_ORIENTATIONS     // Catch:{ Exception -> 0x00dd }
                int r2 = r17.getExifOrientation()     // Catch:{ Exception -> 0x00dd }
                java.lang.Integer r2 = java.lang.Integer.valueOf(r2)     // Catch:{ Exception -> 0x00dd }
                boolean r1 = r1.contains(r2)     // Catch:{ Exception -> 0x00dd }
                r14 = 85
                if (r1 == 0) goto L_0x0071
                com.facebook.imagepipeline.common.RotationOptions r1 = r3.getRotationOptions()     // Catch:{ Exception -> 0x00dd }
                int r15 = com.facebook.imagepipeline.producers.ResizeAndRotateProducer.getForceRotatedInvertedExifOrientation(r1, r0)     // Catch:{ Exception -> 0x00dd }
                r7 = 0
                r1 = r16
                r2 = r17
                r4 = r12
                java.util.Map r10 = r1.getExtraMap(r2, r3, r4, r5, r6, r7)     // Catch:{ Exception -> 0x00dd }
                com.facebook.imagepipeline.nativecode.JpegTranscoder.transcodeJpegWithExifOrientation(r13, r9, r15, r12, r14)     // Catch:{ Exception -> 0x00dd }
                goto L_0x0086
            L_0x0071:
                com.facebook.imagepipeline.common.RotationOptions r1 = r3.getRotationOptions()     // Catch:{ Exception -> 0x00dd }
                int r15 = com.facebook.imagepipeline.producers.ResizeAndRotateProducer.getRotationAngle(r1, r0)     // Catch:{ Exception -> 0x00dd }
                r1 = r16
                r2 = r17
                r4 = r12
                r7 = r15
                java.util.Map r10 = r1.getExtraMap(r2, r3, r4, r5, r6, r7)     // Catch:{ Exception -> 0x00dd }
                com.facebook.imagepipeline.nativecode.JpegTranscoder.transcodeJpeg(r13, r9, r15, r12, r14)     // Catch:{ Exception -> 0x00dd }
            L_0x0086:
                com.facebook.common.memory.PooledByteBuffer r0 = r9.toByteBuffer()     // Catch:{ Exception -> 0x00dd }
                com.facebook.common.references.CloseableReference r1 = com.facebook.common.references.CloseableReference.m112of(r0)     // Catch:{ Exception -> 0x00dd }
                com.facebook.imagepipeline.image.EncodedImage r2 = new com.facebook.imagepipeline.image.EncodedImage     // Catch:{ all -> 0x00d4 }
                r2.<init>(r1)     // Catch:{ all -> 0x00d4 }
                com.facebook.imageformat.ImageFormat r0 = com.facebook.imageformat.DefaultImageFormats.JPEG     // Catch:{ all -> 0x00d4 }
                r2.setImageFormat(r0)     // Catch:{ all -> 0x00d4 }
                r2.parseMetaData()     // Catch:{ all -> 0x00cb }
                com.facebook.imagepipeline.producers.ProducerContext r0 = r8.mProducerContext     // Catch:{ all -> 0x00cb }
                com.facebook.imagepipeline.producers.ProducerListener r0 = r0.getListener()     // Catch:{ all -> 0x00cb }
                com.facebook.imagepipeline.producers.ProducerContext r3 = r8.mProducerContext     // Catch:{ all -> 0x00cb }
                java.lang.String r3 = r3.getId()     // Catch:{ all -> 0x00cb }
                java.lang.String r4 = "ResizeAndRotateProducer"
                r0.onProducerFinishWithSuccess(r3, r4, r10)     // Catch:{ all -> 0x00cb }
                r0 = 1
                if (r11 == r0) goto L_0x00b3
                r0 = r18 | 16
                r3 = r0
                goto L_0x00b5
            L_0x00b3:
                r3 = r18
            L_0x00b5:
                com.facebook.imagepipeline.producers.Consumer r0 = r16.getConsumer()     // Catch:{ all -> 0x00c9 }
                r0.onNewResult(r2, r3)     // Catch:{ all -> 0x00c9 }
                com.facebook.imagepipeline.image.EncodedImage.closeSafely(r2)     // Catch:{ all -> 0x00d2 }
                com.facebook.common.references.CloseableReference.closeSafely(r1)     // Catch:{ Exception -> 0x00db }
                com.facebook.common.internal.Closeables.closeQuietly(r13)
                r9.close()
                return
            L_0x00c9:
                r0 = move-exception
                goto L_0x00ce
            L_0x00cb:
                r0 = move-exception
                r3 = r18
            L_0x00ce:
                com.facebook.imagepipeline.image.EncodedImage.closeSafely(r2)     // Catch:{ all -> 0x00d2 }
                throw r0     // Catch:{ all -> 0x00d2 }
            L_0x00d2:
                r0 = move-exception
                goto L_0x00d7
            L_0x00d4:
                r0 = move-exception
                r3 = r18
            L_0x00d7:
                com.facebook.common.references.CloseableReference.closeSafely(r1)     // Catch:{ Exception -> 0x00db }
                throw r0     // Catch:{ Exception -> 0x00db }
            L_0x00db:
                r0 = move-exception
                goto L_0x00e8
            L_0x00dd:
                r0 = move-exception
                r3 = r18
                goto L_0x00e8
            L_0x00e1:
                r0 = move-exception
                r13 = r10
                goto L_0x010e
            L_0x00e4:
                r0 = move-exception
                r3 = r18
                r13 = r10
            L_0x00e8:
                com.facebook.imagepipeline.producers.ProducerContext r1 = r8.mProducerContext     // Catch:{ all -> 0x010d }
                com.facebook.imagepipeline.producers.ProducerListener r1 = r1.getListener()     // Catch:{ all -> 0x010d }
                com.facebook.imagepipeline.producers.ProducerContext r2 = r8.mProducerContext     // Catch:{ all -> 0x010d }
                java.lang.String r2 = r2.getId()     // Catch:{ all -> 0x010d }
                java.lang.String r4 = "ResizeAndRotateProducer"
                r1.onProducerFinishWithFailure(r2, r4, r0, r10)     // Catch:{ all -> 0x010d }
                boolean r1 = isLast(r3)     // Catch:{ all -> 0x010d }
                if (r1 == 0) goto L_0x0106
                com.facebook.imagepipeline.producers.Consumer r1 = r16.getConsumer()     // Catch:{ all -> 0x010d }
                r1.onFailure(r0)     // Catch:{ all -> 0x010d }
            L_0x0106:
                com.facebook.common.internal.Closeables.closeQuietly(r13)
                r9.close()
                return
            L_0x010d:
                r0 = move-exception
            L_0x010e:
                com.facebook.common.internal.Closeables.closeQuietly(r13)
                r9.close()
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.facebook.imagepipeline.producers.ResizeAndRotateProducer.TransformingConsumer.doTransform(com.facebook.imagepipeline.image.EncodedImage, int):void");
        }

        private Map<String, String> getExtraMap(EncodedImage encodedImage, ImageRequest imageRequest, int i, int i2, int i3, int i4) {
            String str;
            String str2;
            if (!this.mProducerContext.getListener().requiresExtraMap(this.mProducerContext.getId())) {
                return null;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(encodedImage.getWidth());
            sb.append("x");
            sb.append(encodedImage.getHeight());
            String sb2 = sb.toString();
            if (imageRequest.getResizeOptions() != null) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(imageRequest.getResizeOptions().width);
                sb3.append("x");
                sb3.append(imageRequest.getResizeOptions().height);
                str = sb3.toString();
            } else {
                str = "Unspecified";
            }
            if (i > 0) {
                StringBuilder sb4 = new StringBuilder();
                sb4.append(i);
                sb4.append("/8");
                str2 = sb4.toString();
            } else {
                str2 = "";
            }
            HashMap hashMap = new HashMap();
            hashMap.put(ResizeAndRotateProducer.ORIGINAL_SIZE_KEY, sb2);
            hashMap.put(ResizeAndRotateProducer.REQUESTED_SIZE_KEY, str);
            hashMap.put(ResizeAndRotateProducer.FRACTION_KEY, str2);
            hashMap.put("queueTime", String.valueOf(this.mJobScheduler.getQueuedTime()));
            hashMap.put(ResizeAndRotateProducer.DOWNSAMPLE_ENUMERATOR_KEY, Integer.toString(i2));
            hashMap.put(ResizeAndRotateProducer.SOFTWARE_ENUMERATOR_KEY, Integer.toString(i3));
            hashMap.put(ResizeAndRotateProducer.ROTATION_ANGLE_KEY, Integer.toString(i4));
            return ImmutableMap.copyOf(hashMap);
        }
    }

    @VisibleForTesting
    static int roundNumerator(float f, float f2) {
        return (int) (f2 + (f * 8.0f));
    }

    private static boolean shouldResize(int i) {
        return i < 8;
    }

    public ResizeAndRotateProducer(Executor executor, PooledByteBufferFactory pooledByteBufferFactory, boolean z, Producer<EncodedImage> producer, boolean z2) {
        this.mExecutor = (Executor) Preconditions.checkNotNull(executor);
        this.mPooledByteBufferFactory = (PooledByteBufferFactory) Preconditions.checkNotNull(pooledByteBufferFactory);
        this.mResizingEnabled = z;
        this.mInputProducer = (Producer) Preconditions.checkNotNull(producer);
        this.mUseDownsamplingRatio = z2;
    }

    public void produceResults(Consumer<EncodedImage> consumer, ProducerContext producerContext) {
        this.mInputProducer.produceResults(new TransformingConsumer(consumer, producerContext), producerContext);
    }

    /* access modifiers changed from: private */
    public static TriState shouldTransform(ImageRequest imageRequest, EncodedImage encodedImage, boolean z) {
        if (encodedImage == null || encodedImage.getImageFormat() == ImageFormat.UNKNOWN) {
            return TriState.UNSET;
        }
        if (encodedImage.getImageFormat() != DefaultImageFormats.JPEG) {
            return TriState.NO;
        }
        return TriState.valueOf(shouldRotate(imageRequest.getRotationOptions(), encodedImage) || shouldResize(getSoftwareNumerator(imageRequest, encodedImage, z)));
    }

    @VisibleForTesting
    static float determineResizeRatio(ResizeOptions resizeOptions, int i, int i2) {
        if (resizeOptions == null) {
            return 1.0f;
        }
        float f = (float) i;
        float f2 = (float) i2;
        float max = Math.max(((float) resizeOptions.width) / f, ((float) resizeOptions.height) / f2);
        if (f * max > resizeOptions.maxBitmapSize) {
            max = resizeOptions.maxBitmapSize / f;
        }
        if (f2 * max > resizeOptions.maxBitmapSize) {
            max = resizeOptions.maxBitmapSize / f2;
        }
        return max;
    }

    /* access modifiers changed from: private */
    public static int getSoftwareNumerator(ImageRequest imageRequest, EncodedImage encodedImage, boolean z) {
        int i;
        int i2;
        if (!z) {
            return 8;
        }
        ResizeOptions resizeOptions = imageRequest.getResizeOptions();
        if (resizeOptions == null) {
            return 8;
        }
        int rotationAngle = getRotationAngle(imageRequest.getRotationOptions(), encodedImage);
        boolean z2 = false;
        int forceRotatedInvertedExifOrientation = INVERTED_EXIF_ORIENTATIONS.contains(Integer.valueOf(encodedImage.getExifOrientation())) ? getForceRotatedInvertedExifOrientation(imageRequest.getRotationOptions(), encodedImage) : 0;
        if (rotationAngle == 90 || rotationAngle == 270 || forceRotatedInvertedExifOrientation == 5 || forceRotatedInvertedExifOrientation == 7) {
            z2 = true;
        }
        if (z2) {
            i = encodedImage.getHeight();
        } else {
            i = encodedImage.getWidth();
        }
        if (z2) {
            i2 = encodedImage.getWidth();
        } else {
            i2 = encodedImage.getHeight();
        }
        int roundNumerator = roundNumerator(determineResizeRatio(resizeOptions, i, i2), resizeOptions.roundUpFraction);
        if (roundNumerator > 8) {
            return 8;
        }
        if (roundNumerator < 1) {
            roundNumerator = 1;
        }
        return roundNumerator;
    }

    /* access modifiers changed from: private */
    public static int getRotationAngle(RotationOptions rotationOptions, EncodedImage encodedImage) {
        if (!rotationOptions.rotationEnabled()) {
            return 0;
        }
        int extractOrientationFromMetadata = extractOrientationFromMetadata(encodedImage);
        if (rotationOptions.useImageMetadata()) {
            return extractOrientationFromMetadata;
        }
        return (extractOrientationFromMetadata + rotationOptions.getForcedAngle()) % FULL_ROUND;
    }

    /* access modifiers changed from: private */
    public static int getForceRotatedInvertedExifOrientation(RotationOptions rotationOptions, EncodedImage encodedImage) {
        int indexOf = INVERTED_EXIF_ORIENTATIONS.indexOf(Integer.valueOf(encodedImage.getExifOrientation()));
        if (indexOf >= 0) {
            int i = 0;
            if (!rotationOptions.useImageMetadata()) {
                i = rotationOptions.getForcedAngle();
            }
            int i2 = i / 90;
            ImmutableList<Integer> immutableList = INVERTED_EXIF_ORIENTATIONS;
            return ((Integer) immutableList.get((indexOf + i2) % immutableList.size())).intValue();
        }
        throw new IllegalArgumentException("Only accepts inverted exif orientations");
    }

    private static int extractOrientationFromMetadata(EncodedImage encodedImage) {
        int rotationAngle = encodedImage.getRotationAngle();
        if (rotationAngle == 90 || rotationAngle == 180 || rotationAngle == 270) {
            return encodedImage.getRotationAngle();
        }
        return 0;
    }

    private static boolean shouldRotate(RotationOptions rotationOptions, EncodedImage encodedImage) {
        return !rotationOptions.canDeferUntilRendered() && (getRotationAngle(rotationOptions, encodedImage) != 0 || shouldRotateUsingExifOrientation(rotationOptions, encodedImage));
    }

    private static boolean shouldRotateUsingExifOrientation(RotationOptions rotationOptions, EncodedImage encodedImage) {
        if (rotationOptions.rotationEnabled() && !rotationOptions.canDeferUntilRendered()) {
            return INVERTED_EXIF_ORIENTATIONS.contains(Integer.valueOf(encodedImage.getExifOrientation()));
        }
        encodedImage.setExifOrientation(0);
        return false;
    }

    @VisibleForTesting
    static int calculateDownsampleNumerator(int i) {
        return Math.max(1, 8 / i);
    }
}

package com.facebook.drawee.backends.pipeline.info;

import com.facebook.common.internal.Objects;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import javax.annotation.Nullable;

public class ImagePerfData {
    public static final int UNSET = -1;
    @Nullable
    private final Object mCallerContext;
    private final long mControllerCancelTimeMs;
    private final long mControllerFailureTimeMs;
    private final long mControllerFinalImageSetTimeMs;
    @Nullable
    private final String mControllerId;
    private final long mControllerIntermediateImageSetTimeMs;
    private final long mControllerSubmitTimeMs;
    @Nullable
    private final ImageInfo mImageInfo;
    private final int mImageOrigin;
    @Nullable
    private final ImageRequest mImageRequest;
    private final long mImageRequestEndTimeMs;
    private final long mImageRequestStartTimeMs;
    private final boolean mIsCanceled;
    private final boolean mIsPrefetch;
    private final boolean mIsSuccessful;
    @Nullable
    private final String mRequestId;

    public ImagePerfData(@Nullable String str, @Nullable String str2, @Nullable ImageRequest imageRequest, @Nullable Object obj, @Nullable ImageInfo imageInfo, long j, long j2, long j3, long j4, long j5, long j6, long j7, int i, boolean z, boolean z2, boolean z3) {
        this.mControllerId = str;
        this.mRequestId = str2;
        this.mImageRequest = imageRequest;
        this.mCallerContext = obj;
        this.mImageInfo = imageInfo;
        this.mControllerSubmitTimeMs = j;
        this.mControllerIntermediateImageSetTimeMs = j2;
        this.mControllerFinalImageSetTimeMs = j3;
        this.mControllerFailureTimeMs = j4;
        this.mControllerCancelTimeMs = j5;
        this.mImageRequestStartTimeMs = j6;
        this.mImageRequestEndTimeMs = j7;
        this.mImageOrigin = i;
        this.mIsCanceled = z;
        this.mIsSuccessful = z2;
        this.mIsPrefetch = z3;
    }

    @Nullable
    public String getControllerId() {
        return this.mControllerId;
    }

    @Nullable
    public String getRequestId() {
        return this.mRequestId;
    }

    @Nullable
    public ImageRequest getImageRequest() {
        return this.mImageRequest;
    }

    @Nullable
    public Object getCallerContext() {
        return this.mCallerContext;
    }

    @Nullable
    public ImageInfo getImageInfo() {
        return this.mImageInfo;
    }

    public long getControllerSubmitTimeMs() {
        return this.mControllerSubmitTimeMs;
    }

    public long getControllerIntermediateImageSetTimeMs() {
        return this.mControllerIntermediateImageSetTimeMs;
    }

    public long getControllerFinalImageSetTimeMs() {
        return this.mControllerFinalImageSetTimeMs;
    }

    public long getControllerFailureTimeMs() {
        return this.mControllerFailureTimeMs;
    }

    public long getImageRequestStartTimeMs() {
        return this.mImageRequestStartTimeMs;
    }

    public long getImageRequestEndTimeMs() {
        return this.mImageRequestEndTimeMs;
    }

    public int getImageOrigin() {
        return this.mImageOrigin;
    }

    public boolean isCanceled() {
        return this.mIsCanceled;
    }

    public boolean isSuccessful() {
        return this.mIsSuccessful;
    }

    public boolean isPrefetch() {
        return this.mIsPrefetch;
    }

    public long getFinalImageLoadTimeMs() {
        if (isSuccessful()) {
            return getImageRequestEndTimeMs() - getImageRequestStartTimeMs();
        }
        return -1;
    }

    public long getIntermediateImageLoadTimeMs() {
        if (isSuccessful()) {
            return getControllerIntermediateImageSetTimeMs() - getControllerSubmitTimeMs();
        }
        return -1;
    }

    public String createDebugString() {
        return Objects.toStringHelper((Object) this).add("controller ID", (Object) this.mControllerId).add("request ID", (Object) this.mRequestId).add("controller submit", this.mControllerSubmitTimeMs).add("controller final image", this.mControllerFinalImageSetTimeMs).add("controller failure", this.mControllerFailureTimeMs).add("controller cancel", this.mControllerCancelTimeMs).add("start time", this.mImageRequestStartTimeMs).add("end time", this.mImageRequestEndTimeMs).add("origin", (Object) ImageOriginUtils.toString(this.mImageOrigin)).add("canceled", this.mIsCanceled).add("successful", this.mIsSuccessful).add("prefetch", this.mIsPrefetch).add("caller context", this.mCallerContext).add("image request", (Object) this.mImageRequest).add("image info", (Object) this.mImageInfo).toString();
    }
}

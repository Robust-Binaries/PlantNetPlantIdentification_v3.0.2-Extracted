package p007io.amarcruz.photoview;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import com.RNFetchBlob.RNFetchBlobConst;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.AutoRotateDrawable;
import com.facebook.drawee.drawable.ScalingUtils.ScaleType;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.common.RotationOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.fresco.ReactNetworkImageRequest;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.uimanager.events.EventDispatcher;
import javax.annotation.Nullable;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import p010me.relex.photodraweeview.OnPhotoTapListener;
import p010me.relex.photodraweeview.OnScaleChangeListener;
import p010me.relex.photodraweeview.OnViewTapListener;
import p010me.relex.photodraweeview.PhotoDraweeView;

/* renamed from: io.amarcruz.photoview.PhotoView */
public class PhotoView extends PhotoDraweeView {
    private ControllerListener<ImageInfo> mControllerListener;
    private int mFadeDurationMs = -1;
    private ReadableMap mHeaders;
    private boolean mIsDirty;
    private boolean mIsLocalImage;
    private Drawable mLoadingImageDrawable;
    private Uri mUri;

    public PhotoView(Context context) {
        super(context);
    }

    public void setSource(@Nullable ReadableMap readableMap, @NonNull ResourceDrawableIdHelper resourceDrawableIdHelper) {
        this.mUri = null;
        if (readableMap != null) {
            String string = readableMap.getString(RNFetchBlobConst.DATA_ENCODE_URI);
            try {
                this.mUri = Uri.parse(string);
                if (this.mUri.getScheme() == null) {
                    this.mUri = null;
                }
                if (readableMap.hasKey("headers")) {
                    this.mHeaders = readableMap.getMap("headers");
                }
            } catch (Exception unused) {
            }
            if (this.mUri == null) {
                this.mUri = resourceDrawableIdHelper.getResourceDrawableUri(getContext(), string);
                this.mIsLocalImage = true;
            } else {
                this.mIsLocalImage = false;
            }
        }
        this.mIsDirty = true;
    }

    public void setLoadingIndicatorSource(@Nullable String str, ResourceDrawableIdHelper resourceDrawableIdHelper) {
        Drawable resourceDrawable = resourceDrawableIdHelper.getResourceDrawable(getContext(), str);
        this.mLoadingImageDrawable = resourceDrawable != null ? new AutoRotateDrawable(resourceDrawable, 1000) : null;
        this.mIsDirty = true;
    }

    public void setFadeDuration(int i) {
        this.mFadeDurationMs = i;
    }

    public void setShouldNotifyLoadEvents(boolean z) {
        if (!z) {
            this.mControllerListener = null;
        } else {
            final EventDispatcher eventDispatcher = ((UIManagerModule) ((ReactContext) getContext()).getNativeModule(UIManagerModule.class)).getEventDispatcher();
            this.mControllerListener = new BaseControllerListener<ImageInfo>() {
                public void onSubmit(String str, Object obj) {
                    eventDispatcher.dispatchEvent(new ImageEvent(PhotoView.this.getId(), 4));
                }

                public void onFinalImageSet(String str, @Nullable ImageInfo imageInfo, @Nullable Animatable animatable) {
                    if (imageInfo != null) {
                        eventDispatcher.dispatchEvent(new ImageEvent(PhotoView.this.getId(), 2));
                        eventDispatcher.dispatchEvent(new ImageEvent(PhotoView.this.getId(), 3));
                        PhotoView.this.update(imageInfo.getWidth(), imageInfo.getHeight());
                    }
                }

                public void onFailure(String str, Throwable th) {
                    eventDispatcher.dispatchEvent(new ImageEvent(PhotoView.this.getId(), 1));
                    eventDispatcher.dispatchEvent(new ImageEvent(PhotoView.this.getId(), 3));
                }
            };
        }
        this.mIsDirty = true;
    }

    public void maybeUpdateView(@NonNull PipelineDraweeControllerBuilder pipelineDraweeControllerBuilder) {
        if (this.mIsDirty) {
            GenericDraweeHierarchy genericDraweeHierarchy = (GenericDraweeHierarchy) getHierarchy();
            Drawable drawable = this.mLoadingImageDrawable;
            if (drawable != null) {
                genericDraweeHierarchy.setPlaceholderImage(drawable, ScaleType.CENTER);
            }
            int i = this.mFadeDurationMs;
            if (i < 0) {
                i = this.mIsLocalImage ? 0 : 300;
            }
            genericDraweeHierarchy.setFadeDuration(i);
            pipelineDraweeControllerBuilder.setImageRequest(ReactNetworkImageRequest.fromBuilderWithHeaders(ImageRequestBuilder.newBuilderWithSource(this.mUri).setRotationOptions(RotationOptions.autoRotate()).setResizeOptions(new ResizeOptions(getMaxTextureSize(), getMaxTextureSize())), this.mHeaders));
            pipelineDraweeControllerBuilder.setAutoPlayAnimations(true);
            pipelineDraweeControllerBuilder.setOldController(getController());
            pipelineDraweeControllerBuilder.setControllerListener(new BaseControllerListener<ImageInfo>() {
                public void onFinalImageSet(String str, ImageInfo imageInfo, Animatable animatable) {
                    super.onFinalImageSet(str, imageInfo, animatable);
                    if (imageInfo != null) {
                        PhotoView.this.update(imageInfo.getWidth(), imageInfo.getHeight());
                    }
                }
            });
            ControllerListener<ImageInfo> controllerListener = this.mControllerListener;
            if (controllerListener != null) {
                pipelineDraweeControllerBuilder.setControllerListener(controllerListener);
            }
            setController(pipelineDraweeControllerBuilder.build());
            setViewCallbacks();
            this.mIsDirty = false;
        }
    }

    private void setViewCallbacks() {
        final EventDispatcher eventDispatcher = ((UIManagerModule) ((ReactContext) getContext()).getNativeModule(UIManagerModule.class)).getEventDispatcher();
        setOnPhotoTapListener(new OnPhotoTapListener() {
            public void onPhotoTap(View view, float f, float f2) {
                WritableMap createMap = Arguments.createMap();
                createMap.putDouble("scale", (double) PhotoView.this.getScale());
                createMap.putDouble("x", (double) f);
                createMap.putDouble("y", (double) f2);
                eventDispatcher.dispatchEvent(new ImageEvent(PhotoView.this.getId(), 5).setExtras(createMap));
            }
        });
        setOnScaleChangeListener(new OnScaleChangeListener() {
            public void onScaleChange(float f, float f2, float f3) {
                WritableMap createMap = Arguments.createMap();
                createMap.putDouble("scale", (double) PhotoView.this.getScale());
                createMap.putDouble("scaleFactor", (double) f);
                createMap.putDouble("focusX", (double) f2);
                createMap.putDouble("focusY", (double) f3);
                eventDispatcher.dispatchEvent(new ImageEvent(PhotoView.this.getId(), 7).setExtras(createMap));
            }
        });
        setOnViewTapListener(new OnViewTapListener() {
            public void onViewTap(View view, float f, float f2) {
                WritableMap createMap = Arguments.createMap();
                createMap.putDouble("scale", (double) PhotoView.this.getScale());
                createMap.putDouble("x", (double) f);
                createMap.putDouble("y", (double) f2);
                eventDispatcher.dispatchEvent(new ImageEvent(PhotoView.this.getId(), 6).setExtras(createMap));
            }
        });
    }

    private int getMaxTextureSize() {
        EGL10 egl10 = (EGL10) EGLContext.getEGL();
        EGLDisplay eglGetDisplay = egl10.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        egl10.eglInitialize(eglGetDisplay, new int[2]);
        int[] iArr = new int[1];
        egl10.eglGetConfigs(eglGetDisplay, null, 0, iArr);
        EGLConfig[] eGLConfigArr = new EGLConfig[iArr[0]];
        egl10.eglGetConfigs(eglGetDisplay, eGLConfigArr, iArr[0], iArr);
        int[] iArr2 = new int[1];
        int i = 0;
        for (int i2 = 0; i2 < iArr[0]; i2++) {
            egl10.eglGetConfigAttrib(eglGetDisplay, eGLConfigArr[i2], 12332, iArr2);
            if (i < iArr2[0]) {
                i = iArr2[0];
            }
        }
        egl10.eglTerminate(eglGetDisplay);
        return Math.max(i, 2048);
    }
}

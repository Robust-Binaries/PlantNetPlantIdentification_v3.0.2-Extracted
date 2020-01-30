package p010me.relex.photodraweeview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.MotionEvent;
import android.view.View.OnLongClickListener;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;

/* renamed from: me.relex.photodraweeview.PhotoDraweeView */
public class PhotoDraweeView extends SimpleDraweeView implements IAttacher {
    private Attacher mAttacher;
    /* access modifiers changed from: private */
    public boolean mEnableDraweeMatrix = true;

    public PhotoDraweeView(Context context, GenericDraweeHierarchy genericDraweeHierarchy) {
        super(context, genericDraweeHierarchy);
        init();
    }

    public PhotoDraweeView(Context context) {
        super(context);
        init();
    }

    public PhotoDraweeView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public PhotoDraweeView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    /* access modifiers changed from: protected */
    public void init() {
        Attacher attacher = this.mAttacher;
        if (attacher == null || attacher.getDraweeView() == null) {
            this.mAttacher = new Attacher(this);
        }
    }

    public Attacher getAttacher() {
        return this.mAttacher;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        return super.onTouchEvent(motionEvent);
    }

    /* access modifiers changed from: protected */
    public void onDraw(@NonNull Canvas canvas) {
        int save = canvas.save();
        if (this.mEnableDraweeMatrix) {
            canvas.concat(this.mAttacher.getDrawMatrix());
        }
        super.onDraw(canvas);
        canvas.restoreToCount(save);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        init();
        super.onAttachedToWindow();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        this.mAttacher.onDetachedFromWindow();
        super.onDetachedFromWindow();
    }

    public float getMinimumScale() {
        return this.mAttacher.getMinimumScale();
    }

    public float getMediumScale() {
        return this.mAttacher.getMediumScale();
    }

    public float getMaximumScale() {
        return this.mAttacher.getMaximumScale();
    }

    public void setMinimumScale(float f) {
        this.mAttacher.setMinimumScale(f);
    }

    public void setMediumScale(float f) {
        this.mAttacher.setMediumScale(f);
    }

    public void setMaximumScale(float f) {
        this.mAttacher.setMaximumScale(f);
    }

    public float getScale() {
        return this.mAttacher.getScale();
    }

    public void setScale(float f) {
        this.mAttacher.setScale(f);
    }

    public void setScale(float f, boolean z) {
        this.mAttacher.setScale(f, z);
    }

    public void setScale(float f, float f2, float f3, boolean z) {
        this.mAttacher.setScale(f, f2, f3, z);
    }

    public void setOrientation(int i) {
        this.mAttacher.setOrientation(i);
    }

    public void setZoomTransitionDuration(long j) {
        this.mAttacher.setZoomTransitionDuration(j);
    }

    public void setAllowParentInterceptOnEdge(boolean z) {
        this.mAttacher.setAllowParentInterceptOnEdge(z);
    }

    public void setOnDoubleTapListener(OnDoubleTapListener onDoubleTapListener) {
        this.mAttacher.setOnDoubleTapListener(onDoubleTapListener);
    }

    public void setOnScaleChangeListener(OnScaleChangeListener onScaleChangeListener) {
        this.mAttacher.setOnScaleChangeListener(onScaleChangeListener);
    }

    public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
        this.mAttacher.setOnLongClickListener(onLongClickListener);
    }

    public void setOnPhotoTapListener(OnPhotoTapListener onPhotoTapListener) {
        this.mAttacher.setOnPhotoTapListener(onPhotoTapListener);
    }

    public void setOnViewTapListener(OnViewTapListener onViewTapListener) {
        this.mAttacher.setOnViewTapListener(onViewTapListener);
    }

    public OnPhotoTapListener getOnPhotoTapListener() {
        return this.mAttacher.getOnPhotoTapListener();
    }

    public OnViewTapListener getOnViewTapListener() {
        return this.mAttacher.getOnViewTapListener();
    }

    public void update(int i, int i2) {
        this.mAttacher.update(i, i2);
    }

    public boolean isEnableDraweeMatrix() {
        return this.mEnableDraweeMatrix;
    }

    public void setEnableDraweeMatrix(boolean z) {
        this.mEnableDraweeMatrix = z;
    }

    public void setPhotoUri(Uri uri) {
        setPhotoUri(uri, null);
    }

    public void setPhotoUri(Uri uri, @Nullable Context context) {
        this.mEnableDraweeMatrix = false;
        setController(((PipelineDraweeControllerBuilder) ((PipelineDraweeControllerBuilder) ((PipelineDraweeControllerBuilder) Fresco.newDraweeControllerBuilder().setCallerContext((Object) context)).setUri(uri).setOldController(getController())).setControllerListener(new BaseControllerListener<ImageInfo>() {
            public void onFailure(String str, Throwable th) {
                super.onFailure(str, th);
                PhotoDraweeView.this.mEnableDraweeMatrix = false;
            }

            public void onFinalImageSet(String str, ImageInfo imageInfo, Animatable animatable) {
                super.onFinalImageSet(str, imageInfo, animatable);
                PhotoDraweeView.this.mEnableDraweeMatrix = true;
                if (imageInfo != null) {
                    PhotoDraweeView.this.update(imageInfo.getWidth(), imageInfo.getHeight());
                }
            }

            public void onIntermediateImageFailed(String str, Throwable th) {
                super.onIntermediateImageFailed(str, th);
                PhotoDraweeView.this.mEnableDraweeMatrix = false;
            }

            public void onIntermediateImageSet(String str, ImageInfo imageInfo) {
                super.onIntermediateImageSet(str, imageInfo);
                PhotoDraweeView.this.mEnableDraweeMatrix = true;
                if (imageInfo != null) {
                    PhotoDraweeView.this.update(imageInfo.getWidth(), imageInfo.getHeight());
                }
            }
        })).build());
    }
}

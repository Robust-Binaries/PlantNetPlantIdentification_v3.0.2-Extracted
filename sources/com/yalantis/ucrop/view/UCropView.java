package com.yalantis.ucrop.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import com.yalantis.ucrop.C1313R;
import com.yalantis.ucrop.callback.CropBoundsChangeListener;
import com.yalantis.ucrop.callback.OverlayViewChangeListener;

public class UCropView extends FrameLayout {
    /* access modifiers changed from: private */
    public GestureCropImageView mGestureCropImageView;
    /* access modifiers changed from: private */
    public final OverlayView mViewOverlay;

    public boolean shouldDelayChildPressedState() {
        return false;
    }

    public UCropView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public UCropView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        LayoutInflater.from(context).inflate(C1313R.layout.ucrop_view, this, true);
        this.mGestureCropImageView = (GestureCropImageView) findViewById(C1313R.C1316id.image_view_crop);
        this.mViewOverlay = (OverlayView) findViewById(C1313R.C1316id.view_overlay);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C1313R.styleable.ucrop_UCropView);
        this.mViewOverlay.processStyledAttributes(obtainStyledAttributes);
        this.mGestureCropImageView.processStyledAttributes(obtainStyledAttributes);
        obtainStyledAttributes.recycle();
        setListenersToViews();
    }

    private void setListenersToViews() {
        this.mGestureCropImageView.setCropBoundsChangeListener(new CropBoundsChangeListener() {
            public void onCropAspectRatioChanged(float f) {
                UCropView.this.mViewOverlay.setTargetAspectRatio(f);
            }
        });
        this.mViewOverlay.setOverlayViewChangeListener(new OverlayViewChangeListener() {
            public void onCropRectUpdated(RectF rectF) {
                UCropView.this.mGestureCropImageView.setCropRect(rectF);
            }
        });
    }

    @NonNull
    public GestureCropImageView getCropImageView() {
        return this.mGestureCropImageView;
    }

    @NonNull
    public OverlayView getOverlayView() {
        return this.mViewOverlay;
    }

    public void resetCropImageView() {
        removeView(this.mGestureCropImageView);
        this.mGestureCropImageView = new GestureCropImageView(getContext());
        setListenersToViews();
        this.mGestureCropImageView.setCropRect(getOverlayView().getCropViewRect());
        addView(this.mGestureCropImageView, 0);
    }
}

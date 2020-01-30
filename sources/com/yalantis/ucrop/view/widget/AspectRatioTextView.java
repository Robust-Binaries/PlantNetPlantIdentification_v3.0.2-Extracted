package com.yalantis.ucrop.view.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.p000v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;
import com.yalantis.ucrop.C1313R;
import com.yalantis.ucrop.model.AspectRatio;
import java.util.Locale;

public class AspectRatioTextView extends TextView {
    private float mAspectRatio;
    private String mAspectRatioTitle;
    private float mAspectRatioX;
    private float mAspectRatioY;
    private final Rect mCanvasClipBounds;
    private Paint mDotPaint;
    private int mDotSize;

    public AspectRatioTextView(Context context) {
        this(context, null);
    }

    public AspectRatioTextView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public AspectRatioTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mCanvasClipBounds = new Rect();
        init(context.obtainStyledAttributes(attributeSet, C1313R.styleable.ucrop_AspectRatioTextView));
    }

    @TargetApi(21)
    public AspectRatioTextView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mCanvasClipBounds = new Rect();
        init(context.obtainStyledAttributes(attributeSet, C1313R.styleable.ucrop_AspectRatioTextView));
    }

    public void setActiveColor(@ColorInt int i) {
        applyActiveColor(i);
        invalidate();
    }

    public void setAspectRatio(@NonNull AspectRatio aspectRatio) {
        this.mAspectRatioTitle = aspectRatio.getAspectRatioTitle();
        this.mAspectRatioX = aspectRatio.getAspectRatioX();
        this.mAspectRatioY = aspectRatio.getAspectRatioY();
        float f = this.mAspectRatioX;
        if (f != 0.0f) {
            float f2 = this.mAspectRatioY;
            if (f2 != 0.0f) {
                this.mAspectRatio = f / f2;
                setTitle();
            }
        }
        this.mAspectRatio = 0.0f;
        setTitle();
    }

    public float getAspectRatio(boolean z) {
        if (z) {
            toggleAspectRatio();
            setTitle();
        }
        return this.mAspectRatio;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isSelected()) {
            canvas.getClipBounds(this.mCanvasClipBounds);
            float f = ((float) (this.mCanvasClipBounds.right - this.mCanvasClipBounds.left)) / 2.0f;
            int i = this.mCanvasClipBounds.bottom;
            int i2 = this.mDotSize;
            canvas.drawCircle(f, (float) (i - i2), (float) (i2 / 2), this.mDotPaint);
        }
    }

    private void init(@NonNull TypedArray typedArray) {
        setGravity(1);
        this.mAspectRatioTitle = typedArray.getString(C1313R.styleable.ucrop_AspectRatioTextView_ucrop_artv_ratio_title);
        this.mAspectRatioX = typedArray.getFloat(C1313R.styleable.ucrop_AspectRatioTextView_ucrop_artv_ratio_x, 0.0f);
        this.mAspectRatioY = typedArray.getFloat(C1313R.styleable.ucrop_AspectRatioTextView_ucrop_artv_ratio_y, 0.0f);
        float f = this.mAspectRatioX;
        if (f != 0.0f) {
            float f2 = this.mAspectRatioY;
            if (f2 != 0.0f) {
                this.mAspectRatio = f / f2;
                this.mDotSize = getContext().getResources().getDimensionPixelSize(C1313R.dimen.ucrop_size_dot_scale_text_view);
                this.mDotPaint = new Paint(1);
                this.mDotPaint.setStyle(Style.FILL);
                setTitle();
                applyActiveColor(getResources().getColor(C1313R.C1314color.ucrop_color_widget_active));
                typedArray.recycle();
            }
        }
        this.mAspectRatio = 0.0f;
        this.mDotSize = getContext().getResources().getDimensionPixelSize(C1313R.dimen.ucrop_size_dot_scale_text_view);
        this.mDotPaint = new Paint(1);
        this.mDotPaint.setStyle(Style.FILL);
        setTitle();
        applyActiveColor(getResources().getColor(C1313R.C1314color.ucrop_color_widget_active));
        typedArray.recycle();
    }

    private void applyActiveColor(@ColorInt int i) {
        Paint paint = this.mDotPaint;
        if (paint != null) {
            paint.setColor(i);
        }
        setTextColor(new ColorStateList(new int[][]{new int[]{16842913}, new int[]{0}}, new int[]{i, ContextCompat.getColor(getContext(), C1313R.C1314color.ucrop_color_widget)}));
    }

    private void toggleAspectRatio() {
        if (this.mAspectRatio != 0.0f) {
            float f = this.mAspectRatioX;
            this.mAspectRatioX = this.mAspectRatioY;
            this.mAspectRatioY = f;
            this.mAspectRatio = this.mAspectRatioX / this.mAspectRatioY;
        }
    }

    private void setTitle() {
        if (!TextUtils.isEmpty(this.mAspectRatioTitle)) {
            setText(this.mAspectRatioTitle);
            return;
        }
        setText(String.format(Locale.US, "%d:%d", new Object[]{Integer.valueOf((int) this.mAspectRatioX), Integer.valueOf((int) this.mAspectRatioY)}));
    }
}

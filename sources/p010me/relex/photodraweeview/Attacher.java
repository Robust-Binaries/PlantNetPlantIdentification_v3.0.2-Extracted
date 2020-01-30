package p010me.relex.photodraweeview;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Build.VERSION;
import android.support.annotation.Nullable;
import android.support.p000v4.view.GestureDetectorCompat;
import android.support.p000v4.view.MotionEventCompat;
import android.support.p000v4.widget.ScrollerCompat;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewParent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import com.facebook.drawee.drawable.ScalingUtils.ScaleType;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.DraweeView;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;

/* renamed from: me.relex.photodraweeview.Attacher */
public class Attacher implements IAttacher, OnTouchListener, OnScaleDragGestureListener {
    private static final int EDGE_BOTH = 2;
    private static final int EDGE_BOTTOM = 1;
    private static final int EDGE_LEFT = 0;
    private static final int EDGE_NONE = -1;
    private static final int EDGE_RIGHT = 1;
    private static final int EDGE_TOP = 0;
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    private boolean mAllowParentInterceptOnEdge = true;
    private boolean mBlockParentIntercept = false;
    private FlingRunnable mCurrentFlingRunnable;
    private final RectF mDisplayRect = new RectF();
    private WeakReference<DraweeView<GenericDraweeHierarchy>> mDraweeView;
    private GestureDetectorCompat mGestureDetector;
    private int mImageInfoHeight = -1;
    private int mImageInfoWidth = -1;
    /* access modifiers changed from: private */
    public OnLongClickListener mLongClickListener;
    /* access modifiers changed from: private */
    public final Matrix mMatrix = new Matrix();
    private final float[] mMatrixValues = new float[9];
    private float mMaxScale = 3.0f;
    private float mMidScale = 1.75f;
    private float mMinScale = 1.0f;
    private int mOrientation = 0;
    private OnPhotoTapListener mPhotoTapListener;
    private OnScaleChangeListener mScaleChangeListener;
    private ScaleDragDetector mScaleDragDetector;
    private int mScrollEdgeX = 2;
    private int mScrollEdgeY = 2;
    private OnViewTapListener mViewTapListener;
    /* access modifiers changed from: private */
    public long mZoomDuration = 200;
    /* access modifiers changed from: private */
    public final Interpolator mZoomInterpolator = new AccelerateDecelerateInterpolator();

    @Retention(RetentionPolicy.SOURCE)
    /* renamed from: me.relex.photodraweeview.Attacher$OrientationMode */
    public @interface OrientationMode {
    }

    /* renamed from: me.relex.photodraweeview.Attacher$AnimatedZoomRunnable */
    private class AnimatedZoomRunnable implements Runnable {
        private final float mFocalX;
        private final float mFocalY;
        private final long mStartTime = System.currentTimeMillis();
        private final float mZoomEnd;
        private final float mZoomStart;

        public AnimatedZoomRunnable(float f, float f2, float f3, float f4) {
            this.mFocalX = f3;
            this.mFocalY = f4;
            this.mZoomStart = f;
            this.mZoomEnd = f2;
        }

        public void run() {
            DraweeView draweeView = Attacher.this.getDraweeView();
            if (draweeView != null) {
                float interpolate = interpolate();
                float f = this.mZoomStart;
                Attacher.this.onScale((f + ((this.mZoomEnd - f) * interpolate)) / Attacher.this.getScale(), this.mFocalX, this.mFocalY);
                if (interpolate < 1.0f) {
                    Attacher.this.postOnAnimation(draweeView, this);
                }
            }
        }

        private float interpolate() {
            return Attacher.this.mZoomInterpolator.getInterpolation(Math.min(1.0f, (((float) (System.currentTimeMillis() - this.mStartTime)) * 1.0f) / ((float) Attacher.this.mZoomDuration)));
        }
    }

    /* renamed from: me.relex.photodraweeview.Attacher$FlingRunnable */
    private class FlingRunnable implements Runnable {
        private int mCurrentX;
        private int mCurrentY;
        private final ScrollerCompat mScroller;

        public FlingRunnable(Context context) {
            this.mScroller = ScrollerCompat.create(context);
        }

        public void cancelFling() {
            this.mScroller.abortAnimation();
        }

        public void fling(int i, int i2, int i3, int i4) {
            int i5;
            int i6;
            int i7;
            int i8;
            RectF displayRect = Attacher.this.getDisplayRect();
            if (displayRect != null) {
                int round = Math.round(-displayRect.left);
                float f = (float) i;
                if (f < displayRect.width()) {
                    i5 = Math.round(displayRect.width() - f);
                    i6 = 0;
                } else {
                    i6 = round;
                    i5 = i6;
                }
                int round2 = Math.round(-displayRect.top);
                float f2 = (float) i2;
                if (f2 < displayRect.height()) {
                    i7 = Math.round(displayRect.height() - f2);
                    i8 = 0;
                } else {
                    i8 = round2;
                    i7 = i8;
                }
                this.mCurrentX = round;
                this.mCurrentY = round2;
                if (!(round == i5 && round2 == i7)) {
                    this.mScroller.fling(round, round2, i3, i4, i6, i5, i8, i7, 0, 0);
                }
            }
        }

        public void run() {
            if (!this.mScroller.isFinished()) {
                DraweeView draweeView = Attacher.this.getDraweeView();
                if (draweeView != null && this.mScroller.computeScrollOffset()) {
                    int currX = this.mScroller.getCurrX();
                    int currY = this.mScroller.getCurrY();
                    Attacher.this.mMatrix.postTranslate((float) (this.mCurrentX - currX), (float) (this.mCurrentY - currY));
                    draweeView.invalidate();
                    this.mCurrentX = currX;
                    this.mCurrentY = currY;
                    Attacher.this.postOnAnimation(draweeView, this);
                }
            }
        }
    }

    public Attacher(DraweeView<GenericDraweeHierarchy> draweeView) {
        this.mDraweeView = new WeakReference<>(draweeView);
        ((GenericDraweeHierarchy) draweeView.getHierarchy()).setActualImageScaleType(ScaleType.FIT_CENTER);
        draweeView.setOnTouchListener(this);
        this.mScaleDragDetector = new ScaleDragDetector(draweeView.getContext(), this);
        this.mGestureDetector = new GestureDetectorCompat(draweeView.getContext(), new SimpleOnGestureListener() {
            public void onLongPress(MotionEvent motionEvent) {
                super.onLongPress(motionEvent);
                if (Attacher.this.mLongClickListener != null) {
                    Attacher.this.mLongClickListener.onLongClick(Attacher.this.getDraweeView());
                }
            }
        });
        this.mGestureDetector.setOnDoubleTapListener(new DefaultOnDoubleTapListener(this));
    }

    public void setOnDoubleTapListener(OnDoubleTapListener onDoubleTapListener) {
        if (onDoubleTapListener != null) {
            this.mGestureDetector.setOnDoubleTapListener(onDoubleTapListener);
        } else {
            this.mGestureDetector.setOnDoubleTapListener(new DefaultOnDoubleTapListener(this));
        }
    }

    @Nullable
    public DraweeView<GenericDraweeHierarchy> getDraweeView() {
        return (DraweeView) this.mDraweeView.get();
    }

    public float getMinimumScale() {
        return this.mMinScale;
    }

    public float getMediumScale() {
        return this.mMidScale;
    }

    public float getMaximumScale() {
        return this.mMaxScale;
    }

    public void setMaximumScale(float f) {
        checkZoomLevels(this.mMinScale, this.mMidScale, f);
        this.mMaxScale = f;
    }

    public void setMediumScale(float f) {
        checkZoomLevels(this.mMinScale, f, this.mMaxScale);
        this.mMidScale = f;
    }

    public void setMinimumScale(float f) {
        checkZoomLevels(f, this.mMidScale, this.mMaxScale);
        this.mMinScale = f;
    }

    public float getScale() {
        return (float) Math.sqrt((double) (((float) Math.pow((double) getMatrixValue(this.mMatrix, 0), 2.0d)) + ((float) Math.pow((double) getMatrixValue(this.mMatrix, 3), 2.0d))));
    }

    public void setScale(float f) {
        setScale(f, false);
    }

    public void setScale(float f, boolean z) {
        DraweeView draweeView = getDraweeView();
        if (draweeView != null) {
            setScale(f, (float) (draweeView.getRight() / 2), (float) (draweeView.getBottom() / 2), z);
        }
    }

    public void setScale(float f, float f2, float f3, boolean z) {
        DraweeView draweeView = getDraweeView();
        if (draweeView != null && f >= this.mMinScale && f <= this.mMaxScale) {
            if (z) {
                AnimatedZoomRunnable animatedZoomRunnable = new AnimatedZoomRunnable(getScale(), f, f2, f3);
                draweeView.post(animatedZoomRunnable);
            } else {
                this.mMatrix.setScale(f, f, f2, f3);
                checkMatrixAndInvalidate();
            }
        }
    }

    public void setOrientation(int i) {
        this.mOrientation = i;
    }

    public void setZoomTransitionDuration(long j) {
        if (j < 0) {
            j = 200;
        }
        this.mZoomDuration = j;
    }

    public void setAllowParentInterceptOnEdge(boolean z) {
        this.mAllowParentInterceptOnEdge = z;
    }

    public void setOnScaleChangeListener(OnScaleChangeListener onScaleChangeListener) {
        this.mScaleChangeListener = onScaleChangeListener;
    }

    public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
        this.mLongClickListener = onLongClickListener;
    }

    public void setOnPhotoTapListener(OnPhotoTapListener onPhotoTapListener) {
        this.mPhotoTapListener = onPhotoTapListener;
    }

    public void setOnViewTapListener(OnViewTapListener onViewTapListener) {
        this.mViewTapListener = onViewTapListener;
    }

    public OnPhotoTapListener getOnPhotoTapListener() {
        return this.mPhotoTapListener;
    }

    public OnViewTapListener getOnViewTapListener() {
        return this.mViewTapListener;
    }

    public void update(int i, int i2) {
        this.mImageInfoWidth = i;
        this.mImageInfoHeight = i2;
        updateBaseMatrix();
    }

    private static void checkZoomLevels(float f, float f2, float f3) {
        if (f >= f2) {
            throw new IllegalArgumentException("MinZoom has to be less than MidZoom");
        } else if (f2 >= f3) {
            throw new IllegalArgumentException("MidZoom has to be less than MaxZoom");
        }
    }

    private int getViewWidth() {
        DraweeView draweeView = getDraweeView();
        if (draweeView != null) {
            return (draweeView.getWidth() - draweeView.getPaddingLeft()) - draweeView.getPaddingRight();
        }
        return 0;
    }

    private int getViewHeight() {
        DraweeView draweeView = getDraweeView();
        if (draweeView != null) {
            return (draweeView.getHeight() - draweeView.getPaddingTop()) - draweeView.getPaddingBottom();
        }
        return 0;
    }

    private float getMatrixValue(Matrix matrix, int i) {
        matrix.getValues(this.mMatrixValues);
        return this.mMatrixValues[i];
    }

    public Matrix getDrawMatrix() {
        return this.mMatrix;
    }

    public RectF getDisplayRect() {
        checkMatrixBounds();
        return getDisplayRect(getDrawMatrix());
    }

    public void checkMatrixAndInvalidate() {
        DraweeView draweeView = getDraweeView();
        if (draweeView != null && checkMatrixBounds()) {
            draweeView.invalidate();
        }
    }

    public boolean checkMatrixBounds() {
        float f;
        RectF displayRect = getDisplayRect(getDrawMatrix());
        if (displayRect == null) {
            return false;
        }
        float height = displayRect.height();
        float width = displayRect.width();
        float viewHeight = (float) getViewHeight();
        float f2 = 0.0f;
        if (height <= viewHeight) {
            f = ((viewHeight - height) / 2.0f) - displayRect.top;
            this.mScrollEdgeY = 2;
        } else if (displayRect.top > 0.0f) {
            f = -displayRect.top;
            this.mScrollEdgeY = 0;
        } else if (displayRect.bottom < viewHeight) {
            f = viewHeight - displayRect.bottom;
            this.mScrollEdgeY = 1;
        } else {
            this.mScrollEdgeY = -1;
            f = 0.0f;
        }
        float viewWidth = (float) getViewWidth();
        if (width <= viewWidth) {
            f2 = ((viewWidth - width) / 2.0f) - displayRect.left;
            this.mScrollEdgeX = 2;
        } else if (displayRect.left > 0.0f) {
            f2 = -displayRect.left;
            this.mScrollEdgeX = 0;
        } else if (displayRect.right < viewWidth) {
            f2 = viewWidth - displayRect.right;
            this.mScrollEdgeX = 1;
        } else {
            this.mScrollEdgeX = -1;
        }
        this.mMatrix.postTranslate(f2, f);
        return true;
    }

    private RectF getDisplayRect(Matrix matrix) {
        DraweeView draweeView = getDraweeView();
        if (draweeView == null || (this.mImageInfoWidth == -1 && this.mImageInfoHeight == -1)) {
            return null;
        }
        this.mDisplayRect.set(0.0f, 0.0f, (float) this.mImageInfoWidth, (float) this.mImageInfoHeight);
        ((GenericDraweeHierarchy) draweeView.getHierarchy()).getActualImageBounds(this.mDisplayRect);
        matrix.mapRect(this.mDisplayRect);
        return this.mDisplayRect;
    }

    private void updateBaseMatrix() {
        if (this.mImageInfoWidth != -1 || this.mImageInfoHeight != -1) {
            resetMatrix();
        }
    }

    private void resetMatrix() {
        this.mMatrix.reset();
        checkMatrixBounds();
        DraweeView draweeView = getDraweeView();
        if (draweeView != null) {
            draweeView.invalidate();
        }
    }

    private void checkMinScale() {
        DraweeView draweeView = getDraweeView();
        if (draweeView != null && getScale() < this.mMinScale) {
            RectF displayRect = getDisplayRect();
            if (displayRect != null) {
                AnimatedZoomRunnable animatedZoomRunnable = new AnimatedZoomRunnable(getScale(), this.mMinScale, displayRect.centerX(), displayRect.centerY());
                draweeView.post(animatedZoomRunnable);
            }
        }
    }

    public void onScale(float f, float f2, float f3) {
        if (getScale() < this.mMaxScale || f < 1.0f) {
            OnScaleChangeListener onScaleChangeListener = this.mScaleChangeListener;
            if (onScaleChangeListener != null) {
                onScaleChangeListener.onScaleChange(f, f2, f3);
            }
            this.mMatrix.postScale(f, f, f2, f3);
            checkMatrixAndInvalidate();
        }
    }

    public void onScaleEnd() {
        checkMinScale();
    }

    public void onDrag(float f, float f2) {
        DraweeView draweeView = getDraweeView();
        if (draweeView != null && !this.mScaleDragDetector.isScaling()) {
            this.mMatrix.postTranslate(f, f2);
            checkMatrixAndInvalidate();
            ViewParent parent = draweeView.getParent();
            if (parent != null) {
                if (!this.mAllowParentInterceptOnEdge || this.mScaleDragDetector.isScaling() || this.mBlockParentIntercept) {
                    parent.requestDisallowInterceptTouchEvent(true);
                } else {
                    if (this.mOrientation == 0) {
                        int i = this.mScrollEdgeX;
                        if (i == 2 || ((i == 0 && f >= 1.0f) || (this.mScrollEdgeX == 1 && f <= -1.0f))) {
                            parent.requestDisallowInterceptTouchEvent(false);
                        }
                    }
                    if (this.mOrientation == 1) {
                        int i2 = this.mScrollEdgeY;
                        if (i2 == 2 || ((i2 == 0 && f2 >= 1.0f) || (this.mScrollEdgeY == 1 && f2 <= -1.0f))) {
                            parent.requestDisallowInterceptTouchEvent(false);
                        }
                    }
                }
            }
        }
    }

    public void onFling(float f, float f2, float f3, float f4) {
        DraweeView draweeView = getDraweeView();
        if (draweeView != null) {
            this.mCurrentFlingRunnable = new FlingRunnable(draweeView.getContext());
            this.mCurrentFlingRunnable.fling(getViewWidth(), getViewHeight(), (int) f3, (int) f4);
            draweeView.post(this.mCurrentFlingRunnable);
        }
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        int actionMasked = MotionEventCompat.getActionMasked(motionEvent);
        boolean z = false;
        if (actionMasked != 3) {
            switch (actionMasked) {
                case 0:
                    ViewParent parent = view.getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                    cancelFling();
                    break;
                case 1:
                    break;
            }
        }
        ViewParent parent2 = view.getParent();
        if (parent2 != null) {
            parent2.requestDisallowInterceptTouchEvent(false);
        }
        boolean isScaling = this.mScaleDragDetector.isScaling();
        boolean isDragging = this.mScaleDragDetector.isDragging();
        boolean onTouchEvent = this.mScaleDragDetector.onTouchEvent(motionEvent);
        boolean z2 = !isScaling && !this.mScaleDragDetector.isScaling();
        boolean z3 = !isDragging && !this.mScaleDragDetector.isDragging();
        if (z2 && z3) {
            z = true;
        }
        this.mBlockParentIntercept = z;
        if (this.mGestureDetector.onTouchEvent(motionEvent)) {
            return true;
        }
        return onTouchEvent;
    }

    private void cancelFling() {
        FlingRunnable flingRunnable = this.mCurrentFlingRunnable;
        if (flingRunnable != null) {
            flingRunnable.cancelFling();
            this.mCurrentFlingRunnable = null;
        }
    }

    /* access modifiers changed from: private */
    public void postOnAnimation(View view, Runnable runnable) {
        if (VERSION.SDK_INT >= 16) {
            view.postOnAnimation(runnable);
        } else {
            view.postDelayed(runnable, 16);
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        cancelFling();
    }
}

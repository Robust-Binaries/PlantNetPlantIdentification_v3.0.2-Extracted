package p010me.relex.photodraweeview;

import android.content.Context;
import android.support.p000v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;

/* renamed from: me.relex.photodraweeview.ScaleDragDetector */
public class ScaleDragDetector implements OnScaleGestureListener {
    private static final int INVALID_POINTER_ID = -1;
    private int mActivePointerId = -1;
    private int mActivePointerIndex = 0;
    private boolean mIsDragging;
    float mLastTouchX;
    float mLastTouchY;
    private final float mMinimumVelocity;
    private final ScaleGestureDetector mScaleDetector;
    private final OnScaleDragGestureListener mScaleDragGestureListener;
    private final float mTouchSlop;
    private VelocityTracker mVelocityTracker;

    public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
        return true;
    }

    public ScaleDragDetector(Context context, OnScaleDragGestureListener onScaleDragGestureListener) {
        this.mScaleDetector = new ScaleGestureDetector(context, this);
        this.mScaleDragGestureListener = onScaleDragGestureListener;
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        this.mMinimumVelocity = (float) viewConfiguration.getScaledMinimumFlingVelocity();
        this.mTouchSlop = (float) viewConfiguration.getScaledTouchSlop();
    }

    public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
        float scaleFactor = scaleGestureDetector.getScaleFactor();
        if (Float.isNaN(scaleFactor) || Float.isInfinite(scaleFactor)) {
            return false;
        }
        this.mScaleDragGestureListener.onScale(scaleFactor, scaleGestureDetector.getFocusX(), scaleGestureDetector.getFocusY());
        return true;
    }

    public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
        this.mScaleDragGestureListener.onScaleEnd();
    }

    public boolean isScaling() {
        return this.mScaleDetector.isInProgress();
    }

    public boolean isDragging() {
        return this.mIsDragging;
    }

    private float getActiveX(MotionEvent motionEvent) {
        try {
            return MotionEventCompat.getX(motionEvent, this.mActivePointerIndex);
        } catch (Exception unused) {
            return motionEvent.getX();
        }
    }

    private float getActiveY(MotionEvent motionEvent) {
        try {
            return MotionEventCompat.getY(motionEvent, this.mActivePointerIndex);
        } catch (Exception unused) {
            return motionEvent.getY();
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        this.mScaleDetector.onTouchEvent(motionEvent);
        int actionMasked = MotionEventCompat.getActionMasked(motionEvent);
        onTouchActivePointer(actionMasked, motionEvent);
        onTouchDragEvent(actionMasked, motionEvent);
        return true;
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x003e  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void onTouchActivePointer(int r5, android.view.MotionEvent r6) {
        /*
            r4 = this;
            r0 = 3
            r1 = -1
            r2 = 0
            if (r5 == r0) goto L_0x0037
            r0 = 6
            if (r5 == r0) goto L_0x0013
            switch(r5) {
                case 0: goto L_0x000c;
                case 1: goto L_0x0037;
                default: goto L_0x000b;
            }
        L_0x000b:
            goto L_0x0039
        L_0x000c:
            int r5 = r6.getPointerId(r2)
            r4.mActivePointerId = r5
            goto L_0x0039
        L_0x0013:
            int r5 = android.support.p000v4.view.MotionEventCompat.getActionIndex(r6)
            int r0 = android.support.p000v4.view.MotionEventCompat.getPointerId(r6, r5)
            int r3 = r4.mActivePointerId
            if (r0 != r3) goto L_0x0039
            if (r5 != 0) goto L_0x0023
            r5 = 1
            goto L_0x0024
        L_0x0023:
            r5 = 0
        L_0x0024:
            int r0 = android.support.p000v4.view.MotionEventCompat.getPointerId(r6, r5)
            r4.mActivePointerId = r0
            float r0 = android.support.p000v4.view.MotionEventCompat.getX(r6, r5)
            r4.mLastTouchX = r0
            float r5 = android.support.p000v4.view.MotionEventCompat.getY(r6, r5)
            r4.mLastTouchY = r5
            goto L_0x0039
        L_0x0037:
            r4.mActivePointerId = r1
        L_0x0039:
            int r5 = r4.mActivePointerId
            if (r5 == r1) goto L_0x003e
            goto L_0x003f
        L_0x003e:
            r5 = 0
        L_0x003f:
            int r5 = android.support.p000v4.view.MotionEventCompat.findPointerIndex(r6, r5)
            r4.mActivePointerIndex = r5
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: p010me.relex.photodraweeview.ScaleDragDetector.onTouchActivePointer(int, android.view.MotionEvent):void");
    }

    private void onTouchDragEvent(int i, MotionEvent motionEvent) {
        boolean z = false;
        switch (i) {
            case 0:
                this.mVelocityTracker = VelocityTracker.obtain();
                VelocityTracker velocityTracker = this.mVelocityTracker;
                if (velocityTracker != null) {
                    velocityTracker.addMovement(motionEvent);
                }
                this.mLastTouchX = getActiveX(motionEvent);
                this.mLastTouchY = getActiveY(motionEvent);
                this.mIsDragging = false;
                return;
            case 1:
                if (this.mIsDragging && this.mVelocityTracker != null) {
                    this.mLastTouchX = getActiveX(motionEvent);
                    this.mLastTouchY = getActiveY(motionEvent);
                    this.mVelocityTracker.addMovement(motionEvent);
                    this.mVelocityTracker.computeCurrentVelocity(1000);
                    float xVelocity = this.mVelocityTracker.getXVelocity();
                    float yVelocity = this.mVelocityTracker.getYVelocity();
                    if (Math.max(Math.abs(xVelocity), Math.abs(yVelocity)) >= this.mMinimumVelocity) {
                        this.mScaleDragGestureListener.onFling(this.mLastTouchX, this.mLastTouchY, -xVelocity, -yVelocity);
                    }
                }
                VelocityTracker velocityTracker2 = this.mVelocityTracker;
                if (velocityTracker2 != null) {
                    velocityTracker2.recycle();
                    this.mVelocityTracker = null;
                    return;
                }
                return;
            case 2:
                float activeX = getActiveX(motionEvent);
                float activeY = getActiveY(motionEvent);
                float f = activeX - this.mLastTouchX;
                float f2 = activeY - this.mLastTouchY;
                if (!this.mIsDragging) {
                    if (Math.sqrt((double) ((f * f) + (f2 * f2))) >= ((double) this.mTouchSlop)) {
                        z = true;
                    }
                    this.mIsDragging = z;
                }
                if (this.mIsDragging) {
                    this.mScaleDragGestureListener.onDrag(f, f2);
                    this.mLastTouchX = activeX;
                    this.mLastTouchY = activeY;
                    VelocityTracker velocityTracker3 = this.mVelocityTracker;
                    if (velocityTracker3 != null) {
                        velocityTracker3.addMovement(motionEvent);
                        return;
                    }
                    return;
                }
                return;
            case 3:
                VelocityTracker velocityTracker4 = this.mVelocityTracker;
                if (velocityTracker4 != null) {
                    velocityTracker4.recycle();
                    this.mVelocityTracker = null;
                    return;
                }
                return;
            default:
                return;
        }
    }
}

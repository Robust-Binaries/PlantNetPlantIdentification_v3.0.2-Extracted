package com.yalantis.ucrop.util;

import android.support.annotation.NonNull;
import android.view.MotionEvent;

public class RotationGestureDetector {
    private static final int INVALID_POINTER_INDEX = -1;

    /* renamed from: fX */
    private float f108fX;

    /* renamed from: fY */
    private float f109fY;
    private float mAngle;
    private boolean mIsFirstTouch;
    private OnRotationGestureListener mListener;
    private int mPointerIndex1 = -1;
    private int mPointerIndex2 = -1;

    /* renamed from: sX */
    private float f110sX;

    /* renamed from: sY */
    private float f111sY;

    public interface OnRotationGestureListener {
        boolean onRotation(RotationGestureDetector rotationGestureDetector);
    }

    public static class SimpleOnRotationGestureListener implements OnRotationGestureListener {
        public boolean onRotation(RotationGestureDetector rotationGestureDetector) {
            return false;
        }
    }

    public RotationGestureDetector(OnRotationGestureListener onRotationGestureListener) {
        this.mListener = onRotationGestureListener;
    }

    public float getAngle() {
        return this.mAngle;
    }

    public boolean onTouchEvent(@NonNull MotionEvent motionEvent) {
        MotionEvent motionEvent2 = motionEvent;
        switch (motionEvent.getActionMasked()) {
            case 0:
                this.f110sX = motionEvent.getX();
                this.f111sY = motionEvent.getY();
                this.mPointerIndex1 = motionEvent2.findPointerIndex(motionEvent2.getPointerId(0));
                this.mAngle = 0.0f;
                this.mIsFirstTouch = true;
                break;
            case 1:
                this.mPointerIndex1 = -1;
                break;
            case 2:
                if (!(this.mPointerIndex1 == -1 || this.mPointerIndex2 == -1 || motionEvent.getPointerCount() <= this.mPointerIndex2)) {
                    float x = motionEvent2.getX(this.mPointerIndex1);
                    float y = motionEvent2.getY(this.mPointerIndex1);
                    float x2 = motionEvent2.getX(this.mPointerIndex2);
                    float y2 = motionEvent2.getY(this.mPointerIndex2);
                    if (this.mIsFirstTouch) {
                        this.mAngle = 0.0f;
                        this.mIsFirstTouch = false;
                    } else {
                        calculateAngleBetweenLines(this.f108fX, this.f109fY, this.f110sX, this.f111sY, x2, y2, x, y);
                    }
                    OnRotationGestureListener onRotationGestureListener = this.mListener;
                    if (onRotationGestureListener != null) {
                        onRotationGestureListener.onRotation(this);
                    }
                    this.f108fX = x2;
                    this.f109fY = y2;
                    this.f110sX = x;
                    this.f111sY = y;
                    break;
                }
            case 5:
                this.f108fX = motionEvent.getX();
                this.f109fY = motionEvent.getY();
                this.mPointerIndex2 = motionEvent2.findPointerIndex(motionEvent2.getPointerId(motionEvent.getActionIndex()));
                this.mAngle = 0.0f;
                this.mIsFirstTouch = true;
                break;
            case 6:
                this.mPointerIndex2 = -1;
                break;
        }
        return true;
    }

    private float calculateAngleBetweenLines(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        return calculateAngleDelta((float) Math.toDegrees((double) ((float) Math.atan2((double) (f2 - f4), (double) (f - f3)))), (float) Math.toDegrees((double) ((float) Math.atan2((double) (f6 - f8), (double) (f5 - f7)))));
    }

    private float calculateAngleDelta(float f, float f2) {
        this.mAngle = (f2 % 360.0f) - (f % 360.0f);
        float f3 = this.mAngle;
        if (f3 < -180.0f) {
            this.mAngle = f3 + 360.0f;
        } else if (f3 > 180.0f) {
            this.mAngle = f3 - 360.0f;
        }
        return this.mAngle;
    }
}

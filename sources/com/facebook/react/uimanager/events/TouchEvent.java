package com.facebook.react.uimanager.events;

import android.support.p000v4.util.Pools.SynchronizedPool;
import android.view.MotionEvent;
import com.facebook.infer.annotation.Assertions;
import com.facebook.react.bridge.SoftAssertions;
import javax.annotation.Nullable;

public class TouchEvent extends Event<TouchEvent> {
    private static final SynchronizedPool<TouchEvent> EVENTS_POOL = new SynchronizedPool<>(3);
    private static final int TOUCH_EVENTS_POOL_SIZE = 3;
    public static final long UNSET = Long.MIN_VALUE;
    private short mCoalescingKey;
    @Nullable
    private MotionEvent mMotionEvent;
    @Nullable
    private TouchEventType mTouchEventType;
    private float mViewX;
    private float mViewY;

    public static TouchEvent obtain(int i, TouchEventType touchEventType, MotionEvent motionEvent, long j, float f, float f2, TouchEventCoalescingKeyHelper touchEventCoalescingKeyHelper) {
        TouchEvent touchEvent = (TouchEvent) EVENTS_POOL.acquire();
        if (touchEvent == null) {
            touchEvent = new TouchEvent();
        }
        touchEvent.init(i, touchEventType, motionEvent, j, f, f2, touchEventCoalescingKeyHelper);
        return touchEvent;
    }

    private TouchEvent() {
    }

    private void init(int i, TouchEventType touchEventType, MotionEvent motionEvent, long j, float f, float f2, TouchEventCoalescingKeyHelper touchEventCoalescingKeyHelper) {
        super.init(i);
        short s = 0;
        SoftAssertions.assertCondition(j != Long.MIN_VALUE, "Gesture start time must be initialized");
        int action = motionEvent.getAction() & 255;
        switch (action) {
            case 0:
                touchEventCoalescingKeyHelper.addCoalescingKey(j);
                break;
            case 1:
                touchEventCoalescingKeyHelper.removeCoalescingKey(j);
                break;
            case 2:
                s = touchEventCoalescingKeyHelper.getCoalescingKey(j);
                break;
            case 3:
                touchEventCoalescingKeyHelper.removeCoalescingKey(j);
                break;
            case 5:
            case 6:
                touchEventCoalescingKeyHelper.incrementCoalescingKey(j);
                break;
            default:
                StringBuilder sb = new StringBuilder();
                sb.append("Unhandled MotionEvent action: ");
                sb.append(action);
                throw new RuntimeException(sb.toString());
        }
        this.mTouchEventType = touchEventType;
        this.mMotionEvent = MotionEvent.obtain(motionEvent);
        this.mCoalescingKey = s;
        this.mViewX = f;
        this.mViewY = f2;
    }

    public void onDispose() {
        ((MotionEvent) Assertions.assertNotNull(this.mMotionEvent)).recycle();
        this.mMotionEvent = null;
        EVENTS_POOL.release(this);
    }

    public String getEventName() {
        return TouchEventType.getJSEventName((TouchEventType) Assertions.assertNotNull(this.mTouchEventType));
    }

    public boolean canCoalesce() {
        switch ((TouchEventType) Assertions.assertNotNull(this.mTouchEventType)) {
            case START:
            case END:
            case CANCEL:
                return false;
            case MOVE:
                return true;
            default:
                StringBuilder sb = new StringBuilder();
                sb.append("Unknown touch event type: ");
                sb.append(this.mTouchEventType);
                throw new RuntimeException(sb.toString());
        }
    }

    public short getCoalescingKey() {
        return this.mCoalescingKey;
    }

    public void dispatch(RCTEventEmitter rCTEventEmitter) {
        TouchesHelper.sendTouchEvent(rCTEventEmitter, (TouchEventType) Assertions.assertNotNull(this.mTouchEventType), getViewTag(), this);
    }

    public MotionEvent getMotionEvent() {
        Assertions.assertNotNull(this.mMotionEvent);
        return this.mMotionEvent;
    }

    public float getViewX() {
        return this.mViewX;
    }

    public float getViewY() {
        return this.mViewY;
    }
}

package com.facebook.react.modules.core;

import com.facebook.common.logging.FLog;
import com.facebook.infer.annotation.Assertions;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.common.ReactConstants;
import com.facebook.react.modules.core.ChoreographerCompat.FrameCallback;
import java.util.ArrayDeque;
import javax.annotation.Nullable;

public class ReactChoreographer {
    private static ReactChoreographer sInstance;
    /* access modifiers changed from: private */
    public final ArrayDeque<FrameCallback>[] mCallbackQueues = new ArrayDeque[CallbackType.values().length];
    /* access modifiers changed from: private */
    @Nullable
    public volatile ChoreographerCompat mChoreographer;
    /* access modifiers changed from: private */
    public boolean mHasPostedCallback = false;
    private final ReactChoreographerDispatcher mReactChoreographerDispatcher = new ReactChoreographerDispatcher();
    /* access modifiers changed from: private */
    public int mTotalCallbacks = 0;

    public enum CallbackType {
        PERF_MARKERS(0),
        DISPATCH_UI(1),
        NATIVE_ANIMATED_MODULE(2),
        TIMERS_EVENTS(3),
        IDLE_EVENT(4);
        
        private final int mOrder;

        private CallbackType(int i) {
            this.mOrder = i;
        }

        /* access modifiers changed from: 0000 */
        public int getOrder() {
            return this.mOrder;
        }
    }

    private class ReactChoreographerDispatcher extends FrameCallback {
        private ReactChoreographerDispatcher() {
        }

        public void doFrame(long j) {
            synchronized (ReactChoreographer.this) {
                ReactChoreographer.this.mHasPostedCallback = false;
                for (int i = 0; i < ReactChoreographer.this.mCallbackQueues.length; i++) {
                    int size = ReactChoreographer.this.mCallbackQueues[i].size();
                    for (int i2 = 0; i2 < size; i2++) {
                        ((FrameCallback) ReactChoreographer.this.mCallbackQueues[i].removeFirst()).doFrame(j);
                        ReactChoreographer.this.mTotalCallbacks = ReactChoreographer.this.mTotalCallbacks - 1;
                    }
                }
                ReactChoreographer.this.maybeRemoveFrameCallback();
            }
        }
    }

    public static void initialize() {
        if (sInstance == null) {
            sInstance = new ReactChoreographer();
        }
    }

    public static ReactChoreographer getInstance() {
        Assertions.assertNotNull(sInstance, "ReactChoreographer needs to be initialized.");
        return sInstance;
    }

    private ReactChoreographer() {
        int i = 0;
        while (true) {
            ArrayDeque<FrameCallback>[] arrayDequeArr = this.mCallbackQueues;
            if (i < arrayDequeArr.length) {
                arrayDequeArr[i] = new ArrayDeque<>();
                i++;
            } else {
                initializeChoreographer(null);
                return;
            }
        }
    }

    public synchronized void postFrameCallback(CallbackType callbackType, FrameCallback frameCallback) {
        this.mCallbackQueues[callbackType.getOrder()].addLast(frameCallback);
        boolean z = true;
        this.mTotalCallbacks++;
        if (this.mTotalCallbacks <= 0) {
            z = false;
        }
        Assertions.assertCondition(z);
        if (!this.mHasPostedCallback) {
            if (this.mChoreographer == null) {
                initializeChoreographer(new Runnable() {
                    public void run() {
                        ReactChoreographer.this.postFrameCallbackOnChoreographer();
                    }
                });
            } else {
                postFrameCallbackOnChoreographer();
            }
        }
    }

    public void postFrameCallbackOnChoreographer() {
        this.mChoreographer.postFrameCallback(this.mReactChoreographerDispatcher);
        this.mHasPostedCallback = true;
    }

    public void initializeChoreographer(@Nullable final Runnable runnable) {
        UiThreadUtil.runOnUiThread(new Runnable() {
            public void run() {
                synchronized (ReactChoreographer.class) {
                    if (ReactChoreographer.this.mChoreographer == null) {
                        ReactChoreographer.this.mChoreographer = ChoreographerCompat.getInstance();
                    }
                }
                Runnable runnable = runnable;
                if (runnable != null) {
                    runnable.run();
                }
            }
        });
    }

    public synchronized void removeFrameCallback(CallbackType callbackType, FrameCallback frameCallback) {
        if (this.mCallbackQueues[callbackType.getOrder()].removeFirstOccurrence(frameCallback)) {
            this.mTotalCallbacks--;
            maybeRemoveFrameCallback();
        } else {
            FLog.m48e(ReactConstants.TAG, "Tried to remove non-existent frame callback");
        }
    }

    /* access modifiers changed from: private */
    public void maybeRemoveFrameCallback() {
        Assertions.assertCondition(this.mTotalCallbacks >= 0);
        if (this.mTotalCallbacks == 0 && this.mHasPostedCallback) {
            if (this.mChoreographer != null) {
                this.mChoreographer.removeFrameCallback(this.mReactChoreographerDispatcher);
            }
            this.mHasPostedCallback = false;
        }
    }
}

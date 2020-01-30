package com.facebook.react.fabric.jsi;

import android.annotation.SuppressLint;
import com.facebook.jni.HybridData;
import com.facebook.proguard.annotations.DoNotStrip;
import com.facebook.react.bridge.JavaScriptContextHolder;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.events.BatchEventDispatchedListener;

@SuppressLint({"MissingNativeLoadLibrary"})
public class EventBeatManager implements BatchEventDispatchedListener {
    @DoNotStrip
    private final HybridData mHybridData;
    private final ReactApplicationContext mReactApplicationContext;

    /* access modifiers changed from: private */
    public native void beat();

    private static native HybridData initHybrid(long j);

    static {
        FabricSoLoader.staticInit();
    }

    public EventBeatManager(JavaScriptContextHolder javaScriptContextHolder, ReactApplicationContext reactApplicationContext) {
        this.mHybridData = initHybrid(javaScriptContextHolder.get());
        this.mReactApplicationContext = reactApplicationContext;
    }

    public void onBatchEventDispatched() {
        dispatchEventsAsync();
    }

    private void dispatchEventsAsync() {
        if (this.mReactApplicationContext.isOnJSQueueThread()) {
            beat();
        } else {
            this.mReactApplicationContext.runOnJSQueueThread(new Runnable() {
                public void run() {
                    EventBeatManager.this.beat();
                }
            });
        }
    }
}

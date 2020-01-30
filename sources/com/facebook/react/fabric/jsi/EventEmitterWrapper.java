package com.facebook.react.fabric.jsi;

import android.annotation.SuppressLint;
import android.support.annotation.Nullable;
import com.facebook.jni.HybridData;
import com.facebook.proguard.annotations.DoNotStrip;
import com.facebook.react.bridge.NativeMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;

@SuppressLint({"MissingNativeLoadLibrary"})
public class EventEmitterWrapper {
    @DoNotStrip
    private final HybridData mHybridData = initHybrid();

    private static native HybridData initHybrid();

    private native void invokeEvent(String str, NativeMap nativeMap);

    static {
        FabricSoLoader.staticInit();
    }

    private EventEmitterWrapper() {
    }

    public void invoke(String str, @Nullable WritableMap writableMap) {
        invokeEvent(str, writableMap == null ? new WritableNativeMap() : (NativeMap) writableMap);
    }
}

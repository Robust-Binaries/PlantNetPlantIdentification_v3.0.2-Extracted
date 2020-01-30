package com.facebook.react.fabric.jsi;

import com.facebook.jni.HybridData;
import com.facebook.proguard.annotations.DoNotStrip;

@DoNotStrip
public class ComponentRegistry {
    private final HybridData mHybridData = initHybrid();

    @DoNotStrip
    private static native HybridData initHybrid();

    static {
        FabricSoLoader.staticInit();
    }
}

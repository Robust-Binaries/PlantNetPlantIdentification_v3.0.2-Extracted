package com.facebook.react.bridge;

import android.support.annotation.NonNull;
import com.facebook.infer.annotation.Assertions;
import com.facebook.jni.HybridData;
import com.facebook.proguard.annotations.DoNotStrip;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@DoNotStrip
public class WritableNativeMap extends ReadableNativeMap implements WritableMap {
    private static native HybridData initHybrid();

    private native void mergeNativeMap(ReadableNativeMap readableNativeMap);

    private native void putNativeArray(String str, WritableNativeArray writableNativeArray);

    private native void putNativeMap(String str, WritableNativeMap writableNativeMap);

    public native void putBoolean(@Nonnull String str, boolean z);

    public native void putDouble(@Nonnull String str, double d);

    public native void putInt(@Nonnull String str, int i);

    public native void putNull(@NonNull String str);

    public native void putString(@Nonnull String str, @Nullable String str2);

    static {
        ReactBridge.staticInit();
    }

    public void putMap(@Nonnull String str, @Nullable WritableMap writableMap) {
        Assertions.assertCondition(writableMap == null || (writableMap instanceof WritableNativeMap), "Illegal type provided");
        putNativeMap(str, (WritableNativeMap) writableMap);
    }

    public void putArray(@Nonnull String str, @Nullable WritableArray writableArray) {
        Assertions.assertCondition(writableArray == null || (writableArray instanceof WritableNativeArray), "Illegal type provided");
        putNativeArray(str, (WritableNativeArray) writableArray);
    }

    public void merge(@Nonnull ReadableMap readableMap) {
        Assertions.assertCondition(readableMap instanceof ReadableNativeMap, "Illegal type provided");
        mergeNativeMap((ReadableNativeMap) readableMap);
    }

    public WritableNativeMap() {
        super(initHybrid());
    }
}

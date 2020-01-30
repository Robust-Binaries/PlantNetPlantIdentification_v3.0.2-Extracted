package com.facebook.react.bridge;

import com.facebook.infer.annotation.Assertions;
import com.facebook.jni.HybridData;
import com.facebook.proguard.annotations.DoNotStrip;
import com.facebook.react.config.ReactFeatureFlags;
import java.util.ArrayList;
import java.util.Arrays;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@DoNotStrip
public class ReadableNativeArray extends NativeArray implements ReadableArray {
    private static int jniPassCounter = 0;
    @Nullable
    private Object[] mLocalArray;
    @Nullable
    private ReadableType[] mLocalTypeArray;

    private native ReadableNativeArray getArrayNative(int i);

    private native boolean getBooleanNative(int i);

    private native double getDoubleNative(int i);

    private native int getIntNative(int i);

    private native ReadableNativeMap getMapNative(int i);

    private native String getStringNative(int i);

    private native ReadableType getTypeNative(int i);

    private native Object[] importArray();

    private native Object[] importTypeArray();

    private native boolean isNullNative(int i);

    private native int sizeNative();

    static {
        ReactBridge.staticInit();
    }

    protected ReadableNativeArray(HybridData hybridData) {
        super(hybridData);
    }

    public static void setUseNativeAccessor(boolean z) {
        ReactFeatureFlags.useArrayNativeAccessor = z;
    }

    public static int getJNIPassCounter() {
        return jniPassCounter;
    }

    private Object[] getLocalArray() {
        Object[] objArr = this.mLocalArray;
        if (objArr != null) {
            return objArr;
        }
        synchronized (this) {
            if (this.mLocalArray == null) {
                jniPassCounter++;
                this.mLocalArray = (Object[]) Assertions.assertNotNull(importArray());
            }
        }
        return this.mLocalArray;
    }

    private ReadableType[] getLocalTypeArray() {
        ReadableType[] readableTypeArr = this.mLocalTypeArray;
        if (readableTypeArr != null) {
            return readableTypeArr;
        }
        synchronized (this) {
            if (this.mLocalTypeArray == null) {
                jniPassCounter++;
                Object[] objArr = (Object[]) Assertions.assertNotNull(importTypeArray());
                this.mLocalTypeArray = (ReadableType[]) Arrays.copyOf(objArr, objArr.length, ReadableType[].class);
            }
        }
        return this.mLocalTypeArray;
    }

    public int size() {
        if (!ReactFeatureFlags.useArrayNativeAccessor) {
            return getLocalArray().length;
        }
        jniPassCounter++;
        return sizeNative();
    }

    public boolean isNull(int i) {
        boolean z = true;
        if (ReactFeatureFlags.useArrayNativeAccessor) {
            jniPassCounter++;
            return isNullNative(i);
        }
        if (getLocalArray()[i] != null) {
            z = false;
        }
        return z;
    }

    public boolean getBoolean(int i) {
        if (!ReactFeatureFlags.useArrayNativeAccessor) {
            return ((Boolean) getLocalArray()[i]).booleanValue();
        }
        jniPassCounter++;
        return getBooleanNative(i);
    }

    public double getDouble(int i) {
        if (!ReactFeatureFlags.useArrayNativeAccessor) {
            return ((Double) getLocalArray()[i]).doubleValue();
        }
        jniPassCounter++;
        return getDoubleNative(i);
    }

    public int getInt(int i) {
        if (!ReactFeatureFlags.useArrayNativeAccessor) {
            return ((Double) getLocalArray()[i]).intValue();
        }
        jniPassCounter++;
        return getIntNative(i);
    }

    @Nullable
    public String getString(int i) {
        if (!ReactFeatureFlags.useArrayNativeAccessor) {
            return (String) getLocalArray()[i];
        }
        jniPassCounter++;
        return getStringNative(i);
    }

    @Nullable
    public ReadableNativeArray getArray(int i) {
        if (!ReactFeatureFlags.useArrayNativeAccessor) {
            return (ReadableNativeArray) getLocalArray()[i];
        }
        jniPassCounter++;
        return getArrayNative(i);
    }

    @Nullable
    public ReadableNativeMap getMap(int i) {
        if (!ReactFeatureFlags.useArrayNativeAccessor) {
            return (ReadableNativeMap) getLocalArray()[i];
        }
        jniPassCounter++;
        return getMapNative(i);
    }

    @Nonnull
    public ReadableType getType(int i) {
        if (!ReactFeatureFlags.useArrayNativeAccessor) {
            return getLocalTypeArray()[i];
        }
        jniPassCounter++;
        return getTypeNative(i);
    }

    @Nonnull
    public Dynamic getDynamic(int i) {
        return DynamicFromArray.create(this, i);
    }

    @Nonnull
    public ArrayList<Object> toArrayList() {
        ArrayList<Object> arrayList = new ArrayList<>();
        for (int i = 0; i < size(); i++) {
            switch (getType(i)) {
                case Null:
                    arrayList.add(null);
                    break;
                case Boolean:
                    arrayList.add(Boolean.valueOf(getBoolean(i)));
                    break;
                case Number:
                    arrayList.add(Double.valueOf(getDouble(i)));
                    break;
                case String:
                    arrayList.add(getString(i));
                    break;
                case Map:
                    arrayList.add(getMap(i).toHashMap());
                    break;
                case Array:
                    arrayList.add(getArray(i).toArrayList());
                    break;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Could not convert object at index: ");
                    sb.append(i);
                    sb.append(".");
                    throw new IllegalArgumentException(sb.toString());
            }
        }
        return arrayList;
    }
}

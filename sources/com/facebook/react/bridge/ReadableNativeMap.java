package com.facebook.react.bridge;

import com.facebook.infer.annotation.Assertions;
import com.facebook.jni.HybridData;
import com.facebook.proguard.annotations.DoNotStrip;
import com.facebook.react.config.ReactFeatureFlags;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@DoNotStrip
public class ReadableNativeMap extends NativeMap implements ReadableMap {
    private static int mJniCallCounter;
    @Nullable
    private String[] mKeys;
    @Nullable
    private HashMap<String, Object> mLocalMap;
    @Nullable
    private HashMap<String, ReadableType> mLocalTypeMap;

    @DoNotStrip
    private static class ReadableNativeMapKeySetIterator implements ReadableMapKeySetIterator {
        @DoNotStrip
        private final HybridData mHybridData;
        @DoNotStrip
        private final ReadableNativeMap mMap;

        private static native HybridData initHybrid(ReadableNativeMap readableNativeMap);

        public native boolean hasNextKey();

        public native String nextKey();

        public ReadableNativeMapKeySetIterator(ReadableNativeMap readableNativeMap) {
            this.mMap = readableNativeMap;
            this.mHybridData = initHybrid(readableNativeMap);
        }
    }

    private native ReadableNativeArray getArrayNative(String str);

    private native boolean getBooleanNative(String str);

    private native double getDoubleNative(String str);

    private native int getIntNative(String str);

    private native ReadableNativeMap getMapNative(String str);

    private native String getStringNative(String str);

    private native ReadableType getTypeNative(String str);

    private native boolean hasKeyNative(String str);

    private native String[] importKeys();

    private native Object[] importTypes();

    private native Object[] importValues();

    private native boolean isNullNative(@Nonnull String str);

    static {
        ReactBridge.staticInit();
    }

    protected ReadableNativeMap(HybridData hybridData) {
        super(hybridData);
    }

    public static void setUseNativeAccessor(boolean z) {
        ReactFeatureFlags.useMapNativeAccessor = z;
    }

    public static int getJNIPassCounter() {
        return mJniCallCounter;
    }

    private HashMap<String, Object> getLocalMap() {
        HashMap<String, Object> hashMap = this.mLocalMap;
        if (hashMap != null) {
            return hashMap;
        }
        synchronized (this) {
            if (this.mKeys == null) {
                this.mKeys = (String[]) Assertions.assertNotNull(importKeys());
                mJniCallCounter++;
            }
            if (this.mLocalMap == null) {
                Object[] objArr = (Object[]) Assertions.assertNotNull(importValues());
                mJniCallCounter++;
                int length = this.mKeys.length;
                this.mLocalMap = new HashMap<>(length);
                for (int i = 0; i < length; i++) {
                    this.mLocalMap.put(this.mKeys[i], objArr[i]);
                }
            }
        }
        return this.mLocalMap;
    }

    @Nonnull
    private HashMap<String, ReadableType> getLocalTypeMap() {
        HashMap<String, ReadableType> hashMap = this.mLocalTypeMap;
        if (hashMap != null) {
            return hashMap;
        }
        synchronized (this) {
            if (this.mKeys == null) {
                this.mKeys = (String[]) Assertions.assertNotNull(importKeys());
                mJniCallCounter++;
            }
            if (this.mLocalTypeMap == null) {
                Object[] objArr = (Object[]) Assertions.assertNotNull(importTypes());
                mJniCallCounter++;
                int length = this.mKeys.length;
                this.mLocalTypeMap = new HashMap<>(length);
                for (int i = 0; i < length; i++) {
                    this.mLocalTypeMap.put(this.mKeys[i], (ReadableType) objArr[i]);
                }
            }
        }
        return this.mLocalTypeMap;
    }

    public boolean hasKey(@Nonnull String str) {
        if (!ReactFeatureFlags.useMapNativeAccessor) {
            return getLocalMap().containsKey(str);
        }
        mJniCallCounter++;
        return hasKeyNative(str);
    }

    public boolean isNull(@Nonnull String str) {
        boolean z = true;
        if (ReactFeatureFlags.useMapNativeAccessor) {
            mJniCallCounter++;
            return isNullNative(str);
        } else if (getLocalMap().containsKey(str)) {
            if (getLocalMap().get(str) != null) {
                z = false;
            }
            return z;
        } else {
            throw new NoSuchKeyException(str);
        }
    }

    @Nonnull
    private Object getValue(@Nonnull String str) {
        if (hasKey(str) && !isNull(str)) {
            return Assertions.assertNotNull(getLocalMap().get(str));
        }
        throw new NoSuchKeyException(str);
    }

    private <T> T getValue(String str, Class<T> cls) {
        T value = getValue(str);
        checkInstance(str, value, cls);
        return value;
    }

    @Nullable
    private Object getNullableValue(String str) {
        if (hasKey(str)) {
            return getLocalMap().get(str);
        }
        throw new NoSuchKeyException(str);
    }

    @Nullable
    private <T> T getNullableValue(String str, Class<T> cls) {
        T nullableValue = getNullableValue(str);
        checkInstance(str, nullableValue, cls);
        return nullableValue;
    }

    private void checkInstance(String str, Object obj, Class cls) {
        if (obj != null && !cls.isInstance(obj)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Value for ");
            sb.append(str);
            sb.append(" cannot be cast from ");
            sb.append(obj.getClass().getSimpleName());
            sb.append(" to ");
            sb.append(cls.getSimpleName());
            throw new ClassCastException(sb.toString());
        }
    }

    public boolean getBoolean(@Nonnull String str) {
        if (!ReactFeatureFlags.useMapNativeAccessor) {
            return ((Boolean) getValue(str, Boolean.class)).booleanValue();
        }
        mJniCallCounter++;
        return getBooleanNative(str);
    }

    public double getDouble(@Nonnull String str) {
        if (!ReactFeatureFlags.useMapNativeAccessor) {
            return ((Double) getValue(str, Double.class)).doubleValue();
        }
        mJniCallCounter++;
        return getDoubleNative(str);
    }

    public int getInt(@Nonnull String str) {
        if (!ReactFeatureFlags.useMapNativeAccessor) {
            return ((Double) getValue(str, Double.class)).intValue();
        }
        mJniCallCounter++;
        return getIntNative(str);
    }

    @Nullable
    public String getString(@Nonnull String str) {
        if (!ReactFeatureFlags.useMapNativeAccessor) {
            return (String) getNullableValue(str, String.class);
        }
        mJniCallCounter++;
        return getStringNative(str);
    }

    @Nullable
    public ReadableArray getArray(@Nonnull String str) {
        if (!ReactFeatureFlags.useMapNativeAccessor) {
            return (ReadableArray) getNullableValue(str, ReadableArray.class);
        }
        mJniCallCounter++;
        return getArrayNative(str);
    }

    @Nullable
    public ReadableNativeMap getMap(@Nonnull String str) {
        if (!ReactFeatureFlags.useMapNativeAccessor) {
            return (ReadableNativeMap) getNullableValue(str, ReadableNativeMap.class);
        }
        mJniCallCounter++;
        return getMapNative(str);
    }

    @Nonnull
    public ReadableType getType(@Nonnull String str) {
        if (ReactFeatureFlags.useMapNativeAccessor) {
            mJniCallCounter++;
            return getTypeNative(str);
        } else if (getLocalTypeMap().containsKey(str)) {
            return (ReadableType) Assertions.assertNotNull(getLocalTypeMap().get(str));
        } else {
            throw new NoSuchKeyException(str);
        }
    }

    @Nonnull
    public Dynamic getDynamic(@Nonnull String str) {
        return DynamicFromMap.create(this, str);
    }

    @Nonnull
    public ReadableMapKeySetIterator keySetIterator() {
        return new ReadableNativeMapKeySetIterator(this);
    }

    @Nonnull
    public HashMap<String, Object> toHashMap() {
        if (ReactFeatureFlags.useMapNativeAccessor) {
            ReadableMapKeySetIterator keySetIterator = keySetIterator();
            HashMap<String, Object> hashMap = new HashMap<>();
            while (keySetIterator.hasNextKey()) {
                mJniCallCounter++;
                String nextKey = keySetIterator.nextKey();
                mJniCallCounter++;
                switch (getType(nextKey)) {
                    case Null:
                        hashMap.put(nextKey, null);
                        break;
                    case Boolean:
                        hashMap.put(nextKey, Boolean.valueOf(getBoolean(nextKey)));
                        break;
                    case Number:
                        hashMap.put(nextKey, Double.valueOf(getDouble(nextKey)));
                        break;
                    case String:
                        hashMap.put(nextKey, getString(nextKey));
                        break;
                    case Map:
                        hashMap.put(nextKey, ((ReadableNativeMap) Assertions.assertNotNull(getMap(nextKey))).toHashMap());
                        break;
                    case Array:
                        hashMap.put(nextKey, ((ReadableArray) Assertions.assertNotNull(getArray(nextKey))).toArrayList());
                        break;
                    default:
                        StringBuilder sb = new StringBuilder();
                        sb.append("Could not convert object with key: ");
                        sb.append(nextKey);
                        sb.append(".");
                        throw new IllegalArgumentException(sb.toString());
                }
            }
            return hashMap;
        }
        HashMap<String, Object> hashMap2 = new HashMap<>(getLocalMap());
        for (String str : hashMap2.keySet()) {
            switch (getType(str)) {
                case Null:
                case Boolean:
                case Number:
                case String:
                    break;
                case Map:
                    hashMap2.put(str, ((ReadableNativeMap) Assertions.assertNotNull(getMap(str))).toHashMap());
                    break;
                case Array:
                    hashMap2.put(str, ((ReadableArray) Assertions.assertNotNull(getArray(str))).toArrayList());
                    break;
                default:
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Could not convert object with key: ");
                    sb2.append(str);
                    sb2.append(".");
                    throw new IllegalArgumentException(sb2.toString());
            }
        }
        return hashMap2;
    }
}

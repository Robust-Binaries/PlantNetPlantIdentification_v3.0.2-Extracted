package com.facebook.react.bridge;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class JavaOnlyMap implements ReadableMap, WritableMap {
    /* access modifiers changed from: private */
    public final Map mBackingMap;

    /* renamed from: of */
    public static JavaOnlyMap m119of(Object... objArr) {
        return new JavaOnlyMap(objArr);
    }

    public static JavaOnlyMap deepClone(ReadableMap readableMap) {
        JavaOnlyMap javaOnlyMap = new JavaOnlyMap();
        ReadableMapKeySetIterator keySetIterator = readableMap.keySetIterator();
        while (keySetIterator.hasNextKey()) {
            String nextKey = keySetIterator.nextKey();
            switch (readableMap.getType(nextKey)) {
                case Null:
                    javaOnlyMap.putNull(nextKey);
                    break;
                case Boolean:
                    javaOnlyMap.putBoolean(nextKey, readableMap.getBoolean(nextKey));
                    break;
                case Number:
                    javaOnlyMap.putDouble(nextKey, readableMap.getDouble(nextKey));
                    break;
                case String:
                    javaOnlyMap.putString(nextKey, readableMap.getString(nextKey));
                    break;
                case Map:
                    javaOnlyMap.putMap(nextKey, deepClone(readableMap.getMap(nextKey)));
                    break;
                case Array:
                    javaOnlyMap.putArray(nextKey, JavaOnlyArray.deepClone(readableMap.getArray(nextKey)));
                    break;
            }
        }
        return javaOnlyMap;
    }

    private JavaOnlyMap(Object... objArr) {
        if (objArr.length % 2 == 0) {
            this.mBackingMap = new HashMap();
            for (int i = 0; i < objArr.length; i += 2) {
                this.mBackingMap.put(objArr[i], objArr[i + 1]);
            }
            return;
        }
        throw new IllegalArgumentException("You must provide the same number of keys and values");
    }

    public JavaOnlyMap() {
        this.mBackingMap = new HashMap();
    }

    public boolean hasKey(@Nonnull String str) {
        return this.mBackingMap.containsKey(str);
    }

    public boolean isNull(@Nonnull String str) {
        return this.mBackingMap.get(str) == null;
    }

    public boolean getBoolean(@Nonnull String str) {
        return ((Boolean) this.mBackingMap.get(str)).booleanValue();
    }

    public double getDouble(@Nonnull String str) {
        return ((Number) this.mBackingMap.get(str)).doubleValue();
    }

    public int getInt(@Nonnull String str) {
        return ((Number) this.mBackingMap.get(str)).intValue();
    }

    public String getString(@Nonnull String str) {
        return (String) this.mBackingMap.get(str);
    }

    public ReadableMap getMap(@Nonnull String str) {
        return (ReadableMap) this.mBackingMap.get(str);
    }

    public JavaOnlyArray getArray(@Nonnull String str) {
        return (JavaOnlyArray) this.mBackingMap.get(str);
    }

    @Nonnull
    public Dynamic getDynamic(@Nonnull String str) {
        return DynamicFromMap.create(this, str);
    }

    @Nonnull
    public ReadableType getType(@Nonnull String str) {
        Object obj = this.mBackingMap.get(str);
        if (obj == null) {
            return ReadableType.Null;
        }
        if (obj instanceof Number) {
            return ReadableType.Number;
        }
        if (obj instanceof String) {
            return ReadableType.String;
        }
        if (obj instanceof Boolean) {
            return ReadableType.Boolean;
        }
        if (obj instanceof ReadableMap) {
            return ReadableType.Map;
        }
        if (obj instanceof ReadableArray) {
            return ReadableType.Array;
        }
        if (obj instanceof Dynamic) {
            return ((Dynamic) obj).getType();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid value ");
        sb.append(obj.toString());
        sb.append(" for key ");
        sb.append(str);
        sb.append("contained in JavaOnlyMap");
        throw new IllegalArgumentException(sb.toString());
    }

    @Nonnull
    public ReadableMapKeySetIterator keySetIterator() {
        return new ReadableMapKeySetIterator() {
            Iterator<String> mIterator = JavaOnlyMap.this.mBackingMap.keySet().iterator();

            public boolean hasNextKey() {
                return this.mIterator.hasNext();
            }

            public String nextKey() {
                return (String) this.mIterator.next();
            }
        };
    }

    public void putBoolean(@Nonnull String str, boolean z) {
        this.mBackingMap.put(str, Boolean.valueOf(z));
    }

    public void putDouble(@Nonnull String str, double d) {
        this.mBackingMap.put(str, Double.valueOf(d));
    }

    public void putInt(@Nonnull String str, int i) {
        this.mBackingMap.put(str, Integer.valueOf(i));
    }

    public void putString(@Nonnull String str, @Nullable String str2) {
        this.mBackingMap.put(str, str2);
    }

    public void putNull(@Nonnull String str) {
        this.mBackingMap.put(str, null);
    }

    public void putMap(@Nonnull String str, @Nullable WritableMap writableMap) {
        this.mBackingMap.put(str, writableMap);
    }

    public void merge(@Nonnull ReadableMap readableMap) {
        this.mBackingMap.putAll(((JavaOnlyMap) readableMap).mBackingMap);
    }

    public void putArray(@Nonnull String str, @Nullable WritableArray writableArray) {
        this.mBackingMap.put(str, writableArray);
    }

    @Nonnull
    public HashMap<String, Object> toHashMap() {
        return new HashMap<>(this.mBackingMap);
    }

    public String toString() {
        return this.mBackingMap.toString();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        JavaOnlyMap javaOnlyMap = (JavaOnlyMap) obj;
        Map map = this.mBackingMap;
        return map == null ? javaOnlyMap.mBackingMap == null : map.equals(javaOnlyMap.mBackingMap);
    }

    public int hashCode() {
        Map map = this.mBackingMap;
        if (map != null) {
            return map.hashCode();
        }
        return 0;
    }
}

package org.pgsqlite;

import com.facebook.react.bridge.NoSuchKeyException;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;

public abstract class SQLitePluginConverter {
    static String getString(ReadableMap readableMap, String str, String str2) {
        if (readableMap == null) {
            return str2;
        }
        try {
            switch (readableMap.getType(str)) {
                case Number:
                    double d = readableMap.getDouble(str);
                    long j = (long) d;
                    if (d == ((double) j)) {
                        return String.valueOf(j);
                    }
                    return String.valueOf(d);
                case Boolean:
                    return String.valueOf(readableMap.getBoolean(str));
                case String:
                    return readableMap.getString(str);
                case Null:
                    return null;
                default:
                    return str2;
            }
        } catch (NoSuchKeyException unused) {
            return str2;
        }
    }

    static boolean getBoolean(ReadableMap readableMap, String str, boolean z) {
        if (readableMap == null) {
            return z;
        }
        try {
            switch (readableMap.getType(str)) {
                case Number:
                    if (readableMap.getDouble(str) == 0.0d) {
                        return Boolean.FALSE.booleanValue();
                    }
                    return Boolean.TRUE.booleanValue();
                case Boolean:
                    return readableMap.getBoolean(str);
                case String:
                    String string = readableMap.getString(str);
                    if ("true".equalsIgnoreCase(string)) {
                        return true;
                    }
                    return "false".equalsIgnoreCase(string) ? false : false;
                case Null:
                    return false;
                default:
                    return z;
            }
        } catch (NoSuchKeyException unused) {
            return z;
        }
    }

    static String getString(ReadableArray readableArray, int i, String str) {
        if (readableArray == null) {
            return str;
        }
        try {
            switch (readableArray.getType(i)) {
                case Number:
                    double d = readableArray.getDouble(i);
                    long j = (long) d;
                    if (d == ((double) j)) {
                        return String.valueOf(j);
                    }
                    return String.valueOf(d);
                case Boolean:
                    return String.valueOf(readableArray.getBoolean(i));
                case String:
                    return readableArray.getString(i);
                case Null:
                    return null;
                default:
                    return str;
            }
        } catch (NoSuchKeyException unused) {
            return str;
        }
    }

    static boolean getBoolean(ReadableArray readableArray, int i, boolean z) {
        if (readableArray == null) {
            return z;
        }
        try {
            switch (readableArray.getType(i)) {
                case Number:
                    if (readableArray.getDouble(i) == 0.0d) {
                        return Boolean.FALSE.booleanValue();
                    }
                    return Boolean.TRUE.booleanValue();
                case Boolean:
                    return readableArray.getBoolean(i);
                case String:
                    String string = readableArray.getString(i);
                    if ("true".equalsIgnoreCase(string)) {
                        return true;
                    }
                    return "false".equalsIgnoreCase(string) ? false : false;
                case Null:
                    return false;
                default:
                    return z;
            }
        } catch (NoSuchKeyException unused) {
            return z;
        }
    }

    static Object get(ReadableMap readableMap, String str, Object obj) {
        if (readableMap == null) {
            return obj;
        }
        try {
            Object obj2 = null;
            switch (readableMap.getType(str)) {
                case Number:
                    obj2 = Double.valueOf(readableMap.getDouble(str));
                    break;
                case Boolean:
                    obj2 = Boolean.valueOf(readableMap.getBoolean(str));
                    break;
                case String:
                    obj2 = readableMap.getString(str);
                    break;
                case Null:
                    break;
                case Map:
                    obj2 = readableMap.getMap(str);
                    break;
                case Array:
                    obj2 = readableMap.getArray(str);
                    break;
            }
            return obj2;
        } catch (NoSuchKeyException unused) {
            return obj;
        }
    }

    static Object get(ReadableArray readableArray, int i, Object obj) {
        if (readableArray == null) {
            return obj;
        }
        Object obj2 = null;
        try {
            switch (readableArray.getType(i)) {
                case Number:
                    obj2 = Double.valueOf(readableArray.getDouble(i));
                    break;
                case Boolean:
                    obj2 = Boolean.valueOf(readableArray.getBoolean(i));
                    break;
                case String:
                    obj2 = readableArray.getString(i);
                    break;
                case Map:
                    obj2 = readableArray.getMap(i);
                    break;
                case Array:
                    obj2 = readableArray.getArray(i);
                    break;
            }
            return obj2;
        } catch (NoSuchKeyException unused) {
            return obj;
        }
    }
}

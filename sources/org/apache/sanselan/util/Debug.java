package org.apache.sanselan.util;

import android.support.p003v7.widget.helper.ItemTouchHelper.Callback;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.color.ICC_Profile;
import java.io.File;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import kotlin.UByte;

public final class Debug {
    private static long counter = 0;
    public static String newline = "\r\n";
    private static final SimpleDateFormat timestamp = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss:SSS");

    public static String getDebug(String str) {
        return str;
    }

    public static void debug(String str) {
        System.out.println(str);
    }

    public static void debug(Object obj) {
        System.out.println(obj == null ? "null" : obj.toString());
    }

    public static void debug() {
        newline();
    }

    public static void newline() {
        System.out.print(newline);
    }

    public static String getDebug(String str, int i) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(str);
        stringBuffer.append(": ");
        stringBuffer.append(i);
        return getDebug(stringBuffer.toString());
    }

    public static String getDebug(String str, double d) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(str);
        stringBuffer.append(": ");
        stringBuffer.append(d);
        return getDebug(stringBuffer.toString());
    }

    public static String getDebug(String str, String str2) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(str);
        stringBuffer.append(" ");
        stringBuffer.append(str2);
        return getDebug(stringBuffer.toString());
    }

    public static String getDebug(String str, long j) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(str);
        stringBuffer.append(" ");
        stringBuffer.append(Long.toString(j));
        return getDebug(stringBuffer.toString());
    }

    public static String getDebug(String str, int[] iArr) {
        StringBuffer stringBuffer = new StringBuffer();
        if (iArr == null) {
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2.append(str);
            stringBuffer2.append(" (");
            stringBuffer2.append(null);
            stringBuffer2.append(")");
            stringBuffer2.append(newline);
            stringBuffer.append(stringBuffer2.toString());
        } else {
            StringBuffer stringBuffer3 = new StringBuffer();
            stringBuffer3.append(str);
            stringBuffer3.append(" (");
            stringBuffer3.append(iArr.length);
            stringBuffer3.append(")");
            stringBuffer3.append(newline);
            stringBuffer.append(stringBuffer3.toString());
            for (int append : iArr) {
                StringBuffer stringBuffer4 = new StringBuffer();
                stringBuffer4.append("\t");
                stringBuffer4.append(append);
                stringBuffer4.append(newline);
                stringBuffer.append(stringBuffer4.toString());
            }
            stringBuffer.append(newline);
        }
        return stringBuffer.toString();
    }

    public static String getDebug(String str, byte[] bArr) {
        return getDebug(str, bArr, Callback.DEFAULT_SWIPE_ANIMATION_DURATION);
    }

    public static String getDebug(String str, byte[] bArr, int i) {
        StringBuffer stringBuffer = new StringBuffer();
        if (bArr == null) {
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2.append(str);
            stringBuffer2.append(" (");
            stringBuffer2.append(null);
            stringBuffer2.append(")");
            stringBuffer2.append(newline);
            stringBuffer.append(stringBuffer2.toString());
        } else {
            StringBuffer stringBuffer3 = new StringBuffer();
            stringBuffer3.append(str);
            stringBuffer3.append(" (");
            stringBuffer3.append(bArr.length);
            stringBuffer3.append(")");
            stringBuffer3.append(newline);
            stringBuffer.append(stringBuffer3.toString());
            int i2 = 0;
            while (i2 < i && i2 < bArr.length) {
                byte b = bArr[i2] & UByte.MAX_VALUE;
                char c = (b == 0 || b == 10 || b == 11 || b == 13) ? ' ' : (char) b;
                StringBuffer stringBuffer4 = new StringBuffer();
                stringBuffer4.append("\t");
                stringBuffer4.append(i2);
                stringBuffer4.append(": ");
                stringBuffer4.append(b);
                stringBuffer4.append(" (");
                stringBuffer4.append(c);
                stringBuffer4.append(", 0x");
                stringBuffer4.append(Integer.toHexString(b));
                stringBuffer4.append(")");
                stringBuffer4.append(newline);
                stringBuffer.append(stringBuffer4.toString());
                i2++;
            }
            if (bArr.length > i) {
                StringBuffer stringBuffer5 = new StringBuffer();
                stringBuffer5.append("\t...");
                stringBuffer5.append(newline);
                stringBuffer.append(stringBuffer5.toString());
            }
            stringBuffer.append(newline);
        }
        return stringBuffer.toString();
    }

    public static String getDebug(String str, char[] cArr) {
        StringBuffer stringBuffer = new StringBuffer();
        if (cArr == null) {
            StringBuffer stringBuffer2 = new StringBuffer();
            StringBuffer stringBuffer3 = new StringBuffer();
            stringBuffer3.append(str);
            stringBuffer3.append(" (");
            stringBuffer3.append(null);
            stringBuffer3.append(")");
            stringBuffer2.append(getDebug(stringBuffer3.toString()));
            stringBuffer2.append(newline);
            stringBuffer.append(stringBuffer2.toString());
        } else {
            StringBuffer stringBuffer4 = new StringBuffer();
            StringBuffer stringBuffer5 = new StringBuffer();
            stringBuffer5.append(str);
            stringBuffer5.append(" (");
            stringBuffer5.append(cArr.length);
            stringBuffer5.append(")");
            stringBuffer4.append(getDebug(stringBuffer5.toString()));
            stringBuffer4.append(newline);
            stringBuffer.append(stringBuffer4.toString());
            for (int i = 0; i < cArr.length; i++) {
                StringBuffer stringBuffer6 = new StringBuffer();
                StringBuffer stringBuffer7 = new StringBuffer();
                stringBuffer7.append("\t");
                stringBuffer7.append(cArr[i]);
                stringBuffer7.append(" (");
                stringBuffer7.append(cArr[i] & 255);
                stringBuffer6.append(getDebug(stringBuffer7.toString()));
                stringBuffer6.append(")");
                stringBuffer6.append(newline);
                stringBuffer.append(stringBuffer6.toString());
            }
            stringBuffer.append(newline);
        }
        return stringBuffer.toString();
    }

    public static String getDebug(String str, List list) {
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer stringBuffer2 = new StringBuffer();
        stringBuffer2.append(" [");
        long j = counter;
        counter = 1 + j;
        stringBuffer2.append(j);
        stringBuffer2.append("]");
        String stringBuffer3 = stringBuffer2.toString();
        StringBuffer stringBuffer4 = new StringBuffer();
        StringBuffer stringBuffer5 = new StringBuffer();
        stringBuffer5.append(str);
        stringBuffer5.append(" (");
        stringBuffer5.append(list.size());
        stringBuffer5.append(")");
        stringBuffer5.append(stringBuffer3);
        stringBuffer4.append(getDebug(stringBuffer5.toString()));
        stringBuffer4.append(newline);
        stringBuffer.append(stringBuffer4.toString());
        for (int i = 0; i < list.size(); i++) {
            StringBuffer stringBuffer6 = new StringBuffer();
            StringBuffer stringBuffer7 = new StringBuffer();
            stringBuffer7.append("\t");
            stringBuffer7.append(list.get(i).toString());
            stringBuffer7.append(stringBuffer3);
            stringBuffer6.append(getDebug(stringBuffer7.toString()));
            stringBuffer6.append(newline);
            stringBuffer.append(stringBuffer6.toString());
        }
        stringBuffer.append(newline);
        return stringBuffer.toString();
    }

    public static void debug(String str, Map map) {
        debug(getDebug(str, map));
    }

    public static String getDebug(String str, Map map) {
        StringBuffer stringBuffer = new StringBuffer();
        if (map == null) {
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2.append(str);
            stringBuffer2.append(" map: ");
            stringBuffer2.append(null);
            return getDebug(stringBuffer2.toString());
        }
        ArrayList arrayList = new ArrayList(map.keySet());
        StringBuffer stringBuffer3 = new StringBuffer();
        StringBuffer stringBuffer4 = new StringBuffer();
        stringBuffer4.append(str);
        stringBuffer4.append(" map: ");
        stringBuffer4.append(arrayList.size());
        stringBuffer3.append(getDebug(stringBuffer4.toString()));
        stringBuffer3.append(newline);
        stringBuffer.append(stringBuffer3.toString());
        for (int i = 0; i < arrayList.size(); i++) {
            Object obj = arrayList.get(i);
            Object obj2 = map.get(obj);
            StringBuffer stringBuffer5 = new StringBuffer();
            StringBuffer stringBuffer6 = new StringBuffer();
            stringBuffer6.append("\t");
            stringBuffer6.append(i);
            stringBuffer6.append(": '");
            stringBuffer6.append(obj);
            stringBuffer6.append("' -> '");
            stringBuffer6.append(obj2);
            stringBuffer6.append("'");
            stringBuffer5.append(getDebug(stringBuffer6.toString()));
            stringBuffer5.append(newline);
            stringBuffer.append(stringBuffer5.toString());
        }
        stringBuffer.append(newline);
        return stringBuffer.toString();
    }

    public static boolean compare(String str, Map map, Map map2) {
        return compare(str, map, map2, null, null);
    }

    private static void log(StringBuffer stringBuffer, String str) {
        debug(str);
        if (stringBuffer != null) {
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2.append(str);
            stringBuffer2.append(newline);
            stringBuffer.append(stringBuffer2.toString());
        }
    }

    public static boolean compare(String str, Map map, Map map2, ArrayList arrayList, StringBuffer stringBuffer) {
        boolean z = true;
        if (map == null && map2 == null) {
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2.append(str);
            stringBuffer2.append(" both maps null");
            log(stringBuffer, stringBuffer2.toString());
            return true;
        } else if (map == null) {
            StringBuffer stringBuffer3 = new StringBuffer();
            stringBuffer3.append(str);
            stringBuffer3.append(" map a: null, map b: map");
            log(stringBuffer, stringBuffer3.toString());
            return false;
        } else if (map2 == null) {
            StringBuffer stringBuffer4 = new StringBuffer();
            stringBuffer4.append(str);
            stringBuffer4.append(" map a: map, map b: null");
            log(stringBuffer, stringBuffer4.toString());
            return false;
        } else {
            ArrayList arrayList2 = new ArrayList(map.keySet());
            ArrayList arrayList3 = new ArrayList(map2.keySet());
            if (arrayList != null) {
                arrayList2.removeAll(arrayList);
                arrayList3.removeAll(arrayList);
            }
            for (int i = 0; i < arrayList2.size(); i++) {
                Object obj = arrayList2.get(i);
                if (!arrayList3.contains(obj)) {
                    StringBuffer stringBuffer5 = new StringBuffer();
                    stringBuffer5.append(str);
                    stringBuffer5.append("b is missing key '");
                    stringBuffer5.append(obj);
                    stringBuffer5.append("' from a");
                    log(stringBuffer, stringBuffer5.toString());
                    z = false;
                } else {
                    arrayList3.remove(obj);
                    Object obj2 = map.get(obj);
                    Object obj3 = map2.get(obj);
                    if (!obj2.equals(obj3)) {
                        StringBuffer stringBuffer6 = new StringBuffer();
                        stringBuffer6.append(str);
                        stringBuffer6.append("key(");
                        stringBuffer6.append(obj);
                        stringBuffer6.append(") value a: ");
                        stringBuffer6.append(obj2);
                        stringBuffer6.append(") !=  b: ");
                        stringBuffer6.append(obj3);
                        stringBuffer6.append(")");
                        log(stringBuffer, stringBuffer6.toString());
                        z = false;
                    }
                }
            }
            int i2 = 0;
            while (i2 < arrayList3.size()) {
                Object obj4 = arrayList3.get(i2);
                StringBuffer stringBuffer7 = new StringBuffer();
                stringBuffer7.append(str);
                stringBuffer7.append("a is missing key '");
                stringBuffer7.append(obj4);
                stringBuffer7.append("' from b");
                log(stringBuffer, stringBuffer7.toString());
                i2++;
                z = false;
            }
            if (z) {
                StringBuffer stringBuffer8 = new StringBuffer();
                stringBuffer8.append(str);
                stringBuffer8.append("a is the same as  b");
                log(stringBuffer, stringBuffer8.toString());
            }
            return z;
        }
    }

    private static final String byteQuadToString(int i) {
        byte b = (byte) ((i >> 24) & 255);
        byte b2 = (byte) ((i >> 16) & 255);
        byte b3 = (byte) ((i >> 8) & 255);
        byte b4 = (byte) ((i >> 0) & 255);
        char c = (char) b;
        char c2 = (char) b2;
        char c3 = (char) b3;
        char c4 = (char) b4;
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(new String(new char[]{c, c2, c3, c4}));
        StringBuffer stringBuffer2 = new StringBuffer();
        stringBuffer2.append(" bytequad: ");
        stringBuffer2.append(i);
        stringBuffer.append(stringBuffer2.toString());
        StringBuffer stringBuffer3 = new StringBuffer();
        stringBuffer3.append(" b1: ");
        stringBuffer3.append(b);
        stringBuffer.append(stringBuffer3.toString());
        StringBuffer stringBuffer4 = new StringBuffer();
        stringBuffer4.append(" b2: ");
        stringBuffer4.append(b2);
        stringBuffer.append(stringBuffer4.toString());
        StringBuffer stringBuffer5 = new StringBuffer();
        stringBuffer5.append(" b3: ");
        stringBuffer5.append(b3);
        stringBuffer.append(stringBuffer5.toString());
        StringBuffer stringBuffer6 = new StringBuffer();
        stringBuffer6.append(" b4: ");
        stringBuffer6.append(b4);
        stringBuffer.append(stringBuffer6.toString());
        return stringBuffer.toString();
    }

    public static String getDebug(String str, ICC_Profile iCC_Profile) {
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer stringBuffer2 = new StringBuffer();
        StringBuffer stringBuffer3 = new StringBuffer();
        stringBuffer3.append("ICC_Profile ");
        stringBuffer3.append(str);
        stringBuffer3.append(": ");
        stringBuffer3.append(iCC_Profile == null ? "null" : iCC_Profile.toString());
        stringBuffer2.append(getDebug(stringBuffer3.toString()));
        stringBuffer2.append(newline);
        stringBuffer.append(stringBuffer2.toString());
        if (iCC_Profile != null) {
            StringBuffer stringBuffer4 = new StringBuffer();
            StringBuffer stringBuffer5 = new StringBuffer();
            stringBuffer5.append("\t getProfileClass: ");
            stringBuffer5.append(byteQuadToString(iCC_Profile.getProfileClass()));
            stringBuffer4.append(getDebug(stringBuffer5.toString()));
            stringBuffer4.append(newline);
            stringBuffer.append(stringBuffer4.toString());
            StringBuffer stringBuffer6 = new StringBuffer();
            StringBuffer stringBuffer7 = new StringBuffer();
            stringBuffer7.append("\t getPCSType: ");
            stringBuffer7.append(byteQuadToString(iCC_Profile.getPCSType()));
            stringBuffer6.append(getDebug(stringBuffer7.toString()));
            stringBuffer6.append(newline);
            stringBuffer.append(stringBuffer6.toString());
            StringBuffer stringBuffer8 = new StringBuffer();
            StringBuffer stringBuffer9 = new StringBuffer();
            stringBuffer9.append("\t getColorSpaceType() : ");
            stringBuffer9.append(byteQuadToString(iCC_Profile.getColorSpaceType()));
            stringBuffer8.append(getDebug(stringBuffer9.toString()));
            stringBuffer8.append(newline);
            stringBuffer.append(stringBuffer8.toString());
        }
        return stringBuffer.toString();
    }

    public static String getDebug(String str, boolean z) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(str);
        stringBuffer.append(" ");
        stringBuffer.append(z ? "true" : "false");
        return getDebug(stringBuffer.toString());
    }

    public static String getDebug(String str, File file) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(str);
        stringBuffer.append(": ");
        stringBuffer.append(file == null ? "null" : file.getPath());
        return getDebug(stringBuffer.toString());
    }

    public static String getDebug(String str, Date date) {
        String str2;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        if (date == null) {
            str2 = "null";
        } else {
            str2 = simpleDateFormat.format(date);
        }
        return getDebug(str, str2);
    }

    public static String getDebug(String str, Calendar calendar) {
        String str2;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        if (calendar == null) {
            str2 = "null";
        } else {
            str2 = simpleDateFormat.format(calendar.getTime());
        }
        return getDebug(str, str2);
    }

    public static void debug(String str, Object obj) {
        if (obj == null) {
            debug(str, "null");
        } else if (obj instanceof char[]) {
            debug(str, (char[]) obj);
        } else if (obj instanceof byte[]) {
            debug(str, (byte[]) obj);
        } else if (obj instanceof int[]) {
            debug(str, (int[]) obj);
        } else if (obj instanceof String) {
            debug(str, (String) obj);
        } else if (obj instanceof List) {
            debug(str, (List) obj);
        } else if (obj instanceof Map) {
            debug(str, (Map) obj);
        } else if (obj instanceof ICC_Profile) {
            debug(str, (ICC_Profile) obj);
        } else if (obj instanceof File) {
            debug(str, (File) obj);
        } else if (obj instanceof Date) {
            debug(str, (Date) obj);
        } else if (obj instanceof Calendar) {
            debug(str, (Calendar) obj);
        } else {
            debug(str, obj.toString());
        }
    }

    public static void debug(String str, Object[] objArr) {
        if (objArr == null) {
            debug(str, "null");
        }
        debug(str, objArr.length);
        int i = 0;
        while (i < objArr.length && i < 10) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("\t");
            stringBuffer.append(i);
            debug(stringBuffer.toString(), objArr[i]);
            i++;
        }
        if (objArr.length > 10) {
            debug("\t...");
        }
        debug();
    }

    public static String getDebug(String str, Object obj) {
        if (obj == null) {
            return getDebug(str, "null");
        }
        if (obj instanceof Calendar) {
            return getDebug(str, (Calendar) obj);
        }
        if (obj instanceof Date) {
            return getDebug(str, (Date) obj);
        }
        if (obj instanceof File) {
            return getDebug(str, (File) obj);
        }
        if (obj instanceof ICC_Profile) {
            return getDebug(str, (ICC_Profile) obj);
        }
        boolean z = obj instanceof Map;
        if (z) {
            return getDebug(str, (Map) obj);
        }
        if (z) {
            return getDebug(str, (Map) obj);
        }
        if (obj instanceof String) {
            return getDebug(str, (String) obj);
        }
        if (obj instanceof byte[]) {
            return getDebug(str, (byte[]) obj);
        }
        if (obj instanceof char[]) {
            return getDebug(str, (char[]) obj);
        }
        if (obj instanceof int[]) {
            return getDebug(str, (int[]) obj);
        }
        if (obj instanceof List) {
            return getDebug(str, (List) obj);
        }
        return getDebug(str, obj.toString());
    }

    public static String getType(Object obj) {
        if (obj == null) {
            return "null";
        }
        if (obj instanceof Object[]) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("[Object[]: ");
            stringBuffer.append(((Object[]) obj).length);
            stringBuffer.append("]");
            return stringBuffer.toString();
        } else if (obj instanceof char[]) {
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2.append("[char[]: ");
            stringBuffer2.append(((char[]) obj).length);
            stringBuffer2.append("]");
            return stringBuffer2.toString();
        } else if (obj instanceof byte[]) {
            StringBuffer stringBuffer3 = new StringBuffer();
            stringBuffer3.append("[byte[]: ");
            stringBuffer3.append(((byte[]) obj).length);
            stringBuffer3.append("]");
            return stringBuffer3.toString();
        } else if (obj instanceof short[]) {
            StringBuffer stringBuffer4 = new StringBuffer();
            stringBuffer4.append("[short[]: ");
            stringBuffer4.append(((short[]) obj).length);
            stringBuffer4.append("]");
            return stringBuffer4.toString();
        } else if (obj instanceof int[]) {
            StringBuffer stringBuffer5 = new StringBuffer();
            stringBuffer5.append("[int[]: ");
            stringBuffer5.append(((int[]) obj).length);
            stringBuffer5.append("]");
            return stringBuffer5.toString();
        } else if (obj instanceof long[]) {
            StringBuffer stringBuffer6 = new StringBuffer();
            stringBuffer6.append("[long[]: ");
            stringBuffer6.append(((long[]) obj).length);
            stringBuffer6.append("]");
            return stringBuffer6.toString();
        } else if (obj instanceof float[]) {
            StringBuffer stringBuffer7 = new StringBuffer();
            stringBuffer7.append("[float[]: ");
            stringBuffer7.append(((float[]) obj).length);
            stringBuffer7.append("]");
            return stringBuffer7.toString();
        } else if (obj instanceof double[]) {
            StringBuffer stringBuffer8 = new StringBuffer();
            stringBuffer8.append("[double[]: ");
            stringBuffer8.append(((double[]) obj).length);
            stringBuffer8.append("]");
            return stringBuffer8.toString();
        } else if (!(obj instanceof boolean[])) {
            return obj.getClass().getName();
        } else {
            StringBuffer stringBuffer9 = new StringBuffer();
            stringBuffer9.append("[boolean[]: ");
            stringBuffer9.append(((boolean[]) obj).length);
            stringBuffer9.append("]");
            return stringBuffer9.toString();
        }
    }

    public static boolean isArray(Object obj) {
        if (obj == null) {
            return false;
        }
        return (obj instanceof Object[]) || (obj instanceof char[]) || (obj instanceof byte[]) || (obj instanceof short[]) || (obj instanceof int[]) || (obj instanceof long[]) || (obj instanceof float[]) || (obj instanceof double[]) || (obj instanceof boolean[]);
    }

    public static String getDebug(String str, Object[] objArr) {
        StringBuffer stringBuffer = new StringBuffer();
        if (objArr == null) {
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2.append(getDebug(str, "null"));
            stringBuffer2.append(newline);
            stringBuffer.append(stringBuffer2.toString());
        }
        stringBuffer.append(getDebug(str, objArr.length));
        int i = 0;
        while (i < objArr.length && i < 10) {
            StringBuffer stringBuffer3 = new StringBuffer();
            StringBuffer stringBuffer4 = new StringBuffer();
            stringBuffer4.append("\t");
            stringBuffer4.append(i);
            stringBuffer3.append(getDebug(stringBuffer4.toString(), objArr[i]));
            stringBuffer3.append(newline);
            stringBuffer.append(stringBuffer3.toString());
            i++;
        }
        if (objArr.length > 10) {
            StringBuffer stringBuffer5 = new StringBuffer();
            stringBuffer5.append(getDebug("\t..."));
            stringBuffer5.append(newline);
            stringBuffer.append(stringBuffer5.toString());
        }
        stringBuffer.append(newline);
        return stringBuffer.toString();
    }

    public static String getDebug(Class cls, Throwable th) {
        return getDebug(cls == null ? "[Unknown]" : cls.getName(), th);
    }

    public static void debug(Class cls, Throwable th) {
        debug(cls.getName(), th);
    }

    public static void debug(String str, boolean z) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(str);
        stringBuffer.append(" ");
        stringBuffer.append(z ? "true" : "false");
        debug(stringBuffer.toString());
    }

    public static void debug(String str, byte[] bArr) {
        debug(getDebug(str, bArr));
    }

    public static void debug(String str, char[] cArr) {
        debug(getDebug(str, cArr));
    }

    public static void debug(String str, Calendar calendar) {
        String str2;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        if (calendar == null) {
            str2 = "null";
        } else {
            str2 = simpleDateFormat.format(calendar.getTime());
        }
        debug(str, str2);
    }

    public static void debug(String str, Date date) {
        String str2;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        if (date == null) {
            str2 = "null";
        } else {
            str2 = simpleDateFormat.format(date);
        }
        debug(str, str2);
    }

    public static void debug(String str, double d) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(str);
        stringBuffer.append(": ");
        stringBuffer.append(d);
        debug(stringBuffer.toString());
    }

    public static void debug(String str, File file) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(str);
        stringBuffer.append(": ");
        stringBuffer.append(file == null ? "null" : file.getPath());
        debug(stringBuffer.toString());
    }

    public static void debug(String str, ICC_Profile iCC_Profile) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("ICC_Profile ");
        stringBuffer.append(str);
        stringBuffer.append(": ");
        stringBuffer.append(iCC_Profile == null ? "null" : iCC_Profile.toString());
        debug(stringBuffer.toString());
        if (iCC_Profile != null) {
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2.append("\t getProfileClass: ");
            stringBuffer2.append(byteQuadToString(iCC_Profile.getProfileClass()));
            debug(stringBuffer2.toString());
            StringBuffer stringBuffer3 = new StringBuffer();
            stringBuffer3.append("\t getPCSType: ");
            stringBuffer3.append(byteQuadToString(iCC_Profile.getPCSType()));
            debug(stringBuffer3.toString());
            StringBuffer stringBuffer4 = new StringBuffer();
            stringBuffer4.append("\t getColorSpaceType() : ");
            stringBuffer4.append(byteQuadToString(iCC_Profile.getColorSpaceType()));
            debug(stringBuffer4.toString());
        }
    }

    public static void debug(String str, int i) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(str);
        stringBuffer.append(": ");
        stringBuffer.append(i);
        debug(stringBuffer.toString());
    }

    public static void debug(String str, int[] iArr) {
        debug(getDebug(str, iArr));
    }

    public static void debug(String str, byte[] bArr, int i) {
        debug(getDebug(str, bArr, i));
    }

    public static void debug(String str, List list) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(" [");
        long j = counter;
        counter = 1 + j;
        stringBuffer.append(j);
        stringBuffer.append("]");
        String stringBuffer2 = stringBuffer.toString();
        StringBuffer stringBuffer3 = new StringBuffer();
        stringBuffer3.append(str);
        stringBuffer3.append(" (");
        stringBuffer3.append(list.size());
        stringBuffer3.append(")");
        stringBuffer3.append(stringBuffer2);
        debug(stringBuffer3.toString());
        for (int i = 0; i < list.size(); i++) {
            StringBuffer stringBuffer4 = new StringBuffer();
            stringBuffer4.append("\t");
            stringBuffer4.append(list.get(i).toString());
            stringBuffer4.append(stringBuffer2);
            debug(stringBuffer4.toString());
        }
        debug();
    }

    public static void debug(String str, long j) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(str);
        stringBuffer.append(" ");
        stringBuffer.append(Long.toString(j));
        debug(stringBuffer.toString());
    }

    public static void debug(String str, Point point) {
        String str2;
        PrintStream printStream = System.out;
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(str);
        stringBuffer.append(": ");
        if (point == null) {
            str2 = "null";
        } else {
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2.append(point.x);
            stringBuffer2.append(", ");
            stringBuffer2.append(point.y);
            str2 = stringBuffer2.toString();
        }
        stringBuffer.append(str2);
        printStream.println(stringBuffer.toString());
    }

    public static void debug(String str, Rectangle rectangle) {
        debug(getDebug(str, rectangle));
    }

    public static void debug(String str, String str2) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(str);
        stringBuffer.append(" ");
        stringBuffer.append(str2);
        debug(stringBuffer.toString());
    }

    public static void debug(String str, Throwable th) {
        debug(getDebug(str, th));
    }

    public static void debug(Throwable th) {
        debug(getDebug(th));
    }

    public static void debug(Throwable th, int i) {
        debug(getDebug(th, i));
    }

    public static void dumpStack() {
        debug(getStackTrace(new Exception("Stack trace"), -1, 1));
    }

    public static void dumpStack(int i) {
        debug(getStackTrace(new Exception("Stack trace"), i, 1));
    }

    public static String getDebug(String str, Throwable th) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(str);
        stringBuffer.append(newline);
        stringBuffer.append(getDebug(th));
        return stringBuffer.toString();
    }

    public static String getDebug(Throwable th) {
        return getDebug(th, -1);
    }

    public static String getDebug(Throwable th, int i) {
        String str;
        StringBuffer stringBuffer = new StringBuffer();
        String lowerCase = timestamp.format(new Date()).toLowerCase();
        stringBuffer.append(newline);
        StringBuffer stringBuffer2 = new StringBuffer();
        stringBuffer2.append("Throwable: ");
        if (th == null) {
            str = "";
        } else {
            StringBuffer stringBuffer3 = new StringBuffer();
            stringBuffer3.append("(");
            stringBuffer3.append(th.getClass().getName());
            stringBuffer3.append(")");
            str = stringBuffer3.toString();
        }
        stringBuffer2.append(str);
        stringBuffer2.append(":");
        stringBuffer2.append(lowerCase);
        stringBuffer2.append(newline);
        stringBuffer.append(stringBuffer2.toString());
        StringBuffer stringBuffer4 = new StringBuffer();
        stringBuffer4.append("Throwable: ");
        stringBuffer4.append(th == null ? "null" : th.getLocalizedMessage());
        stringBuffer4.append(newline);
        stringBuffer.append(stringBuffer4.toString());
        stringBuffer.append(newline);
        stringBuffer.append(getStackTrace(th, i));
        StringBuffer stringBuffer5 = new StringBuffer();
        stringBuffer5.append("Caught here:");
        stringBuffer5.append(newline);
        stringBuffer.append(stringBuffer5.toString());
        stringBuffer.append(getStackTrace(new Exception(), i, 1));
        stringBuffer.append(newline);
        return stringBuffer.toString();
    }

    public static String getStackTrace(Throwable th) {
        return getStackTrace(th, -1);
    }

    public static String getStackTrace(Throwable th, int i) {
        return getStackTrace(th, i, 0);
    }

    public static String getStackTrace(Throwable th, int i, int i2) {
        StringBuffer stringBuffer = new StringBuffer();
        if (th != null) {
            StackTraceElement[] stackTrace = th.getStackTrace();
            if (stackTrace != null) {
                while (i2 < stackTrace.length && (i < 0 || i2 < i)) {
                    StackTraceElement stackTraceElement = stackTrace[i2];
                    StringBuffer stringBuffer2 = new StringBuffer();
                    stringBuffer2.append("\tat ");
                    stringBuffer2.append(stackTraceElement.getClassName());
                    stringBuffer2.append(".");
                    stringBuffer2.append(stackTraceElement.getMethodName());
                    stringBuffer2.append("(");
                    stringBuffer2.append(stackTraceElement.getFileName());
                    stringBuffer2.append(":");
                    stringBuffer2.append(stackTraceElement.getLineNumber());
                    stringBuffer2.append(")");
                    stringBuffer2.append(newline);
                    stringBuffer.append(stringBuffer2.toString());
                    i2++;
                }
                if (i >= 0 && stackTrace.length > i) {
                    StringBuffer stringBuffer3 = new StringBuffer();
                    stringBuffer3.append("\t...");
                    stringBuffer3.append(newline);
                    stringBuffer.append(stringBuffer3.toString());
                }
            }
            stringBuffer.append(newline);
        }
        return stringBuffer.toString();
    }

    public static void debugByteQuad(String str, int i) {
        int i2 = (i >> 24) & 255;
        int i3 = (i >> 16) & 255;
        int i4 = (i >> 8) & 255;
        int i5 = (i >> 0) & 255;
        PrintStream printStream = System.out;
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(str);
        stringBuffer.append(": ");
        stringBuffer.append("alpha: ");
        stringBuffer.append(i2);
        stringBuffer.append(", ");
        stringBuffer.append("red: ");
        stringBuffer.append(i3);
        stringBuffer.append(", ");
        stringBuffer.append("green: ");
        stringBuffer.append(i4);
        stringBuffer.append(", ");
        stringBuffer.append("blue: ");
        stringBuffer.append(i5);
        printStream.println(stringBuffer.toString());
    }

    public static void debugIPQuad(String str, int i) {
        int i2 = (i >> 24) & 255;
        int i3 = (i >> 16) & 255;
        int i4 = (i >> 8) & 255;
        int i5 = (i >> 0) & 255;
        PrintStream printStream = System.out;
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(str);
        stringBuffer.append(": ");
        stringBuffer.append("b1: ");
        stringBuffer.append(i2);
        stringBuffer.append(", ");
        stringBuffer.append("b2: ");
        stringBuffer.append(i3);
        stringBuffer.append(", ");
        stringBuffer.append("b3: ");
        stringBuffer.append(i4);
        stringBuffer.append(", ");
        stringBuffer.append("b4: ");
        stringBuffer.append(i5);
        printStream.println(stringBuffer.toString());
    }

    public static void debugIPQuad(String str, byte[] bArr) {
        PrintStream printStream = System.out;
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(str);
        stringBuffer.append(": ");
        printStream.print(stringBuffer.toString());
        if (bArr == null) {
            System.out.print("null");
        } else {
            for (int i = 0; i < bArr.length; i++) {
                if (i > 0) {
                    System.out.print(".");
                }
                System.out.print(bArr[i] & UByte.MAX_VALUE);
            }
        }
        System.out.println();
    }

    public static String getDebug(String str, Dimension dimension) {
        String str2;
        String str3 = "null";
        String str4 = "null";
        if (dimension != null) {
            double d = (double) dimension.width;
            double d2 = (double) dimension.height;
            Double.isNaN(d);
            Double.isNaN(d2);
            double d3 = d / d2;
            double d4 = 1.0d / d3;
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("");
            stringBuffer.append(d3);
            str3 = stringBuffer.toString();
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2.append("");
            stringBuffer2.append(d4);
            str4 = stringBuffer2.toString();
            if (str3.length() > 7) {
                str3 = str3.substring(0, 7);
            }
            if (str4.length() > 7) {
                str4 = str4.substring(0, 7);
            }
        }
        StringBuffer stringBuffer3 = new StringBuffer();
        stringBuffer3.append(str);
        stringBuffer3.append(": ");
        if (dimension == null) {
            str2 = "null";
        } else {
            StringBuffer stringBuffer4 = new StringBuffer();
            stringBuffer4.append(dimension.width);
            stringBuffer4.append("x");
            stringBuffer4.append(dimension.height);
            str2 = stringBuffer4.toString();
        }
        stringBuffer3.append(str2);
        stringBuffer3.append(" aspect_ratio: ");
        stringBuffer3.append(str3);
        stringBuffer3.append(" (");
        stringBuffer3.append(str4);
        stringBuffer3.append(")");
        return stringBuffer3.toString();
    }

    public static void debug(String str, Dimension dimension) {
        debug(getDebug(str, dimension));
    }

    public static String getDebug(String str, Rectangle rectangle) {
        String str2;
        String str3 = "null";
        String str4 = "null";
        if (rectangle != null) {
            double d = (double) rectangle.width;
            double d2 = (double) rectangle.height;
            Double.isNaN(d);
            Double.isNaN(d2);
            double d3 = d / d2;
            double d4 = 1.0d / d3;
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("");
            stringBuffer.append(d3);
            str3 = stringBuffer.toString();
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2.append("");
            stringBuffer2.append(d4);
            str4 = stringBuffer2.toString();
            if (str3.length() > 7) {
                str3 = str3.substring(0, 7);
            }
            if (str4.length() > 7) {
                str4 = str4.substring(0, 7);
            }
        }
        StringBuffer stringBuffer3 = new StringBuffer();
        stringBuffer3.append(str);
        stringBuffer3.append(": ");
        if (rectangle == null) {
            str2 = "null";
        } else {
            StringBuffer stringBuffer4 = new StringBuffer();
            stringBuffer4.append(rectangle.x);
            stringBuffer4.append("x");
            stringBuffer4.append(rectangle.y);
            stringBuffer4.append(",");
            stringBuffer4.append(rectangle.width);
            stringBuffer4.append("x");
            stringBuffer4.append(rectangle.height);
            str2 = stringBuffer4.toString();
        }
        stringBuffer3.append(str2);
        stringBuffer3.append(" aspect_ratio: ");
        stringBuffer3.append(str3);
        stringBuffer3.append(" (");
        stringBuffer3.append(str4);
        stringBuffer3.append(")");
        return stringBuffer3.toString();
    }

    public static String getDebug(String str, Point point) {
        String str2;
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(str);
        stringBuffer.append(": ");
        if (point == null) {
            str2 = "null";
        } else {
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2.append(point.x);
            stringBuffer2.append(", ");
            stringBuffer2.append(point.y);
            str2 = stringBuffer2.toString();
        }
        stringBuffer.append(str2);
        return stringBuffer.toString();
    }

    public static void dump(String str, Object obj) {
        if (obj == null) {
            debug(str, "null");
            return;
        }
        int i = 0;
        if (obj instanceof Object[]) {
            Object[] objArr = (Object[]) obj;
            debug(str, objArr);
            while (i < objArr.length) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(str);
                stringBuffer.append("\t");
                stringBuffer.append(i);
                stringBuffer.append(": ");
                dump(stringBuffer.toString(), objArr[i]);
                i++;
            }
        } else if (obj instanceof int[]) {
            int[] iArr = (int[]) obj;
            debug(str, iArr);
            while (i < iArr.length) {
                StringBuffer stringBuffer2 = new StringBuffer();
                stringBuffer2.append(str);
                stringBuffer2.append("\t");
                stringBuffer2.append(i);
                stringBuffer2.append(": ");
                debug(stringBuffer2.toString(), iArr[i]);
                i++;
            }
        } else if (obj instanceof char[]) {
            char[] cArr = (char[]) obj;
            StringBuffer stringBuffer3 = new StringBuffer();
            stringBuffer3.append("[");
            stringBuffer3.append(new String(cArr));
            stringBuffer3.append("]");
            debug(str, stringBuffer3.toString());
        } else if (obj instanceof long[]) {
            long[] jArr = (long[]) obj;
            debug(str, (Object) jArr);
            while (i < jArr.length) {
                StringBuffer stringBuffer4 = new StringBuffer();
                stringBuffer4.append(str);
                stringBuffer4.append("\t");
                stringBuffer4.append(i);
                stringBuffer4.append(": ");
                debug(stringBuffer4.toString(), jArr[i]);
                i++;
            }
        } else if (obj instanceof boolean[]) {
            boolean[] zArr = (boolean[]) obj;
            debug(str, (Object) zArr);
            while (i < zArr.length) {
                StringBuffer stringBuffer5 = new StringBuffer();
                stringBuffer5.append(str);
                stringBuffer5.append("\t");
                stringBuffer5.append(i);
                stringBuffer5.append(": ");
                debug(stringBuffer5.toString(), zArr[i]);
                i++;
            }
        } else {
            boolean z = obj instanceof byte[];
            if (z) {
                byte[] bArr = (byte[]) obj;
                debug(str, bArr);
                while (i < bArr.length) {
                    StringBuffer stringBuffer6 = new StringBuffer();
                    stringBuffer6.append(str);
                    stringBuffer6.append("\t");
                    stringBuffer6.append(i);
                    stringBuffer6.append(": ");
                    debug(stringBuffer6.toString(), (int) bArr[i]);
                    i++;
                }
            } else if (obj instanceof float[]) {
                float[] fArr = (float[]) obj;
                debug(str, (Object) fArr);
                while (i < fArr.length) {
                    StringBuffer stringBuffer7 = new StringBuffer();
                    stringBuffer7.append(str);
                    stringBuffer7.append("\t");
                    stringBuffer7.append(i);
                    stringBuffer7.append(": ");
                    debug(stringBuffer7.toString(), (double) fArr[i]);
                    i++;
                }
            } else if (z) {
                double[] dArr = (double[]) obj;
                debug(str, (Object) dArr);
                while (i < dArr.length) {
                    StringBuffer stringBuffer8 = new StringBuffer();
                    stringBuffer8.append(str);
                    stringBuffer8.append("\t");
                    stringBuffer8.append(i);
                    stringBuffer8.append(": ");
                    debug(stringBuffer8.toString(), dArr[i]);
                    i++;
                }
            } else if (obj instanceof List) {
                List list = (List) obj;
                debug(str, "list");
                while (i < list.size()) {
                    StringBuffer stringBuffer9 = new StringBuffer();
                    stringBuffer9.append(str);
                    stringBuffer9.append("\t");
                    stringBuffer9.append("list: ");
                    stringBuffer9.append(i);
                    stringBuffer9.append(": ");
                    dump(stringBuffer9.toString(), list.get(i));
                    i++;
                }
            } else if (obj instanceof Map) {
                Map map = (Map) obj;
                debug(str, "map");
                ArrayList arrayList = new ArrayList(map.keySet());
                Collections.sort(arrayList);
                while (i < arrayList.size()) {
                    Object obj2 = arrayList.get(i);
                    StringBuffer stringBuffer10 = new StringBuffer();
                    stringBuffer10.append(str);
                    stringBuffer10.append("\t");
                    stringBuffer10.append("map: ");
                    stringBuffer10.append(obj2);
                    stringBuffer10.append(" -> ");
                    dump(stringBuffer10.toString(), map.get(obj2));
                    i++;
                }
            } else {
                debug(str, obj.toString());
                StringBuffer stringBuffer11 = new StringBuffer();
                stringBuffer11.append(str);
                stringBuffer11.append("\t");
                debug(stringBuffer11.toString(), obj.getClass().getName());
            }
        }
    }

    public static final void purgeMemory() {
        try {
            System.runFinalization();
            Thread.sleep(50);
            System.gc();
            Thread.sleep(50);
        } catch (Throwable th) {
            debug(th);
        }
    }
}

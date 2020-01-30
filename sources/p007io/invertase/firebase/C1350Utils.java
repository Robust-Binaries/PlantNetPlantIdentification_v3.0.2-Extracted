package p007io.invertase.firebase;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.util.Log;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.LifecycleState;
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;
import javax.annotation.Nullable;

/* renamed from: io.invertase.firebase.Utils */
public class C1350Utils {
    private static final String TAG = "Utils";

    public static String timestampToUTC(long j) {
        Date date = new Date((j + ((long) Calendar.getInstance().getTimeZone().getOffset(j))) * 1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat.format(date);
    }

    public static void sendEvent(ReactContext reactContext, String str, Object obj) {
        if (reactContext != null) {
            ((RCTDeviceEventEmitter) reactContext.getJSModule(RCTDeviceEventEmitter.class)).emit(str, obj);
        } else {
            Log.d(TAG, "Missing context - cannot send event!");
        }
    }

    public static void mapPutValue(String str, @Nullable Object obj, WritableMap writableMap) {
        if (obj == null) {
            writableMap.putNull(str);
            return;
        }
        String name = obj.getClass().getName();
        char c = 65535;
        switch (name.hashCode()) {
            case -2056817302:
                if (name.equals("java.lang.Integer")) {
                    c = 4;
                    break;
                }
                break;
            case -527879800:
                if (name.equals("java.lang.Float")) {
                    c = 2;
                    break;
                }
                break;
            case 344809556:
                if (name.equals("java.lang.Boolean")) {
                    c = 0;
                    break;
                }
                break;
            case 398795216:
                if (name.equals("java.lang.Long")) {
                    c = 1;
                    break;
                }
                break;
            case 761287205:
                if (name.equals("java.lang.Double")) {
                    c = 3;
                    break;
                }
                break;
            case 1195259493:
                if (name.equals("java.lang.String")) {
                    c = 5;
                    break;
                }
                break;
            case 1614941136:
                if (name.equals("org.json.JSONObject$1")) {
                    c = 6;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                writableMap.putBoolean(str, ((Boolean) obj).booleanValue());
                return;
            case 1:
                writableMap.putDouble(str, (double) ((Long) obj).longValue());
                return;
            case 2:
                writableMap.putDouble(str, (double) ((Float) obj).floatValue());
                return;
            case 3:
                writableMap.putDouble(str, ((Double) obj).doubleValue());
                return;
            case 4:
                writableMap.putInt(str, ((Integer) obj).intValue());
                return;
            case 5:
                writableMap.putString(str, (String) obj);
                return;
            case 6:
                writableMap.putString(str, obj.toString());
                return;
            default:
                if (List.class.isAssignableFrom(obj.getClass())) {
                    writableMap.putArray(str, Arguments.makeNativeArray((List) obj));
                    return;
                } else if (Map.class.isAssignableFrom(obj.getClass())) {
                    WritableMap createMap = Arguments.createMap();
                    for (Entry entry : ((Map) obj).entrySet()) {
                        mapPutValue((String) entry.getKey(), entry.getValue(), createMap);
                    }
                    writableMap.putMap(str, createMap);
                    return;
                } else {
                    String str2 = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("utils:mapPutValue:unknownType:");
                    sb.append(name);
                    Log.d(str2, sb.toString());
                    writableMap.putNull(str);
                    return;
                }
        }
    }

    public static WritableMap readableMapToWritableMap(ReadableMap readableMap) {
        WritableMap createMap = Arguments.createMap();
        createMap.merge(readableMap);
        return createMap;
    }

    public static Map<String, Object> recursivelyDeconstructReadableMap(ReadableMap readableMap) {
        return readableMap.toHashMap();
    }

    public static List<Object> recursivelyDeconstructReadableArray(ReadableArray readableArray) {
        return readableArray.toArrayList();
    }

    public static boolean isAppInForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        if (activityManager == null) {
            return false;
        }
        List<RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        if (runningAppProcesses == null) {
            return false;
        }
        String packageName = context.getPackageName();
        for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
            if (runningAppProcessInfo.importance == 100 && runningAppProcessInfo.processName.equals(packageName)) {
                boolean z = true;
                try {
                    if (((ReactContext) context).getLifecycleState() != LifecycleState.RESUMED) {
                        z = false;
                    }
                    return z;
                } catch (ClassCastException unused) {
                    return true;
                }
            }
        }
        return false;
    }

    public static int getResId(Context context, String str) {
        int identifier = context.getResources().getIdentifier(str, "string", context.getPackageName());
        if (identifier == 0) {
            String str2 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("resource ");
            sb.append(str);
            sb.append(" could not be found");
            Log.e(str2, sb.toString());
        }
        return identifier;
    }
}

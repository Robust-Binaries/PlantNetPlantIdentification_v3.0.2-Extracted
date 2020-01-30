package p007io.invertase.firebase.perf;

import android.util.Log;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.HttpMetric;
import com.google.firebase.perf.metrics.Trace;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/* renamed from: io.invertase.firebase.perf.RNFirebasePerformance */
public class RNFirebasePerformance extends ReactContextBaseJavaModule {
    private static final String TAG = "RNFirebasePerformance";
    private HashMap<String, HttpMetric> httpMetrics = new HashMap<>();
    private HashMap<String, Trace> traces = new HashMap<>();

    public String getName() {
        return TAG;
    }

    public RNFirebasePerformance(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
        Log.d(TAG, "New instance");
    }

    @ReactMethod
    public void setPerformanceCollectionEnabled(Boolean bool, Promise promise) {
        FirebasePerformance.getInstance().setPerformanceCollectionEnabled(bool.booleanValue());
        promise.resolve(null);
    }

    @ReactMethod
    public void getTraceAttribute(String str, String str2, Promise promise) {
        promise.resolve(getOrCreateTrace(str).getAttribute(str2));
    }

    @ReactMethod
    public void getTraceAttributes(String str, Promise promise) {
        Map attributes = getOrCreateTrace(str).getAttributes();
        WritableMap createMap = Arguments.createMap();
        for (Entry entry : attributes.entrySet()) {
            createMap.putString((String) entry.getKey(), (String) entry.getValue());
        }
        promise.resolve(createMap);
    }

    @ReactMethod
    public void getTraceLongMetric(String str, String str2, Promise promise) {
        promise.resolve(Integer.valueOf(Long.valueOf(getOrCreateTrace(str).getLongMetric(str2)).intValue()));
    }

    @ReactMethod
    public void incrementTraceMetric(String str, String str2, Integer num, Promise promise) {
        getOrCreateTrace(str).incrementMetric(str2, num.longValue());
        promise.resolve(null);
    }

    @ReactMethod
    public void putTraceAttribute(String str, String str2, String str3, Promise promise) {
        getOrCreateTrace(str).putAttribute(str2, str3);
        if (getOrCreateTrace(str).getAttributes().containsKey(str2)) {
            promise.resolve(Boolean.valueOf(true));
        } else {
            promise.resolve(Boolean.valueOf(false));
        }
    }

    @ReactMethod
    public void putTraceMetric(String str, String str2, Integer num, Promise promise) {
        getOrCreateTrace(str).putMetric(str2, num.longValue());
        promise.resolve(null);
    }

    @ReactMethod
    public void removeTraceAttribute(String str, String str2, Promise promise) {
        getOrCreateTrace(str).removeAttribute(str2);
        promise.resolve(null);
    }

    @ReactMethod
    public void startTrace(String str, Promise promise) {
        getOrCreateTrace(str).start();
        promise.resolve(null);
    }

    @ReactMethod
    public void stopTrace(String str, Promise promise) {
        getOrCreateTrace(str).stop();
        this.traces.remove(str);
        promise.resolve(null);
    }

    @ReactMethod
    public void incrementCounter(String str, String str2) {
        getOrCreateTrace(str).incrementCounter(str2);
    }

    @ReactMethod
    public void getHttpMetricAttribute(String str, String str2, String str3, Promise promise) {
        promise.resolve(getOrCreateHttpMetric(str, str2).getAttribute(str3));
    }

    @ReactMethod
    public void getHttpMetricAttributes(String str, String str2, Promise promise) {
        Map attributes = getOrCreateHttpMetric(str, str2).getAttributes();
        WritableMap createMap = Arguments.createMap();
        for (Entry entry : attributes.entrySet()) {
            createMap.putString((String) entry.getKey(), (String) entry.getValue());
        }
        promise.resolve(createMap);
    }

    @ReactMethod
    public void putHttpMetricAttribute(String str, String str2, String str3, String str4, Promise promise) {
        getOrCreateHttpMetric(str, str2).putAttribute(str3, str4);
        if (getOrCreateHttpMetric(str, str2).getAttributes().containsKey(str3)) {
            promise.resolve(Boolean.valueOf(true));
        } else {
            promise.resolve(Boolean.valueOf(false));
        }
    }

    @ReactMethod
    public void removeHttpMetricAttribute(String str, String str2, String str3, Promise promise) {
        getOrCreateHttpMetric(str, str2).removeAttribute(str3);
        promise.resolve(null);
    }

    @ReactMethod
    public void setHttpMetricResponseCode(String str, String str2, Integer num, Promise promise) {
        getOrCreateHttpMetric(str, str2).setHttpResponseCode(num.intValue());
        promise.resolve(null);
    }

    @ReactMethod
    public void setHttpMetricRequestPayloadSize(String str, String str2, Integer num, Promise promise) {
        getOrCreateHttpMetric(str, str2).setRequestPayloadSize(num.longValue());
        promise.resolve(null);
    }

    @ReactMethod
    public void setHttpMetricResponseContentType(String str, String str2, String str3, Promise promise) {
        getOrCreateHttpMetric(str, str2).setResponseContentType(str3);
        promise.resolve(null);
    }

    @ReactMethod
    public void setHttpMetricResponsePayloadSize(String str, String str2, Integer num, Promise promise) {
        getOrCreateHttpMetric(str, str2).setResponsePayloadSize(num.longValue());
        promise.resolve(null);
    }

    @ReactMethod
    public void startHttpMetric(String str, String str2, Promise promise) {
        getOrCreateHttpMetric(str, str2).start();
        promise.resolve(null);
    }

    @ReactMethod
    public void stopHttpMetric(String str, String str2, Promise promise) {
        getOrCreateHttpMetric(str, str2).stop();
        HashMap<String, HttpMetric> hashMap = this.httpMetrics;
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(str2);
        hashMap.remove(sb.toString());
        promise.resolve(null);
    }

    private Trace getOrCreateTrace(String str) {
        if (this.traces.containsKey(str)) {
            return (Trace) this.traces.get(str);
        }
        Trace newTrace = FirebasePerformance.getInstance().newTrace(str);
        this.traces.put(str, newTrace);
        return newTrace;
    }

    private HttpMetric getOrCreateHttpMetric(String str, String str2) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(str2);
        String sb2 = sb.toString();
        if (this.httpMetrics.containsKey(sb2)) {
            return (HttpMetric) this.httpMetrics.get(sb2);
        }
        HttpMetric newHttpMetric = FirebasePerformance.getInstance().newHttpMetric(str, mapStringToMethod(str2));
        this.httpMetrics.put(sb2, newHttpMetric);
        return newHttpMetric;
    }

    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String mapStringToMethod(java.lang.String r2) {
        /*
            r1 = this;
            int r0 = r2.hashCode()
            switch(r0) {
                case -531492226: goto L_0x0059;
                case 70454: goto L_0x004f;
                case 79599: goto L_0x0045;
                case 2213344: goto L_0x003b;
                case 2461856: goto L_0x0031;
                case 75900968: goto L_0x0027;
                case 80083237: goto L_0x001c;
                case 1669334218: goto L_0x0012;
                case 2012838315: goto L_0x0008;
                default: goto L_0x0007;
            }
        L_0x0007:
            goto L_0x0063
        L_0x0008:
            java.lang.String r0 = "DELETE"
            boolean r2 = r2.equals(r0)
            if (r2 == 0) goto L_0x0063
            r2 = 1
            goto L_0x0064
        L_0x0012:
            java.lang.String r0 = "CONNECT"
            boolean r2 = r2.equals(r0)
            if (r2 == 0) goto L_0x0063
            r2 = 0
            goto L_0x0064
        L_0x001c:
            java.lang.String r0 = "TRACE"
            boolean r2 = r2.equals(r0)
            if (r2 == 0) goto L_0x0063
            r2 = 8
            goto L_0x0064
        L_0x0027:
            java.lang.String r0 = "PATCH"
            boolean r2 = r2.equals(r0)
            if (r2 == 0) goto L_0x0063
            r2 = 5
            goto L_0x0064
        L_0x0031:
            java.lang.String r0 = "POST"
            boolean r2 = r2.equals(r0)
            if (r2 == 0) goto L_0x0063
            r2 = 6
            goto L_0x0064
        L_0x003b:
            java.lang.String r0 = "HEAD"
            boolean r2 = r2.equals(r0)
            if (r2 == 0) goto L_0x0063
            r2 = 3
            goto L_0x0064
        L_0x0045:
            java.lang.String r0 = "PUT"
            boolean r2 = r2.equals(r0)
            if (r2 == 0) goto L_0x0063
            r2 = 7
            goto L_0x0064
        L_0x004f:
            java.lang.String r0 = "GET"
            boolean r2 = r2.equals(r0)
            if (r2 == 0) goto L_0x0063
            r2 = 2
            goto L_0x0064
        L_0x0059:
            java.lang.String r0 = "OPTIONS"
            boolean r2 = r2.equals(r0)
            if (r2 == 0) goto L_0x0063
            r2 = 4
            goto L_0x0064
        L_0x0063:
            r2 = -1
        L_0x0064:
            switch(r2) {
                case 0: goto L_0x0082;
                case 1: goto L_0x007f;
                case 2: goto L_0x007c;
                case 3: goto L_0x0079;
                case 4: goto L_0x0076;
                case 5: goto L_0x0073;
                case 6: goto L_0x0070;
                case 7: goto L_0x006d;
                case 8: goto L_0x006a;
                default: goto L_0x0067;
            }
        L_0x0067:
            java.lang.String r2 = ""
            return r2
        L_0x006a:
            java.lang.String r2 = "TRACE"
            return r2
        L_0x006d:
            java.lang.String r2 = "PUT"
            return r2
        L_0x0070:
            java.lang.String r2 = "POST"
            return r2
        L_0x0073:
            java.lang.String r2 = "PATCH"
            return r2
        L_0x0076:
            java.lang.String r2 = "OPTIONS"
            return r2
        L_0x0079:
            java.lang.String r2 = "HEAD"
            return r2
        L_0x007c:
            java.lang.String r2 = "GET"
            return r2
        L_0x007f:
            java.lang.String r2 = "DELETE"
            return r2
        L_0x0082:
            java.lang.String r2 = "CONNECT"
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: p007io.invertase.firebase.perf.RNFirebasePerformance.mapStringToMethod(java.lang.String):java.lang.String");
    }
}

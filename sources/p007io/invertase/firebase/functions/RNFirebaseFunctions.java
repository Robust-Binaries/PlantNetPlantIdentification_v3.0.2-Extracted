package p007io.invertase.firebase.functions;

import android.util.Log;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.functions.HttpsCallableResult;
import javax.annotation.Nonnull;
import p007io.invertase.firebase.C1350Utils;

/* renamed from: io.invertase.firebase.functions.RNFirebaseFunctions */
public class RNFirebaseFunctions extends ReactContextBaseJavaModule {
    private static final String CODE_KEY = "code";
    private static final String DATA_KEY = "data";
    private static final String DETAILS_KEY = "details";
    private static final String ERROR_KEY = "__error";
    private static final String MSG_KEY = "message";
    private static final String TAG = "RNFirebaseFunctions";

    public String getName() {
        return TAG;
    }

    RNFirebaseFunctions(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
        Log.d(TAG, "New instance");
    }

    @ReactMethod
    public void useFunctionsEmulator(String str, String str2, String str3, Promise promise) {
        FirebaseFunctions.getInstance(FirebaseApp.getInstance(str), str2).useFunctionsEmulator(str3);
        promise.resolve(null);
    }

    @ReactMethod
    public void httpsCallable(String str, String str2, final String str3, ReadableMap readableMap, final Promise promise) {
        Object obj = readableMap.toHashMap().get("data");
        String str4 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("function:call:input:");
        sb.append(str3);
        sb.append(":");
        sb.append(obj != null ? obj.toString() : "null");
        Log.d(str4, sb.toString());
        FirebaseFunctions.getInstance(FirebaseApp.getInstance(str), str2).getHttpsCallable(str3).call(obj).addOnSuccessListener(new OnSuccessListener<HttpsCallableResult>() {
            public void onSuccess(HttpsCallableResult httpsCallableResult) {
                WritableMap createMap = Arguments.createMap();
                Object data = httpsCallableResult.getData();
                String str = RNFirebaseFunctions.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("function:call:onSuccess:");
                sb.append(str3);
                Log.d(str, sb.toString());
                String str2 = RNFirebaseFunctions.TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("function:call:onSuccess:result:type:");
                sb2.append(str3);
                sb2.append(":");
                sb2.append(data != null ? data.getClass().getName() : "null");
                Log.d(str2, sb2.toString());
                String str3 = RNFirebaseFunctions.TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("function:call:onSuccess:result:data:");
                sb3.append(str3);
                sb3.append(":");
                sb3.append(data != null ? data.toString() : "null");
                Log.d(str3, sb3.toString());
                C1350Utils.mapPutValue("data", data, createMap);
                promise.resolve(createMap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            public void onFailure(@Nonnull Exception exc) {
                String str;
                Object obj;
                String str2 = RNFirebaseFunctions.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("function:call:onFailure:");
                sb.append(str3);
                Log.d(str2, sb.toString(), exc);
                String str3 = "UNKNOWN";
                WritableMap createMap = Arguments.createMap();
                if (exc instanceof FirebaseFunctionsException) {
                    FirebaseFunctionsException firebaseFunctionsException = (FirebaseFunctionsException) exc;
                    Object details = firebaseFunctionsException.getDetails();
                    String name = firebaseFunctionsException.getCode().name();
                    str = firebaseFunctionsException.getMessage();
                    String str4 = name;
                    obj = details;
                    str3 = str4;
                } else {
                    str = exc.getMessage();
                    obj = null;
                }
                C1350Utils.mapPutValue(RNFirebaseFunctions.CODE_KEY, str3, createMap);
                C1350Utils.mapPutValue(RNFirebaseFunctions.MSG_KEY, str, createMap);
                C1350Utils.mapPutValue(RNFirebaseFunctions.ERROR_KEY, Boolean.valueOf(true), createMap);
                C1350Utils.mapPutValue(RNFirebaseFunctions.DETAILS_KEY, obj, createMap);
                promise.resolve(createMap);
            }
        });
    }
}

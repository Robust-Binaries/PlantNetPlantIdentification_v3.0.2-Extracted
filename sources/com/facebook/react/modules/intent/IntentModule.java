package com.facebook.react.modules.intent;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import com.facebook.react.bridge.JSApplicationIllegalArgumentException;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.module.annotations.ReactModule;
import javax.annotation.Nullable;

@ReactModule(name = "IntentAndroid")
public class IntentModule extends ReactContextBaseJavaModule {
    public static final String NAME = "IntentAndroid";

    public String getName() {
        return NAME;
    }

    public IntentModule(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
    }

    @ReactMethod
    public void getInitialURL(Promise promise) {
        try {
            Activity currentActivity = getCurrentActivity();
            String str = null;
            if (currentActivity != null) {
                Intent intent = currentActivity.getIntent();
                String action = intent.getAction();
                Uri data = intent.getData();
                if ("android.intent.action.VIEW".equals(action) && data != null) {
                    str = data.toString();
                }
            }
            promise.resolve(str);
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Could not get the initial URL : ");
            sb.append(e.getMessage());
            promise.reject((Throwable) new JSApplicationIllegalArgumentException(sb.toString()));
        }
    }

    @ReactMethod
    public void openURL(String str, Promise promise) {
        if (str == null || str.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Invalid URL: ");
            sb.append(str);
            promise.reject((Throwable) new JSApplicationIllegalArgumentException(sb.toString()));
            return;
        }
        try {
            Activity currentActivity = getCurrentActivity();
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(str).normalizeScheme());
            String packageName = getReactApplicationContext().getPackageName();
            ComponentName resolveActivity = intent.resolveActivity(getReactApplicationContext().getPackageManager());
            String packageName2 = resolveActivity != null ? resolveActivity.getPackageName() : "";
            if (currentActivity == null || !packageName.equals(packageName2)) {
                intent.addFlags(268435456);
            }
            if (currentActivity != null) {
                currentActivity.startActivity(intent);
            } else {
                getReactApplicationContext().startActivity(intent);
            }
            promise.resolve(Boolean.valueOf(true));
        } catch (Exception e) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Could not open URL '");
            sb2.append(str);
            sb2.append("': ");
            sb2.append(e.getMessage());
            promise.reject((Throwable) new JSApplicationIllegalArgumentException(sb2.toString()));
        }
    }

    @ReactMethod
    public void canOpenURL(String str, Promise promise) {
        if (str == null || str.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Invalid URL: ");
            sb.append(str);
            promise.reject((Throwable) new JSApplicationIllegalArgumentException(sb.toString()));
            return;
        }
        try {
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(str));
            intent.addFlags(268435456);
            promise.resolve(Boolean.valueOf(intent.resolveActivity(getReactApplicationContext().getPackageManager()) != null));
        } catch (Exception e) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Could not check if URL '");
            sb2.append(str);
            sb2.append("' can be opened: ");
            sb2.append(e.getMessage());
            promise.reject((Throwable) new JSApplicationIllegalArgumentException(sb2.toString()));
        }
    }

    @ReactMethod
    public void sendIntent(String str, @Nullable ReadableArray readableArray, Promise promise) {
        if (str == null || str.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Invalid Action: ");
            sb.append(str);
            sb.append(".");
            promise.reject((Throwable) new JSApplicationIllegalArgumentException(sb.toString()));
            return;
        }
        Intent intent = new Intent(str);
        if (intent.resolveActivity(getReactApplicationContext().getPackageManager()) == null) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Could not launch Intent with action ");
            sb2.append(str);
            sb2.append(".");
            promise.reject((Throwable) new JSApplicationIllegalArgumentException(sb2.toString()));
            return;
        }
        if (readableArray != null) {
            for (int i = 0; i < readableArray.size(); i++) {
                ReadableMap map = readableArray.getMap(i);
                String nextKey = map.keySetIterator().nextKey();
                switch (map.getType(nextKey)) {
                    case String:
                        intent.putExtra(nextKey, map.getString(nextKey));
                        break;
                    case Number:
                        intent.putExtra(nextKey, Double.valueOf(map.getDouble(nextKey)));
                        break;
                    case Boolean:
                        intent.putExtra(nextKey, map.getBoolean(nextKey));
                        break;
                    default:
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("Extra type for ");
                        sb3.append(nextKey);
                        sb3.append(" not supported.");
                        promise.reject((Throwable) new JSApplicationIllegalArgumentException(sb3.toString()));
                        return;
                }
            }
        }
        getReactApplicationContext().startActivity(intent);
    }
}

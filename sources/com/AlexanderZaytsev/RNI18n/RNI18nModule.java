package com.AlexanderZaytsev.RNI18n;

import android.os.Build.VERSION;
import android.os.LocaleList;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableArray;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RNI18nModule extends ReactContextBaseJavaModule {
    public String getName() {
        return "RNI18n";
    }

    public RNI18nModule(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
    }

    private String toLanguageTag(Locale locale) {
        String str;
        if (VERSION.SDK_INT >= 21) {
            str = locale.toLanguageTag();
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(locale.getLanguage());
            if (locale.getCountry() != null) {
                sb.append("-");
                sb.append(locale.getCountry());
            }
            str = sb.toString();
        }
        return str.matches("^(iw|in|ji).*") ? str.replace("iw", "he").replace("in", "id").replace("ji", "yi") : str;
    }

    private WritableArray getLocaleList() {
        WritableArray createArray = Arguments.createArray();
        if (VERSION.SDK_INT >= 24) {
            LocaleList locales = getReactApplicationContext().getResources().getConfiguration().getLocales();
            for (int i = 0; i < locales.size(); i++) {
                createArray.pushString(toLanguageTag(locales.get(i)));
            }
        } else {
            createArray.pushString(toLanguageTag(getReactApplicationContext().getResources().getConfiguration().locale));
        }
        return createArray;
    }

    public Map<String, Object> getConstants() {
        HashMap hashMap = new HashMap();
        hashMap.put("languages", getLocaleList());
        return hashMap;
    }

    @ReactMethod
    public void getLanguages(Promise promise) {
        try {
            promise.resolve(getLocaleList());
        } catch (Exception e) {
            promise.reject((Throwable) e);
        }
    }
}

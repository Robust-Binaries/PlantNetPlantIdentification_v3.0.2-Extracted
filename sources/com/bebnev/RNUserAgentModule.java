package com.bebnev;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build.VERSION;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import java.util.HashMap;
import java.util.Map;

public class RNUserAgentModule extends ReactContextBaseJavaModule {
    private final ReactApplicationContext reactContext;

    public String getName() {
        return "RNUserAgent";
    }

    public RNUserAgentModule(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
        this.reactContext = reactApplicationContext;
    }

    /* access modifiers changed from: protected */
    public String getUserAgent() {
        return VERSION.SDK_INT >= 17 ? System.getProperty("http.agent") : "";
    }

    /* access modifiers changed from: protected */
    public String getWebViewUserAgent() {
        if (VERSION.SDK_INT >= 17) {
            return WebSettings.getDefaultUserAgent(this.reactContext);
        }
        return new WebView(this.reactContext).getSettings().getUserAgentString();
    }

    public Map<String, Object> getConstants() {
        HashMap hashMap = new HashMap();
        PackageManager packageManager = this.reactContext.getPackageManager();
        String packageName = this.reactContext.getPackageName();
        String substring = packageName.substring(packageName.lastIndexOf(".") + 1);
        String str = "";
        String str2 = "";
        Integer valueOf = Integer.valueOf(0);
        String userAgent = getUserAgent();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            str = this.reactContext.getApplicationInfo().loadLabel(this.reactContext.getPackageManager()).toString();
            str2 = packageInfo.versionName;
            valueOf = Integer.valueOf(packageInfo.versionCode);
            StringBuilder sb = new StringBuilder();
            sb.append(substring);
            sb.append('/');
            sb.append(str2);
            sb.append('.');
            sb.append(valueOf.toString());
            sb.append(' ');
            sb.append(userAgent);
            userAgent = sb.toString();
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        hashMap.put("systemName", "Android");
        hashMap.put("systemVersion", VERSION.RELEASE);
        hashMap.put("packageName", packageName);
        hashMap.put("shortPackageName", substring);
        hashMap.put("applicationName", str);
        hashMap.put("applicationVersion", str2);
        hashMap.put("applicationBuildNumber", valueOf);
        hashMap.put("userAgent", userAgent);
        hashMap.put("webViewUserAgent", getWebViewUserAgent());
        return hashMap;
    }
}

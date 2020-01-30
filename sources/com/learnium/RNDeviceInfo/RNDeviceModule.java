package com.learnium.RNDeviceInfo;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.KeyguardManager;
import android.app.UiModeManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings.Global;
import android.provider.Settings.Secure;
import android.provider.Settings.System;
import android.support.p000v4.app.NotificationCompat;
import android.support.p000v4.p002os.EnvironmentCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.webkit.WebSettings;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.ViewProps;
import com.google.android.gms.iid.InstanceID;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import java.math.BigInteger;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;
import javax.annotation.Nullable;
import org.apache.sanselan.ImageInfo;

public class RNDeviceModule extends ReactContextBaseJavaModule {
    Map<String, Object> constants;
    DeviceType deviceType;
    AsyncTask<Void, Void, Map<String, Object>> futureConstants;
    ReactApplicationContext reactContext;
    WifiInfo wifiInfo;

    public String getName() {
        return "RNDeviceInfo";
    }

    public RNDeviceModule(ReactApplicationContext reactApplicationContext, boolean z) {
        super(reactApplicationContext);
        this.reactContext = reactApplicationContext;
        this.deviceType = getDeviceType(reactApplicationContext);
        if (z) {
            this.futureConstants = new AsyncTask<Void, Void, Map<String, Object>>() {
                /* access modifiers changed from: protected */
                public Map<String, Object> doInBackground(Void... voidArr) {
                    return RNDeviceModule.this.generateConstants();
                }
            }.execute(new Void[0]);
        } else {
            this.constants = generateConstants();
        }
    }

    private WifiInfo getWifiInfo() {
        if (this.wifiInfo == null) {
            this.wifiInfo = ((WifiManager) this.reactContext.getApplicationContext().getSystemService("wifi")).getConnectionInfo();
        }
        return this.wifiInfo;
    }

    private String getCurrentLanguage() {
        Locale locale;
        if (VERSION.SDK_INT >= 24) {
            locale = getReactApplicationContext().getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = getReactApplicationContext().getResources().getConfiguration().locale;
        }
        if (VERSION.SDK_INT >= 21) {
            return locale.toLanguageTag();
        }
        StringBuilder sb = new StringBuilder();
        sb.append(locale.getLanguage());
        if (locale.getCountry() != null) {
            sb.append("-");
            sb.append(locale.getCountry());
        }
        return sb.toString();
    }

    private ArrayList<String> getPreferredLocales() {
        Configuration configuration = getReactApplicationContext().getResources().getConfiguration();
        ArrayList<String> arrayList = new ArrayList<>();
        if (VERSION.SDK_INT >= 24) {
            for (int i = 0; i < configuration.getLocales().size(); i++) {
                arrayList.add(configuration.getLocales().get(i).getLanguage());
            }
        } else {
            arrayList.add(configuration.locale.getLanguage());
        }
        return arrayList;
    }

    private String getCurrentCountry() {
        Locale locale;
        if (VERSION.SDK_INT >= 24) {
            locale = getReactApplicationContext().getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = getReactApplicationContext().getResources().getConfiguration().locale;
        }
        return locale.getCountry();
    }

    private Boolean isEmulator() {
        return Boolean.valueOf(Build.FINGERPRINT.startsWith("generic") || Build.FINGERPRINT.startsWith(EnvironmentCompat.MEDIA_UNKNOWN) || Build.MODEL.contains("google_sdk") || Build.MODEL.toLowerCase().contains("droid4x") || Build.MODEL.contains("Emulator") || Build.MODEL.contains("Android SDK built for x86") || Build.MANUFACTURER.contains("Genymotion") || Build.HARDWARE.contains("goldfish") || Build.HARDWARE.contains("ranchu") || Build.HARDWARE.contains("vbox86") || Build.PRODUCT.contains("sdk") || Build.PRODUCT.contains("google_sdk") || Build.PRODUCT.contains("sdk_google") || Build.PRODUCT.contains("sdk_x86") || Build.PRODUCT.contains("vbox86p") || Build.PRODUCT.contains("emulator") || Build.PRODUCT.contains("simulator") || Build.BOARD.toLowerCase().contains("nox") || Build.BOOTLOADER.toLowerCase().contains("nox") || Build.HARDWARE.toLowerCase().contains("nox") || Build.PRODUCT.toLowerCase().contains("nox") || Build.SERIAL.toLowerCase().contains("nox") || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")));
    }

    private Boolean isTablet() {
        return Boolean.valueOf(this.deviceType == DeviceType.TABLET);
    }

    private static DeviceType getDeviceType(ReactApplicationContext reactApplicationContext) {
        if (reactApplicationContext.getApplicationContext().getPackageManager().hasSystemFeature("amazon.hardware.fire_tv")) {
            return DeviceType.TV;
        }
        UiModeManager uiModeManager = (UiModeManager) reactApplicationContext.getSystemService("uimode");
        if (uiModeManager != null && uiModeManager.getCurrentModeType() == 4) {
            return DeviceType.TV;
        }
        WindowManager windowManager = (WindowManager) reactApplicationContext.getSystemService("window");
        if (windowManager == null) {
            return DeviceType.UNKNOWN;
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        if (VERSION.SDK_INT >= 17) {
            windowManager.getDefaultDisplay().getRealMetrics(displayMetrics);
        } else {
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        }
        double d = (double) displayMetrics.widthPixels;
        double d2 = (double) displayMetrics.xdpi;
        Double.isNaN(d);
        Double.isNaN(d2);
        double d3 = d / d2;
        double d4 = (double) displayMetrics.heightPixels;
        double d5 = (double) displayMetrics.ydpi;
        Double.isNaN(d4);
        Double.isNaN(d5);
        double sqrt = Math.sqrt(Math.pow(d3, 2.0d) + Math.pow(d4 / d5, 2.0d));
        if (sqrt >= 3.0d && sqrt <= 6.9d) {
            return DeviceType.HANDSET;
        }
        if (sqrt <= 6.9d || sqrt > 18.0d) {
            return DeviceType.UNKNOWN;
        }
        return DeviceType.TABLET;
    }

    private float fontScale() {
        return getReactApplicationContext().getResources().getConfiguration().fontScale;
    }

    private Boolean is24Hour() {
        return Boolean.valueOf(DateFormat.is24HourFormat(this.reactContext.getApplicationContext()));
    }

    @ReactMethod
    public void isPinOrFingerprintSet(Callback callback) {
        callback.invoke(Boolean.valueOf(((KeyguardManager) this.reactContext.getApplicationContext().getSystemService("keyguard")).isKeyguardSecure()));
    }

    @ReactMethod
    public void getIpAddress(Promise promise) {
        promise.resolve(Formatter.formatIpAddress(getWifiInfo().getIpAddress()));
    }

    @ReactMethod
    public void getCameraPresence(Promise promise) {
        boolean z = true;
        if (VERSION.SDK_INT >= 21) {
            try {
                if (((CameraManager) getReactApplicationContext().getSystemService("camera")).getCameraIdList().length <= 0) {
                    z = false;
                }
                promise.resolve(Boolean.valueOf(z));
            } catch (CameraAccessException e) {
                promise.reject((Throwable) e);
            }
        } else {
            if (Camera.getNumberOfCameras() <= 0) {
                z = false;
            }
            promise.resolve(Boolean.valueOf(z));
        }
    }

    @ReactMethod
    public void getMacAddress(Promise promise) {
        String macAddress = getWifiInfo().getMacAddress();
        if (this.reactContext.checkCallingOrSelfPermission("android.permission.INTERNET") == 0) {
            try {
                for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                    if (networkInterface.getName().equalsIgnoreCase("wlan0")) {
                        byte[] hardwareAddress = networkInterface.getHardwareAddress();
                        if (hardwareAddress == null) {
                            macAddress = "";
                        } else {
                            StringBuilder sb = new StringBuilder();
                            for (byte valueOf : hardwareAddress) {
                                sb.append(String.format("%02X:", new Object[]{Byte.valueOf(valueOf)}));
                            }
                            if (sb.length() > 0) {
                                sb.deleteCharAt(sb.length() - 1);
                            }
                            macAddress = sb.toString();
                        }
                    }
                }
            } catch (Exception unused) {
            }
        }
        promise.resolve(macAddress);
    }

    @ReactMethod
    public String getCarrier() {
        return ((TelephonyManager) this.reactContext.getSystemService("phone")).getNetworkOperatorName();
    }

    @ReactMethod
    public BigInteger getTotalDiskCapacity() {
        try {
            StatFs statFs = new StatFs(Environment.getRootDirectory().getAbsolutePath());
            return BigInteger.valueOf((long) statFs.getBlockCount()).multiply(BigInteger.valueOf((long) statFs.getBlockSize()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ReactMethod
    public BigInteger getFreeDiskStorage() {
        long j;
        long j2;
        try {
            StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
            if (VERSION.SDK_INT < 18) {
                j2 = (long) statFs.getAvailableBlocks();
                j = (long) statFs.getBlockSize();
            } else {
                j2 = statFs.getAvailableBlocksLong();
                j = statFs.getBlockSizeLong();
            }
            return BigInteger.valueOf(j2).multiply(BigInteger.valueOf(j));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ReactMethod
    public void isBatteryCharging(Promise promise) {
        promise.resolve(Boolean.valueOf(this.reactContext.getApplicationContext().registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED")).getIntExtra(NotificationCompat.CATEGORY_STATUS, -1) == 2));
    }

    @ReactMethod
    public void getBatteryLevel(Promise promise) {
        Intent registerReceiver = this.reactContext.getApplicationContext().registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        promise.resolve(Float.valueOf(((float) registerReceiver.getIntExtra(Param.LEVEL, -1)) / ((float) registerReceiver.getIntExtra("scale", -1))));
    }

    @ReactMethod
    public void isAirPlaneMode(Promise promise) {
        boolean z = true;
        if (VERSION.SDK_INT < 17) {
            if (System.getInt(this.reactContext.getContentResolver(), "airplane_mode_on", 0) == 0) {
                z = false;
            }
        } else if (Global.getInt(this.reactContext.getContentResolver(), "airplane_mode_on", 0) == 0) {
            z = false;
        }
        promise.resolve(Boolean.valueOf(z));
    }

    @ReactMethod
    public void isAutoDateAndTime(Promise promise) {
        boolean z = true;
        if (VERSION.SDK_INT < 17) {
            if (System.getInt(this.reactContext.getContentResolver(), "auto_time", 0) == 0) {
                z = false;
            }
        } else if (Global.getInt(this.reactContext.getContentResolver(), "auto_time", 0) == 0) {
            z = false;
        }
        promise.resolve(Boolean.valueOf(z));
    }

    @ReactMethod
    public void isAutoTimeZone(Promise promise) {
        boolean z = true;
        if (VERSION.SDK_INT < 17) {
            if (System.getInt(this.reactContext.getContentResolver(), "auto_time_zone", 0) == 0) {
                z = false;
            }
        } else if (Global.getInt(this.reactContext.getContentResolver(), "auto_time_zone", 0) == 0) {
            z = false;
        }
        promise.resolve(Boolean.valueOf(z));
    }

    @ReactMethod
    public void hasSystemFeature(String str, Promise promise) {
        if (str == null || str == "") {
            promise.resolve(Boolean.valueOf(false));
        } else {
            promise.resolve(Boolean.valueOf(this.reactContext.getApplicationContext().getPackageManager().hasSystemFeature(str)));
        }
    }

    @ReactMethod
    public void getSystemAvailableFeatures(Promise promise) {
        FeatureInfo[] systemAvailableFeatures = this.reactContext.getApplicationContext().getPackageManager().getSystemAvailableFeatures();
        WritableArray createArray = Arguments.createArray();
        for (FeatureInfo featureInfo : systemAvailableFeatures) {
            if (featureInfo.name != null) {
                createArray.pushString(featureInfo.name);
            }
        }
        promise.resolve(createArray);
    }

    @ReactMethod
    public void isLocationEnabled(Promise promise) {
        boolean z = VERSION.SDK_INT >= 28 ? ((LocationManager) this.reactContext.getApplicationContext().getSystemService(Param.LOCATION)).isLocationEnabled() : VERSION.SDK_INT >= 19 ? Secure.getInt(this.reactContext.getContentResolver(), "location_mode", 0) != 0 : !TextUtils.isEmpty(Secure.getString(this.reactContext.getContentResolver(), "location_providers_allowed"));
        promise.resolve(Boolean.valueOf(z));
    }

    @ReactMethod
    public void getAvailableLocationProviders(Promise promise) {
        LocationManager locationManager = (LocationManager) this.reactContext.getApplicationContext().getSystemService(Param.LOCATION);
        List<String> providers = locationManager.getProviders(false);
        WritableMap createMap = Arguments.createMap();
        for (String str : providers) {
            createMap.putBoolean(str, locationManager.isProviderEnabled(str));
        }
        promise.resolve(createMap);
    }

    public String getInstallReferrer() {
        return getReactApplicationContext().getSharedPreferences("react-native-device-info", 0).getString("installReferrer", null);
    }

    /* access modifiers changed from: private */
    @SuppressLint({"MissingPermission", "HardwareIds"})
    public Map<String, Object> generateConstants() {
        HashMap hashMap = new HashMap();
        PackageManager packageManager = this.reactContext.getPackageManager();
        String packageName = this.reactContext.getPackageName();
        hashMap.put("appVersion", "not available");
        hashMap.put("appName", "not available");
        hashMap.put("buildVersion", "not available");
        hashMap.put("buildNumber", Integer.valueOf(0));
        try {
            packageManager.getPackageInfo(packageName, 0);
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            String charSequence = this.reactContext.getApplicationInfo().loadLabel(this.reactContext.getPackageManager()).toString();
            hashMap.put("appVersion", packageInfo.versionName);
            hashMap.put("buildNumber", Integer.valueOf(packageInfo.versionCode));
            hashMap.put("firstInstallTime", Long.valueOf(packageInfo.firstInstallTime));
            hashMap.put("lastUpdateTime", Long.valueOf(packageInfo.lastUpdateTime));
            hashMap.put("appName", charSequence);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        String str = ImageInfo.COMPRESSION_ALGORITHM_UNKNOWN;
        if (this.reactContext.checkCallingOrSelfPermission("android.permission.BLUETOOTH") == 0) {
            try {
                BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
                if (defaultAdapter != null) {
                    str = defaultAdapter.getName();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        try {
            if (Class.forName("com.google.android.gms.iid.InstanceID") != null) {
                hashMap.put("instanceId", InstanceID.getInstance(this.reactContext).getId());
            }
        } catch (ClassNotFoundException unused) {
            hashMap.put("instanceId", "N/A: Add com.google.android.gms:play-services-gcm to your project.");
        }
        hashMap.put("serialNumber", Build.SERIAL);
        hashMap.put("deviceName", str);
        hashMap.put("systemName", "Android");
        hashMap.put("systemVersion", VERSION.RELEASE);
        hashMap.put("model", Build.MODEL);
        hashMap.put("brand", Build.BRAND);
        hashMap.put("buildId", Build.ID);
        hashMap.put("deviceId", Build.BOARD);
        hashMap.put("apiLevel", Integer.valueOf(VERSION.SDK_INT));
        hashMap.put("bootloader", Build.BOOTLOADER);
        hashMap.put("device", Build.DEVICE);
        hashMap.put(ViewProps.DISPLAY, Build.DISPLAY);
        hashMap.put("fingerprint", Build.FINGERPRINT);
        hashMap.put("hardware", Build.HARDWARE);
        hashMap.put("host", Build.HOST);
        hashMap.put("product", Build.PRODUCT);
        hashMap.put("tags", Build.TAGS);
        hashMap.put("type", Build.TYPE);
        if (VERSION.SDK_INT >= 23) {
            hashMap.put("baseOS", VERSION.BASE_OS);
            hashMap.put("previewSdkInt", Integer.valueOf(VERSION.PREVIEW_SDK_INT));
            hashMap.put("securityPatch", VERSION.SECURITY_PATCH);
        }
        hashMap.put("codename", VERSION.CODENAME);
        hashMap.put("incremental", VERSION.INCREMENTAL);
        hashMap.put("deviceLocale", getCurrentLanguage());
        hashMap.put("preferredLocales", getPreferredLocales());
        hashMap.put("deviceCountry", getCurrentCountry());
        hashMap.put("uniqueId", Secure.getString(this.reactContext.getContentResolver(), "android_id"));
        hashMap.put("systemManufacturer", Build.MANUFACTURER);
        hashMap.put("bundleId", packageName);
        try {
            if (VERSION.SDK_INT >= 17) {
                hashMap.put("userAgent", WebSettings.getDefaultUserAgent(this.reactContext));
            } else {
                hashMap.put("userAgent", System.getProperty("http.agent"));
            }
        } catch (RuntimeException unused2) {
            hashMap.put("userAgent", System.getProperty("http.agent"));
        }
        hashMap.put("timezone", TimeZone.getDefault().getID());
        hashMap.put("isEmulator", isEmulator());
        hashMap.put("isTablet", isTablet());
        hashMap.put("fontScale", Float.valueOf(fontScale()));
        hashMap.put("is24Hour", is24Hour());
        hashMap.put("carrier", getCarrier());
        hashMap.put("totalDiskCapacity", getTotalDiskCapacity());
        hashMap.put("freeDiskStorage", getFreeDiskStorage());
        hashMap.put("installReferrer", getInstallReferrer());
        ReactApplicationContext reactApplicationContext = this.reactContext;
        if (reactApplicationContext == null || (reactApplicationContext.checkCallingOrSelfPermission("android.permission.READ_PHONE_STATE") != 0 && ((VERSION.SDK_INT < 23 || this.reactContext.checkCallingOrSelfPermission("android.permission.READ_SMS") != 0) && (VERSION.SDK_INT < 26 || this.reactContext.checkCallingOrSelfPermission("android.permission.READ_PHONE_NUMBERS") != 0)))) {
            hashMap.put("phoneNumber", null);
        } else {
            hashMap.put("phoneNumber", ((TelephonyManager) this.reactContext.getApplicationContext().getSystemService("phone")).getLine1Number());
        }
        hashMap.put("maxMemory", Long.valueOf(Runtime.getRuntime().maxMemory()));
        ActivityManager activityManager = (ActivityManager) this.reactContext.getSystemService("activity");
        MemoryInfo memoryInfo = new MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        hashMap.put("totalMemory", Long.valueOf(memoryInfo.totalMem));
        hashMap.put("deviceType", this.deviceType.getValue());
        if (VERSION.SDK_INT >= 21) {
            hashMap.put("supportedABIs", Build.SUPPORTED_ABIS);
            hashMap.put("supported32BitAbis", Arrays.asList(Build.SUPPORTED_32_BIT_ABIS));
            hashMap.put("supported64BitAbis", Arrays.asList(Build.SUPPORTED_64_BIT_ABIS));
        } else {
            hashMap.put("supportedABIs", new String[]{Build.CPU_ABI});
            hashMap.put("supported32BitAbis", Arrays.asList(new String[0]));
            hashMap.put("supported64BitAbis", Arrays.asList(new String[0]));
        }
        return hashMap;
    }

    @Nullable
    public Map<String, Object> getConstants() {
        if (this.constants == null) {
            AsyncTask<Void, Void, Map<String, Object>> asyncTask = this.futureConstants;
            if (asyncTask != null) {
                try {
                    this.constants = (Map) asyncTask.get();
                } catch (InterruptedException unused) {
                    return null;
                } catch (ExecutionException e) {
                    throw new RuntimeException(e.getCause());
                }
            }
        }
        return this.constants;
    }
}

package p004br.com.classapp.RNSensitiveInfo;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.hardware.fingerprint.FingerprintManager;
import android.hardware.fingerprint.FingerprintManager.AuthenticationCallback;
import android.hardware.fingerprint.FingerprintManager.AuthenticationResult;
import android.hardware.fingerprint.FingerprintManager.CryptoObject;
import android.os.Build.VERSION;
import android.os.CancellationSignal;
import android.security.keystore.KeyGenParameterSpec.Builder;
import android.security.keystore.KeyInfo;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import p004br.com.classapp.RNSensitiveInfo.utils.AppConstants;
import p004br.com.classapp.RNSensitiveInfo.view.Fragments.FingerprintAuthenticationDialogFragment;
import p004br.com.classapp.RNSensitiveInfo.view.Fragments.FingerprintUiHelper.Callback;

/* renamed from: br.com.classapp.RNSensitiveInfo.RNSensitiveInfoModule */
public class RNSensitiveInfoModule extends ReactContextBaseJavaModule {
    private static final String AES_DEFAULT_TRANSFORMATION = "AES/CBC/PKCS7Padding";
    private static final String ANDROID_KEYSTORE_PROVIDER = "AndroidKeyStore";
    private static final String DELIMITER = "]";
    private static final String KEY_ALIAS_AES = "MyAesKeyAlias";
    private boolean invalidateEnrollment = true;
    private CancellationSignal mCancellationSignal;
    private FingerprintManager mFingerprintManager;
    private KeyStore mKeyStore;

    public String getName() {
        return "RNSensitiveInfo";
    }

    public RNSensitiveInfoModule(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
        if (VERSION.SDK_INT >= 23) {
            try {
                this.mFingerprintManager = (FingerprintManager) reactApplicationContext.getSystemService("fingerprint");
            } catch (Exception unused) {
                Log.d("RNSensitiveInfo", "Fingerprint not supported");
            }
            initKeyStore();
        }
    }

    private boolean hasSetupFingerprint() {
        try {
            if (VERSION.SDK_INT < 23 || this.mFingerprintManager == null || !this.mFingerprintManager.isHardwareDetected() || !this.mFingerprintManager.hasEnrolledFingerprints()) {
                return false;
            }
            return true;
        } catch (SecurityException unused) {
            return false;
        }
    }

    @ReactMethod
    public void setInvalidatedByBiometricEnrollment(boolean z, Promise promise) {
        this.invalidateEnrollment = z;
        try {
            prepareKey();
        } catch (Exception e) {
            promise.reject((Throwable) e);
        }
    }

    @ReactMethod
    public void isHardwareDetected(Promise promise) {
        if (VERSION.SDK_INT >= 23) {
            promise.resolve(Boolean.valueOf(this.mFingerprintManager.isHardwareDetected()));
        } else {
            promise.resolve(Boolean.valueOf(false));
        }
    }

    @ReactMethod
    public void hasEnrolledFingerprints(Promise promise) {
        if (VERSION.SDK_INT >= 23) {
            promise.resolve(Boolean.valueOf(this.mFingerprintManager.hasEnrolledFingerprints()));
        } else {
            promise.resolve(Boolean.valueOf(false));
        }
    }

    @ReactMethod
    public void isSensorAvailable(Promise promise) {
        promise.resolve(Boolean.valueOf(hasSetupFingerprint()));
    }

    @ReactMethod
    public void getItem(String str, ReadableMap readableMap, Promise promise) {
        String string = prefs(sharedPreferences(readableMap)).getString(str, null);
        if (string == null || !readableMap.hasKey("touchID") || !readableMap.getBoolean("touchID")) {
            promise.resolve(string);
        } else {
            decryptWithAes(string, readableMap.hasKey("showModal") && readableMap.getBoolean("showModal"), readableMap.hasKey("strings") ? readableMap.getMap("strings").toHashMap() : new HashMap(), promise, null);
        }
    }

    @ReactMethod
    public void setItem(String str, String str2, ReadableMap readableMap, Promise promise) {
        String sharedPreferences = sharedPreferences(readableMap);
        if (!readableMap.hasKey("touchID") || !readableMap.getBoolean("touchID")) {
            try {
                putExtra(str, str2, prefs(sharedPreferences));
                promise.resolve(str2);
            } catch (Exception e) {
                Log.d("RNSensitiveInfo", e.getCause().getMessage());
                promise.reject((Throwable) e);
            }
        } else {
            putExtraWithAES(str, str2, prefs(sharedPreferences), readableMap.hasKey("showModal") && readableMap.getBoolean("showModal"), readableMap.hasKey("strings") ? readableMap.getMap("strings").toHashMap() : new HashMap(), promise, null);
        }
    }

    @ReactMethod
    public void deleteItem(String str, ReadableMap readableMap, Promise promise) {
        prefs(sharedPreferences(readableMap)).edit().remove(str).apply();
        promise.resolve(null);
    }

    @ReactMethod
    public void getAllItems(ReadableMap readableMap, Promise promise) {
        Map all = prefs(sharedPreferences(readableMap)).getAll();
        WritableNativeMap writableNativeMap = new WritableNativeMap();
        for (Entry entry : all.entrySet()) {
            writableNativeMap.putString((String) entry.getKey(), entry.getValue().toString());
        }
        promise.resolve(writableNativeMap);
    }

    @ReactMethod
    public void cancelFingerprintAuth() {
        CancellationSignal cancellationSignal = this.mCancellationSignal;
        if (cancellationSignal != null && !cancellationSignal.isCanceled()) {
            this.mCancellationSignal.cancel();
        }
    }

    private SharedPreferences prefs(String str) {
        return getReactApplicationContext().getSharedPreferences(str, 0);
    }

    @NonNull
    private String sharedPreferences(ReadableMap readableMap) {
        String string = readableMap.hasKey("sharedPreferencesName") ? readableMap.getString("sharedPreferencesName") : "shared_preferences";
        return string == null ? "shared_preferences" : string;
    }

    private void putExtra(String str, Object obj, SharedPreferences sharedPreferences) {
        Editor edit = sharedPreferences.edit();
        if (obj instanceof String) {
            edit.putString(str, (String) obj).apply();
        } else if (obj instanceof Boolean) {
            edit.putBoolean(str, ((Boolean) obj).booleanValue()).apply();
        } else if (obj instanceof Integer) {
            edit.putInt(str, ((Integer) obj).intValue()).apply();
        } else if (obj instanceof Long) {
            edit.putLong(str, ((Long) obj).longValue()).apply();
        } else if (obj instanceof Float) {
            edit.putFloat(str, ((Float) obj).floatValue()).apply();
        }
    }

    private void showDialog(HashMap hashMap, Object obj, Callback callback) {
        if (VERSION.SDK_INT >= 23) {
            Activity currentActivity = getCurrentActivity();
            if (currentActivity == null) {
                callback.onError(AppConstants.E_INIT_FAILURE, hashMap.containsKey("cancelled") ? hashMap.get("cancelled").toString() : "Authentication was cancelled");
                return;
            }
            FragmentTransaction beginTransaction = currentActivity.getFragmentManager().beginTransaction();
            Fragment findFragmentByTag = getCurrentActivity().getFragmentManager().findFragmentByTag(AppConstants.DIALOG_FRAGMENT_TAG);
            if (findFragmentByTag != null) {
                beginTransaction.remove(findFragmentByTag);
            }
            beginTransaction.addToBackStack(null);
            FingerprintAuthenticationDialogFragment newInstance = FingerprintAuthenticationDialogFragment.newInstance(hashMap);
            newInstance.setCryptoObject((CryptoObject) obj);
            newInstance.setCallback(callback);
            newInstance.show(beginTransaction, AppConstants.DIALOG_FRAGMENT_TAG);
        }
    }

    private void initKeyStore() {
        try {
            this.mKeyStore = KeyStore.getInstance(ANDROID_KEYSTORE_PROVIDER);
            this.mKeyStore.load(null);
            if (!this.mKeyStore.containsAlias(KEY_ALIAS_AES)) {
                prepareKey();
            }
        } catch (Exception unused) {
        }
    }

    private void prepareKey() throws Exception {
        if (VERSION.SDK_INT >= 23) {
            KeyGenerator instance = KeyGenerator.getInstance("AES", ANDROID_KEYSTORE_PROVIDER);
            Builder builder = new Builder(KEY_ALIAS_AES, 3);
            builder.setBlockModes(new String[]{"CBC"}).setKeySize(256).setEncryptionPaddings(new String[]{"PKCS7Padding"}).setUserAuthenticationRequired(true);
            if (VERSION.SDK_INT >= 24) {
                try {
                    builder.setInvalidatedByBiometricEnrollment(this.invalidateEnrollment);
                } catch (Exception e) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Error setting setInvalidatedByBiometricEnrollment: ");
                    sb.append(e.getMessage());
                    Log.d("RNSensitiveInfo", sb.toString());
                }
            }
            instance.init(builder.build());
            instance.generateKey();
        }
    }

    /* access modifiers changed from: private */
    public void putExtraWithAES(String str, String str2, SharedPreferences sharedPreferences, boolean z, HashMap hashMap, Promise promise, Cipher cipher) {
        Promise promise2 = promise;
        Cipher cipher2 = cipher;
        if (VERSION.SDK_INT < 23 || !hasSetupFingerprint()) {
            promise2.reject("Fingerprint not supported", "Fingerprint not supported");
        } else if (cipher2 == null) {
            try {
                SecretKey secretKey = (SecretKey) this.mKeyStore.getKey(KEY_ALIAS_AES, null);
                Cipher instance = Cipher.getInstance(AES_DEFAULT_TRANSFORMATION);
                instance.init(1, secretKey);
                KeyInfo keyInfo = (KeyInfo) SecretKeyFactory.getInstance(secretKey.getAlgorithm(), ANDROID_KEYSTORE_PROVIDER).getKeySpec(secretKey, KeyInfo.class);
                if (keyInfo.isUserAuthenticationRequired() && keyInfo.getUserAuthenticationValidityDurationSeconds() == -1) {
                    if (z) {
                        CryptoObject cryptoObject = new CryptoObject(instance);
                        final String str3 = str;
                        final String str4 = str2;
                        final SharedPreferences sharedPreferences2 = sharedPreferences;
                        final boolean z2 = z;
                        final HashMap hashMap2 = hashMap;
                        final Promise promise3 = promise;
                        AnonymousClass1PutExtraWithAESCallback r1 = new Callback() {
                            public void onAuthenticated(AuthenticationResult authenticationResult) {
                                if (VERSION.SDK_INT >= 23) {
                                    RNSensitiveInfoModule.this.putExtraWithAES(str3, str4, sharedPreferences2, z2, hashMap2, promise3, authenticationResult.getCryptoObject().getCipher());
                                }
                            }

                            public void onError(String str, CharSequence charSequence) {
                                promise3.reject(String.valueOf(str), charSequence.toString());
                            }
                        };
                        showDialog(hashMap, cryptoObject, r1);
                    } else {
                        HashMap hashMap3 = hashMap;
                        this.mCancellationSignal = new CancellationSignal();
                        FingerprintManager fingerprintManager = this.mFingerprintManager;
                        CryptoObject cryptoObject2 = new CryptoObject(instance);
                        CancellationSignal cancellationSignal = this.mCancellationSignal;
                        final Promise promise4 = promise;
                        final String str5 = str;
                        final String str6 = str2;
                        final SharedPreferences sharedPreferences3 = sharedPreferences;
                        final boolean z3 = z;
                        final HashMap hashMap4 = hashMap;
                        C05541 r12 = new AuthenticationCallback() {
                            public void onAuthenticationFailed() {
                                super.onAuthenticationFailed();
                                ((RCTDeviceEventEmitter) RNSensitiveInfoModule.this.getReactApplicationContext().getJSModule(RCTDeviceEventEmitter.class)).emit("FINGERPRINT_AUTHENTICATION_HELP", "Fingerprint not recognized.");
                            }

                            public void onAuthenticationError(int i, CharSequence charSequence) {
                                super.onAuthenticationError(i, charSequence);
                                promise4.reject(String.valueOf(i), charSequence.toString());
                            }

                            public void onAuthenticationHelp(int i, CharSequence charSequence) {
                                super.onAuthenticationHelp(i, charSequence);
                                ((RCTDeviceEventEmitter) RNSensitiveInfoModule.this.getReactApplicationContext().getJSModule(RCTDeviceEventEmitter.class)).emit("FINGERPRINT_AUTHENTICATION_HELP", charSequence.toString());
                            }

                            public void onAuthenticationSucceeded(AuthenticationResult authenticationResult) {
                                super.onAuthenticationSucceeded(authenticationResult);
                                if (VERSION.SDK_INT >= 23) {
                                    RNSensitiveInfoModule.this.putExtraWithAES(str5, str6, sharedPreferences3, z3, hashMap4, promise4, authenticationResult.getCryptoObject().getCipher());
                                }
                            }
                        };
                        fingerprintManager.authenticate(cryptoObject2, cancellationSignal, 0, r12, null);
                    }
                }
            } catch (InvalidKeyException e) {
                InvalidKeyException invalidKeyException = e;
                try {
                    this.mKeyStore.deleteEntry(KEY_ALIAS_AES);
                    prepareKey();
                } catch (Exception e2) {
                    promise2.reject((Throwable) e2);
                }
                promise2.reject((Throwable) invalidKeyException);
            } catch (SecurityException e3) {
                promise2.reject((Throwable) e3);
            } catch (Exception e4) {
                promise2.reject((Throwable) e4);
            }
        } else {
            byte[] doFinal = cipher2.doFinal(str2.getBytes());
            String encodeToString = Base64.encodeToString(cipher.getIV(), 0);
            String encodeToString2 = Base64.encodeToString(doFinal, 0);
            StringBuilder sb = new StringBuilder();
            sb.append(encodeToString);
            sb.append(DELIMITER);
            sb.append(encodeToString2);
            putExtra(str, sb.toString(), sharedPreferences);
            promise2.resolve(str2);
        }
    }

    /* access modifiers changed from: private */
    public void decryptWithAes(String str, boolean z, HashMap hashMap, Promise promise, Cipher cipher) {
        Promise promise2 = promise;
        Cipher cipher2 = cipher;
        if (VERSION.SDK_INT < 23 || !hasSetupFingerprint()) {
            promise2.reject("Fingerprint not supported", "Fingerprint not supported");
        } else {
            String[] split = str.split(DELIMITER);
            if (split.length < 2) {
                promise2.reject("DecryptionFailed", "DecryptionFailed");
            }
            try {
                byte[] decode = Base64.decode(split[0], 0);
                byte[] decode2 = Base64.decode(split[1], 0);
                if (cipher2 == null) {
                    SecretKey secretKey = (SecretKey) this.mKeyStore.getKey(KEY_ALIAS_AES, null);
                    Cipher instance = Cipher.getInstance(AES_DEFAULT_TRANSFORMATION);
                    instance.init(2, secretKey, new IvParameterSpec(decode));
                    KeyInfo keyInfo = (KeyInfo) SecretKeyFactory.getInstance(secretKey.getAlgorithm(), ANDROID_KEYSTORE_PROVIDER).getKeySpec(secretKey, KeyInfo.class);
                    if (keyInfo.isUserAuthenticationRequired() && keyInfo.getUserAuthenticationValidityDurationSeconds() == -1) {
                        if (z) {
                            CryptoObject cryptoObject = new CryptoObject(instance);
                            final String str2 = str;
                            final boolean z2 = z;
                            final HashMap hashMap2 = hashMap;
                            final Promise promise3 = promise;
                            AnonymousClass1DecryptWithAesCallback r1 = new Callback() {
                                public void onAuthenticated(AuthenticationResult authenticationResult) {
                                    if (VERSION.SDK_INT >= 23) {
                                        RNSensitiveInfoModule.this.decryptWithAes(str2, z2, hashMap2, promise3, authenticationResult.getCryptoObject().getCipher());
                                    }
                                }

                                public void onError(String str, CharSequence charSequence) {
                                    promise3.reject(String.valueOf(str), charSequence.toString());
                                }
                            };
                            showDialog(hashMap, cryptoObject, r1);
                        } else {
                            HashMap hashMap3 = hashMap;
                            this.mCancellationSignal = new CancellationSignal();
                            FingerprintManager fingerprintManager = this.mFingerprintManager;
                            CryptoObject cryptoObject2 = new CryptoObject(instance);
                            CancellationSignal cancellationSignal = this.mCancellationSignal;
                            final Promise promise4 = promise;
                            final String str3 = str;
                            final boolean z3 = z;
                            final HashMap hashMap4 = hashMap;
                            C05552 r12 = new AuthenticationCallback() {
                                public void onAuthenticationFailed() {
                                    super.onAuthenticationFailed();
                                    ((RCTDeviceEventEmitter) RNSensitiveInfoModule.this.getReactApplicationContext().getJSModule(RCTDeviceEventEmitter.class)).emit("FINGERPRINT_AUTHENTICATION_HELP", "Fingerprint not recognized.");
                                }

                                public void onAuthenticationError(int i, CharSequence charSequence) {
                                    super.onAuthenticationError(i, charSequence);
                                    promise4.reject(String.valueOf(i), charSequence.toString());
                                }

                                public void onAuthenticationHelp(int i, CharSequence charSequence) {
                                    super.onAuthenticationHelp(i, charSequence);
                                    ((RCTDeviceEventEmitter) RNSensitiveInfoModule.this.getReactApplicationContext().getJSModule(RCTDeviceEventEmitter.class)).emit("FINGERPRINT_AUTHENTICATION_HELP", charSequence.toString());
                                }

                                public void onAuthenticationSucceeded(AuthenticationResult authenticationResult) {
                                    super.onAuthenticationSucceeded(authenticationResult);
                                    if (VERSION.SDK_INT >= 23) {
                                        RNSensitiveInfoModule.this.decryptWithAes(str3, z3, hashMap4, promise4, authenticationResult.getCryptoObject().getCipher());
                                    }
                                }
                            };
                            fingerprintManager.authenticate(cryptoObject2, cancellationSignal, 0, r12, null);
                        }
                    }
                    return;
                }
                promise2.resolve(new String(cipher2.doFinal(decode2)));
            } catch (InvalidKeyException e) {
                InvalidKeyException invalidKeyException = e;
                try {
                    this.mKeyStore.deleteEntry(KEY_ALIAS_AES);
                    prepareKey();
                } catch (Exception e2) {
                    promise2.reject((Throwable) e2);
                }
                promise2.reject((Throwable) invalidKeyException);
            } catch (SecurityException e3) {
                promise2.reject((Throwable) e3);
            } catch (Exception e4) {
                promise2.reject((Throwable) e4);
            }
        }
    }
}

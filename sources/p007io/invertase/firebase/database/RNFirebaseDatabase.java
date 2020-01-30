package p007io.invertase.firebase.database;

import android.os.AsyncTask;
import android.support.p000v4.p002os.EnvironmentCompat;
import android.util.Log;
import android.util.SparseArray;
import com.RNFetchBlob.RNFetchBlobConst;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseReference.CompletionListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger.Level;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.Transaction.Handler;
import com.google.firebase.database.Transaction.Result;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nonnull;
import p007io.invertase.firebase.C1350Utils;
import p007io.invertase.firebase.ErrorUtils;

/* renamed from: io.invertase.firebase.database.RNFirebaseDatabase */
public class RNFirebaseDatabase extends ReactContextBaseJavaModule {
    private static final String TAG = "RNFirebaseDatabase";
    private static boolean enableLogging = false;
    private static HashMap<String, Boolean> loggingLevelSet = new HashMap<>();
    private static ReactApplicationContext reactApplicationContext;
    private static HashMap<String, RNFirebaseDatabaseReference> references = new HashMap<>();
    /* access modifiers changed from: private */
    public static SparseArray<RNFirebaseTransactionHandler> transactionHandlers = new SparseArray<>();

    public String getName() {
        return TAG;
    }

    RNFirebaseDatabase(ReactApplicationContext reactApplicationContext2) {
        super(reactApplicationContext2);
    }

    static ReactApplicationContext getReactApplicationContextInstance() {
        return reactApplicationContext;
    }

    static void handlePromise(Promise promise, DatabaseError databaseError) {
        if (databaseError != null) {
            WritableMap jSError = getJSError(databaseError);
            promise.reject(jSError.getString("code"), jSError.getString("message"), (Throwable) databaseError.toException());
            return;
        }
        promise.resolve(null);
    }

    static FirebaseDatabase getDatabaseForApp(String str, String str2) {
        FirebaseDatabase firebaseDatabase;
        if (str2 == null || str2.length() <= 0) {
            firebaseDatabase = FirebaseDatabase.getInstance(FirebaseApp.getInstance(str));
        } else if (str == null || str.length() <= 0) {
            firebaseDatabase = FirebaseDatabase.getInstance(str2);
        } else {
            firebaseDatabase = FirebaseDatabase.getInstance(FirebaseApp.getInstance(str), str2);
        }
        Boolean bool = (Boolean) loggingLevelSet.get(firebaseDatabase.getApp().getName());
        if (enableLogging && (bool == null || !bool.booleanValue())) {
            try {
                loggingLevelSet.put(firebaseDatabase.getApp().getName(), Boolean.valueOf(enableLogging));
                firebaseDatabase.setLogLevel(Level.DEBUG);
            } catch (DatabaseException unused) {
                String str3 = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("WARNING: enableLogging(bool) must be called before any other use of database(). \nIf you are sure you've done this then this message can be ignored during development as \nRN reloads can cause false positives. APP: ");
                sb.append(firebaseDatabase.getApp().getName());
                Log.w(str3, sb.toString());
            }
        } else if (!enableLogging && bool != null && bool.booleanValue()) {
            try {
                loggingLevelSet.put(firebaseDatabase.getApp().getName(), Boolean.valueOf(enableLogging));
                firebaseDatabase.setLogLevel(Level.WARN);
            } catch (DatabaseException unused2) {
                String str4 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("WARNING: enableLogging(bool) must be called before any other use of database(). \nIf you are sure you've done this then this message can be ignored during development as \nRN reloads can cause false positives. APP: ");
                sb2.append(firebaseDatabase.getApp().getName());
                Log.w(str4, sb2.toString());
            }
        }
        return firebaseDatabase;
    }

    static WritableMap getJSError(DatabaseError databaseError) {
        String str;
        String str2;
        WritableMap createMap = Arguments.createMap();
        createMap.putInt("nativeErrorCode", databaseError.getCode());
        createMap.putString("nativeErrorMessage", databaseError.getMessage());
        String str3 = "Database";
        int code = databaseError.getCode();
        switch (code) {
            case -25:
                str = ErrorUtils.getCodeWithService(str3, "write-cancelled");
                str2 = ErrorUtils.getMessageWithService("The write was canceled by the user.", str3, str);
                break;
            case -24:
                str = ErrorUtils.getCodeWithService(str3, "network-error");
                str2 = ErrorUtils.getMessageWithService("The operation could not be performed due to a network error.", str3, str);
                break;
            default:
                switch (code) {
                    case -11:
                        str = ErrorUtils.getCodeWithService(str3, "user-code-exception");
                        str2 = ErrorUtils.getMessageWithService("User code called from the Firebase Database runloop threw an exception.", str3, str);
                        break;
                    case -10:
                        str = ErrorUtils.getCodeWithService(str3, "unavailable");
                        str2 = ErrorUtils.getMessageWithService("The service is unavailable.", str3, str);
                        break;
                    case -9:
                        str = ErrorUtils.getCodeWithService(str3, "overridden-by-set");
                        str2 = ErrorUtils.getMessageWithService("The transaction was overridden by a subsequent set.", str3, str);
                        break;
                    case -8:
                        str = ErrorUtils.getCodeWithService(str3, "max-retries");
                        str2 = ErrorUtils.getMessageWithService("The transaction had too many retries.", str3, str);
                        break;
                    case -7:
                        str = ErrorUtils.getCodeWithService(str3, "invalid-token");
                        str2 = ErrorUtils.getMessageWithService("The supplied auth token was invalid.", str3, str);
                        break;
                    case -6:
                        str = ErrorUtils.getCodeWithService(str3, "expired-token");
                        str2 = ErrorUtils.getMessageWithService("The supplied auth token has expired.", str3, str);
                        break;
                    default:
                        switch (code) {
                            case -4:
                                str = ErrorUtils.getCodeWithService(str3, "disconnected");
                                str2 = ErrorUtils.getMessageWithService("The operation had to be aborted due to a network disconnect.", str3, str);
                                break;
                            case -3:
                                str = ErrorUtils.getCodeWithService(str3, "permission-denied");
                                str2 = ErrorUtils.getMessageWithService("Client doesn't have permission to access the desired data.", str3, str);
                                break;
                            case -2:
                                str = ErrorUtils.getCodeWithService(str3, "failure");
                                str2 = ErrorUtils.getMessageWithService("The server indicated that this operation failed.", str3, str);
                                break;
                            case -1:
                                str = ErrorUtils.getCodeWithService(str3, "data-stale");
                                str2 = ErrorUtils.getMessageWithService("The transaction needs to be run again with current data.", str3, str);
                                break;
                            default:
                                str = ErrorUtils.getCodeWithService(str3, EnvironmentCompat.MEDIA_UNKNOWN);
                                str2 = ErrorUtils.getMessageWithService("An unknown error occurred.", str3, str);
                                break;
                        }
                }
        }
        createMap.putString("code", str);
        createMap.putString("message", str2);
        return createMap;
    }

    public void initialize() {
        super.initialize();
        Log.d(TAG, "RNFirebaseDatabase:initialized");
        reactApplicationContext = getReactApplicationContext();
    }

    public void onCatalystInstanceDestroy() {
        super.onCatalystInstanceDestroy();
        Iterator it = references.entrySet().iterator();
        while (it.hasNext()) {
            ((RNFirebaseDatabaseReference) ((Entry) it.next()).getValue()).removeAllEventListeners();
            it.remove();
        }
    }

    @ReactMethod
    public void goOnline(String str, String str2) {
        getDatabaseForApp(str, str2).goOnline();
    }

    @ReactMethod
    public void goOffline(String str, String str2) {
        getDatabaseForApp(str, str2).goOffline();
    }

    @ReactMethod
    public void setPersistence(String str, String str2, Boolean bool) {
        getDatabaseForApp(str, str2).setPersistenceEnabled(bool.booleanValue());
    }

    @ReactMethod
    public void setPersistenceCacheSizeBytes(String str, String str2, int i) {
        getDatabaseForApp(str, str2).setPersistenceCacheSizeBytes((long) i);
    }

    @ReactMethod
    public void enableLogging(Boolean bool) {
        enableLogging = bool.booleanValue();
        for (FirebaseApp firebaseApp : FirebaseApp.getApps(getReactApplicationContext())) {
            loggingLevelSet.put(firebaseApp.getName(), bool);
            try {
                if (enableLogging) {
                    FirebaseDatabase.getInstance(firebaseApp).setLogLevel(Level.DEBUG);
                } else {
                    FirebaseDatabase.getInstance(firebaseApp).setLogLevel(Level.WARN);
                }
            } catch (DatabaseException unused) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("WARNING: enableLogging(bool) must be called before any other use of database(). \nIf you are sure you've done this then this message can be ignored during development as \nRN reloads can cause false positives. APP: ");
                sb.append(firebaseApp.getName());
                Log.w(str, sb.toString());
            }
        }
    }

    @ReactMethod
    public void keepSynced(String str, String str2, String str3, String str4, ReadableArray readableArray, Boolean bool) {
        getInternalReferenceForApp(str, str2, str3, str4, readableArray).getQuery().keepSynced(bool.booleanValue());
    }

    @ReactMethod
    public void transactionTryCommit(String str, String str2, int i, ReadableMap readableMap) {
        RNFirebaseTransactionHandler rNFirebaseTransactionHandler = (RNFirebaseTransactionHandler) transactionHandlers.get(i);
        if (rNFirebaseTransactionHandler != null) {
            rNFirebaseTransactionHandler.signalUpdateReceived(readableMap);
        }
    }

    @ReactMethod
    public void transactionStart(String str, String str2, String str3, int i, Boolean bool) {
        final String str4 = str;
        final String str5 = str2;
        final String str6 = str3;
        final int i2 = i;
        final Boolean bool2 = bool;
        C13981 r0 = new Runnable() {
            public void run() {
                RNFirebaseDatabase.this.getReferenceForAppPath(str4, str5, str6).runTransaction(new Handler() {
                    @Nonnull
                    public Result doTransaction(@Nonnull MutableData mutableData) {
                        RNFirebaseTransactionHandler rNFirebaseTransactionHandler = new RNFirebaseTransactionHandler(i2, str4, str5);
                        RNFirebaseDatabase.transactionHandlers.put(i2, rNFirebaseTransactionHandler);
                        final WritableMap createUpdateMap = rNFirebaseTransactionHandler.createUpdateMap(mutableData);
                        AsyncTask.execute(new Runnable() {
                            public void run() {
                                C1350Utils.sendEvent(RNFirebaseDatabase.this.getReactApplicationContext(), "database_transaction_event", createUpdateMap);
                            }
                        });
                        try {
                            rNFirebaseTransactionHandler.await();
                            if (rNFirebaseTransactionHandler.abort) {
                                return Transaction.abort();
                            }
                            if (rNFirebaseTransactionHandler.timeout) {
                                return Transaction.abort();
                            }
                            mutableData.setValue(rNFirebaseTransactionHandler.value);
                            return Transaction.success(mutableData);
                        } catch (InterruptedException unused) {
                            rNFirebaseTransactionHandler.interrupted = true;
                            return Transaction.abort();
                        }
                    }

                    public void onComplete(DatabaseError databaseError, boolean z, DataSnapshot dataSnapshot) {
                        C1350Utils.sendEvent(RNFirebaseDatabase.this.getReactApplicationContext(), "database_transaction_event", ((RNFirebaseTransactionHandler) RNFirebaseDatabase.transactionHandlers.get(i2)).createResultMap(databaseError, z, dataSnapshot));
                        RNFirebaseDatabase.transactionHandlers.delete(i2);
                    }
                }, bool2.booleanValue());
            }
        };
        AsyncTask.execute(r0);
    }

    @com.facebook.react.bridge.ReactMethod
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onDisconnectSet(java.lang.String r2, java.lang.String r3, java.lang.String r4, com.facebook.react.bridge.ReadableMap r5, final com.facebook.react.bridge.Promise r6) {
        /*
            r1 = this;
            java.lang.String r0 = "type"
            java.lang.String r0 = r5.getString(r0)
            com.google.firebase.database.DatabaseReference r2 = r1.getReferenceForAppPath(r2, r3, r4)
            com.google.firebase.database.OnDisconnect r2 = r2.onDisconnect()
            io.invertase.firebase.database.RNFirebaseDatabase$2 r3 = new io.invertase.firebase.database.RNFirebaseDatabase$2
            r3.<init>(r6)
            int r4 = r0.hashCode()
            switch(r4) {
                case -1034364087: goto L_0x004d;
                case -1023368385: goto L_0x0043;
                case -891985903: goto L_0x0039;
                case 3392903: goto L_0x002f;
                case 64711720: goto L_0x0025;
                case 93090393: goto L_0x001b;
                default: goto L_0x001a;
            }
        L_0x001a:
            goto L_0x0057
        L_0x001b:
            java.lang.String r4 = "array"
            boolean r4 = r0.equals(r4)
            if (r4 == 0) goto L_0x0057
            r4 = 1
            goto L_0x0058
        L_0x0025:
            java.lang.String r4 = "boolean"
            boolean r4 = r0.equals(r4)
            if (r4 == 0) goto L_0x0057
            r4 = 4
            goto L_0x0058
        L_0x002f:
            java.lang.String r4 = "null"
            boolean r4 = r0.equals(r4)
            if (r4 == 0) goto L_0x0057
            r4 = 5
            goto L_0x0058
        L_0x0039:
            java.lang.String r4 = "string"
            boolean r4 = r0.equals(r4)
            if (r4 == 0) goto L_0x0057
            r4 = 2
            goto L_0x0058
        L_0x0043:
            java.lang.String r4 = "object"
            boolean r4 = r0.equals(r4)
            if (r4 == 0) goto L_0x0057
            r4 = 0
            goto L_0x0058
        L_0x004d:
            java.lang.String r4 = "number"
            boolean r4 = r0.equals(r4)
            if (r4 == 0) goto L_0x0057
            r4 = 3
            goto L_0x0058
        L_0x0057:
            r4 = -1
        L_0x0058:
            switch(r4) {
                case 0: goto L_0x0095;
                case 1: goto L_0x0087;
                case 2: goto L_0x007d;
                case 3: goto L_0x006f;
                case 4: goto L_0x0061;
                case 5: goto L_0x005c;
                default: goto L_0x005b;
            }
        L_0x005b:
            goto L_0x00a2
        L_0x005c:
            r4 = 0
            r2.setValue(r4, r3)
            goto L_0x00a2
        L_0x0061:
            java.lang.String r4 = "value"
            boolean r4 = r5.getBoolean(r4)
            java.lang.Boolean r4 = java.lang.Boolean.valueOf(r4)
            r2.setValue(r4, r3)
            goto L_0x00a2
        L_0x006f:
            java.lang.String r4 = "value"
            double r4 = r5.getDouble(r4)
            java.lang.Double r4 = java.lang.Double.valueOf(r4)
            r2.setValue(r4, r3)
            goto L_0x00a2
        L_0x007d:
            java.lang.String r4 = "value"
            java.lang.String r4 = r5.getString(r4)
            r2.setValue(r4, r3)
            goto L_0x00a2
        L_0x0087:
            java.lang.String r4 = "value"
            com.facebook.react.bridge.ReadableArray r4 = r5.getArray(r4)
            java.util.List r4 = p007io.invertase.firebase.C1350Utils.recursivelyDeconstructReadableArray(r4)
            r2.setValue(r4, r3)
            goto L_0x00a2
        L_0x0095:
            java.lang.String r4 = "value"
            com.facebook.react.bridge.ReadableMap r4 = r5.getMap(r4)
            java.util.Map r4 = p007io.invertase.firebase.C1350Utils.recursivelyDeconstructReadableMap(r4)
            r2.setValue(r4, r3)
        L_0x00a2:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: p007io.invertase.firebase.database.RNFirebaseDatabase.onDisconnectSet(java.lang.String, java.lang.String, java.lang.String, com.facebook.react.bridge.ReadableMap, com.facebook.react.bridge.Promise):void");
    }

    @ReactMethod
    public void onDisconnectUpdate(String str, String str2, String str3, ReadableMap readableMap, final Promise promise) {
        getReferenceForAppPath(str, str2, str3).onDisconnect().updateChildren(C1350Utils.recursivelyDeconstructReadableMap(readableMap), new CompletionListener() {
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                RNFirebaseDatabase.handlePromise(promise, databaseError);
            }
        });
    }

    @ReactMethod
    public void onDisconnectRemove(String str, String str2, String str3, final Promise promise) {
        getReferenceForAppPath(str, str2, str3).onDisconnect().removeValue(new CompletionListener() {
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                RNFirebaseDatabase.handlePromise(promise, databaseError);
            }
        });
    }

    @ReactMethod
    public void onDisconnectCancel(String str, String str2, String str3, final Promise promise) {
        getReferenceForAppPath(str, str2, str3).onDisconnect().cancel(new CompletionListener() {
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                RNFirebaseDatabase.handlePromise(promise, databaseError);
            }
        });
    }

    @ReactMethod
    public void set(String str, String str2, String str3, ReadableMap readableMap, final Promise promise) {
        getReferenceForAppPath(str, str2, str3).setValue(C1350Utils.recursivelyDeconstructReadableMap(readableMap).get("value"), new CompletionListener() {
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                RNFirebaseDatabase.handlePromise(promise, databaseError);
            }
        });
    }

    @ReactMethod
    public void setPriority(String str, String str2, String str3, ReadableMap readableMap, final Promise promise) {
        getReferenceForAppPath(str, str2, str3).setPriority(C1350Utils.recursivelyDeconstructReadableMap(readableMap).get("value"), new CompletionListener() {
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                RNFirebaseDatabase.handlePromise(promise, databaseError);
            }
        });
    }

    @ReactMethod
    public void setWithPriority(String str, String str2, String str3, ReadableMap readableMap, ReadableMap readableMap2, final Promise promise) {
        getReferenceForAppPath(str, str2, str3).setValue(C1350Utils.recursivelyDeconstructReadableMap(readableMap).get("value"), C1350Utils.recursivelyDeconstructReadableMap(readableMap2).get("value"), new CompletionListener() {
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                RNFirebaseDatabase.handlePromise(promise, databaseError);
            }
        });
    }

    @ReactMethod
    public void update(String str, String str2, String str3, ReadableMap readableMap, final Promise promise) {
        getReferenceForAppPath(str, str2, str3).updateChildren(C1350Utils.recursivelyDeconstructReadableMap(readableMap), new CompletionListener() {
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                RNFirebaseDatabase.handlePromise(promise, databaseError);
            }
        });
    }

    @ReactMethod
    public void remove(String str, String str2, String str3, final Promise promise) {
        getReferenceForAppPath(str, str2, str3).removeValue(new CompletionListener() {
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                RNFirebaseDatabase.handlePromise(promise, databaseError);
            }
        });
    }

    @ReactMethod
    public void once(String str, String str2, String str3, String str4, ReadableArray readableArray, String str5, Promise promise) {
        getInternalReferenceForApp(str, str2, str3, str4, readableArray).once(str5, promise);
    }

    @ReactMethod
    /* renamed from: on */
    public void mo20011on(String str, String str2, ReadableMap readableMap) {
        getCachedInternalReferenceForApp(str, str2, readableMap).mo20041on(readableMap.getString("eventType"), readableMap.getMap("registration"));
    }

    @ReactMethod
    public void off(String str, String str2) {
        RNFirebaseDatabaseReference rNFirebaseDatabaseReference = (RNFirebaseDatabaseReference) references.get(str);
        if (rNFirebaseDatabaseReference != null) {
            rNFirebaseDatabaseReference.removeEventListener(str2);
            if (!rNFirebaseDatabaseReference.hasListeners().booleanValue()) {
                references.remove(str);
            }
        }
    }

    /* access modifiers changed from: private */
    public DatabaseReference getReferenceForAppPath(String str, String str2, String str3) {
        return getDatabaseForApp(str, str2).getReference(str3);
    }

    private RNFirebaseDatabaseReference getInternalReferenceForApp(String str, String str2, String str3, String str4, ReadableArray readableArray) {
        RNFirebaseDatabaseReference rNFirebaseDatabaseReference = new RNFirebaseDatabaseReference(str, str2, str3, str4, readableArray);
        return rNFirebaseDatabaseReference;
    }

    private RNFirebaseDatabaseReference getCachedInternalReferenceForApp(String str, String str2, ReadableMap readableMap) {
        String string = readableMap.getString("key");
        String string2 = readableMap.getString(RNFetchBlobConst.RNFB_RESPONSE_PATH);
        ReadableArray array = readableMap.getArray("modifiers");
        RNFirebaseDatabaseReference rNFirebaseDatabaseReference = (RNFirebaseDatabaseReference) references.get(string);
        if (rNFirebaseDatabaseReference != null) {
            return rNFirebaseDatabaseReference;
        }
        RNFirebaseDatabaseReference internalReferenceForApp = getInternalReferenceForApp(str, str2, string, string2, array);
        references.put(string, internalReferenceForApp);
        return internalReferenceForApp;
    }

    public Map<String, Object> getConstants() {
        HashMap hashMap = new HashMap();
        hashMap.put("serverValueTimestamp", ServerValue.TIMESTAMP);
        return hashMap;
    }
}

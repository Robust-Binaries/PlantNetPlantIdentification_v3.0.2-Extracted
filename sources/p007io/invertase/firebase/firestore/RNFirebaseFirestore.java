package p007io.invertase.firebase.firestore;

import android.os.AsyncTask;
import android.support.p000v4.p002os.EnvironmentCompat;
import android.util.Log;
import android.util.SparseArray;
import com.RNFetchBlob.RNFetchBlobConst;
import com.facebook.common.util.UriUtil;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreException.Code;
import com.google.firebase.firestore.FirebaseFirestoreSettings.Builder;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Transaction.Function;
import com.google.firebase.firestore.WriteBatch;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import p007io.invertase.firebase.C1350Utils;
import p007io.invertase.firebase.ErrorUtils;

/* renamed from: io.invertase.firebase.firestore.RNFirebaseFirestore */
public class RNFirebaseFirestore extends ReactContextBaseJavaModule {
    private static final String TAG = "RNFirebaseFirestore";
    private SparseArray<RNFirebaseFirestoreTransactionHandler> transactionHandlers = new SparseArray<>();

    /* renamed from: io.invertase.firebase.firestore.RNFirebaseFirestore$5 */
    static /* synthetic */ class C14255 {

        /* renamed from: $SwitchMap$com$google$firebase$firestore$FirebaseFirestoreException$Code */
        static final /* synthetic */ int[] f113xf2f5b1d8 = new int[Code.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(36:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31|32|33|34|36) */
        /* JADX WARNING: Code restructure failed: missing block: B:37:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x0040 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x004b */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x0056 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0062 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x006e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x007a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:23:0x0086 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:25:0x0092 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x009e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:29:0x00aa */
        /* JADX WARNING: Missing exception handler attribute for start block: B:31:0x00b6 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:33:0x00c2 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0035 */
        static {
            /*
                com.google.firebase.firestore.FirebaseFirestoreException$Code[] r0 = com.google.firebase.firestore.FirebaseFirestoreException.Code.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                f113xf2f5b1d8 = r0
                int[] r0 = f113xf2f5b1d8     // Catch:{ NoSuchFieldError -> 0x0014 }
                com.google.firebase.firestore.FirebaseFirestoreException$Code r1 = com.google.firebase.firestore.FirebaseFirestoreException.Code.OK     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = f113xf2f5b1d8     // Catch:{ NoSuchFieldError -> 0x001f }
                com.google.firebase.firestore.FirebaseFirestoreException$Code r1 = com.google.firebase.firestore.FirebaseFirestoreException.Code.CANCELLED     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r0 = f113xf2f5b1d8     // Catch:{ NoSuchFieldError -> 0x002a }
                com.google.firebase.firestore.FirebaseFirestoreException$Code r1 = com.google.firebase.firestore.FirebaseFirestoreException.Code.UNKNOWN     // Catch:{ NoSuchFieldError -> 0x002a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                int[] r0 = f113xf2f5b1d8     // Catch:{ NoSuchFieldError -> 0x0035 }
                com.google.firebase.firestore.FirebaseFirestoreException$Code r1 = com.google.firebase.firestore.FirebaseFirestoreException.Code.INVALID_ARGUMENT     // Catch:{ NoSuchFieldError -> 0x0035 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0035 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0035 }
            L_0x0035:
                int[] r0 = f113xf2f5b1d8     // Catch:{ NoSuchFieldError -> 0x0040 }
                com.google.firebase.firestore.FirebaseFirestoreException$Code r1 = com.google.firebase.firestore.FirebaseFirestoreException.Code.DEADLINE_EXCEEDED     // Catch:{ NoSuchFieldError -> 0x0040 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0040 }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0040 }
            L_0x0040:
                int[] r0 = f113xf2f5b1d8     // Catch:{ NoSuchFieldError -> 0x004b }
                com.google.firebase.firestore.FirebaseFirestoreException$Code r1 = com.google.firebase.firestore.FirebaseFirestoreException.Code.NOT_FOUND     // Catch:{ NoSuchFieldError -> 0x004b }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x004b }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x004b }
            L_0x004b:
                int[] r0 = f113xf2f5b1d8     // Catch:{ NoSuchFieldError -> 0x0056 }
                com.google.firebase.firestore.FirebaseFirestoreException$Code r1 = com.google.firebase.firestore.FirebaseFirestoreException.Code.ALREADY_EXISTS     // Catch:{ NoSuchFieldError -> 0x0056 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0056 }
                r2 = 7
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0056 }
            L_0x0056:
                int[] r0 = f113xf2f5b1d8     // Catch:{ NoSuchFieldError -> 0x0062 }
                com.google.firebase.firestore.FirebaseFirestoreException$Code r1 = com.google.firebase.firestore.FirebaseFirestoreException.Code.PERMISSION_DENIED     // Catch:{ NoSuchFieldError -> 0x0062 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0062 }
                r2 = 8
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0062 }
            L_0x0062:
                int[] r0 = f113xf2f5b1d8     // Catch:{ NoSuchFieldError -> 0x006e }
                com.google.firebase.firestore.FirebaseFirestoreException$Code r1 = com.google.firebase.firestore.FirebaseFirestoreException.Code.RESOURCE_EXHAUSTED     // Catch:{ NoSuchFieldError -> 0x006e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x006e }
                r2 = 9
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x006e }
            L_0x006e:
                int[] r0 = f113xf2f5b1d8     // Catch:{ NoSuchFieldError -> 0x007a }
                com.google.firebase.firestore.FirebaseFirestoreException$Code r1 = com.google.firebase.firestore.FirebaseFirestoreException.Code.FAILED_PRECONDITION     // Catch:{ NoSuchFieldError -> 0x007a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x007a }
                r2 = 10
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x007a }
            L_0x007a:
                int[] r0 = f113xf2f5b1d8     // Catch:{ NoSuchFieldError -> 0x0086 }
                com.google.firebase.firestore.FirebaseFirestoreException$Code r1 = com.google.firebase.firestore.FirebaseFirestoreException.Code.ABORTED     // Catch:{ NoSuchFieldError -> 0x0086 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0086 }
                r2 = 11
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0086 }
            L_0x0086:
                int[] r0 = f113xf2f5b1d8     // Catch:{ NoSuchFieldError -> 0x0092 }
                com.google.firebase.firestore.FirebaseFirestoreException$Code r1 = com.google.firebase.firestore.FirebaseFirestoreException.Code.OUT_OF_RANGE     // Catch:{ NoSuchFieldError -> 0x0092 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0092 }
                r2 = 12
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0092 }
            L_0x0092:
                int[] r0 = f113xf2f5b1d8     // Catch:{ NoSuchFieldError -> 0x009e }
                com.google.firebase.firestore.FirebaseFirestoreException$Code r1 = com.google.firebase.firestore.FirebaseFirestoreException.Code.UNIMPLEMENTED     // Catch:{ NoSuchFieldError -> 0x009e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x009e }
                r2 = 13
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x009e }
            L_0x009e:
                int[] r0 = f113xf2f5b1d8     // Catch:{ NoSuchFieldError -> 0x00aa }
                com.google.firebase.firestore.FirebaseFirestoreException$Code r1 = com.google.firebase.firestore.FirebaseFirestoreException.Code.INTERNAL     // Catch:{ NoSuchFieldError -> 0x00aa }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x00aa }
                r2 = 14
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x00aa }
            L_0x00aa:
                int[] r0 = f113xf2f5b1d8     // Catch:{ NoSuchFieldError -> 0x00b6 }
                com.google.firebase.firestore.FirebaseFirestoreException$Code r1 = com.google.firebase.firestore.FirebaseFirestoreException.Code.UNAVAILABLE     // Catch:{ NoSuchFieldError -> 0x00b6 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x00b6 }
                r2 = 15
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x00b6 }
            L_0x00b6:
                int[] r0 = f113xf2f5b1d8     // Catch:{ NoSuchFieldError -> 0x00c2 }
                com.google.firebase.firestore.FirebaseFirestoreException$Code r1 = com.google.firebase.firestore.FirebaseFirestoreException.Code.DATA_LOSS     // Catch:{ NoSuchFieldError -> 0x00c2 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x00c2 }
                r2 = 16
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x00c2 }
            L_0x00c2:
                int[] r0 = f113xf2f5b1d8     // Catch:{ NoSuchFieldError -> 0x00ce }
                com.google.firebase.firestore.FirebaseFirestoreException$Code r1 = com.google.firebase.firestore.FirebaseFirestoreException.Code.UNAUTHENTICATED     // Catch:{ NoSuchFieldError -> 0x00ce }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x00ce }
                r2 = 17
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x00ce }
            L_0x00ce:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: p007io.invertase.firebase.firestore.RNFirebaseFirestore.C14255.<clinit>():void");
        }
    }

    public String getName() {
        return TAG;
    }

    RNFirebaseFirestore(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
    }

    static void promiseRejectException(Promise promise, FirebaseFirestoreException firebaseFirestoreException) {
        WritableMap jSError = getJSError(firebaseFirestoreException);
        promise.reject(jSError.getString("code"), jSError.getString("message"), (Throwable) firebaseFirestoreException);
    }

    static FirebaseFirestore getFirestoreForApp(String str) {
        return FirebaseFirestore.getInstance(FirebaseApp.getInstance(str));
    }

    static WritableMap getJSError(FirebaseFirestoreException firebaseFirestoreException) {
        String str;
        String str2;
        WritableMap createMap = Arguments.createMap();
        createMap.putInt("nativeErrorCode", firebaseFirestoreException.getCode().value());
        createMap.putString("nativeErrorMessage", firebaseFirestoreException.getMessage());
        String str3 = "Firestore";
        switch (C14255.f113xf2f5b1d8[firebaseFirestoreException.getCode().ordinal()]) {
            case 1:
                str = ErrorUtils.getCodeWithService(str3, "ok");
                str2 = ErrorUtils.getMessageWithService("Ok.", str3, str);
                break;
            case 2:
                str = ErrorUtils.getCodeWithService(str3, "cancelled");
                str2 = ErrorUtils.getMessageWithService("The operation was cancelled.", str3, str);
                break;
            case 3:
                str = ErrorUtils.getCodeWithService(str3, EnvironmentCompat.MEDIA_UNKNOWN);
                str2 = ErrorUtils.getMessageWithService("Unknown error or an error from a different error domain.", str3, str);
                break;
            case 4:
                str = ErrorUtils.getCodeWithService(str3, "invalid-argument");
                str2 = ErrorUtils.getMessageWithService("Client specified an invalid argument.", str3, str);
                break;
            case 5:
                str = ErrorUtils.getCodeWithService(str3, "deadline-exceeded");
                str2 = ErrorUtils.getMessageWithService("Deadline expired before operation could complete.", str3, str);
                break;
            case 6:
                str = ErrorUtils.getCodeWithService(str3, "not-found");
                str2 = ErrorUtils.getMessageWithService("Some requested document was not found.", str3, str);
                break;
            case 7:
                str = ErrorUtils.getCodeWithService(str3, "already-exists");
                str2 = ErrorUtils.getMessageWithService("Some document that we attempted to create already exists.", str3, str);
                break;
            case 8:
                str = ErrorUtils.getCodeWithService(str3, "permission-denied");
                str2 = ErrorUtils.getMessageWithService("The caller does not have permission to execute the specified operation.", str3, str);
                break;
            case 9:
                str = ErrorUtils.getCodeWithService(str3, "resource-exhausted");
                str2 = ErrorUtils.getMessageWithService("Some resource has been exhausted, perhaps a per-user quota, or perhaps the entire file system is out of space.", str3, str);
                break;
            case 10:
                str = ErrorUtils.getCodeWithService(str3, "failed-precondition");
                str2 = ErrorUtils.getMessageWithService("Operation was rejected because the system is not in a state required for the operation`s execution.", str3, str);
                break;
            case 11:
                str = ErrorUtils.getCodeWithService(str3, "aborted");
                str2 = ErrorUtils.getMessageWithService("The operation was aborted, typically due to a concurrency issue like transaction aborts, etc.", str3, str);
                break;
            case 12:
                str = ErrorUtils.getCodeWithService(str3, "out-of-range");
                str2 = ErrorUtils.getMessageWithService("Operation was attempted past the valid range.", str3, str);
                break;
            case 13:
                str = ErrorUtils.getCodeWithService(str3, "unimplemented");
                str2 = ErrorUtils.getMessageWithService("Operation is not implemented or not supported/enabled.", str3, str);
                break;
            case 14:
                str = ErrorUtils.getCodeWithService(str3, "internal");
                str2 = ErrorUtils.getMessageWithService("Internal errors.", str3, str);
                break;
            case 15:
                str = ErrorUtils.getCodeWithService(str3, "unavailable");
                str2 = ErrorUtils.getMessageWithService("The service is currently unavailable.", str3, str);
                break;
            case 16:
                str = ErrorUtils.getCodeWithService(str3, "data-loss");
                str2 = ErrorUtils.getMessageWithService("Unrecoverable data loss or corruption.", str3, str);
                break;
            case 17:
                str = ErrorUtils.getCodeWithService(str3, "unauthenticated");
                str2 = ErrorUtils.getMessageWithService("The request does not have valid authentication credentials for the operation.", str3, str);
                break;
            default:
                str = ErrorUtils.getCodeWithService(str3, EnvironmentCompat.MEDIA_UNKNOWN);
                str2 = ErrorUtils.getMessageWithService("An unknown error occurred.", str3, str);
                break;
        }
        createMap.putString("code", str);
        createMap.putString("message", str2);
        return createMap;
    }

    @ReactMethod
    public void disableNetwork(String str, final Promise promise) {
        getFirestoreForApp(str).disableNetwork().addOnCompleteListener(new OnCompleteListener<Void>() {
            public void onComplete(@Nonnull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(RNFirebaseFirestore.TAG, "disableNetwork:onComplete:success");
                    promise.resolve(null);
                    return;
                }
                Log.e(RNFirebaseFirestore.TAG, "disableNetwork:onComplete:failure", task.getException());
                RNFirebaseFirestore.promiseRejectException(promise, task.getException());
            }
        });
    }

    @ReactMethod
    public void setLogLevel(String str) {
        if ("debug".equals(str) || "error".equals(str)) {
            FirebaseFirestore.setLoggingEnabled(true);
        } else {
            FirebaseFirestore.setLoggingEnabled(false);
        }
    }

    @ReactMethod
    public void enableNetwork(String str, final Promise promise) {
        getFirestoreForApp(str).enableNetwork().addOnCompleteListener(new OnCompleteListener<Void>() {
            public void onComplete(@Nonnull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(RNFirebaseFirestore.TAG, "enableNetwork:onComplete:success");
                    promise.resolve(null);
                    return;
                }
                Log.e(RNFirebaseFirestore.TAG, "enableNetwork:onComplete:failure", task.getException());
                RNFirebaseFirestore.promiseRejectException(promise, task.getException());
            }
        });
    }

    @ReactMethod
    public void collectionGet(String str, String str2, ReadableArray readableArray, ReadableArray readableArray2, ReadableMap readableMap, ReadableMap readableMap2, Promise promise) {
        getCollectionForAppPath(str, str2, readableArray, readableArray2, readableMap).get(readableMap2, promise);
    }

    @ReactMethod
    public void collectionOffSnapshot(String str, String str2, ReadableArray readableArray, ReadableArray readableArray2, ReadableMap readableMap, String str3) {
        RNFirebaseFirestoreCollectionReference.offSnapshot(str3);
    }

    @ReactMethod
    public void collectionOnSnapshot(String str, String str2, ReadableArray readableArray, ReadableArray readableArray2, ReadableMap readableMap, String str3, ReadableMap readableMap2) {
        getCollectionForAppPath(str, str2, readableArray, readableArray2, readableMap).onSnapshot(str3, readableMap2);
    }

    @ReactMethod
    public void documentBatch(String str, ReadableArray readableArray, final Promise promise) {
        FirebaseFirestore firestoreForApp = getFirestoreForApp(str);
        WriteBatch batch = firestoreForApp.batch();
        for (Map map : FirestoreSerialize.parseDocumentBatches(firestoreForApp, readableArray)) {
            String str2 = (String) map.get("type");
            Map map2 = (Map) map.get(UriUtil.DATA_SCHEME);
            DocumentReference document = firestoreForApp.document((String) map.get(RNFetchBlobConst.RNFB_RESPONSE_PATH));
            char c = 65535;
            int hashCode = str2.hashCode();
            if (hashCode != -1785516855) {
                if (hashCode != 81986) {
                    if (hashCode == 2012838315 && str2.equals("DELETE")) {
                        c = 0;
                    }
                } else if (str2.equals("SET")) {
                    c = 1;
                }
            } else if (str2.equals("UPDATE")) {
                c = 2;
            }
            switch (c) {
                case 0:
                    batch = batch.delete(document);
                    break;
                case 1:
                    Map map3 = (Map) map.get("options");
                    if (map3 != null && map3.containsKey("merge") && ((Boolean) map3.get("merge")).booleanValue()) {
                        batch = batch.set(document, map2, SetOptions.merge());
                        break;
                    } else {
                        batch = batch.set(document, map2);
                        break;
                    }
                case 2:
                    batch = batch.update(document, map2);
                    break;
            }
        }
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            public void onComplete(@Nonnull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(RNFirebaseFirestore.TAG, "documentBatch:onComplete:success");
                    promise.resolve(null);
                    return;
                }
                Log.e(RNFirebaseFirestore.TAG, "documentBatch:onComplete:failure", task.getException());
                RNFirebaseFirestore.promiseRejectException(promise, task.getException());
            }
        });
    }

    @ReactMethod
    public void documentDelete(String str, String str2, Promise promise) {
        getDocumentForAppPath(str, str2).delete(promise);
    }

    @ReactMethod
    public void documentGet(String str, String str2, ReadableMap readableMap, Promise promise) {
        getDocumentForAppPath(str, str2).get(readableMap, promise);
    }

    @ReactMethod
    public void documentOffSnapshot(String str, String str2, String str3) {
        RNFirebaseFirestoreDocumentReference.offSnapshot(str3);
    }

    @ReactMethod
    public void documentOnSnapshot(String str, String str2, String str3, ReadableMap readableMap) {
        getDocumentForAppPath(str, str2).onSnapshot(str3, readableMap);
    }

    @ReactMethod
    public void documentSet(String str, String str2, ReadableMap readableMap, ReadableMap readableMap2, Promise promise) {
        getDocumentForAppPath(str, str2).set(readableMap, readableMap2, promise);
    }

    @ReactMethod
    public void documentUpdate(String str, String str2, ReadableMap readableMap, Promise promise) {
        getDocumentForAppPath(str, str2).update(readableMap, promise);
    }

    @ReactMethod
    public void settings(String str, ReadableMap readableMap, Promise promise) {
        FirebaseFirestore firestoreForApp = getFirestoreForApp(str);
        Builder builder = new Builder();
        if (readableMap.hasKey("host")) {
            builder.setHost(readableMap.getString("host"));
        } else {
            builder.setHost(firestoreForApp.getFirestoreSettings().getHost());
        }
        if (readableMap.hasKey("persistence")) {
            builder.setPersistenceEnabled(readableMap.getBoolean("persistence"));
        } else {
            builder.setPersistenceEnabled(firestoreForApp.getFirestoreSettings().isPersistenceEnabled());
        }
        if (readableMap.hasKey("ssl")) {
            builder.setSslEnabled(readableMap.getBoolean("ssl"));
        } else {
            builder.setSslEnabled(firestoreForApp.getFirestoreSettings().isSslEnabled());
        }
        if (readableMap.hasKey("timestampsInSnapshots")) {
            builder.setTimestampsInSnapshotsEnabled(readableMap.getBoolean("timestampsInSnapshots"));
        }
        firestoreForApp.setFirestoreSettings(builder.build());
        promise.resolve(null);
    }

    public void onCatalystInstanceDestroy() {
        int size = this.transactionHandlers.size();
        for (int i = 0; i < size; i++) {
            RNFirebaseFirestoreTransactionHandler rNFirebaseFirestoreTransactionHandler = (RNFirebaseFirestoreTransactionHandler) this.transactionHandlers.get(i);
            if (rNFirebaseFirestoreTransactionHandler != null) {
                rNFirebaseFirestoreTransactionHandler.abort();
            }
        }
        this.transactionHandlers.clear();
    }

    @ReactMethod
    public void transactionGetDocument(String str, int i, String str2, Promise promise) {
        RNFirebaseFirestoreTransactionHandler rNFirebaseFirestoreTransactionHandler = (RNFirebaseFirestoreTransactionHandler) this.transactionHandlers.get(i);
        if (rNFirebaseFirestoreTransactionHandler == null) {
            promise.reject("internal-error", "An internal error occurred whilst attempting to find a native transaction by id.");
        } else {
            rNFirebaseFirestoreTransactionHandler.getDocument(getDocumentForAppPath(str, str2).getRef(), promise);
        }
    }

    @ReactMethod
    public void transactionDispose(String str, int i) {
        RNFirebaseFirestoreTransactionHandler rNFirebaseFirestoreTransactionHandler = (RNFirebaseFirestoreTransactionHandler) this.transactionHandlers.get(i);
        if (rNFirebaseFirestoreTransactionHandler != null) {
            rNFirebaseFirestoreTransactionHandler.abort();
            this.transactionHandlers.delete(i);
        }
    }

    @ReactMethod
    public void transactionApplyBuffer(String str, int i, ReadableArray readableArray) {
        RNFirebaseFirestoreTransactionHandler rNFirebaseFirestoreTransactionHandler = (RNFirebaseFirestoreTransactionHandler) this.transactionHandlers.get(i);
        if (rNFirebaseFirestoreTransactionHandler != null) {
            rNFirebaseFirestoreTransactionHandler.signalBufferReceived(readableArray);
        }
    }

    @ReactMethod
    public void transactionBegin(final String str, int i) {
        final RNFirebaseFirestoreTransactionHandler rNFirebaseFirestoreTransactionHandler = new RNFirebaseFirestoreTransactionHandler(str, i);
        this.transactionHandlers.put(i, rNFirebaseFirestoreTransactionHandler);
        AsyncTask.execute(new Runnable() {
            public void run() {
                RNFirebaseFirestore.getFirestoreForApp(str).runTransaction(new Function<Void>() {
                    /* JADX WARNING: Removed duplicated region for block: B:28:0x00a9  */
                    /* JADX WARNING: Removed duplicated region for block: B:29:0x00b1  */
                    /* JADX WARNING: Removed duplicated region for block: B:30:0x00cb  */
                    /* JADX WARNING: Removed duplicated region for block: B:44:0x008d A[SYNTHETIC] */
                    /* Code decompiled incorrectly, please refer to instructions dump. */
                    public java.lang.Void apply(@javax.annotation.Nonnull com.google.firebase.firestore.Transaction r12) throws com.google.firebase.firestore.FirebaseFirestoreException {
                        /*
                            r11 = this;
                            io.invertase.firebase.firestore.RNFirebaseFirestore$4 r0 = p007io.invertase.firebase.firestore.RNFirebaseFirestore.C14204.this
                            io.invertase.firebase.firestore.RNFirebaseFirestoreTransactionHandler r0 = r0
                            r0.resetState(r12)
                            io.invertase.firebase.firestore.RNFirebaseFirestore$4$3$1 r0 = new io.invertase.firebase.firestore.RNFirebaseFirestore$4$3$1
                            r0.<init>()
                            android.os.AsyncTask.execute(r0)
                            io.invertase.firebase.firestore.RNFirebaseFirestore$4 r0 = p007io.invertase.firebase.firestore.RNFirebaseFirestore.C14204.this
                            io.invertase.firebase.firestore.RNFirebaseFirestoreTransactionHandler r0 = r0
                            r0.await()
                            io.invertase.firebase.firestore.RNFirebaseFirestore$4 r0 = p007io.invertase.firebase.firestore.RNFirebaseFirestore.C14204.this
                            io.invertase.firebase.firestore.RNFirebaseFirestoreTransactionHandler r0 = r0
                            boolean r0 = r0.aborted
                            if (r0 != 0) goto L_0x0117
                            io.invertase.firebase.firestore.RNFirebaseFirestore$4 r0 = p007io.invertase.firebase.firestore.RNFirebaseFirestore.C14204.this
                            io.invertase.firebase.firestore.RNFirebaseFirestoreTransactionHandler r0 = r0
                            boolean r0 = r0.timeout
                            if (r0 != 0) goto L_0x010d
                            io.invertase.firebase.firestore.RNFirebaseFirestore$4 r0 = p007io.invertase.firebase.firestore.RNFirebaseFirestore.C14204.this
                            io.invertase.firebase.firestore.RNFirebaseFirestoreTransactionHandler r0 = r0
                            com.facebook.react.bridge.ReadableArray r0 = r0.getCommandBuffer()
                            r1 = 0
                            if (r0 != 0) goto L_0x0032
                            return r1
                        L_0x0032:
                            int r2 = r0.size()
                            r3 = 0
                            r4 = 0
                        L_0x0038:
                            if (r4 >= r2) goto L_0x010c
                            com.facebook.react.bridge.ReadableMap r5 = r0.getMap(r4)
                            java.lang.String r6 = "path"
                            java.lang.String r6 = r5.getString(r6)
                            java.lang.String r7 = "type"
                            java.lang.String r7 = r5.getString(r7)
                            io.invertase.firebase.firestore.RNFirebaseFirestore$4 r8 = p007io.invertase.firebase.firestore.RNFirebaseFirestore.C14204.this
                            io.invertase.firebase.firestore.RNFirebaseFirestore r8 = p007io.invertase.firebase.firestore.RNFirebaseFirestore.this
                            io.invertase.firebase.firestore.RNFirebaseFirestore$4 r9 = p007io.invertase.firebase.firestore.RNFirebaseFirestore.C14204.this
                            java.lang.String r9 = r3
                            io.invertase.firebase.firestore.RNFirebaseFirestoreDocumentReference r6 = r8.getDocumentForAppPath(r9, r6)
                            r8 = -1
                            int r9 = r7.hashCode()
                            r10 = -1335458389(0xffffffffb06685ab, float:-8.3863466E-10)
                            if (r9 == r10) goto L_0x007f
                            r10 = -838846263(0xffffffffce0038c9, float:-5.3780128E8)
                            if (r9 == r10) goto L_0x0075
                            r10 = 113762(0x1bc62, float:1.59415E-40)
                            if (r9 == r10) goto L_0x006b
                            goto L_0x0089
                        L_0x006b:
                            java.lang.String r9 = "set"
                            boolean r7 = r7.equals(r9)
                            if (r7 == 0) goto L_0x0089
                            r7 = 0
                            goto L_0x008a
                        L_0x0075:
                            java.lang.String r9 = "update"
                            boolean r7 = r7.equals(r9)
                            if (r7 == 0) goto L_0x0089
                            r7 = 1
                            goto L_0x008a
                        L_0x007f:
                            java.lang.String r9 = "delete"
                            boolean r7 = r7.equals(r9)
                            if (r7 == 0) goto L_0x0089
                            r7 = 2
                            goto L_0x008a
                        L_0x0089:
                            r7 = -1
                        L_0x008a:
                            switch(r7) {
                                case 0: goto L_0x00cb;
                                case 1: goto L_0x00b1;
                                case 2: goto L_0x00a9;
                                default: goto L_0x008d;
                            }
                        L_0x008d:
                            java.lang.IllegalArgumentException r12 = new java.lang.IllegalArgumentException
                            java.lang.StringBuilder r0 = new java.lang.StringBuilder
                            r0.<init>()
                            java.lang.String r1 = "Unknown command type at index "
                            r0.append(r1)
                            r0.append(r4)
                            java.lang.String r1 = "."
                            r0.append(r1)
                            java.lang.String r0 = r0.toString()
                            r12.<init>(r0)
                            throw r12
                        L_0x00a9:
                            com.google.firebase.firestore.DocumentReference r5 = r6.getRef()
                            r12.delete(r5)
                            goto L_0x0108
                        L_0x00b1:
                            java.lang.String r7 = "data"
                            com.facebook.react.bridge.ReadableMap r5 = r5.getMap(r7)
                            io.invertase.firebase.firestore.RNFirebaseFirestore$4 r7 = p007io.invertase.firebase.firestore.RNFirebaseFirestore.C14204.this
                            java.lang.String r7 = r3
                            com.google.firebase.firestore.FirebaseFirestore r7 = p007io.invertase.firebase.firestore.RNFirebaseFirestore.getFirestoreForApp(r7)
                            java.util.Map r5 = p007io.invertase.firebase.firestore.FirestoreSerialize.parseReadableMap(r7, r5)
                            com.google.firebase.firestore.DocumentReference r6 = r6.getRef()
                            r12.update(r6, r5)
                            goto L_0x0108
                        L_0x00cb:
                            java.lang.String r7 = "data"
                            com.facebook.react.bridge.ReadableMap r7 = r5.getMap(r7)
                            java.lang.String r8 = "options"
                            com.facebook.react.bridge.ReadableMap r5 = r5.getMap(r8)
                            io.invertase.firebase.firestore.RNFirebaseFirestore$4 r8 = p007io.invertase.firebase.firestore.RNFirebaseFirestore.C14204.this
                            java.lang.String r8 = r3
                            com.google.firebase.firestore.FirebaseFirestore r8 = p007io.invertase.firebase.firestore.RNFirebaseFirestore.getFirestoreForApp(r8)
                            java.util.Map r7 = p007io.invertase.firebase.firestore.FirestoreSerialize.parseReadableMap(r8, r7)
                            if (r5 == 0) goto L_0x0101
                            java.lang.String r8 = "merge"
                            boolean r8 = r5.hasKey(r8)
                            if (r8 == 0) goto L_0x0101
                            java.lang.String r8 = "merge"
                            boolean r5 = r5.getBoolean(r8)
                            if (r5 == 0) goto L_0x0101
                            com.google.firebase.firestore.DocumentReference r5 = r6.getRef()
                            com.google.firebase.firestore.SetOptions r6 = com.google.firebase.firestore.SetOptions.merge()
                            r12.set(r5, r7, r6)
                            goto L_0x0108
                        L_0x0101:
                            com.google.firebase.firestore.DocumentReference r5 = r6.getRef()
                            r12.set(r5, r7)
                        L_0x0108:
                            int r4 = r4 + 1
                            goto L_0x0038
                        L_0x010c:
                            return r1
                        L_0x010d:
                            com.google.firebase.firestore.FirebaseFirestoreException r12 = new com.google.firebase.firestore.FirebaseFirestoreException
                            com.google.firebase.firestore.FirebaseFirestoreException$Code r0 = com.google.firebase.firestore.FirebaseFirestoreException.Code.DEADLINE_EXCEEDED
                            java.lang.String r1 = "timeout"
                            r12.<init>(r1, r0)
                            throw r12
                        L_0x0117:
                            com.google.firebase.firestore.FirebaseFirestoreException r12 = new com.google.firebase.firestore.FirebaseFirestoreException
                            com.google.firebase.firestore.FirebaseFirestoreException$Code r0 = com.google.firebase.firestore.FirebaseFirestoreException.Code.ABORTED
                            java.lang.String r1 = "abort"
                            r12.<init>(r1, r0)
                            throw r12
                            return
                        */
                        throw new UnsupportedOperationException("Method not decompiled: p007io.invertase.firebase.firestore.RNFirebaseFirestore.C14204.C14233.apply(com.google.firebase.firestore.Transaction):java.lang.Void");
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    public void onSuccess(Void voidR) {
                        if (!rNFirebaseFirestoreTransactionHandler.aborted) {
                            Log.d(RNFirebaseFirestore.TAG, "Transaction onSuccess!");
                            C1350Utils.sendEvent(RNFirebaseFirestore.this.getReactApplicationContext(), "firestore_transaction_event", rNFirebaseFirestoreTransactionHandler.createEventMap(null, "complete"));
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    public void onFailure(@Nonnull Exception exc) {
                        if (!rNFirebaseFirestoreTransactionHandler.aborted) {
                            Log.w(RNFirebaseFirestore.TAG, "Transaction onFailure.", exc);
                            C1350Utils.sendEvent(RNFirebaseFirestore.this.getReactApplicationContext(), "firestore_transaction_event", rNFirebaseFirestoreTransactionHandler.createEventMap((FirebaseFirestoreException) exc, "error"));
                        }
                    }
                });
            }
        });
    }

    private RNFirebaseFirestoreCollectionReference getCollectionForAppPath(String str, String str2, ReadableArray readableArray, ReadableArray readableArray2, ReadableMap readableMap) {
        RNFirebaseFirestoreCollectionReference rNFirebaseFirestoreCollectionReference = new RNFirebaseFirestoreCollectionReference(getReactApplicationContext(), str, str2, readableArray, readableArray2, readableMap);
        return rNFirebaseFirestoreCollectionReference;
    }

    /* access modifiers changed from: private */
    public RNFirebaseFirestoreDocumentReference getDocumentForAppPath(String str, String str2) {
        return new RNFirebaseFirestoreDocumentReference(getReactApplicationContext(), str, str2);
    }

    public Map<String, Object> getConstants() {
        HashMap hashMap = new HashMap();
        hashMap.put("deleteFieldValue", FieldValue.delete().toString());
        hashMap.put("serverTimestampFieldValue", FieldValue.serverTimestamp().toString());
        return hashMap;
    }
}

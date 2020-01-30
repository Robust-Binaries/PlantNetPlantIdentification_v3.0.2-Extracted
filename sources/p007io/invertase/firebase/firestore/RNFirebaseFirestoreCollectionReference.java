package p007io.invertase.firebase.firestore;

import android.util.Log;
import com.RNFetchBlob.RNFetchBlobConst;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Query.Direction;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import p007io.invertase.firebase.C1350Utils;

/* renamed from: io.invertase.firebase.firestore.RNFirebaseFirestoreCollectionReference */
class RNFirebaseFirestoreCollectionReference {
    private static final String TAG = "RNFSCollectionReference";
    /* access modifiers changed from: private */
    public static Map<String, ListenerRegistration> collectionSnapshotListeners = new HashMap();
    /* access modifiers changed from: private */
    public final String appName;
    private final ReadableArray filters;
    private final ReadableMap options;
    private final ReadableArray orders;
    /* access modifiers changed from: private */
    public final String path;
    private final Query query = buildQuery();
    /* access modifiers changed from: private */
    public ReactContext reactContext;

    RNFirebaseFirestoreCollectionReference(ReactContext reactContext2, String str, String str2, ReadableArray readableArray, ReadableArray readableArray2, ReadableMap readableMap) {
        this.appName = str;
        this.path = str2;
        this.filters = readableArray;
        this.orders = readableArray2;
        this.options = readableMap;
        this.reactContext = reactContext2;
    }

    static void offSnapshot(String str) {
        ListenerRegistration listenerRegistration = (ListenerRegistration) collectionSnapshotListeners.remove(str);
        if (listenerRegistration != null) {
            listenerRegistration.remove();
        }
    }

    /* access modifiers changed from: 0000 */
    public void get(ReadableMap readableMap, final Promise promise) {
        Source source;
        if (readableMap == null || !readableMap.hasKey(Param.SOURCE)) {
            source = Source.DEFAULT;
        } else {
            String string = readableMap.getString(Param.SOURCE);
            if ("server".equals(string)) {
                source = Source.SERVER;
            } else if ("cache".equals(string)) {
                source = Source.CACHE;
            } else {
                source = Source.DEFAULT;
            }
        }
        final C14261 r0 = new QuerySnapshotSerializeAsyncTask(this.reactContext, this) {
            /* access modifiers changed from: protected */
            public void onPostExecute(WritableMap writableMap) {
                promise.resolve(writableMap);
            }
        };
        this.query.get(source).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            public void onComplete(@Nonnull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d(RNFirebaseFirestoreCollectionReference.TAG, "get:onComplete:success");
                    r0.execute(new Object[]{task.getResult()});
                    return;
                }
                Log.e(RNFirebaseFirestoreCollectionReference.TAG, "get:onComplete:failure", task.getException());
                RNFirebaseFirestore.promiseRejectException(promise, task.getException());
            }
        });
    }

    /* access modifiers changed from: 0000 */
    public void onSnapshot(final String str, ReadableMap readableMap) {
        MetadataChanges metadataChanges;
        if (!collectionSnapshotListeners.containsKey(str)) {
            C14283 r0 = new EventListener<QuerySnapshot>() {
                public void onEvent(QuerySnapshot querySnapshot, FirebaseFirestoreException firebaseFirestoreException) {
                    if (firebaseFirestoreException == null) {
                        RNFirebaseFirestoreCollectionReference.this.handleQuerySnapshotEvent(str, querySnapshot);
                        return;
                    }
                    ListenerRegistration listenerRegistration = (ListenerRegistration) RNFirebaseFirestoreCollectionReference.collectionSnapshotListeners.remove(str);
                    if (listenerRegistration != null) {
                        listenerRegistration.remove();
                    }
                    RNFirebaseFirestoreCollectionReference.this.handleQuerySnapshotError(str, firebaseFirestoreException);
                }
            };
            if (readableMap == null || !readableMap.hasKey("includeMetadataChanges") || !readableMap.getBoolean("includeMetadataChanges")) {
                metadataChanges = MetadataChanges.EXCLUDE;
            } else {
                metadataChanges = MetadataChanges.INCLUDE;
            }
            collectionSnapshotListeners.put(str, this.query.addSnapshotListener(metadataChanges, r0));
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean hasListeners() {
        return !collectionSnapshotListeners.isEmpty();
    }

    private Query buildQuery() {
        FirebaseFirestore firestoreForApp = RNFirebaseFirestore.getFirestoreForApp(this.appName);
        return applyOptions(firestoreForApp, applyOrders(applyFilters(firestoreForApp, firestoreForApp.collection(this.path))));
    }

    private Query applyFilters(FirebaseFirestore firebaseFirestore, Query query2) {
        Query query3 = query2;
        for (int i = 0; i < this.filters.size(); i++) {
            ReadableMap map = this.filters.getMap(i);
            ReadableMap map2 = map.getMap("fieldPath");
            String string = map2.getString("type");
            String string2 = map.getString("operator");
            Object parseTypeMap = FirestoreSerialize.parseTypeMap(firebaseFirestore, map.getMap("value"));
            char c = 65535;
            if (!string.equals("string")) {
                ReadableArray array = map2.getArray("elements");
                String[] strArr = new String[array.size()];
                for (int i2 = 0; i2 < array.size(); i2++) {
                    strArr[i2] = array.getString(i2);
                }
                FieldPath of = FieldPath.of(strArr);
                switch (string2.hashCode()) {
                    case -2081783184:
                        if (string2.equals("LESS_THAN_OR_EQUAL")) {
                            c = 4;
                            break;
                        }
                        break;
                    case -1112834937:
                        if (string2.equals("LESS_THAN")) {
                            c = 3;
                            break;
                        }
                        break;
                    case 66219796:
                        if (string2.equals("EQUAL")) {
                            c = 0;
                            break;
                        }
                        break;
                    case 67210597:
                        if (string2.equals("ARRAY_CONTAINS")) {
                            c = 5;
                            break;
                        }
                        break;
                    case 972152550:
                        if (string2.equals("GREATER_THAN")) {
                            c = 1;
                            break;
                        }
                        break;
                    case 989027057:
                        if (string2.equals("GREATER_THAN_OR_EQUAL")) {
                            c = 2;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        query3 = query3.whereEqualTo(of, parseTypeMap);
                        break;
                    case 1:
                        query3 = query3.whereGreaterThan(of, parseTypeMap);
                        break;
                    case 2:
                        query3 = query3.whereGreaterThanOrEqualTo(of, parseTypeMap);
                        break;
                    case 3:
                        query3 = query3.whereLessThan(of, parseTypeMap);
                        break;
                    case 4:
                        query3 = query3.whereLessThanOrEqualTo(of, parseTypeMap);
                        break;
                    case 5:
                        query3 = query3.whereArrayContains(of, parseTypeMap);
                        break;
                }
            } else {
                String string3 = map2.getString("string");
                switch (string2.hashCode()) {
                    case -2081783184:
                        if (string2.equals("LESS_THAN_OR_EQUAL")) {
                            c = 4;
                            break;
                        }
                        break;
                    case -1112834937:
                        if (string2.equals("LESS_THAN")) {
                            c = 3;
                            break;
                        }
                        break;
                    case 66219796:
                        if (string2.equals("EQUAL")) {
                            c = 0;
                            break;
                        }
                        break;
                    case 67210597:
                        if (string2.equals("ARRAY_CONTAINS")) {
                            c = 5;
                            break;
                        }
                        break;
                    case 972152550:
                        if (string2.equals("GREATER_THAN")) {
                            c = 1;
                            break;
                        }
                        break;
                    case 989027057:
                        if (string2.equals("GREATER_THAN_OR_EQUAL")) {
                            c = 2;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        query3 = query3.whereEqualTo(string3, parseTypeMap);
                        break;
                    case 1:
                        query3 = query3.whereGreaterThan(string3, parseTypeMap);
                        break;
                    case 2:
                        query3 = query3.whereGreaterThanOrEqualTo(string3, parseTypeMap);
                        break;
                    case 3:
                        query3 = query3.whereLessThan(string3, parseTypeMap);
                        break;
                    case 4:
                        query3 = query3.whereLessThanOrEqualTo(string3, parseTypeMap);
                        break;
                    case 5:
                        query3 = query3.whereArrayContains(string3, parseTypeMap);
                        break;
                }
            }
        }
        return query3;
    }

    private Query applyOrders(Query query2) {
        for (Map map : C1350Utils.recursivelyDeconstructReadableArray(this.orders)) {
            String str = (String) map.get("direction");
            Map map2 = (Map) map.get("fieldPath");
            if (((String) map2.get("type")).equals("string")) {
                query2 = query2.orderBy((String) map2.get("string"), Direction.valueOf(str));
            } else {
                List list = (List) map2.get("elements");
                query2 = query2.orderBy(FieldPath.of((String[]) list.toArray(new String[list.size()])), Direction.valueOf(str));
            }
        }
        return query2;
    }

    private Query applyOptions(FirebaseFirestore firebaseFirestore, Query query2) {
        if (this.options.hasKey("endAt")) {
            query2 = query2.endAt(FirestoreSerialize.parseReadableArray(firebaseFirestore, this.options.getArray("endAt")).toArray());
        }
        if (this.options.hasKey("endBefore")) {
            query2 = query2.endBefore(FirestoreSerialize.parseReadableArray(firebaseFirestore, this.options.getArray("endBefore")).toArray());
        }
        if (this.options.hasKey("limit")) {
            query2 = query2.limit((long) this.options.getInt("limit"));
        }
        if (this.options.hasKey("startAfter")) {
            query2 = query2.startAfter(FirestoreSerialize.parseReadableArray(firebaseFirestore, this.options.getArray("startAfter")).toArray());
        }
        return this.options.hasKey("startAt") ? query2.startAt(FirestoreSerialize.parseReadableArray(firebaseFirestore, this.options.getArray("startAt")).toArray()) : query2;
    }

    /* access modifiers changed from: private */
    public void handleQuerySnapshotEvent(final String str, QuerySnapshot querySnapshot) {
        new QuerySnapshotSerializeAsyncTask(this.reactContext, this) {
            /* access modifiers changed from: protected */
            public void onPostExecute(WritableMap writableMap) {
                WritableMap createMap = Arguments.createMap();
                createMap.putString(RNFetchBlobConst.RNFB_RESPONSE_PATH, RNFirebaseFirestoreCollectionReference.this.path);
                createMap.putString("appName", RNFirebaseFirestoreCollectionReference.this.appName);
                createMap.putString("listenerId", str);
                createMap.putMap("querySnapshot", writableMap);
                C1350Utils.sendEvent(RNFirebaseFirestoreCollectionReference.this.reactContext, "firestore_collection_sync_event", createMap);
            }
        }.execute(new Object[]{querySnapshot});
    }

    /* access modifiers changed from: private */
    public void handleQuerySnapshotError(String str, FirebaseFirestoreException firebaseFirestoreException) {
        WritableMap createMap = Arguments.createMap();
        createMap.putString("appName", this.appName);
        createMap.putString(RNFetchBlobConst.RNFB_RESPONSE_PATH, this.path);
        createMap.putString("listenerId", str);
        createMap.putMap("error", RNFirebaseFirestore.getJSError(firebaseFirestoreException));
        C1350Utils.sendEvent(this.reactContext, "firestore_collection_sync_event", createMap);
    }
}

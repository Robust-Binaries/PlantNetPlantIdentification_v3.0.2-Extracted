package p007io.invertase.firebase.database;

import android.os.AsyncTask;
import android.util.Log;
import com.facebook.common.util.UriUtil;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.google.android.gms.measurement.api.AppMeasurementSdk.ConditionalUserProperty;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import p007io.invertase.firebase.C1350Utils;

/* renamed from: io.invertase.firebase.database.RNFirebaseDatabaseReference */
class RNFirebaseDatabaseReference {
    private static final String TAG = "RNFirebaseDBReference";
    private String appName;
    private HashMap<String, ChildEventListener> childEventListeners = new HashMap<>();
    private String dbURL;
    /* access modifiers changed from: private */
    public String key;
    /* access modifiers changed from: private */
    public Query query;
    private HashMap<String, ValueEventListener> valueEventListeners = new HashMap<>();

    /* renamed from: io.invertase.firebase.database.RNFirebaseDatabaseReference$DataSnapshotToMapAsyncTask */
    private static class DataSnapshotToMapAsyncTask extends AsyncTask<Object, Void, WritableMap> {
        private WeakReference<RNFirebaseDatabaseReference> referenceWeakReference;

        /* access modifiers changed from: protected */
        public void onPostExecute(WritableMap writableMap) {
        }

        DataSnapshotToMapAsyncTask(RNFirebaseDatabaseReference rNFirebaseDatabaseReference) {
            this.referenceWeakReference = new WeakReference<>(rNFirebaseDatabaseReference);
        }

        /* access modifiers changed from: protected */
        public final WritableMap doInBackground(Object... objArr) {
            try {
                return RNFirebaseDatabaseUtils.snapshotToMap(objArr[0], objArr[1]);
            } catch (RuntimeException e) {
                if (isAvailable().booleanValue()) {
                    RNFirebaseDatabase.getReactApplicationContextInstance().handleException(e);
                }
                throw e;
            }
        }

        /* access modifiers changed from: 0000 */
        public Boolean isAvailable() {
            return Boolean.valueOf((RNFirebaseDatabase.getReactApplicationContextInstance() == null || this.referenceWeakReference.get() == null) ? false : true);
        }
    }

    RNFirebaseDatabaseReference(String str, String str2, String str3, String str4, ReadableArray readableArray) {
        this.key = str3;
        this.query = null;
        this.appName = str;
        this.dbURL = str2;
        buildDatabaseQueryAtPathAndModifiers(str4, readableArray);
    }

    /* access modifiers changed from: 0000 */
    public void removeAllEventListeners() {
        if (hasListeners().booleanValue()) {
            Iterator it = this.valueEventListeners.entrySet().iterator();
            while (it.hasNext()) {
                this.query.removeEventListener((ValueEventListener) ((Entry) it.next()).getValue());
                it.remove();
            }
            Iterator it2 = this.childEventListeners.entrySet().iterator();
            while (it2.hasNext()) {
                this.query.removeEventListener((ChildEventListener) ((Entry) it2.next()).getValue());
                it2.remove();
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public Query getQuery() {
        return this.query;
    }

    private Boolean hasEventListener(String str) {
        return Boolean.valueOf(this.valueEventListeners.containsKey(str) || this.childEventListeners.containsKey(str));
    }

    /* access modifiers changed from: 0000 */
    public Boolean hasListeners() {
        return Boolean.valueOf(this.valueEventListeners.size() > 0 || this.childEventListeners.size() > 0);
    }

    /* access modifiers changed from: 0000 */
    public void removeEventListener(String str) {
        if (this.valueEventListeners.containsKey(str)) {
            this.query.removeEventListener((ValueEventListener) this.valueEventListeners.get(str));
            this.valueEventListeners.remove(str);
        }
        if (this.childEventListeners.containsKey(str)) {
            this.query.removeEventListener((ChildEventListener) this.childEventListeners.get(str));
            this.childEventListeners.remove(str);
        }
    }

    private void addEventListener(String str, ValueEventListener valueEventListener) {
        this.valueEventListeners.put(str, valueEventListener);
        this.query.addValueEventListener(valueEventListener);
    }

    private void addEventListener(String str, ChildEventListener childEventListener) {
        this.childEventListeners.put(str, childEventListener);
        this.query.addChildEventListener(childEventListener);
    }

    private void addOnceValueEventListener(final Promise promise) {
        final C14101 r0 = new DataSnapshotToMapAsyncTask(this) {
            /* access modifiers changed from: protected */
            public void onPostExecute(WritableMap writableMap) {
                if (isAvailable().booleanValue()) {
                    promise.resolve(writableMap);
                }
            }
        };
        this.query.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(@Nonnull DataSnapshot dataSnapshot) {
                r0.execute(new Object[]{dataSnapshot, null});
            }

            public void onCancelled(@Nonnull DatabaseError databaseError) {
                RNFirebaseDatabase.handlePromise(promise, databaseError);
            }
        });
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Added OnceValueEventListener for key: ");
        sb.append(this.key);
        Log.d(str, sb.toString());
    }

    private void addChildOnceEventListener(final String str, final Promise promise) {
        this.query.addChildEventListener(new ChildEventListener() {
            public void onChildAdded(@Nonnull DataSnapshot dataSnapshot, String str) {
                if ("child_added".equals(str)) {
                    RNFirebaseDatabaseReference.this.query.removeEventListener(this);
                    promise.resolve(RNFirebaseDatabaseUtils.snapshotToMap(dataSnapshot, str));
                }
            }

            public void onChildChanged(@Nonnull DataSnapshot dataSnapshot, String str) {
                if ("child_changed".equals(str)) {
                    RNFirebaseDatabaseReference.this.query.removeEventListener(this);
                    promise.resolve(RNFirebaseDatabaseUtils.snapshotToMap(dataSnapshot, str));
                }
            }

            public void onChildRemoved(@Nonnull DataSnapshot dataSnapshot) {
                if ("child_removed".equals(str)) {
                    RNFirebaseDatabaseReference.this.query.removeEventListener(this);
                    promise.resolve(RNFirebaseDatabaseUtils.snapshotToMap(dataSnapshot, null));
                }
            }

            public void onChildMoved(@Nonnull DataSnapshot dataSnapshot, String str) {
                if ("child_moved".equals(str)) {
                    RNFirebaseDatabaseReference.this.query.removeEventListener(this);
                    promise.resolve(RNFirebaseDatabaseUtils.snapshotToMap(dataSnapshot, str));
                }
            }

            public void onCancelled(@Nonnull DatabaseError databaseError) {
                RNFirebaseDatabaseReference.this.query.removeEventListener(this);
                RNFirebaseDatabase.handlePromise(promise, databaseError);
            }
        });
    }

    /* access modifiers changed from: 0000 */
    /* renamed from: on */
    public void mo20041on(String str, ReadableMap readableMap) {
        if (str.equals("value")) {
            addValueEventListener(readableMap);
        } else {
            addChildEventListener(readableMap, str);
        }
    }

    /* access modifiers changed from: 0000 */
    public void once(String str, Promise promise) {
        if (str.equals("value")) {
            addOnceValueEventListener(promise);
        } else {
            addChildOnceEventListener(str, promise);
        }
    }

    private void addChildEventListener(final ReadableMap readableMap, final String str) {
        final String string = readableMap.getString("eventRegistrationKey");
        readableMap.getString("registrationCancellationKey");
        if (!hasEventListener(string).booleanValue()) {
            addEventListener(string, (ChildEventListener) new ChildEventListener() {
                public void onChildAdded(@Nonnull DataSnapshot dataSnapshot, String str) {
                    if ("child_added".equals(str)) {
                        RNFirebaseDatabaseReference.this.handleDatabaseEvent("child_added", readableMap, dataSnapshot, str);
                    }
                }

                public void onChildChanged(@Nonnull DataSnapshot dataSnapshot, String str) {
                    if ("child_changed".equals(str)) {
                        RNFirebaseDatabaseReference.this.handleDatabaseEvent("child_changed", readableMap, dataSnapshot, str);
                    }
                }

                public void onChildRemoved(@Nonnull DataSnapshot dataSnapshot) {
                    if ("child_removed".equals(str)) {
                        RNFirebaseDatabaseReference.this.handleDatabaseEvent("child_removed", readableMap, dataSnapshot, null);
                    }
                }

                public void onChildMoved(@Nonnull DataSnapshot dataSnapshot, String str) {
                    if ("child_moved".equals(str)) {
                        RNFirebaseDatabaseReference.this.handleDatabaseEvent("child_moved", readableMap, dataSnapshot, str);
                    }
                }

                public void onCancelled(@Nonnull DatabaseError databaseError) {
                    RNFirebaseDatabaseReference.this.removeEventListener(string);
                    RNFirebaseDatabaseReference.this.handleDatabaseError(readableMap, databaseError);
                }
            });
        }
    }

    private void addValueEventListener(final ReadableMap readableMap) {
        final String string = readableMap.getString("eventRegistrationKey");
        if (!hasEventListener(string).booleanValue()) {
            addEventListener(string, (ValueEventListener) new ValueEventListener() {
                public void onDataChange(@Nonnull DataSnapshot dataSnapshot) {
                    RNFirebaseDatabaseReference.this.handleDatabaseEvent("value", readableMap, dataSnapshot, null);
                }

                public void onCancelled(@Nonnull DatabaseError databaseError) {
                    RNFirebaseDatabaseReference.this.removeEventListener(string);
                    RNFirebaseDatabaseReference.this.handleDatabaseError(readableMap, databaseError);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void handleDatabaseEvent(final String str, final ReadableMap readableMap, DataSnapshot dataSnapshot, @Nullable String str2) {
        new DataSnapshotToMapAsyncTask(this) {
            /* access modifiers changed from: protected */
            public void onPostExecute(WritableMap writableMap) {
                if (isAvailable().booleanValue()) {
                    WritableMap createMap = Arguments.createMap();
                    createMap.putMap(UriUtil.DATA_SCHEME, writableMap);
                    createMap.putString("key", RNFirebaseDatabaseReference.this.key);
                    createMap.putString("eventType", str);
                    createMap.putMap("registration", C1350Utils.readableMapToWritableMap(readableMap));
                    C1350Utils.sendEvent(RNFirebaseDatabase.getReactApplicationContextInstance(), "database_sync_event", createMap);
                }
            }
        }.execute(new Object[]{dataSnapshot, str2});
    }

    /* access modifiers changed from: private */
    public void handleDatabaseError(ReadableMap readableMap, DatabaseError databaseError) {
        WritableMap createMap = Arguments.createMap();
        createMap.putString("key", this.key);
        createMap.putMap("error", RNFirebaseDatabase.getJSError(databaseError));
        createMap.putMap("registration", C1350Utils.readableMapToWritableMap(readableMap));
        C1350Utils.sendEvent(RNFirebaseDatabase.getReactApplicationContextInstance(), "database_sync_event", createMap);
    }

    private void buildDatabaseQueryAtPathAndModifiers(String str, ReadableArray readableArray) {
        this.query = RNFirebaseDatabase.getDatabaseForApp(this.appName, this.dbURL).getReference(str);
        for (Map map : C1350Utils.recursivelyDeconstructReadableArray(readableArray)) {
            String str2 = (String) map.get("type");
            String str3 = (String) map.get(ConditionalUserProperty.NAME);
            if ("orderBy".equals(str2)) {
                applyOrderByModifier(str3, str2, map);
            } else if ("limit".equals(str2)) {
                applyLimitModifier(str3, str2, map);
            } else if ("filter".equals(str2)) {
                applyFilterModifier(str3, map);
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x0046  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0057  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0060  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0069  */
    /* JADX WARNING: Removed duplicated region for block: B:30:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void applyOrderByModifier(java.lang.String r2, java.lang.String r3, java.util.Map r4) {
        /*
            r1 = this;
            int r3 = r2.hashCode()
            r0 = -626148087(0xffffffffdaadbd09, float:-2.44515087E16)
            if (r3 == r0) goto L_0x0037
            r0 = 729747418(0x2b7f0fda, float:9.0616197E-13)
            if (r3 == r0) goto L_0x002d
            r0 = 1200288727(0x478af3d7, float:71143.68)
            if (r3 == r0) goto L_0x0023
            r0 = 1217630252(0x4893902c, float:302209.38)
            if (r3 == r0) goto L_0x0019
            goto L_0x0041
        L_0x0019:
            java.lang.String r3 = "orderByValue"
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0041
            r2 = 2
            goto L_0x0042
        L_0x0023:
            java.lang.String r3 = "orderByChild"
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0041
            r2 = 3
            goto L_0x0042
        L_0x002d:
            java.lang.String r3 = "orderByKey"
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0041
            r2 = 0
            goto L_0x0042
        L_0x0037:
            java.lang.String r3 = "orderByPriority"
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0041
            r2 = 1
            goto L_0x0042
        L_0x0041:
            r2 = -1
        L_0x0042:
            switch(r2) {
                case 0: goto L_0x0069;
                case 1: goto L_0x0060;
                case 2: goto L_0x0057;
                case 3: goto L_0x0046;
                default: goto L_0x0045;
            }
        L_0x0045:
            goto L_0x0071
        L_0x0046:
            java.lang.String r2 = "key"
            java.lang.Object r2 = r4.get(r2)
            java.lang.String r2 = (java.lang.String) r2
            com.google.firebase.database.Query r3 = r1.query
            com.google.firebase.database.Query r2 = r3.orderByChild(r2)
            r1.query = r2
            goto L_0x0071
        L_0x0057:
            com.google.firebase.database.Query r2 = r1.query
            com.google.firebase.database.Query r2 = r2.orderByValue()
            r1.query = r2
            goto L_0x0071
        L_0x0060:
            com.google.firebase.database.Query r2 = r1.query
            com.google.firebase.database.Query r2 = r2.orderByPriority()
            r1.query = r2
            goto L_0x0071
        L_0x0069:
            com.google.firebase.database.Query r2 = r1.query
            com.google.firebase.database.Query r2 = r2.orderByKey()
            r1.query = r2
        L_0x0071:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: p007io.invertase.firebase.database.RNFirebaseDatabaseReference.applyOrderByModifier(java.lang.String, java.lang.String, java.util.Map):void");
    }

    private void applyLimitModifier(String str, String str2, Map map) {
        int intValue = ((Double) map.get("limit")).intValue();
        if ("limitToLast".equals(str)) {
            this.query = this.query.limitToLast(intValue);
        } else if ("limitToFirst".equals(str)) {
            this.query = this.query.limitToFirst(intValue);
        }
    }

    private void applyFilterModifier(String str, Map map) {
        String str2 = (String) map.get("valueType");
        String str3 = (String) map.get("key");
        if ("equalTo".equals(str)) {
            applyEqualToFilter(str3, str2, map);
        } else if ("endAt".equals(str)) {
            applyEndAtFilter(str3, str2, map);
        } else if ("startAt".equals(str)) {
            applyStartAtFilter(str3, str2, map);
        }
    }

    private void applyEqualToFilter(String str, String str2, Map map) {
        if ("number".equals(str2)) {
            double doubleValue = ((Double) map.get("value")).doubleValue();
            if (str == null) {
                this.query = this.query.equalTo(doubleValue);
            } else {
                this.query = this.query.equalTo(doubleValue, str);
            }
        } else if ("boolean".equals(str2)) {
            boolean booleanValue = ((Boolean) map.get("value")).booleanValue();
            if (str == null) {
                this.query = this.query.equalTo(booleanValue);
            } else {
                this.query = this.query.equalTo(booleanValue, str);
            }
        } else if ("string".equals(str2)) {
            String str3 = (String) map.get("value");
            if (str == null) {
                this.query = this.query.equalTo(str3);
            } else {
                this.query = this.query.equalTo(str3, str);
            }
        }
    }

    private void applyEndAtFilter(String str, String str2, Map map) {
        if ("number".equals(str2)) {
            double doubleValue = ((Double) map.get("value")).doubleValue();
            if (str == null) {
                this.query = this.query.endAt(doubleValue);
            } else {
                this.query = this.query.endAt(doubleValue, str);
            }
        } else if ("boolean".equals(str2)) {
            boolean booleanValue = ((Boolean) map.get("value")).booleanValue();
            if (str == null) {
                this.query = this.query.endAt(booleanValue);
            } else {
                this.query = this.query.endAt(booleanValue, str);
            }
        } else if ("string".equals(str2)) {
            String str3 = (String) map.get("value");
            if (str == null) {
                this.query = this.query.endAt(str3);
            } else {
                this.query = this.query.endAt(str3, str);
            }
        }
    }

    private void applyStartAtFilter(String str, String str2, Map map) {
        if ("number".equals(str2)) {
            double doubleValue = ((Double) map.get("value")).doubleValue();
            if (str == null) {
                this.query = this.query.startAt(doubleValue);
            } else {
                this.query = this.query.startAt(doubleValue, str);
            }
        } else if ("boolean".equals(str2)) {
            boolean booleanValue = ((Boolean) map.get("value")).booleanValue();
            if (str == null) {
                this.query = this.query.startAt(booleanValue);
            } else {
                this.query = this.query.startAt(booleanValue, str);
            }
        } else if ("string".equals(str2)) {
            String str3 = (String) map.get("value");
            if (str == null) {
                this.query = this.query.startAt(str3);
            } else {
                this.query = this.query.startAt(str3, str);
            }
        }
    }
}

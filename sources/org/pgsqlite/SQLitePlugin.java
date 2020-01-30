package org.pgsqlite;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Base64;
import com.RNFetchBlob.RNFetchBlobConst;
import com.facebook.common.logging.FLog;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.google.android.gms.measurement.api.AppMeasurementSdk.ConditionalUserProperty;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SQLitePlugin extends ReactContextBaseJavaModule {
    private static final Pattern FIRST_WORD = Pattern.compile("^\\s*(\\S+)", 2);
    private static final String PLUGIN_NAME = "SQLite";
    public static final String TAG = "SQLitePlugin";
    static ConcurrentHashMap<String, DBRunner> dbrmap = new ConcurrentHashMap<>();
    protected Context context = null;
    protected ExecutorService threadPool;

    private enum Action {
        open,
        close,
        attach,
        delete,
        executeSqlBatch,
        backgroundExecuteSqlBatch,
        echoStringValue
    }

    private final class DBQuery {
        final CallbackContext cbc;
        final boolean close;
        final boolean delete;
        final String[] queries;
        final String[] queryIDs;
        final ReadableArray[] queryParams;
        final boolean stop;

        DBQuery(String[] strArr, String[] strArr2, ReadableArray[] readableArrayArr, CallbackContext callbackContext) {
            this.stop = false;
            this.close = false;
            this.delete = false;
            this.queries = strArr;
            this.queryIDs = strArr2;
            this.queryParams = readableArrayArr;
            this.cbc = callbackContext;
        }

        DBQuery(boolean z, CallbackContext callbackContext) {
            this.stop = true;
            this.close = true;
            this.delete = z;
            this.queries = null;
            this.queryIDs = null;
            this.queryParams = null;
            this.cbc = callbackContext;
        }

        DBQuery() {
            this.stop = true;
            this.close = false;
            this.delete = false;
            this.queries = null;
            this.queryIDs = null;
            this.queryParams = null;
            this.cbc = null;
        }
    }

    private class DBRunner implements Runnable {
        private boolean androidLockWorkaround;
        private String assetFilename;
        final String dbname;
        SQLiteDatabase mydb;
        final CallbackContext openCbc;
        final int openFlags;

        /* renamed from: q */
        final BlockingQueue<DBQuery> f183q;

        DBRunner(String str, ReadableMap readableMap, CallbackContext callbackContext) {
            this.dbname = str;
            int i = 268435456;
            try {
                this.assetFilename = SQLitePluginConverter.getString(readableMap, "assetFilename", (String) null);
                if (this.assetFilename != null && this.assetFilename.length() > 0 && SQLitePluginConverter.getBoolean(readableMap, "readOnly", false)) {
                    i = 1;
                }
            } catch (Exception e) {
                FLog.m49e(SQLitePlugin.TAG, "Error retrieving assetFilename or mode from options:", (Throwable) e);
            }
            this.openFlags = i;
            this.androidLockWorkaround = SQLitePluginConverter.getBoolean(readableMap, "androidLockWorkaround", false);
            if (this.androidLockWorkaround) {
                FLog.m60i(SQLitePlugin.TAG, "Android db closing/locking workaround applied");
            }
            this.f183q = new LinkedBlockingQueue();
            this.openCbc = callbackContext;
        }

        /* JADX WARNING: Removed duplicated region for block: B:27:0x0087 A[Catch:{ Exception -> 0x00c8 }] */
        /* JADX WARNING: Removed duplicated region for block: B:28:0x008f A[SYNTHETIC, Splitter:B:28:0x008f] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
                r10 = this;
                org.pgsqlite.SQLitePlugin r0 = org.pgsqlite.SQLitePlugin.this     // Catch:{ SQLiteException -> 0x0113, Exception -> 0x00eb }
                java.lang.String r1 = r10.dbname     // Catch:{ SQLiteException -> 0x0113, Exception -> 0x00eb }
                java.lang.String r2 = r10.assetFilename     // Catch:{ SQLiteException -> 0x0113, Exception -> 0x00eb }
                int r3 = r10.openFlags     // Catch:{ SQLiteException -> 0x0113, Exception -> 0x00eb }
                org.pgsqlite.CallbackContext r4 = r10.openCbc     // Catch:{ SQLiteException -> 0x0113, Exception -> 0x00eb }
                android.database.sqlite.SQLiteDatabase r0 = r0.openDatabase(r1, r2, r3, r4)     // Catch:{ SQLiteException -> 0x0113, Exception -> 0x00eb }
                r10.mydb = r0     // Catch:{ SQLiteException -> 0x0113, Exception -> 0x00eb }
                r0 = 0
                java.util.concurrent.BlockingQueue<org.pgsqlite.SQLitePlugin$DBQuery> r1 = r10.f183q     // Catch:{ Exception -> 0x0064 }
                java.lang.Object r1 = r1.take()     // Catch:{ Exception -> 0x0064 }
                org.pgsqlite.SQLitePlugin$DBQuery r1 = (org.pgsqlite.SQLitePlugin.DBQuery) r1     // Catch:{ Exception -> 0x0064 }
            L_0x0019:
                boolean r2 = r1.stop     // Catch:{ Exception -> 0x0062 }
                if (r2 != 0) goto L_0x006f
                org.pgsqlite.SQLitePlugin r3 = org.pgsqlite.SQLitePlugin.this     // Catch:{ Exception -> 0x0062 }
                java.lang.String r4 = r10.dbname     // Catch:{ Exception -> 0x0062 }
                java.lang.String[] r5 = r1.queries     // Catch:{ Exception -> 0x0062 }
                com.facebook.react.bridge.ReadableArray[] r6 = r1.queryParams     // Catch:{ Exception -> 0x0062 }
                java.lang.String[] r7 = r1.queryIDs     // Catch:{ Exception -> 0x0062 }
                org.pgsqlite.CallbackContext r8 = r1.cbc     // Catch:{ Exception -> 0x0062 }
                r3.executeSqlBatch(r4, r5, r6, r7, r8)     // Catch:{ Exception -> 0x0062 }
                boolean r2 = r10.androidLockWorkaround     // Catch:{ Exception -> 0x0062 }
                if (r2 == 0) goto L_0x0058
                java.lang.String[] r2 = r1.queries     // Catch:{ Exception -> 0x0062 }
                int r2 = r2.length     // Catch:{ Exception -> 0x0062 }
                r3 = 1
                if (r2 != r3) goto L_0x0058
                java.lang.String[] r2 = r1.queries     // Catch:{ Exception -> 0x0062 }
                r3 = 0
                r2 = r2[r3]     // Catch:{ Exception -> 0x0062 }
                java.lang.String r3 = "COMMIT"
                boolean r2 = r2.equals(r3)     // Catch:{ Exception -> 0x0062 }
                if (r2 == 0) goto L_0x0058
                org.pgsqlite.SQLitePlugin r2 = org.pgsqlite.SQLitePlugin.this     // Catch:{ Exception -> 0x0062 }
                java.lang.String r3 = r10.dbname     // Catch:{ Exception -> 0x0062 }
                r2.closeDatabaseNow(r3)     // Catch:{ Exception -> 0x0062 }
                org.pgsqlite.SQLitePlugin r2 = org.pgsqlite.SQLitePlugin.this     // Catch:{ Exception -> 0x0062 }
                java.lang.String r3 = r10.dbname     // Catch:{ Exception -> 0x0062 }
                java.lang.String r4 = ""
                int r5 = r10.openFlags     // Catch:{ Exception -> 0x0062 }
                android.database.sqlite.SQLiteDatabase r2 = r2.openDatabase(r3, r4, r5, r0)     // Catch:{ Exception -> 0x0062 }
                r10.mydb = r2     // Catch:{ Exception -> 0x0062 }
            L_0x0058:
                java.util.concurrent.BlockingQueue<org.pgsqlite.SQLitePlugin$DBQuery> r2 = r10.f183q     // Catch:{ Exception -> 0x0062 }
                java.lang.Object r2 = r2.take()     // Catch:{ Exception -> 0x0062 }
                org.pgsqlite.SQLitePlugin$DBQuery r2 = (org.pgsqlite.SQLitePlugin.DBQuery) r2     // Catch:{ Exception -> 0x0062 }
                r1 = r2
                goto L_0x0019
            L_0x0062:
                r0 = move-exception
                goto L_0x0068
            L_0x0064:
                r1 = move-exception
                r9 = r1
                r1 = r0
                r0 = r9
            L_0x0068:
                java.lang.String r2 = org.pgsqlite.SQLitePlugin.TAG
                java.lang.String r3 = "unexpected error"
                com.facebook.common.logging.FLog.m49e(r2, r3, r0)
            L_0x006f:
                if (r1 == 0) goto L_0x00ea
                boolean r0 = r1.close
                if (r0 == 0) goto L_0x00ea
                org.pgsqlite.SQLitePlugin r0 = org.pgsqlite.SQLitePlugin.this     // Catch:{ Exception -> 0x00c8 }
                java.lang.String r2 = r10.dbname     // Catch:{ Exception -> 0x00c8 }
                r0.closeDatabaseNow(r2)     // Catch:{ Exception -> 0x00c8 }
                java.util.concurrent.ConcurrentHashMap<java.lang.String, org.pgsqlite.SQLitePlugin$DBRunner> r0 = org.pgsqlite.SQLitePlugin.dbrmap     // Catch:{ Exception -> 0x00c8 }
                java.lang.String r2 = r10.dbname     // Catch:{ Exception -> 0x00c8 }
                r0.remove(r2)     // Catch:{ Exception -> 0x00c8 }
                boolean r0 = r1.delete     // Catch:{ Exception -> 0x00c8 }
                if (r0 != 0) goto L_0x008f
                org.pgsqlite.CallbackContext r0 = r1.cbc     // Catch:{ Exception -> 0x00c8 }
                java.lang.String r2 = "database removed"
                r0.success(r2)     // Catch:{ Exception -> 0x00c8 }
                goto L_0x00ea
            L_0x008f:
                org.pgsqlite.SQLitePlugin r0 = org.pgsqlite.SQLitePlugin.this     // Catch:{ Exception -> 0x00a9 }
                java.lang.String r2 = r10.dbname     // Catch:{ Exception -> 0x00a9 }
                boolean r0 = r0.deleteDatabaseNow(r2)     // Catch:{ Exception -> 0x00a9 }
                if (r0 == 0) goto L_0x00a1
                org.pgsqlite.CallbackContext r0 = r1.cbc     // Catch:{ Exception -> 0x00a9 }
                java.lang.String r2 = "database removed"
                r0.success(r2)     // Catch:{ Exception -> 0x00a9 }
                goto L_0x00ea
            L_0x00a1:
                org.pgsqlite.CallbackContext r0 = r1.cbc     // Catch:{ Exception -> 0x00a9 }
                java.lang.String r2 = "couldn't delete database"
                r0.error(r2)     // Catch:{ Exception -> 0x00a9 }
                goto L_0x00ea
            L_0x00a9:
                r0 = move-exception
                java.lang.String r2 = org.pgsqlite.SQLitePlugin.TAG     // Catch:{ Exception -> 0x00c8 }
                java.lang.String r3 = "couldn't delete database"
                com.facebook.common.logging.FLog.m49e(r2, r3, r0)     // Catch:{ Exception -> 0x00c8 }
                org.pgsqlite.CallbackContext r2 = r1.cbc     // Catch:{ Exception -> 0x00c8 }
                java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00c8 }
                r3.<init>()     // Catch:{ Exception -> 0x00c8 }
                java.lang.String r4 = "couldn't delete database: "
                r3.append(r4)     // Catch:{ Exception -> 0x00c8 }
                r3.append(r0)     // Catch:{ Exception -> 0x00c8 }
                java.lang.String r0 = r3.toString()     // Catch:{ Exception -> 0x00c8 }
                r2.error(r0)     // Catch:{ Exception -> 0x00c8 }
                goto L_0x00ea
            L_0x00c8:
                r0 = move-exception
                java.lang.String r2 = org.pgsqlite.SQLitePlugin.TAG
                java.lang.String r3 = "couldn't close database"
                com.facebook.common.logging.FLog.m49e(r2, r3, r0)
                org.pgsqlite.CallbackContext r2 = r1.cbc
                if (r2 == 0) goto L_0x00ea
                org.pgsqlite.CallbackContext r1 = r1.cbc
                java.lang.StringBuilder r2 = new java.lang.StringBuilder
                r2.<init>()
                java.lang.String r3 = "couldn't close database: "
                r2.append(r3)
                r2.append(r0)
                java.lang.String r0 = r2.toString()
                r1.error(r0)
            L_0x00ea:
                return
            L_0x00eb:
                r0 = move-exception
                java.lang.String r1 = org.pgsqlite.SQLitePlugin.TAG
                java.lang.String r2 = "Unexpected error opening database, stopping db thread"
                com.facebook.common.logging.FLog.m49e(r1, r2, r0)
                org.pgsqlite.CallbackContext r1 = r10.openCbc
                if (r1 == 0) goto L_0x010b
                java.lang.StringBuilder r2 = new java.lang.StringBuilder
                r2.<init>()
                java.lang.String r3 = "Can't open database."
                r2.append(r3)
                r2.append(r0)
                java.lang.String r0 = r2.toString()
                r1.error(r0)
            L_0x010b:
                java.util.concurrent.ConcurrentHashMap<java.lang.String, org.pgsqlite.SQLitePlugin$DBRunner> r0 = org.pgsqlite.SQLitePlugin.dbrmap
                java.lang.String r1 = r10.dbname
                r0.remove(r1)
                return
            L_0x0113:
                r0 = move-exception
                java.lang.String r1 = org.pgsqlite.SQLitePlugin.TAG
                java.lang.String r2 = "SQLite error opening database, stopping db thread"
                com.facebook.common.logging.FLog.m49e(r1, r2, r0)
                org.pgsqlite.CallbackContext r1 = r10.openCbc
                if (r1 == 0) goto L_0x0133
                java.lang.StringBuilder r2 = new java.lang.StringBuilder
                r2.<init>()
                java.lang.String r3 = "Can't open database."
                r2.append(r3)
                r2.append(r0)
                java.lang.String r0 = r2.toString()
                r1.error(r0)
            L_0x0133:
                java.util.concurrent.ConcurrentHashMap<java.lang.String, org.pgsqlite.SQLitePlugin$DBRunner> r0 = org.pgsqlite.SQLitePlugin.dbrmap
                java.lang.String r1 = r10.dbname
                r0.remove(r1)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: org.pgsqlite.SQLitePlugin.DBRunner.run():void");
        }
    }

    private enum QueryType {
        update,
        insert,
        delete,
        select,
        begin,
        commit,
        rollback,
        other
    }

    public String getName() {
        return PLUGIN_NAME;
    }

    public SQLitePlugin(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
        this.context = reactApplicationContext.getApplicationContext();
        this.threadPool = Executors.newCachedThreadPool();
    }

    @ReactMethod
    public void open(ReadableMap readableMap, Callback callback, Callback callback2) {
        try {
            execute("open", readableMap, new CallbackContext(callback, callback2));
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Unexpected error:");
            sb.append(e.getMessage());
            callback2.invoke(sb.toString());
        }
    }

    @ReactMethod
    public void close(ReadableMap readableMap, Callback callback, Callback callback2) {
        try {
            execute("close", readableMap, new CallbackContext(callback, callback2));
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Unexpected error");
            sb.append(e.getMessage());
            callback2.invoke(sb.toString());
        }
    }

    @ReactMethod
    public void attach(ReadableMap readableMap, Callback callback, Callback callback2) {
        try {
            execute("attach", readableMap, new CallbackContext(callback, callback2));
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Unexpected error");
            sb.append(e.getMessage());
            callback2.invoke(sb.toString());
        }
    }

    @ReactMethod
    public void delete(ReadableMap readableMap, Callback callback, Callback callback2) {
        try {
            execute("delete", readableMap, new CallbackContext(callback, callback2));
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Unexpected error");
            sb.append(e.getMessage());
            callback2.invoke(sb.toString());
        }
    }

    @ReactMethod
    public void backgroundExecuteSqlBatch(ReadableMap readableMap, Callback callback, Callback callback2) {
        try {
            execute("backgroundExecuteSqlBatch", readableMap, new CallbackContext(callback, callback2));
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Unexpected error");
            sb.append(e.getMessage());
            callback2.invoke(sb.toString());
        }
    }

    @ReactMethod
    public void executeSqlBatch(ReadableMap readableMap, Callback callback, Callback callback2) {
        try {
            execute("executeSqlBatch", readableMap, new CallbackContext(callback, callback2));
        } catch (Exception unused) {
            callback2.invoke("Unexpected error");
        }
    }

    @ReactMethod
    public void echoStringValue(ReadableMap readableMap, Callback callback, Callback callback2) {
        try {
            execute("echoStringValue", readableMap, new CallbackContext(callback, callback2));
        } catch (Exception unused) {
            callback2.invoke("Unexpected error");
        }
    }

    /* access modifiers changed from: protected */
    public ExecutorService getThreadPool() {
        return this.threadPool;
    }

    /* access modifiers changed from: protected */
    public Context getContext() {
        return this.context;
    }

    /* access modifiers changed from: protected */
    public boolean execute(String str, ReadableMap readableMap, CallbackContext callbackContext) throws Exception {
        try {
            try {
                return executeAndPossiblyThrow(Action.valueOf(str), readableMap, callbackContext);
            } catch (Exception e) {
                FLog.m49e(TAG, "unexpected error", (Throwable) e);
                callbackContext.error("Unexpected error executing processing SQLite query");
                throw e;
            }
        } catch (IllegalArgumentException e2) {
            FLog.m49e(TAG, "unexpected error", (Throwable) e2);
            callbackContext.error("Unexpected error executing processing SQLite query");
            throw e2;
        }
    }

    private boolean executeAndPossiblyThrow(Action action, ReadableMap readableMap, CallbackContext callbackContext) {
        ReadableArray[] readableArrayArr;
        String[] strArr;
        String[] strArr2;
        switch (action) {
            case echoStringValue:
                callbackContext.success(SQLitePluginConverter.getString(readableMap, "value", ""));
                break;
            case open:
                startDatabase(SQLitePluginConverter.getString(readableMap, ConditionalUserProperty.NAME, ""), readableMap, callbackContext);
                break;
            case close:
                closeDatabase(SQLitePluginConverter.getString(readableMap, RNFetchBlobConst.RNFB_RESPONSE_PATH, ""), callbackContext);
                break;
            case attach:
                attachDatabase(SQLitePluginConverter.getString(readableMap, RNFetchBlobConst.RNFB_RESPONSE_PATH, ""), SQLitePluginConverter.getString(readableMap, "dbName", ""), SQLitePluginConverter.getString(readableMap, "dbAlias", ""), callbackContext);
                break;
            case delete:
                deleteDatabase(SQLitePluginConverter.getString(readableMap, RNFetchBlobConst.RNFB_RESPONSE_PATH, ""), callbackContext);
                break;
            case executeSqlBatch:
            case backgroundExecuteSqlBatch:
                String string = SQLitePluginConverter.getString((ReadableMap) SQLitePluginConverter.get(readableMap, "dbargs", (Object) null), "dbname", "");
                ReadableArray readableArray = (ReadableArray) SQLitePluginConverter.get(readableMap, "executes", (Object) null);
                if (readableArray.isNull(0)) {
                    strArr2 = new String[0];
                    strArr = null;
                    readableArrayArr = null;
                } else {
                    int size = readableArray.size();
                    String[] strArr3 = new String[size];
                    String[] strArr4 = new String[size];
                    ReadableArray[] readableArrayArr2 = new ReadableArray[size];
                    for (int i = 0; i < size; i++) {
                        ReadableMap readableMap2 = (ReadableMap) SQLitePluginConverter.get(readableArray, i, (Object) null);
                        strArr3[i] = SQLitePluginConverter.getString(readableMap2, "sql", "");
                        strArr4[i] = SQLitePluginConverter.getString(readableMap2, "qid", "");
                        readableArrayArr2[i] = (ReadableArray) SQLitePluginConverter.get(readableMap2, "params", (Object) null);
                    }
                    strArr2 = strArr3;
                    strArr = strArr4;
                    readableArrayArr = readableArrayArr2;
                }
                DBQuery dBQuery = new DBQuery(strArr2, strArr, readableArrayArr, callbackContext);
                DBRunner dBRunner = (DBRunner) dbrmap.get(string);
                if (dBRunner == null) {
                    callbackContext.error("database not open");
                    break;
                } else {
                    try {
                        dBRunner.f183q.put(dBQuery);
                        break;
                    } catch (Exception e) {
                        FLog.m49e(TAG, "couldn't add to queue", (Throwable) e);
                        callbackContext.error("couldn't add to queue");
                        break;
                    }
                }
        }
        return true;
    }

    public void closeAllOpenDatabases() {
        while (!dbrmap.isEmpty()) {
            String str = (String) dbrmap.keySet().iterator().next();
            closeDatabaseNow(str);
            try {
                ((DBRunner) dbrmap.get(str)).f183q.put(new DBQuery());
            } catch (Exception e) {
                String str2 = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("couldn't stop db thread for db: ");
                sb.append(str);
                FLog.m49e(str2, sb.toString(), (Throwable) e);
            }
            dbrmap.remove(str);
        }
    }

    private void startDatabase(String str, ReadableMap readableMap, CallbackContext callbackContext) {
        if (((DBRunner) dbrmap.get(str)) != null) {
            callbackContext.success("database started");
            return;
        }
        DBRunner dBRunner = new DBRunner(str, readableMap, callbackContext);
        dbrmap.put(str, dBRunner);
        getThreadPool().execute(dBRunner);
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Can't wrap try/catch for region: R(3:26|27|28) */
    /* JADX WARNING: Can't wrap try/catch for region: R(3:43|44|45) */
    /* JADX WARNING: Can't wrap try/catch for region: R(4:55|56|62|63) */
    /* JADX WARNING: Code restructure failed: missing block: B:27:?, code lost:
        r1 = TAG;
        r5 = new java.lang.StringBuilder();
        r5.append("pre-populated DB asset NOT FOUND in app bundle www subdirectory: ");
        r5.append(r11);
        com.facebook.common.logging.FLog.m48e(r1, r5.toString());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x007a, code lost:
        r5 = null;
        r1 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:?, code lost:
        r1 = TAG;
        r5 = new java.lang.StringBuilder();
        r5.append("pre-populated DB asset NOT FOUND in app bundle www subdirectory: ");
        r5.append(r11);
        com.facebook.common.logging.FLog.m48e(r1, r5.toString());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x00d4, code lost:
        r5 = null;
        r1 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:74:0x015c, code lost:
        r10 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:76:?, code lost:
        com.facebook.common.logging.FLog.m49e(TAG, "Error importing pre-populated DB asset", (java.lang.Throwable) r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:77:0x016b, code lost:
        throw new java.lang.Exception("Error importing pre-populated DB asset");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:88:0x01b5, code lost:
        r10 = th;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:26:0x0064 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:43:0x00be */
    /* JADX WARNING: Missing exception handler attribute for start block: B:62:0x011f */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:26:0x0064=Splitter:B:26:0x0064, B:43:0x00be=Splitter:B:43:0x00be, B:62:0x011f=Splitter:B:62:0x011f} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.database.sqlite.SQLiteDatabase openDatabase(java.lang.String r10, java.lang.String r11, int r12, org.pgsqlite.CallbackContext r13) throws java.lang.Exception {
        /*
            r9 = this;
            r0 = 0
            android.database.sqlite.SQLiteDatabase r1 = r9.getDatabase(r10)     // Catch:{ all -> 0x01b7 }
            if (r1 == 0) goto L_0x0016
            boolean r1 = r1.isOpen()     // Catch:{ all -> 0x01b7 }
            if (r1 != 0) goto L_0x000e
            goto L_0x0016
        L_0x000e:
            java.lang.Exception r10 = new java.lang.Exception     // Catch:{ all -> 0x01b7 }
            java.lang.String r11 = "Database already open"
            r10.<init>(r11)     // Catch:{ all -> 0x01b7 }
            throw r10     // Catch:{ all -> 0x01b7 }
        L_0x0016:
            r1 = 0
            r2 = 1
            if (r11 == 0) goto L_0x0022
            int r3 = r11.length()     // Catch:{ all -> 0x01b7 }
            if (r3 <= 0) goto L_0x0022
            r3 = 1
            goto L_0x0023
        L_0x0022:
            r3 = 0
        L_0x0023:
            if (r3 == 0) goto L_0x0137
            java.lang.String r4 = "1"
            int r4 = r11.compareTo(r4)     // Catch:{ all -> 0x01b7 }
            if (r4 != 0) goto L_0x007e
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ all -> 0x01b7 }
            r11.<init>()     // Catch:{ all -> 0x01b7 }
            java.lang.String r4 = "www/"
            r11.append(r4)     // Catch:{ all -> 0x01b7 }
            r11.append(r10)     // Catch:{ all -> 0x01b7 }
            java.lang.String r11 = r11.toString()     // Catch:{ all -> 0x01b7 }
            android.content.Context r4 = r9.getContext()     // Catch:{ Exception -> 0x0063 }
            android.content.res.AssetManager r4 = r4.getAssets()     // Catch:{ Exception -> 0x0063 }
            java.io.InputStream r4 = r4.open(r11)     // Catch:{ Exception -> 0x0063 }
            java.lang.String r5 = TAG     // Catch:{ Exception -> 0x0064 }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0064 }
            r6.<init>()     // Catch:{ Exception -> 0x0064 }
            java.lang.String r7 = "Pre-populated DB asset FOUND  in app bundle www subdirectory: "
            r6.append(r7)     // Catch:{ Exception -> 0x0064 }
            r6.append(r11)     // Catch:{ Exception -> 0x0064 }
            java.lang.String r6 = r6.toString()     // Catch:{ Exception -> 0x0064 }
            com.facebook.common.logging.FLog.m76v(r5, r6)     // Catch:{ Exception -> 0x0064 }
            r5 = r0
            goto L_0x0139
        L_0x0063:
            r4 = r0
        L_0x0064:
            java.lang.String r1 = TAG     // Catch:{ all -> 0x01b5 }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x01b5 }
            r5.<init>()     // Catch:{ all -> 0x01b5 }
            java.lang.String r6 = "pre-populated DB asset NOT FOUND in app bundle www subdirectory: "
            r5.append(r6)     // Catch:{ all -> 0x01b5 }
            r5.append(r11)     // Catch:{ all -> 0x01b5 }
            java.lang.String r11 = r5.toString()     // Catch:{ all -> 0x01b5 }
            com.facebook.common.logging.FLog.m48e(r1, r11)     // Catch:{ all -> 0x01b5 }
            r5 = r0
            r1 = 1
            goto L_0x0139
        L_0x007e:
            char r4 = r11.charAt(r1)     // Catch:{ all -> 0x01b7 }
            r5 = 126(0x7e, float:1.77E-43)
            if (r4 != r5) goto L_0x00d7
            java.lang.String r4 = "~/"
            boolean r4 = r11.startsWith(r4)     // Catch:{ all -> 0x01b7 }
            if (r4 == 0) goto L_0x0094
            r4 = 2
            java.lang.String r11 = r11.substring(r4)     // Catch:{ all -> 0x01b7 }
            goto L_0x0098
        L_0x0094:
            java.lang.String r11 = r11.substring(r2)     // Catch:{ all -> 0x01b7 }
        L_0x0098:
            android.content.Context r4 = r9.getContext()     // Catch:{ Exception -> 0x00bd }
            android.content.res.AssetManager r4 = r4.getAssets()     // Catch:{ Exception -> 0x00bd }
            java.io.InputStream r4 = r4.open(r11)     // Catch:{ Exception -> 0x00bd }
            java.lang.String r5 = TAG     // Catch:{ Exception -> 0x00be }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00be }
            r6.<init>()     // Catch:{ Exception -> 0x00be }
            java.lang.String r7 = "Pre-populated DB asset FOUND in app bundle subdirectory: "
            r6.append(r7)     // Catch:{ Exception -> 0x00be }
            r6.append(r11)     // Catch:{ Exception -> 0x00be }
            java.lang.String r6 = r6.toString()     // Catch:{ Exception -> 0x00be }
            com.facebook.common.logging.FLog.m76v(r5, r6)     // Catch:{ Exception -> 0x00be }
            r5 = r0
            goto L_0x0139
        L_0x00bd:
            r4 = r0
        L_0x00be:
            java.lang.String r1 = TAG     // Catch:{ all -> 0x01b5 }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x01b5 }
            r5.<init>()     // Catch:{ all -> 0x01b5 }
            java.lang.String r6 = "pre-populated DB asset NOT FOUND in app bundle www subdirectory: "
            r5.append(r6)     // Catch:{ all -> 0x01b5 }
            r5.append(r11)     // Catch:{ all -> 0x01b5 }
            java.lang.String r11 = r5.toString()     // Catch:{ all -> 0x01b5 }
            com.facebook.common.logging.FLog.m48e(r1, r11)     // Catch:{ all -> 0x01b5 }
            r5 = r0
            r1 = 1
            goto L_0x0139
        L_0x00d7:
            android.content.Context r4 = r9.getContext()     // Catch:{ all -> 0x01b7 }
            java.io.File r4 = r4.getFilesDir()     // Catch:{ all -> 0x01b7 }
            java.lang.String r5 = "/"
            boolean r5 = r11.startsWith(r5)     // Catch:{ all -> 0x01b7 }
            if (r5 == 0) goto L_0x00eb
            java.lang.String r11 = r11.substring(r2)     // Catch:{ all -> 0x01b7 }
        L_0x00eb:
            java.io.File r5 = new java.io.File     // Catch:{ Exception -> 0x011d }
            r5.<init>(r4, r11)     // Catch:{ Exception -> 0x011d }
            java.io.FileInputStream r4 = new java.io.FileInputStream     // Catch:{ Exception -> 0x011d }
            r4.<init>(r5)     // Catch:{ Exception -> 0x011d }
            java.lang.String r6 = TAG     // Catch:{ Exception -> 0x011b }
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x011b }
            r7.<init>()     // Catch:{ Exception -> 0x011b }
            java.lang.String r8 = "Pre-populated DB asset FOUND in Files subdirectory: "
            r7.append(r8)     // Catch:{ Exception -> 0x011b }
            java.lang.String r8 = r5.getCanonicalPath()     // Catch:{ Exception -> 0x011b }
            r7.append(r8)     // Catch:{ Exception -> 0x011b }
            java.lang.String r7 = r7.toString()     // Catch:{ Exception -> 0x011b }
            com.facebook.common.logging.FLog.m76v(r6, r7)     // Catch:{ Exception -> 0x011b }
            if (r12 != r2) goto L_0x0119
            java.lang.String r6 = TAG     // Catch:{ Exception -> 0x011f }
            java.lang.String r7 = "Detected read-only mode request for external asset."
            com.facebook.common.logging.FLog.m76v(r6, r7)     // Catch:{ Exception -> 0x011f }
            goto L_0x0139
        L_0x0119:
            r5 = r0
            goto L_0x0139
        L_0x011b:
            r5 = r0
            goto L_0x011f
        L_0x011d:
            r4 = r0
            r5 = r4
        L_0x011f:
            java.lang.String r1 = TAG     // Catch:{ all -> 0x01b5 }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ all -> 0x01b5 }
            r6.<init>()     // Catch:{ all -> 0x01b5 }
            java.lang.String r7 = "Error opening pre-populated DB asset in app bundle www subdirectory: "
            r6.append(r7)     // Catch:{ all -> 0x01b5 }
            r6.append(r11)     // Catch:{ all -> 0x01b5 }
            java.lang.String r11 = r6.toString()     // Catch:{ all -> 0x01b5 }
            com.facebook.common.logging.FLog.m48e(r1, r11)     // Catch:{ all -> 0x01b5 }
            r1 = 1
            goto L_0x0139
        L_0x0137:
            r4 = r0
            r5 = r4
        L_0x0139:
            if (r5 != 0) goto L_0x0188
            r12 = 268435456(0x10000000, float:2.5243549E-29)
            android.content.Context r11 = r9.getContext()     // Catch:{ all -> 0x01b5 }
            java.io.File r5 = r11.getDatabasePath(r10)     // Catch:{ all -> 0x01b5 }
            boolean r11 = r5.exists()     // Catch:{ all -> 0x01b5 }
            if (r11 != 0) goto L_0x017b
            if (r3 == 0) goto L_0x017b
            if (r1 != 0) goto L_0x016c
            if (r4 == 0) goto L_0x016c
            java.lang.String r11 = TAG     // Catch:{ all -> 0x01b5 }
            java.lang.String r1 = "Copying pre-populated db asset to destination"
            com.facebook.common.logging.FLog.m76v(r11, r1)     // Catch:{ all -> 0x01b5 }
            r9.createFromAssets(r10, r5, r4)     // Catch:{ Exception -> 0x015c }
            goto L_0x017b
        L_0x015c:
            r10 = move-exception
            java.lang.String r11 = TAG     // Catch:{ all -> 0x01b5 }
            java.lang.String r12 = "Error importing pre-populated DB asset"
            com.facebook.common.logging.FLog.m49e(r11, r12, r10)     // Catch:{ all -> 0x01b5 }
            java.lang.Exception r10 = new java.lang.Exception     // Catch:{ all -> 0x01b5 }
            java.lang.String r11 = "Error importing pre-populated DB asset"
            r10.<init>(r11)     // Catch:{ all -> 0x01b5 }
            throw r10     // Catch:{ all -> 0x01b5 }
        L_0x016c:
            java.lang.String r10 = TAG     // Catch:{ all -> 0x01b5 }
            java.lang.String r11 = "Unable to import pre-populated db asset"
            com.facebook.common.logging.FLog.m48e(r10, r11)     // Catch:{ all -> 0x01b5 }
            java.lang.Exception r10 = new java.lang.Exception     // Catch:{ all -> 0x01b5 }
            java.lang.String r11 = "Unable to import pre-populated db asset"
            r10.<init>(r11)     // Catch:{ all -> 0x01b5 }
            throw r10     // Catch:{ all -> 0x01b5 }
        L_0x017b:
            boolean r10 = r5.exists()     // Catch:{ all -> 0x01b5 }
            if (r10 != 0) goto L_0x0188
            java.io.File r10 = r5.getParentFile()     // Catch:{ all -> 0x01b5 }
            r10.mkdirs()     // Catch:{ all -> 0x01b5 }
        L_0x0188:
            java.lang.String r10 = TAG     // Catch:{ all -> 0x01b5 }
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ all -> 0x01b5 }
            r11.<init>()     // Catch:{ all -> 0x01b5 }
            java.lang.String r1 = "DB file is ready, proceeding to OPEN SQLite DB: "
            r11.append(r1)     // Catch:{ all -> 0x01b5 }
            java.lang.String r1 = r5.getAbsolutePath()     // Catch:{ all -> 0x01b5 }
            r11.append(r1)     // Catch:{ all -> 0x01b5 }
            java.lang.String r11 = r11.toString()     // Catch:{ all -> 0x01b5 }
            com.facebook.common.logging.FLog.m76v(r10, r11)     // Catch:{ all -> 0x01b5 }
            java.lang.String r10 = r5.getAbsolutePath()     // Catch:{ all -> 0x01b5 }
            android.database.sqlite.SQLiteDatabase r10 = android.database.sqlite.SQLiteDatabase.openDatabase(r10, r0, r12)     // Catch:{ all -> 0x01b5 }
            if (r13 == 0) goto L_0x01b1
            java.lang.String r11 = "Database opened"
            r13.success(r11)     // Catch:{ all -> 0x01b5 }
        L_0x01b1:
            r9.closeQuietly(r4)
            return r10
        L_0x01b5:
            r10 = move-exception
            goto L_0x01b9
        L_0x01b7:
            r10 = move-exception
            r4 = r0
        L_0x01b9:
            r9.closeQuietly(r4)
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: org.pgsqlite.SQLitePlugin.openDatabase(java.lang.String, java.lang.String, int, org.pgsqlite.CallbackContext):android.database.sqlite.SQLiteDatabase");
    }

    private void createFromAssets(String str, File file, InputStream inputStream) throws Exception {
        FileOutputStream fileOutputStream = null;
        try {
            FLog.m76v(TAG, "Copying pre-populated DB content");
            String absolutePath = file.getAbsolutePath();
            String substring = absolutePath.substring(0, absolutePath.lastIndexOf("/") + 1);
            File file2 = new File(substring);
            if (!file2.exists()) {
                file2.mkdirs();
            }
            StringBuilder sb = new StringBuilder();
            sb.append(substring);
            sb.append(str);
            File file3 = new File(sb.toString());
            FileOutputStream fileOutputStream2 = new FileOutputStream(file3);
            try {
                byte[] bArr = new byte[1024];
                while (true) {
                    int read = inputStream.read(bArr);
                    if (read > 0) {
                        fileOutputStream2.write(bArr, 0, read);
                    } else {
                        String str2 = TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Copied pre-populated DB asset to: ");
                        sb2.append(file3.getAbsolutePath());
                        FLog.m76v(str2, sb2.toString());
                        closeQuietly(fileOutputStream2);
                        return;
                    }
                }
            } catch (Throwable th) {
                fileOutputStream = fileOutputStream2;
                th = th;
                closeQuietly(fileOutputStream);
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            closeQuietly(fileOutputStream);
            throw th;
        }
    }

    private void closeDatabase(String str, CallbackContext callbackContext) {
        DBRunner dBRunner = (DBRunner) dbrmap.get(str);
        if (dBRunner != null) {
            try {
                dBRunner.f183q.put(new DBQuery(false, callbackContext));
            } catch (Exception e) {
                if (callbackContext != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("couldn't close database");
                    sb.append(e);
                    callbackContext.error(sb.toString());
                }
                FLog.m49e(TAG, "couldn't close database", (Throwable) e);
            }
        } else if (callbackContext != null) {
            callbackContext.success("database closed");
        }
    }

    /* access modifiers changed from: private */
    public void closeDatabaseNow(String str) {
        SQLiteDatabase database = getDatabase(str);
        if (database != null) {
            database.close();
        }
    }

    private void attachDatabase(String str, String str2, String str3, CallbackContext callbackContext) {
        DBRunner dBRunner = (DBRunner) dbrmap.get(str);
        if (dBRunner != null) {
            String absolutePath = getContext().getDatabasePath(str2).getAbsolutePath();
            StringBuilder sb = new StringBuilder();
            sb.append("ATTACH DATABASE '");
            sb.append(absolutePath);
            sb.append("' AS ");
            sb.append(str3);
            DBQuery dBQuery = new DBQuery(new String[]{sb.toString()}, new String[]{"1111"}, null, callbackContext);
            try {
                dBRunner.f183q.put(dBQuery);
            } catch (InterruptedException unused) {
                callbackContext.error("Can't put query in the queue. Interrupted.");
            }
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Database ");
            sb2.append(str);
            sb2.append("i s not created yet");
            callbackContext.error(sb2.toString());
        }
    }

    private void deleteDatabase(String str, CallbackContext callbackContext) {
        DBRunner dBRunner = (DBRunner) dbrmap.get(str);
        if (dBRunner != null) {
            try {
                dBRunner.f183q.put(new DBQuery(true, callbackContext));
            } catch (Exception e) {
                if (callbackContext != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("couldn't close database");
                    sb.append(e);
                    callbackContext.error(sb.toString());
                }
                FLog.m49e(TAG, "couldn't close database", (Throwable) e);
            }
        } else if (deleteDatabaseNow(str)) {
            callbackContext.success("database deleted");
        } else {
            callbackContext.error("couldn't delete database");
        }
    }

    /* access modifiers changed from: private */
    @SuppressLint({"NewApi"})
    public boolean deleteDatabaseNow(String str) {
        return SQLiteDatabase.deleteDatabase(getContext().getDatabasePath(str));
    }

    private SQLiteDatabase getDatabase(String str) {
        DBRunner dBRunner = (DBRunner) dbrmap.get(str);
        if (dBRunner == null) {
            return null;
        }
        return dBRunner.mydb;
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* JADX WARNING: Removed duplicated region for block: B:120:0x015f A[Catch:{ Exception -> 0x0181 }] */
    /* JADX WARNING: Removed duplicated region for block: B:126:0x016b A[Catch:{ Exception -> 0x0169 }] */
    /* JADX WARNING: Removed duplicated region for block: B:129:0x0170 A[Catch:{ Exception -> 0x0169 }] */
    /* JADX WARNING: Removed duplicated region for block: B:144:0x0193  */
    /* JADX WARNING: Removed duplicated region for block: B:145:0x01ac  */
    @android.annotation.SuppressLint({"NewApi"})
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void executeSqlBatch(java.lang.String r20, java.lang.String[] r21, com.facebook.react.bridge.ReadableArray[] r22, java.lang.String[] r23, org.pgsqlite.CallbackContext r24) {
        /*
            r19 = this;
            r1 = r19
            r2 = r21
            r3 = r24
            android.database.sqlite.SQLiteDatabase r4 = r19.getDatabase(r20)
            if (r4 != 0) goto L_0x0012
            java.lang.String r0 = "database has been closed"
            r3.error(r0)
            return
        L_0x0012:
            int r5 = r2.length
            com.facebook.react.bridge.WritableArray r6 = com.facebook.react.bridge.Arguments.createArray()
            r8 = 0
        L_0x0018:
            if (r8 >= r5) goto L_0x01d1
            r9 = r23[r8]
            java.lang.String r0 = "unknown"
            r11 = r2[r8]     // Catch:{ Exception -> 0x0183 }
            org.pgsqlite.SQLitePlugin$QueryType r12 = r1.getQueryType(r11)     // Catch:{ Exception -> 0x0183 }
            org.pgsqlite.SQLitePlugin$QueryType r13 = org.pgsqlite.SQLitePlugin.QueryType.update     // Catch:{ Exception -> 0x0183 }
            r14 = 1
            if (r12 == r13) goto L_0x012f
            org.pgsqlite.SQLitePlugin$QueryType r13 = org.pgsqlite.SQLitePlugin.QueryType.delete     // Catch:{ Exception -> 0x0183 }
            if (r12 != r13) goto L_0x002f
            goto L_0x012f
        L_0x002f:
            org.pgsqlite.SQLitePlugin$QueryType r13 = org.pgsqlite.SQLitePlugin.QueryType.insert     // Catch:{ Exception -> 0x0183 }
            if (r12 != r13) goto L_0x0099
            if (r22 == 0) goto L_0x0099
            java.lang.String r12 = "executeSqlBatch"
            java.lang.String r13 = "INSERT"
            com.facebook.common.logging.FLog.m36d(r12, r13)     // Catch:{ Exception -> 0x0094 }
            android.database.sqlite.SQLiteStatement r12 = r4.compileStatement(r11)     // Catch:{ Exception -> 0x0094 }
            r13 = r22[r8]     // Catch:{ Exception -> 0x0094 }
            r1.bindArgsToStatement(r12, r13)     // Catch:{ Exception -> 0x0094 }
            r13 = r11
            long r10 = r12.executeInsert()     // Catch:{ SQLiteException -> 0x0077, all -> 0x0074 }
            com.facebook.react.bridge.WritableMap r15 = com.facebook.react.bridge.Arguments.createMap()     // Catch:{ SQLiteException -> 0x0077, all -> 0x0074 }
            r16 = -1
            int r18 = (r10 > r16 ? 1 : (r10 == r16 ? 0 : -1))
            if (r18 == 0) goto L_0x0060
            java.lang.String r7 = "insertId"
            double r10 = (double) r10     // Catch:{ SQLiteException -> 0x0071, all -> 0x006e }
            r15.putDouble(r7, r10)     // Catch:{ SQLiteException -> 0x0071, all -> 0x006e }
            java.lang.String r7 = "rowsAffected"
            r15.putInt(r7, r14)     // Catch:{ SQLiteException -> 0x0071, all -> 0x006e }
            goto L_0x0066
        L_0x0060:
            java.lang.String r7 = "rowsAffected"
            r10 = 0
            r15.putInt(r7, r10)     // Catch:{ SQLiteException -> 0x0071, all -> 0x006e }
        L_0x0066:
            r1.closeQuietly(r12)     // Catch:{ Exception -> 0x006b }
            r10 = r15
            goto L_0x0088
        L_0x006b:
            r0 = move-exception
            r7 = r15
            goto L_0x0096
        L_0x006e:
            r0 = move-exception
            r10 = r15
            goto L_0x0090
        L_0x0071:
            r0 = move-exception
            r10 = r15
            goto L_0x0079
        L_0x0074:
            r0 = move-exception
            r10 = 0
            goto L_0x0090
        L_0x0077:
            r0 = move-exception
            r10 = 0
        L_0x0079:
            java.lang.String r7 = r0.getMessage()     // Catch:{ all -> 0x008f }
            java.lang.String r11 = TAG     // Catch:{ all -> 0x008f }
            java.lang.String r14 = "SQLiteDatabase.executeInsert() failed"
            com.facebook.common.logging.FLog.m49e(r11, r14, r0)     // Catch:{ all -> 0x008f }
            r1.closeQuietly(r12)     // Catch:{ Exception -> 0x00c7 }
            r0 = r7
        L_0x0088:
            r7 = r10
            r10 = r13
            r11 = 0
            r16 = 0
            goto L_0x016e
        L_0x008f:
            r0 = move-exception
        L_0x0090:
            r1.closeQuietly(r12)     // Catch:{ Exception -> 0x00c7 }
            throw r0     // Catch:{ Exception -> 0x00c7 }
        L_0x0094:
            r0 = move-exception
            r7 = 0
        L_0x0096:
            r11 = 0
            goto L_0x0186
        L_0x0099:
            r13 = r11
            org.pgsqlite.SQLitePlugin$QueryType r7 = org.pgsqlite.SQLitePlugin.QueryType.begin     // Catch:{ Exception -> 0x0183 }
            if (r12 != r7) goto L_0x00ca
            r4.beginTransaction()     // Catch:{ SQLiteException -> 0x00b8 }
            com.facebook.react.bridge.WritableMap r10 = com.facebook.react.bridge.Arguments.createMap()     // Catch:{ SQLiteException -> 0x00b8 }
            java.lang.String r7 = "rowsAffected"
            r11 = 0
            r10.putInt(r7, r11)     // Catch:{ Exception -> 0x00b2 }
        L_0x00ab:
            r7 = r10
            r10 = r13
            r11 = 0
            r16 = 0
            goto L_0x016e
        L_0x00b2:
            r0 = move-exception
        L_0x00b3:
            r7 = r10
            goto L_0x0186
        L_0x00b6:
            r0 = move-exception
            goto L_0x00ba
        L_0x00b8:
            r0 = move-exception
            r10 = 0
        L_0x00ba:
            java.lang.String r7 = r0.getMessage()     // Catch:{ Exception -> 0x00c7 }
            java.lang.String r11 = TAG     // Catch:{ Exception -> 0x00c7 }
            java.lang.String r12 = "SQLiteDatabase.beginTransaction() failed"
            com.facebook.common.logging.FLog.m49e(r11, r12, r0)     // Catch:{ Exception -> 0x00c7 }
            r0 = r7
            goto L_0x00ab
        L_0x00c7:
            r0 = move-exception
            r7 = r10
            goto L_0x0096
        L_0x00ca:
            org.pgsqlite.SQLitePlugin$QueryType r7 = org.pgsqlite.SQLitePlugin.QueryType.commit     // Catch:{ Exception -> 0x0183 }
            if (r12 != r7) goto L_0x00f6
            r4.setTransactionSuccessful()     // Catch:{ SQLiteException -> 0x00e7 }
            r4.endTransaction()     // Catch:{ SQLiteException -> 0x00e7 }
            com.facebook.react.bridge.WritableMap r10 = com.facebook.react.bridge.Arguments.createMap()     // Catch:{ SQLiteException -> 0x00e7 }
            java.lang.String r7 = "rowsAffected"
            r11 = 0
            r10.putInt(r7, r11)     // Catch:{  }
        L_0x00de:
            r7 = r10
            r10 = r13
            r11 = 0
            r16 = 0
            goto L_0x016e
        L_0x00e5:
            r0 = move-exception
            goto L_0x00e9
        L_0x00e7:
            r0 = move-exception
            r10 = 0
        L_0x00e9:
            java.lang.String r7 = r0.getMessage()     // Catch:{ Exception -> 0x00c7 }
            java.lang.String r11 = TAG     // Catch:{ Exception -> 0x00c7 }
            java.lang.String r12 = "SQLiteDatabase.setTransactionSuccessful/endTransaction() failed"
            com.facebook.common.logging.FLog.m49e(r11, r12, r0)     // Catch:{ Exception -> 0x00c7 }
            r0 = r7
            goto L_0x00de
        L_0x00f6:
            org.pgsqlite.SQLitePlugin$QueryType r7 = org.pgsqlite.SQLitePlugin.QueryType.rollback     // Catch:{ Exception -> 0x0183 }
            if (r12 != r7) goto L_0x0129
            r4.endTransaction()     // Catch:{ SQLiteException -> 0x0115 }
            com.facebook.react.bridge.WritableMap r10 = com.facebook.react.bridge.Arguments.createMap()     // Catch:{ SQLiteException -> 0x0115 }
            java.lang.String r7 = "rowsAffected"
            r11 = 0
            r10.putInt(r7, r11)     // Catch:{ SQLiteException -> 0x010d }
            r7 = r10
            r10 = r13
            r16 = 0
            goto L_0x016e
        L_0x010d:
            r0 = move-exception
            goto L_0x0118
        L_0x010f:
            r0 = move-exception
            r11 = 0
            goto L_0x00b3
        L_0x0112:
            r0 = move-exception
            r11 = 0
            goto L_0x0118
        L_0x0115:
            r0 = move-exception
            r11 = 0
            r10 = 0
        L_0x0118:
            java.lang.String r7 = r0.getMessage()     // Catch:{  }
            java.lang.String r12 = TAG     // Catch:{  }
            java.lang.String r14 = "SQLiteDatabase.endTransaction() failed"
            com.facebook.common.logging.FLog.m49e(r12, r14, r0)     // Catch:{  }
            r0 = r7
            r7 = r10
            r10 = r13
            r16 = 0
            goto L_0x016e
        L_0x0129:
            r11 = 0
            r10 = r13
            r7 = 0
            r16 = 1
            goto L_0x016e
        L_0x012f:
            r13 = r11
            r11 = 0
            r7 = -1
            r10 = r13
            android.database.sqlite.SQLiteStatement r12 = r4.compileStatement(r10)     // Catch:{ SQLiteException -> 0x014b, all -> 0x0148 }
            if (r22 == 0) goto L_0x013e
            r13 = r22[r8]     // Catch:{ SQLiteException -> 0x0146 }
            r1.bindArgsToStatement(r12, r13)     // Catch:{ SQLiteException -> 0x0146 }
        L_0x013e:
            int r13 = r12.executeUpdateDelete()     // Catch:{ SQLiteException -> 0x0146 }
            r1.closeQuietly(r12)     // Catch:{ Exception -> 0x0181 }
            goto L_0x015d
        L_0x0146:
            r0 = move-exception
            goto L_0x014d
        L_0x0148:
            r0 = move-exception
            r12 = 0
            goto L_0x017d
        L_0x014b:
            r0 = move-exception
            r12 = 0
        L_0x014d:
            java.lang.String r13 = r0.getMessage()     // Catch:{ all -> 0x017c }
            java.lang.String r14 = TAG     // Catch:{ all -> 0x017c }
            java.lang.String r15 = "SQLiteStatement.executeUpdateDelete() failed"
            com.facebook.common.logging.FLog.m49e(r14, r15, r0)     // Catch:{ all -> 0x017c }
            r1.closeQuietly(r12)     // Catch:{ Exception -> 0x0181 }
            r0 = r13
            r13 = -1
        L_0x015d:
            if (r13 == r7) goto L_0x016b
            com.facebook.react.bridge.WritableMap r7 = com.facebook.react.bridge.Arguments.createMap()     // Catch:{ Exception -> 0x0181 }
            java.lang.String r12 = "rowsAffected"
            r7.putInt(r12, r13)     // Catch:{ Exception -> 0x0169 }
            goto L_0x016c
        L_0x0169:
            r0 = move-exception
            goto L_0x0186
        L_0x016b:
            r7 = 0
        L_0x016c:
            r16 = 0
        L_0x016e:
            if (r16 == 0) goto L_0x017a
            if (r22 == 0) goto L_0x0175
            r12 = r22[r8]     // Catch:{ Exception -> 0x0169 }
            goto L_0x0176
        L_0x0175:
            r12 = 0
        L_0x0176:
            com.facebook.react.bridge.WritableMap r7 = r1.executeSqlStatementQuery(r4, r10, r12, r3)     // Catch:{ Exception -> 0x0169 }
        L_0x017a:
            r10 = r0
            goto L_0x0191
        L_0x017c:
            r0 = move-exception
        L_0x017d:
            r1.closeQuietly(r12)     // Catch:{ Exception -> 0x0181 }
            throw r0     // Catch:{ Exception -> 0x0181 }
        L_0x0181:
            r0 = move-exception
            goto L_0x0185
        L_0x0183:
            r0 = move-exception
            r11 = 0
        L_0x0185:
            r7 = 0
        L_0x0186:
            java.lang.String r10 = r0.getMessage()
            java.lang.String r12 = TAG
            java.lang.String r13 = "SQLitePlugin.executeSql[Batch](): failed"
            com.facebook.common.logging.FLog.m49e(r12, r13, r0)
        L_0x0191:
            if (r7 == 0) goto L_0x01ac
            com.facebook.react.bridge.WritableMap r0 = com.facebook.react.bridge.Arguments.createMap()
            java.lang.String r10 = "qid"
            r0.putString(r10, r9)
            java.lang.String r9 = "type"
            java.lang.String r10 = "success"
            r0.putString(r9, r10)
            java.lang.String r9 = "result"
            r0.putMap(r9, r7)
            r6.pushMap(r0)
            goto L_0x01cd
        L_0x01ac:
            com.facebook.react.bridge.WritableMap r0 = com.facebook.react.bridge.Arguments.createMap()
            java.lang.String r7 = "qid"
            r0.putString(r7, r9)
            java.lang.String r7 = "type"
            java.lang.String r9 = "error"
            r0.putString(r7, r9)
            com.facebook.react.bridge.WritableMap r7 = com.facebook.react.bridge.Arguments.createMap()
            java.lang.String r9 = "message"
            r7.putString(r9, r10)
            java.lang.String r9 = "result"
            r0.putMap(r9, r7)
            r6.pushMap(r0)
        L_0x01cd:
            int r8 = r8 + 1
            goto L_0x0018
        L_0x01d1:
            r3.success(r6)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.pgsqlite.SQLitePlugin.executeSqlBatch(java.lang.String, java.lang.String[], com.facebook.react.bridge.ReadableArray[], java.lang.String[], org.pgsqlite.CallbackContext):void");
    }

    private QueryType getQueryType(String str) {
        Matcher matcher = FIRST_WORD.matcher(str);
        if (matcher.find()) {
            try {
                return QueryType.valueOf(matcher.group(1).toLowerCase(Locale.US));
            } catch (IllegalArgumentException unused) {
            }
        }
        return QueryType.other;
    }

    private void bindArgsToStatement(SQLiteStatement sQLiteStatement, ReadableArray readableArray) {
        for (int i = 0; i < readableArray.size(); i++) {
            if (readableArray.getType(i) == ReadableType.Number) {
                double d = readableArray.getDouble(i);
                long j = (long) d;
                if (d == ((double) j)) {
                    sQLiteStatement.bindLong(i + 1, j);
                } else {
                    sQLiteStatement.bindDouble(i + 1, d);
                }
            } else if (readableArray.isNull(i)) {
                sQLiteStatement.bindNull(i + 1);
            } else {
                sQLiteStatement.bindString(i + 1, SQLitePluginConverter.getString(readableArray, i, ""));
            }
        }
    }

    private WritableMap executeSqlStatementQuery(SQLiteDatabase sQLiteDatabase, String str, ReadableArray readableArray, CallbackContext callbackContext) throws Exception {
        WritableMap createMap = Arguments.createMap();
        Cursor cursor = null;
        try {
            String[] strArr = new String[0];
            if (readableArray != null) {
                int size = readableArray.size();
                String[] strArr2 = new String[size];
                for (int i = 0; i < size; i++) {
                    if (readableArray.isNull(i)) {
                        strArr2[i] = "";
                    } else {
                        strArr2[i] = SQLitePluginConverter.getString(readableArray, i, "");
                    }
                }
                strArr = strArr2;
            }
            cursor = sQLiteDatabase.rawQuery(str, strArr);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    WritableArray createArray = Arguments.createArray();
                    int columnCount = cursor.getColumnCount();
                    do {
                        WritableMap createMap2 = Arguments.createMap();
                        for (int i2 = 0; i2 < columnCount; i2++) {
                            bindRow(createMap2, cursor.getColumnName(i2), cursor, i2);
                        }
                        createArray.pushMap(createMap2);
                    } while (cursor.moveToNext());
                    createMap.putArray("rows", createArray);
                }
            }
            closeQuietly(cursor);
            return createMap;
        } catch (Exception e) {
            FLog.m49e(TAG, "SQLitePlugin.executeSql[Batch]() failed", (Throwable) e);
            throw e;
        } catch (Throwable th) {
            closeQuietly(cursor);
            throw th;
        }
    }

    @SuppressLint({"NewApi"})
    private void bindRow(WritableMap writableMap, String str, Cursor cursor, int i) {
        int type = cursor.getType(i);
        if (type != 4) {
            switch (type) {
                case 0:
                    writableMap.putNull(str);
                    return;
                case 1:
                    writableMap.putDouble(str, (double) cursor.getLong(i));
                    return;
                case 2:
                    writableMap.putDouble(str, cursor.getDouble(i));
                    return;
                default:
                    writableMap.putString(str, cursor.getString(i));
                    return;
            }
        } else {
            writableMap.putString(str, new String(Base64.encode(cursor.getBlob(i), 0)));
        }
    }

    private void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException unused) {
            }
        }
    }
}

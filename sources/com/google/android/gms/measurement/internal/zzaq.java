package com.google.android.gms.measurement.internal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.WorkerThread;
import com.google.android.gms.common.util.Clock;
import com.google.android.gms.common.util.VisibleForTesting;

public final class zzaq extends zzf {
    private final zzar zzjj = new zzar(this, getContext(), "google_app_measurement_local.db");
    private boolean zzjk;

    zzaq(zzby zzby) {
        super(zzby);
    }

    /* access modifiers changed from: protected */
    public final boolean zzak() {
        return false;
    }

    @WorkerThread
    public final void resetAnalyticsData() {
        zzo();
        zzq();
        try {
            int delete = getWritableDatabase().delete("messages", null, null) + 0;
            if (delete > 0) {
                zzad().zzdi().zza("Reset local analytics data. records", Integer.valueOf(delete));
            }
        } catch (SQLiteException e) {
            zzad().zzda().zza("Error resetting local analytics data. error", e);
        }
    }

    /* JADX WARNING: type inference failed for: r2v0 */
    /* JADX WARNING: type inference failed for: r2v1, types: [int, boolean] */
    /* JADX WARNING: type inference failed for: r7v0 */
    /* JADX WARNING: type inference failed for: r12v0, types: [android.database.Cursor] */
    /* JADX WARNING: type inference failed for: r9v0, types: [android.database.sqlite.SQLiteDatabase] */
    /* JADX WARNING: type inference failed for: r9v1 */
    /* JADX WARNING: type inference failed for: r7v1 */
    /* JADX WARNING: type inference failed for: r12v1 */
    /* JADX WARNING: type inference failed for: r2v4 */
    /* JADX WARNING: type inference failed for: r9v2, types: [android.database.sqlite.SQLiteDatabase] */
    /* JADX WARNING: type inference failed for: r7v2, types: [android.database.Cursor] */
    /* JADX WARNING: type inference failed for: r9v3 */
    /* JADX WARNING: type inference failed for: r9v4, types: [android.database.sqlite.SQLiteDatabase] */
    /* JADX WARNING: type inference failed for: r7v3, types: [android.database.Cursor] */
    /* JADX WARNING: type inference failed for: r9v5 */
    /* JADX WARNING: type inference failed for: r12v2, types: [android.database.Cursor] */
    /* JADX WARNING: type inference failed for: r7v4, types: [android.database.sqlite.SQLiteDatabase] */
    /* JADX WARNING: type inference failed for: r9v6 */
    /* JADX WARNING: type inference failed for: r12v3 */
    /* JADX WARNING: type inference failed for: r9v7 */
    /* JADX WARNING: type inference failed for: r12v4 */
    /* JADX WARNING: type inference failed for: r9v8, types: [android.database.sqlite.SQLiteDatabase] */
    /* JADX WARNING: type inference failed for: r12v5 */
    /* JADX WARNING: type inference failed for: r7v5 */
    /* JADX WARNING: type inference failed for: r12v6 */
    /* JADX WARNING: type inference failed for: r12v7, types: [android.database.Cursor] */
    /* JADX WARNING: type inference failed for: r7v6 */
    /* JADX WARNING: type inference failed for: r7v7 */
    /* JADX WARNING: type inference failed for: r2v11 */
    /* JADX WARNING: type inference failed for: r7v8 */
    /* JADX WARNING: type inference failed for: r7v9 */
    /* JADX WARNING: type inference failed for: r7v10 */
    /* JADX WARNING: type inference failed for: r7v11 */
    /* JADX WARNING: type inference failed for: r7v12 */
    /* JADX WARNING: type inference failed for: r9v9 */
    /* JADX WARNING: type inference failed for: r2v12 */
    /* JADX WARNING: type inference failed for: r9v10 */
    /* JADX WARNING: type inference failed for: r9v11 */
    /* JADX WARNING: type inference failed for: r7v13 */
    /* JADX WARNING: type inference failed for: r7v14 */
    /* JADX WARNING: type inference failed for: r9v12 */
    /* JADX WARNING: type inference failed for: r9v13 */
    /* JADX WARNING: type inference failed for: r7v15 */
    /* JADX WARNING: type inference failed for: r7v16 */
    /* JADX WARNING: type inference failed for: r12v8 */
    /* JADX WARNING: type inference failed for: r9v14 */
    /* JADX WARNING: type inference failed for: r9v15 */
    /* JADX WARNING: type inference failed for: r9v16 */
    /* JADX WARNING: type inference failed for: r9v17 */
    /* JADX WARNING: type inference failed for: r9v18 */
    /* JADX WARNING: type inference failed for: r12v9 */
    /* JADX WARNING: type inference failed for: r12v10 */
    /* JADX WARNING: type inference failed for: r12v11 */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r2v1, types: [int, boolean]
      assigns: []
      uses: [?[int, short, byte, char], int, boolean]
      mth insns count: 161
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x00c6 A[SYNTHETIC, Splitter:B:49:0x00c6] */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x00e0  */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x00e5  */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x00f5  */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x00fa  */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x0111  */
    /* JADX WARNING: Removed duplicated region for block: B:78:0x0116  */
    /* JADX WARNING: Removed duplicated region for block: B:83:0x0123  */
    /* JADX WARNING: Removed duplicated region for block: B:85:0x0128  */
    /* JADX WARNING: Removed duplicated region for block: B:93:0x0119 A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:95:0x0119 A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:97:0x0119 A[SYNTHETIC] */
    /* JADX WARNING: Unknown variable types count: 19 */
    @android.support.annotation.WorkerThread
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final boolean zza(int r17, byte[] r18) {
        /*
            r16 = this;
            r1 = r16
            r16.zzo()
            r16.zzq()
            boolean r0 = r1.zzjk
            r2 = 0
            if (r0 == 0) goto L_0x000e
            return r2
        L_0x000e:
            android.content.ContentValues r3 = new android.content.ContentValues
            r3.<init>()
            java.lang.String r0 = "type"
            java.lang.Integer r4 = java.lang.Integer.valueOf(r17)
            r3.put(r0, r4)
            java.lang.String r0 = "entry"
            r4 = r18
            r3.put(r0, r4)
            r4 = 5
            r5 = 0
            r6 = 5
        L_0x0026:
            if (r5 >= r4) goto L_0x012c
            r7 = 0
            r8 = 1
            android.database.sqlite.SQLiteDatabase r9 = r16.getWritableDatabase()     // Catch:{ SQLiteFullException -> 0x00fe, SQLiteDatabaseLockedException -> 0x00ec, SQLiteException -> 0x00c2, all -> 0x00be }
            if (r9 != 0) goto L_0x0038
            r1.zzjk = r8     // Catch:{ SQLiteFullException -> 0x00bc, SQLiteDatabaseLockedException -> 0x00ed, SQLiteException -> 0x00b8 }
            if (r9 == 0) goto L_0x0037
            r9.close()
        L_0x0037:
            return r2
        L_0x0038:
            r9.beginTransaction()     // Catch:{ SQLiteFullException -> 0x00bc, SQLiteDatabaseLockedException -> 0x00ed, SQLiteException -> 0x00b8 }
            r10 = 0
            java.lang.String r0 = "select count(1) from messages"
            android.database.Cursor r12 = r9.rawQuery(r0, r7)     // Catch:{ SQLiteFullException -> 0x00bc, SQLiteDatabaseLockedException -> 0x00ed, SQLiteException -> 0x00b8 }
            if (r12 == 0) goto L_0x0059
            boolean r0 = r12.moveToFirst()     // Catch:{ SQLiteFullException -> 0x0055, SQLiteDatabaseLockedException -> 0x00b6, SQLiteException -> 0x0053, all -> 0x0050 }
            if (r0 == 0) goto L_0x0059
            long r10 = r12.getLong(r2)     // Catch:{ SQLiteFullException -> 0x0055, SQLiteDatabaseLockedException -> 0x00b6, SQLiteException -> 0x0053, all -> 0x0050 }
            goto L_0x0059
        L_0x0050:
            r0 = move-exception
            goto L_0x0121
        L_0x0053:
            r0 = move-exception
            goto L_0x00ba
        L_0x0055:
            r0 = move-exception
            r7 = r12
            goto L_0x0100
        L_0x0059:
            r13 = 100000(0x186a0, double:4.94066E-319)
            int r0 = (r10 > r13 ? 1 : (r10 == r13 ? 0 : -1))
            if (r0 < 0) goto L_0x00a0
            com.google.android.gms.measurement.internal.zzau r0 = r16.zzad()     // Catch:{ SQLiteFullException -> 0x0055, SQLiteDatabaseLockedException -> 0x00b6, SQLiteException -> 0x0053, all -> 0x0050 }
            com.google.android.gms.measurement.internal.zzaw r0 = r0.zzda()     // Catch:{ SQLiteFullException -> 0x0055, SQLiteDatabaseLockedException -> 0x00b6, SQLiteException -> 0x0053, all -> 0x0050 }
            java.lang.String r15 = "Data loss, local db full"
            r0.zzaq(r15)     // Catch:{ SQLiteFullException -> 0x0055, SQLiteDatabaseLockedException -> 0x00b6, SQLiteException -> 0x0053, all -> 0x0050 }
            long r13 = r13 - r10
            r10 = 1
            long r13 = r13 + r10
            java.lang.String r0 = "messages"
            java.lang.String r10 = "rowid in (select rowid from messages order by rowid asc limit ?)"
            java.lang.String[] r11 = new java.lang.String[r8]     // Catch:{ SQLiteFullException -> 0x0055, SQLiteDatabaseLockedException -> 0x00b6, SQLiteException -> 0x0053, all -> 0x0050 }
            java.lang.String r15 = java.lang.Long.toString(r13)     // Catch:{ SQLiteFullException -> 0x0055, SQLiteDatabaseLockedException -> 0x00b6, SQLiteException -> 0x0053, all -> 0x0050 }
            r11[r2] = r15     // Catch:{ SQLiteFullException -> 0x0055, SQLiteDatabaseLockedException -> 0x00b6, SQLiteException -> 0x0053, all -> 0x0050 }
            int r0 = r9.delete(r0, r10, r11)     // Catch:{ SQLiteFullException -> 0x0055, SQLiteDatabaseLockedException -> 0x00b6, SQLiteException -> 0x0053, all -> 0x0050 }
            long r10 = (long) r0     // Catch:{ SQLiteFullException -> 0x0055, SQLiteDatabaseLockedException -> 0x00b6, SQLiteException -> 0x0053, all -> 0x0050 }
            int r0 = (r10 > r13 ? 1 : (r10 == r13 ? 0 : -1))
            if (r0 == 0) goto L_0x00a0
            com.google.android.gms.measurement.internal.zzau r0 = r16.zzad()     // Catch:{ SQLiteFullException -> 0x0055, SQLiteDatabaseLockedException -> 0x00b6, SQLiteException -> 0x0053, all -> 0x0050 }
            com.google.android.gms.measurement.internal.zzaw r0 = r0.zzda()     // Catch:{ SQLiteFullException -> 0x0055, SQLiteDatabaseLockedException -> 0x00b6, SQLiteException -> 0x0053, all -> 0x0050 }
            java.lang.String r15 = "Different delete count than expected in local db. expected, received, difference"
            java.lang.Long r4 = java.lang.Long.valueOf(r13)     // Catch:{ SQLiteFullException -> 0x0055, SQLiteDatabaseLockedException -> 0x00b6, SQLiteException -> 0x0053, all -> 0x0050 }
            java.lang.Long r2 = java.lang.Long.valueOf(r10)     // Catch:{ SQLiteFullException -> 0x0055, SQLiteDatabaseLockedException -> 0x00b6, SQLiteException -> 0x0053, all -> 0x0050 }
            long r13 = r13 - r10
            java.lang.Long r10 = java.lang.Long.valueOf(r13)     // Catch:{ SQLiteFullException -> 0x0055, SQLiteDatabaseLockedException -> 0x00b6, SQLiteException -> 0x0053, all -> 0x0050 }
            r0.zza(r15, r4, r2, r10)     // Catch:{ SQLiteFullException -> 0x0055, SQLiteDatabaseLockedException -> 0x00b6, SQLiteException -> 0x0053, all -> 0x0050 }
        L_0x00a0:
            java.lang.String r0 = "messages"
            r9.insertOrThrow(r0, r7, r3)     // Catch:{ SQLiteFullException -> 0x0055, SQLiteDatabaseLockedException -> 0x00b6, SQLiteException -> 0x0053, all -> 0x0050 }
            r9.setTransactionSuccessful()     // Catch:{ SQLiteFullException -> 0x0055, SQLiteDatabaseLockedException -> 0x00b6, SQLiteException -> 0x0053, all -> 0x0050 }
            r9.endTransaction()     // Catch:{ SQLiteFullException -> 0x0055, SQLiteDatabaseLockedException -> 0x00b6, SQLiteException -> 0x0053, all -> 0x0050 }
            if (r12 == 0) goto L_0x00b0
            r12.close()
        L_0x00b0:
            if (r9 == 0) goto L_0x00b5
            r9.close()
        L_0x00b5:
            return r8
        L_0x00b6:
            r7 = r12
            goto L_0x00ed
        L_0x00b8:
            r0 = move-exception
            r12 = r7
        L_0x00ba:
            r7 = r9
            goto L_0x00c4
        L_0x00bc:
            r0 = move-exception
            goto L_0x0100
        L_0x00be:
            r0 = move-exception
            r9 = r7
            r12 = r9
            goto L_0x0121
        L_0x00c2:
            r0 = move-exception
            r12 = r7
        L_0x00c4:
            if (r7 == 0) goto L_0x00cf
            boolean r2 = r7.inTransaction()     // Catch:{ all -> 0x00e9 }
            if (r2 == 0) goto L_0x00cf
            r7.endTransaction()     // Catch:{ all -> 0x00e9 }
        L_0x00cf:
            com.google.android.gms.measurement.internal.zzau r2 = r16.zzad()     // Catch:{ all -> 0x00e9 }
            com.google.android.gms.measurement.internal.zzaw r2 = r2.zzda()     // Catch:{ all -> 0x00e9 }
            java.lang.String r4 = "Error writing entry to local database"
            r2.zza(r4, r0)     // Catch:{ all -> 0x00e9 }
            r1.zzjk = r8     // Catch:{ all -> 0x00e9 }
            if (r12 == 0) goto L_0x00e3
            r12.close()
        L_0x00e3:
            if (r7 == 0) goto L_0x0119
            r7.close()
            goto L_0x0119
        L_0x00e9:
            r0 = move-exception
            r9 = r7
            goto L_0x0121
        L_0x00ec:
            r9 = r7
        L_0x00ed:
            long r10 = (long) r6
            android.os.SystemClock.sleep(r10)     // Catch:{ all -> 0x011f }
            int r6 = r6 + 20
            if (r7 == 0) goto L_0x00f8
            r7.close()
        L_0x00f8:
            if (r9 == 0) goto L_0x0119
            r9.close()
            goto L_0x0119
        L_0x00fe:
            r0 = move-exception
            r9 = r7
        L_0x0100:
            com.google.android.gms.measurement.internal.zzau r2 = r16.zzad()     // Catch:{ all -> 0x011f }
            com.google.android.gms.measurement.internal.zzaw r2 = r2.zzda()     // Catch:{ all -> 0x011f }
            java.lang.String r4 = "Error writing entry to local database"
            r2.zza(r4, r0)     // Catch:{ all -> 0x011f }
            r1.zzjk = r8     // Catch:{ all -> 0x011f }
            if (r7 == 0) goto L_0x0114
            r7.close()
        L_0x0114:
            if (r9 == 0) goto L_0x0119
            r9.close()
        L_0x0119:
            int r5 = r5 + 1
            r2 = 0
            r4 = 5
            goto L_0x0026
        L_0x011f:
            r0 = move-exception
            r12 = r7
        L_0x0121:
            if (r12 == 0) goto L_0x0126
            r12.close()
        L_0x0126:
            if (r9 == 0) goto L_0x012b
            r9.close()
        L_0x012b:
            throw r0
        L_0x012c:
            com.google.android.gms.measurement.internal.zzau r0 = r16.zzad()
            com.google.android.gms.measurement.internal.zzaw r0 = r0.zzdd()
            java.lang.String r2 = "Failed to write entry to local database"
            r0.zzaq(r2)
            r2 = 0
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zzaq.zza(int, byte[]):boolean");
    }

    public final boolean zza(zzaj zzaj) {
        Parcel obtain = Parcel.obtain();
        zzaj.writeToParcel(obtain, 0);
        byte[] marshall = obtain.marshall();
        obtain.recycle();
        if (marshall.length <= 131072) {
            return zza(0, marshall);
        }
        zzad().zzdd().zzaq("Event is too long for local database. Sending event directly to service");
        return false;
    }

    public final boolean zza(zzga zzga) {
        Parcel obtain = Parcel.obtain();
        zzga.writeToParcel(obtain, 0);
        byte[] marshall = obtain.marshall();
        obtain.recycle();
        if (marshall.length <= 131072) {
            return zza(1, marshall);
        }
        zzad().zzdd().zzaq("User property too long for local database. Sending directly to service");
        return false;
    }

    public final boolean zzc(zzr zzr) {
        zzab();
        byte[] zza = zzgd.zza((Parcelable) zzr);
        if (zza.length <= 131072) {
            return zza(2, zza);
        }
        zzad().zzdd().zzaq("Conditional user property too long for local database. Sending directly to service");
        return false;
    }

    /* JADX WARNING: type inference failed for: r2v0 */
    /* JADX WARNING: type inference failed for: r2v1, types: [java.util.List<com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable>] */
    /* JADX WARNING: type inference failed for: r9v0, types: [android.database.Cursor] */
    /* JADX WARNING: type inference failed for: r2v5 */
    /* JADX WARNING: type inference failed for: r2v6, types: [android.database.Cursor] */
    /* JADX WARNING: type inference failed for: r9v1 */
    /* JADX WARNING: type inference failed for: r2v8 */
    /* JADX WARNING: type inference failed for: r2v9, types: [android.database.Cursor] */
    /* JADX WARNING: type inference failed for: r9v3 */
    /* JADX WARNING: type inference failed for: r2v11 */
    /* JADX WARNING: type inference failed for: r9v4, types: [android.database.Cursor] */
    /* JADX WARNING: type inference failed for: r9v5 */
    /* JADX WARNING: type inference failed for: r9v6 */
    /* JADX WARNING: type inference failed for: r2v18 */
    /* JADX WARNING: type inference failed for: r2v20 */
    /* JADX WARNING: type inference failed for: r2v25 */
    /* JADX WARNING: type inference failed for: r2v26 */
    /* JADX WARNING: type inference failed for: r9v10 */
    /* JADX WARNING: type inference failed for: r2v28 */
    /* JADX WARNING: type inference failed for: r2v29 */
    /* JADX WARNING: type inference failed for: r2v30 */
    /* JADX WARNING: type inference failed for: r2v31 */
    /* JADX WARNING: type inference failed for: r2v32 */
    /* JADX WARNING: type inference failed for: r9v11 */
    /* JADX WARNING: type inference failed for: r9v12 */
    /* JADX WARNING: type inference failed for: r9v13 */
    /* JADX WARNING: type inference failed for: r2v33 */
    /* JADX WARNING: type inference failed for: r2v34 */
    /* JADX WARNING: type inference failed for: r9v14 */
    /* JADX WARNING: Can't wrap try/catch for region: R(4:52|53|54|55) */
    /* JADX WARNING: Can't wrap try/catch for region: R(4:67|68|69|70) */
    /* JADX WARNING: Can't wrap try/catch for region: R(5:39|40|41|42|164) */
    /* JADX WARNING: Code restructure failed: missing block: B:102:0x0185, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:103:0x0186, code lost:
        r2 = r15;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:?, code lost:
        zzad().zzda().zzaq("Failed to load event from local database");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:?, code lost:
        r13.recycle();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:?, code lost:
        zzad().zzda().zzaq("Failed to load user property from local database");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:?, code lost:
        r13.recycle();
        r0 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:68:?, code lost:
        zzad().zzda().zzaq("Failed to load user property from local database");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:70:?, code lost:
        r13.recycle();
        r0 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:94:0x017b, code lost:
        r0 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:95:0x017c, code lost:
        r2 = r15;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:96:0x017e, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:97:0x017f, code lost:
        r2 = r15;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:98:0x0181, code lost:
        r2 = r15;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:39:0x00a5 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:52:0x00d5 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:67:0x010b */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:112:0x0193 A[SYNTHETIC, Splitter:B:112:0x0193] */
    /* JADX WARNING: Removed duplicated region for block: B:118:0x01ad  */
    /* JADX WARNING: Removed duplicated region for block: B:120:0x01b2  */
    /* JADX WARNING: Removed duplicated region for block: B:129:0x01c2  */
    /* JADX WARNING: Removed duplicated region for block: B:131:0x01c7  */
    /* JADX WARNING: Removed duplicated region for block: B:139:0x01e3  */
    /* JADX WARNING: Removed duplicated region for block: B:141:0x01e8  */
    /* JADX WARNING: Removed duplicated region for block: B:146:0x01f6  */
    /* JADX WARNING: Removed duplicated region for block: B:148:0x01fb  */
    /* JADX WARNING: Removed duplicated region for block: B:156:0x01eb A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:158:0x01eb A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:160:0x01eb A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:99:? A[ExcHandler: SQLiteDatabaseLockedException (unused android.database.sqlite.SQLiteDatabaseLockedException), SYNTHETIC, Splitter:B:12:0x0031] */
    /* JADX WARNING: Unknown variable types count: 13 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.util.List<com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable> zzc(int r20) {
        /*
            r19 = this;
            r1 = r19
            r19.zzq()
            r19.zzo()
            boolean r0 = r1.zzjk
            r2 = 0
            if (r0 == 0) goto L_0x000e
            return r2
        L_0x000e:
            java.util.ArrayList r3 = new java.util.ArrayList
            r3.<init>()
            android.content.Context r0 = r19.getContext()
            java.lang.String r4 = "google_app_measurement_local.db"
            java.io.File r0 = r0.getDatabasePath(r4)
            boolean r0 = r0.exists()
            if (r0 != 0) goto L_0x0024
            return r3
        L_0x0024:
            r4 = 5
            r5 = 0
            r6 = 0
            r7 = 5
        L_0x0028:
            if (r6 >= r4) goto L_0x01ff
            r8 = 1
            android.database.sqlite.SQLiteDatabase r15 = r19.getWritableDatabase()     // Catch:{ SQLiteFullException -> 0x01cf, SQLiteDatabaseLockedException -> 0x01b8, SQLiteException -> 0x018e, all -> 0x0189 }
            if (r15 != 0) goto L_0x0041
            r1.zzjk = r8     // Catch:{ SQLiteFullException -> 0x003e, SQLiteDatabaseLockedException -> 0x0181, SQLiteException -> 0x0039 }
            if (r15 == 0) goto L_0x0038
            r15.close()
        L_0x0038:
            return r2
        L_0x0039:
            r0 = move-exception
            r9 = r2
            r2 = r15
            goto L_0x0191
        L_0x003e:
            r0 = move-exception
            goto L_0x01d2
        L_0x0041:
            r15.beginTransaction()     // Catch:{ SQLiteFullException -> 0x0185, SQLiteDatabaseLockedException -> 0x0181, SQLiteException -> 0x017e, all -> 0x017b }
            java.lang.String r10 = "messages"
            r0 = 3
            java.lang.String[] r11 = new java.lang.String[r0]     // Catch:{ SQLiteFullException -> 0x0185, SQLiteDatabaseLockedException -> 0x0181, SQLiteException -> 0x017e, all -> 0x017b }
            java.lang.String r0 = "rowid"
            r11[r5] = r0     // Catch:{ SQLiteFullException -> 0x0185, SQLiteDatabaseLockedException -> 0x0181, SQLiteException -> 0x017e, all -> 0x017b }
            java.lang.String r0 = "type"
            r11[r8] = r0     // Catch:{ SQLiteFullException -> 0x0185, SQLiteDatabaseLockedException -> 0x0181, SQLiteException -> 0x017e, all -> 0x017b }
            java.lang.String r0 = "entry"
            r14 = 2
            r11[r14] = r0     // Catch:{ SQLiteFullException -> 0x0185, SQLiteDatabaseLockedException -> 0x0181, SQLiteException -> 0x017e, all -> 0x017b }
            r12 = 0
            r13 = 0
            r0 = 0
            r16 = 0
            java.lang.String r17 = "rowid asc"
            r9 = 100
            java.lang.String r18 = java.lang.Integer.toString(r9)     // Catch:{ SQLiteFullException -> 0x0185, SQLiteDatabaseLockedException -> 0x0181, SQLiteException -> 0x017e, all -> 0x017b }
            r9 = r15
            r4 = 2
            r14 = r0
            r2 = r15
            r15 = r16
            r16 = r17
            r17 = r18
            android.database.Cursor r9 = r9.query(r10, r11, r12, r13, r14, r15, r16, r17)     // Catch:{ SQLiteFullException -> 0x0178, SQLiteDatabaseLockedException -> 0x0182, SQLiteException -> 0x0176, all -> 0x0174 }
            r10 = -1
        L_0x0073:
            boolean r0 = r9.moveToNext()     // Catch:{ SQLiteFullException -> 0x016f, SQLiteDatabaseLockedException -> 0x016c, SQLiteException -> 0x016a }
            if (r0 == 0) goto L_0x0136
            long r10 = r9.getLong(r5)     // Catch:{ SQLiteFullException -> 0x016f, SQLiteDatabaseLockedException -> 0x016c, SQLiteException -> 0x016a }
            int r0 = r9.getInt(r8)     // Catch:{ SQLiteFullException -> 0x016f, SQLiteDatabaseLockedException -> 0x016c, SQLiteException -> 0x016a }
            byte[] r12 = r9.getBlob(r4)     // Catch:{ SQLiteFullException -> 0x016f, SQLiteDatabaseLockedException -> 0x016c, SQLiteException -> 0x016a }
            if (r0 != 0) goto L_0x00ba
            android.os.Parcel r13 = android.os.Parcel.obtain()     // Catch:{ SQLiteFullException -> 0x016f, SQLiteDatabaseLockedException -> 0x016c, SQLiteException -> 0x016a }
            int r0 = r12.length     // Catch:{ ParseException -> 0x00a5 }
            r13.unmarshall(r12, r5, r0)     // Catch:{ ParseException -> 0x00a5 }
            r13.setDataPosition(r5)     // Catch:{ ParseException -> 0x00a5 }
            android.os.Parcelable$Creator<com.google.android.gms.measurement.internal.zzaj> r0 = com.google.android.gms.measurement.internal.zzaj.CREATOR     // Catch:{ ParseException -> 0x00a5 }
            java.lang.Object r0 = r0.createFromParcel(r13)     // Catch:{ ParseException -> 0x00a5 }
            com.google.android.gms.measurement.internal.zzaj r0 = (com.google.android.gms.measurement.internal.zzaj) r0     // Catch:{ ParseException -> 0x00a5 }
            r13.recycle()     // Catch:{ SQLiteFullException -> 0x016f, SQLiteDatabaseLockedException -> 0x016c, SQLiteException -> 0x016a }
            if (r0 == 0) goto L_0x0073
            r3.add(r0)     // Catch:{ SQLiteFullException -> 0x016f, SQLiteDatabaseLockedException -> 0x016c, SQLiteException -> 0x016a }
            goto L_0x0073
        L_0x00a3:
            r0 = move-exception
            goto L_0x00b6
        L_0x00a5:
            com.google.android.gms.measurement.internal.zzau r0 = r19.zzad()     // Catch:{ all -> 0x00a3 }
            com.google.android.gms.measurement.internal.zzaw r0 = r0.zzda()     // Catch:{ all -> 0x00a3 }
            java.lang.String r12 = "Failed to load event from local database"
            r0.zzaq(r12)     // Catch:{ all -> 0x00a3 }
            r13.recycle()     // Catch:{ SQLiteFullException -> 0x016f, SQLiteDatabaseLockedException -> 0x016c, SQLiteException -> 0x016a }
            goto L_0x0073
        L_0x00b6:
            r13.recycle()     // Catch:{ SQLiteFullException -> 0x016f, SQLiteDatabaseLockedException -> 0x016c, SQLiteException -> 0x016a }
            throw r0     // Catch:{ SQLiteFullException -> 0x016f, SQLiteDatabaseLockedException -> 0x016c, SQLiteException -> 0x016a }
        L_0x00ba:
            if (r0 != r8) goto L_0x00f0
            android.os.Parcel r13 = android.os.Parcel.obtain()     // Catch:{ SQLiteFullException -> 0x016f, SQLiteDatabaseLockedException -> 0x016c, SQLiteException -> 0x016a }
            int r0 = r12.length     // Catch:{ ParseException -> 0x00d5 }
            r13.unmarshall(r12, r5, r0)     // Catch:{ ParseException -> 0x00d5 }
            r13.setDataPosition(r5)     // Catch:{ ParseException -> 0x00d5 }
            android.os.Parcelable$Creator<com.google.android.gms.measurement.internal.zzga> r0 = com.google.android.gms.measurement.internal.zzga.CREATOR     // Catch:{ ParseException -> 0x00d5 }
            java.lang.Object r0 = r0.createFromParcel(r13)     // Catch:{ ParseException -> 0x00d5 }
            com.google.android.gms.measurement.internal.zzga r0 = (com.google.android.gms.measurement.internal.zzga) r0     // Catch:{ ParseException -> 0x00d5 }
            r13.recycle()     // Catch:{ SQLiteFullException -> 0x016f, SQLiteDatabaseLockedException -> 0x016c, SQLiteException -> 0x016a }
            goto L_0x00e6
        L_0x00d3:
            r0 = move-exception
            goto L_0x00ec
        L_0x00d5:
            com.google.android.gms.measurement.internal.zzau r0 = r19.zzad()     // Catch:{ all -> 0x00d3 }
            com.google.android.gms.measurement.internal.zzaw r0 = r0.zzda()     // Catch:{ all -> 0x00d3 }
            java.lang.String r12 = "Failed to load user property from local database"
            r0.zzaq(r12)     // Catch:{ all -> 0x00d3 }
            r13.recycle()     // Catch:{ SQLiteFullException -> 0x016f, SQLiteDatabaseLockedException -> 0x016c, SQLiteException -> 0x016a }
            r0 = 0
        L_0x00e6:
            if (r0 == 0) goto L_0x0073
            r3.add(r0)     // Catch:{ SQLiteFullException -> 0x016f, SQLiteDatabaseLockedException -> 0x016c, SQLiteException -> 0x016a }
            goto L_0x0073
        L_0x00ec:
            r13.recycle()     // Catch:{ SQLiteFullException -> 0x016f, SQLiteDatabaseLockedException -> 0x016c, SQLiteException -> 0x016a }
            throw r0     // Catch:{ SQLiteFullException -> 0x016f, SQLiteDatabaseLockedException -> 0x016c, SQLiteException -> 0x016a }
        L_0x00f0:
            if (r0 != r4) goto L_0x0127
            android.os.Parcel r13 = android.os.Parcel.obtain()     // Catch:{ SQLiteFullException -> 0x016f, SQLiteDatabaseLockedException -> 0x016c, SQLiteException -> 0x016a }
            int r0 = r12.length     // Catch:{ ParseException -> 0x010b }
            r13.unmarshall(r12, r5, r0)     // Catch:{ ParseException -> 0x010b }
            r13.setDataPosition(r5)     // Catch:{ ParseException -> 0x010b }
            android.os.Parcelable$Creator<com.google.android.gms.measurement.internal.zzr> r0 = com.google.android.gms.measurement.internal.zzr.CREATOR     // Catch:{ ParseException -> 0x010b }
            java.lang.Object r0 = r0.createFromParcel(r13)     // Catch:{ ParseException -> 0x010b }
            com.google.android.gms.measurement.internal.zzr r0 = (com.google.android.gms.measurement.internal.zzr) r0     // Catch:{ ParseException -> 0x010b }
            r13.recycle()     // Catch:{ SQLiteFullException -> 0x016f, SQLiteDatabaseLockedException -> 0x016c, SQLiteException -> 0x016a }
            goto L_0x011c
        L_0x0109:
            r0 = move-exception
            goto L_0x0123
        L_0x010b:
            com.google.android.gms.measurement.internal.zzau r0 = r19.zzad()     // Catch:{ all -> 0x0109 }
            com.google.android.gms.measurement.internal.zzaw r0 = r0.zzda()     // Catch:{ all -> 0x0109 }
            java.lang.String r12 = "Failed to load user property from local database"
            r0.zzaq(r12)     // Catch:{ all -> 0x0109 }
            r13.recycle()     // Catch:{ SQLiteFullException -> 0x016f, SQLiteDatabaseLockedException -> 0x016c, SQLiteException -> 0x016a }
            r0 = 0
        L_0x011c:
            if (r0 == 0) goto L_0x0073
            r3.add(r0)     // Catch:{ SQLiteFullException -> 0x016f, SQLiteDatabaseLockedException -> 0x016c, SQLiteException -> 0x016a }
            goto L_0x0073
        L_0x0123:
            r13.recycle()     // Catch:{ SQLiteFullException -> 0x016f, SQLiteDatabaseLockedException -> 0x016c, SQLiteException -> 0x016a }
            throw r0     // Catch:{ SQLiteFullException -> 0x016f, SQLiteDatabaseLockedException -> 0x016c, SQLiteException -> 0x016a }
        L_0x0127:
            com.google.android.gms.measurement.internal.zzau r0 = r19.zzad()     // Catch:{ SQLiteFullException -> 0x016f, SQLiteDatabaseLockedException -> 0x016c, SQLiteException -> 0x016a }
            com.google.android.gms.measurement.internal.zzaw r0 = r0.zzda()     // Catch:{ SQLiteFullException -> 0x016f, SQLiteDatabaseLockedException -> 0x016c, SQLiteException -> 0x016a }
            java.lang.String r12 = "Unknown record type in local database"
            r0.zzaq(r12)     // Catch:{ SQLiteFullException -> 0x016f, SQLiteDatabaseLockedException -> 0x016c, SQLiteException -> 0x016a }
            goto L_0x0073
        L_0x0136:
            java.lang.String r0 = "messages"
            java.lang.String r4 = "rowid <= ?"
            java.lang.String[] r12 = new java.lang.String[r8]     // Catch:{ SQLiteFullException -> 0x016f, SQLiteDatabaseLockedException -> 0x016c, SQLiteException -> 0x016a }
            java.lang.String r10 = java.lang.Long.toString(r10)     // Catch:{ SQLiteFullException -> 0x016f, SQLiteDatabaseLockedException -> 0x016c, SQLiteException -> 0x016a }
            r12[r5] = r10     // Catch:{ SQLiteFullException -> 0x016f, SQLiteDatabaseLockedException -> 0x016c, SQLiteException -> 0x016a }
            int r0 = r2.delete(r0, r4, r12)     // Catch:{ SQLiteFullException -> 0x016f, SQLiteDatabaseLockedException -> 0x016c, SQLiteException -> 0x016a }
            int r4 = r3.size()     // Catch:{ SQLiteFullException -> 0x016f, SQLiteDatabaseLockedException -> 0x016c, SQLiteException -> 0x016a }
            if (r0 >= r4) goto L_0x0159
            com.google.android.gms.measurement.internal.zzau r0 = r19.zzad()     // Catch:{ SQLiteFullException -> 0x016f, SQLiteDatabaseLockedException -> 0x016c, SQLiteException -> 0x016a }
            com.google.android.gms.measurement.internal.zzaw r0 = r0.zzda()     // Catch:{ SQLiteFullException -> 0x016f, SQLiteDatabaseLockedException -> 0x016c, SQLiteException -> 0x016a }
            java.lang.String r4 = "Fewer entries removed from local database than expected"
            r0.zzaq(r4)     // Catch:{ SQLiteFullException -> 0x016f, SQLiteDatabaseLockedException -> 0x016c, SQLiteException -> 0x016a }
        L_0x0159:
            r2.setTransactionSuccessful()     // Catch:{ SQLiteFullException -> 0x016f, SQLiteDatabaseLockedException -> 0x016c, SQLiteException -> 0x016a }
            r2.endTransaction()     // Catch:{ SQLiteFullException -> 0x016f, SQLiteDatabaseLockedException -> 0x016c, SQLiteException -> 0x016a }
            if (r9 == 0) goto L_0x0164
            r9.close()
        L_0x0164:
            if (r2 == 0) goto L_0x0169
            r2.close()
        L_0x0169:
            return r3
        L_0x016a:
            r0 = move-exception
            goto L_0x0191
        L_0x016c:
            r4 = r2
            r2 = r9
            goto L_0x01ba
        L_0x016f:
            r0 = move-exception
            r15 = r2
            r2 = r9
            goto L_0x01d2
        L_0x0174:
            r0 = move-exception
            goto L_0x018b
        L_0x0176:
            r0 = move-exception
            goto L_0x0190
        L_0x0178:
            r0 = move-exception
            r15 = r2
            goto L_0x0187
        L_0x017b:
            r0 = move-exception
            r2 = r15
            goto L_0x018b
        L_0x017e:
            r0 = move-exception
            r2 = r15
            goto L_0x0190
        L_0x0181:
            r2 = r15
        L_0x0182:
            r4 = r2
            r2 = 0
            goto L_0x01ba
        L_0x0185:
            r0 = move-exception
            r2 = r15
        L_0x0187:
            r2 = 0
            goto L_0x01d2
        L_0x0189:
            r0 = move-exception
            r2 = 0
        L_0x018b:
            r9 = 0
            goto L_0x01f4
        L_0x018e:
            r0 = move-exception
            r2 = 0
        L_0x0190:
            r9 = 0
        L_0x0191:
            if (r2 == 0) goto L_0x019c
            boolean r4 = r2.inTransaction()     // Catch:{ all -> 0x01b6 }
            if (r4 == 0) goto L_0x019c
            r2.endTransaction()     // Catch:{ all -> 0x01b6 }
        L_0x019c:
            com.google.android.gms.measurement.internal.zzau r4 = r19.zzad()     // Catch:{ all -> 0x01b6 }
            com.google.android.gms.measurement.internal.zzaw r4 = r4.zzda()     // Catch:{ all -> 0x01b6 }
            java.lang.String r10 = "Error reading entries from local database"
            r4.zza(r10, r0)     // Catch:{ all -> 0x01b6 }
            r1.zzjk = r8     // Catch:{ all -> 0x01b6 }
            if (r9 == 0) goto L_0x01b0
            r9.close()
        L_0x01b0:
            if (r2 == 0) goto L_0x01eb
            r2.close()
            goto L_0x01eb
        L_0x01b6:
            r0 = move-exception
            goto L_0x01f4
        L_0x01b8:
            r2 = 0
            r4 = 0
        L_0x01ba:
            long r8 = (long) r7
            android.os.SystemClock.sleep(r8)     // Catch:{ all -> 0x01cb }
            int r7 = r7 + 20
            if (r2 == 0) goto L_0x01c5
            r2.close()
        L_0x01c5:
            if (r4 == 0) goto L_0x01eb
            r4.close()
            goto L_0x01eb
        L_0x01cb:
            r0 = move-exception
            r9 = r2
            r2 = r4
            goto L_0x01f4
        L_0x01cf:
            r0 = move-exception
            r2 = 0
            r15 = 0
        L_0x01d2:
            com.google.android.gms.measurement.internal.zzau r4 = r19.zzad()     // Catch:{ all -> 0x01f1 }
            com.google.android.gms.measurement.internal.zzaw r4 = r4.zzda()     // Catch:{ all -> 0x01f1 }
            java.lang.String r9 = "Error reading entries from local database"
            r4.zza(r9, r0)     // Catch:{ all -> 0x01f1 }
            r1.zzjk = r8     // Catch:{ all -> 0x01f1 }
            if (r2 == 0) goto L_0x01e6
            r2.close()
        L_0x01e6:
            if (r15 == 0) goto L_0x01eb
            r15.close()
        L_0x01eb:
            int r6 = r6 + 1
            r2 = 0
            r4 = 5
            goto L_0x0028
        L_0x01f1:
            r0 = move-exception
            r9 = r2
            r2 = r15
        L_0x01f4:
            if (r9 == 0) goto L_0x01f9
            r9.close()
        L_0x01f9:
            if (r2 == 0) goto L_0x01fe
            r2.close()
        L_0x01fe:
            throw r0
        L_0x01ff:
            com.google.android.gms.measurement.internal.zzau r0 = r19.zzad()
            com.google.android.gms.measurement.internal.zzaw r0 = r0.zzdd()
            java.lang.String r2 = "Failed to read events from database in reasonable time"
            r0.zzaq(r2)
            r2 = 0
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zzaq.zzc(int):java.util.List");
    }

    @WorkerThread
    @VisibleForTesting
    private final SQLiteDatabase getWritableDatabase() throws SQLiteException {
        if (this.zzjk) {
            return null;
        }
        SQLiteDatabase writableDatabase = this.zzjj.getWritableDatabase();
        if (writableDatabase != null) {
            return writableDatabase;
        }
        this.zzjk = true;
        return null;
    }

    public final /* bridge */ /* synthetic */ void zzn() {
        super.zzn();
    }

    public final /* bridge */ /* synthetic */ void zzo() {
        super.zzo();
    }

    public final /* bridge */ /* synthetic */ void zzp() {
        super.zzp();
    }

    public final /* bridge */ /* synthetic */ void zzq() {
        super.zzq();
    }

    public final /* bridge */ /* synthetic */ zza zzr() {
        return super.zzr();
    }

    public final /* bridge */ /* synthetic */ zzdd zzs() {
        return super.zzs();
    }

    public final /* bridge */ /* synthetic */ zzap zzt() {
        return super.zzt();
    }

    public final /* bridge */ /* synthetic */ zzeg zzu() {
        return super.zzu();
    }

    public final /* bridge */ /* synthetic */ zzed zzv() {
        return super.zzv();
    }

    public final /* bridge */ /* synthetic */ zzaq zzw() {
        return super.zzw();
    }

    public final /* bridge */ /* synthetic */ zzfj zzx() {
        return super.zzx();
    }

    public final /* bridge */ /* synthetic */ zzad zzy() {
        return super.zzy();
    }

    public final /* bridge */ /* synthetic */ Clock zzz() {
        return super.zzz();
    }

    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    public final /* bridge */ /* synthetic */ zzas zzaa() {
        return super.zzaa();
    }

    public final /* bridge */ /* synthetic */ zzgd zzab() {
        return super.zzab();
    }

    public final /* bridge */ /* synthetic */ zzbt zzac() {
        return super.zzac();
    }

    public final /* bridge */ /* synthetic */ zzau zzad() {
        return super.zzad();
    }

    public final /* bridge */ /* synthetic */ zzbf zzae() {
        return super.zzae();
    }

    public final /* bridge */ /* synthetic */ zzt zzaf() {
        return super.zzaf();
    }

    public final /* bridge */ /* synthetic */ zzq zzag() {
        return super.zzag();
    }
}

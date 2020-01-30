package com.google.android.gms.measurement.internal;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Parcelable;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;
import android.util.Pair;
import com.facebook.common.util.UriUtil;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.android.gms.internal.measurement.zzbt.zzd;
import com.google.android.gms.internal.measurement.zzbt.zzd.zza;
import com.google.android.gms.internal.measurement.zzbx;
import com.google.android.gms.internal.measurement.zzby;
import com.google.android.gms.internal.measurement.zzcb;
import com.google.android.gms.internal.measurement.zzcf;
import com.google.android.gms.internal.measurement.zzch;
import com.google.android.gms.internal.measurement.zzez;
import com.google.android.gms.internal.measurement.zziv;
import com.google.android.gms.measurement.api.AppMeasurementSdk.ConditionalUserProperty;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

final class zzw extends zzfs {
    /* access modifiers changed from: private */
    public static final String[] zzed = {"last_bundled_timestamp", "ALTER TABLE events ADD COLUMN last_bundled_timestamp INTEGER;", "last_bundled_day", "ALTER TABLE events ADD COLUMN last_bundled_day INTEGER;", "last_sampled_complex_event_id", "ALTER TABLE events ADD COLUMN last_sampled_complex_event_id INTEGER;", "last_sampling_rate", "ALTER TABLE events ADD COLUMN last_sampling_rate INTEGER;", "last_exempt_from_sampling", "ALTER TABLE events ADD COLUMN last_exempt_from_sampling INTEGER;"};
    /* access modifiers changed from: private */
    public static final String[] zzee = {"origin", "ALTER TABLE user_attributes ADD COLUMN origin TEXT;"};
    /* access modifiers changed from: private */
    public static final String[] zzef = {"app_version", "ALTER TABLE apps ADD COLUMN app_version TEXT;", "app_store", "ALTER TABLE apps ADD COLUMN app_store TEXT;", "gmp_version", "ALTER TABLE apps ADD COLUMN gmp_version INTEGER;", "dev_cert_hash", "ALTER TABLE apps ADD COLUMN dev_cert_hash INTEGER;", "measurement_enabled", "ALTER TABLE apps ADD COLUMN measurement_enabled INTEGER;", "last_bundle_start_timestamp", "ALTER TABLE apps ADD COLUMN last_bundle_start_timestamp INTEGER;", "day", "ALTER TABLE apps ADD COLUMN day INTEGER;", "daily_public_events_count", "ALTER TABLE apps ADD COLUMN daily_public_events_count INTEGER;", "daily_events_count", "ALTER TABLE apps ADD COLUMN daily_events_count INTEGER;", "daily_conversions_count", "ALTER TABLE apps ADD COLUMN daily_conversions_count INTEGER;", "remote_config", "ALTER TABLE apps ADD COLUMN remote_config BLOB;", "config_fetched_time", "ALTER TABLE apps ADD COLUMN config_fetched_time INTEGER;", "failed_config_fetch_time", "ALTER TABLE apps ADD COLUMN failed_config_fetch_time INTEGER;", "app_version_int", "ALTER TABLE apps ADD COLUMN app_version_int INTEGER;", "firebase_instance_id", "ALTER TABLE apps ADD COLUMN firebase_instance_id TEXT;", "daily_error_events_count", "ALTER TABLE apps ADD COLUMN daily_error_events_count INTEGER;", "daily_realtime_events_count", "ALTER TABLE apps ADD COLUMN daily_realtime_events_count INTEGER;", "health_monitor_sample", "ALTER TABLE apps ADD COLUMN health_monitor_sample TEXT;", "android_id", "ALTER TABLE apps ADD COLUMN android_id INTEGER;", "adid_reporting_enabled", "ALTER TABLE apps ADD COLUMN adid_reporting_enabled INTEGER;", "ssaid_reporting_enabled", "ALTER TABLE apps ADD COLUMN ssaid_reporting_enabled INTEGER;", "admob_app_id", "ALTER TABLE apps ADD COLUMN admob_app_id TEXT;", "linked_admob_app_id", "ALTER TABLE apps ADD COLUMN linked_admob_app_id TEXT;", "dynamite_version", "ALTER TABLE apps ADD COLUMN dynamite_version INTEGER;"};
    /* access modifiers changed from: private */
    public static final String[] zzeg = {"realtime", "ALTER TABLE raw_events ADD COLUMN realtime INTEGER;"};
    /* access modifiers changed from: private */
    public static final String[] zzeh = {"has_realtime", "ALTER TABLE queue ADD COLUMN has_realtime INTEGER;", "retry_count", "ALTER TABLE queue ADD COLUMN retry_count INTEGER;"};
    /* access modifiers changed from: private */
    public static final String[] zzei = {"previous_install_count", "ALTER TABLE app2 ADD COLUMN previous_install_count INTEGER;"};
    private final zzz zzej = new zzz(this, getContext(), "google_app_measurement.db");
    /* access modifiers changed from: private */
    public final zzfo zzek = new zzfo(zzz());

    zzw(zzft zzft) {
        super(zzft);
    }

    /* access modifiers changed from: protected */
    public final boolean zzak() {
        return false;
    }

    @WorkerThread
    public final void beginTransaction() {
        zzah();
        getWritableDatabase().beginTransaction();
    }

    @WorkerThread
    public final void setTransactionSuccessful() {
        zzah();
        getWritableDatabase().setTransactionSuccessful();
    }

    @WorkerThread
    public final void endTransaction() {
        zzah();
        getWritableDatabase().endTransaction();
    }

    @WorkerThread
    private final long zza(String str, String[] strArr) {
        Cursor cursor = null;
        try {
            cursor = getWritableDatabase().rawQuery(str, strArr);
            if (cursor.moveToFirst()) {
                long j = cursor.getLong(0);
                if (cursor != null) {
                    cursor.close();
                }
                return j;
            }
            throw new SQLiteException("Database returned empty set");
        } catch (SQLiteException e) {
            zzad().zzda().zza("Database error", str, e);
            throw e;
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }

    @WorkerThread
    private final long zza(String str, String[] strArr, long j) {
        Cursor cursor = null;
        try {
            Cursor rawQuery = getWritableDatabase().rawQuery(str, strArr);
            if (rawQuery.moveToFirst()) {
                long j2 = rawQuery.getLong(0);
                if (rawQuery != null) {
                    rawQuery.close();
                }
                return j2;
            }
            if (rawQuery != null) {
                rawQuery.close();
            }
            return j;
        } catch (SQLiteException e) {
            zzad().zzda().zza("Database error", str, e);
            throw e;
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    @VisibleForTesting
    public final SQLiteDatabase getWritableDatabase() {
        zzq();
        try {
            return this.zzej.getWritableDatabase();
        } catch (SQLiteException e) {
            zzad().zzdd().zza("Error opening database", e);
            throw e;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:55:0x012d  */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x0134  */
    @android.support.annotation.WorkerThread
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final com.google.android.gms.measurement.internal.zzaf zzc(java.lang.String r23, java.lang.String r24) {
        /*
            r22 = this;
            r15 = r24
            com.google.android.gms.common.internal.Preconditions.checkNotEmpty(r23)
            com.google.android.gms.common.internal.Preconditions.checkNotEmpty(r24)
            r22.zzq()
            r22.zzah()
            r16 = 0
            android.database.sqlite.SQLiteDatabase r1 = r22.getWritableDatabase()     // Catch:{ SQLiteException -> 0x010d, all -> 0x0109 }
            java.lang.String r2 = "events"
            r0 = 8
            java.lang.String[] r3 = new java.lang.String[r0]     // Catch:{ SQLiteException -> 0x010d, all -> 0x0109 }
            java.lang.String r0 = "lifetime_count"
            r9 = 0
            r3[r9] = r0     // Catch:{ SQLiteException -> 0x010d, all -> 0x0109 }
            java.lang.String r0 = "current_bundle_count"
            r10 = 1
            r3[r10] = r0     // Catch:{ SQLiteException -> 0x010d, all -> 0x0109 }
            java.lang.String r0 = "last_fire_timestamp"
            r11 = 2
            r3[r11] = r0     // Catch:{ SQLiteException -> 0x010d, all -> 0x0109 }
            java.lang.String r0 = "last_bundled_timestamp"
            r12 = 3
            r3[r12] = r0     // Catch:{ SQLiteException -> 0x010d, all -> 0x0109 }
            java.lang.String r0 = "last_bundled_day"
            r13 = 4
            r3[r13] = r0     // Catch:{ SQLiteException -> 0x010d, all -> 0x0109 }
            java.lang.String r0 = "last_sampled_complex_event_id"
            r14 = 5
            r3[r14] = r0     // Catch:{ SQLiteException -> 0x010d, all -> 0x0109 }
            java.lang.String r0 = "last_sampling_rate"
            r8 = 6
            r3[r8] = r0     // Catch:{ SQLiteException -> 0x010d, all -> 0x0109 }
            java.lang.String r0 = "last_exempt_from_sampling"
            r7 = 7
            r3[r7] = r0     // Catch:{ SQLiteException -> 0x010d, all -> 0x0109 }
            java.lang.String r4 = "app_id=? and name=?"
            java.lang.String[] r5 = new java.lang.String[r11]     // Catch:{ SQLiteException -> 0x010d, all -> 0x0109 }
            r5[r9] = r23     // Catch:{ SQLiteException -> 0x010d, all -> 0x0109 }
            r5[r10] = r15     // Catch:{ SQLiteException -> 0x010d, all -> 0x0109 }
            r6 = 0
            r0 = 0
            r17 = 0
            r7 = r0
            r0 = 6
            r8 = r17
            android.database.Cursor r8 = r1.query(r2, r3, r4, r5, r6, r7, r8)     // Catch:{ SQLiteException -> 0x010d, all -> 0x0109 }
            boolean r1 = r8.moveToFirst()     // Catch:{ SQLiteException -> 0x0105, all -> 0x0101 }
            if (r1 != 0) goto L_0x0062
            if (r8 == 0) goto L_0x0061
            r8.close()
        L_0x0061:
            return r16
        L_0x0062:
            long r4 = r8.getLong(r9)     // Catch:{ SQLiteException -> 0x0105, all -> 0x0101 }
            long r6 = r8.getLong(r10)     // Catch:{ SQLiteException -> 0x0105, all -> 0x0101 }
            long r19 = r8.getLong(r11)     // Catch:{ SQLiteException -> 0x0105, all -> 0x0101 }
            boolean r1 = r8.isNull(r12)     // Catch:{ SQLiteException -> 0x0105, all -> 0x0101 }
            if (r1 == 0) goto L_0x0077
            r1 = 0
            goto L_0x007b
        L_0x0077:
            long r1 = r8.getLong(r12)     // Catch:{ SQLiteException -> 0x0105, all -> 0x0101 }
        L_0x007b:
            r11 = r1
            boolean r1 = r8.isNull(r13)     // Catch:{ SQLiteException -> 0x0105, all -> 0x0101 }
            if (r1 == 0) goto L_0x0085
            r13 = r16
            goto L_0x008e
        L_0x0085:
            long r1 = r8.getLong(r13)     // Catch:{ SQLiteException -> 0x0105, all -> 0x0101 }
            java.lang.Long r1 = java.lang.Long.valueOf(r1)     // Catch:{ SQLiteException -> 0x0105, all -> 0x0101 }
            r13 = r1
        L_0x008e:
            boolean r1 = r8.isNull(r14)     // Catch:{ SQLiteException -> 0x0105, all -> 0x0101 }
            if (r1 == 0) goto L_0x0097
            r14 = r16
            goto L_0x00a0
        L_0x0097:
            long r1 = r8.getLong(r14)     // Catch:{ SQLiteException -> 0x0105, all -> 0x0101 }
            java.lang.Long r1 = java.lang.Long.valueOf(r1)     // Catch:{ SQLiteException -> 0x0105, all -> 0x0101 }
            r14 = r1
        L_0x00a0:
            boolean r1 = r8.isNull(r0)     // Catch:{ SQLiteException -> 0x0105, all -> 0x0101 }
            if (r1 == 0) goto L_0x00aa
            r0 = r16
        L_0x00a8:
            r1 = 7
            goto L_0x00b3
        L_0x00aa:
            long r0 = r8.getLong(r0)     // Catch:{ SQLiteException -> 0x0105, all -> 0x0101 }
            java.lang.Long r0 = java.lang.Long.valueOf(r0)     // Catch:{ SQLiteException -> 0x0105, all -> 0x0101 }
            goto L_0x00a8
        L_0x00b3:
            boolean r2 = r8.isNull(r1)     // Catch:{ SQLiteException -> 0x0105, all -> 0x0101 }
            if (r2 != 0) goto L_0x00cb
            long r1 = r8.getLong(r1)     // Catch:{ SQLiteException -> 0x0105, all -> 0x0101 }
            r17 = 1
            int r3 = (r1 > r17 ? 1 : (r1 == r17 ? 0 : -1))
            if (r3 != 0) goto L_0x00c4
            r9 = 1
        L_0x00c4:
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r9)     // Catch:{ SQLiteException -> 0x0105, all -> 0x0101 }
            r17 = r1
            goto L_0x00cd
        L_0x00cb:
            r17 = r16
        L_0x00cd:
            com.google.android.gms.measurement.internal.zzaf r18 = new com.google.android.gms.measurement.internal.zzaf     // Catch:{ SQLiteException -> 0x0105, all -> 0x0101 }
            r1 = r18
            r2 = r23
            r3 = r24
            r21 = r8
            r8 = r19
            r10 = r11
            r12 = r13
            r13 = r14
            r14 = r0
            r15 = r17
            r1.<init>(r2, r3, r4, r6, r8, r10, r12, r13, r14, r15)     // Catch:{ SQLiteException -> 0x00ff }
            boolean r0 = r21.moveToNext()     // Catch:{ SQLiteException -> 0x00ff }
            if (r0 == 0) goto L_0x00f9
            com.google.android.gms.measurement.internal.zzau r0 = r22.zzad()     // Catch:{ SQLiteException -> 0x00ff }
            com.google.android.gms.measurement.internal.zzaw r0 = r0.zzda()     // Catch:{ SQLiteException -> 0x00ff }
            java.lang.String r1 = "Got multiple records for event aggregates, expected one. appId"
            java.lang.Object r2 = com.google.android.gms.measurement.internal.zzau.zzao(r23)     // Catch:{ SQLiteException -> 0x00ff }
            r0.zza(r1, r2)     // Catch:{ SQLiteException -> 0x00ff }
        L_0x00f9:
            if (r21 == 0) goto L_0x00fe
            r21.close()
        L_0x00fe:
            return r18
        L_0x00ff:
            r0 = move-exception
            goto L_0x0110
        L_0x0101:
            r0 = move-exception
            r21 = r8
            goto L_0x0132
        L_0x0105:
            r0 = move-exception
            r21 = r8
            goto L_0x0110
        L_0x0109:
            r0 = move-exception
            r21 = r16
            goto L_0x0132
        L_0x010d:
            r0 = move-exception
            r21 = r16
        L_0x0110:
            com.google.android.gms.measurement.internal.zzau r1 = r22.zzad()     // Catch:{ all -> 0x0131 }
            com.google.android.gms.measurement.internal.zzaw r1 = r1.zzda()     // Catch:{ all -> 0x0131 }
            java.lang.String r2 = "Error querying events. appId"
            java.lang.Object r3 = com.google.android.gms.measurement.internal.zzau.zzao(r23)     // Catch:{ all -> 0x0131 }
            com.google.android.gms.measurement.internal.zzas r4 = r22.zzaa()     // Catch:{ all -> 0x0131 }
            r5 = r24
            java.lang.String r4 = r4.zzal(r5)     // Catch:{ all -> 0x0131 }
            r1.zza(r2, r3, r4, r0)     // Catch:{ all -> 0x0131 }
            if (r21 == 0) goto L_0x0130
            r21.close()
        L_0x0130:
            return r16
        L_0x0131:
            r0 = move-exception
        L_0x0132:
            if (r21 == 0) goto L_0x0137
            r21.close()
        L_0x0137:
            throw r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zzw.zzc(java.lang.String, java.lang.String):com.google.android.gms.measurement.internal.zzaf");
    }

    @WorkerThread
    public final void zza(zzaf zzaf) {
        Preconditions.checkNotNull(zzaf);
        zzq();
        zzah();
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", zzaf.zzcf);
        contentValues.put(ConditionalUserProperty.NAME, zzaf.name);
        contentValues.put("lifetime_count", Long.valueOf(zzaf.zzfe));
        contentValues.put("current_bundle_count", Long.valueOf(zzaf.zzff));
        contentValues.put("last_fire_timestamp", Long.valueOf(zzaf.zzfg));
        contentValues.put("last_bundled_timestamp", Long.valueOf(zzaf.zzfh));
        contentValues.put("last_bundled_day", zzaf.zzfi);
        contentValues.put("last_sampled_complex_event_id", zzaf.zzfj);
        contentValues.put("last_sampling_rate", zzaf.zzfk);
        contentValues.put("last_exempt_from_sampling", (zzaf.zzfl == null || !zzaf.zzfl.booleanValue()) ? null : Long.valueOf(1));
        try {
            if (getWritableDatabase().insertWithOnConflict("events", null, contentValues, 5) == -1) {
                zzad().zzda().zza("Failed to insert/update event aggregates (got -1). appId", zzau.zzao(zzaf.zzcf));
            }
        } catch (SQLiteException e) {
            zzad().zzda().zza("Error storing event aggregates. appId", zzau.zzao(zzaf.zzcf), e);
        }
    }

    @WorkerThread
    public final void zzd(String str, String str2) {
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotEmpty(str2);
        zzq();
        zzah();
        try {
            zzad().zzdi().zza("Deleted user attribute rows", Integer.valueOf(getWritableDatabase().delete("user_attributes", "app_id=? and name=?", new String[]{str, str2})));
        } catch (SQLiteException e) {
            zzad().zzda().zza("Error deleting user attribute. appId", zzau.zzao(str), zzaa().zzan(str2), e);
        }
    }

    @WorkerThread
    public final boolean zza(zzgc zzgc) {
        Preconditions.checkNotNull(zzgc);
        zzq();
        zzah();
        if (zze(zzgc.zzcf, zzgc.name) == null) {
            if (zzgd.zzbm(zzgc.name)) {
                if (zza("select count(1) from user_attributes where app_id=? and name not like '!_%' escape '!'", new String[]{zzgc.zzcf}) >= 25) {
                    return false;
                }
            } else if (!zzaf().zze(zzgc.zzcf, zzal.zzin)) {
                if (zza("select count(1) from user_attributes where app_id=? and origin=? AND name like '!_%' escape '!'", new String[]{zzgc.zzcf, zzgc.origin}) >= 25) {
                    return false;
                }
            } else if (!"_npa".equals(zzgc.name)) {
                if (zza("select count(1) from user_attributes where app_id=? and origin=? AND name like '!_%' escape '!'", new String[]{zzgc.zzcf, zzgc.origin}) >= 25) {
                    return false;
                }
            }
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", zzgc.zzcf);
        contentValues.put("origin", zzgc.origin);
        contentValues.put(ConditionalUserProperty.NAME, zzgc.name);
        contentValues.put("set_timestamp", Long.valueOf(zzgc.zzsx));
        zza(contentValues, "value", zzgc.value);
        try {
            if (getWritableDatabase().insertWithOnConflict("user_attributes", null, contentValues, 5) == -1) {
                zzad().zzda().zza("Failed to insert/update user property (got -1). appId", zzau.zzao(zzgc.zzcf));
            }
        } catch (SQLiteException e) {
            zzad().zzda().zza("Error storing user property. appId", zzau.zzao(zzgc.zzcf), e);
        }
        return true;
    }

    /* JADX WARNING: Removed duplicated region for block: B:31:0x00a7  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x00ae  */
    @android.support.annotation.WorkerThread
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final com.google.android.gms.measurement.internal.zzgc zze(java.lang.String r19, java.lang.String r20) {
        /*
            r18 = this;
            r8 = r20
            com.google.android.gms.common.internal.Preconditions.checkNotEmpty(r19)
            com.google.android.gms.common.internal.Preconditions.checkNotEmpty(r20)
            r18.zzq()
            r18.zzah()
            r9 = 0
            android.database.sqlite.SQLiteDatabase r10 = r18.getWritableDatabase()     // Catch:{ SQLiteException -> 0x0088, all -> 0x0083 }
            java.lang.String r11 = "user_attributes"
            r0 = 3
            java.lang.String[] r12 = new java.lang.String[r0]     // Catch:{ SQLiteException -> 0x0088, all -> 0x0083 }
            java.lang.String r0 = "set_timestamp"
            r1 = 0
            r12[r1] = r0     // Catch:{ SQLiteException -> 0x0088, all -> 0x0083 }
            java.lang.String r0 = "value"
            r2 = 1
            r12[r2] = r0     // Catch:{ SQLiteException -> 0x0088, all -> 0x0083 }
            java.lang.String r0 = "origin"
            r3 = 2
            r12[r3] = r0     // Catch:{ SQLiteException -> 0x0088, all -> 0x0083 }
            java.lang.String r13 = "app_id=? and name=?"
            java.lang.String[] r14 = new java.lang.String[r3]     // Catch:{ SQLiteException -> 0x0088, all -> 0x0083 }
            r14[r1] = r19     // Catch:{ SQLiteException -> 0x0088, all -> 0x0083 }
            r14[r2] = r8     // Catch:{ SQLiteException -> 0x0088, all -> 0x0083 }
            r15 = 0
            r16 = 0
            r17 = 0
            android.database.Cursor r10 = r10.query(r11, r12, r13, r14, r15, r16, r17)     // Catch:{ SQLiteException -> 0x0088, all -> 0x0083 }
            boolean r0 = r10.moveToFirst()     // Catch:{ SQLiteException -> 0x007f, all -> 0x007b }
            if (r0 != 0) goto L_0x0044
            if (r10 == 0) goto L_0x0043
            r10.close()
        L_0x0043:
            return r9
        L_0x0044:
            long r5 = r10.getLong(r1)     // Catch:{ SQLiteException -> 0x007f, all -> 0x007b }
            r11 = r18
            java.lang.Object r7 = r11.zza(r10, r2)     // Catch:{ SQLiteException -> 0x0079 }
            java.lang.String r3 = r10.getString(r3)     // Catch:{ SQLiteException -> 0x0079 }
            com.google.android.gms.measurement.internal.zzgc r0 = new com.google.android.gms.measurement.internal.zzgc     // Catch:{ SQLiteException -> 0x0079 }
            r1 = r0
            r2 = r19
            r4 = r20
            r1.<init>(r2, r3, r4, r5, r7)     // Catch:{ SQLiteException -> 0x0079 }
            boolean r1 = r10.moveToNext()     // Catch:{ SQLiteException -> 0x0079 }
            if (r1 == 0) goto L_0x0073
            com.google.android.gms.measurement.internal.zzau r1 = r18.zzad()     // Catch:{ SQLiteException -> 0x0079 }
            com.google.android.gms.measurement.internal.zzaw r1 = r1.zzda()     // Catch:{ SQLiteException -> 0x0079 }
            java.lang.String r2 = "Got multiple records for user property, expected one. appId"
            java.lang.Object r3 = com.google.android.gms.measurement.internal.zzau.zzao(r19)     // Catch:{ SQLiteException -> 0x0079 }
            r1.zza(r2, r3)     // Catch:{ SQLiteException -> 0x0079 }
        L_0x0073:
            if (r10 == 0) goto L_0x0078
            r10.close()
        L_0x0078:
            return r0
        L_0x0079:
            r0 = move-exception
            goto L_0x008c
        L_0x007b:
            r0 = move-exception
            r11 = r18
            goto L_0x00ac
        L_0x007f:
            r0 = move-exception
            r11 = r18
            goto L_0x008c
        L_0x0083:
            r0 = move-exception
            r11 = r18
            r10 = r9
            goto L_0x00ac
        L_0x0088:
            r0 = move-exception
            r11 = r18
            r10 = r9
        L_0x008c:
            com.google.android.gms.measurement.internal.zzau r1 = r18.zzad()     // Catch:{ all -> 0x00ab }
            com.google.android.gms.measurement.internal.zzaw r1 = r1.zzda()     // Catch:{ all -> 0x00ab }
            java.lang.String r2 = "Error querying user property. appId"
            java.lang.Object r3 = com.google.android.gms.measurement.internal.zzau.zzao(r19)     // Catch:{ all -> 0x00ab }
            com.google.android.gms.measurement.internal.zzas r4 = r18.zzaa()     // Catch:{ all -> 0x00ab }
            java.lang.String r4 = r4.zzan(r8)     // Catch:{ all -> 0x00ab }
            r1.zza(r2, r3, r4, r0)     // Catch:{ all -> 0x00ab }
            if (r10 == 0) goto L_0x00aa
            r10.close()
        L_0x00aa:
            return r9
        L_0x00ab:
            r0 = move-exception
        L_0x00ac:
            if (r10 == 0) goto L_0x00b1
            r10.close()
        L_0x00b1:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zzw.zze(java.lang.String, java.lang.String):com.google.android.gms.measurement.internal.zzgc");
    }

    /* JADX WARNING: Removed duplicated region for block: B:38:0x00b4  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x00bb  */
    @android.support.annotation.WorkerThread
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.util.List<com.google.android.gms.measurement.internal.zzgc> zzad(java.lang.String r23) {
        /*
            r22 = this;
            com.google.android.gms.common.internal.Preconditions.checkNotEmpty(r23)
            r22.zzq()
            r22.zzah()
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r1 = 0
            android.database.sqlite.SQLiteDatabase r2 = r22.getWritableDatabase()     // Catch:{ SQLiteException -> 0x009d, all -> 0x0098 }
            java.lang.String r3 = "user_attributes"
            r4 = 4
            java.lang.String[] r4 = new java.lang.String[r4]     // Catch:{ SQLiteException -> 0x009d, all -> 0x0098 }
            java.lang.String r5 = "name"
            r11 = 0
            r4[r11] = r5     // Catch:{ SQLiteException -> 0x009d, all -> 0x0098 }
            java.lang.String r5 = "origin"
            r12 = 1
            r4[r12] = r5     // Catch:{ SQLiteException -> 0x009d, all -> 0x0098 }
            java.lang.String r5 = "set_timestamp"
            r13 = 2
            r4[r13] = r5     // Catch:{ SQLiteException -> 0x009d, all -> 0x0098 }
            java.lang.String r5 = "value"
            r14 = 3
            r4[r14] = r5     // Catch:{ SQLiteException -> 0x009d, all -> 0x0098 }
            java.lang.String r5 = "app_id=?"
            java.lang.String[] r6 = new java.lang.String[r12]     // Catch:{ SQLiteException -> 0x009d, all -> 0x0098 }
            r6[r11] = r23     // Catch:{ SQLiteException -> 0x009d, all -> 0x0098 }
            r7 = 0
            r8 = 0
            java.lang.String r9 = "rowid"
            java.lang.String r10 = "1000"
            android.database.Cursor r2 = r2.query(r3, r4, r5, r6, r7, r8, r9, r10)     // Catch:{ SQLiteException -> 0x009d, all -> 0x0098 }
            boolean r3 = r2.moveToFirst()     // Catch:{ SQLiteException -> 0x0094, all -> 0x0090 }
            if (r3 != 0) goto L_0x0048
            if (r2 == 0) goto L_0x0047
            r2.close()
        L_0x0047:
            return r0
        L_0x0048:
            java.lang.String r18 = r2.getString(r11)     // Catch:{ SQLiteException -> 0x0094, all -> 0x0090 }
            java.lang.String r3 = r2.getString(r12)     // Catch:{ SQLiteException -> 0x0094, all -> 0x0090 }
            if (r3 != 0) goto L_0x0057
            java.lang.String r3 = ""
            r17 = r3
            goto L_0x0059
        L_0x0057:
            r17 = r3
        L_0x0059:
            long r19 = r2.getLong(r13)     // Catch:{ SQLiteException -> 0x0094, all -> 0x0090 }
            r3 = r22
            java.lang.Object r21 = r3.zza(r2, r14)     // Catch:{ SQLiteException -> 0x008e }
            if (r21 != 0) goto L_0x0077
            com.google.android.gms.measurement.internal.zzau r4 = r22.zzad()     // Catch:{ SQLiteException -> 0x008e }
            com.google.android.gms.measurement.internal.zzaw r4 = r4.zzda()     // Catch:{ SQLiteException -> 0x008e }
            java.lang.String r5 = "Read invalid user property value, ignoring it. appId"
            java.lang.Object r6 = com.google.android.gms.measurement.internal.zzau.zzao(r23)     // Catch:{ SQLiteException -> 0x008e }
            r4.zza(r5, r6)     // Catch:{ SQLiteException -> 0x008e }
            goto L_0x0082
        L_0x0077:
            com.google.android.gms.measurement.internal.zzgc r4 = new com.google.android.gms.measurement.internal.zzgc     // Catch:{ SQLiteException -> 0x008e }
            r15 = r4
            r16 = r23
            r15.<init>(r16, r17, r18, r19, r21)     // Catch:{ SQLiteException -> 0x008e }
            r0.add(r4)     // Catch:{ SQLiteException -> 0x008e }
        L_0x0082:
            boolean r4 = r2.moveToNext()     // Catch:{ SQLiteException -> 0x008e }
            if (r4 != 0) goto L_0x0048
            if (r2 == 0) goto L_0x008d
            r2.close()
        L_0x008d:
            return r0
        L_0x008e:
            r0 = move-exception
            goto L_0x00a1
        L_0x0090:
            r0 = move-exception
            r3 = r22
            goto L_0x00b9
        L_0x0094:
            r0 = move-exception
            r3 = r22
            goto L_0x00a1
        L_0x0098:
            r0 = move-exception
            r3 = r22
            r2 = r1
            goto L_0x00b9
        L_0x009d:
            r0 = move-exception
            r3 = r22
            r2 = r1
        L_0x00a1:
            com.google.android.gms.measurement.internal.zzau r4 = r22.zzad()     // Catch:{ all -> 0x00b8 }
            com.google.android.gms.measurement.internal.zzaw r4 = r4.zzda()     // Catch:{ all -> 0x00b8 }
            java.lang.String r5 = "Error querying user properties. appId"
            java.lang.Object r6 = com.google.android.gms.measurement.internal.zzau.zzao(r23)     // Catch:{ all -> 0x00b8 }
            r4.zza(r5, r6, r0)     // Catch:{ all -> 0x00b8 }
            if (r2 == 0) goto L_0x00b7
            r2.close()
        L_0x00b7:
            return r1
        L_0x00b8:
            r0 = move-exception
        L_0x00b9:
            if (r2 == 0) goto L_0x00be
            r2.close()
        L_0x00be:
            throw r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zzw.zzad(java.lang.String):java.util.List");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:55:0x011a, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x011b, code lost:
        r14 = r21;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x0122, code lost:
        r0 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x0123, code lost:
        r14 = r21;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x0126, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x0127, code lost:
        r14 = r21;
        r11 = r22;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x0122 A[ExcHandler: all (th java.lang.Throwable), Splitter:B:1:0x000f] */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x0141  */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x0149  */
    @android.support.annotation.WorkerThread
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.util.List<com.google.android.gms.measurement.internal.zzgc> zza(java.lang.String r22, java.lang.String r23, java.lang.String r24) {
        /*
            r21 = this;
            com.google.android.gms.common.internal.Preconditions.checkNotEmpty(r22)
            r21.zzq()
            r21.zzah()
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r1 = 0
            java.util.ArrayList r2 = new java.util.ArrayList     // Catch:{ SQLiteException -> 0x0126, all -> 0x0122 }
            r3 = 3
            r2.<init>(r3)     // Catch:{ SQLiteException -> 0x0126, all -> 0x0122 }
            r11 = r22
            r2.add(r11)     // Catch:{ SQLiteException -> 0x011e, all -> 0x0122 }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ SQLiteException -> 0x011e, all -> 0x0122 }
            java.lang.String r5 = "app_id=?"
            r4.<init>(r5)     // Catch:{ SQLiteException -> 0x011e, all -> 0x0122 }
            boolean r5 = android.text.TextUtils.isEmpty(r23)     // Catch:{ SQLiteException -> 0x011e, all -> 0x0122 }
            if (r5 != 0) goto L_0x0032
            r5 = r23
            r2.add(r5)     // Catch:{ SQLiteException -> 0x011a, all -> 0x0122 }
            java.lang.String r6 = " and origin=?"
            r4.append(r6)     // Catch:{ SQLiteException -> 0x011a, all -> 0x0122 }
            goto L_0x0034
        L_0x0032:
            r5 = r23
        L_0x0034:
            boolean r6 = android.text.TextUtils.isEmpty(r24)     // Catch:{ SQLiteException -> 0x011a, all -> 0x0122 }
            if (r6 != 0) goto L_0x004c
            java.lang.String r6 = java.lang.String.valueOf(r24)     // Catch:{ SQLiteException -> 0x011a, all -> 0x0122 }
            java.lang.String r7 = "*"
            java.lang.String r6 = r6.concat(r7)     // Catch:{ SQLiteException -> 0x011a, all -> 0x0122 }
            r2.add(r6)     // Catch:{ SQLiteException -> 0x011a, all -> 0x0122 }
            java.lang.String r6 = " and name glob ?"
            r4.append(r6)     // Catch:{ SQLiteException -> 0x011a, all -> 0x0122 }
        L_0x004c:
            int r6 = r2.size()     // Catch:{ SQLiteException -> 0x011a, all -> 0x0122 }
            java.lang.String[] r6 = new java.lang.String[r6]     // Catch:{ SQLiteException -> 0x011a, all -> 0x0122 }
            java.lang.Object[] r2 = r2.toArray(r6)     // Catch:{ SQLiteException -> 0x011a, all -> 0x0122 }
            r16 = r2
            java.lang.String[] r16 = (java.lang.String[]) r16     // Catch:{ SQLiteException -> 0x011a, all -> 0x0122 }
            android.database.sqlite.SQLiteDatabase r12 = r21.getWritableDatabase()     // Catch:{ SQLiteException -> 0x011a, all -> 0x0122 }
            java.lang.String r13 = "user_attributes"
            r2 = 4
            java.lang.String[] r14 = new java.lang.String[r2]     // Catch:{ SQLiteException -> 0x011a, all -> 0x0122 }
            java.lang.String r2 = "name"
            r10 = 0
            r14[r10] = r2     // Catch:{ SQLiteException -> 0x011a, all -> 0x0122 }
            java.lang.String r2 = "set_timestamp"
            r8 = 1
            r14[r8] = r2     // Catch:{ SQLiteException -> 0x011a, all -> 0x0122 }
            java.lang.String r2 = "value"
            r9 = 2
            r14[r9] = r2     // Catch:{ SQLiteException -> 0x011a, all -> 0x0122 }
            java.lang.String r2 = "origin"
            r14[r3] = r2     // Catch:{ SQLiteException -> 0x011a, all -> 0x0122 }
            java.lang.String r15 = r4.toString()     // Catch:{ SQLiteException -> 0x011a, all -> 0x0122 }
            r17 = 0
            r18 = 0
            java.lang.String r19 = "rowid"
            java.lang.String r20 = "1001"
            android.database.Cursor r2 = r12.query(r13, r14, r15, r16, r17, r18, r19, r20)     // Catch:{ SQLiteException -> 0x011a, all -> 0x0122 }
            boolean r4 = r2.moveToFirst()     // Catch:{ SQLiteException -> 0x0116, all -> 0x0112 }
            if (r4 != 0) goto L_0x0092
            if (r2 == 0) goto L_0x0091
            r2.close()
        L_0x0091:
            return r0
        L_0x0092:
            int r4 = r0.size()     // Catch:{ SQLiteException -> 0x0116, all -> 0x0112 }
            r6 = 1000(0x3e8, float:1.401E-42)
            if (r4 < r6) goto L_0x00ae
            com.google.android.gms.measurement.internal.zzau r3 = r21.zzad()     // Catch:{ SQLiteException -> 0x0116, all -> 0x0112 }
            com.google.android.gms.measurement.internal.zzaw r3 = r3.zzda()     // Catch:{ SQLiteException -> 0x0116, all -> 0x0112 }
            java.lang.String r4 = "Read more than the max allowed user properties, ignoring excess"
            java.lang.Integer r6 = java.lang.Integer.valueOf(r6)     // Catch:{ SQLiteException -> 0x0116, all -> 0x0112 }
            r3.zza(r4, r6)     // Catch:{ SQLiteException -> 0x0116, all -> 0x0112 }
            r14 = r21
            goto L_0x00fb
        L_0x00ae:
            java.lang.String r7 = r2.getString(r10)     // Catch:{ SQLiteException -> 0x0116, all -> 0x0112 }
            long r12 = r2.getLong(r8)     // Catch:{ SQLiteException -> 0x0116, all -> 0x0112 }
            r14 = r21
            java.lang.Object r15 = r14.zza(r2, r9)     // Catch:{ SQLiteException -> 0x0110 }
            java.lang.String r6 = r2.getString(r3)     // Catch:{ SQLiteException -> 0x0110 }
            if (r15 != 0) goto L_0x00e0
            com.google.android.gms.measurement.internal.zzau r4 = r21.zzad()     // Catch:{ SQLiteException -> 0x00dd }
            com.google.android.gms.measurement.internal.zzaw r4 = r4.zzda()     // Catch:{ SQLiteException -> 0x00dd }
            java.lang.String r5 = "(2)Read invalid user property value, ignoring it"
            java.lang.Object r7 = com.google.android.gms.measurement.internal.zzau.zzao(r22)     // Catch:{ SQLiteException -> 0x00dd }
            r12 = r24
            r4.zza(r5, r7, r6, r12)     // Catch:{ SQLiteException -> 0x00dd }
            r17 = r6
            r12 = 0
            r18 = 1
            r19 = 2
            goto L_0x00f5
        L_0x00dd:
            r0 = move-exception
            r5 = r6
            goto L_0x012e
        L_0x00e0:
            com.google.android.gms.measurement.internal.zzgc r5 = new com.google.android.gms.measurement.internal.zzgc     // Catch:{ SQLiteException -> 0x010a }
            r4 = r5
            r3 = r5
            r5 = r22
            r17 = r6
            r18 = 1
            r19 = 2
            r8 = r12
            r12 = 0
            r10 = r15
            r4.<init>(r5, r6, r7, r8, r10)     // Catch:{ SQLiteException -> 0x0108 }
            r0.add(r3)     // Catch:{ SQLiteException -> 0x0108 }
        L_0x00f5:
            boolean r3 = r2.moveToNext()     // Catch:{ SQLiteException -> 0x0108 }
            if (r3 != 0) goto L_0x0101
        L_0x00fb:
            if (r2 == 0) goto L_0x0100
            r2.close()
        L_0x0100:
            return r0
        L_0x0101:
            r5 = r17
            r3 = 3
            r8 = 1
            r9 = 2
            r10 = 0
            goto L_0x0092
        L_0x0108:
            r0 = move-exception
            goto L_0x010d
        L_0x010a:
            r0 = move-exception
            r17 = r6
        L_0x010d:
            r5 = r17
            goto L_0x012e
        L_0x0110:
            r0 = move-exception
            goto L_0x012e
        L_0x0112:
            r0 = move-exception
            r14 = r21
            goto L_0x0146
        L_0x0116:
            r0 = move-exception
            r14 = r21
            goto L_0x012e
        L_0x011a:
            r0 = move-exception
            r14 = r21
            goto L_0x012d
        L_0x011e:
            r0 = move-exception
            r14 = r21
            goto L_0x012b
        L_0x0122:
            r0 = move-exception
            r14 = r21
            goto L_0x0147
        L_0x0126:
            r0 = move-exception
            r14 = r21
            r11 = r22
        L_0x012b:
            r5 = r23
        L_0x012d:
            r2 = r1
        L_0x012e:
            com.google.android.gms.measurement.internal.zzau r3 = r21.zzad()     // Catch:{ all -> 0x0145 }
            com.google.android.gms.measurement.internal.zzaw r3 = r3.zzda()     // Catch:{ all -> 0x0145 }
            java.lang.String r4 = "(2)Error querying user properties"
            java.lang.Object r6 = com.google.android.gms.measurement.internal.zzau.zzao(r22)     // Catch:{ all -> 0x0145 }
            r3.zza(r4, r6, r5, r0)     // Catch:{ all -> 0x0145 }
            if (r2 == 0) goto L_0x0144
            r2.close()
        L_0x0144:
            return r1
        L_0x0145:
            r0 = move-exception
        L_0x0146:
            r1 = r2
        L_0x0147:
            if (r1 == 0) goto L_0x014c
            r1.close()
        L_0x014c:
            throw r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zzw.zza(java.lang.String, java.lang.String, java.lang.String):java.util.List");
    }

    @WorkerThread
    public final boolean zza(zzr zzr) {
        Preconditions.checkNotNull(zzr);
        zzq();
        zzah();
        if (zze(zzr.packageName, zzr.zzdv.name) == null) {
            if (zza("SELECT COUNT(1) FROM conditional_properties WHERE app_id=?", new String[]{zzr.packageName}) >= 1000) {
                return false;
            }
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", zzr.packageName);
        contentValues.put("origin", zzr.origin);
        contentValues.put(ConditionalUserProperty.NAME, zzr.zzdv.name);
        zza(contentValues, "value", zzr.zzdv.getValue());
        contentValues.put("active", Boolean.valueOf(zzr.active));
        contentValues.put(ConditionalUserProperty.TRIGGER_EVENT_NAME, zzr.triggerEventName);
        contentValues.put(ConditionalUserProperty.TRIGGER_TIMEOUT, Long.valueOf(zzr.triggerTimeout));
        zzab();
        contentValues.put("timed_out_event", zzgd.zza((Parcelable) zzr.zzdw));
        contentValues.put(ConditionalUserProperty.CREATION_TIMESTAMP, Long.valueOf(zzr.creationTimestamp));
        zzab();
        contentValues.put("triggered_event", zzgd.zza((Parcelable) zzr.zzdx));
        contentValues.put(ConditionalUserProperty.TRIGGERED_TIMESTAMP, Long.valueOf(zzr.zzdv.zzsx));
        contentValues.put(ConditionalUserProperty.TIME_TO_LIVE, Long.valueOf(zzr.timeToLive));
        zzab();
        contentValues.put("expired_event", zzgd.zza((Parcelable) zzr.zzdy));
        try {
            if (getWritableDatabase().insertWithOnConflict("conditional_properties", null, contentValues, 5) == -1) {
                zzad().zzda().zza("Failed to insert/update conditional user property (got -1)", zzau.zzao(zzr.packageName));
            }
        } catch (SQLiteException e) {
            zzad().zzda().zza("Error storing conditional user property", zzau.zzao(zzr.packageName), e);
        }
        return true;
    }

    /* JADX WARNING: Removed duplicated region for block: B:35:0x014e  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x0155  */
    @android.support.annotation.WorkerThread
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final com.google.android.gms.measurement.internal.zzr zzf(java.lang.String r33, java.lang.String r34) {
        /*
            r32 = this;
            r7 = r34
            com.google.android.gms.common.internal.Preconditions.checkNotEmpty(r33)
            com.google.android.gms.common.internal.Preconditions.checkNotEmpty(r34)
            r32.zzq()
            r32.zzah()
            r8 = 0
            android.database.sqlite.SQLiteDatabase r9 = r32.getWritableDatabase()     // Catch:{ SQLiteException -> 0x012f, all -> 0x012a }
            java.lang.String r10 = "conditional_properties"
            r0 = 11
            java.lang.String[] r11 = new java.lang.String[r0]     // Catch:{ SQLiteException -> 0x012f, all -> 0x012a }
            java.lang.String r0 = "origin"
            r1 = 0
            r11[r1] = r0     // Catch:{ SQLiteException -> 0x012f, all -> 0x012a }
            java.lang.String r0 = "value"
            r2 = 1
            r11[r2] = r0     // Catch:{ SQLiteException -> 0x012f, all -> 0x012a }
            java.lang.String r0 = "active"
            r3 = 2
            r11[r3] = r0     // Catch:{ SQLiteException -> 0x012f, all -> 0x012a }
            java.lang.String r0 = "trigger_event_name"
            r4 = 3
            r11[r4] = r0     // Catch:{ SQLiteException -> 0x012f, all -> 0x012a }
            java.lang.String r0 = "trigger_timeout"
            r5 = 4
            r11[r5] = r0     // Catch:{ SQLiteException -> 0x012f, all -> 0x012a }
            java.lang.String r0 = "timed_out_event"
            r6 = 5
            r11[r6] = r0     // Catch:{ SQLiteException -> 0x012f, all -> 0x012a }
            java.lang.String r0 = "creation_timestamp"
            r15 = 6
            r11[r15] = r0     // Catch:{ SQLiteException -> 0x012f, all -> 0x012a }
            java.lang.String r0 = "triggered_event"
            r14 = 7
            r11[r14] = r0     // Catch:{ SQLiteException -> 0x012f, all -> 0x012a }
            java.lang.String r0 = "triggered_timestamp"
            r13 = 8
            r11[r13] = r0     // Catch:{ SQLiteException -> 0x012f, all -> 0x012a }
            java.lang.String r0 = "time_to_live"
            r12 = 9
            r11[r12] = r0     // Catch:{ SQLiteException -> 0x012f, all -> 0x012a }
            java.lang.String r0 = "expired_event"
            r6 = 10
            r11[r6] = r0     // Catch:{ SQLiteException -> 0x012f, all -> 0x012a }
            java.lang.String r0 = "app_id=? and name=?"
            java.lang.String[] r13 = new java.lang.String[r3]     // Catch:{ SQLiteException -> 0x012f, all -> 0x012a }
            r13[r1] = r33     // Catch:{ SQLiteException -> 0x012f, all -> 0x012a }
            r13[r2] = r7     // Catch:{ SQLiteException -> 0x012f, all -> 0x012a }
            r17 = 0
            r18 = 0
            r19 = 0
            r6 = 9
            r12 = r0
            r0 = 8
            r6 = 7
            r14 = r17
            r0 = 6
            r15 = r18
            r16 = r19
            android.database.Cursor r9 = r9.query(r10, r11, r12, r13, r14, r15, r16)     // Catch:{ SQLiteException -> 0x012f, all -> 0x012a }
            boolean r10 = r9.moveToFirst()     // Catch:{ SQLiteException -> 0x0126, all -> 0x0122 }
            if (r10 != 0) goto L_0x007e
            if (r9 == 0) goto L_0x007d
            r9.close()
        L_0x007d:
            return r8
        L_0x007e:
            java.lang.String r19 = r9.getString(r1)     // Catch:{ SQLiteException -> 0x0126, all -> 0x0122 }
            r10 = r32
            java.lang.Object r11 = r10.zza(r9, r2)     // Catch:{ SQLiteException -> 0x0120 }
            int r3 = r9.getInt(r3)     // Catch:{ SQLiteException -> 0x0120 }
            if (r3 == 0) goto L_0x0091
            r23 = 1
            goto L_0x0093
        L_0x0091:
            r23 = 0
        L_0x0093:
            java.lang.String r24 = r9.getString(r4)     // Catch:{ SQLiteException -> 0x0120 }
            long r26 = r9.getLong(r5)     // Catch:{ SQLiteException -> 0x0120 }
            com.google.android.gms.measurement.internal.zzfz r1 = r32.zzdm()     // Catch:{ SQLiteException -> 0x0120 }
            r2 = 5
            byte[] r2 = r9.getBlob(r2)     // Catch:{ SQLiteException -> 0x0120 }
            android.os.Parcelable$Creator<com.google.android.gms.measurement.internal.zzaj> r3 = com.google.android.gms.measurement.internal.zzaj.CREATOR     // Catch:{ SQLiteException -> 0x0120 }
            android.os.Parcelable r1 = r1.zza(r2, r3)     // Catch:{ SQLiteException -> 0x0120 }
            r25 = r1
            com.google.android.gms.measurement.internal.zzaj r25 = (com.google.android.gms.measurement.internal.zzaj) r25     // Catch:{ SQLiteException -> 0x0120 }
            long r12 = r9.getLong(r0)     // Catch:{ SQLiteException -> 0x0120 }
            com.google.android.gms.measurement.internal.zzfz r0 = r32.zzdm()     // Catch:{ SQLiteException -> 0x0120 }
            byte[] r1 = r9.getBlob(r6)     // Catch:{ SQLiteException -> 0x0120 }
            android.os.Parcelable$Creator<com.google.android.gms.measurement.internal.zzaj> r2 = com.google.android.gms.measurement.internal.zzaj.CREATOR     // Catch:{ SQLiteException -> 0x0120 }
            android.os.Parcelable r0 = r0.zza(r1, r2)     // Catch:{ SQLiteException -> 0x0120 }
            r28 = r0
            com.google.android.gms.measurement.internal.zzaj r28 = (com.google.android.gms.measurement.internal.zzaj) r28     // Catch:{ SQLiteException -> 0x0120 }
            r0 = 8
            long r3 = r9.getLong(r0)     // Catch:{ SQLiteException -> 0x0120 }
            r0 = 9
            long r29 = r9.getLong(r0)     // Catch:{ SQLiteException -> 0x0120 }
            com.google.android.gms.measurement.internal.zzfz r0 = r32.zzdm()     // Catch:{ SQLiteException -> 0x0120 }
            r1 = 10
            byte[] r1 = r9.getBlob(r1)     // Catch:{ SQLiteException -> 0x0120 }
            android.os.Parcelable$Creator<com.google.android.gms.measurement.internal.zzaj> r2 = com.google.android.gms.measurement.internal.zzaj.CREATOR     // Catch:{ SQLiteException -> 0x0120 }
            android.os.Parcelable r0 = r0.zza(r1, r2)     // Catch:{ SQLiteException -> 0x0120 }
            r31 = r0
            com.google.android.gms.measurement.internal.zzaj r31 = (com.google.android.gms.measurement.internal.zzaj) r31     // Catch:{ SQLiteException -> 0x0120 }
            com.google.android.gms.measurement.internal.zzga r20 = new com.google.android.gms.measurement.internal.zzga     // Catch:{ SQLiteException -> 0x0120 }
            r1 = r20
            r2 = r34
            r5 = r11
            r6 = r19
            r1.<init>(r2, r3, r5, r6)     // Catch:{ SQLiteException -> 0x0120 }
            com.google.android.gms.measurement.internal.zzr r0 = new com.google.android.gms.measurement.internal.zzr     // Catch:{ SQLiteException -> 0x0120 }
            r17 = r0
            r18 = r33
            r21 = r12
            r17.<init>(r18, r19, r20, r21, r23, r24, r25, r26, r28, r29, r31)     // Catch:{ SQLiteException -> 0x0120 }
            boolean r1 = r9.moveToNext()     // Catch:{ SQLiteException -> 0x0120 }
            if (r1 == 0) goto L_0x011a
            com.google.android.gms.measurement.internal.zzau r1 = r32.zzad()     // Catch:{ SQLiteException -> 0x0120 }
            com.google.android.gms.measurement.internal.zzaw r1 = r1.zzda()     // Catch:{ SQLiteException -> 0x0120 }
            java.lang.String r2 = "Got multiple records for conditional property, expected one"
            java.lang.Object r3 = com.google.android.gms.measurement.internal.zzau.zzao(r33)     // Catch:{ SQLiteException -> 0x0120 }
            com.google.android.gms.measurement.internal.zzas r4 = r32.zzaa()     // Catch:{ SQLiteException -> 0x0120 }
            java.lang.String r4 = r4.zzan(r7)     // Catch:{ SQLiteException -> 0x0120 }
            r1.zza(r2, r3, r4)     // Catch:{ SQLiteException -> 0x0120 }
        L_0x011a:
            if (r9 == 0) goto L_0x011f
            r9.close()
        L_0x011f:
            return r0
        L_0x0120:
            r0 = move-exception
            goto L_0x0133
        L_0x0122:
            r0 = move-exception
            r10 = r32
            goto L_0x0153
        L_0x0126:
            r0 = move-exception
            r10 = r32
            goto L_0x0133
        L_0x012a:
            r0 = move-exception
            r10 = r32
            r9 = r8
            goto L_0x0153
        L_0x012f:
            r0 = move-exception
            r10 = r32
            r9 = r8
        L_0x0133:
            com.google.android.gms.measurement.internal.zzau r1 = r32.zzad()     // Catch:{ all -> 0x0152 }
            com.google.android.gms.measurement.internal.zzaw r1 = r1.zzda()     // Catch:{ all -> 0x0152 }
            java.lang.String r2 = "Error querying conditional property"
            java.lang.Object r3 = com.google.android.gms.measurement.internal.zzau.zzao(r33)     // Catch:{ all -> 0x0152 }
            com.google.android.gms.measurement.internal.zzas r4 = r32.zzaa()     // Catch:{ all -> 0x0152 }
            java.lang.String r4 = r4.zzan(r7)     // Catch:{ all -> 0x0152 }
            r1.zza(r2, r3, r4, r0)     // Catch:{ all -> 0x0152 }
            if (r9 == 0) goto L_0x0151
            r9.close()
        L_0x0151:
            return r8
        L_0x0152:
            r0 = move-exception
        L_0x0153:
            if (r9 == 0) goto L_0x0158
            r9.close()
        L_0x0158:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zzw.zzf(java.lang.String, java.lang.String):com.google.android.gms.measurement.internal.zzr");
    }

    @WorkerThread
    public final int zzg(String str, String str2) {
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotEmpty(str2);
        zzq();
        zzah();
        try {
            return getWritableDatabase().delete("conditional_properties", "app_id=? and name=?", new String[]{str, str2});
        } catch (SQLiteException e) {
            zzad().zzda().zza("Error deleting conditional property", zzau.zzao(str), zzaa().zzan(str2), e);
            return 0;
        }
    }

    @WorkerThread
    public final List<zzr> zzb(String str, String str2, String str3) {
        Preconditions.checkNotEmpty(str);
        zzq();
        zzah();
        ArrayList arrayList = new ArrayList(3);
        arrayList.add(str);
        StringBuilder sb = new StringBuilder("app_id=?");
        if (!TextUtils.isEmpty(str2)) {
            arrayList.add(str2);
            sb.append(" and origin=?");
        }
        if (!TextUtils.isEmpty(str3)) {
            arrayList.add(String.valueOf(str3).concat("*"));
            sb.append(" and name glob ?");
        }
        return zzb(sb.toString(), (String[]) arrayList.toArray(new String[arrayList.size()]));
    }

    /* JADX WARNING: Removed duplicated region for block: B:33:0x016d  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0175  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.util.List<com.google.android.gms.measurement.internal.zzr> zzb(java.lang.String r40, java.lang.String[] r41) {
        /*
            r39 = this;
            r39.zzq()
            r39.zzah()
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            android.database.sqlite.SQLiteDatabase r2 = r39.getWritableDatabase()     // Catch:{ SQLiteException -> 0x0158, all -> 0x0155 }
            java.lang.String r3 = "conditional_properties"
            r4 = 13
            java.lang.String[] r4 = new java.lang.String[r4]     // Catch:{ SQLiteException -> 0x0158, all -> 0x0155 }
            java.lang.String r5 = "app_id"
            r11 = 0
            r4[r11] = r5     // Catch:{ SQLiteException -> 0x0158, all -> 0x0155 }
            java.lang.String r5 = "origin"
            r12 = 1
            r4[r12] = r5     // Catch:{ SQLiteException -> 0x0158, all -> 0x0155 }
            java.lang.String r5 = "name"
            r13 = 2
            r4[r13] = r5     // Catch:{ SQLiteException -> 0x0158, all -> 0x0155 }
            java.lang.String r5 = "value"
            r14 = 3
            r4[r14] = r5     // Catch:{ SQLiteException -> 0x0158, all -> 0x0155 }
            java.lang.String r5 = "active"
            r15 = 4
            r4[r15] = r5     // Catch:{ SQLiteException -> 0x0158, all -> 0x0155 }
            java.lang.String r5 = "trigger_event_name"
            r10 = 5
            r4[r10] = r5     // Catch:{ SQLiteException -> 0x0158, all -> 0x0155 }
            java.lang.String r5 = "trigger_timeout"
            r9 = 6
            r4[r9] = r5     // Catch:{ SQLiteException -> 0x0158, all -> 0x0155 }
            java.lang.String r5 = "timed_out_event"
            r8 = 7
            r4[r8] = r5     // Catch:{ SQLiteException -> 0x0158, all -> 0x0155 }
            java.lang.String r5 = "creation_timestamp"
            r7 = 8
            r4[r7] = r5     // Catch:{ SQLiteException -> 0x0158, all -> 0x0155 }
            java.lang.String r5 = "triggered_event"
            r6 = 9
            r4[r6] = r5     // Catch:{ SQLiteException -> 0x0158, all -> 0x0155 }
            java.lang.String r5 = "triggered_timestamp"
            r1 = 10
            r4[r1] = r5     // Catch:{ SQLiteException -> 0x0158, all -> 0x0155 }
            java.lang.String r5 = "time_to_live"
            r1 = 11
            r4[r1] = r5     // Catch:{ SQLiteException -> 0x0158, all -> 0x0155 }
            java.lang.String r5 = "expired_event"
            r1 = 12
            r4[r1] = r5     // Catch:{ SQLiteException -> 0x0158, all -> 0x0155 }
            r19 = 0
            r20 = 0
            java.lang.String r21 = "rowid"
            java.lang.String r22 = "1001"
            r5 = r40
            r1 = 9
            r6 = r41
            r1 = 8
            r7 = r19
            r1 = 7
            r8 = r20
            r1 = 6
            r9 = r21
            r1 = 5
            r10 = r22
            android.database.Cursor r2 = r2.query(r3, r4, r5, r6, r7, r8, r9, r10)     // Catch:{ SQLiteException -> 0x0158, all -> 0x0155 }
            boolean r3 = r2.moveToFirst()     // Catch:{ SQLiteException -> 0x0152, all -> 0x0150 }
            if (r3 != 0) goto L_0x0086
            if (r2 == 0) goto L_0x0085
            r2.close()
        L_0x0085:
            return r0
        L_0x0086:
            int r3 = r0.size()     // Catch:{ SQLiteException -> 0x0152, all -> 0x0150 }
            r4 = 1000(0x3e8, float:1.401E-42)
            if (r3 < r4) goto L_0x00a1
            com.google.android.gms.measurement.internal.zzau r1 = r39.zzad()     // Catch:{ SQLiteException -> 0x0152, all -> 0x0150 }
            com.google.android.gms.measurement.internal.zzaw r1 = r1.zzda()     // Catch:{ SQLiteException -> 0x0152, all -> 0x0150 }
            java.lang.String r3 = "Read more than the max allowed conditional properties, ignoring extra"
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)     // Catch:{ SQLiteException -> 0x0152, all -> 0x0150 }
            r1.zza(r3, r4)     // Catch:{ SQLiteException -> 0x0152, all -> 0x0150 }
            goto L_0x0146
        L_0x00a1:
            java.lang.String r3 = r2.getString(r11)     // Catch:{ SQLiteException -> 0x0152, all -> 0x0150 }
            java.lang.String r10 = r2.getString(r12)     // Catch:{ SQLiteException -> 0x0152, all -> 0x0150 }
            java.lang.String r5 = r2.getString(r13)     // Catch:{ SQLiteException -> 0x0152, all -> 0x0150 }
            r9 = r39
            java.lang.Object r8 = r9.zza(r2, r14)     // Catch:{ SQLiteException -> 0x0152, all -> 0x0150 }
            int r4 = r2.getInt(r15)     // Catch:{ SQLiteException -> 0x0152, all -> 0x0150 }
            if (r4 == 0) goto L_0x00bc
            r22 = 1
            goto L_0x00be
        L_0x00bc:
            r22 = 0
        L_0x00be:
            java.lang.String r24 = r2.getString(r1)     // Catch:{ SQLiteException -> 0x0152, all -> 0x0150 }
            r6 = 6
            long r25 = r2.getLong(r6)     // Catch:{ SQLiteException -> 0x0152, all -> 0x0150 }
            com.google.android.gms.measurement.internal.zzfz r4 = r39.zzdm()     // Catch:{ SQLiteException -> 0x0152, all -> 0x0150 }
            r7 = 7
            byte[] r1 = r2.getBlob(r7)     // Catch:{ SQLiteException -> 0x0152, all -> 0x0150 }
            android.os.Parcelable$Creator<com.google.android.gms.measurement.internal.zzaj> r6 = com.google.android.gms.measurement.internal.zzaj.CREATOR     // Catch:{ SQLiteException -> 0x0152, all -> 0x0150 }
            android.os.Parcelable r1 = r4.zza(r1, r6)     // Catch:{ SQLiteException -> 0x0152, all -> 0x0150 }
            com.google.android.gms.measurement.internal.zzaj r1 = (com.google.android.gms.measurement.internal.zzaj) r1     // Catch:{ SQLiteException -> 0x0152, all -> 0x0150 }
            r6 = 8
            long r27 = r2.getLong(r6)     // Catch:{ SQLiteException -> 0x0152, all -> 0x0150 }
            com.google.android.gms.measurement.internal.zzfz r4 = r39.zzdm()     // Catch:{ SQLiteException -> 0x0152, all -> 0x0150 }
            r11 = 9
            byte[] r6 = r2.getBlob(r11)     // Catch:{ SQLiteException -> 0x0152, all -> 0x0150 }
            android.os.Parcelable$Creator<com.google.android.gms.measurement.internal.zzaj> r7 = com.google.android.gms.measurement.internal.zzaj.CREATOR     // Catch:{ SQLiteException -> 0x0152, all -> 0x0150 }
            android.os.Parcelable r4 = r4.zza(r6, r7)     // Catch:{ SQLiteException -> 0x0152, all -> 0x0150 }
            r29 = r4
            com.google.android.gms.measurement.internal.zzaj r29 = (com.google.android.gms.measurement.internal.zzaj) r29     // Catch:{ SQLiteException -> 0x0152, all -> 0x0150 }
            r6 = 10
            long r16 = r2.getLong(r6)     // Catch:{ SQLiteException -> 0x0152, all -> 0x0150 }
            r7 = 11
            long r31 = r2.getLong(r7)     // Catch:{ SQLiteException -> 0x0152, all -> 0x0150 }
            com.google.android.gms.measurement.internal.zzfz r4 = r39.zzdm()     // Catch:{ SQLiteException -> 0x0152, all -> 0x0150 }
            r11 = 12
            byte[] r6 = r2.getBlob(r11)     // Catch:{ SQLiteException -> 0x0152, all -> 0x0150 }
            android.os.Parcelable$Creator<com.google.android.gms.measurement.internal.zzaj> r7 = com.google.android.gms.measurement.internal.zzaj.CREATOR     // Catch:{ SQLiteException -> 0x0152, all -> 0x0150 }
            android.os.Parcelable r4 = r4.zza(r6, r7)     // Catch:{ SQLiteException -> 0x0152, all -> 0x0150 }
            r30 = r4
            com.google.android.gms.measurement.internal.zzaj r30 = (com.google.android.gms.measurement.internal.zzaj) r30     // Catch:{ SQLiteException -> 0x0152, all -> 0x0150 }
            com.google.android.gms.measurement.internal.zzga r33 = new com.google.android.gms.measurement.internal.zzga     // Catch:{ SQLiteException -> 0x0152, all -> 0x0150 }
            r4 = r33
            r34 = 6
            r35 = 7
            r36 = 8
            r37 = 10
            r38 = 11
            r6 = r16
            r9 = r10
            r4.<init>(r5, r6, r8, r9)     // Catch:{ SQLiteException -> 0x0152, all -> 0x0150 }
            com.google.android.gms.measurement.internal.zzr r4 = new com.google.android.gms.measurement.internal.zzr     // Catch:{ SQLiteException -> 0x0152, all -> 0x0150 }
            r16 = r4
            r17 = r3
            r18 = r10
            r19 = r33
            r20 = r27
            r23 = r24
            r24 = r1
            r27 = r29
            r28 = r31
            r16.<init>(r17, r18, r19, r20, r22, r23, r24, r25, r27, r28, r30)     // Catch:{ SQLiteException -> 0x0152, all -> 0x0150 }
            r0.add(r4)     // Catch:{ SQLiteException -> 0x0152, all -> 0x0150 }
            boolean r1 = r2.moveToNext()     // Catch:{ SQLiteException -> 0x0152, all -> 0x0150 }
            if (r1 != 0) goto L_0x014c
        L_0x0146:
            if (r2 == 0) goto L_0x014b
            r2.close()
        L_0x014b:
            return r0
        L_0x014c:
            r1 = 5
            r11 = 0
            goto L_0x0086
        L_0x0150:
            r0 = move-exception
            goto L_0x0173
        L_0x0152:
            r0 = move-exception
            r1 = r2
            goto L_0x015a
        L_0x0155:
            r0 = move-exception
            r2 = 0
            goto L_0x0173
        L_0x0158:
            r0 = move-exception
            r1 = 0
        L_0x015a:
            com.google.android.gms.measurement.internal.zzau r2 = r39.zzad()     // Catch:{ all -> 0x0171 }
            com.google.android.gms.measurement.internal.zzaw r2 = r2.zzda()     // Catch:{ all -> 0x0171 }
            java.lang.String r3 = "Error querying conditional user property value"
            r2.zza(r3, r0)     // Catch:{ all -> 0x0171 }
            java.util.List r0 = java.util.Collections.emptyList()     // Catch:{ all -> 0x0171 }
            if (r1 == 0) goto L_0x0170
            r1.close()
        L_0x0170:
            return r0
        L_0x0171:
            r0 = move-exception
            r2 = r1
        L_0x0173:
            if (r2 == 0) goto L_0x0178
            r2.close()
        L_0x0178:
            throw r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zzw.zzb(java.lang.String, java.lang.String[]):java.util.List");
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x0186 A[Catch:{ SQLiteException -> 0x022a }] */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x018a A[Catch:{ SQLiteException -> 0x022a }] */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x01be A[Catch:{ SQLiteException -> 0x022a }] */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x01c1 A[Catch:{ SQLiteException -> 0x022a }] */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x01d0 A[Catch:{ SQLiteException -> 0x022a }] */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x0200 A[Catch:{ SQLiteException -> 0x022a }] */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x0203 A[Catch:{ SQLiteException -> 0x022a }] */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x0213 A[Catch:{ SQLiteException -> 0x022a }] */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x0226  */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x0250  */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x0257  */
    @android.support.annotation.WorkerThread
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final com.google.android.gms.measurement.internal.zzg zzae(java.lang.String r22) {
        /*
            r21 = this;
            r1 = r22
            com.google.android.gms.common.internal.Preconditions.checkNotEmpty(r22)
            r21.zzq()
            r21.zzah()
            r2 = 0
            android.database.sqlite.SQLiteDatabase r3 = r21.getWritableDatabase()     // Catch:{ SQLiteException -> 0x0239, all -> 0x0234 }
            java.lang.String r4 = "apps"
            r0 = 27
            java.lang.String[] r5 = new java.lang.String[r0]     // Catch:{ SQLiteException -> 0x0239, all -> 0x0234 }
            java.lang.String r0 = "app_instance_id"
            r11 = 0
            r5[r11] = r0     // Catch:{ SQLiteException -> 0x0239, all -> 0x0234 }
            java.lang.String r0 = "gmp_app_id"
            r12 = 1
            r5[r12] = r0     // Catch:{ SQLiteException -> 0x0239, all -> 0x0234 }
            java.lang.String r0 = "resettable_device_id_hash"
            r13 = 2
            r5[r13] = r0     // Catch:{ SQLiteException -> 0x0239, all -> 0x0234 }
            java.lang.String r0 = "last_bundle_index"
            r14 = 3
            r5[r14] = r0     // Catch:{ SQLiteException -> 0x0239, all -> 0x0234 }
            java.lang.String r0 = "last_bundle_start_timestamp"
            r15 = 4
            r5[r15] = r0     // Catch:{ SQLiteException -> 0x0239, all -> 0x0234 }
            java.lang.String r0 = "last_bundle_end_timestamp"
            r10 = 5
            r5[r10] = r0     // Catch:{ SQLiteException -> 0x0239, all -> 0x0234 }
            java.lang.String r0 = "app_version"
            r9 = 6
            r5[r9] = r0     // Catch:{ SQLiteException -> 0x0239, all -> 0x0234 }
            java.lang.String r0 = "app_store"
            r8 = 7
            r5[r8] = r0     // Catch:{ SQLiteException -> 0x0239, all -> 0x0234 }
            r0 = 8
            java.lang.String r6 = "gmp_version"
            r5[r0] = r6     // Catch:{ SQLiteException -> 0x0239, all -> 0x0234 }
            r0 = 9
            java.lang.String r6 = "dev_cert_hash"
            r5[r0] = r6     // Catch:{ SQLiteException -> 0x0239, all -> 0x0234 }
            java.lang.String r0 = "measurement_enabled"
            r7 = 10
            r5[r7] = r0     // Catch:{ SQLiteException -> 0x0239, all -> 0x0234 }
            r0 = 11
            java.lang.String r6 = "day"
            r5[r0] = r6     // Catch:{ SQLiteException -> 0x0239, all -> 0x0234 }
            r0 = 12
            java.lang.String r6 = "daily_public_events_count"
            r5[r0] = r6     // Catch:{ SQLiteException -> 0x0239, all -> 0x0234 }
            r0 = 13
            java.lang.String r6 = "daily_events_count"
            r5[r0] = r6     // Catch:{ SQLiteException -> 0x0239, all -> 0x0234 }
            r0 = 14
            java.lang.String r6 = "daily_conversions_count"
            r5[r0] = r6     // Catch:{ SQLiteException -> 0x0239, all -> 0x0234 }
            r0 = 15
            java.lang.String r6 = "config_fetched_time"
            r5[r0] = r6     // Catch:{ SQLiteException -> 0x0239, all -> 0x0234 }
            r0 = 16
            java.lang.String r6 = "failed_config_fetch_time"
            r5[r0] = r6     // Catch:{ SQLiteException -> 0x0239, all -> 0x0234 }
            java.lang.String r0 = "app_version_int"
            r6 = 17
            r5[r6] = r0     // Catch:{ SQLiteException -> 0x0239, all -> 0x0234 }
            r0 = 18
            java.lang.String r16 = "firebase_instance_id"
            r5[r0] = r16     // Catch:{ SQLiteException -> 0x0239, all -> 0x0234 }
            r0 = 19
            java.lang.String r16 = "daily_error_events_count"
            r5[r0] = r16     // Catch:{ SQLiteException -> 0x0239, all -> 0x0234 }
            r0 = 20
            java.lang.String r16 = "daily_realtime_events_count"
            r5[r0] = r16     // Catch:{ SQLiteException -> 0x0239, all -> 0x0234 }
            r0 = 21
            java.lang.String r16 = "health_monitor_sample"
            r5[r0] = r16     // Catch:{ SQLiteException -> 0x0239, all -> 0x0234 }
            java.lang.String r0 = "android_id"
            r15 = 22
            r5[r15] = r0     // Catch:{ SQLiteException -> 0x0239, all -> 0x0234 }
            java.lang.String r0 = "adid_reporting_enabled"
            r15 = 23
            r5[r15] = r0     // Catch:{ SQLiteException -> 0x0239, all -> 0x0234 }
            java.lang.String r0 = "ssaid_reporting_enabled"
            r15 = 24
            r5[r15] = r0     // Catch:{ SQLiteException -> 0x0239, all -> 0x0234 }
            r0 = 25
            java.lang.String r17 = "admob_app_id"
            r5[r0] = r17     // Catch:{ SQLiteException -> 0x0239, all -> 0x0234 }
            java.lang.String r0 = "dynamite_version"
            r15 = 26
            r5[r15] = r0     // Catch:{ SQLiteException -> 0x0239, all -> 0x0234 }
            java.lang.String r0 = "app_id=?"
            java.lang.String[] r7 = new java.lang.String[r12]     // Catch:{ SQLiteException -> 0x0239, all -> 0x0234 }
            r7[r11] = r1     // Catch:{ SQLiteException -> 0x0239, all -> 0x0234 }
            r18 = 0
            r19 = 0
            r20 = 0
            r15 = 17
            r6 = r0
            r0 = 10
            r15 = 7
            r8 = r18
            r0 = 6
            r9 = r19
            r15 = 5
            r10 = r20
            android.database.Cursor r3 = r3.query(r4, r5, r6, r7, r8, r9, r10)     // Catch:{ SQLiteException -> 0x0239, all -> 0x0234 }
            boolean r4 = r3.moveToFirst()     // Catch:{ SQLiteException -> 0x0230, all -> 0x022c }
            if (r4 != 0) goto L_0x00da
            if (r3 == 0) goto L_0x00d9
            r3.close()
        L_0x00d9:
            return r2
        L_0x00da:
            com.google.android.gms.measurement.internal.zzg r4 = new com.google.android.gms.measurement.internal.zzg     // Catch:{ SQLiteException -> 0x0230, all -> 0x022c }
            r5 = r21
            com.google.android.gms.measurement.internal.zzft r6 = r5.zzkt     // Catch:{ SQLiteException -> 0x022a }
            com.google.android.gms.measurement.internal.zzby r6 = r6.zzgi()     // Catch:{ SQLiteException -> 0x022a }
            r4.<init>(r6, r1)     // Catch:{ SQLiteException -> 0x022a }
            java.lang.String r6 = r3.getString(r11)     // Catch:{ SQLiteException -> 0x022a }
            r4.zza(r6)     // Catch:{ SQLiteException -> 0x022a }
            java.lang.String r6 = r3.getString(r12)     // Catch:{ SQLiteException -> 0x022a }
            r4.zzb(r6)     // Catch:{ SQLiteException -> 0x022a }
            java.lang.String r6 = r3.getString(r13)     // Catch:{ SQLiteException -> 0x022a }
            r4.zzd(r6)     // Catch:{ SQLiteException -> 0x022a }
            long r6 = r3.getLong(r14)     // Catch:{ SQLiteException -> 0x022a }
            r4.zzk(r6)     // Catch:{ SQLiteException -> 0x022a }
            r6 = 4
            long r6 = r3.getLong(r6)     // Catch:{ SQLiteException -> 0x022a }
            r4.zze(r6)     // Catch:{ SQLiteException -> 0x022a }
            long r6 = r3.getLong(r15)     // Catch:{ SQLiteException -> 0x022a }
            r4.zzf(r6)     // Catch:{ SQLiteException -> 0x022a }
            java.lang.String r0 = r3.getString(r0)     // Catch:{ SQLiteException -> 0x022a }
            r4.zzf(r0)     // Catch:{ SQLiteException -> 0x022a }
            r0 = 7
            java.lang.String r0 = r3.getString(r0)     // Catch:{ SQLiteException -> 0x022a }
            r4.zzg(r0)     // Catch:{ SQLiteException -> 0x022a }
            r0 = 8
            long r6 = r3.getLong(r0)     // Catch:{ SQLiteException -> 0x022a }
            r4.zzh(r6)     // Catch:{ SQLiteException -> 0x022a }
            r0 = 9
            long r6 = r3.getLong(r0)     // Catch:{ SQLiteException -> 0x022a }
            r4.zzi(r6)     // Catch:{ SQLiteException -> 0x022a }
            r0 = 10
            boolean r6 = r3.isNull(r0)     // Catch:{ SQLiteException -> 0x022a }
            if (r6 != 0) goto L_0x0144
            int r0 = r3.getInt(r0)     // Catch:{ SQLiteException -> 0x022a }
            if (r0 == 0) goto L_0x0142
            goto L_0x0144
        L_0x0142:
            r0 = 0
            goto L_0x0145
        L_0x0144:
            r0 = 1
        L_0x0145:
            r4.setMeasurementEnabled(r0)     // Catch:{ SQLiteException -> 0x022a }
            r0 = 11
            long r6 = r3.getLong(r0)     // Catch:{ SQLiteException -> 0x022a }
            r4.zzn(r6)     // Catch:{ SQLiteException -> 0x022a }
            r0 = 12
            long r6 = r3.getLong(r0)     // Catch:{ SQLiteException -> 0x022a }
            r4.zzo(r6)     // Catch:{ SQLiteException -> 0x022a }
            r0 = 13
            long r6 = r3.getLong(r0)     // Catch:{ SQLiteException -> 0x022a }
            r4.zzp(r6)     // Catch:{ SQLiteException -> 0x022a }
            r0 = 14
            long r6 = r3.getLong(r0)     // Catch:{ SQLiteException -> 0x022a }
            r4.zzq(r6)     // Catch:{ SQLiteException -> 0x022a }
            r0 = 15
            long r6 = r3.getLong(r0)     // Catch:{ SQLiteException -> 0x022a }
            r4.zzl(r6)     // Catch:{ SQLiteException -> 0x022a }
            r0 = 16
            long r6 = r3.getLong(r0)     // Catch:{ SQLiteException -> 0x022a }
            r4.zzm(r6)     // Catch:{ SQLiteException -> 0x022a }
            r0 = 17
            boolean r6 = r3.isNull(r0)     // Catch:{ SQLiteException -> 0x022a }
            if (r6 == 0) goto L_0x018a
            r6 = -2147483648(0xffffffff80000000, double:NaN)
            goto L_0x018f
        L_0x018a:
            int r0 = r3.getInt(r0)     // Catch:{ SQLiteException -> 0x022a }
            long r6 = (long) r0     // Catch:{ SQLiteException -> 0x022a }
        L_0x018f:
            r4.zzg(r6)     // Catch:{ SQLiteException -> 0x022a }
            r0 = 18
            java.lang.String r0 = r3.getString(r0)     // Catch:{ SQLiteException -> 0x022a }
            r4.zze(r0)     // Catch:{ SQLiteException -> 0x022a }
            r0 = 19
            long r6 = r3.getLong(r0)     // Catch:{ SQLiteException -> 0x022a }
            r4.zzs(r6)     // Catch:{ SQLiteException -> 0x022a }
            r0 = 20
            long r6 = r3.getLong(r0)     // Catch:{ SQLiteException -> 0x022a }
            r4.zzr(r6)     // Catch:{ SQLiteException -> 0x022a }
            r0 = 21
            java.lang.String r0 = r3.getString(r0)     // Catch:{ SQLiteException -> 0x022a }
            r4.zzh(r0)     // Catch:{ SQLiteException -> 0x022a }
            r0 = 22
            boolean r6 = r3.isNull(r0)     // Catch:{ SQLiteException -> 0x022a }
            if (r6 == 0) goto L_0x01c1
            r6 = 0
            goto L_0x01c5
        L_0x01c1:
            long r6 = r3.getLong(r0)     // Catch:{ SQLiteException -> 0x022a }
        L_0x01c5:
            r4.zzt(r6)     // Catch:{ SQLiteException -> 0x022a }
            r0 = 23
            boolean r6 = r3.isNull(r0)     // Catch:{ SQLiteException -> 0x022a }
            if (r6 != 0) goto L_0x01d9
            int r0 = r3.getInt(r0)     // Catch:{ SQLiteException -> 0x022a }
            if (r0 == 0) goto L_0x01d7
            goto L_0x01d9
        L_0x01d7:
            r0 = 0
            goto L_0x01da
        L_0x01d9:
            r0 = 1
        L_0x01da:
            r4.zzb(r0)     // Catch:{ SQLiteException -> 0x022a }
            r0 = 24
            boolean r6 = r3.isNull(r0)     // Catch:{ SQLiteException -> 0x022a }
            if (r6 != 0) goto L_0x01eb
            int r0 = r3.getInt(r0)     // Catch:{ SQLiteException -> 0x022a }
            if (r0 == 0) goto L_0x01ec
        L_0x01eb:
            r11 = 1
        L_0x01ec:
            r4.zzc(r11)     // Catch:{ SQLiteException -> 0x022a }
            r0 = 25
            java.lang.String r0 = r3.getString(r0)     // Catch:{ SQLiteException -> 0x022a }
            r4.zzc(r0)     // Catch:{ SQLiteException -> 0x022a }
            r0 = 26
            boolean r6 = r3.isNull(r0)     // Catch:{ SQLiteException -> 0x022a }
            if (r6 == 0) goto L_0x0203
            r6 = 0
            goto L_0x0207
        L_0x0203:
            long r6 = r3.getLong(r0)     // Catch:{ SQLiteException -> 0x022a }
        L_0x0207:
            r4.zzj(r6)     // Catch:{ SQLiteException -> 0x022a }
            r4.zzam()     // Catch:{ SQLiteException -> 0x022a }
            boolean r0 = r3.moveToNext()     // Catch:{ SQLiteException -> 0x022a }
            if (r0 == 0) goto L_0x0224
            com.google.android.gms.measurement.internal.zzau r0 = r21.zzad()     // Catch:{ SQLiteException -> 0x022a }
            com.google.android.gms.measurement.internal.zzaw r0 = r0.zzda()     // Catch:{ SQLiteException -> 0x022a }
            java.lang.String r6 = "Got multiple records for app, expected one. appId"
            java.lang.Object r7 = com.google.android.gms.measurement.internal.zzau.zzao(r22)     // Catch:{ SQLiteException -> 0x022a }
            r0.zza(r6, r7)     // Catch:{ SQLiteException -> 0x022a }
        L_0x0224:
            if (r3 == 0) goto L_0x0229
            r3.close()
        L_0x0229:
            return r4
        L_0x022a:
            r0 = move-exception
            goto L_0x023d
        L_0x022c:
            r0 = move-exception
            r5 = r21
            goto L_0x0255
        L_0x0230:
            r0 = move-exception
            r5 = r21
            goto L_0x023d
        L_0x0234:
            r0 = move-exception
            r5 = r21
            r3 = r2
            goto L_0x0255
        L_0x0239:
            r0 = move-exception
            r5 = r21
            r3 = r2
        L_0x023d:
            com.google.android.gms.measurement.internal.zzau r4 = r21.zzad()     // Catch:{ all -> 0x0254 }
            com.google.android.gms.measurement.internal.zzaw r4 = r4.zzda()     // Catch:{ all -> 0x0254 }
            java.lang.String r6 = "Error querying app. appId"
            java.lang.Object r1 = com.google.android.gms.measurement.internal.zzau.zzao(r22)     // Catch:{ all -> 0x0254 }
            r4.zza(r6, r1, r0)     // Catch:{ all -> 0x0254 }
            if (r3 == 0) goto L_0x0253
            r3.close()
        L_0x0253:
            return r2
        L_0x0254:
            r0 = move-exception
        L_0x0255:
            if (r3 == 0) goto L_0x025a
            r3.close()
        L_0x025a:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zzw.zzae(java.lang.String):com.google.android.gms.measurement.internal.zzg");
    }

    @WorkerThread
    public final void zza(zzg zzg) {
        Preconditions.checkNotNull(zzg);
        zzq();
        zzah();
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", zzg.zzan());
        contentValues.put("app_instance_id", zzg.getAppInstanceId());
        contentValues.put("gmp_app_id", zzg.getGmpAppId());
        contentValues.put("resettable_device_id_hash", zzg.zzap());
        contentValues.put("last_bundle_index", Long.valueOf(zzg.zzay()));
        contentValues.put("last_bundle_start_timestamp", Long.valueOf(zzg.zzaq()));
        contentValues.put("last_bundle_end_timestamp", Long.valueOf(zzg.zzar()));
        contentValues.put("app_version", zzg.zzas());
        contentValues.put("app_store", zzg.zzau());
        contentValues.put("gmp_version", Long.valueOf(zzg.zzav()));
        contentValues.put("dev_cert_hash", Long.valueOf(zzg.zzaw()));
        contentValues.put("measurement_enabled", Boolean.valueOf(zzg.isMeasurementEnabled()));
        contentValues.put("day", Long.valueOf(zzg.zzbc()));
        contentValues.put("daily_public_events_count", Long.valueOf(zzg.zzbd()));
        contentValues.put("daily_events_count", Long.valueOf(zzg.zzbe()));
        contentValues.put("daily_conversions_count", Long.valueOf(zzg.zzbf()));
        contentValues.put("config_fetched_time", Long.valueOf(zzg.zzaz()));
        contentValues.put("failed_config_fetch_time", Long.valueOf(zzg.zzba()));
        contentValues.put("app_version_int", Long.valueOf(zzg.zzat()));
        contentValues.put("firebase_instance_id", zzg.getFirebaseInstanceId());
        contentValues.put("daily_error_events_count", Long.valueOf(zzg.zzbh()));
        contentValues.put("daily_realtime_events_count", Long.valueOf(zzg.zzbg()));
        contentValues.put("health_monitor_sample", zzg.zzbi());
        contentValues.put("android_id", Long.valueOf(zzg.zzbk()));
        contentValues.put("adid_reporting_enabled", Boolean.valueOf(zzg.zzbl()));
        contentValues.put("ssaid_reporting_enabled", Boolean.valueOf(zzg.zzbm()));
        contentValues.put("admob_app_id", zzg.zzao());
        contentValues.put("dynamite_version", Long.valueOf(zzg.zzax()));
        try {
            SQLiteDatabase writableDatabase = getWritableDatabase();
            if (((long) writableDatabase.update("apps", contentValues, "app_id = ?", new String[]{zzg.zzan()})) == 0 && writableDatabase.insertWithOnConflict("apps", null, contentValues, 5) == -1) {
                zzad().zzda().zza("Failed to insert/update app (got -1). appId", zzau.zzao(zzg.zzan()));
            }
        } catch (SQLiteException e) {
            zzad().zzda().zza("Error storing app. appId", zzau.zzao(zzg.zzan()), e);
        }
    }

    public final long zzaf(String str) {
        Preconditions.checkNotEmpty(str);
        zzq();
        zzah();
        try {
            return (long) getWritableDatabase().delete("raw_events", "rowid in (select rowid from raw_events where app_id=? order by rowid desc limit -1 offset ?)", new String[]{str, String.valueOf(Math.max(0, Math.min(1000000, zzaf().zzb(str, zzal.zzgs))))});
        } catch (SQLiteException e) {
            zzad().zzda().zza("Error deleting over the limit events. appId", zzau.zzao(str), e);
            return 0;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:39:0x0127  */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x012f  */
    @android.support.annotation.WorkerThread
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final com.google.android.gms.measurement.internal.zzx zza(long r19, java.lang.String r21, boolean r22, boolean r23, boolean r24, boolean r25, boolean r26) {
        /*
            r18 = this;
            com.google.android.gms.common.internal.Preconditions.checkNotEmpty(r21)
            r18.zzq()
            r18.zzah()
            r0 = 1
            java.lang.String[] r1 = new java.lang.String[r0]
            r2 = 0
            r1[r2] = r21
            com.google.android.gms.measurement.internal.zzx r3 = new com.google.android.gms.measurement.internal.zzx
            r3.<init>()
            android.database.sqlite.SQLiteDatabase r13 = r18.getWritableDatabase()     // Catch:{ SQLiteException -> 0x0112, all -> 0x010f }
            java.lang.String r6 = "apps"
            r5 = 6
            java.lang.String[] r7 = new java.lang.String[r5]     // Catch:{ SQLiteException -> 0x0112, all -> 0x010f }
            java.lang.String r5 = "day"
            r7[r2] = r5     // Catch:{ SQLiteException -> 0x0112, all -> 0x010f }
            java.lang.String r5 = "daily_events_count"
            r7[r0] = r5     // Catch:{ SQLiteException -> 0x0112, all -> 0x010f }
            java.lang.String r5 = "daily_public_events_count"
            r14 = 2
            r7[r14] = r5     // Catch:{ SQLiteException -> 0x0112, all -> 0x010f }
            java.lang.String r5 = "daily_conversions_count"
            r15 = 3
            r7[r15] = r5     // Catch:{ SQLiteException -> 0x0112, all -> 0x010f }
            java.lang.String r5 = "daily_error_events_count"
            r12 = 4
            r7[r12] = r5     // Catch:{ SQLiteException -> 0x0112, all -> 0x010f }
            java.lang.String r5 = "daily_realtime_events_count"
            r11 = 5
            r7[r11] = r5     // Catch:{ SQLiteException -> 0x0112, all -> 0x010f }
            java.lang.String r8 = "app_id=?"
            java.lang.String[] r9 = new java.lang.String[r0]     // Catch:{ SQLiteException -> 0x0112, all -> 0x010f }
            r9[r2] = r21     // Catch:{ SQLiteException -> 0x0112, all -> 0x010f }
            r10 = 0
            r16 = 0
            r17 = 0
            r5 = r13
            r4 = 5
            r11 = r16
            r4 = 4
            r12 = r17
            android.database.Cursor r5 = r5.query(r6, r7, r8, r9, r10, r11, r12)     // Catch:{ SQLiteException -> 0x0112, all -> 0x010f }
            boolean r6 = r5.moveToFirst()     // Catch:{ SQLiteException -> 0x010c, all -> 0x010a }
            if (r6 != 0) goto L_0x006c
            com.google.android.gms.measurement.internal.zzau r0 = r18.zzad()     // Catch:{ SQLiteException -> 0x010c, all -> 0x010a }
            com.google.android.gms.measurement.internal.zzaw r0 = r0.zzdd()     // Catch:{ SQLiteException -> 0x010c, all -> 0x010a }
            java.lang.String r1 = "Not updating daily counts, app is not known. appId"
            java.lang.Object r2 = com.google.android.gms.measurement.internal.zzau.zzao(r21)     // Catch:{ SQLiteException -> 0x010c, all -> 0x010a }
            r0.zza(r1, r2)     // Catch:{ SQLiteException -> 0x010c, all -> 0x010a }
            if (r5 == 0) goto L_0x006b
            r5.close()
        L_0x006b:
            return r3
        L_0x006c:
            long r6 = r5.getLong(r2)     // Catch:{ SQLiteException -> 0x010c, all -> 0x010a }
            int r2 = (r6 > r19 ? 1 : (r6 == r19 ? 0 : -1))
            if (r2 != 0) goto L_0x0093
            long r6 = r5.getLong(r0)     // Catch:{ SQLiteException -> 0x010c, all -> 0x010a }
            r3.zzem = r6     // Catch:{ SQLiteException -> 0x010c, all -> 0x010a }
            long r6 = r5.getLong(r14)     // Catch:{ SQLiteException -> 0x010c, all -> 0x010a }
            r3.zzel = r6     // Catch:{ SQLiteException -> 0x010c, all -> 0x010a }
            long r6 = r5.getLong(r15)     // Catch:{ SQLiteException -> 0x010c, all -> 0x010a }
            r3.zzen = r6     // Catch:{ SQLiteException -> 0x010c, all -> 0x010a }
            long r6 = r5.getLong(r4)     // Catch:{ SQLiteException -> 0x010c, all -> 0x010a }
            r3.zzeo = r6     // Catch:{ SQLiteException -> 0x010c, all -> 0x010a }
            r0 = 5
            long r6 = r5.getLong(r0)     // Catch:{ SQLiteException -> 0x010c, all -> 0x010a }
            r3.zzep = r6     // Catch:{ SQLiteException -> 0x010c, all -> 0x010a }
        L_0x0093:
            r6 = 1
            if (r22 == 0) goto L_0x009c
            long r8 = r3.zzem     // Catch:{ SQLiteException -> 0x010c, all -> 0x010a }
            long r8 = r8 + r6
            r3.zzem = r8     // Catch:{ SQLiteException -> 0x010c, all -> 0x010a }
        L_0x009c:
            if (r23 == 0) goto L_0x00a3
            long r8 = r3.zzel     // Catch:{ SQLiteException -> 0x010c, all -> 0x010a }
            long r8 = r8 + r6
            r3.zzel = r8     // Catch:{ SQLiteException -> 0x010c, all -> 0x010a }
        L_0x00a3:
            if (r24 == 0) goto L_0x00aa
            long r8 = r3.zzen     // Catch:{ SQLiteException -> 0x010c, all -> 0x010a }
            long r8 = r8 + r6
            r3.zzen = r8     // Catch:{ SQLiteException -> 0x010c, all -> 0x010a }
        L_0x00aa:
            if (r25 == 0) goto L_0x00b1
            long r8 = r3.zzeo     // Catch:{ SQLiteException -> 0x010c, all -> 0x010a }
            long r8 = r8 + r6
            r3.zzeo = r8     // Catch:{ SQLiteException -> 0x010c, all -> 0x010a }
        L_0x00b1:
            if (r26 == 0) goto L_0x00b8
            long r8 = r3.zzep     // Catch:{ SQLiteException -> 0x010c, all -> 0x010a }
            long r8 = r8 + r6
            r3.zzep = r8     // Catch:{ SQLiteException -> 0x010c, all -> 0x010a }
        L_0x00b8:
            android.content.ContentValues r0 = new android.content.ContentValues     // Catch:{ SQLiteException -> 0x010c, all -> 0x010a }
            r0.<init>()     // Catch:{ SQLiteException -> 0x010c, all -> 0x010a }
            java.lang.String r2 = "day"
            java.lang.Long r4 = java.lang.Long.valueOf(r19)     // Catch:{ SQLiteException -> 0x010c, all -> 0x010a }
            r0.put(r2, r4)     // Catch:{ SQLiteException -> 0x010c, all -> 0x010a }
            java.lang.String r2 = "daily_public_events_count"
            long r6 = r3.zzel     // Catch:{ SQLiteException -> 0x010c, all -> 0x010a }
            java.lang.Long r4 = java.lang.Long.valueOf(r6)     // Catch:{ SQLiteException -> 0x010c, all -> 0x010a }
            r0.put(r2, r4)     // Catch:{ SQLiteException -> 0x010c, all -> 0x010a }
            java.lang.String r2 = "daily_events_count"
            long r6 = r3.zzem     // Catch:{ SQLiteException -> 0x010c, all -> 0x010a }
            java.lang.Long r4 = java.lang.Long.valueOf(r6)     // Catch:{ SQLiteException -> 0x010c, all -> 0x010a }
            r0.put(r2, r4)     // Catch:{ SQLiteException -> 0x010c, all -> 0x010a }
            java.lang.String r2 = "daily_conversions_count"
            long r6 = r3.zzen     // Catch:{ SQLiteException -> 0x010c, all -> 0x010a }
            java.lang.Long r4 = java.lang.Long.valueOf(r6)     // Catch:{ SQLiteException -> 0x010c, all -> 0x010a }
            r0.put(r2, r4)     // Catch:{ SQLiteException -> 0x010c, all -> 0x010a }
            java.lang.String r2 = "daily_error_events_count"
            long r6 = r3.zzeo     // Catch:{ SQLiteException -> 0x010c, all -> 0x010a }
            java.lang.Long r4 = java.lang.Long.valueOf(r6)     // Catch:{ SQLiteException -> 0x010c, all -> 0x010a }
            r0.put(r2, r4)     // Catch:{ SQLiteException -> 0x010c, all -> 0x010a }
            java.lang.String r2 = "daily_realtime_events_count"
            long r6 = r3.zzep     // Catch:{ SQLiteException -> 0x010c, all -> 0x010a }
            java.lang.Long r4 = java.lang.Long.valueOf(r6)     // Catch:{ SQLiteException -> 0x010c, all -> 0x010a }
            r0.put(r2, r4)     // Catch:{ SQLiteException -> 0x010c, all -> 0x010a }
            java.lang.String r2 = "apps"
            java.lang.String r4 = "app_id=?"
            r13.update(r2, r0, r4, r1)     // Catch:{ SQLiteException -> 0x010c, all -> 0x010a }
            if (r5 == 0) goto L_0x0109
            r5.close()
        L_0x0109:
            return r3
        L_0x010a:
            r0 = move-exception
            goto L_0x012d
        L_0x010c:
            r0 = move-exception
            r4 = r5
            goto L_0x0114
        L_0x010f:
            r0 = move-exception
            r5 = 0
            goto L_0x012d
        L_0x0112:
            r0 = move-exception
            r4 = 0
        L_0x0114:
            com.google.android.gms.measurement.internal.zzau r1 = r18.zzad()     // Catch:{ all -> 0x012b }
            com.google.android.gms.measurement.internal.zzaw r1 = r1.zzda()     // Catch:{ all -> 0x012b }
            java.lang.String r2 = "Error updating daily counts. appId"
            java.lang.Object r5 = com.google.android.gms.measurement.internal.zzau.zzao(r21)     // Catch:{ all -> 0x012b }
            r1.zza(r2, r5, r0)     // Catch:{ all -> 0x012b }
            if (r4 == 0) goto L_0x012a
            r4.close()
        L_0x012a:
            return r3
        L_0x012b:
            r0 = move-exception
            r5 = r4
        L_0x012d:
            if (r5 == 0) goto L_0x0132
            r5.close()
        L_0x0132:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zzw.zza(long, java.lang.String, boolean, boolean, boolean, boolean, boolean):com.google.android.gms.measurement.internal.zzx");
    }

    /* JADX WARNING: Removed duplicated region for block: B:24:0x0072  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0079  */
    @android.support.annotation.WorkerThread
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final byte[] zzag(java.lang.String r12) {
        /*
            r11 = this;
            com.google.android.gms.common.internal.Preconditions.checkNotEmpty(r12)
            r11.zzq()
            r11.zzah()
            r0 = 0
            android.database.sqlite.SQLiteDatabase r1 = r11.getWritableDatabase()     // Catch:{ SQLiteException -> 0x005d, all -> 0x005a }
            java.lang.String r2 = "apps"
            r3 = 1
            java.lang.String[] r4 = new java.lang.String[r3]     // Catch:{ SQLiteException -> 0x005d, all -> 0x005a }
            java.lang.String r5 = "remote_config"
            r9 = 0
            r4[r9] = r5     // Catch:{ SQLiteException -> 0x005d, all -> 0x005a }
            java.lang.String r5 = "app_id=?"
            java.lang.String[] r6 = new java.lang.String[r3]     // Catch:{ SQLiteException -> 0x005d, all -> 0x005a }
            r6[r9] = r12     // Catch:{ SQLiteException -> 0x005d, all -> 0x005a }
            r7 = 0
            r8 = 0
            r10 = 0
            r3 = r4
            r4 = r5
            r5 = r6
            r6 = r7
            r7 = r8
            r8 = r10
            android.database.Cursor r1 = r1.query(r2, r3, r4, r5, r6, r7, r8)     // Catch:{ SQLiteException -> 0x005d, all -> 0x005a }
            boolean r2 = r1.moveToFirst()     // Catch:{ SQLiteException -> 0x0058 }
            if (r2 != 0) goto L_0x0037
            if (r1 == 0) goto L_0x0036
            r1.close()
        L_0x0036:
            return r0
        L_0x0037:
            byte[] r2 = r1.getBlob(r9)     // Catch:{ SQLiteException -> 0x0058 }
            boolean r3 = r1.moveToNext()     // Catch:{ SQLiteException -> 0x0058 }
            if (r3 == 0) goto L_0x0052
            com.google.android.gms.measurement.internal.zzau r3 = r11.zzad()     // Catch:{ SQLiteException -> 0x0058 }
            com.google.android.gms.measurement.internal.zzaw r3 = r3.zzda()     // Catch:{ SQLiteException -> 0x0058 }
            java.lang.String r4 = "Got multiple records for app config, expected one. appId"
            java.lang.Object r5 = com.google.android.gms.measurement.internal.zzau.zzao(r12)     // Catch:{ SQLiteException -> 0x0058 }
            r3.zza(r4, r5)     // Catch:{ SQLiteException -> 0x0058 }
        L_0x0052:
            if (r1 == 0) goto L_0x0057
            r1.close()
        L_0x0057:
            return r2
        L_0x0058:
            r2 = move-exception
            goto L_0x005f
        L_0x005a:
            r12 = move-exception
            r1 = r0
            goto L_0x0077
        L_0x005d:
            r2 = move-exception
            r1 = r0
        L_0x005f:
            com.google.android.gms.measurement.internal.zzau r3 = r11.zzad()     // Catch:{ all -> 0x0076 }
            com.google.android.gms.measurement.internal.zzaw r3 = r3.zzda()     // Catch:{ all -> 0x0076 }
            java.lang.String r4 = "Error querying remote config. appId"
            java.lang.Object r12 = com.google.android.gms.measurement.internal.zzau.zzao(r12)     // Catch:{ all -> 0x0076 }
            r3.zza(r4, r12, r2)     // Catch:{ all -> 0x0076 }
            if (r1 == 0) goto L_0x0075
            r1.close()
        L_0x0075:
            return r0
        L_0x0076:
            r12 = move-exception
        L_0x0077:
            if (r1 == 0) goto L_0x007c
            r1.close()
        L_0x007c:
            throw r12
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zzw.zzag(java.lang.String):byte[]");
    }

    @WorkerThread
    public final boolean zza(zzch zzch, boolean z) {
        zzq();
        zzah();
        Preconditions.checkNotNull(zzch);
        Preconditions.checkNotEmpty(zzch.zzcf);
        Preconditions.checkNotNull(zzch.zzxs);
        zzca();
        long currentTimeMillis = zzz().currentTimeMillis();
        if (zzch.zzxs.longValue() < currentTimeMillis - zzt.zzbs() || zzch.zzxs.longValue() > zzt.zzbs() + currentTimeMillis) {
            zzad().zzdd().zza("Storing bundle outside of the max uploading time span. appId, now, timestamp", zzau.zzao(zzch.zzcf), Long.valueOf(currentTimeMillis), zzch.zzxs);
        }
        try {
            byte[] zzc = zzdm().zzc(zziv.zzb(zzch));
            zzad().zzdi().zza("Saving bundle, size", Integer.valueOf(zzc.length));
            ContentValues contentValues = new ContentValues();
            contentValues.put("app_id", zzch.zzcf);
            contentValues.put("bundle_end_timestamp", zzch.zzxs);
            contentValues.put(UriUtil.DATA_SCHEME, zzc);
            contentValues.put("has_realtime", Integer.valueOf(z ? 1 : 0));
            if (zzch.zzyp != null) {
                contentValues.put("retry_count", zzch.zzyp);
            }
            try {
                if (getWritableDatabase().insert("queue", null, contentValues) != -1) {
                    return true;
                }
                zzad().zzda().zza("Failed to insert bundle (got -1). appId", zzau.zzao(zzch.zzcf));
                return false;
            } catch (SQLiteException e) {
                zzad().zzda().zza("Error storing bundle. appId", zzau.zzao(zzch.zzcf), e);
                return false;
            }
        } catch (IOException e2) {
            zzad().zzda().zza("Data loss. Failed to serialize bundle. appId", zzau.zzao(zzch.zzcf), e2);
            return false;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x003a  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0041  */
    @android.support.annotation.WorkerThread
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.lang.String zzby() {
        /*
            r6 = this;
            android.database.sqlite.SQLiteDatabase r0 = r6.getWritableDatabase()
            r1 = 0
            java.lang.String r2 = "select app_id from queue order by has_realtime desc, rowid asc limit 1;"
            android.database.Cursor r0 = r0.rawQuery(r2, r1)     // Catch:{ SQLiteException -> 0x0029, all -> 0x0024 }
            boolean r2 = r0.moveToFirst()     // Catch:{ SQLiteException -> 0x0022 }
            if (r2 == 0) goto L_0x001c
            r2 = 0
            java.lang.String r1 = r0.getString(r2)     // Catch:{ SQLiteException -> 0x0022 }
            if (r0 == 0) goto L_0x001b
            r0.close()
        L_0x001b:
            return r1
        L_0x001c:
            if (r0 == 0) goto L_0x0021
            r0.close()
        L_0x0021:
            return r1
        L_0x0022:
            r2 = move-exception
            goto L_0x002b
        L_0x0024:
            r0 = move-exception
            r5 = r1
            r1 = r0
            r0 = r5
            goto L_0x003f
        L_0x0029:
            r2 = move-exception
            r0 = r1
        L_0x002b:
            com.google.android.gms.measurement.internal.zzau r3 = r6.zzad()     // Catch:{ all -> 0x003e }
            com.google.android.gms.measurement.internal.zzaw r3 = r3.zzda()     // Catch:{ all -> 0x003e }
            java.lang.String r4 = "Database error getting next bundle app id"
            r3.zza(r4, r2)     // Catch:{ all -> 0x003e }
            if (r0 == 0) goto L_0x003d
            r0.close()
        L_0x003d:
            return r1
        L_0x003e:
            r1 = move-exception
        L_0x003f:
            if (r0 == 0) goto L_0x0044
            r0.close()
        L_0x0044:
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zzw.zzby():java.lang.String");
    }

    public final boolean zzbz() {
        return zza("select count(1) > 0 from queue where has_realtime = 1", (String[]) null) != 0;
    }

    @WorkerThread
    public final List<Pair<zzch, Long>> zza(String str, int i, int i2) {
        int i3 = i2;
        zzq();
        zzah();
        Preconditions.checkArgument(i > 0);
        Preconditions.checkArgument(i3 > 0);
        Preconditions.checkNotEmpty(str);
        Cursor cursor = null;
        try {
            cursor = getWritableDatabase().query("queue", new String[]{"rowid", UriUtil.DATA_SCHEME, "retry_count"}, "app_id=?", new String[]{str}, null, null, "rowid", String.valueOf(i));
            if (!cursor.moveToFirst()) {
                List<Pair<zzch, Long>> emptyList = Collections.emptyList();
                if (cursor != null) {
                    cursor.close();
                }
                return emptyList;
            }
            ArrayList arrayList = new ArrayList();
            int i4 = 0;
            do {
                long j = cursor.getLong(0);
                try {
                    byte[] zzb = zzdm().zzb(cursor.getBlob(1));
                    if (!arrayList.isEmpty() && zzb.length + i4 > i3) {
                        break;
                    }
                    try {
                        zzch zzf = zzch.zzf(zzb);
                        if (!cursor.isNull(2)) {
                            zzf.zzyp = Integer.valueOf(cursor.getInt(2));
                        }
                        i4 += zzb.length;
                        arrayList.add(Pair.create(zzf, Long.valueOf(j)));
                    } catch (IOException e) {
                        zzad().zzda().zza("Failed to merge queued bundle. appId", zzau.zzao(str), e);
                    }
                    if (!cursor.moveToNext()) {
                        break;
                    }
                } catch (IOException e2) {
                    zzad().zzda().zza("Failed to unzip queued bundle. appId", zzau.zzao(str), e2);
                }
            } while (i4 <= i3);
            if (cursor != null) {
                cursor.close();
            }
            return arrayList;
        } catch (SQLiteException e3) {
            zzad().zzda().zza("Error querying bundles. appId", zzau.zzao(str), e3);
            List<Pair<zzch, Long>> emptyList2 = Collections.emptyList();
            if (cursor != null) {
                cursor.close();
            }
            return emptyList2;
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final void zzca() {
        zzq();
        zzah();
        if (zzcg()) {
            long j = zzae().zzle.get();
            long elapsedRealtime = zzz().elapsedRealtime();
            if (Math.abs(elapsedRealtime - j) > ((Long) zzal.zzhb.get(null)).longValue()) {
                zzae().zzle.set(elapsedRealtime);
                zzq();
                zzah();
                if (zzcg()) {
                    int delete = getWritableDatabase().delete("queue", "abs(bundle_end_timestamp - ?) > cast(? as integer)", new String[]{String.valueOf(zzz().currentTimeMillis()), String.valueOf(zzt.zzbs())});
                    if (delete > 0) {
                        zzad().zzdi().zza("Deleted stale rows. rowsDeleted", Integer.valueOf(delete));
                    }
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    @VisibleForTesting
    public final void zza(List<Long> list) {
        zzq();
        zzah();
        Preconditions.checkNotNull(list);
        Preconditions.checkNotZero(list.size());
        if (zzcg()) {
            String join = TextUtils.join(",", list);
            StringBuilder sb = new StringBuilder(String.valueOf(join).length() + 2);
            sb.append("(");
            sb.append(join);
            sb.append(")");
            String sb2 = sb.toString();
            StringBuilder sb3 = new StringBuilder(String.valueOf(sb2).length() + 80);
            sb3.append("SELECT COUNT(1) FROM queue WHERE rowid IN ");
            sb3.append(sb2);
            sb3.append(" AND retry_count =  2147483647 LIMIT 1");
            if (zza(sb3.toString(), (String[]) null) > 0) {
                zzad().zzdd().zzaq("The number of upload retries exceeds the limit. Will remain unchanged.");
            }
            try {
                SQLiteDatabase writableDatabase = getWritableDatabase();
                StringBuilder sb4 = new StringBuilder(String.valueOf(sb2).length() + 127);
                sb4.append("UPDATE queue SET retry_count = IFNULL(retry_count, 0) + 1 WHERE rowid IN ");
                sb4.append(sb2);
                sb4.append(" AND (retry_count IS NULL OR retry_count < 2147483647)");
                writableDatabase.execSQL(sb4.toString());
            } catch (SQLiteException e) {
                zzad().zzda().zza("Error incrementing retry count. error", e);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final void zza(String str, zzbx[] zzbxArr) {
        boolean z;
        zzah();
        zzq();
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotNull(zzbxArr);
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.beginTransaction();
        try {
            zzah();
            zzq();
            Preconditions.checkNotEmpty(str);
            SQLiteDatabase writableDatabase2 = getWritableDatabase();
            writableDatabase2.delete("property_filters", "app_id=?", new String[]{str});
            writableDatabase2.delete("event_filters", "app_id=?", new String[]{str});
            for (zzbx zzbx : zzbxArr) {
                zzah();
                zzq();
                Preconditions.checkNotEmpty(str);
                Preconditions.checkNotNull(zzbx);
                Preconditions.checkNotNull(zzbx.zzvw);
                Preconditions.checkNotNull(zzbx.zzvv);
                if (zzbx.zzvu == null) {
                    zzad().zzdd().zza("Audience with no ID. appId", zzau.zzao(str));
                } else {
                    int intValue = zzbx.zzvu.intValue();
                    zzby[] zzbyArr = zzbx.zzvw;
                    int length = zzbyArr.length;
                    int i = 0;
                    while (true) {
                        if (i >= length) {
                            zzcb[] zzcbArr = zzbx.zzvv;
                            int length2 = zzcbArr.length;
                            int i2 = 0;
                            while (true) {
                                if (i2 >= length2) {
                                    zzby[] zzbyArr2 = zzbx.zzvw;
                                    int length3 = zzbyArr2.length;
                                    int i3 = 0;
                                    while (true) {
                                        if (i3 >= length3) {
                                            z = true;
                                            break;
                                        } else if (!zza(str, intValue, zzbyArr2[i3])) {
                                            z = false;
                                            break;
                                        } else {
                                            i3++;
                                        }
                                    }
                                    if (z) {
                                        zzcb[] zzcbArr2 = zzbx.zzvv;
                                        int length4 = zzcbArr2.length;
                                        int i4 = 0;
                                        while (true) {
                                            if (i4 >= length4) {
                                                break;
                                            } else if (!zza(str, intValue, zzcbArr2[i4])) {
                                                z = false;
                                                break;
                                            } else {
                                                i4++;
                                            }
                                        }
                                    }
                                    if (!z) {
                                        zzah();
                                        zzq();
                                        Preconditions.checkNotEmpty(str);
                                        SQLiteDatabase writableDatabase3 = getWritableDatabase();
                                        writableDatabase3.delete("property_filters", "app_id=? and audience_id=?", new String[]{str, String.valueOf(intValue)});
                                        writableDatabase3.delete("event_filters", "app_id=? and audience_id=?", new String[]{str, String.valueOf(intValue)});
                                    }
                                } else if (zzcbArr[i2].zzwa == null) {
                                    zzad().zzdd().zza("Property filter with no ID. Audience definition ignored. appId, audienceId", zzau.zzao(str), zzbx.zzvu);
                                    break;
                                } else {
                                    i2++;
                                }
                            }
                        } else if (zzbyArr[i].zzwa == null) {
                            zzad().zzdd().zza("Event filter with no ID. Audience definition ignored. appId, audienceId", zzau.zzao(str), zzbx.zzvu);
                            break;
                        } else {
                            i++;
                        }
                    }
                }
            }
            ArrayList arrayList = new ArrayList();
            for (zzbx zzbx2 : zzbxArr) {
                arrayList.add(zzbx2.zzvu);
            }
            zza(str, (List<Integer>) arrayList);
            writableDatabase.setTransactionSuccessful();
        } finally {
            writableDatabase.endTransaction();
        }
    }

    @WorkerThread
    private final boolean zza(String str, int i, zzby zzby) {
        zzah();
        zzq();
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotNull(zzby);
        if (TextUtils.isEmpty(zzby.zzwb)) {
            zzad().zzdd().zza("Event filter had no event name. Audience definition ignored. appId, audienceId, filterId", zzau.zzao(str), Integer.valueOf(i), String.valueOf(zzby.zzwa));
            return false;
        }
        byte[] zzb = zziv.zzb(zzby);
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", str);
        contentValues.put("audience_id", Integer.valueOf(i));
        contentValues.put("filter_id", zzby.zzwa);
        contentValues.put("event_name", zzby.zzwb);
        contentValues.put(UriUtil.DATA_SCHEME, zzb);
        try {
            if (getWritableDatabase().insertWithOnConflict("event_filters", null, contentValues, 5) == -1) {
                zzad().zzda().zza("Failed to insert event filter (got -1). appId", zzau.zzao(str));
            }
            return true;
        } catch (SQLiteException e) {
            zzad().zzda().zza("Error storing event filter. appId", zzau.zzao(str), e);
            return false;
        }
    }

    @WorkerThread
    private final boolean zza(String str, int i, zzcb zzcb) {
        zzah();
        zzq();
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotNull(zzcb);
        if (TextUtils.isEmpty(zzcb.zzwq)) {
            zzad().zzdd().zza("Property filter had no property name. Audience definition ignored. appId, audienceId, filterId", zzau.zzao(str), Integer.valueOf(i), String.valueOf(zzcb.zzwa));
            return false;
        }
        byte[] zzb = zziv.zzb(zzcb);
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", str);
        contentValues.put("audience_id", Integer.valueOf(i));
        contentValues.put("filter_id", zzcb.zzwa);
        contentValues.put("property_name", zzcb.zzwq);
        contentValues.put(UriUtil.DATA_SCHEME, zzb);
        try {
            if (getWritableDatabase().insertWithOnConflict("property_filters", null, contentValues, 5) != -1) {
                return true;
            }
            zzad().zzda().zza("Failed to insert property filter (got -1). appId", zzau.zzao(str));
            return false;
        } catch (SQLiteException e) {
            zzad().zzda().zza("Error storing property filter. appId", zzau.zzao(str), e);
            return false;
        }
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x00af  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00b6  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.util.Map<java.lang.Integer, java.util.List<com.google.android.gms.internal.measurement.zzby>> zzh(java.lang.String r13, java.lang.String r14) {
        /*
            r12 = this;
            r12.zzah()
            r12.zzq()
            com.google.android.gms.common.internal.Preconditions.checkNotEmpty(r13)
            com.google.android.gms.common.internal.Preconditions.checkNotEmpty(r14)
            android.support.v4.util.ArrayMap r0 = new android.support.v4.util.ArrayMap
            r0.<init>()
            android.database.sqlite.SQLiteDatabase r1 = r12.getWritableDatabase()
            r9 = 0
            java.lang.String r2 = "event_filters"
            r3 = 2
            java.lang.String[] r4 = new java.lang.String[r3]     // Catch:{ SQLiteException -> 0x009a, all -> 0x0097 }
            java.lang.String r5 = "audience_id"
            r10 = 0
            r4[r10] = r5     // Catch:{ SQLiteException -> 0x009a, all -> 0x0097 }
            java.lang.String r5 = "data"
            r11 = 1
            r4[r11] = r5     // Catch:{ SQLiteException -> 0x009a, all -> 0x0097 }
            java.lang.String r5 = "app_id=? AND event_name=?"
            java.lang.String[] r6 = new java.lang.String[r3]     // Catch:{ SQLiteException -> 0x009a, all -> 0x0097 }
            r6[r10] = r13     // Catch:{ SQLiteException -> 0x009a, all -> 0x0097 }
            r6[r11] = r14     // Catch:{ SQLiteException -> 0x009a, all -> 0x0097 }
            r14 = 0
            r7 = 0
            r8 = 0
            r3 = r4
            r4 = r5
            r5 = r6
            r6 = r14
            android.database.Cursor r14 = r1.query(r2, r3, r4, r5, r6, r7, r8)     // Catch:{ SQLiteException -> 0x009a, all -> 0x0097 }
            boolean r1 = r14.moveToFirst()     // Catch:{ SQLiteException -> 0x0095 }
            if (r1 != 0) goto L_0x0048
            java.util.Map r13 = java.util.Collections.emptyMap()     // Catch:{ SQLiteException -> 0x0095 }
            if (r14 == 0) goto L_0x0047
            r14.close()
        L_0x0047:
            return r13
        L_0x0048:
            byte[] r1 = r14.getBlob(r11)     // Catch:{ SQLiteException -> 0x0095 }
            com.google.android.gms.internal.measurement.zzby r2 = new com.google.android.gms.internal.measurement.zzby     // Catch:{ IOException -> 0x0077 }
            r2.<init>()     // Catch:{ IOException -> 0x0077 }
            com.google.android.gms.internal.measurement.zziv r1 = com.google.android.gms.internal.measurement.zziv.zza(r2, r1)     // Catch:{ IOException -> 0x0077 }
            com.google.android.gms.internal.measurement.zzby r1 = (com.google.android.gms.internal.measurement.zzby) r1     // Catch:{ IOException -> 0x0077 }
            int r2 = r14.getInt(r10)     // Catch:{ SQLiteException -> 0x0095 }
            java.lang.Integer r3 = java.lang.Integer.valueOf(r2)     // Catch:{ SQLiteException -> 0x0095 }
            java.lang.Object r3 = r0.get(r3)     // Catch:{ SQLiteException -> 0x0095 }
            java.util.List r3 = (java.util.List) r3     // Catch:{ SQLiteException -> 0x0095 }
            if (r3 != 0) goto L_0x0073
            java.util.ArrayList r3 = new java.util.ArrayList     // Catch:{ SQLiteException -> 0x0095 }
            r3.<init>()     // Catch:{ SQLiteException -> 0x0095 }
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)     // Catch:{ SQLiteException -> 0x0095 }
            r0.put(r2, r3)     // Catch:{ SQLiteException -> 0x0095 }
        L_0x0073:
            r3.add(r1)     // Catch:{ SQLiteException -> 0x0095 }
            goto L_0x0089
        L_0x0077:
            r1 = move-exception
            com.google.android.gms.measurement.internal.zzau r2 = r12.zzad()     // Catch:{ SQLiteException -> 0x0095 }
            com.google.android.gms.measurement.internal.zzaw r2 = r2.zzda()     // Catch:{ SQLiteException -> 0x0095 }
            java.lang.String r3 = "Failed to merge filter. appId"
            java.lang.Object r4 = com.google.android.gms.measurement.internal.zzau.zzao(r13)     // Catch:{ SQLiteException -> 0x0095 }
            r2.zza(r3, r4, r1)     // Catch:{ SQLiteException -> 0x0095 }
        L_0x0089:
            boolean r1 = r14.moveToNext()     // Catch:{ SQLiteException -> 0x0095 }
            if (r1 != 0) goto L_0x0048
            if (r14 == 0) goto L_0x0094
            r14.close()
        L_0x0094:
            return r0
        L_0x0095:
            r0 = move-exception
            goto L_0x009c
        L_0x0097:
            r13 = move-exception
            r14 = r9
            goto L_0x00b4
        L_0x009a:
            r0 = move-exception
            r14 = r9
        L_0x009c:
            com.google.android.gms.measurement.internal.zzau r1 = r12.zzad()     // Catch:{ all -> 0x00b3 }
            com.google.android.gms.measurement.internal.zzaw r1 = r1.zzda()     // Catch:{ all -> 0x00b3 }
            java.lang.String r2 = "Database error querying filters. appId"
            java.lang.Object r13 = com.google.android.gms.measurement.internal.zzau.zzao(r13)     // Catch:{ all -> 0x00b3 }
            r1.zza(r2, r13, r0)     // Catch:{ all -> 0x00b3 }
            if (r14 == 0) goto L_0x00b2
            r14.close()
        L_0x00b2:
            return r9
        L_0x00b3:
            r13 = move-exception
        L_0x00b4:
            if (r14 == 0) goto L_0x00b9
            r14.close()
        L_0x00b9:
            throw r13
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zzw.zzh(java.lang.String, java.lang.String):java.util.Map");
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x00af  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00b6  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.util.Map<java.lang.Integer, java.util.List<com.google.android.gms.internal.measurement.zzcb>> zzi(java.lang.String r13, java.lang.String r14) {
        /*
            r12 = this;
            r12.zzah()
            r12.zzq()
            com.google.android.gms.common.internal.Preconditions.checkNotEmpty(r13)
            com.google.android.gms.common.internal.Preconditions.checkNotEmpty(r14)
            android.support.v4.util.ArrayMap r0 = new android.support.v4.util.ArrayMap
            r0.<init>()
            android.database.sqlite.SQLiteDatabase r1 = r12.getWritableDatabase()
            r9 = 0
            java.lang.String r2 = "property_filters"
            r3 = 2
            java.lang.String[] r4 = new java.lang.String[r3]     // Catch:{ SQLiteException -> 0x009a, all -> 0x0097 }
            java.lang.String r5 = "audience_id"
            r10 = 0
            r4[r10] = r5     // Catch:{ SQLiteException -> 0x009a, all -> 0x0097 }
            java.lang.String r5 = "data"
            r11 = 1
            r4[r11] = r5     // Catch:{ SQLiteException -> 0x009a, all -> 0x0097 }
            java.lang.String r5 = "app_id=? AND property_name=?"
            java.lang.String[] r6 = new java.lang.String[r3]     // Catch:{ SQLiteException -> 0x009a, all -> 0x0097 }
            r6[r10] = r13     // Catch:{ SQLiteException -> 0x009a, all -> 0x0097 }
            r6[r11] = r14     // Catch:{ SQLiteException -> 0x009a, all -> 0x0097 }
            r14 = 0
            r7 = 0
            r8 = 0
            r3 = r4
            r4 = r5
            r5 = r6
            r6 = r14
            android.database.Cursor r14 = r1.query(r2, r3, r4, r5, r6, r7, r8)     // Catch:{ SQLiteException -> 0x009a, all -> 0x0097 }
            boolean r1 = r14.moveToFirst()     // Catch:{ SQLiteException -> 0x0095 }
            if (r1 != 0) goto L_0x0048
            java.util.Map r13 = java.util.Collections.emptyMap()     // Catch:{ SQLiteException -> 0x0095 }
            if (r14 == 0) goto L_0x0047
            r14.close()
        L_0x0047:
            return r13
        L_0x0048:
            byte[] r1 = r14.getBlob(r11)     // Catch:{ SQLiteException -> 0x0095 }
            com.google.android.gms.internal.measurement.zzcb r2 = new com.google.android.gms.internal.measurement.zzcb     // Catch:{ IOException -> 0x0077 }
            r2.<init>()     // Catch:{ IOException -> 0x0077 }
            com.google.android.gms.internal.measurement.zziv r1 = com.google.android.gms.internal.measurement.zziv.zza(r2, r1)     // Catch:{ IOException -> 0x0077 }
            com.google.android.gms.internal.measurement.zzcb r1 = (com.google.android.gms.internal.measurement.zzcb) r1     // Catch:{ IOException -> 0x0077 }
            int r2 = r14.getInt(r10)     // Catch:{ SQLiteException -> 0x0095 }
            java.lang.Integer r3 = java.lang.Integer.valueOf(r2)     // Catch:{ SQLiteException -> 0x0095 }
            java.lang.Object r3 = r0.get(r3)     // Catch:{ SQLiteException -> 0x0095 }
            java.util.List r3 = (java.util.List) r3     // Catch:{ SQLiteException -> 0x0095 }
            if (r3 != 0) goto L_0x0073
            java.util.ArrayList r3 = new java.util.ArrayList     // Catch:{ SQLiteException -> 0x0095 }
            r3.<init>()     // Catch:{ SQLiteException -> 0x0095 }
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)     // Catch:{ SQLiteException -> 0x0095 }
            r0.put(r2, r3)     // Catch:{ SQLiteException -> 0x0095 }
        L_0x0073:
            r3.add(r1)     // Catch:{ SQLiteException -> 0x0095 }
            goto L_0x0089
        L_0x0077:
            r1 = move-exception
            com.google.android.gms.measurement.internal.zzau r2 = r12.zzad()     // Catch:{ SQLiteException -> 0x0095 }
            com.google.android.gms.measurement.internal.zzaw r2 = r2.zzda()     // Catch:{ SQLiteException -> 0x0095 }
            java.lang.String r3 = "Failed to merge filter"
            java.lang.Object r4 = com.google.android.gms.measurement.internal.zzau.zzao(r13)     // Catch:{ SQLiteException -> 0x0095 }
            r2.zza(r3, r4, r1)     // Catch:{ SQLiteException -> 0x0095 }
        L_0x0089:
            boolean r1 = r14.moveToNext()     // Catch:{ SQLiteException -> 0x0095 }
            if (r1 != 0) goto L_0x0048
            if (r14 == 0) goto L_0x0094
            r14.close()
        L_0x0094:
            return r0
        L_0x0095:
            r0 = move-exception
            goto L_0x009c
        L_0x0097:
            r13 = move-exception
            r14 = r9
            goto L_0x00b4
        L_0x009a:
            r0 = move-exception
            r14 = r9
        L_0x009c:
            com.google.android.gms.measurement.internal.zzau r1 = r12.zzad()     // Catch:{ all -> 0x00b3 }
            com.google.android.gms.measurement.internal.zzaw r1 = r1.zzda()     // Catch:{ all -> 0x00b3 }
            java.lang.String r2 = "Database error querying filters. appId"
            java.lang.Object r13 = com.google.android.gms.measurement.internal.zzau.zzao(r13)     // Catch:{ all -> 0x00b3 }
            r1.zza(r2, r13, r0)     // Catch:{ all -> 0x00b3 }
            if (r14 == 0) goto L_0x00b2
            r14.close()
        L_0x00b2:
            return r9
        L_0x00b3:
            r13 = move-exception
        L_0x00b4:
            if (r14 == 0) goto L_0x00b9
            r14.close()
        L_0x00b9:
            throw r13
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zzw.zzi(java.lang.String, java.lang.String):java.util.Map");
    }

    private final boolean zza(String str, List<Integer> list) {
        Preconditions.checkNotEmpty(str);
        zzah();
        zzq();
        SQLiteDatabase writableDatabase = getWritableDatabase();
        try {
            long zza = zza("select count(1) from audience_filter_values where app_id=?", new String[]{str});
            int max = Math.max(0, Math.min(2000, zzaf().zzb(str, zzal.zzhi)));
            if (zza <= ((long) max)) {
                return false;
            }
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                Integer num = (Integer) list.get(i);
                if (num == null || !(num instanceof Integer)) {
                    return false;
                }
                arrayList.add(Integer.toString(num.intValue()));
            }
            String join = TextUtils.join(",", arrayList);
            StringBuilder sb = new StringBuilder(String.valueOf(join).length() + 2);
            sb.append("(");
            sb.append(join);
            sb.append(")");
            String sb2 = sb.toString();
            StringBuilder sb3 = new StringBuilder(String.valueOf(sb2).length() + 140);
            sb3.append("audience_id in (select audience_id from audience_filter_values where app_id=? and audience_id not in ");
            sb3.append(sb2);
            sb3.append(" order by rowid desc limit -1 offset ?)");
            return writableDatabase.delete("audience_filter_values", sb3.toString(), new String[]{str, Integer.toString(max)}) > 0;
        } catch (SQLiteException e) {
            zzad().zzda().zza("Database error querying filters. appId", zzau.zzao(str), e);
            return false;
        }
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x008f  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0096  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.util.Map<java.lang.Integer, com.google.android.gms.internal.measurement.zzbt.zzf> zzah(java.lang.String r12) {
        /*
            r11 = this;
            r11.zzah()
            r11.zzq()
            com.google.android.gms.common.internal.Preconditions.checkNotEmpty(r12)
            android.database.sqlite.SQLiteDatabase r0 = r11.getWritableDatabase()
            r8 = 0
            java.lang.String r1 = "audience_filter_values"
            r2 = 2
            java.lang.String[] r2 = new java.lang.String[r2]     // Catch:{ SQLiteException -> 0x007a, all -> 0x0077 }
            java.lang.String r3 = "audience_id"
            r9 = 0
            r2[r9] = r3     // Catch:{ SQLiteException -> 0x007a, all -> 0x0077 }
            java.lang.String r3 = "current_results"
            r10 = 1
            r2[r10] = r3     // Catch:{ SQLiteException -> 0x007a, all -> 0x0077 }
            java.lang.String r3 = "app_id=?"
            java.lang.String[] r4 = new java.lang.String[r10]     // Catch:{ SQLiteException -> 0x007a, all -> 0x0077 }
            r4[r9] = r12     // Catch:{ SQLiteException -> 0x007a, all -> 0x0077 }
            r5 = 0
            r6 = 0
            r7 = 0
            android.database.Cursor r0 = r0.query(r1, r2, r3, r4, r5, r6, r7)     // Catch:{ SQLiteException -> 0x007a, all -> 0x0077 }
            boolean r1 = r0.moveToFirst()     // Catch:{ SQLiteException -> 0x0075 }
            if (r1 != 0) goto L_0x0036
            if (r0 == 0) goto L_0x0035
            r0.close()
        L_0x0035:
            return r8
        L_0x0036:
            android.support.v4.util.ArrayMap r1 = new android.support.v4.util.ArrayMap     // Catch:{ SQLiteException -> 0x0075 }
            r1.<init>()     // Catch:{ SQLiteException -> 0x0075 }
        L_0x003b:
            int r2 = r0.getInt(r9)     // Catch:{ SQLiteException -> 0x0075 }
            byte[] r3 = r0.getBlob(r10)     // Catch:{ SQLiteException -> 0x0075 }
            com.google.android.gms.internal.measurement.zzem r4 = com.google.android.gms.internal.measurement.zzem.zzlt()     // Catch:{ IOException -> 0x0053 }
            com.google.android.gms.internal.measurement.zzbt$zzf r3 = com.google.android.gms.internal.measurement.zzbt.zzf.zza(r3, r4)     // Catch:{ IOException -> 0x0053 }
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)     // Catch:{ SQLiteException -> 0x0075 }
            r1.put(r2, r3)     // Catch:{ SQLiteException -> 0x0075 }
            goto L_0x0069
        L_0x0053:
            r3 = move-exception
            com.google.android.gms.measurement.internal.zzau r4 = r11.zzad()     // Catch:{ SQLiteException -> 0x0075 }
            com.google.android.gms.measurement.internal.zzaw r4 = r4.zzda()     // Catch:{ SQLiteException -> 0x0075 }
            java.lang.String r5 = "Failed to merge filter results. appId, audienceId, error"
            java.lang.Object r6 = com.google.android.gms.measurement.internal.zzau.zzao(r12)     // Catch:{ SQLiteException -> 0x0075 }
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)     // Catch:{ SQLiteException -> 0x0075 }
            r4.zza(r5, r6, r2, r3)     // Catch:{ SQLiteException -> 0x0075 }
        L_0x0069:
            boolean r2 = r0.moveToNext()     // Catch:{ SQLiteException -> 0x0075 }
            if (r2 != 0) goto L_0x003b
            if (r0 == 0) goto L_0x0074
            r0.close()
        L_0x0074:
            return r1
        L_0x0075:
            r1 = move-exception
            goto L_0x007c
        L_0x0077:
            r12 = move-exception
            r0 = r8
            goto L_0x0094
        L_0x007a:
            r1 = move-exception
            r0 = r8
        L_0x007c:
            com.google.android.gms.measurement.internal.zzau r2 = r11.zzad()     // Catch:{ all -> 0x0093 }
            com.google.android.gms.measurement.internal.zzaw r2 = r2.zzda()     // Catch:{ all -> 0x0093 }
            java.lang.String r3 = "Database error querying filter results. appId"
            java.lang.Object r12 = com.google.android.gms.measurement.internal.zzau.zzao(r12)     // Catch:{ all -> 0x0093 }
            r2.zza(r3, r12, r1)     // Catch:{ all -> 0x0093 }
            if (r0 == 0) goto L_0x0092
            r0.close()
        L_0x0092:
            return r8
        L_0x0093:
            r12 = move-exception
        L_0x0094:
            if (r0 == 0) goto L_0x0099
            r0.close()
        L_0x0099:
            throw r12
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zzw.zzah(java.lang.String):java.util.Map");
    }

    @WorkerThread
    private static void zza(ContentValues contentValues, String str, Object obj) {
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotNull(obj);
        if (obj instanceof String) {
            contentValues.put(str, (String) obj);
        } else if (obj instanceof Long) {
            contentValues.put(str, (Long) obj);
        } else if (obj instanceof Double) {
            contentValues.put(str, (Double) obj);
        } else {
            throw new IllegalArgumentException("Invalid value type");
        }
    }

    @WorkerThread
    @VisibleForTesting
    private final Object zza(Cursor cursor, int i) {
        int type = cursor.getType(i);
        switch (type) {
            case 0:
                zzad().zzda().zzaq("Loaded invalid null value from database");
                return null;
            case 1:
                return Long.valueOf(cursor.getLong(i));
            case 2:
                return Double.valueOf(cursor.getDouble(i));
            case 3:
                return cursor.getString(i);
            case 4:
                zzad().zzda().zzaq("Loaded invalid blob type value, ignoring it");
                return null;
            default:
                zzad().zzda().zza("Loaded invalid unknown value type, ignoring it", Integer.valueOf(type));
                return null;
        }
    }

    @WorkerThread
    public final long zzcb() {
        return zza("select max(bundle_end_timestamp) from queue", (String[]) null, 0);
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    @VisibleForTesting
    public final long zzj(String str, String str2) {
        long j;
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotEmpty(str2);
        zzq();
        zzah();
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.beginTransaction();
        try {
            StringBuilder sb = new StringBuilder(String.valueOf(str2).length() + 32);
            sb.append("select ");
            sb.append(str2);
            sb.append(" from app2 where app_id=?");
            j = zza(sb.toString(), new String[]{str}, -1);
            if (j == -1) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("app_id", str);
                contentValues.put("first_open_count", Integer.valueOf(0));
                contentValues.put("previous_install_count", Integer.valueOf(0));
                if (writableDatabase.insertWithOnConflict("app2", null, contentValues, 5) == -1) {
                    zzad().zzda().zza("Failed to insert column (got -1). appId", zzau.zzao(str), str2);
                    writableDatabase.endTransaction();
                    return -1;
                }
                j = 0;
            }
            try {
                ContentValues contentValues2 = new ContentValues();
                contentValues2.put("app_id", str);
                contentValues2.put(str2, Long.valueOf(1 + j));
                if (((long) writableDatabase.update("app2", contentValues2, "app_id = ?", new String[]{str})) == 0) {
                    zzad().zzda().zza("Failed to update column (got 0). appId", zzau.zzao(str), str2);
                    writableDatabase.endTransaction();
                    return -1;
                }
                writableDatabase.setTransactionSuccessful();
                writableDatabase.endTransaction();
                return j;
            } catch (SQLiteException e) {
                e = e;
                try {
                    zzad().zzda().zza("Error inserting column. appId", zzau.zzao(str), str2, e);
                    return j;
                } finally {
                    writableDatabase.endTransaction();
                }
            }
        } catch (SQLiteException e2) {
            e = e2;
            j = 0;
            zzad().zzda().zza("Error inserting column. appId", zzau.zzao(str), str2, e);
            return j;
        }
    }

    @WorkerThread
    public final long zzcc() {
        return zza("select max(timestamp) from raw_events", (String[]) null, 0);
    }

    public final long zza(zzch zzch) throws IOException {
        zzq();
        zzah();
        Preconditions.checkNotNull(zzch);
        Preconditions.checkNotEmpty(zzch.zzcf);
        byte[] zzb = zziv.zzb(zzch);
        long zza = zzdm().zza(zzb);
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", zzch.zzcf);
        contentValues.put("metadata_fingerprint", Long.valueOf(zza));
        contentValues.put("metadata", zzb);
        try {
            getWritableDatabase().insertWithOnConflict("raw_events_metadata", null, contentValues, 4);
            return zza;
        } catch (SQLiteException e) {
            zzad().zzda().zza("Error storing raw event metadata. appId", zzau.zzao(zzch.zzcf), e);
            throw e;
        }
    }

    public final boolean zzcd() {
        return zza("select count(1) > 0 from raw_events", (String[]) null) != 0;
    }

    public final boolean zzce() {
        return zza("select count(1) > 0 from raw_events where realtime = 1", (String[]) null) != 0;
    }

    public final long zzai(String str) {
        Preconditions.checkNotEmpty(str);
        return zza("select count(1) from events where app_id=? and name not like '!_%' escape '!'", new String[]{str}, 0);
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x0054  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x005b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.lang.String zzu(long r5) {
        /*
            r4 = this;
            r4.zzq()
            r4.zzah()
            r0 = 0
            android.database.sqlite.SQLiteDatabase r1 = r4.getWritableDatabase()     // Catch:{ SQLiteException -> 0x0043, all -> 0x0040 }
            java.lang.String r2 = "select app_id from apps where app_id in (select distinct app_id from raw_events) and config_fetched_time < ? order by failed_config_fetch_time limit 1;"
            r3 = 1
            java.lang.String[] r3 = new java.lang.String[r3]     // Catch:{ SQLiteException -> 0x0043, all -> 0x0040 }
            java.lang.String r5 = java.lang.String.valueOf(r5)     // Catch:{ SQLiteException -> 0x0043, all -> 0x0040 }
            r6 = 0
            r3[r6] = r5     // Catch:{ SQLiteException -> 0x0043, all -> 0x0040 }
            android.database.Cursor r5 = r1.rawQuery(r2, r3)     // Catch:{ SQLiteException -> 0x0043, all -> 0x0040 }
            boolean r1 = r5.moveToFirst()     // Catch:{ SQLiteException -> 0x003e }
            if (r1 != 0) goto L_0x0034
            com.google.android.gms.measurement.internal.zzau r6 = r4.zzad()     // Catch:{ SQLiteException -> 0x003e }
            com.google.android.gms.measurement.internal.zzaw r6 = r6.zzdi()     // Catch:{ SQLiteException -> 0x003e }
            java.lang.String r1 = "No expired configs for apps with pending events"
            r6.zzaq(r1)     // Catch:{ SQLiteException -> 0x003e }
            if (r5 == 0) goto L_0x0033
            r5.close()
        L_0x0033:
            return r0
        L_0x0034:
            java.lang.String r6 = r5.getString(r6)     // Catch:{ SQLiteException -> 0x003e }
            if (r5 == 0) goto L_0x003d
            r5.close()
        L_0x003d:
            return r6
        L_0x003e:
            r6 = move-exception
            goto L_0x0045
        L_0x0040:
            r6 = move-exception
            r5 = r0
            goto L_0x0059
        L_0x0043:
            r6 = move-exception
            r5 = r0
        L_0x0045:
            com.google.android.gms.measurement.internal.zzau r1 = r4.zzad()     // Catch:{ all -> 0x0058 }
            com.google.android.gms.measurement.internal.zzaw r1 = r1.zzda()     // Catch:{ all -> 0x0058 }
            java.lang.String r2 = "Error selecting expired configs"
            r1.zza(r2, r6)     // Catch:{ all -> 0x0058 }
            if (r5 == 0) goto L_0x0057
            r5.close()
        L_0x0057:
            return r0
        L_0x0058:
            r6 = move-exception
        L_0x0059:
            if (r5 == 0) goto L_0x005e
            r5.close()
        L_0x005e:
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zzw.zzu(long):java.lang.String");
    }

    public final long zzcf() {
        Cursor cursor = null;
        try {
            cursor = getWritableDatabase().rawQuery("select rowid from raw_events order by rowid desc limit 1;", null);
            if (!cursor.moveToFirst()) {
                if (cursor != null) {
                    cursor.close();
                }
                return -1;
            }
            long j = cursor.getLong(0);
            if (cursor != null) {
                cursor.close();
            }
            return j;
        } catch (SQLiteException e) {
            zzad().zzda().zza("Error querying raw events", e);
            if (cursor != null) {
                cursor.close();
            }
            return -1;
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:33:0x007f  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x0086  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final android.util.Pair<com.google.android.gms.internal.measurement.zzcf, java.lang.Long> zza(java.lang.String r8, java.lang.Long r9) {
        /*
            r7 = this;
            r7.zzq()
            r7.zzah()
            r0 = 0
            android.database.sqlite.SQLiteDatabase r1 = r7.getWritableDatabase()     // Catch:{ SQLiteException -> 0x006e, all -> 0x006b }
            java.lang.String r2 = "select main_event, children_to_process from main_event_params where app_id=? and event_id=?"
            r3 = 2
            java.lang.String[] r3 = new java.lang.String[r3]     // Catch:{ SQLiteException -> 0x006e, all -> 0x006b }
            r4 = 0
            r3[r4] = r8     // Catch:{ SQLiteException -> 0x006e, all -> 0x006b }
            java.lang.String r5 = java.lang.String.valueOf(r9)     // Catch:{ SQLiteException -> 0x006e, all -> 0x006b }
            r6 = 1
            r3[r6] = r5     // Catch:{ SQLiteException -> 0x006e, all -> 0x006b }
            android.database.Cursor r1 = r1.rawQuery(r2, r3)     // Catch:{ SQLiteException -> 0x006e, all -> 0x006b }
            boolean r2 = r1.moveToFirst()     // Catch:{ SQLiteException -> 0x0069 }
            if (r2 != 0) goto L_0x0037
            com.google.android.gms.measurement.internal.zzau r8 = r7.zzad()     // Catch:{ SQLiteException -> 0x0069 }
            com.google.android.gms.measurement.internal.zzaw r8 = r8.zzdi()     // Catch:{ SQLiteException -> 0x0069 }
            java.lang.String r9 = "Main event not found"
            r8.zzaq(r9)     // Catch:{ SQLiteException -> 0x0069 }
            if (r1 == 0) goto L_0x0036
            r1.close()
        L_0x0036:
            return r0
        L_0x0037:
            byte[] r2 = r1.getBlob(r4)     // Catch:{ SQLiteException -> 0x0069 }
            long r3 = r1.getLong(r6)     // Catch:{ SQLiteException -> 0x0069 }
            java.lang.Long r3 = java.lang.Long.valueOf(r3)     // Catch:{ SQLiteException -> 0x0069 }
            com.google.android.gms.internal.measurement.zzcf r8 = com.google.android.gms.internal.measurement.zzcf.zze(r2)     // Catch:{ IOException -> 0x0051 }
            android.util.Pair r8 = android.util.Pair.create(r8, r3)     // Catch:{ SQLiteException -> 0x0069 }
            if (r1 == 0) goto L_0x0050
            r1.close()
        L_0x0050:
            return r8
        L_0x0051:
            r2 = move-exception
            com.google.android.gms.measurement.internal.zzau r3 = r7.zzad()     // Catch:{ SQLiteException -> 0x0069 }
            com.google.android.gms.measurement.internal.zzaw r3 = r3.zzda()     // Catch:{ SQLiteException -> 0x0069 }
            java.lang.String r4 = "Failed to merge main event. appId, eventId"
            java.lang.Object r8 = com.google.android.gms.measurement.internal.zzau.zzao(r8)     // Catch:{ SQLiteException -> 0x0069 }
            r3.zza(r4, r8, r9, r2)     // Catch:{ SQLiteException -> 0x0069 }
            if (r1 == 0) goto L_0x0068
            r1.close()
        L_0x0068:
            return r0
        L_0x0069:
            r8 = move-exception
            goto L_0x0070
        L_0x006b:
            r8 = move-exception
            r1 = r0
            goto L_0x0084
        L_0x006e:
            r8 = move-exception
            r1 = r0
        L_0x0070:
            com.google.android.gms.measurement.internal.zzau r9 = r7.zzad()     // Catch:{ all -> 0x0083 }
            com.google.android.gms.measurement.internal.zzaw r9 = r9.zzda()     // Catch:{ all -> 0x0083 }
            java.lang.String r2 = "Error selecting main event"
            r9.zza(r2, r8)     // Catch:{ all -> 0x0083 }
            if (r1 == 0) goto L_0x0082
            r1.close()
        L_0x0082:
            return r0
        L_0x0083:
            r8 = move-exception
        L_0x0084:
            if (r1 == 0) goto L_0x0089
            r1.close()
        L_0x0089:
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zzw.zza(java.lang.String, java.lang.Long):android.util.Pair");
    }

    public final boolean zza(String str, Long l, long j, zzcf zzcf) {
        zzq();
        zzah();
        Preconditions.checkNotNull(zzcf);
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotNull(l);
        byte[] zzb = zziv.zzb(zzcf);
        zzad().zzdi().zza("Saving complex main event, appId, data size", zzaa().zzal(str), Integer.valueOf(zzb.length));
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", str);
        contentValues.put("event_id", l);
        contentValues.put("children_to_process", Long.valueOf(j));
        contentValues.put("main_event", zzb);
        try {
            if (getWritableDatabase().insertWithOnConflict("main_event_params", null, contentValues, 5) != -1) {
                return true;
            }
            zzad().zzda().zza("Failed to insert complex main event (got -1). appId", zzau.zzao(str));
            return false;
        } catch (SQLiteException e) {
            zzad().zzda().zza("Error storing complex main event. appId", zzau.zzao(str), e);
            return false;
        }
    }

    public final boolean zza(zzae zzae, long j, boolean z) {
        zzq();
        zzah();
        Preconditions.checkNotNull(zzae);
        Preconditions.checkNotEmpty(zzae.zzcf);
        zzcf zzcf = new zzcf();
        zzcf.zzxk = Long.valueOf(zzae.zzfc);
        zzcf.zzxi = new zzd[zzae.zzfd.size()];
        Iterator it = zzae.zzfd.iterator();
        int i = 0;
        while (it.hasNext()) {
            String str = (String) it.next();
            zza zzbw = zzd.zzht().zzbw(str);
            zzdm().zza(zzbw, zzae.zzfd.get(str));
            int i2 = i + 1;
            zzcf.zzxi[i] = (zzd) ((zzez) zzbw.zzmr());
            i = i2;
        }
        byte[] zzb = zziv.zzb(zzcf);
        zzad().zzdi().zza("Saving event, name, data size", zzaa().zzal(zzae.name), Integer.valueOf(zzb.length));
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", zzae.zzcf);
        contentValues.put(ConditionalUserProperty.NAME, zzae.name);
        contentValues.put("timestamp", Long.valueOf(zzae.timestamp));
        contentValues.put("metadata_fingerprint", Long.valueOf(j));
        contentValues.put(UriUtil.DATA_SCHEME, zzb);
        contentValues.put("realtime", Integer.valueOf(z ? 1 : 0));
        try {
            if (getWritableDatabase().insert("raw_events", null, contentValues) != -1) {
                return true;
            }
            zzad().zzda().zza("Failed to insert raw event (got -1). appId", zzau.zzao(zzae.zzcf));
            return false;
        } catch (SQLiteException e) {
            zzad().zzda().zza("Error storing raw event. appId", zzau.zzao(zzae.zzcf), e);
            return false;
        }
    }

    private final boolean zzcg() {
        return getContext().getDatabasePath("google_app_measurement.db").exists();
    }
}

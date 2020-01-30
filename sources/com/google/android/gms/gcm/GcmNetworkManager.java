package com.google.android.gms.gcm;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.support.p000v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.gms.internal.gcm.zzq;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.annotation.concurrent.GuardedBy;

public class GcmNetworkManager {
    public static final int RESULT_FAILURE = 2;
    public static final int RESULT_RESCHEDULE = 1;
    public static final int RESULT_SUCCESS = 0;
    @GuardedBy("GcmNetworkManager.class")
    private static GcmNetworkManager zzh;
    private final Context zzi;
    @GuardedBy("this")
    private final Map<String, Map<String, Boolean>> zzj = new ArrayMap();

    public static GcmNetworkManager getInstance(Context context) {
        GcmNetworkManager gcmNetworkManager;
        synchronized (GcmNetworkManager.class) {
            if (zzh == null) {
                zzh = new GcmNetworkManager(context.getApplicationContext());
            }
            gcmNetworkManager = zzh;
        }
        return gcmNetworkManager;
    }

    private GcmNetworkManager(Context context) {
        this.zzi = context;
    }

    @NonNull
    private final zzn zze() {
        if (GoogleCloudMessaging.zzf(this.zzi) >= 5000000) {
            return new zzm(this.zzi);
        }
        Log.e("GcmNetworkManager", "Google Play services is not available, dropping all GcmNetworkManager requests");
        return new zzo();
    }

    @WorkerThread
    public synchronized void schedule(Task task) {
        Throwable th;
        String valueOf = String.valueOf("nts:client:schedule:");
        String valueOf2 = String.valueOf(task.getTag());
        zzp zzp = new zzp(valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf));
        try {
            zze(task.getServiceName());
            if (zze().zzd(task)) {
                Map map = (Map) this.zzj.get(task.getServiceName());
                if (map != null && map.containsKey(task.getTag())) {
                    map.put(task.getTag(), Boolean.valueOf(true));
                }
            }
            zzd((Throwable) null, zzp);
        } catch (Throwable th2) {
            zzd(th, zzp);
            throw th2;
        }
    }

    @WorkerThread
    public void cancelTask(String str, Class<? extends GcmTaskService> cls) {
        Throwable th;
        ComponentName componentName = new ComponentName(this.zzi, cls);
        String valueOf = String.valueOf("nts:client:cancel:");
        String valueOf2 = String.valueOf(str);
        zzp zzp = new zzp(valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf));
        try {
            zzd(str);
            zze(componentName.getClassName());
            zze().zzd(componentName, str);
            zzd((Throwable) null, zzp);
        } catch (Throwable th2) {
            zzd(th, zzp);
            throw th2;
        }
    }

    @WorkerThread
    public void cancelAllTasks(Class<? extends GcmTaskService> cls) {
        Throwable th;
        ComponentName componentName = new ComponentName(this.zzi, cls);
        zzp zzp = new zzp("nts:client:cancelAll");
        try {
            zze(componentName.getClassName());
            zze().zzd(componentName);
            zzd((Throwable) null, zzp);
        } catch (Throwable th2) {
            zzd(th, zzp);
            throw th2;
        }
    }

    static void zzd(String str) {
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException("Must provide a valid tag.");
        } else if (100 < str.length()) {
            throw new IllegalArgumentException("Tag is larger than max permissible tag length (100)");
        }
    }

    private final boolean zze(String str) {
        List<ResolveInfo> list;
        Intent intent;
        Preconditions.checkNotNull(str, "GcmTaskService must not be null.");
        PackageManager packageManager = this.zzi.getPackageManager();
        if (packageManager == null) {
            list = Collections.emptyList();
        } else {
            if (str != null) {
                intent = new Intent(GcmTaskService.SERVICE_ACTION_EXECUTE_TASK).setClassName(this.zzi, str);
            } else {
                intent = new Intent(GcmTaskService.SERVICE_ACTION_EXECUTE_TASK).setPackage(this.zzi.getPackageName());
            }
            list = packageManager.queryIntentServices(intent, 0);
        }
        if (CollectionUtils.isEmpty(list)) {
            Log.e("GcmNetworkManager", String.valueOf(str).concat(" is not available. This may cause the task to be lost."));
            return true;
        }
        for (ResolveInfo resolveInfo : list) {
            if (resolveInfo.serviceInfo != null && resolveInfo.serviceInfo.enabled) {
                return true;
            }
        }
        StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 118);
        sb.append("The GcmTaskService class you provided ");
        sb.append(str);
        sb.append(" does not seem to support receiving com.google.android.gms.gcm.ACTION_TASK_READY");
        throw new IllegalArgumentException(sb.toString());
    }

    /* access modifiers changed from: 0000 */
    public final synchronized boolean zzd(String str, String str2) {
        Map map = (Map) this.zzj.get(str2);
        if (map == null) {
            map = new ArrayMap();
            this.zzj.put(str2, map);
        }
        if (map.put(str, Boolean.valueOf(false)) == null) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: 0000 */
    public final synchronized void zze(String str, String str2) {
        Map map = (Map) this.zzj.get(str2);
        if (map != null) {
            if ((map.remove(str) != null) && map.isEmpty()) {
                this.zzj.remove(str2);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public final synchronized boolean zzf(String str, String str2) {
        Map map = (Map) this.zzj.get(str2);
        if (map == null) {
            return false;
        }
        Boolean bool = (Boolean) map.get(str);
        if (bool == null) {
            return false;
        }
        return bool.booleanValue();
    }

    /* access modifiers changed from: 0000 */
    public final synchronized boolean zzf(String str) {
        return this.zzj.containsKey(str);
    }

    private static /* synthetic */ void zzd(Throwable th, zzp zzp) {
        if (th != null) {
            try {
                zzp.close();
            } catch (Throwable th2) {
                zzq.zzd(th, th2);
            }
        } else {
            zzp.close();
        }
    }
}

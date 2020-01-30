package com.google.android.gms.gcm;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.google.android.gms.common.util.PlatformVersion;
import com.google.android.gms.common.util.UidVerifier;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.android.gms.internal.gcm.zzg;
import com.google.android.gms.internal.gcm.zzj;
import com.google.android.gms.internal.gcm.zzl;
import com.google.android.gms.internal.gcm.zzm;
import com.google.android.gms.internal.gcm.zzq;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import javax.annotation.concurrent.GuardedBy;

public abstract class GcmTaskService extends Service {
    public static final String SERVICE_ACTION_EXECUTE_TASK = "com.google.android.gms.gcm.ACTION_TASK_READY";
    public static final String SERVICE_ACTION_INITIALIZE = "com.google.android.gms.gcm.SERVICE_ACTION_INITIALIZE";
    public static final String SERVICE_PERMISSION = "com.google.android.gms.permission.BIND_NETWORK_TASK_SERVICE";
    /* access modifiers changed from: private */
    public ComponentName componentName;
    /* access modifiers changed from: private */
    public final Object lock = new Object();
    /* access modifiers changed from: private */
    public zzl zzg;
    /* access modifiers changed from: private */
    @GuardedBy("lock")
    public int zzu;
    private ExecutorService zzv;
    private Messenger zzw;
    /* access modifiers changed from: private */
    public GcmNetworkManager zzx;

    @TargetApi(21)
    @VisibleForTesting
    class zzd extends zzj {
        zzd(Looper looper) {
            super(looper);
        }

        public final void handleMessage(Message message) {
            if (!UidVerifier.uidHasPackageName(GcmTaskService.this, message.sendingUid, "com.google.android.gms")) {
                Log.e("GcmTaskService", "unable to verify presence of Google Play Services");
                return;
            }
            int i = message.what;
            if (i != 4) {
                switch (i) {
                    case 1:
                        Bundle data = message.getData();
                        if (!data.isEmpty()) {
                            Messenger messenger = message.replyTo;
                            if (messenger != null) {
                                String string = data.getString("tag");
                                ArrayList parcelableArrayList = data.getParcelableArrayList("triggered_uris");
                                long j = data.getLong("max_exec_duration", 180);
                                if (!GcmTaskService.this.zzg(string)) {
                                    zze zze = new zze(string, messenger, data.getBundle("extras"), j, (List<Uri>) parcelableArrayList);
                                    GcmTaskService.this.zzd(zze);
                                }
                            }
                        }
                        return;
                    case 2:
                        if (Log.isLoggable("GcmTaskService", 3)) {
                            String valueOf = String.valueOf(message);
                            StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 45);
                            sb.append("ignoring unimplemented stop message for now: ");
                            sb.append(valueOf);
                            Log.d("GcmTaskService", sb.toString());
                        }
                        return;
                    default:
                        String valueOf2 = String.valueOf(message);
                        StringBuilder sb2 = new StringBuilder(String.valueOf(valueOf2).length() + 31);
                        sb2.append("Unrecognized message received: ");
                        sb2.append(valueOf2);
                        Log.e("GcmTaskService", sb2.toString());
                        return;
                }
            } else {
                GcmTaskService.this.onInitializeTasks();
            }
        }
    }

    class zze implements Runnable {
        private final Bundle extras;
        private final String tag;
        private final List<Uri> zzaa;
        private final long zzab;
        @Nullable
        private final zzg zzac;
        @Nullable
        private final Messenger zzad;

        zze(String str, @NonNull IBinder iBinder, Bundle bundle, long j, List<Uri> list) {
            zzg zzg;
            this.tag = str;
            if (iBinder == null) {
                zzg = null;
            } else {
                IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.gcm.INetworkTaskCallback");
                if (queryLocalInterface instanceof zzg) {
                    zzg = (zzg) queryLocalInterface;
                } else {
                    zzg = new zzh(iBinder);
                }
            }
            this.zzac = zzg;
            this.extras = bundle;
            this.zzab = j;
            this.zzaa = list;
            this.zzad = null;
        }

        zze(String str, @NonNull Messenger messenger, Bundle bundle, long j, List<Uri> list) {
            this.tag = str;
            this.zzad = messenger;
            this.extras = bundle;
            this.zzab = j;
            this.zzaa = list;
            this.zzac = null;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:19:0x0050, code lost:
            r2 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x0054, code lost:
            zzd(r1, r0);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:24:0x0057, code lost:
            throw r2;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final void run() {
            /*
                r9 = this;
                com.google.android.gms.gcm.zzp r0 = new com.google.android.gms.gcm.zzp
                java.lang.String r1 = "nts:client:onRunTask:"
                java.lang.String r1 = java.lang.String.valueOf(r1)
                java.lang.String r2 = r9.tag
                java.lang.String r2 = java.lang.String.valueOf(r2)
                int r3 = r2.length()
                if (r3 == 0) goto L_0x0019
                java.lang.String r1 = r1.concat(r2)
                goto L_0x001f
            L_0x0019:
                java.lang.String r2 = new java.lang.String
                r2.<init>(r1)
                r1 = r2
            L_0x001f:
                r0.<init>(r1)
                r1 = 0
                com.google.android.gms.gcm.TaskParams r8 = new com.google.android.gms.gcm.TaskParams     // Catch:{ Throwable -> 0x0052 }
                java.lang.String r3 = r9.tag     // Catch:{ Throwable -> 0x0052 }
                android.os.Bundle r4 = r9.extras     // Catch:{ Throwable -> 0x0052 }
                long r5 = r9.zzab     // Catch:{ Throwable -> 0x0052 }
                java.util.List<android.net.Uri> r7 = r9.zzaa     // Catch:{ Throwable -> 0x0052 }
                r2 = r8
                r2.<init>(r3, r4, r5, r7)     // Catch:{ Throwable -> 0x0052 }
                com.google.android.gms.gcm.GcmTaskService r2 = com.google.android.gms.gcm.GcmTaskService.this     // Catch:{ Throwable -> 0x0052 }
                com.google.android.gms.internal.gcm.zzl r2 = r2.zzg     // Catch:{ Throwable -> 0x0052 }
                java.lang.String r3 = "onRunTask"
                int r4 = com.google.android.gms.internal.gcm.zzp.zzdo     // Catch:{ Throwable -> 0x0052 }
                r2.zzd(r3, r4)     // Catch:{ Throwable -> 0x0052 }
                com.google.android.gms.gcm.GcmTaskService r2 = com.google.android.gms.gcm.GcmTaskService.this     // Catch:{ Throwable -> 0x004d }
                int r2 = r2.onRunTask(r8)     // Catch:{ Throwable -> 0x004d }
                r9.zze(r2)     // Catch:{ Throwable -> 0x0052 }
                zzd(r1, r0)
                return
            L_0x004b:
                r2 = move-exception
                goto L_0x004f
            L_0x004d:
                r2 = move-exception
                throw r2     // Catch:{ all -> 0x004b }
            L_0x004f:
                throw r2     // Catch:{ Throwable -> 0x0052 }
            L_0x0050:
                r2 = move-exception
                goto L_0x0054
            L_0x0052:
                r1 = move-exception
                throw r1     // Catch:{ all -> 0x0050 }
            L_0x0054:
                zzd(r1, r0)
                throw r2
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.gcm.GcmTaskService.zze.run():void");
        }

        /* access modifiers changed from: private */
        /* JADX WARNING: Code restructure failed: missing block: B:12:0x005c, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final void zze(int r6) {
            /*
                r5 = this;
                com.google.android.gms.gcm.GcmTaskService r0 = com.google.android.gms.gcm.GcmTaskService.this
                java.lang.Object r0 = r0.lock
                monitor-enter(r0)
                com.google.android.gms.gcm.GcmTaskService r1 = com.google.android.gms.gcm.GcmTaskService.this     // Catch:{ RemoteException -> 0x00d0 }
                com.google.android.gms.gcm.GcmNetworkManager r1 = r1.zzx     // Catch:{ RemoteException -> 0x00d0 }
                java.lang.String r2 = r5.tag     // Catch:{ RemoteException -> 0x00d0 }
                com.google.android.gms.gcm.GcmTaskService r3 = com.google.android.gms.gcm.GcmTaskService.this     // Catch:{ RemoteException -> 0x00d0 }
                android.content.ComponentName r3 = r3.componentName     // Catch:{ RemoteException -> 0x00d0 }
                java.lang.String r3 = r3.getClassName()     // Catch:{ RemoteException -> 0x00d0 }
                boolean r1 = r1.zzf(r2, r3)     // Catch:{ RemoteException -> 0x00d0 }
                if (r1 == 0) goto L_0x005d
                com.google.android.gms.gcm.GcmTaskService r6 = com.google.android.gms.gcm.GcmTaskService.this     // Catch:{ all -> 0x0169 }
                com.google.android.gms.gcm.GcmNetworkManager r6 = r6.zzx     // Catch:{ all -> 0x0169 }
                java.lang.String r1 = r5.tag     // Catch:{ all -> 0x0169 }
                com.google.android.gms.gcm.GcmTaskService r2 = com.google.android.gms.gcm.GcmTaskService.this     // Catch:{ all -> 0x0169 }
                android.content.ComponentName r2 = r2.componentName     // Catch:{ all -> 0x0169 }
                java.lang.String r2 = r2.getClassName()     // Catch:{ all -> 0x0169 }
                r6.zze(r1, r2)     // Catch:{ all -> 0x0169 }
                boolean r6 = r5.zzg()     // Catch:{ all -> 0x0169 }
                if (r6 != 0) goto L_0x005b
                com.google.android.gms.gcm.GcmTaskService r6 = com.google.android.gms.gcm.GcmTaskService.this     // Catch:{ all -> 0x0169 }
                com.google.android.gms.gcm.GcmNetworkManager r6 = r6.zzx     // Catch:{ all -> 0x0169 }
                com.google.android.gms.gcm.GcmTaskService r1 = com.google.android.gms.gcm.GcmTaskService.this     // Catch:{ all -> 0x0169 }
                android.content.ComponentName r1 = r1.componentName     // Catch:{ all -> 0x0169 }
                java.lang.String r1 = r1.getClassName()     // Catch:{ all -> 0x0169 }
                boolean r6 = r6.zzf(r1)     // Catch:{ all -> 0x0169 }
                if (r6 != 0) goto L_0x005b
                com.google.android.gms.gcm.GcmTaskService r6 = com.google.android.gms.gcm.GcmTaskService.this     // Catch:{ all -> 0x0169 }
                com.google.android.gms.gcm.GcmTaskService r1 = com.google.android.gms.gcm.GcmTaskService.this     // Catch:{ all -> 0x0169 }
                int r1 = r1.zzu     // Catch:{ all -> 0x0169 }
                r6.stopSelf(r1)     // Catch:{ all -> 0x0169 }
            L_0x005b:
                monitor-exit(r0)     // Catch:{ all -> 0x0169 }
                return
            L_0x005d:
                boolean r1 = r5.zzg()     // Catch:{ RemoteException -> 0x00d0 }
                if (r1 == 0) goto L_0x008c
                android.os.Messenger r1 = r5.zzad     // Catch:{ RemoteException -> 0x00d0 }
                android.os.Message r2 = android.os.Message.obtain()     // Catch:{ RemoteException -> 0x00d0 }
                r3 = 3
                r2.what = r3     // Catch:{ RemoteException -> 0x00d0 }
                r2.arg1 = r6     // Catch:{ RemoteException -> 0x00d0 }
                android.os.Bundle r6 = new android.os.Bundle     // Catch:{ RemoteException -> 0x00d0 }
                r6.<init>()     // Catch:{ RemoteException -> 0x00d0 }
                java.lang.String r3 = "component"
                com.google.android.gms.gcm.GcmTaskService r4 = com.google.android.gms.gcm.GcmTaskService.this     // Catch:{ RemoteException -> 0x00d0 }
                android.content.ComponentName r4 = r4.componentName     // Catch:{ RemoteException -> 0x00d0 }
                r6.putParcelable(r3, r4)     // Catch:{ RemoteException -> 0x00d0 }
                java.lang.String r3 = "tag"
                java.lang.String r4 = r5.tag     // Catch:{ RemoteException -> 0x00d0 }
                r6.putString(r3, r4)     // Catch:{ RemoteException -> 0x00d0 }
                r2.setData(r6)     // Catch:{ RemoteException -> 0x00d0 }
                r1.send(r2)     // Catch:{ RemoteException -> 0x00d0 }
                goto L_0x0091
            L_0x008c:
                com.google.android.gms.gcm.zzg r1 = r5.zzac     // Catch:{ RemoteException -> 0x00d0 }
                r1.zzf(r6)     // Catch:{ RemoteException -> 0x00d0 }
            L_0x0091:
                com.google.android.gms.gcm.GcmTaskService r6 = com.google.android.gms.gcm.GcmTaskService.this     // Catch:{ all -> 0x0169 }
                com.google.android.gms.gcm.GcmNetworkManager r6 = r6.zzx     // Catch:{ all -> 0x0169 }
                java.lang.String r1 = r5.tag     // Catch:{ all -> 0x0169 }
                com.google.android.gms.gcm.GcmTaskService r2 = com.google.android.gms.gcm.GcmTaskService.this     // Catch:{ all -> 0x0169 }
                android.content.ComponentName r2 = r2.componentName     // Catch:{ all -> 0x0169 }
                java.lang.String r2 = r2.getClassName()     // Catch:{ all -> 0x0169 }
                r6.zze(r1, r2)     // Catch:{ all -> 0x0169 }
                boolean r6 = r5.zzg()     // Catch:{ all -> 0x0169 }
                if (r6 != 0) goto L_0x012a
                com.google.android.gms.gcm.GcmTaskService r6 = com.google.android.gms.gcm.GcmTaskService.this     // Catch:{ all -> 0x0169 }
                com.google.android.gms.gcm.GcmNetworkManager r6 = r6.zzx     // Catch:{ all -> 0x0169 }
                com.google.android.gms.gcm.GcmTaskService r1 = com.google.android.gms.gcm.GcmTaskService.this     // Catch:{ all -> 0x0169 }
                android.content.ComponentName r1 = r1.componentName     // Catch:{ all -> 0x0169 }
                java.lang.String r1 = r1.getClassName()     // Catch:{ all -> 0x0169 }
                boolean r6 = r6.zzf(r1)     // Catch:{ all -> 0x0169 }
                if (r6 != 0) goto L_0x012a
                com.google.android.gms.gcm.GcmTaskService r6 = com.google.android.gms.gcm.GcmTaskService.this     // Catch:{ all -> 0x0169 }
                com.google.android.gms.gcm.GcmTaskService r1 = com.google.android.gms.gcm.GcmTaskService.this     // Catch:{ all -> 0x0169 }
                int r1 = r1.zzu     // Catch:{ all -> 0x0169 }
                r6.stopSelf(r1)     // Catch:{ all -> 0x0169 }
                goto L_0x012a
            L_0x00ce:
                r6 = move-exception
                goto L_0x012c
            L_0x00d0:
                java.lang.String r6 = "GcmTaskService"
                java.lang.String r1 = "Error reporting result of operation to scheduler for "
                java.lang.String r2 = r5.tag     // Catch:{ all -> 0x00ce }
                java.lang.String r2 = java.lang.String.valueOf(r2)     // Catch:{ all -> 0x00ce }
                int r3 = r2.length()     // Catch:{ all -> 0x00ce }
                if (r3 == 0) goto L_0x00e5
                java.lang.String r1 = r1.concat(r2)     // Catch:{ all -> 0x00ce }
                goto L_0x00eb
            L_0x00e5:
                java.lang.String r2 = new java.lang.String     // Catch:{ all -> 0x00ce }
                r2.<init>(r1)     // Catch:{ all -> 0x00ce }
                r1 = r2
            L_0x00eb:
                android.util.Log.e(r6, r1)     // Catch:{ all -> 0x00ce }
                com.google.android.gms.gcm.GcmTaskService r6 = com.google.android.gms.gcm.GcmTaskService.this     // Catch:{ all -> 0x0169 }
                com.google.android.gms.gcm.GcmNetworkManager r6 = r6.zzx     // Catch:{ all -> 0x0169 }
                java.lang.String r1 = r5.tag     // Catch:{ all -> 0x0169 }
                com.google.android.gms.gcm.GcmTaskService r2 = com.google.android.gms.gcm.GcmTaskService.this     // Catch:{ all -> 0x0169 }
                android.content.ComponentName r2 = r2.componentName     // Catch:{ all -> 0x0169 }
                java.lang.String r2 = r2.getClassName()     // Catch:{ all -> 0x0169 }
                r6.zze(r1, r2)     // Catch:{ all -> 0x0169 }
                boolean r6 = r5.zzg()     // Catch:{ all -> 0x0169 }
                if (r6 != 0) goto L_0x012a
                com.google.android.gms.gcm.GcmTaskService r6 = com.google.android.gms.gcm.GcmTaskService.this     // Catch:{ all -> 0x0169 }
                com.google.android.gms.gcm.GcmNetworkManager r6 = r6.zzx     // Catch:{ all -> 0x0169 }
                com.google.android.gms.gcm.GcmTaskService r1 = com.google.android.gms.gcm.GcmTaskService.this     // Catch:{ all -> 0x0169 }
                android.content.ComponentName r1 = r1.componentName     // Catch:{ all -> 0x0169 }
                java.lang.String r1 = r1.getClassName()     // Catch:{ all -> 0x0169 }
                boolean r6 = r6.zzf(r1)     // Catch:{ all -> 0x0169 }
                if (r6 != 0) goto L_0x012a
                com.google.android.gms.gcm.GcmTaskService r6 = com.google.android.gms.gcm.GcmTaskService.this     // Catch:{ all -> 0x0169 }
                com.google.android.gms.gcm.GcmTaskService r1 = com.google.android.gms.gcm.GcmTaskService.this     // Catch:{ all -> 0x0169 }
                int r1 = r1.zzu     // Catch:{ all -> 0x0169 }
                r6.stopSelf(r1)     // Catch:{ all -> 0x0169 }
            L_0x012a:
                monitor-exit(r0)     // Catch:{ all -> 0x0169 }
                return
            L_0x012c:
                com.google.android.gms.gcm.GcmTaskService r1 = com.google.android.gms.gcm.GcmTaskService.this     // Catch:{ all -> 0x0169 }
                com.google.android.gms.gcm.GcmNetworkManager r1 = r1.zzx     // Catch:{ all -> 0x0169 }
                java.lang.String r2 = r5.tag     // Catch:{ all -> 0x0169 }
                com.google.android.gms.gcm.GcmTaskService r3 = com.google.android.gms.gcm.GcmTaskService.this     // Catch:{ all -> 0x0169 }
                android.content.ComponentName r3 = r3.componentName     // Catch:{ all -> 0x0169 }
                java.lang.String r3 = r3.getClassName()     // Catch:{ all -> 0x0169 }
                r1.zze(r2, r3)     // Catch:{ all -> 0x0169 }
                boolean r1 = r5.zzg()     // Catch:{ all -> 0x0169 }
                if (r1 != 0) goto L_0x0168
                com.google.android.gms.gcm.GcmTaskService r1 = com.google.android.gms.gcm.GcmTaskService.this     // Catch:{ all -> 0x0169 }
                com.google.android.gms.gcm.GcmNetworkManager r1 = r1.zzx     // Catch:{ all -> 0x0169 }
                com.google.android.gms.gcm.GcmTaskService r2 = com.google.android.gms.gcm.GcmTaskService.this     // Catch:{ all -> 0x0169 }
                android.content.ComponentName r2 = r2.componentName     // Catch:{ all -> 0x0169 }
                java.lang.String r2 = r2.getClassName()     // Catch:{ all -> 0x0169 }
                boolean r1 = r1.zzf(r2)     // Catch:{ all -> 0x0169 }
                if (r1 != 0) goto L_0x0168
                com.google.android.gms.gcm.GcmTaskService r1 = com.google.android.gms.gcm.GcmTaskService.this     // Catch:{ all -> 0x0169 }
                com.google.android.gms.gcm.GcmTaskService r2 = com.google.android.gms.gcm.GcmTaskService.this     // Catch:{ all -> 0x0169 }
                int r2 = r2.zzu     // Catch:{ all -> 0x0169 }
                r1.stopSelf(r2)     // Catch:{ all -> 0x0169 }
            L_0x0168:
                throw r6     // Catch:{ all -> 0x0169 }
            L_0x0169:
                r6 = move-exception
                monitor-exit(r0)     // Catch:{ all -> 0x0169 }
                throw r6
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.gcm.GcmTaskService.zze.zze(int):void");
        }

        private final boolean zzg() {
            return this.zzad != null;
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

    public void onInitializeTasks() {
    }

    public abstract int onRunTask(TaskParams taskParams);

    @CallSuper
    public void onCreate() {
        super.onCreate();
        this.zzx = GcmNetworkManager.getInstance(this);
        this.zzv = zzg.zzaa().zzd(10, new zze(this), 10);
        this.zzw = new Messenger(new zzd(Looper.getMainLooper()));
        this.componentName = new ComponentName(this, getClass());
        zzm.zzab();
        getClass();
        this.zzg = zzm.zzdk;
    }

    @CallSuper
    public void onDestroy() {
        super.onDestroy();
        List shutdownNow = this.zzv.shutdownNow();
        if (!shutdownNow.isEmpty()) {
            int size = shutdownNow.size();
            StringBuilder sb = new StringBuilder(79);
            sb.append("Shutting down, but not all tasks are finished executing. Remaining: ");
            sb.append(size);
            Log.e("GcmTaskService", sb.toString());
        }
    }

    @CallSuper
    public int onStartCommand(Intent intent, int i, int i2) {
        if (intent == null) {
            zzd(i2);
            return 2;
        }
        try {
            intent.setExtrasClassLoader(PendingCallback.class.getClassLoader());
            String action = intent.getAction();
            if (SERVICE_ACTION_EXECUTE_TASK.equals(action)) {
                String stringExtra = intent.getStringExtra("tag");
                Parcelable parcelableExtra = intent.getParcelableExtra("callback");
                Bundle bundleExtra = intent.getBundleExtra("extras");
                ArrayList parcelableArrayListExtra = intent.getParcelableArrayListExtra("triggered_uris");
                long longExtra = intent.getLongExtra("max_exec_duration", 180);
                if (!(parcelableExtra instanceof PendingCallback)) {
                    String packageName = getPackageName();
                    StringBuilder sb = new StringBuilder(String.valueOf(packageName).length() + 47 + String.valueOf(stringExtra).length());
                    sb.append(packageName);
                    sb.append(" ");
                    sb.append(stringExtra);
                    sb.append(": Could not process request, invalid callback.");
                    Log.e("GcmTaskService", sb.toString());
                    return 2;
                } else if (zzg(stringExtra)) {
                    zzd(i2);
                    return 2;
                } else {
                    zze zze2 = new zze(stringExtra, ((PendingCallback) parcelableExtra).zzan, bundleExtra, longExtra, (List<Uri>) parcelableArrayListExtra);
                    zzd(zze2);
                }
            } else if (SERVICE_ACTION_INITIALIZE.equals(action)) {
                onInitializeTasks();
            } else {
                StringBuilder sb2 = new StringBuilder(String.valueOf(action).length() + 37);
                sb2.append("Unknown action received ");
                sb2.append(action);
                sb2.append(", terminating");
                Log.e("GcmTaskService", sb2.toString());
            }
            zzd(i2);
            return 2;
        } finally {
            zzd(i2);
        }
    }

    /* access modifiers changed from: private */
    public final boolean zzg(String str) {
        boolean z;
        synchronized (this.lock) {
            z = !this.zzx.zzd(str, this.componentName.getClassName());
            if (z) {
                String packageName = getPackageName();
                StringBuilder sb = new StringBuilder(String.valueOf(packageName).length() + 44 + String.valueOf(str).length());
                sb.append(packageName);
                sb.append(" ");
                sb.append(str);
                sb.append(": Task already running, won't start another");
                Log.w("GcmTaskService", sb.toString());
            }
        }
        return z;
    }

    private final void zzd(int i) {
        synchronized (this.lock) {
            this.zzu = i;
            if (!this.zzx.zzf(this.componentName.getClassName())) {
                stopSelf(this.zzu);
            }
        }
    }

    @CallSuper
    public IBinder onBind(Intent intent) {
        if (intent == null || !PlatformVersion.isAtLeastLollipop() || !SERVICE_ACTION_EXECUTE_TASK.equals(intent.getAction())) {
            return null;
        }
        return this.zzw.getBinder();
    }

    /* access modifiers changed from: private */
    public final void zzd(zze zze2) {
        try {
            this.zzv.execute(zze2);
        } catch (RejectedExecutionException e) {
            Log.e("GcmTaskService", "Executor is shutdown. onDestroy was called but main looper had an unprocessed start task message. The task will be retried with backoff delay.", e);
            zze2.zze(1);
        }
    }
}

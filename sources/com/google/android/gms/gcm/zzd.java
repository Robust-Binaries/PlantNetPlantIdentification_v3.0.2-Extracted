package com.google.android.gms.gcm;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import java.util.MissingFormatArgumentException;
import java.util.concurrent.atomic.AtomicInteger;
import org.json.JSONArray;
import org.json.JSONException;

final class zzd {
    static zzd zzk;
    private final Context zzl;
    private String zzm;
    private final AtomicInteger zzn = new AtomicInteger((int) SystemClock.elapsedRealtime());

    static synchronized zzd zzd(Context context) {
        zzd zzd;
        synchronized (zzd.class) {
            if (zzk == null) {
                zzk = new zzd(context);
            }
            zzd = zzk;
        }
        return zzd;
    }

    static String zzd(Bundle bundle, String str) {
        String string = bundle.getString(str);
        return string == null ? bundle.getString(str.replace("gcm.n.", "gcm.notification.")) : string;
    }

    private zzd(Context context) {
        this.zzl = context.getApplicationContext();
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x009c  */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x009e  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0102  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0116  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x014a  */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x019d  */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x01cf  */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x01d1  */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x0235  */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x023e  */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x0253  */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x025c  */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x0261  */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x0266  */
    /* JADX WARNING: Removed duplicated region for block: B:79:0x027b  */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x0292  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean zze(android.os.Bundle r13) {
        /*
            r12 = this;
            java.lang.String r0 = "gcm.n.title"
            java.lang.String r0 = r12.zze(r13, r0)
            boolean r1 = android.text.TextUtils.isEmpty(r0)
            if (r1 == 0) goto L_0x001c
            android.content.Context r0 = r12.zzl
            android.content.pm.ApplicationInfo r0 = r0.getApplicationInfo()
            android.content.Context r1 = r12.zzl
            android.content.pm.PackageManager r1 = r1.getPackageManager()
            java.lang.CharSequence r0 = r0.loadLabel(r1)
        L_0x001c:
            java.lang.String r1 = "gcm.n.body"
            java.lang.String r1 = r12.zze(r13, r1)
            java.lang.String r2 = "gcm.n.icon"
            java.lang.String r2 = zzd(r13, r2)
            boolean r3 = android.text.TextUtils.isEmpty(r2)
            if (r3 != 0) goto L_0x0077
            android.content.Context r3 = r12.zzl
            android.content.res.Resources r3 = r3.getResources()
            java.lang.String r4 = "drawable"
            android.content.Context r5 = r12.zzl
            java.lang.String r5 = r5.getPackageName()
            int r4 = r3.getIdentifier(r2, r4, r5)
            if (r4 == 0) goto L_0x0043
            goto L_0x0089
        L_0x0043:
            java.lang.String r4 = "mipmap"
            android.content.Context r5 = r12.zzl
            java.lang.String r5 = r5.getPackageName()
            int r4 = r3.getIdentifier(r2, r4, r5)
            if (r4 == 0) goto L_0x0052
            goto L_0x0089
        L_0x0052:
            java.lang.String r3 = "GcmNotification"
            java.lang.String r4 = java.lang.String.valueOf(r2)
            int r4 = r4.length()
            int r4 = r4 + 57
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>(r4)
            java.lang.String r4 = "Icon resource "
            r5.append(r4)
            r5.append(r2)
            java.lang.String r2 = " not found. Notification will use app icon."
            r5.append(r2)
            java.lang.String r2 = r5.toString()
            android.util.Log.w(r3, r2)
        L_0x0077:
            android.content.Context r2 = r12.zzl
            android.content.pm.ApplicationInfo r2 = r2.getApplicationInfo()
            int r2 = r2.icon
            if (r2 != 0) goto L_0x0088
            r2 = 17301651(0x1080093, float:2.4979667E-38)
            r4 = 17301651(0x1080093, float:2.4979667E-38)
            goto L_0x0089
        L_0x0088:
            r4 = r2
        L_0x0089:
            java.lang.String r2 = "gcm.n.color"
            java.lang.String r2 = zzd(r13, r2)
            java.lang.String r3 = "gcm.n.sound2"
            java.lang.String r3 = zzd(r13, r3)
            boolean r5 = android.text.TextUtils.isEmpty(r3)
            r6 = 0
            if (r5 == 0) goto L_0x009e
            r3 = r6
            goto L_0x00f6
        L_0x009e:
            java.lang.String r5 = "default"
            boolean r5 = r5.equals(r3)
            if (r5 != 0) goto L_0x00f1
            android.content.Context r5 = r12.zzl
            android.content.res.Resources r5 = r5.getResources()
            java.lang.String r7 = "raw"
            android.content.Context r8 = r12.zzl
            java.lang.String r8 = r8.getPackageName()
            int r5 = r5.getIdentifier(r3, r7, r8)
            if (r5 == 0) goto L_0x00f1
            android.content.Context r5 = r12.zzl
            java.lang.String r5 = r5.getPackageName()
            java.lang.String r7 = java.lang.String.valueOf(r5)
            int r7 = r7.length()
            int r7 = r7 + 24
            java.lang.String r8 = java.lang.String.valueOf(r3)
            int r8 = r8.length()
            int r7 = r7 + r8
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>(r7)
            java.lang.String r7 = "android.resource://"
            r8.append(r7)
            r8.append(r5)
            java.lang.String r5 = "/raw/"
            r8.append(r5)
            r8.append(r3)
            java.lang.String r3 = r8.toString()
            android.net.Uri r3 = android.net.Uri.parse(r3)
            goto L_0x00f6
        L_0x00f1:
            r3 = 2
            android.net.Uri r3 = android.media.RingtoneManager.getDefaultUri(r3)
        L_0x00f6:
            java.lang.String r5 = "gcm.n.click_action"
            java.lang.String r5 = zzd(r13, r5)
            boolean r7 = android.text.TextUtils.isEmpty(r5)
            if (r7 != 0) goto L_0x0116
            android.content.Intent r7 = new android.content.Intent
            r7.<init>(r5)
            android.content.Context r5 = r12.zzl
            java.lang.String r5 = r5.getPackageName()
            r7.setPackage(r5)
            r5 = 268435456(0x10000000, float:2.5243549E-29)
            r7.setFlags(r5)
            goto L_0x0131
        L_0x0116:
            android.content.Context r5 = r12.zzl
            android.content.pm.PackageManager r5 = r5.getPackageManager()
            android.content.Context r7 = r12.zzl
            java.lang.String r7 = r7.getPackageName()
            android.content.Intent r7 = r5.getLaunchIntentForPackage(r7)
            if (r7 != 0) goto L_0x0131
            java.lang.String r5 = "GcmNotification"
            java.lang.String r7 = "No activity found to launch app"
            android.util.Log.w(r5, r7)
            r5 = r6
            goto L_0x0172
        L_0x0131:
            android.os.Bundle r5 = new android.os.Bundle
            r5.<init>(r13)
            com.google.android.gms.gcm.GcmListenerService.zzd(r5)
            r7.putExtras(r5)
            java.util.Set r5 = r5.keySet()
            java.util.Iterator r5 = r5.iterator()
        L_0x0144:
            boolean r8 = r5.hasNext()
            if (r8 == 0) goto L_0x0164
            java.lang.Object r8 = r5.next()
            java.lang.String r8 = (java.lang.String) r8
            java.lang.String r9 = "gcm.n."
            boolean r9 = r8.startsWith(r9)
            if (r9 != 0) goto L_0x0160
            java.lang.String r9 = "gcm.notification."
            boolean r9 = r8.startsWith(r9)
            if (r9 == 0) goto L_0x0144
        L_0x0160:
            r7.removeExtra(r8)
            goto L_0x0144
        L_0x0164:
            android.content.Context r5 = r12.zzl
            java.util.concurrent.atomic.AtomicInteger r8 = r12.zzn
            int r8 = r8.getAndIncrement()
            r9 = 1073741824(0x40000000, float:2.0)
            android.app.PendingIntent r5 = android.app.PendingIntent.getActivity(r5, r8, r7, r9)
        L_0x0172:
            java.lang.String r7 = "gcm.n.android_channel_id"
            java.lang.String r7 = zzd(r13, r7)
            boolean r8 = com.google.android.gms.common.util.PlatformVersion.isAtLeastO()
            r9 = 3
            if (r8 == 0) goto L_0x021f
            android.content.Context r8 = r12.zzl
            android.content.pm.ApplicationInfo r8 = r8.getApplicationInfo()
            int r8 = r8.targetSdkVersion
            r10 = 26
            if (r8 >= r10) goto L_0x018d
            goto L_0x021f
        L_0x018d:
            android.content.Context r6 = r12.zzl
            java.lang.Class<android.app.NotificationManager> r8 = android.app.NotificationManager.class
            java.lang.Object r6 = r6.getSystemService(r8)
            android.app.NotificationManager r6 = (android.app.NotificationManager) r6
            boolean r8 = android.text.TextUtils.isEmpty(r7)
            if (r8 != 0) goto L_0x01cb
            android.app.NotificationChannel r8 = r6.getNotificationChannel(r7)
            if (r8 == 0) goto L_0x01a6
            r6 = r7
            goto L_0x021f
        L_0x01a6:
            java.lang.String r8 = "GcmNotification"
            java.lang.String r10 = java.lang.String.valueOf(r7)
            int r10 = r10.length()
            int r10 = r10 + 122
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>(r10)
            java.lang.String r10 = "Notification Channel requested ("
            r11.append(r10)
            r11.append(r7)
            java.lang.String r7 = ") has not been created by the app. Manifest configuration, or default, value will be used."
            r11.append(r7)
            java.lang.String r7 = r11.toString()
            android.util.Log.w(r8, r7)
        L_0x01cb:
            java.lang.String r7 = r12.zzm
            if (r7 == 0) goto L_0x01d1
            r6 = r7
            goto L_0x021f
        L_0x01d1:
            android.os.Bundle r7 = r12.zzf()
            java.lang.String r8 = "com.google.android.gms.gcm.default_notification_channel_id"
            java.lang.String r7 = r7.getString(r8)
            r12.zzm = r7
            java.lang.String r7 = r12.zzm
            boolean r7 = android.text.TextUtils.isEmpty(r7)
            if (r7 != 0) goto L_0x01f8
            java.lang.String r7 = r12.zzm
            android.app.NotificationChannel r7 = r6.getNotificationChannel(r7)
            if (r7 == 0) goto L_0x01f0
            java.lang.String r6 = r12.zzm
            goto L_0x021f
        L_0x01f0:
            java.lang.String r7 = "GcmNotification"
            java.lang.String r8 = "Notification Channel set in AndroidManifest.xml has not been created by the app. Default value will be used."
            android.util.Log.w(r7, r8)
            goto L_0x01ff
        L_0x01f8:
            java.lang.String r7 = "GcmNotification"
            java.lang.String r8 = "Missing Default Notification Channel metadata in AndroidManifest. Default value will be used."
            android.util.Log.w(r7, r8)
        L_0x01ff:
            java.lang.String r7 = "fcm_fallback_notification_channel"
            android.app.NotificationChannel r7 = r6.getNotificationChannel(r7)
            if (r7 != 0) goto L_0x0219
            android.app.NotificationChannel r7 = new android.app.NotificationChannel
            java.lang.String r8 = "fcm_fallback_notification_channel"
            android.content.Context r10 = r12.zzl
            int r11 = com.google.android.gms.gcm.C1124R.string.gcm_fallback_notification_channel_label
            java.lang.String r10 = r10.getString(r11)
            r7.<init>(r8, r10, r9)
            r6.createNotificationChannel(r7)
        L_0x0219:
            java.lang.String r6 = "fcm_fallback_notification_channel"
            r12.zzm = r6
            java.lang.String r6 = r12.zzm
        L_0x021f:
            android.support.v4.app.NotificationCompat$Builder r7 = new android.support.v4.app.NotificationCompat$Builder
            android.content.Context r8 = r12.zzl
            r7.<init>(r8)
            r8 = 1
            android.support.v4.app.NotificationCompat$Builder r7 = r7.setAutoCancel(r8)
            android.support.v4.app.NotificationCompat$Builder r4 = r7.setSmallIcon(r4)
            boolean r7 = android.text.TextUtils.isEmpty(r0)
            if (r7 != 0) goto L_0x0238
            r4.setContentTitle(r0)
        L_0x0238:
            boolean r0 = android.text.TextUtils.isEmpty(r1)
            if (r0 != 0) goto L_0x024d
            r4.setContentText(r1)
            android.support.v4.app.NotificationCompat$BigTextStyle r0 = new android.support.v4.app.NotificationCompat$BigTextStyle
            r0.<init>()
            android.support.v4.app.NotificationCompat$BigTextStyle r0 = r0.bigText(r1)
            r4.setStyle(r0)
        L_0x024d:
            boolean r0 = android.text.TextUtils.isEmpty(r2)
            if (r0 != 0) goto L_0x025a
            int r0 = android.graphics.Color.parseColor(r2)
            r4.setColor(r0)
        L_0x025a:
            if (r3 == 0) goto L_0x025f
            r4.setSound(r3)
        L_0x025f:
            if (r5 == 0) goto L_0x0264
            r4.setContentIntent(r5)
        L_0x0264:
            if (r6 == 0) goto L_0x0269
            r4.setChannelId(r6)
        L_0x0269:
            android.app.Notification r0 = r4.build()
            java.lang.String r1 = "gcm.n.tag"
            java.lang.String r13 = zzd(r13, r1)
            java.lang.String r1 = "GcmNotification"
            boolean r1 = android.util.Log.isLoggable(r1, r9)
            if (r1 == 0) goto L_0x0282
            java.lang.String r1 = "GcmNotification"
            java.lang.String r2 = "Showing notification"
            android.util.Log.d(r1, r2)
        L_0x0282:
            android.content.Context r1 = r12.zzl
            java.lang.String r2 = "notification"
            java.lang.Object r1 = r1.getSystemService(r2)
            android.app.NotificationManager r1 = (android.app.NotificationManager) r1
            boolean r2 = android.text.TextUtils.isEmpty(r13)
            if (r2 == 0) goto L_0x02a9
            long r2 = android.os.SystemClock.uptimeMillis()
            r13 = 37
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>(r13)
            java.lang.String r13 = "GCM-Notification:"
            r4.append(r13)
            r4.append(r2)
            java.lang.String r13 = r4.toString()
        L_0x02a9:
            r2 = 0
            r1.notify(r13, r2, r0)
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.gcm.zzd.zze(android.os.Bundle):boolean");
    }

    private final String zze(Bundle bundle, String str) {
        String zzd = zzd(bundle, str);
        if (!TextUtils.isEmpty(zzd)) {
            return zzd;
        }
        String valueOf = String.valueOf(str);
        String valueOf2 = String.valueOf("_loc_key");
        String zzd2 = zzd(bundle, valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf));
        if (TextUtils.isEmpty(zzd2)) {
            return null;
        }
        Resources resources = this.zzl.getResources();
        int identifier = resources.getIdentifier(zzd2, "string", this.zzl.getPackageName());
        if (identifier == 0) {
            String str2 = "GcmNotification";
            String valueOf3 = String.valueOf(str);
            String valueOf4 = String.valueOf("_loc_key");
            String substring = (valueOf4.length() != 0 ? valueOf3.concat(valueOf4) : new String(valueOf3)).substring(6);
            StringBuilder sb = new StringBuilder(String.valueOf(substring).length() + 49 + String.valueOf(zzd2).length());
            sb.append(substring);
            sb.append(" resource not found: ");
            sb.append(zzd2);
            sb.append(" Default value will be used.");
            Log.w(str2, sb.toString());
            return null;
        }
        String valueOf5 = String.valueOf(str);
        String valueOf6 = String.valueOf("_loc_args");
        String zzd3 = zzd(bundle, valueOf6.length() != 0 ? valueOf5.concat(valueOf6) : new String(valueOf5));
        if (TextUtils.isEmpty(zzd3)) {
            return resources.getString(identifier);
        }
        try {
            JSONArray jSONArray = new JSONArray(zzd3);
            Object[] objArr = new String[jSONArray.length()];
            for (int i = 0; i < objArr.length; i++) {
                objArr[i] = jSONArray.opt(i);
            }
            return resources.getString(identifier, objArr);
        } catch (JSONException unused) {
            String str3 = "GcmNotification";
            String valueOf7 = String.valueOf(str);
            String valueOf8 = String.valueOf("_loc_args");
            String substring2 = (valueOf8.length() != 0 ? valueOf7.concat(valueOf8) : new String(valueOf7)).substring(6);
            StringBuilder sb2 = new StringBuilder(String.valueOf(substring2).length() + 41 + String.valueOf(zzd3).length());
            sb2.append("Malformed ");
            sb2.append(substring2);
            sb2.append(": ");
            sb2.append(zzd3);
            sb2.append("  Default value will be used.");
            Log.w(str3, sb2.toString());
            return null;
        } catch (MissingFormatArgumentException e) {
            StringBuilder sb3 = new StringBuilder(String.valueOf(zzd2).length() + 58 + String.valueOf(zzd3).length());
            sb3.append("Missing format argument for ");
            sb3.append(zzd2);
            sb3.append(": ");
            sb3.append(zzd3);
            sb3.append(" Default value will be used.");
            Log.w("GcmNotification", sb3.toString(), e);
            return null;
        }
    }

    private final Bundle zzf() {
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = this.zzl.getPackageManager().getApplicationInfo(this.zzl.getPackageName(), 128);
        } catch (NameNotFoundException unused) {
            applicationInfo = null;
        }
        if (applicationInfo == null || applicationInfo.metaData == null) {
            return Bundle.EMPTY;
        }
        return applicationInfo.metaData;
    }
}

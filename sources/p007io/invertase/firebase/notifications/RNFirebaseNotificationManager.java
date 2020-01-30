package p007io.invertase.firebase.notifications;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;
import android.support.p000v4.app.NotificationCompat;
import android.support.p000v4.content.LocalBroadcastManager;
import android.util.Log;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.google.android.gms.measurement.api.AppMeasurementSdk.ConditionalUserProperty;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import p007io.invertase.firebase.C1350Utils;
import p007io.invertase.firebase.messaging.BundleJSONConverter;

/* renamed from: io.invertase.firebase.notifications.RNFirebaseNotificationManager */
class RNFirebaseNotificationManager {
    private static final String PREFERENCES_KEY = "RNFNotifications";
    static final String SCHEDULED_NOTIFICATION_EVENT = "notifications-scheduled-notification";
    private static final String TAG = "RNFNotificationManager";
    private AlarmManager alarmManager;
    private Context context;
    private NotificationManager notificationManager;
    private SharedPreferences preferences;
    private ReactApplicationContext reactContext;

    RNFirebaseNotificationManager(ReactApplicationContext reactApplicationContext) {
        this(reactApplicationContext.getApplicationContext());
        this.reactContext = reactApplicationContext;
    }

    RNFirebaseNotificationManager(Context context2) {
        this.alarmManager = (AlarmManager) context2.getSystemService(NotificationCompat.CATEGORY_ALARM);
        this.context = context2;
        this.notificationManager = (NotificationManager) context2.getSystemService("notification");
        this.preferences = context2.getSharedPreferences(PREFERENCES_KEY, 0);
    }

    static int getResourceId(Context context2, String str, String str2) {
        return context2.getResources().getIdentifier(str2, str, context2.getPackageName());
    }

    static Uri getSound(Context context2, String str) {
        if (str == null) {
            return null;
        }
        if (str.contains("://")) {
            return Uri.parse(str);
        }
        if (str.equalsIgnoreCase("default")) {
            return RingtoneManager.getDefaultUri(2);
        }
        int resourceId = getResourceId(context2, "raw", str);
        if (resourceId == 0) {
            resourceId = getResourceId(context2, "raw", str.substring(0, str.lastIndexOf(46)));
        }
        StringBuilder sb = new StringBuilder();
        sb.append("android.resource://");
        sb.append(context2.getPackageName());
        sb.append("/");
        sb.append(resourceId);
        return Uri.parse(sb.toString());
    }

    /* access modifiers changed from: 0000 */
    public void cancelAllNotifications(Promise promise) {
        try {
            for (String cancelAlarm : this.preferences.getAll().keySet()) {
                cancelAlarm(cancelAlarm);
            }
            this.preferences.edit().clear().apply();
            promise.resolve(null);
        } catch (SecurityException e) {
            Log.e(TAG, e.getMessage());
            promise.reject("notification/cancel_notifications_error", "Could not cancel notifications", (Throwable) e);
        }
    }

    /* access modifiers changed from: 0000 */
    public void cancelNotification(String str, Promise promise) {
        try {
            cancelAlarm(str);
            this.preferences.edit().remove(str).apply();
            promise.resolve(null);
        } catch (SecurityException e) {
            Log.e(TAG, e.getMessage());
            promise.reject("notification/cancel_notification_error", "Could not cancel notifications", (Throwable) e);
        }
    }

    /* access modifiers changed from: 0000 */
    public void createChannel(ReadableMap readableMap) {
        if (VERSION.SDK_INT >= 26) {
            this.notificationManager.createNotificationChannel(parseChannelMap(readableMap));
        }
    }

    /* access modifiers changed from: 0000 */
    public void createChannelGroup(ReadableMap readableMap) {
        if (VERSION.SDK_INT >= 26) {
            this.notificationManager.createNotificationChannelGroup(parseChannelGroupMap(readableMap));
        }
    }

    /* access modifiers changed from: 0000 */
    public void createChannelGroups(ReadableArray readableArray) {
        if (VERSION.SDK_INT >= 26) {
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < readableArray.size(); i++) {
                arrayList.add(parseChannelGroupMap(readableArray.getMap(i)));
            }
            this.notificationManager.createNotificationChannelGroups(arrayList);
        }
    }

    /* access modifiers changed from: 0000 */
    public void createChannels(ReadableArray readableArray) {
        if (VERSION.SDK_INT >= 26) {
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < readableArray.size(); i++) {
                arrayList.add(parseChannelMap(readableArray.getMap(i)));
            }
            this.notificationManager.createNotificationChannels(arrayList);
        }
    }

    /* access modifiers changed from: 0000 */
    public void deleteChannelGroup(String str) {
        if (VERSION.SDK_INT >= 26) {
            this.notificationManager.deleteNotificationChannelGroup(str);
        }
    }

    /* access modifiers changed from: 0000 */
    public void deleteChannel(String str) {
        if (VERSION.SDK_INT >= 26) {
            this.notificationManager.deleteNotificationChannel(str);
        }
    }

    /* access modifiers changed from: 0000 */
    public void displayNotification(ReadableMap readableMap, Promise promise) {
        displayNotification(Arguments.toBundle(readableMap), promise);
    }

    /* access modifiers changed from: 0000 */
    public void displayScheduledNotification(Bundle bundle) {
        if (!bundle.getBundle("schedule").containsKey("repeated") || !bundle.getBundle("schedule").getBoolean("repeated")) {
            this.preferences.edit().remove(bundle.getString("notificationId")).apply();
        }
        if (C1350Utils.isAppInForeground(this.context)) {
            Intent intent = new Intent(SCHEDULED_NOTIFICATION_EVENT);
            intent.putExtra("notification", bundle);
            LocalBroadcastManager.getInstance(this.context).sendBroadcast(intent);
            return;
        }
        displayNotification(bundle, (Promise) null);
    }

    /* access modifiers changed from: 0000 */
    public WritableMap getChannel(String str) {
        if (VERSION.SDK_INT >= 26) {
            return createChannelMap(this.notificationManager.getNotificationChannel(str));
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public WritableArray getChannels() {
        if (VERSION.SDK_INT >= 26) {
            return createChannelsArray(this.notificationManager.getNotificationChannels());
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public WritableMap getChannelGroup(String str) {
        if (VERSION.SDK_INT >= 28) {
            return createChannelGroupMap(this.notificationManager.getNotificationChannelGroup(str));
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public WritableArray getChannelGroups() {
        if (VERSION.SDK_INT >= 26) {
            return createChannelGroupsArray(this.notificationManager.getNotificationChannelGroups());
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public ArrayList<Bundle> getScheduledNotifications() {
        ArrayList<Bundle> arrayList = new ArrayList<>();
        Map all = this.preferences.getAll();
        for (String str : all.keySet()) {
            try {
                arrayList.add(BundleJSONConverter.convertToBundle(new JSONObject((String) all.get(str))));
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        }
        return arrayList;
    }

    /* access modifiers changed from: 0000 */
    public void removeAllDeliveredNotifications(Promise promise) {
        this.notificationManager.cancelAll();
        promise.resolve(null);
    }

    /* access modifiers changed from: 0000 */
    public void removeDeliveredNotification(String str, Promise promise) {
        this.notificationManager.cancel(str.hashCode());
        promise.resolve(null);
    }

    /* access modifiers changed from: 0000 */
    public void removeDeliveredNotificationsByTag(String str, Promise promise) {
        StatusBarNotification[] activeNotifications;
        if (VERSION.SDK_INT >= 23) {
            for (StatusBarNotification statusBarNotification : this.notificationManager.getActiveNotifications()) {
                if (str.equals(statusBarNotification.getTag())) {
                    this.notificationManager.cancel(statusBarNotification.getTag(), statusBarNotification.getId());
                }
            }
        }
        promise.resolve(null);
    }

    /* access modifiers changed from: 0000 */
    public void rescheduleNotifications() {
        Iterator it = getScheduledNotifications().iterator();
        while (it.hasNext()) {
            scheduleNotification((Bundle) it.next(), (Promise) null);
        }
    }

    /* access modifiers changed from: 0000 */
    public void scheduleNotification(ReadableMap readableMap, Promise promise) {
        scheduleNotification(Arguments.toBundle(readableMap), promise);
    }

    private void cancelAlarm(String str) {
        this.alarmManager.cancel(PendingIntent.getBroadcast(this.context, str.hashCode(), new Intent(this.context, RNFirebaseNotificationReceiver.class), 134217728));
    }

    private void displayNotification(Bundle bundle, Promise promise) {
        DisplayNotificationTask displayNotificationTask = new DisplayNotificationTask(this.context, this.reactContext, this.notificationManager, bundle, promise);
        displayNotificationTask.execute(new Void[0]);
    }

    private NotificationChannelGroup parseChannelGroupMap(ReadableMap readableMap) {
        if (VERSION.SDK_INT < 26) {
            return null;
        }
        NotificationChannelGroup notificationChannelGroup = new NotificationChannelGroup(readableMap.getString("groupId"), readableMap.getString(ConditionalUserProperty.NAME));
        if (VERSION.SDK_INT >= 28 && readableMap.hasKey("description")) {
            notificationChannelGroup.setDescription(readableMap.getString("description"));
        }
        return notificationChannelGroup;
    }

    private String getFileName(Uri uri) {
        String str = null;
        if (uri.getScheme() == "content") {
            Cursor query = this.reactContext.getContentResolver().query(uri, null, null, null, null);
            if (query != null) {
                try {
                    if (query.moveToFirst()) {
                        str = query.getString(query.getColumnIndexOrThrow("_display_name"));
                    }
                } catch (Throwable th) {
                    if (query != null) {
                        query.close();
                    }
                    throw th;
                }
            }
            if (query != null) {
                query.close();
            }
        }
        if (str == null) {
            str = uri.getPath();
            if (str != null) {
                int lastIndexOf = str.lastIndexOf(47);
                str = lastIndexOf != -1 ? str.substring(lastIndexOf + 1) : "default";
            }
        }
        return str.equals("notification_sound") ? "default" : str;
    }

    @RequiresApi(api = 26)
    private WritableArray createChannelsArray(List<NotificationChannel> list) {
        WritableArray createArray = Arguments.createArray();
        if (VERSION.SDK_INT >= 26) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                createArray.pushMap(createChannelMap((NotificationChannel) list.get(i)));
            }
        }
        return createArray;
    }

    @RequiresApi(api = 26)
    private WritableArray createChannelGroupsArray(List<NotificationChannelGroup> list) {
        WritableArray createArray = Arguments.createArray();
        if (VERSION.SDK_INT >= 26) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                createArray.pushMap(createChannelGroupMap((NotificationChannelGroup) list.get(i)));
            }
        }
        return createArray;
    }

    @RequiresApi(api = 26)
    private WritableMap createChannelGroupMap(NotificationChannelGroup notificationChannelGroup) {
        WritableMap createMap = Arguments.createMap();
        if (VERSION.SDK_INT >= 26) {
            createMap.putString("groupId", notificationChannelGroup.getId());
            createMap.putString(ConditionalUserProperty.NAME, notificationChannelGroup.getName().toString());
            createMap.putArray("channels", createChannelsArray(notificationChannelGroup.getChannels()));
            if (VERSION.SDK_INT >= 28) {
                createMap.putString("description", notificationChannelGroup.getDescription());
            }
        }
        return createMap;
    }

    @RequiresApi(api = 26)
    private WritableMap createChannelMap(NotificationChannel notificationChannel) {
        if (notificationChannel == null) {
            return null;
        }
        WritableMap createMap = Arguments.createMap();
        if (VERSION.SDK_INT >= 26) {
            createMap.putString("channelId", notificationChannel.getId());
            createMap.putString(ConditionalUserProperty.NAME, notificationChannel.getName().toString());
            createMap.putInt("importance", notificationChannel.getImportance());
            createMap.putString("description", notificationChannel.getDescription());
            createMap.putBoolean("bypassDnd", notificationChannel.canBypassDnd());
            createMap.putString("group", notificationChannel.getGroup());
            createMap.putString("lightColor", String.format("#%06X", new Object[]{Integer.valueOf(16777215 & notificationChannel.getLightColor())}));
            createMap.putBoolean("lightsEnabled", notificationChannel.shouldShowLights());
            int lockscreenVisibility = notificationChannel.getLockscreenVisibility();
            if (lockscreenVisibility == -1000) {
                createMap.putNull("lockScreenVisibility");
            } else {
                createMap.putInt("lockScreenVisibility", lockscreenVisibility);
            }
            createMap.putBoolean("showBadge", notificationChannel.canShowBadge());
            createMap.putString("sound", getFileName(notificationChannel.getSound()));
            createMap.putBoolean("vibrationEnabled", notificationChannel.shouldVibrate());
            long[] vibrationPattern = notificationChannel.getVibrationPattern();
            WritableArray createArray = Arguments.createArray();
            if (vibrationPattern != null) {
                for (long j : vibrationPattern) {
                    createArray.pushDouble((double) j);
                }
            }
            createMap.putArray("vibrationPattern", createArray);
        }
        return createMap;
    }

    @RequiresApi(api = 26)
    private NotificationChannel parseChannelMap(ReadableMap readableMap) {
        if (VERSION.SDK_INT < 26) {
            return null;
        }
        NotificationChannel notificationChannel = new NotificationChannel(readableMap.getString("channelId"), readableMap.getString(ConditionalUserProperty.NAME), readableMap.getInt("importance"));
        if (readableMap.hasKey("bypassDnd")) {
            notificationChannel.setBypassDnd(readableMap.getBoolean("bypassDnd"));
        }
        if (readableMap.hasKey("description")) {
            notificationChannel.setDescription(readableMap.getString("description"));
        }
        if (readableMap.hasKey("group")) {
            notificationChannel.setGroup(readableMap.getString("group"));
        }
        if (readableMap.hasKey("lightColor")) {
            notificationChannel.setLightColor(Color.parseColor(readableMap.getString("lightColor")));
        }
        if (readableMap.hasKey("lightsEnabled")) {
            notificationChannel.enableLights(readableMap.getBoolean("lightsEnabled"));
        }
        if (readableMap.hasKey("lockScreenVisibility")) {
            notificationChannel.setLockscreenVisibility(readableMap.getInt("lockScreenVisibility"));
        }
        if (readableMap.hasKey("showBadge")) {
            notificationChannel.setShowBadge(readableMap.getBoolean("showBadge"));
        }
        if (readableMap.hasKey("sound")) {
            notificationChannel.setSound(getSound(this.context, readableMap.getString("sound")), null);
        }
        if (readableMap.hasKey("vibrationEnabled")) {
            notificationChannel.enableVibration(readableMap.getBoolean("vibrationEnabled"));
        }
        if (readableMap.hasKey("vibrationPattern")) {
            ReadableArray array = readableMap.getArray("vibrationPattern");
            long[] jArr = new long[array.size()];
            for (int i = 0; i < array.size(); i++) {
                jArr[i] = (long) array.getDouble(i);
            }
            notificationChannel.setVibrationPattern(jArr);
        }
        return notificationChannel;
    }

    /* JADX WARNING: Removed duplicated region for block: B:50:0x0137  */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x0151  */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x0163  */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x0170  */
    @android.annotation.SuppressLint({"ShortAlarm"})
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void scheduleNotification(android.os.Bundle r19, @javax.annotation.Nullable com.facebook.react.bridge.Promise r20) {
        /*
            r18 = this;
            r1 = r18
            r0 = r19
            r2 = r20
            java.lang.String r3 = "notificationId"
            boolean r3 = r0.containsKey(r3)
            if (r3 != 0) goto L_0x0020
            if (r2 != 0) goto L_0x0018
            java.lang.String r0 = "RNFNotificationManager"
            java.lang.String r2 = "Missing notificationId"
            android.util.Log.e(r0, r2)
            goto L_0x001f
        L_0x0018:
            java.lang.String r0 = "notification/schedule_notification_error"
            java.lang.String r3 = "Missing notificationId"
            r2.reject(r0, r3)
        L_0x001f:
            return
        L_0x0020:
            java.lang.String r3 = "schedule"
            boolean r3 = r0.containsKey(r3)
            if (r3 != 0) goto L_0x003a
            if (r2 != 0) goto L_0x0032
            java.lang.String r0 = "RNFNotificationManager"
            java.lang.String r2 = "Missing schedule information"
            android.util.Log.e(r0, r2)
            goto L_0x0039
        L_0x0032:
            java.lang.String r0 = "notification/schedule_notification_error"
            java.lang.String r3 = "Missing schedule information"
            r2.reject(r0, r3)
        L_0x0039:
            return
        L_0x003a:
            java.lang.String r3 = "notificationId"
            java.lang.String r3 = r0.getString(r3)
            java.lang.String r4 = "schedule"
            android.os.Bundle r4 = r0.getBundle(r4)
            r5 = -1
            java.lang.Long r7 = java.lang.Long.valueOf(r5)
            java.lang.String r8 = "fireDate"
            java.lang.Object r8 = r4.get(r8)
            boolean r9 = r8 instanceof java.lang.Long
            if (r9 == 0) goto L_0x005a
            r7 = r8
            java.lang.Long r7 = (java.lang.Long) r7
            goto L_0x0068
        L_0x005a:
            boolean r9 = r8 instanceof java.lang.Double
            if (r9 == 0) goto L_0x0068
            java.lang.Double r8 = (java.lang.Double) r8
            long r7 = r8.longValue()
            java.lang.Long r7 = java.lang.Long.valueOf(r7)
        L_0x0068:
            long r8 = r7.longValue()
            int r10 = (r8 > r5 ? 1 : (r8 == r5 ? 0 : -1))
            if (r10 != 0) goto L_0x0082
            if (r2 != 0) goto L_0x007a
            java.lang.String r0 = "RNFNotificationManager"
            java.lang.String r2 = "Missing schedule information"
            android.util.Log.e(r0, r2)
            goto L_0x0081
        L_0x007a:
            java.lang.String r0 = "notification/schedule_notification_error"
            java.lang.String r3 = "Missing fireDate information"
            r2.reject(r0, r3)
        L_0x0081:
            return
        L_0x0082:
            org.json.JSONObject r5 = p007io.invertase.firebase.messaging.BundleJSONConverter.convertToJSON(r19)     // Catch:{ JSONException -> 0x024c }
            android.content.SharedPreferences r6 = r1.preferences     // Catch:{ JSONException -> 0x024c }
            android.content.SharedPreferences$Editor r6 = r6.edit()     // Catch:{ JSONException -> 0x024c }
            java.lang.String r5 = r5.toString()     // Catch:{ JSONException -> 0x024c }
            android.content.SharedPreferences$Editor r5 = r6.putString(r3, r5)     // Catch:{ JSONException -> 0x024c }
            r5.apply()     // Catch:{ JSONException -> 0x024c }
            android.content.Intent r5 = new android.content.Intent
            android.content.Context r6 = r1.context
            java.lang.Class<io.invertase.firebase.notifications.RNFirebaseNotificationReceiver> r8 = p007io.invertase.firebase.notifications.RNFirebaseNotificationReceiver.class
            r5.<init>(r6, r8)
            r5.putExtras(r0)
            android.content.Context r0 = r1.context
            int r3 = r3.hashCode()
            r6 = 134217728(0x8000000, float:3.85186E-34)
            android.app.PendingIntent r14 = android.app.PendingIntent.getBroadcast(r0, r3, r5, r6)
            java.lang.String r0 = "repeatInterval"
            boolean r0 = r4.containsKey(r0)
            r5 = 0
            if (r0 == 0) goto L_0x021c
            long r8 = r7.longValue()
            long r10 = java.lang.System.currentTimeMillis()
            r12 = 3645428(0x379ff4, float:5.108333E-39)
            r13 = 3208676(0x30f5e4, float:4.496313E-39)
            r15 = 99228(0x1839c, float:1.39048E-40)
            r0 = -1074026988(0xffffffffbffba614, float:-1.9660058)
            r16 = -1
            r6 = 1
            int r17 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r17 >= 0) goto L_0x017b
            java.lang.String r8 = "RNFNotificationManager"
            java.lang.String r9 = "Scheduled notification date is in the past, will adjust it to be in future"
            android.util.Log.w(r8, r9)
            java.util.Calendar r8 = java.util.Calendar.getInstance()
            java.util.Calendar r9 = java.util.Calendar.getInstance()
            long r10 = r7.longValue()
            r9.setTimeInMillis(r10)
            r7 = 13
            int r10 = r9.get(r7)
            r8.set(r7, r10)
            java.lang.String r7 = "repeatInterval"
            java.lang.String r7 = r4.getString(r7)
            int r10 = r7.hashCode()
            if (r10 == r0) goto L_0x0123
            if (r10 == r15) goto L_0x0119
            if (r10 == r13) goto L_0x010f
            if (r10 == r12) goto L_0x0105
            goto L_0x012d
        L_0x0105:
            java.lang.String r10 = "week"
            boolean r7 = r7.equals(r10)
            if (r7 == 0) goto L_0x012d
            r7 = 3
            goto L_0x012e
        L_0x010f:
            java.lang.String r10 = "hour"
            boolean r7 = r7.equals(r10)
            if (r7 == 0) goto L_0x012d
            r7 = 1
            goto L_0x012e
        L_0x0119:
            java.lang.String r10 = "day"
            boolean r7 = r7.equals(r10)
            if (r7 == 0) goto L_0x012d
            r7 = 2
            goto L_0x012e
        L_0x0123:
            java.lang.String r10 = "minute"
            boolean r7 = r7.equals(r10)
            if (r7 == 0) goto L_0x012d
            r7 = 0
            goto L_0x012e
        L_0x012d:
            r7 = -1
        L_0x012e:
            r10 = 5
            r11 = 11
            r3 = 12
            switch(r7) {
                case 0: goto L_0x0170;
                case 1: goto L_0x0163;
                case 2: goto L_0x0151;
                case 3: goto L_0x0137;
                default: goto L_0x0136;
            }
        L_0x0136:
            goto L_0x0173
        L_0x0137:
            int r7 = r9.get(r3)
            r8.set(r3, r7)
            int r3 = r9.get(r11)
            r8.set(r11, r3)
            int r3 = r9.get(r10)
            r8.set(r10, r3)
            r3 = 7
            r8.add(r10, r3)
            goto L_0x0173
        L_0x0151:
            int r7 = r9.get(r3)
            r8.set(r3, r7)
            int r3 = r9.get(r11)
            r8.set(r11, r3)
            r8.add(r10, r6)
            goto L_0x0173
        L_0x0163:
            int r7 = r9.get(r3)
            r8.set(r3, r7)
            r3 = 10
            r8.add(r3, r6)
            goto L_0x0173
        L_0x0170:
            r8.add(r3, r6)
        L_0x0173:
            long r7 = r8.getTimeInMillis()
            java.lang.Long r7 = java.lang.Long.valueOf(r7)
        L_0x017b:
            java.lang.String r3 = "repeatInterval"
            java.lang.String r3 = r4.getString(r3)
            int r8 = r3.hashCode()
            if (r8 == r0) goto L_0x01af
            if (r8 == r15) goto L_0x01a4
            if (r8 == r13) goto L_0x0199
            if (r8 == r12) goto L_0x018e
            goto L_0x01b9
        L_0x018e:
            java.lang.String r0 = "week"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x01b9
            r16 = 3
            goto L_0x01b9
        L_0x0199:
            java.lang.String r0 = "hour"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x01b9
            r16 = 1
            goto L_0x01b9
        L_0x01a4:
            java.lang.String r0 = "day"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x01b9
            r16 = 2
            goto L_0x01b9
        L_0x01af:
            java.lang.String r0 = "minute"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x01b9
            r16 = 0
        L_0x01b9:
            switch(r16) {
                case 0: goto L_0x01f2;
                case 1: goto L_0x01ea;
                case 2: goto L_0x01e2;
                case 3: goto L_0x01da;
                default: goto L_0x01bc;
            }
        L_0x01bc:
            java.lang.String r0 = "RNFNotificationManager"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r5 = "Invalid interval: "
            r3.append(r5)
            java.lang.String r5 = "interval"
            java.lang.String r4 = r4.getString(r5)
            r3.append(r4)
            java.lang.String r3 = r3.toString()
            android.util.Log.e(r0, r3)
            r3 = 0
            goto L_0x01f9
        L_0x01da:
            r3 = 604800000(0x240c8400, double:2.988109026E-315)
            java.lang.Long r3 = java.lang.Long.valueOf(r3)
            goto L_0x01f9
        L_0x01e2:
            r3 = 86400000(0x5265c00, double:4.2687272E-316)
            java.lang.Long r3 = java.lang.Long.valueOf(r3)
            goto L_0x01f9
        L_0x01ea:
            r3 = 3600000(0x36ee80, double:1.7786363E-317)
            java.lang.Long r3 = java.lang.Long.valueOf(r3)
            goto L_0x01f9
        L_0x01f2:
            r3 = 60000(0xea60, double:2.9644E-319)
            java.lang.Long r3 = java.lang.Long.valueOf(r3)
        L_0x01f9:
            if (r3 != 0) goto L_0x020d
            if (r2 != 0) goto L_0x0205
            java.lang.String r0 = "RNFNotificationManager"
            java.lang.String r2 = "Invalid interval"
            android.util.Log.e(r0, r2)
            goto L_0x020c
        L_0x0205:
            java.lang.String r0 = "notification/schedule_notification_error"
            java.lang.String r3 = "Invalid interval"
            r2.reject(r0, r3)
        L_0x020c:
            return
        L_0x020d:
            android.app.AlarmManager r8 = r1.alarmManager
            r9 = 0
            long r10 = r7.longValue()
            long r12 = r3.longValue()
            r8.setRepeating(r9, r10, r12, r14)
            goto L_0x0245
        L_0x021c:
            java.lang.String r0 = "exact"
            boolean r0 = r4.containsKey(r0)
            if (r0 == 0) goto L_0x023c
            java.lang.String r0 = "exact"
            boolean r0 = r4.getBoolean(r0)
            if (r0 == 0) goto L_0x023c
            int r0 = android.os.Build.VERSION.SDK_INT
            r3 = 19
            if (r0 < r3) goto L_0x023c
            android.app.AlarmManager r0 = r1.alarmManager
            long r3 = r7.longValue()
            r0.setExact(r5, r3, r14)
            goto L_0x0245
        L_0x023c:
            android.app.AlarmManager r0 = r1.alarmManager
            long r3 = r7.longValue()
            r0.set(r5, r3, r14)
        L_0x0245:
            if (r2 == 0) goto L_0x024b
            r0 = 0
            r2.resolve(r0)
        L_0x024b:
            return
        L_0x024c:
            r0 = move-exception
            if (r2 != 0) goto L_0x0257
            java.lang.String r0 = "RNFNotificationManager"
            java.lang.String r2 = "Failed to store notification"
            android.util.Log.e(r0, r2)
            goto L_0x025e
        L_0x0257:
            java.lang.String r3 = "notification/schedule_notification_error"
            java.lang.String r4 = "Failed to store notification"
            r2.reject(r3, r4, r0)
        L_0x025e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: p007io.invertase.firebase.notifications.RNFirebaseNotificationManager.scheduleNotification(android.os.Bundle, com.facebook.react.bridge.Promise):void");
    }
}

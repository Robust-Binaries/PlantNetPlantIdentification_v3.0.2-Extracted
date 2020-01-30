package p007io.invertase.firebase.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.p000v4.app.NotificationCompat.Action;
import android.support.p000v4.app.NotificationCompat.Action.Builder;
import android.support.p000v4.app.RemoteInput;
import android.util.Log;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/* renamed from: io.invertase.firebase.notifications.DisplayNotificationTask */
public class DisplayNotificationTask extends AsyncTask<Void, Void, Void> {
    private static final String TAG = "DisplayNotificationTask";
    private final WeakReference<Context> contextWeakReference;
    private final Bundle notification;
    private final NotificationManager notificationManager;
    private final Promise promise;
    private final WeakReference<ReactApplicationContext> reactContextWeakReference;

    DisplayNotificationTask(Context context, ReactApplicationContext reactApplicationContext, NotificationManager notificationManager2, Bundle bundle, Promise promise2) {
        this.contextWeakReference = new WeakReference<>(context);
        this.reactContextWeakReference = new WeakReference<>(reactApplicationContext);
        this.promise = promise2;
        this.notification = bundle;
        this.notificationManager = notificationManager2;
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(Void voidR) {
        this.contextWeakReference.clear();
        this.reactContextWeakReference.clear();
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Can't wrap try/catch for region: R(97:10|11|12|13|14|15|16|(1:18)|19|(1:21)|22|(1:24)|25|(1:27)|28|(1:30)|31|(1:33)|34|(3:38|39|40)|41|43|44|(9:46|(1:48)|49|(2:51|(1:53))|54|(1:56)|57|(1:59)|60)|61|(5:63|(1:65)|66|(1:68)|69)|70|(1:72)|73|(1:75)|76|(3:80|81|82)|83|85|86|(1:88)|89|(3:91|(2:93|(3:95|(2:98|96)|222))|99)|100|(1:102)|103|(3:107|108|109)|110|112|113|(1:115)|116|(2:118|(1:120))|121|(1:123)|124|(1:126)|127|(1:129)|130|(1:132)|133|(1:135)|136|(2:138|(3:140|(2:143|141)|223))|144|(1:146)|147|(1:149)|150|(1:152)|153|(3:157|158|159)|160|162|163|(1:165)|166|(2:168|(2:170|(1:172)(1:173)))|174|(1:176)|177|(1:179)|180|(1:182)|183|(1:185)|186|(2:188|(4:190|(2:193|191)|224|194))|195|(1:197)|198|(1:200)|201|(3:203|(2:206|204)|225)|207|(1:209)(1:210)|211|(1:213)|214|(1:216)|221) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:14:0x003c */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Void doInBackground(java.lang.Void... r12) {
        /*
            r11 = this;
            java.lang.ref.WeakReference<android.content.Context> r12 = r11.contextWeakReference
            java.lang.Object r12 = r12.get()
            android.content.Context r12 = (android.content.Context) r12
            r0 = 0
            if (r12 != 0) goto L_0x000c
            return r0
        L_0x000c:
            java.lang.Class r1 = r11.getMainActivityClass(r12)     // Catch:{ Exception -> 0x0513 }
            if (r1 != 0) goto L_0x0020
            com.facebook.react.bridge.Promise r12 = r11.promise     // Catch:{ Exception -> 0x0513 }
            if (r12 == 0) goto L_0x001f
            com.facebook.react.bridge.Promise r12 = r11.promise     // Catch:{ Exception -> 0x0513 }
            java.lang.String r1 = "notification/display_notification_error"
            java.lang.String r2 = "Could not find main activity class"
            r12.reject(r1, r2)     // Catch:{ Exception -> 0x0513 }
        L_0x001f:
            return r0
        L_0x0020:
            android.os.Bundle r2 = r11.notification     // Catch:{ Exception -> 0x0513 }
            java.lang.String r3 = "android"
            android.os.Bundle r2 = r2.getBundle(r3)     // Catch:{ Exception -> 0x0513 }
            android.os.Bundle r3 = r11.notification     // Catch:{ Exception -> 0x0513 }
            java.lang.String r4 = "notificationId"
            java.lang.String r3 = r3.getString(r4)     // Catch:{ Exception -> 0x0513 }
            java.lang.String r4 = "channelId"
            java.lang.String r4 = r2.getString(r4)     // Catch:{ Throwable -> 0x003c }
            android.support.v4.app.NotificationCompat$Builder r5 = new android.support.v4.app.NotificationCompat$Builder     // Catch:{ Throwable -> 0x003c }
            r5.<init>(r12, r4)     // Catch:{ Throwable -> 0x003c }
            goto L_0x0041
        L_0x003c:
            android.support.v4.app.NotificationCompat$Builder r5 = new android.support.v4.app.NotificationCompat$Builder     // Catch:{ Exception -> 0x0513 }
            r5.<init>(r12)     // Catch:{ Exception -> 0x0513 }
        L_0x0041:
            android.os.Bundle r4 = r11.notification     // Catch:{ Exception -> 0x0513 }
            java.lang.String r6 = "body"
            boolean r4 = r4.containsKey(r6)     // Catch:{ Exception -> 0x0513 }
            if (r4 == 0) goto L_0x0057
            android.os.Bundle r4 = r11.notification     // Catch:{ Exception -> 0x0513 }
            java.lang.String r6 = "body"
            java.lang.String r4 = r4.getString(r6)     // Catch:{ Exception -> 0x0513 }
            android.support.v4.app.NotificationCompat$Builder r5 = r5.setContentText(r4)     // Catch:{ Exception -> 0x0513 }
        L_0x0057:
            android.os.Bundle r4 = r11.notification     // Catch:{ Exception -> 0x0513 }
            java.lang.String r6 = "data"
            boolean r4 = r4.containsKey(r6)     // Catch:{ Exception -> 0x0513 }
            if (r4 == 0) goto L_0x006d
            android.os.Bundle r4 = r11.notification     // Catch:{ Exception -> 0x0513 }
            java.lang.String r6 = "data"
            android.os.Bundle r4 = r4.getBundle(r6)     // Catch:{ Exception -> 0x0513 }
            android.support.v4.app.NotificationCompat$Builder r5 = r5.setExtras(r4)     // Catch:{ Exception -> 0x0513 }
        L_0x006d:
            android.os.Bundle r4 = r11.notification     // Catch:{ Exception -> 0x0513 }
            java.lang.String r6 = "sound"
            boolean r4 = r4.containsKey(r6)     // Catch:{ Exception -> 0x0513 }
            if (r4 == 0) goto L_0x0087
            android.os.Bundle r4 = r11.notification     // Catch:{ Exception -> 0x0513 }
            java.lang.String r6 = "sound"
            java.lang.String r4 = r4.getString(r6)     // Catch:{ Exception -> 0x0513 }
            android.net.Uri r4 = p007io.invertase.firebase.notifications.RNFirebaseNotificationManager.getSound(r12, r4)     // Catch:{ Exception -> 0x0513 }
            android.support.v4.app.NotificationCompat$Builder r5 = r5.setSound(r4)     // Catch:{ Exception -> 0x0513 }
        L_0x0087:
            android.os.Bundle r4 = r11.notification     // Catch:{ Exception -> 0x0513 }
            java.lang.String r6 = "subtitle"
            boolean r4 = r4.containsKey(r6)     // Catch:{ Exception -> 0x0513 }
            if (r4 == 0) goto L_0x009d
            android.os.Bundle r4 = r11.notification     // Catch:{ Exception -> 0x0513 }
            java.lang.String r6 = "subtitle"
            java.lang.String r4 = r4.getString(r6)     // Catch:{ Exception -> 0x0513 }
            android.support.v4.app.NotificationCompat$Builder r5 = r5.setSubText(r4)     // Catch:{ Exception -> 0x0513 }
        L_0x009d:
            android.os.Bundle r4 = r11.notification     // Catch:{ Exception -> 0x0513 }
            java.lang.String r6 = "title"
            boolean r4 = r4.containsKey(r6)     // Catch:{ Exception -> 0x0513 }
            if (r4 == 0) goto L_0x00b3
            android.os.Bundle r4 = r11.notification     // Catch:{ Exception -> 0x0513 }
            java.lang.String r6 = "title"
            java.lang.String r4 = r4.getString(r6)     // Catch:{ Exception -> 0x0513 }
            android.support.v4.app.NotificationCompat$Builder r5 = r5.setContentTitle(r4)     // Catch:{ Exception -> 0x0513 }
        L_0x00b3:
            java.lang.String r4 = "autoCancel"
            boolean r4 = r2.containsKey(r4)     // Catch:{ Exception -> 0x0513 }
            if (r4 == 0) goto L_0x00c5
            java.lang.String r4 = "autoCancel"
            boolean r4 = r2.getBoolean(r4)     // Catch:{ Exception -> 0x0513 }
            android.support.v4.app.NotificationCompat$Builder r5 = r5.setAutoCancel(r4)     // Catch:{ Exception -> 0x0513 }
        L_0x00c5:
            java.lang.String r4 = "badgeIconType"
            boolean r4 = r2.containsKey(r4)     // Catch:{ Exception -> 0x0513 }
            r6 = 26
            if (r4 == 0) goto L_0x00e5
            int r4 = android.os.Build.VERSION.SDK_INT     // Catch:{ Exception -> 0x0513 }
            if (r4 < r6) goto L_0x00e5
            java.lang.String r4 = "badgeIconType"
            double r7 = r2.getDouble(r4)     // Catch:{ Exception -> 0x0513 }
            java.lang.Double r4 = java.lang.Double.valueOf(r7)     // Catch:{ Exception -> 0x0513 }
            int r4 = r4.intValue()     // Catch:{ Throwable -> 0x00e5 }
            android.support.v4.app.NotificationCompat$Builder r5 = r5.setBadgeIconType(r4)     // Catch:{ Throwable -> 0x00e5 }
        L_0x00e5:
            java.lang.String r4 = "bigPicture"
            boolean r4 = r2.containsKey(r4)     // Catch:{ Exception -> 0x0513 }
            if (r4 == 0) goto L_0x0148
            java.lang.String r4 = "bigPicture"
            android.os.Bundle r4 = r2.getBundle(r4)     // Catch:{ Exception -> 0x0513 }
            android.support.v4.app.NotificationCompat$BigPictureStyle r7 = new android.support.v4.app.NotificationCompat$BigPictureStyle     // Catch:{ Exception -> 0x0513 }
            r7.<init>()     // Catch:{ Exception -> 0x0513 }
            java.lang.String r8 = "picture"
            java.lang.String r8 = r4.getString(r8)     // Catch:{ Exception -> 0x0513 }
            android.graphics.Bitmap r8 = r11.getBitmap(r8)     // Catch:{ Exception -> 0x0513 }
            if (r8 == 0) goto L_0x0108
            android.support.v4.app.NotificationCompat$BigPictureStyle r7 = r7.bigPicture(r8)     // Catch:{ Exception -> 0x0513 }
        L_0x0108:
            java.lang.String r8 = "largeIcon"
            boolean r8 = r4.containsKey(r8)     // Catch:{ Exception -> 0x0513 }
            if (r8 == 0) goto L_0x0120
            java.lang.String r8 = "largeIcon"
            java.lang.String r8 = r4.getString(r8)     // Catch:{ Exception -> 0x0513 }
            android.graphics.Bitmap r8 = r11.getBitmap(r8)     // Catch:{ Exception -> 0x0513 }
            if (r8 == 0) goto L_0x0120
            android.support.v4.app.NotificationCompat$BigPictureStyle r7 = r7.bigLargeIcon(r8)     // Catch:{ Exception -> 0x0513 }
        L_0x0120:
            java.lang.String r8 = "contentTitle"
            boolean r8 = r4.containsKey(r8)     // Catch:{ Exception -> 0x0513 }
            if (r8 == 0) goto L_0x0132
            java.lang.String r8 = "contentTitle"
            java.lang.String r8 = r4.getString(r8)     // Catch:{ Exception -> 0x0513 }
            android.support.v4.app.NotificationCompat$BigPictureStyle r7 = r7.setBigContentTitle(r8)     // Catch:{ Exception -> 0x0513 }
        L_0x0132:
            java.lang.String r8 = "summaryText"
            boolean r8 = r4.containsKey(r8)     // Catch:{ Exception -> 0x0513 }
            if (r8 == 0) goto L_0x0144
            java.lang.String r8 = "summaryText"
            java.lang.String r4 = r4.getString(r8)     // Catch:{ Exception -> 0x0513 }
            android.support.v4.app.NotificationCompat$BigPictureStyle r7 = r7.setSummaryText(r4)     // Catch:{ Exception -> 0x0513 }
        L_0x0144:
            android.support.v4.app.NotificationCompat$Builder r5 = r5.setStyle(r7)     // Catch:{ Exception -> 0x0513 }
        L_0x0148:
            java.lang.String r4 = "bigText"
            boolean r4 = r2.containsKey(r4)     // Catch:{ Exception -> 0x0513 }
            if (r4 == 0) goto L_0x018c
            java.lang.String r4 = "bigText"
            android.os.Bundle r4 = r2.getBundle(r4)     // Catch:{ Exception -> 0x0513 }
            android.support.v4.app.NotificationCompat$BigTextStyle r7 = new android.support.v4.app.NotificationCompat$BigTextStyle     // Catch:{ Exception -> 0x0513 }
            r7.<init>()     // Catch:{ Exception -> 0x0513 }
            java.lang.String r8 = "text"
            java.lang.String r8 = r4.getString(r8)     // Catch:{ Exception -> 0x0513 }
            r7.bigText(r8)     // Catch:{ Exception -> 0x0513 }
            java.lang.String r8 = "contentTitle"
            boolean r8 = r4.containsKey(r8)     // Catch:{ Exception -> 0x0513 }
            if (r8 == 0) goto L_0x0176
            java.lang.String r8 = "contentTitle"
            java.lang.String r8 = r4.getString(r8)     // Catch:{ Exception -> 0x0513 }
            android.support.v4.app.NotificationCompat$BigTextStyle r7 = r7.setBigContentTitle(r8)     // Catch:{ Exception -> 0x0513 }
        L_0x0176:
            java.lang.String r8 = "summaryText"
            boolean r8 = r4.containsKey(r8)     // Catch:{ Exception -> 0x0513 }
            if (r8 == 0) goto L_0x0188
            java.lang.String r8 = "summaryText"
            java.lang.String r4 = r4.getString(r8)     // Catch:{ Exception -> 0x0513 }
            android.support.v4.app.NotificationCompat$BigTextStyle r7 = r7.setSummaryText(r4)     // Catch:{ Exception -> 0x0513 }
        L_0x0188:
            android.support.v4.app.NotificationCompat$Builder r5 = r5.setStyle(r7)     // Catch:{ Exception -> 0x0513 }
        L_0x018c:
            java.lang.String r4 = "category"
            boolean r4 = r2.containsKey(r4)     // Catch:{ Exception -> 0x0513 }
            if (r4 == 0) goto L_0x019e
            java.lang.String r4 = "category"
            java.lang.String r4 = r2.getString(r4)     // Catch:{ Exception -> 0x0513 }
            android.support.v4.app.NotificationCompat$Builder r5 = r5.setCategory(r4)     // Catch:{ Exception -> 0x0513 }
        L_0x019e:
            java.lang.String r4 = "color"
            boolean r4 = r2.containsKey(r4)     // Catch:{ Exception -> 0x0513 }
            if (r4 == 0) goto L_0x01b4
            java.lang.String r4 = "color"
            java.lang.String r4 = r2.getString(r4)     // Catch:{ Exception -> 0x0513 }
            int r4 = android.graphics.Color.parseColor(r4)     // Catch:{ Exception -> 0x0513 }
            android.support.v4.app.NotificationCompat$Builder r5 = r5.setColor(r4)     // Catch:{ Exception -> 0x0513 }
        L_0x01b4:
            java.lang.String r4 = "colorized"
            boolean r4 = r2.containsKey(r4)     // Catch:{ Exception -> 0x0513 }
            if (r4 == 0) goto L_0x01ca
            int r4 = android.os.Build.VERSION.SDK_INT     // Catch:{ Exception -> 0x0513 }
            if (r4 < r6) goto L_0x01ca
            java.lang.String r4 = "colorized"
            boolean r4 = r2.getBoolean(r4)     // Catch:{ Throwable -> 0x01ca }
            android.support.v4.app.NotificationCompat$Builder r5 = r5.setColorized(r4)     // Catch:{ Throwable -> 0x01ca }
        L_0x01ca:
            java.lang.String r4 = "contentInfo"
            boolean r4 = r2.containsKey(r4)     // Catch:{ Exception -> 0x0513 }
            if (r4 == 0) goto L_0x01dc
            java.lang.String r4 = "contentInfo"
            java.lang.String r4 = r2.getString(r4)     // Catch:{ Exception -> 0x0513 }
            android.support.v4.app.NotificationCompat$Builder r5 = r5.setContentInfo(r4)     // Catch:{ Exception -> 0x0513 }
        L_0x01dc:
            java.lang.String r4 = "defaults"
            boolean r4 = r2.containsKey(r4)     // Catch:{ Exception -> 0x0513 }
            if (r4 == 0) goto L_0x0216
            java.lang.String r4 = "defaults"
            double r7 = r2.getDouble(r4)     // Catch:{ Exception -> 0x0513 }
            java.lang.Double r4 = java.lang.Double.valueOf(r7)     // Catch:{ Exception -> 0x0513 }
            int r4 = r4.intValue()     // Catch:{ Exception -> 0x0513 }
            if (r4 != 0) goto L_0x0212
            java.lang.String r7 = "defaults"
            java.util.ArrayList r7 = r2.getIntegerArrayList(r7)     // Catch:{ Exception -> 0x0513 }
            if (r7 == 0) goto L_0x0212
            java.util.Iterator r7 = r7.iterator()     // Catch:{ Exception -> 0x0513 }
        L_0x0200:
            boolean r8 = r7.hasNext()     // Catch:{ Exception -> 0x0513 }
            if (r8 == 0) goto L_0x0212
            java.lang.Object r8 = r7.next()     // Catch:{ Exception -> 0x0513 }
            java.lang.Integer r8 = (java.lang.Integer) r8     // Catch:{ Exception -> 0x0513 }
            int r8 = r8.intValue()     // Catch:{ Exception -> 0x0513 }
            r4 = r4 | r8
            goto L_0x0200
        L_0x0212:
            android.support.v4.app.NotificationCompat$Builder r5 = r5.setDefaults(r4)     // Catch:{ Exception -> 0x0513 }
        L_0x0216:
            java.lang.String r4 = "group"
            boolean r4 = r2.containsKey(r4)     // Catch:{ Exception -> 0x0513 }
            if (r4 == 0) goto L_0x0228
            java.lang.String r4 = "group"
            java.lang.String r4 = r2.getString(r4)     // Catch:{ Exception -> 0x0513 }
            android.support.v4.app.NotificationCompat$Builder r5 = r5.setGroup(r4)     // Catch:{ Exception -> 0x0513 }
        L_0x0228:
            java.lang.String r4 = "groupAlertBehaviour"
            boolean r4 = r2.containsKey(r4)     // Catch:{ Exception -> 0x0513 }
            if (r4 == 0) goto L_0x0246
            int r4 = android.os.Build.VERSION.SDK_INT     // Catch:{ Exception -> 0x0513 }
            if (r4 < r6) goto L_0x0246
            java.lang.String r4 = "groupAlertBehaviour"
            double r7 = r2.getDouble(r4)     // Catch:{ Exception -> 0x0513 }
            java.lang.Double r4 = java.lang.Double.valueOf(r7)     // Catch:{ Exception -> 0x0513 }
            int r4 = r4.intValue()     // Catch:{ Throwable -> 0x0246 }
            android.support.v4.app.NotificationCompat$Builder r5 = r5.setGroupAlertBehavior(r4)     // Catch:{ Throwable -> 0x0246 }
        L_0x0246:
            java.lang.String r4 = "groupSummary"
            boolean r4 = r2.containsKey(r4)     // Catch:{ Exception -> 0x0513 }
            if (r4 == 0) goto L_0x0258
            java.lang.String r4 = "groupSummary"
            boolean r4 = r2.getBoolean(r4)     // Catch:{ Exception -> 0x0513 }
            android.support.v4.app.NotificationCompat$Builder r5 = r5.setGroupSummary(r4)     // Catch:{ Exception -> 0x0513 }
        L_0x0258:
            java.lang.String r4 = "largeIcon"
            boolean r4 = r2.containsKey(r4)     // Catch:{ Exception -> 0x0513 }
            if (r4 == 0) goto L_0x0270
            java.lang.String r4 = "largeIcon"
            java.lang.String r4 = r2.getString(r4)     // Catch:{ Exception -> 0x0513 }
            android.graphics.Bitmap r4 = r11.getBitmap(r4)     // Catch:{ Exception -> 0x0513 }
            if (r4 == 0) goto L_0x0270
            android.support.v4.app.NotificationCompat$Builder r5 = r5.setLargeIcon(r4)     // Catch:{ Exception -> 0x0513 }
        L_0x0270:
            java.lang.String r4 = "lights"
            boolean r4 = r2.containsKey(r4)     // Catch:{ Exception -> 0x0513 }
            if (r4 == 0) goto L_0x02ac
            java.lang.String r4 = "lights"
            android.os.Bundle r4 = r2.getBundle(r4)     // Catch:{ Exception -> 0x0513 }
            java.lang.String r7 = "argb"
            double r7 = r4.getDouble(r7)     // Catch:{ Exception -> 0x0513 }
            java.lang.Double r7 = java.lang.Double.valueOf(r7)     // Catch:{ Exception -> 0x0513 }
            java.lang.String r8 = "onMs"
            double r8 = r4.getDouble(r8)     // Catch:{ Exception -> 0x0513 }
            java.lang.Double r8 = java.lang.Double.valueOf(r8)     // Catch:{ Exception -> 0x0513 }
            java.lang.String r9 = "offMs"
            double r9 = r4.getDouble(r9)     // Catch:{ Exception -> 0x0513 }
            java.lang.Double r4 = java.lang.Double.valueOf(r9)     // Catch:{ Exception -> 0x0513 }
            int r7 = r7.intValue()     // Catch:{ Exception -> 0x0513 }
            int r8 = r8.intValue()     // Catch:{ Exception -> 0x0513 }
            int r4 = r4.intValue()     // Catch:{ Exception -> 0x0513 }
            android.support.v4.app.NotificationCompat$Builder r5 = r5.setLights(r7, r8, r4)     // Catch:{ Exception -> 0x0513 }
        L_0x02ac:
            java.lang.String r4 = "localOnly"
            boolean r4 = r2.containsKey(r4)     // Catch:{ Exception -> 0x0513 }
            if (r4 == 0) goto L_0x02be
            java.lang.String r4 = "localOnly"
            boolean r4 = r2.getBoolean(r4)     // Catch:{ Exception -> 0x0513 }
            android.support.v4.app.NotificationCompat$Builder r5 = r5.setLocalOnly(r4)     // Catch:{ Exception -> 0x0513 }
        L_0x02be:
            java.lang.String r4 = "number"
            boolean r4 = r2.containsKey(r4)     // Catch:{ Exception -> 0x0513 }
            if (r4 == 0) goto L_0x02d8
            java.lang.String r4 = "number"
            double r7 = r2.getDouble(r4)     // Catch:{ Exception -> 0x0513 }
            java.lang.Double r4 = java.lang.Double.valueOf(r7)     // Catch:{ Exception -> 0x0513 }
            int r4 = r4.intValue()     // Catch:{ Exception -> 0x0513 }
            android.support.v4.app.NotificationCompat$Builder r5 = r5.setNumber(r4)     // Catch:{ Exception -> 0x0513 }
        L_0x02d8:
            java.lang.String r4 = "ongoing"
            boolean r4 = r2.containsKey(r4)     // Catch:{ Exception -> 0x0513 }
            if (r4 == 0) goto L_0x02ea
            java.lang.String r4 = "ongoing"
            boolean r4 = r2.getBoolean(r4)     // Catch:{ Exception -> 0x0513 }
            android.support.v4.app.NotificationCompat$Builder r5 = r5.setOngoing(r4)     // Catch:{ Exception -> 0x0513 }
        L_0x02ea:
            java.lang.String r4 = "onlyAlertOnce"
            boolean r4 = r2.containsKey(r4)     // Catch:{ Exception -> 0x0513 }
            if (r4 == 0) goto L_0x02fc
            java.lang.String r4 = "onlyAlertOnce"
            boolean r4 = r2.getBoolean(r4)     // Catch:{ Exception -> 0x0513 }
            android.support.v4.app.NotificationCompat$Builder r5 = r5.setOnlyAlertOnce(r4)     // Catch:{ Exception -> 0x0513 }
        L_0x02fc:
            java.lang.String r4 = "people"
            boolean r4 = r2.containsKey(r4)     // Catch:{ Exception -> 0x0513 }
            if (r4 == 0) goto L_0x0321
            java.lang.String r4 = "people"
            java.util.ArrayList r4 = r2.getStringArrayList(r4)     // Catch:{ Exception -> 0x0513 }
            if (r4 == 0) goto L_0x0321
            java.util.Iterator r4 = r4.iterator()     // Catch:{ Exception -> 0x0513 }
        L_0x0310:
            boolean r7 = r4.hasNext()     // Catch:{ Exception -> 0x0513 }
            if (r7 == 0) goto L_0x0321
            java.lang.Object r7 = r4.next()     // Catch:{ Exception -> 0x0513 }
            java.lang.String r7 = (java.lang.String) r7     // Catch:{ Exception -> 0x0513 }
            android.support.v4.app.NotificationCompat$Builder r5 = r5.addPerson(r7)     // Catch:{ Exception -> 0x0513 }
            goto L_0x0310
        L_0x0321:
            java.lang.String r4 = "priority"
            boolean r4 = r2.containsKey(r4)     // Catch:{ Exception -> 0x0513 }
            if (r4 == 0) goto L_0x033b
            java.lang.String r4 = "priority"
            double r7 = r2.getDouble(r4)     // Catch:{ Exception -> 0x0513 }
            java.lang.Double r4 = java.lang.Double.valueOf(r7)     // Catch:{ Exception -> 0x0513 }
            int r4 = r4.intValue()     // Catch:{ Exception -> 0x0513 }
            android.support.v4.app.NotificationCompat$Builder r5 = r5.setPriority(r4)     // Catch:{ Exception -> 0x0513 }
        L_0x033b:
            java.lang.String r4 = "progress"
            boolean r4 = r2.containsKey(r4)     // Catch:{ Exception -> 0x0513 }
            if (r4 == 0) goto L_0x036f
            java.lang.String r4 = "progress"
            android.os.Bundle r4 = r2.getBundle(r4)     // Catch:{ Exception -> 0x0513 }
            java.lang.String r7 = "max"
            double r7 = r4.getDouble(r7)     // Catch:{ Exception -> 0x0513 }
            java.lang.Double r7 = java.lang.Double.valueOf(r7)     // Catch:{ Exception -> 0x0513 }
            java.lang.String r8 = "progress"
            double r8 = r4.getDouble(r8)     // Catch:{ Exception -> 0x0513 }
            java.lang.Double r8 = java.lang.Double.valueOf(r8)     // Catch:{ Exception -> 0x0513 }
            int r7 = r7.intValue()     // Catch:{ Exception -> 0x0513 }
            int r8 = r8.intValue()     // Catch:{ Exception -> 0x0513 }
            java.lang.String r9 = "indeterminate"
            boolean r4 = r4.getBoolean(r9)     // Catch:{ Exception -> 0x0513 }
            android.support.v4.app.NotificationCompat$Builder r5 = r5.setProgress(r7, r8, r4)     // Catch:{ Exception -> 0x0513 }
        L_0x036f:
            java.lang.String r4 = "remoteInputHistory"
            boolean r4 = r2.containsKey(r4)     // Catch:{ Exception -> 0x0513 }
            if (r4 == 0) goto L_0x0381
            java.lang.String r4 = "remoteInputHistory"
            java.lang.String[] r4 = r2.getStringArray(r4)     // Catch:{ Exception -> 0x0513 }
            android.support.v4.app.NotificationCompat$Builder r5 = r5.setRemoteInputHistory(r4)     // Catch:{ Exception -> 0x0513 }
        L_0x0381:
            java.lang.String r4 = "shortcutId"
            boolean r4 = r2.containsKey(r4)     // Catch:{ Exception -> 0x0513 }
            if (r4 == 0) goto L_0x0397
            int r4 = android.os.Build.VERSION.SDK_INT     // Catch:{ Exception -> 0x0513 }
            if (r4 < r6) goto L_0x0397
            java.lang.String r4 = "shortcutId"
            java.lang.String r4 = r2.getString(r4)     // Catch:{ Throwable -> 0x0397 }
            android.support.v4.app.NotificationCompat$Builder r5 = r5.setShortcutId(r4)     // Catch:{ Throwable -> 0x0397 }
        L_0x0397:
            java.lang.String r4 = "showWhen"
            boolean r4 = r2.containsKey(r4)     // Catch:{ Exception -> 0x0513 }
            if (r4 == 0) goto L_0x03a9
            java.lang.String r4 = "showWhen"
            boolean r4 = r2.getBoolean(r4)     // Catch:{ Exception -> 0x0513 }
            android.support.v4.app.NotificationCompat$Builder r5 = r5.setShowWhen(r4)     // Catch:{ Exception -> 0x0513 }
        L_0x03a9:
            java.lang.String r4 = "smallIcon"
            boolean r4 = r2.containsKey(r4)     // Catch:{ Exception -> 0x0513 }
            if (r4 == 0) goto L_0x03e2
            java.lang.String r4 = "smallIcon"
            android.os.Bundle r4 = r2.getBundle(r4)     // Catch:{ Exception -> 0x0513 }
            java.lang.String r6 = "icon"
            java.lang.String r6 = r4.getString(r6)     // Catch:{ Exception -> 0x0513 }
            int r6 = r11.getIcon(r6)     // Catch:{ Exception -> 0x0513 }
            if (r6 == 0) goto L_0x03e2
            java.lang.String r7 = "level"
            boolean r7 = r4.containsKey(r7)     // Catch:{ Exception -> 0x0513 }
            if (r7 == 0) goto L_0x03de
            java.lang.String r7 = "level"
            double r7 = r4.getDouble(r7)     // Catch:{ Exception -> 0x0513 }
            java.lang.Double r4 = java.lang.Double.valueOf(r7)     // Catch:{ Exception -> 0x0513 }
            int r4 = r4.intValue()     // Catch:{ Exception -> 0x0513 }
            android.support.v4.app.NotificationCompat$Builder r5 = r5.setSmallIcon(r6, r4)     // Catch:{ Exception -> 0x0513 }
            goto L_0x03e2
        L_0x03de:
            android.support.v4.app.NotificationCompat$Builder r5 = r5.setSmallIcon(r6)     // Catch:{ Exception -> 0x0513 }
        L_0x03e2:
            java.lang.String r4 = "sortKey"
            boolean r4 = r2.containsKey(r4)     // Catch:{ Exception -> 0x0513 }
            if (r4 == 0) goto L_0x03f4
            java.lang.String r4 = "sortKey"
            java.lang.String r4 = r2.getString(r4)     // Catch:{ Exception -> 0x0513 }
            android.support.v4.app.NotificationCompat$Builder r5 = r5.setSortKey(r4)     // Catch:{ Exception -> 0x0513 }
        L_0x03f4:
            java.lang.String r4 = "ticker"
            boolean r4 = r2.containsKey(r4)     // Catch:{ Exception -> 0x0513 }
            if (r4 == 0) goto L_0x0406
            java.lang.String r4 = "ticker"
            java.lang.String r4 = r2.getString(r4)     // Catch:{ Exception -> 0x0513 }
            android.support.v4.app.NotificationCompat$Builder r5 = r5.setTicker(r4)     // Catch:{ Exception -> 0x0513 }
        L_0x0406:
            java.lang.String r4 = "timeoutAfter"
            boolean r4 = r2.containsKey(r4)     // Catch:{ Exception -> 0x0513 }
            if (r4 == 0) goto L_0x0420
            java.lang.String r4 = "timeoutAfter"
            double r6 = r2.getDouble(r4)     // Catch:{ Exception -> 0x0513 }
            java.lang.Double r4 = java.lang.Double.valueOf(r6)     // Catch:{ Exception -> 0x0513 }
            long r6 = r4.longValue()     // Catch:{ Exception -> 0x0513 }
            android.support.v4.app.NotificationCompat$Builder r5 = r5.setTimeoutAfter(r6)     // Catch:{ Exception -> 0x0513 }
        L_0x0420:
            java.lang.String r4 = "usesChronometer"
            boolean r4 = r2.containsKey(r4)     // Catch:{ Exception -> 0x0513 }
            if (r4 == 0) goto L_0x0432
            java.lang.String r4 = "usesChronometer"
            boolean r4 = r2.getBoolean(r4)     // Catch:{ Exception -> 0x0513 }
            android.support.v4.app.NotificationCompat$Builder r5 = r5.setUsesChronometer(r4)     // Catch:{ Exception -> 0x0513 }
        L_0x0432:
            java.lang.String r4 = "vibrate"
            boolean r4 = r2.containsKey(r4)     // Catch:{ Exception -> 0x0513 }
            if (r4 == 0) goto L_0x0462
            java.lang.String r4 = "vibrate"
            java.util.ArrayList r4 = r2.getIntegerArrayList(r4)     // Catch:{ Exception -> 0x0513 }
            if (r4 == 0) goto L_0x0462
            int r6 = r4.size()     // Catch:{ Exception -> 0x0513 }
            long[] r6 = new long[r6]     // Catch:{ Exception -> 0x0513 }
            r7 = 0
        L_0x0449:
            int r8 = r4.size()     // Catch:{ Exception -> 0x0513 }
            if (r7 >= r8) goto L_0x045e
            java.lang.Object r8 = r4.get(r7)     // Catch:{ Exception -> 0x0513 }
            java.lang.Integer r8 = (java.lang.Integer) r8     // Catch:{ Exception -> 0x0513 }
            long r8 = r8.longValue()     // Catch:{ Exception -> 0x0513 }
            r6[r7] = r8     // Catch:{ Exception -> 0x0513 }
            int r7 = r7 + 1
            goto L_0x0449
        L_0x045e:
            android.support.v4.app.NotificationCompat$Builder r5 = r5.setVibrate(r6)     // Catch:{ Exception -> 0x0513 }
        L_0x0462:
            java.lang.String r4 = "visibility"
            boolean r4 = r2.containsKey(r4)     // Catch:{ Exception -> 0x0513 }
            if (r4 == 0) goto L_0x047c
            java.lang.String r4 = "visibility"
            double r6 = r2.getDouble(r4)     // Catch:{ Exception -> 0x0513 }
            java.lang.Double r4 = java.lang.Double.valueOf(r6)     // Catch:{ Exception -> 0x0513 }
            int r4 = r4.intValue()     // Catch:{ Exception -> 0x0513 }
            android.support.v4.app.NotificationCompat$Builder r5 = r5.setVisibility(r4)     // Catch:{ Exception -> 0x0513 }
        L_0x047c:
            java.lang.String r4 = "when"
            boolean r4 = r2.containsKey(r4)     // Catch:{ Exception -> 0x0513 }
            if (r4 == 0) goto L_0x0496
            java.lang.String r4 = "when"
            double r6 = r2.getDouble(r4)     // Catch:{ Exception -> 0x0513 }
            java.lang.Double r4 = java.lang.Double.valueOf(r6)     // Catch:{ Exception -> 0x0513 }
            long r6 = r4.longValue()     // Catch:{ Exception -> 0x0513 }
            android.support.v4.app.NotificationCompat$Builder r5 = r5.setWhen(r6)     // Catch:{ Exception -> 0x0513 }
        L_0x0496:
            java.lang.String r4 = "actions"
            boolean r4 = r2.containsKey(r4)     // Catch:{ Exception -> 0x0513 }
            if (r4 == 0) goto L_0x04c1
            java.lang.String r4 = "actions"
            java.io.Serializable r4 = r2.getSerializable(r4)     // Catch:{ Exception -> 0x0513 }
            java.util.List r4 = (java.util.List) r4     // Catch:{ Exception -> 0x0513 }
            java.util.Iterator r4 = r4.iterator()     // Catch:{ Exception -> 0x0513 }
        L_0x04aa:
            boolean r6 = r4.hasNext()     // Catch:{ Exception -> 0x0513 }
            if (r6 == 0) goto L_0x04c1
            java.lang.Object r6 = r4.next()     // Catch:{ Exception -> 0x0513 }
            android.os.Bundle r6 = (android.os.Bundle) r6     // Catch:{ Exception -> 0x0513 }
            android.os.Bundle r7 = r11.notification     // Catch:{ Exception -> 0x0513 }
            android.support.v4.app.NotificationCompat$Action r6 = r11.createAction(r12, r6, r1, r7)     // Catch:{ Exception -> 0x0513 }
            android.support.v4.app.NotificationCompat$Builder r5 = r5.addAction(r6)     // Catch:{ Exception -> 0x0513 }
            goto L_0x04aa
        L_0x04c1:
            java.lang.String r4 = "tag"
            boolean r4 = r2.containsKey(r4)     // Catch:{ Exception -> 0x0513 }
            if (r4 == 0) goto L_0x04d0
            java.lang.String r4 = "tag"
            java.lang.String r4 = r2.getString(r4)     // Catch:{ Exception -> 0x0513 }
            goto L_0x04d1
        L_0x04d0:
            r4 = r0
        L_0x04d1:
            android.os.Bundle r6 = r11.notification     // Catch:{ Exception -> 0x0513 }
            java.lang.String r7 = "clickAction"
            java.lang.String r2 = r2.getString(r7)     // Catch:{ Exception -> 0x0513 }
            android.app.PendingIntent r12 = r11.createIntent(r12, r1, r6, r2)     // Catch:{ Exception -> 0x0513 }
            android.support.v4.app.NotificationCompat$Builder r12 = r5.setContentIntent(r12)     // Catch:{ Exception -> 0x0513 }
            android.app.Notification r12 = r12.build()     // Catch:{ Exception -> 0x0513 }
            android.app.NotificationManager r1 = r11.notificationManager     // Catch:{ Exception -> 0x0513 }
            int r2 = r3.hashCode()     // Catch:{ Exception -> 0x0513 }
            r1.notify(r4, r2, r12)     // Catch:{ Exception -> 0x0513 }
            java.lang.ref.WeakReference<com.facebook.react.bridge.ReactApplicationContext> r12 = r11.reactContextWeakReference     // Catch:{ Exception -> 0x0513 }
            java.lang.Object r12 = r12.get()     // Catch:{ Exception -> 0x0513 }
            if (r12 == 0) goto L_0x0509
            java.lang.ref.WeakReference<com.facebook.react.bridge.ReactApplicationContext> r12 = r11.reactContextWeakReference     // Catch:{ Exception -> 0x0513 }
            java.lang.Object r12 = r12.get()     // Catch:{ Exception -> 0x0513 }
            com.facebook.react.bridge.ReactContext r12 = (com.facebook.react.bridge.ReactContext) r12     // Catch:{ Exception -> 0x0513 }
            java.lang.String r1 = "notifications_notification_displayed"
            android.os.Bundle r2 = r11.notification     // Catch:{ Exception -> 0x0513 }
            com.facebook.react.bridge.WritableMap r2 = com.facebook.react.bridge.Arguments.fromBundle(r2)     // Catch:{ Exception -> 0x0513 }
            p007io.invertase.firebase.C1350Utils.sendEvent(r12, r1, r2)     // Catch:{ Exception -> 0x0513 }
        L_0x0509:
            com.facebook.react.bridge.Promise r12 = r11.promise     // Catch:{ Exception -> 0x0513 }
            if (r12 == 0) goto L_0x0526
            com.facebook.react.bridge.Promise r12 = r11.promise     // Catch:{ Exception -> 0x0513 }
            r12.resolve(r0)     // Catch:{ Exception -> 0x0513 }
            goto L_0x0526
        L_0x0513:
            r12 = move-exception
            java.lang.String r1 = "DisplayNotificationTask"
            java.lang.String r2 = "Failed to send notification"
            android.util.Log.e(r1, r2, r12)
            com.facebook.react.bridge.Promise r1 = r11.promise
            if (r1 == 0) goto L_0x0526
            java.lang.String r2 = "notification/display_notification_error"
            java.lang.String r3 = "Could not send notification"
            r1.reject(r2, r3, r12)
        L_0x0526:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: p007io.invertase.firebase.notifications.DisplayNotificationTask.doInBackground(java.lang.Void[]):java.lang.Void");
    }

    private Action createAction(Context context, Bundle bundle, Class cls, Bundle bundle2) {
        PendingIntent pendingIntent;
        String string = bundle.getString("action");
        if (bundle.containsKey("showUserInterface") && bundle.getBoolean("showUserInterface")) {
            pendingIntent = createIntent(context, cls, bundle2, string);
        } else {
            pendingIntent = createBroadcastIntent(context, bundle2, string);
        }
        Builder builder = new Builder(getIcon(bundle.getString("icon")), bundle.getString("title"), pendingIntent);
        if (bundle.containsKey("allowGeneratedReplies")) {
            builder = builder.setAllowGeneratedReplies(bundle.getBoolean("allowGeneratedReplies"));
        }
        if (bundle.containsKey("remoteInputs")) {
            for (Bundle createRemoteInput : (List) bundle.getSerializable("remoteInputs")) {
                builder = builder.addRemoteInput(createRemoteInput(createRemoteInput));
            }
        }
        return builder.build();
    }

    private PendingIntent createIntent(Context context, Class cls, Bundle bundle, String str) {
        Intent intent = new Intent(context, cls);
        intent.addFlags(536870912);
        intent.putExtras(bundle);
        if (str != null) {
            intent.setAction(str);
        }
        return PendingIntent.getActivity(context, bundle.getString("notificationId").hashCode(), intent, 134217728);
    }

    private PendingIntent createBroadcastIntent(Context context, Bundle bundle, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(bundle.getString("notificationId"));
        sb.append(str);
        String sb2 = sb.toString();
        Intent intent = new Intent(context, RNFirebaseBackgroundNotificationActionReceiver.class);
        intent.putExtra("action", str);
        intent.addFlags(536870912);
        intent.putExtra("notification", bundle);
        intent.setAction("io.invertase.firebase.notifications.BackgroundAction");
        return PendingIntent.getBroadcast(context, sb2.hashCode(), intent, 134217728);
    }

    private RemoteInput createRemoteInput(Bundle bundle) {
        RemoteInput.Builder builder = new RemoteInput.Builder(bundle.getString("resultKey"));
        if (bundle.containsKey("allowedDataTypes")) {
            for (Bundle bundle2 : (List) bundle.getSerializable("allowedDataTypes")) {
                builder.setAllowDataType(bundle2.getString("mimeType"), bundle2.getBoolean("allow"));
            }
        }
        if (bundle.containsKey("allowFreeFormInput")) {
            builder.setAllowFreeFormInput(bundle.getBoolean("allowFreeFormInput"));
        }
        if (bundle.containsKey("choices")) {
            ArrayList stringArrayList = bundle.getStringArrayList("choices");
            builder.setChoices((CharSequence[]) stringArrayList.toArray(new String[stringArrayList.size()]));
        }
        if (bundle.containsKey("label")) {
            builder.setLabel(bundle.getString("label"));
        }
        return builder.build();
    }

    private Bitmap getBitmap(String str) {
        if (str.startsWith("http://") || str.startsWith("https://")) {
            return getBitmapFromUrl(str);
        }
        if (str.startsWith("file://")) {
            return BitmapFactory.decodeFile(str.replace("file://", ""));
        }
        return BitmapFactory.decodeResource(((Context) this.contextWeakReference.get()).getResources(), getIcon(str));
    }

    private Bitmap getBitmapFromUrl(String str) {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();
            return BitmapFactory.decodeStream(httpURLConnection.getInputStream());
        } catch (IOException e) {
            String str2 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to get bitmap for url: ");
            sb.append(str);
            Log.e(str2, sb.toString(), e);
            return null;
        }
    }

    private int getIcon(String str) {
        int resourceId = RNFirebaseNotificationManager.getResourceId((Context) this.contextWeakReference.get(), "mipmap", str);
        return resourceId == 0 ? RNFirebaseNotificationManager.getResourceId((Context) this.contextWeakReference.get(), "drawable", str) : resourceId;
    }

    private Class getMainActivityClass(Context context) {
        try {
            return Class.forName(context.getPackageManager().getLaunchIntentForPackage(context.getPackageName()).getComponent().getClassName());
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "Failed to get main activity class", e);
            return null;
        }
    }
}

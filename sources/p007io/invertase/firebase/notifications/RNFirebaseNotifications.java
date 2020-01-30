package p007io.invertase.firebase.notifications;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.p000v4.app.RemoteInput;
import android.support.p000v4.content.LocalBroadcastManager;
import android.util.Log;
import com.facebook.common.util.UriUtil;
import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.uimanager.ViewProps;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.messaging.RemoteMessage.Notification;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import me.leolin.shortcutbadger.ShortcutBadger;
import p007io.invertase.firebase.C1350Utils;
import p007io.invertase.firebase.messaging.RNFirebaseMessagingService;

/* renamed from: io.invertase.firebase.notifications.RNFirebaseNotifications */
public class RNFirebaseNotifications extends ReactContextBaseJavaModule implements ActivityEventListener {
    private static final String BADGE_FILE = "BadgeCountFile";
    private static final String BADGE_KEY = "BadgeCount";
    private static final String TAG = "RNFirebaseNotifications";
    private RNFirebaseNotificationManager notificationManager;
    private SharedPreferences sharedPreferences = null;

    /* renamed from: io.invertase.firebase.notifications.RNFirebaseNotifications$RemoteNotificationReceiver */
    private class RemoteNotificationReceiver extends BroadcastReceiver {
        private RemoteNotificationReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (RNFirebaseNotifications.this.getReactApplicationContext().hasActiveCatalystInstance()) {
                Log.d(RNFirebaseNotifications.TAG, "Received new remote notification");
                C1350Utils.sendEvent(RNFirebaseNotifications.this.getReactApplicationContext(), "notifications_notification_received", RNFirebaseNotifications.this.parseRemoteMessage(intent.getParcelableExtra("notification")));
            }
        }
    }

    /* renamed from: io.invertase.firebase.notifications.RNFirebaseNotifications$ScheduledNotificationReceiver */
    private class ScheduledNotificationReceiver extends BroadcastReceiver {
        private ScheduledNotificationReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (RNFirebaseNotifications.this.getReactApplicationContext().hasActiveCatalystInstance()) {
                Log.d(RNFirebaseNotifications.TAG, "Received new scheduled notification");
                C1350Utils.sendEvent(RNFirebaseNotifications.this.getReactApplicationContext(), "notifications_notification_received", RNFirebaseNotifications.this.parseNotificationBundle(intent.getBundleExtra("notification")));
            }
        }
    }

    public String getName() {
        return TAG;
    }

    public void onActivityResult(Activity activity, int i, int i2, Intent intent) {
    }

    RNFirebaseNotifications(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
        reactApplicationContext.addActivityEventListener(this);
        this.notificationManager = new RNFirebaseNotificationManager(reactApplicationContext);
        this.sharedPreferences = reactApplicationContext.getSharedPreferences(BADGE_FILE, 0);
        LocalBroadcastManager instance = LocalBroadcastManager.getInstance(reactApplicationContext);
        instance.registerReceiver(new RemoteNotificationReceiver(), new IntentFilter(RNFirebaseMessagingService.REMOTE_NOTIFICATION_EVENT));
        instance.registerReceiver(new ScheduledNotificationReceiver(), new IntentFilter("notifications-scheduled-notification"));
    }

    @ReactMethod
    public void cancelAllNotifications(Promise promise) {
        this.notificationManager.cancelAllNotifications(promise);
    }

    @ReactMethod
    public void cancelNotification(String str, Promise promise) {
        this.notificationManager.cancelNotification(str, promise);
    }

    @ReactMethod
    public void displayNotification(ReadableMap readableMap, Promise promise) {
        this.notificationManager.displayNotification(readableMap, promise);
    }

    @ReactMethod
    public void getBadge(Promise promise) {
        int i = this.sharedPreferences.getInt(BADGE_KEY, 0);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Got badge count: ");
        sb.append(i);
        Log.d(str, sb.toString());
        promise.resolve(Integer.valueOf(i));
    }

    @ReactMethod
    public void getInitialNotification(Promise promise) {
        promise.resolve(getCurrentActivity() != null ? parseIntentForNotification(getCurrentActivity().getIntent()) : null);
    }

    @ReactMethod
    public void getScheduledNotifications(Promise promise) {
        ArrayList scheduledNotifications = this.notificationManager.getScheduledNotifications();
        WritableArray createArray = Arguments.createArray();
        Iterator it = scheduledNotifications.iterator();
        while (it.hasNext()) {
            createArray.pushMap(parseNotificationBundle((Bundle) it.next()));
        }
        promise.resolve(createArray);
    }

    @ReactMethod
    public void removeAllDeliveredNotifications(Promise promise) {
        this.notificationManager.removeAllDeliveredNotifications(promise);
    }

    @ReactMethod
    public void removeDeliveredNotification(String str, Promise promise) {
        this.notificationManager.removeDeliveredNotification(str, promise);
    }

    @ReactMethod
    public void removeDeliveredNotificationsByTag(String str, Promise promise) {
        this.notificationManager.removeDeliveredNotificationsByTag(str, promise);
    }

    @ReactMethod
    public void setBadge(int i, Promise promise) {
        this.sharedPreferences.edit().putInt(BADGE_KEY, i).apply();
        if (i == 0) {
            Log.d(TAG, "Remove badge count");
            ShortcutBadger.removeCount(getReactApplicationContext());
        } else {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Apply badge count: ");
            sb.append(i);
            Log.d(str, sb.toString());
            ShortcutBadger.applyCount(getReactApplicationContext(), i);
        }
        promise.resolve(null);
    }

    @ReactMethod
    public void scheduleNotification(ReadableMap readableMap, Promise promise) {
        this.notificationManager.scheduleNotification(readableMap, promise);
    }

    @ReactMethod
    public void createChannel(ReadableMap readableMap, Promise promise) {
        try {
            this.notificationManager.createChannel(readableMap);
        } catch (Throwable unused) {
        }
        promise.resolve(null);
    }

    @ReactMethod
    public void createChannelGroup(ReadableMap readableMap, Promise promise) {
        try {
            this.notificationManager.createChannelGroup(readableMap);
        } catch (Throwable unused) {
        }
        promise.resolve(null);
    }

    @ReactMethod
    public void createChannelGroups(ReadableArray readableArray, Promise promise) {
        try {
            this.notificationManager.createChannelGroups(readableArray);
        } catch (Throwable unused) {
        }
        promise.resolve(null);
    }

    @ReactMethod
    public void createChannels(ReadableArray readableArray, Promise promise) {
        try {
            this.notificationManager.createChannels(readableArray);
        } catch (Throwable unused) {
        }
        promise.resolve(null);
    }

    @ReactMethod
    public void deleteChannelGroup(String str, Promise promise) {
        try {
            this.notificationManager.deleteChannelGroup(str);
            promise.resolve(null);
        } catch (NullPointerException unused) {
            promise.reject("notifications/channel-group-not-found", "The requested NotificationChannelGroup does not exist, have you created it?");
        }
    }

    @ReactMethod
    public void deleteChannel(String str, Promise promise) {
        try {
            this.notificationManager.deleteChannel(str);
        } catch (Throwable unused) {
        }
        promise.resolve(null);
    }

    @ReactMethod
    public void getChannel(String str, Promise promise) {
        try {
            promise.resolve(this.notificationManager.getChannel(str));
        } catch (Throwable unused) {
            promise.resolve(null);
        }
    }

    @ReactMethod
    public void getChannels(Promise promise) {
        try {
            promise.resolve(this.notificationManager.getChannels());
        } catch (Throwable unused) {
            promise.resolve(Collections.emptyList());
        }
    }

    @ReactMethod
    public void getChannelGroup(String str, Promise promise) {
        try {
            promise.resolve(this.notificationManager.getChannelGroup(str));
        } catch (Throwable unused) {
            promise.resolve(null);
        }
    }

    @ReactMethod
    public void getChannelGroups(Promise promise) {
        try {
            promise.resolve(this.notificationManager.getChannelGroups());
        } catch (Throwable unused) {
            promise.resolve(Collections.emptyList());
        }
    }

    public void onNewIntent(Intent intent) {
        WritableMap parseIntentForNotification = parseIntentForNotification(intent);
        if (parseIntentForNotification != null) {
            C1350Utils.sendEvent(getReactApplicationContext(), "notifications_notification_opened", parseIntentForNotification);
        }
    }

    private WritableMap parseIntentForNotification(Intent intent) {
        WritableMap parseIntentForRemoteNotification = parseIntentForRemoteNotification(intent);
        return parseIntentForRemoteNotification == null ? parseIntentForLocalNotification(intent) : parseIntentForRemoteNotification;
    }

    private WritableMap parseIntentForLocalNotification(Intent intent) {
        if (intent.getExtras() == null || !intent.hasExtra("notificationId")) {
            return null;
        }
        WritableNativeMap makeNativeMap = Arguments.makeNativeMap(intent.getExtras());
        WritableMap createMap = Arguments.createMap();
        createMap.putString("action", intent.getAction());
        createMap.putMap("notification", makeNativeMap);
        Bundle resultsFromIntent = RemoteInput.getResultsFromIntent(intent);
        if (resultsFromIntent != null) {
            createMap.putMap("results", Arguments.makeNativeMap(resultsFromIntent));
        }
        return createMap;
    }

    private WritableMap parseIntentForRemoteNotification(Intent intent) {
        if (intent.getExtras() == null || !intent.hasExtra("google.message_id")) {
            return null;
        }
        Bundle extras = intent.getExtras();
        WritableMap createMap = Arguments.createMap();
        WritableMap createMap2 = Arguments.createMap();
        for (String str : extras.keySet()) {
            if (str.equals("google.message_id")) {
                createMap.putString("notificationId", extras.getString(str));
            } else if (!str.equals("collapse_key") && !str.equals("from") && !str.equals("google.sent_time") && !str.equals("google.ttl") && !str.equals("_fbSourceApplicationHasBeenSet")) {
                createMap2.putString(str, extras.getString(str));
            }
        }
        createMap.putMap(UriUtil.DATA_SCHEME, createMap2);
        WritableMap createMap3 = Arguments.createMap();
        createMap3.putString("action", intent.getAction());
        createMap3.putMap("notification", createMap);
        return createMap3;
    }

    /* access modifiers changed from: private */
    public WritableMap parseNotificationBundle(Bundle bundle) {
        return Arguments.makeNativeMap(bundle);
    }

    /* access modifiers changed from: private */
    public WritableMap parseRemoteMessage(RemoteMessage remoteMessage) {
        Notification notification = remoteMessage.getNotification();
        WritableMap createMap = Arguments.createMap();
        WritableMap createMap2 = Arguments.createMap();
        String notificationBody = getNotificationBody(notification);
        if (notificationBody != null) {
            createMap.putString("body", notificationBody);
        }
        if (remoteMessage.getData() != null) {
            for (Entry entry : remoteMessage.getData().entrySet()) {
                createMap2.putString((String) entry.getKey(), (String) entry.getValue());
            }
        }
        createMap.putMap(UriUtil.DATA_SCHEME, createMap2);
        if (remoteMessage.getMessageId() != null) {
            createMap.putString("notificationId", remoteMessage.getMessageId());
        }
        if (notification.getSound() != null) {
            createMap.putString("sound", notification.getSound());
        }
        String notificationTitle = getNotificationTitle(notification);
        if (notificationTitle != null) {
            createMap.putString("title", notificationTitle);
        }
        WritableMap createMap3 = Arguments.createMap();
        if (notification.getClickAction() != null) {
            createMap3.putString("clickAction", notification.getClickAction());
        }
        if (notification.getColor() != null) {
            createMap3.putString(ViewProps.COLOR, notification.getColor());
        }
        if (notification.getIcon() != null) {
            WritableMap createMap4 = Arguments.createMap();
            createMap4.putString("icon", notification.getIcon());
            createMap3.putMap("smallIcon", createMap4);
        }
        if (notification.getTag() != null) {
            createMap3.putString("group", notification.getTag());
            createMap3.putString("tag", notification.getTag());
        }
        createMap.putMap("android", createMap3);
        return createMap;
    }

    @Nullable
    private String getNotificationBody(Notification notification) {
        String body = notification.getBody();
        String bodyLocalizationKey = notification.getBodyLocalizationKey();
        if (bodyLocalizationKey == null) {
            return body;
        }
        String[] bodyLocalizationArgs = notification.getBodyLocalizationArgs();
        ReactApplicationContext reactApplicationContext = getReactApplicationContext();
        return reactApplicationContext.getResources().getString(C1350Utils.getResId(reactApplicationContext, bodyLocalizationKey), (Object[]) bodyLocalizationArgs);
    }

    @Nullable
    private String getNotificationTitle(Notification notification) {
        String title = notification.getTitle();
        String titleLocalizationKey = notification.getTitleLocalizationKey();
        if (titleLocalizationKey == null) {
            return title;
        }
        String[] titleLocalizationArgs = notification.getTitleLocalizationArgs();
        ReactApplicationContext reactApplicationContext = getReactApplicationContext();
        return reactApplicationContext.getResources().getString(C1350Utils.getResId(reactApplicationContext, titleLocalizationKey), (Object[]) titleLocalizationArgs);
    }
}

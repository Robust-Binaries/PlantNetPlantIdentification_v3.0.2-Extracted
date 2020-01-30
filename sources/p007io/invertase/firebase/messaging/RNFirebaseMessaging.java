package p007io.invertase.firebase.messaging;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.p000v4.app.NotificationManagerCompat;
import android.support.p000v4.content.LocalBroadcastManager;
import android.util.Log;
import com.facebook.common.util.UriUtil;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage.Builder;
import java.io.IOException;
import javax.annotation.Nonnull;
import p007io.invertase.firebase.C1350Utils;

/* renamed from: io.invertase.firebase.messaging.RNFirebaseMessaging */
public class RNFirebaseMessaging extends ReactContextBaseJavaModule {
    private static final String TAG = "RNFirebaseMessaging";

    /* renamed from: io.invertase.firebase.messaging.RNFirebaseMessaging$MessageReceiver */
    private class MessageReceiver extends BroadcastReceiver {
        private MessageReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (RNFirebaseMessaging.this.getReactApplicationContext().hasActiveCatalystInstance()) {
                Log.d(RNFirebaseMessaging.TAG, "Received new message");
                C1350Utils.sendEvent(RNFirebaseMessaging.this.getReactApplicationContext(), "messaging_message_received", MessagingSerializer.parseRemoteMessage(intent.getParcelableExtra("message")));
            }
        }
    }

    /* renamed from: io.invertase.firebase.messaging.RNFirebaseMessaging$RefreshTokenReceiver */
    private class RefreshTokenReceiver extends BroadcastReceiver {
        private RefreshTokenReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (RNFirebaseMessaging.this.getReactApplicationContext().hasActiveCatalystInstance()) {
                Log.d(RNFirebaseMessaging.TAG, "Received new messaging token.");
                new Thread(new Runnable() {
                    public void run() {
                        String str;
                        try {
                            str = FirebaseInstanceId.getInstance().getToken(FirebaseApp.getInstance().getOptions().getGcmSenderId(), "FCM");
                        } catch (IOException e) {
                            Log.d(RNFirebaseMessaging.TAG, "onNewToken error", e);
                            str = null;
                        }
                        if (str != null) {
                            Log.d(RNFirebaseMessaging.TAG, "Sending new messaging token event.");
                            C1350Utils.sendEvent(RNFirebaseMessaging.this.getReactApplicationContext(), "messaging_token_refreshed", str);
                        }
                    }
                }).start();
            }
        }
    }

    public String getName() {
        return TAG;
    }

    RNFirebaseMessaging(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
        LocalBroadcastManager instance = LocalBroadcastManager.getInstance(reactApplicationContext);
        instance.registerReceiver(new MessageReceiver(), new IntentFilter(RNFirebaseMessagingService.MESSAGE_EVENT));
        instance.registerReceiver(new RefreshTokenReceiver(), new IntentFilter(RNFirebaseMessagingService.NEW_TOKEN_EVENT));
    }

    @ReactMethod
    public void getToken(Promise promise) {
        try {
            promise.resolve(FirebaseInstanceId.getInstance().getToken(FirebaseApp.getInstance().getOptions().getGcmSenderId(), "FCM"));
        } catch (Throwable th) {
            th.printStackTrace();
            promise.reject("messaging/fcm-token-error", th.getMessage());
        }
    }

    @ReactMethod
    public void deleteToken(Promise promise) {
        try {
            FirebaseInstanceId.getInstance().deleteToken(FirebaseApp.getInstance().getOptions().getGcmSenderId(), "FCM");
            promise.resolve(null);
        } catch (Throwable th) {
            th.printStackTrace();
            promise.reject("messaging/fcm-token-error", th.getMessage());
        }
    }

    @ReactMethod
    public void requestPermission(Promise promise) {
        promise.resolve(null);
    }

    @ReactMethod
    public void hasPermission(Promise promise) {
        promise.resolve(Boolean.valueOf(NotificationManagerCompat.from(getReactApplicationContext()).areNotificationsEnabled()));
    }

    @ReactMethod
    public void sendMessage(ReadableMap readableMap, Promise promise) {
        if (!readableMap.hasKey("to")) {
            promise.reject("messaging/invalid-message", "The supplied message is missing a 'to' field");
            return;
        }
        Builder builder = new Builder(readableMap.getString("to"));
        if (readableMap.hasKey("collapseKey")) {
            builder = builder.setCollapseKey(readableMap.getString("collapseKey"));
        }
        if (readableMap.hasKey("messageId")) {
            builder = builder.setMessageId(readableMap.getString("messageId"));
        }
        if (readableMap.hasKey("messageType")) {
            builder = builder.setMessageType(readableMap.getString("messageType"));
        }
        if (readableMap.hasKey("ttl")) {
            builder = builder.setTtl(readableMap.getInt("ttl"));
        }
        if (readableMap.hasKey(UriUtil.DATA_SCHEME)) {
            ReadableMap map = readableMap.getMap(UriUtil.DATA_SCHEME);
            ReadableMapKeySetIterator keySetIterator = map.keySetIterator();
            while (keySetIterator.hasNextKey()) {
                String nextKey = keySetIterator.nextKey();
                builder = builder.addData(nextKey, map.getString(nextKey));
            }
        }
        FirebaseMessaging.getInstance().send(builder.build());
        promise.resolve(null);
    }

    @ReactMethod
    public void subscribeToTopic(String str, final Promise promise) {
        FirebaseMessaging.getInstance().subscribeToTopic(str).addOnCompleteListener(new OnCompleteListener<Void>() {
            public void onComplete(@Nonnull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(RNFirebaseMessaging.TAG, "subscribeToTopic:onComplete:success");
                    promise.resolve(null);
                    return;
                }
                Exception exception = task.getException();
                Log.e(RNFirebaseMessaging.TAG, "subscribeToTopic:onComplete:failure", exception);
                promise.reject((Throwable) exception);
            }
        });
    }

    @ReactMethod
    public void unsubscribeFromTopic(String str, final Promise promise) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(str).addOnCompleteListener(new OnCompleteListener<Void>() {
            public void onComplete(@Nonnull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(RNFirebaseMessaging.TAG, "unsubscribeFromTopic:onComplete:success");
                    promise.resolve(null);
                    return;
                }
                Exception exception = task.getException();
                Log.e(RNFirebaseMessaging.TAG, "unsubscribeFromTopic:onComplete:failure", exception);
                promise.reject((Throwable) exception);
            }
        });
    }
}

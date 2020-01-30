package com.facebook.react.packagerconnection;

import com.facebook.common.logging.FLog;
import javax.annotation.Nullable;

public abstract class RequestOnlyHandler implements RequestHandler {
    private static final String TAG = JSPackagerClient.class.getSimpleName();

    public abstract void onRequest(@Nullable Object obj, Responder responder);

    public final void onNotification(@Nullable Object obj) {
        FLog.m48e(TAG, "Notification is not supported");
    }
}

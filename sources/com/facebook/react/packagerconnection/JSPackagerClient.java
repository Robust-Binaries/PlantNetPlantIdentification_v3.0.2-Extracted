package com.facebook.react.packagerconnection;

import android.net.Uri.Builder;
import com.facebook.common.logging.FLog;
import com.facebook.react.modules.systeminfo.AndroidInfoHelpers;
import com.facebook.react.packagerconnection.ReconnectingWebSocket.ConnectionCallback;
import com.facebook.react.packagerconnection.ReconnectingWebSocket.MessageCallback;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import java.util.Map;
import javax.annotation.Nullable;
import okio.ByteString;
import org.json.JSONObject;

public final class JSPackagerClient implements MessageCallback {
    private static final String PACKAGER_CONNECTION_URL_FORMAT = "ws://%s/message?device=%s&app=%s&context=%s";
    private static final int PROTOCOL_VERSION = 2;
    /* access modifiers changed from: private */
    public static final String TAG = "JSPackagerClient";
    private Map<String, RequestHandler> mRequestHandlers;
    /* access modifiers changed from: private */
    public ReconnectingWebSocket mWebSocket;

    private class ResponderImpl implements Responder {
        private Object mId;

        public ResponderImpl(Object obj) {
            this.mId = obj;
        }

        public void respond(Object obj) {
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("version", 2);
                jSONObject.put("id", this.mId);
                jSONObject.put("result", obj);
                JSPackagerClient.this.mWebSocket.sendMessage(jSONObject.toString());
            } catch (Exception e) {
                FLog.m49e(JSPackagerClient.TAG, "Responding failed", (Throwable) e);
            }
        }

        public void error(Object obj) {
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("version", 2);
                jSONObject.put("id", this.mId);
                jSONObject.put("error", obj);
                JSPackagerClient.this.mWebSocket.sendMessage(jSONObject.toString());
            } catch (Exception e) {
                FLog.m49e(JSPackagerClient.TAG, "Responding with error failed", (Throwable) e);
            }
        }
    }

    public JSPackagerClient(String str, PackagerConnectionSettings packagerConnectionSettings, Map<String, RequestHandler> map) {
        this(str, packagerConnectionSettings, map, null);
    }

    public JSPackagerClient(String str, PackagerConnectionSettings packagerConnectionSettings, Map<String, RequestHandler> map, @Nullable ConnectionCallback connectionCallback) {
        Builder builder = new Builder();
        builder.scheme("ws").encodedAuthority(packagerConnectionSettings.getDebugServerHost()).appendPath("message").appendQueryParameter("device", AndroidInfoHelpers.getFriendlyDeviceName()).appendQueryParameter("app", packagerConnectionSettings.getPackageName()).appendQueryParameter("clientid", str);
        this.mWebSocket = new ReconnectingWebSocket(builder.build().toString(), this, connectionCallback);
        this.mRequestHandlers = map;
    }

    public void init() {
        this.mWebSocket.connect();
    }

    public void close() {
        this.mWebSocket.closeQuietly();
    }

    public void onMessage(String str) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            int optInt = jSONObject.optInt("version");
            String optString = jSONObject.optString(Param.METHOD);
            Object opt = jSONObject.opt("id");
            Object opt2 = jSONObject.opt("params");
            if (optInt != 2) {
                String str2 = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Message with incompatible or missing version of protocol received: ");
                sb.append(optInt);
                FLog.m48e(str2, sb.toString());
            } else if (optString == null) {
                abortOnMessage(opt, "No method provided");
            } else {
                RequestHandler requestHandler = (RequestHandler) this.mRequestHandlers.get(optString);
                if (requestHandler == null) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("No request handler for method: ");
                    sb2.append(optString);
                    abortOnMessage(opt, sb2.toString());
                    return;
                }
                if (opt == null) {
                    requestHandler.onNotification(opt2);
                } else {
                    requestHandler.onRequest(opt2, new ResponderImpl(opt));
                }
            }
        } catch (Exception e) {
            FLog.m49e(TAG, "Handling the message failed", (Throwable) e);
        }
    }

    public void onMessage(ByteString byteString) {
        FLog.m88w(TAG, "Websocket received message with payload of unexpected type binary");
    }

    private void abortOnMessage(Object obj, String str) {
        if (obj != null) {
            new ResponderImpl(obj).error(str);
        }
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Handling the message failed with reason: ");
        sb.append(str);
        FLog.m48e(str2, sb.toString());
    }
}

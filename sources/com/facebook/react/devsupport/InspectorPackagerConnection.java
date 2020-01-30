package com.facebook.react.devsupport;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.p000v4.app.NotificationCompat;
import com.facebook.common.logging.FLog;
import com.facebook.react.bridge.Inspector;
import com.facebook.react.bridge.Inspector.LocalConnection;
import com.facebook.react.bridge.Inspector.Page;
import com.facebook.react.bridge.Inspector.RemoteConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import okhttp3.C1512Response;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InspectorPackagerConnection {
    private static final String TAG = "InspectorPackagerConnection";
    private BundleStatusProvider mBundleStatusProvider;
    private final Connection mConnection;
    /* access modifiers changed from: private */
    public final Map<String, LocalConnection> mInspectorConnections = new HashMap();
    private final String mPackageName;

    public static class BundleStatus {
        public Boolean isLastDownloadSucess;
        public long updateTimestamp;

        public BundleStatus(Boolean bool, long j) {
            this.updateTimestamp = -1;
            this.isLastDownloadSucess = bool;
            this.updateTimestamp = j;
        }

        public BundleStatus() {
            this(Boolean.valueOf(false), -1);
        }
    }

    public interface BundleStatusProvider {
        BundleStatus getBundleStatus();
    }

    private class Connection extends WebSocketListener {
        private static final int RECONNECT_DELAY_MS = 2000;
        /* access modifiers changed from: private */
        public boolean mClosed;
        private final Handler mHandler = new Handler(Looper.getMainLooper());
        private OkHttpClient mHttpClient;
        private boolean mSuppressConnectionErrors;
        private final String mUrl;
        @Nullable
        private WebSocket mWebSocket;

        public Connection(String str) {
            this.mUrl = str;
        }

        public void onOpen(WebSocket webSocket, C1512Response response) {
            this.mWebSocket = webSocket;
        }

        public void onFailure(WebSocket webSocket, Throwable th, C1512Response response) {
            if (this.mWebSocket != null) {
                abort("Websocket exception", th);
            }
            if (!this.mClosed) {
                reconnect();
            }
        }

        public void onMessage(WebSocket webSocket, String str) {
            try {
                InspectorPackagerConnection.this.handleProxyMessage(new JSONObject(str));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public void onClosed(WebSocket webSocket, int i, String str) {
            this.mWebSocket = null;
            InspectorPackagerConnection.this.closeAllConnections();
            if (!this.mClosed) {
                reconnect();
            }
        }

        public void connect() {
            if (!this.mClosed) {
                if (this.mHttpClient == null) {
                    this.mHttpClient = new Builder().connectTimeout(10, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS).readTimeout(0, TimeUnit.MINUTES).build();
                }
                this.mHttpClient.newWebSocket(new Request.Builder().url(this.mUrl).build(), this);
                return;
            }
            throw new IllegalStateException("Can't connect closed client");
        }

        private void reconnect() {
            if (!this.mClosed) {
                if (!this.mSuppressConnectionErrors) {
                    FLog.m88w(InspectorPackagerConnection.TAG, "Couldn't connect to packager, will silently retry");
                    this.mSuppressConnectionErrors = true;
                }
                this.mHandler.postDelayed(new Runnable() {
                    public void run() {
                        if (!Connection.this.mClosed) {
                            Connection.this.connect();
                        }
                    }
                }, 2000);
                return;
            }
            throw new IllegalStateException("Can't reconnect closed client");
        }

        public void close() {
            this.mClosed = true;
            WebSocket webSocket = this.mWebSocket;
            if (webSocket != null) {
                try {
                    webSocket.close(1000, "End of session");
                } catch (Exception unused) {
                }
                this.mWebSocket = null;
            }
        }

        public void send(final JSONObject jSONObject) {
            new AsyncTask<WebSocket, Void, Void>() {
                /* access modifiers changed from: protected */
                public Void doInBackground(WebSocket... webSocketArr) {
                    if (webSocketArr == null || webSocketArr.length == 0) {
                        return null;
                    }
                    try {
                        webSocketArr[0].send(jSONObject.toString());
                    } catch (Exception e) {
                        FLog.m89w(InspectorPackagerConnection.TAG, "Couldn't send event to packager", (Throwable) e);
                    }
                    return null;
                }
            }.execute(new WebSocket[]{this.mWebSocket});
        }

        private void abort(String str, Throwable th) {
            String str2 = InspectorPackagerConnection.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Error occurred, shutting down websocket connection: ");
            sb.append(str);
            FLog.m49e(str2, sb.toString(), th);
            InspectorPackagerConnection.this.closeAllConnections();
            closeWebSocketQuietly();
        }

        private void closeWebSocketQuietly() {
            WebSocket webSocket = this.mWebSocket;
            if (webSocket != null) {
                try {
                    webSocket.close(1000, "End of session");
                } catch (Exception unused) {
                }
                this.mWebSocket = null;
            }
        }
    }

    public InspectorPackagerConnection(String str, String str2, BundleStatusProvider bundleStatusProvider) {
        this.mConnection = new Connection(str);
        this.mPackageName = str2;
        this.mBundleStatusProvider = bundleStatusProvider;
    }

    public void connect() {
        this.mConnection.connect();
    }

    public void closeQuietly() {
        this.mConnection.close();
    }

    public void sendEventToAllConnections(String str) {
        for (Entry value : this.mInspectorConnections.entrySet()) {
            ((LocalConnection) value.getValue()).sendMessage(str);
        }
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x004b  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0062  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x006c  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0076  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0080  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void handleProxyMessage(org.json.JSONObject r4) throws org.json.JSONException, java.io.IOException {
        /*
            r3 = this;
            java.lang.String r0 = "event"
            java.lang.String r0 = r4.getString(r0)
            int r1 = r0.hashCode()
            r2 = 530405532(0x1f9d589c, float:6.663868E-20)
            if (r1 == r2) goto L_0x003d
            r2 = 951351530(0x38b478ea, float:8.605591E-5)
            if (r1 == r2) goto L_0x0033
            r2 = 1328613653(0x4f310915, float:2.97016243E9)
            if (r1 == r2) goto L_0x0029
            r2 = 1962251790(0x74f5960e, float:1.5565872E32)
            if (r1 == r2) goto L_0x001f
            goto L_0x0047
        L_0x001f:
            java.lang.String r1 = "getPages"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L_0x0047
            r1 = 0
            goto L_0x0048
        L_0x0029:
            java.lang.String r1 = "wrappedEvent"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L_0x0047
            r1 = 1
            goto L_0x0048
        L_0x0033:
            java.lang.String r1 = "connect"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L_0x0047
            r1 = 2
            goto L_0x0048
        L_0x003d:
            java.lang.String r1 = "disconnect"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L_0x0047
            r1 = 3
            goto L_0x0048
        L_0x0047:
            r1 = -1
        L_0x0048:
            switch(r1) {
                case 0: goto L_0x0080;
                case 1: goto L_0x0076;
                case 2: goto L_0x006c;
                case 3: goto L_0x0062;
                default: goto L_0x004b;
            }
        L_0x004b:
            java.lang.IllegalArgumentException r4 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Unknown event: "
            r1.append(r2)
            r1.append(r0)
            java.lang.String r0 = r1.toString()
            r4.<init>(r0)
            throw r4
        L_0x0062:
            java.lang.String r0 = "payload"
            org.json.JSONObject r4 = r4.getJSONObject(r0)
            r3.handleDisconnect(r4)
            goto L_0x0089
        L_0x006c:
            java.lang.String r0 = "payload"
            org.json.JSONObject r4 = r4.getJSONObject(r0)
            r3.handleConnect(r4)
            goto L_0x0089
        L_0x0076:
            java.lang.String r0 = "payload"
            org.json.JSONObject r4 = r4.getJSONObject(r0)
            r3.handleWrappedEvent(r4)
            goto L_0x0089
        L_0x0080:
            java.lang.String r4 = "getPages"
            org.json.JSONArray r0 = r3.getPages()
            r3.sendEvent(r4, r0)
        L_0x0089:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.react.devsupport.InspectorPackagerConnection.handleProxyMessage(org.json.JSONObject):void");
    }

    /* access modifiers changed from: 0000 */
    public void closeAllConnections() {
        for (Entry value : this.mInspectorConnections.entrySet()) {
            ((LocalConnection) value.getValue()).disconnect();
        }
        this.mInspectorConnections.clear();
    }

    private void handleConnect(JSONObject jSONObject) throws JSONException {
        final String string = jSONObject.getString("pageId");
        if (((LocalConnection) this.mInspectorConnections.remove(string)) == null) {
            try {
                this.mInspectorConnections.put(string, Inspector.connect(Integer.parseInt(string), new RemoteConnection() {
                    public void onMessage(String str) {
                        try {
                            InspectorPackagerConnection.this.sendWrappedEvent(string, str);
                        } catch (JSONException e) {
                            FLog.m89w(InspectorPackagerConnection.TAG, "Couldn't send event to packager", (Throwable) e);
                        }
                    }

                    public void onDisconnect() {
                        try {
                            InspectorPackagerConnection.this.mInspectorConnections.remove(string);
                            InspectorPackagerConnection.this.sendEvent("disconnect", InspectorPackagerConnection.this.makePageIdPayload(string));
                        } catch (JSONException e) {
                            FLog.m89w(InspectorPackagerConnection.TAG, "Couldn't send event to packager", (Throwable) e);
                        }
                    }
                }));
            } catch (Exception e) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Failed to open page: ");
                sb.append(string);
                FLog.m89w(str, sb.toString(), (Throwable) e);
                sendEvent("disconnect", makePageIdPayload(string));
            }
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Already connected: ");
            sb2.append(string);
            throw new IllegalStateException(sb2.toString());
        }
    }

    private void handleDisconnect(JSONObject jSONObject) throws JSONException {
        LocalConnection localConnection = (LocalConnection) this.mInspectorConnections.remove(jSONObject.getString("pageId"));
        if (localConnection != null) {
            localConnection.disconnect();
        }
    }

    private void handleWrappedEvent(JSONObject jSONObject) throws JSONException {
        String string = jSONObject.getString("pageId");
        String string2 = jSONObject.getString("wrappedEvent");
        LocalConnection localConnection = (LocalConnection) this.mInspectorConnections.get(string);
        if (localConnection != null) {
            localConnection.sendMessage(string2);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Not connected: ");
        sb.append(string);
        throw new IllegalStateException(sb.toString());
    }

    private JSONArray getPages() throws JSONException {
        List<Page> pages = Inspector.getPages();
        JSONArray jSONArray = new JSONArray();
        BundleStatus bundleStatus = this.mBundleStatusProvider.getBundleStatus();
        for (Page page : pages) {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("id", String.valueOf(page.getId()));
            jSONObject.put("title", page.getTitle());
            jSONObject.put("app", this.mPackageName);
            jSONObject.put("vm", page.getVM());
            jSONObject.put("isLastBundleDownloadSuccess", bundleStatus.isLastDownloadSucess);
            jSONObject.put("bundleUpdateTimestamp", bundleStatus.updateTimestamp);
            jSONArray.put(jSONObject);
        }
        return jSONArray;
    }

    /* access modifiers changed from: private */
    public void sendWrappedEvent(String str, String str2) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("pageId", str);
        jSONObject.put("wrappedEvent", str2);
        sendEvent("wrappedEvent", jSONObject);
    }

    /* access modifiers changed from: private */
    public void sendEvent(String str, Object obj) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put(NotificationCompat.CATEGORY_EVENT, str);
        jSONObject.put("payload", obj);
        this.mConnection.send(jSONObject);
    }

    /* access modifiers changed from: private */
    public JSONObject makePageIdPayload(String str) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("pageId", str);
        return jSONObject;
    }
}

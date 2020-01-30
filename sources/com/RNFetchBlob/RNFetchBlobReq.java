package com.RNFetchBlob;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.os.Build.VERSION;
import android.support.p000v4.app.NotificationCompat;
import com.facebook.common.logging.FLog;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import okhttp3.C1512Response;
import okhttp3.Call;
import okhttp3.ConnectionPool;
import okhttp3.ConnectionSpec;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.TlsVersion;

public class RNFetchBlobReq extends BroadcastReceiver implements Runnable {
    public static HashMap<String, Long> androidDownloadManagerTaskTable = new HashMap<>();
    static ConnectionPool pool = new ConnectionPool();
    static HashMap<String, RNFetchBlobProgressConfig> progressReport = new HashMap<>();
    public static HashMap<String, Call> taskTable = new HashMap<>();
    static HashMap<String, RNFetchBlobProgressConfig> uploadProgressReport = new HashMap<>();
    Callback callback;
    OkHttpClient client;
    long contentLength;
    String destPath;
    long downloadManagerId;
    ReadableMap headers;
    String method;
    RNFetchBlobConfig options;
    String rawRequestBody;
    ReadableArray rawRequestBodyArray;
    ArrayList<String> redirects = new ArrayList<>();
    RNFetchBlobBody requestBody;
    RequestType requestType;
    WritableMap respInfo;
    ResponseFormat responseFormat = ResponseFormat.Auto;
    ResponseType responseType;
    String taskId;
    boolean timeout = false;
    String url;

    enum RequestType {
        Form,
        SingleFile,
        AsIs,
        WithoutBody,
        Others
    }

    enum ResponseFormat {
        Auto,
        UTF8,
        BASE64
    }

    enum ResponseType {
        KeepInMemory,
        FileStorage
    }

    public RNFetchBlobReq(ReadableMap readableMap, String str, String str2, String str3, ReadableMap readableMap2, String str4, ReadableArray readableArray, OkHttpClient okHttpClient, Callback callback2) {
        this.method = str2.toUpperCase();
        this.options = new RNFetchBlobConfig(readableMap);
        this.taskId = str;
        this.url = str3;
        this.headers = readableMap2;
        this.callback = callback2;
        this.rawRequestBody = str4;
        this.rawRequestBodyArray = readableArray;
        this.client = okHttpClient;
        if (this.options.fileCache.booleanValue() || this.options.path != null) {
            this.responseType = ResponseType.FileStorage;
        } else {
            this.responseType = ResponseType.KeepInMemory;
        }
        if (str4 != null) {
            this.requestType = RequestType.SingleFile;
        } else if (readableArray != null) {
            this.requestType = RequestType.Form;
        } else {
            this.requestType = RequestType.WithoutBody;
        }
    }

    public static void cancelTask(String str) {
        if (taskTable.containsKey(str)) {
            ((Call) taskTable.get(str)).cancel();
            taskTable.remove(str);
        }
        if (androidDownloadManagerTaskTable.containsKey(str)) {
            long longValue = ((Long) androidDownloadManagerTaskTable.get(str)).longValue();
            ((DownloadManager) C0568RNFetchBlob.RCTContext.getApplicationContext().getSystemService("download")).remove(new long[]{longValue});
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:116:0x02fe A[Catch:{ Exception -> 0x044a }] */
    /* JADX WARNING: Removed duplicated region for block: B:117:0x0300 A[Catch:{ Exception -> 0x044a }] */
    /* JADX WARNING: Removed duplicated region for block: B:126:0x0333 A[Catch:{ Exception -> 0x044a }] */
    /* JADX WARNING: Removed duplicated region for block: B:127:0x0380 A[Catch:{ Exception -> 0x044a }] */
    /* JADX WARNING: Removed duplicated region for block: B:128:0x03af A[Catch:{ Exception -> 0x044a }] */
    /* JADX WARNING: Removed duplicated region for block: B:131:0x03fb A[Catch:{ Exception -> 0x044a }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
            r10 = this;
            com.RNFetchBlob.RNFetchBlobConfig r0 = r10.options
            com.facebook.react.bridge.ReadableMap r0 = r0.addAndroidDownloads
            r1 = 2
            r2 = 1
            if (r0 == 0) goto L_0x0118
            com.RNFetchBlob.RNFetchBlobConfig r0 = r10.options
            com.facebook.react.bridge.ReadableMap r0 = r0.addAndroidDownloads
            java.lang.String r3 = "useDownloadManager"
            boolean r0 = r0.hasKey(r3)
            if (r0 == 0) goto L_0x0118
            com.RNFetchBlob.RNFetchBlobConfig r0 = r10.options
            com.facebook.react.bridge.ReadableMap r0 = r0.addAndroidDownloads
            java.lang.String r3 = "useDownloadManager"
            boolean r0 = r0.getBoolean(r3)
            if (r0 == 0) goto L_0x0118
            java.lang.String r0 = r10.url
            android.net.Uri r0 = android.net.Uri.parse(r0)
            android.app.DownloadManager$Request r3 = new android.app.DownloadManager$Request
            r3.<init>(r0)
            com.RNFetchBlob.RNFetchBlobConfig r0 = r10.options
            com.facebook.react.bridge.ReadableMap r0 = r0.addAndroidDownloads
            java.lang.String r4 = "notification"
            boolean r0 = r0.getBoolean(r4)
            if (r0 == 0) goto L_0x003b
            r3.setNotificationVisibility(r2)
            goto L_0x003e
        L_0x003b:
            r3.setNotificationVisibility(r1)
        L_0x003e:
            com.RNFetchBlob.RNFetchBlobConfig r0 = r10.options
            com.facebook.react.bridge.ReadableMap r0 = r0.addAndroidDownloads
            java.lang.String r1 = "title"
            boolean r0 = r0.hasKey(r1)
            if (r0 == 0) goto L_0x0057
            com.RNFetchBlob.RNFetchBlobConfig r0 = r10.options
            com.facebook.react.bridge.ReadableMap r0 = r0.addAndroidDownloads
            java.lang.String r1 = "title"
            java.lang.String r0 = r0.getString(r1)
            r3.setTitle(r0)
        L_0x0057:
            com.RNFetchBlob.RNFetchBlobConfig r0 = r10.options
            com.facebook.react.bridge.ReadableMap r0 = r0.addAndroidDownloads
            java.lang.String r1 = "description"
            boolean r0 = r0.hasKey(r1)
            if (r0 == 0) goto L_0x0070
            com.RNFetchBlob.RNFetchBlobConfig r0 = r10.options
            com.facebook.react.bridge.ReadableMap r0 = r0.addAndroidDownloads
            java.lang.String r1 = "description"
            java.lang.String r0 = r0.getString(r1)
            r3.setDescription(r0)
        L_0x0070:
            com.RNFetchBlob.RNFetchBlobConfig r0 = r10.options
            com.facebook.react.bridge.ReadableMap r0 = r0.addAndroidDownloads
            java.lang.String r1 = "path"
            boolean r0 = r0.hasKey(r1)
            if (r0 == 0) goto L_0x009e
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "file://"
            r0.append(r1)
            com.RNFetchBlob.RNFetchBlobConfig r1 = r10.options
            com.facebook.react.bridge.ReadableMap r1 = r1.addAndroidDownloads
            java.lang.String r2 = "path"
            java.lang.String r1 = r1.getString(r2)
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            android.net.Uri r0 = android.net.Uri.parse(r0)
            r3.setDestinationUri(r0)
        L_0x009e:
            com.RNFetchBlob.RNFetchBlobConfig r0 = r10.options
            com.facebook.react.bridge.ReadableMap r0 = r0.addAndroidDownloads
            java.lang.String r1 = "mime"
            boolean r0 = r0.hasKey(r1)
            if (r0 == 0) goto L_0x00b7
            com.RNFetchBlob.RNFetchBlobConfig r0 = r10.options
            com.facebook.react.bridge.ReadableMap r0 = r0.addAndroidDownloads
            java.lang.String r1 = "mime"
            java.lang.String r0 = r0.getString(r1)
            r3.setMimeType(r0)
        L_0x00b7:
            com.facebook.react.bridge.ReadableMap r0 = r10.headers
            com.facebook.react.bridge.ReadableMapKeySetIterator r0 = r0.keySetIterator()
            com.RNFetchBlob.RNFetchBlobConfig r1 = r10.options
            com.facebook.react.bridge.ReadableMap r1 = r1.addAndroidDownloads
            java.lang.String r2 = "mediaScannable"
            boolean r1 = r1.hasKey(r2)
            if (r1 == 0) goto L_0x00d8
            com.RNFetchBlob.RNFetchBlobConfig r1 = r10.options
            com.facebook.react.bridge.ReadableMap r1 = r1.addAndroidDownloads
            java.lang.String r2 = "mediaScannable"
            boolean r1 = r1.hasKey(r2)
            if (r1 == 0) goto L_0x00d8
            r3.allowScanningByMediaScanner()
        L_0x00d8:
            boolean r1 = r0.hasNextKey()
            if (r1 == 0) goto L_0x00ec
            java.lang.String r1 = r0.nextKey()
            com.facebook.react.bridge.ReadableMap r2 = r10.headers
            java.lang.String r2 = r2.getString(r1)
            r3.addRequestHeader(r1, r2)
            goto L_0x00d8
        L_0x00ec:
            com.facebook.react.bridge.ReactApplicationContext r0 = com.RNFetchBlob.C0568RNFetchBlob.RCTContext
            android.content.Context r0 = r0.getApplicationContext()
            java.lang.String r1 = "download"
            java.lang.Object r1 = r0.getSystemService(r1)
            android.app.DownloadManager r1 = (android.app.DownloadManager) r1
            long r1 = r1.enqueue(r3)
            r10.downloadManagerId = r1
            java.util.HashMap<java.lang.String, java.lang.Long> r1 = androidDownloadManagerTaskTable
            java.lang.String r2 = r10.taskId
            long r3 = r10.downloadManagerId
            java.lang.Long r3 = java.lang.Long.valueOf(r3)
            r1.put(r2, r3)
            android.content.IntentFilter r1 = new android.content.IntentFilter
            java.lang.String r2 = "android.intent.action.DOWNLOAD_COMPLETE"
            r1.<init>(r2)
            r0.registerReceiver(r10, r1)
            return
        L_0x0118:
            java.lang.String r0 = r10.taskId
            com.RNFetchBlob.RNFetchBlobConfig r3 = r10.options
            java.lang.String r3 = r3.appendExt
            boolean r3 = r3.isEmpty()
            if (r3 == 0) goto L_0x0127
            java.lang.String r3 = ""
            goto L_0x013c
        L_0x0127:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "."
            r3.append(r4)
            com.RNFetchBlob.RNFetchBlobConfig r4 = r10.options
            java.lang.String r4 = r4.appendExt
            r3.append(r4)
            java.lang.String r3 = r3.toString()
        L_0x013c:
            com.RNFetchBlob.RNFetchBlobConfig r4 = r10.options
            java.lang.String r4 = r4.key
            r5 = 0
            r6 = 0
            if (r4 == 0) goto L_0x0183
            com.RNFetchBlob.RNFetchBlobConfig r0 = r10.options
            java.lang.String r0 = r0.key
            java.lang.String r0 = com.RNFetchBlob.RNFetchBlobUtils.getMD5(r0)
            if (r0 != 0) goto L_0x0150
            java.lang.String r0 = r10.taskId
        L_0x0150:
            java.io.File r4 = new java.io.File
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r8 = com.RNFetchBlob.RNFetchBlobFS.getTmpPath(r0)
            r7.append(r8)
            r7.append(r3)
            java.lang.String r7 = r7.toString()
            r4.<init>(r7)
            boolean r7 = r4.exists()
            if (r7 == 0) goto L_0x0183
            com.facebook.react.bridge.Callback r0 = r10.callback
            r3 = 3
            java.lang.Object[] r3 = new java.lang.Object[r3]
            r3[r6] = r5
            java.lang.String r5 = "path"
            r3[r2] = r5
            java.lang.String r2 = r4.getAbsolutePath()
            r3[r1] = r2
            r0.invoke(r3)
            return
        L_0x0183:
            com.RNFetchBlob.RNFetchBlobConfig r1 = r10.options
            java.lang.String r1 = r1.path
            if (r1 == 0) goto L_0x0190
            com.RNFetchBlob.RNFetchBlobConfig r0 = r10.options
            java.lang.String r0 = r0.path
            r10.destPath = r0
            goto L_0x01af
        L_0x0190:
            com.RNFetchBlob.RNFetchBlobConfig r1 = r10.options
            java.lang.Boolean r1 = r1.fileCache
            boolean r1 = r1.booleanValue()
            if (r1 == 0) goto L_0x01af
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r0 = com.RNFetchBlob.RNFetchBlobFS.getTmpPath(r0)
            r1.append(r0)
            r1.append(r3)
            java.lang.String r0 = r1.toString()
            r10.destPath = r0
        L_0x01af:
            com.RNFetchBlob.RNFetchBlobConfig r0 = r10.options     // Catch:{ Exception -> 0x044a }
            java.lang.Boolean r0 = r0.trusty     // Catch:{ Exception -> 0x044a }
            boolean r0 = r0.booleanValue()     // Catch:{ Exception -> 0x044a }
            if (r0 == 0) goto L_0x01c0
            okhttp3.OkHttpClient r0 = r10.client     // Catch:{ Exception -> 0x044a }
            okhttp3.OkHttpClient$Builder r0 = com.RNFetchBlob.RNFetchBlobUtils.getUnsafeOkHttpClient(r0)     // Catch:{ Exception -> 0x044a }
            goto L_0x01c6
        L_0x01c0:
            okhttp3.OkHttpClient r0 = r10.client     // Catch:{ Exception -> 0x044a }
            okhttp3.OkHttpClient$Builder r0 = r0.newBuilder()     // Catch:{ Exception -> 0x044a }
        L_0x01c6:
            okhttp3.Request$Builder r1 = new okhttp3.Request$Builder     // Catch:{ Exception -> 0x044a }
            r1.<init>()     // Catch:{ Exception -> 0x044a }
            java.net.URL r3 = new java.net.URL     // Catch:{ MalformedURLException -> 0x01d6 }
            java.lang.String r4 = r10.url     // Catch:{ MalformedURLException -> 0x01d6 }
            r3.<init>(r4)     // Catch:{ MalformedURLException -> 0x01d6 }
            r1.url(r3)     // Catch:{ MalformedURLException -> 0x01d6 }
            goto L_0x01da
        L_0x01d6:
            r3 = move-exception
            r3.printStackTrace()     // Catch:{ Exception -> 0x044a }
        L_0x01da:
            java.util.HashMap r3 = new java.util.HashMap     // Catch:{ Exception -> 0x044a }
            r3.<init>()     // Catch:{ Exception -> 0x044a }
            com.facebook.react.bridge.ReadableMap r4 = r10.headers     // Catch:{ Exception -> 0x044a }
            if (r4 == 0) goto L_0x022a
            com.facebook.react.bridge.ReadableMap r4 = r10.headers     // Catch:{ Exception -> 0x044a }
            com.facebook.react.bridge.ReadableMapKeySetIterator r4 = r4.keySetIterator()     // Catch:{ Exception -> 0x044a }
        L_0x01e9:
            boolean r7 = r4.hasNextKey()     // Catch:{ Exception -> 0x044a }
            if (r7 == 0) goto L_0x022a
            java.lang.String r7 = r4.nextKey()     // Catch:{ Exception -> 0x044a }
            com.facebook.react.bridge.ReadableMap r8 = r10.headers     // Catch:{ Exception -> 0x044a }
            java.lang.String r8 = r8.getString(r7)     // Catch:{ Exception -> 0x044a }
            java.lang.String r9 = "RNFB-Response"
            boolean r9 = r7.equalsIgnoreCase(r9)     // Catch:{ Exception -> 0x044a }
            if (r9 == 0) goto L_0x021b
            java.lang.String r7 = "base64"
            boolean r7 = r8.equalsIgnoreCase(r7)     // Catch:{ Exception -> 0x044a }
            if (r7 == 0) goto L_0x020e
            com.RNFetchBlob.RNFetchBlobReq$ResponseFormat r7 = com.RNFetchBlob.RNFetchBlobReq.ResponseFormat.BASE64     // Catch:{ Exception -> 0x044a }
            r10.responseFormat = r7     // Catch:{ Exception -> 0x044a }
            goto L_0x01e9
        L_0x020e:
            java.lang.String r7 = "utf8"
            boolean r7 = r8.equalsIgnoreCase(r7)     // Catch:{ Exception -> 0x044a }
            if (r7 == 0) goto L_0x01e9
            com.RNFetchBlob.RNFetchBlobReq$ResponseFormat r7 = com.RNFetchBlob.RNFetchBlobReq.ResponseFormat.UTF8     // Catch:{ Exception -> 0x044a }
            r10.responseFormat = r7     // Catch:{ Exception -> 0x044a }
            goto L_0x01e9
        L_0x021b:
            java.lang.String r9 = r7.toLowerCase()     // Catch:{ Exception -> 0x044a }
            r1.header(r9, r8)     // Catch:{ Exception -> 0x044a }
            java.lang.String r7 = r7.toLowerCase()     // Catch:{ Exception -> 0x044a }
            r3.put(r7, r8)     // Catch:{ Exception -> 0x044a }
            goto L_0x01e9
        L_0x022a:
            java.lang.String r4 = r10.method     // Catch:{ Exception -> 0x044a }
            java.lang.String r7 = "post"
            boolean r4 = r4.equalsIgnoreCase(r7)     // Catch:{ Exception -> 0x044a }
            if (r4 != 0) goto L_0x024f
            java.lang.String r4 = r10.method     // Catch:{ Exception -> 0x044a }
            java.lang.String r7 = "put"
            boolean r4 = r4.equalsIgnoreCase(r7)     // Catch:{ Exception -> 0x044a }
            if (r4 != 0) goto L_0x024f
            java.lang.String r4 = r10.method     // Catch:{ Exception -> 0x044a }
            java.lang.String r7 = "patch"
            boolean r4 = r4.equalsIgnoreCase(r7)     // Catch:{ Exception -> 0x044a }
            if (r4 == 0) goto L_0x0249
            goto L_0x024f
        L_0x0249:
            com.RNFetchBlob.RNFetchBlobReq$RequestType r4 = com.RNFetchBlob.RNFetchBlobReq.RequestType.WithoutBody     // Catch:{ Exception -> 0x044a }
            r10.requestType = r4     // Catch:{ Exception -> 0x044a }
            goto L_0x02e5
        L_0x024f:
            java.lang.String r4 = "Content-Type"
            java.lang.String r4 = r10.getHeaderIgnoreCases(r3, r4)     // Catch:{ Exception -> 0x044a }
            java.lang.String r4 = r4.toLowerCase()     // Catch:{ Exception -> 0x044a }
            com.facebook.react.bridge.ReadableArray r7 = r10.rawRequestBodyArray     // Catch:{ Exception -> 0x044a }
            if (r7 == 0) goto L_0x0262
            com.RNFetchBlob.RNFetchBlobReq$RequestType r7 = com.RNFetchBlob.RNFetchBlobReq.RequestType.Form     // Catch:{ Exception -> 0x044a }
            r10.requestType = r7     // Catch:{ Exception -> 0x044a }
            goto L_0x027b
        L_0x0262:
            boolean r7 = r4.isEmpty()     // Catch:{ Exception -> 0x044a }
            if (r7 == 0) goto L_0x027b
            java.lang.String r7 = ""
            boolean r7 = r4.equalsIgnoreCase(r7)     // Catch:{ Exception -> 0x044a }
            if (r7 != 0) goto L_0x0277
            java.lang.String r7 = "Content-Type"
            java.lang.String r8 = "application/octet-stream"
            r1.header(r7, r8)     // Catch:{ Exception -> 0x044a }
        L_0x0277:
            com.RNFetchBlob.RNFetchBlobReq$RequestType r7 = com.RNFetchBlob.RNFetchBlobReq.RequestType.SingleFile     // Catch:{ Exception -> 0x044a }
            r10.requestType = r7     // Catch:{ Exception -> 0x044a }
        L_0x027b:
            java.lang.String r7 = r10.rawRequestBody     // Catch:{ Exception -> 0x044a }
            if (r7 == 0) goto L_0x02e5
            java.lang.String r7 = r10.rawRequestBody     // Catch:{ Exception -> 0x044a }
            java.lang.String r8 = "RNFetchBlob-file://"
            boolean r7 = r7.startsWith(r8)     // Catch:{ Exception -> 0x044a }
            if (r7 != 0) goto L_0x02e1
            java.lang.String r7 = r10.rawRequestBody     // Catch:{ Exception -> 0x044a }
            java.lang.String r8 = "RNFetchBlob-content://"
            boolean r7 = r7.startsWith(r8)     // Catch:{ Exception -> 0x044a }
            if (r7 == 0) goto L_0x0294
            goto L_0x02e1
        L_0x0294:
            java.lang.String r7 = r4.toLowerCase()     // Catch:{ Exception -> 0x044a }
            java.lang.String r8 = ";base64"
            boolean r7 = r7.contains(r8)     // Catch:{ Exception -> 0x044a }
            if (r7 != 0) goto L_0x02b2
            java.lang.String r7 = r4.toLowerCase()     // Catch:{ Exception -> 0x044a }
            java.lang.String r8 = "application/octet"
            boolean r7 = r7.startsWith(r8)     // Catch:{ Exception -> 0x044a }
            if (r7 == 0) goto L_0x02ad
            goto L_0x02b2
        L_0x02ad:
            com.RNFetchBlob.RNFetchBlobReq$RequestType r4 = com.RNFetchBlob.RNFetchBlobReq.RequestType.AsIs     // Catch:{ Exception -> 0x044a }
            r10.requestType = r4     // Catch:{ Exception -> 0x044a }
            goto L_0x02e5
        L_0x02b2:
            java.lang.String r7 = ";base64"
            java.lang.String r8 = ""
            java.lang.String r4 = r4.replace(r7, r8)     // Catch:{ Exception -> 0x044a }
            java.lang.String r7 = ";BASE64"
            java.lang.String r8 = ""
            java.lang.String r4 = r4.replace(r7, r8)     // Catch:{ Exception -> 0x044a }
            java.lang.String r7 = "content-type"
            boolean r7 = r3.containsKey(r7)     // Catch:{ Exception -> 0x044a }
            if (r7 == 0) goto L_0x02cf
            java.lang.String r7 = "content-type"
            r3.put(r7, r4)     // Catch:{ Exception -> 0x044a }
        L_0x02cf:
            java.lang.String r7 = "Content-Type"
            boolean r7 = r3.containsKey(r7)     // Catch:{ Exception -> 0x044a }
            if (r7 == 0) goto L_0x02dc
            java.lang.String r7 = "Content-Type"
            r3.put(r7, r4)     // Catch:{ Exception -> 0x044a }
        L_0x02dc:
            com.RNFetchBlob.RNFetchBlobReq$RequestType r4 = com.RNFetchBlob.RNFetchBlobReq.RequestType.SingleFile     // Catch:{ Exception -> 0x044a }
            r10.requestType = r4     // Catch:{ Exception -> 0x044a }
            goto L_0x02e5
        L_0x02e1:
            com.RNFetchBlob.RNFetchBlobReq$RequestType r4 = com.RNFetchBlob.RNFetchBlobReq.RequestType.SingleFile     // Catch:{ Exception -> 0x044a }
            r10.requestType = r4     // Catch:{ Exception -> 0x044a }
        L_0x02e5:
            java.lang.String r4 = "Transfer-Encoding"
            java.lang.String r4 = r10.getHeaderIgnoreCases(r3, r4)     // Catch:{ Exception -> 0x044a }
            java.lang.String r7 = "chunked"
            boolean r4 = r4.equalsIgnoreCase(r7)     // Catch:{ Exception -> 0x044a }
            int[] r7 = com.RNFetchBlob.RNFetchBlobReq.C05884.$SwitchMap$com$RNFetchBlob$RNFetchBlobReq$RequestType     // Catch:{ Exception -> 0x044a }
            com.RNFetchBlob.RNFetchBlobReq$RequestType r8 = r10.requestType     // Catch:{ Exception -> 0x044a }
            int r8 = r8.ordinal()     // Catch:{ Exception -> 0x044a }
            r7 = r7[r8]     // Catch:{ Exception -> 0x044a }
            switch(r7) {
                case 1: goto L_0x03af;
                case 2: goto L_0x0380;
                case 3: goto L_0x0333;
                case 4: goto L_0x0300;
                default: goto L_0x02fe;
            }     // Catch:{ Exception -> 0x044a }
        L_0x02fe:
            goto L_0x03dd
        L_0x0300:
            java.lang.String r3 = r10.method     // Catch:{ Exception -> 0x044a }
            java.lang.String r4 = "post"
            boolean r3 = r3.equalsIgnoreCase(r4)     // Catch:{ Exception -> 0x044a }
            if (r3 != 0) goto L_0x0326
            java.lang.String r3 = r10.method     // Catch:{ Exception -> 0x044a }
            java.lang.String r4 = "put"
            boolean r3 = r3.equalsIgnoreCase(r4)     // Catch:{ Exception -> 0x044a }
            if (r3 != 0) goto L_0x0326
            java.lang.String r3 = r10.method     // Catch:{ Exception -> 0x044a }
            java.lang.String r4 = "patch"
            boolean r3 = r3.equalsIgnoreCase(r4)     // Catch:{ Exception -> 0x044a }
            if (r3 == 0) goto L_0x031f
            goto L_0x0326
        L_0x031f:
            java.lang.String r3 = r10.method     // Catch:{ Exception -> 0x044a }
            r1.method(r3, r5)     // Catch:{ Exception -> 0x044a }
            goto L_0x03dd
        L_0x0326:
            java.lang.String r3 = r10.method     // Catch:{ Exception -> 0x044a }
            byte[] r4 = new byte[r6]     // Catch:{ Exception -> 0x044a }
            okhttp3.RequestBody r4 = okhttp3.RequestBody.create(r5, r4)     // Catch:{ Exception -> 0x044a }
            r1.method(r3, r4)     // Catch:{ Exception -> 0x044a }
            goto L_0x03dd
        L_0x0333:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x044a }
            r3.<init>()     // Catch:{ Exception -> 0x044a }
            java.lang.String r5 = "RNFetchBlob-"
            r3.append(r5)     // Catch:{ Exception -> 0x044a }
            java.lang.String r5 = r10.taskId     // Catch:{ Exception -> 0x044a }
            r3.append(r5)     // Catch:{ Exception -> 0x044a }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x044a }
            com.RNFetchBlob.RNFetchBlobBody r5 = new com.RNFetchBlob.RNFetchBlobBody     // Catch:{ Exception -> 0x044a }
            java.lang.String r7 = r10.taskId     // Catch:{ Exception -> 0x044a }
            r5.<init>(r7)     // Catch:{ Exception -> 0x044a }
            com.RNFetchBlob.RNFetchBlobBody r4 = r5.chunkedEncoding(r4)     // Catch:{ Exception -> 0x044a }
            com.RNFetchBlob.RNFetchBlobReq$RequestType r5 = r10.requestType     // Catch:{ Exception -> 0x044a }
            com.RNFetchBlob.RNFetchBlobBody r4 = r4.setRequestType(r5)     // Catch:{ Exception -> 0x044a }
            com.facebook.react.bridge.ReadableArray r5 = r10.rawRequestBodyArray     // Catch:{ Exception -> 0x044a }
            com.RNFetchBlob.RNFetchBlobBody r4 = r4.setBody(r5)     // Catch:{ Exception -> 0x044a }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x044a }
            r5.<init>()     // Catch:{ Exception -> 0x044a }
            java.lang.String r7 = "multipart/form-data; boundary="
            r5.append(r7)     // Catch:{ Exception -> 0x044a }
            r5.append(r3)     // Catch:{ Exception -> 0x044a }
            java.lang.String r3 = r5.toString()     // Catch:{ Exception -> 0x044a }
            okhttp3.MediaType r3 = okhttp3.MediaType.parse(r3)     // Catch:{ Exception -> 0x044a }
            com.RNFetchBlob.RNFetchBlobBody r3 = r4.setMIME(r3)     // Catch:{ Exception -> 0x044a }
            r10.requestBody = r3     // Catch:{ Exception -> 0x044a }
            java.lang.String r3 = r10.method     // Catch:{ Exception -> 0x044a }
            com.RNFetchBlob.RNFetchBlobBody r4 = r10.requestBody     // Catch:{ Exception -> 0x044a }
            r1.method(r3, r4)     // Catch:{ Exception -> 0x044a }
            goto L_0x03dd
        L_0x0380:
            com.RNFetchBlob.RNFetchBlobBody r5 = new com.RNFetchBlob.RNFetchBlobBody     // Catch:{ Exception -> 0x044a }
            java.lang.String r7 = r10.taskId     // Catch:{ Exception -> 0x044a }
            r5.<init>(r7)     // Catch:{ Exception -> 0x044a }
            com.RNFetchBlob.RNFetchBlobBody r4 = r5.chunkedEncoding(r4)     // Catch:{ Exception -> 0x044a }
            com.RNFetchBlob.RNFetchBlobReq$RequestType r5 = r10.requestType     // Catch:{ Exception -> 0x044a }
            com.RNFetchBlob.RNFetchBlobBody r4 = r4.setRequestType(r5)     // Catch:{ Exception -> 0x044a }
            java.lang.String r5 = r10.rawRequestBody     // Catch:{ Exception -> 0x044a }
            com.RNFetchBlob.RNFetchBlobBody r4 = r4.setBody(r5)     // Catch:{ Exception -> 0x044a }
            java.lang.String r5 = "content-type"
            java.lang.String r3 = r10.getHeaderIgnoreCases(r3, r5)     // Catch:{ Exception -> 0x044a }
            okhttp3.MediaType r3 = okhttp3.MediaType.parse(r3)     // Catch:{ Exception -> 0x044a }
            com.RNFetchBlob.RNFetchBlobBody r3 = r4.setMIME(r3)     // Catch:{ Exception -> 0x044a }
            r10.requestBody = r3     // Catch:{ Exception -> 0x044a }
            java.lang.String r3 = r10.method     // Catch:{ Exception -> 0x044a }
            com.RNFetchBlob.RNFetchBlobBody r4 = r10.requestBody     // Catch:{ Exception -> 0x044a }
            r1.method(r3, r4)     // Catch:{ Exception -> 0x044a }
            goto L_0x03dd
        L_0x03af:
            com.RNFetchBlob.RNFetchBlobBody r5 = new com.RNFetchBlob.RNFetchBlobBody     // Catch:{ Exception -> 0x044a }
            java.lang.String r7 = r10.taskId     // Catch:{ Exception -> 0x044a }
            r5.<init>(r7)     // Catch:{ Exception -> 0x044a }
            com.RNFetchBlob.RNFetchBlobBody r4 = r5.chunkedEncoding(r4)     // Catch:{ Exception -> 0x044a }
            com.RNFetchBlob.RNFetchBlobReq$RequestType r5 = r10.requestType     // Catch:{ Exception -> 0x044a }
            com.RNFetchBlob.RNFetchBlobBody r4 = r4.setRequestType(r5)     // Catch:{ Exception -> 0x044a }
            java.lang.String r5 = r10.rawRequestBody     // Catch:{ Exception -> 0x044a }
            com.RNFetchBlob.RNFetchBlobBody r4 = r4.setBody(r5)     // Catch:{ Exception -> 0x044a }
            java.lang.String r5 = "content-type"
            java.lang.String r3 = r10.getHeaderIgnoreCases(r3, r5)     // Catch:{ Exception -> 0x044a }
            okhttp3.MediaType r3 = okhttp3.MediaType.parse(r3)     // Catch:{ Exception -> 0x044a }
            com.RNFetchBlob.RNFetchBlobBody r3 = r4.setMIME(r3)     // Catch:{ Exception -> 0x044a }
            r10.requestBody = r3     // Catch:{ Exception -> 0x044a }
            java.lang.String r3 = r10.method     // Catch:{ Exception -> 0x044a }
            com.RNFetchBlob.RNFetchBlobBody r4 = r10.requestBody     // Catch:{ Exception -> 0x044a }
            r1.method(r3, r4)     // Catch:{ Exception -> 0x044a }
        L_0x03dd:
            okhttp3.Request r1 = r1.build()     // Catch:{ Exception -> 0x044a }
            com.RNFetchBlob.RNFetchBlobReq$1 r3 = new com.RNFetchBlob.RNFetchBlobReq$1     // Catch:{ Exception -> 0x044a }
            r3.<init>()     // Catch:{ Exception -> 0x044a }
            r0.addNetworkInterceptor(r3)     // Catch:{ Exception -> 0x044a }
            com.RNFetchBlob.RNFetchBlobReq$2 r3 = new com.RNFetchBlob.RNFetchBlobReq$2     // Catch:{ Exception -> 0x044a }
            r3.<init>(r1)     // Catch:{ Exception -> 0x044a }
            r0.addInterceptor(r3)     // Catch:{ Exception -> 0x044a }
            com.RNFetchBlob.RNFetchBlobConfig r3 = r10.options     // Catch:{ Exception -> 0x044a }
            long r3 = r3.timeout     // Catch:{ Exception -> 0x044a }
            r7 = 0
            int r5 = (r3 > r7 ? 1 : (r3 == r7 ? 0 : -1))
            if (r5 < 0) goto L_0x040d
            com.RNFetchBlob.RNFetchBlobConfig r3 = r10.options     // Catch:{ Exception -> 0x044a }
            long r3 = r3.timeout     // Catch:{ Exception -> 0x044a }
            java.util.concurrent.TimeUnit r5 = java.util.concurrent.TimeUnit.MILLISECONDS     // Catch:{ Exception -> 0x044a }
            r0.connectTimeout(r3, r5)     // Catch:{ Exception -> 0x044a }
            com.RNFetchBlob.RNFetchBlobConfig r3 = r10.options     // Catch:{ Exception -> 0x044a }
            long r3 = r3.timeout     // Catch:{ Exception -> 0x044a }
            java.util.concurrent.TimeUnit r5 = java.util.concurrent.TimeUnit.MILLISECONDS     // Catch:{ Exception -> 0x044a }
            r0.readTimeout(r3, r5)     // Catch:{ Exception -> 0x044a }
        L_0x040d:
            okhttp3.ConnectionPool r3 = pool     // Catch:{ Exception -> 0x044a }
            r0.connectionPool(r3)     // Catch:{ Exception -> 0x044a }
            r0.retryOnConnectionFailure(r6)     // Catch:{ Exception -> 0x044a }
            com.RNFetchBlob.RNFetchBlobConfig r3 = r10.options     // Catch:{ Exception -> 0x044a }
            java.lang.Boolean r3 = r3.followRedirect     // Catch:{ Exception -> 0x044a }
            boolean r3 = r3.booleanValue()     // Catch:{ Exception -> 0x044a }
            r0.followRedirects(r3)     // Catch:{ Exception -> 0x044a }
            com.RNFetchBlob.RNFetchBlobConfig r3 = r10.options     // Catch:{ Exception -> 0x044a }
            java.lang.Boolean r3 = r3.followRedirect     // Catch:{ Exception -> 0x044a }
            boolean r3 = r3.booleanValue()     // Catch:{ Exception -> 0x044a }
            r0.followSslRedirects(r3)     // Catch:{ Exception -> 0x044a }
            r0.retryOnConnectionFailure(r2)     // Catch:{ Exception -> 0x044a }
            okhttp3.OkHttpClient$Builder r0 = enableTls12OnPreLollipop(r0)     // Catch:{ Exception -> 0x044a }
            okhttp3.OkHttpClient r0 = r0.build()     // Catch:{ Exception -> 0x044a }
            okhttp3.Call r0 = r0.newCall(r1)     // Catch:{ Exception -> 0x044a }
            java.util.HashMap<java.lang.String, okhttp3.Call> r1 = taskTable     // Catch:{ Exception -> 0x044a }
            java.lang.String r3 = r10.taskId     // Catch:{ Exception -> 0x044a }
            r1.put(r3, r0)     // Catch:{ Exception -> 0x044a }
            com.RNFetchBlob.RNFetchBlobReq$3 r1 = new com.RNFetchBlob.RNFetchBlobReq$3     // Catch:{ Exception -> 0x044a }
            r1.<init>()     // Catch:{ Exception -> 0x044a }
            r0.enqueue(r1)     // Catch:{ Exception -> 0x044a }
            goto L_0x0476
        L_0x044a:
            r0 = move-exception
            r0.printStackTrace()
            r10.releaseTaskResource()
            com.facebook.react.bridge.Callback r1 = r10.callback
            java.lang.Object[] r2 = new java.lang.Object[r2]
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "RNFetchBlob request error: "
            r3.append(r4)
            java.lang.String r4 = r0.getMessage()
            r3.append(r4)
            java.lang.Throwable r0 = r0.getCause()
            r3.append(r0)
            java.lang.String r0 = r3.toString()
            r2[r6] = r0
            r1.invoke(r2)
        L_0x0476:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.RNFetchBlob.RNFetchBlobReq.run():void");
    }

    /* access modifiers changed from: private */
    public void releaseTaskResource() {
        if (taskTable.containsKey(this.taskId)) {
            taskTable.remove(this.taskId);
        }
        if (androidDownloadManagerTaskTable.containsKey(this.taskId)) {
            androidDownloadManagerTaskTable.remove(this.taskId);
        }
        if (uploadProgressReport.containsKey(this.taskId)) {
            uploadProgressReport.remove(this.taskId);
        }
        if (progressReport.containsKey(this.taskId)) {
            progressReport.remove(this.taskId);
        }
        RNFetchBlobBody rNFetchBlobBody = this.requestBody;
        if (rNFetchBlobBody != null) {
            rNFetchBlobBody.clearRequestBody();
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Can't wrap try/catch for region: R(5:21|22|23|24|(1:26)(1:27)) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:23:0x00e0 */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x00e6 A[Catch:{ IOException -> 0x010c }] */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x00f8 A[Catch:{ IOException -> 0x010c }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void done(okhttp3.C1512Response r12) {
        /*
            r11 = this;
            boolean r0 = r11.isBlobResponse(r12)
            com.facebook.react.bridge.WritableMap r1 = r11.getResponseInfo(r12, r0)
            r11.emitStateEvent(r1)
            int[] r1 = com.RNFetchBlob.RNFetchBlobReq.C05884.$SwitchMap$com$RNFetchBlob$RNFetchBlobReq$ResponseType
            com.RNFetchBlob.RNFetchBlobReq$ResponseType r2 = r11.responseType
            int r2 = r2.ordinal()
            r1 = r1[r2]
            r2 = 3
            r3 = 0
            r4 = 1
            r5 = 0
            r6 = 2
            switch(r1) {
                case 1: goto L_0x0047;
                case 2: goto L_0x0021;
                default: goto L_0x001d;
            }
        L_0x001d:
            com.facebook.react.bridge.Callback r0 = r11.callback     // Catch:{ IOException -> 0x0137 }
            goto L_0x011a
        L_0x0021:
            okhttp3.ResponseBody r0 = r12.body()     // Catch:{ Exception -> 0x0028 }
            r0.bytes()     // Catch:{ Exception -> 0x0028 }
        L_0x0028:
            java.lang.String r0 = r11.destPath
            java.lang.String r1 = "?append=true"
            java.lang.String r7 = ""
            java.lang.String r0 = r0.replace(r1, r7)
            r11.destPath = r0
            com.facebook.react.bridge.Callback r0 = r11.callback
            java.lang.Object[] r1 = new java.lang.Object[r2]
            r1[r5] = r3
            java.lang.String r2 = "path"
            r1[r4] = r2
            java.lang.String r2 = r11.destPath
            r1[r6] = r2
            r0.invoke(r1)
            goto L_0x0144
        L_0x0047:
            if (r0 == 0) goto L_0x0094
            com.RNFetchBlob.RNFetchBlobConfig r0 = r11.options     // Catch:{ IOException -> 0x010c }
            java.lang.Boolean r0 = r0.auto     // Catch:{ IOException -> 0x010c }
            boolean r0 = r0.booleanValue()     // Catch:{ IOException -> 0x010c }
            if (r0 == 0) goto L_0x0094
            java.lang.String r0 = r11.taskId     // Catch:{ IOException -> 0x010c }
            java.lang.String r0 = com.RNFetchBlob.RNFetchBlobFS.getTmpPath(r0)     // Catch:{ IOException -> 0x010c }
            okhttp3.ResponseBody r1 = r12.body()     // Catch:{ IOException -> 0x010c }
            java.io.InputStream r1 = r1.byteStream()     // Catch:{ IOException -> 0x010c }
            java.io.FileOutputStream r7 = new java.io.FileOutputStream     // Catch:{ IOException -> 0x010c }
            java.io.File r8 = new java.io.File     // Catch:{ IOException -> 0x010c }
            r8.<init>(r0)     // Catch:{ IOException -> 0x010c }
            r7.<init>(r8)     // Catch:{ IOException -> 0x010c }
            r8 = 10240(0x2800, float:1.4349E-41)
            byte[] r8 = new byte[r8]     // Catch:{ IOException -> 0x010c }
        L_0x006f:
            int r9 = r1.read(r8)     // Catch:{ IOException -> 0x010c }
            r10 = -1
            if (r9 == r10) goto L_0x007a
            r7.write(r8, r5, r9)     // Catch:{ IOException -> 0x010c }
            goto L_0x006f
        L_0x007a:
            r1.close()     // Catch:{ IOException -> 0x010c }
            r7.flush()     // Catch:{ IOException -> 0x010c }
            r7.close()     // Catch:{ IOException -> 0x010c }
            com.facebook.react.bridge.Callback r1 = r11.callback     // Catch:{ IOException -> 0x010c }
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ IOException -> 0x010c }
            r2[r5] = r3     // Catch:{ IOException -> 0x010c }
            java.lang.String r7 = "path"
            r2[r4] = r7     // Catch:{ IOException -> 0x010c }
            r2[r6] = r0     // Catch:{ IOException -> 0x010c }
            r1.invoke(r2)     // Catch:{ IOException -> 0x010c }
            goto L_0x0144
        L_0x0094:
            okhttp3.ResponseBody r0 = r12.body()     // Catch:{ IOException -> 0x010c }
            byte[] r0 = r0.bytes()     // Catch:{ IOException -> 0x010c }
            java.lang.String r1 = "UTF-8"
            java.nio.charset.Charset r1 = java.nio.charset.Charset.forName(r1)     // Catch:{ IOException -> 0x010c }
            java.nio.charset.CharsetEncoder r1 = r1.newEncoder()     // Catch:{ IOException -> 0x010c }
            com.RNFetchBlob.RNFetchBlobReq$ResponseFormat r7 = r11.responseFormat     // Catch:{ IOException -> 0x010c }
            com.RNFetchBlob.RNFetchBlobReq$ResponseFormat r8 = com.RNFetchBlob.RNFetchBlobReq.ResponseFormat.BASE64     // Catch:{ IOException -> 0x010c }
            if (r7 != r8) goto L_0x00c0
            com.facebook.react.bridge.Callback r1 = r11.callback     // Catch:{ IOException -> 0x010c }
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ IOException -> 0x010c }
            r2[r5] = r3     // Catch:{ IOException -> 0x010c }
            java.lang.String r7 = "base64"
            r2[r4] = r7     // Catch:{ IOException -> 0x010c }
            java.lang.String r0 = android.util.Base64.encodeToString(r0, r6)     // Catch:{ IOException -> 0x010c }
            r2[r6] = r0     // Catch:{ IOException -> 0x010c }
            r1.invoke(r2)     // Catch:{ IOException -> 0x010c }
            return
        L_0x00c0:
            java.nio.ByteBuffer r7 = java.nio.ByteBuffer.wrap(r0)     // Catch:{ CharacterCodingException -> 0x00e0 }
            java.nio.CharBuffer r7 = r7.asCharBuffer()     // Catch:{ CharacterCodingException -> 0x00e0 }
            r1.encode(r7)     // Catch:{ CharacterCodingException -> 0x00e0 }
            java.lang.String r1 = new java.lang.String     // Catch:{ CharacterCodingException -> 0x00e0 }
            r1.<init>(r0)     // Catch:{ CharacterCodingException -> 0x00e0 }
            com.facebook.react.bridge.Callback r7 = r11.callback     // Catch:{ CharacterCodingException -> 0x00e0 }
            java.lang.Object[] r8 = new java.lang.Object[r2]     // Catch:{ CharacterCodingException -> 0x00e0 }
            r8[r5] = r3     // Catch:{ CharacterCodingException -> 0x00e0 }
            java.lang.String r9 = "utf8"
            r8[r4] = r9     // Catch:{ CharacterCodingException -> 0x00e0 }
            r8[r6] = r1     // Catch:{ CharacterCodingException -> 0x00e0 }
            r7.invoke(r8)     // Catch:{ CharacterCodingException -> 0x00e0 }
            goto L_0x0144
        L_0x00e0:
            com.RNFetchBlob.RNFetchBlobReq$ResponseFormat r1 = r11.responseFormat     // Catch:{ IOException -> 0x010c }
            com.RNFetchBlob.RNFetchBlobReq$ResponseFormat r7 = com.RNFetchBlob.RNFetchBlobReq.ResponseFormat.UTF8     // Catch:{ IOException -> 0x010c }
            if (r1 != r7) goto L_0x00f8
            com.facebook.react.bridge.Callback r0 = r11.callback     // Catch:{ IOException -> 0x010c }
            java.lang.Object[] r1 = new java.lang.Object[r2]     // Catch:{ IOException -> 0x010c }
            r1[r5] = r3     // Catch:{ IOException -> 0x010c }
            java.lang.String r2 = "utf8"
            r1[r4] = r2     // Catch:{ IOException -> 0x010c }
            java.lang.String r2 = ""
            r1[r6] = r2     // Catch:{ IOException -> 0x010c }
            r0.invoke(r1)     // Catch:{ IOException -> 0x010c }
            goto L_0x0144
        L_0x00f8:
            com.facebook.react.bridge.Callback r1 = r11.callback     // Catch:{ IOException -> 0x010c }
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ IOException -> 0x010c }
            r2[r5] = r3     // Catch:{ IOException -> 0x010c }
            java.lang.String r7 = "base64"
            r2[r4] = r7     // Catch:{ IOException -> 0x010c }
            java.lang.String r0 = android.util.Base64.encodeToString(r0, r6)     // Catch:{ IOException -> 0x010c }
            r2[r6] = r0     // Catch:{ IOException -> 0x010c }
            r1.invoke(r2)     // Catch:{ IOException -> 0x010c }
            goto L_0x0144
        L_0x010c:
            com.facebook.react.bridge.Callback r0 = r11.callback
            java.lang.Object[] r1 = new java.lang.Object[r6]
            java.lang.String r2 = "RNFetchBlob failed to encode response data to BASE64 string."
            r1[r5] = r2
            r1[r4] = r3
            r0.invoke(r1)
            goto L_0x0144
        L_0x011a:
            java.lang.Object[] r1 = new java.lang.Object[r2]     // Catch:{ IOException -> 0x0137 }
            r1[r5] = r3     // Catch:{ IOException -> 0x0137 }
            java.lang.String r2 = "utf8"
            r1[r4] = r2     // Catch:{ IOException -> 0x0137 }
            java.lang.String r2 = new java.lang.String     // Catch:{ IOException -> 0x0137 }
            okhttp3.ResponseBody r7 = r12.body()     // Catch:{ IOException -> 0x0137 }
            byte[] r7 = r7.bytes()     // Catch:{ IOException -> 0x0137 }
            java.lang.String r8 = "UTF-8"
            r2.<init>(r7, r8)     // Catch:{ IOException -> 0x0137 }
            r1[r6] = r2     // Catch:{ IOException -> 0x0137 }
            r0.invoke(r1)     // Catch:{ IOException -> 0x0137 }
            goto L_0x0144
        L_0x0137:
            com.facebook.react.bridge.Callback r0 = r11.callback
            java.lang.Object[] r1 = new java.lang.Object[r6]
            java.lang.String r2 = "RNFetchBlob failed to encode response data to UTF8 string."
            r1[r5] = r2
            r1[r4] = r3
            r0.invoke(r1)
        L_0x0144:
            okhttp3.ResponseBody r12 = r12.body()
            r12.close()
            r11.releaseTaskResource()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.RNFetchBlob.RNFetchBlobReq.done(okhttp3.Response):void");
    }

    public static RNFetchBlobProgressConfig getReportProgress(String str) {
        if (!progressReport.containsKey(str)) {
            return null;
        }
        return (RNFetchBlobProgressConfig) progressReport.get(str);
    }

    public static RNFetchBlobProgressConfig getReportUploadProgress(String str) {
        if (!uploadProgressReport.containsKey(str)) {
            return null;
        }
        return (RNFetchBlobProgressConfig) uploadProgressReport.get(str);
    }

    private WritableMap getResponseInfo(C1512Response response, boolean z) {
        WritableMap createMap = Arguments.createMap();
        createMap.putInt(NotificationCompat.CATEGORY_STATUS, response.code());
        createMap.putString("state", "2");
        createMap.putString("taskId", this.taskId);
        createMap.putBoolean("timeout", this.timeout);
        WritableMap createMap2 = Arguments.createMap();
        for (int i = 0; i < response.headers().size(); i++) {
            createMap2.putString(response.headers().name(i), response.headers().value(i));
        }
        WritableArray createArray = Arguments.createArray();
        Iterator it = this.redirects.iterator();
        while (it.hasNext()) {
            createArray.pushString((String) it.next());
        }
        createMap.putArray("redirects", createArray);
        createMap.putMap("headers", createMap2);
        Headers headers2 = response.headers();
        if (z) {
            createMap.putString("respType", "blob");
        } else if (getHeaderIgnoreCases(headers2, "content-type").equalsIgnoreCase("text/")) {
            createMap.putString("respType", "text");
        } else if (getHeaderIgnoreCases(headers2, "content-type").contains("application/json")) {
            createMap.putString("respType", "json");
        } else {
            createMap.putString("respType", "");
        }
        return createMap;
    }

    private boolean isBlobResponse(C1512Response response) {
        boolean z;
        String headerIgnoreCases = getHeaderIgnoreCases(response.headers(), "Content-Type");
        boolean z2 = !headerIgnoreCases.equalsIgnoreCase("text/");
        boolean z3 = !headerIgnoreCases.equalsIgnoreCase("application/json");
        if (this.options.binaryContentTypes != null) {
            int i = 0;
            while (true) {
                if (i >= this.options.binaryContentTypes.size()) {
                    break;
                } else if (headerIgnoreCases.toLowerCase().contains(this.options.binaryContentTypes.getString(i).toLowerCase())) {
                    z = true;
                    break;
                } else {
                    i++;
                }
            }
            if ((!z3 || z2) && !z) {
                return false;
            }
            return true;
        }
        z = false;
        if (!z3) {
        }
        return false;
    }

    private String getHeaderIgnoreCases(Headers headers2, String str) {
        String str2 = headers2.get(str);
        if (str2 != null) {
            return str2;
        }
        return headers2.get(str.toLowerCase()) == null ? "" : headers2.get(str.toLowerCase());
    }

    private String getHeaderIgnoreCases(HashMap<String, String> hashMap, String str) {
        String str2 = (String) hashMap.get(str);
        if (str2 != null) {
            return str2;
        }
        String str3 = (String) hashMap.get(str.toLowerCase());
        if (str3 == null) {
            str3 = "";
        }
        return str3;
    }

    private void emitStateEvent(WritableMap writableMap) {
        ((RCTDeviceEventEmitter) C0568RNFetchBlob.RCTContext.getJSModule(RCTDeviceEventEmitter.class)).emit(RNFetchBlobConst.EVENT_HTTP_STATE, writableMap);
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x00dd A[SYNTHETIC, Splitter:B:22:0x00dd] */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x011e  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onReceive(android.content.Context r13, android.content.Intent r14) {
        /*
            r12 = this;
            java.lang.String r13 = r14.getAction()
            java.lang.String r0 = "android.intent.action.DOWNLOAD_COMPLETE"
            boolean r13 = r0.equals(r13)
            if (r13 == 0) goto L_0x0141
            com.facebook.react.bridge.ReactApplicationContext r13 = com.RNFetchBlob.C0568RNFetchBlob.RCTContext
            android.content.Context r13 = r13.getApplicationContext()
            android.os.Bundle r14 = r14.getExtras()
            java.lang.String r0 = "extra_download_id"
            long r0 = r14.getLong(r0)
            long r2 = r12.downloadManagerId
            int r14 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r14 != 0) goto L_0x0141
            r12.releaseTaskResource()
            android.app.DownloadManager$Query r14 = new android.app.DownloadManager$Query
            r14.<init>()
            r0 = 1
            long[] r1 = new long[r0]
            long r2 = r12.downloadManagerId
            r4 = 0
            r1[r4] = r2
            r14.setFilterById(r1)
            java.lang.String r1 = "download"
            java.lang.Object r1 = r13.getSystemService(r1)
            android.app.DownloadManager r1 = (android.app.DownloadManager) r1
            r1.query(r14)
            android.database.Cursor r14 = r1.query(r14)
            boolean r1 = r14.moveToFirst()
            r2 = 3
            r3 = 2
            r5 = 0
            if (r1 == 0) goto L_0x00d0
            java.lang.String r1 = "status"
            int r1 = r14.getColumnIndex(r1)
            int r1 = r14.getInt(r1)
            r6 = 16
            if (r1 != r6) goto L_0x0084
            com.facebook.react.bridge.Callback r13 = r12.callback
            java.lang.Object[] r14 = new java.lang.Object[r2]
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r6 = "Download manager failed to download from  "
            r2.append(r6)
            java.lang.String r6 = r12.url
            r2.append(r6)
            java.lang.String r6 = ". Status Code = "
            r2.append(r6)
            r2.append(r1)
            java.lang.String r1 = r2.toString()
            r14[r4] = r1
            r14[r0] = r5
            r14[r3] = r5
            r13.invoke(r14)
            return
        L_0x0084:
            java.lang.String r1 = "local_uri"
            int r1 = r14.getColumnIndex(r1)
            java.lang.String r14 = r14.getString(r1)
            if (r14 == 0) goto L_0x00d0
            com.RNFetchBlob.RNFetchBlobConfig r1 = r12.options
            com.facebook.react.bridge.ReadableMap r1 = r1.addAndroidDownloads
            java.lang.String r6 = "mime"
            boolean r1 = r1.hasKey(r6)
            if (r1 == 0) goto L_0x00d0
            com.RNFetchBlob.RNFetchBlobConfig r1 = r12.options
            com.facebook.react.bridge.ReadableMap r1 = r1.addAndroidDownloads
            java.lang.String r6 = "mime"
            java.lang.String r1 = r1.getString(r6)
            java.lang.String r6 = "image"
            boolean r1 = r1.contains(r6)
            if (r1 == 0) goto L_0x00d0
            android.net.Uri r7 = android.net.Uri.parse(r14)
            android.content.ContentResolver r6 = r13.getContentResolver()
            java.lang.String[] r8 = new java.lang.String[r0]
            java.lang.String r13 = "_data"
            r8[r4] = r13
            r9 = 0
            r10 = 0
            r11 = 0
            android.database.Cursor r13 = r6.query(r7, r8, r9, r10, r11)
            if (r13 == 0) goto L_0x00d0
            r13.moveToFirst()
            java.lang.String r14 = r13.getString(r4)
            r13.close()
            goto L_0x00d1
        L_0x00d0:
            r14 = r5
        L_0x00d1:
            com.RNFetchBlob.RNFetchBlobConfig r13 = r12.options
            com.facebook.react.bridge.ReadableMap r13 = r13.addAndroidDownloads
            java.lang.String r1 = "path"
            boolean r13 = r13.hasKey(r1)
            if (r13 == 0) goto L_0x011e
            com.RNFetchBlob.RNFetchBlobConfig r13 = r12.options     // Catch:{ Exception -> 0x010a }
            com.facebook.react.bridge.ReadableMap r13 = r13.addAndroidDownloads     // Catch:{ Exception -> 0x010a }
            java.lang.String r14 = "path"
            java.lang.String r13 = r13.getString(r14)     // Catch:{ Exception -> 0x010a }
            java.io.File r14 = new java.io.File     // Catch:{ Exception -> 0x010a }
            r14.<init>(r13)     // Catch:{ Exception -> 0x010a }
            boolean r14 = r14.exists()     // Catch:{ Exception -> 0x010a }
            if (r14 == 0) goto L_0x0102
            com.facebook.react.bridge.Callback r14 = r12.callback     // Catch:{ Exception -> 0x010a }
            java.lang.Object[] r1 = new java.lang.Object[r2]     // Catch:{ Exception -> 0x010a }
            r1[r4] = r5     // Catch:{ Exception -> 0x010a }
            java.lang.String r2 = "path"
            r1[r0] = r2     // Catch:{ Exception -> 0x010a }
            r1[r3] = r13     // Catch:{ Exception -> 0x010a }
            r14.invoke(r1)     // Catch:{ Exception -> 0x010a }
            goto L_0x0141
        L_0x0102:
            java.lang.Exception r13 = new java.lang.Exception     // Catch:{ Exception -> 0x010a }
            java.lang.String r14 = "Download manager download failed, the file does not downloaded to destination."
            r13.<init>(r14)     // Catch:{ Exception -> 0x010a }
            throw r13     // Catch:{ Exception -> 0x010a }
        L_0x010a:
            r13 = move-exception
            r13.printStackTrace()
            com.facebook.react.bridge.Callback r14 = r12.callback
            java.lang.Object[] r1 = new java.lang.Object[r3]
            java.lang.String r13 = r13.getLocalizedMessage()
            r1[r4] = r13
            r1[r0] = r5
            r14.invoke(r1)
            goto L_0x0141
        L_0x011e:
            if (r14 != 0) goto L_0x0132
            com.facebook.react.bridge.Callback r13 = r12.callback
            java.lang.Object[] r14 = new java.lang.Object[r2]
            java.lang.String r1 = "Download manager could not resolve downloaded file path."
            r14[r4] = r1
            java.lang.String r1 = "path"
            r14[r0] = r1
            r14[r3] = r5
            r13.invoke(r14)
            goto L_0x0141
        L_0x0132:
            com.facebook.react.bridge.Callback r13 = r12.callback
            java.lang.Object[] r1 = new java.lang.Object[r2]
            r1[r4] = r5
            java.lang.String r2 = "path"
            r1[r0] = r2
            r1[r3] = r14
            r13.invoke(r1)
        L_0x0141:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.RNFetchBlob.RNFetchBlobReq.onReceive(android.content.Context, android.content.Intent):void");
    }

    public static Builder enableTls12OnPreLollipop(Builder builder) {
        if (VERSION.SDK_INT >= 16 && VERSION.SDK_INT <= 19) {
            try {
                TrustManagerFactory instance = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                instance.init(null);
                TrustManager[] trustManagers = instance.getTrustManagers();
                if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unexpected default trust managers:");
                    sb.append(Arrays.toString(trustManagers));
                    throw new IllegalStateException(sb.toString());
                }
                X509TrustManager x509TrustManager = (X509TrustManager) trustManagers[0];
                SSLContext instance2 = SSLContext.getInstance("SSL");
                instance2.init(null, new TrustManager[]{x509TrustManager}, null);
                builder.sslSocketFactory(instance2.getSocketFactory(), x509TrustManager);
                ConnectionSpec build = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS).tlsVersions(TlsVersion.TLS_1_2).build();
                ArrayList arrayList = new ArrayList();
                arrayList.add(build);
                arrayList.add(ConnectionSpec.COMPATIBLE_TLS);
                arrayList.add(ConnectionSpec.CLEARTEXT);
                builder.connectionSpecs(arrayList);
            } catch (Exception e) {
                FLog.m49e("OkHttpClientProvider", "Error while enabling TLS 1.2", (Throwable) e);
            }
        }
        return builder;
    }
}

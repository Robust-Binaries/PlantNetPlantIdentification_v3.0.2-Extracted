package com.facebook.react.devsupport;

import android.support.p000v4.app.NotificationCompat;
import android.support.p000v4.media.session.PlaybackStateCompat;
import android.support.p000v4.p002os.EnvironmentCompat;
import android.util.Log;
import android.util.Pair;
import com.facebook.cache.disk.DefaultDiskStorage.FileType;
import com.facebook.common.logging.FLog;
import com.facebook.infer.annotation.Assertions;
import com.facebook.react.bridge.NativeDeltaClient;
import com.facebook.react.common.DebugServerException;
import com.facebook.react.common.ReactConstants;
import com.facebook.react.devsupport.BundleDeltaClient.ClientType;
import com.facebook.react.devsupport.MultipartStreamReader.ChunkListener;
import com.facebook.react.devsupport.interfaces.DevBundleDownloadListener;
import com.google.android.gms.common.internal.ImagesContract;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import okhttp3.C1512Response;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request.Builder;
import okio.Buffer;
import okio.BufferedSource;
import okio.Okio;
import okio.Source;
import org.json.JSONException;
import org.json.JSONObject;

public class BundleDownloader {
    private static final int FILES_CHANGED_COUNT_NOT_BUILT_BY_BUNDLER = -2;
    private static final String TAG = "BundleDownloader";
    private BundleDeltaClient mBundleDeltaClient;
    private final OkHttpClient mClient;
    /* access modifiers changed from: private */
    @Nullable
    public Call mDownloadBundleFromURLCall;

    public static class BundleInfo {
        /* access modifiers changed from: private */
        @Nullable
        public String mDeltaClientName;
        /* access modifiers changed from: private */
        public int mFilesChangedCount;
        /* access modifiers changed from: private */
        @Nullable
        public String mUrl;

        @Nullable
        public static BundleInfo fromJSONString(String str) {
            if (str == null) {
                return null;
            }
            BundleInfo bundleInfo = new BundleInfo();
            try {
                JSONObject jSONObject = new JSONObject(str);
                bundleInfo.mDeltaClientName = jSONObject.getString("deltaClient");
                bundleInfo.mUrl = jSONObject.getString(ImagesContract.URL);
                bundleInfo.mFilesChangedCount = jSONObject.getInt("filesChangedCount");
                return bundleInfo;
            } catch (JSONException e) {
                Log.e(BundleDownloader.TAG, "Invalid bundle info: ", e);
                return null;
            }
        }

        @Nullable
        public String toJSONString() {
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put("deltaClient", this.mDeltaClientName);
                jSONObject.put(ImagesContract.URL, this.mUrl);
                jSONObject.put("filesChangedCount", this.mFilesChangedCount);
                return jSONObject.toString();
            } catch (JSONException e) {
                Log.e(BundleDownloader.TAG, "Can't serialize bundle info: ", e);
                return null;
            }
        }

        @Nullable
        public String getDeltaClient() {
            return this.mDeltaClientName;
        }

        public String getUrl() {
            String str = this.mUrl;
            return str != null ? str : EnvironmentCompat.MEDIA_UNKNOWN;
        }

        public int getFilesChangedCount() {
            return this.mFilesChangedCount;
        }
    }

    public BundleDownloader(OkHttpClient okHttpClient) {
        this.mClient = okHttpClient;
    }

    public void downloadBundleFromURL(DevBundleDownloadListener devBundleDownloadListener, File file, String str, @Nullable BundleInfo bundleInfo, ClientType clientType) {
        downloadBundleFromURL(devBundleDownloadListener, file, str, bundleInfo, clientType, new Builder());
    }

    public void downloadBundleFromURL(DevBundleDownloadListener devBundleDownloadListener, File file, String str, @Nullable BundleInfo bundleInfo, ClientType clientType, Builder builder) {
        this.mDownloadBundleFromURLCall = (Call) Assertions.assertNotNull(this.mClient.newCall(builder.url(formatBundleUrl(str, clientType)).build()));
        Call call = this.mDownloadBundleFromURLCall;
        final DevBundleDownloadListener devBundleDownloadListener2 = devBundleDownloadListener;
        final File file2 = file;
        final BundleInfo bundleInfo2 = bundleInfo;
        final ClientType clientType2 = clientType;
        C08731 r0 = new Callback() {
            public void onFailure(Call call, IOException iOException) {
                if (BundleDownloader.this.mDownloadBundleFromURLCall == null || BundleDownloader.this.mDownloadBundleFromURLCall.isCanceled()) {
                    BundleDownloader.this.mDownloadBundleFromURLCall = null;
                    return;
                }
                BundleDownloader.this.mDownloadBundleFromURLCall = null;
                DevBundleDownloadListener devBundleDownloadListener = devBundleDownloadListener2;
                StringBuilder sb = new StringBuilder();
                sb.append("URL: ");
                sb.append(call.request().url().toString());
                devBundleDownloadListener.onFailure(DebugServerException.makeGeneric("Could not connect to development server.", sb.toString(), iOException));
            }

            public void onResponse(Call call, C1512Response response) throws IOException {
                Throwable th;
                if (BundleDownloader.this.mDownloadBundleFromURLCall == null || BundleDownloader.this.mDownloadBundleFromURLCall.isCanceled()) {
                    BundleDownloader.this.mDownloadBundleFromURLCall = null;
                    return;
                }
                BundleDownloader.this.mDownloadBundleFromURLCall = null;
                String httpUrl = response.request().url().toString();
                Matcher matcher = Pattern.compile("multipart/mixed;.*boundary=\"([^\"]+)\"").matcher(response.header("content-type"));
                try {
                    if (matcher.find()) {
                        BundleDownloader.this.processMultipartResponse(httpUrl, response, matcher.group(1), file2, bundleInfo2, clientType2, devBundleDownloadListener2);
                    } else {
                        BundleDownloader.this.processBundleResult(httpUrl, response.code(), response.headers(), Okio.buffer((Source) response.body().source()), file2, bundleInfo2, clientType2, devBundleDownloadListener2);
                    }
                    if (response != null) {
                        response.close();
                    }
                    return;
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        };
        call.enqueue(r0);
    }

    private String formatBundleUrl(String str, ClientType clientType) {
        if (!BundleDeltaClient.isDeltaUrl(str)) {
            return str;
        }
        BundleDeltaClient bundleDeltaClient = this.mBundleDeltaClient;
        return (bundleDeltaClient == null || !bundleDeltaClient.canHandle(clientType)) ? str : this.mBundleDeltaClient.extendUrlForDelta(str);
    }

    /* access modifiers changed from: private */
    public void processMultipartResponse(String str, C1512Response response, String str2, File file, @Nullable BundleInfo bundleInfo, ClientType clientType, DevBundleDownloadListener devBundleDownloadListener) throws IOException {
        String str3 = str2;
        MultipartStreamReader multipartStreamReader = new MultipartStreamReader(response.body().source(), str2);
        final C1512Response response2 = response;
        final String str4 = str;
        final File file2 = file;
        final BundleInfo bundleInfo2 = bundleInfo;
        final ClientType clientType2 = clientType;
        final DevBundleDownloadListener devBundleDownloadListener2 = devBundleDownloadListener;
        C08742 r2 = new ChunkListener() {
            public void onChunkComplete(Map<String, String> map, Buffer buffer, boolean z) throws IOException {
                if (z) {
                    BundleDownloader.this.processBundleResult(str4, map.containsKey("X-Http-Status") ? Integer.parseInt((String) map.get("X-Http-Status")) : response2.code(), Headers.m189of(map), buffer, file2, bundleInfo2, clientType2, devBundleDownloadListener2);
                } else if (map.containsKey("Content-Type") && ((String) map.get("Content-Type")).equals("application/json")) {
                    try {
                        JSONObject jSONObject = new JSONObject(buffer.readUtf8());
                        Integer num = null;
                        String string = jSONObject.has(NotificationCompat.CATEGORY_STATUS) ? jSONObject.getString(NotificationCompat.CATEGORY_STATUS) : null;
                        Integer valueOf = jSONObject.has("done") ? Integer.valueOf(jSONObject.getInt("done")) : null;
                        if (jSONObject.has("total")) {
                            num = Integer.valueOf(jSONObject.getInt("total"));
                        }
                        devBundleDownloadListener2.onProgress(string, valueOf, num);
                    } catch (JSONException e) {
                        String str = ReactConstants.TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("Error parsing progress JSON. ");
                        sb.append(e.toString());
                        FLog.m48e(str, sb.toString());
                    }
                }
            }

            public void onChunkProgress(Map<String, String> map, long j, long j2) throws IOException {
                if ("application/javascript".equals(map.get("Content-Type"))) {
                    devBundleDownloadListener2.onProgress("Downloading JavaScript bundle", Integer.valueOf((int) (j / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID)), Integer.valueOf((int) (j2 / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID)));
                }
            }
        };
        if (!multipartStreamReader.readAllParts(r2)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Error while reading multipart response.\n\nResponse code: ");
            sb.append(response.code());
            sb.append("\n\nURL: ");
            sb.append(str.toString());
            sb.append("\n\n");
            devBundleDownloadListener.onFailure(new DebugServerException(sb.toString()));
        }
    }

    /* access modifiers changed from: private */
    public void processBundleResult(String str, int i, Headers headers, BufferedSource bufferedSource, File file, BundleInfo bundleInfo, ClientType clientType, DevBundleDownloadListener devBundleDownloadListener) throws IOException {
        boolean z;
        if (i != 200) {
            String readUtf8 = bufferedSource.readUtf8();
            DebugServerException parse = DebugServerException.parse(readUtf8);
            if (parse != null) {
                devBundleDownloadListener.onFailure(parse);
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("The development server returned response error code: ");
                sb.append(i);
                sb.append("\n\n");
                sb.append("URL: ");
                sb.append(str);
                sb.append("\n\n");
                sb.append("Body:\n");
                sb.append(readUtf8);
                devBundleDownloadListener.onFailure(new DebugServerException(sb.toString()));
            }
            return;
        }
        if (bundleInfo != null) {
            populateBundleInfo(str, headers, clientType, bundleInfo);
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(file.getPath());
        sb2.append(FileType.TEMP);
        File file2 = new File(sb2.toString());
        NativeDeltaClient nativeDeltaClient = null;
        if (BundleDeltaClient.isDeltaUrl(str)) {
            BundleDeltaClient bundleDeltaClient = getBundleDeltaClient(clientType);
            Assertions.assertNotNull(bundleDeltaClient);
            Pair processDelta = bundleDeltaClient.processDelta(headers, bufferedSource, file2);
            z = ((Boolean) processDelta.first).booleanValue();
            nativeDeltaClient = (NativeDeltaClient) processDelta.second;
        } else {
            this.mBundleDeltaClient = null;
            z = storePlainJSInFile(bufferedSource, file2);
        }
        if (!z || file2.renameTo(file)) {
            devBundleDownloadListener.onSuccess(nativeDeltaClient);
            return;
        }
        StringBuilder sb3 = new StringBuilder();
        sb3.append("Couldn't rename ");
        sb3.append(file2);
        sb3.append(" to ");
        sb3.append(file);
        throw new IOException(sb3.toString());
    }

    private BundleDeltaClient getBundleDeltaClient(ClientType clientType) {
        BundleDeltaClient bundleDeltaClient = this.mBundleDeltaClient;
        if (bundleDeltaClient == null || !bundleDeltaClient.canHandle(clientType)) {
            this.mBundleDeltaClient = BundleDeltaClient.create(clientType);
        }
        return this.mBundleDeltaClient;
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0014  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean storePlainJSInFile(okio.BufferedSource r0, java.io.File r1) throws java.io.IOException {
        /*
            okio.Sink r1 = okio.Okio.sink(r1)     // Catch:{ all -> 0x0010 }
            r0.readAll(r1)     // Catch:{ all -> 0x000e }
            if (r1 == 0) goto L_0x000c
            r1.close()
        L_0x000c:
            r0 = 1
            return r0
        L_0x000e:
            r0 = move-exception
            goto L_0x0012
        L_0x0010:
            r0 = move-exception
            r1 = 0
        L_0x0012:
            if (r1 == 0) goto L_0x0017
            r1.close()
        L_0x0017:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.react.devsupport.BundleDownloader.storePlainJSInFile(okio.BufferedSource, java.io.File):boolean");
    }

    private static void populateBundleInfo(String str, Headers headers, ClientType clientType, BundleInfo bundleInfo) {
        bundleInfo.mDeltaClientName = clientType == ClientType.NONE ? null : clientType.name();
        bundleInfo.mUrl = str;
        String str2 = headers.get("X-Metro-Files-Changed-Count");
        if (str2 != null) {
            try {
                bundleInfo.mFilesChangedCount = Integer.parseInt(str2);
            } catch (NumberFormatException unused) {
                bundleInfo.mFilesChangedCount = -2;
            }
        }
    }
}

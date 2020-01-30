package com.RNFetchBlob;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Base64;
import com.facebook.common.util.UriUtil;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter;
import com.google.android.gms.gcm.Task;
import com.google.android.gms.measurement.api.AppMeasurementSdk.ConditionalUserProperty;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

class RNFetchBlobBody extends RequestBody {
    private File bodyCache;
    private Boolean chunkedEncoding = Boolean.valueOf(false);
    private long contentLength = 0;
    private ReadableArray form;
    private String mTaskId;
    private MediaType mime;
    private String rawBody;
    int reported = 0;
    private InputStream requestStream;
    private RequestType requestType;

    private class FormField {
        public String data;
        String filename;
        String mime;
        public String name;

        FormField(ReadableMap readableMap) {
            if (readableMap.hasKey(ConditionalUserProperty.NAME)) {
                this.name = readableMap.getString(ConditionalUserProperty.NAME);
            }
            if (readableMap.hasKey("filename")) {
                this.filename = readableMap.getString("filename");
            }
            if (readableMap.hasKey("type")) {
                this.mime = readableMap.getString("type");
            } else {
                this.mime = this.filename == null ? "text/plain" : "application/octet-stream";
            }
            if (readableMap.hasKey(UriUtil.DATA_SCHEME)) {
                this.data = readableMap.getString(UriUtil.DATA_SCHEME);
            }
        }
    }

    RNFetchBlobBody(String str) {
        this.mTaskId = str;
    }

    /* access modifiers changed from: 0000 */
    public RNFetchBlobBody chunkedEncoding(boolean z) {
        this.chunkedEncoding = Boolean.valueOf(z);
        return this;
    }

    /* access modifiers changed from: 0000 */
    public RNFetchBlobBody setMIME(MediaType mediaType) {
        this.mime = mediaType;
        return this;
    }

    /* access modifiers changed from: 0000 */
    public RNFetchBlobBody setRequestType(RequestType requestType2) {
        this.requestType = requestType2;
        return this;
    }

    /* access modifiers changed from: 0000 */
    public RNFetchBlobBody setBody(String str) {
        this.rawBody = str;
        if (this.rawBody == null) {
            this.rawBody = "";
            this.requestType = RequestType.AsIs;
        }
        try {
            switch (this.requestType) {
                case SingleFile:
                    this.requestStream = getRequestStream();
                    this.contentLength = (long) this.requestStream.available();
                    break;
                case AsIs:
                    this.contentLength = (long) this.rawBody.getBytes().length;
                    this.requestStream = new ByteArrayInputStream(this.rawBody.getBytes());
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            StringBuilder sb = new StringBuilder();
            sb.append("RNFetchBlob failed to create single content request body :");
            sb.append(e.getLocalizedMessage());
            sb.append("\r\n");
            RNFetchBlobUtils.emitWarningEvent(sb.toString());
        }
        return this;
    }

    /* access modifiers changed from: 0000 */
    public RNFetchBlobBody setBody(ReadableArray readableArray) {
        this.form = readableArray;
        try {
            this.bodyCache = createMultipartBodyCache();
            this.requestStream = new FileInputStream(this.bodyCache);
            this.contentLength = this.bodyCache.length();
        } catch (Exception e) {
            e.printStackTrace();
            StringBuilder sb = new StringBuilder();
            sb.append("RNFetchBlob failed to create request multipart body :");
            sb.append(e.getLocalizedMessage());
            RNFetchBlobUtils.emitWarningEvent(sb.toString());
        }
        return this;
    }

    public long contentLength() {
        if (this.chunkedEncoding.booleanValue()) {
            return -1;
        }
        return this.contentLength;
    }

    public MediaType contentType() {
        return this.mime;
    }

    public void writeTo(@NonNull BufferedSink bufferedSink) {
        try {
            pipeStreamToSink(this.requestStream, bufferedSink);
        } catch (Exception e) {
            RNFetchBlobUtils.emitWarningEvent(e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean clearRequestBody() {
        try {
            if (this.bodyCache != null && this.bodyCache.exists()) {
                this.bodyCache.delete();
            }
            return true;
        } catch (Exception e) {
            RNFetchBlobUtils.emitWarningEvent(e.getLocalizedMessage());
            return false;
        }
    }

    private InputStream getRequestStream() throws Exception {
        if (this.rawBody.startsWith(RNFetchBlobConst.FILE_PREFIX)) {
            String normalizePath = RNFetchBlobFS.normalizePath(this.rawBody.substring(19));
            if (RNFetchBlobFS.isAsset(normalizePath)) {
                try {
                    return C0568RNFetchBlob.RCTContext.getAssets().open(normalizePath.replace(RNFetchBlobConst.FILE_PREFIX_BUNDLE_ASSET, ""));
                } catch (Exception e) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("error when getting request stream from asset : ");
                    sb.append(e.getLocalizedMessage());
                    throw new Exception(sb.toString());
                }
            } else {
                File file = new File(RNFetchBlobFS.normalizePath(normalizePath));
                try {
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    return new FileInputStream(file);
                } catch (Exception e2) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("error when getting request stream: ");
                    sb2.append(e2.getLocalizedMessage());
                    throw new Exception(sb2.toString());
                }
            }
        } else if (this.rawBody.startsWith(RNFetchBlobConst.CONTENT_PREFIX)) {
            String substring = this.rawBody.substring(22);
            try {
                return C0568RNFetchBlob.RCTContext.getContentResolver().openInputStream(Uri.parse(substring));
            } catch (Exception e3) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("error when getting request stream for content URI: ");
                sb3.append(substring);
                throw new Exception(sb3.toString(), e3);
            }
        } else {
            try {
                return new ByteArrayInputStream(Base64.decode(this.rawBody, 0));
            } catch (Exception e4) {
                StringBuilder sb4 = new StringBuilder();
                sb4.append("error when getting request stream: ");
                sb4.append(e4.getLocalizedMessage());
                throw new Exception(sb4.toString());
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0146, code lost:
        if (r6 != null) goto L_0x0148;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0148, code lost:
        r6.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0170, code lost:
        if (r6 == null) goto L_0x01c6;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.io.File createMultipartBodyCache() throws java.io.IOException {
        /*
            r10 = this;
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "RNFetchBlob-"
            r0.append(r1)
            java.lang.String r1 = r10.mTaskId
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            com.facebook.react.bridge.ReactApplicationContext r1 = com.RNFetchBlob.C0568RNFetchBlob.RCTContext
            java.io.File r1 = r1.getCacheDir()
            java.lang.String r2 = "rnfb-form-tmp"
            java.lang.String r3 = ""
            java.io.File r1 = java.io.File.createTempFile(r2, r3, r1)
            java.io.FileOutputStream r2 = new java.io.FileOutputStream
            r2.<init>(r1)
            java.util.ArrayList r3 = r10.countFormDataLength()
            com.facebook.react.bridge.ReactApplicationContext r4 = com.RNFetchBlob.C0568RNFetchBlob.RCTContext
            java.util.Iterator r3 = r3.iterator()
        L_0x0030:
            boolean r5 = r3.hasNext()
            if (r5 == 0) goto L_0x01d1
            java.lang.Object r5 = r3.next()
            com.RNFetchBlob.RNFetchBlobBody$FormField r5 = (com.RNFetchBlob.RNFetchBlobBody.FormField) r5
            java.lang.String r6 = r5.data
            java.lang.String r7 = r5.name
            if (r7 == 0) goto L_0x0030
            if (r6 != 0) goto L_0x0045
            goto L_0x0030
        L_0x0045:
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            java.lang.String r9 = "--"
            r8.append(r9)
            r8.append(r0)
            java.lang.String r9 = "\r\n"
            r8.append(r9)
            java.lang.String r8 = r8.toString()
            java.lang.String r9 = r5.filename
            if (r9 == 0) goto L_0x0182
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            r9.append(r8)
            java.lang.String r8 = "Content-Disposition: form-data; name=\""
            r9.append(r8)
            r9.append(r7)
            java.lang.String r7 = "\"; filename=\""
            r9.append(r7)
            java.lang.String r7 = r5.filename
            r9.append(r7)
            java.lang.String r7 = "\"\r\n"
            r9.append(r7)
            java.lang.String r7 = r9.toString()
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            r8.append(r7)
            java.lang.String r7 = "Content-Type: "
            r8.append(r7)
            java.lang.String r5 = r5.mime
            r8.append(r5)
            java.lang.String r5 = "\r\n\r\n"
            r8.append(r5)
            java.lang.String r5 = r8.toString()
            byte[] r5 = r5.getBytes()
            r2.write(r5)
            java.lang.String r5 = "RNFetchBlob-file://"
            boolean r5 = r6.startsWith(r5)
            if (r5 == 0) goto L_0x0128
            r5 = 19
            java.lang.String r5 = r6.substring(r5)
            java.lang.String r5 = com.RNFetchBlob.RNFetchBlobFS.normalizePath(r5)
            boolean r6 = com.RNFetchBlob.RNFetchBlobFS.isAsset(r5)
            if (r6 == 0) goto L_0x00f4
            java.lang.String r6 = "bundle-assets://"
            java.lang.String r7 = ""
            java.lang.String r6 = r5.replace(r6, r7)     // Catch:{ IOException -> 0x00d1 }
            android.content.res.AssetManager r7 = r4.getAssets()     // Catch:{ IOException -> 0x00d1 }
            java.io.InputStream r6 = r7.open(r6)     // Catch:{ IOException -> 0x00d1 }
            r10.pipeStreamToFileStream(r6, r2)     // Catch:{ IOException -> 0x00d1 }
            goto L_0x01c6
        L_0x00d1:
            r6 = move-exception
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r8 = "Failed to create form data asset :"
            r7.append(r8)
            r7.append(r5)
            java.lang.String r5 = ", "
            r7.append(r5)
            java.lang.String r5 = r6.getLocalizedMessage()
            r7.append(r5)
            java.lang.String r5 = r7.toString()
            com.RNFetchBlob.RNFetchBlobUtils.emitWarningEvent(r5)
            goto L_0x01c6
        L_0x00f4:
            java.io.File r6 = new java.io.File
            java.lang.String r7 = com.RNFetchBlob.RNFetchBlobFS.normalizePath(r5)
            r6.<init>(r7)
            boolean r7 = r6.exists()
            if (r7 == 0) goto L_0x010d
            java.io.FileInputStream r5 = new java.io.FileInputStream
            r5.<init>(r6)
            r10.pipeStreamToFileStream(r5, r2)
            goto L_0x01c6
        L_0x010d:
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "Failed to create form data from path :"
            r6.append(r7)
            r6.append(r5)
            java.lang.String r5 = ", file not exists."
            r6.append(r5)
            java.lang.String r5 = r6.toString()
            com.RNFetchBlob.RNFetchBlobUtils.emitWarningEvent(r5)
            goto L_0x01c6
        L_0x0128:
            java.lang.String r5 = "RNFetchBlob-content://"
            boolean r5 = r6.startsWith(r5)
            if (r5 == 0) goto L_0x0179
            r5 = 22
            java.lang.String r5 = r6.substring(r5)
            r6 = 0
            android.content.ContentResolver r7 = r4.getContentResolver()     // Catch:{ Exception -> 0x014f }
            android.net.Uri r8 = android.net.Uri.parse(r5)     // Catch:{ Exception -> 0x014f }
            java.io.InputStream r6 = r7.openInputStream(r8)     // Catch:{ Exception -> 0x014f }
            r10.pipeStreamToFileStream(r6, r2)     // Catch:{ Exception -> 0x014f }
            if (r6 == 0) goto L_0x01c6
        L_0x0148:
            r6.close()
            goto L_0x01c6
        L_0x014d:
            r0 = move-exception
            goto L_0x0173
        L_0x014f:
            r7 = move-exception
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ all -> 0x014d }
            r8.<init>()     // Catch:{ all -> 0x014d }
            java.lang.String r9 = "Failed to create form data from content URI:"
            r8.append(r9)     // Catch:{ all -> 0x014d }
            r8.append(r5)     // Catch:{ all -> 0x014d }
            java.lang.String r5 = ", "
            r8.append(r5)     // Catch:{ all -> 0x014d }
            java.lang.String r5 = r7.getLocalizedMessage()     // Catch:{ all -> 0x014d }
            r8.append(r5)     // Catch:{ all -> 0x014d }
            java.lang.String r5 = r8.toString()     // Catch:{ all -> 0x014d }
            com.RNFetchBlob.RNFetchBlobUtils.emitWarningEvent(r5)     // Catch:{ all -> 0x014d }
            if (r6 == 0) goto L_0x01c6
            goto L_0x0148
        L_0x0173:
            if (r6 == 0) goto L_0x0178
            r6.close()
        L_0x0178:
            throw r0
        L_0x0179:
            r5 = 0
            byte[] r5 = android.util.Base64.decode(r6, r5)
            r2.write(r5)
            goto L_0x01c6
        L_0x0182:
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            r6.append(r8)
            java.lang.String r8 = "Content-Disposition: form-data; name=\""
            r6.append(r8)
            r6.append(r7)
            java.lang.String r7 = "\"\r\n"
            r6.append(r7)
            java.lang.String r6 = r6.toString()
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            r7.append(r6)
            java.lang.String r6 = "Content-Type: "
            r7.append(r6)
            java.lang.String r6 = r5.mime
            r7.append(r6)
            java.lang.String r6 = "\r\n\r\n"
            r7.append(r6)
            java.lang.String r6 = r7.toString()
            byte[] r6 = r6.getBytes()
            r2.write(r6)
            java.lang.String r5 = r5.data
            byte[] r5 = r5.getBytes()
            r2.write(r5)
        L_0x01c6:
            java.lang.String r5 = "\r\n"
            byte[] r5 = r5.getBytes()
            r2.write(r5)
            goto L_0x0030
        L_0x01d1:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "--"
            r3.append(r4)
            r3.append(r0)
            java.lang.String r0 = "--\r\n"
            r3.append(r0)
            java.lang.String r0 = r3.toString()
            byte[] r0 = r0.getBytes()
            r2.write(r0)
            r2.flush()
            r2.close()
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.RNFetchBlob.RNFetchBlobBody.createMultipartBodyCache():java.io.File");
    }

    private void pipeStreamToSink(InputStream inputStream, BufferedSink bufferedSink) throws IOException {
        byte[] bArr = new byte[Task.EXTRAS_LIMIT_BYTES];
        long j = 0;
        while (true) {
            int read = inputStream.read(bArr, 0, Task.EXTRAS_LIMIT_BYTES);
            if (read > 0) {
                bufferedSink.write(bArr, 0, read);
                j += (long) read;
                emitUploadProgress(j);
            } else {
                inputStream.close();
                return;
            }
        }
    }

    private void pipeStreamToFileStream(InputStream inputStream, FileOutputStream fileOutputStream) throws IOException {
        byte[] bArr = new byte[Task.EXTRAS_LIMIT_BYTES];
        while (true) {
            int read = inputStream.read(bArr);
            if (read > 0) {
                fileOutputStream.write(bArr, 0, read);
            } else {
                inputStream.close();
                return;
            }
        }
    }

    private ArrayList<FormField> countFormDataLength() throws IOException {
        ArrayList<FormField> arrayList = new ArrayList<>();
        ReactApplicationContext reactApplicationContext = C0568RNFetchBlob.RCTContext;
        long j = 0;
        for (int i = 0; i < this.form.size(); i++) {
            FormField formField = new FormField(this.form.getMap(i));
            arrayList.add(formField);
            if (formField.data == null) {
                StringBuilder sb = new StringBuilder();
                sb.append("RNFetchBlob multipart request builder has found a field without `data` property, the field `");
                sb.append(formField.name);
                sb.append("` will be removed implicitly.");
                RNFetchBlobUtils.emitWarningEvent(sb.toString());
            } else if (formField.filename != null) {
                String str = formField.data;
                if (str.startsWith(RNFetchBlobConst.FILE_PREFIX)) {
                    String normalizePath = RNFetchBlobFS.normalizePath(str.substring(19));
                    if (RNFetchBlobFS.isAsset(normalizePath)) {
                        try {
                            j += (long) reactApplicationContext.getAssets().open(normalizePath.replace(RNFetchBlobConst.FILE_PREFIX_BUNDLE_ASSET, "")).available();
                        } catch (IOException e) {
                            RNFetchBlobUtils.emitWarningEvent(e.getLocalizedMessage());
                        }
                    } else {
                        j += new File(RNFetchBlobFS.normalizePath(normalizePath)).length();
                    }
                } else if (str.startsWith(RNFetchBlobConst.CONTENT_PREFIX)) {
                    String substring = str.substring(22);
                    InputStream inputStream = null;
                    try {
                        inputStream = reactApplicationContext.getContentResolver().openInputStream(Uri.parse(substring));
                        j += (long) inputStream.available();
                        if (inputStream == null) {
                        }
                    } catch (Exception e2) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Failed to estimate form data length from content URI:");
                        sb2.append(substring);
                        sb2.append(", ");
                        sb2.append(e2.getLocalizedMessage());
                        RNFetchBlobUtils.emitWarningEvent(sb2.toString());
                        if (inputStream == null) {
                        }
                    } catch (Throwable th) {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        throw th;
                    }
                    inputStream.close();
                } else {
                    j += (long) Base64.decode(str, 0).length;
                }
            } else {
                j += (long) formField.data.getBytes().length;
            }
        }
        this.contentLength = j;
        return arrayList;
    }

    private void emitUploadProgress(long j) {
        RNFetchBlobProgressConfig reportUploadProgress = RNFetchBlobReq.getReportUploadProgress(this.mTaskId);
        if (reportUploadProgress != null) {
            long j2 = this.contentLength;
            if (j2 != 0 && reportUploadProgress.shouldReport(((float) j) / ((float) j2))) {
                WritableMap createMap = Arguments.createMap();
                createMap.putString("taskId", this.mTaskId);
                createMap.putString("written", String.valueOf(j));
                createMap.putString("total", String.valueOf(this.contentLength));
                ((RCTDeviceEventEmitter) C0568RNFetchBlob.RCTContext.getJSModule(RCTDeviceEventEmitter.class)).emit(RNFetchBlobConst.EVENT_UPLOAD_PROGRESS, createMap);
            }
        }
    }
}

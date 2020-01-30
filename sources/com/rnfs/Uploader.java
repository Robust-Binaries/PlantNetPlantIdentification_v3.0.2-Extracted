package com.rnfs;

import android.os.AsyncTask;
import android.webkit.MimeTypeMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class Uploader extends AsyncTask<UploadParams, int[], UploadResult> {
    private AtomicBoolean mAbort = new AtomicBoolean(false);
    /* access modifiers changed from: private */
    public UploadParams mParams;
    /* access modifiers changed from: private */
    public UploadResult res;

    /* access modifiers changed from: protected */
    public UploadResult doInBackground(UploadParams... uploadParamsArr) {
        this.mParams = uploadParamsArr[0];
        this.res = new UploadResult();
        new Thread(new Runnable() {
            public void run() {
                try {
                    Uploader.this.upload(Uploader.this.mParams, Uploader.this.res);
                    Uploader.this.mParams.onUploadComplete.onUploadComplete(Uploader.this.res);
                } catch (Exception e) {
                    Uploader.this.res.exception = e;
                    Uploader.this.mParams.onUploadComplete.onUploadComplete(Uploader.this.res);
                }
            }
        }).start();
        return this.res;
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:86:0x033f  */
    /* JADX WARNING: Removed duplicated region for block: B:88:0x0344  */
    /* JADX WARNING: Removed duplicated region for block: B:90:0x0349  */
    /* JADX WARNING: Removed duplicated region for block: B:92:0x034e  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void upload(com.rnfs.UploadParams r22, com.rnfs.UploadResult r23) throws java.lang.Exception {
        /*
            r21 = this;
            r1 = r21
            r0 = r22
            java.lang.String r2 = "\r\n"
            java.lang.String r3 = "--"
            java.lang.String r4 = "*****"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            r5.append(r2)
            r5.append(r3)
            r5.append(r4)
            r5.append(r3)
            r5.append(r2)
            java.lang.String r5 = r5.toString()
            java.lang.String r6 = ""
            java.lang.String r7 = ""
            java.net.URL r9 = r0.src     // Catch:{ all -> 0x0338 }
            java.net.URLConnection r9 = r9.openConnection()     // Catch:{ all -> 0x0338 }
            java.net.HttpURLConnection r9 = (java.net.HttpURLConnection) r9     // Catch:{ all -> 0x0338 }
            r10 = 1
            r9.setDoOutput(r10)     // Catch:{ all -> 0x0333 }
            com.facebook.react.bridge.ReadableMap r11 = r0.headers     // Catch:{ all -> 0x0333 }
            com.facebook.react.bridge.ReadableMapKeySetIterator r11 = r11.keySetIterator()     // Catch:{ all -> 0x0333 }
            java.lang.String r12 = r0.method     // Catch:{ all -> 0x0333 }
            r9.setRequestMethod(r12)     // Catch:{ all -> 0x0333 }
            java.lang.String r12 = "Content-Type"
            java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch:{ all -> 0x0333 }
            r13.<init>()     // Catch:{ all -> 0x0333 }
            java.lang.String r14 = "multipart/form-data;boundary="
            r13.append(r14)     // Catch:{ all -> 0x0333 }
            r13.append(r4)     // Catch:{ all -> 0x0333 }
            java.lang.String r13 = r13.toString()     // Catch:{ all -> 0x0333 }
            r9.setRequestProperty(r12, r13)     // Catch:{ all -> 0x0333 }
        L_0x0053:
            boolean r12 = r11.hasNextKey()     // Catch:{ all -> 0x0333 }
            if (r12 == 0) goto L_0x0067
            java.lang.String r12 = r11.nextKey()     // Catch:{ all -> 0x0333 }
            com.facebook.react.bridge.ReadableMap r13 = r0.headers     // Catch:{ all -> 0x0333 }
            java.lang.String r13 = r13.getString(r12)     // Catch:{ all -> 0x0333 }
            r9.setRequestProperty(r12, r13)     // Catch:{ all -> 0x0333 }
            goto L_0x0053
        L_0x0067:
            com.facebook.react.bridge.ReadableMap r11 = r0.fields     // Catch:{ all -> 0x0333 }
            com.facebook.react.bridge.ReadableMapKeySetIterator r11 = r11.keySetIterator()     // Catch:{ all -> 0x0333 }
        L_0x006d:
            boolean r12 = r11.hasNextKey()     // Catch:{ all -> 0x0333 }
            if (r12 == 0) goto L_0x00ac
            java.lang.String r12 = r11.nextKey()     // Catch:{ all -> 0x0333 }
            com.facebook.react.bridge.ReadableMap r13 = r0.fields     // Catch:{ all -> 0x0333 }
            java.lang.String r13 = r13.getString(r12)     // Catch:{ all -> 0x0333 }
            java.lang.StringBuilder r14 = new java.lang.StringBuilder     // Catch:{ all -> 0x0333 }
            r14.<init>()     // Catch:{ all -> 0x0333 }
            r14.append(r6)     // Catch:{ all -> 0x0333 }
            r14.append(r3)     // Catch:{ all -> 0x0333 }
            r14.append(r4)     // Catch:{ all -> 0x0333 }
            r14.append(r2)     // Catch:{ all -> 0x0333 }
            java.lang.String r6 = "Content-Disposition: form-data; name=\""
            r14.append(r6)     // Catch:{ all -> 0x0333 }
            r14.append(r12)     // Catch:{ all -> 0x0333 }
            java.lang.String r6 = "\""
            r14.append(r6)     // Catch:{ all -> 0x0333 }
            r14.append(r2)     // Catch:{ all -> 0x0333 }
            r14.append(r2)     // Catch:{ all -> 0x0333 }
            r14.append(r13)     // Catch:{ all -> 0x0333 }
            r14.append(r2)     // Catch:{ all -> 0x0333 }
            java.lang.String r6 = r14.toString()     // Catch:{ all -> 0x0333 }
            goto L_0x006d
        L_0x00ac:
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ all -> 0x0333 }
            r11.<init>()     // Catch:{ all -> 0x0333 }
            r11.append(r7)     // Catch:{ all -> 0x0333 }
            r11.append(r6)     // Catch:{ all -> 0x0333 }
            java.lang.String r7 = r11.toString()     // Catch:{ all -> 0x0333 }
            java.util.ArrayList<com.facebook.react.bridge.ReadableMap> r11 = r0.files     // Catch:{ all -> 0x0333 }
            java.lang.Object[] r11 = r11.toArray()     // Catch:{ all -> 0x0333 }
            int r11 = r11.length     // Catch:{ all -> 0x0333 }
            java.lang.String[] r11 = new java.lang.String[r11]     // Catch:{ all -> 0x0333 }
            java.util.ArrayList<com.facebook.react.bridge.ReadableMap> r12 = r0.files     // Catch:{ all -> 0x0333 }
            java.util.Iterator r12 = r12.iterator()     // Catch:{ all -> 0x0333 }
            r14 = 0
            r8 = 0
        L_0x00cd:
            boolean r16 = r12.hasNext()     // Catch:{ all -> 0x0333 }
            if (r16 == 0) goto L_0x01d1
            java.lang.Object r16 = r12.next()     // Catch:{ all -> 0x01ca }
            r13 = r16
            com.facebook.react.bridge.ReadableMap r13 = (com.facebook.react.bridge.ReadableMap) r13     // Catch:{ all -> 0x01ca }
            java.lang.String r10 = "name"
            java.lang.String r10 = r13.getString(r10)     // Catch:{ NoSuchKeyException -> 0x00ff }
            r17 = r10
            java.lang.String r10 = "filename"
            java.lang.String r10 = r13.getString(r10)     // Catch:{ NoSuchKeyException -> 0x00ff }
            r18 = r10
            java.lang.String r10 = "filetype"
            java.lang.String r10 = r13.getString(r10)     // Catch:{ NoSuchKeyException -> 0x00ff }
            r19 = r9
            r20 = r18
            r18 = r6
            r6 = r10
            r10 = r17
            r17 = r12
            r12 = r20
            goto L_0x0126
        L_0x00ff:
            java.lang.String r10 = "name"
            java.lang.String r10 = r13.getString(r10)     // Catch:{ all -> 0x01ca }
            r17 = r10
            java.lang.String r10 = "filename"
            java.lang.String r10 = r13.getString(r10)     // Catch:{ all -> 0x01ca }
            r18 = r10
            java.lang.String r10 = "filepath"
            java.lang.String r10 = r13.getString(r10)     // Catch:{ all -> 0x01ca }
            java.lang.String r10 = r1.getMimeType(r10)     // Catch:{ all -> 0x01ca }
            r19 = r9
            r20 = r18
            r18 = r6
            r6 = r10
            r10 = r17
            r17 = r12
            r12 = r20
        L_0x0126:
            java.io.File r9 = new java.io.File     // Catch:{ all -> 0x01c6 }
            java.lang.String r1 = "filepath"
            java.lang.String r1 = r13.getString(r1)     // Catch:{ all -> 0x01c6 }
            r9.<init>(r1)     // Catch:{ all -> 0x01c6 }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x01c6 }
            r1.<init>()     // Catch:{ all -> 0x01c6 }
            r1.append(r3)     // Catch:{ all -> 0x01c6 }
            r1.append(r4)     // Catch:{ all -> 0x01c6 }
            r1.append(r2)     // Catch:{ all -> 0x01c6 }
            java.lang.String r13 = "Content-Disposition: form-data; name=\""
            r1.append(r13)     // Catch:{ all -> 0x01c6 }
            r1.append(r10)     // Catch:{ all -> 0x01c6 }
            java.lang.String r10 = "\"; filename=\""
            r1.append(r10)     // Catch:{ all -> 0x01c6 }
            r1.append(r12)     // Catch:{ all -> 0x01c6 }
            java.lang.String r10 = "\""
            r1.append(r10)     // Catch:{ all -> 0x01c6 }
            r1.append(r2)     // Catch:{ all -> 0x01c6 }
            java.lang.String r10 = "Content-Type: "
            r1.append(r10)     // Catch:{ all -> 0x01c6 }
            r1.append(r6)     // Catch:{ all -> 0x01c6 }
            r1.append(r2)     // Catch:{ all -> 0x01c6 }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x01c6 }
            long r9 = r9.length()     // Catch:{ all -> 0x01c6 }
            long r14 = r14 + r9
            java.util.ArrayList<com.facebook.react.bridge.ReadableMap> r6 = r0.files     // Catch:{ all -> 0x01c6 }
            java.lang.Object[] r6 = r6.toArray()     // Catch:{ all -> 0x01c6 }
            int r6 = r6.length     // Catch:{ all -> 0x01c6 }
            r12 = 1
            int r6 = r6 - r12
            if (r6 != r8) goto L_0x017c
            int r6 = r5.length()     // Catch:{ all -> 0x01c6 }
            long r12 = (long) r6     // Catch:{ all -> 0x01c6 }
            long r14 = r14 + r12
        L_0x017c:
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ all -> 0x01c6 }
            r6.<init>()     // Catch:{ all -> 0x01c6 }
            java.lang.String r12 = "Content-length: "
            r6.append(r12)     // Catch:{ all -> 0x01c6 }
            r6.append(r9)     // Catch:{ all -> 0x01c6 }
            r6.append(r2)     // Catch:{ all -> 0x01c6 }
            java.lang.String r6 = r6.toString()     // Catch:{ all -> 0x01c6 }
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ all -> 0x01c6 }
            r9.<init>()     // Catch:{ all -> 0x01c6 }
            r9.append(r1)     // Catch:{ all -> 0x01c6 }
            r9.append(r6)     // Catch:{ all -> 0x01c6 }
            r9.append(r2)     // Catch:{ all -> 0x01c6 }
            java.lang.String r9 = r9.toString()     // Catch:{ all -> 0x01c6 }
            r11[r8] = r9     // Catch:{ all -> 0x01c6 }
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ all -> 0x01c6 }
            r9.<init>()     // Catch:{ all -> 0x01c6 }
            r9.append(r7)     // Catch:{ all -> 0x01c6 }
            r9.append(r1)     // Catch:{ all -> 0x01c6 }
            r9.append(r6)     // Catch:{ all -> 0x01c6 }
            r9.append(r2)     // Catch:{ all -> 0x01c6 }
            java.lang.String r7 = r9.toString()     // Catch:{ all -> 0x01c6 }
            int r8 = r8 + 1
            r12 = r17
            r6 = r18
            r9 = r19
            r1 = r21
            r10 = 1
            goto L_0x00cd
        L_0x01c6:
            r0 = move-exception
            r9 = r19
            goto L_0x01cd
        L_0x01ca:
            r0 = move-exception
            r19 = r9
        L_0x01cd:
            r1 = r21
            goto L_0x0334
        L_0x01d1:
            r18 = r6
            r19 = r9
            com.rnfs.UploadParams r3 = r1.mParams     // Catch:{ all -> 0x032f }
            com.rnfs.UploadParams$onUploadBegin r3 = r3.onUploadBegin     // Catch:{ all -> 0x032f }
            r3.onUploadBegin()     // Catch:{ all -> 0x032f }
            int r3 = r7.length()     // Catch:{ all -> 0x032f }
            long r3 = (long) r3     // Catch:{ all -> 0x032f }
            long r3 = r3 + r14
            java.util.ArrayList<com.facebook.react.bridge.ReadableMap> r6 = r0.files     // Catch:{ all -> 0x032f }
            java.lang.Object[] r6 = r6.toArray()     // Catch:{ all -> 0x032f }
            int r6 = r6.length     // Catch:{ all -> 0x032f }
            int r6 = r6 * 2
            long r6 = (long) r6     // Catch:{ all -> 0x032f }
            long r3 = r3 + r6
            java.lang.String r6 = "Content-length"
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ all -> 0x032f }
            r7.<init>()     // Catch:{ all -> 0x032f }
            java.lang.String r8 = ""
            r7.append(r8)     // Catch:{ all -> 0x032f }
            int r3 = (int) r3     // Catch:{ all -> 0x032f }
            r7.append(r3)     // Catch:{ all -> 0x032f }
            java.lang.String r4 = r7.toString()     // Catch:{ all -> 0x032f }
            r9 = r19
            r9.setRequestProperty(r6, r4)     // Catch:{ all -> 0x0333 }
            r9.setFixedLengthStreamingMode(r3)     // Catch:{ all -> 0x0333 }
            r9.connect()     // Catch:{ all -> 0x0333 }
            java.io.DataOutputStream r8 = new java.io.DataOutputStream     // Catch:{ all -> 0x0333 }
            java.io.OutputStream r3 = r9.getOutputStream()     // Catch:{ all -> 0x0333 }
            r8.<init>(r3)     // Catch:{ all -> 0x0333 }
            r6 = r18
            r8.writeBytes(r6)     // Catch:{ all -> 0x032b }
            java.lang.Runtime r3 = java.lang.Runtime.getRuntime()     // Catch:{ all -> 0x032b }
            java.util.ArrayList<com.facebook.react.bridge.ReadableMap> r0 = r0.files     // Catch:{ all -> 0x032b }
            java.util.Iterator r0 = r0.iterator()     // Catch:{ all -> 0x032b }
            r4 = 0
            r6 = 0
        L_0x0226:
            boolean r7 = r0.hasNext()     // Catch:{ all -> 0x032b }
            if (r7 == 0) goto L_0x02a1
            java.lang.Object r7 = r0.next()     // Catch:{ all -> 0x032b }
            com.facebook.react.bridge.ReadableMap r7 = (com.facebook.react.bridge.ReadableMap) r7     // Catch:{ all -> 0x032b }
            r10 = r11[r4]     // Catch:{ all -> 0x032b }
            r8.writeBytes(r10)     // Catch:{ all -> 0x032b }
            java.io.File r10 = new java.io.File     // Catch:{ all -> 0x032b }
            java.lang.String r12 = "filepath"
            java.lang.String r7 = r7.getString(r12)     // Catch:{ all -> 0x032b }
            r10.<init>(r7)     // Catch:{ all -> 0x032b }
            long r12 = r10.length()     // Catch:{ all -> 0x032b }
            int r7 = (int) r12     // Catch:{ all -> 0x032b }
            java.io.BufferedInputStream r12 = new java.io.BufferedInputStream     // Catch:{ all -> 0x032b }
            java.io.FileInputStream r13 = new java.io.FileInputStream     // Catch:{ all -> 0x032b }
            r13.<init>(r10)     // Catch:{ all -> 0x032b }
            r12.<init>(r13)     // Catch:{ all -> 0x032b }
            float r7 = (float) r7     // Catch:{ all -> 0x032b }
            r10 = 1120403456(0x42c80000, float:100.0)
            float r7 = r7 / r10
            r22 = r6
            double r6 = (double) r7     // Catch:{ all -> 0x032b }
            double r6 = java.lang.Math.ceil(r6)     // Catch:{ all -> 0x032b }
            int r6 = (int) r6     // Catch:{ all -> 0x032b }
            float r7 = (float) r6     // Catch:{ all -> 0x032b }
            r13 = r11
            long r10 = r3.freeMemory()     // Catch:{ all -> 0x032b }
            float r10 = (float) r10     // Catch:{ all -> 0x032b }
            r11 = 1092616192(0x41200000, float:10.0)
            float r10 = r10 / r11
            int r7 = (r7 > r10 ? 1 : (r7 == r10 ? 0 : -1))
            if (r7 <= 0) goto L_0x0277
            long r6 = r3.freeMemory()     // Catch:{ all -> 0x032b }
            float r6 = (float) r6     // Catch:{ all -> 0x032b }
            float r6 = r6 / r11
            double r6 = (double) r6     // Catch:{ all -> 0x032b }
            double r6 = java.lang.Math.ceil(r6)     // Catch:{ all -> 0x032b }
            int r6 = (int) r6     // Catch:{ all -> 0x032b }
        L_0x0277:
            byte[] r6 = new byte[r6]     // Catch:{ all -> 0x032b }
            r7 = r22
        L_0x027b:
            int r10 = r12.read(r6)     // Catch:{ all -> 0x032b }
            r11 = -1
            if (r10 == r11) goto L_0x0296
            r11 = 0
            r8.write(r6, r11, r10)     // Catch:{ all -> 0x032b }
            int r7 = r7 + r10
            com.rnfs.UploadParams r10 = r1.mParams     // Catch:{ all -> 0x032b }
            com.rnfs.UploadParams$onUploadProgress r10 = r10.onUploadProgress     // Catch:{ all -> 0x032b }
            int r11 = (int) r14     // Catch:{ all -> 0x032b }
            int r16 = r5.length()     // Catch:{ all -> 0x032b }
            int r11 = r11 - r16
            r10.onUploadProgress(r11, r7)     // Catch:{ all -> 0x032b }
            goto L_0x027b
        L_0x0296:
            r8.writeBytes(r2)     // Catch:{ all -> 0x032b }
            int r4 = r4 + 1
            r12.close()     // Catch:{ all -> 0x032b }
            r6 = r7
            r11 = r13
            goto L_0x0226
        L_0x02a1:
            r8.writeBytes(r5)     // Catch:{ all -> 0x032b }
            r8.flush()     // Catch:{ all -> 0x032b }
            r8.close()     // Catch:{ all -> 0x032b }
            java.io.BufferedInputStream r2 = new java.io.BufferedInputStream     // Catch:{ all -> 0x032b }
            java.io.InputStream r0 = r9.getInputStream()     // Catch:{ all -> 0x032b }
            r2.<init>(r0)     // Catch:{ all -> 0x032b }
            java.io.BufferedReader r3 = new java.io.BufferedReader     // Catch:{ all -> 0x0329 }
            java.io.InputStreamReader r0 = new java.io.InputStreamReader     // Catch:{ all -> 0x0329 }
            r0.<init>(r2)     // Catch:{ all -> 0x0329 }
            r3.<init>(r0)     // Catch:{ all -> 0x0329 }
            com.facebook.react.bridge.WritableMap r0 = com.facebook.react.bridge.Arguments.createMap()     // Catch:{ all -> 0x0327 }
            java.util.Map r4 = r9.getHeaderFields()     // Catch:{ all -> 0x0327 }
            java.util.Set r4 = r4.entrySet()     // Catch:{ all -> 0x0327 }
            java.util.Iterator r4 = r4.iterator()     // Catch:{ all -> 0x0327 }
        L_0x02cd:
            boolean r5 = r4.hasNext()     // Catch:{ all -> 0x0327 }
            if (r5 == 0) goto L_0x02f0
            java.lang.Object r5 = r4.next()     // Catch:{ all -> 0x0327 }
            java.util.Map$Entry r5 = (java.util.Map.Entry) r5     // Catch:{ all -> 0x0327 }
            java.lang.Object r6 = r5.getKey()     // Catch:{ all -> 0x0327 }
            java.lang.String r6 = (java.lang.String) r6     // Catch:{ all -> 0x0327 }
            java.lang.Object r5 = r5.getValue()     // Catch:{ all -> 0x0327 }
            java.util.List r5 = (java.util.List) r5     // Catch:{ all -> 0x0327 }
            r7 = 0
            java.lang.Object r5 = r5.get(r7)     // Catch:{ all -> 0x0327 }
            java.lang.String r5 = (java.lang.String) r5     // Catch:{ all -> 0x0327 }
            r0.putString(r6, r5)     // Catch:{ all -> 0x0327 }
            goto L_0x02cd
        L_0x02f0:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x0327 }
            r4.<init>()     // Catch:{ all -> 0x0327 }
        L_0x02f5:
            java.lang.String r5 = r3.readLine()     // Catch:{ all -> 0x0327 }
            if (r5 == 0) goto L_0x0304
            r4.append(r5)     // Catch:{ all -> 0x0327 }
            java.lang.String r5 = "\n"
            r4.append(r5)     // Catch:{ all -> 0x0327 }
            goto L_0x02f5
        L_0x0304:
            java.lang.String r4 = r4.toString()     // Catch:{ all -> 0x0327 }
            int r5 = r9.getResponseCode()     // Catch:{ all -> 0x0327 }
            com.rnfs.UploadResult r6 = r1.res     // Catch:{ all -> 0x0327 }
            r6.headers = r0     // Catch:{ all -> 0x0327 }
            com.rnfs.UploadResult r0 = r1.res     // Catch:{ all -> 0x0327 }
            r0.body = r4     // Catch:{ all -> 0x0327 }
            com.rnfs.UploadResult r0 = r1.res     // Catch:{ all -> 0x0327 }
            r0.statusCode = r5     // Catch:{ all -> 0x0327 }
            if (r9 == 0) goto L_0x031d
            r9.disconnect()
        L_0x031d:
            r8.close()
            r2.close()
            r3.close()
            return
        L_0x0327:
            r0 = move-exception
            goto L_0x033d
        L_0x0329:
            r0 = move-exception
            goto L_0x032d
        L_0x032b:
            r0 = move-exception
            r2 = 0
        L_0x032d:
            r3 = 0
            goto L_0x033d
        L_0x032f:
            r0 = move-exception
            r9 = r19
            goto L_0x0334
        L_0x0333:
            r0 = move-exception
        L_0x0334:
            r2 = 0
            r3 = 0
            r8 = 0
            goto L_0x033d
        L_0x0338:
            r0 = move-exception
            r2 = 0
            r3 = 0
            r8 = 0
            r9 = 0
        L_0x033d:
            if (r9 == 0) goto L_0x0342
            r9.disconnect()
        L_0x0342:
            if (r8 == 0) goto L_0x0347
            r8.close()
        L_0x0347:
            if (r2 == 0) goto L_0x034c
            r2.close()
        L_0x034c:
            if (r3 == 0) goto L_0x0351
            r3.close()
        L_0x0351:
            throw r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.rnfs.Uploader.upload(com.rnfs.UploadParams, com.rnfs.UploadResult):void");
    }

    /* access modifiers changed from: protected */
    public String getMimeType(String str) {
        String fileExtensionFromUrl = MimeTypeMap.getFileExtensionFromUrl(str);
        String mimeTypeFromExtension = fileExtensionFromUrl != null ? MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtensionFromUrl.toLowerCase()) : null;
        return mimeTypeFromExtension == null ? "*/*" : mimeTypeFromExtension;
    }

    /* access modifiers changed from: protected */
    public void stop() {
        this.mAbort.set(true);
    }
}

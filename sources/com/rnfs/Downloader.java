package com.rnfs;

import android.os.AsyncTask;
import android.os.Build.VERSION;
import java.net.HttpURLConnection;
import java.util.concurrent.atomic.AtomicBoolean;

public class Downloader extends AsyncTask<DownloadParams, long[], DownloadResult> {
    private AtomicBoolean mAbort = new AtomicBoolean(false);
    /* access modifiers changed from: private */
    public DownloadParams mParam;
    DownloadResult res;

    /* access modifiers changed from: protected */
    public void onPostExecute(Exception exc) {
    }

    /* access modifiers changed from: protected */
    public DownloadResult doInBackground(DownloadParams... downloadParamsArr) {
        this.mParam = downloadParamsArr[0];
        this.res = new DownloadResult();
        new Thread(new Runnable() {
            public void run() {
                try {
                    Downloader.this.download(Downloader.this.mParam, Downloader.this.res);
                    Downloader.this.mParam.onTaskCompleted.onTaskCompleted(Downloader.this.res);
                } catch (Exception e) {
                    Downloader.this.res.exception = e;
                    Downloader.this.mParam.onTaskCompleted.onTaskCompleted(Downloader.this.res);
                }
            }
        }).start();
        return this.res;
    }

    /* JADX WARNING: type inference failed for: r3v0 */
    /* JADX WARNING: type inference failed for: r3v1, types: [java.io.OutputStream] */
    /* JADX WARNING: type inference failed for: r9v0 */
    /* JADX WARNING: type inference failed for: r3v2 */
    /* JADX WARNING: type inference failed for: r3v3, types: [java.io.OutputStream] */
    /* JADX WARNING: type inference failed for: r11v3, types: [java.io.FileOutputStream] */
    /* JADX WARNING: type inference failed for: r9v1 */
    /* JADX WARNING: type inference failed for: r3v4 */
    /* JADX WARNING: type inference failed for: r9v2 */
    /* JADX WARNING: type inference failed for: r11v4 */
    /* JADX WARNING: type inference failed for: r9v3, types: [int] */
    /* JADX WARNING: type inference failed for: r9v4, types: [java.io.OutputStream] */
    /* JADX WARNING: type inference failed for: r3v6 */
    /* JADX WARNING: type inference failed for: r9v5 */
    /* JADX WARNING: type inference failed for: r20v0 */
    /* JADX WARNING: type inference failed for: r9v6 */
    /* JADX WARNING: type inference failed for: r9v7 */
    /* JADX WARNING: type inference failed for: r9v8, types: [java.io.OutputStream] */
    /* JADX WARNING: type inference failed for: r11v6 */
    /* JADX WARNING: type inference failed for: r9v9 */
    /* JADX WARNING: type inference failed for: r9v10 */
    /* JADX WARNING: type inference failed for: r9v16 */
    /* JADX WARNING: type inference failed for: r3v7 */
    /* JADX WARNING: type inference failed for: r3v8 */
    /* JADX WARNING: type inference failed for: r3v9 */
    /* JADX WARNING: type inference failed for: r3v10 */
    /* JADX WARNING: type inference failed for: r3v11 */
    /* JADX WARNING: type inference failed for: r3v12 */
    /* JADX WARNING: type inference failed for: r3v13 */
    /* JADX WARNING: type inference failed for: r9v17 */
    /* JADX WARNING: type inference failed for: r3v14 */
    /* JADX WARNING: type inference failed for: r3v15 */
    /* JADX WARNING: type inference failed for: r3v16 */
    /* JADX WARNING: type inference failed for: r11v22 */
    /* JADX WARNING: type inference failed for: r9v18 */
    /* JADX WARNING: type inference failed for: r9v19 */
    /* JADX WARNING: type inference failed for: r9v20 */
    /* JADX WARNING: type inference failed for: r9v21 */
    /* access modifiers changed from: private */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r3v2
      assigns: []
      uses: []
      mth insns count: 202
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Removed duplicated region for block: B:100:0x01c8  */
    /* JADX WARNING: Removed duplicated region for block: B:102:0x01cd  */
    /* JADX WARNING: Removed duplicated region for block: B:104:0x01d2  */
    /* JADX WARNING: Unknown variable types count: 17 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void download(com.rnfs.DownloadParams r26, com.rnfs.DownloadResult r27) throws java.lang.Exception {
        /*
            r25 = this;
            r1 = r25
            r0 = r26
            r2 = r27
            r3 = 0
            java.net.URL r4 = r0.src     // Catch:{ all -> 0x01c3 }
            java.net.URLConnection r4 = r4.openConnection()     // Catch:{ all -> 0x01c3 }
            java.net.HttpURLConnection r4 = (java.net.HttpURLConnection) r4     // Catch:{ all -> 0x01c3 }
            com.facebook.react.bridge.ReadableMap r5 = r0.headers     // Catch:{ all -> 0x01bf }
            com.facebook.react.bridge.ReadableMapKeySetIterator r5 = r5.keySetIterator()     // Catch:{ all -> 0x01bf }
        L_0x0015:
            boolean r6 = r5.hasNextKey()     // Catch:{ all -> 0x01bf }
            if (r6 == 0) goto L_0x0029
            java.lang.String r6 = r5.nextKey()     // Catch:{ all -> 0x01bf }
            com.facebook.react.bridge.ReadableMap r7 = r0.headers     // Catch:{ all -> 0x01bf }
            java.lang.String r7 = r7.getString(r6)     // Catch:{ all -> 0x01bf }
            r4.setRequestProperty(r6, r7)     // Catch:{ all -> 0x01bf }
            goto L_0x0015
        L_0x0029:
            int r5 = r0.connectionTimeout     // Catch:{ all -> 0x01bf }
            r4.setConnectTimeout(r5)     // Catch:{ all -> 0x01bf }
            int r5 = r0.readTimeout     // Catch:{ all -> 0x01bf }
            r4.setReadTimeout(r5)     // Catch:{ all -> 0x01bf }
            r4.connect()     // Catch:{ all -> 0x01bf }
            int r5 = r4.getResponseCode()     // Catch:{ all -> 0x01bf }
            long r6 = r1.getContentLength(r4)     // Catch:{ all -> 0x01bf }
            r8 = 200(0xc8, float:2.8E-43)
            r9 = 1
            r10 = 0
            if (r5 == r8) goto L_0x0056
            r11 = 301(0x12d, float:4.22E-43)
            if (r5 == r11) goto L_0x0054
            r11 = 302(0x12e, float:4.23E-43)
            if (r5 == r11) goto L_0x0054
            r11 = 307(0x133, float:4.3E-43)
            if (r5 == r11) goto L_0x0054
            r11 = 308(0x134, float:4.32E-43)
            if (r5 != r11) goto L_0x0056
        L_0x0054:
            r11 = 1
            goto L_0x0057
        L_0x0056:
            r11 = 0
        L_0x0057:
            if (r11 == 0) goto L_0x007e
            java.lang.String r5 = "Location"
            java.lang.String r5 = r4.getHeaderField(r5)     // Catch:{ all -> 0x01bf }
            r4.disconnect()     // Catch:{ all -> 0x01bf }
            java.net.URL r6 = new java.net.URL     // Catch:{ all -> 0x01bf }
            r6.<init>(r5)     // Catch:{ all -> 0x01bf }
            java.net.URLConnection r5 = r6.openConnection()     // Catch:{ all -> 0x01bf }
            java.net.HttpURLConnection r5 = (java.net.HttpURLConnection) r5     // Catch:{ all -> 0x01bf }
            r4 = 5000(0x1388, float:7.006E-42)
            r5.setConnectTimeout(r4)     // Catch:{ all -> 0x01a7 }
            r5.connect()     // Catch:{ all -> 0x01a7 }
            int r4 = r5.getResponseCode()     // Catch:{ all -> 0x01a7 }
            long r6 = r1.getContentLength(r5)     // Catch:{ all -> 0x01a7 }
            goto L_0x0083
        L_0x007e:
            r24 = r5
            r5 = r4
            r4 = r24
        L_0x0083:
            if (r4 < r8) goto L_0x01aa
            r8 = 300(0x12c, float:4.2E-43)
            if (r4 >= r8) goto L_0x01aa
            java.util.Map r8 = r5.getHeaderFields()     // Catch:{ all -> 0x01a7 }
            java.util.HashMap r11 = new java.util.HashMap     // Catch:{ all -> 0x01a7 }
            r11.<init>()     // Catch:{ all -> 0x01a7 }
            java.util.Set r8 = r8.entrySet()     // Catch:{ all -> 0x01a7 }
            java.util.Iterator r8 = r8.iterator()     // Catch:{ all -> 0x01a7 }
        L_0x009a:
            boolean r12 = r8.hasNext()     // Catch:{ all -> 0x01a7 }
            if (r12 == 0) goto L_0x00c0
            java.lang.Object r12 = r8.next()     // Catch:{ all -> 0x01a7 }
            java.util.Map$Entry r12 = (java.util.Map.Entry) r12     // Catch:{ all -> 0x01a7 }
            java.lang.Object r13 = r12.getKey()     // Catch:{ all -> 0x01a7 }
            java.lang.String r13 = (java.lang.String) r13     // Catch:{ all -> 0x01a7 }
            java.lang.Object r12 = r12.getValue()     // Catch:{ all -> 0x01a7 }
            java.util.List r12 = (java.util.List) r12     // Catch:{ all -> 0x01a7 }
            java.lang.Object r12 = r12.get(r10)     // Catch:{ all -> 0x01a7 }
            java.lang.String r12 = (java.lang.String) r12     // Catch:{ all -> 0x01a7 }
            if (r13 == 0) goto L_0x009a
            if (r12 == 0) goto L_0x009a
            r11.put(r13, r12)     // Catch:{ all -> 0x01a7 }
            goto L_0x009a
        L_0x00c0:
            com.rnfs.DownloadParams r8 = r1.mParam     // Catch:{ all -> 0x01a7 }
            com.rnfs.DownloadParams$OnDownloadBegin r8 = r8.onDownloadBegin     // Catch:{ all -> 0x01a7 }
            r8.onDownloadBegin(r4, r6, r11)     // Catch:{ all -> 0x01a7 }
            java.io.BufferedInputStream r8 = new java.io.BufferedInputStream     // Catch:{ all -> 0x01a7 }
            java.io.InputStream r11 = r5.getInputStream()     // Catch:{ all -> 0x01a7 }
            r12 = 8192(0x2000, float:1.14794E-41)
            r8.<init>(r11, r12)     // Catch:{ all -> 0x01a7 }
            java.io.FileOutputStream r11 = new java.io.FileOutputStream     // Catch:{ all -> 0x01bd }
            java.io.File r13 = r0.dest     // Catch:{ all -> 0x01bd }
            r11.<init>(r13)     // Catch:{ all -> 0x01bd }
            byte[] r3 = new byte[r12]     // Catch:{ all -> 0x01a3 }
            r12 = 0
            r16 = 0
        L_0x00df:
            int r14 = r8.read(r3)     // Catch:{ all -> 0x01a3 }
            r15 = -1
            if (r14 == r15) goto L_0x0199
            java.util.concurrent.atomic.AtomicBoolean r15 = r1.mAbort     // Catch:{ all -> 0x01a3 }
            boolean r15 = r15.get()     // Catch:{ all -> 0x01a3 }
            if (r15 != 0) goto L_0x0190
            r20 = r11
            long r10 = (long) r14
            long r12 = r12 + r10
            float r10 = r0.progressDivider     // Catch:{ all -> 0x018c }
            r11 = 0
            r15 = 2
            int r10 = (r10 > r11 ? 1 : (r10 == r11 ? 0 : -1))
            if (r10 > 0) goto L_0x0114
            long[][] r10 = new long[r9][]     // Catch:{ all -> 0x010f }
            long[] r11 = new long[r15]     // Catch:{ all -> 0x010f }
            r15 = 0
            r11[r15] = r6     // Catch:{ all -> 0x010f }
            r11[r9] = r12     // Catch:{ all -> 0x010f }
            r10[r15] = r11     // Catch:{ all -> 0x010f }
            r1.publishProgress(r10)     // Catch:{ all -> 0x010f }
            r21 = r12
            r11 = 1
            r18 = 0
            goto L_0x0180
        L_0x010f:
            r0 = move-exception
            r3 = r20
            goto L_0x01c6
        L_0x0114:
            double r9 = (double) r12
            r21 = 4636737291354636288(0x4059000000000000, double:100.0)
            java.lang.Double.isNaN(r9)
            double r9 = r9 * r21
            r21 = r12
            double r11 = (double) r6
            java.lang.Double.isNaN(r11)
            double r9 = r9 / r11
            long r9 = java.lang.Math.round(r9)     // Catch:{ all -> 0x018c }
            double r9 = (double) r9     // Catch:{ all -> 0x018c }
            float r11 = r0.progressDivider     // Catch:{ all -> 0x018c }
            double r11 = (double) r11
            java.lang.Double.isNaN(r9)
            java.lang.Double.isNaN(r11)
            double r11 = r9 % r11
            r18 = 0
            int r23 = (r11 > r18 ? 1 : (r11 == r18 ? 0 : -1))
            if (r23 != 0) goto L_0x017f
            int r11 = (r9 > r16 ? 1 : (r9 == r16 ? 0 : -1))
            if (r11 != 0) goto L_0x0144
            int r11 = (r21 > r6 ? 1 : (r21 == r6 ? 0 : -1))
            if (r11 != 0) goto L_0x0142
            goto L_0x0144
        L_0x0142:
            r11 = 1
            goto L_0x0180
        L_0x0144:
            java.lang.String r11 = "Downloader"
            java.lang.StringBuilder r12 = new java.lang.StringBuilder     // Catch:{ all -> 0x010f }
            r12.<init>()     // Catch:{ all -> 0x010f }
            java.lang.String r13 = "EMIT: "
            r12.append(r13)     // Catch:{ all -> 0x010f }
            java.lang.String r13 = java.lang.String.valueOf(r9)     // Catch:{ all -> 0x010f }
            r12.append(r13)     // Catch:{ all -> 0x010f }
            java.lang.String r13 = ", TOTAL:"
            r12.append(r13)     // Catch:{ all -> 0x010f }
            java.lang.String r13 = java.lang.String.valueOf(r21)     // Catch:{ all -> 0x010f }
            r12.append(r13)     // Catch:{ all -> 0x010f }
            java.lang.String r12 = r12.toString()     // Catch:{ all -> 0x010f }
            android.util.Log.d(r11, r12)     // Catch:{ all -> 0x010f }
            r11 = 1
            long[][] r12 = new long[r11][]     // Catch:{ all -> 0x010f }
            long[] r13 = new long[r15]     // Catch:{ all -> 0x010f }
            r15 = 0
            r13[r15] = r6     // Catch:{ all -> 0x010f }
            r13[r11] = r21     // Catch:{ all -> 0x010f }
            r12[r15] = r13     // Catch:{ all -> 0x010f }
            r1.publishProgress(r12)     // Catch:{ all -> 0x010f }
            r16 = r9
            r9 = r20
            r10 = 0
            goto L_0x0183
        L_0x017f:
            r11 = 1
        L_0x0180:
            r9 = r20
            r10 = 0
        L_0x0183:
            r9.write(r3, r10, r14)     // Catch:{ all -> 0x01a1 }
            r11 = r9
            r12 = r21
            r9 = 1
            goto L_0x00df
        L_0x018c:
            r0 = move-exception
            r9 = r20
            goto L_0x01a5
        L_0x0190:
            r9 = r11
            java.lang.Exception r0 = new java.lang.Exception     // Catch:{ all -> 0x01a1 }
            java.lang.String r2 = "Download has been aborted"
            r0.<init>(r2)     // Catch:{ all -> 0x01a1 }
            throw r0     // Catch:{ all -> 0x01a1 }
        L_0x0199:
            r9 = r11
            r9.flush()     // Catch:{ all -> 0x01a1 }
            r2.bytesWritten = r12     // Catch:{ all -> 0x01a1 }
            r3 = r9
            goto L_0x01ab
        L_0x01a1:
            r0 = move-exception
            goto L_0x01a5
        L_0x01a3:
            r0 = move-exception
            r9 = r11
        L_0x01a5:
            r3 = r9
            goto L_0x01c6
        L_0x01a7:
            r0 = move-exception
            r8 = r3
            goto L_0x01c6
        L_0x01aa:
            r8 = r3
        L_0x01ab:
            r2.statusCode = r4     // Catch:{ all -> 0x01bd }
            if (r3 == 0) goto L_0x01b2
            r3.close()
        L_0x01b2:
            if (r8 == 0) goto L_0x01b7
            r8.close()
        L_0x01b7:
            if (r5 == 0) goto L_0x01bc
            r5.disconnect()
        L_0x01bc:
            return
        L_0x01bd:
            r0 = move-exception
            goto L_0x01c6
        L_0x01bf:
            r0 = move-exception
            r8 = r3
            r5 = r4
            goto L_0x01c6
        L_0x01c3:
            r0 = move-exception
            r5 = r3
            r8 = r5
        L_0x01c6:
            if (r3 == 0) goto L_0x01cb
            r3.close()
        L_0x01cb:
            if (r8 == 0) goto L_0x01d0
            r8.close()
        L_0x01d0:
            if (r5 == 0) goto L_0x01d5
            r5.disconnect()
        L_0x01d5:
            throw r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.rnfs.Downloader.download(com.rnfs.DownloadParams, com.rnfs.DownloadResult):void");
    }

    private long getContentLength(HttpURLConnection httpURLConnection) {
        if (VERSION.SDK_INT >= 24) {
            return httpURLConnection.getContentLengthLong();
        }
        return (long) httpURLConnection.getContentLength();
    }

    /* access modifiers changed from: protected */
    public void stop() {
        this.mAbort.set(true);
    }

    /* access modifiers changed from: protected */
    public void onProgressUpdate(long[]... jArr) {
        super.onProgressUpdate(jArr);
        this.mParam.onDownloadProgress.onDownloadProgress(jArr[0][0], jArr[0][1]);
    }
}

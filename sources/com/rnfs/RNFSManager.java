package com.rnfs;

import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.StatFs;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.SparseArray;
import com.RNFetchBlob.RNFetchBlobConst;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.RCTNativeAppEventEmitter;
import com.google.android.gms.gcm.Task;
import com.google.android.gms.measurement.api.AppMeasurementSdk.ConditionalUserProperty;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import com.rnfs.DownloadParams.OnDownloadBegin;
import com.rnfs.DownloadParams.OnDownloadProgress;
import com.rnfs.DownloadParams.OnTaskCompleted;
import com.rnfs.UploadParams.onUploadBegin;
import com.rnfs.UploadParams.onUploadComplete;
import com.rnfs.UploadParams.onUploadProgress;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class RNFSManager extends ReactContextBaseJavaModule {
    private static final String RNFSCachesDirectoryPath = "RNFSCachesDirectoryPath";
    private static final String RNFSDocumentDirectory = "RNFSDocumentDirectory";
    private static final String RNFSDocumentDirectoryPath = "RNFSDocumentDirectoryPath";
    private static final String RNFSExternalCachesDirectoryPath = "RNFSExternalCachesDirectoryPath";
    private static final String RNFSExternalDirectoryPath = "RNFSExternalDirectoryPath";
    private static final String RNFSExternalStorageDirectoryPath = "RNFSExternalStorageDirectoryPath";
    private static final String RNFSFileTypeDirectory = "RNFSFileTypeDirectory";
    private static final String RNFSFileTypeRegular = "RNFSFileTypeRegular";
    private static final String RNFSPicturesDirectoryPath = "RNFSPicturesDirectoryPath";
    private static final String RNFSTemporaryDirectoryPath = "RNFSTemporaryDirectoryPath";
    private SparseArray<Downloader> downloaders = new SparseArray<>();
    private ReactApplicationContext reactContext;
    private SparseArray<Uploader> uploaders = new SparseArray<>();

    private class CopyFileTask extends AsyncTask<String, Void, Exception> {
        private CopyFileTask() {
        }

        /* access modifiers changed from: protected */
        public Exception doInBackground(String... strArr) {
            try {
                String str = strArr[0];
                String str2 = strArr[1];
                InputStream access$200 = RNFSManager.this.getInputStream(str);
                OutputStream access$300 = RNFSManager.this.getOutputStream(str2, false);
                byte[] bArr = new byte[1024];
                while (true) {
                    int read = access$200.read(bArr);
                    if (read > 0) {
                        access$300.write(bArr, 0, read);
                        Thread.yield();
                    } else {
                        access$200.close();
                        access$300.close();
                        return null;
                    }
                }
            } catch (Exception e) {
                return e;
            }
        }
    }

    public String getName() {
        return "RNFSManager";
    }

    @ReactMethod
    public void pathForBundle(String str, Promise promise) {
    }

    @ReactMethod
    public void pathForGroup(String str, Promise promise) {
    }

    public RNFSManager(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
        this.reactContext = reactApplicationContext;
    }

    private Uri getFileUri(String str, boolean z) throws IORejectionException {
        Uri parse = Uri.parse(str);
        if (parse.getScheme() != null) {
            return parse;
        }
        File file = new File(str);
        if (z || !file.isDirectory()) {
            StringBuilder sb = new StringBuilder();
            sb.append("file://");
            sb.append(str);
            return Uri.parse(sb.toString());
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("EISDIR: illegal operation on a directory, read '");
        sb2.append(str);
        sb2.append("'");
        throw new IORejectionException("EISDIR", sb2.toString());
    }

    private String getOriginalFilepath(String str, boolean z) throws IORejectionException {
        Uri fileUri = getFileUri(str, z);
        if (!fileUri.getScheme().equals("content")) {
            return str;
        }
        try {
            Cursor query = this.reactContext.getContentResolver().query(fileUri, null, null, null, null);
            return query.moveToFirst() ? query.getString(query.getColumnIndexOrThrow("_data")) : str;
        } catch (IllegalArgumentException unused) {
            return str;
        }
    }

    /* access modifiers changed from: private */
    public InputStream getInputStream(String str) throws IORejectionException {
        try {
            InputStream openInputStream = this.reactContext.getContentResolver().openInputStream(getFileUri(str, false));
            if (openInputStream != null) {
                return openInputStream;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("ENOENT: could not open an input stream for '");
            sb.append(str);
            sb.append("'");
            throw new IORejectionException("ENOENT", sb.toString());
        } catch (FileNotFoundException unused) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("ENOENT: no such file or directory, open '");
            sb2.append(str);
            sb2.append("'");
            throw new IORejectionException("ENOENT", sb2.toString());
        }
    }

    /* access modifiers changed from: private */
    public OutputStream getOutputStream(String str, boolean z) throws IORejectionException {
        try {
            OutputStream openOutputStream = this.reactContext.getContentResolver().openOutputStream(getFileUri(str, false), z ? "wa" : "w");
            if (openOutputStream != null) {
                return openOutputStream;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("ENOENT: could not open an output stream for '");
            sb.append(str);
            sb.append("'");
            throw new IORejectionException("ENOENT", sb.toString());
        } catch (FileNotFoundException unused) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("ENOENT: no such file or directory, open '");
            sb2.append(str);
            sb2.append("'");
            throw new IORejectionException("ENOENT", sb2.toString());
        }
    }

    private static byte[] getInputStreamBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[1024];
        while (true) {
            try {
                int read = inputStream.read(bArr);
                if (read == -1) {
                    break;
                }
                byteArrayOutputStream.write(bArr, 0, read);
            } finally {
                try {
                    byteArrayOutputStream.close();
                } catch (IOException unused) {
                }
            }
        }
        return byteArrayOutputStream.toByteArray();
    }

    @ReactMethod
    public void writeFile(String str, String str2, ReadableMap readableMap, Promise promise) {
        try {
            byte[] decode = Base64.decode(str2, 0);
            OutputStream outputStream = getOutputStream(str, false);
            outputStream.write(decode);
            outputStream.close();
            promise.resolve(null);
        } catch (Exception e) {
            e.printStackTrace();
            reject(promise, str, e);
        }
    }

    @ReactMethod
    public void appendFile(String str, String str2, Promise promise) {
        try {
            byte[] decode = Base64.decode(str2, 0);
            OutputStream outputStream = getOutputStream(str, true);
            outputStream.write(decode);
            outputStream.close();
            promise.resolve(null);
        } catch (Exception e) {
            e.printStackTrace();
            reject(promise, str, e);
        }
    }

    @ReactMethod
    public void write(String str, String str2, int i, Promise promise) {
        try {
            byte[] decode = Base64.decode(str2, 0);
            if (i < 0) {
                OutputStream outputStream = getOutputStream(str, true);
                outputStream.write(decode);
                outputStream.close();
            } else {
                RandomAccessFile randomAccessFile = new RandomAccessFile(str, "rw");
                randomAccessFile.seek((long) i);
                randomAccessFile.write(decode);
                randomAccessFile.close();
            }
            promise.resolve(null);
        } catch (Exception e) {
            e.printStackTrace();
            reject(promise, str, e);
        }
    }

    @ReactMethod
    public void exists(String str, Promise promise) {
        try {
            promise.resolve(Boolean.valueOf(new File(str).exists()));
        } catch (Exception e) {
            e.printStackTrace();
            reject(promise, str, e);
        }
    }

    @ReactMethod
    public void readFile(String str, Promise promise) {
        try {
            promise.resolve(Base64.encodeToString(getInputStreamBytes(getInputStream(str)), 2));
        } catch (Exception e) {
            e.printStackTrace();
            reject(promise, str, e);
        }
    }

    @ReactMethod
    public void read(String str, int i, int i2, Promise promise) {
        try {
            InputStream inputStream = getInputStream(str);
            byte[] bArr = new byte[i];
            inputStream.skip((long) i2);
            promise.resolve(Base64.encodeToString(bArr, 0, inputStream.read(bArr, 0, i), 2));
        } catch (Exception e) {
            e.printStackTrace();
            reject(promise, str, e);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0031, code lost:
        if (r0 != null) goto L_0x0033;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:?, code lost:
        r0.close();
     */
    @com.facebook.react.bridge.ReactMethod
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void readFileAssets(java.lang.String r4, com.facebook.react.bridge.Promise r5) {
        /*
            r3 = this;
            r0 = 0
            com.facebook.react.bridge.ReactApplicationContext r1 = r3.getReactApplicationContext()     // Catch:{ Exception -> 0x0039 }
            android.content.res.AssetManager r1 = r1.getAssets()     // Catch:{ Exception -> 0x0039 }
            r2 = 0
            java.io.InputStream r0 = r1.open(r4, r2)     // Catch:{ Exception -> 0x0039 }
            if (r0 != 0) goto L_0x0020
            java.lang.Exception r1 = new java.lang.Exception     // Catch:{ Exception -> 0x0039 }
            java.lang.String r2 = "Failed to open file"
            r1.<init>(r2)     // Catch:{ Exception -> 0x0039 }
            r3.reject(r5, r4, r1)     // Catch:{ Exception -> 0x0039 }
            if (r0 == 0) goto L_0x001f
            r0.close()     // Catch:{ IOException -> 0x001f }
        L_0x001f:
            return
        L_0x0020:
            int r1 = r0.available()     // Catch:{ Exception -> 0x0039 }
            byte[] r1 = new byte[r1]     // Catch:{ Exception -> 0x0039 }
            r0.read(r1)     // Catch:{ Exception -> 0x0039 }
            r2 = 2
            java.lang.String r1 = android.util.Base64.encodeToString(r1, r2)     // Catch:{ Exception -> 0x0039 }
            r5.resolve(r1)     // Catch:{ Exception -> 0x0039 }
            if (r0 == 0) goto L_0x0043
        L_0x0033:
            r0.close()     // Catch:{ IOException -> 0x0043 }
            goto L_0x0043
        L_0x0037:
            r4 = move-exception
            goto L_0x0044
        L_0x0039:
            r1 = move-exception
            r1.printStackTrace()     // Catch:{ all -> 0x0037 }
            r3.reject(r5, r4, r1)     // Catch:{ all -> 0x0037 }
            if (r0 == 0) goto L_0x0043
            goto L_0x0033
        L_0x0043:
            return
        L_0x0044:
            if (r0 == 0) goto L_0x0049
            r0.close()     // Catch:{ IOException -> 0x0049 }
        L_0x0049:
            throw r4
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.rnfs.RNFSManager.readFileAssets(java.lang.String, com.facebook.react.bridge.Promise):void");
    }

    @ReactMethod
    public void hash(String str, String str2, Promise promise) {
        try {
            HashMap hashMap = new HashMap();
            hashMap.put("md5", "MD5");
            hashMap.put("sha1", "SHA-1");
            hashMap.put("sha224", "SHA-224");
            hashMap.put("sha256", "SHA-256");
            hashMap.put("sha384", "SHA-384");
            hashMap.put("sha512", "SHA-512");
            if (hashMap.containsKey(str2)) {
                File file = new File(str);
                if (file.isDirectory()) {
                    rejectFileIsDirectory(promise);
                } else if (!file.exists()) {
                    rejectFileNotFound(promise, str);
                } else {
                    MessageDigest instance = MessageDigest.getInstance((String) hashMap.get(str2));
                    FileInputStream fileInputStream = new FileInputStream(str);
                    byte[] bArr = new byte[Task.EXTRAS_LIMIT_BYTES];
                    while (true) {
                        int read = fileInputStream.read(bArr);
                        if (read == -1) {
                            break;
                        }
                        instance.update(bArr, 0, read);
                    }
                    StringBuilder sb = new StringBuilder();
                    for (byte valueOf : instance.digest()) {
                        sb.append(String.format("%02x", new Object[]{Byte.valueOf(valueOf)}));
                    }
                    promise.resolve(sb.toString());
                }
            } else {
                throw new Exception("Invalid hash algorithm");
            }
        } catch (Exception e) {
            e.printStackTrace();
            reject(promise, str, e);
        }
    }

    @ReactMethod
    public void moveFile(final String str, String str2, ReadableMap readableMap, final Promise promise) {
        try {
            final File file = new File(str);
            if (!file.renameTo(new File(str2))) {
                new CopyFileTask() {
                    /* access modifiers changed from: protected */
                    public void onPostExecute(Exception exc) {
                        if (exc == null) {
                            file.delete();
                            promise.resolve(Boolean.valueOf(true));
                            return;
                        }
                        exc.printStackTrace();
                        RNFSManager.this.reject(promise, str, exc);
                    }
                }.execute(new String[]{str, str2});
                return;
            }
            promise.resolve(Boolean.valueOf(true));
        } catch (Exception e) {
            e.printStackTrace();
            reject(promise, str, e);
        }
    }

    @ReactMethod
    public void copyFile(final String str, String str2, ReadableMap readableMap, final Promise promise) {
        new CopyFileTask() {
            /* access modifiers changed from: protected */
            public void onPostExecute(Exception exc) {
                if (exc == null) {
                    promise.resolve(null);
                    return;
                }
                exc.printStackTrace();
                RNFSManager.this.reject(promise, str, exc);
            }
        }.execute(new String[]{str, str2});
    }

    @ReactMethod
    public void readDir(String str, Promise promise) {
        try {
            File file = new File(str);
            if (file.exists()) {
                File[] listFiles = file.listFiles();
                WritableArray createArray = Arguments.createArray();
                for (File file2 : listFiles) {
                    WritableMap createMap = Arguments.createMap();
                    String str2 = "mtime";
                    double lastModified = (double) file2.lastModified();
                    Double.isNaN(lastModified);
                    createMap.putDouble(str2, lastModified / 1000.0d);
                    createMap.putString(ConditionalUserProperty.NAME, file2.getName());
                    createMap.putString(RNFetchBlobConst.RNFB_RESPONSE_PATH, file2.getAbsolutePath());
                    createMap.putInt("size", (int) file2.length());
                    createMap.putInt("type", file2.isDirectory() ? 1 : 0);
                    createArray.pushMap(createMap);
                }
                promise.resolve(createArray);
                return;
            }
            throw new Exception("Folder does not exist");
        } catch (Exception e) {
            e.printStackTrace();
            reject(promise, str, e);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x005e A[Catch:{ IOException -> 0x0079 }] */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0060 A[Catch:{ IOException -> 0x0079 }] */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x006a A[Catch:{ IOException -> 0x0079 }] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x006b A[Catch:{ IOException -> 0x0079 }] */
    @com.facebook.react.bridge.ReactMethod
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void readDirAssets(java.lang.String r13, com.facebook.react.bridge.Promise r14) {
        /*
            r12 = this;
            com.facebook.react.bridge.ReactApplicationContext r0 = r12.getReactApplicationContext()     // Catch:{ IOException -> 0x0079 }
            android.content.res.AssetManager r0 = r0.getAssets()     // Catch:{ IOException -> 0x0079 }
            java.lang.String[] r1 = r0.list(r13)     // Catch:{ IOException -> 0x0079 }
            com.facebook.react.bridge.WritableArray r2 = com.facebook.react.bridge.Arguments.createArray()     // Catch:{ IOException -> 0x0079 }
            int r3 = r1.length     // Catch:{ IOException -> 0x0079 }
            r4 = 0
            r5 = 0
        L_0x0013:
            if (r5 >= r3) goto L_0x0075
            r6 = r1[r5]     // Catch:{ IOException -> 0x0079 }
            com.facebook.react.bridge.WritableMap r7 = com.facebook.react.bridge.Arguments.createMap()     // Catch:{ IOException -> 0x0079 }
            java.lang.String r8 = "name"
            r7.putString(r8, r6)     // Catch:{ IOException -> 0x0079 }
            boolean r8 = r13.isEmpty()     // Catch:{ IOException -> 0x0079 }
            r9 = 1
            if (r8 == 0) goto L_0x0028
            goto L_0x0035
        L_0x0028:
            java.lang.String r8 = "%s/%s"
            r10 = 2
            java.lang.Object[] r10 = new java.lang.Object[r10]     // Catch:{ IOException -> 0x0079 }
            r10[r4] = r13     // Catch:{ IOException -> 0x0079 }
            r10[r9] = r6     // Catch:{ IOException -> 0x0079 }
            java.lang.String r6 = java.lang.String.format(r8, r10)     // Catch:{ IOException -> 0x0079 }
        L_0x0035:
            java.lang.String r8 = "path"
            r7.putString(r8, r6)     // Catch:{ IOException -> 0x0079 }
            android.content.res.AssetFileDescriptor r6 = r0.openFd(r6)     // Catch:{ IOException -> 0x004f }
            if (r6 == 0) goto L_0x004c
            long r10 = r6.getLength()     // Catch:{ IOException -> 0x004f }
            int r8 = (int) r10
            r6.close()     // Catch:{ IOException -> 0x004a }
            r6 = 0
            goto L_0x0061
        L_0x004a:
            r6 = move-exception
            goto L_0x0051
        L_0x004c:
            r6 = 1
            r8 = 0
            goto L_0x0061
        L_0x004f:
            r6 = move-exception
            r8 = 0
        L_0x0051:
            java.lang.String r6 = r6.getMessage()     // Catch:{ IOException -> 0x0079 }
            java.lang.String r10 = "compressed"
            int r6 = r6.indexOf(r10)     // Catch:{ IOException -> 0x0079 }
            r10 = -1
            if (r6 != r10) goto L_0x0060
            r6 = 1
            goto L_0x0061
        L_0x0060:
            r6 = 0
        L_0x0061:
            java.lang.String r10 = "size"
            r7.putInt(r10, r8)     // Catch:{ IOException -> 0x0079 }
            java.lang.String r8 = "type"
            if (r6 == 0) goto L_0x006b
            goto L_0x006c
        L_0x006b:
            r9 = 0
        L_0x006c:
            r7.putInt(r8, r9)     // Catch:{ IOException -> 0x0079 }
            r2.pushMap(r7)     // Catch:{ IOException -> 0x0079 }
            int r5 = r5 + 1
            goto L_0x0013
        L_0x0075:
            r14.resolve(r2)     // Catch:{ IOException -> 0x0079 }
            goto L_0x007d
        L_0x0079:
            r0 = move-exception
            r12.reject(r14, r13, r0)
        L_0x007d:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.rnfs.RNFSManager.readDirAssets(java.lang.String, com.facebook.react.bridge.Promise):void");
    }

    @ReactMethod
    public void copyFileAssets(String str, String str2, Promise promise) {
        try {
            copyInputStream(getReactApplicationContext().getAssets().open(str), str, str2, promise);
        } catch (IOException unused) {
            reject(promise, str, new Exception(String.format("Asset '%s' could not be opened", new Object[]{str})));
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0026, code lost:
        if (r2 != null) goto L_0x0028;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:?, code lost:
        r2.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0036, code lost:
        if (r2 == null) goto L_0x0046;
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:26:0x003e */
    @com.facebook.react.bridge.ReactMethod
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void existsAssets(java.lang.String r4, com.facebook.react.bridge.Promise r5) {
        /*
            r3 = this;
            com.facebook.react.bridge.ReactApplicationContext r0 = r3.getReactApplicationContext()     // Catch:{ Exception -> 0x003f }
            android.content.res.AssetManager r0 = r0.getAssets()     // Catch:{ Exception -> 0x003f }
            r1 = 1
            java.lang.String[] r2 = r0.list(r4)     // Catch:{ Exception -> 0x001a }
            if (r2 == 0) goto L_0x001a
            int r2 = r2.length     // Catch:{ Exception -> 0x001a }
            if (r2 <= 0) goto L_0x001a
            java.lang.Boolean r2 = java.lang.Boolean.valueOf(r1)     // Catch:{ Exception -> 0x001a }
            r5.resolve(r2)     // Catch:{ Exception -> 0x001a }
            return
        L_0x001a:
            r2 = 0
            java.io.InputStream r2 = r0.open(r4)     // Catch:{ Exception -> 0x002e }
            java.lang.Boolean r0 = java.lang.Boolean.valueOf(r1)     // Catch:{ Exception -> 0x002e }
            r5.resolve(r0)     // Catch:{ Exception -> 0x002e }
            if (r2 == 0) goto L_0x0046
        L_0x0028:
            r2.close()     // Catch:{ Exception -> 0x0046 }
            goto L_0x0046
        L_0x002c:
            r0 = move-exception
            goto L_0x0039
        L_0x002e:
            r0 = 0
            java.lang.Boolean r0 = java.lang.Boolean.valueOf(r0)     // Catch:{ all -> 0x002c }
            r5.resolve(r0)     // Catch:{ all -> 0x002c }
            if (r2 == 0) goto L_0x0046
            goto L_0x0028
        L_0x0039:
            if (r2 == 0) goto L_0x003e
            r2.close()     // Catch:{ Exception -> 0x003e }
        L_0x003e:
            throw r0     // Catch:{ Exception -> 0x003f }
        L_0x003f:
            r0 = move-exception
            r0.printStackTrace()
            r3.reject(r5, r4, r0)
        L_0x0046:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.rnfs.RNFSManager.existsAssets(java.lang.String, com.facebook.react.bridge.Promise):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:26:0x004f A[SYNTHETIC, Splitter:B:26:0x004f] */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x005a A[SYNTHETIC, Splitter:B:32:0x005a] */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x0061 A[SYNTHETIC, Splitter:B:36:0x0061] */
    /* JADX WARNING: Removed duplicated region for block: B:43:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void copyInputStream(java.io.InputStream r8, java.lang.String r9, java.lang.String r10, com.facebook.react.bridge.Promise r11) {
        /*
            r7 = this;
            r0 = 0
            r1 = 0
            java.io.OutputStream r2 = r7.getOutputStream(r10, r1)     // Catch:{ Exception -> 0x002c, all -> 0x0029 }
            r3 = 10240(0x2800, float:1.4349E-41)
            byte[] r3 = new byte[r3]     // Catch:{ Exception -> 0x0027 }
        L_0x000a:
            int r4 = r8.read(r3)     // Catch:{ Exception -> 0x0027 }
            r5 = -1
            if (r4 == r5) goto L_0x0015
            r2.write(r3, r1, r4)     // Catch:{ Exception -> 0x0027 }
            goto L_0x000a
        L_0x0015:
            r11.resolve(r0)     // Catch:{ Exception -> 0x0027 }
            if (r8 == 0) goto L_0x001f
            r8.close()     // Catch:{ IOException -> 0x001e }
            goto L_0x001f
        L_0x001e:
        L_0x001f:
            if (r2 == 0) goto L_0x0057
        L_0x0021:
            r2.close()     // Catch:{ IOException -> 0x0057 }
            goto L_0x0057
        L_0x0025:
            r9 = move-exception
            goto L_0x0058
        L_0x0027:
            r0 = move-exception
            goto L_0x0030
        L_0x0029:
            r9 = move-exception
            r2 = r0
            goto L_0x0058
        L_0x002c:
            r2 = move-exception
            r6 = r2
            r2 = r0
            r0 = r6
        L_0x0030:
            java.lang.Exception r3 = new java.lang.Exception     // Catch:{ all -> 0x0025 }
            java.lang.String r4 = "Failed to copy '%s' to %s (%s)"
            r5 = 3
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch:{ all -> 0x0025 }
            r5[r1] = r9     // Catch:{ all -> 0x0025 }
            r1 = 1
            r5[r1] = r10     // Catch:{ all -> 0x0025 }
            r10 = 2
            java.lang.String r0 = r0.getLocalizedMessage()     // Catch:{ all -> 0x0025 }
            r5[r10] = r0     // Catch:{ all -> 0x0025 }
            java.lang.String r10 = java.lang.String.format(r4, r5)     // Catch:{ all -> 0x0025 }
            r3.<init>(r10)     // Catch:{ all -> 0x0025 }
            r7.reject(r11, r9, r3)     // Catch:{ all -> 0x0025 }
            if (r8 == 0) goto L_0x0054
            r8.close()     // Catch:{ IOException -> 0x0053 }
            goto L_0x0054
        L_0x0053:
        L_0x0054:
            if (r2 == 0) goto L_0x0057
            goto L_0x0021
        L_0x0057:
            return
        L_0x0058:
            if (r8 == 0) goto L_0x005f
            r8.close()     // Catch:{ IOException -> 0x005e }
            goto L_0x005f
        L_0x005e:
        L_0x005f:
            if (r2 == 0) goto L_0x0064
            r2.close()     // Catch:{ IOException -> 0x0064 }
        L_0x0064:
            throw r9
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.rnfs.RNFSManager.copyInputStream(java.io.InputStream, java.lang.String, java.lang.String, com.facebook.react.bridge.Promise):void");
    }

    @ReactMethod
    public void setReadable(String str, Boolean bool, Boolean bool2, Promise promise) {
        try {
            File file = new File(str);
            if (file.exists()) {
                file.setReadable(bool.booleanValue(), bool2.booleanValue());
                promise.resolve(Boolean.valueOf(true));
                return;
            }
            throw new Exception("File does not exist");
        } catch (Exception e) {
            e.printStackTrace();
            reject(promise, str, e);
        }
    }

    @ReactMethod
    public void stat(String str, Promise promise) {
        int i = 1;
        try {
            String originalFilepath = getOriginalFilepath(str, true);
            File file = new File(originalFilepath);
            if (file.exists()) {
                WritableMap createMap = Arguments.createMap();
                createMap.putInt("ctime", (int) (file.lastModified() / 1000));
                createMap.putInt("mtime", (int) (file.lastModified() / 1000));
                createMap.putInt("size", (int) file.length());
                String str2 = "type";
                if (!file.isDirectory()) {
                    i = 0;
                }
                createMap.putInt(str2, i);
                createMap.putString("originalFilepath", originalFilepath);
                promise.resolve(createMap);
                return;
            }
            throw new Exception("File does not exist");
        } catch (Exception e) {
            e.printStackTrace();
            reject(promise, str, e);
        }
    }

    @ReactMethod
    public void unlink(String str, Promise promise) {
        try {
            File file = new File(str);
            if (file.exists()) {
                DeleteRecursive(file);
                promise.resolve(null);
                return;
            }
            throw new Exception("File does not exist");
        } catch (Exception e) {
            e.printStackTrace();
            reject(promise, str, e);
        }
    }

    private void DeleteRecursive(File file) {
        if (file.isDirectory()) {
            for (File DeleteRecursive : file.listFiles()) {
                DeleteRecursive(DeleteRecursive);
            }
        }
        file.delete();
    }

    @ReactMethod
    public void mkdir(String str, ReadableMap readableMap, Promise promise) {
        try {
            File file = new File(str);
            file.mkdirs();
            if (file.exists()) {
                promise.resolve(null);
                return;
            }
            throw new Exception("Directory could not be created");
        } catch (Exception e) {
            e.printStackTrace();
            reject(promise, str, e);
        }
    }

    /* access modifiers changed from: private */
    public void sendEvent(ReactContext reactContext2, String str, @Nullable WritableMap writableMap) {
        ((RCTNativeAppEventEmitter) reactContext2.getJSModule(RCTNativeAppEventEmitter.class)).emit(str, writableMap);
    }

    @ReactMethod
    public void downloadFile(final ReadableMap readableMap, final Promise promise) {
        try {
            File file = new File(readableMap.getString("toFile"));
            URL url = new URL(readableMap.getString("fromUrl"));
            final int i = readableMap.getInt("jobId");
            ReadableMap map = readableMap.getMap("headers");
            int i2 = readableMap.getInt("progressDivider");
            int i3 = readableMap.getInt("readTimeout");
            int i4 = readableMap.getInt("connectionTimeout");
            DownloadParams downloadParams = new DownloadParams();
            downloadParams.src = url;
            downloadParams.dest = file;
            downloadParams.headers = map;
            downloadParams.progressDivider = (float) i2;
            downloadParams.readTimeout = i3;
            downloadParams.connectionTimeout = i4;
            downloadParams.onTaskCompleted = new OnTaskCompleted() {
                public void onTaskCompleted(DownloadResult downloadResult) {
                    if (downloadResult.exception == null) {
                        WritableMap createMap = Arguments.createMap();
                        createMap.putInt("jobId", i);
                        createMap.putInt("statusCode", downloadResult.statusCode);
                        createMap.putDouble("bytesWritten", (double) downloadResult.bytesWritten);
                        promise.resolve(createMap);
                        return;
                    }
                    RNFSManager.this.reject(promise, readableMap.getString("toFile"), downloadResult.exception);
                }
            };
            downloadParams.onDownloadBegin = new OnDownloadBegin() {
                public void onDownloadBegin(int i, long j, Map<String, String> map) {
                    WritableMap createMap = Arguments.createMap();
                    for (Entry entry : map.entrySet()) {
                        createMap.putString((String) entry.getKey(), (String) entry.getValue());
                    }
                    WritableMap createMap2 = Arguments.createMap();
                    createMap2.putInt("jobId", i);
                    createMap2.putInt("statusCode", i);
                    createMap2.putDouble("contentLength", (double) j);
                    createMap2.putMap("headers", createMap);
                    RNFSManager rNFSManager = RNFSManager.this;
                    ReactApplicationContext access$400 = rNFSManager.getReactApplicationContext();
                    StringBuilder sb = new StringBuilder();
                    sb.append("DownloadBegin-");
                    sb.append(i);
                    rNFSManager.sendEvent(access$400, sb.toString(), createMap2);
                }
            };
            downloadParams.onDownloadProgress = new OnDownloadProgress() {
                public void onDownloadProgress(long j, long j2) {
                    WritableMap createMap = Arguments.createMap();
                    createMap.putInt("jobId", i);
                    createMap.putDouble("contentLength", (double) j);
                    createMap.putDouble("bytesWritten", (double) j2);
                    RNFSManager rNFSManager = RNFSManager.this;
                    ReactApplicationContext access$600 = rNFSManager.getReactApplicationContext();
                    StringBuilder sb = new StringBuilder();
                    sb.append("DownloadProgress-");
                    sb.append(i);
                    rNFSManager.sendEvent(access$600, sb.toString(), createMap);
                }
            };
            Downloader downloader = new Downloader();
            downloader.execute(new DownloadParams[]{downloadParams});
            this.downloaders.put(i, downloader);
        } catch (Exception e) {
            e.printStackTrace();
            reject(promise, readableMap.getString("toFile"), e);
        }
    }

    @ReactMethod
    public void stopDownload(int i) {
        Downloader downloader = (Downloader) this.downloaders.get(i);
        if (downloader != null) {
            downloader.stop();
        }
    }

    @ReactMethod
    public void uploadFiles(final ReadableMap readableMap, final Promise promise) {
        try {
            ReadableArray array = readableMap.getArray("files");
            URL url = new URL(readableMap.getString("toUrl"));
            final int i = readableMap.getInt("jobId");
            ReadableMap map = readableMap.getMap("headers");
            ReadableMap map2 = readableMap.getMap("fields");
            String string = readableMap.getString(Param.METHOD);
            ArrayList<ReadableMap> arrayList = new ArrayList<>();
            UploadParams uploadParams = new UploadParams();
            for (int i2 = 0; i2 < array.size(); i2++) {
                arrayList.add(array.getMap(i2));
            }
            uploadParams.src = url;
            uploadParams.files = arrayList;
            uploadParams.headers = map;
            uploadParams.method = string;
            uploadParams.fields = map2;
            uploadParams.onUploadComplete = new onUploadComplete() {
                public void onUploadComplete(UploadResult uploadResult) {
                    if (uploadResult.exception == null) {
                        WritableMap createMap = Arguments.createMap();
                        createMap.putInt("jobId", i);
                        createMap.putInt("statusCode", uploadResult.statusCode);
                        createMap.putMap("headers", uploadResult.headers);
                        createMap.putString("body", uploadResult.body);
                        promise.resolve(createMap);
                        return;
                    }
                    RNFSManager.this.reject(promise, readableMap.getString("toUrl"), uploadResult.exception);
                }
            };
            uploadParams.onUploadBegin = new onUploadBegin() {
                public void onUploadBegin() {
                    WritableMap createMap = Arguments.createMap();
                    createMap.putInt("jobId", i);
                    RNFSManager rNFSManager = RNFSManager.this;
                    ReactApplicationContext access$700 = rNFSManager.getReactApplicationContext();
                    StringBuilder sb = new StringBuilder();
                    sb.append("UploadBegin-");
                    sb.append(i);
                    rNFSManager.sendEvent(access$700, sb.toString(), createMap);
                }
            };
            uploadParams.onUploadProgress = new onUploadProgress() {
                public void onUploadProgress(int i, int i2) {
                    WritableMap createMap = Arguments.createMap();
                    createMap.putInt("jobId", i);
                    createMap.putInt("totalBytesExpectedToSend", i);
                    createMap.putInt("totalBytesSent", i2);
                    RNFSManager rNFSManager = RNFSManager.this;
                    ReactApplicationContext access$800 = rNFSManager.getReactApplicationContext();
                    StringBuilder sb = new StringBuilder();
                    sb.append("UploadProgress-");
                    sb.append(i);
                    rNFSManager.sendEvent(access$800, sb.toString(), createMap);
                }
            };
            Uploader uploader = new Uploader();
            uploader.execute(new UploadParams[]{uploadParams});
            this.uploaders.put(i, uploader);
        } catch (Exception e) {
            e.printStackTrace();
            reject(promise, readableMap.getString("toUrl"), e);
        }
    }

    @ReactMethod
    public void stopUpload(int i) {
        Uploader uploader = (Uploader) this.uploaders.get(i);
        if (uploader != null) {
            uploader.stop();
        }
    }

    @ReactMethod
    public void getFSInfo(Promise promise) {
        long j;
        long j2;
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        if (VERSION.SDK_INT >= 18) {
            j = statFs.getTotalBytes();
            j2 = statFs.getFreeBytes();
        } else {
            long blockSize = (long) statFs.getBlockSize();
            j2 = ((long) statFs.getAvailableBlocks()) * blockSize;
            j = ((long) statFs.getBlockCount()) * blockSize;
        }
        WritableMap createMap = Arguments.createMap();
        createMap.putDouble("totalSpace", (double) j);
        createMap.putDouble("freeSpace", (double) j2);
        promise.resolve(createMap);
    }

    @ReactMethod
    public void touch(String str, double d, double d2, Promise promise) {
        try {
            promise.resolve(Boolean.valueOf(new File(str).setLastModified((long) d)));
        } catch (Exception e) {
            e.printStackTrace();
            reject(promise, str, e);
        }
    }

    @ReactMethod
    public void getAllExternalFilesDirs(Promise promise) {
        File[] externalFilesDirs = getReactApplicationContext().getExternalFilesDirs(null);
        WritableArray createArray = Arguments.createArray();
        for (File absolutePath : externalFilesDirs) {
            createArray.pushString(absolutePath.getAbsolutePath());
        }
        promise.resolve(createArray);
    }

    @ReactMethod
    public void scanFile(String str, final Promise promise) {
        MediaScannerConnection.scanFile(getReactApplicationContext(), new String[]{str}, null, new MediaScannerConnectionClient() {
            public void onMediaScannerConnected() {
            }

            public void onScanCompleted(String str, Uri uri) {
                promise.resolve(str);
            }
        });
    }

    /* access modifiers changed from: private */
    public void reject(Promise promise, String str, Exception exc) {
        if (exc instanceof FileNotFoundException) {
            rejectFileNotFound(promise, str);
        } else if (exc instanceof IORejectionException) {
            IORejectionException iORejectionException = (IORejectionException) exc;
            promise.reject(iORejectionException.getCode(), iORejectionException.getMessage());
        } else {
            promise.reject((String) null, exc.getMessage());
        }
    }

    private void rejectFileNotFound(Promise promise, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("ENOENT: no such file or directory, open '");
        sb.append(str);
        sb.append("'");
        promise.reject("ENOENT", sb.toString());
    }

    private void rejectFileIsDirectory(Promise promise) {
        promise.reject("EISDIR", "EISDIR: illegal operation on a directory, read");
    }

    public Map<String, Object> getConstants() {
        HashMap hashMap = new HashMap();
        hashMap.put(RNFSDocumentDirectory, Integer.valueOf(0));
        hashMap.put(RNFSDocumentDirectoryPath, getReactApplicationContext().getFilesDir().getAbsolutePath());
        hashMap.put(RNFSTemporaryDirectoryPath, getReactApplicationContext().getCacheDir().getAbsolutePath());
        hashMap.put(RNFSPicturesDirectoryPath, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath());
        hashMap.put(RNFSCachesDirectoryPath, getReactApplicationContext().getCacheDir().getAbsolutePath());
        hashMap.put(RNFSFileTypeRegular, Integer.valueOf(0));
        hashMap.put(RNFSFileTypeDirectory, Integer.valueOf(1));
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        if (externalStorageDirectory != null) {
            hashMap.put(RNFSExternalStorageDirectoryPath, externalStorageDirectory.getAbsolutePath());
        } else {
            hashMap.put(RNFSExternalStorageDirectoryPath, null);
        }
        File externalFilesDir = getReactApplicationContext().getExternalFilesDir(null);
        if (externalFilesDir != null) {
            hashMap.put(RNFSExternalDirectoryPath, externalFilesDir.getAbsolutePath());
        } else {
            hashMap.put(RNFSExternalDirectoryPath, null);
        }
        File externalCacheDir = getReactApplicationContext().getExternalCacheDir();
        if (externalCacheDir != null) {
            hashMap.put(RNFSExternalCachesDirectoryPath, externalCacheDir.getAbsolutePath());
        } else {
            hashMap.put(RNFSExternalCachesDirectoryPath, null);
        }
        return hashMap;
    }
}

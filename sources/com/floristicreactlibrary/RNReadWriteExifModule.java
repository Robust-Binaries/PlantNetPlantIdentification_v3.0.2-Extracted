package com.floristicreactlibrary;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.floristicreactlibrary.tasks.CopyExifTask;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class RNReadWriteExifModule extends ReactContextBaseJavaModule {
    private static final String E_READ_DEST_FILE_ERROR = "E_READ_DEST_FILE_ERROR";
    private static final String E_READ_SRC_FILE_ERROR = "E_READ_SRC_FILE_ERROR";
    private final ReactApplicationContext reactContext;

    public String getName() {
        return "RNReadWriteExif";
    }

    public RNReadWriteExifModule(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
        this.reactContext = reactApplicationContext;
    }

    public Map<String, Object> getConstants() {
        return new HashMap();
    }

    private void checkFileExists(File file) throws Exception {
        if (file == null || !file.exists()) {
            throw new Exception("File does not exist");
        } else if (!file.isFile()) {
            throw new Exception("This is not a file");
        } else if (!file.canRead()) {
            throw new Exception("File cannot be read");
        } else if (!file.canWrite()) {
            throw new Exception("File cannot be written");
        }
    }

    @ReactMethod
    public void copyExifCallback(String str, String str2, Callback callback, Callback callback2) {
        try {
            File file = new File(Uri.parse(str).getPath());
            checkFileExists(file);
            StringBuilder sb = new StringBuilder();
            sb.append("file exists (r/w): ");
            sb.append(str);
            Log.d("copyExifCallback", sb.toString());
            try {
                File file2 = new File(Uri.parse(str2).getPath());
                checkFileExists(file2);
                StringBuilder sb2 = new StringBuilder();
                sb2.append("file exists (r/w): ");
                sb2.append(str2);
                Log.d("copyExifCallback", sb2.toString());
                copyExif(file, file2, callback, callback2, null);
            } catch (Throwable th) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("E_READ_DEST_FILE_ERROR ");
                sb3.append(th.getMessage());
                callback.invoke(sb3.toString());
            }
        } catch (Throwable th2) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append("E_READ_SRC_FILE_ERROR ");
            sb4.append(th2.getMessage());
            callback.invoke(sb4.toString());
        }
    }

    @ReactMethod
    public void copyExifPromise(String str, String str2, Promise promise) {
        try {
            File file = new File(Uri.parse(str).getPath());
            checkFileExists(file);
            StringBuilder sb = new StringBuilder();
            sb.append("file exists (r/w): ");
            sb.append(str);
            Log.d("copyExifPromise", sb.toString());
            try {
                File file2 = new File(Uri.parse(str2).getPath());
                checkFileExists(file2);
                StringBuilder sb2 = new StringBuilder();
                sb2.append("file exists (r/w): ");
                sb2.append(str2);
                Log.d("copyExifPromise", sb2.toString());
                copyExif(file, file2, null, null, promise);
            } catch (Throwable th) {
                promise.reject(E_READ_DEST_FILE_ERROR, th);
            }
        } catch (Throwable th2) {
            promise.reject(E_READ_SRC_FILE_ERROR, th2);
        }
    }

    private void copyExif(@NonNull File file, @NonNull File file2, @Nullable Callback callback, @Nullable Callback callback2, @Nullable Promise promise) {
        CopyExifTask copyExifTask = new CopyExifTask(file, file2, callback, callback2, promise);
        copyExifTask.execute(new Integer[0]);
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x0030 A[SYNTHETIC, Splitter:B:11:0x0030] */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0099  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x009f  */
    @com.facebook.react.bridge.ReactMethod
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void scanFile(java.lang.String r8, com.facebook.react.bridge.Promise r9) {
        /*
            r7 = this;
            r0 = 0
            java.io.File r1 = new java.io.File     // Catch:{ Throwable -> 0x002a }
            android.net.Uri r2 = android.net.Uri.parse(r8)     // Catch:{ Throwable -> 0x002a }
            java.lang.String r2 = r2.getPath()     // Catch:{ Throwable -> 0x002a }
            r1.<init>(r2)     // Catch:{ Throwable -> 0x002a }
            r7.checkFileExists(r1)     // Catch:{ Throwable -> 0x0028 }
            java.lang.String r2 = "scanFile"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0028 }
            r3.<init>()     // Catch:{ Throwable -> 0x0028 }
            java.lang.String r4 = "file exists (r/w): "
            r3.append(r4)     // Catch:{ Throwable -> 0x0028 }
            r3.append(r8)     // Catch:{ Throwable -> 0x0028 }
            java.lang.String r8 = r3.toString()     // Catch:{ Throwable -> 0x0028 }
            android.util.Log.d(r2, r8)     // Catch:{ Throwable -> 0x0028 }
            goto L_0x002d
        L_0x0028:
            r8 = move-exception
            goto L_0x002c
        L_0x002a:
            r8 = move-exception
            r1 = r0
        L_0x002c:
            r0 = r8
        L_0x002d:
            r8 = 1
            if (r1 == 0) goto L_0x0097
            com.facebook.react.bridge.ReactApplicationContext r2 = r7.reactContext     // Catch:{ Throwable -> 0x004a }
            java.lang.String[] r3 = new java.lang.String[r8]     // Catch:{ Throwable -> 0x004a }
            java.lang.String r4 = r1.getPath()     // Catch:{ Throwable -> 0x004a }
            r5 = 0
            r3[r5] = r4     // Catch:{ Throwable -> 0x004a }
            java.lang.String[] r4 = new java.lang.String[r8]     // Catch:{ Throwable -> 0x004a }
            java.lang.String r6 = "image/jpeg"
            r4[r5] = r6     // Catch:{ Throwable -> 0x004a }
            com.floristicreactlibrary.RNReadWriteExifModule$1 r5 = new com.floristicreactlibrary.RNReadWriteExifModule$1     // Catch:{ Throwable -> 0x004a }
            r5.<init>()     // Catch:{ Throwable -> 0x004a }
            android.media.MediaScannerConnection.scanFile(r2, r3, r4, r5)     // Catch:{ Throwable -> 0x004a }
            goto L_0x004b
        L_0x004a:
            r0 = move-exception
        L_0x004b:
            android.content.Intent r2 = new android.content.Intent     // Catch:{ Throwable -> 0x005f }
            java.lang.String r3 = "android.intent.action.MEDIA_SCANNER_SCAN_FILE"
            r2.<init>(r3)     // Catch:{ Throwable -> 0x005f }
            android.net.Uri r3 = android.net.Uri.fromFile(r1)     // Catch:{ Throwable -> 0x005f }
            r2.setData(r3)     // Catch:{ Throwable -> 0x005f }
            com.facebook.react.bridge.ReactApplicationContext r3 = r7.reactContext     // Catch:{ Throwable -> 0x005f }
            r3.sendBroadcast(r2)     // Catch:{ Throwable -> 0x005f }
            goto L_0x0060
        L_0x005f:
            r0 = move-exception
        L_0x0060:
            com.facebook.react.bridge.ReactApplicationContext r2 = r7.reactContext     // Catch:{ Throwable -> 0x0096 }
            android.content.Intent r3 = new android.content.Intent     // Catch:{ Throwable -> 0x0096 }
            java.lang.String r4 = "android.intent.action.MEDIA_MOUNTED"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0096 }
            r5.<init>()     // Catch:{ Throwable -> 0x0096 }
            java.lang.String r6 = "file://"
            r5.append(r6)     // Catch:{ Throwable -> 0x0096 }
            java.io.File r6 = android.os.Environment.getExternalStorageDirectory()     // Catch:{ Throwable -> 0x0096 }
            r5.append(r6)     // Catch:{ Throwable -> 0x0096 }
            java.lang.String r5 = r5.toString()     // Catch:{ Throwable -> 0x0096 }
            android.net.Uri r5 = android.net.Uri.parse(r5)     // Catch:{ Throwable -> 0x0096 }
            r3.<init>(r4, r5)     // Catch:{ Throwable -> 0x0096 }
            r2.sendBroadcast(r3)     // Catch:{ Throwable -> 0x0096 }
            com.facebook.react.bridge.ReactApplicationContext r2 = r7.reactContext     // Catch:{ Throwable -> 0x0096 }
            android.content.Intent r3 = new android.content.Intent     // Catch:{ Throwable -> 0x0096 }
            java.lang.String r4 = "android.intent.action.MEDIA_MOUNTED"
            android.net.Uri r1 = android.net.Uri.fromFile(r1)     // Catch:{ Throwable -> 0x0096 }
            r3.<init>(r4, r1)     // Catch:{ Throwable -> 0x0096 }
            r2.sendBroadcast(r3)     // Catch:{ Throwable -> 0x0096 }
            goto L_0x0097
        L_0x0096:
            r0 = move-exception
        L_0x0097:
            if (r0 == 0) goto L_0x009f
            java.lang.String r8 = "E_READ_SRC_FILE_ERROR"
            r9.reject(r8, r0)
            return
        L_0x009f:
            java.lang.Boolean r8 = java.lang.Boolean.valueOf(r8)
            r9.resolve(r8)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.floristicreactlibrary.RNReadWriteExifModule.scanFile(java.lang.String, com.facebook.react.bridge.Promise):void");
    }
}

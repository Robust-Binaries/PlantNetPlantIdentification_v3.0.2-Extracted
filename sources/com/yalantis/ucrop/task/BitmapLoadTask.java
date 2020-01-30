package com.yalantis.ucrop.task;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.p000v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import com.facebook.common.util.UriUtil;
import com.yalantis.ucrop.callback.BitmapLoadCallback;
import com.yalantis.ucrop.model.ExifInfo;
import com.yalantis.ucrop.util.BitmapLoadUtils;
import com.yalantis.ucrop.util.FileUtils;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class BitmapLoadTask extends AsyncTask<Void, Void, BitmapWorkerResult> {
    private static final String TAG = "BitmapWorkerTask";
    private final BitmapLoadCallback mBitmapLoadCallback;
    private final Context mContext;
    private Uri mInputUri;
    private Uri mOutputUri;
    private final int mRequiredHeight;
    private final int mRequiredWidth;

    public static class BitmapWorkerResult {
        Bitmap mBitmapResult;
        Exception mBitmapWorkerException;
        ExifInfo mExifInfo;

        public BitmapWorkerResult(@NonNull Bitmap bitmap, @NonNull ExifInfo exifInfo) {
            this.mBitmapResult = bitmap;
            this.mExifInfo = exifInfo;
        }

        public BitmapWorkerResult(@NonNull Exception exc) {
            this.mBitmapWorkerException = exc;
        }
    }

    public BitmapLoadTask(@NonNull Context context, @NonNull Uri uri, @Nullable Uri uri2, int i, int i2, BitmapLoadCallback bitmapLoadCallback) {
        this.mContext = context;
        this.mInputUri = uri;
        this.mOutputUri = uri2;
        this.mRequiredWidth = i;
        this.mRequiredHeight = i2;
        this.mBitmapLoadCallback = bitmapLoadCallback;
    }

    /* access modifiers changed from: protected */
    @NonNull
    public BitmapWorkerResult doInBackground(Void... voidArr) {
        if (this.mInputUri == null) {
            return new BitmapWorkerResult(new NullPointerException("Input Uri cannot be null"));
        }
        try {
            processInputUri();
            try {
                ParcelFileDescriptor openFileDescriptor = this.mContext.getContentResolver().openFileDescriptor(this.mInputUri, "r");
                if (openFileDescriptor != null) {
                    FileDescriptor fileDescriptor = openFileDescriptor.getFileDescriptor();
                    Options options = new Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
                    if (options.outWidth == -1 || options.outHeight == -1) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Bounds for bitmap could not be retrieved from the Uri: [");
                        sb.append(this.mInputUri);
                        sb.append("]");
                        return new BitmapWorkerResult(new IllegalArgumentException(sb.toString()));
                    }
                    options.inSampleSize = BitmapLoadUtils.calculateInSampleSize(options, this.mRequiredWidth, this.mRequiredHeight);
                    boolean z = false;
                    options.inJustDecodeBounds = false;
                    Bitmap bitmap = null;
                    while (!z) {
                        try {
                            bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
                            z = true;
                        } catch (OutOfMemoryError e) {
                            Log.e(TAG, "doInBackground: BitmapFactory.decodeFileDescriptor: ", e);
                            options.inSampleSize *= 2;
                        }
                    }
                    if (bitmap == null) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Bitmap could not be decoded from the Uri: [");
                        sb2.append(this.mInputUri);
                        sb2.append("]");
                        return new BitmapWorkerResult(new IllegalArgumentException(sb2.toString()));
                    }
                    if (VERSION.SDK_INT >= 16) {
                        BitmapLoadUtils.close(openFileDescriptor);
                    }
                    int exifOrientation = BitmapLoadUtils.getExifOrientation(this.mContext, this.mInputUri);
                    int exifToDegrees = BitmapLoadUtils.exifToDegrees(exifOrientation);
                    int exifToTranslation = BitmapLoadUtils.exifToTranslation(exifOrientation);
                    ExifInfo exifInfo = new ExifInfo(exifOrientation, exifToDegrees, exifToTranslation);
                    Matrix matrix = new Matrix();
                    if (exifToDegrees != 0) {
                        matrix.preRotate((float) exifToDegrees);
                    }
                    if (exifToTranslation != 1) {
                        matrix.postScale((float) exifToTranslation, 1.0f);
                    }
                    if (!matrix.isIdentity()) {
                        return new BitmapWorkerResult(BitmapLoadUtils.transformBitmap(bitmap, matrix), exifInfo);
                    }
                    return new BitmapWorkerResult(bitmap, exifInfo);
                }
                StringBuilder sb3 = new StringBuilder();
                sb3.append("ParcelFileDescriptor was null for given Uri: [");
                sb3.append(this.mInputUri);
                sb3.append("]");
                return new BitmapWorkerResult(new NullPointerException(sb3.toString()));
            } catch (FileNotFoundException e2) {
                return new BitmapWorkerResult(e2);
            }
        } catch (IOException | NullPointerException e3) {
            return new BitmapWorkerResult(e3);
        }
    }

    private void processInputUri() throws NullPointerException, IOException {
        String scheme = this.mInputUri.getScheme();
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Uri scheme: ");
        sb.append(scheme);
        Log.d(str, sb.toString());
        if (UriUtil.HTTP_SCHEME.equals(scheme) || UriUtil.HTTPS_SCHEME.equals(scheme)) {
            try {
                downloadFile(this.mInputUri, this.mOutputUri);
            } catch (IOException | NullPointerException e) {
                Log.e(TAG, "Downloading failed", e);
                throw e;
            }
        } else if ("content".equals(scheme)) {
            String filePath = getFilePath();
            if (TextUtils.isEmpty(filePath) || !new File(filePath).exists()) {
                try {
                    copyFile(this.mInputUri, this.mOutputUri);
                } catch (IOException | NullPointerException e2) {
                    Log.e(TAG, "Copying failed", e2);
                    throw e2;
                }
            } else {
                this.mInputUri = Uri.fromFile(new File(filePath));
            }
        } else if (!UriUtil.LOCAL_FILE_SCHEME.equals(scheme)) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Invalid Uri scheme ");
            sb2.append(scheme);
            Log.e(TAG, sb2.toString());
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Invalid Uri scheme");
            sb3.append(scheme);
            throw new IllegalArgumentException(sb3.toString());
        }
    }

    private String getFilePath() {
        if (ContextCompat.checkSelfPermission(this.mContext, "android.permission.READ_EXTERNAL_STORAGE") == 0) {
            return FileUtils.getPath(this.mContext, this.mInputUri);
        }
        return null;
    }

    private void copyFile(@NonNull Uri uri, @Nullable Uri uri2) throws NullPointerException, IOException {
        InputStream inputStream;
        Log.d(TAG, "copyFile");
        if (uri2 != null) {
            FileOutputStream fileOutputStream = null;
            try {
                inputStream = this.mContext.getContentResolver().openInputStream(uri);
                try {
                    FileOutputStream fileOutputStream2 = new FileOutputStream(new File(uri2.getPath()));
                    if (inputStream != null) {
                        try {
                            byte[] bArr = new byte[1024];
                            while (true) {
                                int read = inputStream.read(bArr);
                                if (read > 0) {
                                    fileOutputStream2.write(bArr, 0, read);
                                } else {
                                    BitmapLoadUtils.close(fileOutputStream2);
                                    BitmapLoadUtils.close(inputStream);
                                    this.mInputUri = this.mOutputUri;
                                    return;
                                }
                            }
                        } catch (Throwable th) {
                            th = th;
                            fileOutputStream = fileOutputStream2;
                            BitmapLoadUtils.close(fileOutputStream);
                            BitmapLoadUtils.close(inputStream);
                            this.mInputUri = this.mOutputUri;
                            throw th;
                        }
                    } else {
                        throw new NullPointerException("InputStream for given input Uri is null");
                    }
                } catch (Throwable th2) {
                    th = th2;
                    BitmapLoadUtils.close(fileOutputStream);
                    BitmapLoadUtils.close(inputStream);
                    this.mInputUri = this.mOutputUri;
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                inputStream = null;
                BitmapLoadUtils.close(fileOutputStream);
                BitmapLoadUtils.close(inputStream);
                this.mInputUri = this.mOutputUri;
                throw th;
            }
        } else {
            throw new NullPointerException("Output Uri is null - cannot copy image");
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x007b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void downloadFile(@android.support.annotation.NonNull android.net.Uri r6, @android.support.annotation.Nullable android.net.Uri r7) throws java.lang.NullPointerException, java.io.IOException {
        /*
            r5 = this;
            java.lang.String r0 = "BitmapWorkerTask"
            java.lang.String r1 = "downloadFile"
            android.util.Log.d(r0, r1)
            if (r7 == 0) goto L_0x008e
            okhttp3.OkHttpClient r0 = new okhttp3.OkHttpClient
            r0.<init>()
            r1 = 0
            okhttp3.Request$Builder r2 = new okhttp3.Request$Builder     // Catch:{ all -> 0x0070 }
            r2.<init>()     // Catch:{ all -> 0x0070 }
            java.lang.String r6 = r6.toString()     // Catch:{ all -> 0x0070 }
            okhttp3.Request$Builder r6 = r2.url(r6)     // Catch:{ all -> 0x0070 }
            okhttp3.Request r6 = r6.build()     // Catch:{ all -> 0x0070 }
            okhttp3.Call r6 = r0.newCall(r6)     // Catch:{ all -> 0x0070 }
            okhttp3.Response r6 = r6.execute()     // Catch:{ all -> 0x0070 }
            okhttp3.ResponseBody r2 = r6.body()     // Catch:{ all -> 0x006c }
            okio.BufferedSource r2 = r2.source()     // Catch:{ all -> 0x006c }
            android.content.Context r3 = r5.mContext     // Catch:{ all -> 0x0066 }
            android.content.ContentResolver r3 = r3.getContentResolver()     // Catch:{ all -> 0x0066 }
            java.io.OutputStream r7 = r3.openOutputStream(r7)     // Catch:{ all -> 0x0066 }
            if (r7 == 0) goto L_0x005e
            okio.Sink r1 = okio.Okio.sink(r7)     // Catch:{ all -> 0x0066 }
            r2.readAll(r1)     // Catch:{ all -> 0x0066 }
            com.yalantis.ucrop.util.BitmapLoadUtils.close(r2)
            com.yalantis.ucrop.util.BitmapLoadUtils.close(r1)
            if (r6 == 0) goto L_0x0052
            okhttp3.ResponseBody r6 = r6.body()
            com.yalantis.ucrop.util.BitmapLoadUtils.close(r6)
        L_0x0052:
            okhttp3.Dispatcher r6 = r0.dispatcher()
            r6.cancelAll()
            android.net.Uri r6 = r5.mOutputUri
            r5.mInputUri = r6
            return
        L_0x005e:
            java.lang.NullPointerException r7 = new java.lang.NullPointerException     // Catch:{ all -> 0x0066 }
            java.lang.String r3 = "OutputStream for given output Uri is null"
            r7.<init>(r3)     // Catch:{ all -> 0x0066 }
            throw r7     // Catch:{ all -> 0x0066 }
        L_0x0066:
            r7 = move-exception
            r4 = r2
            r2 = r6
            r6 = r1
            r1 = r4
            goto L_0x0073
        L_0x006c:
            r7 = move-exception
            r2 = r6
            r6 = r1
            goto L_0x0073
        L_0x0070:
            r7 = move-exception
            r6 = r1
            r2 = r6
        L_0x0073:
            com.yalantis.ucrop.util.BitmapLoadUtils.close(r1)
            com.yalantis.ucrop.util.BitmapLoadUtils.close(r6)
            if (r2 == 0) goto L_0x0082
            okhttp3.ResponseBody r6 = r2.body()
            com.yalantis.ucrop.util.BitmapLoadUtils.close(r6)
        L_0x0082:
            okhttp3.Dispatcher r6 = r0.dispatcher()
            r6.cancelAll()
            android.net.Uri r6 = r5.mOutputUri
            r5.mInputUri = r6
            throw r7
        L_0x008e:
            java.lang.NullPointerException r6 = new java.lang.NullPointerException
            java.lang.String r7 = "Output Uri is null - cannot download image"
            r6.<init>(r7)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.yalantis.ucrop.task.BitmapLoadTask.downloadFile(android.net.Uri, android.net.Uri):void");
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(@NonNull BitmapWorkerResult bitmapWorkerResult) {
        if (bitmapWorkerResult.mBitmapWorkerException == null) {
            BitmapLoadCallback bitmapLoadCallback = this.mBitmapLoadCallback;
            Bitmap bitmap = bitmapWorkerResult.mBitmapResult;
            ExifInfo exifInfo = bitmapWorkerResult.mExifInfo;
            String path = this.mInputUri.getPath();
            Uri uri = this.mOutputUri;
            bitmapLoadCallback.onBitmapLoaded(bitmap, exifInfo, path, uri == null ? null : uri.getPath());
            return;
        }
        this.mBitmapLoadCallback.onFailure(bitmapWorkerResult.mBitmapWorkerException);
    }
}

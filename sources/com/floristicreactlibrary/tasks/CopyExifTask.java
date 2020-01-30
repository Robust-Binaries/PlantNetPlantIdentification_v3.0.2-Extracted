package com.floristicreactlibrary.tasks;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.floristicreactlibrary.utils.C1116Utils;
import java.io.File;

public class CopyExifTask extends AsyncTask<Integer, Integer, Boolean> {
    private static final String E_COPY_EXIF_ERROR = "E_COPY_EXIF_ERROR";
    private static final String MODULE_NAME = "CopyExifTask";
    private static final String M_UNKNOWN_ERROR = "Unknown error";
    private File destFile;
    private Callback errorCallback;
    private Promise promise;
    private File srcFile;
    private Callback successCallback;
    private Throwable throwable;

    public CopyExifTask(@NonNull File file, @NonNull File file2, @Nullable Callback callback, @Nullable Callback callback2, @Nullable Promise promise2) {
        this.srcFile = file;
        this.destFile = file2;
        this.errorCallback = callback;
        this.successCallback = callback2;
        this.promise = promise2;
    }

    /* access modifiers changed from: protected */
    public Boolean doInBackground(Integer... numArr) {
        Log.d(MODULE_NAME, "running");
        File file = this.srcFile;
        if (file != null) {
            File file2 = this.destFile;
            if (file2 != null) {
                try {
                    return C1116Utils.copyExifData(file, file2, null, Boolean.valueOf(true));
                } catch (Throwable th) {
                    this.throwable = th;
                }
            }
        }
        Log.d(MODULE_NAME, "failed: missing file(s)");
        return Boolean.valueOf(false);
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(Boolean bool) {
        Log.d(MODULE_NAME, "ending");
        String str = MODULE_NAME;
        StringBuilder sb = new StringBuilder();
        sb.append("succeeded: ");
        sb.append(bool != 0 ? bool : "null");
        Log.d(str, sb.toString());
        String str2 = MODULE_NAME;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("throwable: ");
        Throwable th = this.throwable;
        sb2.append(th != null ? th.getMessage() : "null");
        Log.d(str2, sb2.toString());
        if (bool == 0 || !bool.booleanValue()) {
            Promise promise2 = this.promise;
            if (promise2 != null) {
                Throwable th2 = this.throwable;
                if (th2 != null) {
                    promise2.reject(E_COPY_EXIF_ERROR, th2);
                } else {
                    promise2.reject(E_COPY_EXIF_ERROR, new Throwable(M_UNKNOWN_ERROR));
                }
            } else {
                Callback callback = this.errorCallback;
                if (callback == null) {
                    return;
                }
                if (this.throwable != null) {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("E_COPY_EXIF_ERROR ");
                    sb3.append(this.throwable.getMessage());
                    callback.invoke(sb3.toString());
                    return;
                }
                callback.invoke("E_COPY_EXIF_ERROR Unknown error");
            }
        } else {
            Promise promise3 = this.promise;
            if (promise3 != null) {
                promise3.resolve(bool);
                return;
            }
            Callback callback2 = this.successCallback;
            if (callback2 != null) {
                callback2.invoke(bool);
            }
        }
    }
}

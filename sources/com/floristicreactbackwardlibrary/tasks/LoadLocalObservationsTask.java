package com.floristicreactbackwardlibrary.tasks;

import android.content.Context;
import android.os.AsyncTask;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.floristicreactbackwardlibrary.RNBackwardPlantnetModule;
import java.io.FileInputStream;
import java.lang.ref.WeakReference;

public class LoadLocalObservationsTask extends AsyncTask<Integer, Integer, String> {
    private static final String OBSERVATIONS_JSON_FILE_NAME = "observations_json.txt";
    private WeakReference<Context> context;
    private Callback errorCallback;
    private Exception exception;
    private Promise promise;
    private Callback successCallback;

    public LoadLocalObservationsTask(Context context2, Callback callback, Callback callback2, Promise promise2) {
        this.context = new WeakReference<>(context2);
        this.errorCallback = callback;
        this.successCallback = callback2;
        this.promise = promise2;
    }

    /* access modifiers changed from: protected */
    public String doInBackground(Integer... numArr) {
        this.exception = null;
        WeakReference<Context> weakReference = this.context;
        if (weakReference == null || weakReference.get() == null) {
            return null;
        }
        try {
            FileInputStream openFileInput = ((Context) this.context.get()).openFileInput(OBSERVATIONS_JSON_FILE_NAME);
            byte[] bArr = new byte[openFileInput.available()];
            openFileInput.read(bArr);
            openFileInput.close();
            return new String(bArr, "UTF-8");
        } catch (Exception e) {
            this.exception = e;
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(String str) {
        String str2;
        if (str != null) {
            Promise promise2 = this.promise;
            if (promise2 != null) {
                promise2.resolve(str);
            }
            Callback callback = this.successCallback;
            if (callback != null) {
                callback.invoke(str);
                return;
            }
            return;
        }
        Promise promise3 = this.promise;
        if (promise3 != null) {
            promise3.reject(RNBackwardPlantnetModule.E_BACKWARD_PLANTNET_MODULE, (Throwable) this.exception);
        }
        Callback callback2 = this.errorCallback;
        if (callback2 != null) {
            Object[] objArr = new Object[1];
            StringBuilder sb = new StringBuilder();
            sb.append(RNBackwardPlantnetModule.E_BACKWARD_PLANTNET_MODULE);
            if (this.exception != null) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(" ");
                sb2.append(this.exception.getMessage());
                str2 = sb2.toString();
            } else {
                str2 = "";
            }
            sb.append(str2);
            objArr[0] = sb.toString();
            callback2.invoke(objArr);
        }
    }
}

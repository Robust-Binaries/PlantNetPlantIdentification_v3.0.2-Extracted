package com.reactnative.ivpusic.imagepicker;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Environment;
import android.util.Log;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReadableMap;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

class Compression {
    Compression() {
    }

    /* access modifiers changed from: 0000 */
    public File resize(String str, int i, int i2, int i3) throws IOException {
        Bitmap decodeFile = BitmapFactory.decodeFile(str);
        float width = ((float) decodeFile.getWidth()) / ((float) decodeFile.getHeight());
        float f = (float) i;
        float f2 = (float) i2;
        if (f / f2 > 1.0f) {
            i = (int) (f2 * width);
        } else {
            i2 = (int) (f / width);
        }
        Bitmap createScaledBitmap = Bitmap.createScaledBitmap(decodeFile, i, i2, true);
        File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        StringBuilder sb = new StringBuilder();
        sb.append(UUID.randomUUID());
        sb.append(".jpg");
        File file = new File(externalStoragePublicDirectory, sb.toString());
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
        createScaledBitmap.compress(CompressFormat.JPEG, i3, bufferedOutputStream);
        bufferedOutputStream.close();
        decodeFile.recycle();
        createScaledBitmap.recycle();
        return file;
    }

    /* access modifiers changed from: 0000 */
    public File compressImage(ReadableMap readableMap, String str, Options options) throws IOException {
        Integer num;
        Integer num2;
        Double d = null;
        Integer valueOf = readableMap.hasKey("compressImageMaxWidth") ? Integer.valueOf(readableMap.getInt("compressImageMaxWidth")) : null;
        Integer valueOf2 = readableMap.hasKey("compressImageMaxHeight") ? Integer.valueOf(readableMap.getInt("compressImageMaxHeight")) : null;
        if (readableMap.hasKey("compressImageQuality")) {
            d = Double.valueOf(readableMap.getDouble("compressImageQuality"));
        }
        boolean z = false;
        boolean z2 = d == null || d.doubleValue() == 1.0d;
        boolean z3 = valueOf == null || valueOf.intValue() >= options.outWidth;
        boolean z4 = valueOf2 == null || valueOf2.intValue() >= options.outHeight;
        List asList = Arrays.asList(new String[]{"image/jpeg", "image/jpg", "image/png", "image/gif", "image/tiff"});
        if (options.outMimeType != null && asList.contains(options.outMimeType.toLowerCase())) {
            z = true;
        }
        if (!z2 || !z3 || !z4 || !z) {
            Log.d("image-crop-picker", "Image compression activated");
            int doubleValue = d != null ? (int) (d.doubleValue() * 100.0d) : 100;
            StringBuilder sb = new StringBuilder();
            sb.append("Compressing image with quality ");
            sb.append(doubleValue);
            Log.d("image-crop-picker", sb.toString());
            if (valueOf == null) {
                num = Integer.valueOf(options.outWidth);
            } else {
                num = Integer.valueOf(Math.min(valueOf.intValue(), options.outWidth));
            }
            if (valueOf2 == null) {
                num2 = Integer.valueOf(options.outHeight);
            } else {
                num2 = Integer.valueOf(Math.min(valueOf2.intValue(), options.outHeight));
            }
            return resize(str, num.intValue(), num2.intValue(), doubleValue);
        }
        Log.d("image-crop-picker", "Skipping image compression");
        return new File(str);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void compressVideo(Activity activity, ReadableMap readableMap, String str, String str2, Promise promise) {
        promise.resolve(str);
    }
}

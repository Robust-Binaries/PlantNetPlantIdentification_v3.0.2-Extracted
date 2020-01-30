package p006fr.bamlab.rnimageresizer;

import android.content.Context;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import com.RNFetchBlob.RNFetchBlobConst;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.google.android.gms.measurement.api.AppMeasurementSdk.ConditionalUserProperty;
import java.io.File;
import java.io.IOException;

/* renamed from: fr.bamlab.rnimageresizer.ImageResizerModule */
class ImageResizerModule extends ReactContextBaseJavaModule {
    private Context context;

    public String getName() {
        return "ImageResizerAndroid";
    }

    public ImageResizerModule(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
        this.context = reactApplicationContext;
    }

    @ReactMethod
    public void createResizedImage(String str, int i, int i2, String str2, int i3, int i4, String str3, Callback callback, Callback callback2) {
        try {
            createResizedImageWithExceptions(str, i, i2, str2, i3, i4, str3, callback, callback2);
        } catch (IOException e) {
            callback2.invoke(e.getMessage());
        }
    }

    private void createResizedImageWithExceptions(String str, int i, int i2, String str2, int i3, int i4, String str3, Callback callback, Callback callback2) throws IOException {
        CompressFormat valueOf = CompressFormat.valueOf(str2);
        File createResizedImage = ImageResizer.createResizedImage(this.context, Uri.parse(str), i, i2, valueOf, i3, i4, str3);
        if (createResizedImage.isFile()) {
            WritableMap createMap = Arguments.createMap();
            createMap.putString(RNFetchBlobConst.RNFB_RESPONSE_PATH, createResizedImage.getAbsolutePath());
            createMap.putString(RNFetchBlobConst.DATA_ENCODE_URI, Uri.fromFile(createResizedImage).toString());
            createMap.putString(ConditionalUserProperty.NAME, createResizedImage.getName());
            createMap.putDouble("size", (double) createResizedImage.length());
            callback.invoke(createMap);
            return;
        }
        callback2.invoke("Error getting resized image path");
    }
}

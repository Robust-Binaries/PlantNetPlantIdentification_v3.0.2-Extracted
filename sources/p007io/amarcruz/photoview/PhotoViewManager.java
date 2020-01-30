package p007io.amarcruz.photoview;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils.ScaleType;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.react.bridge.JSApplicationIllegalArgumentException;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import java.util.Map;
import javax.annotation.Nullable;

/* renamed from: io.amarcruz.photoview.PhotoViewManager */
public class PhotoViewManager extends SimpleViewManager<PhotoView> {
    private static final String REACT_CLASS = "PhotoViewAndroid";
    private ResourceDrawableIdHelper mResourceDrawableIdHelper = new ResourceDrawableIdHelper();

    public String getName() {
        return REACT_CLASS;
    }

    PhotoViewManager(ReactApplicationContext reactApplicationContext) {
    }

    /* access modifiers changed from: protected */
    public PhotoView createViewInstance(ThemedReactContext themedReactContext) {
        return new PhotoView(themedReactContext);
    }

    @ReactProp(name = "src")
    public void setSource(PhotoView photoView, @Nullable ReadableMap readableMap) {
        photoView.setSource(readableMap, this.mResourceDrawableIdHelper);
    }

    @ReactProp(name = "loadingIndicatorSrc")
    public void setLoadingIndicatorSource(PhotoView photoView, @Nullable String str) {
        photoView.setLoadingIndicatorSource(str, this.mResourceDrawableIdHelper);
    }

    @ReactProp(name = "fadeDuration")
    public void setFadeDuration(PhotoView photoView, int i) {
        photoView.setFadeDuration(i);
    }

    @ReactProp(name = "shouldNotifyLoadEvents")
    public void setLoadHandlersRegistered(PhotoView photoView, boolean z) {
        photoView.setShouldNotifyLoadEvents(z);
    }

    @ReactProp(name = "minimumZoomScale")
    public void setMinimumZoomScale(PhotoView photoView, float f) {
        photoView.setMinimumScale(f);
    }

    @ReactProp(name = "maximumZoomScale")
    public void setMaximumZoomScale(PhotoView photoView, float f) {
        photoView.setMaximumScale(f);
    }

    @ReactProp(name = "scale")
    public void setScale(PhotoView photoView, float f) {
        photoView.setScale(f, true);
    }

    @ReactProp(name = "zoomTransitionDuration")
    public void setZoomTransitionDuration(PhotoView photoView, int i) {
        photoView.setZoomTransitionDuration((long) i);
    }

    @ReactProp(name = "resizeMode")
    public void setScaleType(PhotoView photoView, @Nullable String str) {
        ScaleType scaleType;
        if (str == null) {
            scaleType = ScaleType.CENTER_CROP;
        } else {
            char c = 65535;
            switch (str.hashCode()) {
                case -1881872635:
                    if (str.equals("stretch")) {
                        c = 5;
                        break;
                    }
                    break;
                case -1364013995:
                    if (str.equals("center")) {
                        c = 0;
                        break;
                    }
                    break;
                case -1274298614:
                    if (str.equals("fitEnd")) {
                        c = 4;
                        break;
                    }
                    break;
                case -522179887:
                    if (str.equals("fitStart")) {
                        c = 3;
                        break;
                    }
                    break;
                case 94852023:
                    if (str.equals("cover")) {
                        c = 2;
                        break;
                    }
                    break;
                case 951526612:
                    if (str.equals("contain")) {
                        c = 1;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    scaleType = ScaleType.CENTER_INSIDE;
                    break;
                case 1:
                    scaleType = ScaleType.FIT_CENTER;
                    break;
                case 2:
                    scaleType = ScaleType.CENTER_CROP;
                    break;
                case 3:
                    scaleType = ScaleType.FIT_START;
                    break;
                case 4:
                    scaleType = ScaleType.FIT_END;
                    break;
                case 5:
                    scaleType = ScaleType.FIT_XY;
                    break;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Invalid resize mode: '");
                    sb.append(str);
                    sb.append("'");
                    throw new JSApplicationIllegalArgumentException(sb.toString());
            }
        }
        ((GenericDraweeHierarchy) photoView.getHierarchy()).setActualImageScaleType(scaleType);
    }

    @Nullable
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        return MapBuilder.m127of(ImageEvent.eventNameForType(1), MapBuilder.m121of("registrationName", "onPhotoViewerError"), ImageEvent.eventNameForType(4), MapBuilder.m121of("registrationName", "onPhotoViewerLoadStart"), ImageEvent.eventNameForType(2), MapBuilder.m121of("registrationName", "onPhotoViewerLoad"), ImageEvent.eventNameForType(3), MapBuilder.m121of("registrationName", "onPhotoViewerLoadEnd"), ImageEvent.eventNameForType(5), MapBuilder.m121of("registrationName", "onPhotoViewerTap"), ImageEvent.eventNameForType(6), MapBuilder.m121of("registrationName", "onPhotoViewerViewTap"), ImageEvent.eventNameForType(7), MapBuilder.m121of("registrationName", "onPhotoViewerScale"));
    }

    /* access modifiers changed from: protected */
    public void onAfterUpdateTransaction(PhotoView photoView) {
        super.onAfterUpdateTransaction(photoView);
        photoView.maybeUpdateView(Fresco.newDraweeControllerBuilder());
    }
}

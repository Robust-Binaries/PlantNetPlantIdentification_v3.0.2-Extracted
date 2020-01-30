package p007io.invertase.firebase.admob;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.PixelUtil;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.facebook.react.views.view.ReactViewGroup;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import java.util.Map;
import javax.annotation.Nullable;

/* renamed from: io.invertase.firebase.admob.RNFirebaseAdMobBanner */
public class RNFirebaseAdMobBanner extends SimpleViewManager<ReactViewGroup> {
    private static final String BANNER_EVENT = "onBannerEvent";
    private static final String REACT_CLASS = "RNFirebaseAdMobBanner";
    /* access modifiers changed from: private */
    public ThemedReactContext context;
    private RCTEventEmitter emitter;
    private Builder request;
    private Boolean requested = Boolean.valueOf(false);
    private AdSize size;
    private String unitId;
    private ReactViewGroup viewGroup;

    /* renamed from: io.invertase.firebase.admob.RNFirebaseAdMobBanner$Events */
    public enum Events {
        EVENT_AD_SIZE_CHANGE("onSizeChange"),
        EVENT_AD_LOADED("onAdLoaded"),
        EVENT_AD_FAILED_TO_LOAD("onAdFailedToLoad"),
        EVENT_AD_OPENED("onAdOpened"),
        EVENT_AD_CLOSED("onAdClosed"),
        EVENT_AD_LEFT_APPLICATION("onAdLeftApplication");
        
        private final String event;

        private Events(String str) {
            this.event = str;
        }

        public String toString() {
            return this.event;
        }
    }

    public String getName() {
        return REACT_CLASS;
    }

    public ReactViewGroup createViewInstance(ThemedReactContext themedReactContext) {
        this.context = themedReactContext;
        this.viewGroup = new ReactViewGroup(themedReactContext);
        this.emitter = (RCTEventEmitter) themedReactContext.getJSModule(RCTEventEmitter.class);
        this.viewGroup.addView(new AdView(this.context));
        setAdListener();
        return this.viewGroup;
    }

    private AdView getAdView() {
        return this.viewGroup.getChildAt(0);
    }

    private void resetAdView() {
        AdView adView = getAdView();
        AdView adView2 = new AdView(this.context);
        this.viewGroup.removeViewAt(0);
        if (adView != null) {
            adView.destroy();
        }
        this.viewGroup.addView(adView2);
        setAdListener();
    }

    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        MapBuilder.Builder builder = MapBuilder.builder();
        builder.put(BANNER_EVENT, MapBuilder.m121of("registrationName", BANNER_EVENT));
        return builder.build();
    }

    @ReactProp(name = "unitId")
    public void setUnitId(ReactViewGroup reactViewGroup, String str) {
        this.unitId = str;
        requestAd();
    }

    @ReactProp(name = "request")
    public void setRequest(ReactViewGroup reactViewGroup, ReadableMap readableMap) {
        this.request = RNFirebaseAdMobUtils.buildRequest(readableMap);
        requestAd();
    }

    @ReactProp(name = "size")
    public void setSize(ReactViewGroup reactViewGroup, String str) {
        int i;
        int i2;
        this.size = RNFirebaseAdMobUtils.stringToAdSize(str);
        WritableMap createMap = Arguments.createMap();
        if (this.size == AdSize.SMART_BANNER) {
            i = (int) PixelUtil.toDIPFromPixel((float) this.size.getWidthInPixels(this.context));
            i2 = (int) PixelUtil.toDIPFromPixel((float) this.size.getHeightInPixels(this.context));
        } else {
            i = this.size.getWidth();
            i2 = this.size.getHeight();
        }
        createMap.putDouble("width", (double) i);
        createMap.putDouble("height", (double) i2);
        sendEvent(Events.EVENT_AD_SIZE_CHANGE.toString(), createMap);
        requestAd();
    }

    private void requestAd() {
        if (this.size != null && this.unitId != null && this.request != null) {
            if (this.requested.booleanValue()) {
                resetAdView();
            }
            AdView adView = getAdView();
            adView.setAdUnitId(this.unitId);
            adView.setAdSize(this.size);
            AdRequest build = this.request.build();
            this.requested = Boolean.valueOf(true);
            adView.loadAd(build);
        }
    }

    private void setAdListener() {
        final AdView adView = getAdView();
        adView.setAdListener(new AdListener() {
            public void onAdLoaded() {
                int left = adView.getLeft();
                int top = adView.getTop();
                int widthInPixels = adView.getAdSize().getWidthInPixels(RNFirebaseAdMobBanner.this.context);
                int heightInPixels = adView.getAdSize().getHeightInPixels(RNFirebaseAdMobBanner.this.context);
                adView.measure(widthInPixels, heightInPixels);
                adView.layout(left, top, left + widthInPixels, top + heightInPixels);
                WritableMap createMap = Arguments.createMap();
                createMap.putBoolean(p007io.invertase.firebase.admob.RNFirebaseAdMobNativeExpress.Events.EVENT_AD_VIDEO_CONTENT.toString(), false);
                createMap.putInt("width", widthInPixels);
                createMap.putInt("height", heightInPixels);
                RNFirebaseAdMobBanner.this.sendEvent(Events.EVENT_AD_LOADED.toString(), createMap);
            }

            public void onAdFailedToLoad(int i) {
                RNFirebaseAdMobBanner.this.sendEvent(Events.EVENT_AD_FAILED_TO_LOAD.toString(), RNFirebaseAdMobUtils.errorCodeToMap(i));
            }

            public void onAdOpened() {
                RNFirebaseAdMobBanner.this.sendEvent(Events.EVENT_AD_OPENED.toString(), null);
            }

            public void onAdClosed() {
                RNFirebaseAdMobBanner.this.sendEvent(Events.EVENT_AD_CLOSED.toString(), null);
            }

            public void onAdLeftApplication() {
                RNFirebaseAdMobBanner.this.sendEvent(Events.EVENT_AD_LEFT_APPLICATION.toString(), null);
            }
        });
    }

    /* access modifiers changed from: private */
    public void sendEvent(String str, @Nullable WritableMap writableMap) {
        WritableMap createMap = Arguments.createMap();
        createMap.putString("type", str);
        if (writableMap != null) {
            createMap.putMap("payload", writableMap);
        }
        this.emitter.receiveEvent(this.viewGroup.getId(), BANNER_EVENT, createMap);
    }
}

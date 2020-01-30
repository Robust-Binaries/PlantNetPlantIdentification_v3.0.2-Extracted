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
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoController.VideoLifecycleCallbacks;
import com.google.android.gms.ads.VideoOptions;
import java.util.Map;
import javax.annotation.Nullable;

/* renamed from: io.invertase.firebase.admob.RNFirebaseAdMobNativeExpress */
public class RNFirebaseAdMobNativeExpress extends SimpleViewManager<ReactViewGroup> {
    private static final String BANNER_EVENT = "onBannerEvent";
    private static final String REACT_CLASS = "RNFirebaseAdMobNativeExpress";
    /* access modifiers changed from: private */
    public ThemedReactContext context;
    private RCTEventEmitter emitter;
    private Builder request;
    private Boolean requested = Boolean.valueOf(false);
    private AdSize size;
    private String unitId;
    private VideoOptions.Builder videoOptions;
    private ReactViewGroup viewGroup;

    /* renamed from: io.invertase.firebase.admob.RNFirebaseAdMobNativeExpress$Events */
    public enum Events {
        EVENT_AD_SIZE_CHANGE("onSizeChange"),
        EVENT_AD_LOADED("onAdLoaded"),
        EVENT_AD_FAILED_TO_LOAD("onAdFailedToLoad"),
        EVENT_AD_OPENED("onAdOpened"),
        EVENT_AD_CLOSED("onAdClosed"),
        EVENT_AD_LEFT_APPLICATION("onAdLeftApplication"),
        EVENT_AD_VIDEO_END("onVideoEnd"),
        EVENT_AD_VIDEO_MUTE("onVideoMute"),
        EVENT_AD_VIDEO_PAUSE("onVideoPause"),
        EVENT_AD_VIDEO_PLAY("onVideoPlay"),
        EVENT_AD_VIDEO_START("onVideoStart"),
        EVENT_AD_VIDEO_CONTENT("hasVideoContent");
        
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
        this.viewGroup.addView(new NativeExpressAdView(this.context));
        setAdListener();
        return this.viewGroup;
    }

    private NativeExpressAdView getAdView() {
        return this.viewGroup.getChildAt(0);
    }

    private void resetAdView() {
        NativeExpressAdView adView = getAdView();
        NativeExpressAdView nativeExpressAdView = new NativeExpressAdView(this.context);
        this.viewGroup.removeViewAt(0);
        if (adView != null) {
            adView.destroy();
        }
        this.viewGroup.addView(nativeExpressAdView);
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

    @ReactProp(name = "video")
    public void setVideoOptions(ReactViewGroup reactViewGroup, ReadableMap readableMap) {
        this.videoOptions = RNFirebaseAdMobUtils.buildVideoOptions(readableMap);
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
        if (this.size != null && this.unitId != null && this.request != null && this.videoOptions != null) {
            if (this.requested.booleanValue()) {
                resetAdView();
            }
            NativeExpressAdView adView = getAdView();
            adView.setAdUnitId(this.unitId);
            adView.setAdSize(this.size);
            adView.setVideoOptions(this.videoOptions.build());
            AdRequest build = this.request.build();
            this.requested = Boolean.valueOf(true);
            adView.loadAd(build);
        }
    }

    private void setAdListener() {
        final NativeExpressAdView adView = getAdView();
        adView.setAdListener(new AdListener() {
            public void onAdLoaded() {
                int left = adView.getLeft();
                int top = adView.getTop();
                int widthInPixels = adView.getAdSize().getWidthInPixels(RNFirebaseAdMobNativeExpress.this.context);
                int heightInPixels = adView.getAdSize().getHeightInPixels(RNFirebaseAdMobNativeExpress.this.context);
                adView.measure(widthInPixels, heightInPixels);
                adView.layout(left, top, left + widthInPixels, top + heightInPixels);
                VideoController videoController = adView.getVideoController();
                WritableMap createMap = Arguments.createMap();
                createMap.putBoolean(Events.EVENT_AD_VIDEO_CONTENT.toString(), videoController.hasVideoContent());
                createMap.putInt("width", widthInPixels);
                createMap.putInt("height", heightInPixels);
                RNFirebaseAdMobNativeExpress.this.sendEvent(Events.EVENT_AD_LOADED.toString(), createMap);
                if (videoController.hasVideoContent()) {
                    videoController.setVideoLifecycleCallbacks(new VideoLifecycleCallbacks() {
                        public void onVideoEnd() {
                            RNFirebaseAdMobNativeExpress.this.sendEvent(Events.EVENT_AD_VIDEO_END.toString(), null);
                        }

                        public void onVideoMute(boolean z) {
                            WritableMap createMap = Arguments.createMap();
                            createMap.putBoolean("isMuted", z);
                            RNFirebaseAdMobNativeExpress.this.sendEvent(Events.EVENT_AD_VIDEO_MUTE.toString(), createMap);
                        }

                        public void onVideoPause() {
                            RNFirebaseAdMobNativeExpress.this.sendEvent(Events.EVENT_AD_VIDEO_PAUSE.toString(), null);
                        }

                        public void onVideoPlay() {
                            RNFirebaseAdMobNativeExpress.this.sendEvent(Events.EVENT_AD_VIDEO_PLAY.toString(), null);
                        }

                        public void onVideoStart() {
                            RNFirebaseAdMobNativeExpress.this.sendEvent(Events.EVENT_AD_VIDEO_START.toString(), null);
                        }
                    });
                }
            }

            public void onAdFailedToLoad(int i) {
                RNFirebaseAdMobNativeExpress.this.sendEvent(Events.EVENT_AD_FAILED_TO_LOAD.toString(), RNFirebaseAdMobUtils.errorCodeToMap(i));
            }

            public void onAdOpened() {
                RNFirebaseAdMobNativeExpress.this.sendEvent(Events.EVENT_AD_OPENED.toString(), null);
            }

            public void onAdClosed() {
                RNFirebaseAdMobNativeExpress.this.sendEvent(Events.EVENT_AD_CLOSED.toString(), null);
            }

            public void onAdLeftApplication() {
                RNFirebaseAdMobNativeExpress.this.sendEvent(Events.EVENT_AD_LEFT_APPLICATION.toString(), null);
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

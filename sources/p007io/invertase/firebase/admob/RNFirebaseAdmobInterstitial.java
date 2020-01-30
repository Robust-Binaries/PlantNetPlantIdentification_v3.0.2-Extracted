package p007io.invertase.firebase.admob;

import android.app.Activity;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import javax.annotation.Nullable;
import p007io.invertase.firebase.C1350Utils;

/* renamed from: io.invertase.firebase.admob.RNFirebaseAdmobInterstitial */
class RNFirebaseAdmobInterstitial {
    private RNFirebaseAdMob adMob;
    private String adUnit;
    /* access modifiers changed from: private */
    public InterstitialAd interstitialAd = new InterstitialAd(this.adMob.getContext());

    RNFirebaseAdmobInterstitial(String str, RNFirebaseAdMob rNFirebaseAdMob) {
        this.adUnit = str;
        this.adMob = rNFirebaseAdMob;
        this.interstitialAd.setAdUnitId(this.adUnit);
        this.interstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                RNFirebaseAdmobInterstitial.this.sendEvent("onAdLoaded", null);
            }

            public void onAdOpened() {
                RNFirebaseAdmobInterstitial.this.sendEvent("onAdOpened", null);
            }

            public void onAdLeftApplication() {
                RNFirebaseAdmobInterstitial.this.sendEvent("onAdLeftApplication", null);
            }

            public void onAdClosed() {
                RNFirebaseAdmobInterstitial.this.sendEvent("onAdClosed", null);
            }

            public void onAdFailedToLoad(int i) {
                RNFirebaseAdmobInterstitial.this.sendEvent("onAdFailedToLoad", RNFirebaseAdMobUtils.errorCodeToMap(i));
            }
        });
    }

    /* access modifiers changed from: 0000 */
    public void loadAd(final AdRequest adRequest) {
        Activity activity = this.adMob.getActivity();
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    RNFirebaseAdmobInterstitial.this.interstitialAd.loadAd(adRequest);
                }
            });
        }
    }

    /* access modifiers changed from: 0000 */
    public void show() {
        Activity activity = this.adMob.getActivity();
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    if (RNFirebaseAdmobInterstitial.this.interstitialAd.isLoaded()) {
                        RNFirebaseAdmobInterstitial.this.interstitialAd.show();
                    }
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void sendEvent(String str, @Nullable WritableMap writableMap) {
        WritableMap createMap = Arguments.createMap();
        createMap.putString("type", str);
        createMap.putString("adUnit", this.adUnit);
        if (writableMap != null) {
            createMap.putMap("payload", writableMap);
        }
        C1350Utils.sendEvent(this.adMob.getContext(), "interstitial_event", createMap);
    }
}

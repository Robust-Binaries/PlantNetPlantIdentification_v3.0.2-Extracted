package p007io.invertase.firebase.admob;

import android.app.Activity;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import javax.annotation.Nullable;
import p007io.invertase.firebase.C1350Utils;

/* renamed from: io.invertase.firebase.admob.RNFirebaseAdMobRewardedVideo */
public class RNFirebaseAdMobRewardedVideo implements RewardedVideoAdListener {
    private RNFirebaseAdMob adMob;
    /* access modifiers changed from: private */
    public String adUnit;
    /* access modifiers changed from: private */
    public RewardedVideoAd rewardedVideo = MobileAds.getRewardedVideoAdInstance(this.adMob.getContext());

    RNFirebaseAdMobRewardedVideo(String str, RNFirebaseAdMob rNFirebaseAdMob) {
        this.adUnit = str;
        this.adMob = rNFirebaseAdMob;
        Activity activity = this.adMob.getActivity();
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    RNFirebaseAdMobRewardedVideo.this.rewardedVideo.setRewardedVideoAdListener(this);
                }
            });
        }
    }

    /* access modifiers changed from: 0000 */
    public void loadAd(final AdRequest adRequest) {
        Activity activity = this.adMob.getActivity();
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    RNFirebaseAdMobRewardedVideo.this.rewardedVideo.loadAd(RNFirebaseAdMobRewardedVideo.this.adUnit, adRequest);
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
                    if (RNFirebaseAdMobRewardedVideo.this.rewardedVideo.isLoaded()) {
                        RNFirebaseAdMobRewardedVideo.this.rewardedVideo.show();
                    }
                }
            });
        }
    }

    public void onRewarded(RewardItem rewardItem) {
        WritableMap createMap = Arguments.createMap();
        createMap.putInt("amount", rewardItem.getAmount());
        createMap.putString("type", rewardItem.getType());
        sendEvent("onRewarded", createMap);
    }

    public void onRewardedVideoAdLeftApplication() {
        sendEvent("onAdLeftApplication", null);
    }

    public void onRewardedVideoAdClosed() {
        sendEvent("onAdClosed", null);
    }

    public void onRewardedVideoCompleted() {
        sendEvent("onAdCompleted", null);
    }

    public void onRewardedVideoAdFailedToLoad(int i) {
        sendEvent("onAdFailedToLoad", RNFirebaseAdMobUtils.errorCodeToMap(i));
    }

    public void onRewardedVideoAdLoaded() {
        sendEvent("onAdLoaded", null);
    }

    public void onRewardedVideoAdOpened() {
        sendEvent("onAdOpened", null);
    }

    public void onRewardedVideoStarted() {
        sendEvent("onRewardedVideoStarted", null);
    }

    private void sendEvent(String str, @Nullable WritableMap writableMap) {
        WritableMap createMap = Arguments.createMap();
        createMap.putString("type", str);
        createMap.putString("adUnit", this.adUnit);
        if (writableMap != null) {
            createMap.putMap("payload", writableMap);
        }
        C1350Utils.sendEvent(this.adMob.getContext(), "rewarded_video_event", createMap);
    }
}

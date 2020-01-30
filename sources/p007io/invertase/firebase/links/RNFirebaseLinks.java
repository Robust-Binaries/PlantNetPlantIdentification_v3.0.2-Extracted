package p007io.invertase.firebase.links;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.p000v4.app.NotificationCompat;
import android.util.Log;
import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import com.google.firebase.appinvite.FirebaseAppInvite;
import com.google.firebase.dynamiclinks.DynamicLink.AndroidParameters;
import com.google.firebase.dynamiclinks.DynamicLink.Builder;
import com.google.firebase.dynamiclinks.DynamicLink.GoogleAnalyticsParameters;
import com.google.firebase.dynamiclinks.DynamicLink.IosParameters;
import com.google.firebase.dynamiclinks.DynamicLink.ItunesConnectAnalyticsParameters;
import com.google.firebase.dynamiclinks.DynamicLink.NavigationInfoParameters;
import com.google.firebase.dynamiclinks.DynamicLink.SocialMetaTagParameters;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import javax.annotation.Nonnull;
import p007io.invertase.firebase.C1350Utils;

/* renamed from: io.invertase.firebase.links.RNFirebaseLinks */
public class RNFirebaseLinks extends ReactContextBaseJavaModule implements ActivityEventListener, LifecycleEventListener {
    /* access modifiers changed from: private */
    public static final String TAG = RNFirebaseLinks.class.getCanonicalName();
    /* access modifiers changed from: private */
    public String mInitialLink = null;
    /* access modifiers changed from: private */
    public boolean mInitialLinkInitialized = false;

    public String getName() {
        return "RNFirebaseLinks";
    }

    public void onActivityResult(Activity activity, int i, int i2, Intent intent) {
    }

    public void onHostPause() {
    }

    public void onHostResume() {
    }

    public RNFirebaseLinks(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
        getReactApplicationContext().addActivityEventListener(this);
        getReactApplicationContext().addLifecycleEventListener(this);
    }

    @ReactMethod
    public void createDynamicLink(ReadableMap readableMap, Promise promise) {
        try {
            String uri = getDynamicLinkBuilder(readableMap).buildDynamicLink().getUri().toString();
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("created dynamic link: ");
            sb.append(uri);
            Log.d(str, sb.toString());
            promise.resolve(uri);
        } catch (Exception e) {
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("create dynamic link failure ");
            sb2.append(e.getMessage());
            Log.e(str2, sb2.toString());
            promise.reject("links/failure", e.getMessage(), (Throwable) e);
        }
    }

    @ReactMethod
    public void createShortDynamicLink(ReadableMap readableMap, String str, final Promise promise) {
        Task task;
        try {
            Builder dynamicLinkBuilder = getDynamicLinkBuilder(readableMap);
            if ("SHORT".equals(str)) {
                task = dynamicLinkBuilder.buildShortDynamicLink(2);
            } else if ("UNGUESSABLE".equals(str)) {
                task = dynamicLinkBuilder.buildShortDynamicLink(1);
            } else {
                task = dynamicLinkBuilder.buildShortDynamicLink();
            }
            task.addOnCompleteListener(new OnCompleteListener<ShortDynamicLink>() {
                public void onComplete(@Nonnull Task<ShortDynamicLink> task) {
                    if (task.isSuccessful()) {
                        String uri = ((ShortDynamicLink) task.getResult()).getShortLink().toString();
                        String access$000 = RNFirebaseLinks.TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("created short dynamic link: ");
                        sb.append(uri);
                        Log.d(access$000, sb.toString());
                        promise.resolve(uri);
                        return;
                    }
                    String access$0002 = RNFirebaseLinks.TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("create short dynamic link failure ");
                    sb2.append(task.getException().getMessage());
                    Log.e(access$0002, sb2.toString());
                    promise.reject("links/failure", task.getException().getMessage(), (Throwable) task.getException());
                }
            });
        } catch (Exception e) {
            String str2 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("create short dynamic link failure ");
            sb.append(e.getMessage());
            Log.e(str2, sb.toString());
            promise.reject("links/failure", e.getMessage(), (Throwable) e);
        }
    }

    @ReactMethod
    public void getInitialLink(final Promise promise) {
        if (this.mInitialLinkInitialized) {
            promise.resolve(this.mInitialLink);
        } else if (getCurrentActivity() != null) {
            FirebaseDynamicLinks.getInstance().getDynamicLink(getCurrentActivity().getIntent()).addOnSuccessListener(new OnSuccessListener<PendingDynamicLinkData>() {
                public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                    if (pendingDynamicLinkData != null && !RNFirebaseLinks.this.isInvitation(pendingDynamicLinkData)) {
                        RNFirebaseLinks.this.mInitialLink = pendingDynamicLinkData.getLink().toString();
                    }
                    String access$000 = RNFirebaseLinks.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("getInitialLink: link is: ");
                    sb.append(RNFirebaseLinks.this.mInitialLink);
                    Log.d(access$000, sb.toString());
                    RNFirebaseLinks.this.mInitialLinkInitialized = true;
                    promise.resolve(RNFirebaseLinks.this.mInitialLink);
                }
            }).addOnFailureListener(new OnFailureListener() {
                public void onFailure(@Nonnull Exception exc) {
                    Log.e(RNFirebaseLinks.TAG, "getInitialLink: failed to resolve link", exc);
                    promise.reject("link/initial-link-error", exc.getMessage(), (Throwable) exc);
                }
            });
        } else {
            Log.d(TAG, "getInitialLink: activity is null");
            promise.resolve(null);
        }
    }

    public void onNewIntent(Intent intent) {
        FirebaseDynamicLinks.getInstance().getDynamicLink(intent).addOnSuccessListener(new OnSuccessListener<PendingDynamicLinkData>() {
            public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                if (pendingDynamicLinkData != null && !RNFirebaseLinks.this.isInvitation(pendingDynamicLinkData)) {
                    C1350Utils.sendEvent(RNFirebaseLinks.this.getReactApplicationContext(), "links_link_received", pendingDynamicLinkData.getLink().toString());
                }
            }
        });
    }

    public void onHostDestroy() {
        this.mInitialLink = null;
        this.mInitialLinkInitialized = false;
    }

    /* access modifiers changed from: private */
    public boolean isInvitation(PendingDynamicLinkData pendingDynamicLinkData) {
        FirebaseAppInvite invitation = FirebaseAppInvite.getInvitation(pendingDynamicLinkData);
        return (invitation == null || invitation.getInvitationId() == null || invitation.getInvitationId().isEmpty()) ? false : true;
    }

    private Builder getDynamicLinkBuilder(ReadableMap readableMap) {
        Builder createDynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink();
        try {
            createDynamicLink.setLink(Uri.parse(readableMap.getString("link")));
            createDynamicLink.setDynamicLinkDomain(readableMap.getString("dynamicLinkDomain"));
            setAnalyticsParameters(readableMap.getMap("analytics"), createDynamicLink);
            setAndroidParameters(readableMap.getMap("android"), createDynamicLink);
            setIosParameters(readableMap.getMap("ios"), createDynamicLink);
            setITunesParameters(readableMap.getMap("itunes"), createDynamicLink);
            setNavigationParameters(readableMap.getMap("navigation"), createDynamicLink);
            setSocialParameters(readableMap.getMap(NotificationCompat.CATEGORY_SOCIAL), createDynamicLink);
            return createDynamicLink;
        } catch (Exception e) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("error while building parameters ");
            sb.append(e.getMessage());
            Log.e(str, sb.toString());
            throw e;
        }
    }

    private void setAnalyticsParameters(ReadableMap readableMap, Builder builder) {
        GoogleAnalyticsParameters.Builder builder2 = new GoogleAnalyticsParameters.Builder();
        if (readableMap.hasKey(Param.CAMPAIGN)) {
            builder2.setCampaign(readableMap.getString(Param.CAMPAIGN));
        }
        if (readableMap.hasKey("content")) {
            builder2.setContent(readableMap.getString("content"));
        }
        if (readableMap.hasKey(Param.MEDIUM)) {
            builder2.setMedium(readableMap.getString(Param.MEDIUM));
        }
        if (readableMap.hasKey(Param.SOURCE)) {
            builder2.setSource(readableMap.getString(Param.SOURCE));
        }
        if (readableMap.hasKey(Param.TERM)) {
            builder2.setTerm(readableMap.getString(Param.TERM));
        }
        builder.setGoogleAnalyticsParameters(builder2.build());
    }

    private void setAndroidParameters(ReadableMap readableMap, Builder builder) {
        if (readableMap.hasKey("packageName")) {
            AndroidParameters.Builder builder2 = new AndroidParameters.Builder(readableMap.getString("packageName"));
            if (readableMap.hasKey("fallbackUrl")) {
                builder2.setFallbackUrl(Uri.parse(readableMap.getString("fallbackUrl")));
            }
            if (readableMap.hasKey("minimumVersion")) {
                builder2.setMinimumVersion(Integer.parseInt(readableMap.getString("minimumVersion")));
            }
            builder.setAndroidParameters(builder2.build());
        }
    }

    private void setIosParameters(ReadableMap readableMap, Builder builder) {
        if (readableMap.hasKey("bundleId")) {
            IosParameters.Builder builder2 = new IosParameters.Builder(readableMap.getString("bundleId"));
            if (readableMap.hasKey("appStoreId")) {
                builder2.setAppStoreId(readableMap.getString("appStoreId"));
            }
            if (readableMap.hasKey("customScheme")) {
                builder2.setCustomScheme(readableMap.getString("customScheme"));
            }
            if (readableMap.hasKey("fallbackUrl")) {
                builder2.setFallbackUrl(Uri.parse(readableMap.getString("fallbackUrl")));
            }
            if (readableMap.hasKey("iPadBundleId")) {
                builder2.setIpadBundleId(readableMap.getString("iPadBundleId"));
            }
            if (readableMap.hasKey("iPadFallbackUrl")) {
                builder2.setIpadFallbackUrl(Uri.parse(readableMap.getString("iPadFallbackUrl")));
            }
            if (readableMap.hasKey("minimumVersion")) {
                builder2.setMinimumVersion(readableMap.getString("minimumVersion"));
            }
            builder.setIosParameters(builder2.build());
        }
    }

    private void setITunesParameters(ReadableMap readableMap, Builder builder) {
        ItunesConnectAnalyticsParameters.Builder builder2 = new ItunesConnectAnalyticsParameters.Builder();
        if (readableMap.hasKey("affiliateToken")) {
            builder2.setAffiliateToken(readableMap.getString("affiliateToken"));
        }
        if (readableMap.hasKey("campaignToken")) {
            builder2.setCampaignToken(readableMap.getString("campaignToken"));
        }
        if (readableMap.hasKey("providerToken")) {
            builder2.setProviderToken(readableMap.getString("providerToken"));
        }
        builder.setItunesConnectAnalyticsParameters(builder2.build());
    }

    private void setNavigationParameters(ReadableMap readableMap, Builder builder) {
        NavigationInfoParameters.Builder builder2 = new NavigationInfoParameters.Builder();
        if (readableMap.hasKey("forcedRedirectEnabled")) {
            builder2.setForcedRedirectEnabled(readableMap.getBoolean("forcedRedirectEnabled"));
        }
        builder.setNavigationInfoParameters(builder2.build());
    }

    private void setSocialParameters(ReadableMap readableMap, Builder builder) {
        SocialMetaTagParameters.Builder builder2 = new SocialMetaTagParameters.Builder();
        if (readableMap.hasKey("descriptionText")) {
            builder2.setDescription(readableMap.getString("descriptionText"));
        }
        if (readableMap.hasKey("imageUrl")) {
            builder2.setImageUrl(Uri.parse(readableMap.getString("imageUrl")));
        }
        if (readableMap.hasKey("title")) {
            builder2.setTitle(readableMap.getString("title"));
        }
        builder.setSocialMetaTagParameters(builder2.build());
    }
}

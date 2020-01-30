package p007io.invertase.firebase.invites;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.WritableMap;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.appinvite.AppInviteInvitation.IntentBuilder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.appinvite.FirebaseAppInvite;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import java.util.Arrays;
import java.util.HashMap;
import javax.annotation.Nonnull;
import p007io.invertase.firebase.C1350Utils;

/* renamed from: io.invertase.firebase.invites.RNFirebaseInvites */
public class RNFirebaseInvites extends ReactContextBaseJavaModule implements ActivityEventListener, LifecycleEventListener {
    private static final int REQUEST_INVITE = 17517;
    private static final String TAG = "RNFirebaseInvites";
    /* access modifiers changed from: private */
    public String mInitialDeepLink = null;
    /* access modifiers changed from: private */
    public String mInitialInvitationId = null;
    /* access modifiers changed from: private */
    public boolean mInitialInvitationInitialized = false;
    private Promise mPromise = null;

    public String getName() {
        return TAG;
    }

    public void onHostPause() {
    }

    public void onHostResume() {
    }

    public RNFirebaseInvites(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
        getReactApplicationContext().addActivityEventListener(this);
    }

    @ReactMethod
    public void getInitialInvitation(final Promise promise) {
        if (this.mInitialInvitationInitialized) {
            if (this.mInitialDeepLink == null && this.mInitialInvitationId == null) {
                promise.resolve(null);
            } else {
                promise.resolve(buildInvitationMap(this.mInitialDeepLink, this.mInitialInvitationId));
            }
        } else if (getCurrentActivity() != null) {
            FirebaseDynamicLinks.getInstance().getDynamicLink(getCurrentActivity().getIntent()).addOnSuccessListener(new OnSuccessListener<PendingDynamicLinkData>() {
                public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                    if (pendingDynamicLinkData != null) {
                        FirebaseAppInvite invitation = FirebaseAppInvite.getInvitation(pendingDynamicLinkData);
                        if (invitation == null) {
                            promise.resolve(null);
                            return;
                        }
                        RNFirebaseInvites.this.mInitialDeepLink = pendingDynamicLinkData.getLink().toString();
                        RNFirebaseInvites.this.mInitialInvitationId = invitation.getInvitationId();
                        Promise promise = promise;
                        RNFirebaseInvites rNFirebaseInvites = RNFirebaseInvites.this;
                        promise.resolve(rNFirebaseInvites.buildInvitationMap(rNFirebaseInvites.mInitialDeepLink, RNFirebaseInvites.this.mInitialInvitationId));
                    } else {
                        promise.resolve(null);
                    }
                    RNFirebaseInvites.this.mInitialInvitationInitialized = true;
                }
            }).addOnFailureListener(new OnFailureListener() {
                public void onFailure(@Nonnull Exception exc) {
                    Log.e(RNFirebaseInvites.TAG, "getInitialInvitation: failed to resolve invitation", exc);
                    promise.reject("invites/initial-invitation-error", exc.getMessage(), (Throwable) exc);
                }
            });
        } else {
            Log.d(TAG, "getInitialInvitation: activity is null");
            promise.resolve(null);
        }
    }

    @ReactMethod
    public void sendInvitation(ReadableMap readableMap, Promise promise) {
        if (!readableMap.hasKey("message")) {
            promise.reject("invites/invalid-invitation", "The supplied invitation is missing a 'message' field");
        } else if (!readableMap.hasKey("title")) {
            promise.reject("invites/invalid-invitation", "The supplied invitation is missing a 'title' field");
        } else {
            IntentBuilder intentBuilder = new IntentBuilder(readableMap.getString("title"));
            if (readableMap.hasKey("androidMinimumVersionCode")) {
                intentBuilder = intentBuilder.setAndroidMinimumVersionCode(Double.valueOf(readableMap.getDouble("androidMinimumVersionCode")).intValue());
            }
            if (readableMap.hasKey("callToActionText")) {
                intentBuilder = intentBuilder.setCallToActionText(readableMap.getString("callToActionText"));
            }
            if (readableMap.hasKey("customImage")) {
                intentBuilder = intentBuilder.setCustomImage(Uri.parse(readableMap.getString("customImage")));
            }
            if (readableMap.hasKey("deepLink")) {
                intentBuilder = intentBuilder.setDeepLink(Uri.parse(readableMap.getString("deepLink")));
            }
            if (readableMap.hasKey("iosClientId")) {
                intentBuilder = intentBuilder.setOtherPlatformsTargetApplication(1, readableMap.getString("iosClientId"));
            }
            IntentBuilder message = intentBuilder.setMessage(readableMap.getString("message"));
            if (readableMap.hasKey("android")) {
                ReadableMap map = readableMap.getMap("android");
                if (map.hasKey("additionalReferralParameters")) {
                    HashMap hashMap = new HashMap();
                    ReadableMap map2 = map.getMap("additionalReferralParameters");
                    ReadableMapKeySetIterator keySetIterator = map2.keySetIterator();
                    while (keySetIterator.hasNextKey()) {
                        String nextKey = keySetIterator.nextKey();
                        hashMap.put(nextKey, map2.getString(nextKey));
                    }
                    message = message.setAdditionalReferralParameters(hashMap);
                }
                if (map.hasKey("emailHtmlContent")) {
                    message = message.setEmailHtmlContent(map.getString("emailHtmlContent"));
                }
                if (map.hasKey("emailSubject")) {
                    message = message.setEmailSubject(map.getString("emailSubject"));
                }
                if (map.hasKey("googleAnalyticsTrackingId")) {
                    message = message.setGoogleAnalyticsTrackingId(map.getString("googleAnalyticsTrackingId"));
                }
            }
            Intent build = message.build();
            this.mPromise = promise;
            getCurrentActivity().startActivityForResult(build, REQUEST_INVITE);
        }
    }

    public void onActivityResult(Activity activity, int i, int i2, Intent intent) {
        if (i == REQUEST_INVITE) {
            if (i2 == -1) {
                this.mPromise.resolve(Arguments.fromList(Arrays.asList(AppInviteInvitation.getInvitationIds(i2, intent))));
            } else if (i2 == 0) {
                this.mPromise.reject("invites/invitation-cancelled", "Invitation cancelled");
            } else {
                this.mPromise.reject("invites/invitation-error", "Invitation failed to send");
            }
            this.mPromise = null;
        }
    }

    public void onNewIntent(Intent intent) {
        FirebaseDynamicLinks.getInstance().getDynamicLink(intent).addOnSuccessListener(new OnSuccessListener<PendingDynamicLinkData>() {
            public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                if (pendingDynamicLinkData != null) {
                    FirebaseAppInvite invitation = FirebaseAppInvite.getInvitation(pendingDynamicLinkData);
                    if (invitation != null) {
                        C1350Utils.sendEvent(RNFirebaseInvites.this.getReactApplicationContext(), "invites_invitation_received", RNFirebaseInvites.this.buildInvitationMap(pendingDynamicLinkData.getLink().toString(), invitation.getInvitationId()));
                    }
                }
            }
        });
    }

    public void onHostDestroy() {
        this.mInitialDeepLink = null;
        this.mInitialInvitationId = null;
        this.mInitialInvitationInitialized = false;
    }

    /* access modifiers changed from: private */
    public WritableMap buildInvitationMap(String str, String str2) {
        WritableMap createMap = Arguments.createMap();
        createMap.putString("deepLink", str);
        createMap.putString("invitationId", str2);
        return createMap;
    }
}

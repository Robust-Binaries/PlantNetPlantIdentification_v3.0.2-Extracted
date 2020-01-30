package p007io.invertase.firebase.auth;

import android.app.Activity;
import android.net.Uri;
import android.os.Parcel;
import android.util.Log;
import com.facebook.common.util.UriUtil;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.internal.ImagesContract;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.ActionCodeResult;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuth.AuthStateListener;
import com.google.firebase.auth.FirebaseAuth.IdTokenListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken;
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest.Builder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;
import p007io.invertase.firebase.C1350Utils;

/* renamed from: io.invertase.firebase.auth.RNFirebaseAuth */
class RNFirebaseAuth extends ReactContextBaseJavaModule {
    private static final String TAG = "RNFirebaseAuth";
    private static HashMap<String, AuthStateListener> mAuthListeners = new HashMap<>();
    private static HashMap<String, IdTokenListener> mIdTokenListeners = new HashMap<>();
    /* access modifiers changed from: private */
    public PhoneAuthCredential mCredential;
    /* access modifiers changed from: private */
    public ForceResendingToken mForceResendingToken;
    private String mLastPhoneNumber;
    /* access modifiers changed from: private */
    public ReactContext mReactContext;
    /* access modifiers changed from: private */
    public String mVerificationId;

    public String getName() {
        return TAG;
    }

    RNFirebaseAuth(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
        this.mReactContext = reactApplicationContext;
        Log.d(TAG, "instance-created");
    }

    public void initialize() {
        super.initialize();
        Log.d(TAG, "instance-initialized");
    }

    public void onCatalystInstanceDestroy() {
        super.onCatalystInstanceDestroy();
        Log.d(TAG, "instance-destroyed");
        Iterator it = mAuthListeners.entrySet().iterator();
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            FirebaseAuth.getInstance(FirebaseApp.getInstance((String) entry.getKey())).removeAuthStateListener((AuthStateListener) entry.getValue());
            it.remove();
        }
        Iterator it2 = mIdTokenListeners.entrySet().iterator();
        while (it2.hasNext()) {
            Entry entry2 = (Entry) it2.next();
            FirebaseAuth.getInstance(FirebaseApp.getInstance((String) entry2.getKey())).removeIdTokenListener((IdTokenListener) entry2.getValue());
            it2.remove();
        }
    }

    @ReactMethod
    public void addAuthStateListener(final String str) {
        Log.d(TAG, "addAuthStateListener");
        FirebaseAuth instance = FirebaseAuth.getInstance(FirebaseApp.getInstance(str));
        if (((AuthStateListener) mAuthListeners.get(str)) == null) {
            C13611 r1 = new AuthStateListener() {
                public void onAuthStateChanged(@Nonnull FirebaseAuth firebaseAuth) {
                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                    WritableMap createMap = Arguments.createMap();
                    if (currentUser != null) {
                        createMap.putString("appName", str);
                        createMap.putMap("user", RNFirebaseAuth.this.firebaseUserToMap(currentUser));
                        C1350Utils.sendEvent(RNFirebaseAuth.this.mReactContext, "auth_state_changed", createMap);
                        return;
                    }
                    createMap.putString("appName", str);
                    C1350Utils.sendEvent(RNFirebaseAuth.this.mReactContext, "auth_state_changed", createMap);
                }
            };
            instance.addAuthStateListener(r1);
            mAuthListeners.put(str, r1);
        }
    }

    @ReactMethod
    public void removeAuthStateListener(String str) {
        Log.d(TAG, "removeAuthStateListener");
        FirebaseAuth instance = FirebaseAuth.getInstance(FirebaseApp.getInstance(str));
        AuthStateListener authStateListener = (AuthStateListener) mAuthListeners.get(str);
        if (authStateListener != null) {
            instance.removeAuthStateListener(authStateListener);
            mAuthListeners.remove(str);
        }
    }

    @ReactMethod
    public void addIdTokenListener(final String str) {
        Log.d(TAG, "addIdTokenListener");
        FirebaseAuth instance = FirebaseAuth.getInstance(FirebaseApp.getInstance(str));
        if (!mIdTokenListeners.containsKey(str)) {
            C13722 r1 = new IdTokenListener() {
                public void onIdTokenChanged(@Nonnull FirebaseAuth firebaseAuth) {
                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                    WritableMap createMap = Arguments.createMap();
                    if (currentUser != null) {
                        createMap.putBoolean("authenticated", true);
                        createMap.putString("appName", str);
                        createMap.putMap("user", RNFirebaseAuth.this.firebaseUserToMap(currentUser));
                        C1350Utils.sendEvent(RNFirebaseAuth.this.mReactContext, "auth_id_token_changed", createMap);
                        return;
                    }
                    createMap.putString("appName", str);
                    createMap.putBoolean("authenticated", false);
                    C1350Utils.sendEvent(RNFirebaseAuth.this.mReactContext, "auth_id_token_changed", createMap);
                }
            };
            instance.addIdTokenListener(r1);
            mIdTokenListeners.put(str, r1);
        }
    }

    @ReactMethod
    public void removeIdTokenListener(String str) {
        Log.d(TAG, "removeIdTokenListener");
        FirebaseAuth instance = FirebaseAuth.getInstance(FirebaseApp.getInstance(str));
        IdTokenListener idTokenListener = (IdTokenListener) mIdTokenListeners.get(str);
        if (idTokenListener != null) {
            instance.removeIdTokenListener(idTokenListener);
            mIdTokenListeners.remove(str);
        }
    }

    @ReactMethod
    public void setAutoRetrievedSmsCodeForPhoneNumber(String str, String str2, String str3, Promise promise) {
        Log.d(TAG, "setAutoRetrievedSmsCodeForPhoneNumber");
        FirebaseAuth.getInstance(FirebaseApp.getInstance(str)).getFirebaseAuthSettings().setAutoRetrievedSmsCodeForPhoneNumber(str2, str3);
        promise.resolve(null);
    }

    @ReactMethod
    public void signOut(String str, Promise promise) {
        FirebaseAuth instance = FirebaseAuth.getInstance(FirebaseApp.getInstance(str));
        Log.d(TAG, "signOut");
        if (instance == null || instance.getCurrentUser() == null) {
            promiseNoUser(promise, Boolean.valueOf(true));
            return;
        }
        instance.signOut();
        promiseNoUser(promise, Boolean.valueOf(false));
    }

    @ReactMethod
    private void signInAnonymously(String str, final Promise promise) {
        FirebaseAuth instance = FirebaseAuth.getInstance(FirebaseApp.getInstance(str));
        Log.d(TAG, "signInAnonymously");
        instance.signInAnonymously().addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            public void onSuccess(AuthResult authResult) {
                Log.d(RNFirebaseAuth.TAG, "signInAnonymously:onComplete:success");
                RNFirebaseAuth.this.promiseWithAuthResult(authResult, promise);
            }
        }).addOnFailureListener(new OnFailureListener() {
            public void onFailure(@Nonnull Exception exc) {
                Log.e(RNFirebaseAuth.TAG, "signInAnonymously:onComplete:failure", exc);
                RNFirebaseAuth.this.promiseRejectAuthException(promise, exc);
            }
        });
    }

    @ReactMethod
    private void createUserWithEmailAndPassword(String str, String str2, String str3, final Promise promise) {
        Log.d(TAG, "createUserWithEmailAndPassword");
        FirebaseAuth.getInstance(FirebaseApp.getInstance(str)).createUserWithEmailAndPassword(str2, str3).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            public void onSuccess(AuthResult authResult) {
                Log.d(RNFirebaseAuth.TAG, "createUserWithEmailAndPassword:onComplete:success");
                RNFirebaseAuth.this.promiseWithAuthResult(authResult, promise);
            }
        }).addOnFailureListener(new OnFailureListener() {
            public void onFailure(@Nonnull Exception exc) {
                Log.e(RNFirebaseAuth.TAG, "createUserWithEmailAndPassword:onComplete:failure", exc);
                RNFirebaseAuth.this.promiseRejectAuthException(promise, exc);
            }
        });
    }

    @ReactMethod
    private void signInWithEmailAndPassword(String str, String str2, String str3, final Promise promise) {
        Log.d(TAG, "signInWithEmailAndPassword");
        FirebaseAuth.getInstance(FirebaseApp.getInstance(str)).signInWithEmailAndPassword(str2, str3).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            public void onSuccess(AuthResult authResult) {
                Log.d(RNFirebaseAuth.TAG, "signInWithEmailAndPassword:onComplete:success");
                RNFirebaseAuth.this.promiseWithAuthResult(authResult, promise);
            }
        }).addOnFailureListener(new OnFailureListener() {
            public void onFailure(@Nonnull Exception exc) {
                Log.e(RNFirebaseAuth.TAG, "signInWithEmailAndPassword:onComplete:failure", exc);
                RNFirebaseAuth.this.promiseRejectAuthException(promise, exc);
            }
        });
    }

    @ReactMethod
    private void signInWithEmailLink(String str, String str2, String str3, final Promise promise) {
        Log.d(TAG, "signInWithEmailLink");
        FirebaseAuth.getInstance(FirebaseApp.getInstance(str)).signInWithEmailLink(str2, str3).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            public void onSuccess(AuthResult authResult) {
                Log.d(RNFirebaseAuth.TAG, "signInWithEmailLink:onComplete:success");
                RNFirebaseAuth.this.promiseWithAuthResult(authResult, promise);
            }
        }).addOnFailureListener(new OnFailureListener() {
            public void onFailure(@Nonnull Exception exc) {
                Log.e(RNFirebaseAuth.TAG, "signInWithEmailLink:onComplete:failure", exc);
                RNFirebaseAuth.this.promiseRejectAuthException(promise, exc);
            }
        });
    }

    @ReactMethod
    private void signInWithCustomToken(String str, String str2, final Promise promise) {
        Log.d(TAG, "signInWithCustomToken");
        FirebaseAuth.getInstance(FirebaseApp.getInstance(str)).signInWithCustomToken(str2).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            public void onSuccess(AuthResult authResult) {
                Log.d(RNFirebaseAuth.TAG, "signInWithCustomToken:onComplete:success");
                RNFirebaseAuth.this.promiseWithAuthResult(authResult, promise);
            }
        }).addOnFailureListener(new OnFailureListener() {
            public void onFailure(@Nonnull Exception exc) {
                Log.e(RNFirebaseAuth.TAG, "signInWithCustomToken:onComplete:failure", exc);
                RNFirebaseAuth.this.promiseRejectAuthException(promise, exc);
            }
        });
    }

    @ReactMethod
    public void sendPasswordResetEmail(String str, String str2, ReadableMap readableMap, final Promise promise) {
        Log.d(TAG, "sendPasswordResetEmail");
        FirebaseAuth instance = FirebaseAuth.getInstance(FirebaseApp.getInstance(str));
        C136513 r0 = new OnCompleteListener<Void>() {
            public void onComplete(@Nonnull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(RNFirebaseAuth.TAG, "sendPasswordResetEmail:onComplete:success");
                    RNFirebaseAuth.this.promiseNoUser(promise, Boolean.valueOf(false));
                    return;
                }
                Exception exception = task.getException();
                Log.e(RNFirebaseAuth.TAG, "sendPasswordResetEmail:onComplete:failure", exception);
                RNFirebaseAuth.this.promiseRejectAuthException(promise, exception);
            }
        };
        if (readableMap == null) {
            instance.sendPasswordResetEmail(str2).addOnCompleteListener(r0);
        } else {
            instance.sendPasswordResetEmail(str2, buildActionCodeSettings(readableMap)).addOnCompleteListener(r0);
        }
    }

    @ReactMethod
    public void sendSignInLinkToEmail(String str, String str2, ReadableMap readableMap, final Promise promise) {
        Log.d(TAG, "sendSignInLinkToEmail");
        FirebaseAuth instance = FirebaseAuth.getInstance(FirebaseApp.getInstance(str));
        instance.sendSignInLinkToEmail(str2, buildActionCodeSettings(readableMap)).addOnCompleteListener(new OnCompleteListener<Void>() {
            public void onComplete(@Nonnull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(RNFirebaseAuth.TAG, "sendSignInLinkToEmail:onComplete:success");
                    RNFirebaseAuth.this.promiseNoUser(promise, Boolean.valueOf(false));
                    return;
                }
                Exception exception = task.getException();
                Log.e(RNFirebaseAuth.TAG, "sendSignInLinkToEmail:onComplete:failure", exception);
                RNFirebaseAuth.this.promiseRejectAuthException(promise, exception);
            }
        });
    }

    @ReactMethod
    public void delete(String str, final Promise promise) {
        FirebaseUser currentUser = FirebaseAuth.getInstance(FirebaseApp.getInstance(str)).getCurrentUser();
        Log.d(TAG, "delete");
        if (currentUser != null) {
            currentUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                public void onComplete(@Nonnull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d(RNFirebaseAuth.TAG, "delete:onComplete:success");
                        RNFirebaseAuth.this.promiseNoUser(promise, Boolean.valueOf(false));
                        return;
                    }
                    Exception exception = task.getException();
                    Log.e(RNFirebaseAuth.TAG, "delete:onComplete:failure", exception);
                    RNFirebaseAuth.this.promiseRejectAuthException(promise, exception);
                }
            });
            return;
        }
        Log.e(TAG, "delete:failure:noCurrentUser");
        promiseNoUser(promise, Boolean.valueOf(true));
    }

    @ReactMethod
    public void reload(String str, final Promise promise) {
        final FirebaseAuth instance = FirebaseAuth.getInstance(FirebaseApp.getInstance(str));
        FirebaseUser currentUser = instance.getCurrentUser();
        Log.d(TAG, "reload");
        if (currentUser == null) {
            promiseNoUser(promise, Boolean.valueOf(false));
            Log.e(TAG, "reload:failure:noCurrentUser");
            return;
        }
        currentUser.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
            public void onComplete(@Nonnull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(RNFirebaseAuth.TAG, "reload:onComplete:success");
                    RNFirebaseAuth.this.promiseWithUser(instance.getCurrentUser(), promise);
                    return;
                }
                Exception exception = task.getException();
                Log.e(RNFirebaseAuth.TAG, "reload:onComplete:failure", exception);
                RNFirebaseAuth.this.promiseRejectAuthException(promise, exception);
            }
        });
    }

    @ReactMethod
    public void sendEmailVerification(String str, ReadableMap readableMap, final Promise promise) {
        final FirebaseAuth instance = FirebaseAuth.getInstance(FirebaseApp.getInstance(str));
        FirebaseUser currentUser = instance.getCurrentUser();
        Log.d(TAG, "sendEmailVerification");
        if (currentUser == null) {
            promiseNoUser(promise, Boolean.valueOf(false));
            Log.e(TAG, "sendEmailVerification:failure:noCurrentUser");
            return;
        }
        C136917 r1 = new OnCompleteListener<Void>() {
            public void onComplete(@Nonnull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(RNFirebaseAuth.TAG, "sendEmailVerification:onComplete:success");
                    RNFirebaseAuth.this.promiseWithUser(instance.getCurrentUser(), promise);
                    return;
                }
                Exception exception = task.getException();
                Log.e(RNFirebaseAuth.TAG, "sendEmailVerification:onComplete:failure", exception);
                RNFirebaseAuth.this.promiseRejectAuthException(promise, exception);
            }
        };
        if (readableMap == null) {
            currentUser.sendEmailVerification().addOnCompleteListener(r1);
        } else {
            currentUser.sendEmailVerification(buildActionCodeSettings(readableMap)).addOnCompleteListener(r1);
        }
    }

    @ReactMethod
    public void updateEmail(String str, String str2, final Promise promise) {
        final FirebaseAuth instance = FirebaseAuth.getInstance(FirebaseApp.getInstance(str));
        FirebaseUser currentUser = instance.getCurrentUser();
        Log.d(TAG, "updateEmail");
        if (currentUser == null) {
            promiseNoUser(promise, Boolean.valueOf(false));
            Log.e(TAG, "updateEmail:failure:noCurrentUser");
            return;
        }
        currentUser.updateEmail(str2).addOnCompleteListener(new OnCompleteListener<Void>() {
            public void onComplete(@Nonnull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(RNFirebaseAuth.TAG, "updateEmail:onComplete:success");
                    RNFirebaseAuth.this.promiseWithUser(instance.getCurrentUser(), promise);
                    return;
                }
                Exception exception = task.getException();
                Log.e(RNFirebaseAuth.TAG, "updateEmail:onComplete:failure", exception);
                RNFirebaseAuth.this.promiseRejectAuthException(promise, exception);
            }
        });
    }

    @ReactMethod
    public void updatePassword(String str, String str2, final Promise promise) {
        final FirebaseAuth instance = FirebaseAuth.getInstance(FirebaseApp.getInstance(str));
        FirebaseUser currentUser = instance.getCurrentUser();
        Log.d(TAG, "updatePassword");
        if (currentUser == null) {
            promiseNoUser(promise, Boolean.valueOf(false));
            Log.e(TAG, "updatePassword:failure:noCurrentUser");
            return;
        }
        currentUser.updatePassword(str2).addOnCompleteListener(new OnCompleteListener<Void>() {
            public void onComplete(@Nonnull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(RNFirebaseAuth.TAG, "updatePassword:onComplete:success");
                    RNFirebaseAuth.this.promiseWithUser(instance.getCurrentUser(), promise);
                    return;
                }
                Exception exception = task.getException();
                Log.e(RNFirebaseAuth.TAG, "updatePassword:onComplete:failure", exception);
                RNFirebaseAuth.this.promiseRejectAuthException(promise, exception);
            }
        });
    }

    @ReactMethod
    private void updatePhoneNumber(String str, String str2, String str3, String str4, final Promise promise) {
        final FirebaseAuth instance = FirebaseAuth.getInstance(FirebaseApp.getInstance(str));
        FirebaseUser currentUser = instance.getCurrentUser();
        if (!str2.equals("phone")) {
            promise.reject("auth/invalid-credential", "The supplied auth credential does not have a phone provider.");
        }
        PhoneAuthCredential phoneAuthCredential = getPhoneAuthCredential(str3, str4);
        if (phoneAuthCredential == null) {
            promise.reject("auth/invalid-credential", "The supplied auth credential is malformed, has expired or is not currently supported.");
        } else if (currentUser == null) {
            promiseNoUser(promise, Boolean.valueOf(false));
            Log.e(TAG, "updatePhoneNumber:failure:noCurrentUser");
        } else {
            Log.d(TAG, "updatePhoneNumber");
            currentUser.updatePhoneNumber(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
                public void onComplete(@Nonnull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d(RNFirebaseAuth.TAG, "updatePhoneNumber:onComplete:success");
                        RNFirebaseAuth.this.promiseWithUser(instance.getCurrentUser(), promise);
                        return;
                    }
                    Exception exception = task.getException();
                    Log.e(RNFirebaseAuth.TAG, "updatePhoneNumber:onComplete:failure", exception);
                    RNFirebaseAuth.this.promiseRejectAuthException(promise, exception);
                }
            });
        }
    }

    @ReactMethod
    public void updateProfile(String str, ReadableMap readableMap, final Promise promise) {
        Uri uri;
        final FirebaseAuth instance = FirebaseAuth.getInstance(FirebaseApp.getInstance(str));
        FirebaseUser currentUser = instance.getCurrentUser();
        Log.d(TAG, "updateProfile");
        if (currentUser == null) {
            promiseNoUser(promise, Boolean.valueOf(false));
            Log.e(TAG, "updateProfile:failure:noCurrentUser");
            return;
        }
        Builder builder = new Builder();
        if (readableMap.hasKey("displayName")) {
            builder.setDisplayName(readableMap.getString("displayName"));
        }
        if (readableMap.hasKey("photoURL")) {
            String string = readableMap.getString("photoURL");
            if (string == null) {
                uri = null;
            } else {
                uri = Uri.parse(string);
            }
            builder.setPhotoUri(uri);
        }
        currentUser.updateProfile(builder.build()).addOnCompleteListener(new OnCompleteListener<Void>() {
            public void onComplete(@Nonnull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(RNFirebaseAuth.TAG, "updateProfile:onComplete:success");
                    RNFirebaseAuth.this.promiseWithUser(instance.getCurrentUser(), promise);
                    return;
                }
                Exception exception = task.getException();
                Log.e(RNFirebaseAuth.TAG, "updateProfile:onComplete:failure", exception);
                RNFirebaseAuth.this.promiseRejectAuthException(promise, exception);
            }
        });
    }

    @ReactMethod
    private void signInWithCredential(String str, String str2, String str3, String str4, final Promise promise) {
        FirebaseAuth instance = FirebaseAuth.getInstance(FirebaseApp.getInstance(str));
        AuthCredential credentialForProvider = getCredentialForProvider(str2, str3, str4);
        if (credentialForProvider == null) {
            promise.reject("auth/invalid-credential", "The supplied auth credential is malformed, has expired or is not currently supported.");
            return;
        }
        Log.d(TAG, "signInWithCredential");
        instance.signInWithCredential(credentialForProvider).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            public void onComplete(@Nonnull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(RNFirebaseAuth.TAG, "signInWithCredential:onComplete:success");
                    RNFirebaseAuth.this.promiseWithAuthResult((AuthResult) task.getResult(), promise);
                    return;
                }
                Exception exception = task.getException();
                Log.e(RNFirebaseAuth.TAG, "signInWithCredential:onComplete:failure", exception);
                RNFirebaseAuth.this.promiseRejectAuthException(promise, exception);
            }
        });
    }

    @ReactMethod
    public void signInWithPhoneNumber(String str, String str2, boolean z, final Promise promise) {
        Log.d(TAG, "signInWithPhoneNumber");
        final FirebaseAuth instance = FirebaseAuth.getInstance(FirebaseApp.getInstance(str));
        Activity currentActivity = this.mReactContext.getCurrentActivity();
        if (!str2.equals(this.mLastPhoneNumber)) {
            this.mForceResendingToken = null;
            this.mLastPhoneNumber = str2;
        }
        this.mVerificationId = null;
        C137623 r7 = new OnVerificationStateChangedCallbacks() {
            /* access modifiers changed from: private */
            public boolean promiseResolved = false;

            public void onVerificationCompleted(final PhoneAuthCredential phoneAuthCredential) {
                instance.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    public void onComplete(@Nonnull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(RNFirebaseAuth.TAG, "signInWithPhoneNumber:autoVerified:signInWithCredential:onComplete:success");
                            if (!C137623.this.promiseResolved) {
                                WritableMap createMap = Arguments.createMap();
                                Parcel obtain = Parcel.obtain();
                                phoneAuthCredential.writeToParcel(obtain, 0);
                                obtain.setDataPosition(16);
                                String readString = obtain.readString();
                                RNFirebaseAuth.this.mVerificationId = readString;
                                obtain.recycle();
                                createMap.putString("verificationId", readString);
                                promise.resolve(createMap);
                                return;
                            }
                            return;
                        }
                        Exception exception = task.getException();
                        Log.e(RNFirebaseAuth.TAG, "signInWithPhoneNumber:autoVerified:signInWithCredential:onComplete:failure", exception);
                        if (!C137623.this.promiseResolved) {
                            RNFirebaseAuth.this.promiseRejectAuthException(promise, exception);
                        }
                    }
                });
            }

            public void onVerificationFailed(FirebaseException firebaseException) {
                Log.d(RNFirebaseAuth.TAG, "signInWithPhoneNumber:verification:failed");
                RNFirebaseAuth.this.promiseRejectAuthException(promise, firebaseException);
            }

            public void onCodeSent(String str, ForceResendingToken forceResendingToken) {
                RNFirebaseAuth.this.mVerificationId = str;
                RNFirebaseAuth.this.mForceResendingToken = forceResendingToken;
                WritableMap createMap = Arguments.createMap();
                createMap.putString("verificationId", str);
                promise.resolve(createMap);
                this.promiseResolved = true;
            }

            public void onCodeAutoRetrievalTimeOut(String str) {
                RNFirebaseAuth.super.onCodeAutoRetrievalTimeOut(str);
            }
        };
        if (currentActivity == null) {
            return;
        }
        if (!z || this.mForceResendingToken == null) {
            PhoneAuthProvider.getInstance(instance).verifyPhoneNumber(str2, 60, TimeUnit.SECONDS, currentActivity, r7);
            return;
        }
        PhoneAuthProvider.getInstance(instance).verifyPhoneNumber(str2, 60, TimeUnit.SECONDS, currentActivity, r7, this.mForceResendingToken);
    }

    @ReactMethod
    public void _confirmVerificationCode(String str, String str2, final Promise promise) {
        FirebaseAuth.getInstance(FirebaseApp.getInstance(str)).signInWithCredential(PhoneAuthProvider.getCredential(this.mVerificationId, str2)).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            public void onComplete(@Nonnull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(RNFirebaseAuth.TAG, "_confirmVerificationCode:signInWithCredential:onComplete:success");
                    RNFirebaseAuth.this.promiseWithUser(((AuthResult) task.getResult()).getUser(), promise);
                    return;
                }
                Exception exception = task.getException();
                Log.e(RNFirebaseAuth.TAG, "_confirmVerificationCode:signInWithCredential:onComplete:failure", exception);
                RNFirebaseAuth.this.promiseRejectAuthException(promise, exception);
            }
        });
    }

    @ReactMethod
    public void verifyPhoneNumber(final String str, String str2, final String str3, int i, boolean z) {
        FirebaseAuth instance = FirebaseAuth.getInstance(FirebaseApp.getInstance(str));
        Activity currentActivity = this.mReactContext.getCurrentActivity();
        String str4 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("verifyPhoneNumber:");
        sb.append(str2);
        Log.d(str4, sb.toString());
        if (!str2.equals(this.mLastPhoneNumber)) {
            this.mForceResendingToken = null;
            this.mLastPhoneNumber = str2;
        }
        this.mCredential = null;
        C137925 r8 = new OnVerificationStateChangedCallbacks() {
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                RNFirebaseAuth.this.mCredential = phoneAuthCredential;
                Log.d(RNFirebaseAuth.TAG, "verifyPhoneNumber:verification:onVerificationCompleted");
                WritableMap createMap = Arguments.createMap();
                Parcel obtain = Parcel.obtain();
                phoneAuthCredential.writeToParcel(obtain, 0);
                obtain.setDataPosition(16);
                String readString = obtain.readString();
                obtain.setDataPosition(obtain.dataPosition() + 8);
                createMap.putString("code", obtain.readString());
                createMap.putString("verificationId", readString);
                obtain.recycle();
                RNFirebaseAuth.this.sendPhoneStateEvent(str, str3, "onVerificationComplete", createMap);
            }

            public void onVerificationFailed(FirebaseException firebaseException) {
                Log.d(RNFirebaseAuth.TAG, "verifyPhoneNumber:verification:onVerificationFailed");
                WritableMap createMap = Arguments.createMap();
                createMap.putMap("error", RNFirebaseAuth.this.getJSError(firebaseException));
                RNFirebaseAuth.this.sendPhoneStateEvent(str, str3, "onVerificationFailed", createMap);
            }

            public void onCodeSent(String str, ForceResendingToken forceResendingToken) {
                Log.d(RNFirebaseAuth.TAG, "verifyPhoneNumber:verification:onCodeSent");
                RNFirebaseAuth.this.mForceResendingToken = forceResendingToken;
                WritableMap createMap = Arguments.createMap();
                createMap.putString("verificationId", str);
                createMap.putString("verificationId", str);
                RNFirebaseAuth.this.sendPhoneStateEvent(str, str3, "onCodeSent", createMap);
            }

            public void onCodeAutoRetrievalTimeOut(String str) {
                RNFirebaseAuth.super.onCodeAutoRetrievalTimeOut(str);
                Log.d(RNFirebaseAuth.TAG, "verifyPhoneNumber:verification:onCodeAutoRetrievalTimeOut");
                WritableMap createMap = Arguments.createMap();
                createMap.putString("verificationId", str);
                RNFirebaseAuth.this.sendPhoneStateEvent(str, str3, "onCodeAutoRetrievalTimeout", createMap);
            }
        };
        if (currentActivity == null) {
            return;
        }
        if (!z || this.mForceResendingToken == null) {
            PhoneAuthProvider.getInstance(instance).verifyPhoneNumber(str2, (long) i, TimeUnit.SECONDS, currentActivity, r8);
            return;
        }
        PhoneAuthProvider.getInstance(instance).verifyPhoneNumber(str2, (long) i, TimeUnit.SECONDS, currentActivity, r8, this.mForceResendingToken);
    }

    @ReactMethod
    public void confirmPasswordReset(String str, String str2, String str3, final Promise promise) {
        Log.d(TAG, "confirmPasswordReset");
        FirebaseAuth.getInstance(FirebaseApp.getInstance(str)).confirmPasswordReset(str2, str3).addOnCompleteListener(new OnCompleteListener<Void>() {
            public void onComplete(@Nonnull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(RNFirebaseAuth.TAG, "confirmPasswordReset:onComplete:success");
                    RNFirebaseAuth.this.promiseNoUser(promise, Boolean.valueOf(false));
                    return;
                }
                Exception exception = task.getException();
                Log.e(RNFirebaseAuth.TAG, "confirmPasswordReset:onComplete:failure", exception);
                RNFirebaseAuth.this.promiseRejectAuthException(promise, exception);
            }
        });
    }

    @ReactMethod
    public void applyActionCode(String str, String str2, final Promise promise) {
        Log.d(TAG, "applyActionCode");
        final FirebaseAuth instance = FirebaseAuth.getInstance(FirebaseApp.getInstance(str));
        instance.applyActionCode(str2).addOnCompleteListener(new OnCompleteListener<Void>() {
            public void onComplete(@Nonnull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(RNFirebaseAuth.TAG, "applyActionCode:onComplete:success");
                    RNFirebaseAuth.this.promiseWithUser(instance.getCurrentUser(), promise);
                    return;
                }
                Exception exception = task.getException();
                Log.e(RNFirebaseAuth.TAG, "applyActionCode:onComplete:failure", exception);
                RNFirebaseAuth.this.promiseRejectAuthException(promise, exception);
            }
        });
    }

    @ReactMethod
    public void checkActionCode(String str, String str2, final Promise promise) {
        Log.d(TAG, "checkActionCode");
        FirebaseAuth.getInstance(FirebaseApp.getInstance(str)).checkActionCode(str2).addOnCompleteListener(new OnCompleteListener<ActionCodeResult>() {
            public void onComplete(@Nonnull Task<ActionCodeResult> task) {
                if (task.isSuccessful()) {
                    Log.d(RNFirebaseAuth.TAG, "checkActionCode:onComplete:success");
                    ActionCodeResult actionCodeResult = (ActionCodeResult) task.getResult();
                    WritableMap createMap = Arguments.createMap();
                    WritableMap createMap2 = Arguments.createMap();
                    createMap2.putString("email", actionCodeResult.getData(0));
                    createMap2.putString("fromEmail", actionCodeResult.getData(1));
                    createMap.putMap(UriUtil.DATA_SCHEME, createMap2);
                    String str = "UNKNOWN";
                    switch (actionCodeResult.getOperation()) {
                        case 0:
                            str = "PASSWORD_RESET";
                            break;
                        case 1:
                            str = "VERIFY_EMAIL";
                            break;
                        case 2:
                            str = "RECOVER_EMAIL";
                            break;
                        case 3:
                            str = "ERROR";
                            break;
                        case 4:
                            str = "EMAIL_SIGNIN";
                            break;
                    }
                    createMap.putString("operation", str);
                    promise.resolve(createMap);
                    return;
                }
                Exception exception = task.getException();
                Log.e(RNFirebaseAuth.TAG, "checkActionCode:onComplete:failure", exception);
                RNFirebaseAuth.this.promiseRejectAuthException(promise, exception);
            }
        });
    }

    @ReactMethod
    private void linkWithCredential(String str, String str2, String str3, String str4, final Promise promise) {
        FirebaseAuth instance = FirebaseAuth.getInstance(FirebaseApp.getInstance(str));
        AuthCredential credentialForProvider = getCredentialForProvider(str2, str3, str4);
        if (credentialForProvider == null) {
            promise.reject("auth/invalid-credential", "The supplied auth credential is malformed, has expired or is not currently supported.");
            return;
        }
        FirebaseUser currentUser = instance.getCurrentUser();
        Log.d(TAG, "link");
        if (currentUser != null) {
            currentUser.linkWithCredential(credentialForProvider).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                public void onComplete(@Nonnull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d(RNFirebaseAuth.TAG, "link:onComplete:success");
                        RNFirebaseAuth.this.promiseWithAuthResult((AuthResult) task.getResult(), promise);
                        return;
                    }
                    Exception exception = task.getException();
                    Log.e(RNFirebaseAuth.TAG, "link:onComplete:failure", exception);
                    RNFirebaseAuth.this.promiseRejectAuthException(promise, exception);
                }
            });
        } else {
            promiseNoUser(promise, Boolean.valueOf(true));
        }
    }

    @ReactMethod
    public void unlink(String str, String str2, final Promise promise) {
        FirebaseUser currentUser = FirebaseAuth.getInstance(FirebaseApp.getInstance(str)).getCurrentUser();
        Log.d(TAG, "unlink");
        if (currentUser != null) {
            currentUser.unlink(str2).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                public void onComplete(@Nonnull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d(RNFirebaseAuth.TAG, "unlink:onComplete:success");
                        RNFirebaseAuth.this.promiseWithUser(((AuthResult) task.getResult()).getUser(), promise);
                        return;
                    }
                    Exception exception = task.getException();
                    Log.e(RNFirebaseAuth.TAG, "unlink:onComplete:failure", exception);
                    RNFirebaseAuth.this.promiseRejectAuthException(promise, exception);
                }
            });
        } else {
            promiseNoUser(promise, Boolean.valueOf(true));
        }
    }

    @ReactMethod
    private void reauthenticateWithCredential(String str, String str2, String str3, String str4, final Promise promise) {
        FirebaseAuth instance = FirebaseAuth.getInstance(FirebaseApp.getInstance(str));
        AuthCredential credentialForProvider = getCredentialForProvider(str2, str3, str4);
        if (credentialForProvider == null) {
            promise.reject("auth/invalid-credential", "The supplied auth credential is malformed, has expired or is not currently supported.");
            return;
        }
        FirebaseUser currentUser = instance.getCurrentUser();
        Log.d(TAG, "reauthenticate");
        if (currentUser != null) {
            currentUser.reauthenticateAndRetrieveData(credentialForProvider).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                public void onComplete(@Nonnull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d(RNFirebaseAuth.TAG, "reauthenticate:onComplete:success");
                        RNFirebaseAuth.this.promiseWithAuthResult((AuthResult) task.getResult(), promise);
                        return;
                    }
                    Exception exception = task.getException();
                    Log.e(RNFirebaseAuth.TAG, "reauthenticate:onComplete:failure", exception);
                    RNFirebaseAuth.this.promiseRejectAuthException(promise, exception);
                }
            });
        } else {
            promiseNoUser(promise, Boolean.valueOf(true));
        }
    }

    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.google.firebase.auth.AuthCredential getCredentialForProvider(java.lang.String r2, java.lang.String r3, java.lang.String r4) {
        /*
            r1 = this;
            int r0 = r2.hashCode()
            switch(r0) {
                case -1830313082: goto L_0x004e;
                case -1536293812: goto L_0x0044;
                case -364826023: goto L_0x003a;
                case 105516695: goto L_0x0030;
                case 106642798: goto L_0x0026;
                case 1216985755: goto L_0x001c;
                case 1985010934: goto L_0x0012;
                case 2120171958: goto L_0x0008;
                default: goto L_0x0007;
            }
        L_0x0007:
            goto L_0x0058
        L_0x0008:
            java.lang.String r0 = "emailLink"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0058
            r0 = 7
            goto L_0x0059
        L_0x0012:
            java.lang.String r0 = "github.com"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0058
            r0 = 3
            goto L_0x0059
        L_0x001c:
            java.lang.String r0 = "password"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0058
            r0 = 6
            goto L_0x0059
        L_0x0026:
            java.lang.String r0 = "phone"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0058
            r0 = 5
            goto L_0x0059
        L_0x0030:
            java.lang.String r0 = "oauth"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0058
            r0 = 4
            goto L_0x0059
        L_0x003a:
            java.lang.String r0 = "facebook.com"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0058
            r0 = 0
            goto L_0x0059
        L_0x0044:
            java.lang.String r0 = "google.com"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0058
            r0 = 1
            goto L_0x0059
        L_0x004e:
            java.lang.String r0 = "twitter.com"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0058
            r0 = 2
            goto L_0x0059
        L_0x0058:
            r0 = -1
        L_0x0059:
            switch(r0) {
                case 0: goto L_0x0081;
                case 1: goto L_0x007c;
                case 2: goto L_0x0077;
                case 3: goto L_0x0072;
                case 4: goto L_0x006d;
                case 5: goto L_0x0068;
                case 6: goto L_0x0063;
                case 7: goto L_0x005e;
                default: goto L_0x005c;
            }
        L_0x005c:
            r2 = 0
            return r2
        L_0x005e:
            com.google.firebase.auth.AuthCredential r2 = com.google.firebase.auth.EmailAuthProvider.getCredentialWithLink(r3, r4)
            return r2
        L_0x0063:
            com.google.firebase.auth.AuthCredential r2 = com.google.firebase.auth.EmailAuthProvider.getCredential(r3, r4)
            return r2
        L_0x0068:
            com.google.firebase.auth.PhoneAuthCredential r2 = r1.getPhoneAuthCredential(r3, r4)
            return r2
        L_0x006d:
            com.google.firebase.auth.AuthCredential r2 = com.google.firebase.auth.OAuthProvider.getCredential(r2, r3, r4)
            return r2
        L_0x0072:
            com.google.firebase.auth.AuthCredential r2 = com.google.firebase.auth.GithubAuthProvider.getCredential(r3)
            return r2
        L_0x0077:
            com.google.firebase.auth.AuthCredential r2 = com.google.firebase.auth.TwitterAuthProvider.getCredential(r3, r4)
            return r2
        L_0x007c:
            com.google.firebase.auth.AuthCredential r2 = com.google.firebase.auth.GoogleAuthProvider.getCredential(r3, r4)
            return r2
        L_0x0081:
            com.google.firebase.auth.AuthCredential r2 = com.google.firebase.auth.FacebookAuthProvider.getCredential(r3)
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: p007io.invertase.firebase.auth.RNFirebaseAuth.getCredentialForProvider(java.lang.String, java.lang.String, java.lang.String):com.google.firebase.auth.AuthCredential");
    }

    private PhoneAuthCredential getPhoneAuthCredential(String str, String str2) {
        if (str == null) {
            PhoneAuthCredential phoneAuthCredential = this.mCredential;
            if (phoneAuthCredential != null) {
                this.mCredential = null;
                return phoneAuthCredential;
            }
        }
        if (str != null) {
            return PhoneAuthProvider.getCredential(str, str2);
        }
        return null;
    }

    @ReactMethod
    public void getIdToken(String str, Boolean bool, final Promise promise) {
        Log.d(TAG, "getIdToken");
        FirebaseUser currentUser = FirebaseAuth.getInstance(FirebaseApp.getInstance(str)).getCurrentUser();
        if (currentUser == null) {
            promiseNoUser(promise, Boolean.valueOf(true));
        } else {
            currentUser.getIdToken(bool.booleanValue()).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                public void onComplete(@Nonnull Task<GetTokenResult> task) {
                    if (task.isSuccessful()) {
                        Log.d(RNFirebaseAuth.TAG, "getIdToken:onComplete:success");
                        promise.resolve(((GetTokenResult) task.getResult()).getToken());
                        return;
                    }
                    Exception exception = task.getException();
                    Log.e(RNFirebaseAuth.TAG, "getIdToken:onComplete:failure", exception);
                    RNFirebaseAuth.this.promiseRejectAuthException(promise, exception);
                }
            });
        }
    }

    @ReactMethod
    public void getIdTokenResult(String str, Boolean bool, final Promise promise) {
        Log.d(TAG, "getIdTokenResult");
        FirebaseUser currentUser = FirebaseAuth.getInstance(FirebaseApp.getInstance(str)).getCurrentUser();
        if (currentUser == null) {
            promiseNoUser(promise, Boolean.valueOf(true));
        } else {
            currentUser.getIdToken(bool.booleanValue()).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                public void onComplete(@Nonnull Task<GetTokenResult> task) {
                    if (task.isSuccessful()) {
                        Log.d(RNFirebaseAuth.TAG, "getIdTokenResult:onComplete:success");
                        GetTokenResult getTokenResult = (GetTokenResult) task.getResult();
                        WritableMap createMap = Arguments.createMap();
                        C1350Utils.mapPutValue("authTime", C1350Utils.timestampToUTC(getTokenResult.getAuthTimestamp()), createMap);
                        C1350Utils.mapPutValue("expirationTime", C1350Utils.timestampToUTC(getTokenResult.getExpirationTimestamp()), createMap);
                        C1350Utils.mapPutValue("issuedAtTime", C1350Utils.timestampToUTC(getTokenResult.getIssuedAtTimestamp()), createMap);
                        C1350Utils.mapPutValue("claims", getTokenResult.getClaims(), createMap);
                        C1350Utils.mapPutValue("signInProvider", getTokenResult.getSignInProvider(), createMap);
                        C1350Utils.mapPutValue("token", getTokenResult.getToken(), createMap);
                        promise.resolve(createMap);
                        return;
                    }
                    Exception exception = task.getException();
                    Log.e(RNFirebaseAuth.TAG, "getIdTokenResult:onComplete:failure", exception);
                    RNFirebaseAuth.this.promiseRejectAuthException(promise, exception);
                }
            });
        }
    }

    @ReactMethod
    public void fetchSignInMethodsForEmail(String str, String str2, final Promise promise) {
        FirebaseAuth instance = FirebaseAuth.getInstance(FirebaseApp.getInstance(str));
        Log.d(TAG, "fetchProvidersForEmail");
        instance.fetchSignInMethodsForEmail(str2).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            public void onComplete(@Nonnull Task<SignInMethodQueryResult> task) {
                if (task.isSuccessful()) {
                    Log.d(RNFirebaseAuth.TAG, "fetchProvidersForEmail:onComplete:success");
                    List<String> signInMethods = ((SignInMethodQueryResult) task.getResult()).getSignInMethods();
                    WritableArray createArray = Arguments.createArray();
                    if (signInMethods != null) {
                        for (String pushString : signInMethods) {
                            createArray.pushString(pushString);
                        }
                    }
                    promise.resolve(createArray);
                    return;
                }
                Exception exception = task.getException();
                Log.d(RNFirebaseAuth.TAG, "fetchProvidersForEmail:onComplete:failure", exception);
                RNFirebaseAuth.this.promiseRejectAuthException(promise, exception);
            }
        });
    }

    @ReactMethod
    public void setLanguageCode(String str, String str2) {
        FirebaseAuth.getInstance(FirebaseApp.getInstance(str)).setLanguageCode(str2);
    }

    @ReactMethod
    public void useDeviceLanguage(String str) {
        FirebaseAuth.getInstance(FirebaseApp.getInstance(str)).useAppLanguage();
    }

    @ReactMethod
    public void verifyPasswordResetCode(String str, String str2, final Promise promise) {
        Log.d(TAG, "verifyPasswordResetCode");
        FirebaseAuth.getInstance(FirebaseApp.getInstance(str)).verifyPasswordResetCode(str2).addOnCompleteListener(new OnCompleteListener<String>() {
            public void onComplete(@Nonnull Task<String> task) {
                if (task.isSuccessful()) {
                    Log.d(RNFirebaseAuth.TAG, "verifyPasswordResetCode:onComplete:success");
                    promise.resolve(task.getResult());
                    return;
                }
                Exception exception = task.getException();
                Log.e(RNFirebaseAuth.TAG, "verifyPasswordResetCode:onComplete:failure", exception);
                RNFirebaseAuth.this.promiseRejectAuthException(promise, exception);
            }
        });
    }

    /* access modifiers changed from: private */
    public void promiseNoUser(Promise promise, Boolean bool) {
        if (bool.booleanValue()) {
            promise.reject("auth/no-current-user", "No user currently signed in.");
        } else {
            promise.resolve(null);
        }
    }

    /* access modifiers changed from: private */
    public void promiseWithUser(FirebaseUser firebaseUser, Promise promise) {
        if (firebaseUser != null) {
            promise.resolve(firebaseUserToMap(firebaseUser));
        } else {
            promiseNoUser(promise, Boolean.valueOf(true));
        }
    }

    /* access modifiers changed from: private */
    public void promiseWithAuthResult(AuthResult authResult, Promise promise) {
        if (authResult == null || authResult.getUser() == null) {
            promiseNoUser(promise, Boolean.valueOf(true));
            return;
        }
        WritableMap createMap = Arguments.createMap();
        WritableMap firebaseUserToMap = firebaseUserToMap(authResult.getUser());
        if (authResult.getAdditionalUserInfo() != null) {
            WritableMap createMap2 = Arguments.createMap();
            createMap2.putBoolean("isNewUser", authResult.getAdditionalUserInfo().isNewUser());
            if (authResult.getAdditionalUserInfo().getProfile() != null) {
                C1350Utils.mapPutValue(Scopes.PROFILE, authResult.getAdditionalUserInfo().getProfile(), createMap2);
            }
            if (authResult.getAdditionalUserInfo().getProviderId() != null) {
                createMap2.putString("providerId", authResult.getAdditionalUserInfo().getProviderId());
            }
            if (authResult.getAdditionalUserInfo().getUsername() != null) {
                createMap2.putString("username", authResult.getAdditionalUserInfo().getUsername());
            }
            createMap.putMap("additionalUserInfo", createMap2);
        }
        createMap.putMap("user", firebaseUserToMap);
        promise.resolve(createMap);
    }

    /* access modifiers changed from: private */
    public void promiseRejectAuthException(Promise promise, Exception exc) {
        WritableMap jSError = getJSError(exc);
        promise.reject(jSError.getString("code"), jSError.getString("message"), (Throwable) exc);
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x00c4, code lost:
        if (r4.equals("CUSTOM_TOKEN_MISMATCH") != false) goto L_0x00fd;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.facebook.react.bridge.WritableMap getJSError(java.lang.Exception r8) {
        /*
            r7 = this;
            com.facebook.react.bridge.WritableMap r0 = com.facebook.react.bridge.Arguments.createMap()
            java.lang.String r1 = "UNKNOWN"
            java.lang.String r2 = r8.getMessage()
            java.lang.String r3 = "The email address is badly formatted."
            r4 = r8
            com.google.firebase.auth.FirebaseAuthException r4 = (com.google.firebase.auth.FirebaseAuthException) r4     // Catch:{ Exception -> 0x001f }
            java.lang.String r1 = r4.getErrorCode()     // Catch:{ Exception -> 0x001f }
            java.lang.String r5 = "nativeErrorCode"
            r0.putString(r5, r1)     // Catch:{ Exception -> 0x001f }
            java.lang.String r2 = r4.getMessage()     // Catch:{ Exception -> 0x001f }
            r4 = r1
            goto L_0x0136
        L_0x001f:
            java.lang.String r4 = "([A-Z]*_[A-Z]*)"
            java.util.regex.Pattern r4 = java.util.regex.Pattern.compile(r4)
            java.util.regex.Matcher r4 = r4.matcher(r2)
            boolean r5 = r4.find()
            if (r5 == 0) goto L_0x0135
            r1 = 1
            java.lang.String r4 = r4.group(r1)
            java.lang.String r4 = r4.trim()
            r5 = -1
            int r6 = r4.hashCode()
            switch(r6) {
                case -2127468245: goto L_0x00f1;
                case -1971163201: goto L_0x00e7;
                case -1112393964: goto L_0x00dd;
                case -1035666916: goto L_0x00d2;
                case -333672188: goto L_0x00c7;
                case -324930558: goto L_0x00be;
                case -311841705: goto L_0x00b3;
                case -75433118: goto L_0x00a8;
                case -49749054: goto L_0x009e;
                case -40686718: goto L_0x0093;
                case 583750925: goto L_0x0088;
                case 748182870: goto L_0x007d;
                case 864281573: goto L_0x0072;
                case 1072360691: goto L_0x0067;
                case 1388786705: goto L_0x005b;
                case 1433767024: goto L_0x004f;
                case 1563975629: goto L_0x0043;
                default: goto L_0x0041;
            }
        L_0x0041:
            goto L_0x00fc
        L_0x0043:
            java.lang.String r1 = "INVALID_USER_TOKEN"
            boolean r1 = r4.equals(r1)
            if (r1 == 0) goto L_0x00fc
            r1 = 13
            goto L_0x00fd
        L_0x004f:
            java.lang.String r1 = "USER_DISABLED"
            boolean r1 = r4.equals(r1)
            if (r1 == 0) goto L_0x00fc
            r1 = 10
            goto L_0x00fd
        L_0x005b:
            java.lang.String r1 = "INVALID_IDENTIFIER"
            boolean r1 = r4.equals(r1)
            if (r1 == 0) goto L_0x00fc
            r1 = 16
            goto L_0x00fd
        L_0x0067:
            java.lang.String r1 = "INVALID_CUSTOM_TOKEN"
            boolean r1 = r4.equals(r1)
            if (r1 == 0) goto L_0x00fc
            r1 = 0
            goto L_0x00fd
        L_0x0072:
            java.lang.String r1 = "ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL"
            boolean r1 = r4.equals(r1)
            if (r1 == 0) goto L_0x00fc
            r1 = 7
            goto L_0x00fd
        L_0x007d:
            java.lang.String r1 = "REQUIRES_RECENT_LOGIN"
            boolean r1 = r4.equals(r1)
            if (r1 == 0) goto L_0x00fc
            r1 = 6
            goto L_0x00fd
        L_0x0088:
            java.lang.String r1 = "WRONG_PASSWORD"
            boolean r1 = r4.equals(r1)
            if (r1 == 0) goto L_0x00fc
            r1 = 4
            goto L_0x00fd
        L_0x0093:
            java.lang.String r1 = "WEAK_PASSWORD"
            boolean r1 = r4.equals(r1)
            if (r1 == 0) goto L_0x00fc
            r1 = 14
            goto L_0x00fd
        L_0x009e:
            java.lang.String r1 = "USER_MISMATCH"
            boolean r1 = r4.equals(r1)
            if (r1 == 0) goto L_0x00fc
            r1 = 5
            goto L_0x00fd
        L_0x00a8:
            java.lang.String r1 = "USER_NOT_FOUND"
            boolean r1 = r4.equals(r1)
            if (r1 == 0) goto L_0x00fc
            r1 = 12
            goto L_0x00fd
        L_0x00b3:
            java.lang.String r1 = "EMAIL_ALREADY_IN_USE"
            boolean r1 = r4.equals(r1)
            if (r1 == 0) goto L_0x00fc
            r1 = 8
            goto L_0x00fd
        L_0x00be:
            java.lang.String r6 = "CUSTOM_TOKEN_MISMATCH"
            boolean r6 = r4.equals(r6)
            if (r6 == 0) goto L_0x00fc
            goto L_0x00fd
        L_0x00c7:
            java.lang.String r1 = "OPERATION_NOT_ALLOWED"
            boolean r1 = r4.equals(r1)
            if (r1 == 0) goto L_0x00fc
            r1 = 15
            goto L_0x00fd
        L_0x00d2:
            java.lang.String r1 = "CREDENTIAL_ALREADY_IN_USE"
            boolean r1 = r4.equals(r1)
            if (r1 == 0) goto L_0x00fc
            r1 = 9
            goto L_0x00fd
        L_0x00dd:
            java.lang.String r1 = "INVALID_EMAIL"
            boolean r1 = r4.equals(r1)
            if (r1 == 0) goto L_0x00fc
            r1 = 3
            goto L_0x00fd
        L_0x00e7:
            java.lang.String r1 = "INVALID_CREDENTIAL"
            boolean r1 = r4.equals(r1)
            if (r1 == 0) goto L_0x00fc
            r1 = 2
            goto L_0x00fd
        L_0x00f1:
            java.lang.String r1 = "USER_TOKEN_EXPIRED"
            boolean r1 = r4.equals(r1)
            if (r1 == 0) goto L_0x00fc
            r1 = 11
            goto L_0x00fd
        L_0x00fc:
            r1 = -1
        L_0x00fd:
            switch(r1) {
                case 0: goto L_0x0132;
                case 1: goto L_0x012f;
                case 2: goto L_0x012c;
                case 3: goto L_0x012a;
                case 4: goto L_0x0127;
                case 5: goto L_0x0124;
                case 6: goto L_0x0121;
                case 7: goto L_0x011e;
                case 8: goto L_0x011b;
                case 9: goto L_0x0118;
                case 10: goto L_0x0115;
                case 11: goto L_0x0112;
                case 12: goto L_0x010f;
                case 13: goto L_0x010c;
                case 14: goto L_0x0109;
                case 15: goto L_0x0106;
                case 16: goto L_0x0101;
                default: goto L_0x0100;
            }
        L_0x0100:
            goto L_0x0136
        L_0x0101:
            java.lang.String r1 = "INVALID_EMAIL"
            r4 = r1
            r2 = r3
            goto L_0x0136
        L_0x0106:
            java.lang.String r2 = "This operation is not allowed. You must enable this service in the console."
            goto L_0x0136
        L_0x0109:
            java.lang.String r2 = "The given password is invalid."
            goto L_0x0136
        L_0x010c:
            java.lang.String r2 = "The user's credential is no longer valid. The user must sign in again."
            goto L_0x0136
        L_0x010f:
            java.lang.String r2 = "There is no user record corresponding to this identifier. The user may have been deleted."
            goto L_0x0136
        L_0x0112:
            java.lang.String r2 = "The user's credential is no longer valid. The user must sign in again."
            goto L_0x0136
        L_0x0115:
            java.lang.String r2 = "The user account has been disabled by an administrator."
            goto L_0x0136
        L_0x0118:
            java.lang.String r2 = "This credential is already associated with a different user account."
            goto L_0x0136
        L_0x011b:
            java.lang.String r2 = "The email address is already in use by another account."
            goto L_0x0136
        L_0x011e:
            java.lang.String r2 = "An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address."
            goto L_0x0136
        L_0x0121:
            java.lang.String r2 = "This operation is sensitive and requires recent authentication. Log in again before retrying this request."
            goto L_0x0136
        L_0x0124:
            java.lang.String r2 = "The supplied credentials do not correspond to the previously signed in user."
            goto L_0x0136
        L_0x0127:
            java.lang.String r2 = "The password is invalid or the user does not have a password."
            goto L_0x0136
        L_0x012a:
            r2 = r3
            goto L_0x0136
        L_0x012c:
            java.lang.String r2 = "The supplied auth credential is malformed or has expired."
            goto L_0x0136
        L_0x012f:
            java.lang.String r2 = "The custom token corresponds to a different audience."
            goto L_0x0136
        L_0x0132:
            java.lang.String r2 = "The custom token format is incorrect. Please check the documentation."
            goto L_0x0136
        L_0x0135:
            r4 = r1
        L_0x0136:
            java.lang.String r1 = "UNKNOWN"
            boolean r1 = r4.equals(r1)
            if (r1 == 0) goto L_0x0145
            boolean r1 = r8 instanceof com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
            if (r1 == 0) goto L_0x0145
            java.lang.String r4 = "INVALID_EMAIL"
            r2 = r3
        L_0x0145:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r3 = "auth/"
            r1.append(r3)
            java.lang.String r3 = r4.toLowerCase()
            java.lang.String r4 = "error_"
            java.lang.String r5 = ""
            java.lang.String r3 = r3.replace(r4, r5)
            r4 = 95
            r5 = 45
            java.lang.String r3 = r3.replace(r4, r5)
            r1.append(r3)
            java.lang.String r1 = r1.toString()
            java.lang.String r3 = "code"
            r0.putString(r3, r1)
            java.lang.String r1 = "message"
            r0.putString(r1, r2)
            java.lang.String r1 = "nativeErrorMessage"
            java.lang.String r8 = r8.getMessage()
            r0.putString(r1, r8)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: p007io.invertase.firebase.auth.RNFirebaseAuth.getJSError(java.lang.Exception):com.facebook.react.bridge.WritableMap");
    }

    private WritableArray convertProviderData(List<? extends UserInfo> list, FirebaseUser firebaseUser) {
        WritableArray createArray = Arguments.createArray();
        for (UserInfo userInfo : list) {
            if (!"firebase".equals(userInfo.getProviderId())) {
                WritableMap createMap = Arguments.createMap();
                createMap.putString("providerId", userInfo.getProviderId());
                createMap.putString("uid", userInfo.getUid());
                createMap.putString("displayName", userInfo.getDisplayName());
                Uri photoUrl = userInfo.getPhotoUrl();
                if (photoUrl == null || "".equals(photoUrl.toString())) {
                    createMap.putNull("photoURL");
                } else {
                    createMap.putString("photoURL", photoUrl.toString());
                }
                String phoneNumber = userInfo.getPhoneNumber();
                if ("phone".equals(userInfo.getProviderId()) && (userInfo.getPhoneNumber() == null || "".equals(userInfo.getPhoneNumber()))) {
                    createMap.putString("phoneNumber", firebaseUser.getPhoneNumber());
                } else if (phoneNumber == null || "".equals(phoneNumber)) {
                    createMap.putNull("phoneNumber");
                } else {
                    createMap.putString("phoneNumber", phoneNumber);
                }
                if ("password".equals(userInfo.getProviderId()) && (userInfo.getEmail() == null || "".equals(userInfo.getEmail()))) {
                    createMap.putString("email", userInfo.getUid());
                } else if (userInfo.getEmail() == null || "".equals(userInfo.getEmail())) {
                    createMap.putNull("email");
                } else {
                    createMap.putString("email", userInfo.getEmail());
                }
                createArray.pushMap(createMap);
            }
        }
        return createArray;
    }

    /* access modifiers changed from: private */
    public WritableMap firebaseUserToMap(FirebaseUser firebaseUser) {
        WritableMap createMap = Arguments.createMap();
        String uid = firebaseUser.getUid();
        String email = firebaseUser.getEmail();
        Uri photoUrl = firebaseUser.getPhotoUrl();
        String displayName = firebaseUser.getDisplayName();
        String providerId = firebaseUser.getProviderId();
        Boolean valueOf = Boolean.valueOf(firebaseUser.isEmailVerified());
        String phoneNumber = firebaseUser.getPhoneNumber();
        createMap.putString("uid", uid);
        createMap.putString("providerId", providerId);
        createMap.putBoolean("emailVerified", valueOf.booleanValue());
        createMap.putBoolean("isAnonymous", firebaseUser.isAnonymous());
        if (email == null || "".equals(email)) {
            createMap.putNull("email");
        } else {
            createMap.putString("email", email);
        }
        if (displayName == null || "".equals(displayName)) {
            createMap.putNull("displayName");
        } else {
            createMap.putString("displayName", displayName);
        }
        if (photoUrl == null || "".equals(photoUrl.toString())) {
            createMap.putNull("photoURL");
        } else {
            createMap.putString("photoURL", photoUrl.toString());
        }
        if (phoneNumber == null || "".equals(phoneNumber)) {
            createMap.putNull("phoneNumber");
        } else {
            createMap.putString("phoneNumber", phoneNumber);
        }
        createMap.putArray("providerData", convertProviderData(firebaseUser.getProviderData(), firebaseUser));
        WritableMap createMap2 = Arguments.createMap();
        FirebaseUserMetadata metadata = firebaseUser.getMetadata();
        if (metadata != null) {
            createMap2.putDouble("creationTime", (double) metadata.getCreationTimestamp());
            createMap2.putDouble("lastSignInTime", (double) metadata.getLastSignInTimestamp());
        }
        createMap.putMap("metadata", createMap2);
        return createMap;
    }

    private ActionCodeSettings buildActionCodeSettings(ReadableMap readableMap) {
        ActionCodeSettings.Builder newBuilder = ActionCodeSettings.newBuilder();
        ReadableMap map = readableMap.getMap("android");
        ReadableMap map2 = readableMap.getMap("iOS");
        String string = readableMap.getString(ImagesContract.URL);
        if (map != null) {
            newBuilder = newBuilder.setAndroidPackageName(map.getString("packageName"), map.hasKey("installApp") && map.getBoolean("installApp"), map.hasKey("minimumVersion") ? map.getString("minimumVersion") : null);
        }
        if (readableMap.hasKey("handleCodeInApp")) {
            newBuilder = newBuilder.setHandleCodeInApp(readableMap.getBoolean("handleCodeInApp"));
        }
        if (map2 != null && map2.hasKey("bundleId")) {
            newBuilder = newBuilder.setIOSBundleId(map2.getString("bundleId"));
        }
        if (string != null) {
            newBuilder = newBuilder.setUrl(string);
        }
        return newBuilder.build();
    }

    /* access modifiers changed from: private */
    public void sendPhoneStateEvent(String str, String str2, String str3, WritableMap writableMap) {
        WritableMap createMap = Arguments.createMap();
        createMap.putString("appName", str);
        createMap.putString("requestKey", str2);
        createMap.putString("type", str3);
        createMap.putMap("state", writableMap);
        C1350Utils.sendEvent(this.mReactContext, "phone_auth_state_changed", createMap);
    }

    public Map<String, Object> getConstants() {
        HashMap hashMap = new HashMap();
        List<FirebaseApp> apps = FirebaseApp.getApps(getReactApplicationContext());
        HashMap hashMap2 = new HashMap();
        HashMap hashMap3 = new HashMap();
        for (FirebaseApp name : apps) {
            String name2 = name.getName();
            FirebaseAuth instance = FirebaseAuth.getInstance(FirebaseApp.getInstance(name2));
            FirebaseUser currentUser = instance.getCurrentUser();
            hashMap2.put(name2, instance.getLanguageCode());
            if (currentUser != null) {
                hashMap3.put(name2, firebaseUserToMap(currentUser));
            }
        }
        hashMap.put("APP_LANGUAGE", hashMap2);
        hashMap.put("APP_USER", hashMap3);
        return hashMap;
    }
}

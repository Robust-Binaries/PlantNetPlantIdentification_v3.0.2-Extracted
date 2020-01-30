package p004br.com.classapp.RNSensitiveInfo.view.Fragments;

import android.hardware.fingerprint.FingerprintManager;
import android.hardware.fingerprint.FingerprintManager.AuthenticationCallback;
import android.hardware.fingerprint.FingerprintManager.AuthenticationResult;
import android.hardware.fingerprint.FingerprintManager.CryptoObject;
import android.os.Build.VERSION;
import android.os.CancellationSignal;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import java.util.HashMap;
import p004br.com.classapp.RNSensitiveInfo.C0550R;

/* renamed from: br.com.classapp.RNSensitiveInfo.view.Fragments.FingerprintUiHelper */
public class FingerprintUiHelper extends AuthenticationCallback {
    private static final long ERROR_TIMEOUT_MILLIS = 1600;
    private static final long SUCCESS_DELAY_MILLIS = 1300;
    /* access modifiers changed from: private */
    public final Callback mCallback;
    private final TextView mCancelButton;
    private CancellationSignal mCancellationSignal;
    /* access modifiers changed from: private */
    public final TextView mErrorTextView;
    private final FingerprintManager mFingerprintManager;
    /* access modifiers changed from: private */
    public final ImageView mIcon;
    private Runnable mResetErrorTextRunnable = new Runnable() {
        public void run() {
            String str;
            FingerprintUiHelper.this.mErrorTextView.setTextColor(FingerprintUiHelper.this.mErrorTextView.getResources().getColor(C0550R.C0551color.hint_color, null));
            TextView access$100 = FingerprintUiHelper.this.mErrorTextView;
            if (FingerprintUiHelper.this.mStrings.containsKey("hint")) {
                str = FingerprintUiHelper.this.mStrings.get("hint").toString();
            } else {
                str = FingerprintUiHelper.this.mErrorTextView.getResources().getString(C0550R.string.fingerprint_hint);
            }
            access$100.setText(str);
            FingerprintUiHelper.this.mIcon.setImageResource(C0550R.C0552drawable.ic_fp_40px);
        }
    };
    private boolean mSelfCancelled;
    /* access modifiers changed from: private */
    public final HashMap mStrings;

    /* renamed from: br.com.classapp.RNSensitiveInfo.view.Fragments.FingerprintUiHelper$Callback */
    public interface Callback {
        void onAuthenticated(AuthenticationResult authenticationResult);

        void onError(String str, CharSequence charSequence);
    }

    FingerprintUiHelper(FingerprintManager fingerprintManager, ImageView imageView, TextView textView, Button button, HashMap hashMap, Callback callback) {
        this.mFingerprintManager = fingerprintManager;
        this.mIcon = imageView;
        this.mErrorTextView = textView;
        this.mCancelButton = button;
        this.mStrings = hashMap;
        this.mCallback = callback;
    }

    public boolean isFingerprintAuthAvailable() {
        boolean z = false;
        if (VERSION.SDK_INT < 23) {
            return false;
        }
        if (this.mFingerprintManager.isHardwareDetected() && this.mFingerprintManager.hasEnrolledFingerprints()) {
            z = true;
        }
        return z;
    }

    public void startListening(CryptoObject cryptoObject) {
        if (isFingerprintAuthAvailable()) {
            this.mCancellationSignal = new CancellationSignal();
            this.mSelfCancelled = false;
            this.mFingerprintManager.authenticate(cryptoObject, this.mCancellationSignal, 0, this, null);
            this.mIcon.setImageResource(C0550R.C0552drawable.ic_fp_40px);
        }
    }

    public void stopListening() {
        CancellationSignal cancellationSignal = this.mCancellationSignal;
        if (cancellationSignal != null) {
            this.mSelfCancelled = true;
            cancellationSignal.cancel();
            this.mCancellationSignal = null;
        }
    }

    public void onAuthenticationError(final int i, final CharSequence charSequence) {
        if (!this.mSelfCancelled) {
            this.mCancelButton.setEnabled(false);
            showError(charSequence);
            this.mIcon.postDelayed(new Runnable() {
                public void run() {
                    FingerprintUiHelper.this.mCallback.onError(String.valueOf(i), charSequence);
                }
            }, ERROR_TIMEOUT_MILLIS);
        }
    }

    public void onAuthenticationHelp(int i, CharSequence charSequence) {
        showError(charSequence);
    }

    public void onAuthenticationFailed() {
        String str;
        if (this.mStrings.containsKey("notRecognized")) {
            str = this.mStrings.get("notRecognized").toString();
        } else {
            str = this.mIcon.getResources().getString(C0550R.string.fingerprint_not_recognized);
        }
        showError(str);
    }

    public void onAuthenticationSucceeded(final AuthenticationResult authenticationResult) {
        String str;
        this.mErrorTextView.removeCallbacks(this.mResetErrorTextRunnable);
        this.mIcon.setImageResource(C0550R.C0552drawable.ic_fingerprint_success);
        TextView textView = this.mErrorTextView;
        textView.setTextColor(textView.getResources().getColor(C0550R.C0551color.success_color, null));
        TextView textView2 = this.mErrorTextView;
        if (this.mStrings.containsKey(Param.SUCCESS)) {
            str = this.mStrings.get(Param.SUCCESS).toString();
        } else {
            str = this.mErrorTextView.getResources().getString(C0550R.string.fingerprint_success);
        }
        textView2.setText(str);
        this.mCancelButton.setEnabled(false);
        this.mIcon.postDelayed(new Runnable() {
            public void run() {
                FingerprintUiHelper.this.mCallback.onAuthenticated(authenticationResult);
            }
        }, SUCCESS_DELAY_MILLIS);
    }

    private void showError(CharSequence charSequence) {
        this.mIcon.setImageResource(C0550R.C0552drawable.ic_fingerprint_error);
        this.mErrorTextView.setText(charSequence);
        TextView textView = this.mErrorTextView;
        textView.setTextColor(textView.getResources().getColor(C0550R.C0551color.warning_color, null));
        this.mErrorTextView.removeCallbacks(this.mResetErrorTextRunnable);
        this.mErrorTextView.postDelayed(this.mResetErrorTextRunnable, ERROR_TIMEOUT_MILLIS);
    }
}

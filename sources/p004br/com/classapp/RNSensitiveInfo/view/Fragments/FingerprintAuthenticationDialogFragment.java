package p004br.com.classapp.RNSensitiveInfo.view.Fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.hardware.fingerprint.FingerprintManager;
import android.hardware.fingerprint.FingerprintManager.AuthenticationResult;
import android.hardware.fingerprint.FingerprintManager.CryptoObject;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.HashMap;
import p004br.com.classapp.RNSensitiveInfo.C0550R;
import p004br.com.classapp.RNSensitiveInfo.utils.AppConstants;
import p004br.com.classapp.RNSensitiveInfo.view.Fragments.FingerprintUiHelper.Callback;

/* renamed from: br.com.classapp.RNSensitiveInfo.view.Fragments.FingerprintAuthenticationDialogFragment */
public class FingerprintAuthenticationDialogFragment extends DialogFragment implements Callback {
    private Activity mActivity;
    private Callback mCallback;
    private Button mCancelButton;
    private CryptoObject mCryptoObject;
    private boolean mDidDismiss = false;
    private boolean mDidInvokeCallback = false;
    private View mFingerprintContent;
    private FingerprintUiHelper mFingerprintUiHelper;
    private boolean mPendingDismiss = false;
    private boolean mSavedInstanceStateDone = false;
    private SharedPreferences mSharedPreferences;
    /* access modifiers changed from: private */
    public HashMap mStrings;

    public static FingerprintAuthenticationDialogFragment newInstance(HashMap hashMap) {
        FingerprintAuthenticationDialogFragment fingerprintAuthenticationDialogFragment = new FingerprintAuthenticationDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("strings", hashMap);
        fingerprintAuthenticationDialogFragment.setArguments(bundle);
        return fingerprintAuthenticationDialogFragment;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRetainInstance(true);
        setStyle(0, 16974393);
        this.mStrings = (HashMap) getArguments().getSerializable("strings");
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setTitle(this.mStrings.containsKey("header") ? this.mStrings.get("header").toString() : getString(C0550R.string.header));
        View inflate = layoutInflater.inflate(C0550R.layout.fingerprint_dialog_container, viewGroup, false);
        ((TextView) inflate.findViewById(C0550R.C0553id.fingerprint_description)).setText(this.mStrings.containsKey("description") ? this.mStrings.get("description").toString() : getString(C0550R.string.fingerprint_description));
        ((TextView) inflate.findViewById(C0550R.C0553id.fingerprint_status)).setText(this.mStrings.containsKey("hint") ? this.mStrings.get("hint").toString() : getString(C0550R.string.fingerprint_hint));
        this.mCancelButton = (Button) inflate.findViewById(C0550R.C0553id.cancel_button);
        this.mCancelButton.setText(this.mStrings.containsKey("cancel") ? this.mStrings.get("cancel").toString() : getString(C0550R.string.cancel));
        this.mCancelButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                FingerprintAuthenticationDialogFragment fingerprintAuthenticationDialogFragment = FingerprintAuthenticationDialogFragment.this;
                fingerprintAuthenticationDialogFragment.callbackOnError(AppConstants.E_AUTHENTICATION_CANCELLED, fingerprintAuthenticationDialogFragment.mStrings.containsKey("cancelled") ? FingerprintAuthenticationDialogFragment.this.mStrings.get("cancelled").toString() : "Authentication was cancelled");
                FingerprintAuthenticationDialogFragment.this.dismissDialog();
            }
        });
        this.mFingerprintContent = inflate.findViewById(C0550R.C0553id.fingerprint_container);
        this.mFingerprintContent.setVisibility(0);
        FingerprintUiHelper fingerprintUiHelper = new FingerprintUiHelper((FingerprintManager) this.mActivity.getSystemService(FingerprintManager.class), (ImageView) inflate.findViewById(C0550R.C0553id.fingerprint_icon), (TextView) inflate.findViewById(C0550R.C0553id.fingerprint_status), this.mCancelButton, this.mStrings, this);
        this.mFingerprintUiHelper = fingerprintUiHelper;
        return inflate;
    }

    public void onResume() {
        super.onResume();
        this.mSavedInstanceStateDone = false;
        if (this.mPendingDismiss) {
            dismissDialog();
        } else {
            this.mFingerprintUiHelper.startListening(this.mCryptoObject);
        }
    }

    public void onPause() {
        super.onPause();
        this.mFingerprintUiHelper.stopListening();
    }

    public void onCancel(DialogInterface dialogInterface) {
        super.onCancel(dialogInterface);
        callbackOnError(AppConstants.E_AUTHENTICATION_CANCELLED, this.mStrings.containsKey("cancelled") ? this.mStrings.get("cancelled").toString() : "Authentication was cancelled");
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        this.mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.mActivity = getActivity();
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        this.mSavedInstanceStateDone = true;
    }

    /* access modifiers changed from: private */
    public void dismissDialog() {
        if (!this.mDidDismiss) {
            if (!this.mSavedInstanceStateDone) {
                this.mDidDismiss = true;
                dismiss();
            } else {
                this.mPendingDismiss = true;
            }
        }
    }

    public void setCryptoObject(CryptoObject cryptoObject) {
        this.mCryptoObject = cryptoObject;
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    public void callbackOnAuthenticated(AuthenticationResult authenticationResult) {
        if (!this.mDidInvokeCallback) {
            this.mDidInvokeCallback = true;
            this.mCallback.onAuthenticated(authenticationResult);
        }
    }

    public void callbackOnError(String str, CharSequence charSequence) {
        if (!this.mDidInvokeCallback) {
            this.mDidInvokeCallback = true;
            this.mCallback.onError(str, charSequence);
        }
    }

    public void onAuthenticated(AuthenticationResult authenticationResult) {
        callbackOnAuthenticated(authenticationResult);
        dismissDialog();
    }

    public void onError(String str, CharSequence charSequence) {
        callbackOnError(str, charSequence);
        dismissDialog();
    }
}

package android.support.p003v7.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.p003v7.widget.FitWindowsViewGroup.OnFitSystemWindowsListener;
import android.util.AttributeSet;
import android.widget.LinearLayout;

@RestrictTo({Scope.LIBRARY_GROUP})
/* renamed from: android.support.v7.widget.FitWindowsLinearLayout */
public class FitWindowsLinearLayout extends LinearLayout implements FitWindowsViewGroup {
    private OnFitSystemWindowsListener mListener;

    public FitWindowsLinearLayout(Context context) {
        super(context);
    }

    public FitWindowsLinearLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void setOnFitSystemWindowsListener(OnFitSystemWindowsListener onFitSystemWindowsListener) {
        this.mListener = onFitSystemWindowsListener;
    }

    /* access modifiers changed from: protected */
    public boolean fitSystemWindows(Rect rect) {
        OnFitSystemWindowsListener onFitSystemWindowsListener = this.mListener;
        if (onFitSystemWindowsListener != null) {
            onFitSystemWindowsListener.onFitSystemWindows(rect);
        }
        return super.fitSystemWindows(rect);
    }
}

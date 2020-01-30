package com.facebook.react.uimanager;

import android.support.annotation.Nullable;
import android.view.View;
import com.facebook.react.bridge.JSApplicationCausedNativeException;

public class IllegalViewOperationException extends JSApplicationCausedNativeException {
    @Nullable
    private View mView;

    public IllegalViewOperationException(String str) {
        super(str);
    }

    public IllegalViewOperationException(String str, @Nullable View view, Throwable th) {
        super(str, th);
        this.mView = view;
    }

    @Nullable
    public View getView() {
        return this.mView;
    }
}

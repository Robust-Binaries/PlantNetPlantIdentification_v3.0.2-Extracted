package com.facebook.react.views.checkbox;

import android.content.Context;
import android.support.p003v7.widget.TintContextWrapper;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.uimanager.annotations.ReactProp;

public class ReactCheckBoxManager extends SimpleViewManager<ReactCheckBox> {
    private static final OnCheckedChangeListener ON_CHECKED_CHANGE_LISTENER = new OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            ((UIManagerModule) getReactContext(compoundButton).getNativeModule(UIManagerModule.class)).getEventDispatcher().dispatchEvent(new ReactCheckBoxEvent(compoundButton.getId(), z));
        }

        private ReactContext getReactContext(CompoundButton compoundButton) {
            Context context = compoundButton.getContext();
            if (context instanceof TintContextWrapper) {
                return (ReactContext) ((TintContextWrapper) context).getBaseContext();
            }
            return (ReactContext) compoundButton.getContext();
        }
    };
    private static final String REACT_CLASS = "AndroidCheckBox";

    public String getName() {
        return REACT_CLASS;
    }

    /* access modifiers changed from: protected */
    public void addEventEmitters(ThemedReactContext themedReactContext, ReactCheckBox reactCheckBox) {
        reactCheckBox.setOnCheckedChangeListener(ON_CHECKED_CHANGE_LISTENER);
    }

    /* access modifiers changed from: protected */
    public ReactCheckBox createViewInstance(ThemedReactContext themedReactContext) {
        return new ReactCheckBox(themedReactContext);
    }

    @ReactProp(defaultBoolean = true, name = "enabled")
    public void setEnabled(ReactCheckBox reactCheckBox, boolean z) {
        reactCheckBox.setEnabled(z);
    }

    @ReactProp(name = "on")
    public void setOn(ReactCheckBox reactCheckBox, boolean z) {
        reactCheckBox.setOnCheckedChangeListener(null);
        reactCheckBox.setOn(z);
        reactCheckBox.setOnCheckedChangeListener(ON_CHECKED_CHANGE_LISTENER);
    }
}

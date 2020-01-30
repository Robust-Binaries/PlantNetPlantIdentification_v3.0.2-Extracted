package com.facebook.react.modules.core;

import com.facebook.common.logging.FLog;
import com.facebook.react.bridge.BaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.common.JavascriptException;
import com.facebook.react.common.ReactConstants;
import com.facebook.react.devsupport.interfaces.DevSupportManager;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.util.JSStackTrace;

@ReactModule(name = "ExceptionsManager")
public class ExceptionsManagerModule extends BaseJavaModule {
    public static final String NAME = "ExceptionsManager";
    private final DevSupportManager mDevSupportManager;

    public String getName() {
        return NAME;
    }

    public ExceptionsManagerModule(DevSupportManager devSupportManager) {
        this.mDevSupportManager = devSupportManager;
    }

    @ReactMethod
    public void reportFatalException(String str, ReadableArray readableArray, int i) {
        showOrThrowError(str, readableArray, i);
    }

    @ReactMethod
    public void reportSoftException(String str, ReadableArray readableArray, int i) {
        if (this.mDevSupportManager.getDevSupportEnabled()) {
            this.mDevSupportManager.showNewJSError(str, readableArray, i);
        } else {
            FLog.m48e(ReactConstants.TAG, JSStackTrace.format(str, readableArray));
        }
    }

    private void showOrThrowError(String str, ReadableArray readableArray, int i) {
        if (this.mDevSupportManager.getDevSupportEnabled()) {
            this.mDevSupportManager.showNewJSError(str, readableArray, i);
            return;
        }
        throw new JavascriptException(JSStackTrace.format(str, readableArray));
    }

    @ReactMethod
    public void updateExceptionMessage(String str, ReadableArray readableArray, int i) {
        if (this.mDevSupportManager.getDevSupportEnabled()) {
            this.mDevSupportManager.updateJSError(str, readableArray, i);
        }
    }

    @ReactMethod
    public void dismissRedbox() {
        if (this.mDevSupportManager.getDevSupportEnabled()) {
            this.mDevSupportManager.hideRedboxDialog();
        }
    }
}

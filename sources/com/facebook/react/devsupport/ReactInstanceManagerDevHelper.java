package com.facebook.react.devsupport;

import android.app.Activity;
import com.facebook.react.bridge.JavaJSExecutor.Factory;
import com.facebook.react.bridge.NativeDeltaClient;
import javax.annotation.Nullable;

public interface ReactInstanceManagerDevHelper {
    @Nullable
    Activity getCurrentActivity();

    void onJSBundleLoadedFromServer(@Nullable NativeDeltaClient nativeDeltaClient);

    void onReloadWithJSDebugger(Factory factory);

    void toggleElementInspector();
}

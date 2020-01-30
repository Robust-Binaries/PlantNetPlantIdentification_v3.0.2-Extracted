package com.facebook.react.turbomodule.core;

import com.facebook.jni.HybridData;
import com.facebook.proguard.annotations.DoNotStrip;
import com.facebook.react.bridge.JSIModule;
import com.facebook.react.bridge.JavaScriptContextHolder;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.queue.MessageQueueThread;
import com.facebook.react.turbomodule.core.interfaces.TurboModule;
import com.facebook.soloader.SoLoader;

public class TurboModuleManager implements JSIModule {
    @DoNotStrip
    private final HybridData mHybridData;
    private final ModuleProvider mModuleProvider;
    private final ReactApplicationContext mReactApplicationContext;

    public interface ModuleProvider {
        TurboModule getModule(String str, ReactApplicationContext reactApplicationContext);
    }

    /* access modifiers changed from: protected */
    public native HybridData initHybrid(long j, MessageQueueThread messageQueueThread);

    public void initialize() {
    }

    /* access modifiers changed from: protected */
    public native void installJSIBindings();

    public void onCatalystInstanceDestroy() {
    }

    static {
        SoLoader.loadLibrary("turbomodulejsijni");
    }

    public TurboModuleManager(ReactApplicationContext reactApplicationContext, JavaScriptContextHolder javaScriptContextHolder, ModuleProvider moduleProvider) {
        this.mReactApplicationContext = reactApplicationContext;
        this.mHybridData = initHybrid(javaScriptContextHolder.get(), this.mReactApplicationContext.getCatalystInstance().getReactQueueConfiguration().getJSQueueThread());
        this.mModuleProvider = moduleProvider;
    }

    /* access modifiers changed from: protected */
    @DoNotStrip
    public TurboModule getJavaModule(String str) {
        return this.mModuleProvider.getModule(str, this.mReactApplicationContext);
    }

    public void installBindings() {
        installJSIBindings();
    }

    /* access modifiers changed from: protected */
    public ReactApplicationContext getReactApplicationContext() {
        return this.mReactApplicationContext;
    }
}

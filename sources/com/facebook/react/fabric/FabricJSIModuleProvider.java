package com.facebook.react.fabric;

import com.facebook.react.bridge.JSIModuleProvider;
import com.facebook.react.bridge.JavaScriptContextHolder;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.UIManager;
import com.facebook.react.fabric.jsi.Binding;
import com.facebook.react.fabric.jsi.ComponentFactoryDelegate;
import com.facebook.react.fabric.jsi.ComponentRegistry;
import com.facebook.react.fabric.jsi.EventBeatManager;
import com.facebook.react.fabric.jsi.EventEmitterWrapper;
import com.facebook.react.fabric.jsi.FabricSoLoader;
import com.facebook.react.fabric.mounting.ContextBasedViewPool;
import com.facebook.react.fabric.mounting.LayoutMetricsConversions;
import com.facebook.react.fabric.mounting.MountingManager;
import com.facebook.react.fabric.mounting.ViewPool;
import com.facebook.react.fabric.mounting.mountitems.BatchMountItem;
import com.facebook.react.fabric.mounting.mountitems.CreateMountItem;
import com.facebook.react.fabric.mounting.mountitems.DeleteMountItem;
import com.facebook.react.fabric.mounting.mountitems.DispatchCommandMountItem;
import com.facebook.react.fabric.mounting.mountitems.InsertMountItem;
import com.facebook.react.fabric.mounting.mountitems.MountItem;
import com.facebook.react.fabric.mounting.mountitems.PreAllocateViewMountItem;
import com.facebook.react.fabric.mounting.mountitems.RemoveMountItem;
import com.facebook.react.fabric.mounting.mountitems.UpdateEventEmitterMountItem;
import com.facebook.react.fabric.mounting.mountitems.UpdateLayoutMountItem;
import com.facebook.react.fabric.mounting.mountitems.UpdateLocalDataMountItem;
import com.facebook.react.fabric.mounting.mountitems.UpdatePropsMountItem;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.systrace.Systrace;

public class FabricJSIModuleProvider implements JSIModuleProvider<UIManager> {
    private final ComponentFactoryDelegate mComponentFactoryDelegate;
    private final ReactNativeConfig mConfig;
    private final JavaScriptContextHolder mJSContext;
    private final ReactApplicationContext mReactApplicationContext;

    public FabricJSIModuleProvider(ReactApplicationContext reactApplicationContext, JavaScriptContextHolder javaScriptContextHolder, ComponentFactoryDelegate componentFactoryDelegate, ReactNativeConfig reactNativeConfig) {
        this.mReactApplicationContext = reactApplicationContext;
        this.mJSContext = javaScriptContextHolder;
        this.mComponentFactoryDelegate = componentFactoryDelegate;
        this.mConfig = reactNativeConfig;
    }

    public UIManager get() {
        EventBeatManager eventBeatManager = new EventBeatManager(this.mJSContext, this.mReactApplicationContext);
        FabricUIManager createUIManager = createUIManager(eventBeatManager);
        Systrace.beginSection(0, "FabricJSIModuleProvider.registerBinding");
        Binding binding = new Binding();
        loadClasses();
        FabricUIManager fabricUIManager = createUIManager;
        binding.register(this.mJSContext, fabricUIManager, eventBeatManager, this.mReactApplicationContext.getCatalystInstance().getReactQueueConfiguration().getJSQueueThread(), this.mComponentFactoryDelegate, this.mConfig);
        Systrace.endSection(0);
        return createUIManager;
    }

    private FabricUIManager createUIManager(EventBeatManager eventBeatManager) {
        Systrace.beginSection(0, "FabricJSIModuleProvider.createUIManager");
        UIManagerModule uIManagerModule = (UIManagerModule) this.mReactApplicationContext.getNativeModule(UIManagerModule.class);
        FabricUIManager fabricUIManager = new FabricUIManager(this.mReactApplicationContext, uIManagerModule.getViewManagerRegistry_DO_NOT_USE(), uIManagerModule.getEventDispatcher(), eventBeatManager);
        Systrace.endSection(0);
        return fabricUIManager;
    }

    private static void loadClasses() {
        FabricEventEmitter.class.getClass();
        FabricUIManager.class.getClass();
        GuardedFrameCallback.class.getClass();
        BatchMountItem.class.getClass();
        CreateMountItem.class.getClass();
        DeleteMountItem.class.getClass();
        DispatchCommandMountItem.class.getClass();
        InsertMountItem.class.getClass();
        MountItem.class.getClass();
        RemoveMountItem.class.getClass();
        UpdateEventEmitterMountItem.class.getClass();
        UpdateLayoutMountItem.class.getClass();
        UpdateLocalDataMountItem.class.getClass();
        UpdatePropsMountItem.class.getClass();
        ContextBasedViewPool.class.getClass();
        LayoutMetricsConversions.class.getClass();
        MountingManager.class.getClass();
        ViewPool.class.getClass();
        Binding.class.getClass();
        ComponentFactoryDelegate.class.getClass();
        ComponentRegistry.class.getClass();
        EventBeatManager.class.getClass();
        EventEmitterWrapper.class.getClass();
        FabricSoLoader.class.getClass();
        PreAllocateViewMountItem.class.getClass();
    }
}

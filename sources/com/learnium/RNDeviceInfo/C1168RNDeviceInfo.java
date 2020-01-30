package com.learnium.RNDeviceInfo;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* renamed from: com.learnium.RNDeviceInfo.RNDeviceInfo */
public class C1168RNDeviceInfo implements ReactPackage {
    private boolean mLoadConstantsAsynchronously;

    public C1168RNDeviceInfo() {
        this(false);
    }

    public C1168RNDeviceInfo(boolean z) {
        this.mLoadConstantsAsynchronously = z;
    }

    public List<NativeModule> createNativeModules(ReactApplicationContext reactApplicationContext) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new RNDeviceModule(reactApplicationContext, this.mLoadConstantsAsynchronously));
        return arrayList;
    }

    public List<Class<? extends JavaScriptModule>> createJSModules() {
        return Collections.emptyList();
    }

    public List<ViewManager> createViewManagers(ReactApplicationContext reactApplicationContext) {
        return Collections.emptyList();
    }
}

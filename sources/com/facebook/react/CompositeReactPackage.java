package com.facebook.react;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import javax.annotation.Nullable;

public class CompositeReactPackage implements ViewManagerOnDemandReactPackage, ReactPackage {
    private final List<ReactPackage> mChildReactPackages = new ArrayList();

    public CompositeReactPackage(ReactPackage reactPackage, ReactPackage reactPackage2, ReactPackage... reactPackageArr) {
        this.mChildReactPackages.add(reactPackage);
        this.mChildReactPackages.add(reactPackage2);
        Collections.addAll(this.mChildReactPackages, reactPackageArr);
    }

    public List<NativeModule> createNativeModules(ReactApplicationContext reactApplicationContext) {
        HashMap hashMap = new HashMap();
        for (ReactPackage createNativeModules : this.mChildReactPackages) {
            for (NativeModule nativeModule : createNativeModules.createNativeModules(reactApplicationContext)) {
                hashMap.put(nativeModule.getName(), nativeModule);
            }
        }
        return new ArrayList(hashMap.values());
    }

    public List<ViewManager> createViewManagers(ReactApplicationContext reactApplicationContext) {
        HashMap hashMap = new HashMap();
        for (ReactPackage createViewManagers : this.mChildReactPackages) {
            for (ViewManager viewManager : createViewManagers.createViewManagers(reactApplicationContext)) {
                hashMap.put(viewManager.getName(), viewManager);
            }
        }
        return new ArrayList(hashMap.values());
    }

    public List<String> getViewManagerNames(ReactApplicationContext reactApplicationContext) {
        HashSet hashSet = new HashSet();
        for (ReactPackage reactPackage : this.mChildReactPackages) {
            if (reactPackage instanceof ViewManagerOnDemandReactPackage) {
                List viewManagerNames = ((ViewManagerOnDemandReactPackage) reactPackage).getViewManagerNames(reactApplicationContext);
                if (viewManagerNames != null) {
                    hashSet.addAll(viewManagerNames);
                }
            }
        }
        return new ArrayList(hashSet);
    }

    @Nullable
    public ViewManager createViewManager(ReactApplicationContext reactApplicationContext, String str) {
        List<ReactPackage> list = this.mChildReactPackages;
        ListIterator listIterator = list.listIterator(list.size());
        while (listIterator.hasPrevious()) {
            ReactPackage reactPackage = (ReactPackage) listIterator.previous();
            if (reactPackage instanceof ViewManagerOnDemandReactPackage) {
                ViewManager createViewManager = ((ViewManagerOnDemandReactPackage) reactPackage).createViewManager(reactApplicationContext, str);
                if (createViewManager != null) {
                    return createViewManager;
                }
            }
        }
        return null;
    }
}

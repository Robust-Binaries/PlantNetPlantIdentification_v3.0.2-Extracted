package com.facebook.react.bridge;

import com.facebook.infer.annotation.Assertions;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSIModuleRegistry {
    private final Map<Class, JSIModuleHolder> mModules = new HashMap();

    public <T extends JSIModule> T getModule(Class<T> cls) {
        JSIModuleHolder jSIModuleHolder = (JSIModuleHolder) this.mModules.get(cls);
        if (jSIModuleHolder != null) {
            return (JSIModule) Assertions.assertNotNull(jSIModuleHolder.getJSIModule());
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Unable to find JSIModule for class ");
        sb.append(cls);
        throw new IllegalArgumentException(sb.toString());
    }

    public void registerModules(List<JSIModuleSpec> list) {
        for (JSIModuleSpec jSIModuleSpec : list) {
            this.mModules.put(jSIModuleSpec.getJSIModuleClass(), new JSIModuleHolder(jSIModuleSpec));
        }
    }

    public void notifyJSInstanceDestroy() {
        for (JSIModuleHolder notifyJSInstanceDestroy : this.mModules.values()) {
            notifyJSInstanceDestroy.notifyJSInstanceDestroy();
        }
    }
}

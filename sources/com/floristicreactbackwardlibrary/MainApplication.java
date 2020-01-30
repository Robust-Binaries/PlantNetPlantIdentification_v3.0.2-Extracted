package com.floristicreactbackwardlibrary;

import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
import java.util.Arrays;
import java.util.List;

public class MainApplication {
    /* access modifiers changed from: protected */
    public List<ReactPackage> getPackages() {
        return Arrays.asList(new ReactPackage[]{new MainReactPackage(), new RNBackwardPlantnetPackage()});
    }
}

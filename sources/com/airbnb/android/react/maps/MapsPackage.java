package com.airbnb.android.react.maps;

import android.app.Activity;
import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MapsPackage implements ReactPackage {
    public MapsPackage(Activity activity) {
    }

    public MapsPackage() {
    }

    public List<NativeModule> createNativeModules(ReactApplicationContext reactApplicationContext) {
        return Arrays.asList(new NativeModule[]{new AirMapModule(reactApplicationContext)});
    }

    public List<Class<? extends JavaScriptModule>> createJSModules() {
        return Collections.emptyList();
    }

    public List<ViewManager> createViewManagers(ReactApplicationContext reactApplicationContext) {
        return Arrays.asList(new ViewManager[]{new AirMapCalloutManager(), new AirMapMarkerManager(), new AirMapPolylineManager(reactApplicationContext), new AirMapPolygonManager(reactApplicationContext), new AirMapCircleManager(reactApplicationContext), new AirMapManager(reactApplicationContext), new AirMapLiteManager(reactApplicationContext), new AirMapUrlTileManager(reactApplicationContext), new AirMapLocalTileManager(reactApplicationContext), new AirMapOverlayManager(reactApplicationContext)});
    }
}

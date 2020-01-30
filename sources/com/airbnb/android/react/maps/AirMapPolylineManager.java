package com.airbnb.android.react.maps;

import android.os.Build.VERSION;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import java.util.Map;
import javax.annotation.Nullable;

public class AirMapPolylineManager extends ViewGroupManager<AirMapPolyline> {
    private final DisplayMetrics metrics;

    public String getName() {
        return "AIRMapPolyline";
    }

    public AirMapPolylineManager(ReactApplicationContext reactApplicationContext) {
        if (VERSION.SDK_INT >= 17) {
            this.metrics = new DisplayMetrics();
            ((WindowManager) reactApplicationContext.getSystemService("window")).getDefaultDisplay().getRealMetrics(this.metrics);
            return;
        }
        this.metrics = reactApplicationContext.getResources().getDisplayMetrics();
    }

    public AirMapPolyline createViewInstance(ThemedReactContext themedReactContext) {
        return new AirMapPolyline(themedReactContext);
    }

    @ReactProp(name = "coordinates")
    public void setCoordinate(AirMapPolyline airMapPolyline, ReadableArray readableArray) {
        airMapPolyline.setCoordinates(readableArray);
    }

    @ReactProp(defaultFloat = 1.0f, name = "strokeWidth")
    public void setStrokeWidth(AirMapPolyline airMapPolyline, float f) {
        airMapPolyline.setWidth(this.metrics.density * f);
    }

    @ReactProp(customType = "Color", defaultInt = -65536, name = "strokeColor")
    public void setStrokeColor(AirMapPolyline airMapPolyline, int i) {
        airMapPolyline.setColor(i);
    }

    @ReactProp(defaultBoolean = false, name = "tappable")
    public void setTappable(AirMapPolyline airMapPolyline, boolean z) {
        airMapPolyline.setTappable(z);
    }

    @ReactProp(defaultBoolean = false, name = "geodesic")
    public void setGeodesic(AirMapPolyline airMapPolyline, boolean z) {
        airMapPolyline.setGeodesic(z);
    }

    @ReactProp(defaultFloat = 1.0f, name = "zIndex")
    public void setZIndex(AirMapPolyline airMapPolyline, float f) {
        airMapPolyline.setZIndex(f);
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0036  */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x003c  */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x0042  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0048  */
    @com.facebook.react.uimanager.annotations.ReactProp(name = "lineCap")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setlineCap(com.airbnb.android.react.maps.AirMapPolyline r3, java.lang.String r4) {
        /*
            r2 = this;
            int r0 = r4.hashCode()
            r1 = -894674659(0xffffffffcaac591d, float:-5647502.5)
            if (r0 == r1) goto L_0x0028
            r1 = 3035667(0x2e5213, float:4.253876E-39)
            if (r0 == r1) goto L_0x001e
            r1 = 108704142(0x67ab18e, float:4.715022E-35)
            if (r0 == r1) goto L_0x0014
            goto L_0x0032
        L_0x0014:
            java.lang.String r0 = "round"
            boolean r4 = r4.equals(r0)
            if (r4 == 0) goto L_0x0032
            r4 = 1
            goto L_0x0033
        L_0x001e:
            java.lang.String r0 = "butt"
            boolean r4 = r4.equals(r0)
            if (r4 == 0) goto L_0x0032
            r4 = 0
            goto L_0x0033
        L_0x0028:
            java.lang.String r0 = "square"
            boolean r4 = r4.equals(r0)
            if (r4 == 0) goto L_0x0032
            r4 = 2
            goto L_0x0033
        L_0x0032:
            r4 = -1
        L_0x0033:
            switch(r4) {
                case 0: goto L_0x0048;
                case 1: goto L_0x0042;
                case 2: goto L_0x003c;
                default: goto L_0x0036;
            }
        L_0x0036:
            com.google.android.gms.maps.model.RoundCap r4 = new com.google.android.gms.maps.model.RoundCap
            r4.<init>()
            goto L_0x004d
        L_0x003c:
            com.google.android.gms.maps.model.SquareCap r4 = new com.google.android.gms.maps.model.SquareCap
            r4.<init>()
            goto L_0x004d
        L_0x0042:
            com.google.android.gms.maps.model.RoundCap r4 = new com.google.android.gms.maps.model.RoundCap
            r4.<init>()
            goto L_0x004d
        L_0x0048:
            com.google.android.gms.maps.model.ButtCap r4 = new com.google.android.gms.maps.model.ButtCap
            r4.<init>()
        L_0x004d:
            r3.setLineCap(r4)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.airbnb.android.react.maps.AirMapPolylineManager.setlineCap(com.airbnb.android.react.maps.AirMapPolyline, java.lang.String):void");
    }

    @ReactProp(name = "lineDashPattern")
    public void setLineDashPattern(AirMapPolyline airMapPolyline, ReadableArray readableArray) {
        airMapPolyline.setLineDashPattern(readableArray);
    }

    @Nullable
    public Map getExportedCustomDirectEventTypeConstants() {
        return MapBuilder.m121of("onPress", MapBuilder.m121of("registrationName", "onPress"));
    }
}

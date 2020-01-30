package com.airbnb.android.react.maps;

import android.graphics.Color;
import android.view.View;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.LayoutShadowNode;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;

public class AirMapMarkerManager extends ViewGroupManager<AirMapMarker> {
    private static final int ANIMATE_MARKER_TO_COORDINATE = 3;
    private static final int HIDE_INFO_WINDOW = 2;
    private static final int REDRAW = 4;
    private static final int SHOW_INFO_WINDOW = 1;

    public String getName() {
        return "AIRMapMarker";
    }

    public AirMapMarker createViewInstance(ThemedReactContext themedReactContext) {
        return new AirMapMarker(themedReactContext);
    }

    @ReactProp(name = "coordinate")
    public void setCoordinate(AirMapMarker airMapMarker, ReadableMap readableMap) {
        airMapMarker.setCoordinate(readableMap);
    }

    @ReactProp(name = "title")
    public void setTitle(AirMapMarker airMapMarker, String str) {
        airMapMarker.setTitle(str);
    }

    @ReactProp(name = "identifier")
    public void setIdentifier(AirMapMarker airMapMarker, String str) {
        airMapMarker.setIdentifier(str);
    }

    @ReactProp(name = "description")
    public void setDescription(AirMapMarker airMapMarker, String str) {
        airMapMarker.setSnippet(str);
    }

    @ReactProp(name = "anchor")
    public void setAnchor(AirMapMarker airMapMarker, ReadableMap readableMap) {
        airMapMarker.setAnchor((readableMap == null || !readableMap.hasKey("x")) ? 0.5d : readableMap.getDouble("x"), (readableMap == null || !readableMap.hasKey("y")) ? 1.0d : readableMap.getDouble("y"));
    }

    @ReactProp(name = "calloutAnchor")
    public void setCalloutAnchor(AirMapMarker airMapMarker, ReadableMap readableMap) {
        airMapMarker.setCalloutAnchor((readableMap == null || !readableMap.hasKey("x")) ? 0.5d : readableMap.getDouble("x"), (readableMap == null || !readableMap.hasKey("y")) ? 0.0d : readableMap.getDouble("y"));
    }

    @ReactProp(name = "image")
    public void setImage(AirMapMarker airMapMarker, @Nullable String str) {
        airMapMarker.setImage(str);
    }

    @ReactProp(name = "icon")
    public void setIcon(AirMapMarker airMapMarker, @Nullable String str) {
        airMapMarker.setImage(str);
    }

    @ReactProp(customType = "Color", defaultInt = -65536, name = "pinColor")
    public void setPinColor(AirMapMarker airMapMarker, int i) {
        float[] fArr = new float[3];
        Color.colorToHSV(i, fArr);
        airMapMarker.setMarkerHue(fArr[0]);
    }

    @ReactProp(defaultFloat = 0.0f, name = "rotation")
    public void setMarkerRotation(AirMapMarker airMapMarker, float f) {
        airMapMarker.setRotation(f);
    }

    @ReactProp(defaultBoolean = false, name = "flat")
    public void setFlat(AirMapMarker airMapMarker, boolean z) {
        airMapMarker.setFlat(z);
    }

    @ReactProp(defaultBoolean = false, name = "draggable")
    public void setDraggable(AirMapMarker airMapMarker, boolean z) {
        airMapMarker.setDraggable(z);
    }

    @ReactProp(defaultFloat = 0.0f, name = "zIndex")
    public void setZIndex(AirMapMarker airMapMarker, float f) {
        super.setZIndex(airMapMarker, f);
        airMapMarker.setZIndex(Math.round(f));
    }

    @ReactProp(defaultFloat = 1.0f, name = "opacity")
    public void setOpacity(AirMapMarker airMapMarker, float f) {
        super.setOpacity(airMapMarker, f);
        airMapMarker.setOpacity(f);
    }

    @ReactProp(defaultBoolean = true, name = "tracksViewChanges")
    public void setTracksViewChanges(AirMapMarker airMapMarker, boolean z) {
        airMapMarker.setTracksViewChanges(z);
    }

    public void addView(AirMapMarker airMapMarker, View view, int i) {
        if (view instanceof AirMapCallout) {
            airMapMarker.setCalloutView((AirMapCallout) view);
            return;
        }
        super.addView(airMapMarker, view, i);
        airMapMarker.update(true);
    }

    public void removeViewAt(AirMapMarker airMapMarker, int i) {
        super.removeViewAt(airMapMarker, i);
        airMapMarker.update(true);
    }

    @Nullable
    public Map<String, Integer> getCommandsMap() {
        return MapBuilder.m124of("showCallout", Integer.valueOf(1), "hideCallout", Integer.valueOf(2), "animateMarkerToCoordinate", Integer.valueOf(3), "redraw", Integer.valueOf(4));
    }

    public void receiveCommand(AirMapMarker airMapMarker, int i, @Nullable ReadableArray readableArray) {
        switch (i) {
            case 1:
                ((Marker) airMapMarker.getFeature()).showInfoWindow();
                return;
            case 2:
                ((Marker) airMapMarker.getFeature()).hideInfoWindow();
                return;
            case 3:
                ReadableMap map = readableArray.getMap(0);
                airMapMarker.animateToCoodinate(new LatLng(Double.valueOf(map.getDouble("latitude")).doubleValue(), Double.valueOf(map.getDouble("longitude")).doubleValue()), Integer.valueOf(readableArray.getInt(1)));
                return;
            case 4:
                airMapMarker.updateMarkerIcon();
                return;
            default:
                return;
        }
    }

    @Nullable
    public Map getExportedCustomDirectEventTypeConstants() {
        Map of = MapBuilder.m125of("onPress", MapBuilder.m121of("registrationName", "onPress"), "onCalloutPress", MapBuilder.m121of("registrationName", "onCalloutPress"), "onDragStart", MapBuilder.m121of("registrationName", "onDragStart"), "onDrag", MapBuilder.m121of("registrationName", "onDrag"), "onDragEnd", MapBuilder.m121of("registrationName", "onDragEnd"));
        of.putAll(MapBuilder.m123of("onDragStart", MapBuilder.m121of("registrationName", "onDragStart"), "onDrag", MapBuilder.m121of("registrationName", "onDrag"), "onDragEnd", MapBuilder.m121of("registrationName", "onDragEnd")));
        return of;
    }

    public LayoutShadowNode createShadowNodeInstance() {
        return new SizeReportingShadowNode();
    }

    public void updateExtraData(AirMapMarker airMapMarker, Object obj) {
        HashMap hashMap = (HashMap) obj;
        airMapMarker.update((int) ((Float) hashMap.get("width")).floatValue(), (int) ((Float) hashMap.get("height")).floatValue());
    }
}

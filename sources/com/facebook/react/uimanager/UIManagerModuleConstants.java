package com.facebook.react.uimanager;

import android.widget.ImageView.ScaleType;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.events.TouchEventType;
import com.facebook.react.views.picker.events.PickerItemSelectEvent;
import java.util.HashMap;
import java.util.Map;

class UIManagerModuleConstants {
    public static final String ACTION_DISMISSED = "dismissed";
    public static final String ACTION_ITEM_SELECTED = "itemSelected";

    UIManagerModuleConstants() {
    }

    static Map getBubblingEventTypeConstants() {
        return MapBuilder.builder().put("topChange", MapBuilder.m121of("phasedRegistrationNames", MapBuilder.m122of("bubbled", "onChange", "captured", "onChangeCapture"))).put(PickerItemSelectEvent.EVENT_NAME, MapBuilder.m121of("phasedRegistrationNames", MapBuilder.m122of("bubbled", "onSelect", "captured", "onSelectCapture"))).put(TouchEventType.getJSEventName(TouchEventType.START), MapBuilder.m121of("phasedRegistrationNames", MapBuilder.m122of("bubbled", "onTouchStart", "captured", "onTouchStartCapture"))).put(TouchEventType.getJSEventName(TouchEventType.MOVE), MapBuilder.m121of("phasedRegistrationNames", MapBuilder.m122of("bubbled", "onTouchMove", "captured", "onTouchMoveCapture"))).put(TouchEventType.getJSEventName(TouchEventType.END), MapBuilder.m121of("phasedRegistrationNames", MapBuilder.m122of("bubbled", "onTouchEnd", "captured", "onTouchEndCapture"))).put(TouchEventType.getJSEventName(TouchEventType.CANCEL), MapBuilder.m121of("phasedRegistrationNames", MapBuilder.m122of("bubbled", "onTouchCancel", "captured", "onTouchCancelCapture"))).build();
    }

    static Map getDirectEventTypeConstants() {
        return MapBuilder.builder().put("topContentSizeChange", MapBuilder.m121of("registrationName", "onContentSizeChange")).put("topLayout", MapBuilder.m121of("registrationName", ViewProps.ON_LAYOUT)).put("topLoadingError", MapBuilder.m121of("registrationName", "onLoadingError")).put("topLoadingFinish", MapBuilder.m121of("registrationName", "onLoadingFinish")).put("topLoadingStart", MapBuilder.m121of("registrationName", "onLoadingStart")).put("topSelectionChange", MapBuilder.m121of("registrationName", "onSelectionChange")).put("topMessage", MapBuilder.m121of("registrationName", "onMessage")).put("topScrollBeginDrag", MapBuilder.m121of("registrationName", "onScrollBeginDrag")).put("topScrollEndDrag", MapBuilder.m121of("registrationName", "onScrollEndDrag")).put("topScroll", MapBuilder.m121of("registrationName", "onScroll")).put("topMomentumScrollBegin", MapBuilder.m121of("registrationName", "onMomentumScrollBegin")).put("topMomentumScrollEnd", MapBuilder.m121of("registrationName", "onMomentumScrollEnd")).build();
    }

    public static Map<String, Object> getConstants() {
        HashMap newHashMap = MapBuilder.newHashMap();
        newHashMap.put("UIView", MapBuilder.m121of("ContentMode", MapBuilder.m123of("ScaleAspectFit", Integer.valueOf(ScaleType.FIT_CENTER.ordinal()), "ScaleAspectFill", Integer.valueOf(ScaleType.CENTER_CROP.ordinal()), "ScaleAspectCenter", Integer.valueOf(ScaleType.CENTER_INSIDE.ordinal()))));
        newHashMap.put("StyleConstants", MapBuilder.m121of("PointerEventsValues", MapBuilder.m124of(ViewProps.NONE, Integer.valueOf(PointerEvents.NONE.ordinal()), "boxNone", Integer.valueOf(PointerEvents.BOX_NONE.ordinal()), "boxOnly", Integer.valueOf(PointerEvents.BOX_ONLY.ordinal()), "unspecified", Integer.valueOf(PointerEvents.AUTO.ordinal()))));
        newHashMap.put("PopupMenu", MapBuilder.m122of(ACTION_DISMISSED, ACTION_DISMISSED, ACTION_ITEM_SELECTED, ACTION_ITEM_SELECTED));
        newHashMap.put("AccessibilityEventTypes", MapBuilder.m123of("typeWindowStateChanged", Integer.valueOf(32), "typeViewFocused", Integer.valueOf(8), "typeViewClicked", Integer.valueOf(1)));
        return newHashMap;
    }
}

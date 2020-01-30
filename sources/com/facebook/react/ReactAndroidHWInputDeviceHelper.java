package com.facebook.react;

import android.view.KeyEvent;
import android.view.View;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ViewProps;
import java.util.Map;

public class ReactAndroidHWInputDeviceHelper {
    private static final Map<Integer, String> KEY_EVENTS_ACTIONS = MapBuilder.builder().put(Integer.valueOf(23), "select").put(Integer.valueOf(66), "select").put(Integer.valueOf(62), "select").put(Integer.valueOf(85), "playPause").put(Integer.valueOf(89), "rewind").put(Integer.valueOf(90), "fastForward").put(Integer.valueOf(19), "up").put(Integer.valueOf(22), ViewProps.RIGHT).put(Integer.valueOf(20), "down").put(Integer.valueOf(21), ViewProps.LEFT).build();
    private int mLastFocusedViewId = -1;
    private final ReactRootView mReactRootView;

    ReactAndroidHWInputDeviceHelper(ReactRootView reactRootView) {
        this.mReactRootView = reactRootView;
    }

    public void handleKeyEvent(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        if (keyEvent.getAction() == 1 && KEY_EVENTS_ACTIONS.containsKey(Integer.valueOf(keyCode))) {
            dispatchEvent((String) KEY_EVENTS_ACTIONS.get(Integer.valueOf(keyCode)), this.mLastFocusedViewId);
        }
    }

    public void onFocusChanged(View view) {
        if (this.mLastFocusedViewId != view.getId()) {
            int i = this.mLastFocusedViewId;
            if (i != -1) {
                dispatchEvent("blur", i);
            }
            this.mLastFocusedViewId = view.getId();
            dispatchEvent("focus", view.getId());
        }
    }

    public void clearFocus() {
        int i = this.mLastFocusedViewId;
        if (i != -1) {
            dispatchEvent("blur", i);
        }
        this.mLastFocusedViewId = -1;
    }

    private void dispatchEvent(String str, int i) {
        WritableNativeMap writableNativeMap = new WritableNativeMap();
        writableNativeMap.putString("eventType", str);
        if (i != -1) {
            writableNativeMap.putInt("tag", i);
        }
        this.mReactRootView.sendEvent("onHWKeyEvent", writableNativeMap);
    }
}

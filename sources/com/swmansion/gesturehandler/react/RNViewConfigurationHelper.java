package com.swmansion.gesturehandler.react;

import android.os.Build.VERSION;
import android.view.View;
import android.view.ViewGroup;
import com.facebook.react.uimanager.PointerEvents;
import com.facebook.react.uimanager.ReactPointerEventsView;
import com.facebook.react.uimanager.ViewProps;
import com.facebook.react.views.view.ReactViewGroup;
import com.swmansion.gesturehandler.PointerEventsConfig;
import com.swmansion.gesturehandler.ViewConfigurationHelper;

public class RNViewConfigurationHelper implements ViewConfigurationHelper {
    public PointerEventsConfig getPointerEventsConfigForView(View view) {
        PointerEvents pointerEvents = view instanceof ReactPointerEventsView ? ((ReactPointerEventsView) view).getPointerEvents() : PointerEvents.AUTO;
        if (!view.isEnabled()) {
            if (pointerEvents == PointerEvents.AUTO) {
                return PointerEventsConfig.BOX_NONE;
            }
            if (pointerEvents == PointerEvents.BOX_ONLY) {
                return PointerEventsConfig.NONE;
            }
        }
        switch (pointerEvents) {
            case BOX_ONLY:
                return PointerEventsConfig.BOX_ONLY;
            case BOX_NONE:
                return PointerEventsConfig.BOX_NONE;
            case NONE:
                return PointerEventsConfig.NONE;
            default:
                return PointerEventsConfig.AUTO;
        }
    }

    public View getChildInDrawingOrderAtIndex(ViewGroup viewGroup, int i) {
        if (viewGroup instanceof ReactViewGroup) {
            return viewGroup.getChildAt(((ReactViewGroup) viewGroup).getZIndexMappedChildIndex(i));
        }
        return viewGroup.getChildAt(i);
    }

    public boolean isViewClippingChildren(ViewGroup viewGroup) {
        if (VERSION.SDK_INT < 18 || viewGroup.getClipChildren()) {
            return true;
        }
        if (!(viewGroup instanceof ReactViewGroup)) {
            return false;
        }
        return ViewProps.HIDDEN.equals(((ReactViewGroup) viewGroup).getOverflow());
    }
}

package com.facebook.react.uimanager.events;

public enum TouchEventType {
    START,
    END,
    MOVE,
    CANCEL;

    public static String getJSEventName(TouchEventType touchEventType) {
        switch (touchEventType) {
            case START:
                return "topTouchStart";
            case END:
                return TouchesHelper.TOP_TOUCH_END_KEY;
            case MOVE:
                return "topTouchMove";
            case CANCEL:
                return TouchesHelper.TOP_TOUCH_CANCEL_KEY;
            default:
                StringBuilder sb = new StringBuilder();
                sb.append("Unexpected type ");
                sb.append(touchEventType);
                throw new IllegalArgumentException(sb.toString());
        }
    }
}

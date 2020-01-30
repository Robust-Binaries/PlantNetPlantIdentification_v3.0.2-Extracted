package com.facebook.react.views.scroll;

public enum ScrollEventType {
    BEGIN_DRAG,
    END_DRAG,
    SCROLL,
    MOMENTUM_BEGIN,
    MOMENTUM_END;

    public static String getJSEventName(ScrollEventType scrollEventType) {
        switch (scrollEventType) {
            case BEGIN_DRAG:
                return "topScrollBeginDrag";
            case END_DRAG:
                return "topScrollEndDrag";
            case SCROLL:
                return "topScroll";
            case MOMENTUM_BEGIN:
                return "topMomentumScrollBegin";
            case MOMENTUM_END:
                return "topMomentumScrollEnd";
            default:
                StringBuilder sb = new StringBuilder();
                sb.append("Unsupported ScrollEventType: ");
                sb.append(scrollEventType);
                throw new IllegalArgumentException(sb.toString());
        }
    }
}

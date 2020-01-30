package com.facebook.react.views.webview.events;

import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.RCTEventEmitter;

public class TopLoadingStartEvent extends Event<TopLoadingStartEvent> {
    public static final String EVENT_NAME = "topLoadingStart";
    private WritableMap mEventData;

    public boolean canCoalesce() {
        return false;
    }

    public short getCoalescingKey() {
        return 0;
    }

    public String getEventName() {
        return "topLoadingStart";
    }

    public TopLoadingStartEvent(int i, WritableMap writableMap) {
        super(i);
        this.mEventData = writableMap;
    }

    public void dispatch(RCTEventEmitter rCTEventEmitter) {
        rCTEventEmitter.receiveEvent(getViewTag(), getEventName(), this.mEventData);
    }
}

package com.facebook.react.views.webview.events;

import com.facebook.common.util.UriUtil;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.RCTEventEmitter;

public class TopMessageEvent extends Event<TopMessageEvent> {
    public static final String EVENT_NAME = "topMessage";
    private final String mData;

    public boolean canCoalesce() {
        return false;
    }

    public short getCoalescingKey() {
        return 0;
    }

    public String getEventName() {
        return "topMessage";
    }

    public TopMessageEvent(int i, String str) {
        super(i);
        this.mData = str;
    }

    public void dispatch(RCTEventEmitter rCTEventEmitter) {
        WritableMap createMap = Arguments.createMap();
        createMap.putString(UriUtil.DATA_SCHEME, this.mData);
        rCTEventEmitter.receiveEvent(getViewTag(), "topMessage", createMap);
    }
}

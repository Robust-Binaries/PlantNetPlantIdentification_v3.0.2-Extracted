package com.reactnativecommunity.webview.events;

import com.facebook.common.util.UriUtil;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(mo20294bv = {1, 0, 3}, mo20295d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\n\n\u0002\b\u0003\u0018\u0000 \u00102\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001\u0010B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\b\u0010\u0007\u001a\u00020\bH\u0016J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0016J\b\u0010\r\u001a\u00020\u000eH\u0016J\b\u0010\u000f\u001a\u00020\u0005H\u0016R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0011"}, mo20296d2 = {"Lcom/reactnativecommunity/webview/events/TopMessageEvent;", "Lcom/facebook/react/uimanager/events/Event;", "viewId", "", "mData", "", "(ILjava/lang/String;)V", "canCoalesce", "", "dispatch", "", "rctEventEmitter", "Lcom/facebook/react/uimanager/events/RCTEventEmitter;", "getCoalescingKey", "", "getEventName", "Companion", "react-native-webview_release"}, mo20297k = 1, mo20298mv = {1, 1, 13})
/* compiled from: TopMessageEvent.kt */
public final class TopMessageEvent extends Event<TopMessageEvent> {
    public static final Companion Companion = new Companion(null);
    @NotNull
    public static final String EVENT_NAME = "topMessage";
    private final String mData;

    @Metadata(mo20294bv = {1, 0, 3}, mo20295d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0005"}, mo20296d2 = {"Lcom/reactnativecommunity/webview/events/TopMessageEvent$Companion;", "", "()V", "EVENT_NAME", "", "react-native-webview_release"}, mo20297k = 1, mo20298mv = {1, 1, 13})
    /* compiled from: TopMessageEvent.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public boolean canCoalesce() {
        return false;
    }

    public short getCoalescingKey() {
        return 0;
    }

    @NotNull
    public String getEventName() {
        return "topMessage";
    }

    public TopMessageEvent(int i, @NotNull String str) {
        Intrinsics.checkParameterIsNotNull(str, "mData");
        super(i);
        this.mData = str;
    }

    public void dispatch(@NotNull RCTEventEmitter rCTEventEmitter) {
        Intrinsics.checkParameterIsNotNull(rCTEventEmitter, "rctEventEmitter");
        WritableMap createMap = Arguments.createMap();
        createMap.putString(UriUtil.DATA_SCHEME, this.mData);
        rCTEventEmitter.receiveEvent(getViewTag(), "topMessage", createMap);
    }
}

package p007io.amarcruz.photoview;

import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* renamed from: io.amarcruz.photoview.ImageEvent */
public class ImageEvent extends Event<ImageEvent> {
    public static final int ON_ERROR = 1;
    public static final int ON_LOAD = 2;
    public static final int ON_LOAD_END = 3;
    public static final int ON_LOAD_START = 4;
    public static final int ON_SCALE = 7;
    public static final int ON_TAP = 5;
    public static final int ON_VIEW_TAP = 6;
    private final int mEventType;
    private WritableMap mMap = null;

    @Retention(RetentionPolicy.SOURCE)
    /* renamed from: io.amarcruz.photoview.ImageEvent$ImageEventType */
    @interface ImageEventType {
    }

    ImageEvent(int i, int i2) {
        super(i);
        this.mEventType = i2;
    }

    public static String eventNameForType(int i) {
        switch (i) {
            case 1:
                return "photoViewError";
            case 2:
                return "photoViewLoad";
            case 3:
                return "photoViewLoadEnd";
            case 4:
                return "photoViewLoadStart";
            case 5:
                return "photoViewTap";
            case 6:
                return "photoViewViewTap";
            case 7:
                return "photoViewScale";
            default:
                StringBuilder sb = new StringBuilder();
                sb.append("Invalid image event: ");
                sb.append(Integer.toString(i));
                throw new IllegalStateException(sb.toString());
        }
    }

    public String getEventName() {
        return eventNameForType(this.mEventType);
    }

    public short getCoalescingKey() {
        return (short) this.mEventType;
    }

    public void dispatch(RCTEventEmitter rCTEventEmitter) {
        rCTEventEmitter.receiveEvent(getViewTag(), getEventName(), this.mMap);
    }

    public ImageEvent setExtras(WritableMap writableMap) {
        this.mMap = writableMap;
        return this;
    }
}

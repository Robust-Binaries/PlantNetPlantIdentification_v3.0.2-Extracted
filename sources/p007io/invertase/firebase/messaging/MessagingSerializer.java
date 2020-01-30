package p007io.invertase.firebase.messaging;

import com.facebook.common.util.UriUtil;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.google.firebase.messaging.RemoteMessage;
import java.util.Map.Entry;

/* renamed from: io.invertase.firebase.messaging.MessagingSerializer */
public class MessagingSerializer {
    public static WritableMap parseRemoteMessage(RemoteMessage remoteMessage) {
        WritableMap createMap = Arguments.createMap();
        WritableMap createMap2 = Arguments.createMap();
        if (remoteMessage.getCollapseKey() != null) {
            createMap.putString("collapseKey", remoteMessage.getCollapseKey());
        }
        if (remoteMessage.getData() != null) {
            for (Entry entry : remoteMessage.getData().entrySet()) {
                createMap2.putString((String) entry.getKey(), (String) entry.getValue());
            }
        }
        createMap.putMap(UriUtil.DATA_SCHEME, createMap2);
        if (remoteMessage.getFrom() != null) {
            createMap.putString("from", remoteMessage.getFrom());
        }
        if (remoteMessage.getMessageId() != null) {
            createMap.putString("messageId", remoteMessage.getMessageId());
        }
        if (remoteMessage.getMessageType() != null) {
            createMap.putString("messageType", remoteMessage.getMessageType());
        }
        createMap.putDouble("sentTime", (double) remoteMessage.getSentTime());
        if (remoteMessage.getTo() != null) {
            createMap.putString("to", remoteMessage.getTo());
        }
        createMap.putDouble("ttl", (double) remoteMessage.getTtl());
        return createMap;
    }
}

package okhttp3.internal.cache;

import java.io.IOException;
import okhttp3.C1512Response;
import okhttp3.Request;

public interface InternalCache {
    C1512Response get(Request request) throws IOException;

    CacheRequest put(C1512Response response) throws IOException;

    void remove(Request request) throws IOException;

    void trackConditionalCacheHit();

    void trackResponse(CacheStrategy cacheStrategy);

    void update(C1512Response response, C1512Response response2);
}

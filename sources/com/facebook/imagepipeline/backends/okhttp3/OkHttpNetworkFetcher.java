package com.facebook.imagepipeline.backends.okhttp3;

import android.os.Looper;
import android.os.SystemClock;
import com.facebook.imagepipeline.common.BytesRange;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.producers.BaseNetworkFetcher;
import com.facebook.imagepipeline.producers.BaseProducerContextCallbacks;
import com.facebook.imagepipeline.producers.Consumer;
import com.facebook.imagepipeline.producers.FetchState;
import com.facebook.imagepipeline.producers.NetworkFetcher.Callback;
import com.facebook.imagepipeline.producers.ProducerContext;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import okhttp3.C1512Response;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Call.Factory;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.ResponseBody;

public class OkHttpNetworkFetcher extends BaseNetworkFetcher<OkHttpNetworkFetchState> {
    private static final String FETCH_TIME = "fetch_time";
    private static final String IMAGE_SIZE = "image_size";
    private static final String QUEUE_TIME = "queue_time";
    private static final String TAG = "OkHttpNetworkFetchProducer";
    private static final String TOTAL_TIME = "total_time";
    private final Factory mCallFactory;
    /* access modifiers changed from: private */
    public Executor mCancellationExecutor;

    public static class OkHttpNetworkFetchState extends FetchState {
        public long fetchCompleteTime;
        public long responseTime;
        public long submitTime;

        public OkHttpNetworkFetchState(Consumer<EncodedImage> consumer, ProducerContext producerContext) {
            super(consumer, producerContext);
        }
    }

    public OkHttpNetworkFetcher(OkHttpClient okHttpClient) {
        this(okHttpClient, okHttpClient.dispatcher().executorService());
    }

    public OkHttpNetworkFetcher(Factory factory, Executor executor) {
        this.mCallFactory = factory;
        this.mCancellationExecutor = executor;
    }

    public OkHttpNetworkFetchState createFetchState(Consumer<EncodedImage> consumer, ProducerContext producerContext) {
        return new OkHttpNetworkFetchState(consumer, producerContext);
    }

    public void fetch(OkHttpNetworkFetchState okHttpNetworkFetchState, Callback callback) {
        okHttpNetworkFetchState.submitTime = SystemClock.elapsedRealtime();
        try {
            Builder builder = new Builder().cacheControl(new CacheControl.Builder().noStore().build()).url(okHttpNetworkFetchState.getUri().toString()).get();
            BytesRange bytesRange = okHttpNetworkFetchState.getContext().getImageRequest().getBytesRange();
            if (bytesRange != null) {
                builder.addHeader("Range", bytesRange.toHttpRangeHeaderValue());
            }
            fetchWithRequest(okHttpNetworkFetchState, callback, builder.build());
        } catch (Exception e) {
            callback.onFailure(e);
        }
    }

    public void onFetchCompletion(OkHttpNetworkFetchState okHttpNetworkFetchState, int i) {
        okHttpNetworkFetchState.fetchCompleteTime = SystemClock.elapsedRealtime();
    }

    public Map<String, String> getExtraMap(OkHttpNetworkFetchState okHttpNetworkFetchState, int i) {
        HashMap hashMap = new HashMap(4);
        hashMap.put(QUEUE_TIME, Long.toString(okHttpNetworkFetchState.responseTime - okHttpNetworkFetchState.submitTime));
        hashMap.put(FETCH_TIME, Long.toString(okHttpNetworkFetchState.fetchCompleteTime - okHttpNetworkFetchState.responseTime));
        hashMap.put(TOTAL_TIME, Long.toString(okHttpNetworkFetchState.fetchCompleteTime - okHttpNetworkFetchState.submitTime));
        hashMap.put(IMAGE_SIZE, Integer.toString(i));
        return hashMap;
    }

    /* access modifiers changed from: protected */
    public void fetchWithRequest(final OkHttpNetworkFetchState okHttpNetworkFetchState, final Callback callback, Request request) {
        final Call newCall = this.mCallFactory.newCall(request);
        okHttpNetworkFetchState.getContext().addCallbacks(new BaseProducerContextCallbacks() {
            public void onCancellationRequested() {
                if (Looper.myLooper() != Looper.getMainLooper()) {
                    newCall.cancel();
                } else {
                    OkHttpNetworkFetcher.this.mCancellationExecutor.execute(new Runnable() {
                        public void run() {
                            newCall.cancel();
                        }
                    });
                }
            }
        });
        newCall.enqueue(new okhttp3.Callback() {
            public void onResponse(Call call, C1512Response response) throws IOException {
                okHttpNetworkFetchState.responseTime = SystemClock.elapsedRealtime();
                ResponseBody body = response.body();
                try {
                    if (!response.isSuccessful()) {
                        OkHttpNetworkFetcher okHttpNetworkFetcher = OkHttpNetworkFetcher.this;
                        StringBuilder sb = new StringBuilder();
                        sb.append("Unexpected HTTP code ");
                        sb.append(response);
                        okHttpNetworkFetcher.handleException(call, new IOException(sb.toString()), callback);
                        body.close();
                        return;
                    }
                    BytesRange fromContentRangeHeader = BytesRange.fromContentRangeHeader(response.header("Content-Range"));
                    if (!(fromContentRangeHeader == null || (fromContentRangeHeader.from == 0 && fromContentRangeHeader.f53to == Integer.MAX_VALUE))) {
                        okHttpNetworkFetchState.setResponseBytesRange(fromContentRangeHeader);
                        okHttpNetworkFetchState.setOnNewResultStatusFlags(8);
                    }
                    long contentLength = body.contentLength();
                    if (contentLength < 0) {
                        contentLength = 0;
                    }
                    callback.onResponse(body.byteStream(), (int) contentLength);
                    body.close();
                } catch (Exception e) {
                    OkHttpNetworkFetcher.this.handleException(call, e, callback);
                } catch (Throwable th) {
                    body.close();
                    throw th;
                }
            }

            public void onFailure(Call call, IOException iOException) {
                OkHttpNetworkFetcher.this.handleException(call, iOException, callback);
            }
        });
    }

    /* access modifiers changed from: private */
    public void handleException(Call call, Exception exc, Callback callback) {
        if (call.isCanceled()) {
            callback.onCancellation();
        } else {
            callback.onFailure(exc);
        }
    }
}

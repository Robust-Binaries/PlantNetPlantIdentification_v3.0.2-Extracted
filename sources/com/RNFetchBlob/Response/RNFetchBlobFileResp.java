package com.RNFetchBlob.Response;

import android.support.annotation.NonNull;
import com.RNFetchBlob.RNFetchBlobConst;
import com.RNFetchBlob.RNFetchBlobProgressConfig;
import com.RNFetchBlob.RNFetchBlobReq;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.Okio;
import okio.Source;
import okio.Timeout;

public class RNFetchBlobFileResp extends ResponseBody {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    long bytesDownloaded = 0;
    String mPath;
    String mTaskId;
    FileOutputStream ofStream;
    ResponseBody originalBody;
    ReactApplicationContext rctContext;

    private class ProgressReportingSource implements Source {
        public Timeout timeout() {
            return null;
        }

        private ProgressReportingSource() {
        }

        public long read(@NonNull Buffer buffer, long j) throws IOException {
            int i = (int) j;
            try {
                byte[] bArr = new byte[i];
                long read = (long) RNFetchBlobFileResp.this.originalBody.byteStream().read(bArr, 0, i);
                RNFetchBlobFileResp rNFetchBlobFileResp = RNFetchBlobFileResp.this;
                rNFetchBlobFileResp.bytesDownloaded += read > 0 ? read : 0;
                if (read > 0) {
                    RNFetchBlobFileResp.this.ofStream.write(bArr, 0, (int) read);
                }
                RNFetchBlobProgressConfig reportProgress = RNFetchBlobReq.getReportProgress(RNFetchBlobFileResp.this.mTaskId);
                if (!(reportProgress == null || RNFetchBlobFileResp.this.contentLength() == 0 || !reportProgress.shouldReport((float) (RNFetchBlobFileResp.this.bytesDownloaded / RNFetchBlobFileResp.this.contentLength())))) {
                    WritableMap createMap = Arguments.createMap();
                    createMap.putString("taskId", RNFetchBlobFileResp.this.mTaskId);
                    createMap.putString("written", String.valueOf(RNFetchBlobFileResp.this.bytesDownloaded));
                    createMap.putString("total", String.valueOf(RNFetchBlobFileResp.this.contentLength()));
                    ((RCTDeviceEventEmitter) RNFetchBlobFileResp.this.rctContext.getJSModule(RCTDeviceEventEmitter.class)).emit(RNFetchBlobConst.EVENT_PROGRESS, createMap);
                }
                return read;
            } catch (Exception unused) {
                return -1;
            }
        }

        public void close() throws IOException {
            RNFetchBlobFileResp.this.ofStream.close();
        }
    }

    public RNFetchBlobFileResp(ReactApplicationContext reactApplicationContext, String str, ResponseBody responseBody, String str2, boolean z) throws IOException {
        this.rctContext = reactApplicationContext;
        this.mTaskId = str;
        this.originalBody = responseBody;
        this.mPath = str2;
        if (str2 != null) {
            boolean z2 = !z;
            String replace = str2.replace("?append=true", "");
            this.mPath = replace;
            File file = new File(replace);
            File parentFile = file.getParentFile();
            if (parentFile == null || parentFile.exists() || parentFile.mkdirs()) {
                if (!file.exists()) {
                    file.createNewFile();
                }
                this.ofStream = new FileOutputStream(new File(replace), z2);
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Couldn't create dir: ");
            sb.append(parentFile);
            throw new IllegalStateException(sb.toString());
        }
    }

    public MediaType contentType() {
        return this.originalBody.contentType();
    }

    public long contentLength() {
        return this.originalBody.contentLength();
    }

    public BufferedSource source() {
        return Okio.buffer((Source) new ProgressReportingSource());
    }
}

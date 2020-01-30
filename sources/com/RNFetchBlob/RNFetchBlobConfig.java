package com.RNFetchBlob;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;

class RNFetchBlobConfig {
    public ReadableMap addAndroidDownloads;
    public String appendExt;
    public Boolean auto;
    public ReadableArray binaryContentTypes;
    public Boolean fileCache;
    public Boolean followRedirect;
    public Boolean increment;
    public String key;
    public String mime;
    public Boolean overwrite = Boolean.valueOf(true);
    public String path;
    public long timeout = 60000;
    public Boolean trusty;

    RNFetchBlobConfig(ReadableMap readableMap) {
        boolean z = false;
        this.increment = Boolean.valueOf(false);
        this.followRedirect = Boolean.valueOf(true);
        String str = null;
        this.binaryContentTypes = null;
        if (readableMap != null) {
            this.fileCache = Boolean.valueOf(readableMap.hasKey("fileCache") ? readableMap.getBoolean("fileCache") : false);
            this.path = readableMap.hasKey(RNFetchBlobConst.RNFB_RESPONSE_PATH) ? readableMap.getString(RNFetchBlobConst.RNFB_RESPONSE_PATH) : null;
            this.appendExt = readableMap.hasKey("appendExt") ? readableMap.getString("appendExt") : "";
            this.trusty = Boolean.valueOf(readableMap.hasKey("trusty") ? readableMap.getBoolean("trusty") : false);
            if (readableMap.hasKey("addAndroidDownloads")) {
                this.addAndroidDownloads = readableMap.getMap("addAndroidDownloads");
            }
            if (readableMap.hasKey("binaryContentTypes")) {
                this.binaryContentTypes = readableMap.getArray("binaryContentTypes");
            }
            String str2 = this.path;
            if (str2 != null && str2.toLowerCase().contains("?append=true")) {
                this.overwrite = Boolean.valueOf(false);
            }
            if (readableMap.hasKey("overwrite")) {
                this.overwrite = Boolean.valueOf(readableMap.getBoolean("overwrite"));
            }
            if (readableMap.hasKey("followRedirect")) {
                this.followRedirect = Boolean.valueOf(readableMap.getBoolean("followRedirect"));
            }
            this.key = readableMap.hasKey("key") ? readableMap.getString("key") : null;
            if (readableMap.hasKey("contentType")) {
                str = readableMap.getString("contentType");
            }
            this.mime = str;
            this.increment = Boolean.valueOf(readableMap.hasKey("increment") ? readableMap.getBoolean("increment") : false);
            if (readableMap.hasKey("auto")) {
                z = readableMap.getBoolean("auto");
            }
            this.auto = Boolean.valueOf(z);
            if (readableMap.hasKey("timeout")) {
                this.timeout = (long) readableMap.getInt("timeout");
            }
        }
    }
}

package com.facebook.imagepipeline.cache;

import com.facebook.common.internal.Supplier;

public class DefaultEncodedMemoryCacheParamsSupplier implements Supplier<MemoryCacheParams> {
    private static final int MAX_CACHE_ENTRIES = Integer.MAX_VALUE;
    private static final int MAX_EVICTION_QUEUE_ENTRIES = Integer.MAX_VALUE;

    public MemoryCacheParams get() {
        int maxCacheSize = getMaxCacheSize();
        MemoryCacheParams memoryCacheParams = new MemoryCacheParams(maxCacheSize, Integer.MAX_VALUE, maxCacheSize, Integer.MAX_VALUE, maxCacheSize / 8);
        return memoryCacheParams;
    }

    private int getMaxCacheSize() {
        int min = (int) Math.min(Runtime.getRuntime().maxMemory(), 2147483647L);
        if (min < 16777216) {
            return 1048576;
        }
        return min < 33554432 ? 2097152 : 4194304;
    }
}

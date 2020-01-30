package com.facebook.react.views.image;

import android.graphics.Bitmap;
import com.facebook.cache.common.CacheKey;
import com.facebook.cache.common.MultiCacheKey;
import com.facebook.common.references.CloseableReference;
import com.facebook.imagepipeline.bitmaps.PlatformBitmapFactory;
import com.facebook.imagepipeline.request.Postprocessor;
import java.util.LinkedList;
import java.util.List;

public class MultiPostprocessor implements Postprocessor {
    private final List<Postprocessor> mPostprocessors;

    public static Postprocessor from(List<Postprocessor> list) {
        switch (list.size()) {
            case 0:
                return null;
            case 1:
                return (Postprocessor) list.get(0);
            default:
                return new MultiPostprocessor(list);
        }
    }

    private MultiPostprocessor(List<Postprocessor> list) {
        this.mPostprocessors = new LinkedList(list);
    }

    public String getName() {
        StringBuilder sb = new StringBuilder();
        for (Postprocessor postprocessor : this.mPostprocessors) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(postprocessor.getName());
        }
        sb.insert(0, "MultiPostProcessor (");
        sb.append(")");
        return sb.toString();
    }

    public CacheKey getPostprocessorCacheKey() {
        LinkedList linkedList = new LinkedList();
        for (Postprocessor postprocessorCacheKey : this.mPostprocessors) {
            linkedList.push(postprocessorCacheKey.getPostprocessorCacheKey());
        }
        return new MultiCacheKey(linkedList);
    }

    /* JADX INFO: finally extract failed */
    public CloseableReference<Bitmap> process(Bitmap bitmap, PlatformBitmapFactory platformBitmapFactory) {
        CloseableReference closeableReference = null;
        try {
            CloseableReference closeableReference2 = null;
            for (Postprocessor process : this.mPostprocessors) {
                closeableReference = process.process(closeableReference2 != null ? (Bitmap) closeableReference2.get() : bitmap, platformBitmapFactory);
                CloseableReference.closeSafely(closeableReference2);
                closeableReference2 = closeableReference.clone();
            }
            CloseableReference<Bitmap> clone = closeableReference.clone();
            CloseableReference.closeSafely(closeableReference);
            return clone;
        } catch (Throwable th) {
            CloseableReference.closeSafely(null);
            throw th;
        }
    }
}

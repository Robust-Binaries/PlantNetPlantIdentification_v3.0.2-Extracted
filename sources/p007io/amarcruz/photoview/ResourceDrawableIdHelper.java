package p007io.amarcruz.photoview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.Uri.Builder;
import com.facebook.common.util.UriUtil;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;

/* renamed from: io.amarcruz.photoview.ResourceDrawableIdHelper */
class ResourceDrawableIdHelper {
    private Map<String, Integer> mResourceDrawableIdMap = new HashMap();

    ResourceDrawableIdHelper() {
    }

    private int getResourceDrawableId(Context context, @Nullable String str) {
        if (str == null || str.isEmpty()) {
            return 0;
        }
        String replace = str.toLowerCase().replace("-", "_");
        if (this.mResourceDrawableIdMap.containsKey(replace)) {
            return ((Integer) this.mResourceDrawableIdMap.get(replace)).intValue();
        }
        int identifier = context.getResources().getIdentifier(replace, "drawable", context.getPackageName());
        this.mResourceDrawableIdMap.put(replace, Integer.valueOf(identifier));
        return identifier;
    }

    @Nullable
    public Drawable getResourceDrawable(Context context, @Nullable String str) {
        int resourceDrawableId = getResourceDrawableId(context, str);
        if (resourceDrawableId > 0) {
            return context.getResources().getDrawable(resourceDrawableId);
        }
        return null;
    }

    public Uri getResourceDrawableUri(Context context, @Nullable String str) {
        int resourceDrawableId = getResourceDrawableId(context, str);
        return resourceDrawableId > 0 ? new Builder().scheme(UriUtil.LOCAL_RESOURCE_SCHEME).path(String.valueOf(resourceDrawableId)).build() : Uri.EMPTY;
    }
}

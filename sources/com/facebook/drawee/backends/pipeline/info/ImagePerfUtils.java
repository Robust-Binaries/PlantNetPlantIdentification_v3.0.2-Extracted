package com.facebook.drawee.backends.pipeline.info;

import android.support.p000v4.p002os.EnvironmentCompat;
import com.google.firebase.analytics.FirebaseAnalytics.Param;

public class ImagePerfUtils {
    public static String toString(int i) {
        switch (i) {
            case 0:
                return "requested";
            case 1:
                return "origin_available";
            case 2:
                return "intermediate_available";
            case 3:
                return Param.SUCCESS;
            case 4:
                return "canceled";
            case 5:
                return "error";
            default:
                return EnvironmentCompat.MEDIA_UNKNOWN;
        }
    }

    private ImagePerfUtils() {
    }
}

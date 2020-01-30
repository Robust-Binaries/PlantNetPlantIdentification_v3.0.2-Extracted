package com.facebook.drawee.backends.pipeline.info;

import android.support.p000v4.p002os.EnvironmentCompat;

public class ImageOriginUtils {
    public static String toString(int i) {
        switch (i) {
            case 0:
                return "network";
            case 1:
                return "disk";
            case 2:
                return "memory_encoded";
            case 3:
                return "memory_bitmap";
            default:
                return EnvironmentCompat.MEDIA_UNKNOWN;
        }
    }

    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int mapProducerNameToImageOrigin(java.lang.String r6) {
        /*
            int r0 = r6.hashCode()
            r1 = 0
            r2 = 1
            r3 = 2
            r4 = 3
            r5 = -1
            switch(r0) {
                case -1914072202: goto L_0x0035;
                case -1307634203: goto L_0x002b;
                case -1224383234: goto L_0x0021;
                case 656304759: goto L_0x0017;
                case 957714404: goto L_0x000d;
                default: goto L_0x000c;
            }
        L_0x000c:
            goto L_0x003f
        L_0x000d:
            java.lang.String r0 = "BitmapMemoryCacheProducer"
            boolean r6 = r6.equals(r0)
            if (r6 == 0) goto L_0x003f
            r6 = 1
            goto L_0x0040
        L_0x0017:
            java.lang.String r0 = "DiskCacheProducer"
            boolean r6 = r6.equals(r0)
            if (r6 == 0) goto L_0x003f
            r6 = 3
            goto L_0x0040
        L_0x0021:
            java.lang.String r0 = "NetworkFetchProducer"
            boolean r6 = r6.equals(r0)
            if (r6 == 0) goto L_0x003f
            r6 = 4
            goto L_0x0040
        L_0x002b:
            java.lang.String r0 = "EncodedMemoryCacheProducer"
            boolean r6 = r6.equals(r0)
            if (r6 == 0) goto L_0x003f
            r6 = 2
            goto L_0x0040
        L_0x0035:
            java.lang.String r0 = "BitmapMemoryCacheGetProducer"
            boolean r6 = r6.equals(r0)
            if (r6 == 0) goto L_0x003f
            r6 = 0
            goto L_0x0040
        L_0x003f:
            r6 = -1
        L_0x0040:
            switch(r6) {
                case 0: goto L_0x0047;
                case 1: goto L_0x0047;
                case 2: goto L_0x0046;
                case 3: goto L_0x0045;
                case 4: goto L_0x0044;
                default: goto L_0x0043;
            }
        L_0x0043:
            return r5
        L_0x0044:
            return r1
        L_0x0045:
            return r2
        L_0x0046:
            return r3
        L_0x0047:
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.drawee.backends.pipeline.info.ImageOriginUtils.mapProducerNameToImageOrigin(java.lang.String):int");
    }

    private ImageOriginUtils() {
    }
}

package com.facebook.react.uimanager.layoutanimation;

enum InterpolatorType {
    LINEAR,
    EASE_IN,
    EASE_OUT,
    EASE_IN_EASE_OUT,
    SPRING;

    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.facebook.react.uimanager.layoutanimation.InterpolatorType fromString(java.lang.String r3) {
        /*
            java.util.Locale r0 = java.util.Locale.US
            java.lang.String r0 = r3.toLowerCase(r0)
            int r1 = r0.hashCode()
            switch(r1) {
                case -1965056864: goto L_0x0036;
                case -1310315117: goto L_0x002c;
                case -1102672091: goto L_0x0022;
                case -895679987: goto L_0x0018;
                case 1164546989: goto L_0x000e;
                default: goto L_0x000d;
            }
        L_0x000d:
            goto L_0x0040
        L_0x000e:
            java.lang.String r1 = "easeineaseout"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0040
            r0 = 3
            goto L_0x0041
        L_0x0018:
            java.lang.String r1 = "spring"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0040
            r0 = 4
            goto L_0x0041
        L_0x0022:
            java.lang.String r1 = "linear"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0040
            r0 = 0
            goto L_0x0041
        L_0x002c:
            java.lang.String r1 = "easein"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0040
            r0 = 1
            goto L_0x0041
        L_0x0036:
            java.lang.String r1 = "easeout"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0040
            r0 = 2
            goto L_0x0041
        L_0x0040:
            r0 = -1
        L_0x0041:
            switch(r0) {
                case 0: goto L_0x0067;
                case 1: goto L_0x0064;
                case 2: goto L_0x0061;
                case 3: goto L_0x005e;
                case 4: goto L_0x005b;
                default: goto L_0x0044;
            }
        L_0x0044:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Unsupported interpolation type : "
            r1.append(r2)
            r1.append(r3)
            java.lang.String r3 = r1.toString()
            r0.<init>(r3)
            throw r0
        L_0x005b:
            com.facebook.react.uimanager.layoutanimation.InterpolatorType r3 = SPRING
            return r3
        L_0x005e:
            com.facebook.react.uimanager.layoutanimation.InterpolatorType r3 = EASE_IN_EASE_OUT
            return r3
        L_0x0061:
            com.facebook.react.uimanager.layoutanimation.InterpolatorType r3 = EASE_OUT
            return r3
        L_0x0064:
            com.facebook.react.uimanager.layoutanimation.InterpolatorType r3 = EASE_IN
            return r3
        L_0x0067:
            com.facebook.react.uimanager.layoutanimation.InterpolatorType r3 = LINEAR
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.react.uimanager.layoutanimation.InterpolatorType.fromString(java.lang.String):com.facebook.react.uimanager.layoutanimation.InterpolatorType");
    }
}

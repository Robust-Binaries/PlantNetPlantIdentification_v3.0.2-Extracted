package com.facebook.react.uimanager.layoutanimation;

enum AnimatedPropertyType {
    OPACITY,
    SCALE_X,
    SCALE_Y,
    SCALE_XY;

    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.facebook.react.uimanager.layoutanimation.AnimatedPropertyType fromString(java.lang.String r3) {
        /*
            int r0 = r3.hashCode()
            switch(r0) {
                case -1267206133: goto L_0x0026;
                case -908189618: goto L_0x001c;
                case -908189617: goto L_0x0012;
                case 1910893003: goto L_0x0008;
                default: goto L_0x0007;
            }
        L_0x0007:
            goto L_0x0030
        L_0x0008:
            java.lang.String r0 = "scaleXY"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0030
            r0 = 3
            goto L_0x0031
        L_0x0012:
            java.lang.String r0 = "scaleY"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0030
            r0 = 2
            goto L_0x0031
        L_0x001c:
            java.lang.String r0 = "scaleX"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0030
            r0 = 1
            goto L_0x0031
        L_0x0026:
            java.lang.String r0 = "opacity"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0030
            r0 = 0
            goto L_0x0031
        L_0x0030:
            r0 = -1
        L_0x0031:
            switch(r0) {
                case 0: goto L_0x0054;
                case 1: goto L_0x0051;
                case 2: goto L_0x004e;
                case 3: goto L_0x004b;
                default: goto L_0x0034;
            }
        L_0x0034:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Unsupported animated property: "
            r1.append(r2)
            r1.append(r3)
            java.lang.String r3 = r1.toString()
            r0.<init>(r3)
            throw r0
        L_0x004b:
            com.facebook.react.uimanager.layoutanimation.AnimatedPropertyType r3 = SCALE_XY
            return r3
        L_0x004e:
            com.facebook.react.uimanager.layoutanimation.AnimatedPropertyType r3 = SCALE_Y
            return r3
        L_0x0051:
            com.facebook.react.uimanager.layoutanimation.AnimatedPropertyType r3 = SCALE_X
            return r3
        L_0x0054:
            com.facebook.react.uimanager.layoutanimation.AnimatedPropertyType r3 = OPACITY
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.react.uimanager.layoutanimation.AnimatedPropertyType.fromString(java.lang.String):com.facebook.react.uimanager.layoutanimation.AnimatedPropertyType");
    }
}

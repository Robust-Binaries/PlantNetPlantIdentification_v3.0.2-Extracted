package com.facebook.drawee.generic;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import com.facebook.drawee.drawable.ScalingUtils.ScaleType;
import com.facebook.infer.annotation.ReturnsOwnership;
import javax.annotation.Nullable;

public class GenericDraweeHierarchyInflater {
    public static GenericDraweeHierarchy inflateHierarchy(Context context, @Nullable AttributeSet attributeSet) {
        return inflateBuilder(context, attributeSet).build();
    }

    public static GenericDraweeHierarchyBuilder inflateBuilder(Context context, @Nullable AttributeSet attributeSet) {
        return updateBuilder(new GenericDraweeHierarchyBuilder(context.getResources()), context, attributeSet);
    }

    /* JADX WARNING: Removed duplicated region for block: B:140:0x024d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.facebook.drawee.generic.GenericDraweeHierarchyBuilder updateBuilder(com.facebook.drawee.generic.GenericDraweeHierarchyBuilder r18, android.content.Context r19, @javax.annotation.Nullable android.util.AttributeSet r20) {
        /*
            r0 = r18
            r1 = r19
            r2 = r20
            if (r2 == 0) goto L_0x025b
            int[] r6 = com.facebook.drawee.C0662R.styleable.GenericDraweeHierarchy
            android.content.res.TypedArray r2 = r1.obtainStyledAttributes(r2, r6)
            int r7 = r2.getIndexCount()     // Catch:{ all -> 0x023b }
            r5 = 1
            r6 = 0
            r8 = 0
            r9 = 1
            r10 = 1
            r11 = 1
            r12 = 1
            r13 = 1
            r14 = 1
            r15 = 1
            r17 = 0
        L_0x001e:
            if (r8 >= r7) goto L_0x01df
            int r3 = r2.getIndex(r8)     // Catch:{ all -> 0x01dc }
            int r4 = com.facebook.drawee.C0662R.styleable.GenericDraweeHierarchy_actualImageScaleType     // Catch:{ all -> 0x01dc }
            if (r3 != r4) goto L_0x0032
            com.facebook.drawee.drawable.ScalingUtils$ScaleType r3 = getScaleTypeFromXml(r2, r3)     // Catch:{ all -> 0x01dc }
            r0.setActualImageScaleType(r3)     // Catch:{ all -> 0x01dc }
            r4 = 0
            goto L_0x01d6
        L_0x0032:
            int r4 = com.facebook.drawee.C0662R.styleable.GenericDraweeHierarchy_placeholderImage     // Catch:{ all -> 0x01dc }
            if (r3 != r4) goto L_0x0040
            android.graphics.drawable.Drawable r3 = getDrawable(r1, r2, r3)     // Catch:{ all -> 0x01dc }
            r0.setPlaceholderImage(r3)     // Catch:{ all -> 0x01dc }
            r4 = 0
            goto L_0x01d6
        L_0x0040:
            int r4 = com.facebook.drawee.C0662R.styleable.GenericDraweeHierarchy_pressedStateOverlayImage     // Catch:{ all -> 0x01dc }
            if (r3 != r4) goto L_0x004e
            android.graphics.drawable.Drawable r3 = getDrawable(r1, r2, r3)     // Catch:{ all -> 0x01dc }
            r0.setPressedStateOverlay(r3)     // Catch:{ all -> 0x01dc }
            r4 = 0
            goto L_0x01d6
        L_0x004e:
            int r4 = com.facebook.drawee.C0662R.styleable.GenericDraweeHierarchy_progressBarImage     // Catch:{ all -> 0x01dc }
            if (r3 != r4) goto L_0x005c
            android.graphics.drawable.Drawable r3 = getDrawable(r1, r2, r3)     // Catch:{ all -> 0x01dc }
            r0.setProgressBarImage(r3)     // Catch:{ all -> 0x01dc }
            r4 = 0
            goto L_0x01d6
        L_0x005c:
            int r4 = com.facebook.drawee.C0662R.styleable.GenericDraweeHierarchy_fadeDuration     // Catch:{ all -> 0x01dc }
            if (r3 != r4) goto L_0x006b
            r4 = 0
            int r3 = r2.getInt(r3, r4)     // Catch:{ all -> 0x01dc }
            r0.setFadeDuration(r3)     // Catch:{ all -> 0x01dc }
            r4 = 0
            goto L_0x01d6
        L_0x006b:
            int r4 = com.facebook.drawee.C0662R.styleable.GenericDraweeHierarchy_viewAspectRatio     // Catch:{ all -> 0x01dc }
            if (r3 != r4) goto L_0x007a
            r4 = 0
            float r3 = r2.getFloat(r3, r4)     // Catch:{ all -> 0x01dc }
            r0.setDesiredAspectRatio(r3)     // Catch:{ all -> 0x01dc }
            r4 = 0
            goto L_0x01d6
        L_0x007a:
            int r4 = com.facebook.drawee.C0662R.styleable.GenericDraweeHierarchy_placeholderImageScaleType     // Catch:{ all -> 0x01dc }
            if (r3 != r4) goto L_0x0088
            com.facebook.drawee.drawable.ScalingUtils$ScaleType r3 = getScaleTypeFromXml(r2, r3)     // Catch:{ all -> 0x01dc }
            r0.setPlaceholderImageScaleType(r3)     // Catch:{ all -> 0x01dc }
            r4 = 0
            goto L_0x01d6
        L_0x0088:
            int r4 = com.facebook.drawee.C0662R.styleable.GenericDraweeHierarchy_retryImage     // Catch:{ all -> 0x01dc }
            if (r3 != r4) goto L_0x0096
            android.graphics.drawable.Drawable r3 = getDrawable(r1, r2, r3)     // Catch:{ all -> 0x01dc }
            r0.setRetryImage(r3)     // Catch:{ all -> 0x01dc }
            r4 = 0
            goto L_0x01d6
        L_0x0096:
            int r4 = com.facebook.drawee.C0662R.styleable.GenericDraweeHierarchy_retryImageScaleType     // Catch:{ all -> 0x01dc }
            if (r3 != r4) goto L_0x00a4
            com.facebook.drawee.drawable.ScalingUtils$ScaleType r3 = getScaleTypeFromXml(r2, r3)     // Catch:{ all -> 0x01dc }
            r0.setRetryImageScaleType(r3)     // Catch:{ all -> 0x01dc }
            r4 = 0
            goto L_0x01d6
        L_0x00a4:
            int r4 = com.facebook.drawee.C0662R.styleable.GenericDraweeHierarchy_failureImage     // Catch:{ all -> 0x01dc }
            if (r3 != r4) goto L_0x00b2
            android.graphics.drawable.Drawable r3 = getDrawable(r1, r2, r3)     // Catch:{ all -> 0x01dc }
            r0.setFailureImage(r3)     // Catch:{ all -> 0x01dc }
            r4 = 0
            goto L_0x01d6
        L_0x00b2:
            int r4 = com.facebook.drawee.C0662R.styleable.GenericDraweeHierarchy_failureImageScaleType     // Catch:{ all -> 0x01dc }
            if (r3 != r4) goto L_0x00c0
            com.facebook.drawee.drawable.ScalingUtils$ScaleType r3 = getScaleTypeFromXml(r2, r3)     // Catch:{ all -> 0x01dc }
            r0.setFailureImageScaleType(r3)     // Catch:{ all -> 0x01dc }
            r4 = 0
            goto L_0x01d6
        L_0x00c0:
            int r4 = com.facebook.drawee.C0662R.styleable.GenericDraweeHierarchy_progressBarImageScaleType     // Catch:{ all -> 0x01dc }
            if (r3 != r4) goto L_0x00ce
            com.facebook.drawee.drawable.ScalingUtils$ScaleType r3 = getScaleTypeFromXml(r2, r3)     // Catch:{ all -> 0x01dc }
            r0.setProgressBarImageScaleType(r3)     // Catch:{ all -> 0x01dc }
            r4 = 0
            goto L_0x01d6
        L_0x00ce:
            int r4 = com.facebook.drawee.C0662R.styleable.GenericDraweeHierarchy_progressBarAutoRotateInterval     // Catch:{ all -> 0x01dc }
            if (r3 != r4) goto L_0x00da
            int r3 = r2.getInteger(r3, r6)     // Catch:{ all -> 0x01dc }
            r6 = r3
            r4 = 0
            goto L_0x01d6
        L_0x00da:
            int r4 = com.facebook.drawee.C0662R.styleable.GenericDraweeHierarchy_backgroundImage     // Catch:{ all -> 0x01dc }
            if (r3 != r4) goto L_0x00e8
            android.graphics.drawable.Drawable r3 = getDrawable(r1, r2, r3)     // Catch:{ all -> 0x01dc }
            r0.setBackground(r3)     // Catch:{ all -> 0x01dc }
            r4 = 0
            goto L_0x01d6
        L_0x00e8:
            int r4 = com.facebook.drawee.C0662R.styleable.GenericDraweeHierarchy_overlayImage     // Catch:{ all -> 0x01dc }
            if (r3 != r4) goto L_0x00f6
            android.graphics.drawable.Drawable r3 = getDrawable(r1, r2, r3)     // Catch:{ all -> 0x01dc }
            r0.setOverlay(r3)     // Catch:{ all -> 0x01dc }
            r4 = 0
            goto L_0x01d6
        L_0x00f6:
            int r4 = com.facebook.drawee.C0662R.styleable.GenericDraweeHierarchy_roundAsCircle     // Catch:{ all -> 0x01dc }
            if (r3 != r4) goto L_0x0109
            com.facebook.drawee.generic.RoundingParams r4 = getRoundingParams(r18)     // Catch:{ all -> 0x01dc }
            r1 = 0
            boolean r3 = r2.getBoolean(r3, r1)     // Catch:{ all -> 0x01dc }
            r4.setRoundAsCircle(r3)     // Catch:{ all -> 0x01dc }
            r4 = 0
            goto L_0x01d6
        L_0x0109:
            int r1 = com.facebook.drawee.C0662R.styleable.GenericDraweeHierarchy_roundedCornerRadius     // Catch:{ all -> 0x01dc }
            if (r3 != r1) goto L_0x0118
            r4 = r17
            int r1 = r2.getDimensionPixelSize(r3, r4)     // Catch:{ all -> 0x01dc }
            r17 = r1
            r4 = 0
            goto L_0x01d6
        L_0x0118:
            r4 = r17
            int r1 = com.facebook.drawee.C0662R.styleable.GenericDraweeHierarchy_roundTopLeft     // Catch:{ all -> 0x01dc }
            if (r3 != r1) goto L_0x0128
            boolean r1 = r2.getBoolean(r3, r9)     // Catch:{ all -> 0x01dc }
            r9 = r1
            r17 = r4
            r4 = 0
            goto L_0x01d6
        L_0x0128:
            int r1 = com.facebook.drawee.C0662R.styleable.GenericDraweeHierarchy_roundTopRight     // Catch:{ all -> 0x01dc }
            if (r3 != r1) goto L_0x0136
            boolean r1 = r2.getBoolean(r3, r11)     // Catch:{ all -> 0x01dc }
            r11 = r1
            r17 = r4
            r4 = 0
            goto L_0x01d6
        L_0x0136:
            int r1 = com.facebook.drawee.C0662R.styleable.GenericDraweeHierarchy_roundBottomLeft     // Catch:{ all -> 0x01dc }
            if (r3 != r1) goto L_0x0144
            boolean r1 = r2.getBoolean(r3, r15)     // Catch:{ all -> 0x01dc }
            r15 = r1
            r17 = r4
            r4 = 0
            goto L_0x01d6
        L_0x0144:
            int r1 = com.facebook.drawee.C0662R.styleable.GenericDraweeHierarchy_roundBottomRight     // Catch:{ all -> 0x01dc }
            if (r3 != r1) goto L_0x0152
            boolean r1 = r2.getBoolean(r3, r13)     // Catch:{ all -> 0x01dc }
            r13 = r1
            r17 = r4
            r4 = 0
            goto L_0x01d6
        L_0x0152:
            int r1 = com.facebook.drawee.C0662R.styleable.GenericDraweeHierarchy_roundTopStart     // Catch:{ all -> 0x01dc }
            if (r3 != r1) goto L_0x0160
            boolean r1 = r2.getBoolean(r3, r10)     // Catch:{ all -> 0x01dc }
            r10 = r1
            r17 = r4
            r4 = 0
            goto L_0x01d6
        L_0x0160:
            int r1 = com.facebook.drawee.C0662R.styleable.GenericDraweeHierarchy_roundTopEnd     // Catch:{ all -> 0x01dc }
            if (r3 != r1) goto L_0x016e
            boolean r1 = r2.getBoolean(r3, r12)     // Catch:{ all -> 0x01dc }
            r12 = r1
            r17 = r4
            r4 = 0
            goto L_0x01d6
        L_0x016e:
            int r1 = com.facebook.drawee.C0662R.styleable.GenericDraweeHierarchy_roundBottomStart     // Catch:{ all -> 0x01dc }
            if (r3 != r1) goto L_0x017b
            boolean r1 = r2.getBoolean(r3, r5)     // Catch:{ all -> 0x01dc }
            r5 = r1
            r17 = r4
            r4 = 0
            goto L_0x01d6
        L_0x017b:
            int r1 = com.facebook.drawee.C0662R.styleable.GenericDraweeHierarchy_roundBottomEnd     // Catch:{ all -> 0x01dc }
            if (r3 != r1) goto L_0x0188
            boolean r1 = r2.getBoolean(r3, r14)     // Catch:{ all -> 0x01dc }
            r14 = r1
            r17 = r4
            r4 = 0
            goto L_0x01d6
        L_0x0188:
            int r1 = com.facebook.drawee.C0662R.styleable.GenericDraweeHierarchy_roundWithOverlayColor     // Catch:{ all -> 0x01dc }
            if (r3 != r1) goto L_0x019c
            com.facebook.drawee.generic.RoundingParams r1 = getRoundingParams(r18)     // Catch:{ all -> 0x01dc }
            r17 = r4
            r4 = 0
            int r3 = r2.getColor(r3, r4)     // Catch:{ all -> 0x01dc }
            r1.setOverlayColor(r3)     // Catch:{ all -> 0x01dc }
            r4 = 0
            goto L_0x01d6
        L_0x019c:
            r17 = r4
            int r1 = com.facebook.drawee.C0662R.styleable.GenericDraweeHierarchy_roundingBorderWidth     // Catch:{ all -> 0x01dc }
            if (r3 != r1) goto L_0x01b1
            com.facebook.drawee.generic.RoundingParams r1 = getRoundingParams(r18)     // Catch:{ all -> 0x01dc }
            r4 = 0
            int r3 = r2.getDimensionPixelSize(r3, r4)     // Catch:{ all -> 0x01dc }
            float r3 = (float) r3     // Catch:{ all -> 0x01dc }
            r1.setBorderWidth(r3)     // Catch:{ all -> 0x01dc }
            r4 = 0
            goto L_0x01d6
        L_0x01b1:
            int r1 = com.facebook.drawee.C0662R.styleable.GenericDraweeHierarchy_roundingBorderColor     // Catch:{ all -> 0x01dc }
            if (r3 != r1) goto L_0x01c3
            com.facebook.drawee.generic.RoundingParams r1 = getRoundingParams(r18)     // Catch:{ all -> 0x01dc }
            r4 = 0
            int r3 = r2.getColor(r3, r4)     // Catch:{ all -> 0x01dc }
            r1.setBorderColor(r3)     // Catch:{ all -> 0x01dc }
            r4 = 0
            goto L_0x01d6
        L_0x01c3:
            int r1 = com.facebook.drawee.C0662R.styleable.GenericDraweeHierarchy_roundingBorderPadding     // Catch:{ all -> 0x01dc }
            if (r3 != r1) goto L_0x01d5
            com.facebook.drawee.generic.RoundingParams r1 = getRoundingParams(r18)     // Catch:{ all -> 0x01dc }
            r4 = 0
            int r3 = r2.getDimensionPixelSize(r3, r4)     // Catch:{ all -> 0x01dc }
            float r3 = (float) r3     // Catch:{ all -> 0x01dc }
            r1.setPadding(r3)     // Catch:{ all -> 0x01dc }
            goto L_0x01d6
        L_0x01d5:
            r4 = 0
        L_0x01d6:
            int r8 = r8 + 1
            r1 = r19
            goto L_0x001e
        L_0x01dc:
            r0 = move-exception
            goto L_0x0244
        L_0x01df:
            r4 = 0
            r2.recycle()
            int r1 = android.os.Build.VERSION.SDK_INT
            r2 = 17
            if (r1 < r2) goto L_0x021a
            android.content.res.Resources r1 = r19.getResources()
            android.content.res.Configuration r1 = r1.getConfiguration()
            int r1 = r1.getLayoutDirection()
            r2 = 1
            if (r1 != r2) goto L_0x021a
            if (r9 == 0) goto L_0x01fe
            if (r12 == 0) goto L_0x01fe
            r1 = 1
            goto L_0x01ff
        L_0x01fe:
            r1 = 0
        L_0x01ff:
            if (r11 == 0) goto L_0x0205
            if (r10 == 0) goto L_0x0205
            r2 = 1
            goto L_0x0206
        L_0x0205:
            r2 = 0
        L_0x0206:
            if (r13 == 0) goto L_0x020c
            if (r5 == 0) goto L_0x020c
            r5 = 1
            goto L_0x020d
        L_0x020c:
            r5 = 0
        L_0x020d:
            if (r15 == 0) goto L_0x0214
            if (r14 == 0) goto L_0x0214
            r16 = 1
            goto L_0x0216
        L_0x0214:
            r16 = 0
        L_0x0216:
            r3 = r5
            r4 = r17
            goto L_0x0262
        L_0x021a:
            if (r9 == 0) goto L_0x0220
            if (r10 == 0) goto L_0x0220
            r1 = 1
            goto L_0x0221
        L_0x0220:
            r1 = 0
        L_0x0221:
            if (r11 == 0) goto L_0x0227
            if (r12 == 0) goto L_0x0227
            r2 = 1
            goto L_0x0228
        L_0x0227:
            r2 = 0
        L_0x0228:
            if (r13 == 0) goto L_0x022e
            if (r14 == 0) goto L_0x022e
            r3 = 1
            goto L_0x022f
        L_0x022e:
            r3 = 0
        L_0x022f:
            if (r15 == 0) goto L_0x0235
            if (r5 == 0) goto L_0x0235
            r5 = 1
            goto L_0x0236
        L_0x0235:
            r5 = 0
        L_0x0236:
            r16 = r5
            r4 = r17
            goto L_0x0262
        L_0x023b:
            r0 = move-exception
            r5 = 1
            r9 = 1
            r10 = 1
            r11 = 1
            r12 = 1
            r13 = 1
            r14 = 1
            r15 = 1
        L_0x0244:
            r2.recycle()
            int r1 = android.os.Build.VERSION.SDK_INT
            r2 = 17
            if (r1 < r2) goto L_0x025a
            android.content.res.Resources r1 = r19.getResources()
            android.content.res.Configuration r1 = r1.getConfiguration()
            int r1 = r1.getLayoutDirection()
            r2 = 1
        L_0x025a:
            throw r0
        L_0x025b:
            r2 = 1
            r4 = 0
            r1 = 1
            r3 = 1
            r6 = 0
            r16 = 1
        L_0x0262:
            android.graphics.drawable.Drawable r5 = r18.getProgressBarImage()
            if (r5 == 0) goto L_0x0276
            if (r6 <= 0) goto L_0x0276
            com.facebook.drawee.drawable.AutoRotateDrawable r5 = new com.facebook.drawee.drawable.AutoRotateDrawable
            android.graphics.drawable.Drawable r7 = r18.getProgressBarImage()
            r5.<init>(r7, r6)
            r0.setProgressBarImage(r5)
        L_0x0276:
            if (r4 <= 0) goto L_0x0293
            com.facebook.drawee.generic.RoundingParams r5 = getRoundingParams(r18)
            if (r1 == 0) goto L_0x0280
            float r1 = (float) r4
            goto L_0x0281
        L_0x0280:
            r1 = 0
        L_0x0281:
            if (r2 == 0) goto L_0x0285
            float r2 = (float) r4
            goto L_0x0286
        L_0x0285:
            r2 = 0
        L_0x0286:
            if (r3 == 0) goto L_0x028a
            float r3 = (float) r4
            goto L_0x028b
        L_0x028a:
            r3 = 0
        L_0x028b:
            if (r16 == 0) goto L_0x028f
            float r4 = (float) r4
            goto L_0x0290
        L_0x028f:
            r4 = 0
        L_0x0290:
            r5.setCornersRadii(r1, r2, r3, r4)
        L_0x0293:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.drawee.generic.GenericDraweeHierarchyInflater.updateBuilder(com.facebook.drawee.generic.GenericDraweeHierarchyBuilder, android.content.Context, android.util.AttributeSet):com.facebook.drawee.generic.GenericDraweeHierarchyBuilder");
    }

    @ReturnsOwnership
    private static RoundingParams getRoundingParams(GenericDraweeHierarchyBuilder genericDraweeHierarchyBuilder) {
        if (genericDraweeHierarchyBuilder.getRoundingParams() == null) {
            genericDraweeHierarchyBuilder.setRoundingParams(new RoundingParams());
        }
        return genericDraweeHierarchyBuilder.getRoundingParams();
    }

    @Nullable
    private static Drawable getDrawable(Context context, TypedArray typedArray, int i) {
        int resourceId = typedArray.getResourceId(i, 0);
        if (resourceId == 0) {
            return null;
        }
        return context.getResources().getDrawable(resourceId);
    }

    @Nullable
    private static ScaleType getScaleTypeFromXml(TypedArray typedArray, int i) {
        switch (typedArray.getInt(i, -2)) {
            case -1:
                return null;
            case 0:
                return ScaleType.FIT_XY;
            case 1:
                return ScaleType.FIT_START;
            case 2:
                return ScaleType.FIT_CENTER;
            case 3:
                return ScaleType.FIT_END;
            case 4:
                return ScaleType.CENTER;
            case 5:
                return ScaleType.CENTER_INSIDE;
            case 6:
                return ScaleType.CENTER_CROP;
            case 7:
                return ScaleType.FOCUS_CROP;
            case 8:
                return ScaleType.FIT_BOTTOM_START;
            default:
                throw new RuntimeException("XML attribute not specified!");
        }
    }
}

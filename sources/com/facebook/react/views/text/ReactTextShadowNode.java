package com.facebook.react.views.text;

import android.annotation.TargetApi;
import android.text.Spannable;
import android.text.TextPaint;
import com.facebook.react.uimanager.UIViewOperationQueue;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.yoga.YogaDirection;
import com.facebook.yoga.YogaMeasureFunction;
import javax.annotation.Nullable;

@TargetApi(23)
public class ReactTextShadowNode extends ReactBaseTextShadowNode {
    /* access modifiers changed from: private */
    public static final TextPaint sTextPaintInstance = new TextPaint(1);
    /* access modifiers changed from: private */
    @Nullable
    public Spannable mPreparedSpannableText;
    /* access modifiers changed from: private */
    public boolean mShouldNotifyOnTextLayout;
    private final YogaMeasureFunction mTextMeasureFunction = new YogaMeasureFunction() {
        /* JADX WARNING: type inference failed for: r12v12, types: [android.text.BoringLayout] */
        /* JADX WARNING: Multi-variable type inference failed */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public long measure(com.facebook.yoga.YogaNode r10, float r11, com.facebook.yoga.YogaMeasureMode r12, float r13, com.facebook.yoga.YogaMeasureMode r14) {
            /*
                r9 = this;
                android.text.TextPaint r2 = com.facebook.react.views.text.ReactTextShadowNode.sTextPaintInstance
                com.facebook.react.views.text.ReactTextShadowNode r10 = com.facebook.react.views.text.ReactTextShadowNode.this
                com.facebook.react.views.text.TextAttributes r10 = r10.mTextAttributes
                int r10 = r10.getEffectiveFontSize()
                float r10 = (float) r10
                r2.setTextSize(r10)
                com.facebook.react.views.text.ReactTextShadowNode r10 = com.facebook.react.views.text.ReactTextShadowNode.this
                android.text.Spannable r10 = r10.mPreparedSpannableText
                java.lang.String r13 = "Spannable element has not been prepared in onBeforeLayout"
                java.lang.Object r10 = com.facebook.infer.annotation.Assertions.assertNotNull(r10, r13)
                android.text.Spanned r10 = (android.text.Spanned) r10
                android.text.BoringLayout$Metrics r6 = android.text.BoringLayout.isBoring(r10, r2)
                if (r6 != 0) goto L_0x0029
                float r13 = android.text.Layout.getDesiredWidth(r10, r2)
                goto L_0x002b
            L_0x0029:
                r13 = 2143289344(0x7fc00000, float:NaN)
            L_0x002b:
                com.facebook.yoga.YogaMeasureMode r14 = com.facebook.yoga.YogaMeasureMode.UNDEFINED
                r0 = 0
                r1 = 0
                r8 = 1
                if (r12 == r14) goto L_0x0039
                int r12 = (r11 > r1 ? 1 : (r11 == r1 ? 0 : -1))
                if (r12 >= 0) goto L_0x0037
                goto L_0x0039
            L_0x0037:
                r12 = 0
                goto L_0x003a
            L_0x0039:
                r12 = 1
            L_0x003a:
                android.text.Layout$Alignment r14 = android.text.Layout.Alignment.ALIGN_NORMAL
                com.facebook.react.views.text.ReactTextShadowNode r3 = com.facebook.react.views.text.ReactTextShadowNode.this
                int r3 = r3.getTextAlign()
                if (r3 == r8) goto L_0x0054
                r4 = 3
                if (r3 == r4) goto L_0x0050
                r4 = 5
                if (r3 == r4) goto L_0x004c
                r4 = r14
                goto L_0x0057
            L_0x004c:
                android.text.Layout$Alignment r14 = android.text.Layout.Alignment.ALIGN_OPPOSITE
                r4 = r14
                goto L_0x0057
            L_0x0050:
                android.text.Layout$Alignment r14 = android.text.Layout.Alignment.ALIGN_NORMAL
                r4 = r14
                goto L_0x0057
            L_0x0054:
                android.text.Layout$Alignment r14 = android.text.Layout.Alignment.ALIGN_CENTER
                r4 = r14
            L_0x0057:
                r14 = 1065353216(0x3f800000, float:1.0)
                r3 = 23
                if (r6 != 0) goto L_0x00ba
                if (r12 != 0) goto L_0x0069
                boolean r5 = com.facebook.yoga.YogaConstants.isUndefined(r13)
                if (r5 != 0) goto L_0x00ba
                int r5 = (r13 > r11 ? 1 : (r13 == r11 ? 0 : -1))
                if (r5 > 0) goto L_0x00ba
            L_0x0069:
                double r11 = (double) r13
                double r11 = java.lang.Math.ceil(r11)
                int r11 = (int) r11
                int r12 = android.os.Build.VERSION.SDK_INT
                if (r12 >= r3) goto L_0x0084
                android.text.StaticLayout r12 = new android.text.StaticLayout
                r5 = 1065353216(0x3f800000, float:1.0)
                r6 = 0
                com.facebook.react.views.text.ReactTextShadowNode r13 = com.facebook.react.views.text.ReactTextShadowNode.this
                boolean r7 = r13.mIncludeFontPadding
                r0 = r12
                r1 = r10
                r3 = r11
                r0.<init>(r1, r2, r3, r4, r5, r6, r7)
                goto L_0x0115
            L_0x0084:
                int r12 = r10.length()
                android.text.StaticLayout$Builder r11 = android.text.StaticLayout.Builder.obtain(r10, r0, r12, r2, r11)
                android.text.StaticLayout$Builder r11 = r11.setAlignment(r4)
                android.text.StaticLayout$Builder r11 = r11.setLineSpacing(r1, r14)
                com.facebook.react.views.text.ReactTextShadowNode r12 = com.facebook.react.views.text.ReactTextShadowNode.this
                boolean r12 = r12.mIncludeFontPadding
                android.text.StaticLayout$Builder r11 = r11.setIncludePad(r12)
                com.facebook.react.views.text.ReactTextShadowNode r12 = com.facebook.react.views.text.ReactTextShadowNode.this
                int r12 = r12.mTextBreakStrategy
                android.text.StaticLayout$Builder r11 = r11.setBreakStrategy(r12)
                android.text.StaticLayout$Builder r11 = r11.setHyphenationFrequency(r8)
                int r12 = android.os.Build.VERSION.SDK_INT
                r13 = 26
                if (r12 < r13) goto L_0x00b5
                com.facebook.react.views.text.ReactTextShadowNode r12 = com.facebook.react.views.text.ReactTextShadowNode.this
                int r12 = r12.mJustificationMode
                r11.setJustificationMode(r12)
            L_0x00b5:
                android.text.StaticLayout r12 = r11.build()
                goto L_0x0115
            L_0x00ba:
                if (r6 == 0) goto L_0x00d8
                if (r12 != 0) goto L_0x00c5
                int r12 = r6.width
                float r12 = (float) r12
                int r12 = (r12 > r11 ? 1 : (r12 == r11 ? 0 : -1))
                if (r12 > 0) goto L_0x00d8
            L_0x00c5:
                int r11 = r6.width
                r12 = 1065353216(0x3f800000, float:1.0)
                r5 = 0
                com.facebook.react.views.text.ReactTextShadowNode r13 = com.facebook.react.views.text.ReactTextShadowNode.this
                boolean r7 = r13.mIncludeFontPadding
                r0 = r10
                r1 = r2
                r2 = r11
                r3 = r4
                r4 = r12
                android.text.BoringLayout r12 = android.text.BoringLayout.make(r0, r1, r2, r3, r4, r5, r6, r7)
                goto L_0x0115
            L_0x00d8:
                int r12 = android.os.Build.VERSION.SDK_INT
                if (r12 >= r3) goto L_0x00ec
                android.text.StaticLayout r12 = new android.text.StaticLayout
                int r3 = (int) r11
                r5 = 1065353216(0x3f800000, float:1.0)
                r6 = 0
                com.facebook.react.views.text.ReactTextShadowNode r11 = com.facebook.react.views.text.ReactTextShadowNode.this
                boolean r7 = r11.mIncludeFontPadding
                r0 = r12
                r1 = r10
                r0.<init>(r1, r2, r3, r4, r5, r6, r7)
                goto L_0x0115
            L_0x00ec:
                int r12 = r10.length()
                int r11 = (int) r11
                android.text.StaticLayout$Builder r11 = android.text.StaticLayout.Builder.obtain(r10, r0, r12, r2, r11)
                android.text.StaticLayout$Builder r11 = r11.setAlignment(r4)
                android.text.StaticLayout$Builder r11 = r11.setLineSpacing(r1, r14)
                com.facebook.react.views.text.ReactTextShadowNode r12 = com.facebook.react.views.text.ReactTextShadowNode.this
                boolean r12 = r12.mIncludeFontPadding
                android.text.StaticLayout$Builder r11 = r11.setIncludePad(r12)
                com.facebook.react.views.text.ReactTextShadowNode r12 = com.facebook.react.views.text.ReactTextShadowNode.this
                int r12 = r12.mTextBreakStrategy
                android.text.StaticLayout$Builder r11 = r11.setBreakStrategy(r12)
                android.text.StaticLayout$Builder r11 = r11.setHyphenationFrequency(r8)
                android.text.StaticLayout r12 = r11.build()
            L_0x0115:
                com.facebook.react.views.text.ReactTextShadowNode r11 = com.facebook.react.views.text.ReactTextShadowNode.this
                boolean r11 = r11.mShouldNotifyOnTextLayout
                if (r11 == 0) goto L_0x014d
                android.text.TextPaint r11 = com.facebook.react.views.text.ReactTextShadowNode.sTextPaintInstance
                com.facebook.react.views.text.ReactTextShadowNode r13 = com.facebook.react.views.text.ReactTextShadowNode.this
                com.facebook.react.uimanager.ThemedReactContext r13 = r13.getThemedContext()
                com.facebook.react.bridge.WritableArray r10 = com.facebook.react.views.text.FontMetricsUtil.getFontMetrics(r10, r12, r11, r13)
                com.facebook.react.bridge.WritableMap r11 = com.facebook.react.bridge.Arguments.createMap()
                java.lang.String r13 = "lines"
                r11.putArray(r13, r10)
                com.facebook.react.views.text.ReactTextShadowNode r10 = com.facebook.react.views.text.ReactTextShadowNode.this
                com.facebook.react.uimanager.ThemedReactContext r10 = r10.getThemedContext()
                java.lang.Class<com.facebook.react.uimanager.events.RCTEventEmitter> r13 = com.facebook.react.uimanager.events.RCTEventEmitter.class
                com.facebook.react.bridge.JavaScriptModule r10 = r10.getJSModule(r13)
                com.facebook.react.uimanager.events.RCTEventEmitter r10 = (com.facebook.react.uimanager.events.RCTEventEmitter) r10
                com.facebook.react.views.text.ReactTextShadowNode r13 = com.facebook.react.views.text.ReactTextShadowNode.this
                int r13 = r13.getReactTag()
                java.lang.String r14 = "topTextLayout"
                r10.receiveEvent(r13, r14, r11)
            L_0x014d:
                com.facebook.react.views.text.ReactTextShadowNode r10 = com.facebook.react.views.text.ReactTextShadowNode.this
                int r10 = r10.mNumberOfLines
                r11 = -1
                if (r10 == r11) goto L_0x0170
                com.facebook.react.views.text.ReactTextShadowNode r10 = com.facebook.react.views.text.ReactTextShadowNode.this
                int r10 = r10.mNumberOfLines
                int r11 = r12.getLineCount()
                if (r10 >= r11) goto L_0x0170
                int r10 = r12.getWidth()
                com.facebook.react.views.text.ReactTextShadowNode r11 = com.facebook.react.views.text.ReactTextShadowNode.this
                int r11 = r11.mNumberOfLines
                int r11 = r11 - r8
                int r11 = r12.getLineBottom(r11)
                long r10 = com.facebook.yoga.YogaMeasureOutput.make(r10, r11)
                return r10
            L_0x0170:
                int r10 = r12.getWidth()
                int r11 = r12.getHeight()
                long r10 = com.facebook.yoga.YogaMeasureOutput.make(r10, r11)
                return r10
            */
            throw new UnsupportedOperationException("Method not decompiled: com.facebook.react.views.text.ReactTextShadowNode.C10811.measure(com.facebook.yoga.YogaNode, float, com.facebook.yoga.YogaMeasureMode, float, com.facebook.yoga.YogaMeasureMode):long");
        }
    };

    public boolean isVirtualAnchor() {
        return true;
    }

    public ReactTextShadowNode() {
        initMeasureFunction();
    }

    private void initMeasureFunction() {
        if (!isVirtual()) {
            setMeasureFunction(this.mTextMeasureFunction);
        }
    }

    /* access modifiers changed from: private */
    public int getTextAlign() {
        int i = this.mTextAlign;
        if (getLayoutDirection() != YogaDirection.RTL) {
            return i;
        }
        if (i == 5) {
            return 3;
        }
        if (i == 3) {
            return 5;
        }
        return i;
    }

    public void onBeforeLayout() {
        this.mPreparedSpannableText = spannedFromShadowNode(this, null);
        markUpdated();
    }

    public void markUpdated() {
        super.markUpdated();
        super.dirty();
    }

    public void onCollectExtraUpdates(UIViewOperationQueue uIViewOperationQueue) {
        super.onCollectExtraUpdates(uIViewOperationQueue);
        Spannable spannable = this.mPreparedSpannableText;
        if (spannable != null) {
            ReactTextUpdate reactTextUpdate = new ReactTextUpdate(spannable, -1, this.mContainsImages, getPadding(4), getPadding(1), getPadding(5), getPadding(3), getTextAlign(), this.mTextBreakStrategy, this.mJustificationMode);
            uIViewOperationQueue.enqueueUpdateExtraData(getReactTag(), reactTextUpdate);
        }
    }

    @ReactProp(name = "onTextLayout")
    public void setShouldNotifyOnTextLayout(boolean z) {
        this.mShouldNotifyOnTextLayout = z;
    }
}

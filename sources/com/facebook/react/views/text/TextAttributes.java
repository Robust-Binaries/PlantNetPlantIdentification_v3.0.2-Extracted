package com.facebook.react.views.text;

import com.facebook.react.bridge.JSApplicationIllegalArgumentException;
import com.facebook.react.uimanager.PixelUtil;

public class TextAttributes {
    public static final float DEFAULT_MAX_FONT_SIZE_MULTIPLIER = 0.0f;
    private boolean mAllowFontScaling = true;
    private float mFontSize = Float.NaN;
    private float mHeightOfTallestInlineImage = Float.NaN;
    private float mLetterSpacing = Float.NaN;
    private float mLineHeight = Float.NaN;
    private float mMaxFontSizeMultiplier = Float.NaN;
    private TextTransform mTextTransform = TextTransform.UNSET;

    public TextAttributes applyChild(TextAttributes textAttributes) {
        TextAttributes textAttributes2 = new TextAttributes();
        textAttributes2.mAllowFontScaling = this.mAllowFontScaling;
        textAttributes2.mFontSize = !Float.isNaN(textAttributes.mFontSize) ? textAttributes.mFontSize : this.mFontSize;
        textAttributes2.mLineHeight = !Float.isNaN(textAttributes.mLineHeight) ? textAttributes.mLineHeight : this.mLineHeight;
        textAttributes2.mLetterSpacing = !Float.isNaN(textAttributes.mLetterSpacing) ? textAttributes.mLetterSpacing : this.mLetterSpacing;
        textAttributes2.mMaxFontSizeMultiplier = !Float.isNaN(textAttributes.mMaxFontSizeMultiplier) ? textAttributes.mMaxFontSizeMultiplier : this.mMaxFontSizeMultiplier;
        textAttributes2.mHeightOfTallestInlineImage = !Float.isNaN(textAttributes.mHeightOfTallestInlineImage) ? textAttributes.mHeightOfTallestInlineImage : this.mHeightOfTallestInlineImage;
        textAttributes2.mTextTransform = textAttributes.mTextTransform != TextTransform.UNSET ? textAttributes.mTextTransform : this.mTextTransform;
        return textAttributes2;
    }

    public boolean getAllowFontScaling() {
        return this.mAllowFontScaling;
    }

    public void setAllowFontScaling(boolean z) {
        this.mAllowFontScaling = z;
    }

    public float getFontSize() {
        return this.mFontSize;
    }

    public void setFontSize(float f) {
        this.mFontSize = f;
    }

    public float getLineHeight() {
        return this.mLineHeight;
    }

    public void setLineHeight(float f) {
        this.mLineHeight = f;
    }

    public float getLetterSpacing() {
        return this.mLetterSpacing;
    }

    public void setLetterSpacing(float f) {
        this.mLetterSpacing = f;
    }

    public float getMaxFontSizeMultiplier() {
        return this.mMaxFontSizeMultiplier;
    }

    public void setMaxFontSizeMultiplier(float f) {
        if (f == 0.0f || f >= 1.0f) {
            this.mMaxFontSizeMultiplier = f;
            return;
        }
        throw new JSApplicationIllegalArgumentException("maxFontSizeMultiplier must be NaN, 0, or >= 1");
    }

    public float getHeightOfTallestInlineImage() {
        return this.mHeightOfTallestInlineImage;
    }

    public void setHeightOfTallestInlineImage(float f) {
        this.mHeightOfTallestInlineImage = f;
    }

    public TextTransform getTextTransform() {
        return this.mTextTransform;
    }

    public void setTextTransform(TextTransform textTransform) {
        this.mTextTransform = textTransform;
    }

    public int getEffectiveFontSize() {
        float f = !Float.isNaN(this.mFontSize) ? this.mFontSize : 14.0f;
        if (this.mAllowFontScaling) {
            return (int) Math.ceil((double) PixelUtil.toPixelFromSP(f, getEffectiveMaxFontSizeMultiplier()));
        }
        return (int) Math.ceil((double) PixelUtil.toPixelFromDIP(f));
    }

    public float getEffectiveLineHeight() {
        float f;
        if (Float.isNaN(this.mLineHeight)) {
            return Float.NaN;
        }
        if (this.mAllowFontScaling) {
            f = PixelUtil.toPixelFromSP(this.mLineHeight, getEffectiveMaxFontSizeMultiplier());
        } else {
            f = PixelUtil.toPixelFromDIP(this.mLineHeight);
        }
        if (!Float.isNaN(this.mHeightOfTallestInlineImage) && this.mHeightOfTallestInlineImage > f) {
            f = this.mHeightOfTallestInlineImage;
        }
        return f;
    }

    public float getEffectiveLetterSpacing() {
        float f;
        if (Float.isNaN(this.mLetterSpacing)) {
            return Float.NaN;
        }
        if (this.mAllowFontScaling) {
            f = PixelUtil.toPixelFromSP(this.mLetterSpacing, getEffectiveMaxFontSizeMultiplier());
        } else {
            f = PixelUtil.toPixelFromDIP(this.mLetterSpacing);
        }
        return f / ((float) getEffectiveFontSize());
    }

    public float getEffectiveMaxFontSizeMultiplier() {
        if (!Float.isNaN(this.mMaxFontSizeMultiplier)) {
            return this.mMaxFontSizeMultiplier;
        }
        return 0.0f;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TextAttributes {\n  getAllowFontScaling(): ");
        sb.append(getAllowFontScaling());
        sb.append("\n  getFontSize(): ");
        sb.append(getFontSize());
        sb.append("\n  getEffectiveFontSize(): ");
        sb.append(getEffectiveFontSize());
        sb.append("\n  getHeightOfTallestInlineImage(): ");
        sb.append(getHeightOfTallestInlineImage());
        sb.append("\n  getLetterSpacing(): ");
        sb.append(getLetterSpacing());
        sb.append("\n  getEffectiveLetterSpacing(): ");
        sb.append(getEffectiveLetterSpacing());
        sb.append("\n  getLineHeight(): ");
        sb.append(getLineHeight());
        sb.append("\n  getEffectiveLineHeight(): ");
        sb.append(getEffectiveLineHeight());
        sb.append("\n  getTextTransform(): ");
        sb.append(getTextTransform());
        sb.append("\n  getMaxFontSizeMultiplier(): ");
        sb.append(getMaxFontSizeMultiplier());
        sb.append("\n  getEffectiveMaxFontSizeMultiplier(): ");
        sb.append(getEffectiveMaxFontSizeMultiplier());
        sb.append("\n}");
        return sb.toString();
    }
}

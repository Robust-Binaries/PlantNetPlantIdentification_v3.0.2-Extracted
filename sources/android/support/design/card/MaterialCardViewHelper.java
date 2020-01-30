package android.support.design.card;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.Dimension;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.design.C0043R;

@RestrictTo({Scope.LIBRARY_GROUP})
class MaterialCardViewHelper {
    private static final int DEFAULT_STROKE_VALUE = -1;
    private final MaterialCardView materialCardView;
    private int strokeColor;
    private int strokeWidth;

    public MaterialCardViewHelper(MaterialCardView materialCardView2) {
        this.materialCardView = materialCardView2;
    }

    public void loadFromAttributes(TypedArray typedArray) {
        this.strokeColor = typedArray.getColor(C0043R.styleable.MaterialCardView_strokeColor, -1);
        this.strokeWidth = typedArray.getDimensionPixelSize(C0043R.styleable.MaterialCardView_strokeWidth, 0);
        updateForeground();
        adjustContentPadding();
    }

    /* access modifiers changed from: 0000 */
    public void setStrokeColor(@ColorInt int i) {
        this.strokeColor = i;
        updateForeground();
    }

    /* access modifiers changed from: 0000 */
    @ColorInt
    public int getStrokeColor() {
        return this.strokeColor;
    }

    /* access modifiers changed from: 0000 */
    public void setStrokeWidth(@Dimension int i) {
        this.strokeWidth = i;
        updateForeground();
        adjustContentPadding();
    }

    /* access modifiers changed from: 0000 */
    @Dimension
    public int getStrokeWidth() {
        return this.strokeWidth;
    }

    /* access modifiers changed from: 0000 */
    public void updateForeground() {
        this.materialCardView.setForeground(createForegroundDrawable());
    }

    private Drawable createForegroundDrawable() {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius(this.materialCardView.getRadius());
        int i = this.strokeColor;
        if (i != -1) {
            gradientDrawable.setStroke(this.strokeWidth, i);
        }
        return gradientDrawable;
    }

    private void adjustContentPadding() {
        this.materialCardView.setContentPadding(this.materialCardView.getContentPaddingLeft() + this.strokeWidth, this.materialCardView.getContentPaddingTop() + this.strokeWidth, this.materialCardView.getContentPaddingRight() + this.strokeWidth, this.materialCardView.getContentPaddingBottom() + this.strokeWidth);
    }
}

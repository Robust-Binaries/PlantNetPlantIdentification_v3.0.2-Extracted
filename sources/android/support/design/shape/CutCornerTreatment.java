package android.support.design.shape;

import android.support.design.internal.Experimental;

@Experimental("The shapes API is currently experimental and subject to change")
public class CutCornerTreatment extends CornerTreatment {
    private final float size;

    public CutCornerTreatment(float f) {
        this.size = f;
    }

    public void getCornerPath(float f, float f2, ShapePath shapePath) {
        shapePath.reset(0.0f, this.size * f2);
        double d = (double) f;
        double sin = Math.sin(d);
        double d2 = (double) this.size;
        Double.isNaN(d2);
        double d3 = sin * d2;
        double d4 = (double) f2;
        Double.isNaN(d4);
        float f3 = (float) (d3 * d4);
        double cos = Math.cos(d);
        double d5 = (double) this.size;
        Double.isNaN(d5);
        double d6 = cos * d5;
        Double.isNaN(d4);
        shapePath.lineTo(f3, (float) (d6 * d4));
    }
}

package com.facebook.react.views.art;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import com.facebook.common.logging.FLog;
import com.facebook.react.bridge.JSApplicationIllegalArgumentException;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.common.ReactConstants;
import com.facebook.react.uimanager.annotations.ReactProp;
import javax.annotation.Nullable;

public class ARTShapeShadowNode extends ARTVirtualNode {
    private static final int CAP_BUTT = 0;
    private static final int CAP_ROUND = 1;
    private static final int CAP_SQUARE = 2;
    private static final int COLOR_TYPE_LINEAR_GRADIENT = 1;
    private static final int COLOR_TYPE_PATTERN = 3;
    private static final int COLOR_TYPE_RADIAL_GRADIENT = 2;
    private static final int COLOR_TYPE_SOLID_COLOR = 0;
    private static final int JOIN_BEVEL = 2;
    private static final int JOIN_MITER = 0;
    private static final int JOIN_ROUND = 1;
    private static final int PATH_TYPE_ARC = 4;
    private static final int PATH_TYPE_CLOSE = 1;
    private static final int PATH_TYPE_CURVETO = 3;
    private static final int PATH_TYPE_LINETO = 2;
    private static final int PATH_TYPE_MOVETO = 0;
    @Nullable
    private float[] mBrushData;
    @Nullable
    protected Path mPath;
    private int mStrokeCap = 1;
    @Nullable
    private float[] mStrokeColor;
    @Nullable
    private float[] mStrokeDash;
    private int mStrokeJoin = 1;
    private float mStrokeWidth = 1.0f;

    private float modulus(float f, float f2) {
        float f3 = f % f2;
        return f3 < 0.0f ? f3 + f2 : f3;
    }

    @ReactProp(name = "d")
    public void setShapePath(@Nullable ReadableArray readableArray) {
        this.mPath = createPath(PropHelper.toFloatArray(readableArray));
        markUpdated();
    }

    @ReactProp(name = "stroke")
    public void setStroke(@Nullable ReadableArray readableArray) {
        this.mStrokeColor = PropHelper.toFloatArray(readableArray);
        markUpdated();
    }

    @ReactProp(name = "strokeDash")
    public void setStrokeDash(@Nullable ReadableArray readableArray) {
        this.mStrokeDash = PropHelper.toFloatArray(readableArray);
        markUpdated();
    }

    @ReactProp(name = "fill")
    public void setFill(@Nullable ReadableArray readableArray) {
        this.mBrushData = PropHelper.toFloatArray(readableArray);
        markUpdated();
    }

    @ReactProp(defaultFloat = 1.0f, name = "strokeWidth")
    public void setStrokeWidth(float f) {
        this.mStrokeWidth = f;
        markUpdated();
    }

    @ReactProp(defaultInt = 1, name = "strokeCap")
    public void setStrokeCap(int i) {
        this.mStrokeCap = i;
        markUpdated();
    }

    @ReactProp(defaultInt = 1, name = "strokeJoin")
    public void setStrokeJoin(int i) {
        this.mStrokeJoin = i;
        markUpdated();
    }

    public void draw(Canvas canvas, Paint paint, float f) {
        float f2 = f * this.mOpacity;
        if (f2 > 0.01f) {
            saveAndSetupCanvas(canvas);
            if (this.mPath != null) {
                if (setupFillPaint(paint, f2)) {
                    canvas.drawPath(this.mPath, paint);
                }
                if (setupStrokePaint(paint, f2)) {
                    canvas.drawPath(this.mPath, paint);
                }
                restoreCanvas(canvas);
            } else {
                throw new JSApplicationIllegalArgumentException("Shapes should have a valid path (d) prop");
            }
        }
        markUpdateSeen();
    }

    /* access modifiers changed from: protected */
    public boolean setupStrokePaint(Paint paint, float f) {
        if (this.mStrokeWidth != 0.0f) {
            float[] fArr = this.mStrokeColor;
            if (!(fArr == null || fArr.length == 0)) {
                paint.reset();
                paint.setFlags(1);
                paint.setStyle(Style.STROKE);
                switch (this.mStrokeCap) {
                    case 0:
                        paint.setStrokeCap(Cap.BUTT);
                        break;
                    case 1:
                        paint.setStrokeCap(Cap.ROUND);
                        break;
                    case 2:
                        paint.setStrokeCap(Cap.SQUARE);
                        break;
                    default:
                        StringBuilder sb = new StringBuilder();
                        sb.append("strokeCap ");
                        sb.append(this.mStrokeCap);
                        sb.append(" unrecognized");
                        throw new JSApplicationIllegalArgumentException(sb.toString());
                }
                switch (this.mStrokeJoin) {
                    case 0:
                        paint.setStrokeJoin(Join.MITER);
                        break;
                    case 1:
                        paint.setStrokeJoin(Join.ROUND);
                        break;
                    case 2:
                        paint.setStrokeJoin(Join.BEVEL);
                        break;
                    default:
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("strokeJoin ");
                        sb2.append(this.mStrokeJoin);
                        sb2.append(" unrecognized");
                        throw new JSApplicationIllegalArgumentException(sb2.toString());
                }
                paint.setStrokeWidth(this.mStrokeWidth * this.mScale);
                float[] fArr2 = this.mStrokeColor;
                int i = (int) (fArr2.length > 3 ? fArr2[3] * f * 255.0f : f * 255.0f);
                float[] fArr3 = this.mStrokeColor;
                paint.setARGB(i, (int) (fArr3[0] * 255.0f), (int) (fArr3[1] * 255.0f), (int) (fArr3[2] * 255.0f));
                float[] fArr4 = this.mStrokeDash;
                if (fArr4 != null && fArr4.length > 0) {
                    paint.setPathEffect(new DashPathEffect(fArr4, 0.0f));
                }
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean setupFillPaint(Paint paint, float f) {
        boolean z;
        float[] fArr;
        int[] iArr;
        Paint paint2 = paint;
        float[] fArr2 = this.mBrushData;
        int i = 0;
        if (fArr2 == null || fArr2.length <= 0) {
            return false;
        }
        paint.reset();
        paint2.setFlags(1);
        paint2.setStyle(Style.FILL);
        float[] fArr3 = this.mBrushData;
        int i2 = (int) fArr3[0];
        switch (i2) {
            case 0:
                int i3 = (int) (fArr3.length > 4 ? fArr3[4] * f * 255.0f : f * 255.0f);
                float[] fArr4 = this.mBrushData;
                paint2.setARGB(i3, (int) (fArr4[1] * 255.0f), (int) (fArr4[2] * 255.0f), (int) (fArr4[3] * 255.0f));
                z = true;
                break;
            case 1:
                int i4 = 5;
                if (fArr3.length >= 5) {
                    float f2 = fArr3[1] * this.mScale;
                    float f3 = this.mBrushData[2] * this.mScale;
                    float f4 = this.mBrushData[3] * this.mScale;
                    float f5 = this.mBrushData[4] * this.mScale;
                    int length = (this.mBrushData.length - 5) / 5;
                    if (length > 0) {
                        int[] iArr2 = new int[length];
                        float[] fArr5 = new float[length];
                        while (i < length) {
                            float[] fArr6 = this.mBrushData;
                            fArr5[i] = fArr6[(length * 4) + i4 + i];
                            int i5 = (i * 4) + i4;
                            int i6 = length;
                            iArr2[i] = Color.argb((int) (fArr6[i5 + 3] * 255.0f), (int) (fArr6[i5 + 0] * 255.0f), (int) (fArr6[i5 + 1] * 255.0f), (int) (fArr6[i5 + 2] * 255.0f));
                            i++;
                            length = i6;
                            i4 = 5;
                        }
                        iArr = iArr2;
                        fArr = fArr5;
                    } else {
                        iArr = null;
                        fArr = null;
                    }
                    LinearGradient linearGradient = new LinearGradient(f2, f3, f4, f5, iArr, fArr, TileMode.CLAMP);
                    paint2.setShader(linearGradient);
                    z = true;
                    break;
                } else {
                    String str = ReactConstants.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("[ARTShapeShadowNode setupFillPaint] expects 5 elements, received ");
                    sb.append(this.mBrushData.length);
                    FLog.m88w(str, sb.toString());
                    return false;
                }
            default:
                String str2 = ReactConstants.TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("ART: Color type ");
                sb2.append(i2);
                sb2.append(" not supported!");
                FLog.m88w(str2, sb2.toString());
                z = true;
                break;
        }
        return z;
    }

    private Path createPath(float[] fArr) {
        Path path = new Path();
        path.moveTo(0.0f, 0.0f);
        int i = 0;
        while (i < fArr.length) {
            int i2 = i + 1;
            int i3 = (int) fArr[i];
            switch (i3) {
                case 0:
                    int i4 = i2 + 1;
                    int i5 = i4 + 1;
                    path.moveTo(fArr[i2] * this.mScale, fArr[i4] * this.mScale);
                    i = i5;
                    break;
                case 1:
                    path.close();
                    i = i2;
                    break;
                case 2:
                    int i6 = i2 + 1;
                    int i7 = i6 + 1;
                    path.lineTo(fArr[i2] * this.mScale, fArr[i6] * this.mScale);
                    i = i7;
                    break;
                case 3:
                    int i8 = i2 + 1;
                    int i9 = i8 + 1;
                    float f = this.mScale * fArr[i8];
                    int i10 = i9 + 1;
                    float f2 = this.mScale * fArr[i9];
                    int i11 = i10 + 1;
                    float f3 = this.mScale * fArr[i10];
                    int i12 = i11 + 1;
                    int i13 = i12 + 1;
                    path.cubicTo(fArr[i2] * this.mScale, f, f2, f3, this.mScale * fArr[i11], fArr[i12] * this.mScale);
                    i = i13;
                    break;
                case 4:
                    int i14 = i2 + 1;
                    float f4 = fArr[i2] * this.mScale;
                    int i15 = i14 + 1;
                    float f5 = fArr[i14] * this.mScale;
                    int i16 = i15 + 1;
                    float f6 = fArr[i15] * this.mScale;
                    int i17 = i16 + 1;
                    float degrees = (float) Math.toDegrees((double) fArr[i16]);
                    int i18 = i17 + 1;
                    float degrees2 = (float) Math.toDegrees((double) fArr[i17]);
                    int i19 = i18 + 1;
                    boolean z = fArr[i18] != 1.0f;
                    float f7 = degrees2 - degrees;
                    if (Math.abs(f7) >= 360.0f) {
                        path.addCircle(f4, f5, f6, z ? Direction.CCW : Direction.CW);
                    } else {
                        float modulus = modulus(f7, 360.0f);
                        if (z && modulus < 360.0f) {
                            modulus = (360.0f - modulus) * -1.0f;
                        }
                        path.arcTo(new RectF(f4 - f6, f5 - f6, f4 + f6, f5 + f6), degrees, modulus);
                    }
                    i = i19;
                    break;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized drawing instruction ");
                    sb.append(i3);
                    throw new JSApplicationIllegalArgumentException(sb.toString());
            }
        }
        return path;
    }
}

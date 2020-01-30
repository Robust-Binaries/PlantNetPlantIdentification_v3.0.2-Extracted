package com.google.maps.android.heatmaps;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.support.p000v4.util.LongSparseArray;
import com.facebook.imageutils.JfifUtil;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileProvider;
import com.google.maps.android.geometry.Bounds;
import com.google.maps.android.geometry.Point;
import com.google.maps.android.quadtree.PointQuadTree;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class HeatmapTileProvider implements TileProvider {
    public static final Gradient DEFAULT_GRADIENT = new Gradient(DEFAULT_GRADIENT_COLORS, DEFAULT_GRADIENT_START_POINTS);
    private static final int[] DEFAULT_GRADIENT_COLORS = {Color.rgb(102, JfifUtil.MARKER_APP1, 0), Color.rgb(255, 0, 0)};
    private static final float[] DEFAULT_GRADIENT_START_POINTS = {0.2f, 1.0f};
    private static final int DEFAULT_MAX_ZOOM = 11;
    private static final int DEFAULT_MIN_ZOOM = 5;
    public static final double DEFAULT_OPACITY = 0.7d;
    public static final int DEFAULT_RADIUS = 20;
    private static final int MAX_RADIUS = 50;
    private static final int MAX_ZOOM_LEVEL = 22;
    private static final int MIN_RADIUS = 10;
    private static final int SCREEN_SIZE = 1280;
    private static final int TILE_DIM = 512;
    static final double WORLD_WIDTH = 1.0d;
    private Bounds mBounds;
    private int[] mColorMap;
    private Collection<WeightedLatLng> mData;
    private Gradient mGradient;
    private double[] mKernel;
    private double[] mMaxIntensity;
    private double mOpacity;
    private int mRadius;
    private PointQuadTree<WeightedLatLng> mTree;

    public static class Builder {
        /* access modifiers changed from: private */
        public Collection<WeightedLatLng> data;
        /* access modifiers changed from: private */
        public Gradient gradient = HeatmapTileProvider.DEFAULT_GRADIENT;
        /* access modifiers changed from: private */
        public double opacity = 0.7d;
        /* access modifiers changed from: private */
        public int radius = 20;

        public Builder data(Collection<LatLng> collection) {
            return weightedData(HeatmapTileProvider.wrapData(collection));
        }

        public Builder weightedData(Collection<WeightedLatLng> collection) {
            this.data = collection;
            if (!this.data.isEmpty()) {
                return this;
            }
            throw new IllegalArgumentException("No input points.");
        }

        public Builder radius(int i) {
            this.radius = i;
            int i2 = this.radius;
            if (i2 >= 10 && i2 <= 50) {
                return this;
            }
            throw new IllegalArgumentException("Radius not within bounds.");
        }

        public Builder gradient(Gradient gradient2) {
            this.gradient = gradient2;
            return this;
        }

        public Builder opacity(double d) {
            this.opacity = d;
            double d2 = this.opacity;
            if (d2 >= 0.0d && d2 <= 1.0d) {
                return this;
            }
            throw new IllegalArgumentException("Opacity must be in range [0, 1]");
        }

        public HeatmapTileProvider build() {
            if (this.data != null) {
                return new HeatmapTileProvider(this);
            }
            throw new IllegalStateException("No input data: you must use either .data or .weightedData before building");
        }
    }

    private HeatmapTileProvider(Builder builder) {
        this.mData = builder.data;
        this.mRadius = builder.radius;
        this.mGradient = builder.gradient;
        this.mOpacity = builder.opacity;
        int i = this.mRadius;
        double d = (double) i;
        Double.isNaN(d);
        this.mKernel = generateKernel(i, d / 3.0d);
        setGradient(this.mGradient);
        setWeightedData(this.mData);
    }

    public void setWeightedData(Collection<WeightedLatLng> collection) {
        this.mData = collection;
        if (!this.mData.isEmpty()) {
            this.mBounds = getBounds(this.mData);
            this.mTree = new PointQuadTree<>(this.mBounds);
            for (WeightedLatLng add : this.mData) {
                this.mTree.add(add);
            }
            this.mMaxIntensity = getMaxIntensities(this.mRadius);
            return;
        }
        throw new IllegalArgumentException("No input points.");
    }

    public void setData(Collection<LatLng> collection) {
        setWeightedData(wrapData(collection));
    }

    /* access modifiers changed from: private */
    public static Collection<WeightedLatLng> wrapData(Collection<LatLng> collection) {
        ArrayList arrayList = new ArrayList();
        for (LatLng weightedLatLng : collection) {
            arrayList.add(new WeightedLatLng(weightedLatLng));
        }
        return arrayList;
    }

    public Tile getTile(int i, int i2, int i3) {
        double d;
        int i4 = i;
        int i5 = i2;
        double pow = 1.0d / Math.pow(2.0d, (double) i3);
        int i6 = this.mRadius;
        double d2 = (double) i6;
        Double.isNaN(d2);
        double d3 = (d2 * pow) / 512.0d;
        double d4 = (2.0d * d3) + pow;
        double d5 = (double) ((i6 * 2) + 512);
        Double.isNaN(d5);
        double d6 = d4 / d5;
        double d7 = (double) i4;
        Double.isNaN(d7);
        double d8 = (d7 * pow) - d3;
        double d9 = (double) (i4 + 1);
        Double.isNaN(d9);
        double d10 = (d9 * pow) + d3;
        double d11 = (double) i5;
        Double.isNaN(d11);
        double d12 = (d11 * pow) - d3;
        double d13 = (double) (i5 + 1);
        Double.isNaN(d13);
        double d14 = (d13 * pow) + d3;
        Collection<WeightedLatLng> arrayList = new ArrayList<>();
        if (d8 < 0.0d) {
            Bounds bounds = new Bounds(d8 + 1.0d, 1.0d, d12, d14);
            arrayList = this.mTree.search(bounds);
            d = -1.0d;
        } else if (d10 > 1.0d) {
            Bounds bounds2 = new Bounds(0.0d, d10 - 1.0d, d12, d14);
            arrayList = this.mTree.search(bounds2);
            d = 1.0d;
        } else {
            d = 0.0d;
        }
        Bounds bounds3 = new Bounds(d8, d10, d12, d14);
        Bounds bounds4 = new Bounds(this.mBounds.minX - d3, this.mBounds.maxX + d3, this.mBounds.minY - d3, this.mBounds.maxY + d3);
        if (!bounds3.intersects(bounds4)) {
            return TileProvider.NO_TILE;
        }
        Collection<WeightedLatLng> search = this.mTree.search(bounds3);
        if (search.isEmpty()) {
            return TileProvider.NO_TILE;
        }
        int i7 = this.mRadius;
        double[][] dArr = (double[][]) Array.newInstance(double.class, new int[]{(i7 * 2) + 512, (i7 * 2) + 512});
        for (WeightedLatLng weightedLatLng : search) {
            Point point = weightedLatLng.getPoint();
            int i8 = (int) ((point.f81x - d8) / d6);
            int i9 = (int) ((point.f82y - d12) / d6);
            double[] dArr2 = dArr[i8];
            dArr2[i9] = dArr2[i9] + weightedLatLng.getIntensity();
        }
        for (WeightedLatLng weightedLatLng2 : arrayList) {
            Point point2 = weightedLatLng2.getPoint();
            int i10 = (int) (((point2.f81x + d) - d8) / d6);
            int i11 = (int) ((point2.f82y - d12) / d6);
            double[] dArr3 = dArr[i10];
            dArr3[i11] = dArr3[i11] + weightedLatLng2.getIntensity();
        }
        return convertBitmap(colorize(convolve(dArr, this.mKernel), this.mColorMap, this.mMaxIntensity[i3]));
    }

    public void setGradient(Gradient gradient) {
        this.mGradient = gradient;
        this.mColorMap = gradient.generateColorMap(this.mOpacity);
    }

    public void setRadius(int i) {
        this.mRadius = i;
        int i2 = this.mRadius;
        double d = (double) i2;
        Double.isNaN(d);
        this.mKernel = generateKernel(i2, d / 3.0d);
        this.mMaxIntensity = getMaxIntensities(this.mRadius);
    }

    public void setOpacity(double d) {
        this.mOpacity = d;
        setGradient(this.mGradient);
    }

    private double[] getMaxIntensities(int i) {
        int i2;
        double[] dArr = new double[22];
        int i3 = 5;
        while (true) {
            if (i3 >= 11) {
                break;
            }
            dArr[i3] = getMaxValue(this.mData, this.mBounds, i, (int) (Math.pow(2.0d, (double) (i3 - 3)) * 1280.0d));
            if (i3 == 5) {
                for (int i4 = 0; i4 < i3; i4++) {
                    dArr[i4] = dArr[i3];
                }
            }
            i3++;
        }
        for (i2 = 11; i2 < 22; i2++) {
            dArr[i2] = dArr[10];
        }
        return dArr;
    }

    private static Tile convertBitmap(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, 100, byteArrayOutputStream);
        return new Tile(512, 512, byteArrayOutputStream.toByteArray());
    }

    static Bounds getBounds(Collection<WeightedLatLng> collection) {
        Iterator it = collection.iterator();
        WeightedLatLng weightedLatLng = (WeightedLatLng) it.next();
        double d = weightedLatLng.getPoint().f81x;
        double d2 = weightedLatLng.getPoint().f81x;
        double d3 = d;
        double d4 = d2;
        double d5 = weightedLatLng.getPoint().f82y;
        double d6 = weightedLatLng.getPoint().f82y;
        while (it.hasNext()) {
            WeightedLatLng weightedLatLng2 = (WeightedLatLng) it.next();
            double d7 = weightedLatLng2.getPoint().f81x;
            double d8 = weightedLatLng2.getPoint().f82y;
            if (d7 < d3) {
                d3 = d7;
            }
            if (d7 > d4) {
                d4 = d7;
            }
            if (d8 < d5) {
                d5 = d8;
            }
            if (d8 > d6) {
                d6 = d8;
            }
        }
        Bounds bounds = new Bounds(d3, d4, d5, d6);
        return bounds;
    }

    static double[] generateKernel(int i, double d) {
        double[] dArr = new double[((i * 2) + 1)];
        for (int i2 = -i; i2 <= i; i2++) {
            int i3 = i2 + i;
            double d2 = (double) ((-i2) * i2);
            double d3 = 2.0d * d * d;
            Double.isNaN(d2);
            dArr[i3] = Math.exp(d2 / d3);
        }
        return dArr;
    }

    static double[][] convolve(double[][] dArr, double[] dArr2) {
        double[][] dArr3 = dArr;
        double[] dArr4 = dArr2;
        double length = (double) dArr4.length;
        Double.isNaN(length);
        int floor = (int) Math.floor(length / 2.0d);
        int length2 = dArr3.length;
        int i = length2 - (floor * 2);
        int i2 = (floor + i) - 1;
        double[][] dArr5 = (double[][]) Array.newInstance(double.class, new int[]{length2, length2});
        for (int i3 = 0; i3 < length2; i3++) {
            for (int i4 = 0; i4 < length2; i4++) {
                double d = dArr3[i3][i4];
                if (d != 0.0d) {
                    int i5 = i3 + floor;
                    if (i2 < i5) {
                        i5 = i2;
                    }
                    int i6 = i5 + 1;
                    int i7 = i3 - floor;
                    for (int i8 = floor > i7 ? floor : i7; i8 < i6; i8++) {
                        double[] dArr6 = dArr5[i8];
                        dArr6[i4] = dArr6[i4] + (dArr4[i8 - i7] * d);
                    }
                }
            }
        }
        double[][] dArr7 = (double[][]) Array.newInstance(double.class, new int[]{i, i});
        for (int i9 = floor; i9 < i2 + 1; i9++) {
            for (int i10 = 0; i10 < length2; i10++) {
                double d2 = dArr5[i9][i10];
                if (d2 != 0.0d) {
                    int i11 = i10 + floor;
                    if (i2 < i11) {
                        i11 = i2;
                    }
                    int i12 = i11 + 1;
                    int i13 = i10 - floor;
                    for (int i14 = floor > i13 ? floor : i13; i14 < i12; i14++) {
                        double[] dArr8 = dArr7[i9 - floor];
                        int i15 = i14 - floor;
                        dArr8[i15] = dArr8[i15] + (dArr4[i14 - i13] * d2);
                    }
                }
            }
        }
        return dArr7;
    }

    static Bitmap colorize(double[][] dArr, int[] iArr, double d) {
        double[][] dArr2 = dArr;
        int[] iArr2 = iArr;
        int i = iArr2[iArr2.length - 1];
        double length = (double) (iArr2.length - 1);
        Double.isNaN(length);
        double d2 = length / d;
        int length2 = dArr2.length;
        int[] iArr3 = new int[(length2 * length2)];
        for (int i2 = 0; i2 < length2; i2++) {
            for (int i3 = 0; i3 < length2; i3++) {
                double d3 = dArr2[i3][i2];
                int i4 = (i2 * length2) + i3;
                int i5 = (int) (d3 * d2);
                if (d3 == 0.0d) {
                    iArr3[i4] = 0;
                } else if (i5 < iArr2.length) {
                    iArr3[i4] = iArr2[i5];
                } else {
                    iArr3[i4] = i;
                }
            }
        }
        Bitmap createBitmap = Bitmap.createBitmap(length2, length2, Config.ARGB_8888);
        createBitmap.setPixels(iArr3, 0, length2, 0, 0, length2, length2);
        return createBitmap;
    }

    static double getMaxValue(Collection<WeightedLatLng> collection, Bounds bounds, int i, int i2) {
        Bounds bounds2 = bounds;
        double d = bounds2.minX;
        double d2 = bounds2.maxX;
        double d3 = bounds2.minY;
        double d4 = d2 - d;
        double d5 = bounds2.maxY - d3;
        if (d4 <= d5) {
            d4 = d5;
        }
        double d6 = (double) (i2 / (i * 2));
        Double.isNaN(d6);
        double d7 = (double) ((int) (d6 + 0.5d));
        Double.isNaN(d7);
        double d8 = d7 / d4;
        LongSparseArray longSparseArray = new LongSparseArray();
        double d9 = 0.0d;
        for (WeightedLatLng weightedLatLng : collection) {
            int i3 = (int) ((weightedLatLng.getPoint().f82y - d3) * d8);
            long j = (long) ((int) ((weightedLatLng.getPoint().f81x - d) * d8));
            LongSparseArray longSparseArray2 = (LongSparseArray) longSparseArray.get(j);
            if (longSparseArray2 == null) {
                longSparseArray2 = new LongSparseArray();
                longSparseArray.put(j, longSparseArray2);
            }
            long j2 = (long) i3;
            Double d10 = (Double) longSparseArray2.get(j2);
            if (d10 == null) {
                d10 = Double.valueOf(0.0d);
            }
            Double valueOf = Double.valueOf(d10.doubleValue() + weightedLatLng.getIntensity());
            longSparseArray2.put(j2, valueOf);
            if (valueOf.doubleValue() > d9) {
                d9 = valueOf.doubleValue();
            }
        }
        return d9;
    }
}

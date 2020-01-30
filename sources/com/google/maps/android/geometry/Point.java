package com.google.maps.android.geometry;

public class Point {

    /* renamed from: x */
    public final double f81x;

    /* renamed from: y */
    public final double f82y;

    public Point(double d, double d2) {
        this.f81x = d;
        this.f82y = d2;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Point{x=");
        sb.append(this.f81x);
        sb.append(", y=");
        sb.append(this.f82y);
        sb.append('}');
        return sb.toString();
    }
}

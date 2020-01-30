package kotlin.random;

import kotlin.Metadata;

@Metadata(mo20294bv = {1, 0, 3}, mo20295d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\r\b\u0000\u0018\u00002\u00020\u0001B\u0017\b\u0010\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003¢\u0006\u0002\u0010\u0005B7\b\u0000\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u0012\u0006\u0010\b\u001a\u00020\u0003\u0012\u0006\u0010\t\u001a\u00020\u0003\u0012\u0006\u0010\n\u001a\u00020\u0003\u0012\u0006\u0010\u000b\u001a\u00020\u0003¢\u0006\u0002\u0010\fJ\u0010\u0010\r\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\u0003H\u0016J\b\u0010\u000f\u001a\u00020\u0003H\u0016R\u000e\u0010\u000b\u001a\u00020\u0003X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0003X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0003X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0003X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0003X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0003X\u000e¢\u0006\u0002\n\u0000¨\u0006\u0010"}, mo20296d2 = {"Lkotlin/random/XorWowRandom;", "Lkotlin/random/Random;", "seed1", "", "seed2", "(II)V", "x", "y", "z", "w", "v", "addend", "(IIIIII)V", "nextBits", "bitCount", "nextInt", "kotlin-stdlib"}, mo20297k = 1, mo20298mv = {1, 1, 13})
/* compiled from: XorWowRandom.kt */
public final class XorWowRandom extends Random {
    private int addend;

    /* renamed from: v */
    private int f117v;

    /* renamed from: w */
    private int f118w;

    /* renamed from: x */
    private int f119x;

    /* renamed from: y */
    private int f120y;

    /* renamed from: z */
    private int f121z;

    public XorWowRandom(int i, int i2, int i3, int i4, int i5, int i6) {
        this.f119x = i;
        this.f120y = i2;
        this.f121z = i3;
        this.f118w = i4;
        this.f117v = i5;
        this.addend = i6;
        if (((((this.f119x | this.f120y) | this.f121z) | this.f118w) | this.f117v) != 0) {
            for (int i7 = 0; i7 < 64; i7++) {
                nextInt();
            }
            return;
        }
        throw new IllegalArgumentException("Initial state must have at least one non-zero element.".toString());
    }

    public XorWowRandom(int i, int i2) {
        int i3 = i;
        int i4 = i2;
        this(i3, i4, 0, 0, i ^ -1, (i << 10) ^ (i2 >>> 4));
    }

    public int nextInt() {
        int i = this.f119x;
        int i2 = i ^ (i >>> 2);
        this.f119x = this.f120y;
        this.f120y = this.f121z;
        this.f121z = this.f118w;
        int i3 = this.f117v;
        this.f118w = i3;
        int i4 = ((i2 ^ (i2 << 1)) ^ i3) ^ (i3 << 4);
        this.f117v = i4;
        this.addend += 362437;
        return i4 + this.addend;
    }

    public int nextBits(int i) {
        return RandomKt.takeUpperBits(nextInt(), i);
    }
}

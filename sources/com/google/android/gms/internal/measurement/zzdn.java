package com.google.android.gms.internal.measurement;

import java.util.Arrays;
import java.util.Collection;
import java.util.RandomAccess;

final class zzdn extends zzdj<Boolean> implements zzfg<Boolean>, zzgt, RandomAccess {
    private static final zzdn zzabw;
    private int size;
    private boolean[] zzabx;

    zzdn() {
        this(new boolean[10], 0);
    }

    private zzdn(boolean[] zArr, int i) {
        this.zzabx = zArr;
        this.size = i;
    }

    /* access modifiers changed from: protected */
    public final void removeRange(int i, int i2) {
        zzka();
        if (i2 >= i) {
            boolean[] zArr = this.zzabx;
            System.arraycopy(zArr, i2, zArr, i, this.size - i2);
            this.size -= i2 - i;
            this.modCount++;
            return;
        }
        throw new IndexOutOfBoundsException("toIndex < fromIndex");
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof zzdn)) {
            return super.equals(obj);
        }
        zzdn zzdn = (zzdn) obj;
        if (this.size != zzdn.size) {
            return false;
        }
        boolean[] zArr = zzdn.zzabx;
        for (int i = 0; i < this.size; i++) {
            if (this.zzabx[i] != zArr[i]) {
                return false;
            }
        }
        return true;
    }

    public final int hashCode() {
        int i = 1;
        for (int i2 = 0; i2 < this.size; i2++) {
            i = (i * 31) + zzfb.zzo(this.zzabx[i2]);
        }
        return i;
    }

    public final int size() {
        return this.size;
    }

    public final void addBoolean(boolean z) {
        zza(this.size, z);
    }

    private final void zza(int i, boolean z) {
        zzka();
        if (i >= 0) {
            int i2 = this.size;
            if (i <= i2) {
                boolean[] zArr = this.zzabx;
                if (i2 < zArr.length) {
                    System.arraycopy(zArr, i, zArr, i + 1, i2 - i);
                } else {
                    boolean[] zArr2 = new boolean[(((i2 * 3) / 2) + 1)];
                    System.arraycopy(zArr, 0, zArr2, 0, i);
                    System.arraycopy(this.zzabx, i, zArr2, i + 1, this.size - i);
                    this.zzabx = zArr2;
                }
                this.zzabx[i] = z;
                this.size++;
                this.modCount++;
                return;
            }
        }
        throw new IndexOutOfBoundsException(zzp(i));
    }

    public final boolean addAll(Collection<? extends Boolean> collection) {
        zzka();
        zzfb.checkNotNull(collection);
        if (!(collection instanceof zzdn)) {
            return super.addAll(collection);
        }
        zzdn zzdn = (zzdn) collection;
        int i = zzdn.size;
        if (i == 0) {
            return false;
        }
        int i2 = this.size;
        if (Integer.MAX_VALUE - i2 >= i) {
            int i3 = i2 + i;
            boolean[] zArr = this.zzabx;
            if (i3 > zArr.length) {
                this.zzabx = Arrays.copyOf(zArr, i3);
            }
            System.arraycopy(zzdn.zzabx, 0, this.zzabx, this.size, zzdn.size);
            this.size = i3;
            this.modCount++;
            return true;
        }
        throw new OutOfMemoryError();
    }

    public final boolean remove(Object obj) {
        zzka();
        for (int i = 0; i < this.size; i++) {
            if (obj.equals(Boolean.valueOf(this.zzabx[i]))) {
                boolean[] zArr = this.zzabx;
                System.arraycopy(zArr, i + 1, zArr, i, (this.size - i) - 1);
                this.size--;
                this.modCount++;
                return true;
            }
        }
        return false;
    }

    private final void zzo(int i) {
        if (i < 0 || i >= this.size) {
            throw new IndexOutOfBoundsException(zzp(i));
        }
    }

    private final String zzp(int i) {
        int i2 = this.size;
        StringBuilder sb = new StringBuilder(35);
        sb.append("Index:");
        sb.append(i);
        sb.append(", Size:");
        sb.append(i2);
        return sb.toString();
    }

    public final /* synthetic */ Object set(int i, Object obj) {
        boolean booleanValue = ((Boolean) obj).booleanValue();
        zzka();
        zzo(i);
        boolean[] zArr = this.zzabx;
        boolean z = zArr[i];
        zArr[i] = booleanValue;
        return Boolean.valueOf(z);
    }

    public final /* synthetic */ Object remove(int i) {
        zzka();
        zzo(i);
        boolean[] zArr = this.zzabx;
        boolean z = zArr[i];
        int i2 = this.size;
        if (i < i2 - 1) {
            System.arraycopy(zArr, i + 1, zArr, i, (i2 - i) - 1);
        }
        this.size--;
        this.modCount++;
        return Boolean.valueOf(z);
    }

    public final /* synthetic */ void add(int i, Object obj) {
        zza(i, ((Boolean) obj).booleanValue());
    }

    public final /* synthetic */ zzfg zzq(int i) {
        if (i >= this.size) {
            return new zzdn(Arrays.copyOf(this.zzabx, i), this.size);
        }
        throw new IllegalArgumentException();
    }

    public final /* synthetic */ Object get(int i) {
        zzo(i);
        return Boolean.valueOf(this.zzabx[i]);
    }

    static {
        zzdn zzdn = new zzdn(new boolean[0], 0);
        zzabw = zzdn;
        zzdn.zzjz();
    }
}

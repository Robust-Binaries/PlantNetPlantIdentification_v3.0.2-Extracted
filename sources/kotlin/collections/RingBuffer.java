package kotlin.collections;

import java.util.Arrays;
import java.util.Iterator;
import java.util.RandomAccess;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(mo20294bv = {1, 0, 3}, mo20295d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0002\b\t\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010(\n\u0002\b\f\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\u00060\u0003j\u0002`\u0004B\r\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\u0013\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00028\u0000¢\u0006\u0002\u0010\u0016J\u0016\u0010\u0017\u001a\u00028\u00002\u0006\u0010\u0018\u001a\u00020\u0006H\u0002¢\u0006\u0002\u0010\u0019J\u0006\u0010\u001a\u001a\u00020\u001bJ\u000f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00028\u00000\u001dH\u0002J\u000e\u0010\u001e\u001a\u00020\u00142\u0006\u0010\u001f\u001a\u00020\u0006J\u0015\u0010 \u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\n0\tH\u0014¢\u0006\u0002\u0010!J'\u0010 \u001a\b\u0012\u0004\u0012\u0002H\u00010\t\"\u0004\b\u0001\u0010\u00012\f\u0010\"\u001a\b\u0012\u0004\u0012\u0002H\u00010\tH\u0014¢\u0006\u0002\u0010#J9\u0010$\u001a\u00020\u0014\"\u0004\b\u0001\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\t2\u0006\u0010\u0015\u001a\u0002H\u00012\b\b\u0002\u0010%\u001a\u00020\u00062\b\b\u0002\u0010&\u001a\u00020\u0006H\u0002¢\u0006\u0002\u0010'J\u0015\u0010(\u001a\u00020\u0006*\u00020\u00062\u0006\u0010\u001f\u001a\u00020\u0006H\bR\u0018\u0010\b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\n0\tX\u0004¢\u0006\u0004\n\u0002\u0010\u000bR\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR$\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u000e\u001a\u00020\u0006@RX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\r\"\u0004\b\u0011\u0010\u0007R\u000e\u0010\u0012\u001a\u00020\u0006X\u000e¢\u0006\u0002\n\u0000¨\u0006)"}, mo20296d2 = {"Lkotlin/collections/RingBuffer;", "T", "Lkotlin/collections/AbstractList;", "Ljava/util/RandomAccess;", "Lkotlin/collections/RandomAccess;", "capacity", "", "(I)V", "buffer", "", "", "[Ljava/lang/Object;", "getCapacity", "()I", "<set-?>", "size", "getSize", "setSize", "startIndex", "add", "", "element", "(Ljava/lang/Object;)V", "get", "index", "(I)Ljava/lang/Object;", "isFull", "", "iterator", "", "removeFirst", "n", "toArray", "()[Ljava/lang/Object;", "array", "([Ljava/lang/Object;)[Ljava/lang/Object;", "fill", "fromIndex", "toIndex", "([Ljava/lang/Object;Ljava/lang/Object;II)V", "forward", "kotlin-stdlib"}, mo20297k = 1, mo20298mv = {1, 1, 13})
/* compiled from: SlidingWindow.kt */
final class RingBuffer<T> extends AbstractList<T> implements RandomAccess {
    /* access modifiers changed from: private */
    public final Object[] buffer;
    private final int capacity;
    private int size;
    /* access modifiers changed from: private */
    public int startIndex;

    public RingBuffer(int i) {
        this.capacity = i;
        if (this.capacity >= 0) {
            this.buffer = new Object[this.capacity];
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("ring buffer capacity should not be negative but it is ");
        sb.append(this.capacity);
        throw new IllegalArgumentException(sb.toString().toString());
    }

    public final int getCapacity() {
        return this.capacity;
    }

    /* access modifiers changed from: private */
    public void setSize(int i) {
        this.size = i;
    }

    public int getSize() {
        return this.size;
    }

    public T get(int i) {
        AbstractList.Companion.checkElementIndex$kotlin_stdlib(i, size());
        return this.buffer[(this.startIndex + i) % getCapacity()];
    }

    public final boolean isFull() {
        return size() == this.capacity;
    }

    @NotNull
    public Iterator<T> iterator() {
        return new RingBuffer$iterator$1<>(this);
    }

    @NotNull
    public <T> T[] toArray(@NotNull T[] tArr) {
        Intrinsics.checkParameterIsNotNull(tArr, "array");
        if (tArr.length < size()) {
            tArr = Arrays.copyOf(tArr, size());
            Intrinsics.checkExpressionValueIsNotNull(tArr, "java.util.Arrays.copyOf(this, newSize)");
        }
        int size2 = size();
        int i = 0;
        int i2 = this.startIndex;
        int i3 = 0;
        while (i3 < size2 && i2 < this.capacity) {
            tArr[i3] = this.buffer[i2];
            i3++;
            i2++;
        }
        while (i3 < size2) {
            tArr[i3] = this.buffer[i];
            i3++;
            i++;
        }
        if (tArr.length > size()) {
            tArr[size()] = null;
        }
        if (tArr != null) {
            return tArr;
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
    }

    @NotNull
    public Object[] toArray() {
        return toArray(new Object[size()]);
    }

    public final void add(T t) {
        if (!isFull()) {
            this.buffer[(this.startIndex + size()) % getCapacity()] = t;
            setSize(size() + 1);
            return;
        }
        throw new IllegalStateException("ring buffer is full");
    }

    public final void removeFirst(int i) {
        boolean z = true;
        if (i >= 0) {
            if (i > size()) {
                z = false;
            }
            if (!z) {
                StringBuilder sb = new StringBuilder();
                sb.append("n shouldn't be greater than the buffer size: n = ");
                sb.append(i);
                sb.append(", size = ");
                sb.append(size());
                throw new IllegalArgumentException(sb.toString().toString());
            } else if (i > 0) {
                int i2 = this.startIndex;
                int capacity2 = (i2 + i) % getCapacity();
                if (i2 > capacity2) {
                    fill(this.buffer, null, i2, this.capacity);
                    fill(this.buffer, null, 0, capacity2);
                } else {
                    fill(this.buffer, null, i2, capacity2);
                }
                this.startIndex = capacity2;
                setSize(size() - i);
            }
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("n shouldn't be negative but it is ");
            sb2.append(i);
            throw new IllegalArgumentException(sb2.toString().toString());
        }
    }

    /* access modifiers changed from: private */
    public final int forward(int i, int i2) {
        return (i + i2) % getCapacity();
    }

    static /* synthetic */ void fill$default(RingBuffer ringBuffer, Object[] objArr, Object obj, int i, int i2, int i3, Object obj2) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = objArr.length;
        }
        ringBuffer.fill(objArr, obj, i, i2);
    }

    private final <T> void fill(@NotNull T[] tArr, T t, int i, int i2) {
        while (i < i2) {
            tArr[i] = t;
            i++;
        }
    }
}

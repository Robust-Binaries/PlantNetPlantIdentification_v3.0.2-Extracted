package kotlin;

import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;
import kotlin.collections.ArraysKt;
import kotlin.collections.UIntIterator;
import kotlin.jvm.internal.CollectionToArray;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMappedMarker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ExperimentalUnsignedTypes
@SinceKotlin(version = "1.3")
@Metadata(mo20294bv = {1, 0, 3}, mo20295d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u001e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0015\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0010\u0000\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0002\b@\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0001-B\u0014\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004ø\u0001\u0000¢\u0006\u0004\b\u0005\u0010\u0006B\u0014\b\u0001\u0012\u0006\u0010\u0007\u001a\u00020\bø\u0001\u0000¢\u0006\u0004\b\u0005\u0010\tJ\u001b\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0002H\u0002ø\u0001\u0000¢\u0006\u0004\b\u0011\u0010\u0012J \u0010\u0013\u001a\u00020\u000f2\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001H\u0016ø\u0001\u0000¢\u0006\u0004\b\u0015\u0010\u0016J\u0013\u0010\u0017\u001a\u00020\u000f2\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019HÖ\u0003J\u001b\u0010\u001a\u001a\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0004H\u0002ø\u0001\u0000¢\u0006\u0004\b\u001c\u0010\u001dJ\t\u0010\u001e\u001a\u00020\u0004HÖ\u0001J\u000f\u0010\u001f\u001a\u00020\u000fH\u0016¢\u0006\u0004\b \u0010!J\u0010\u0010\"\u001a\u00020#H\u0002¢\u0006\u0004\b$\u0010%J#\u0010&\u001a\u00020'2\u0006\u0010\u001b\u001a\u00020\u00042\u0006\u0010(\u001a\u00020\u0002H\u0002ø\u0001\u0000¢\u0006\u0004\b)\u0010*J\t\u0010+\u001a\u00020,HÖ\u0001R\u0014\u0010\u0003\u001a\u00020\u00048VX\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0016\u0010\u0007\u001a\u00020\b8\u0000X\u0004¢\u0006\b\n\u0000\u0012\u0004\b\f\u0010\rø\u0001\u0000\u0002\u0004\n\u0002\b\u0019¨\u0006."}, mo20296d2 = {"Lkotlin/UIntArray;", "", "Lkotlin/UInt;", "size", "", "constructor-impl", "(I)[I", "storage", "", "([I)[I", "getSize-impl", "([I)I", "storage$annotations", "()V", "contains", "", "element", "contains-WZ4Q5Ns", "([II)Z", "containsAll", "elements", "containsAll-impl", "([ILjava/util/Collection;)Z", "equals", "other", "", "get", "index", "get-impl", "([II)I", "hashCode", "isEmpty", "isEmpty-impl", "([I)Z", "iterator", "Lkotlin/collections/UIntIterator;", "iterator-impl", "([I)Lkotlin/collections/UIntIterator;", "set", "", "value", "set-VXSXFK8", "([III)V", "toString", "", "Iterator", "kotlin-stdlib"}, mo20297k = 1, mo20298mv = {1, 1, 13})
/* compiled from: UIntArray.kt */
public final class UIntArray implements Collection<UInt>, KMappedMarker {
    @NotNull
    private final int[] storage;

    @Metadata(mo20294bv = {1, 0, 3}, mo20295d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\bH\u0002J\u0010\u0010\t\u001a\u00020\nH\u0016ø\u0001\u0000¢\u0006\u0002\u0010\u000bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u000e¢\u0006\u0002\n\u0000\u0002\u0004\n\u0002\b\u0019¨\u0006\f"}, mo20296d2 = {"Lkotlin/UIntArray$Iterator;", "Lkotlin/collections/UIntIterator;", "array", "", "([I)V", "index", "", "hasNext", "", "nextUInt", "Lkotlin/UInt;", "()I", "kotlin-stdlib"}, mo20297k = 1, mo20298mv = {1, 1, 13})
    /* compiled from: UIntArray.kt */
    private static final class Iterator extends UIntIterator {
        private final int[] array;
        private int index;

        public Iterator(@NotNull int[] iArr) {
            Intrinsics.checkParameterIsNotNull(iArr, "array");
            this.array = iArr;
        }

        public boolean hasNext() {
            return this.index < this.array.length;
        }

        public int nextUInt() {
            int i = this.index;
            int[] iArr = this.array;
            if (i < iArr.length) {
                this.index = i + 1;
                return UInt.m281constructorimpl(iArr[i]);
            }
            throw new NoSuchElementException(String.valueOf(i));
        }
    }

    @NotNull
    @PublishedApi
    /* renamed from: constructor-impl reason: not valid java name */
    public static int[] m325constructorimpl(@NotNull int[] iArr) {
        Intrinsics.checkParameterIsNotNull(iArr, "storage");
        return iArr;
    }

    /* renamed from: equals-impl reason: not valid java name */
    public static boolean m328equalsimpl(int[] iArr, @Nullable Object obj) {
        return (obj instanceof UIntArray) && Intrinsics.areEqual((Object) iArr, (Object) ((UIntArray) obj).m339unboximpl());
    }

    /* renamed from: equals-impl0 reason: not valid java name */
    public static final boolean m329equalsimpl0(@NotNull int[] iArr, @NotNull int[] iArr2) {
        Intrinsics.checkParameterIsNotNull(iArr, "p1");
        Intrinsics.checkParameterIsNotNull(iArr2, "p2");
        throw null;
    }

    /* renamed from: hashCode-impl reason: not valid java name */
    public static int m332hashCodeimpl(int[] iArr) {
        if (iArr != null) {
            return Arrays.hashCode(iArr);
        }
        return 0;
    }

    @PublishedApi
    public static /* synthetic */ void storage$annotations() {
    }

    @NotNull
    /* renamed from: toString-impl reason: not valid java name */
    public static String m336toStringimpl(int[] iArr) {
        StringBuilder sb = new StringBuilder();
        sb.append("UIntArray(storage=");
        sb.append(Arrays.toString(iArr));
        sb.append(")");
        return sb.toString();
    }

    public /* synthetic */ boolean add(Object obj) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    /* renamed from: add-WZ4Q5Ns reason: not valid java name */
    public boolean m337addWZ4Q5Ns(int i) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public boolean addAll(Collection<? extends UInt> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public void clear() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    /* renamed from: contains-WZ4Q5Ns reason: not valid java name */
    public boolean m338containsWZ4Q5Ns(int i) {
        return m326containsWZ4Q5Ns(this.storage, i);
    }

    public boolean containsAll(@NotNull Collection<? extends Object> collection) {
        return m327containsAllimpl(this.storage, collection);
    }

    public boolean equals(Object obj) {
        return m328equalsimpl(this.storage, obj);
    }

    public int getSize() {
        return m331getSizeimpl(this.storage);
    }

    public int hashCode() {
        return m332hashCodeimpl(this.storage);
    }

    public boolean isEmpty() {
        return m333isEmptyimpl(this.storage);
    }

    @NotNull
    public UIntIterator iterator() {
        return m334iteratorimpl(this.storage);
    }

    public boolean remove(Object obj) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public boolean removeAll(Collection<? extends Object> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public boolean retainAll(Collection<? extends Object> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public Object[] toArray() {
        return CollectionToArray.toArray(this);
    }

    public <T> T[] toArray(T[] tArr) {
        return CollectionToArray.toArray(this, tArr);
    }

    public String toString() {
        return m336toStringimpl(this.storage);
    }

    @NotNull
    /* renamed from: unbox-impl reason: not valid java name */
    public final /* synthetic */ int[] m339unboximpl() {
        return this.storage;
    }

    public final /* bridge */ boolean contains(Object obj) {
        if (obj instanceof UInt) {
            return m338containsWZ4Q5Ns(((UInt) obj).m322unboximpl());
        }
        return false;
    }

    public final /* bridge */ int size() {
        return getSize();
    }

    @PublishedApi
    private /* synthetic */ UIntArray(@NotNull int[] iArr) {
        Intrinsics.checkParameterIsNotNull(iArr, "storage");
        this.storage = iArr;
    }

    @NotNull
    /* renamed from: constructor-impl reason: not valid java name */
    public static int[] m324constructorimpl(int i) {
        return m325constructorimpl(new int[i]);
    }

    /* renamed from: get-impl reason: not valid java name */
    public static final int m330getimpl(int[] iArr, int i) {
        return UInt.m281constructorimpl(iArr[i]);
    }

    /* renamed from: set-VXSXFK8 reason: not valid java name */
    public static final void m335setVXSXFK8(int[] iArr, int i, int i2) {
        iArr[i] = i2;
    }

    /* renamed from: getSize-impl reason: not valid java name */
    public static int m331getSizeimpl(int[] iArr) {
        return iArr.length;
    }

    @NotNull
    /* renamed from: iterator-impl reason: not valid java name */
    public static UIntIterator m334iteratorimpl(int[] iArr) {
        return new Iterator(iArr);
    }

    /* renamed from: contains-WZ4Q5Ns reason: not valid java name */
    public static boolean m326containsWZ4Q5Ns(int[] iArr, int i) {
        return ArraysKt.contains(iArr, i);
    }

    /* renamed from: containsAll-impl reason: not valid java name */
    public static boolean m327containsAllimpl(int[] iArr, @NotNull Collection<UInt> collection) {
        Intrinsics.checkParameterIsNotNull(collection, "elements");
        Iterable<UInt> iterable = collection;
        if (((Collection) iterable).isEmpty()) {
            return true;
        }
        for (UInt r0 : iterable) {
            if (!ArraysKt.contains(iArr, r0.m322unboximpl())) {
                return false;
            }
        }
        return true;
    }

    /* renamed from: isEmpty-impl reason: not valid java name */
    public static boolean m333isEmptyimpl(int[] iArr) {
        return iArr.length == 0;
    }
}

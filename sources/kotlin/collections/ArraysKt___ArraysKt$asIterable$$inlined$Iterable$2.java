package kotlin.collections;

import java.util.Iterator;
import kotlin.Metadata;
import kotlin.jvm.internal.ArrayIteratorsKt;
import kotlin.jvm.internal.markers.KMappedMarker;
import org.jetbrains.annotations.NotNull;

@Metadata(mo20294bv = {1, 0, 3}, mo20295d1 = {"\u0000\u0011\n\u0000\n\u0002\u0010\u001c\n\u0000\n\u0002\u0010(\n\u0000*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J\u000f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00028\u00000\u0003H\u0002¨\u0006\u0004¸\u0006\u0000"}, mo20296d2 = {"kotlin/collections/CollectionsKt__IterablesKt$Iterable$1", "", "iterator", "", "kotlin-stdlib"}, mo20297k = 1, mo20298mv = {1, 1, 13})
/* compiled from: Iterables.kt */
public final class ArraysKt___ArraysKt$asIterable$$inlined$Iterable$2 implements Iterable<Byte>, KMappedMarker {
    final /* synthetic */ byte[] $this_asIterable$inlined;

    public ArraysKt___ArraysKt$asIterable$$inlined$Iterable$2(byte[] bArr) {
        this.$this_asIterable$inlined = bArr;
    }

    @NotNull
    public Iterator<Byte> iterator() {
        return ArrayIteratorsKt.iterator(this.$this_asIterable$inlined);
    }
}
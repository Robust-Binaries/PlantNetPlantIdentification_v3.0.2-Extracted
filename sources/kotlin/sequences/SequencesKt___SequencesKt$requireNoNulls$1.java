package kotlin.sequences;

import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mo20294bv = {1, 0, 3}, mo20295d1 = {"\u0000\f\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\u0010\u0000\u001a\u0002H\u0001\"\b\b\u0000\u0010\u0001*\u00020\u00022\b\u0010\u0003\u001a\u0004\u0018\u0001H\u0001H\n¢\u0006\u0004\b\u0004\u0010\u0005"}, mo20296d2 = {"<anonymous>", "T", "", "it", "invoke", "(Ljava/lang/Object;)Ljava/lang/Object;"}, mo20297k = 3, mo20298mv = {1, 1, 13})
/* compiled from: _Sequences.kt */
final class SequencesKt___SequencesKt$requireNoNulls$1 extends Lambda implements Function1<T, T> {
    final /* synthetic */ Sequence $this_requireNoNulls;

    SequencesKt___SequencesKt$requireNoNulls$1(Sequence sequence) {
        this.$this_requireNoNulls = sequence;
        super(1);
    }

    @NotNull
    public final T invoke(@Nullable T t) {
        if (t != null) {
            return t;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("null element found in ");
        sb.append(this.$this_requireNoNulls);
        sb.append('.');
        throw new IllegalArgumentException(sb.toString());
    }
}

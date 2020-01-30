package kotlin.jvm.functions;

import kotlin.Function;
import kotlin.Metadata;

@Metadata(mo20294bv = {1, 0, 3}, mo20295d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\bf\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u0000*\u0006\b\u0001\u0010\u0002 \u0000*\u0006\b\u0002\u0010\u0003 \u00012\b\u0012\u0004\u0012\u0002H\u00030\u0004J\u001e\u0010\u0005\u001a\u00028\u00022\u0006\u0010\u0006\u001a\u00028\u00002\u0006\u0010\u0007\u001a\u00028\u0001H¦\u0002¢\u0006\u0002\u0010\b¨\u0006\t"}, mo20296d2 = {"Lkotlin/jvm/functions/Function2;", "P1", "P2", "R", "Lkotlin/Function;", "invoke", "p1", "p2", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", "kotlin-stdlib"}, mo20297k = 1, mo20298mv = {1, 1, 13})
/* compiled from: Functions.kt */
public interface Function2<P1, P2, R> extends Function<R> {
    R invoke(P1 p1, P2 p2);
}

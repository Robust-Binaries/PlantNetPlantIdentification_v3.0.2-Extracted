package kotlin.coroutines;

import kotlin.Metadata;
import kotlin.coroutines.CoroutineContext.Element;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

@Metadata(mo20294bv = {1, 0, 3}, mo20295d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\n¢\u0006\u0002\b\u0005"}, mo20296d2 = {"<anonymous>", "", "acc", "element", "Lkotlin/coroutines/CoroutineContext$Element;", "invoke"}, mo20297k = 3, mo20298mv = {1, 1, 13})
/* compiled from: CoroutineContextImpl.kt */
final class CombinedContext$toString$1 extends Lambda implements Function2<String, Element, String> {
    public static final CombinedContext$toString$1 INSTANCE = new CombinedContext$toString$1();

    CombinedContext$toString$1() {
        super(2);
    }

    @NotNull
    public final String invoke(@NotNull String str, @NotNull Element element) {
        Intrinsics.checkParameterIsNotNull(str, "acc");
        Intrinsics.checkParameterIsNotNull(element, "element");
        if (str.length() == 0) {
            return element.toString();
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(", ");
        sb.append(element);
        return sb.toString();
    }
}

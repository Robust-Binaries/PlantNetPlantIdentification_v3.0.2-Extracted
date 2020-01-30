package kotlin.text;

import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;

@Metadata(mo20294bv = {1, 0, 3}, mo20295d1 = {"\u0000 \n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0004\n\u0002\b\u0004\n\u0002\b\u0004\n\u0002\b\u0005\u0010\u0000\u001a\u00020\u0001\"\u0014\b\u0000\u0010\u0002\u0018\u0001*\u00020\u0003*\b\u0012\u0004\u0012\u0002H\u00020\u00042\u000e\u0010\u0005\u001a\n \u0006*\u0004\u0018\u0001H\u0002H\u0002H\n¢\u0006\u0004\b\u0007\u0010\b¨\u0006\t"}, mo20296d2 = {"<anonymous>", "", "T", "Lkotlin/text/FlagEnum;", "", "it", "kotlin.jvm.PlatformType", "invoke", "(Ljava/lang/Enum;)Z", "kotlin/text/RegexKt$fromInt$1$1"}, mo20297k = 3, mo20298mv = {1, 1, 13})
/* compiled from: Regex.kt */
public final class Regex$fromInt$$inlined$apply$lambda$1 extends Lambda implements Function1<T, Boolean> {
    final /* synthetic */ int $value$inlined;

    public Regex$fromInt$$inlined$apply$lambda$1(int i) {
        this.$value$inlined = i;
        super(1);
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        return Boolean.valueOf(invoke((T) (Enum) obj));
    }

    public final boolean invoke(T t) {
        FlagEnum flagEnum = (FlagEnum) t;
        return (this.$value$inlined & flagEnum.getMask()) == flagEnum.getValue();
    }
}

package kotlin.text;

import java.nio.charset.Charset;
import kotlin.Metadata;
import kotlin.internal.InlineOnly;
import kotlin.jvm.JvmName;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo20294bv = {1, 0, 3}, mo20295d1 = {"\u0000\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\u001a\u0011\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\b¨\u0006\u0004"}, mo20296d2 = {"charset", "Ljava/nio/charset/Charset;", "charsetName", "", "kotlin-stdlib"}, mo20297k = 2, mo20298mv = {1, 1, 13})
@JvmName(name = "CharsetsKt")
/* compiled from: Charsets.kt */
public final class CharsetsKt {
    @InlineOnly
    private static final Charset charset(String str) {
        Charset forName = Charset.forName(str);
        Intrinsics.checkExpressionValueIsNotNull(forName, "Charset.forName(charsetName)");
        return forName;
    }
}

package kotlin;

import kotlin.internal.InlineOnly;
import kotlin.jvm.functions.Function1;

@Metadata(mo20294bv = {1, 0, 3}, mo20295d1 = {"\u0000\u001a\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\u001a-\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00060\u0005H\bø\u0001\u0000¢\u0006\u0002\u0010\u0007\u001a\u001f\u0010\b\u001a\u00020\u00012\n\u0010\t\u001a\u00020\u0001\"\u00020\u0006H\bø\u0001\u0000¢\u0006\u0004\b\n\u0010\u000b\u0002\u0004\n\u0002\b\u0019¨\u0006\f"}, mo20296d2 = {"UByteArray", "Lkotlin/UByteArray;", "size", "", "init", "Lkotlin/Function1;", "Lkotlin/UByte;", "(ILkotlin/jvm/functions/Function1;)[B", "ubyteArrayOf", "elements", "ubyteArrayOf-GBYM_sE", "([B)[B", "kotlin-stdlib"}, mo20297k = 2, mo20298mv = {1, 1, 13})
/* compiled from: UByteArray.kt */
public final class UByteArrayKt {
    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    @InlineOnly
    /* renamed from: ubyteArrayOf-GBYM_sE reason: not valid java name */
    private static final byte[] m273ubyteArrayOfGBYM_sE(byte... bArr) {
        return bArr;
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    @InlineOnly
    private static final byte[] UByteArray(int i, Function1<? super Integer, UByte> function1) {
        byte[] bArr = new byte[i];
        int length = bArr.length;
        for (int i2 = 0; i2 < length; i2++) {
            bArr[i2] = ((UByte) function1.invoke(Integer.valueOf(i2))).m255unboximpl();
        }
        return UByteArray.m258constructorimpl(bArr);
    }
}

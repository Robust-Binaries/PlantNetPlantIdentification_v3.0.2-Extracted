package kotlin.text;

import kotlin.ExperimentalUnsignedTypes;
import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.UByte;
import kotlin.UInt;
import kotlin.ULong;
import kotlin.UShort;
import kotlin.UnsignedKt;
import kotlin.jvm.JvmName;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mo20294bv = {1, 0, 3}, mo20295d1 = {"\u0000,\n\u0000\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0013\u001a\u001e\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u0007ø\u0001\u0000¢\u0006\u0004\b\u0005\u0010\u0006\u001a\u001e\u0010\u0000\u001a\u00020\u0001*\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u0004H\u0007ø\u0001\u0000¢\u0006\u0004\b\b\u0010\t\u001a\u001e\u0010\u0000\u001a\u00020\u0001*\u00020\n2\u0006\u0010\u0003\u001a\u00020\u0004H\u0007ø\u0001\u0000¢\u0006\u0004\b\u000b\u0010\f\u001a\u001e\u0010\u0000\u001a\u00020\u0001*\u00020\r2\u0006\u0010\u0003\u001a\u00020\u0004H\u0007ø\u0001\u0000¢\u0006\u0004\b\u000e\u0010\u000f\u001a\u0014\u0010\u0010\u001a\u00020\u0002*\u00020\u0001H\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u0011\u001a\u001c\u0010\u0010\u001a\u00020\u0002*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u0012\u001a\u0011\u0010\u0013\u001a\u0004\u0018\u00010\u0002*\u00020\u0001H\u0007ø\u0001\u0000\u001a\u0019\u0010\u0013\u001a\u0004\u0018\u00010\u0002*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007ø\u0001\u0000\u001a\u0014\u0010\u0014\u001a\u00020\u0007*\u00020\u0001H\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u0015\u001a\u001c\u0010\u0014\u001a\u00020\u0007*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u0016\u001a\u0011\u0010\u0017\u001a\u0004\u0018\u00010\u0007*\u00020\u0001H\u0007ø\u0001\u0000\u001a\u0019\u0010\u0017\u001a\u0004\u0018\u00010\u0007*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007ø\u0001\u0000\u001a\u0014\u0010\u0018\u001a\u00020\n*\u00020\u0001H\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u0019\u001a\u001c\u0010\u0018\u001a\u00020\n*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u001a\u001a\u0011\u0010\u001b\u001a\u0004\u0018\u00010\n*\u00020\u0001H\u0007ø\u0001\u0000\u001a\u0019\u0010\u001b\u001a\u0004\u0018\u00010\n*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007ø\u0001\u0000\u001a\u0014\u0010\u001c\u001a\u00020\r*\u00020\u0001H\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u001d\u001a\u001c\u0010\u001c\u001a\u00020\r*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u001e\u001a\u0011\u0010\u001f\u001a\u0004\u0018\u00010\r*\u00020\u0001H\u0007ø\u0001\u0000\u001a\u0019\u0010\u001f\u001a\u0004\u0018\u00010\r*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007ø\u0001\u0000\u0002\u0004\n\u0002\b\u0019¨\u0006 "}, mo20296d2 = {"toString", "", "Lkotlin/UByte;", "radix", "", "toString-LxnNnR4", "(BI)Ljava/lang/String;", "Lkotlin/UInt;", "toString-V7xB4Y4", "(II)Ljava/lang/String;", "Lkotlin/ULong;", "toString-JSWoG40", "(JI)Ljava/lang/String;", "Lkotlin/UShort;", "toString-olVBNx4", "(SI)Ljava/lang/String;", "toUByte", "(Ljava/lang/String;)B", "(Ljava/lang/String;I)B", "toUByteOrNull", "toUInt", "(Ljava/lang/String;)I", "(Ljava/lang/String;I)I", "toUIntOrNull", "toULong", "(Ljava/lang/String;)J", "(Ljava/lang/String;I)J", "toULongOrNull", "toUShort", "(Ljava/lang/String;)S", "(Ljava/lang/String;I)S", "toUShortOrNull", "kotlin-stdlib"}, mo20297k = 2, mo20298mv = {1, 1, 13})
@JvmName(name = "UStringsKt")
/* compiled from: UStrings.kt */
public final class UStringsKt {
    @ExperimentalUnsignedTypes
    @NotNull
    @SinceKotlin(version = "1.3")
    /* renamed from: toString-LxnNnR4 reason: not valid java name */
    public static final String m565toStringLxnNnR4(byte b, int i) {
        String num = Integer.toString(b & UByte.MAX_VALUE, CharsKt.checkRadix(i));
        Intrinsics.checkExpressionValueIsNotNull(num, "java.lang.Integer.toStri…(this, checkRadix(radix))");
        return num;
    }

    @ExperimentalUnsignedTypes
    @NotNull
    @SinceKotlin(version = "1.3")
    /* renamed from: toString-olVBNx4 reason: not valid java name */
    public static final String m567toStringolVBNx4(short s, int i) {
        String num = Integer.toString(s & UShort.MAX_VALUE, CharsKt.checkRadix(i));
        Intrinsics.checkExpressionValueIsNotNull(num, "java.lang.Integer.toStri…(this, checkRadix(radix))");
        return num;
    }

    @ExperimentalUnsignedTypes
    @NotNull
    @SinceKotlin(version = "1.3")
    /* renamed from: toString-V7xB4Y4 reason: not valid java name */
    public static final String m566toStringV7xB4Y4(int i, int i2) {
        String l = Long.toString(((long) i) & 4294967295L, CharsKt.checkRadix(i2));
        Intrinsics.checkExpressionValueIsNotNull(l, "java.lang.Long.toString(this, checkRadix(radix))");
        return l;
    }

    @ExperimentalUnsignedTypes
    @NotNull
    @SinceKotlin(version = "1.3")
    /* renamed from: toString-JSWoG40 reason: not valid java name */
    public static final String m564toStringJSWoG40(long j, int i) {
        return UnsignedKt.ulongToString(j, CharsKt.checkRadix(i));
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    public static final byte toUByte(@NotNull String str) {
        Intrinsics.checkParameterIsNotNull(str, "receiver$0");
        UByte uByteOrNull = toUByteOrNull(str);
        if (uByteOrNull != null) {
            return uByteOrNull.m255unboximpl();
        }
        StringsKt.numberFormatError(str);
        throw null;
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    public static final byte toUByte(@NotNull String str, int i) {
        Intrinsics.checkParameterIsNotNull(str, "receiver$0");
        UByte uByteOrNull = toUByteOrNull(str, i);
        if (uByteOrNull != null) {
            return uByteOrNull.m255unboximpl();
        }
        StringsKt.numberFormatError(str);
        throw null;
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    public static final short toUShort(@NotNull String str) {
        Intrinsics.checkParameterIsNotNull(str, "receiver$0");
        UShort uShortOrNull = toUShortOrNull(str);
        if (uShortOrNull != null) {
            return uShortOrNull.m454unboximpl();
        }
        StringsKt.numberFormatError(str);
        throw null;
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    public static final short toUShort(@NotNull String str, int i) {
        Intrinsics.checkParameterIsNotNull(str, "receiver$0");
        UShort uShortOrNull = toUShortOrNull(str, i);
        if (uShortOrNull != null) {
            return uShortOrNull.m454unboximpl();
        }
        StringsKt.numberFormatError(str);
        throw null;
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    public static final int toUInt(@NotNull String str) {
        Intrinsics.checkParameterIsNotNull(str, "receiver$0");
        UInt uIntOrNull = toUIntOrNull(str);
        if (uIntOrNull != null) {
            return uIntOrNull.m322unboximpl();
        }
        StringsKt.numberFormatError(str);
        throw null;
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    public static final int toUInt(@NotNull String str, int i) {
        Intrinsics.checkParameterIsNotNull(str, "receiver$0");
        UInt uIntOrNull = toUIntOrNull(str, i);
        if (uIntOrNull != null) {
            return uIntOrNull.m322unboximpl();
        }
        StringsKt.numberFormatError(str);
        throw null;
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    public static final long toULong(@NotNull String str) {
        Intrinsics.checkParameterIsNotNull(str, "receiver$0");
        ULong uLongOrNull = toULongOrNull(str);
        if (uLongOrNull != null) {
            return uLongOrNull.m389unboximpl();
        }
        StringsKt.numberFormatError(str);
        throw null;
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    public static final long toULong(@NotNull String str, int i) {
        Intrinsics.checkParameterIsNotNull(str, "receiver$0");
        ULong uLongOrNull = toULongOrNull(str, i);
        if (uLongOrNull != null) {
            return uLongOrNull.m389unboximpl();
        }
        StringsKt.numberFormatError(str);
        throw null;
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    @Nullable
    public static final UByte toUByteOrNull(@NotNull String str) {
        Intrinsics.checkParameterIsNotNull(str, "receiver$0");
        return toUByteOrNull(str, 10);
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    @Nullable
    public static final UByte toUByteOrNull(@NotNull String str, int i) {
        Intrinsics.checkParameterIsNotNull(str, "receiver$0");
        UInt uIntOrNull = toUIntOrNull(str, i);
        if (uIntOrNull == null) {
            return null;
        }
        int r1 = uIntOrNull.m322unboximpl();
        if (UnsignedKt.uintCompare(r1, UInt.m281constructorimpl(255)) > 0) {
            return null;
        }
        return UByte.m210boximpl(UByte.m216constructorimpl((byte) r1));
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    @Nullable
    public static final UShort toUShortOrNull(@NotNull String str) {
        Intrinsics.checkParameterIsNotNull(str, "receiver$0");
        return toUShortOrNull(str, 10);
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    @Nullable
    public static final UShort toUShortOrNull(@NotNull String str, int i) {
        Intrinsics.checkParameterIsNotNull(str, "receiver$0");
        UInt uIntOrNull = toUIntOrNull(str, i);
        if (uIntOrNull == null) {
            return null;
        }
        int r1 = uIntOrNull.m322unboximpl();
        if (UnsignedKt.uintCompare(r1, UInt.m281constructorimpl(65535)) > 0) {
            return null;
        }
        return UShort.m409boximpl(UShort.m415constructorimpl((short) r1));
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    @Nullable
    public static final UInt toUIntOrNull(@NotNull String str) {
        Intrinsics.checkParameterIsNotNull(str, "receiver$0");
        return toUIntOrNull(str, 10);
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    @Nullable
    public static final UInt toUIntOrNull(@NotNull String str, int i) {
        Intrinsics.checkParameterIsNotNull(str, "receiver$0");
        CharsKt.checkRadix(i);
        int length = str.length();
        if (length == 0) {
            return null;
        }
        int i2 = 0;
        char charAt = str.charAt(0);
        int i3 = 1;
        if (charAt >= '0') {
            i3 = 0;
        } else if (length == 1 || charAt != '+') {
            return null;
        }
        int r4 = UInt.m281constructorimpl(i);
        int r2 = UnsignedKt.m473uintDivideJ1ME1BU(-1, r4);
        while (i3 < length) {
            int digitOf = CharsKt.digitOf(str.charAt(i3), i);
            if (digitOf < 0 || UnsignedKt.uintCompare(i2, r2) > 0) {
                return null;
            }
            int r3 = UInt.m281constructorimpl(i2 * r4);
            int r5 = UInt.m281constructorimpl(UInt.m281constructorimpl(digitOf) + r3);
            if (UnsignedKt.uintCompare(r5, r3) < 0) {
                return null;
            }
            i3++;
            i2 = r5;
        }
        return UInt.m275boximpl(i2);
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    @Nullable
    public static final ULong toULongOrNull(@NotNull String str) {
        Intrinsics.checkParameterIsNotNull(str, "receiver$0");
        return toULongOrNull(str, 10);
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    @Nullable
    public static final ULong toULongOrNull(@NotNull String str, int i) {
        Intrinsics.checkParameterIsNotNull(str, "receiver$0");
        CharsKt.checkRadix(i);
        int length = str.length();
        if (length == 0) {
            return null;
        }
        int i2 = 0;
        char charAt = str.charAt(0);
        if (charAt < '0') {
            if (length == 1 || charAt != '+') {
                return null;
            }
            i2 = 1;
        }
        long r5 = ((long) UInt.m281constructorimpl(i)) & 4294967295L;
        long r2 = UnsignedKt.m475ulongDivideeb3DHEI(-1, ULong.m348constructorimpl(r5));
        long j = 0;
        while (i2 < length) {
            int digitOf = CharsKt.digitOf(str.charAt(i2), i);
            if (digitOf < 0 || UnsignedKt.ulongCompare(j, r2) > 0) {
                return null;
            }
            long r9 = ULong.m348constructorimpl(j * ULong.m348constructorimpl(r5));
            long r11 = ULong.m348constructorimpl(ULong.m348constructorimpl(((long) UInt.m281constructorimpl(digitOf)) & 4294967295L) + r9);
            if (UnsignedKt.ulongCompare(r11, r9) < 0) {
                return null;
            }
            i2++;
            j = r11;
        }
        return ULong.m342boximpl(j);
    }
}

package kotlin.sequences;

import java.util.Iterator;
import kotlin.Metadata;
import kotlin.Result.Failure;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.RestrictedSuspendLambda;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mo20294bv = {1, 0, 3}, mo20295d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H@ø\u0001\u0000¢\u0006\u0004\b\u0004\u0010\u0005"}, mo20296d2 = {"<anonymous>", "", "T", "Lkotlin/sequences/SequenceScope;", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, mo20297k = 3, mo20298mv = {1, 1, 13})
@DebugMetadata(mo20877c = "kotlin/sequences/SequencesKt__SequencesKt$ifEmpty$1", mo20878f = "Sequences.kt", mo20879i = {0, 1}, mo20880l = {66, 71, 72}, mo20881m = "invokeSuspend", mo20882n = {"iterator", "iterator"}, mo20883s = {"L$0", "L$0"})
/* compiled from: Sequences.kt */
final class SequencesKt__SequencesKt$ifEmpty$1 extends RestrictedSuspendLambda implements Function2<SequenceScope<? super T>, Continuation<? super Unit>, Object> {
    final /* synthetic */ Function0 $defaultValue;
    final /* synthetic */ Sequence $this_ifEmpty;
    Object L$0;
    int label;

    /* renamed from: p$ */
    private SequenceScope f123p$;

    SequencesKt__SequencesKt$ifEmpty$1(Sequence sequence, Function0 function0, Continuation continuation) {
        this.$this_ifEmpty = sequence;
        this.$defaultValue = function0;
        super(2, continuation);
    }

    @NotNull
    public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        SequencesKt__SequencesKt$ifEmpty$1 sequencesKt__SequencesKt$ifEmpty$1 = new SequencesKt__SequencesKt$ifEmpty$1(this.$this_ifEmpty, this.$defaultValue, continuation);
        sequencesKt__SequencesKt$ifEmpty$1.f123p$ = (SequenceScope) obj;
        return sequencesKt__SequencesKt$ifEmpty$1;
    }

    public final Object invoke(Object obj, Object obj2) {
        return ((SequencesKt__SequencesKt$ifEmpty$1) create(obj, (Continuation) obj2)).invokeSuspend(Unit.INSTANCE);
    }

    @Nullable
    public final Object invokeSuspend(@NotNull Object obj) {
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                if (!(obj instanceof Failure)) {
                    SequenceScope sequenceScope = this.f123p$;
                    Iterator it = this.$this_ifEmpty.iterator();
                    if (it.hasNext()) {
                        this.L$0 = it;
                        this.label = 1;
                        if (sequenceScope.yieldAll(it, (Continuation<? super Unit>) this) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                    } else {
                        Sequence sequence = (Sequence) this.$defaultValue.invoke();
                        this.L$0 = it;
                        this.label = 2;
                        if (sequenceScope.yieldAll(sequence, (Continuation<? super Unit>) this) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                    }
                } else {
                    throw ((Failure) obj).exception;
                }
                break;
            case 1:
                Iterator it2 = (Iterator) this.L$0;
                if (obj instanceof Failure) {
                    throw ((Failure) obj).exception;
                }
                break;
            case 2:
                Iterator it3 = (Iterator) this.L$0;
                if (obj instanceof Failure) {
                    throw ((Failure) obj).exception;
                }
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        return Unit.INSTANCE;
    }
}

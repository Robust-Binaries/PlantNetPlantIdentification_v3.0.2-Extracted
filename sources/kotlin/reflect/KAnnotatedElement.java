package kotlin.reflect;

import java.lang.annotation.Annotation;
import java.util.List;
import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;

@Metadata(mo20294bv = {1, 0, 3}, mo20295d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0010\u001b\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001R\u0018\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0007"}, mo20296d2 = {"Lkotlin/reflect/KAnnotatedElement;", "", "annotations", "", "", "getAnnotations", "()Ljava/util/List;", "kotlin-stdlib"}, mo20297k = 1, mo20298mv = {1, 1, 13})
/* compiled from: KAnnotatedElement.kt */
public interface KAnnotatedElement {
    @NotNull
    List<Annotation> getAnnotations();
}

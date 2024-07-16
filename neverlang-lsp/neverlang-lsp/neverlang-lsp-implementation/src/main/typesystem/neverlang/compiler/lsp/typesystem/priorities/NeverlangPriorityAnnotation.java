package neverlang.compiler.lsp.typesystem.priorities;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import neverlang.core.typelang.annotations.TypeSystemKind;
import neverlang.core.typelang.annotations.TypeSystemKindAnnotation;

@TypeSystemKindAnnotation(TypeSystemKind.PRIORITY)
@Retention(RetentionPolicy.RUNTIME)
public @interface NeverlangPriorityAnnotation {
  NeverlangPriorityEnum value();
}

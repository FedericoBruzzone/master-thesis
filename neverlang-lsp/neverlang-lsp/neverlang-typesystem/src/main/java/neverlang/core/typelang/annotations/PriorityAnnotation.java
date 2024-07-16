package neverlang.core.typelang.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@TypeSystemKindAnnotation(TypeSystemKind.PRIORITY)
@Retention(RetentionPolicy.RUNTIME)
public @interface PriorityAnnotation {
  PriorityEnum value();
}

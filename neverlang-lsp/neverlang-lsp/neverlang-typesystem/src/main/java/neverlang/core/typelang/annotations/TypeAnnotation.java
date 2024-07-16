package neverlang.core.typelang.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@TypeSystemKindAnnotation(TypeSystemKind.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TypeAnnotation {
  TypeEnum value();
}

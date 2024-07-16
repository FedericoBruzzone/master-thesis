package neverlang.core.typelang.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@TypeSystemKindAnnotation(TypeSystemKind.SIGNATURE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SignatureAnnotation {
  SignatureEnum value();
}

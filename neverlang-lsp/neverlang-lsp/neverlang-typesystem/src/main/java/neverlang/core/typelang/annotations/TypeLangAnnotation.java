package neverlang.core.typelang.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.jetbrains.annotations.NotNull;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TypeLangAnnotation {

  @NotNull
  String keyword() default "";

  @NotNull
  TypeSystemKind kind();

  @NotNull
  String label() default "";
}

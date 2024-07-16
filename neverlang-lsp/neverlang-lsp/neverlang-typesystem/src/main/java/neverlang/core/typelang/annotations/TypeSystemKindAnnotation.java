package neverlang.core.typelang.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.jetbrains.annotations.NotNull;

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TypeSystemKindAnnotation {
  @NotNull
  TypeSystemKind value();
}

//  @TypeAnnotation(TypeEnum.A)
//  public class TypeTest {
//    public static Optional<Keyword> extractFrom(Class<?> from, TypeSystemKind kind) {
//      return Arrays.stream(from.getAnnotations()).filter(a ->
// a.annotationType().isAnnotationPresent(TypeSystemKindAnnotation.class))
//              .filter(a ->
// a.annotationType().getAnnotation(TypeSystemKindAnnotation.class).value() == kind)
//              .map(a -> {
//                try {
//                  return (Keyword) a.annotationType().getDeclaredMethod("value").invoke(a);
//                } catch (Exception e) {
//                  return null;
//                }
//              }).filter(Objects::nonNull).findFirst();
//    }
//
//    public static Optional<TypeSystemKind> getKind(Class<?> cls) {
//      return Arrays.stream(cls.getAnnotations()).filter(a ->
// a.annotationType().isAnnotationPresent(TypeSystemKindAnnotation.class))
//              .map(a ->
// a.annotationType().getAnnotation(TypeSystemKindAnnotation.class).value()).findFirst();
//    }
//  }

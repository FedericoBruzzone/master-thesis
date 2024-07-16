package neverlang.core.typelang.annotations;

// Keyword is an interface that is used to define the name of a keyword inside of a
// TypeSystemKindAnnotation
// The behavior of this interface is similiar to the behavior the keyword argument in the
// TypeLangAnnotation
public interface Keyword {
  String name();

  default String keyword() {
    return name().toLowerCase();
  }
}

package typelang.utils;

import neverlang.core.typelang.annotations.TypeLangAnnotation;
import neverlang.core.typelang.annotations.TypeSystemKind;
import typelang.TypeMapperGeneratorTest;

@TypeLangAnnotation(label = TypeMapperGeneratorTest.PRIORITY_LABEL, kind = TypeSystemKind.PRIORITY)
public enum Priority {
  MODULE,
  FILE
}

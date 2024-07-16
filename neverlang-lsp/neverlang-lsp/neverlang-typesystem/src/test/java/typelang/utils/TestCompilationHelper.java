package typelang.utils;

import neverlang.core.typelang.annotations.TypeLangAnnotation;
import neverlang.core.typelang.annotations.TypeSystemKind;
import neverlang.core.typesystem.AbstractCompilationHelper;
import neverlang.core.typesystem.Scope;
import typelang.TypeMapperGeneratorTest;

@TypeLangAnnotation(
    label = TypeMapperGeneratorTest.COMPILATION_HELPER_LABEL,
    kind = TypeSystemKind.COMPILATION_HELPER)
public class TestCompilationHelper extends AbstractCompilationHelper<String, Integer> {
  @Override
  protected Scope<String> generateRootType() {
    return null;
  }
}

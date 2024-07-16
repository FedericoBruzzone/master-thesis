package typelang.utils;

import neverlang.core.typelang.annotations.*;
import neverlang.core.typesystem.Type;
import typelang.TypeMapperGeneratorTest;

@TypeAnnotation(TypeEnum.FILE)
@TypeLangAnnotation(label = TypeMapperGeneratorTest.TYPE_LABEL, kind = TypeSystemKind.TYPE)
public class TypeTest implements Type {
  @Override
  public String id() {
    return null;
  }

  @Callback(keyword = "validateFinalState")
  public void customMethod() {}
}

package simplelang.typesystem.basetypes;

import java.util.List;
import neverlang.core.lsp.defaults.types.TypePrimitive;
import neverlang.core.typelang.annotations.TypeLangAnnotation;
import neverlang.core.typelang.annotations.TypeSystemKind;
import neverlang.core.typesystem.BaseType;
import neverlang.core.typesystem.symboltable.EntryKind;

@TypeLangAnnotation(keyword = "boolean", kind = TypeSystemKind.BASE_TYPE)
public class BooleanBaseType extends BaseType {

  @Override
  public BaseTypeParams baseTypeParams() {
    return new BaseTypeParams(
        EntryKind.DEFINE, "boolean", TypePrimitive.of(TypePrimitive.Name.BOOLEAN), List.of(), null);
  }
}

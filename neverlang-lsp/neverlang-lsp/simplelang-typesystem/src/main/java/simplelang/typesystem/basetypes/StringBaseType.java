package simplelang.typesystem.basetypes;

import java.util.List;
import neverlang.core.lsp.defaults.types.TypePrimitive;
import neverlang.core.typelang.annotations.TypeLangAnnotation;
import neverlang.core.typelang.annotations.TypeSystemKind;
import neverlang.core.typesystem.BaseType;
import neverlang.core.typesystem.symboltable.EntryKind;

@TypeLangAnnotation(keyword = "string", kind = TypeSystemKind.BASE_TYPE)
public class StringBaseType extends BaseType {
  @Override
  public BaseTypeParams baseTypeParams() {
    return new BaseTypeParams(
        EntryKind.DEFINE, "string", TypePrimitive.of(TypePrimitive.Name.STRING), List.of(), null);
  }
}

package simplelang.typesystem.basetypes;

import java.util.List;
import neverlang.core.lsp.defaults.typenv.OperatorEntry;
import neverlang.core.lsp.defaults.types.TypeFunction;
import neverlang.core.lsp.defaults.types.TypePrimitive;
import neverlang.core.typelang.annotations.TypeLangAnnotation;
import neverlang.core.typelang.annotations.TypeSystemKind;
import neverlang.core.typesystem.BaseType;
import neverlang.core.typesystem.symboltable.EntryKind;

@TypeLangAnnotation(keyword = "==", kind = TypeSystemKind.BASE_TYPE)
public class EqualsBaseType extends BaseType {
  @Override
  public BaseTypeParams baseTypeParams() {
    return new BaseTypeParams(
        EntryKind.DEFINE,
        "==",
        new TypeFunction(),
        List.of(
            TypePrimitive.of(TypePrimitive.Name.INT),
            TypePrimitive.of(TypePrimitive.Name.INT),
            TypePrimitive.of(TypePrimitive.Name.BOOLEAN)),
        new OperatorEntry());
  }
}

package neverlang.core.lsp.defaults.types;

import neverlang.core.typelang.annotations.TypeAnnotation;
import neverlang.core.typelang.annotations.TypeEnum;
import neverlang.core.typelang.annotations.TypeLangAnnotation;
import neverlang.core.typelang.annotations.TypeSystemKind;
import neverlang.core.typesystem.defaults.DefaultTypeScope;
import neverlang.core.typesystem.defaults.SingleTypeTypeBinder;
import neverlang.core.typesystem.typenv.EntryTypeBinder;

// public enum TypeEnum
// {
//    FILE,
//    FUNCTION,
//    SINGLE
// }
//
// @TypeSystemKindAnnotation(TypeSystemKind.TYPE)
// public @interface TypeAnnotation {
//    TypeEnum value();
// }
//
// @TypeAnnotation(TypeEnum.FILE)

@TypeAnnotation(TypeEnum.FILE)
@TypeLangAnnotation(keyword = "file", kind = TypeSystemKind.TYPE)
public class TypeFile extends DefaultTypeScope {
  @Override
  public String id() {
    return "file";
  }

  @Override
  public Class<? extends EntryTypeBinder> getTypeBinder() {

    //        return FunctionTypeBinder.class;
    return SingleTypeTypeBinder.class;
  }
}

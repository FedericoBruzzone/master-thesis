package neverlang.core.typesystem.defaults;

import neverlang.core.typelang.annotations.TypeLangAnnotation;
import neverlang.core.typelang.annotations.TypeSystemKind;
import neverlang.core.typesystem.ExternalVisible;
import neverlang.core.typesystem.typenv.EntryTypeBinder;

@TypeLangAnnotation(
    keyword = "file",
    label = Constants.DEFAULT_TYPES_LABEL,
    kind = TypeSystemKind.TYPE)
public class DefaultTypeFile extends DefaultTypeScope implements ExternalVisible<String> {
  @Override
  public String id() {
    return "file";
  }

  @Override
  public Class<? extends EntryTypeBinder> getTypeBinder() {
    return SingleTypeTypeBinder.class;
  }
}

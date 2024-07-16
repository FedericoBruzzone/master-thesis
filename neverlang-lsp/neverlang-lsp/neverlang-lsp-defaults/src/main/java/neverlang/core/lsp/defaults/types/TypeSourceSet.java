package neverlang.core.lsp.defaults.types;

import neverlang.core.typesystem.ExternalVisible;
import neverlang.core.typesystem.defaults.DefaultTypeScope;
import neverlang.core.typesystem.defaults.SingleTypeTypeBinder;
import neverlang.core.typesystem.typenv.EntryTypeBinder;

public class TypeSourceSet extends DefaultTypeScope implements ExternalVisible<String> {
  @Override
  public String id() {
    return "sourceSet";
  }

  @Override
  public Class<? extends EntryTypeBinder> getTypeBinder() {
    //        return FunctionTypeBinder.class;
    return SingleTypeTypeBinder.class;
  }
}

package neverlang.core.typesystem.defaults;

import neverlang.core.typesystem.ExternalVisible;
import neverlang.core.typesystem.typenv.EntryTypeBinder;

public class DefaultTypeSourceSet extends DefaultTypeScope implements ExternalVisible<String> {
  @Override
  public String id() {
    return "sourceSet";
  }

  @Override
  public Class<? extends EntryTypeBinder> getTypeBinder() {
    return SingleTypeTypeBinder.class;
  }
}

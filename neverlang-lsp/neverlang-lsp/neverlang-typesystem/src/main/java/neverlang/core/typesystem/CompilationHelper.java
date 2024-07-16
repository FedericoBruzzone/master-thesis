package neverlang.core.typesystem;

import neverlang.core.typesystem.defaults.DefaultTypeSourceSet;

public class CompilationHelper extends AbstractCompilationHelper<String, Priority> {

  @Override
  protected Scope<String> generateRootType() {
    return new DefaultTypeSourceSet();
  }
}

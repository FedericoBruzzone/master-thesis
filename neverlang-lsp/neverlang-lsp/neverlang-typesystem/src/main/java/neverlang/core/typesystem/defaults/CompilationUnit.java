package neverlang.core.typesystem.defaults;

import neverlang.core.typesystem.AbstractCompilationUnit;
import neverlang.core.typesystem.InferencingStrategy;
import neverlang.core.typesystem.Scope;
import neverlang.core.typesystem.symbols.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CompilationUnit extends AbstractCompilationUnit<String> {
  public CompilationUnit(
      @NotNull Scope<String> scope,
      @NotNull InferencingStrategy inferencingStrategy,
      @Nullable AbstractCompilationUnit<String> compilationUnitParent,
      @Nullable Location location,
      @Nullable String id) {
    super(scope, inferencingStrategy, compilationUnitParent, location, id);
  }
}

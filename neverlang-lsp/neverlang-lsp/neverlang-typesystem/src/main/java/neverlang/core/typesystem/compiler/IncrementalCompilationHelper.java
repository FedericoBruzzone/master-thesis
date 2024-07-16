package neverlang.core.typesystem.compiler;

import java.nio.file.Path;
import java.util.Set;
import neverlang.core.typesystem.AbstractCompilationHelper;
import neverlang.core.typesystem.CompilationUnitToken;
import neverlang.runtime.Context;

public interface IncrementalCompilationHelper {
  boolean isIncremental();

  void sourceToRecompile(AbstractCompilationHelper<?, ?> compilationHelper, Set<Path> pathSet);

  void beforeCompilationUnitRecompilation(
      AbstractCompilationHelper<?, ?> compilationHelper,
      Context context,
      CompilationUnitToken token);
}

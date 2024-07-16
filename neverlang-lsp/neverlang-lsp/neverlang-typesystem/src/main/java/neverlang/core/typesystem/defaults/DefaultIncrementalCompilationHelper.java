package neverlang.core.typesystem.defaults;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import neverlang.core.typesystem.*;
import neverlang.core.typesystem.compiler.Compiler;
import neverlang.core.typesystem.compiler.IncrementalCompilationHelper;
import neverlang.runtime.Context;

public class DefaultIncrementalCompilationHelper implements IncrementalCompilationHelper {
  @Override
  public boolean isIncremental() {
    return true;
  }

  @Override
  public void sourceToRecompile(
      AbstractCompilationHelper<?, ?> compilationHelper, Set<Path> pathSet) {
    var graph = compilationHelper.getGraph();

    var list = Optional.ofNullable(graph)
        .map(g -> g.clearByPathAndGetReferredCompilationUnit(pathSet))
        .orElse(List.of());
    if (graph != null) {
      list.stream()
          .map(AbstractCompilationUnit::location)
          .filter(Objects::nonNull)
          .forEach(graph.getIndexStructure()::removeByLocation);
    }

    compilationHelper
        .getRoot()
        .getTypingEnvironment()
        .removeIf(e -> e.location() != null
            && e.location().uri() != null
            && pathSet.contains(Path.of(e.location().uri())));
    recomputeCompilationUnit(compilationHelper, list);
  }

  private void recomputeCompilationUnit(
      AbstractCompilationHelper<?, ?> helper, List<AbstractCompilationUnit> list) {
    Compiler.logger.info("%d compilation unit to recompute".formatted(list.size()));
    helper.submit(new RecompileUnitEvent(list));
    list.parallelStream()
        .map(e -> helper.getTaskBuilder().withCompilationUnit(e))
        .forEach(CompilationUnitTask.Builder::registerIndependentTask);
  }

  @Override
  public void beforeCompilationUnitRecompilation(
      AbstractCompilationHelper<?, ?> helper, Context context, CompilationUnitToken token) {
    helper.clear(context.node(), token);
  }
}

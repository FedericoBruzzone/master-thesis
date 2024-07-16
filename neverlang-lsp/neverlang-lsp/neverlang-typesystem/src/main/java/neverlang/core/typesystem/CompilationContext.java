package neverlang.core.typesystem;

import java.util.Optional;
import java.util.stream.Stream;
import neverlang.core.typesystem.compiler.IncrementalCompilationHelper;
import neverlang.core.typesystem.compiler.Source;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record CompilationContext(
    @NotNull CompilationUnitToken token,
    @Nullable IncrementalCompilationHelper _ignored,
    Stream<Source> sourceStream,
    @Nullable Runnable onEnd) {
  public CompilationContext(
      CompilationUnitToken token,
      Stream<Source> sourceStream,
      IncrementalCompilationHelper _ignored) {
    this(token, _ignored, sourceStream, null);
  }

  public Optional<IncrementalCompilationHelper> incrementalCompilationHelper() {
    return Optional.ofNullable(_ignored);
  }

  public boolean incremental() {
    return incrementalCompilationHelper()
        .map(IncrementalCompilationHelper::isIncremental)
        .orElse(false);
  }
}

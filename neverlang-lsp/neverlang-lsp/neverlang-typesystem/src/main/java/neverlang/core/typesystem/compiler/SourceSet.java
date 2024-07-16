package neverlang.core.typesystem.compiler;

import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface SourceSet {

  default void updateAll(@NotNull Stream<Map.Entry<Path, SourceEventCase>> sourceStream) {
    // TODO: FIND A WAY TO MAKE THIS PARALLEL AND MORE EFFICIENT
    sourceStream.forEachOrdered(e -> update(e.getKey(), e.getValue()));
  }

  void update(@NotNull Path p, @Nullable SourceEventCase sourceEventCase);

  /**
   * Returns a copy of the source stream
   *
   * @return
   */
  Stream<Source> getClonedStream();
}

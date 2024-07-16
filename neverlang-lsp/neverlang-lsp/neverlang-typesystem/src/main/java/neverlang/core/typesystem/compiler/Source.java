package neverlang.core.typesystem.compiler;

import java.nio.file.Path;
import org.jetbrains.annotations.NotNull;

public interface Source {

  @NotNull
  Path path();

  @NotNull
  String source();

  boolean dirty();

  default boolean hasPath(Path path) {
    return path().equals(path);
  }
}

package neverlang.core.typesystem.compiler;

import java.nio.file.Path;
import java.util.Set;

public record RecompileEvent(Set<Path> pathList) {

  public boolean isResetted(Path path) {
    return pathList.contains(path);
  }
}

package neverlang.core.typesystem.defaults;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import neverlang.core.typesystem.compiler.Source;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record DefaultSource(@NotNull Path path, @Nullable String _source, @NotNull boolean dirty)
    implements Source {

  public DefaultSource(Path file) {
    // TODO: MAKE THIS MORE EFFICIENT
    this(file, null, true);
  }

  private static String readSource(Path path) {
    try {
      return Files.readString(path);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @NotNull
  @Override
  public String source() {
    return Optional.ofNullable(_source).orElseGet(() -> readSource(path));
  }
}

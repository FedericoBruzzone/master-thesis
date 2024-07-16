package neverlang.core.lsp.compiler;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import neverlang.core.typesystem.compiler.SourceEventCase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface SourceEvent {
  @Nullable
  default SourceEventCase eventCase() {
    return null;
  }

  default Path path() {
    try {
      return Path.of(new URI(uri()));
    } catch (URISyntaxException e) {
      throw new RuntimeException("Invalid URI: " + uri(), e);
    }
  }

  @NotNull
  String uri();
}

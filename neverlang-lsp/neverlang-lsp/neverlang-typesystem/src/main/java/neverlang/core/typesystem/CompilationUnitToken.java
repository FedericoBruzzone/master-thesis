package neverlang.core.typesystem;

import java.util.UUID;

public record CompilationUnitToken(String token) {
  public CompilationUnitToken() {
    this(UUID.randomUUID().toString());
  }
}

package neverlang.core.lsp.compiler;

import neverlang.core.typesystem.compiler.SourceEventCase;
import org.jetbrains.annotations.NotNull;

public record SaveSourceEvent(String uri) implements SourceEvent {
  @NotNull
  @Override
  public SourceEventCase eventCase() {
    return SourceEventCase.MODIFIED;
  }
}

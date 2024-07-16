package neverlang.core.lsp.compiler;

import neverlang.core.typesystem.compiler.SourceEventCase;
import org.jetbrains.annotations.Nullable;

public record RemoveSourceEvent(String uri) implements SourceEvent {
  @Nullable
  @Override
  public SourceEventCase eventCase() {
    return SourceEventCase.REMOVED;
  }
}

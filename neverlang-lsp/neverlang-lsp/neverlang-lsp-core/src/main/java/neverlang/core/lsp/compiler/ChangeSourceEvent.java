package neverlang.core.lsp.compiler;

import java.util.List;
import neverlang.core.typesystem.compiler.SourceEventCase;
import org.eclipse.lsp4j.TextDocumentContentChangeEvent;
import org.jetbrains.annotations.NotNull;

public record ChangeSourceEvent(String uri, List<TextDocumentContentChangeEvent> contentChanges)
    implements SourceEvent {
  @NotNull
  @Override
  public SourceEventCase eventCase() {
    return SourceEventCase.MODIFIED;
  }
}

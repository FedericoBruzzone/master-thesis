package neverlang.core.lsp.services;

import java.util.concurrent.SubmissionPublisher;
import neverlang.core.lsp.compiler.ChangeSourceEvent;
import neverlang.core.lsp.compiler.SourceEvent;
import neverlang.core.lsp.utils.Conversions;
import org.eclipse.lsp4j.DidChangeConfigurationParams;
import org.eclipse.lsp4j.DidChangeWatchedFilesParams;
import org.eclipse.lsp4j.services.WorkspaceService;

public class NeverlangLSPWorkspaceService extends NeverlangLSPService implements WorkspaceService {

  public NeverlangLSPWorkspaceService(SubmissionPublisher<SourceEvent> publisher) {
    super(publisher);
  }

  @Override
  public void didChangeConfiguration(DidChangeConfigurationParams params) {}

  @Override
  public void didChangeWatchedFiles(DidChangeWatchedFilesParams params) {
    params.getChanges().stream()
        .map(Conversions::toSourceEvent)
        // CHANGES ARE ALREADY HANDLED BY THE LSP
        .filter(e -> !(e instanceof ChangeSourceEvent))
        .forEach(this::submit);
  }
}

package neverlang.core.lsp.launcher;

import java.util.List;
import java.util.concurrent.SubmissionPublisher;
import java.util.stream.Stream;
import neverlang.core.lsp.capabilities.CapabilityBuilder;
import neverlang.core.lsp.compiler.SourceEvent;
import neverlang.core.lsp.services.NeverlangLSPDocumentService;
import neverlang.core.lsp.services.NeverlangLSPLanguageServer;
import neverlang.core.lsp.services.NeverlangLSPWorkspaceService;
import neverlang.core.lsp.services.WorkspaceBuilder;
import org.eclipse.lsp4j.services.TextDocumentService;
import org.eclipse.lsp4j.services.WorkspaceService;

public abstract class NeverlangLSPProvider {
  public final SubmissionPublisher<SourceEvent> publisher = new SubmissionPublisher<>();

  public WorkspaceService workspaceService() {
    return new NeverlangLSPWorkspaceService(publisher);
  }

  public TextDocumentService documentService() {
    return new NeverlangLSPDocumentService(publisher);
  }

  public WorkspaceBuilder workspaceBuilder() {
    return (workspace) -> Stream.empty();
  }

  public List<CapabilityBuilder> capabilities() {
    return List.of();
  }

  public NeverlangLSPLanguageServer newLanguageServer() {
    return new NeverlangLSPLanguageServer(
        documentService(), workspaceService(), workspaceBuilder(), capabilities(), publisher);
  }
}

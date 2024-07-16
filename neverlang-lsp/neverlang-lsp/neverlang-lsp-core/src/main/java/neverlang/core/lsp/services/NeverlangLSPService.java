package neverlang.core.lsp.services;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import neverlang.core.lsp.compiler.SourceEvent;
import neverlang.core.lsp.compiler.WorkspaceHandler;
import neverlang.core.typesystem.Submitter;

public abstract class NeverlangLSPService
    implements Flow.Publisher<SourceEvent>, Submitter<SourceEvent> {

  private final SubmissionPublisher<SourceEvent> publisher;
  private List<WorkspaceHandler> workspaceHandlers;

  public NeverlangLSPService(SubmissionPublisher<SourceEvent> publisher) {
    this.publisher = publisher;
  }

  @Override
  public void subscribe(Flow.Subscriber<? super SourceEvent> subscriber) {
    publisher.subscribe(subscriber);
  }

  @Override
  public void submit(SourceEvent sourceEvent) {
    publisher.submit(sourceEvent);
  }

  public void setWorkspaceHandlers(List<WorkspaceHandler> workspaceHandlers) {
    this.workspaceHandlers = workspaceHandlers;
  }

  public Optional<WorkspaceHandler> getWorkspaceHandler(Path path) {
    return Optional.ofNullable(workspaceHandlers).stream()
        .flatMap(Collection::stream)
        .filter(e -> e.canHandle(path))
        .findFirst();
  }
}

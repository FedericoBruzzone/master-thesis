package neverlang.core.lsp.services;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.SubmissionPublisher;
import neverlang.core.lsp.capabilities.*;
import neverlang.core.lsp.compiler.SourceEvent;
import neverlang.core.lsp.compiler.WorkspaceHandler;
import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.services.LanguageClient;
import org.eclipse.lsp4j.services.LanguageServer;
import org.eclipse.lsp4j.services.TextDocumentService;
import org.eclipse.lsp4j.services.WorkspaceService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NeverlangLSPLanguageServer implements LanguageServer {

  private final TextDocumentService textDocumentService;
  private final WorkspaceService workspaceService;
  private final WorkspaceBuilder workspaceBuilder;
  private final List<CapabilityBuilder> capabilityBuilder;
  private final SubmissionPublisher<SourceEvent> publisher;

  @Nullable
  private LanguageClient client;

  public NeverlangLSPLanguageServer(
      @NotNull TextDocumentService textDocumentService,
      @NotNull WorkspaceService workspaceService,
      @NotNull WorkspaceBuilder workspaceBuilder,
      @NotNull List<CapabilityBuilder> capabilityBuilders,
      @NotNull SubmissionPublisher<SourceEvent> publisher) {
    this.textDocumentService = textDocumentService;
    this.workspaceService = workspaceService;
    this.workspaceBuilder = workspaceBuilder;
    this.capabilityBuilder = capabilityBuilders;
    this.publisher = publisher;
  }

  @Override
  public CompletableFuture<InitializeResult> initialize(InitializeParams params) {
    var initializeResult = new InitializeResult(new ServerCapabilities());
    initializeResult.getCapabilities().setTextDocumentSync(TextDocumentSyncKind.Full);
    capabilityBuilder.stream()
        .map(CapabilityBuilder::build)
        .forEach(e -> e.setCapability(initializeResult.getCapabilities()));
    return CompletableFuture.supplyAsync(() -> initializeResult);
  }

  @Override
  public void initialized(InitializedParams params) {
    getClient().ifPresent(cl -> cl.workspaceFolders().thenAccept(workspaceFolders -> {
      var workspaceHandlers = workspaceFolders.stream()
          .map(Workspace::of)
          .filter(Optional::isPresent)
          .map(Optional::get)
          .flatMap(workspaceBuilder::build)
          .toList();
      if (getTextDocumentService() instanceof NeverlangLSPService neverlangLSPService) {
        neverlangLSPService.setWorkspaceHandlers(workspaceHandlers);
      }
      if (getWorkspaceService() instanceof NeverlangLSPService neverlangLSPService) {
        neverlangLSPService.setWorkspaceHandlers(workspaceHandlers);
      }
      workspaceHandlers.forEach(publisher::subscribe);
      workspaceHandlers.stream()
          .peek(workspace -> {
            try {
              var capabilities = capabilityBuilder.stream()
                  .map(CapabilityBuilder::build)
                  .peek(e -> {
                    if (e instanceof BaseCapability baseCapability) {
                      // TODO: this should be done by injection
                      baseCapability.setLanguageServer(this);
                      baseCapability.setWorkspaceHandler(workspace);
                    }
                  })
                  .toList();
              workspace.setCapabilityHolder(new CapabilitiesHolder(capabilities));
            } catch (Exception exception) {
              exception.printStackTrace();
            }
          })
          .forEach(WorkspaceHandler::initialize);
    }));
  }

  @Override
  public CompletableFuture<Object> shutdown() {
    return CompletableFuture.completedFuture(null);
  }

  @Override
  public void exit() {
    //        System.exit(0); // TODO: Uncomment this line to enable server shutdown
  }

  @NotNull
  @Override
  public TextDocumentService getTextDocumentService() {
    return textDocumentService;
  }

  @NotNull
  @Override
  public WorkspaceService getWorkspaceService() {
    return workspaceService;
  }

  public void setClient(@NotNull LanguageClient client) {
    this.client = client;
  }

  public Optional<LanguageClient> getClient() {
    return Optional.ofNullable(client);
  }

  @Override
  public void setTrace(SetTraceParams params) {
    //        LanguageServer.super.setTrace(params);
  }
}

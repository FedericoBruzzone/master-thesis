package neverlang.core.lsp.capabilities;

import neverlang.core.lsp.compiler.WorkspaceHandler;
import neverlang.core.lsp.services.NeverlangLSPLanguageServer;
import org.eclipse.lsp4j.services.LanguageClient;

public abstract class BaseCapability implements Capability {

  WorkspaceHandler workspaceHandler;
  NeverlangLSPLanguageServer languageServer;

  @Override
  public WorkspaceHandler getWorkspaceHandler() {
    return workspaceHandler;
  }

  @Override
  public NeverlangLSPLanguageServer getLanguageServer() {
    return languageServer;
  }

  public void setLanguageServer(NeverlangLSPLanguageServer languageServer) {
    this.languageServer = languageServer;
  }

  public void setWorkspaceHandler(WorkspaceHandler workspaceHandler) {
    this.workspaceHandler = workspaceHandler;
  }

  public LanguageClient getLanguageClient() {
    return languageServer.getClient().get();
  }
}

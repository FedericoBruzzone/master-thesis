package neverlang.core.lsp.capabilities;

import neverlang.core.lsp.compiler.WorkspaceHandler;
import org.eclipse.lsp4j.ServerCapabilities;
import org.eclipse.lsp4j.services.LanguageServer;

public interface Capability {
  WorkspaceHandler getWorkspaceHandler();

  LanguageServer getLanguageServer();

  void setCapability(ServerCapabilities serverCapabilities);
}

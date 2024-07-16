package neverlanglsp.astvisualizer;

import neverlang.core.lsp.launcher.NeverlangLSPProvider;
import org.eclipse.lsp4j.services.TextDocumentService;

public class NeverlangASTNodeVisualizerLSPProvider extends NeverlangLSPProvider {
  private final SimpleServer webSocketServer;

  public NeverlangASTNodeVisualizerLSPProvider(SimpleServer webSocketServer) {
    super();
    this.webSocketServer = webSocketServer;
  }

  @Override
  public TextDocumentService documentService() {
    return new NeverlangASTNodeVisualizerDocumentService(webSocketServer);
  }
}

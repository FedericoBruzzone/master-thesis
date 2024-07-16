package neverlang.core.lsp.launcher;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutionException;
import neverlang.core.lsp.services.NeverlangLSPLanguageServer;
import org.eclipse.lsp4j.launch.LSPLauncher;

public abstract class NeverlangLSPLauncher {

  private final NeverlangLSPProvider provider;

  public NeverlangLSPLauncher(NeverlangLSPProvider provider) {
    this.provider = provider;
  }

  public void startServer(InputStream inputStream, OutputStream outputStream)
      throws ExecutionException, InterruptedException {
    NeverlangLSPLanguageServer languageServer;
    try {
      languageServer = provider.newLanguageServer();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    var launcher = LSPLauncher.createServerLauncher(languageServer, inputStream, outputStream);
    // Get the client that request to launch the LS.
    var client = launcher.getRemoteProxy();
    // Set the client to language server
    languageServer.setClient(client);
    // Start the listener for JsonRPC
    launcher.startListening().get();
  }
}

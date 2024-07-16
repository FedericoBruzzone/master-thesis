package neverlanglsp.astvisualizer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import neverlang.core.lsp.launcher.NeverlangLSPSocketLauncher;

public class SocketLauncher {
  public static void main(String[] args) {
    ExecutorService executorService = Executors.newFixedThreadPool(2);
    var webSocketServer = new SimpleServer("localhost", 8888);
    var provider = new NeverlangASTNodeVisualizerLSPProvider(webSocketServer);
    var socketLauncher = new NeverlangLSPSocketLauncher(provider, 5100);
    executorService.submit(webSocketServer);
    executorService.submit(socketLauncher);
  }
}

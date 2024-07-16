package neverlang.compiler.lsp;

import neverlang.core.lsp.launcher.NeverlangLSPSocketLauncher;

public class SocketLauncher {
  public static void main(String[] args) {
    new NeverlangLSPSocketLauncher(new LSPProvider(), 5123).run();
  }
}

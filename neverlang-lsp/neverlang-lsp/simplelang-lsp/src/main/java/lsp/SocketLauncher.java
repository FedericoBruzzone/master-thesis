package lsp;

import java.util.logging.Level;
import neverlang.core.lsp.launcher.NeverlangLSPSocketLauncher;
import neverlang.core.typesystem.compiler.Compiler;

public class SocketLauncher {
  public static void main(String[] args) {
    Compiler.logger.setLevel(Level.SEVERE); // Level.INFO
    var lspProvider = new LSPProvider();
    new NeverlangLSPSocketLauncher(lspProvider, 5123).run();

    //        new NeverlangLSPSocketLauncher(lspProvider)
    //                .run_pipe();
  }
}

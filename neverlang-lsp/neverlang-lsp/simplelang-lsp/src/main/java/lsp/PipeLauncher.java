package lsp;

import java.util.logging.Level;
import neverlang.core.lsp.launcher.NeverlangLSPSocketLauncher;
import neverlang.core.typesystem.compiler.Compiler;

public class PipeLauncher {
  public static void main(String[] args) {
    Compiler.logger.setLevel(Level.SEVERE); // Level.INFO
    var lspProvider = new LSPProvider();

    new NeverlangLSPSocketLauncher(lspProvider).run_pipe();
  }
}

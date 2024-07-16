package lsp;

import neverlang.core.lsp.compiler.WorkspaceHandler;
import neverlang.core.lsp.launcher.NeverlangLSPProvider;

// TODO: implement
public interface LspModule {

  public NeverlangLSPProvider lspProvider();

  //    public SocketLauncher socketLauncher();
  public PipeLauncher pipeLauncher();

  public WorkspaceHandler workspaceHandler();

  // LanguageProvider languageProvider();
}
/*
public class Conc implements LspModule {

    @Override
    public NeverlangLSPProvider lspProvider() {
        return new LSPProvider();
    }

    @Override
    public SocketLauncher socketLauncher() {
        return null;
    }

    @Override
    public WorkspaceHandler workspaceHandler() {
        return null;
    }
}
*/

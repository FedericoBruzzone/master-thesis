package lsp;

import java.util.stream.Stream;
import neverlang.core.lsp.compiler.WorkspaceHandler;
import neverlang.core.lsp.defaults.launcher.DefaultNeverlangLSPProvider;

// NEW
public class NewLSPProvider extends DefaultNeverlangLSPProvider {
  public Stream<String> typesystems() {
    return Stream.of("simplelang.typesystem.types", "neverlang.core.lsp.defaults.types"); // "simplelang.typesystem.types" can be removed
  }

  public String langName() {
    return "SimpleLang";
  }

  public Class<? extends WorkspaceHandler> workspaceHandler() {
    return SimpleLangWorkspaceHandler.class;
  }
}

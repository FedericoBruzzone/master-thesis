package neverlang.core.lsp.defaults.launcher;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Stream;
import neverlang.core.lsp.capabilities.CapabilityBuilder;
import neverlang.core.lsp.compiler.WorkspaceHandler;
import neverlang.core.lsp.defaults.*;
import neverlang.core.lsp.launcher.NeverlangLSPProvider;
import neverlang.core.lsp.services.Workspace;
import neverlang.core.lsp.services.WorkspaceBuilder;

public class DefaultNeverlangLSPProvider extends NeverlangLSPProvider {
  @Override
  public WorkspaceBuilder workspaceBuilder() {
    try {
      Constructor<? extends WorkspaceHandler> constructor =
          workspaceHandler().getConstructor(Workspace.class);
      return (workspace) -> {
        try {
          return Stream.of(constructor.newInstance(workspace));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
          throw new RuntimeException(e);
        }
      };
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<CapabilityBuilder> capabilities() {
    return List.of(
        () -> new DefaultDocumentSymbol(typesystems()),
        () -> new DefaultSemanticToken(typesystems()),
        () -> new DefaultDiagnostic(langName()),
        DefaultGoToDefinition::new,
        DefaultReferences::new,
        DefaultFoldingRange::new,
        () -> new DefaultInlayHint(typesystems()));
  }
}

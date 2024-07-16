package lsp;

import java.util.List;
import java.util.stream.Stream;
import neverlang.core.lsp.capabilities.CapabilityBuilder;
import neverlang.core.lsp.defaults.*;
import neverlang.core.lsp.launcher.NeverlangLSPProvider;
import neverlang.core.lsp.services.WorkspaceBuilder;
import simplelang.SimpleLangModule;

public class LSPProvider extends NeverlangLSPProvider {

  private static final String typesystem = "simplelang.typesystem.types";
  private static final String typesystem2 = "neverlang.core.lsp.defaults.types";

  @Override
  public WorkspaceBuilder workspaceBuilder() {
    return (workspace) -> Stream.of(new SimpleLangWorkspaceHandler(workspace));
  }

  @Override
  public List<CapabilityBuilder> capabilities() {
    return List.of(
        () -> new DefaultDocumentSymbol(Stream.of(typesystem, typesystem2)),
        () -> new DefaultSemanticToken(Stream.of(typesystem, typesystem2)),
        () -> new DefaultDiagnostic(SimpleLangModule.LANGUAGE),
        DefaultGoToDefinition::new,
        DefaultReferences::new,
        DefaultFoldingRange::new,
        () -> new DefaultInlayHint(Stream.of(typesystem, typesystem2)));
  }
}

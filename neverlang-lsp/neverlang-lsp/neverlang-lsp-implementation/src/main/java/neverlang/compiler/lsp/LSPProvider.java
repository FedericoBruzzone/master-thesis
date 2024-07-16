package neverlang.compiler.lsp;

import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.stream.Stream;
import neverlang.compiler.lsp.handlers.GradleNeverlangWorkspaceHandler;
import neverlang.compiler.lsp.handlers.NeverlangWorkspaceHandler;
import neverlang.core.lsp.capabilities.*;
import neverlang.core.lsp.compiler.WorkspaceHandler;
import neverlang.core.lsp.defaults.*;
import neverlang.core.lsp.launcher.NeverlangLSPProvider;
import neverlang.core.lsp.services.WorkspaceBuilder;
import neverlang.core.typesystem.compiler.Compiler;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;
import org.gradle.tooling.model.GradleProject;

public class LSPProvider extends NeverlangLSPProvider {
  private static final String typesystem = "neverlang.compiler.lsp.types";
  private static final String typesystem2 = "neverlang.core.lsp.defaults.types";

  @Override
  public WorkspaceBuilder workspaceBuilder() {
    return (workspace) -> {
      var buildGradle = workspace.path().resolve("build.gradle").toFile();
      if (buildGradle.isFile()) {
        var connector = GradleConnector.newConnector();
        try (ProjectConnection connection =
            connector.forProjectDirectory(workspace.path().toFile()).connect()) {
          GradleProject rootProject = connection.getModel(GradleProject.class);
          var children = rootProject.getChildren();
          if (!children.isEmpty()) {
            return children.stream()
                .filter(e -> e.getProjectDirectory().toPath().startsWith(workspace.path()))
                .<WorkspaceHandler>map(e -> {
                  try (var subprojectConnection =
                      connector.forProjectDirectory(e.getProjectDirectory()).connect()) {
                    return GradleNeverlangWorkspaceHandler.of(subprojectConnection, e);
                  } catch (Exception ex) {
                    Compiler.logger.log(
                        Level.INFO,
                        "Ignored subproject "
                            + e.getName()
                            + " because it is not a Neverlang project");
                    return null;
                  }
                })
                .filter(Objects::nonNull);
          } else {
            return Stream.of(GradleNeverlangWorkspaceHandler.of(connection, rootProject));
          }
        } catch (Exception e) {
          Compiler.logger.log(Level.WARNING, e.getMessage(), e);
          return Stream.of(new NeverlangWorkspaceHandler(workspace));
        }
      } else {
        return Stream.of(new NeverlangWorkspaceHandler(workspace));
      }
    };
  }

  @Override
  public List<CapabilityBuilder> capabilities() {
    return List.of(
        () -> new DefaultDiagnostic(NeverlangLangLSPModule.LANGUAGE),
        () -> new DefaultSemanticToken(Stream.of(typesystem, typesystem2)),
        () -> new DefaultDocumentSymbol(Stream.of(typesystem, typesystem2)),
        DefaultReferences::new,
        DefaultGoToDefinition::new,
        DefaultFoldingRange::new,
        () -> new DefaultInlayHint(Stream.of(typesystem, typesystem2)),
        () -> new DefaultHover(Stream.of(typesystem, typesystem2)));
  }
}

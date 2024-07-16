package neverlang.core.lsp.services;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Optional;
import org.eclipse.lsp4j.WorkspaceFolder;

public record Workspace(Path path, String name) {
  public static Optional<Workspace> of(WorkspaceFolder workspaceFolder) {
    try {
      var path = Path.of(new URI(workspaceFolder.getUri()));
      return Optional.of(new Workspace(path, workspaceFolder.getName()));
    } catch (URISyntaxException uriSyntaxException) {
      return Optional.empty();
    }
  }
}

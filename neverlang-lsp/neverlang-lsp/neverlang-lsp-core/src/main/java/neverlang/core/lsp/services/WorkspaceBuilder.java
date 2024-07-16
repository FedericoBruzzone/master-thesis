package neverlang.core.lsp.services;

import java.util.stream.Stream;
import neverlang.core.lsp.compiler.WorkspaceHandler;

public interface WorkspaceBuilder {
  Stream<WorkspaceHandler> build(Workspace workspace);
}

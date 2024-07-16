package neverlang.core.lsp.capabilities;

import java.util.List;
import org.eclipse.lsp4j.RenameParams;
import org.eclipse.lsp4j.TypeHierarchyItem;
import org.eclipse.lsp4j.TypeHierarchyPrepareParams;
import org.eclipse.lsp4j.WorkspaceEdit;

public interface RenameCapability extends Capability {
  WorkspaceEdit rename(RenameParams params);

  List<TypeHierarchyItem> prepareTypeHierarchy(TypeHierarchyPrepareParams params);
}

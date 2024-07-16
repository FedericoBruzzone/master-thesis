package neverlang.core.lsp.capabilities;

import java.util.List;
import org.eclipse.lsp4j.CodeLens;
import org.eclipse.lsp4j.CodeLensParams;

public interface CodeLensCapability extends Capability {
  List<? extends CodeLens> codeLens(CodeLensParams params);

  CodeLens resolveCodeLens(CodeLens unresolved);
}

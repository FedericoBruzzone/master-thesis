package neverlang.core.lsp.capabilities;

import java.util.List;
import org.eclipse.lsp4j.InlayHint;
import org.eclipse.lsp4j.InlayHintParams;

/**
 * Inlay hints are special markers that appear in the editor and provide you with additional
 * information about your code, like the names of the parameters that a called method expects. Other
 * types of hints inform you about annotations, method parameters, usages, and so on (depending on
 * the language).
 */
public interface InlayHintCapability extends Capability {
  List<InlayHint> getInlayHints(InlayHintParams inlayHintParams);

  InlayHint resolveInlayHint(InlayHint unresolved);
}

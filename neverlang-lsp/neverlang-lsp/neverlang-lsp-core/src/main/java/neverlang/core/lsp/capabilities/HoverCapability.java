package neverlang.core.lsp.capabilities;

import org.eclipse.lsp4j.Hover;
import org.eclipse.lsp4j.HoverParams;

public interface HoverCapability extends Capability {
  Hover getHover(HoverParams hoverParams);
}

package neverlang.core.lsp.capabilities;

import java.util.List;
import org.eclipse.lsp4j.Location;
import org.eclipse.lsp4j.ReferenceParams;

public interface ReferenceCapability extends Capability {
  List<? extends Location> references(ReferenceParams params);
}

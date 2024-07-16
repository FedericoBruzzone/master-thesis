package neverlang.core.lsp.capabilities;

import java.util.List;
import org.eclipse.lsp4j.DefinitionParams;
import org.eclipse.lsp4j.Location;
import org.eclipse.lsp4j.LocationLink;
import org.eclipse.lsp4j.jsonrpc.messages.Either;

public interface DefinitionCapability extends Capability {

  Either<List<? extends Location>, List<? extends LocationLink>> getDefinition(
      DefinitionParams params);
}

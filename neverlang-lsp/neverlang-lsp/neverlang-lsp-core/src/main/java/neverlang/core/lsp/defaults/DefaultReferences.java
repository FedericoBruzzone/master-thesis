package neverlang.core.lsp.defaults;

import java.util.List;
import java.util.Objects;
import neverlang.core.lsp.capabilities.BaseCapability;
import neverlang.core.lsp.capabilities.ReferenceCapability;
import neverlang.core.lsp.utils.Conversions;
import neverlang.core.typesystem.SymbolTableEntry;
import org.eclipse.lsp4j.Location;
import org.eclipse.lsp4j.ReferenceParams;
import org.eclipse.lsp4j.ServerCapabilities;

public class DefaultReferences extends BaseCapability implements ReferenceCapability {

  @Override
  public void setCapability(ServerCapabilities serverCapabilities) {
    serverCapabilities.setReferencesProvider(true);
  }

  @Override
  public List<? extends Location> references(ReferenceParams params) {
    var workspaceHandler = getWorkspaceHandler();
    return workspaceHandler
        .<SymbolTableEntry>lookupToken(
            Conversions.extractPath(params).get(),
            params.getPosition().getLine(),
            params.getPosition().getCharacter())
        .flatMap(workspaceHandler::getReferences)
        .map(e -> e.map(Conversions::toLocation).filter(Objects::nonNull).toList())
        .orElse(List.of());
  }
}

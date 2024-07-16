package neverlang.core.lsp.defaults;

import java.util.List;
import neverlang.core.lsp.capabilities.BaseCapability;
import neverlang.core.lsp.capabilities.DefinitionCapability;
import neverlang.core.lsp.utils.Conversions;
import neverlang.core.typesystem.SymbolTableEntry;
import org.eclipse.lsp4j.DefinitionParams;
import org.eclipse.lsp4j.Location;
import org.eclipse.lsp4j.LocationLink;
import org.eclipse.lsp4j.ServerCapabilities;
import org.eclipse.lsp4j.jsonrpc.messages.Either;

public class DefaultGoToDefinition extends BaseCapability implements DefinitionCapability {

  @Override
  public void setCapability(ServerCapabilities serverCapabilities) {
    serverCapabilities.setDefinitionProvider(true);
  }

  @Override
  public Either<List<? extends Location>, List<? extends LocationLink>> getDefinition(
      DefinitionParams params) {
    var workspaceHandler = getWorkspaceHandler();
    return workspaceHandler
        .<SymbolTableEntry>lookupToken(
            Conversions.extractPath(params).get(),
            params.getPosition().getLine(),
            params.getPosition().getCharacter())
        .flatMap(workspaceHandler::getDefinition)
        .map(e -> Conversions.toLocation(e.token().location()))
        .map(List::of)
        .map(Either::<List<? extends Location>, List<? extends LocationLink>>forLeft)
        .orElse(null);
  }
}

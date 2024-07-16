package neverlang.core.lsp.capabilities;

import java.util.List;
import org.eclipse.lsp4j.DocumentSymbol;
import org.eclipse.lsp4j.DocumentSymbolParams;
import org.eclipse.lsp4j.SymbolInformation;
import org.eclipse.lsp4j.jsonrpc.messages.Either;

public interface DocumentSymbolCapability extends Capability {
  List<Either<SymbolInformation, DocumentSymbol>> getDocumentSymbol(DocumentSymbolParams params);
}

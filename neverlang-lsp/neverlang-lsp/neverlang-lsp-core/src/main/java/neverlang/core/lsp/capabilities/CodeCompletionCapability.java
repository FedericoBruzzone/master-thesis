package neverlang.core.lsp.capabilities;

import java.util.List;
import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.CompletionList;
import org.eclipse.lsp4j.CompletionParams;
import org.eclipse.lsp4j.jsonrpc.messages.Either;

public interface CodeCompletionCapability extends Capability {
  CompletionItem resolveCompletionItem(CompletionItem unresolved);

  Either<List<CompletionItem>, CompletionList> completion(CompletionParams position);
}

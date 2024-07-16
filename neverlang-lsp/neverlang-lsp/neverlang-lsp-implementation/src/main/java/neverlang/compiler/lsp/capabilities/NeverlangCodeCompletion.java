package neverlang.compiler.lsp.capabilities;

import java.util.List;
import neverlang.core.lsp.capabilities.BaseCapability;
import neverlang.core.lsp.capabilities.CodeCompletionCapability;
import neverlang.core.lsp.utils.Conversions;
import neverlang.core.typesystem.compiler.Source;
import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.jsonrpc.messages.Either;

public class NeverlangCodeCompletion extends BaseCapability implements CodeCompletionCapability {
  //    @Nullable
  //    @Override
  //    public Flow.Subscriber<Object> initializeCapability(CapabilityInitializeParams params) {
  //        params.capabilities().setCompletionProvider(new CompletionOptions(true, List.of()));
  ////        this.compilerHandler = server.getLspCompilerHandler().orElse(null);
  //        return null;
  //    }

  @Override
  public void setCapability(ServerCapabilities serverCapabilities) {
    serverCapabilities.setCompletionProvider(new CompletionOptions(true, List.of()));
  }

  @Override
  public CompletionItem resolveCompletionItem(CompletionItem unresolved) {
    return null;
  }

  @Override
  public Either<List<CompletionItem>, CompletionList> completion(CompletionParams params) {
    var txt = getWorkspaceHandler()
        .getSourceSetStream()
        .filter(e -> e.hasPath(Conversions.extractPath(params).get()))
        .findFirst()
        .map(Source::source)
        .orElse("");

    return Either.forLeft(List.of(
        createSnippet("role(${1:role name}) { ${2:semantic actions} }"),
        createSnippet("reference syntax { }")));
  }

  public CompletionItem createSnippet(String txt) {
    var completionItem = new CompletionItem(txt);
    completionItem.setKind(CompletionItemKind.Snippet);
    return completionItem;
  }
}

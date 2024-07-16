package neverlang.core.lsp.services;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.SubmissionPublisher;
import neverlang.core.lsp.capabilities.*;
import neverlang.core.lsp.compiler.*;
import neverlang.core.lsp.utils.Conversions;
import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.eclipse.lsp4j.services.TextDocumentService;

public class NeverlangLSPDocumentService extends NeverlangLSPService
    implements TextDocumentService {

  public NeverlangLSPDocumentService(SubmissionPublisher<SourceEvent> publisher) {
    super(publisher);
  }

  @Override
  public CompletableFuture<DocumentDiagnosticReport> diagnostic(DocumentDiagnosticParams params) {
    return whenYouCan(params);
  }

  @Override
  public CompletableFuture<List<Either<SymbolInformation, DocumentSymbol>>> documentSymbol(
      DocumentSymbolParams params) {
    return whenYouCan(params);
  }

  @Override
  public CompletableFuture<List<? extends CodeLens>> codeLens(CodeLensParams params) {
    return whenYouCan(params);
  }

  @Override
  public CompletableFuture<CodeLens> resolveCodeLens(CodeLens params) {
    return whenYouCan(params);
  }

  @Override
  public CompletableFuture<Either<List<? extends Location>, List<? extends LocationLink>>>
      definition(DefinitionParams params) {
    return whenYouCan(params);
  }

  @Override
  public CompletableFuture<List<? extends Location>> references(ReferenceParams params) {
    return whenYouCan(params);
  }

  @Override
  public CompletableFuture<Hover> hover(HoverParams params) {
    return whenYouCan(params);
  }

  @Override
  public CompletableFuture<List<InlayHint>> inlayHint(InlayHintParams params) {
    return whenYouCan(params);
  }

  @Override
  public CompletableFuture<InlayHint> resolveInlayHint(InlayHint params) {
    return whenYouCan(params);
  }

  @Override
  public CompletableFuture<SemanticTokens> semanticTokensFull(SemanticTokensParams params) {
    return whenYouCan(params);
  }

  @Override
  public CompletableFuture<Either<SemanticTokens, SemanticTokensDelta>> semanticTokensFullDelta(
      SemanticTokensDeltaParams params) {
    return whenYouCan(params);
  }

  @Override
  public CompletableFuture<SemanticTokens> semanticTokensRange(SemanticTokensRangeParams params) {
    return whenYouCan(params);
  }

  @Override
  public CompletableFuture<List<FoldingRange>> foldingRange(FoldingRangeRequestParams params) {
    return whenYouCan(params);
  }

  @Override
  public CompletableFuture<List<Either<Command, CodeAction>>> codeAction(CodeActionParams params) {
    return whenYouCan(params);
  }

  @Override
  public CompletableFuture<CodeAction> resolveCodeAction(CodeAction params) {
    return whenYouCan(params);
  }

  @Override
  public CompletableFuture<List<CallHierarchyIncomingCall>> callHierarchyIncomingCalls(
      CallHierarchyIncomingCallsParams params) {
    return whenYouCan(params);
  }

  @Override
  public CompletableFuture<List<CallHierarchyOutgoingCall>> callHierarchyOutgoingCalls(
      CallHierarchyOutgoingCallsParams params) {
    return whenYouCan(params);
  }

  @Override
  public CompletableFuture<List<CallHierarchyItem>> prepareCallHierarchy(
      CallHierarchyPrepareParams params) {
    return whenYouCan(params);
  }

  @Override
  public CompletableFuture<CompletionItem> resolveCompletionItem(CompletionItem params) {
    return whenYouCan(params);
  }

  @Override
  public CompletableFuture<Either<List<CompletionItem>, CompletionList>> completion(
      CompletionParams params) {
    return whenYouCan(params);
  }

  @Override
  public CompletableFuture<List<? extends TextEdit>> formatting(DocumentFormattingParams params) {
    return whenYouCan(params);
  }

  @Override
  public CompletableFuture<List<? extends TextEdit>> onTypeFormatting(
      DocumentOnTypeFormattingParams params) {
    return whenYouCan(params);
  }

  @Override
  public CompletableFuture<List<? extends TextEdit>> rangeFormatting(
      DocumentRangeFormattingParams params) {
    return whenYouCan(params);
  }

  @Override
  public CompletableFuture<WorkspaceEdit> rename(RenameParams params) {
    return whenYouCan(params);
  }

  @Override
  public CompletableFuture<List<TypeHierarchyItem>> prepareTypeHierarchy(
      TypeHierarchyPrepareParams params) {
    return whenYouCan(params);
  }

  @Override
  public CompletableFuture<SignatureHelp> signatureHelp(SignatureHelpParams params) {
    return whenYouCan(params);
  }

  public <T extends Capability, PARAMS, RETURN> CompletableFuture<RETURN> whenYouCan(
      PARAMS params) {
    return CompletableFuture.supplyAsync(() -> Conversions.extractPath(params)
        .flatMap(this::getWorkspaceHandler)
        .map(WorkspaceHandler::waitUntilIsNotCompiling)
        .map(e -> e.<RETURN>dispatch(params))
        .orElse(null));
  }

  @Override
  public void didOpen(DidOpenTextDocumentParams params) {
    submit(new OpenSourceEvent(params.getTextDocument().getUri()));
  }

  @Override
  public void didSave(DidSaveTextDocumentParams params) {
    submit(new SaveSourceEvent(params.getTextDocument().getUri()));
  }

  @Override
  public void didChange(DidChangeTextDocumentParams params) {
    submit(new ChangeSourceEvent(params.getTextDocument().getUri(), params.getContentChanges()));
  }

  @Override
  public void didClose(DidCloseTextDocumentParams params) {
    submit(new CloseSourceEvent(params.getTextDocument().getUri()));
  }
}

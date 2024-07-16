package neverlang.compiler.lsp.capabilities;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Flow;
import neverlang.core.lsp.capabilities.BaseCapability;
import neverlang.core.lsp.capabilities.CodeActionCapability;
import neverlang.core.lsp.services.NeverlangLSPLanguageServer;
import neverlang.core.lsp.utils.Conversions;
import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.jsonrpc.messages.Either;

public class NeverlangCodeAction extends BaseCapability
    implements CodeActionCapability, Flow.Subscriber<Object> {

  HashMap<Path, List<Either<Command, CodeAction>>> pathListHashMap = new HashMap<>();
  private final String languageID;
  private Flow.Subscription subscription;
  private NeverlangLSPLanguageServer server;

  public NeverlangCodeAction(String languageID) {
    this.languageID = languageID;
  }

  @Override
  public void setCapability(ServerCapabilities serverCapabilities) {
    serverCapabilities.setCodeActionProvider(true);
  }

  @Override
  public List<Either<Command, CodeAction>> codeAction(CodeActionParams params) {
    var path = Conversions.extractPath(params);
    return pathListHashMap.getOrDefault(path, List.of());
  }

  @Override
  public CodeAction resolveCodeAction(CodeAction unresolved) {
    return null;
  }

  @Override
  public void onSubscribe(Flow.Subscription subscription) {
    this.subscription = subscription;
    subscription.request(1);
  }

  @Override
  public void onNext(Object item) {
    //        if(item instanceof LogRecord logRecord){
    //            if(logRecord.getThrown() instanceof InferenceException inferenceException){
    //
    //                if(inferenceException.getInferenceResult().signature() instanceof
    // ModuleSignature){
    //                    var location = inferenceException.location();
    //                    Path root = server.getLspCompilerHandler().get().getRootDir();
    //                    var moduleName =
    // inferenceException.getInferenceResult().token().text();
    //                    var res = moduleName.replaceAll("\\.", File.separator);
    //                    //TODO: refactoring
    //                    var newPath =
    // root.resolve(Path.of("src","main","neverlang")).resolve(res
    // + ".nl");
    //                    var uri = location.toPath();
    //                    var diagnostic = new Diagnostic(
    //                            Conversions.toRange(location.range()),
    //                            logRecord.getMessage(),
    //
    // Conversions.logLevelToDiagnosticSeverity(logRecord.getLevel()),
    //                            languageID
    //                    );
    //                    var codeAction = new CodeAction("Create module");
    //                    codeAction.setKind(CodeActionKind.QuickFix);
    //                    var newText = new TextEdit();
    //                    newText.setNewText("module " + moduleName + "{\n\treference syntax {
    // }\n}");
    //                    var fileUri = newPath.toAbsolutePath().toUri().toString();
    //                    var textDocumentEdit = new TextDocumentEdit(new
    // VersionedTextDocumentIdentifier(fileUri, null), List.of(newText));
    //                    var workspaceEdit = new WorkspaceEdit();
    //                    workspaceEdit.setDocumentChanges(List.of(
    //                            Either.forRight(new CreateFile(fileUri, new
    // CreateFileOptions(false, true))),
    //                            Either.forLeft(textDocumentEdit)
    //                    ));
    //                    codeAction.setEdit(workspaceEdit);
    //                    codeAction.setDiagnostics(List.of(diagnostic));
    //                    pathListHashMap.putIfAbsent(location.toPath(), new ArrayList<>());
    //
    // pathListHashMap.get(location.toPath()).add(Either.forRight(codeAction));
    //                }
    //            }
    //        }
    //        subscription.request(1);
  }

  @Override
  public void onError(Throwable throwable) {
    subscription.request(1);
  }

  @Override
  public void onComplete() {
    subscription.cancel();
  }
}

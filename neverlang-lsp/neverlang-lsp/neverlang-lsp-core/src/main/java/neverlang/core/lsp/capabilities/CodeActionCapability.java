package neverlang.core.lsp.capabilities;

import java.util.List;
import org.eclipse.lsp4j.CodeAction;
import org.eclipse.lsp4j.CodeActionParams;
import org.eclipse.lsp4j.Command;
import org.eclipse.lsp4j.jsonrpc.messages.Either;

public interface CodeActionCapability extends Capability {
  List<Either<Command, CodeAction>> codeAction(CodeActionParams params);

  CodeAction resolveCodeAction(CodeAction unresolved);
}

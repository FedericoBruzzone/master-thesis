package neverlang.core.lsp.capabilities;

import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.jsonrpc.messages.Either;

public interface SemanticTokenCapability extends Capability {
  SemanticTokens semanticTokensFull(SemanticTokensParams params);

  Either<SemanticTokens, SemanticTokensDelta> semanticTokensFullDelta(
      SemanticTokensDeltaParams params);

  SemanticTokens semanticTokensRange(SemanticTokensRangeParams params);
}

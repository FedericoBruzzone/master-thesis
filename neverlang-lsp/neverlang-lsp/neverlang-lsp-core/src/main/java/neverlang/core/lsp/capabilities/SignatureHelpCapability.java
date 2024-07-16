package neverlang.core.lsp.capabilities;

import org.eclipse.lsp4j.SignatureHelp;
import org.eclipse.lsp4j.SignatureHelpParams;

public interface SignatureHelpCapability extends Capability {
  SignatureHelp signatureHelp(SignatureHelpParams params);
}

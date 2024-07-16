package neverlang.core.lsp.capabilities;

import org.eclipse.lsp4j.DocumentDiagnosticParams;
import org.eclipse.lsp4j.DocumentDiagnosticReport;

public interface DiagnosticCapability extends Capability {
  DocumentDiagnosticReport diagnostic(DocumentDiagnosticParams params);
}

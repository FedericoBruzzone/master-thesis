package neverlang.core.lsp.capabilities;

import neverlang.core.lsp.services.NeverlangLSPLanguageServer;
import org.eclipse.lsp4j.ServerCapabilities;

public record CapabilityInitializeParams(
    ServerCapabilities capabilities, NeverlangLSPLanguageServer server) {}

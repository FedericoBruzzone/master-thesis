package neverlang.core.lsp.defaults;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.Flow;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.stream.Collectors;
import neverlang.core.lsp.capabilities.BaseCapability;
import neverlang.core.lsp.capabilities.DiagnosticCapability;
import neverlang.core.lsp.capabilities.Subscribable;
import neverlang.core.lsp.utils.Conversions;
import neverlang.core.typesystem.AbstractCompilationUnit;
import neverlang.core.typesystem.NeverlangTypesystemException;
import neverlang.core.typesystem.RecompileUnitEvent;
import neverlang.core.typesystem.compiler.Compiler;
import neverlang.core.typesystem.compiler.RecompileEvent;
import neverlang.core.typesystem.compiler.Source;
import neverlang.core.typesystem.symbols.Range;
import neverlang.parser.ParsingException;
import org.eclipse.lsp4j.*;

public class DefaultDiagnostic extends BaseCapability
    implements DiagnosticCapability, Flow.Subscriber<Object>, Subscribable {

  private Map<Path, List<Diagnostic>> diagnostics = new HashMap<>();
  private final String languageID;

  private Flow.Subscription subscription;

  public DefaultDiagnostic(String languageID) {
    this.languageID = languageID;
  }

  @Override
  public void setCapability(ServerCapabilities serverCapabilities) {
    serverCapabilities.setDiagnosticProvider(new DiagnosticRegistrationOptions());
  }

  @Override
  public void onSubscribe(Flow.Subscription subscription) {
    this.subscription = subscription;
    subscription.request(1);
  }

  @Override
  public void onNext(Object item) {
    if (item instanceof LogRecord logRecord) {
      var objects = logRecord.getParameters();
      Diagnostic diagnostic = null;
      Path uri = null;
      if (objects != null && logRecord.getThrown() instanceof ParsingException parsingException) {
        var range = Range.fromNeverlangToken(parsingException.unexpectedSymbol());
        Source source = (Source) objects[0];
        uri = source.path();
        diagnostic = new Diagnostic(
            Conversions.toRange(range),
            logRecord.getMessage(),
            Conversions.logLevelToDiagnosticSeverity(logRecord.getLevel()),
            languageID);

      } else if (logRecord.getThrown()
          instanceof NeverlangTypesystemException neverlangTypesystemException) {
        // ANOTHER DIAGNOSTIC
        var location = neverlangTypesystemException.location();
        uri = location.toPath();
        diagnostic = new Diagnostic(
            Conversions.toRange(location.range()),
            logRecord.getMessage(),
            Conversions.logLevelToDiagnosticSeverity(logRecord.getLevel()),
            languageID);
      } else if (objects != null
          && objects.length > 0
          && objects[0] instanceof neverlang.core.typesystem.symbols.Location location) {
        // ANOTHER DIAGNOSTIC
        uri = location.toPath();
        diagnostic = new Diagnostic(
            Conversions.toRange(location.range()),
            logRecord.getMessage(),
            Conversions.logLevelToDiagnosticSeverity(logRecord.getLevel()),
            languageID);
      } else {
        Compiler.logger.log(Level.WARNING, "Unknown diagnostic: " + logRecord.getMessage());
      }
      if (uri != null) {
        diagnostics.computeIfAbsent(uri, (k) -> new ArrayList<>());
        diagnostics.get(uri).add(diagnostic);
        getLanguageClient()
            .publishDiagnostics(
                new PublishDiagnosticsParams(getUriFromPath(uri), diagnostics.get(uri)));
      }
    } else if (item instanceof RecompileEvent recompileEvent) {
      diagnostics = diagnostics.entrySet().stream()
          .map(e -> {
            var p = e.getKey();
            if (recompileEvent.isResetted(p)) {
              getLanguageClient()
                  .publishDiagnostics(new PublishDiagnosticsParams(getUriFromPath(p), List.of()));
              return null;
            } else {
              return e;
            }
          })
          .filter(Objects::nonNull)
          .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    } else if (item instanceof RecompileUnitEvent recompileUnitEvent) {
      var uriList = recompileUnitEvent.list().stream()
          .map(AbstractCompilationUnit::location)
          .filter(Objects::nonNull)
          .map(e -> {
            var uri = getUriFromPath(e.toPath());
            Optional.ofNullable(diagnostics.get(getUriFromPath(e.toPath())))
                .ifPresent(diagnosticList -> diagnosticList.removeIf(diagnostic ->
                    e.range().contains(Conversions.fromLSPRange(diagnostic.getRange()))));
            return uri;
          })
          .distinct()
          .filter(Objects::nonNull)
          .toList();
      uriList.forEach(uri -> getLanguageClient()
          .publishDiagnostics(
              new PublishDiagnosticsParams(uri, diagnostics.getOrDefault(uri, List.of()))));
    }
    subscription.request(1);
  }

  private static String getUriFromPath(Path uri) {
    return uri.toUri().toString();
  }

  @Override
  public void onError(Throwable throwable) {
    subscription.request(1);
    throwable.printStackTrace();
  }

  @Override
  public void onComplete() {}

  @Override
  public DocumentDiagnosticReport diagnostic(DocumentDiagnosticParams params) {
    return null;
  }

  @Override
  public Flow.Subscriber<Object> getSubscriber() {
    return this;
  }
}

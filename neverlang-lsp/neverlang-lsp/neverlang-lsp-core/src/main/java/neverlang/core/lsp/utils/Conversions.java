package neverlang.core.lsp.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import neverlang.core.lsp.compiler.ChangeSourceEvent;
import neverlang.core.lsp.compiler.CreateSourceEvent;
import neverlang.core.lsp.compiler.RemoveSourceEvent;
import neverlang.core.lsp.compiler.SourceEvent;
import neverlang.core.typesystem.symbols.Location;
import neverlang.core.typesystem.typenv.EntryType;
import org.eclipse.lsp4j.*;
import org.jetbrains.annotations.Nullable;

public class Conversions {

  public static Range toRange(Location location) {
    return toRange(location.range());
  }

  public static Range toRange(neverlang.core.typesystem.symbols.Range range) {
    return new Range(toPositionStart(range), toPositionEnd(range));
  }

  public static Position toPositionStart(Location location) {
    return toPositionStart(location.range());
  }

  public static Position toPositionStart(neverlang.core.typesystem.symbols.Range range) {
    return new Position(range.startRow(), range.startCol());
  }

  public static Position toPositionEnd(neverlang.core.typesystem.symbols.Range range) {
    return new Position(range.endRow(), range.endCol());
  }

  public static org.eclipse.lsp4j.Location toLocation(EntryType entryType) {
    return toLocation(entryType.token().location());
  }

  @Nullable
  public static org.eclipse.lsp4j.Location toLocation(@Nullable Location location) {
    if (location == null || location.uri() == null) {
      return null;
    }
    return new org.eclipse.lsp4j.Location(location.uri().toString(), toRange(location));
  }

  public static Optional<Path> extractPath(Object object) {
    try {
      URI uri = null;
      if (object instanceof ReferenceParams referenceParams) {
        uri = new URI(referenceParams.getTextDocument().getUri());
      } else if (object instanceof DocumentSymbolParams documentSymbolParams) {
        uri = new URI(documentSymbolParams.getTextDocument().getUri());
      } else if (object instanceof DefinitionParams params) {
        uri = new URI(params.getTextDocument().getUri());
      } else if (object instanceof FoldingRangeRequestParams params) {
        uri = new URI(params.getTextDocument().getUri());
      } else if (object instanceof SemanticTokensParams params) {
        uri = new URI(params.getTextDocument().getUri());
      } else if (object instanceof HoverParams params) {
        uri = new URI(params.getTextDocument().getUri());
      } else if (object instanceof InlayHintParams params) {
        uri = new URI(params.getTextDocument().getUri());
      } else if (object instanceof CompletionParams params) {
        uri = new URI(params.getTextDocument().getUri());
      } else if (object instanceof CodeActionParams params) {
        uri = new URI(params.getTextDocument().getUri());
      }
      return Optional.ofNullable(uri).map(Path::of);
    } catch (URISyntaxException e) {
      return Optional.empty();
    }
  }

  public static FoldingRange toFoldingRange(neverlang.core.typesystem.symbols.Range e) {
    var foldingRange = new FoldingRange(e.startRow(), e.endRow());
    foldingRange.setStartCharacter(e.startCol());
    foldingRange.setEndCharacter(e.endCol());
    foldingRange.setKind("region");
    return foldingRange;
  }

  public static SourceEvent toSourceEvent(FileEvent fileEvent) {
    return switch (fileEvent.getType()) {
      case Created -> new CreateSourceEvent(fileEvent.getUri());
      case Changed -> new ChangeSourceEvent(fileEvent.getUri(), List.of());
      case Deleted -> new RemoveSourceEvent(fileEvent.getUri());
    };
  }

  public static DiagnosticSeverity logLevelToDiagnosticSeverity(Level level) {
    return switch (level.getName()) {
      case "SEVERE" -> DiagnosticSeverity.Error;
      case "WARNING" -> DiagnosticSeverity.Warning;
      case "CONFIG" -> DiagnosticSeverity.Hint;
      default -> DiagnosticSeverity.Information;
    };
  }

  public static neverlang.core.typesystem.symbols.Range fromLSPRange(Range range) {
    return new neverlang.core.typesystem.symbols.Range(
        range.getStart().getLine(),
        range.getStart().getCharacter(),
        range.getEnd().getLine(),
        range.getEnd().getCharacter());
  }
}

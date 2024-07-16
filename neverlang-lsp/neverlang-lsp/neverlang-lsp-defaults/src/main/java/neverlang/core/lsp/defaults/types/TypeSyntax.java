package neverlang.core.lsp.defaults.types;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import neverlang.core.lsp.defaults.signatures.SyntaxSignature;
import neverlang.core.typelang.annotations.*;
import neverlang.core.typesystem.ExternalVisible;
import neverlang.core.typesystem.Signature;
import neverlang.core.typesystem.SymbolTableEntry;
import neverlang.core.typesystem.UnbindableEntryException;
import neverlang.core.typesystem.defaults.DefaultTypeScope;
import neverlang.core.typesystem.defaults.SingleTypeTypeBinder;
import neverlang.core.typesystem.typenv.EntryTypeBinder;
import org.eclipse.lsp4j.MarkupContent;
import org.eclipse.lsp4j.MarkupKind;
import org.eclipse.lsp4j.SymbolKind;

@TypeAnnotation(TypeEnum.SYNTAX)
@TypeLangAnnotation(keyword = "syntax", kind = TypeSystemKind.TYPE)
public class TypeSyntax extends DefaultTypeScope implements ExternalVisible<String> {

  AtomicInteger productionCounter = new AtomicInteger(0);

  @Override
  public String id() {
    return "syntax";
  }

  @Override
  public boolean matchSignature(Signature signature) {
    return signature instanceof SyntaxSignature;
  }

  @Override
  public Class<? extends EntryTypeBinder> getTypeBinder() {
    return SingleTypeTypeBinder.class;
  }

  @Override
  public void bindTypeToIdentifier(String variable, SymbolTableEntry entry)
      throws UnbindableEntryException {
    if (entry.type() instanceof TypeProduction typeProduction) {
      typeProduction.initCounter(productionCounter);
      super.bindTypeToIdentifier("$%d".formatted(productionCounter.get()), entry);
      if (!variable.equals("PRODUCTION")) {
        super.bindTypeToIdentifier(variable, entry);
      }
    }
  }

  @DocumentSymbol
  public SymbolKind documentSymbol(SymbolTableEntry entry) {
    return switch (entry.entryKind()) {
      case DEFINE, IMPORT -> SymbolKind.Method;
      default -> null;
    };
  }

  public MarkupContent getMarkup() {
    var list = streamAllEntries()
        .map(Map.Entry::getValue)
        .distinct()
        .filter(e -> e.type() instanceof TypeProduction)
        .toList();
    var str = list.stream()
        .map(e -> e.<TypeProduction>type().getMarkup(null))
        .collect(Collectors.joining("\n"));
    return new MarkupContent(MarkupKind.MARKDOWN, str);
  }
}

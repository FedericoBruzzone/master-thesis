package neverlang.core.lsp.defaults.types;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import neverlang.core.lsp.defaults.signatures.ProductionSignature;
import neverlang.core.typelang.annotations.*;
import neverlang.core.typesystem.ExternalVisible;
import neverlang.core.typesystem.Signature;
import neverlang.core.typesystem.SymbolTableEntry;
import neverlang.core.typesystem.UnbindableEntryException;
import neverlang.core.typesystem.defaults.DefaultTypeScope;
import neverlang.core.typesystem.defaults.SingleTypeTypeBinder;
import neverlang.core.typesystem.typenv.EntryType;
import neverlang.core.typesystem.typenv.EntryTypeBinder;
import org.eclipse.lsp4j.SemanticTokenTypes;
import org.eclipse.lsp4j.SymbolKind;

@TypeAnnotation(TypeEnum.PRODUCTION)
@TypeLangAnnotation(keyword = "production", kind = TypeSystemKind.TYPE)
public class TypeProduction extends DefaultTypeScope implements ExternalVisible<String> {

  String name = "";
  AtomicInteger nonTerminalCounter;
  AtomicInteger terminalCounter = new AtomicInteger(0);

  List<SymbolTableEntry> symbolsList = new ArrayList<>();

  @Override
  public String id() {
    return "production";
  }

  public TypeProduction withName(String name) {
    this.name = name;
    return this;
  }

  @Override
  public Class<? extends EntryTypeBinder> getTypeBinder() {
    return SingleTypeTypeBinder.class;
  }

  public void initCounter(AtomicInteger nonTerminalCounter) {
    this.nonTerminalCounter = nonTerminalCounter;
  }

  @Override
  public boolean matchSignature(Signature signature) {
    return signature instanceof ProductionSignature;
  }

  @Override
  public void bindTypeToIdentifier(String variable, SymbolTableEntry entry)
      throws UnbindableEntryException {
    Integer counter = null;

    if (entry.type() instanceof TypeTerminal || entry.type() instanceof TypeRegex) {
      counter = terminalCounter.getAndIncrement();
    } else if (entry.type() instanceof TypeNonTerminal) {
      counter = nonTerminalCounter.getAndIncrement();
    }

    if (counter != null && entry.type() instanceof TypeSymbol typeSymbol) {
      var identifier = typeSymbol.setup(counter, this);
      symbolsList.add(entry);
      super.bindTypeToIdentifier(identifier, entry);
    }

    if (entry.type() instanceof TypeLabel typeLabel) {
      var identifier =
          typeLabel.setup(variable, this); // May be a way to pass the name of the label here
      symbolsList.add(entry);
      super.bindTypeToIdentifier(identifier, entry);
    }

    if (entry.type() instanceof TypeRegex typeRegex) {
      var matcher = TypeRegex.REGEX_PATTERN.matcher(variable);
      if (matcher.find()) {
        var regexMatcher = TypeRegex.EXTRACT_REGEX.matcher(variable);
        if (regexMatcher.find()) {
          typeRegex.setRegex(regexMatcher.group(1));
        }
        if (matcher.groupCount() > 1) {
          typeRegex.setDescription(matcher.group(2));
        }
        if (matcher.groupCount() == 3) {
          var label = matcher.group(3);
          typeRegex.setLabel(label);
          super.bindTypeToIdentifier(label, entry);
        }
      }
    }
  }

  @DocumentSymbol
  public SymbolKind documentSymbol(SymbolTableEntry entry) {
    return switch (entry.entryKind()) {
      case DEFINE -> SymbolKind.Field;
      default -> null;
    };
  }

  @SemanticToken(SemanticTokenTypes.Variable)
  public String semanticToken(SymbolTableEntry entry) {
    return SemanticTokenTypes.Variable;
  }

  public List<SymbolTableEntry> getSymbolsList() {
    return symbolsList;
  }

  public String getMarkup(@Nullable EntryType entryType) {
    var list = getSymbolsList().stream()
        .map(SymbolTableEntry::entryType)
        .map(e -> markString(e, entryType))
        .toList();
    return "%s <-- %s"
        .formatted(list.get(0), list.stream().skip(1).collect(Collectors.joining(" ")));
  }

  // TODO: refactoring in better way
  private String markString(EntryType entry, @Nullable EntryType other) {
    if (other != null && entry.refType().equals(other.refType())) {
      return "*%s*".formatted(entry.tokenString());
    } else {
      return entry.tokenString();
    }
  }
}

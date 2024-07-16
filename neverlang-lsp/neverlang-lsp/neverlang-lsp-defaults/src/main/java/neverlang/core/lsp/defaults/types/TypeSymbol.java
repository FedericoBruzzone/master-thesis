package neverlang.core.lsp.defaults.types;

import neverlang.core.lsp.utils.Conversions;
import neverlang.core.typesystem.SymbolTableEntry;
import neverlang.core.typesystem.Type;
import neverlang.core.typesystem.typenv.EntryType;
import org.eclipse.lsp4j.InlayHintKind;
import org.eclipse.lsp4j.MarkupContent;
import org.eclipse.lsp4j.MarkupKind;

public abstract class TypeSymbol<T> implements Type {

  String identifier;
  TypeProduction production;

  public String setup(T position, TypeProduction production) {
    this.identifier = createIdentifier(position);
    this.production = production;
    return identifier;
  }

  public abstract String createIdentifier(T position);

  public String getIdentifier() {
    return identifier;
  }

  public org.eclipse.lsp4j.InlayHint inlayHint(SymbolTableEntry entry) {
    return switch (entry.entryKind()) {
      case DEFINE -> {
        var inlay = new org.eclipse.lsp4j.InlayHint();
        inlay.setLabel(getIdentifier());
        inlay.setPosition(Conversions.toPositionStart(entry.selectionRange()));
        inlay.setKind(InlayHintKind.Parameter);
        inlay.setPaddingRight(true);
        yield inlay;
      }
      default -> null;
    };
  }

  public MarkupContent getMarkup(EntryType entryType) {
    return new MarkupContent(MarkupKind.MARKDOWN, production.getMarkup(entryType));
  }
}

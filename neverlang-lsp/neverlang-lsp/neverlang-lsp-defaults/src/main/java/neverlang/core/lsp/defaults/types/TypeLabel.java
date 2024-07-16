package neverlang.core.lsp.defaults.types;

import neverlang.core.lsp.defaults.signatures.LabelSignature;
import neverlang.core.typelang.annotations.*;
import neverlang.core.typesystem.Signature;
import neverlang.core.typesystem.SymbolTableEntry;
import org.eclipse.lsp4j.InlayHint;
import org.eclipse.lsp4j.SemanticTokenTypes;

@TypeAnnotation(TypeEnum.LABEL)
@TypeLangAnnotation(keyword = "label", kind = TypeSystemKind.TYPE)
public class TypeLabel extends TypeSymbol<String> {

  String name = "Then";

  public String name() {
    return name;
  }

  @Override
  public String id() {
    return "label";
  }

  @Override
  public boolean matchSignature(Signature signature) {
    return signature instanceof LabelSignature;
  }

  public String setup(String identifier, TypeProduction production) {
    this.identifier = createIdentifier(identifier);
    this.production = production;
    return identifier;
  }

  @Override
  public String createIdentifier(String identifier) {
    return identifier;
  }

  @Override
  @neverlang.core.typelang.annotations.InlayHint
  public InlayHint inlayHint(SymbolTableEntry entry) {
    return switch (entry.entryKind()) {
      case DEFINE -> super.inlayHint(entry);
      default -> null;
    };
  }

  @SemanticToken(SemanticTokenTypes.Type)
  public String semanticToken(SymbolTableEntry entry) {
    return SemanticTokenTypes.Type;
  }

  @Hover
  public org.eclipse.lsp4j.Hover hover(SymbolTableEntry entry) {
    return switch (entry.entryKind()) {
      case USE -> {
        var hover = new org.eclipse.lsp4j.Hover();
        hover.setContents(getMarkup(entry.entryType()));
        yield hover;
      }
      default -> null;
    };
  }
}

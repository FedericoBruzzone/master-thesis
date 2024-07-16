package neverlang.core.lsp.defaults.types;

import neverlang.core.lsp.defaults.signatures.NonTerminalSignature;
import neverlang.core.typelang.annotations.*;
import neverlang.core.typesystem.Signature;
import neverlang.core.typesystem.SymbolTableEntry;
import org.eclipse.lsp4j.InlayHint;
import org.eclipse.lsp4j.SemanticTokenTypes;

@TypeAnnotation(TypeEnum.NONTERMINAL)
@TypeLangAnnotation(keyword = "nonterminal", kind = TypeSystemKind.TYPE)
public class TypeNonTerminal extends TypeSymbol<Integer> {
  @Override
  public String id() {
    return "nonterminal";
  }

  @Override
  public boolean matchSignature(Signature signature) {
    return signature instanceof NonTerminalSignature;
  }

  @Override
  public String createIdentifier(Integer position) {
    return "$%d".formatted(position);
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

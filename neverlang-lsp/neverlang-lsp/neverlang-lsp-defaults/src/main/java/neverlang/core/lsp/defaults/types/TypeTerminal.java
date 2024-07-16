package neverlang.core.lsp.defaults.types;

import neverlang.core.typelang.annotations.*;
import neverlang.core.typesystem.SymbolTableEntry;
import org.eclipse.lsp4j.InlayHint;
import org.eclipse.lsp4j.SemanticTokenTypes;

@TypeAnnotation(TypeEnum.TERMINAL)
@TypeLangAnnotation(keyword = "terminal", kind = TypeSystemKind.TYPE)
public class TypeTerminal extends TypeSymbol<Integer> {
  @Override
  public String id() {
    return "terminal";
  }

  @Override
  public String createIdentifier(Integer position) {
    return "#%d".formatted(position);
  }

  @Override
  @neverlang.core.typelang.annotations.InlayHint
  public InlayHint inlayHint(SymbolTableEntry entry) {
    return super.inlayHint(entry);
  }

  @SemanticToken(SemanticTokenTypes.String)
  public String semanticToken(SymbolTableEntry entry) {
    return SemanticTokenTypes.String;
  }
}

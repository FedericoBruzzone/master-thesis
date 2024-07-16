package neverlang.core.lsp.defaults.types;

import neverlang.core.typelang.annotations.*;
import neverlang.core.typesystem.SymbolTableEntry;
import neverlang.core.typesystem.defaults.DefaultTypeScope;
import neverlang.core.typesystem.defaults.SingleTypeTypeBinder;
import neverlang.core.typesystem.typenv.EntryTypeBinder;
import org.eclipse.lsp4j.SemanticTokenTypes;
import org.eclipse.lsp4j.SymbolKind;

@TypeAnnotation(TypeEnum.ROLE)
@TypeLangAnnotation(keyword = "role", kind = TypeSystemKind.TYPE)
public class TypeRole extends DefaultTypeScope {
  @Override
  public String id() {
    return "role";
  }

  @Override
  public Class<? extends EntryTypeBinder> getTypeBinder() {
    return SingleTypeTypeBinder.class;
  }

  @DocumentSymbol
  public SymbolKind documentSymbol(SymbolTableEntry entry) {
    return switch (entry.entryKind()) {
      case DEFINE -> SymbolKind.Method;
      default -> null;
    };
  }

  @SemanticToken(SemanticTokenTypes.Method)
  public String semanticToken(SymbolTableEntry entry) {
    return SemanticTokenTypes.Method;
  }
}

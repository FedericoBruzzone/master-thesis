package neverlang.core.lsp.defaults.types;

import neverlang.core.typelang.annotations.*;
import neverlang.core.typesystem.SymbolTableEntry;
import neverlang.core.typesystem.defaults.DefaultTypeScope;
import neverlang.core.typesystem.defaults.SingleTypeTypeBinder;
import neverlang.core.typesystem.symboltable.EntryKind;
import neverlang.core.typesystem.typenv.EntryTypeBinder;
import org.eclipse.lsp4j.SemanticTokenTypes;
import org.eclipse.lsp4j.SymbolKind;

@TypeAnnotation(TypeEnum.SLICE)
@TypeLangAnnotation(keyword = "slice", kind = TypeSystemKind.TYPE)
public class TypeSlice extends DefaultTypeScope {
  @Override
  public String id() {
    return "slice";
  }

  @Override
  public Class<? extends EntryTypeBinder> getTypeBinder() {
    return SingleTypeTypeBinder.class;
  }

  @DocumentSymbol
  public SymbolKind documentSymbol(SymbolTableEntry entry) {
    return entry.entryKind().equals(EntryKind.DEFINE) ? SymbolKind.Module : null;
  }

  @SemanticToken(SemanticTokenTypes.Class)
  public String semanticToken(SymbolTableEntry entry) {
    return SemanticTokenTypes.Class;
  }
}
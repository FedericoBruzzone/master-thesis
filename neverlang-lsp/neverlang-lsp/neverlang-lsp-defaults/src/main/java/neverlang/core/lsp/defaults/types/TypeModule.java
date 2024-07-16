package neverlang.core.lsp.defaults.types;

import java.util.Optional;
import neverlang.core.lsp.defaults.signatures.ModuleSignature;
import neverlang.core.typelang.annotations.*;
import neverlang.core.typesystem.Signature;
import neverlang.core.typesystem.SymbolTableEntry;
import neverlang.core.typesystem.defaults.DefaultTypeScope;
import neverlang.core.typesystem.defaults.SingleTypeTypeBinder;
import neverlang.core.typesystem.symboltable.EntryKind;
import neverlang.core.typesystem.typenv.EntryTypeBinder;
import org.eclipse.lsp4j.SemanticTokenTypes;
import org.eclipse.lsp4j.SymbolKind;

@TypeAnnotation(TypeEnum.MODULE)
@TypeLangAnnotation(keyword = "module", kind = TypeSystemKind.TYPE)
public class TypeModule extends DefaultTypeScope {
  @Override
  public String id() {
    return "module";
  }

  @Override
  public boolean matchSignature(Signature signature) {
    return signature instanceof ModuleSignature;
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

  @Hover
  public org.eclipse.lsp4j.Hover hover(SymbolTableEntry entry) {
    return switch (entry.entryKind()) {
      case USE -> {
        var module = entry.<TypeModule>type();
        yield module
            .getSyntax()
            .map(e -> {
              var syntax = module.getSyntax().get().<TypeSyntax>type();
              var hover = new org.eclipse.lsp4j.Hover();
              hover.setContents(syntax.getMarkup());
              return hover;
            })
            .orElse(null);
      }
      default -> null;
    };
  }

  private Optional<SymbolTableEntry> getSyntax() {
    return streamEntries("SYNTAX").findFirst();
  }
}

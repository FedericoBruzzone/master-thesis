package neverlang.core.lsp.defaults.types;

import neverlang.core.lsp.defaults.signatures.BundleSignature;
import neverlang.core.lsp.defaults.signatures.ModuleSignature;
import neverlang.core.typelang.annotations.*;
import neverlang.core.typesystem.Signature;
import neverlang.core.typesystem.SymbolTableEntry;
import neverlang.core.typesystem.UnbindableEntryException;
import neverlang.core.typesystem.defaults.DefaultTypeScope;
import neverlang.core.typesystem.defaults.SingleTypeTypeBinder;
import neverlang.core.typesystem.symboltable.EntryKind;
import neverlang.core.typesystem.typenv.EntryTypeBinder;
import org.eclipse.lsp4j.SemanticTokenTypes;
import org.eclipse.lsp4j.SymbolKind;

@TypeAnnotation(TypeEnum.BUNDLE)
@TypeLangAnnotation(keyword = "bundle", kind = TypeSystemKind.TYPE)
public class TypeBundle extends DefaultTypeScope {
  @Override
  public String id() {
    return "bundle";
  }

  @Override
  public Class<? extends EntryTypeBinder> getTypeBinder() {
    return SingleTypeTypeBinder.class;
  }

  @Override
  public boolean matchSignature(Signature signature) {
    return signature instanceof BundleSignature;
  }

  @DocumentSymbol
  public SymbolKind documentSymbol(SymbolTableEntry entry) {
    return entry.entryKind().equals(EntryKind.DEFINE) ? SymbolKind.Module : null;
  }

  @SemanticToken(SemanticTokenTypes.Namespace)
  public String semanticToken(SymbolTableEntry entry) {
    return SemanticTokenTypes.Namespace;
  }

  @Override
  public void importInScope(String id, SymbolTableEntry entry) {
    if (entry.type().matchSignature(new BundleSignature())
        || entry.type().matchSignature(new ModuleSignature())) {
      bindTypeToIdentifier(id, entry);
    } else {
      throw new UnbindableEntryException("Cannot import " + entry.type() + " in bundle", entry);
    }
  }
}

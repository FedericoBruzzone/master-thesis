package neverlang.core.lsp.defaults.signatures;

import neverlang.core.lsp.defaults.types.TypeUnresolved;
import neverlang.core.typelang.annotations.SignatureAnnotation;
import neverlang.core.typelang.annotations.SignatureEnum;
import neverlang.core.typelang.annotations.TypeLangAnnotation;
import neverlang.core.typelang.annotations.TypeSystemKind;
import neverlang.core.typesystem.Signature;
import neverlang.core.typesystem.SymbolTableEntry;

@SignatureAnnotation(SignatureEnum.IDENTIFIER)
@TypeLangAnnotation(keyword = "identifier", kind = TypeSystemKind.SIGNATURE)
public class IdentifierSignature implements Signature {

  private final SymbolTableEntry[] symbolTableEntry;

  public IdentifierSignature() {
    symbolTableEntry = new SymbolTableEntry[0];
  }

  public IdentifierSignature(SymbolTableEntry[] symbolTableEntry) {
    this.symbolTableEntry = symbolTableEntry;
  }

  @Override
  public SymbolTableEntry typeResolution(SymbolTableEntry entryType) {
    if (symbolTableEntry.length == 1 && entryType.type() instanceof TypeUnresolved) {
      entryType.refType().set(symbolTableEntry[0].type());
    }
    return entryType;
  }
}

package neverlang.core.lsp.defaults.signatures;

import neverlang.core.lsp.defaults.types.TypeNonTerminal;
import neverlang.core.lsp.defaults.types.TypeProduction;
import neverlang.core.typesystem.Signature;
import neverlang.core.typesystem.SymbolTableEntry;

public record ProductionSignature(int offset, String name) implements Signature {

  public ProductionSignature(int offset) {
    this(offset, "");
  }

  public ProductionSignature() {
    this(0, "");
  }

  @Override
  public SymbolTableEntry typeResolution(SymbolTableEntry entryType) {
    if (entryType.type() instanceof TypeProduction typeProduction) {
      return typeProduction.getSymbolsList().stream()
          .filter(e -> e.type() instanceof TypeNonTerminal)
          .toList()
          .get(offset());
    } else {
      return entryType;
    }
  }
}

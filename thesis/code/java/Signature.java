public interface Signature {
  default SymbolTableEntry typeResolution(SymbolTableEntry entryType) {
    return entryType;
  }
}


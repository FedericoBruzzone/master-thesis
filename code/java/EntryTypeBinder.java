public interface EntryTypeBinder {

  EntryTypeBinder bindEntry(SymbolTableEntry type);

  boolean isBound();

  Stream<SymbolTableEntry> stream();

  Optional<SymbolTableEntry> getBoundEntry();

  void removeIf(Predicate<SymbolTableEntry> predicate);

}

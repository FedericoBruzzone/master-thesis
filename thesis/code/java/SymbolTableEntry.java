public interface SymbolTableEntry extends Indexable {

  EntryKind entryKind();

  EntryType entryType();

  EntryDetails details();

  default <T extends Type> T type() {
    return (T) refType().get();
  }

  default AtomicReference<Type> refType() {
    return entryType().refType();
  }

  default Location location() {
    return entryType().token().location();
  }

  default boolean isAssignableFrom(SymbolTableEntry symbolTableEntry,
                                   Variance variance) {
    return type().isAssignableFrom(symbolTableEntry.type(), variance);
  }

}

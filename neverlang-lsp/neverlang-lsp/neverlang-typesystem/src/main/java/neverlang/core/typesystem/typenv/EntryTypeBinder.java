package neverlang.core.typesystem.typenv;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;
import neverlang.core.typesystem.SymbolTableEntry;

public interface EntryTypeBinder {
  EntryTypeBinder bindEntry(SymbolTableEntry type);

  //    TypeBinder unboundType(TypeLocator type);
  boolean isBound();

  Stream<SymbolTableEntry> stream();

  Optional<SymbolTableEntry> getBoundEntry();

  void removeIf(Predicate<SymbolTableEntry> predicate);
}
